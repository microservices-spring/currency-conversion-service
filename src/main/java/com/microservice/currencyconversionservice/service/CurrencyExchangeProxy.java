package com.microservice.currencyconversionservice.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.microservice.currencyconversionservice.entity.CurrencyConversionBean;

@FeignClient(name = "currency-exchange-service", url = "localhost:8100")
public interface CurrencyExchangeProxy {
	
	@GetMapping("/currency-exchange/from/{from}/to/{to}")
	public CurrencyConversionBean retrieveExchangevalue(@PathVariable String from, @PathVariable String to);
}
