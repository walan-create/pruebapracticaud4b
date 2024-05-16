package org.iesvdm.appointment.entity;


public class User extends BaseEntity {

    private String userName;

    private String password;

    public User() {
        super();
    }
    public User(Integer id, String userName, String password) {
        super(id);
        this.userName = userName;
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
