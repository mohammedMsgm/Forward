package com.vogella.myapplication.Fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.telephony.CellSignalStrength;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.vogella.myapplication.Adapters.CartAdapter;
import com.vogella.myapplication.Adapters.StoreAdapter;
import com.vogella.myapplication.EditNameDialogFragment;
import com.vogella.myapplication.Pojo.EditNameDialogListener;
import com.vogella.myapplication.Pojo.Item;
import com.vogella.myapplication.R;
import com.vogella.myapplication.databinding.FragmentBasketBinding;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

public class BasketFragment extends Fragment implements EditNameDialogListener {
    FragmentBasketBinding binding;
    static List<Item> types;
    int totalPrice;
    FirebaseFirestore firestore;
    FirebaseUser currentUser;
    ArrayList<Item> itemArrayList;
    CartAdapter adapter;
    String phoneNuber;


    public BasketFragment() {
        // Required empty public constructor
    }

    public static BasketFragment newInstance(String param1, String param2) {
        BasketFragment fragment = new BasketFragment();
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
        binding = FragmentBasketBinding.inflate(getLayoutInflater(), container, false);
        View v = binding.getRoot();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String uid = currentUser.getUid();
        firestore = FirebaseFirestore.getInstance();
        CollectionReference collection = firestore.collection("users/" + uid + "/events");
        collection.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@javax.annotation.Nullable QuerySnapshot queryDocumentSnapshots, @javax.annotation.Nullable FirebaseFirestoreException e) {
                itemArrayList = new ArrayList<Item>();
                types = queryDocumentSnapshots.toObjects(Item.class);

                boolean b = types.isEmpty() || types == null || types.size() == 0 || queryDocumentSnapshots.isEmpty();
                if (b) {
                    binding.isEmpty.setVisibility(View.VISIBLE);
                } else {
                    itemArrayList.clear();
                    binding.isEmpty.setVisibility(View.GONE);
                    itemArrayList.addAll(types);
                    if (v.getContext() != null) {
                        binding.recyclerViewBascket.setLayoutManager(new LinearLayoutManager(v.getContext()));
                        adapter = new CartAdapter(getContext(), itemArrayList);
                        binding.recyclerViewBascket.setAdapter(adapter);

                        for (int i = 0; i<itemArrayList.size(); i++){
                            totalPrice = totalPrice + itemArrayList.get(i).getTotalPrice();
                            binding.totalPriceBascket.setText(totalPrice + " DA");
                        }
                    } else {
                        binding.isEmpty.setVisibility(View.VISIBLE);
                    }


                }
            }
        });
        binding.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder
                        (getContext())
                        .setTitle("Finishing the purchases")
                        .setMessage("Total Price: " + totalPrice + " DA").setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        checkRequests(currentUser.getUid());
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                })

                        .show();

            }
        });
        return v;
    }

    public boolean checkRequests(String id) {
        Map<String, Object> request = new HashMap<>();
        request.put("id", currentUser.getUid());
        request.put("name", currentUser.getDisplayName());
        request.put("imageUrl", currentUser.getPhotoUrl().toString());
        request.put("totalPrice", totalPrice);
        request.put("date", new Date());
        request.put("phoneNumber", phoneNuber);
        ArrayList<Map<String, Object>> ordersList = new ArrayList<>();
        for (int i = 0; i< itemArrayList.size(); i++) {
            Map<String, Object> map = new HashMap<>();
            map.put("amount", itemArrayList.get(i).getAmount());
            map.put("totalPrice", totalPrice);
            map.put("name", itemArrayList.get(i).getItemName());
            map.put("totalPrice", itemArrayList.get(i).getTotalPrice());
            map.put("sizes", itemArrayList.get(i).getSizes());
            map.put("selectedSize", itemArrayList.get(i).getSelectedSize());
            map.put("imageUrl", itemArrayList.get(i).getImageUrl());
            ordersList.add(map);
        }
        request.put("orders", ordersList);

        DocumentReference document = firestore.collection("work").document("requests");
        document.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    ArrayList<Map<String, Object>> arrayList = (ArrayList<Map<String, Object>>) task.getResult().get("arrayList");
                    boolean bool = true;
                    for (Map<String, Object> map : arrayList) {
                       /* if (map.get("id").toString().equals(currentUser.getUid())) {
                            new AlertDialog.Builder
                                    (getContext())
                                    .setTitle("يرجى المحاولة لاحقا")
                                    .setMessage("لقد قمت بارسال طلب مسبقا، لن تتمكن من ارسال طلب جديد حتى يتم معالجة طلبك السابق")
                                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.cancel();
                                }
                            })

                                    .show();                            bool = false;
                          //  document.update("arrayList", FieldValue.arrayRemove(map));
                        }*/
                    }
                    if (bool == true){
                        yep(request);
                    }
                }
            }
        });
        return true;
    }
    public void yep(Map<String, Object> request){
        DocumentReference document = firestore.collection("work").document("requests");
        document.update("arrayList", FieldValue.arrayUnion(request)).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    CollectionReference collection = firestore.collection("users/" + currentUser.getUid() + "/events");
                    for (Item item :
                            itemArrayList) {
                        collection.document(item.getDocumentPath().split("/")[1].toString()).delete();
                    }
                    Toast.makeText(getContext(), "تم ارسال الطلب", Toast.LENGTH_SHORT).show();
                } else {
                    binding.totalPriceBascket.setText(totalPrice + " DA");
                    Toast.makeText(getContext(), "لم يتم ارسال الطلب", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void showEditDialog() {
        FragmentManager fm = getFragmentManager();
        EditNameDialogFragment editNameDialogFragment = EditNameDialogFragment.newInstance("Put Phone Number");
        // SETS the target fragment for use later when sending results
        editNameDialogFragment.setTargetFragment(BasketFragment.this, 300);
        editNameDialogFragment.show(fm, "fragment_edit_name");
    }

    // This is called when the dialog is completed and the results have been passed
    @Override
    public void onFinishEditDialog(String inputText) {
        Toast.makeText(getContext(), "Hi, " + inputText, Toast.LENGTH_SHORT).show();
    }
}
