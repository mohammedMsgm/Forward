package com.vogella.myapplication.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.vogella.myapplication.Activity.ChargeActivity;
import com.vogella.myapplication.PayChargeActivity;
import com.vogella.myapplication.Pojo.User;
import com.vogella.myapplication.R;
import com.vogella.myapplication.Activity.SignUpActivity;
import com.vogella.myapplication.databinding.FragmentProfileBinding;

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
                    startActivity(new Intent(getActivity(), ChargeActivity.class));
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
                    Toast.makeText(getActivity(), "لم يتم إضافة هذه الخاصية بعد", Toast.LENGTH_SHORT).show();
                }
            });

            String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
            fireStore.collection("users").document(uid).addSnapshotListener(new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                    if (documentSnapshot.exists()) {
                        Log.d(TAG, "currentData:" + documentSnapshot.toString());
                        User profile = documentSnapshot.toObject(User.class);
                        binding.name.setText(profile.getName());
                        binding.email.setText("Email: " + profile.getEmail());
                        binding.phoneNumber.setText("Phone: " + profile.getPhone());
                        binding.adressProfile.setText("Address: " + profile.getAddress());
                        Glide.with(getActivity()).load(profile.getProfileImageUrl()).circleCrop().into(binding.profileImage);
                        Glide.with(getActivity()).load(profile.getProfileCoverUrl()).into(binding.coverImage);
                        binding.whieght.setText("Weight: " + profile.getmWeight());
                        binding.height.setText("Height: " + profile.getmHeight());
                        binding.maxPullups.setText("Max pull ups: " + profile.getMaxPullUps());
                        binding.maxPushUps.setText("Max push ups: " + profile.getMaxPullUps());
                    }
                    binding.version.setText("Version: 1.0.0");
                    binding.theProgrammer.setText("Developers: ");
                    binding.termsAndCondition.setText("Terms and condition  >>");
                    binding.rateTheApp.setText("Rate the app  >>");
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
                    startActivity(new Intent(getContext(), SignUpActivity.class));
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
            FragmentManager fm = getActivity().getSupportFragmentManager();
            fm.beginTransaction().setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                    .replace(R.id.container, fragment)
                    .commit();
            return true;
        }
        return false;
    }
}
