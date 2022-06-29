package br.com.benefrancis.spring.boot.docker.demo.event;

import lombok.Data;

@Data
public class ProductEvent {
	private Long productId;
	private String code;
	private String username;
}
