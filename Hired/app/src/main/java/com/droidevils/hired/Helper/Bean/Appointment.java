package com.droidevils.hired.Helper.Bean;

public class Appointment {


    /*
    Check whether all function are working properly before integrating to GUI
    Use Helper/Bean/TempMain class for testing purpose. It always run when application starts
     */

    private String serviveProviderId;
    private String serviveProviderName;
    private String serviveRecieverId;
    private String serviveRecieverName;
    private String serviveId;
    private String serviveName;
    private String time;
    private String date;
    private String status;
    private String comment;

    public Appointment() {
    }

    public Appointment(String serviveProviderId, String serviveProviderName, String serviveRecieverId, String serviveRecieverName, String serviveId, String serviveName, String time, String date, String status, String comment) {
        this.serviveProviderId = serviveProviderId;
        this.serviveProviderName = serviveProviderName;
        this.serviveRecieverId = serviveRecieverId;
        this.serviveRecieverName = serviveRecieverName;
        this.serviveId = serviveId;
        this.serviveName = serviveName;
        this.time = time;
        this.date = date;
        this.status = status; //Pending While creating
        this.comment = comment;
    }

    public static void getAllAppointment(AppointmentInterface appointmentInterface){

        //Retrieve All Appointment Info and pass it to appointmentInterface.getAllAppointment(ArrayList<Appointment>)

    }

    public static void getAppointmentById( String serviceProviderId, String serviceReceiverId, String serviceId, AppointmentInterface appointmentInterface ){

        //Retrieve Appointment Info and pass it to appointmentInterface.getAppointmentById(Appointment)

    }

    public void addAppointment(AppointmentInterface appointmentInterface){

        //Add Information to firebase to return task.isSuccessful() to appointmentInterface.getResultBoolean(Boolean);

    }

    public void deleteAppointment(AppointmentInterface appointmentInterface){

        //Remove Information to firebase to return task.isSuccessful() to appointmentInterface.getResultBoolean(Boolean);

    }

    public void respondToAppointment(String response, AppointmentInterface appointmentInterface ){

        //Update Appointment->uniqueId->status to response and return task.isSuccessful() to appointmentInterface.getResultBoolean(Boolean);

    }


    public String getServiveProviderId() {
        return serviveProviderId;
    }

    public void setServiveProviderId(String serviveProviderId) {
        this.serviveProviderId = serviveProviderId;
    }

    public String getServiveProviderName() {
        return serviveProviderName;
    }

    public void setServiveProviderName(String serviveProviderName) {
        this.serviveProviderName = serviveProviderName;
    }

    public String getServiveRecieverId() {
        return serviveRecieverId;
    }

    public void setServiveRecieverId(String serviveRecieverId) {
        this.serviveRecieverId = serviveRecieverId;
    }

    public String getServiveRecieverName() {
        return serviveRecieverName;
    }

    public void setServiveRecieverName(String serviveRecieverName) {
        this.serviveRecieverName = serviveRecieverName;
    }

    public String getServiveId() {
        return serviveId;
    }

    public void setServiveId(String serviveId) {
        this.serviveId = serviveId;
    }

    public String getServiveName() {
        return serviveName;
    }

    public void setServiveName(String serviveName) {
        this.serviveName = serviveName;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
