package com.vogella.myapplication.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.vogella.myapplication.MainActivity;
import com.vogella.myapplication.R;

public class SignInActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    EditText emailEditText,passwordEditText;
    String TAG = SignInActivity.class.getName();
    ProgressBar progressBar;
    Button signInButton, signUpButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        emailEditText = findViewById(R.id.last_name_up);
        passwordEditText = findViewById(R.id.birth_date_up);
    }
    public void open(View view){
        if (emailEditText.getText().toString().equals("")
                || passwordEditText.getText().toString().equals("")
                || emailEditText.getText().toString()== null
                || passwordEditText.getText().toString() == null ){
            Toast.makeText(this, "Email or Password is Empty", Toast.LENGTH_SHORT).show();
        }else {
            progressBar = findViewById(R.id.progressBar);
            progressBar.setVisibility(View.VISIBLE);

        signIn(emailEditText.getText().toString(), passwordEditText.getText().toString());}
    }
    public void signIn (String email, String password){
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressBar.setVisibility(View.GONE);
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(SignInActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(TAG, e.getMessage());
            }
        });
    }
    public void updateUI(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
    public void openSignUp(View v){
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
    }

}
