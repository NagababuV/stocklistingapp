package com.stack.wishlistservice.repository;

import com.stack.wishlistservice.model.Stock;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StockRepository extends MongoRepository<Stock, String> {
    List<Stock> findByUsernameAndType(String username, String type);
    void deleteByUsernameAndSymbolAndType(String username, String symbol, String type);
}
