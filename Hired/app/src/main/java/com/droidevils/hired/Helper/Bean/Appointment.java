package com.droidevils.hired.Helper.Bean;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class Appointment {

    public static final String PENDING = "Pending";
    public static final String ACCEPT = "Accept";
    public static final String REJECT = "Reject";

    private String serviceProviderId;
    private String serviceProviderName;
    private String serviceReceiverId;
    private String serviceReceiverName;
    private String serviceId;
    private String serviceName;
    private String time;
    private String date;
    private String status;
    private String comment;

    public Appointment() {
    }

    public Appointment(String serviceProviderId, String serviceProviderName, String serviceReceiverId, String serviceReceiverName, String serviceId, String serviceName, String time, String date, String status, String comment) {
        this.serviceProviderId = serviceProviderId;
        this.serviceProviderName = serviceProviderName;
        this.serviceReceiverId = serviceReceiverId;
        this.serviceReceiverName = serviceReceiverName;
        this.serviceId = serviceId;
        this.serviceName = serviceName;
        this.time = time;
        this.date = date;
        this.status = status;
        this.comment = comment;
    }

    public static void getAllAppointment(AppointmentInterface appointmentInterface) {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Appointment");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if (snapshot != null) {
                    ArrayList<Appointment> appointments = new ArrayList<>();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        appointments.add(dataSnapshot.getValue(Appointment.class));
                    }
                    appointmentInterface.getAllAppointment(appointments);
                } else {
                    appointmentInterface.getAllAppointment(null);
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }

    public static void getAppointmentById(String serviceProviderId, String serviceReceiverId, String serviceId, AppointmentInterface appointmentInterface) {


        String uniqueId = serviceProviderId + "::" + serviceId + "::" + serviceReceiverId;
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Appointment");
        reference.child(uniqueId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                appointmentInterface.getAppointmentById(snapshot.getValue(Appointment.class));
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }

    public void addAppointment(AppointmentInterface appointmentInterface) {

        String uniqueId = serviceProviderId + "::" + serviceId + "::" + serviceReceiverId;
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Appointment");
        reference.child(uniqueId).setValue(this).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<Void> task) {
                appointmentInterface.getBooleanResult(task.isSuccessful());
            }
        });
    }

    public void deleteAppointment(AppointmentInterface appointmentInterface) {

        String uniqueId = serviceProviderId + "::" + serviceId + "::" + serviceReceiverId;
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Appointment");
        reference.child(uniqueId).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<Void> task) {
                appointmentInterface.getBooleanResult(task.isSuccessful());
            }
        });
    }

    public void respondToAppointment(String response, AppointmentInterface appointmentInterface) {

        String uniqueId = serviceProviderId + "::" + serviceId + "::" + serviceReceiverId;
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Appointment");
        reference.child(uniqueId).child("status").setValue(response).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<Void> task) {
                appointmentInterface.getBooleanResult(task.isSuccessful());
            }
        });
    }

    public String getServiceProviderId() {
        return serviceProviderId;
    }

    public void setServiceProviderId(String serviceProviderId) {
        this.serviceProviderId = serviceProviderId;
    }

    public String getServiceProviderName() {
        return serviceProviderName;
    }

    public void setServiceProviderName(String serviceProviderName) {
        this.serviceProviderName = serviceProviderName;
    }

    public String getServiceReceiverId() {
        return serviceReceiverId;
    }

    public void setServiceReceiverId(String serviceReceiverId) {
        this.serviceReceiverId = serviceReceiverId;
    }

    public String getServiceReceiverName() {
        return serviceReceiverName;
    }

    public void setServiceReceiverName(String serviceReceiverName) {
        this.serviceReceiverName = serviceReceiverName;
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

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
