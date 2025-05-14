package com.example.lab4_20211688.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lab4_20211688.R;
import com.example.lab4_20211688.models.HourlyForecastModel;

import java.util.List;

public class FutureForecastAdapter extends RecyclerView.Adapter<FutureForecastAdapter.ViewHolder> {

    private List<HourlyForecastModel> forecastList;

    public FutureForecastAdapter(List<HourlyForecastModel> forecastList) {
        this.forecastList = forecastList;
    }

    @NonNull
    @Override
    public FutureForecastAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_future_forecast, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        HourlyForecastModel item = forecastList.get(position);
        holder.tvTime.setText("Hora: " + item.getTime());
        holder.tvTemp.setText("Temp: " + item.getTempC() + " °C");
        holder.tvCondition.setText("Condición: " + item.getCondition());
        holder.tvHumidity.setText("Humedad: " + item.getHumidity() + "%");
        holder.tvRainChance.setText("Lluvia: " + item.getChanceOfRain() + " %");
    }

    @Override
    public int getItemCount() {
        return forecastList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTime, tvTemp, tvCondition, tvHumidity, tvRainChance;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTime = itemView.findViewById(R.id.tvTime);
            tvTemp = itemView.findViewById(R.id.tvTemp);
            tvCondition = itemView.findViewById(R.id.tvCondition);
            tvHumidity = itemView.findViewById(R.id.tvHumidity);
            tvRainChance = itemView.findViewById(R.id.tvRainChance);
        }
    }
}
