package com.vogella.myapplication.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class NotesFragment extends Fragment {
    static int notesAmount;
    public static SelectingSessionFragment newInstance(int boughtDays) {
        notesAmount = boughtDays;
        Bundle args = new Bundle();
        SelectingSessionFragment fragment = new SelectingSessionFragment();
        fragment.setArguments(args);
        return fragment;
    }
    public NotesFragment() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return null;
    }
}
