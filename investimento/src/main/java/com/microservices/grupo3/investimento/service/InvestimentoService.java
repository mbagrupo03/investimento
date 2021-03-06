package com.microservices.grupo3.investimento.service;

import org.springframework.stereotype.Service;

import com.microservices.grupo3.investimento.client.request.CriarCreditoRequest;
import com.microservices.grupo3.investimento.client.request.CriarDebitoRequest;
import com.microservices.grupo3.investimento.exceptions.ContaIdExistenteBadRequestException;
import com.microservices.grupo3.investimento.exceptions.ContaIdNotFoundException;
import com.microservices.grupo3.investimento.model.Investimento;
import com.microservices.grupo3.investimento.repository.InvestimentoRepository;

@Service
public class InvestimentoService {

    private InvestimentoRepository repository;
    private CreditoClient creditoClient;
    private DebitoClient debitoClient;

    public InvestimentoService(InvestimentoRepository repository,
                               CreditoClient creditoClient,
                               DebitoClient debitoClient) {
        this.repository = repository;
        this.creditoClient = creditoClient;
        this.debitoClient = debitoClient;
    }



    public Investimento criarInvestimento(Investimento investimento) {

        var contaExistente = repository.findByContaId(investimento.getContaId()).isPresent();

        if(contaExistente) {
            throw new ContaIdExistenteBadRequestException("ContaId " + investimento.getContaId() + " já existe");
        }

        var credito = creditoClient.criarCredito(
                new CriarCreditoRequest(investimento.getContaId(), 0.0, investimento.getClienteId(), "investimento"));

        var debito = debitoClient.criarDebito(
                new CriarDebitoRequest(investimento.getContaId(), 0.0, investimento.getClienteId(), "investimento"));

        return repository.save(investimento);
    }

    public Investimento buscarConta(Integer contaId) {
        atualizarSaldoConta(contaId);

        if (repository.findByContaId(contaId).isPresent()) {
            return repository.findByContaId(contaId).get();
        }
        return null;
    }

    public Investimento atualizarSaldoConta(Integer contaId) {
        var investimento = repository.findByContaId(contaId)
                .orElseThrow(() -> new ContaIdNotFoundException("Não encontrada conta id: " + contaId));

        var debito = debitoClient.getSaldoDebitoConta(contaId);
        var credito = creditoClient.getSaldoCreditoConta(contaId);

        double saldoTotalConta = credito.getValorCredito() - debito.getValorDebito();

        investimento.setSaldo(saldoTotalConta);

        return repository.save(investimento);
    }

    public Investimento aplicarInvestimento(Integer contaIdInvestimento, Integer contaIdContaCorrente, double valorAplicar) {

        var investimento = repository.findByContaId(contaIdInvestimento).get();

        var credito = creditoClient.criarCredito(
                new CriarCreditoRequest(contaIdInvestimento, valorAplicar, investimento.getClienteId(), "investimento"));

        var debito = debitoClient.criarDebito(
                new CriarDebitoRequest(contaIdContaCorrente, valorAplicar, investimento.getClienteId(), "contacorrente"));

        var contaInvestimentoAtualizada = buscarConta(contaIdInvestimento);

        return contaInvestimentoAtualizada;
    }

    public Investimento resgatarInvestimento(Integer contaIdInvestimento, Integer contaIdContaCorrente, double valorResgatar) {

        var investimento = repository.findByContaId(contaIdInvestimento).get();

        var debito = debitoClient.criarDebito(
                new CriarDebitoRequest(contaIdInvestimento, valorResgatar, investimento.getClienteId(), "investimento"));

        var credito = creditoClient.criarCredito(
                new CriarCreditoRequest(contaIdContaCorrente, valorResgatar, investimento.getClienteId(), "contacorrente"));

        var contaInvestimentoAtualizada = buscarConta(contaIdInvestimento);

        return contaInvestimentoAtualizada;
    }
}
