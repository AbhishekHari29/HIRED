package com.droidevils.hired.Helper.Bean;

import java.util.ArrayList;

public interface AvailableServiceInterface {
    default void getBooleanResult(Boolean result){};
    default void getService(AvailableService service){};
    default void getServiceArrayList(ArrayList<AvailableService> services){};
}
