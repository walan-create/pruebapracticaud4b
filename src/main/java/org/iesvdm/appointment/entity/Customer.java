package org.iesvdm.appointment.entity;


import java.util.List;


public class Customer extends User {

    private List<Appointment> appointments;

    public Customer() {
        super();
    }

    public Customer(Integer id, String userName, String password, List<Appointment> appointments) {
        super(id, userName, password);
        this.appointments = appointments;
    }

    public List<Appointment> getAppointments() {
        return appointments;
    }

    public void setAppointments(List<Appointment> appointments) {
        this.appointments = appointments;
    }
}
