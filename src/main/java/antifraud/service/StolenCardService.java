package antifraud.service;

import antifraud.model.entity.StolenCardEntity;
import antifraud.model.response.CardDeleteStatus;

import java.util.List;

public interface StolenCardService {
    StolenCardEntity saveStolenCard(String number);

    CardDeleteStatus deleteStolenCard(String number);

    List<StolenCardEntity> getStolenCardList();
}
