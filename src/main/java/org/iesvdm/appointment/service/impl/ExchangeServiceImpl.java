package org.iesvdm.appointment.service.impl;

import org.iesvdm.appointment.entity.*;
import org.iesvdm.appointment.repository.AppointmentRepository;
import org.iesvdm.appointment.repository.ExchangeRequestRepository;
import org.iesvdm.appointment.service.ExchangeService;
import org.iesvdm.appointment.service.NotificationService;

import java.time.LocalDateTime;
import java.util.List;


public class ExchangeServiceImpl implements ExchangeService {

    private final AppointmentRepository appointmentRepository;
    private final NotificationService notificationService;
    private final ExchangeRequestRepository exchangeRequestRepository;

    public ExchangeServiceImpl(AppointmentRepository appointmentRepository, NotificationService notificationService, ExchangeRequestRepository exchangeRequestRepository) {
        this.appointmentRepository = appointmentRepository;
        this.notificationService = notificationService;
        this.exchangeRequestRepository = exchangeRequestRepository;
    }

    @Override
    public boolean checkIfEligibleForExchange(int userId, int appointmentId) {
        Appointment appointment = appointmentRepository.getOne(appointmentId);
        return appointment.getStart().minusHours(24).isAfter(LocalDateTime.now())
                && appointment.getStatus().equals(AppointmentStatus.SCHEDULED)
                && appointment.getCustomer().getId() == userId;
    }

    @Override
    public List<Appointment> getEligibleAppointmentsForExchange(int appointmentId) {
        Appointment appointmentToExchange = appointmentRepository.getOne(appointmentId);
        return appointmentRepository.getEligibleAppointmentsForExchange(LocalDateTime.now().plusHours(24)
                , appointmentToExchange.getCustomer().getId());
    }

    @Override
    public boolean checkIfExchangeIsPossible(int oldAppointmentId, int newAppointmentId, int userId) {
        Appointment oldAppointment = appointmentRepository.getOne(oldAppointmentId);
        Appointment newAppointment = appointmentRepository.getOne(newAppointmentId);
        if (oldAppointment.getCustomer().getId() == userId) {
            return  oldAppointment.getStart().minusHours(24).isAfter(LocalDateTime.now())
                    && newAppointment.getStart().minusHours(24).isAfter(LocalDateTime.now());
        } else {
            throw new RuntimeException("Unauthorized");
        }

    }

    @Override
    public boolean acceptExchange(int exchangeId, int userId) {
        ExchangeRequest exchangeRequest = exchangeRequestRepository.getOne(exchangeId);
        Appointment requestor = exchangeRequest.getRequestor();
        Appointment requested = exchangeRequest.getRequested();
        Customer tempCustomer = requestor.getCustomer();
        requestor.setStatus(AppointmentStatus.SCHEDULED);
        exchangeRequest.setStatus(ExchangeStatus.ACCEPTED);
        requestor.setCustomer(requested.getCustomer());
        requested.setCustomer(tempCustomer);
        exchangeRequestRepository.save(exchangeRequest);
        appointmentRepository.save(requested);
        appointmentRepository.save(requestor);
        notificationService.newExchangeAcceptedNotification(exchangeRequest, true);
        return true;
    }

    @Override
    public boolean rejectExchange(int exchangeId) {
        ExchangeRequest exchangeRequest = exchangeRequestRepository.getOne(exchangeId);
        Appointment requestor = exchangeRequest.getRequestor();
        exchangeRequest.setStatus(ExchangeStatus.REJECTED);
        requestor.setStatus(AppointmentStatus.SCHEDULED);
        exchangeRequestRepository.save(exchangeRequest);
        appointmentRepository.save(requestor);
        notificationService.newExchangeRejectedNotification(exchangeRequest, true);
        return true;
    }

    @Override
    public boolean requestExchange(int oldAppointmentId, int newAppointmentId, int userId) {
        if (checkIfExchangeIsPossible(oldAppointmentId, newAppointmentId, userId)) {
            Appointment oldAppointment = appointmentRepository.getOne(oldAppointmentId);
            Appointment newAppointment = appointmentRepository.getOne(newAppointmentId);
            oldAppointment.setStatus(AppointmentStatus.EXCHANGE_REQUESTED);
            appointmentRepository.save(oldAppointment);
            ExchangeRequest exchangeRequest = new ExchangeRequest(oldAppointment, newAppointment, ExchangeStatus.PENDING);
            exchangeRequestRepository.save(exchangeRequest);
            notificationService.newExchangeRequestedNotification(oldAppointment, newAppointment, true);
            return true;
        }
        return false;
    }
}
