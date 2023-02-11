package com.example.numberswap.Fragments;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.numberswap.Activities.CreateAccountActivity;
import com.example.numberswap.Activities.SetUpAccount;
import com.example.numberswap.DB.BusinessCardDB;
import com.example.numberswap.Interface.IBusinessCardDAO;
import com.example.numberswap.R;

public class AccountFragment extends Fragment {

    RecyclerView recyclerView;
    Button receive,addAccount;
    IBusinessCardDAO dbInterface;
    Context context;
    public AccountFragment() {
    }
    public AccountFragment(Context context)
    {
        this.context = context;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.accounts, container, false);
        dbInterface = new BusinessCardDB(context);

        recyclerView = view.findViewById(R.id.accountRecyclerView);
        receive = view.findViewById(R.id.receive);
        addAccount = view.findViewById(R.id.addAcc);
        getAccounts();


        addAccount.setOnClickListener(v->{
            Intent intent = new Intent(context, CreateAccountActivity.class);
            startActivity(intent);
        });
        return view;
    }
    private void getAccounts()
    {
        Cursor cursor = dbInterface.loadCards();
        if(cursor.getCount() == 0)
        {
            Intent intent = new Intent(context, SetUpAccount.class);
            startActivity(intent);
        }
    }
}