package com.mandacarubroker.service;

import com.mandacarubroker.domain.stock.RequestStockDTO;
import com.mandacarubroker.domain.stock.Stock;
import com.mandacarubroker.domain.stock.StockRepository;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Service class for handling operations related to stocks.
 */
@Service
public class StockService {

    /**
     * Stock repository.
     */
    private final StockRepository stockRepository;

    /**
     * The StockService used by this controller to perform operations on stocks.
     */
    private static final Logger logger = LoggerFactory.getLogger(StockService.class);

    /**
     * The Validator instance used for validating objects.
     * This Validator instance is responsible for performing validation checks
     * on objects, typically instances of DTOs (Data Transfer Objects) or domain
     * objects, to ensure they meet the specified constraints defined by validation
     * annotations such as @NotNull, @Size, @Pattern, etc. The Validator is obtained
     * from the ValidatorFactory, which is responsible for creating Validator instances
     * based on the validation provider configuration.
     *
     * @see jakarta.validation.Validator
     * @see jakarta.validation.ValidatorFactory
     */
    private final Validator validator;

    /**
     * Constructs a new StockService with the given StockRepository.
     *
     * @param stockRepo The repository for accessing stock data.
     */
    public StockService(final StockRepository stockRepo) {
        this.stockRepository = stockRepo;
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            this.validator = factory.getValidator();
        }
    }

    /**
     * Retrieves all stocks from the repository.
     *
     * @return A list of all stocks.
     */
    public List<Stock> getAllStocks() {
        logger.info("Retrieving all stocks");
        return stockRepository.findAll();
    }

    /**
     * Retrieves a stock by its ID.
     *
     * @param id The ID of the stock to retrieve.
     * @return An Optional containing the stock, or empty if not found.
     */
    public Optional<Stock> getStockById(final String id) {
        logger.info("Retrieving stock by ID: {}", id);
        return stockRepository.findById(id);
    }

    /**
     * Creates a new stock based on the provided data.
     *
     * @param data The data for creating the new stock.
     * @return The newly created stock.
     */
    public Stock createStock(final RequestStockDTO data) {
        logger.info("Creating new stock");
        validateRequestStockDTO(data);
        Stock newStock = new Stock(data);
        return stockRepository.save(newStock);
    }

    /**
     * Checks if a stock with certain ID exists.
     *
     * @param id The ID of the stock.
     * @return wether there's a stock with the specified ID or not.
     */
    public boolean stockExists(final String id) {
        Optional<Stock> stock = stockRepository.findById(id);
        return stock.isPresent();
    }

    /**
     * Updates an existing stock with the provided ID and data.
     *
     * @param id           The ID of the stock to update.
     * @param updatedStock The updated data for the stock.
     * @return updatedStock
     */
    public Stock updateStock(final String id, final Stock updatedStock) {
        logger.info("Updating stock with ID: {}", id);
        stockRepository.findById(id)
                .map(stock -> {
                    stock.setSymbol(updatedStock.getSymbol());
                    stock.setCompanyName(updatedStock.getCompanyName());
                    double newPrice = updatedStock.getPrice();
                    stock.setPrice(newPrice);
                    return stockRepository.save(stock);
                });
        return updatedStock;
    }

    /**
     * Deletes a stock with the provided ID.
     *
     * @param id The ID of the stock to delete.
     */
    public void deleteStock(final String id) {
        logger.info("Deleting stock with ID: {}", id);
        stockRepository.deleteById(id);
    }

    /**
     * Validates the RequestStockDTO object.
     *
     * @param data The RequestStockDTO object to validate.
     * @throws ConstraintViolationException If validation fails.
     */
    public void validateRequestStockDTO(final RequestStockDTO data) {
        logger.info("Validating RequestStockDTO");
        Set<ConstraintViolation<RequestStockDTO>> violations = validator.validate(data);
        if (!violations.isEmpty()) {
            StringBuilder errorMessage = new StringBuilder("Validation failed. Details: ");
            for (ConstraintViolation<RequestStockDTO> violation : violations) {
                errorMessage.append(String.format("[%s: %s], ", violation.getPropertyPath(), violation.getMessage()));
            }
            errorMessage.delete(errorMessage.length() - 2, errorMessage.length());
            logger.error(errorMessage.toString());
            throw new ConstraintViolationException(errorMessage.toString(), violations);
        }
    }

    /**
     * Validates the RequestStockDTO object and creates a new stock if validation passes.
     *
     * @param data The RequestStockDTO object to validate and create a stock from.
     */
    public void validateAndCreateStock(final RequestStockDTO data) {
        logger.info("Validating and creating new stock");
        validateRequestStockDTO(data);
        Stock newStock = new Stock(data);
        stockRepository.save(newStock);
    }}
