package com.microservices.grupo3.investimento.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.microservices.grupo3.investimento.controller.request.OperacaoInvestimentoRequest;
import com.microservices.grupo3.investimento.model.Investimento;
import com.microservices.grupo3.investimento.service.InvestimentoService;

import io.swagger.v3.oas.annotations.tags.Tag;


@RestController
@RequestMapping("/conta/investimento")
public class InvestimentoController {

    @Autowired
    private final InvestimentoService investimentoService;

    public InvestimentoController(InvestimentoService investimentoService) {
        this.investimentoService = investimentoService;
    }
    

    @GetMapping(path = "/status_aplicacao")
    public String checarStatusAplicacao(){
        return "Aplicação Investimento online";
    }
    
    

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public Investimento criarContaInvestimento(@RequestBody Investimento investimentoRequest){
        return investimentoService.criarInvestimento(investimentoRequest);
    }

    @GetMapping("/consulta/{contaId}")
    public Investimento consultarContaId(@PathVariable Integer contaId) {
        return investimentoService.buscarConta(contaId);
    }

    @PatchMapping("/aplicar/{contaIdInvestimento}")
    @ResponseStatus(HttpStatus.OK)
    public Investimento aplicarInvestimento(@RequestBody OperacaoInvestimentoRequest aplicacaoRequest,
                                            @PathVariable Integer contaIdInvestimento){
        return investimentoService.aplicarInvestimento(contaIdInvestimento,
                aplicacaoRequest.getContaIdContaCorrente(),
                aplicacaoRequest.getValor());
    }

    @PatchMapping("/resgatar/{contaIdInvestimento}")
    @ResponseStatus(HttpStatus.OK)
    public Investimento resgatarInvestimento(@RequestBody OperacaoInvestimentoRequest resgateRequest,
                                             @PathVariable Integer contaIdInvestimento){
        return investimentoService.resgatarInvestimento(contaIdInvestimento,
                resgateRequest.getContaIdContaCorrente(),
                resgateRequest.getValor());
    }
}
