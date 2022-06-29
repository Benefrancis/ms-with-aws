package com.myorg;

import java.util.HashMap;
import java.util.Map;

import software.amazon.awscdk.Duration;
import software.amazon.awscdk.Fn;
import software.amazon.awscdk.RemovalPolicy;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.StackProps;
import software.amazon.awscdk.services.applicationautoscaling.EnableScalingProps;
import software.amazon.awscdk.services.ecs.AwsLogDriverProps;
import software.amazon.awscdk.services.ecs.Cluster;
import software.amazon.awscdk.services.ecs.ContainerImage;
import software.amazon.awscdk.services.ecs.CpuUtilizationScalingProps;
import software.amazon.awscdk.services.ecs.LogDriver;
import software.amazon.awscdk.services.ecs.MemoryUtilizationScalingProps;
import software.amazon.awscdk.services.ecs.ScalableTaskCount;
import software.amazon.awscdk.services.ecs.patterns.ApplicationLoadBalancedFargateService;
import software.amazon.awscdk.services.ecs.patterns.ApplicationLoadBalancedTaskImageOptions;
import software.amazon.awscdk.services.elasticloadbalancingv2.HealthCheck;
import software.amazon.awscdk.services.logs.LogGroup;
// import software.amazon.awscdk.Duration;
// import software.amazon.awscdk.services.sqs.Queue;
import software.constructs.Construct;

public class Service01Stack extends Stack {
	public Service01Stack(final Construct scope, final String id, Cluster cluster) {
		this(scope, id, null, cluster);
	}

	public Service01Stack(final Construct scope, final String id, final StackProps props, Cluster cluster) {
		super(scope, id, props);

		// @formatter:off
 		
 		Map<String,String> envVariables = new HashMap<>();
 		envVariables.put("SPRING_DATASOURCE_URL", "jdbc:mariadb://"+Fn.importValue("rds-endpoint")+":3306/aws_project01?createDatabaseIfNotExist=true");
 		envVariables.put("SPRING_DATASOURCE_USERNAME","admin");
 		envVariables.put("SPRING_DATASOURCE_PASSWORD", Fn.importValue("rds-password"));
		
 		// The code that defines your stack goes here
		ApplicationLoadBalancedFargateService service01 = 
				ApplicationLoadBalancedFargateService
				.Builder
				.create(this, "ApplicationLoadBalance01")
				.serviceName("service01")
				.cluster(cluster)
				.cpu(512)
				.desiredCount(2) //instâncias
				.listenerPort(80) //Porta para acesso externo
				.assignPublicIp(true) 
				.memoryLimitMiB(1024)
				.taskImageOptions (
						ApplicationLoadBalancedTaskImageOptions.builder()
						.containerName("spring-boot-docker")
						.image(ContainerImage.fromRegistry ("benefrancis/spring-boot-docker:0.0.13"))//Nome da imagem no hub do docker
						.containerPort(8080)
						.logDriver(LogDriver.awsLogs(AwsLogDriverProps.builder() //Especificando onde os logs ficarão. Redirecionados ao serviço cloud watch
							.logGroup(LogGroup.Builder.create (this,"service01LogGroup") //Grupo do Log
								.logGroupName("service01")
								.removalPolicy(RemovalPolicy.DESTROY)
								.build())
							.streamPrefix("service01")
						.build()))
						.environment(envVariables)
					.build())
				.publicLoadBalancer(true)
			.build();
		
		
		/**
		 * Verificar a saúde da application
		 */
		service01.getTargetGroup().configureHealthCheck(new HealthCheck.Builder()
						.path("/actuator/health")
						.port("8080")
						.healthyHttpCodes("200")
						.build());
		
				
		ScalableTaskCount scalableTaskCount = service01.getService()
								.autoScaleTaskCount(EnableScalingProps.builder()
								.minCapacity(2)
								.maxCapacity(4)
								.build());
				
				
		scalableTaskCount.scaleOnCpuUtilization("service01AutoScalingOnCpuUtilization", CpuUtilizationScalingProps.builder()
						.targetUtilizationPercent(50)
						.scaleInCooldown(Duration.seconds(60))
						.scaleOutCooldown(Duration.seconds(60))
						.build()) ;
		
		
		scalableTaskCount.scaleOnMemoryUtilization(
	              "service01AutoScalingOnMemoryUtilization",MemoryUtilizationScalingProps.builder()
	                    .targetUtilizationPercent(80)
	                    .scaleInCooldown(Duration.seconds(300))
	                    .scaleOutCooldown(Duration.seconds(60))
	                    .build());
		
		// @formatter:on

	}
}
