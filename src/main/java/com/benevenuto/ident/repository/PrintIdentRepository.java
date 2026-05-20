package com.benevenuto.ident.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.benevenuto.ident.entity.PrintIdent;

import java.util.List;
import java.util.UUID;

@Repository
public interface PrintIdentRepository extends JpaRepository<PrintIdent, UUID> {

    /**
     * Busca por número do operador com a regra:
     * 1. Urgentes primeiro (isUrgent DESC -> true vem antes de false)
     * 2. Criados recentemente primeiro (createdAt DESC)
     */
    List<PrintIdent> findByOperatorNumberOrderByIsUrgentDescCreatedAtDesc(String operatorNumber);

    /**
     * Caso você precise de uma listagem geral seguindo a mesma regra de ordenação
     * (O Service que te passei usa o Sort, mas este método é uma alternativa)
     */
    List<PrintIdent> findAllByOrderByIsUrgentDescCreatedAtDesc();
}