package com.droidevils.hired.Helper.Bean;

import java.util.ArrayList;

public interface AvailableServiceInterface {
    public default void getBooleanResult(Boolean result){};
    public default void getServiceById(AvailableService service){};
    public default void getServiceByCategory(ArrayList<AvailableService> services){};
    public default void getServiceByUser(ArrayList<AvailableService> services){};
    public default void getServiceByName(ArrayList<AvailableService> services){};
    public default void getAllService(ArrayList<AvailableService> services){};
}
