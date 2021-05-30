package com.droidevils.hired.Admin;

import android.util.Log;

import com.droidevils.hired.Helper.Bean.AvailableService;
import com.droidevils.hired.Helper.Bean.AvailableServiceInterface;
import com.droidevils.hired.Helper.Bean.Service;
import com.droidevils.hired.Helper.Bean.ServiceInterface;

import java.util.ArrayList;

public class TempMain {

    public void main() {



    }

    private void countAvailableService() {

        Service.getAllService(new ServiceInterface() {
            @Override
            public void getServiceArrayList(ArrayList<Service> services) {
                if (services != null) {
                    for (Service service : services) {
                        service.setCount(0);
                    }
                    ArrayList<Service> myServices = new ArrayList<>(services);
                    AvailableService.getAllService(new AvailableServiceInterface() {
                        @Override
                        public void getServiceArrayList(ArrayList<AvailableService> services) {
                            if (services != null) {
                                for (AvailableService availableService : services) {
                                    for (Service service : myServices)
                                        if (availableService.getServiceId().equals(service.getServiceId())) {
                                            service.count++;
                                            break;
                                        }
                                }
                                for (Service service : myServices) {
                                    service.updateService(new ServiceInterface() {
                                        @Override
                                        public void getBooleanResult(Boolean result) {
                                            Log.i("MESSAGE: ", "Service count: " + service.getCount());
                                        }
                                    });
                                }
                            }
                        }
                    });

                }
            }
        });

    }
}


