package com.example.lab4_20211688.fragments;

import android.app.AlertDialog;
import android.content.Context;
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
import com.example.lab4_20211688.adapters.ForecastAdapter;
import com.example.lab4_20211688.databinding.FragmentPronosticoBinding;
import com.example.lab4_20211688.models.ForecastDayModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;


public class PronosticoFragment extends Fragment {

    //atributos del sensor se hizo con ayuda de chatgpt:
    private SensorManager sensorManager;
    private Sensor accelerometer;
    private SensorEventListener sensorEventListener;
    private static final float SHAKE_THRESHOLD = 20.0f;
    private long lastShakeTime = 0;


    private FragmentPronosticoBinding binding;
    private RequestQueue queue;
    private List<ForecastDayModel> forecastList = new ArrayList<>();
    private ForecastAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentPronosticoBinding.inflate(inflater, container, false);
        queue = Volley.newRequestQueue(requireContext());

        binding.recyclerViewPronostico.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new ForecastAdapter(forecastList);
        binding.recyclerViewPronostico.setAdapter(adapter);

        binding.btnBuscarPronostico.setOnClickListener(v -> {
            String id = binding.editIdLocation.getText().toString().trim();
            String dias = binding.editDias.getText().toString().trim();
            if (!id.isEmpty() && !dias.isEmpty()) {
                fetchForecast(id, dias);
            }
        });


        //funcion del sensor con ayuda de chatgpt:
        sensorManager = (SensorManager) requireContext().getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        sensorEventListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                float x = event.values[0];
                float y = event.values[1];
                float z = event.values[2];

                double acceleration = Math.sqrt(x * x + y * y + z * z);

                if (acceleration > SHAKE_THRESHOLD && System.currentTimeMillis() - lastShakeTime > 1500) {
                    lastShakeTime = System.currentTimeMillis();
                    mostrarDialogoConfirmacion();
                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) { }
        };



        return binding.getRoot();
    }


    //para esta funcion se hizo uso de  chatgpt:
    private void fetchForecast(String idLocation, String days) {
        String url = "https://api.weatherapi.com/v1/forecast.json?key=ec24b1c6dd8a4d528c1205500250305&q=id:" +
                Uri.encode(idLocation) + "&days=" + days;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        forecastList.clear();
                        JSONArray forecastArray = response.getJSONObject("forecast").getJSONArray("forecastday");
                        for (int i = 0; i < forecastArray.length(); i++) {
                            JSONObject obj = forecastArray.getJSONObject(i);
                            String date = obj.getString("date");
                            JSONObject day = obj.getJSONObject("day");
                            double maxTemp = day.getDouble("maxtemp_c");
                            double minTemp = day.getDouble("mintemp_c");
                            double maxWind = day.getDouble("maxwind_mph");
                            String condition = day.getJSONObject("condition").getString("text");

                            forecastList.add(new ForecastDayModel(date, maxTemp, minTemp, condition, maxWind));
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

    @Override
    public void onResume() {
        super.onResume();
        if (accelerometer != null) {
            sensorManager.registerListener(sensorEventListener, accelerometer, SensorManager.SENSOR_DELAY_UI);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (accelerometer != null) {
            sensorManager.unregisterListener(sensorEventListener);
        }
    }

    private void mostrarDialogoConfirmacion() {
        new AlertDialog.Builder(getContext())
                .setTitle("¡Alerta, vas a eliminar pronósticos!")
                .setMessage("Amigo me estás agitando mucho :0 ¿Deseas borrar los pronósticos?")
                .setPositiveButton("Sí", (dialog, which) -> {
                    forecastList.clear();
                    adapter.notifyDataSetChanged();
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }


}
