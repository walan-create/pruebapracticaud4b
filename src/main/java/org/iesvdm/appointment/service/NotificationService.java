package org.iesvdm.appointment.service;


import org.iesvdm.appointment.entity.Appointment;
import org.iesvdm.appointment.entity.ExchangeRequest;


public interface NotificationService {

    void newExchangeRequestedNotification(Appointment oldAppointment, Appointment newAppointment, boolean sendEmail);

    void newExchangeAcceptedNotification(ExchangeRequest exchangeRequest, boolean sendEmail);

    void newExchangeRejectedNotification(ExchangeRequest exchangeRequest, boolean sendEmail);
}
