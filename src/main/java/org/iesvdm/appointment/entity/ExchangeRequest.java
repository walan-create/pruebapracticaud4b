package org.iesvdm.appointment.entity;

public class ExchangeRequest extends BaseEntity {


    private ExchangeStatus status;


    private Appointment requestor;


    private Appointment requested;


    public ExchangeRequest() {

    }

    public ExchangeRequest(Appointment requestor, Appointment requested, ExchangeStatus status) {
        this.status = status;
        this.requestor = requestor;
        this.requested = requested;
    }

    public ExchangeStatus getStatus() {
        return status;
    }

    public void setStatus(ExchangeStatus status) {
        this.status = status;
    }

    public Appointment getRequestor() {
        return requestor;
    }

    public void setRequestor(Appointment requestor) {
        this.requestor = requestor;
    }

    public Appointment getRequested() {
        return requested;
    }

    public void setRequested(Appointment requested) {
        this.requested = requested;
    }
}
