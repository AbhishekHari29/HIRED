package com.droidevils.hired.Helper.Bean;

import java.util.HashMap;

public interface UserLocationInterface {
    default void getLocation(UserLocation userLocation){};
    default void getLocationHashMap(HashMap<String, UserLocation> locationMap){};
}
