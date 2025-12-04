package com.fabrick_wrapper.demo_controller.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record TransactionDTO(
        String transactionId,
        String operationId,
        LocalDate accountingDate,
        LocalDate valueDate,
        TypeDTO type,
        BigDecimal amount,
        String currency,
        String description) {
    public record TypeDTO(String enumeration, String value) {
    }
}
