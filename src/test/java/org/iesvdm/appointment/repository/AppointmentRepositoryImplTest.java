package org.iesvdm.appointment.repository;

import org.iesvdm.appointment.entity.Appointment;
import org.iesvdm.appointment.entity.AppointmentStatus;
import org.iesvdm.appointment.entity.Customer;
import org.iesvdm.appointment.repository.impl.AppointmentRepositoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class AppointmentRepositoryImplTest {

    private Set<Appointment> appointments;

    private AppointmentRepository appointmentRepository;

    @BeforeEach
    public void setup() {
        appointments = new HashSet<>();
        appointmentRepository = new AppointmentRepositoryImpl(appointments);
    }

    /**
     * Crea 2 citas (Appointment) una con id 1 y otra con id 2,
     * resto de valores inventados.
     * Agrégalas a las citas (appointments) con la que
     * construyes el objeto appointmentRepository bajo test.
     * Comprueba que cuando invocas appointmentRepository.getOne con uno
     * de los id's anteriores recuperas obtienes el objeto.
     * Pero si lo invocas con otro id diferente recuperas null
     */
    @Test
    void getOneTest() {
        Appointment appointment1 = new Appointment();
        appointment1.setId(1);
        Appointment appointment2 = new Appointment();
        appointment2.setId(2);

        appointments.add(appointment1);
        appointments.add(appointment2);

        assertEquals(appointment1, appointmentRepository.getOne(1));
        assertEquals(appointment2, appointmentRepository.getOne(2));
        assertNull(appointmentRepository.getOne(3));

    }

    /**
     * Crea 2 citas (Appointment) y guárdalas mediante
     * appointmentRepository.save.
     * Comprueba que la colección appointments
     * contiene sólo esas 2 citas.
     */
    @Test
    void saveTest() {
        Appointment appointment1 = new Appointment();
        appointment1.setId(1);
        Appointment appointment2 = new Appointment();
        appointment2.setId(2);
        appointmentRepository.save(appointment1);
        appointmentRepository.save(appointment2);
        assertEquals(2, appointments.size());
        assertTrue(appointments.contains(appointment1));
        assertTrue(appointments.contains(appointment2));
    }

    /**
     * Crea 2 citas (Appointment) una cancelada por un usuario y otra no,
     * (atención al estado de la cita, lee el código) y agrégalas mediante
     * appointmentRepository.save a la colección de appointments
     * Comprueba que mediante appointmentRepository.findCanceledByUser
     * obtienes la cita cancelada.
     */
    @Test
    void findCanceledByUserTest() {
        Customer canceler = new Customer();
        canceler.setId(1);

        Appointment appointment1 = new Appointment();
        appointment1.setId(1);
        appointment1.setCanceler(canceler);
        appointment1.setStatus(AppointmentStatus.CANCELED); // Initialize status

        Appointment appointment2 = new Appointment();
        appointment2.setId(2);
        appointment2.setStatus(AppointmentStatus.SCHEDULED); // Initialize status

        appointmentRepository.save(appointment1);
        appointmentRepository.save(appointment2);

        List<Appointment> result = appointmentRepository.findCanceledByUser(canceler.getId());
        assertEquals(1, result.size());
        assertTrue(result.contains(appointment1));
    }

    /**
     * Crea 3 citas (Appointment), 2 para un mismo cliente (Customer)
     * con sólo una cita de ellas presentando fecha de inicio (start)
     * y fin (end) dentro del periodo de búsqueda (startPeriod,endPeriod).
     * Guárdalas mediante appointmentRepository.save.
     * Comprueba que appointmentRepository.findByCustomerIdWithStartInPeroid
     * encuentra la cita en cuestión.
     * Nota: utiliza LocalDateTime.of(...) para crear los LocalDateTime
     */
    @Test
    void findByCustomerIdWithStartInPeroidTest() {
        Customer customer = new Customer();
        customer.setId(1);
        LocalDateTime start1 = LocalDateTime.of(2023, 5, 17, 10, 0);
        LocalDateTime end1 = LocalDateTime.of(2023, 5, 17, 11, 0);
        LocalDateTime start2 = LocalDateTime.of(2023, 5, 18, 10, 0);
        LocalDateTime end2 = LocalDateTime.of(2023, 5, 18, 11, 0);
        LocalDateTime startPeriod = LocalDateTime.of(2023, 5, 17, 0, 0);
        LocalDateTime endPeriod = LocalDateTime.of(2023, 5, 17, 23, 59);
        Appointment appointment1 = new Appointment(start1, end1, customer);
        appointment1.setId(1);
        appointment1.setStatus(AppointmentStatus.SCHEDULED); // Initialize status
        Appointment appointment2 = new Appointment(start2, end2, customer);
        appointment2.setId(2);
        appointment2.setStatus(AppointmentStatus.SCHEDULED); // Initialize status
        appointmentRepository.save(appointment1);
        appointmentRepository.save(appointment2);
        List<Appointment> result = appointmentRepository.findByCustomerIdWithStartInPeroid(1, startPeriod, endPeriod);
        assertEquals(1, result.size());
        assertTrue(result.contains(appointment1));
    }


    /**
     * Crea 2 citas (Appointment) una planificada (SCHEDULED) con tiempo fin
     * anterior a la tiempo buscado por appointmentRepository.findScheduledWithEndBeforeDate
     * guardándolas mediante appointmentRepository.save para la prueba de findScheduledWithEndBeforeDate
     *
     */
    @Test
    void findScheduledWithEndBeforeDateTest() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime past = now.minusDays(1);
        Appointment appointment1 = new Appointment();
        appointment1.setId(1);
        appointment1.setStatus(AppointmentStatus.SCHEDULED);
        appointment1.setEnd(past);
        Appointment appointment2 = new Appointment();
        appointment2.setId(2);
        appointment2.setStatus(AppointmentStatus.SCHEDULED);
        appointment2.setEnd(now.plusDays(1));
        appointmentRepository.save(appointment1);
        appointmentRepository.save(appointment2);
        List<Appointment> result = appointmentRepository.findScheduledWithEndBeforeDate(now);
        assertEquals(1, result.size());
        assertTrue(result.contains(appointment1));
    }


    /**
     * Crea 3 citas (Appointment) planificadas (SCHEDULED)
     * , 2 para un mismo cliente, con una elegible para cambio (con fecha de inicio, start, adecuada)
     * y otra no.
     * La tercera ha de ser de otro cliente.
     * Guárdalas mediante appointmentRepository.save
     * Comprueba que getEligibleAppointmentsForExchange encuentra la correcta.
     */
    @Test
    void getEligibleAppointmentsForExchangeTest() {
        Customer customer1 = new Customer();
        customer1.setId(1);

        Customer customer2 = new Customer();
        customer2.setId(2);

        LocalDateTime futureStart1 = LocalDateTime.now().plusDays(1);
        LocalDateTime futureStart2 = LocalDateTime.now().plusDays(2);
        LocalDateTime futureStart3 = LocalDateTime.now().plusDays(3);

    }


    /**
     * Igual que antes, pero ahora las 3 citas tienen que tener
     * clientes diferentes y 2 de ellas con fecha de inicio (start)
     * antes de la especificada en el método de búsqueda para
     * findExchangeRequestedWithStartBefore
     */
    @Test
    void findExchangeRequestedWithStartBeforeTest() {
        LocalDateTime now = LocalDateTime.now();
        Appointment appointment1 = new Appointment();
        appointment1.setId(1);
        appointment1.setStatus(AppointmentStatus.EXCHANGE_REQUESTED);
        appointment1.setStart(now.minusDays(1));
        Appointment appointment2 = new Appointment();
        appointment2.setId(2);
        appointment2.setStatus(AppointmentStatus.EXCHANGE_REQUESTED);
        appointment2.setStart(now.plusDays(1));
        appointmentRepository.save(appointment1);
        appointmentRepository.save(appointment2);
        List<Appointment> result = appointmentRepository.findExchangeRequestedWithStartBefore(now);
        assertEquals(1, result.size());
        assertTrue(result.contains(appointment1));
    }
}
