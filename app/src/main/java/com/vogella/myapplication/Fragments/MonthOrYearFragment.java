package com.vogella.myapplication.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.vogella.myapplication.R;
import com.vogella.myapplication.databinding.FragmentMonthOrYearBinding;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MonthOrYearFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MonthOrYearFragment extends Fragment {
    FragmentMonthOrYearBinding binding;
    static String mType = "typeExample";
    int boughtDays = 12;
    Toast toast;
    DocumentReference document;

    public MonthOrYearFragment() {
        // Required empty public constructor
    }

    public static MonthOrYearFragment newInstance(String type) {
        mType = type;
        MonthOrYearFragment fragment = new MonthOrYearFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentMonthOrYearBinding.inflate(getLayoutInflater(), container, false);
        View v = binding.getRoot();
        toast = Toast.makeText(getActivity(), "رمز تعبئة خاطئ", Toast.LENGTH_SHORT);
        binding.buttonMonthTocharge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.buttonMonthTocharge.setEnabled(false);
                isValidateKey(binding.monthEditText.getText().toString());
            }
        });

        return v;
    }

    public boolean loadFragment(Fragment fragment) {

        if (fragment != null) {
            FragmentManager fm = getActivity().getSupportFragmentManager();
            fm.beginTransaction().setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                    .replace(R.id.containerPayCharge, fragment)
                    .commit();
            return true;
        }
        return false;
    }

    private void isValidateKey(String key) {
        document = FirebaseFirestore.getInstance().document("work/" + "keys");
        document.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){

                    boolean isWrong = true;
                    ArrayList arrayList = (ArrayList<String>) task.getResult().get("keys");
                    for (int i = 0; i < arrayList.size(); i++) {
                        if (arrayList.get(i).equals(binding.monthEditText.getText().toString())) {
                            Toast.makeText(getActivity(), "رمز تعبئة صحيح", Toast.LENGTH_SHORT).show();
                            isWrong = false;
                            loadFragment(SelectingSessionFragment.newInstance(binding.monthEditText.getText().toString(), mType, boughtDays)); }
                    }
                    if (isWrong) {
                        toast.show();
                    }
                } else {
                    Toast.makeText(getActivity(), "يرجى المحاولة لاحقا", Toast.LENGTH_SHORT).show();
                    binding.buttonMonthTocharge.setEnabled(false);
                }
            }
        });

    }
    }