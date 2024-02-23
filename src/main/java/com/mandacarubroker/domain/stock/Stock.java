package com.mandacarubroker.domain.stock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import jakarta.persistence.Table;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;


/**
 * Class representing a stock.
 */
@Table(name = "stock")
@Entity(name = "stock")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Stock {
    /**
     * The StockService used by this controller to perform operations on stocks.
     */
    private static final Logger logger = LoggerFactory.getLogger(Stock.class);

    /**
     * Unique identifier for the stock.
     */
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    /**
     * Symbol of the stock.
     */
    private String symbol;

    /**
     * Company name associated with the stock.
     */
    private String companyName;

    /**
     * Current price of the stock.
     */
    private double price;

    /**
     * Constructor initializing a Stock instance based on data provided by a RequestStockDTO object.
     * @param requestStockDTO Object containing stock data.
     */
    public Stock(final RequestStockDTO requestStockDTO) {
        this.symbol = requestStockDTO.symbol();
        this.companyName = requestStockDTO.companyName();
        this.price = requestStockDTO.price();
    }

    /**
     * Method to change the stock price.
     * @param amount Amount to be changed in the stock price.
     * @param increase Boolean indicating whether the price is increasing or decreasing.
     * @return The new stock price.
     */
    public double changePrice(final double amount, final boolean increase) {
        logger.info("Changing price for stock: {}", this.symbol);
        double newPrice;
        if (increase) {
            newPrice = increasePrice(amount);
        } else {
            newPrice = decreasePrice(amount);
        }
        logger.info("New price for stock {}: {}", this.symbol, newPrice);
        return newPrice;
    }

    /**
     * Method to increase the stock price.
     * @param amount Value to be added to the current stock price.
     * @return The new stock price after the increase.
     */
    public double increasePrice(final double amount) {
        logger.info("Increasing price for stock: {} by {}", this.symbol, amount);
        return this.price + amount;
    }

    /**
     * Method to decrease the stock price.
     * @param amount Value to be subtracted from the current stock price.
     * @return The new stock price after the decrease.
     */
    public double decreasePrice(final double amount) {
        logger.info("Decreasing price for stock: {} by {}", this.symbol, amount);
        double newPrice = this.price - amount;
        if (newPrice < 0) {
            logger.error("Invalid operation: Decreasing price resulted in negative price for stock: {}", this.symbol);
            throw new IllegalStateException("Price cannot be negative");
        }
        return newPrice;
    }
}

