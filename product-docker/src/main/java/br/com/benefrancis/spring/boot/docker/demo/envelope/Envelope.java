package br.com.benefrancis.spring.boot.docker.demo.envelope;

import br.com.benefrancis.spring.boot.docker.demo.enums.EventType;
import lombok.Data;

@Data
public class Envelope {
	private EventType eventType;
	private String data;
}
