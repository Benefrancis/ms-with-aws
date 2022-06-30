package com.myorg;

import java.util.HashMap;
import java.util.Map;

import software.amazon.awscdk.Duration;
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
import software.amazon.awscdk.services.events.targets.SnsTopic;
import software.amazon.awscdk.services.logs.LogGroup;
import software.constructs.Construct;

public class Service02Stack extends Stack {
	public Service02Stack(final Construct scope, final String id, Cluster cluster, SnsTopic productEventsTopic) {
		this(scope, id, null, cluster, productEventsTopic);
	}

	public Service02Stack(final Construct scope, final String id, final StackProps props, Cluster cluster,
			SnsTopic productEventsTopic) {
		super(scope, id, props);

		// @formatter:off
 		
 		Map<String,String> envVariables = new HashMap<>();
 
 		
 		envVariables.put("AWS_REGION", "us-east-1");
 		envVariables.put("AWS_SNS_TOPIC_PRODUCT_EVENTS_ARN", productEventsTopic.getTopic().getTopicArn());
 		
 		// The code that defines your stack goes here
		ApplicationLoadBalancedFargateService service02 = 
				ApplicationLoadBalancedFargateService
				.Builder
				.create(this, "ApplicationLoadBalance02")
				.serviceName("service02")
				.cluster(cluster)
				.cpu(512)
				.desiredCount(2) //inst�ncias
				.listenerPort(80) //Porta para acesso externo
				.assignPublicIp(true) 
				.memoryLimitMiB(1024)
				.taskImageOptions (
						ApplicationLoadBalancedTaskImageOptions.builder()
						.containerName("spring-boot-docker")
						.image(ContainerImage.fromRegistry ("benefrancis/consumidor:0.0.1"))//Nome da imagem no hub do docker
						.containerPort(9090)
						.logDriver(LogDriver.awsLogs(AwsLogDriverProps.builder() //Especificando onde os logs ficarão. Redirecionados ao serviço cloud watch
							.logGroup(LogGroup.Builder.create (this,"service02LogGroup") //Grupo do Log
								.logGroupName("service02")
								.removalPolicy(RemovalPolicy.DESTROY)
								.build())
							.streamPrefix("service02")
						.build()))
						.environment(envVariables)
					.build())
				.publicLoadBalancer(true)
			.build();
		
		
		/**
		 * Verificar a saúde da application
		 */
		service02.getTargetGroup().configureHealthCheck(new HealthCheck.Builder()
						.path("/actuator/health")
						.port("9090")
						.healthyHttpCodes("200")
						.build());
		
				
		ScalableTaskCount scalableTaskCount = service02.getService()
								.autoScaleTaskCount(EnableScalingProps.builder()
								.minCapacity(2)
								.maxCapacity(4)
								.build());
				
				
		scalableTaskCount.scaleOnCpuUtilization("service02AutoScalingOnCpuUtilization", CpuUtilizationScalingProps.builder()
						.targetUtilizationPercent(50)
						.scaleInCooldown(Duration.seconds(60))
						.scaleOutCooldown(Duration.seconds(60))
						.build()) ;
		
		
		scalableTaskCount.scaleOnMemoryUtilization(
	              "service02AutoScalingOnMemoryUtilization",MemoryUtilizationScalingProps.builder()
	                    .targetUtilizationPercent(80)
	                    .scaleInCooldown(Duration.seconds(300))
	                    .scaleOutCooldown(Duration.seconds(60))
	                    .build());
		
		
		productEventsTopic.getTopic().grantPublish(service02.getTaskDefinition().getTaskRole());
		
		// @formatter:on

	}

}
