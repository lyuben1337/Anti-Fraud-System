package antifraud.controller;

import antifraud.model.request.FeedbackRequest;
import antifraud.model.request.TransactionRequest;
import antifraud.model.response.TransactionDTO;
import antifraud.model.response.TransactionResponse;
import antifraud.service.TransactionService;
import antifraud.validation.CardNumber;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;


@RestController
@RequestMapping("/api/antifraud")
@Validated
@AllArgsConstructor
public class TransactionController {
    TransactionService transactionService;

    @PutMapping("/transaction")
    TransactionDTO feedback(@Valid @RequestBody FeedbackRequest feedback) {
        return transactionService.feedback(feedback.transactionId(), feedback.feedback());
    }

    @PostMapping("/transaction")
    @ResponseStatus(HttpStatus.OK)
    TransactionResponse transaction(@Valid @RequestBody TransactionRequest transactionRequest) {
        return transactionService.createTransaction(transactionRequest.amount(),
                transactionRequest.ip(),
                transactionRequest.number(),
                transactionRequest.region(),
                transactionRequest.date());
    }

    @GetMapping("/history")
    List<TransactionDTO> history() {
        return transactionService.history();
    }

    @GetMapping("/history/{card-number}")
    List<TransactionDTO> history(@PathVariable("card-number") @CardNumber String cardNumber) {
        return transactionService.history(cardNumber);
    }
}
