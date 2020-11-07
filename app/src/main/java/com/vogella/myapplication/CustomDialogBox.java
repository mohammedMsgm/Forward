package com.vogella.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.renderscript.ScriptGroup;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.DialogFragment;

import com.vogella.myapplication.Activity.BuyingActivity;
import com.vogella.myapplication.Activity.ChargeActivity;
import com.vogella.myapplication.Activity.SignUpActivity;
import com.vogella.myapplication.databinding.DialogfragmentBinding;

public class CustomDialogBox extends DialogFragment {
    DialogfragmentBinding binding;
    int sessionNumber, attendanceNumber;


    public CustomDialogBox(int sessionNumber, int attendanceNumber) {
        super();
        this.sessionNumber = sessionNumber;
        this.attendanceNumber = attendanceNumber;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DialogfragmentBinding.inflate(getLayoutInflater(), container, false);
        View v = binding.getRoot();
        // Do all the stuff to initialize your custom view
        binding.sessionsNumber.setText(sessionNumber + " حصص");
        /*binding.attendanceNumber.setText("لقد حضرت " + attendanceNumber + " حصص في الوقت المناسب لهذا الشهر");*/
        binding.buySeesionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), ChargeActivity.class));
                getActivity().finish();
            }
        });
        binding.cancelButtonDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        return v;
    }
}
