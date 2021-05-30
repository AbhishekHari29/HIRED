package com.droidevils.hired.Helper.Adapter;

public class AvailableServiceHelper {

    private int serviceImage;
    private String userId;
    private String userName;
    private String serviceId;
    private String serviceName;
    private float serviceRating;
    private boolean availability;
    private String timeFrom;
    private String timeTo;
    private String workingDays;
    private double distance;

    public AvailableServiceHelper(int serviceImage, String userId, String userName, String serviceId, String serviceName, float serviceRating, boolean availability, String timeFrom, String timeTo, String workingDays) {
        this.serviceImage = serviceImage;
        this.userId = userId;
        this.userName = userName;
        this.serviceId = serviceId;
        this.serviceName = serviceName;
        this.serviceRating = serviceRating;
        this.availability = availability;
        this.timeFrom = timeFrom;
        this.timeTo = timeTo;
        this.workingDays = workingDays;
        this.distance = Double.MAX_VALUE;
    }

    public int getServiceImage() {
        return serviceImage;
    }

    public String getServiceId() {
        return serviceId;
    }

    public String getServiceName() {
        return serviceName;
    }

    public String getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public float getServiceRating() {
        return serviceRating;
    }

    public boolean isAvailability() {
        return availability;
    }

    public String getTimeFrom() {
        return timeFrom;
    }

    public String getTimeTo() {
        return timeTo;
    }

    public String getWorkingDays() {
        return workingDays;
    }

    public void setAvailability(boolean availability) {
        this.availability = availability;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }
}
