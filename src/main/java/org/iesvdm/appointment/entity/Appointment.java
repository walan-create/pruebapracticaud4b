package org.iesvdm.appointment.entity;


import java.time.LocalDateTime;

public class Appointment extends BaseEntity implements Comparable<Appointment> {

    private LocalDateTime start;

    private LocalDateTime end;

    private LocalDateTime canceledAt;

    private User canceler;

    private AppointmentStatus status;

    private Customer customer;


    private ExchangeRequest exchangeRequest;

    public Appointment() {
    }

    public Appointment(LocalDateTime start, LocalDateTime end, Customer customer) {
        this.start = start;
        this.end = end;
        this.customer = customer;
    }

    public Appointment(LocalDateTime start, LocalDateTime end, LocalDateTime canceledAt, User canceler, AppointmentStatus status, Customer customer, ExchangeRequest exchangeRequest) {
        this.start = start;
        this.end = end;
        this.canceledAt = canceledAt;
        this.canceler = canceler;
        this.status = status;
        this.customer = customer;
        this.exchangeRequest = exchangeRequest;
    }

    @Override
    public int compareTo(Appointment o) {
        return this.getStart().compareTo(o.getStart());
    }

    public LocalDateTime getStart() {
        return start;
    }

    public void setStart(LocalDateTime start) {
        this.start = start;
    }

    public LocalDateTime getEnd() {
        return end;
    }

    public void setEnd(LocalDateTime end) {
        this.end = end;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public AppointmentStatus getStatus() {
        return status;
    }

    public void setStatus(AppointmentStatus status) {
        this.status = status;
    }


    public User getCanceler() {
        return canceler;
    }

    public void setCanceler(User canceler) {
        this.canceler = canceler;
    }

    public LocalDateTime getCanceledAt() {
        return canceledAt;
    }

    public void setCanceledAt(LocalDateTime canceledAt) {
        this.canceledAt = canceledAt;
    }

    public ExchangeRequest getExchangeRequest() {
        return exchangeRequest;
    }

    public void setExchangeRequest(ExchangeRequest exchangeRequest) {
        this.exchangeRequest = exchangeRequest;
    }
}
