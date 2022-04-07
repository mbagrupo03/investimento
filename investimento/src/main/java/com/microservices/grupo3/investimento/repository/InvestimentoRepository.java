package com.microservices.grupo3.investimento.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservices.grupo3.investimento.model.Investimento;

public interface InvestimentoRepository extends JpaRepository<Investimento, Integer> {

    Optional<Investimento> findByContaId(Integer contaId);
}
