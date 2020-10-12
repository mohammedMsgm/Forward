package com.vogella.myapplication.Adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.logging.LogRecord;

public class ImageAdapter extends PagerAdapter {
    private Context context;
    private ArrayList imageUrls;
    private  boolean isResource = false;

    public ImageAdapter(Context context, ArrayList imageUrls) {
        this.context = context;
        this.imageUrls = imageUrls;
    }
    public ImageAdapter(Context context, ArrayList imageUrls, boolean isResource) {
        this.context = context;
        this.imageUrls = imageUrls;
        this.isResource = isResource;
    }

    @Override
    public int getCount() {
        return imageUrls.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        ImageView imageView = new ImageView(context);
        Object o = imageUrls.get(position);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProgressDialog progressDialog= new ProgressDialog(imageView.getContext());
                progressDialog.setMessage("تحميل البرامج التدربية...");
                progressDialog.setTitle("البرامج التدريبية");
                progressDialog.setCancelable(true);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.cancel();
                    }
                }, 3000);
                progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialogInterface) {
                        Toast.makeText(imageView.getContext(), "لا يوجد برامج تدريبية حاليا!!", Toast.LENGTH_SHORT).show();

                    }
                });

                progressDialog.show();
            }
        });
        if(isResource){
            imageView.setImageResource((Integer) o);
        }else {
            String model = (String)imageUrls.get(position);
            RequestOptions requestOptions = new RequestOptions();
            requestOptions = requestOptions.transforms(new CenterCrop(), new RoundedCorners(16));
            Glide.with(context).load(model).apply(requestOptions).into(imageView);

        }

        container.addView(imageView);
        return imageView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
