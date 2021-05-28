package com.droidevils.hired.Helper.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.droidevils.hired.Helper.Bean.Appointment;
import com.droidevils.hired.R;

import java.util.ArrayList;

public class AppointmentAdaptor extends ArrayAdapter<AppointmentHelper> {

    Context context;
    ArrayList<AppointmentHelper> appointmentHelpers;

    public AppointmentAdaptor(Context context, ArrayList<AppointmentHelper> appointmentHelpers) {
        super(context, R.layout.appointment_card_design, appointmentHelpers);
        this.context = context;
        this.appointmentHelpers = appointmentHelpers;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        AppointmentHelper appointmentHelper = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.appointment_card_design, parent, false);
        }

        TextView userName = convertView.findViewById(R.id.appointment_user_name);
        TextView serviceName = convertView.findViewById(R.id.appointment_service_name);
        TextView time = convertView.findViewById(R.id.appointment_time);
        TextView date = convertView.findViewById(R.id.appointment_date);
        ImageView status = convertView.findViewById(R.id.appointment_status);

        userName.setText(appointmentHelper.getUserName());
        userName.setContentDescription(appointmentHelper.getUserId());
        serviceName.setText(appointmentHelper.getServiceName());
        serviceName.setContentDescription(appointmentHelper.getServiceId());
        time.setText(appointmentHelper.getTime());
        date.setText(appointmentHelper.getDate());
        switch (appointmentHelper.getStatus()) {
            case Appointment.ACCEPT:
                status.setImageResource(R.drawable.ic_circle_green);
                break;
            case Appointment.REJECT:
                status.setImageResource(R.drawable.ic_circle_red);
                break;
            case Appointment.PENDING:
                status.setImageResource(R.drawable.ic_circle_yellow);
                break;
        }

        return convertView;
    }

}
