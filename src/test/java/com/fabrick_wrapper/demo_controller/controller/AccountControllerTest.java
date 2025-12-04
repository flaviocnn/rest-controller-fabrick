package com.fabrick_wrapper.demo_controller.controller;

import com.fabrick_wrapper.demo_controller.dto.BalanceDTO;
import com.fabrick_wrapper.demo_controller.dto.MoneyTransferRequestDTO;
import com.fabrick_wrapper.demo_controller.dto.MoneyTransferResponseDTO;
import com.fabrick_wrapper.demo_controller.dto.TransactionDTO;
import com.fabrick_wrapper.demo_controller.service.FabrickService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AccountController.class)
class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FabrickService fabrickService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getBalance_ShouldReturnBalance() throws Exception {
        BalanceDTO balanceDTO = new BalanceDTO(LocalDate.now(), BigDecimal.valueOf(100), BigDecimal.valueOf(100),
                "EUR");
        when(fabrickService.getAccountBalance(12345L)).thenReturn(balanceDTO);

        mockMvc.perform(get("/api/account/12345/balance"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.balance").value(100));
    }

    @Test
    void getTransactions_ShouldReturnTransactions() throws Exception {
        TransactionDTO transactionDTO = new TransactionDTO(
                "TX1", "OP1", LocalDate.now(), LocalDate.now(), null, BigDecimal.TEN, "EUR", "Desc");
        when(fabrickService.getTransactions(eq(12345L), any(), any())).thenReturn(List.of(transactionDTO));

        mockMvc.perform(get("/api/account/12345/transactions")
                .param("fromAccountingDate", "2023-01-01")
                .param("toAccountingDate", "2023-01-05"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].transactionId").value("TX1"));
    }

    @Test
    void createMoneyTransfer_ShouldReturnResponse() throws Exception {
        MoneyTransferRequestDTO request = new MoneyTransferRequestDTO(
                "Creditor", "IT123", "Desc", "EUR", BigDecimal.TEN, LocalDate.now());
        MoneyTransferResponseDTO response = new MoneyTransferResponseDTO("API000", "Error");

        when(fabrickService.createMoneyTransfer(eq(12345L), any())).thenReturn(response);

        mockMvc.perform(post("/api/account/12345/payments/money-transfers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("API000"));
    }
}
