package com.droidevils.hired.Helper.Bean;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Service {

    private String serviceId;
    private String serviceName;
    private String description;
    private String categoryId;
    private int count;

    public Service() {
    }

    public Service(String serviceId, String serviceName, String description, String categoryId, int count) {
        this.serviceId = serviceId;
        this.serviceName = serviceName;
        this.description = description;
        this.categoryId = categoryId;
        this.count = count;
    }

    public static void getAllService(ServiceInterface serviceInterface) {
        FirebaseDatabase rootNode = FirebaseDatabase.getInstance();
        DatabaseReference reference = rootNode.getReference("Service");
        Log.i("MESSAGE", "Before Reading from firebase");
        reference.orderByKey().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.i("MESSAGE", "Found Data");
                ArrayList<Service> services = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Log.i("MESSAGE", "Iterating Data");
                    Service service = dataSnapshot.getValue(Service.class);
                    services.add(service);
                }
                serviceInterface.getAllService(services);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                serviceInterface.getAllService(null);
            }
        });
        Log.i("MESSAGE", "After Reading from firebase");
    }

    public static void getServiceByCategory(String categoryId, ServiceInterface serviceInterface) {
        Log.i("MESSAGE", "Before Reading from firebase");
        FirebaseDatabase rootNode = FirebaseDatabase.getInstance();
        DatabaseReference reference = rootNode.getReference("Service");
        reference.orderByChild("categoryId").equalTo(categoryId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.i("MESSAGE", "Found Data");
                ArrayList<Service> services = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Log.i("MESSAGE", "Iterating Data");
                    Service service = dataSnapshot.getValue(Service.class);
                    services.add(service);
                }
                serviceInterface.getServiceByCategory(services);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                serviceInterface.getAllService(null);
            }
        });
        Log.i("MESSAGE", "After Reading from firebase");
    }

    public static void getServiceByName(String serviceName, ServiceInterface serviceInterface) {
        FirebaseDatabase rootNode = FirebaseDatabase.getInstance();
        DatabaseReference reference = rootNode.getReference("Service");
        reference.orderByChild("serviceName").equalTo(serviceName).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<Service> services = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Service service = dataSnapshot.getValue(Service.class);
                    services.add(service);
                }
                serviceInterface.getServiceByName(services);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                serviceInterface.getServiceById(null);
            }
        });
    }

    public static void getServiceById(String serviceId, ServiceInterface serviceInterface) {
        FirebaseDatabase rootNode = FirebaseDatabase.getInstance();
        DatabaseReference reference = rootNode.getReference("Service");
        reference.child(serviceId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Service service = snapshot.getValue(Service.class);
                serviceInterface.getServiceById(service);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                serviceInterface.getServiceById(null);
            }
        });
    }

    public boolean addService() {
        final boolean[] result = {false};
        FirebaseDatabase rootNode = FirebaseDatabase.getInstance();
        DatabaseReference reference = rootNode.getReference("Service");
        reference.child(serviceId).setValue(this).addOnCompleteListener(task -> {
            result[0] = task.isSuccessful();
        });
        return result[0];
    }

    public boolean deleteService() {
        final boolean[] result = new boolean[1];
        FirebaseDatabase rootNode = FirebaseDatabase.getInstance();
        DatabaseReference reference = rootNode.getReference("Service");
        reference.child(serviceId).removeValue().addOnCompleteListener(task -> {
            result[0] = task.isSuccessful();
        });
        return result[0];
    }

    public boolean updateService() {
        return addService();
    }

    public boolean incrementServiceCount() {

        // TODO Ensure Concurrent Operation
        final boolean[] result = new boolean[1];
        FirebaseDatabase rootNode = FirebaseDatabase.getInstance();
        DatabaseReference reference = rootNode.getReference("Service");
        reference.child(serviceId).child("count").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                count = (int) snapshot.getValue(Integer.class);
                snapshot.getRef().setValue(++count).addOnCompleteListener(task -> result[0] = task.isSuccessful());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                result[0] = false;
            }
        });
        return result[0];
    }

    public boolean decrementServiceCount() {

        // TODO Ensure Concurrent Operation
        final boolean[] result = new boolean[1];
        FirebaseDatabase rootNode = FirebaseDatabase.getInstance();
        DatabaseReference reference = rootNode.getReference("Service");
        reference.child(serviceId).child("count").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                count = (int) snapshot.getValue(Integer.class);
                snapshot.getRef().setValue(--count).addOnCompleteListener(task -> result[0] = task.isSuccessful());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                result[0] = false;
            }
        });
        return result[0];
    }


    public void displayService() {
        Log.i("MESSAGE","---------------------------------------------");
        Log.i("MESSAGE","ServiceID:\t" + serviceId);
        Log.i("MESSAGE","ServiceName:\t" + serviceName);
        Log.i("MESSAGE","Description:\t" + description);
        Log.i("MESSAGE","CategoryID:\t" + categoryId);
        Log.i("MESSAGE","Count:\t" + count);
        Log.i("MESSAGE","---------------------------------------------");
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }
}

