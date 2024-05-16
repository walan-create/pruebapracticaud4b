package org.iesvdm.appointment.repository;


import org.iesvdm.appointment.entity.Appointment;
import org.iesvdm.appointment.entity.ExchangeRequest;

import java.time.LocalDateTime;
import java.util.List;

public interface AppointmentRepository {

    public Appointment getOne(int appointmentId);

    public void save(Appointment appointment);

    public List<Appointment> findCanceledByUser(int userId);

    public List<Appointment> findByCustomerIdWithStartInPeroid( int customerId, LocalDateTime startPeroid, LocalDateTime endPeroid);

    public List<Appointment> findScheduledWithEndBeforeDate( LocalDateTime now);

    public List<Appointment> getEligibleAppointmentsForExchange(LocalDateTime start, Integer customerId);

    public List<Appointment> findExchangeRequestedWithStartBefore(LocalDateTime date);

}
