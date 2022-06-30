package br.com.benefrancis.spring.boot.docker.demo.config.local;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClient;
import com.amazonaws.services.sns.model.CreateTopicRequest;
import com.amazonaws.services.sns.model.Topic;

@Configuration
@Profile("local")
public class SnsCreate {

	private static final Logger log = LoggerFactory.getLogger(SnsCreate.class);

	private final String productEventsTopic;
	private final AmazonSNS snsClient;

	public SnsCreate() {
		// @formatter:off
		this.snsClient = AmazonSNSClient.builder()
				.withEndpointConfiguration(
				new AwsClientBuilder
				.EndpointConfiguration("http://localhost:4566", Regions.US_EAST_1.getName()))
				.withCredentials(new DefaultAWSCredentialsProviderChain())
				.build();
		CreateTopicRequest reateTopicRequest = new CreateTopicRequest("product-events");
		this.productEventsTopic = this.snsClient.createTopic(reateTopicRequest).getTopicArn();
		// @formatter:on
		log.info("SNS TOPIC ARN: {}", this.productEventsTopic);
	}

	@Bean
	public AmazonSNS snsClient() {
		return this.snsClient;
	}

	@Bean(name = "productEventsTopic")
	public Topic snsProductEventsTopic() {
		return new Topic().withTopicArn(productEventsTopic);
	}

}
