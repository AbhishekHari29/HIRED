package com.droidevils.hired.Helper.Bean;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AvailableService {

    private String userId;
    private String userName;
    private String serviceId;
    private String serviceName;
    private boolean availability;
    private String timeFrom;
    private String timeTo;
    private String workingDays;
    private float rating;

    public AvailableService() {
    }

    public AvailableService(String userId, String userName, String serviceId, String serviceName, boolean availability, String timeFrom, String timeTo, String workingDays, float rating) {
        this.userId = userId;
        this.userName = userName;
        this.serviceId = serviceId;
        this.serviceName = serviceName;
        this.availability = availability;
        this.timeFrom = timeFrom;
        this.timeTo = timeTo;
        this.workingDays = workingDays;
        this.rating = rating;
    }

    public static void getAllService(AvailableServiceInterface serviceInterface) {
        FirebaseDatabase rootNode = FirebaseDatabase.getInstance();
        DatabaseReference reference = rootNode.getReference("AvailableService");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<AvailableService> services = new ArrayList<>();
                for (DataSnapshot serviceSnapshot : snapshot.getChildren()) {
                    for (DataSnapshot userSnapshot : serviceSnapshot.getChildren()) {
                        AvailableService service = userSnapshot.getValue(AvailableService.class);
                        services.add(service);
                    }
                }
                serviceInterface.getServiceArrayList(services);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                serviceInterface.getServiceArrayList(null);
            }
        });
    }

    public static void getServiceByName(String serviceName, AvailableServiceInterface serviceInterface) {
        ArrayList<AvailableService> availableServices = new ArrayList<>();
        Service.getServiceByName(serviceName, new ServiceInterface() {
            @Override
            public void getServiceArrayList(ArrayList<Service> services) {
                if (services != null && services.size() > 0) {
                    FirebaseDatabase rootNode = FirebaseDatabase.getInstance();
                    DatabaseReference reference = rootNode.getReference("AvailableService");
                    reference.child(services.get(0).getServiceId()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                AvailableService service = dataSnapshot.getValue(AvailableService.class);
                                availableServices.add(service);
                            }
                            serviceInterface.getServiceArrayList(availableServices);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            serviceInterface.getServiceArrayList(null);
                        }
                    });
                } else {
                    serviceInterface.getServiceArrayList(null);

                }
            }
        });
    }

    public static void getServiceByUser(String userId, AvailableServiceInterface serviceInterface) {
        FirebaseDatabase rootNode = FirebaseDatabase.getInstance();
        DatabaseReference reference = rootNode.getReference("AvailableService");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<AvailableService> services = new ArrayList<>();
                for (DataSnapshot serviceSnapshot : snapshot.getChildren()) {
                    for (DataSnapshot userSnapshot : serviceSnapshot.getChildren()) {
                        AvailableService service = userSnapshot.getValue(AvailableService.class);
                        if (service.getUserId().equals(userId))
                            services.add(service);
                    }
                }
                serviceInterface.getServiceArrayList(services);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                serviceInterface.getServiceArrayList(null);
            }
        });
    }

    // TODO Prints all... Doesn't filter
    public static void getServiceByCategory(String categoryId, AvailableServiceInterface serviceInterface) {
        FirebaseDatabase rootNode = FirebaseDatabase.getInstance();
        DatabaseReference reference = rootNode.getReference("AvailableService");
        reference.orderByKey().startAt(categoryId).endAt(categoryId + "\uf8ff").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<AvailableService> services = new ArrayList<>();
                for (DataSnapshot serviceSnapshot : snapshot.getChildren()) {
                    for (DataSnapshot userSnapshot : serviceSnapshot.getChildren()) {
                        AvailableService service = userSnapshot.getValue(AvailableService.class);
                        services.add(service);
                    }
                }
                serviceInterface.getServiceArrayList(services);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                serviceInterface.getServiceArrayList(null);
            }
        });
    }

    public static void getServiceById(String userId, String serviceId, AvailableServiceInterface serviceInterface) {
        FirebaseDatabase rootNode = FirebaseDatabase.getInstance();
        DatabaseReference reference = rootNode.getReference("AvailableService");
        reference.child(serviceId).child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                AvailableService service = snapshot.getValue(AvailableService.class);
                serviceInterface.getService(service);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                serviceInterface.getService(null);
            }
        });
    }

    public void addService(AvailableServiceInterface serviceInterface) {

        updateService(new AvailableServiceInterface() {
            @Override
            public void getBooleanResult(Boolean result) {
                if (result) {
                    Log.i("MESSAGE", "Service Added: " + result);
                    Service service = new Service();
                    service.setServiceId(serviceId);
                    service.incrementServiceCount(new ServiceInterface() {
                        @Override
                        public void getBooleanResult(Boolean result) {
                            Log.i("MESSAGE", "Service Incremented: " + result);
                            serviceInterface.getBooleanResult(result);
                        }
                    });
                } else {
                    serviceInterface.getBooleanResult(false);
                }
            }
        });
    }

    public void deleteService(AvailableServiceInterface serviceInterface) {
        FirebaseDatabase rootNode = FirebaseDatabase.getInstance();
        DatabaseReference reference = rootNode.getReference("AvailableService");
        reference.child(serviceId).child(userId).removeValue().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Service service = new Service();
                service.setServiceId(serviceId);
                service.decrementServiceCount(new ServiceInterface() {
                    @Override
                    public void getBooleanResult(Boolean result) {
                        serviceInterface.getBooleanResult(result);
                    }
                });
            } else {
                serviceInterface.getBooleanResult(false);
            }
        });
    }

    public void updateService(AvailableServiceInterface serviceInterface) {
        FirebaseDatabase rootNode = FirebaseDatabase.getInstance();
        DatabaseReference reference = rootNode.getReference("AvailableService");
        reference.child(serviceId).child(userId).setValue(this).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                serviceInterface.getBooleanResult(task.isSuccessful());
            }
        });
    }

    public void enableAvailability(AvailableServiceInterface serviceInterface) {
        FirebaseDatabase rootNode = FirebaseDatabase.getInstance();
        DatabaseReference reference = rootNode.getReference("AvailableService");
        reference.child(serviceId).child(userId).child("availability").setValue(true).addOnCompleteListener(task -> {
            serviceInterface.getBooleanResult(task.isSuccessful());
        });
    }

    public void disableAvailability(AvailableServiceInterface serviceInterface) {
        FirebaseDatabase rootNode = FirebaseDatabase.getInstance();
        DatabaseReference reference = rootNode.getReference("AvailableService");
        reference.child(serviceId).child(userId).child("availability").setValue(false).addOnCompleteListener(task -> {
            serviceInterface.getBooleanResult(task.isSuccessful());
        });
    }

    public void displayService() {
        Log.i("MESSAGE", "---------------------------------------------");
        Log.i("MESSAGE", "UserID:\t" + userId);
        Log.i("MESSAGE", "UserName:\t" + userName);
        Log.i("MESSAGE", "ServiceID:\t" + serviceId);
        Log.i("MESSAGE", "ServiceName:\t" + serviceId);
        Log.i("MESSAGE", "Availability:\t" + availability);
        Log.i("MESSAGE", "Time From:\t" + timeFrom);
        Log.i("MESSAGE", "Time To:\t" + timeTo);
        Log.i("MESSAGE", "WorkingDays:\t" + workingDays);
        Log.i("MESSAGE", "Rating:\t" + rating);
        Log.i("MESSAGE", "---------------------------------------------");
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public boolean isAvailability() {
        return availability;
    }

    public void setAvailability(boolean availability) {
        this.availability = availability;
    }

    public String getTimeFrom() {
        return timeFrom;
    }

    public void setTimeFrom(String timeFrom) {
        this.timeFrom = timeFrom;
    }

    public String getTimeTo() {
        return timeTo;
    }

    public void setTimeTo(String timeTo) {
        this.timeTo = timeTo;
    }

    public String getWorkingDays() {
        return workingDays;
    }

    public void setWorkingDays(String workingDays) {
        this.workingDays = workingDays;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }
}

