package com.vogella.myapplication.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.EventDay;
import com.applandeo.materialcalendarview.exceptions.OutOfDateRangeException;
import com.applandeo.materialcalendarview.listeners.OnDayClickListener;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.vogella.myapplication.Pojo.MyEventDay;
import com.vogella.myapplication.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;


public class CheckFragment extends Fragment {
    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    CalendarView calendarView;
    List<EventDay> events;
    FirebaseFirestore fireStore;
    ArrayList<Date> monthEvents;
    ObjectMapper mapper;
    View emptyLayout;


    public CheckFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        events = new ArrayList<>();
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        fireStore = FirebaseFirestore.getInstance();
        View view;
        boolean isSignIN = currentUser == null;
        if (isSignIN) {
            view = inflater.inflate(R.layout.pleas_sign_in_or_up, container, false);
        } else {
            view = inflater.inflate(R.layout.fragment_check, container, false);
        }
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (currentUser != null) {
            fireStore.collection("users").document(currentUser.getUid()).addSnapshotListener(new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@javax.annotation.Nullable DocumentSnapshot documentSnapshot, @javax.annotation.Nullable FirebaseFirestoreException e) {
                    if (e != null && !documentSnapshot.exists()) {
                        Log.e("mohammed msgm", e.getMessage());
                    }
                    if (documentSnapshot.exists()) {
                        monthEvents = (ArrayList) documentSnapshot.get("myEvents");
                        if (monthEvents != null){
                            emptyLayout.setVisibility(View.GONE);
                            if (!monthEvents.isEmpty()){
                                emptyLayout.setVisibility(View.GONE);
                                calendarView = view.findViewById(R.id.calendar_view_check);
                                calendarView.setSwipeEnabled(true);
                                ArrayList<MyEventDay> myEventDays = new ArrayList<>();
                                ArrayList<Calendar> calendars = new ArrayList<>();
                                ArrayList<Map> mapsEvents = (ArrayList<Map>) documentSnapshot.get("myEvents");
                                Map<String, Object> map;
                                for(int i = 0;  i< mapsEvents.size(); i++){
                                    map = mapsEvents.get(i);
                                    Calendar instance = Calendar.getInstance();
                                    instance.setTime((Date) map.get("date"));
                                    myEventDays.add(new MyEventDay(instance, R.drawable.ic_dumbell_icon, (String) map.get("type"),(String) map.get("note")));
                                    calendars.add(instance);
                                }
                                calendarView.setHighlightedDays(calendars);
                                calendarView.setEvents((List) myEventDays);
                                ConstraintLayout constraintLayout = view.findViewById(R.id.constraintLayout_check);
                                Button okButton = view.findViewById(R.id.ok_check);
                                okButton.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        constraintLayout.setVisibility(View.GONE);
                                    }
                                });
                                calendarView.setOnDayClickListener(new OnDayClickListener() {
                                    @Override
                                    public void onDayClick(EventDay eventDay) {
                                        try {
                                            MyEventDay myEventDay = (MyEventDay) eventDay;
                                            String note = myEventDay.getNote();
                                            constraintLayout.setVisibility(View.VISIBLE);
                                        }catch (Exception e){
                                            Toast.makeText(getActivity(), "ليس هنالك حصة لذالك اليوم!", Toast.LENGTH_SHORT).show();
                                        }


                                    }
                                });
                            }else{
                                calendarView.setSwipeEnabled(true);
                                emptyLayout.setVisibility(View.VISIBLE);
                            }
                        }else {
                            emptyLayout.setVisibility(View.VISIBLE);
                        }
                    }
                }
            });

        }
    }
}

