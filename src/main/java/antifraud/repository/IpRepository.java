package antifraud.repository;

import antifraud.model.entity.IPEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IpRepository extends JpaRepository<IPEntity, Long> {

    boolean existsByIp(String ip);

    long deleteByIp(String ip);
}