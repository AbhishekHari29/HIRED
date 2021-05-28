package com.droidevils.hired.Helper.Bean;

import java.util.ArrayList;

public interface AppointmentInterface {
    public default void getBooleanResult(Boolean result){};
    public default void getAppointmentById(Appointment appointment){};
    public default void getAppointmentArrayList(ArrayList<Appointment> appointments){};

}
