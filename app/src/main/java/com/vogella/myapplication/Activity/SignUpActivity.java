package com.vogella.myapplication.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.vogella.myapplication.DatePickerFragment;
import com.vogella.myapplication.MainActivity;
import com.vogella.myapplication.Pojo.User;
import com.vogella.myapplication.databinding.ActivitySignUpBinding;

import java.io.IOException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.UUID;


public class SignUpActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    ActivitySignUpBinding binding;
    private FirebaseAuth mAuth;
    private String TAG = SignUpActivity.class.getName();
    private Uri filePath;
    private final int PICK_IMAGE_REQUEST = 71;
    //Firebase
    StorageReference storageReference;
    FirebaseFirestore firestore;
    Context context = this;
    String downloadUrl;
    Calendar calendar;
    String email,
            password,
            name,
            birthDate,
            address,
            phone;
    String lang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //TODO initializing:
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mAuth = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        firestore = FirebaseFirestore.getInstance();

        /*binding.switch2.setChecked(false);
        lang = "en";
        binding.switch2.setText("language: english");
        binding.switch2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    lang = "ar";
                    binding.switch2.setText("language: arabic");
                }
                if (!b){
                    lang = "en";
                    binding.switch2.setText("language: english");
                }
            }
        });*/


        //settings listeners:
        binding.birthDateUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment datePicker = new DatePickerFragment();
                datePicker.show(getSupportFragmentManager(), "date picker");
            }
        });
        binding.nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email = binding.emailUp.getText().toString();
                password = binding.passwordUp.getText().toString();
                name = binding.nameUp.getText().toString();
                birthDate = binding.birthDateUp.getText().toString();
                phone = binding.phoneUp.getText().toString();
                address = binding.addressUp.getText().toString();
                boolean value = false;
                value = name != null && !name.equals("") &&
                        email != null && !email.equals("") &&
                        birthDate != null && !birthDate.equals("") &&
                        phone != null && !phone.equals("") &&
                        password != null && !password.equals("") &&
                        address != null && !address.equals("") &&
                        calendar != null && downloadUrl != null;
                if (value) {
                    signUp(email, password);
                } else {
                    Toast.makeText(context, "تأكد من بياناتك و  صورتك الشخصية رجاء", Toast.LENGTH_SHORT).show();
                }
            }
        });
        binding.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseImage();
            }
        });

    }

    void signUp(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            ProgressDialog progressDialogSignUp = new ProgressDialog(context);
                            progressDialogSignUp.setTitle("Signing up...");
                            progressDialogSignUp.show();
                            progressDialogSignUp.setCancelable(true);
                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(name)
                                    .setPhotoUri(Uri.parse(downloadUrl))
                                    .build();
                            task.getResult().getUser().updateProfile(profileUpdates);
                            User user = new User(email, password, phone, name
                                    , downloadUrl, address, calendar.getTime()
                                    , task.getResult().getUser().getUid()
                                    , 000000, 0, 0, 0, 0
                                    , new ArrayList(), "ar");
                            firestore.collection("users").document(task.getResult().getUser().getUid()).set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    updateUI();
                                    Toast.makeText(context, "تم تسجيل الدخول بنجاح", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(context, "فشلت عملية التسجيل", Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(SignUpActivity.this, "فشلت عملية التسجيل", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void updateUI() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        String currentDateString = DateFormat.getDateInstance(DateFormat.FULL).format(c.getTime());
        binding.birthDateUp.setText(currentDateString);
        calendar = c;
    }

    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            filePath = data.getData();
            uploadImage(UUID.randomUUID().toString());

            /*try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);

                binding.imageView.setImageBitmap(bitmap);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }*/
        }
    }

    private void uploadImage(String uid) {

        if (filePath != null) {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();
            progressDialog.setCancelable(true);
            StorageReference ref = storageReference.child("users/" + uid);
            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            Toast.makeText(context, "Uploaded", Toast.LENGTH_SHORT).show();
                            ref.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                    Uri uri = task.getResult();
                                    downloadUrl = uri.toString();
                                    Glide.with(context).load(downloadUrl).circleCrop().into(binding.imageView);
                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(context, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot
                                    .getTotalByteCount());
                            progressDialog.setMessage("Uploaded " + (int) progress + "%");
                        }
                    });
        }
    }
}
