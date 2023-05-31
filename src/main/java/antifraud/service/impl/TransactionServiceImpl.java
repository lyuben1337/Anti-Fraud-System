package antifraud.service.impl;

import antifraud.model.consts.Region;
import antifraud.model.consts.TransactionResult;
import antifraud.model.entity.TransactionHistory;
import antifraud.model.entity.TransactionLimits;
import antifraud.model.response.TransactionDTO;
import antifraud.model.response.TransactionResponse;
import antifraud.repository.IpRepository;
import antifraud.repository.StolenCardRepository;
import antifraud.repository.TransactionHistoryRepository;
import antifraud.repository.TransactionLimitsRepository;
import antifraud.service.TransactionService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import static java.lang.Math.ceil;

@Slf4j
@Service
@Transactional
@AllArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    TransactionHistoryRepository transactionRepository;
    IpRepository ipRepository;
    StolenCardRepository cardRepository;
    TransactionLimitsRepository transactionLimitsRepository;

    @Override
    public TransactionResponse createTransaction(long amount, String ip, String number, String regionCode, LocalDateTime date) {
        if(transactionLimitsRepository.findById(1L).isEmpty()) {
            transactionLimitsRepository.save(TransactionLimits.builder()
                            .maxManual(1500L)
                            .maxAllowed(200L)
                    .build());
        }

        var limits = transactionLimitsRepository.findById(1L).get();

        var region = Region.valueOf(regionCode);
        TransactionResult transactionResult = null;

        Set<String> reasons = new TreeSet<>();
        var transactions = transactionRepository.findByDateBetween(date.minusHours(1), date);

        log.error(limits.toString());

        if(amount > limits.getMaxManual()) {
            reasons.add("amount");
            transactionResult = TransactionResult.PROHIBITED;
        }

        if(ipRepository.existsByIp(ip)) {
            reasons.add("ip");
            transactionResult = TransactionResult.PROHIBITED;
        }

        if(cardRepository.existsByNumber(number)) {
            reasons.add("card-number");
            transactionResult = TransactionResult.PROHIBITED;
        }

        if(transactions.stream()
                .map(TransactionHistory::getRegion)
                .filter(transactionRegion -> !transactionRegion.equals(region))
                .distinct()
                .count() > 2
        ) {
            reasons.add("region-correlation");
            transactionResult = TransactionResult.PROHIBITED;
        }

        if(transactions.stream()
                .map(TransactionHistory::getIp)
                .filter(transactionIp -> !transactionIp.equals(ip))
                .distinct()
                .count() > 2
        ) {
            transactions.stream()
                    .map(TransactionHistory::getIp)
                    .filter(transactionIp -> !transactionIp.equals(ip))
                    .distinct()
                    .forEach(log::error);
            transactions.forEach(transactionHistory -> log.error(transactionHistory.toString()));
            log.error("{}, {}", date.toString(), ip);
            reasons.add("ip-correlation");
            transactionResult = TransactionResult.PROHIBITED;
        }

        if(transactionResult != null) {
            transactionRepository.save(TransactionHistory.builder()
                    .amount(amount)
                    .ip(ip)
                    .number(number)
                    .region(region)
                    .date(date)
                    .result(transactionResult)
                    .feedback("")
                    .build()
            );
            return new TransactionResponse(transactionResult.name(), String.join(", ", reasons));
        }

        if(amount > limits.getMaxAllowed() && amount <= limits.getMaxManual()) {
            reasons.add("amount");
            transactionResult = TransactionResult.MANUAL_PROCESSING;
        }

        if(transactions.stream()
                .map(TransactionHistory::getRegion)
                .filter(transactionRegion -> !transactionRegion.equals(region))
                .distinct()
                .count() == 2
        ) {
            reasons.add("region-correlation");
            transactionResult = TransactionResult.MANUAL_PROCESSING;
        }

        if(transactions.stream()
                .map(TransactionHistory::getIp)
                .filter(transactionIp -> !transactionIp.equals(ip))
                .distinct()
                .count() == 2
        ) {
            reasons.add("ip-correlation");
            transactionResult = TransactionResult.MANUAL_PROCESSING;
        }

        if(transactionResult != null) {
            transactionRepository.save(TransactionHistory.builder()
                    .amount(amount)
                    .ip(ip)
                    .number(number)
                    .region(region)
                    .date(date)
                    .result(transactionResult)
                    .feedback("")
                    .build()
            );
            return new TransactionResponse(transactionResult.name(), String.join(", ", reasons));
        }

        transactionResult = TransactionResult.ALLOWED;

        transactionRepository.save(TransactionHistory.builder()
                .amount(amount)
                .ip(ip)
                .number(number)
                .region(region)
                .date(date)
                .result(transactionResult)
                .feedback("")
                .build()
        );
        return new TransactionResponse(transactionResult.name(), "none");
    }

    @Override
    public TransactionDTO feedback(Long transactionId, String feedback) {
        TransactionHistory transaction = transactionRepository.findById(transactionId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND)
        );
        if(transaction.getResult().name().equals(feedback)) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY);
        }
        if(!transaction.getFeedback().equals("")) {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }

        transaction.setFeedback(feedback);
        var limits = transactionLimitsRepository.findById(1L).get();
        transactionRepository.save(transaction);

        double new_limit_manual = limits.getMaxManual();
        double new_limit_allowed = limits.getMaxAllowed();

        switch (TransactionResult.valueOf(feedback)) {
            case ALLOWED -> {
                switch (transaction.getResult()) {
                    case PROHIBITED -> {
                        new_limit_manual = 0.8 * limits.getMaxManual() + 0.2 * transaction.getAmount();
                        new_limit_allowed = 0.8 * limits.getMaxAllowed() + 0.2 * transaction.getAmount();
                    }
                    case MANUAL_PROCESSING -> {
                        new_limit_allowed = 0.8 * limits.getMaxAllowed() + 0.2 * transaction.getAmount();
                    }
                }
            }
            case MANUAL_PROCESSING -> {
                switch (transaction.getResult()) {
                    case PROHIBITED -> {
                        new_limit_manual = 0.8 * limits.getMaxManual() + 0.2 * transaction.getAmount();
                    }
                    case ALLOWED -> {
                        new_limit_allowed = 0.8 * limits.getMaxAllowed() - 0.2 * transaction.getAmount();
                    }
                }
            }
            case PROHIBITED -> {
                switch (transaction.getResult()) {
                    case ALLOWED -> {
                        new_limit_manual = 0.8 * limits.getMaxManual() - 0.2 * transaction.getAmount();
                        new_limit_allowed = 0.8 * limits.getMaxAllowed() - 0.2 * transaction.getAmount();
                    }
                    case MANUAL_PROCESSING -> {
                        new_limit_manual = 0.8 * limits.getMaxManual() - 0.2 * transaction.getAmount();
                    }
                }
            }
        }

        limits.setMaxAllowed((long)ceil(new_limit_allowed));
        limits.setMaxManual((long) ceil(new_limit_manual));

        return TransactionDTO.builder()
                .transactionId(transaction.getId())
                .amount(transaction.getAmount())
                .ip(transaction.getIp())
                .number(transaction.getNumber())
                .region(transaction.getRegion().name())
                .date(transaction.getDate())
                .result(transaction.getResult().name())
                .feedback(transaction.getFeedback())
                .build();
    }

    @Override
    public List<TransactionDTO> history() {
        return transactionRepository.findAll()
                .stream()
                .map(transactionHistory -> TransactionDTO.builder()
                        .transactionId(transactionHistory.getId())
                        .amount(transactionHistory.getAmount())
                        .ip(transactionHistory.getIp())
                        .number(transactionHistory.getNumber())
                        .region(transactionHistory.getRegion().name())
                        .date(transactionHistory.getDate())
                        .result(transactionHistory.getResult().name())
                        .feedback(transactionHistory.getFeedback())
                        .build()
                )
                .collect(Collectors.toList());
    }

    @Override
    public List<TransactionDTO> history(String cardNumber) {
        var transactions = transactionRepository.findByNumber(cardNumber).stream()
                .map(transactionHistory -> TransactionDTO.builder()
                        .transactionId(transactionHistory.getId())
                        .amount(transactionHistory.getAmount())
                        .ip(transactionHistory.getIp())
                        .number(transactionHistory.getNumber())
                        .region(transactionHistory.getRegion().name())
                        .date(transactionHistory.getDate())
                        .result(transactionHistory.getResult().name())
                        .feedback(transactionHistory.getFeedback())
                        .build()
                )
                .collect(Collectors.toList());

        if(transactions.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        return transactions;
    }
}
