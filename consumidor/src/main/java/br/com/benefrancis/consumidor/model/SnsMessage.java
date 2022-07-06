package br.com.benefrancis.consumidor.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SnsMessage {

	@JsonProperty("Message")
	private String message;

	@JsonProperty("Type")
	private String type;

	@JsonProperty("TopicArn")
	private String tipicArn;

	@JsonProperty("Timestamp")
	private String timestamp;

	@JsonProperty("MessageId")
	private String messageId;

}