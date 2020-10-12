package com.vogella.myapplication.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.vogella.myapplication.Pojo.User;
import com.vogella.myapplication.R;


public class QrFragment extends Fragment {
   FirebaseAuth mAuth;
   FirebaseUser currentUser;
   FirebaseUser mUser;
    public QrFragment() {
        // Required empty public constructor
    }
    public static QrFragment newInstance(User user) {

        Bundle args = new Bundle();
        QrFragment fragment = new QrFragment();
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        mUser = mAuth.getCurrentUser();


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if(currentUser == null){
            return inflater.inflate(R.layout.pleas_sign_in_or_up, container, false);
        }else {
            View view = inflater.inflate(R.layout.fragment_qr, container, false);
            ImageView viewById = view.findViewById(R.id.imageView3);
            TextView qrTet = view.findViewById(R.id.qr_text);
            ImageView qrCode = view.findViewById(R.id.qr_code);
            Glide.with(getActivity()).load(currentUser.getPhotoUrl()).circleCrop().into(viewById);
            qrTet.setText("" + mUser.getDisplayName());
            Glide.with(getActivity()).load("https://chart.googleapis.com/chart?chs=300x300&cht=qr&chl="
                    + mUser.getUid()).into(qrCode);
            return view;
        }

    }



}
