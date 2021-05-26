package com.droidevils.hired.Helper;

public class AvailableServiceHelper {

    private int serviceImage;
    private String serviceId;
    private String serviceName;
    private float serviceRating;
    private boolean availability;
    private String timeFrom;
    private String timeTo;
    private String workingDays;

    public AvailableServiceHelper(int serviceImage, String serviceId, String serviceName, float serviceRating, boolean availability, String timeFrom, String timeTo, String workingDays) {
        this.serviceImage = serviceImage;
        this.serviceId = serviceId;
        this.serviceName = serviceName;
        this.serviceRating = serviceRating;
        this.availability = availability;
        this.timeFrom = timeFrom;
        this.timeTo = timeTo;
        this.workingDays = workingDays;
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
}
