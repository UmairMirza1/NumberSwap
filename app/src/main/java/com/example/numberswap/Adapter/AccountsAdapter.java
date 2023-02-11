package com.example.numberswap.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.numberswap.JavaClasses.BusinessCard;
import com.example.numberswap.R;
import com.example.numberswap.Util.Utility;

import java.util.ArrayList;

public class AccountsAdapter extends RecyclerView.Adapter<AccountsAdapter.AccountsViewHolder> {

    private ArrayList<BusinessCard> list;

    public AccountsAdapter(Context context , ArrayList<BusinessCard> ds) {

        list = ds;

    }

    @NonNull
    @Override
    public AccountsAdapter.AccountsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.business_card, parent, false);
        return new AccountsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AccountsAdapter.AccountsViewHolder holder, int position) {
        BusinessCard businessCard = list.get(position);
        holder.imageView.setImageBitmap(businessCard.getImage());
        holder.date.setText(businessCard.getDateOfBirth());
        holder.email.setText(businessCard.getEmail());
        holder.phone.setText(businessCard.getPhoneNumber());
        holder.name.setText(businessCard.getFullName());


    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class AccountsViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView date, email, phone, name;

        public AccountsViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            date = itemView.findViewById(R.id.date);
            email = itemView.findViewById(R.id.email);
            phone = itemView.findViewById(R.id.phone);
            name = itemView.findViewById(R.id.userName);
        }

    }

}
