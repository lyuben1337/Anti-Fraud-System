package antifraud.service.impl;

import antifraud.model.entity.StolenCardEntity;
import antifraud.model.response.CardDeleteStatus;
import antifraud.repository.StolenCardRepository;
import antifraud.service.StolenCardService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@Transactional
@AllArgsConstructor
public class StolenCardServiceImpl implements StolenCardService {
    StolenCardRepository repository;

    @Override
    public StolenCardEntity saveStolenCard(String number) {
        if(repository.existsByNumber(number)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    number + " is already in the database");
        }
        return repository.save(
                StolenCardEntity.builder()
                        .number(number)
                        .build()
        );
    }

    @Override
    public CardDeleteStatus deleteStolenCard(String number) {
        if(!repository.existsByNumber(number)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    number + " is not found");
        }
        repository.deleteByNumber(number);
        return new CardDeleteStatus("Card " + number + " successfully removed!");
    }

    @Override
    public List<StolenCardEntity> getStolenCardList() {
        return repository.findAll();
    }
}
