package com.stack.stockservice.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class StockControllerTest {

    @InjectMocks
    private StockController stockController;

    @Mock
    private RestTemplate restTemplate;

    private Map<String, Object> apiResponse;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Set up a mock API response
        apiResponse = new HashMap<>();
        List<Map<String, String>> stockData = new ArrayList<>();

        Map<String, String> stock1 = new HashMap<>();
        stock1.put("symbol", "AAPL");
        stock1.put("exchange", "NASDAQ");
        stockData.add(stock1);

        Map<String, String> stock2 = new HashMap<>();
        stock2.put("symbol", "GOOGL");
        stock2.put("exchange", "NASDAQ");
        stockData.add(stock2);

        apiResponse.put("data", stockData);
    }

    @Test
    void callPartyApi_SuccessfulResponse_ShouldReturnUniqueStocks() {
        when(restTemplate.getForObject(anyString(), eq(Map.class))).thenReturn(apiResponse);

        ResponseEntity<List<Map<String, String>>> response = stockController.callPartyApi("USA");
        List<Map<String, String>> expectedStocks = new ArrayList<>((Integer) apiResponse.get("data"));

        // Deduplicate based on symbol and exchange
        List<Map<String, String>> uniqueStocks = expectedStocks.stream()
                .collect(Collectors.toMap(
                        stock -> stock.get("symbol") + "_" + stock.get("exchange"),
                        stock -> stock,
                        (existing, replacement) -> existing
                ))
                .values()
                .stream()
                .collect(Collectors.toList());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(uniqueStocks, response.getBody());
    }

    @Test
    void callPartyApi_ClientError_ShouldReturnClientErrorStatus() {
        when(restTemplate.getForObject(anyString(), eq(Map.class)))
                .thenThrow(new HttpClientErrorException(HttpStatus.BAD_REQUEST));

        ResponseEntity<List<Map<String, String>>> response = stockController.callPartyApi("USA");

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(Collections.emptyList(), response.getBody());
    }

    @Test
    void callPartyApi_ServerError_ShouldReturnServerErrorStatus() {
        when(restTemplate.getForObject(anyString(), eq(Map.class)))
                .thenThrow(new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR));

        ResponseEntity<List<Map<String, String>>> response = stockController.callPartyApi("USA");

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(Collections.emptyList(), response.getBody());
    }

}
