package com.myorg;

import java.util.Collections;

import software.amazon.awscdk.CfnOutput;
import software.amazon.awscdk.CfnParameter;
import software.amazon.awscdk.SecretValue;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.StackProps;
import software.amazon.awscdk.services.ec2.ISecurityGroup;
import software.amazon.awscdk.services.ec2.InstanceClass;
import software.amazon.awscdk.services.ec2.InstanceSize;
import software.amazon.awscdk.services.ec2.InstanceType;
import software.amazon.awscdk.services.ec2.Peer;
import software.amazon.awscdk.services.ec2.Port;
import software.amazon.awscdk.services.ec2.SecurityGroup;
import software.amazon.awscdk.services.ec2.SubnetSelection;
import software.amazon.awscdk.services.ec2.Vpc;
import software.amazon.awscdk.services.ecs.Cluster;
import software.amazon.awscdk.services.rds.Credentials;
import software.amazon.awscdk.services.rds.CredentialsFromUsernameOptions;
import software.amazon.awscdk.services.rds.DatabaseInstance;
import software.amazon.awscdk.services.rds.DatabaseInstanceEngine;
import software.amazon.awscdk.services.rds.MySqlInstanceEngineProps;
import software.amazon.awscdk.services.rds.MysqlEngineVersion;
// import software.amazon.awscdk.Duration;
// import software.amazon.awscdk.services.sqs.Queue;
import software.constructs.Construct;

public class RdsStack extends Stack {

	private Cluster cluster;

	public RdsStack(final Construct scope, final String id, Vpc vpc) {
		this(scope, id, null, vpc);
	}

	public RdsStack(final Construct scope, final String id, final StackProps props, Vpc vpc) {
		super(scope, id, props);

		
		/*
		 * cdk deploy --parameters Rds:databasePassword=root Rds VirtualPrivateCloud Cluster Service01
		 */
		
		// @formatter:off
 		CfnParameter databasePassword = CfnParameter
				.Builder
				.create(this,"databasePassword")
				.type("String")
				.description("The RDS instance password")
				.build();
	

 		
 		ISecurityGroup securityGroup = SecurityGroup 				
 				.fromSecurityGroupId(this, id, vpc.getVpcDefaultSecurityGroup());
 		
 		//Qualquer IP de dentro da VPC poderá ter acesso ao Banco de Dados
 		securityGroup.addIngressRule(Peer.anyIpv4(), Port.tcp(3306));
 		
 		
		DatabaseInstance bdInstance = DatabaseInstance.Builder
 				.create(this,"RDS-01")
 				.instanceIdentifier("aws-project01-db")
 				.engine(DatabaseInstanceEngine.mysql(MySqlInstanceEngineProps.builder().version(MysqlEngineVersion.VER_5_7)
 						.build()))
 				.vpc(vpc)
 				.credentials(Credentials.fromUsername("admin", 
 						CredentialsFromUsernameOptions.builder()
 						.password( 
 								SecretValue.unsafePlainText(databasePassword.getValueAsString()))
 						.build()
 						))
 				.instanceType(InstanceType.of(InstanceClass.BURSTABLE2,InstanceSize.MICRO))
 				.multiAz(false)
 				.allocatedStorage(10)
 				.securityGroups(Collections.singletonList(securityGroup))
// 				.vpcSubnets(SubnetSelection.builder()
// 						.subnets(vpc.getPrivateSubnets())
// 						.build())
 				
 				
 				.vpcSubnets(SubnetSelection.builder()
 					      .subnets(vpc.getPublicSubnets())
 					      .build())
 				
 				.build();
 	 
 		CfnOutput.Builder.create(this, "rds-endpoint")
 		.exportName("rds-endpoint")
 		.value(bdInstance.getDbInstanceEndpointAddress())
 		.build();
 		
 	 	 
 		CfnOutput.Builder.create(this, "rds-password")
 		.exportName("rds-password")
 		.value(databasePassword.getValueAsString())
 		.build();
 		
 		// @formatter:on
	
	}

	public Cluster getCluster() {
		return cluster;
	}
}
