package com.example.lab4_20211688.fragments;

import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.lab4_20211688.adapters.FutureForecastAdapter;
import com.example.lab4_20211688.databinding.FragmentFuturoBinding;
import com.example.lab4_20211688.models.HourlyForecastModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class FuturoFragment extends Fragment {

    private FragmentFuturoBinding binding;
    private RequestQueue queue;
    private List<HourlyForecastModel> forecastList = new ArrayList<>();
    private FutureForecastAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentFuturoBinding.inflate(inflater, container, false);
        queue = Volley.newRequestQueue(requireContext());

        binding.recyclerViewFuture.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new FutureForecastAdapter(forecastList);
        binding.recyclerViewFuture.setAdapter(adapter);

        binding.btnBuscarFuture.setOnClickListener(v -> {
            String id = binding.editIdLocation.getText().toString().trim();
            String fecha = binding.editFecha.getText().toString().trim();
            if (!id.isEmpty() && !fecha.isEmpty()) {
                fetchFutureForecast(id, fecha);
            }
        });

        return binding.getRoot();
    }
    //esta funcion se realizó con ayuda de chatgpt
    private void fetchFutureForecast(String idLocation, String fecha) {
        String url = "https://api.weatherapi.com/v1/future.json?key=ec24b1c6dd8a4d528c1205500250305&q=id:" +
                Uri.encode(idLocation) + "&dt=" + fecha;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        forecastList.clear();
                        JSONArray hourly = response.getJSONObject("forecast")
                                .getJSONArray("forecastday")
                                .getJSONObject(0)
                                .getJSONArray("hour");

                        for (int i = 0; i < hourly.length(); i++) {
                            JSONObject obj = hourly.getJSONObject(i);
                            String time = obj.getString("time");
                            double temp = obj.getDouble("temp_c");
                            int humidity = obj.getInt("humidity");
                            double rain = obj.has("chance_of_rain") ? obj.getDouble("chance_of_rain") : 0;
                            String condition = obj.getJSONObject("condition").getString("text");

                            forecastList.add(new HourlyForecastModel(time, temp, condition, humidity, rain));
                        }

                        adapter.notifyDataSetChanged();
                    } catch (Exception e) {
                        Toast.makeText(getContext(), "Error al procesar datos", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                },
                error -> {
                    Toast.makeText(getContext(), "Error en la consulta", Toast.LENGTH_SHORT).show();
                    error.printStackTrace();
                });

        queue.add(request);
    }
}
