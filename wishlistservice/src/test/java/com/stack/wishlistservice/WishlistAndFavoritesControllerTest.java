package com.stack.wishlistservice;

import com.stack.wishlistservice.controller.WishlistAndFavoritesController;
import com.stack.wishlistservice.dto.StockDto;
import com.stack.wishlistservice.service.WishlistAndFavoritesService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class WishlistAndFavoritesControllerTest {

    @InjectMocks
    private WishlistAndFavoritesController wishlistAndFavoritesController;

    @Mock
    private WishlistAndFavoritesService wishlistAndFavoritesService;

    private StockDto stockDto;
    private String username;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        stockDto = new StockDto();
        stockDto.setSymbol("EPIGRAL");
        stockDto.setName("Epigral Limited");
        stockDto.setCurrency("INR");
        stockDto.setExchange("NSE");
        stockDto.setMicCode("XNSE");
        stockDto.setCountry("India");
        username = "user";
    }

    @Test
    void getList_ShouldReturnStockList() {
        List<StockDto> stockList = Collections.singletonList(stockDto);
        when(wishlistAndFavoritesService.getList(username, "wishlist")).thenReturn(stockList);

        ResponseEntity<List<StockDto>> response = wishlistAndFavoritesController.getList("wishlist", username);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(stockList, response.getBody());
    }

    @Test
    void addToList_ShouldReturnOk() {
        doNothing().when(wishlistAndFavoritesService).addToList(username, stockDto, "wishlist");

        ResponseEntity<Void> response = wishlistAndFavoritesController.addToList("wishlist", stockDto, username);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void removeFromList_ShouldReturnOk() {
        doNothing().when(wishlistAndFavoritesService).removeFromList(username, "EPIGRAL", "wishlist");

        ResponseEntity<Void> response = wishlistAndFavoritesController.removeFromList("wishlist", "EPIGRAL", username);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}
