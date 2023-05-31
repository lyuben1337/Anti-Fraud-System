package antifraud.service;

import antifraud.model.entity.IPEntity;
import antifraud.model.response.IpDeleteStatus;

import java.util.List;

public interface IpService {
    IPEntity register(String ip);

    IpDeleteStatus delete(String ip);

    List<IPEntity> getIpList();
}
