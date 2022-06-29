package br.com.benefrancis.spring.boot.docker.demo.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.benefrancis.spring.boot.docker.demo.entity.Product;
import br.com.benefrancis.spring.boot.docker.demo.enums.EventType;
import br.com.benefrancis.spring.boot.docker.demo.repository.ProductRepository;
import br.com.benefrancis.spring.boot.docker.demo.service.ProductPublisher;

@RestController
@RequestMapping(value = "/api/product")
public class ProducController {

	@Autowired
	private ProductRepository repo;

	@Autowired
	private ProductPublisher publisher;

	@GetMapping
	public Iterable<Product> findAll() {
		return repo.findAll();
	}

	@GetMapping(value = "/{id}")
	public ResponseEntity<Product> findById(@PathVariable Long id) {
		Optional<Product> opt = repo.findById(id);
		if (opt.isPresent()) {
			return ResponseEntity.ok(opt.get());
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	@PostMapping
	public ResponseEntity<Product> save(@RequestBody Product p) {
		Product created = repo.save(p);
		publisher.publishProductEvent(created, EventType.PRODUCT_CREATED, "Benefrancis");
		return ResponseEntity.ok(created);
	}

	@PutMapping(value = "/{id}")
	public ResponseEntity<Product> update(@RequestBody Product p, @PathVariable Long id) {
		if (repo.existsById(id)) {
			p.setId(id);
			Product salvo = repo.save(p);

			publisher.publishProductEvent(salvo, EventType.PRODUCT_UPDATE, "Benefrancis");

			return ResponseEntity.ok(salvo);
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Product> delete(@PathVariable Long id) {
		Optional<Product> opt = repo.findById(id);
		if (opt.isPresent()) {
			Product deletado = opt.get();
			repo.delete(deletado);

			publisher.publishProductEvent(deletado, EventType.PRODUCT_DELETED, "Benefrancis");

			return ResponseEntity.ok(deletado);
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	@GetMapping(value = "/bycode")
	public ResponseEntity<Product> findByCode(@RequestParam String code) {
		Optional<Product> opt = repo.findByCode(code);
		if (opt.isPresent()) {
			return ResponseEntity.ok(opt.get());
		} else {
			return ResponseEntity.notFound().build();
		}
	}

}
