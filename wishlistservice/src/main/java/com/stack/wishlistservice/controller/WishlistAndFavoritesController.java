package com.stack.wishlistservice.controller;

import com.stack.wishlistservice.dto.StockDto;
import com.stack.wishlistservice.service.WishlistAndFavoritesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/wishlist")
public class WishlistAndFavoritesController {

    @Autowired
    private WishlistAndFavoritesService wishlistAndFavoritesService;

    @GetMapping("/{type}/list")
    public ResponseEntity<List<StockDto>> getList(
            @PathVariable String type,
            @RequestParam String username) {
        List<StockDto> stockList = wishlistAndFavoritesService.getList(username, type);
        return ResponseEntity.ok(stockList);
    }

    @PostMapping("/{type}/add")
    public ResponseEntity<Void> addToList(
            @PathVariable String type,
            @RequestBody StockDto stockDto,
            @RequestParam String username) {
        wishlistAndFavoritesService.addToList(username, stockDto, type);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{type}/remove")
    public ResponseEntity<Void> removeFromList(
            @PathVariable String type,
            @RequestParam String symbol,
            @RequestParam String username) {
        wishlistAndFavoritesService.removeFromList(username, symbol, type);
        return ResponseEntity.ok().build();
    }
}
