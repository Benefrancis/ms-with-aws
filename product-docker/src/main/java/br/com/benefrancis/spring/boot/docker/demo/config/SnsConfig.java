package br.com.benefrancis.spring.boot.docker.demo.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClientBuilder;
import com.amazonaws.services.sns.model.Topic;

@Configuration
@Profile("!local")
public class SnsConfig {

	@Value("${aws.region}")
	private String awsRegion;

	@Value("${aws.sns.topic.product.events.arn}")
	private String productEventsTopic;

	@Bean
	public AmazonSNS snsClient() {

		// @formatter:off
 		return AmazonSNSClientBuilder.standard()
				.withRegion(awsRegion)
				.withCredentials(new DefaultAWSCredentialsProviderChain())
				.build();
 		// @formatter:on

	}

	@Bean(name = "productEventsTopic")
	public Topic snsProductEventsTopic() {
		return new Topic().withTopicArn(productEventsTopic);
	}

}
