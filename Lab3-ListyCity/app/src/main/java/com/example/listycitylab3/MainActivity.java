package com.example.listycitylab3;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements
        AddCityFragment.FragmentInteractionListener {

    private ArrayList<City> dataList;
    private ListView cityList;
    private CityArrayAdapter cityAdapter;
    private int selectedPosition = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String[] cities = {
                "Edmonton", "Vancouver", "Toronto"
        };
        String[] provinces = {
                "AB", "BC", "ON"
        };

        dataList = new ArrayList<City>();
        for (int i = 0; i < cities.length; i++) {
            dataList.add(new City(cities[i], provinces[i]));
        }

        cityList = findViewById(R.id.city_list);
        cityAdapter = new CityArrayAdapter(this, dataList);
        cityList.setAdapter(cityAdapter);

        // Handling the Add option
        FloatingActionButton fab = findViewById(R.id.button_add_city);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedPosition = -1;
                new AddCityFragment().show(getSupportFragmentManager(), "add_city");
            }
        });

        // Handling the Edit option
        cityList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                selectedPosition = position;
                City selectedCity = dataList.get(position);

                AddCityFragment.newInstance(selectedCity).show(getSupportFragmentManager(), "edit_city");
            }
        });
    }

    @Override
    public void AddEditCity(City city) {
        if (selectedPosition == -1) {
            cityAdapter.add(city);
        } else {
            City cityToUpdate = dataList.get(selectedPosition);
            cityToUpdate.setName(city.getName());
            cityToUpdate.setProvince(city.getProvince());
            cityAdapter.notifyDataSetChanged();
        }
    }
}