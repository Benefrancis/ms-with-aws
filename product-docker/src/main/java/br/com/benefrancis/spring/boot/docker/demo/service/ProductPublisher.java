package br.com.benefrancis.spring.boot.docker.demo.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.model.Topic;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.benefrancis.spring.boot.docker.demo.entity.Product;
import br.com.benefrancis.spring.boot.docker.demo.enums.EventType;
import br.com.benefrancis.spring.boot.docker.demo.envelope.Envelope;
import br.com.benefrancis.spring.boot.docker.demo.event.ProductEvent;

@Service
public class ProductPublisher {

	private static final Logger log = LoggerFactory.getLogger(ProductPublisher.class);

	@Autowired
	AmazonSNS snsClient;

	@Autowired
	@Qualifier("productEventsTopic")
	private Topic productEventTopic;

	@Autowired
	ObjectMapper objectMapper;

	public void publishProductEvent(Product product, EventType evento, String username) {
		ProductEvent productEvent = new ProductEvent();
		productEvent.setCode(product.getCode());
		productEvent.setProductId(product.getId());
		productEvent.setUsername(username);

		Envelope envelope = new Envelope();

		try {
			
			envelope.setData(objectMapper.writeValueAsString(productEvent));
			envelope.setEventType(evento);

			snsClient.publish(productEventTopic.getTopicArn(), objectMapper.writeValueAsString(envelope));

		} catch (JsonProcessingException e) {
			log.error("Failed to create product event message");
		}

	}

}
