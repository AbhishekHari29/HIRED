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
    public int count;

    public Service() {
    }

    public Service(String serviceId, String serviceName, String description, String categoryId, int count) {
        this.serviceId = serviceId;
        this.serviceName = serviceName;
        this.description = description;
        this.categoryId = categoryId;
        this.count = count;
    }

    public static void getTopNService(int number, ServiceInterface serviceInterface){

        FirebaseDatabase rootNode = FirebaseDatabase.getInstance();
        DatabaseReference reference = rootNode.getReference("Service");
        reference.orderByChild("count").limitToLast(number).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<Service> services = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Service service = dataSnapshot.getValue(Service.class);
                    services.add(service);
                }
                serviceInterface.getServiceArrayList(services);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                serviceInterface.getServiceArrayList(null);
            }
        });

    }

    public static void getAllService(ServiceInterface serviceInterface) {
        FirebaseDatabase rootNode = FirebaseDatabase.getInstance();
        DatabaseReference reference = rootNode.getReference("Service");
        reference.orderByKey().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<Service> services = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Service service = dataSnapshot.getValue(Service.class);
                    services.add(service);
                }
                serviceInterface.getServiceArrayList(services);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                serviceInterface.getServiceArrayList(null);
            }
        });
    }

    public static void getServiceByCategory(String categoryId, ServiceInterface serviceInterface) {
        FirebaseDatabase rootNode = FirebaseDatabase.getInstance();
        DatabaseReference reference = rootNode.getReference("Service");
        reference.orderByChild("categoryId").equalTo(categoryId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<Service> services = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Service service = dataSnapshot.getValue(Service.class);
                    services.add(service);
                }
                serviceInterface.getServiceArrayList(services);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                serviceInterface.getServiceArrayList(null);
            }
        });
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
                serviceInterface.getServiceArrayList(services);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                serviceInterface.getService(null);
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
                serviceInterface.getService(service);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                serviceInterface.getService(null);
            }
        });
    }

    public void addService(ServiceInterface serviceInterface) {
        FirebaseDatabase rootNode = FirebaseDatabase.getInstance();
        DatabaseReference reference = rootNode.getReference("Service");
        reference.child(serviceId).setValue(this)
                .addOnCompleteListener(task -> serviceInterface.getBooleanResult(task.isSuccessful()));
    }

    public void deleteService(ServiceInterface serviceInterface) {
        FirebaseDatabase rootNode = FirebaseDatabase.getInstance();
        DatabaseReference reference = rootNode.getReference("Service");
        reference.child(serviceId).removeValue()
                .addOnCompleteListener(task -> serviceInterface.getBooleanResult(task.isSuccessful()));
    }

    public void updateService(ServiceInterface serviceInterface) {
        addService(serviceInterface);
    }

    public void incrementServiceCount(ServiceInterface serviceInterface) {
        FirebaseDatabase rootNode = FirebaseDatabase.getInstance();
        DatabaseReference reference = rootNode.getReference("Service");
        reference.child(serviceId).child("count").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                count = snapshot.getValue(Integer.class);
                snapshot.getRef().setValue(++count).addOnCompleteListener(task -> serviceInterface.getBooleanResult(task.isSuccessful()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                serviceInterface.getBooleanResult(false);
            }
        });
    }

    public void decrementServiceCount(ServiceInterface serviceInterface) {
        FirebaseDatabase rootNode = FirebaseDatabase.getInstance();
        DatabaseReference reference = rootNode.getReference("Service");
        reference.child(serviceId).child("count").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                count = snapshot.getValue(Integer.class);
                snapshot.getRef().setValue(--count).addOnCompleteListener(task -> serviceInterface.getBooleanResult(task.isSuccessful()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                serviceInterface.getBooleanResult(false);
            }
        });
    }


    public void displayService() {
        Log.i("MESSAGE", "---------------------------------------------");
        Log.i("MESSAGE", "ServiceID:\t" + serviceId);
        Log.i("MESSAGE", "ServiceName:\t" + serviceName);
        Log.i("MESSAGE", "Description:\t" + description);
        Log.i("MESSAGE", "CategoryID:\t" + categoryId);
        Log.i("MESSAGE", "Count:\t" + count);
        Log.i("MESSAGE", "---------------------------------------------");
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

