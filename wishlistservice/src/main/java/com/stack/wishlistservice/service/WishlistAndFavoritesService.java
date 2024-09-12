package com.stack.wishlistservice.service;

import com.stack.wishlistservice.dto.StockDto;
import com.stack.wishlistservice.model.Stock;
import com.stack.wishlistservice.repository.StockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class WishlistAndFavoritesService {

    @Autowired
    private StockRepository stockRepository;

    public List<StockDto> getList(String username, String type) {
        List<Stock> stocks = stockRepository.findByUsernameAndType(username, type);
        return stocks.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    public void addToList(String username, StockDto stockDto, String type) {
        List<Stock> existingStocks = stockRepository.findByUsernameAndType(username, type);
        boolean stockExists = existingStocks.stream().anyMatch(stock ->
                stock.getSymbol().equals(stockDto.getSymbol()) &&
                        stock.getName().equals(stockDto.getName()) &&
                        stock.getCurrency().equals(stockDto.getCurrency()) &&
                        stock.getExchange().equals(stockDto.getExchange()) &&
                        stock.getCountry().equals(stockDto.getCountry()));

        if (!stockExists) {
            Stock stock = convertToEntity(stockDto);
            stock.setUsername(username);
            stock.setType(type);
            stockRepository.save(stock);
        } else {
            // Optionally handle duplicate scenario, e.g., log a message or throw an exception
        }
    }

    public void removeFromList(String username, String symbol, String type) {
        stockRepository.deleteByUsernameAndSymbolAndType(username, symbol, type);
    }

    private Stock convertToEntity(StockDto stockDto) {
        Stock stock = new Stock();
        stock.setSymbol(stockDto.getSymbol());
        stock.setName(stockDto.getName());
        stock.setCurrency(stockDto.getCurrency());
        stock.setExchange(stockDto.getExchange());
        stock.setMicCode(stockDto.getMicCode());
        stock.setCountry(stockDto.getCountry());
        return stock;
    }

    private StockDto convertToDto(Stock stock) {
        StockDto stockDto = new StockDto();
        stockDto.setSymbol(stock.getSymbol());
        stockDto.setName(stock.getName());
        stockDto.setCurrency(stock.getCurrency());
        stockDto.setExchange(stock.getExchange());
        stockDto.setMicCode(stock.getMicCode());
        stockDto.setCountry(stock.getCountry());
        return stockDto;
    }
}
