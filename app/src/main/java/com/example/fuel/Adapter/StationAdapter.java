package com.example.fuel.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fuel.Model.StationModel;
import com.example.fuel.R;

import java.util.ArrayList;

public class StationAdapter extends RecyclerView.Adapter<StationAdapter.StationHolder> {

    ArrayList<StationModel> arrayList;
    private StationClickListener listener;

    public StationAdapter(ArrayList<StationModel> arrayList, StationClickListener listener) {
        this.arrayList = arrayList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public StationAdapter.StationHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.station_recyclerview, parent, false);
        return new StationHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull StationHolder holder, int position) {
        holder.price.setText(arrayList.get(position).getPrice());
        holder.brand.setText(arrayList.get(position).getBrand());
        holder.stationAdress.setText(arrayList.get(position).getStationAdress());



    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class StationHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView  brand, price, stationAdress;
        StationClickListener listener;

        public StationHolder(@NonNull View itemView,  StationClickListener listener) {
            super(itemView);

            brand = itemView.findViewById(R.id.brand);
            price = itemView.findViewById(R.id.price);
            stationAdress = itemView.findViewById(R.id.stationAdress);

            //click options
            this.listener = listener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            listener.onStationClick(getAdapterPosition());
        }
    }
    public interface StationClickListener{
        void onStationClick(int position);
    }
}
