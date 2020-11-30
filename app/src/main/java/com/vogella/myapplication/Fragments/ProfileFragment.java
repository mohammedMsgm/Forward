package com.vogella.myapplication.Fragments;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.vogella.myapplication.Activity.ChargeActivity;
import com.vogella.myapplication.MainActivity;
import com.vogella.myapplication.PayChargeActivity;
import com.vogella.myapplication.Pojo.User;
import com.vogella.myapplication.R;
import com.vogella.myapplication.Activity.SignUpActivity;
import com.vogella.myapplication.databinding.FragmentProfileBinding;

import java.util.Locale;

import javax.annotation.Nullable;


public class ProfileFragment extends Fragment {
    // Create a storage reference from our app
    StorageReference mStorageRef;
    String TAG = "mohammed msgm";
    StorageReference pathReference;
    FirebaseAuth mAuth;
    FirebaseFirestore fireStore;
    FragmentProfileBinding binding;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mStorageRef = FirebaseStorage.getInstance().getReference();
        // Create a reference with an initial file path and name
        pathReference = mStorageRef.child("images/stars.jpg");
        mAuth = mAuth.getInstance();
        fireStore = FirebaseFirestore.getInstance();


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view;
        if (mAuth.getCurrentUser() != null) {
            binding = FragmentProfileBinding.inflate(inflater, container, false);
            view = binding.getRoot();
            binding.addSessions.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(getContext(), ChargeActivity.class));
                }
            });
            binding.purchases.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    loadFragment(new BasketFragment());
                }
            });
            binding.chargeAccount.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(getContext(), "لم يتم إضاف ة هذه الخاصية بعد", Toast.LENGTH_SHORT).show();
                }
            });

            String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
            fireStore.collection("users").document(uid).addSnapshotListener(new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                    if (documentSnapshot.exists()) {
                        User profile = documentSnapshot.toObject(User.class);
                        binding.name.setText(profile.getName());
                        binding.email.setText(getResources().getText(R.string.email) + ":" + profile.getEmail());
                        binding.phoneNumber.setText(getResources().getText(R.string.phone) + ":" + profile.getPhone());
                        binding.adressProfile.setText(getResources().getText(R.string.address) + ":" + profile.getAddress());
                        Glide.with(getContext()).load(profile.getProfileImageUrl()).circleCrop().into(binding.profileImage);
                        if(profile.getProfileCoverUrl() != null) Glide.with(getContext()).load(profile.getProfileCoverUrl()).into(binding.coverImage);


                        binding.whieght.setText(getResources().getText(R.string.weight) + ":" + profile.getmWeight());
                        binding.height.setText(getResources().getText(R.string.dips) + ":" + profile.getmHeight());
                        binding.maxPullups.setText(getResources().getText(R.string.pull_ups) + ":" + profile.getMaxPullUps());
                        binding.maxPushUps.setText(getResources().getText(R.string.push_ups) + ":" + profile.getMaxPullUps());
                    }
                }
            });
            binding.signoutPtofile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mAuth.signOut();
                    loadFragment(new ProfileFragment());
                }
            });
            binding.updateProfile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new AlertDialog.Builder
                            (getContext()).setTitle("password reset").setMessage("sending password reset message to email... ").setCancelable(true);
                    mAuth.sendPasswordResetEmail(mAuth.getCurrentUser().getEmail()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(getContext(), "يمكنك اكمال العملية على الايمايل الخاص بك", Toast.LENGTH_SHORT).show();
                            }else {
                                Toast.makeText(getContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            });
            //.inflate(R.layout.pleas_sign_in_or_up, container, false);
        } else {
            view = inflater.inflate(R.layout.pleas_sign_in_or_up, container, false);

        }
        return view;
    }
    private boolean loadFragment(Fragment fragment) {

        if (fragment != null) {
            FragmentManager fm = ((AppCompatActivity)getContext()).getSupportFragmentManager();
            fm.beginTransaction().setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                    .replace(R.id.container, fragment).addToBackStack( "tag" )
                    .commit();
            return true;
        }
        return false;
    }
    public void setLocale(String lang) {
        Locale myLocale = new Locale(lang);
        Resources res = getResources();
        FirebaseFirestore.getInstance()
                .document("users/" + FirebaseAuth.getInstance().getCurrentUser().getUid())
                .update("language", lang);
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
        Intent refresh = new Intent(getActivity(), MainActivity.class);
        getActivity().finish();
        startActivity(refresh);
    }
}
