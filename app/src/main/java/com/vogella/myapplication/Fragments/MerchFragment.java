package com.vogella.myapplication.Fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.vogella.myapplication.Adapters.StoreAdapter;
import com.vogella.myapplication.Pojo.Item;
import com.vogella.myapplication.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.UnaryOperator;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class MerchFragment extends Fragment {
    Context context;
    RecyclerView recyclerView;
    ArrayList<Item> arrayList;
    DocumentReference documentReference;
    static List<Item> types;
    public MerchFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_merch, container, false);
        arrayList = new ArrayList<>();
        // Add the following lines to create RecyclerView
        documentReference = FirebaseFirestore.getInstance().document("storeItems/merch");
        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
        FirebaseFirestore.getInstance().collection("merch").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@javax.annotation.Nullable QuerySnapshot queryDocumentSnapshots, @javax.annotation.Nullable FirebaseFirestoreException e) {
               types = queryDocumentSnapshots.toObjects(Item.class);
                arrayList.clear();
                for (int u =0; u<15; u++){
                    arrayList.addAll(types);
                }
                recyclerView = view.findViewById(R.id.merche_recyclerview);
                recyclerView.setLayoutManager(new GridLayoutManager(view.getContext(), 2));
                recyclerView.setAdapter(new StoreAdapter(arrayList));            }
        });

    }

}