package br.com.benefrancis.consumidor.service;

import java.io.IOException;

import javax.jms.JMSException;
import javax.jms.TextMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.benefrancis.consumidor.model.Envelope;
import br.com.benefrancis.consumidor.model.ProductEvent;
import br.com.benefrancis.consumidor.model.SnsMessage;

@Service
public class ProductEventConsumer {
	
	private static final Logger log = LoggerFactory.getLogger(ProductEventConsumer.class);
	
	@Autowired
	private ObjectMapper objectMapper;
 

	@JmsListener(destination = "${aws.sqs.queue.product.events.name}")
	public void receiveProductEvent(TextMessage textMessage) throws JMSException, IOException {

		SnsMessage snsMessage = objectMapper.readValue(textMessage.getText(), SnsMessage.class);

		Envelope envelope = objectMapper.readValue(snsMessage.getMessage(), Envelope.class);

		ProductEvent productEvent = objectMapper.readValue(envelope.getData(), ProductEvent.class);

		log.info("Product event received - Event: {} - ProductId: {} - MessageId: {}", envelope.getEventType(),
				productEvent.getProductId(), snsMessage.getMessageId());
	}
}
