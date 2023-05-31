package antifraud.model.entity;

import antifraud.model.consts.Region;
import antifraud.model.consts.TransactionResult;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;

import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@ToString
public class TransactionHistory {
    @Id
    @GeneratedValue
    Long id;

    long amount;

    String ip;

    String number;

    @Enumerated(EnumType.STRING)
    Region region;

    @Column(name = "transaction_date")
    @Temporal(TemporalType.TIMESTAMP)
    LocalDateTime date;

    @Enumerated(EnumType.STRING)
    TransactionResult result;

    String feedback;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        TransactionHistory that = (TransactionHistory) o;
        return getId() != null && Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
