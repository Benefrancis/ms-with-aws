package br.com.benefrancis.aws_project01_start.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TesteController {
    private static final Logger LOG = LoggerFactory.getLogger(TesteController.class);

    @GetMapping("/dog/{name}")
    public ResponseEntity<?> dogTeste(@PathVariable String name) {
        LOG.info("Teste Controller - name: {}", name);
        return ResponseEntity.ok("Name: " + name);
    }
}
