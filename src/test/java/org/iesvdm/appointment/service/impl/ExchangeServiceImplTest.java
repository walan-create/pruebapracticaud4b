package org.iesvdm.appointment.service.impl;

import net.bytebuddy.asm.Advice;
import org.iesvdm.appointment.entity.*;
import org.iesvdm.appointment.repository.AppointmentRepository;
import org.iesvdm.appointment.repository.ExchangeRequestRepository;
import org.iesvdm.appointment.repository.impl.AppointmentRepositoryImpl;
import org.iesvdm.appointment.service.NotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

public class ExchangeServiceImplTest {

    @Spy
    private AppointmentRepository appointmentRepository = new AppointmentRepositoryImpl(new HashSet<>());

    @Mock
    private NotificationService notificationService;

    @Mock
    private  ExchangeRequestRepository exchangeRequestRepository;

    @InjectMocks
    private ExchangeServiceImpl exchangeService;

    private Customer customer1 = new Customer(1
            ,"paco"
            , "1234"
            , new ArrayList<>());
    private Customer customer2 = new Customer(2
            ,"pepe"
            , "1111"
            , new ArrayList<>());

    @Captor
    private ArgumentCaptor<Integer> appointmentIdCaptor;

    @Spy
    private Appointment appointment1 = new Appointment(LocalDateTime.of(2024, 6, 10,6, 0)
            , LocalDateTime.of(2024, 6, 16,18, 0)
            , null
            , null
            , AppointmentStatus.SCHEDULED
            , customer1
            , null
                                );

    @Spy
    private Appointment appointment2 = new Appointment(LocalDateTime.of(2024, 5, 18,8, 15)
            , LocalDateTime.of(2024, 5, 18,10, 15)
            , null
            , null
            , AppointmentStatus.SCHEDULED
            , customer2
            , null
    );

    @BeforeEach
    public void setup() {

        MockitoAnnotations.initMocks(this);

    }

    /**
     * Crea un stub para appointmentRepository.getOne
     * que devuelva una cita (Appointment) que
     * cumple que la fecha/tiempo de inicio (start) es
     * al menos un día después de la fecha/tiempo de búsqueda (actual)
     * , junto con los parámetros de estar planificada (SCHEDULED) y
     * pertenecer al cliente con userId 3.
     * De este modo que al invocar exchangeServiceImpl.checkIfEligibleForExchange
     * se debe obtener true.
     */
    @Test
    void checkIfEligibleForExchange() {
        int userId = 3;
        int appointmentId = 1;
        LocalDateTime start = LocalDateTime.now().plusDays(2);

        Appointment eligibleAppointment = new Appointment(
                start,
                start.plusHours(1),
                null,
                null,
                AppointmentStatus.SCHEDULED,
                new Customer(userId, "test", "test", new ArrayList<>()),
                null
        );

        when(appointmentRepository.getOne(appointmentId)).thenReturn(eligibleAppointment);

        boolean result = exchangeService.checkIfEligibleForExchange(userId, appointmentId);

        assertTrue(result);
    }

    /**
     * Añade mediante appointementRepository.save
     * 2 citas (Appointment) de modo que la eligible
     * la 2a empieza más de 24 horas más tarde
     * y pertenece a un cliente (Customer) con id diferente del
     * cliente de la primera que será appointmentToExchange.
     * Se debe verificar la invocación de los métodos appointmentRepository.getOne
     * con el appointmentId pasado a capturar mediante el captor de id
     */
    @Test
    void getEligibleAppointmentsForExchangeTest() {
        int appointmentToExchangeId = 1;

        Appointment appointmentToExchange = new Appointment(
                LocalDateTime.of(2024, 6, 10, 6, 0),
                LocalDateTime.of(2024, 6, 16, 18, 0),
                null, null, AppointmentStatus.SCHEDULED, new Customer(1, "paco", "1234", new ArrayList<>()), null);

        Appointment eligibleAppointment = new Appointment(
                LocalDateTime.of(2024, 6, 17, 6, 0),
                LocalDateTime.of(2024, 6, 17, 18, 0),
                null, null, AppointmentStatus.SCHEDULED, new Customer(2, "pepe", "1111", new ArrayList<>()), null);

        when(appointmentRepository.getOne(appointmentToExchangeId)).thenReturn(appointmentToExchange);
        when(appointmentRepository.getEligibleAppointmentsForExchange(any(), anyInt())).thenReturn(List.of(eligibleAppointment));

        List<Appointment> eligibleAppointments = exchangeService.getEligibleAppointmentsForExchange(appointmentToExchangeId);

        assertFalse(eligibleAppointments.isEmpty());
        verify(appointmentRepository).getOne(appointmentToExchangeId);
    }

    /**
     * Realiza una prueba que mediante stubs apropiados demuestre
     * que cuando el userID es igual al userId del oldAppointment
     * se lanza una RuntimeException con mensaje Unauthorized
     */
    @Test
    void checkIfExchangeIsPossibleTest() {
        int userId = 1;
        int oldAppointmentId = 1;
        int newAppointmentId = 2;
        Appointment oldAppointment = new Appointment(
                LocalDateTime.of(2024, 6, 10, 6, 0),
                LocalDateTime.of(2024, 6, 16, 18, 0),
                null, null, AppointmentStatus.SCHEDULED, new Customer(userId, "Cesar", "1234", new ArrayList<>()), null);
        Appointment newAppointment = new Appointment(
                LocalDateTime.of(2024, 6, 17, 6, 0),
                LocalDateTime.of(2024, 6, 17, 18, 0),
                null, null, AppointmentStatus.SCHEDULED, new Customer(2, "Adri", "1111", new ArrayList<>()), null);
        when(appointmentRepository.getOne(oldAppointmentId)).thenReturn(oldAppointment);
        when(appointmentRepository.getOne(newAppointmentId)).thenReturn(newAppointment);
        boolean result = exchangeService.checkIfExchangeIsPossible(oldAppointmentId, newAppointmentId, userId);
        assertTrue(result);
    }

    /**
     * Crea un stub para exchangeRequestRepository.getOne
     * que devuelva un exchangeRequest que contiene una cita (Appointment)
     * en el método getRequestor.
     * Verifica que se invoca exchangeRequestRepository.save capturando
     * al exchangeRequest y comprobando que se le ha establecido un status
     * rechazado (REJECTED).
     * Verfifica se invoca al método con el exchangeRequest del stub.
     */
    @Test
     void rejectExchangeTest() {
        AppointmentRepository appointmentRepository = Mockito.mock(AppointmentRepository.class);
        NotificationService notificationService = Mockito.mock(NotificationService.class);
        ExchangeRequestRepository exchangeRequestRepository = Mockito.mock(ExchangeRequestRepository.class);
        ExchangeServiceImpl exchangeService = new ExchangeServiceImpl(appointmentRepository, notificationService, exchangeRequestRepository);
        int exchangeId = 1;
        Appointment requestor = new Appointment();
        requestor.setId(1);
        ExchangeRequest exchangeRequest = new ExchangeRequest();
        exchangeRequest.setId(exchangeId);
        exchangeRequest.setRequestor(requestor);
        exchangeRequest.setStatus(ExchangeStatus.PENDING);
        Mockito.when(exchangeRequestRepository.getOne(exchangeId)).thenReturn(exchangeRequest);
        exchangeService.rejectExchange(exchangeId);
        ArgumentCaptor<ExchangeRequest> exchangeRequestCaptor = ArgumentCaptor.forClass(ExchangeRequest.class);
        Mockito.verify(exchangeRequestRepository).save(exchangeRequestCaptor.capture());
        ExchangeRequest capturedExchangeRequest = exchangeRequestCaptor.getValue();
        assertEquals(ExchangeStatus.REJECTED, capturedExchangeRequest.getStatus());
        Mockito.verify(exchangeRequestRepository).save(exchangeRequest);
     }

}
