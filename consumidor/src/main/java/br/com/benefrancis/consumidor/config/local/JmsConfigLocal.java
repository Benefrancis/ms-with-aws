package br.com.benefrancis.consumidor.config.local;

 


import com.amazon.sqs.javamessaging.ProviderConfiguration;
import com.amazon.sqs.javamessaging.SQSConnectionFactory;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sqs.AmazonSQSClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.support.destination.DynamicDestinationResolver;

import javax.jms.Session;
/**
 * 
 * @author Francis
 * 
 * Existe uma imagem docker dele, desta forma basta executar o seguinte comando:

    docker run --rm -p 4566:4566 -p 4571:4571 localstack/localstack -e "SERVICES=sns, sqs, dynamodb, s3"
 *
 */
@Configuration
@EnableJms
@Profile("local")
public class JmsConfigLocal {

    private SQSConnectionFactory sqsConnectionFactory;

    @Bean
    public DefaultJmsListenerContainerFactory jmsListenerContainerFactory() {
        sqsConnectionFactory = new SQSConnectionFactory(
                new ProviderConfiguration(),
                AmazonSQSClient.builder()
                        .withEndpointConfiguration(
                                new AwsClientBuilder.EndpointConfiguration("http://localhost:4566",
                                        Regions.US_EAST_1.getName()))
                        .withCredentials(new DefaultAWSCredentialsProviderChain())
                        .build());

        DefaultJmsListenerContainerFactory factory =
                new DefaultJmsListenerContainerFactory();
        factory.setConnectionFactory(sqsConnectionFactory);
        factory.setDestinationResolver(new DynamicDestinationResolver());
        factory.setConcurrency("2");
        factory.setSessionAcknowledgeMode(Session.CLIENT_ACKNOWLEDGE);

        return factory;
    }
}
