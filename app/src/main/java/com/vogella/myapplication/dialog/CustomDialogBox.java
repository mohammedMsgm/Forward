package com.vogella.myapplication.dialog;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.bumptech.glide.Glide;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Transaction;
import com.vogella.myapplication.Pojo.User;
import com.vogella.myapplication.databinding.DialogfragmentBinding;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomDialogBox extends DialogFragment {
   /* DialogfragmentBinding binding;
    User user;
    FirebaseFirestore db;
    ListenerRegistration registration;
    String uId;
    Toast toastlk;
    SimpleDateFormat dateFormat = new SimpleDateFormat("HH : mm");
    Toast toast;
    Task<Void> voidTask;

    public CustomDialogBox(String uId) {
        super();
        this.uId = uId;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DialogfragmentBinding.inflate(getLayoutInflater(), container, false);
        View v = binding.getRoot();
        toast = Toast.makeText(getContext(), "تم اقتصاص حصة", Toast.LENGTH_SHORT);
        toastlk = Toast.makeText(getContext(), "لم يتم اقتصاص الحصص", Toast.LENGTH_SHORT);
        db = FirebaseFirestore.getInstance();
        // Do all the stuff to initialize your custom view
        registration = db.collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).collection("members").document(uId).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if (value.exists() && error == null) {
                    user = value.toObject(User.class);
                    Glide.with(getContext()).load(user.getProfileImageUrl()).circleCrop().centerCrop().into(binding.dialogImage);
                    binding.dialogName.setText(user.getName());
                    if (user.getMyEvents() != null) {
                        if (user.getMyEvents().size() != 0) {
                            Date date = ((Timestamp) user.getMyEvents().get(0).get("date")).toDate();
                            binding.exersizeTime.setText(dateFormat.format(date));
                            binding.sessionsNumber.setText(user.getMyEvents().size() + "");

                        }
                    }
                    if (user.getLastChargeDate() != null) {
                        if (user.getMyEvents().size() == 0 || user.getLastChargeDate().before(new Date())) {
                            binding.getRoot().setBackgroundColor(getResources().getColor(R.color.red));
                            binding.acceptDialog.setText("تعبئة الحصص");
                            binding.acceptDialog.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    registration.remove();
                                    loadFragment(SelectingSessionFragment.newInstance("type", 12, uId));
                                    dismiss();
                                }
                            });
                            if (user.getLastChargeDate().before(new Date())) {
                                binding.afterExpire.setVisibility(View.VISIBLE);
                                binding.afterExpire.setText("لقد تجاوز الوقت المحدد");
                            } else {
                                binding.afterExpire.setText("");
                                binding.afterExpire.setVisibility(View.GONE);
                            }

                        } else {
                            binding.afterExpire.setText("");
                            binding.afterExpire.setVisibility(View.GONE);
                            binding.getRoot().setBackgroundColor(Color.WHITE);
                            binding.acceptDialog.setText(getString(R.string.deduct_sessions));
                            binding.acceptDialog.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    removeSession(user.getQrCodeUrl());
                                }
                            });

                        }
                    }


                    binding.cancelButtonDialog.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            registration.remove();
                            dismiss();
                        }
                    });
                }else {
                    Toast.makeText(getContext(), uId, Toast.LENGTH_SHORT).show();
                }
            }
        });

        return v;
    }

    private void removeSession(String userId) {
        ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setTitle("اقتصاص الحصص");
        progressDialog.setMessage("يرجى الانتظار");
        progressDialog.show();
        setCancelable(false);

        voidTask = db.runTransaction(new Transaction.Function<Void>() {
            @Nullable
            @Override
            public Void apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {
                DocumentReference documentReference = db.collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .collection("members").document(userId);
                DocumentSnapshot documentSnapshot = transaction.get(documentReference);
                List<Map<String, Object>> arrayList = user.getMyEvents();
                int j = 0;
                for (int i = 0; i < arrayList.size(); i++) {
                    Date date = ((Timestamp) arrayList.get(i).get("date")).toDate();
                    long currentDate = new Date().getTime();
                    long sub = currentDate - date.getTime();
                    if ((sub <= (1000 * 3600 * 5) && sub >= -(1000 * 3600 * 5))) {
                        j = i;
                    }
                }
                transaction.update(documentReference, "myEvents", FieldValue.arrayRemove(arrayList.get(j)));
                DocumentReference document = db.collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .collection("work").document("recent");
                //ArrayList<Map<String, Object>> koko =(ArrayList<Map<String, Object>>) documentSnapshot.get("myEvents");
                Map<String, Object> map = new HashMap<>();
                map.put("name", documentSnapshot.getString("name"));
                map.put("time", new Date());
                map.put("imageUrl", documentSnapshot.get("profileImageUrl"));
                map.put("id", documentSnapshot.getId());
                // map.put("leftSessions", koko.size());
                transaction.update(document, "arrayList", FieldValue.arrayUnion(map));
                return null;
            }
        });
        voidTask.addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                progressDialog.dismiss();
                if (task.isSuccessful()) {
                    toast.show();
                    registration.remove();
                    dismiss();
                } else {
                    Log.e("mohammedmsgm", task.getException().getMessage());
                    toastlk.show();
                }
            }
        });
    }

    @Override
    public void onCancel(@NonNull DialogInterface dialog) {
        super.onCancel(dialog);
        registration.remove();
    }

    public boolean loadFragment(Fragment fragment) {

        if (fragment != null) {
            FragmentManager fm = getActivity().getSupportFragmentManager();
            fm.beginTransaction().addToBackStack("tag")
                    .replace(R.id.containermains, fragment)
                    .commit();
            return true;
        }
        return false;
    }*/

}
