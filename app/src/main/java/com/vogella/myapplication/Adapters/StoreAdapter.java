package com.vogella.myapplication.Adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.vogella.myapplication.Activity.BuyingActivity;
import com.vogella.myapplication.Pojo.Item;
import com.vogella.myapplication.Pojo.Trainer;
import com.vogella.myapplication.R;

import java.util.ArrayList;

import static android.widget.Toast.LENGTH_LONG;

public class StoreAdapter extends RecyclerView.Adapter<StoreAdapter.ItemViewHolder> {

    private ArrayList<Item> mData;
    private ArrayList<Trainer> tData;
    private ItemClickListener mClickListener;
    private boolean isTrainer = false;

    // data is passed into the constructor
    public StoreAdapter(ArrayList<Item> data) {
        this.mData = data;
    }
    public StoreAdapter(ArrayList<Trainer> data, boolean isTrainer) {
        this.tData = data;
        this.isTrainer = isTrainer;
    }

    // inflates the cell layout from xml when needed
    @Override
    @NonNull
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(isTrainer){
            return new ItemViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.trainer_item_layout, parent, false));
        }else{
        return new ItemViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.store_item_layout, parent, false)); }
    }

    // binds the data to the TextView in each cell
    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        if(isTrainer){
            holder.itemName.setText(tData.get(position).getName());
            holder.trainerType.setText(tData.get(position).getType());
            Glide.with(holder.itemView.getContext()).load(tData.get(position).getImageUrl()).centerCrop().into(holder.itemImage);
            holder.ratingBar.setRating(5);
        }else {
            holder.itemName.setText(mData.get(position).getItemName());
            holder.itemPrice.setText(mData.get(position).getPrice() + " DA");
            Glide.with(holder.itemView.getContext()).load(mData.get(position).getImageUrl()).optionalCenterCrop().into(holder.itemImage);
        }
    }

    // total number of cells
    @Override
    public int getItemCount() {
        if (isTrainer){
            return tData.size();
        }else {
            return mData.size();
        }
    }


    // stores and recycles views as they are scrolled off screen
    public class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView itemName;
        TextView itemPrice, trainerType;
        ImageView itemImage;
        RatingBar ratingBar;


        ItemViewHolder(View itemView) {
            super(itemView);
            if (isTrainer){
                itemName = itemView.findViewById(R.id.item_trainer_name);
                trainerType = itemView.findViewById(R.id.item_trainer_type);
                itemImage = itemView.findViewById(R.id.item_trainer_image);
                ratingBar = itemView.findViewById(R.id.item_rating_bar);
            }else {
                itemName = itemView.findViewById(R.id.item_name);
                itemPrice = itemView.findViewById(R.id.item_price);
                itemImage = itemView.findViewById(R.id.item_image);
            }
            itemView.setOnClickListener(this);
        }
        @Override
        public void onClick(View view) {
            Toast.makeText(view.getContext(), "clicked item", LENGTH_LONG).show();
            Intent intent = new Intent(view.getContext(), BuyingActivity.class);
            if(isTrainer){
                String documentPath = tData.get(getAdapterPosition()).getDocumentPath();

                loadFragment(BuyingActivity.newInstance(documentPath));
            }else {
                String documentPath = mData.get(getAdapterPosition()).getDocumentPath();
                loadFragment(BuyingActivity.newInstance(documentPath));
            }
            if (mClickListener != null){
                mClickListener.onItemClick(view, getAdapterPosition());
            }
        }
        private boolean loadFragment(Fragment fragment) {

            if (fragment != null) {
                FragmentManager fm = ((AppCompatActivity)itemView.getContext()).getSupportFragmentManager();
                fm.beginTransaction().setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                        .replace(R.id.container, fragment).addToBackStack( "tag" )
                        .commit();
                return true;
            }
            return false;
        }
    }
    // convenience method for getting data at click position
    Item getItem(int id) {
        return mData.get(id);
    }
    // allows clicks events to be caught
    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }
    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}