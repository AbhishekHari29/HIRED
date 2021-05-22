package com.droidevils.hired.Helper.Bean;

import java.util.ArrayList;

public interface ServiceInterface {
    public default void getServiceById(Service service){};
    public default void getServiceByName(ArrayList<Service> services){};
    public default void getServiceByCategory(ArrayList<Service> services){};
    public default void getAllService(ArrayList<Service> services){};
}
