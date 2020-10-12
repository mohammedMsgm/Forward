package com.vogella.myapplication.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.telephony.CellSignalStrength;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
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
import com.vogella.myapplication.Pojo.Item;
import com.vogella.myapplication.R;
import com.vogella.myapplication.databinding.FragmentBasketBinding;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

public class BasketFragment extends Fragment {
    FragmentBasketBinding binding;
    static List<Item> types;
    int totalPrice = 0;
    FirebaseFirestore firestore;
    DocumentReference documentReference;
    FirebaseUser currentUser;
    CartAdapter adapter;


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
                ArrayList arrayList = new ArrayList<Item>();
                types = queryDocumentSnapshots.toObjects(Item.class);

                boolean b = types.isEmpty() || types == null || types.size() == 0 || queryDocumentSnapshots.isEmpty();
                if (b) {
                    Toast.makeText(getContext(), "ليس لديك محتويات في السلة!!", Toast.LENGTH_SHORT).show();
                    binding.isEmpty.setVisibility(View.VISIBLE);
                } else {
                    arrayList.clear();
                    binding.isEmpty.setVisibility(View.GONE);
                    arrayList.addAll(types);
                    if (v.getContext() != null) {
                        binding.recyclerViewBascket.setLayoutManager(new LinearLayoutManager(v.getContext()));
                        adapter = new CartAdapter(getContext(), arrayList);
                        binding.recyclerViewBascket.setAdapter(adapter);
                        binding.textView30.setText("Total Price: " + adapter.getTotalTotalPrice() + "DA");

                    } else {
                        binding.isEmpty.setVisibility(View.VISIBLE);
                    }


                }
            }
        });
        binding.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkRequests(currentUser.getUid());

            }
        });
        return v;
    }

    public boolean checkRequests(String id) {
        Map<String, Object> request = new HashMap<>();
        request.put("id", currentUser.getUid());
        request.put("name", currentUser.getDisplayName());
        request.put("imageUrl", currentUser.getPhotoUrl().toString());
        request.put("totalPrice", adapter.getTotalTotalPrice());
        request.put("date", new Date());
        DocumentReference document = firestore.collection("work").document("requests");
        document.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    ArrayList<Map<String, Object>> arrayList = (ArrayList<Map<String, Object>>) task.getResult().get("arrayList");
                    boolean bool = true;
                    for (Map<String, Object> map : arrayList) {
                        if (map.get("id").toString().equals(currentUser.getUid())) {
                            Toast.makeText(getContext(), "لفد قمت بعملية شراء مسبقا", Toast.LENGTH_SHORT).show();
                            bool = false;
                            document.update("arrayList", FieldValue.arrayRemove(map));
                        }
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
                    Toast.makeText(getContext(), "تم ارسال الطلب", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "لم يتم ارسال الطلب", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
