package com.droidevils.hired.User;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
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
import com.droidevils.hired.Helper.Bean.UserBean;
import com.droidevils.hired.Helper.Bean.UserInterface;
import com.droidevils.hired.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AppointmentBookFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AppointmentBookFragment extends Fragment {

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

    public AppointmentBookFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MyAppointFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AppointmentBookFragment newInstance(String param1, String param2) {
        AppointmentBookFragment fragment = new AppointmentBookFragment();
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
        View view = inflater.inflate(R.layout.fragment_book_appointment, container, false);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        appointmentListView = view.findViewById(R.id.appointment_list_one);
        appointmentHelpers = new ArrayList<>();
        appointmentAdaptor = new AppointmentAdaptor(getActivity(), appointmentHelpers);
        appointmentListView.setAdapter(appointmentAdaptor);
        retrieveAppointmentInformation();

        registerForContextMenu(appointmentListView);

        return view;
    }

    private void retrieveAppointmentInformation() {

        Appointment.getAppointmentByReceiverId(currentUser.getUid() ,new AppointmentInterface() {
            @Override
            public void getAppointmentArrayList(ArrayList<Appointment> appointments) {
                if (appointments != null) {
                    for (Appointment appointment : appointments)
                        appointmentHelpers.add(new AppointmentHelper(
                                appointment.getServiceProviderId(), appointment.getServiceProviderName(),
                                appointment.getServiceId(), appointment.getServiceName(),
                                appointment.getTime(), appointment.getDate(),
                                appointment.getStatus(), appointment.getComment()));
                    appointmentAdaptor.notifyDataSetChanged();
                }
            }
        });

//        Appointment.getAllAppointment(new AppointmentInterface() {
//            @Override
//            public void getAppointmentArrayList(ArrayList<Appointment> appointments) {
//                if (appointments != null) {
//                    for (Appointment appointment : appointments)
//                        appointmentHelpers.add(new AppointmentHelper(
//                                appointment.getServiceProviderId(), appointment.getServiceProviderName(),
//                                appointment.getServiceId(), appointment.getServiceName(),
//                                appointment.getTime(), appointment.getDate(),
//                                appointment.getStatus(), appointment.getComment()));
//                    appointmentAdaptor.notifyDataSetChanged();
//                }
//            }
//        });
    }

    @Override
    public void onCreateContextMenu(@NonNull ContextMenu menu, @NonNull View v, @Nullable ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.context_appointment_book_menu, menu);
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
            case R.id.context_appointment_edit:
                UserBean.getUserById(currentUser.getUid(), new UserInterface() {
                    @Override
                    public void getUserById(UserBean userBean) {
                        Intent appointmentIntent = new Intent(getActivity().getApplicationContext(), AppointmentBookActivity.class);
                        Bundle extras = new Bundle();
                        extras.putString(AppointmentBookActivity.APPOINTMENT_OPERATION, AppointmentBookActivity.APPOINTMENT_MODIFY);
                        extras.putString(AppointmentBookActivity.APPOINTMENT_SERVICE_PROVIDER_ID, appointmentHelper.getUserId());
                        extras.putString(AppointmentBookActivity.APPOINTMENT_SERVICE_PROVIDER_NAME, appointmentHelper.getUserName());
                        extras.putString(AppointmentBookActivity.APPOINTMENT_SERVICE_RECEIVER_ID, currentUser.getUid());
                        extras.putString(AppointmentBookActivity.APPOINTMENT_SERVICE_RECEIVER_NAME, (userBean != null) ? userBean.getFullName() : "");
                        extras.putString(AppointmentBookActivity.APPOINTMENT_SERVICE_ID, appointmentHelper.getServiceId());
                        extras.putString(AppointmentBookActivity.APPOINTMENT_SERVICE_NAME, appointmentHelper.getServiceName());
                        appointmentIntent.putExtras(extras);
                        startActivity(appointmentIntent);
                    }
                });
                return true;
            case R.id.context_appointment_delete:
                new AlertDialog.Builder(getActivity())
                        .setTitle("Delete Appointment")
                        .setMessage("Are you sure you want to delete this Appointment ?")
                        .setIcon(R.drawable.ic_baseline_delete_24)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Appointment appointment = new Appointment();
                                appointment.setServiceId(appointmentHelper.getServiceId());
                                appointment.setServiceProviderId(appointmentHelper.getUserId());
                                appointment.setServiceReceiverId(currentUser.getUid());
                                appointment.deleteAppointment(new AppointmentInterface() {
                                    @Override
                                    public void getBooleanResult(Boolean result) {
                                        Toast.makeText(getActivity(), result ? "Appointment Deleted" : "Appointment couldn't be Deleted", Toast.LENGTH_SHORT).show();
                                        if (result) {
                                            appointmentHelpers.remove((int) menuInfo.id);
                                            appointmentAdaptor.notifyDataSetChanged();
                                        }
                                    }
                                });
                            }
                        })
                        .setNegativeButton(android.R.string.no, null).show();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }
}