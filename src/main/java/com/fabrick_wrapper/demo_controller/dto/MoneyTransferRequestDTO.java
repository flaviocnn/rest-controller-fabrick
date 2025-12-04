package com.fabrick_wrapper.demo_controller.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record MoneyTransferRequestDTO(
        String creditorName,
        String creditorAccountCode,
        String description,
        String currency,
        BigDecimal amount,
        LocalDate executionDate) {
}
