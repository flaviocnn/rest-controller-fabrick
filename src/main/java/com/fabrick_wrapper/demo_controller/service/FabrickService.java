package com.fabrick_wrapper.demo_controller.service;

import com.fabrick_wrapper.demo_controller.dto.BalanceDTO;
import com.fabrick_wrapper.demo_controller.dto.MoneyTransferRequestDTO;
import com.fabrick_wrapper.demo_controller.dto.MoneyTransferResponseDTO;
import com.fabrick_wrapper.demo_controller.dto.TransactionDTO;

import java.time.LocalDate;
import java.util.List;

public interface FabrickService {
    BalanceDTO getAccountBalance(Long accountId);

    List<TransactionDTO> getTransactions(Long accountId, LocalDate fromDate, LocalDate toDate);

    MoneyTransferResponseDTO createMoneyTransfer(Long accountId, MoneyTransferRequestDTO request);
}
