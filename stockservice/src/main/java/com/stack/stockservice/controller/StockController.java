package com.stack.stockservice.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/data")
public class StockController {

    @GetMapping("/invoke")
    public ResponseEntity<List<Map<String, String>>> callPartyApi(@RequestParam String country) {
        RestTemplate restTemplate = new RestTemplate();
        String apiURl = "https://api.twelvedata.com/stocks?country=" + country;

        try {
            Map<String, Object> response = restTemplate.getForObject(apiURl, Map.class);
            List<Map<String, String>> stockData = (List<Map<String, String>>) response.get("data");

            // Deduplicate based on symbol and exchange
            List<Map<String, String>> uniqueStocks = stockData.stream()
                    .collect(Collectors.toMap(
                            stock -> stock.get("symbol") + "_" + stock.get("exchange"),
                            stock -> stock,
                            (existing, replacement) -> existing // In case of a duplicate, keep the existing one
                    ))
                    .values()
                    .stream()
                    .collect(Collectors.toList());

            return ResponseEntity.ok(uniqueStocks);
        } catch (HttpClientErrorException | HttpServerErrorException ex) {
            return ResponseEntity.status(ex.getStatusCode()).body(Collections.emptyList());
        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "An error occurred while fetching stock data", ex);
        }
    }
}
