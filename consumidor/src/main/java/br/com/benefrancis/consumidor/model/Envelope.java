package br.com.benefrancis.consumidor.model;

import br.com.benefrancis.consumidor.enums.EventType;
import lombok.Data;

@Data
public class Envelope {
	private EventType eventType;
	private String data;

}
