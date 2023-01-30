package com.example.weatherapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weatherapp.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class WeatherAdapter extends RecyclerView.Adapter<WeatherAdapter.Holder> {

    private final Context context;
    private ArrayList<WeatherModel> list;

    public WeatherAdapter(Context context, ArrayList<WeatherModel> list) {
        super();
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);

        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        WeatherModel model = list.get(position);

        holder.tvDate.setText(model.getDate());
        holder.tvCondition.setText(model.getCondition());
        holder.tvTemp.setText(model.getCurrentTemp());
        Picasso.get().load("https:".concat(model.getImageURL())).into(holder.imgV);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class Holder extends RecyclerView.ViewHolder {

        private final TextView tvDate;
        private final TextView tvCondition;
        private final TextView tvTemp;
        private final ImageView imgV;

        public Holder(View view) {
            super(view);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvCondition = itemView.findViewById(R.id.tvCondition);
            tvTemp = itemView.findViewById(R.id.tvTemp);
            imgV = itemView.findViewById(R.id.imgV);
        }}
    public void updateData(ArrayList<WeatherModel> list){
        this.list = list;
        this.notifyDataSetChanged();
    }
}

