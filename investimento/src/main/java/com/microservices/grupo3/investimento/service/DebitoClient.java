package com.microservices.grupo3.investimento.service;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.microservices.grupo3.investimento.client.request.CriarDebitoRequest;
import com.microservices.grupo3.investimento.client.response.SaldoDebitoResponse;

@FeignClient(name = "debito", url = "${clients.debito}")
public interface DebitoClient {

    @RequestMapping(value = "/saldo/investimento/{contaId}", method = RequestMethod.GET)
    SaldoDebitoResponse getSaldoDebitoConta(@PathVariable("contaId") Integer contaId);

    @PostMapping()
    CriarDebitoRequest criarDebito(@RequestBody CriarDebitoRequest debitoRequest);
}
