package com.vogella.myapplication.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Transaction;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.vogella.myapplication.MainActivity;
import com.vogella.myapplication.R;
import com.vogella.myapplication.databinding.FragmentSlelctingSessionActivityBinding;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import static com.prolificinteractive.materialcalendarview.MaterialCalendarView.SELECTION_MODE_MULTIPLE;

public class SelectingSessionFragment extends Fragment {
    FragmentSlelctingSessionActivityBinding binding;
    int leftDays = 12;
    static int mBoughtDays = 12;
    DocumentReference document;
    String uid;
    DocumentReference reference;
    static String mKey;
    static String mType = "type example";
    String mNote = "هذه ملاحظة اعتباطية من اجل تجربة الكوود الذي أقوم بكتابته.";

    public static SelectingSessionFragment newInstance(String key, String type, int boughtDays) {
        mBoughtDays = boughtDays;
        mType = type;
        mKey = key;
        Bundle args = new Bundle();
        SelectingSessionFragment fragment = new SelectingSessionFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public SelectingSessionFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentSlelctingSessionActivityBinding.inflate(getLayoutInflater(), container, false);
        View v = binding.getRoot();
        //TODO: initialize your shite:
        document = FirebaseFirestore.getInstance().document("work/" + "keys");

        reference = FirebaseFirestore.getInstance().collection("users").document(uid);
        Toast toast = Toast.makeText(getContext(), "لقد قمت سابقا باختيار جميع الايام", Toast.LENGTH_SHORT);
        Calendar ViewCalendar = Calendar.getInstance();
        Date date = new Date();
        date.setTime(date.getTime() - (1000 * 3600 * 24));
        ViewCalendar.setTime(date);

        //TODO: Start typing you code:
        binding.calendarViewSelecting.setCurrentDate(new Date());
        binding.calendarViewSelecting.setSelectionMode(SELECTION_MODE_MULTIPLE);
        binding.leftDays.setText("" + leftDays);
        binding.calendarViewSelecting.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                int size1 = binding.calendarViewSelecting.getSelectedDates().size();
                if (size1 > mBoughtDays) {
                    toast.show();
                    binding.calendarViewSelecting.setDateSelected(date, false);
                } else {
                    leftDays = mBoughtDays - size1;
                    binding.leftDays.setText("" + leftDays);
                    toast.cancel();
                }

            }
        });
        reference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if (e == null && documentSnapshot.exists()) {
                    ArrayList<Map<String, Object>> arrayList = (ArrayList<Map<String, Object>>) documentSnapshot.get("myEvents");
                    if (arrayList != null && arrayList.size() != 0) {

                        for (int i = 0; i < arrayList.size(); i++) {
                            binding.calendarViewSelecting.setDateSelected(((Timestamp) arrayList.get(i).get("date")).toDate(), true);
                            mBoughtDays += 1;
                        }
                    }
                }
            }
        });
        binding.slecttingPayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addSession();
            }
        });
        return v;
    }

    public boolean loadFragment(Fragment fragment) {

        if (fragment != null) {
            FragmentManager fm = getActivity().getSupportFragmentManager();
            fm.beginTransaction()
                    .replace(R.id.containerPayCharge, fragment)
                    .commit();
            return true;
        }
        return false;
    }

    private void addSession() {
        // binding.selectingPayButton.setEnabled(false);
        boolean b = binding.calendarViewSelecting.getSelectedDates().size() == mBoughtDays;
        if (b) {
            binding.progressBar3.setVisibility(View.VISIBLE);

            FirebaseFirestore.getInstance().runTransaction(new Transaction.Function<Void>() {
                @androidx.annotation.Nullable
                @Override
                public Void apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {
                   /* ArrayList arrayList = (ArrayList<String>) transaction.get(document).get("names");
                    boolean isTrue = false;
                    for (int i = 0; i < arrayList.size(); i++) {
                        if (arrayList.get(i).equals(mKey)) {
                            isTrue = true;
                        }
                    }
                    if (isTrue) {*/
                    List<CalendarDay> calendarDays = binding.calendarViewSelecting.getSelectedDates();
                    transaction.update(reference, "myEvents", new ArrayList());
                    for (int j = 0; j < binding.calendarViewSelecting.getSelectedDates().size(); j++) {
                        Map<String, Object> map = new HashMap<>();
                        map.put("date", calendarDays.get(j).getCalendar().getTime());
                        map.put("note", mNote);
                        map.put("type", mType);
                        //list.add(map);
                        transaction.update(reference, "myEvents", FieldValue.arrayUnion(map));
                    }
                    // transaction.update(document, "names", FieldValue.arrayRemove(mKey));
                       /* if (isWrong) {
                            Toast.makeText(getActivity(), "رمز تعبئة خاطئ", Toast.LENGTH_SHORT).show();
                        }*/


                    return null;
                }
            }).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(getContext(), "تم تعبئة الحصص", Toast.LENGTH_SHORT).show();
                        //startActivity(new Intent(getActivity(), MainActivity.class).putExtra("loadedFragment", "CheckFragment"));
                        getActivity().finish();
                    } else {
                        Log.e("mohammedtransitiion", task.getException().getMessage());
                        Toast.makeText(getActivity(), "فشل الشحن يرجى التأكد من الشبكة", Toast.LENGTH_SHORT).show();
                    }
                    binding.progressBar3.setVisibility(View.GONE);

                }
            }).addOnCanceledListener(new OnCanceledListener() {
                @Override
                public void onCanceled() {
                    Log.e("mohammedMsgm", "the update got canceled");
                }
            });

        } else {
            Toast.makeText(getActivity(), "قم بتحديد باقي الايام للتقدم", Toast.LENGTH_SHORT).show();
            binding.slecttingPayButton.setEnabled(true);
        }}
}
