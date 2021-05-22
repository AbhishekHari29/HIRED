package com.droidevils.hired.Helper.Bean;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AvailableService {

    private String userId;
    private String serviceId;
    private boolean availability;
    private String timeFrom;
    private String timeTo;
    private String workingDays;
    private float rating;

    public AvailableService() {
    }

    public AvailableService(String userId, String serviceId, boolean availability, String timeFrom, String timeTo, String workingDays, float rating) {
        this.userId = userId;
        this.serviceId = serviceId;
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
                serviceInterface.getAllService(services);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                serviceInterface.getAllService(null);
            }
        });
    }

    public static void getServiceByName(String serviceName, AvailableServiceInterface serviceInterface) {
        ArrayList<AvailableService> availableServices = new ArrayList<>();
        Service.getServiceByName(serviceName, new ServiceInterface() {
            @Override
            public void getServiceByName(ArrayList<Service> services) {
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
                            serviceInterface.getServiceByName(availableServices);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            serviceInterface.getServiceByName(null);
                        }
                    });
                } else {
                    serviceInterface.getServiceByName(null);

                }
            }
        });
    }

    // TODO Doesn't work
    public static void getServiceByUser(String userId, AvailableServiceInterface serviceInterface) {
        FirebaseDatabase rootNode = FirebaseDatabase.getInstance();
        DatabaseReference reference = rootNode.getReference("AvailableService");
        reference.orderByValue().equalTo(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<AvailableService> services = new ArrayList<>();
                for (DataSnapshot serviceSnapshot : snapshot.getChildren()) {
                    for (DataSnapshot userSnapshot : serviceSnapshot.getChildren()) {
                        AvailableService service = userSnapshot.getValue(AvailableService.class);
                        services.add(service);
                    }
                }
                serviceInterface.getServiceByUser(services);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                serviceInterface.getServiceByUser(null);
            }
        });
    }

    // TODO Prints all... Doesn't filter
    public static void getServiceByCategory(String categoryId, AvailableServiceInterface serviceInterface) {
        FirebaseDatabase rootNode = FirebaseDatabase.getInstance();
        DatabaseReference reference = rootNode.getReference("AvailableService");
        reference.orderByKey().startAt(categoryId).endAt(categoryId+"\uf8ff").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<AvailableService> services = new ArrayList<>();
                for (DataSnapshot serviceSnapshot : snapshot.getChildren()) {
                    for (DataSnapshot userSnapshot : serviceSnapshot.getChildren()) {
                        AvailableService service = userSnapshot.getValue(AvailableService.class);
                        services.add(service);
                    }
                }
                serviceInterface.getServiceByCategory(services);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                serviceInterface.getServiceByCategory(null);
            }
        });
    }

    public static void getServiceById(String userId, String serviceId, AvailableServiceInterface serviceInterface) {
        FirebaseDatabase rootNode = FirebaseDatabase.getInstance();
        DatabaseReference reference = rootNode.getReference("AvailableService");
//        reference.child(serviceId).child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
        reference.child(serviceId).child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                AvailableService service = snapshot.getValue(AvailableService.class);
                serviceInterface.getServiceById(service);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                serviceInterface.getServiceById(null);
            }
        });
    }

    public boolean addService() {

        boolean result = updateService();
        Service.getServiceById(serviceId, new ServiceInterface() {
            @Override
            public void getServiceById(Service service) {
                service.incrementServiceCount();
                Log.i("MESSAGE", "Service count incremented");
            }
        });
        return result;


//        final boolean[] result = {false};
//        Log.i("MESSAGE", "Starting Add Service");
//        FirebaseDatabase rootNode = FirebaseDatabase.getInstance();
//        Log.i("MESSAGE", "Initiating Firebase");
//        DatabaseReference reference = rootNode.getReference("AvailableService");
//        Log.i("MESSAGE", "Getting Reference");
//        reference.child(serviceId).child(userId).setValue(this).addOnCompleteListener(task -> {
//            Log.i("MESSAGE", "Inside Event Listener");
//            if (task.isSuccessful()){
//                Log.i("MESSAGE", "Service Added");
//            }else {
//                Log.i("MESSAGE", "Service Not Added");
//            }
//            result[0] = task.isSuccessful();
//            Service.getServiceById(serviceId, new ServiceInterface() {
//                @Override
//                public void getServiceById(Service service) {
//                    service.incrementServiceCount();
//                    Log.i("MESSAGE", "Service count incremented");
//                }
//            });
//        });
//        Log.i("MESSAGE", "Starting Add Service");
//        return result[0];
    }

    public boolean deleteService() {
        final boolean[] result = new boolean[1];
        FirebaseDatabase rootNode = FirebaseDatabase.getInstance();
        DatabaseReference reference = rootNode.getReference("AvailableService");
        reference.child(serviceId).child(userId).removeValue().addOnCompleteListener(task -> {
            result[0] = task.isSuccessful();
            Service.getServiceById(serviceId, new ServiceInterface() {
                @Override
                public void getServiceById(Service service) {
                    service.decrementServiceCount();
                }
            });
        });
        return result[0];
    }

    public boolean updateService() {
        final boolean[] result = {false};
        FirebaseDatabase rootNode = FirebaseDatabase.getInstance();
        DatabaseReference reference = rootNode.getReference("AvailableService");
        reference.child(serviceId).child(userId).setValue(this).addOnCompleteListener(task -> {
            result[0] = task.isSuccessful();
        });
        return result[0];
    }

    public boolean enableAvailability() {
        final boolean[] result = {false};
        FirebaseDatabase rootNode = FirebaseDatabase.getInstance();
        DatabaseReference reference = rootNode.getReference("AvailableService");
        reference.child(serviceId).child(userId).child("availability").setValue(true).addOnCompleteListener(task -> {
            result[0] = task.isSuccessful();
        });
        return result[0];
    }

    public boolean disableAvailability() {
        final boolean[] result = {false};
        FirebaseDatabase rootNode = FirebaseDatabase.getInstance();
        DatabaseReference reference = rootNode.getReference("AvailableService");
        reference.child(serviceId).child(userId).child("availability").setValue(false).addOnCompleteListener(task -> {
            result[0] = task.isSuccessful();
        });
        return result[0];
    }

    public void displayService() {
        Log.i("MESSAGE", "---------------------------------------------");
        Log.i("MESSAGE", "UserID:\t" + userId);
        Log.i("MESSAGE", "ServiceID:\t" + serviceId);
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

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
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

