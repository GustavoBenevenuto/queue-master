package com.benevenuto.ident.service;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.benevenuto.ident.entity.PrintIdent;
import com.benevenuto.ident.repository.PrintIdentRepository;

@Service
public class PrintIdentService {

	@Autowired
    private PrintIdentRepository repository;

    public PrintIdent create(PrintIdent entity) {
        return repository.save(entity);
    }

    public List<PrintIdent> findAllSorted() {
        // Ordena por Urgência (true primeiro) e depois por Data (mais novo primeiro)
        return repository.findAll(Sort.by(
            Sort.Order.desc("isUrgent"),
            Sort.Order.desc("createdAt")
        ));
    }

    public List<PrintIdent> findByOperator(String operatorNumber) {
        return repository.findByOperatorNumberOrderByIsUrgentDescCreatedAtDesc(operatorNumber);
    }

    public PrintIdent update(UUID id, PrintIdent data) {
        PrintIdent existing = repository.findById(id)
            .orElseThrow(() -> new RuntimeException("Registro não encontrado"));
        
        // Atualiza apenas o que faz sentido mudar
        existing.setContent(data.getContent());
        existing.setQuantity(data.getQuantity());
        existing.setStatus(data.getStatus());
        existing.setIsUrgent(data.getIsUrgent());
        existing.setOperatorNumber(data.getOperatorNumber());
        
        return repository.save(existing);
    }

    public void delete(UUID id) {
        repository.deleteById(id);
    }
}