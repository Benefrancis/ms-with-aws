package br.com.benefrancis.spring.boot.docker.demo.entity;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.ToString;


@Builder
@Entity
@Data
//@formatter:off
@Table(name = "tb_product",
		uniqueConstraints = @UniqueConstraint(
				name = "UK_CODE", 
				columnNames = { "CODE"}
				))
// @formatter:on
@RequiredArgsConstructor
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Product implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NonNull
	@Column(length = 32, nullable = false)
	private String name;

	@NonNull
	@Column(length = 24, nullable = false)
	private String model;
	
	@NonNull
	@Column(length = 8, nullable = false)
	private String code;
	
	@Column(length = 12,  nullable = true)
	private String cor;

	@NonNull
	private Float price;

}
