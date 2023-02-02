package com.example.numberswap;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class Adapter extends RecyclerView.Adapter<Adapter.DeviceHolder>{

    ArrayList<Devices> list = new ArrayList<>();
    DeviceInterface deviceInterface;
    ArrayList<Devices> checkedDevices = new ArrayList<>();

    public Adapter(ArrayList<Devices> list, DeviceInterface deviceInterface) {
        this.list = list;
        this.deviceInterface = deviceInterface;
    }

    @NonNull
    @Override
    public DeviceHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new DeviceHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.nearby_devices,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull DeviceHolder holder, int position) {
        holder.deviceName.setText(list.get(position).getDeviceName());
        holder.checkBox.setOnClickListener(v->
        {
            if(holder.checkBox.isChecked())
            {
                deviceInterface.getCheckedDevices(list.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class DeviceHolder extends RecyclerView.ViewHolder {

        TextView deviceName;
        CheckBox checkBox;
        public DeviceHolder(@NonNull View itemView) {
            super(itemView);
            deviceName = itemView.findViewById(R.id.deviceName);
            checkBox = itemView.findViewById(R.id.check);
        }
    }
    interface DeviceInterface {
        public void getCheckedDevices(Devices devicesList );
    }
}
