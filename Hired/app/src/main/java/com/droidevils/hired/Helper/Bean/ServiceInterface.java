package com.droidevils.hired.Helper.Bean;

import java.util.ArrayList;

public interface ServiceInterface {
    public default void getBooleanResult(Boolean result){};
    public default void getService(Service service){};
    public default void getServiceArrayList(ArrayList<Service> services){};
}
