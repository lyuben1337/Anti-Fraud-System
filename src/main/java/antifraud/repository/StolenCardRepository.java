package antifraud.repository;

import antifraud.model.entity.StolenCardEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface StolenCardRepository extends JpaRepository<StolenCardEntity, Long> {
    boolean existsByNumber(String number);

    @Transactional
    @Modifying
    @Query("delete from StolenCardEntity s where s.number = ?1")
    int deleteByNumber(String number);
}