package antifraud.repository;

import antifraud.model.entity.TransactionHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

public interface TransactionHistoryRepository extends JpaRepository<TransactionHistory, Long> {
    List<TransactionHistory> findByDateBetween(LocalDateTime dateStart, LocalDateTime dateEnd);

    List<TransactionHistory> findByNumber(String number);



}