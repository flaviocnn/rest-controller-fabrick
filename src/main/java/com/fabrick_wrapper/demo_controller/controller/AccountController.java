package com.fabrick_wrapper.demo_controller.controller;

import com.fabrick_wrapper.demo_controller.dto.BalanceDTO;
import com.fabrick_wrapper.demo_controller.dto.MoneyTransferRequestDTO;
import com.fabrick_wrapper.demo_controller.dto.MoneyTransferResponseDTO;
import com.fabrick_wrapper.demo_controller.dto.TransactionDTO;
import com.fabrick_wrapper.demo_controller.service.FabrickService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/account")
@RequiredArgsConstructor
public class AccountController {

    private final FabrickService fabrickService;

    @GetMapping("/{accountId}/balance")
    public ResponseEntity<BalanceDTO> getBalance(@PathVariable Long accountId) {
        return ResponseEntity.ok(fabrickService.getAccountBalance(accountId));
    }

    @GetMapping("/{accountId}/transactions")
    public ResponseEntity<List<TransactionDTO>> getTransactions(
            @PathVariable Long accountId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromAccountingDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toAccountingDate) {
        return ResponseEntity.ok(fabrickService.getTransactions(accountId, fromAccountingDate, toAccountingDate));
    }

    @PostMapping("/{accountId}/payments/money-transfers")
    public ResponseEntity<MoneyTransferResponseDTO> createMoneyTransfer(
            @PathVariable Long accountId,
            @RequestBody MoneyTransferRequestDTO request) {
        return ResponseEntity.ok(fabrickService.createMoneyTransfer(accountId, request));
    }
}
