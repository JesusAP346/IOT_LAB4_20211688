package com.example.lab4_20211688.fragments;

import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.lab4_20211688.R;
import com.example.lab4_20211688.adapters.LocationAdapter;
import com.example.lab4_20211688.databinding.FragmentLocationsBinding;
import com.example.lab4_20211688.models.LocationModel;

import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class LocationsFragment extends Fragment {

    private FragmentLocationsBinding binding;
    private RequestQueue queue;
    private List<LocationModel> locationList = new ArrayList<>();
    private LocationAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentLocationsBinding.inflate(inflater, container, false);
        queue = Volley.newRequestQueue(requireContext());

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new LocationAdapter(locationList);
        binding.recyclerView.setAdapter(adapter);

        binding.btnBuscar.setOnClickListener(v -> {
            String query = binding.editBuscar.getText().toString().trim();
            if (!query.isEmpty()) fetchLocations(query);
        });

        return binding.getRoot();
    }


    //use chatgpt para esta función :
    private void fetchLocations(String location) {
        String url = "https://api.weatherapi.com/v1/search.json?key=ec24b1c6dd8a4d528c1205500250305&q=" + Uri.encode(location);
        Log.d("API_URL", url); // <-- Añade esto


        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                response -> {
                    locationList.clear();
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject obj = response.getJSONObject(i);
                            LocationModel loc = new LocationModel();
                            Field[] fields = LocationModel.class.getDeclaredFields();
                            for (Field field : fields) {
                                field.setAccessible(true);
                                if (obj.has(field.getName())) {
                                    if (field.getType() == int.class) {
                                        field.setInt(loc, Integer.parseInt(obj.getString(field.getName())));
                                    } else if (field.getType() == double.class) {
                                        field.setDouble(loc, Double.parseDouble(obj.getString(field.getName())));
                                    } else {
                                        field.set(loc, obj.getString(field.getName()));
                                    }

                                }
                            }
                            locationList.add(loc);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    adapter.notifyDataSetChanged();
                },
                error -> Toast.makeText(getContext(), "Error al consultar", Toast.LENGTH_SHORT).show());

        queue.add(request);
    }
}
