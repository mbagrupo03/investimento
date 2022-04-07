package com.microservices.grupo3.investimento.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.microservices.grupo3.investimento.client.request.CriarCreditoRequest;
import com.microservices.grupo3.investimento.client.response.SaldoCreditoResponse;



@FeignClient(name = "credito", url = "${clients.credito}")
public interface CreditoClient {

    @RequestMapping(value = "/saldo/investimento/{contaId}", method = RequestMethod.GET)
    SaldoCreditoResponse getSaldoCreditoConta(@PathVariable("contaId") Integer contaId);

    @PostMapping()
    CriarCreditoRequest criarCredito(@RequestBody CriarCreditoRequest creditoRequest);
}
