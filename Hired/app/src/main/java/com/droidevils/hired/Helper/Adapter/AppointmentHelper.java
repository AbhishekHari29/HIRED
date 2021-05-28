package com.droidevils.hired.Helper.Adapter;

public class AppointmentHelper {

    private String userId;
    private String userName;
    private String serviceId;
    private String serviceName;
    private String time;
    private String date;
    private String status;
    private String comment;

    public AppointmentHelper(String userId, String userName, String serviceId, String serviceName, String time, String date, String status, String comment) {
        this.userId = userId;
        this.userName = userName;
        this.serviceId = serviceId;
        this.serviceName = serviceName;
        this.time = time;
        this.date = date;
        this.status = status;
        this.comment = comment;
    }

    public String getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public String getServiceId() {
        return serviceId;
    }

    public String getServiceName() {
        return serviceName;
    }

    public String getTime() {
        return time;
    }

    public String getDate() {
        return date;
    }

    public String getStatus() {
        return status;
    }

    public String getComment() {
        return comment;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
