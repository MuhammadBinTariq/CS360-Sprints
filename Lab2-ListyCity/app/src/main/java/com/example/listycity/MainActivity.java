package com.example.listycity;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    ListView cityList;
    EditText editTextName;
    Button btnAdd;
    Button btnDelete;
    Button btnConfirm;

    ArrayList<String> dataList;
    ArrayAdapter<String> cityAdapter;

    // Tracks the index of the item the user last tapped
    int selectedPosition = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cityList = findViewById(R.id.city_list);
        editTextName = findViewById(R.id.editText_name);
        btnAdd = findViewById(R.id.button_add);
        btnDelete = findViewById(R.id.button_delete);
        btnConfirm = findViewById(R.id.button_confirm);

        String[] cities = {"Edmonton", "Vancouver", "Moscow", "Sydney", "Berlin", "Vienna", "Tokyo", "Beijing", "Osaka", "New Delhi"};
        dataList = new ArrayList<>();
        dataList.addAll(Arrays.asList(cities));
        cityAdapter = new ArrayAdapter<>(this, R.layout.content, dataList);
        cityList.setAdapter(cityAdapter);

        // 1. ADD CITY Button: Swaps buttons for input field
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editTextName.setVisibility(View.VISIBLE);
                btnConfirm.setVisibility(View.VISIBLE);
                btnAdd.setVisibility(View.GONE);
                btnDelete.setVisibility(View.GONE);
                editTextName.setText("");
            }
        });

        // 2. CONFIRM Button: Adds data and resets buttons
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newCity = editTextName.getText().toString();
                if (!newCity.trim().isEmpty()) {
                    dataList.add(newCity);
                    cityAdapter.notifyDataSetChanged();

                    editTextName.setVisibility(View.GONE);
                    btnConfirm.setVisibility(View.GONE);
                    btnAdd.setVisibility(View.VISIBLE);
                    btnDelete.setVisibility(View.VISIBLE);
                }
            }
        });

        // 3. Selection Logic: Just tracks the position clicked
        cityList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedPosition = position;
            }
        });

        // 4. DELETE CITY Button: Removes item at tracked position
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedPosition != -1 && selectedPosition < dataList.size()) {
                    dataList.remove(selectedPosition);
                    cityAdapter.notifyDataSetChanged();
                    selectedPosition = -1; // Reset selection after deletion
                }
            }
        });
    }
}