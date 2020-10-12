package com.vogella.myapplication.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.vogella.myapplication.MainActivity;
import com.vogella.myapplication.Pojo.Item;
import com.vogella.myapplication.R;

import java.util.ArrayList;
import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {

    private ArrayList<Item> mData;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private Context context;
    long totalTotalPrice = 1;
    // data is passed into the constructor
    public CartAdapter(Context context, ArrayList<Item> data) {
        this.mData = data;
        this.context = context;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view =  LayoutInflater.from(parent.getContext())
                .inflate(R.layout.profile_number_one, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Item item = mData.get(position);
        holder.nameText.setText(item.getItemName());
        holder.minusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (item.getAmount() > 1){
                    item.setAmount(item.getAmount() - 1);
                    holder.amountText.setText("" + item.getAmount());
                    item.setTotalPrice(item.getPrice() * item.getAmount());
                    holder.totalPrice.setText("Total: " + item.getTotalPrice() + " DA");

                }

            }
        });

        holder.plusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (item.getAmount() < 10){
                    item.setAmount(item.getAmount() + 1);
                    holder.amountText.setText("" +item.getAmount());
                    item.setTotalPrice(item.getPrice() * item.getAmount());
                    holder.totalPrice.setText("Total: " + item.getTotalPrice() + " DA");

                }

            }
        });
        holder.amountText.setText(item.getAmount() + "");

        Glide.with(context).load(item.getImageUrl()).centerCrop().into(holder.imageView);
        List<String> sizes = item.getSizes();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,
                android.R.layout.simple_spinner_item, sizes);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        holder.spinner.setAdapter(adapter);
        holder.spinner.setSelection(item.getSelectedSize());
        item.setTotalPrice(item.getPrice() * item.getAmount());
        holder.totalPrice.setText("Total: " + item.getTotalPrice() + " DA");
    }


    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView nameText, amountText, totalPrice;
        Button plusButton, minusButton;
        ImageView imageView;
        Spinner spinner;

        ViewHolder(View itemView) {
            super(itemView);
            nameText = itemView.findViewById(R.id.nameText);
            amountText = itemView.findViewById(R.id.amountText);
            plusButton = itemView.findViewById(R.id.buttonPlus);
            minusButton = itemView.findViewById(R.id.buttonMinus);
            itemView.setOnClickListener(this);
            spinner = itemView.findViewById(R.id.spinner);
            totalPrice = itemView.findViewById(R.id.totalPrice);
            imageView = itemView.findViewById(R.id.imageView6);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }
    // allows clicks events to be caught
    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
    public long getTotalTotalPrice(){
        return totalTotalPrice;
    }
}