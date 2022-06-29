package br.com.benefrancis.spring.boot.docker.demo.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import br.com.benefrancis.spring.boot.docker.demo.entity.Product;

@Repository
public interface ProductRepository extends CrudRepository<Product, Long> {

	Optional<Product> findByCode(String cod);

}
