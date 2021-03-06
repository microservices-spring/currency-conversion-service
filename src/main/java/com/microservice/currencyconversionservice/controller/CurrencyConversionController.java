package com.microservice.currencyconversionservice.controller;

import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.microservice.currencyconversionservice.entity.CurrencyConversionBean;
import com.microservice.currencyconversionservice.service.CurrencyExchangeProxy;

@RestController
@RequestMapping("currency-converter")
public class CurrencyConversionController {
	@Autowired
	private CurrencyExchangeProxy currencyExchangeProxy;

	@GetMapping("/from/{from}/to/{to}/quantity/{amount}")
	public CurrencyConversionBean convert(@PathVariable String from, @PathVariable String to,
			@PathVariable BigDecimal amount) {
		RestTemplate restTemplate = new RestTemplate();
		try {
			Map<String, Object> uriVariables = new HashMap<String, Object>();
			uriVariables.put("from", from);
			uriVariables.put("to", to);

			CurrencyConversionBean currencyBean = restTemplate.getForObject(
					"http://localhost:8000/currency-exchange/from/{from}/to/{to}", CurrencyConversionBean.class,
					uriVariables);
			if (Objects.nonNull(currencyBean)) {
				currencyBean.setQuantity(amount);
				currencyBean.setCalculatedAmount(amount.multiply(currencyBean.getConversionMultiple()));
				return currencyBean;
			}
		} catch (RestClientException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@GetMapping("/proxy/from/{from}/to/{to}/quantity/{amount}")
	public CurrencyConversionBean convertUsingProxy(@PathVariable String from, @PathVariable String to,
			@PathVariable BigDecimal amount) {
		CurrencyConversionBean currencyBean = currencyExchangeProxy.retrieveExchangevalue(from, to);
		if(Objects.nonNull(currencyBean)) {
			currencyBean.setQuantity(amount);
			currencyBean.setCalculatedAmount(amount.multiply(currencyBean.getConversionMultiple()));
		}
		return currencyBean;
	}
}
