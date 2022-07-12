package br.com.benefrancis.consumidor.model;

import lombok.Data;

@Data
public class ProductEvent {
	private long productId;
	private String code;
	private String username;

}
