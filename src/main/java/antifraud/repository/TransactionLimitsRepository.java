package antifraud.repository;

import antifraud.model.entity.TransactionLimits;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TransactionLimitsRepository extends JpaRepository<TransactionLimits, Long> {

}