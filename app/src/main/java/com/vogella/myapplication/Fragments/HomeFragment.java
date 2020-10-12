package com.vogella.myapplication.Fragments;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.core.app.NotificationCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.NumberPicker;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.vogella.myapplication.Adapters.ImageAdapter;
import com.vogella.myapplication.AlertReceiver;
import com.vogella.myapplication.CustomDialogBox;
import com.vogella.myapplication.MainActivity;
import com.vogella.myapplication.Pojo.MyEventDay;
import com.vogella.myapplication.R;
import com.vogella.myapplication.databinding.FragmentHomFragmetBinding;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;


public class HomeFragment extends Fragment {
    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    FirebaseFirestore fireStore;
    Date nextSessionDate;
    FragmentHomFragmetBinding binding;
    int u;
    ArrayList<Date> dates =new ArrayList<>();
    int weight, maxPullUps, maxPushUps, mawDips;
    static String appIs="http://instagram.com/AyoubHongar";
     static String urlIs="http://instagram.com/_u/AyoubHongar";

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        fireStore = FirebaseFirestore.getInstance();


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view;
        // Inflate the layout for this fragment
        if (currentUser == null) {
            view = inflater.inflate(R.layout.pleas_sign_in_or_up, container, false);
        } else {
            binding = FragmentHomFragmetBinding.inflate(inflater, container, false);
            view = binding.getRoot();
            ArrayList imageUrls = new ArrayList();
            imageUrls.add(R.drawable.calisthaniks1);
            imageUrls.add(R.drawable.body_building1);
            imageUrls.add(R.drawable.body_building);
            binding.programHomeViewPAger.setAdapter(new ImageAdapter(getContext(), imageUrls, true));

            // Disable clip to padding
            binding.programHomeViewPAger.setClipToPadding(false);
            // set padding manually, the more you set the padding the more you see of prev & next page
            binding.programHomeViewPAger.setPadding(45, 0, 60, 0);
            // sets a margin b/w individual pages to ensure that there is a gap b/w them
            binding.programHomeViewPAger.setPageMargin(16);
            //homeRecyvlerView = view.findViewById(R.id.home_recyclerView);
            binding.subProgressBar.setIndeterminate(false);

            //TODO: document
            fireStore.document("users/" + currentUser.getUid()).addSnapshotListener(getActivity(), new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                    ArrayList<Calendar> calendars = new ArrayList<>();
                    ArrayList<Map> mapsEvents = (ArrayList<Map>) documentSnapshot.get("myEvents");
                    if(mapsEvents != null){
                        Map<String, Object> map;
                        Calendar instance = Calendar.getInstance();
                        for(int i = 0;  i< mapsEvents.size(); i++) {
                            map = mapsEvents.get(i);
                            instance.setTime((Date) map.get("date"));
                            calendars.add(instance);
                            dates.add(instance.getTime());
                        }
                        Collections.sort(dates);
                        Date currentDate = new Date();
                        for(int k = 0; k<dates.size(); k++){
                            boolean bool = currentDate.getTime() > dates.get(k).getTime();
                            if (currentDate.after(dates.get(k))) {
                                u = k + 1;
                                if(u < dates.size()){
                                    nextSessionDate = dates.get(u);
                                    binding.subProgressBar.setMax(dates.size());
                                    binding.subProgressBar.setProgress(u);
                                    binding.subProgressBarText.setText(k + "/" + dates.size());
                                    binding.attProgressBarText.setText( 2 + "/" + k);
                                    binding.atteProgressBar.setProgress(2);
                                    binding.atteProgressBar.setMax(k);
                                }else {
                                    binding.subProgressBar.setMax(dates.size());
                                    binding.subProgressBar.setProgress(k);
                                    binding.subProgressBarText.setText(k + "/" + dates.size());
                                    binding.attProgressBarText.setText( 2 + "/" + k);
                                    binding.atteProgressBar.setProgress(2);
                                    binding.atteProgressBar.setMax(k);

                                }

                            }
                        }
                        SimpleDateFormat simpleDateFormatDate = new SimpleDateFormat("E, dd MMM");
                        SimpleDateFormat simpleDateFormatTime = new SimpleDateFormat("HH:mm");
                        if(nextSessionDate !=null){
                            instance.setTime(nextSessionDate);
                            startAlarm(instance, 1);
                            binding.nextSession.setText(simpleDateFormatDate.format(nextSessionDate) + "\n" + simpleDateFormatTime.format(nextSessionDate));

                        }else {
                            binding.nextSession.setText("No next sessions -<->_<->-");
                        }
                    }
                    }

            });
            binding.weightPicker.setMinValue(1);
            binding.weightPicker.setMaxValue(250);
            binding.weightPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                @Override
                public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                    weight =binding.weightPicker.getValue();
                }
            });
            binding.maxPullupsPicker.setMinValue(1);
            binding.maxPullupsPicker.setMaxValue(250);
            binding.maxPullupsPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                @Override
                public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                    weight =binding.maxPullupsPicker.getValue();
                }
            });
            binding.maxPushupsPicker.setMinValue(1);
            binding.maxPushupsPicker.setMaxValue(250);
            binding.maxPushupsPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                @Override
                public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                    weight =binding.maxPushupsPicker.getValue();
                }
            });
            binding.maxDipsPicker.setMinValue(1);
            binding.maxDipsPicker.setMaxValue(250);
            binding.maxDipsPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                @Override
                public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                    weight =binding.maxDipsPicker.getValue();
                }
            });
        binding.submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "تم حفظ تقدمك!!", Toast.LENGTH_SHORT).show();
            }
        });
        binding.facebookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(newFacebookIntent(getContext(), "AyoubHongar"));
            }
        });
        binding.instagramButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    startActivity(newInstagramProfileIntent(getContext().getPackageManager(), "AyoubHongar"));
                }catch (ActivityNotFoundException anfe)
                {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse(urlIs)));
                }
            }
        });
        binding.youtubeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(newYoutubeInetent());
            }
        });
        binding.whaysUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(newWhatsUpIntent("0559329541"));
            }
        });
        binding.phoneHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        binding.constraintLayout1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                Fragment prev = getFragmentManager().findFragmentByTag("dialog");
                if (prev != null) {
                    ft.remove(prev);
                }
                ft.addToBackStack(null);
                CustomDialogBox dialogFragment = new CustomDialogBox(dates.size() - u, 2);
                dialogFragment.show(ft, "dialog");
                /*ProgressDialog progressDialog= new ProgressDialog(getContext());
                progressDialog.setMessage("التحقق من حسابك...");
                progressDialog.setTitle("معلومات إشتراكك");
                progressDialog.setCancelable(true);
                progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialogInterface) {
                        Toast.makeText(getContext(), "يجب عليك الإشتراك في النادي أولا!!", Toast.LENGTH_SHORT).show();
                    }
                });
                progressDialog.show();*/
            }
        });
        binding.nextSession.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Get an instance of NotificationManager//
                NotificationCompat.Builder mBuilder =
                        new NotificationCompat.Builder(getContext())
                                .setSmallIcon(R.drawable.ic_lion)
                                .setContentTitle("Next session")
                                .setContentText("حصة كمال اجسام، تمارين صدر، مع محمد لطرش")
                                .setSound(Settings.System.DEFAULT_RINGTONE_URI).setColor(655)
                                //C:\Users\pc\Documents\GitHub\MyApplication\app\src\main\res\raw\notification1.mp3
                        .setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 });

                // Gets an instance of the NotificationManager service//

                NotificationManager mNotificationManager =

                        (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
                // When you issue multiple notifications about the same type of event,
                // it’s best practice for your app to try to update an existing notification
                // with this new information, rather than immediately creating a new notification.
                // If you want to update this notification at a later date, you need to assign it an ID.
                // You can then use this ID whenever you issue a subsequent notification.
                // If the previous notification is still visible, the system will update this existing notification,
                // rather than create a new one. In this example, the notification’s ID is 001//



                mNotificationManager.notify(001, mBuilder.build());


            }
        });
        binding.phoneHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + "0559329541"));
                startActivity(intent);
            }
        });}

        return view;

    }
        /**
         * Intent to open the official Instagram app to the user's profile. If the Instagram app is not
         * installed then the Web Browser will be used.</p>
         *
         * Example usage:</p> {@code newInstagramProfileIntent(context.getPackageManager(),
         *     "http://instagram.com/jaredrummler");}</p>
         *
         * @param pm
         *            The {@link PackageManager}. You can find this class through
         *             Context#getPackageManager().
         * @param url
         *            The URL to the user's Instagram profile.
         * @return The intent to open the Instagram app to the user's profile.
         */
        public static Intent newInstagramProfileIntent(PackageManager pm, String url) {

                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(appIs));
                intent.setPackage("com.instagram.android");
                return (intent);

        }
        public static Intent newYoutubeInetent(){
            return new Intent(Intent.ACTION_VIEW,   Uri.parse("http://www.youtube.com/channel/UCZhoZDX68kZ_49v5fcwroUQ"));

        }
    public static Intent newFacebookIntent(Context context, String url) {
        String FACEBOOK_URL = "https://www.facebook.com/" + url;
        String FACEBOOK_PAGE_ID = "YourPageName";
        String s;
//method to get the right URL to use in the intent
            PackageManager packageManager = context.getPackageManager();
            try {
                int versionCode = packageManager.getPackageInfo("com.facebook.katana", 0).versionCode;
                if (versionCode >= 3002850) { //newer versions of fb app
                    s = "fb://facewebmodal/f?href=" + FACEBOOK_URL;
                } else { //older versions of fb app
                    s = "fb://page/" + FACEBOOK_PAGE_ID;
                }
            } catch (PackageManager.NameNotFoundException e) {
                s =FACEBOOK_URL; //normal web url
            }
        Intent facebookIntent = new Intent(Intent.ACTION_VIEW);
        String facebookUrl = s;
        facebookIntent.setData(Uri.parse(facebookUrl));
        return facebookIntent;
    }
    public static Intent newWhatsUpIntent(String number){
        String url = "https://api.whatsapp.com/send?phone=" + number;
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        return i;
    }

    private void startAlarm(Calendar c, int requestCode) {
            Date date = c.getTime();
            date.setTime(date.getTime() - (30 * 60 * 1000));
            c.setTime(date);
        try{
            AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(getActivity(), AlertReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(), requestCode, intent, 0);
            if (c.before(Calendar.getInstance())) {
                c.add(Calendar.DATE, 1);
            }
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);
        }catch (Exception e){
            Toast.makeText(getContext(), "تحديد المنبه", Toast.LENGTH_SHORT).show();
        }

    }}