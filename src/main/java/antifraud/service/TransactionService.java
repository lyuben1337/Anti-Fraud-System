package antifraud.service;

import antifraud.model.response.TransactionDTO;
import antifraud.model.response.TransactionResponse;

import java.time.LocalDateTime;
import java.util.List;


public interface TransactionService {

    TransactionResponse createTransaction(long amount, String ip, String number, String region, LocalDateTime date);

    TransactionDTO feedback(Long transactionId, String feedback);

    List<TransactionDTO> history();

    List<TransactionDTO> history(String cardNumber);
}
