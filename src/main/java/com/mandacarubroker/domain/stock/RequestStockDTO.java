package com.mandacarubroker.domain.stock;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record RequestStockDTO(
        @Pattern(regexp = "^[A-Z]{4}\\d{1,2}([A-Z]?|[A-Z]\\d|F|B|N[1-3])?$",
                message = "Symbol dos not match the B3 pattern")
        String symbol,
        @NotBlank(message = "Company name cannot be blank")
        String companyName,
        @NotNull(message = "Price cannot be null")
        double price
) {
}
