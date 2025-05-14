package com.example.lab4_20211688.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lab4_20211688.R;
import com.example.lab4_20211688.models.ForecastDayModel;

import java.util.List;

public class ForecastAdapter extends RecyclerView.Adapter<ForecastAdapter.ViewHolder> {

    private List<ForecastDayModel> forecastList;

    public ForecastAdapter(List<ForecastDayModel> forecastList) {
        this.forecastList = forecastList;
    }

    @NonNull
    @Override
    public ForecastAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_forecast, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ForecastAdapter.ViewHolder holder, int position) {
        ForecastDayModel item = forecastList.get(position);
        holder.tvFecha.setText("Fecha: " + item.getDate());
        holder.tvMaxTemp.setText("Máx Temp: " + item.getMaxtemp_c() + " °C");
        holder.tvMinTemp.setText("Mín Temp: " + item.getMintemp_c() + " °C");
        holder.tvCondition.setText("Condición: " + item.getConditionText());
        holder.tvMaxWind.setText("Viento Máx: " + item.getMaxwind_mph() + " mph");
    }

    @Override
    public int getItemCount() {
        return forecastList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvFecha, tvMaxTemp, tvMinTemp, tvCondition, tvMaxWind;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvFecha = itemView.findViewById(R.id.tvFecha);
            tvMaxTemp = itemView.findViewById(R.id.tvMaxTemp);
            tvMinTemp = itemView.findViewById(R.id.tvMinTemp);
            tvCondition = itemView.findViewById(R.id.tvCondition);
            tvMaxWind = itemView.findViewById(R.id.tvMaxWind);
        }
    }
}
