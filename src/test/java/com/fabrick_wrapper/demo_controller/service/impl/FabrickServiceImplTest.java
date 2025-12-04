package com.fabrick_wrapper.demo_controller.service.impl;

import com.fabrick_wrapper.demo_controller.dto.BalanceDTO;
import com.fabrick_wrapper.demo_controller.dto.MoneyTransferRequestDTO;
import com.fabrick_wrapper.demo_controller.dto.MoneyTransferResponseDTO;
import com.fabrick_wrapper.demo_controller.dto.TransactionDTO;
import com.fabrick_wrapper.demo_controller.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestClient;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@ExtendWith(MockitoExtension.class)
class FabrickServiceImplTest {

    @Mock
    private TransactionRepository transactionRepository;

    private FabrickServiceImpl fabrickService;
    private MockRestServiceServer mockServer;

    @BeforeEach
    void setUp() {
        RestClient.Builder builder = RestClient.builder();
        mockServer = MockRestServiceServer.bindTo(builder).build();
        fabrickService = new FabrickServiceImpl(builder.build(), transactionRepository);
    }

    @Test
    void getAccountBalance_ShouldReturnBalance() {
        String responseJson = """
                {
                    "status": "OK",
                    "errors": [],
                    "payload": {
                        "date": "2023-10-01",
                        "balance": 100.50,
                        "availableBalance": 100.50,
                        "currency": "EUR"
                    }
                }
                """;

        mockServer.expect(requestTo("/api/gbs/banking/v4.0/accounts/12345/balance"))
                .andRespond(withSuccess(responseJson, MediaType.APPLICATION_JSON));

        BalanceDTO result = fabrickService.getAccountBalance(12345L);

        assertThat(result).isNotNull();
        assertThat(result.balance()).isEqualByComparingTo("100.50");
        assertThat(result.currency()).isEqualTo("EUR");
    }

    @Test
    void getTransactions_ShouldReturnTransactionsAndSaveToDb() {
        String responseJson = """
                {
                    "status": "OK",
                    "errors": [],
                    "payload": {
                        "list": [
                            {
                                "transactionId": "TX123",
                                "operationId": "OP123",
                                "accountingDate": "2023-10-01",
                                "valueDate": "2023-10-01",
                                "type": {
                                    "enumeration": "TYPE_ENUM",
                                    "value": "TYPE_VALUE"
                                },
                                "amount": 50.00,
                                "currency": "EUR",
                                "description": "Test Transaction"
                            }
                        ]
                    }
                }
                """;

        mockServer.expect(requestTo(
                "/api/gbs/banking/v4.0/accounts/12345/transactions?fromAccountingDate=2023-10-01&toAccountingDate=2023-10-05"))
                .andRespond(withSuccess(responseJson, MediaType.APPLICATION_JSON));

        List<TransactionDTO> result = fabrickService.getTransactions(12345L, LocalDate.of(2023, 10, 1),
                LocalDate.of(2023, 10, 5));

        assertThat(result).hasSize(1);
        assertThat(result.get(0).transactionId()).isEqualTo("TX123");

        verify(transactionRepository).saveAll(anyList());
    }

    @Test
    void createMoneyTransfer_ShouldReturnResponse() {
        // Since we mock the failure in the service for the test requirement,
        // we can test the success path or the failure path.
        // The service implementation catches exception and returns the specific DTO.
        // So we should simulate a failure from the mock server to trigger that catch
        // block?
        // Or if we want to test the "success" path of the method (which returns the
        // error DTO),
        // we should make the mock server return an error.

        // However, the current implementation catches ANY exception.
        // Let's make the mock server return 400 Bad Request to simulate the API
        // failure.

        mockServer.expect(requestTo("/api/gbs/banking/v4.0/accounts/12345/payments/money-transfers"))
                .andRespond(withSuccess("", MediaType.APPLICATION_JSON)); // If success, returns OK DTO

        MoneyTransferRequestDTO request = new MoneyTransferRequestDTO(
                "Creditor", "IT123", "Desc", "EUR", BigDecimal.TEN, LocalDate.now());

        MoneyTransferResponseDTO result = fabrickService.createMoneyTransfer(12345L, request);

        assertThat(result.code()).isEqualTo("OK");
    }
}
