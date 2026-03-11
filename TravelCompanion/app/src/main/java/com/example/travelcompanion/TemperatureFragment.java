package com.example.travelcompanion;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class TemperatureFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public TemperatureFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TemperatureFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TemperatureFragment newInstance(String param1, String param2) {
        TemperatureFragment fragment = new TemperatureFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_temperature, container, false);
        EditText editTextNumber = view.findViewById(R.id.editTextNumber);
        Spinner spinnerFrom = view.findViewById(R.id.tempSpinnerFrom);
        Spinner spinnerTo = view.findViewById(R.id.tempSpinnerTo);
        Button button = view.findViewById(R.id.tempBtn);
        TextView resultText = view.findViewById(R.id.tempResult);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.temp_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFrom.setAdapter(adapter);
        spinnerTo.setAdapter(adapter);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inputText = editTextNumber.getText().toString();
                if (inputText.isEmpty()) {
                    Toast.makeText(getActivity(), "Please enter a number", Toast.LENGTH_SHORT).show();
                    return;
                }
                double amount = Double.parseDouble(inputText);

                String from = spinnerFrom.getSelectedItem().toString();
                String to = spinnerTo.getSelectedItem().toString();
                double result = convertTemperature(amount, from, to);
                resultText.setText("Result: " + result);

            }
        });
        return view;
    }
    private double convertTemperature(double amount, String from, String to) {
        double celsius = 0;
        if (from.equals("Celsius")) {
            celsius = amount;
        } else if (from.equals("Fahrenheit")) {
            celsius = (amount - 32) / 1.8;
        } else if (from.equals("Kelvin")) {
            celsius = amount - 273.15;
        }
        if (to.equals("Celsius")) {
            return celsius;
        } else if (to.equals("Fahrenheit")) {
            return (celsius * 1.8) + 32;
        } else if (to.equals("Kelvin")) {
            return celsius + 273.15;
        }

        return celsius;
    }
}
