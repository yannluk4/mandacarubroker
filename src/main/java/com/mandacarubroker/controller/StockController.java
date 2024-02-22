package com.mandacarubroker.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.mandacarubroker.domain.stock.Stock;
import com.mandacarubroker.domain.stock.RequestStockDTO;
import com.mandacarubroker.service.StockService;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * Controller class to handle HTTP requests related to stocks.
 */
@RestController
@RequestMapping("/stocks")
public class StockController {

    /**
     * The StockService used by this controller to perform operations on stocks.
     */
    private final StockService stockService;
    /**
     * The logger service used for Observability  purposes.
     */
    private static final Logger logger = LoggerFactory.getLogger(StockController.class);

    /**
     * Constructs a new StockController with the provided StockService.
     *
     * @param service the StockService to be used by the controller
     */
    public StockController(final StockService service) {
        this.stockService = service;
    }

    /**
     * Checks if the ID is valid (not null or empty).
     *
     * @param id the ID to check
     * @return true if the ID is valid, false otherwise
     */
    private boolean isValidId(final String id) {
        return id != null && !id.trim().isEmpty();
    }

    /**
     * Checks if a stock with the given ID exists.
     *
     * @param id the ID of the stock
     * @return true if the stock exists, false otherwise
     */
    private boolean stockExists(final String id) {
        return stockService.stockExists(id);
    }

    /**
     * Retrieves all stocks.
     *
     * @return a list of all stocks
     */
    @GetMapping
    public List<Stock> getAllStocks() {
        logger.info("Retrieving all stocks");
        return stockService.getAllStocks();
    }

    /**
     * Retrieves a stock by its ID.
     *
     * @param id the ID of the stock to retrieve
     * @return the stock with the given ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getStockById(@PathVariable final String id) {
        logger.info("Retrieving stock with ID: {}", id);
        return stockService.getStockById(id)
                .map(stock -> ResponseEntity.ok().body(stock))
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Creates a new stock.
     *
     * @param data the data for the new stock
     * @return the newly created stock
     */
    @PostMapping
    public ResponseEntity<Stock> createStock(@RequestBody final RequestStockDTO data) {
        if (data.price() <= 0) {
            logger.error("Invalid price provided for new stock creation");
            return ResponseEntity.badRequest().build();
        }
        logger.info("Creating new stock");
        Stock createdStock = stockService.createStock(data);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdStock);
    }

    /**
     * Updates an existing stock.
     *
     * @param id           the ID of the stock to update
     * @param updatedStock the updated information for the stock
     * @return the updated stock, or 404 code if no stock with the given ID is found
     */
    @PutMapping("/{id}")
    public ResponseEntity<Stock> updateStock(@PathVariable final String id, @RequestBody final Stock updatedStock) {
        logger.info("Updating stock with ID: {}", id);
        boolean stockIsNotValid = !isValidId(id);
        if (stockIsNotValid) {
            logger.error("Invalid ID provided for update operation");
            return ResponseEntity.badRequest().build();
        }

        boolean stockDoesNotExists = !stockExists(id);
        if (stockDoesNotExists) {
            logger.error("Stock with ID {} not found for update", id);
            return ResponseEntity.notFound().build();
        }

        Stock newStock = stockService.updateStock(id, updatedStock);
        return ResponseEntity.status(HttpStatus.CREATED).body(newStock);
    }

    /**
     * Deletes a stock by its ID.
     *
     * @param id the ID of the stock to delete
     * @return a ResponseEntity indicating the success of the deletion operation or a 404 code if the ID was not found
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStockById(@PathVariable final String id) {
        logger.info("Deleting stock with ID: {}", id);
        boolean stockIsNotValid = !isValidId(id);
        if (stockIsNotValid) {
            logger.error("Invalid ID provided for delete operation");
            return ResponseEntity.badRequest().build();
        }

        boolean stockDoesNotExists = !stockExists(id);
        if (stockDoesNotExists) {
            logger.error("Stock with ID {} not found for deletion", id);
            return ResponseEntity.notFound().build();
        }

        stockService.deleteStock(id);
        return ResponseEntity.ok().build();
    }
}