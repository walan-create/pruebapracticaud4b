package org.iesvdm.appointment.repository;


import org.iesvdm.appointment.entity.ExchangeRequest;

public interface ExchangeRequestRepository {

    public ExchangeRequest getOne(int exchangeRequestId);

    public void save(ExchangeRequest exchangeRequest);

}
