package com.benevenuto.ident.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.benevenuto.ident.entity.PrintIdent;
import com.benevenuto.ident.service.PrintIdentService;

@RestController
@RequestMapping("/reprint-ident")
public class PrintIdentController {

	@Autowired
    private PrintIdentService service;

    @PostMapping
    public ResponseEntity<PrintIdent> create(@RequestBody PrintIdent printIdent) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(printIdent));
    }

    @GetMapping
    public List<PrintIdent> listAll() {
        return service.findAllSorted();
    }

    @GetMapping("/operator/{number}")
    public List<PrintIdent> listByOperator(@PathVariable String number) {
        return service.findByOperator(number);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PrintIdent> update(@PathVariable UUID id, @RequestBody PrintIdent data) {
        return ResponseEntity.ok(service.update(id, data));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}