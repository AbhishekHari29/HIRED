package com.droidevils.hired.User;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.droidevils.hired.Helper.Adapter.AppointmentAdaptor;
import com.droidevils.hired.Helper.Adapter.AppointmentHelper;
import com.droidevils.hired.Helper.Bean.Appointment;
import com.droidevils.hired.Helper.Bean.AppointmentInterface;
import com.droidevils.hired.R;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AppointmentRequestFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AppointmentRequestFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private ListView appointmentListView;
    private ArrayList<AppointmentHelper> appointmentHelpers;
    private AppointmentAdaptor appointmentAdaptor;

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;

    public AppointmentRequestFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RequestAppointmentFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AppointmentRequestFragment newInstance(String param1, String param2) {
        AppointmentRequestFragment fragment = new AppointmentRequestFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_request_appointment, container, false);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        appointmentListView = view.findViewById(R.id.appointment_list_two);
        appointmentHelpers = new ArrayList<>();
        appointmentAdaptor = new AppointmentAdaptor(getActivity(), appointmentHelpers);
        appointmentListView.setAdapter(appointmentAdaptor);
        retrieveAppointmentInformation();
        registerForContextMenu(appointmentListView);

        return view;
    }

    private void retrieveAppointmentInformation() {

        Appointment.getAppointmentByProviderId(currentUser.getUid(), new AppointmentInterface() {
            @Override
            public void getAppointmentArrayList(ArrayList<Appointment> appointments) {
                if (appointments != null) {
                    for (Appointment appointment : appointments)
                        appointmentHelpers.add(new AppointmentHelper(
                                appointment.getServiceReceiverId(), appointment.getServiceReceiverName(),
                                appointment.getServiceId(), appointment.getServiceName(),
                                appointment.getTime(), appointment.getDate(),
                                appointment.getStatus(), appointment.getComment()));
                    appointmentAdaptor.notifyDataSetChanged();
                }
            }
        });
    }

    @Override
    public void onCreateContextMenu(@NonNull ContextMenu menu, @NonNull View v, @Nullable ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.context_appointment_request_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        AppointmentHelper appointmentHelper = appointmentHelpers.get((int) menuInfo.id);
        switch (item.getItemId()) {
            case R.id.context_appointment_view_profile:
                Intent profileIntent = new Intent(getActivity(), ProfileActivity.class);
                Bundle extras = new Bundle();
                extras.putString(ProfileActivity.PROFILE_TYPE, ProfileActivity.OTHER_PROFILE);
                extras.putString(ProfileActivity.PROFILE_ID, appointmentHelper.getUserId());
                profileIntent.putExtras(extras);
                startActivity(profileIntent);
                return true;
            case R.id.context_appointment_respond:
                new MaterialAlertDialogBuilder(getActivity())
                        .setTitle("Respond")
                        .setMessage("How do you want to respond to this Appointment")
                        .setNeutralButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(getActivity(), "Response: Neutral", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setPositiveButton(Appointment.ACCEPT, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Appointment appointment = new Appointment();
                                appointment.setServiceProviderId(currentUser.getUid());
                                appointment.setServiceReceiverId(appointmentHelper.getUserId());
                                appointment.setServiceId(appointmentHelper.getServiceId());
                                appointment.respondToAppointment(Appointment.ACCEPT, new AppointmentInterface() {
                                    @Override
                                    public void getBooleanResult(Boolean result) {
                                        if (result){
                                            appointmentHelper.setStatus(Appointment.ACCEPT);
                                            appointmentHelpers.set((int) menuInfo.id, appointmentHelper);
                                            appointmentAdaptor.notifyDataSetChanged();
                                            Toast.makeText(getActivity(), "Response Updated", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(getActivity(), "Response not Updated", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }
                        })
                        .setNegativeButton(Appointment.REJECT, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Appointment appointment = new Appointment();
                                appointment.setServiceProviderId(currentUser.getUid());
                                appointment.setServiceReceiverId(appointmentHelper.getUserId());
                                appointment.setServiceId(appointmentHelper.getServiceId());
                                appointment.respondToAppointment(Appointment.REJECT, new AppointmentInterface() {
                                    @Override
                                    public void getBooleanResult(Boolean result) {
                                        if (result){
                                            appointmentHelper.setStatus(Appointment.REJECT);
                                            appointmentHelpers.set((int) menuInfo.id, appointmentHelper);
                                            appointmentAdaptor.notifyDataSetChanged();
                                            Toast.makeText(getActivity(), "Response Updated", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(getActivity(), "Response not Updated", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }
                        })
                        .show();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

}