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

import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FuelFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FuelFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FuelFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FuelFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FuelFragment newInstance(String param1, String param2) {
        FuelFragment fragment = new FuelFragment();
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
        View view = inflater.inflate(R.layout.fragment_fuel, container, false);
        EditText editTextNumber = view.findViewById(R.id.editTextNumber);
        Spinner spinnerFrom = view.findViewById(R.id.fuelSpinnerFrom);
        Spinner spinnerTo = view.findViewById(R.id.fuelSpinnerTo);
        Button button = view.findViewById(R.id.fuelBtn);
        TextView resultText = view.findViewById(R.id.fuelResult);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.fuel_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFrom.setAdapter(adapter);
        spinnerTo.setAdapter(adapter);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inputText = editTextNumber.getText().toString().trim();
                if (inputText.isEmpty()) {
                    Toast.makeText(getActivity(), "Please enter a number", Toast.LENGTH_SHORT).show();
                    return;
                }

                double amount;
                try {
                    amount = Double.parseDouble(inputText);
                } catch (NumberFormatException e) {
                    Toast.makeText(getActivity(), "Invalid numeric input", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (amount < 0) {
                    Toast.makeText(getActivity(), "Value cannot be negative", Toast.LENGTH_SHORT).show();
                    return;
                }

                String from = spinnerFrom.getSelectedItem().toString();
                String to = spinnerTo.getSelectedItem().toString();

                if (from.equals(to)) {
                    Toast.makeText(getActivity(), "Source and target units are the same", Toast.LENGTH_SHORT).show();
                    resultText.setText(String.format(Locale.getDefault(), "Result: %.2f", amount));
                    return;
                }

                try {
                    double result = convertFuel(amount, from, to);
                    resultText.setText(String.format(Locale.getDefault(), "Result: %.2f", result));
                } catch (ArithmeticException e) {
                    Toast.makeText(getActivity(), "Error: Division by zero", Toast.LENGTH_SHORT).show();
                }

            }
        });
        return view;
    }

    private double convertFuel(double amount, String from, String to) {
        // Handle divisions by zero if amount is 0 and it's L/100km
        if (amount == 0 && (from.equals("L/100km") || to.equals("L/100km"))) {
            // Technically km/L = 100/0 is infinity. We should probably handle this.
            // But if amount is 0, let's just return 0 or throw if appropriate.
            if (from.equals("L/100km")) throw new ArithmeticException("Division by zero");
        }

        double kmL = 0;
        boolean efficiencyConversion = false;

        if (from.equals("km/L")) {
            kmL = amount;
            efficiencyConversion = true;
        } else if (from.equals("L/100km")) {
            if (amount == 0) throw new ArithmeticException("Division by zero");
            kmL = 100 / amount;
            efficiencyConversion = true;
        } else if (from.equals("MPG (US)")) {
            kmL = 0.425 * amount;
            efficiencyConversion = true;
        }

        if (efficiencyConversion) {
            if (to.equals("km/L")) {
                return kmL;
            } else if (to.equals("L/100km")) {
                if (kmL == 0) return 0; // Or handle as infinity
                return 100 / kmL;
            } else if (to.equals("MPG (US)")) {
                return kmL / 0.425;
            }
            // If 'to' is not an efficiency unit, it's an incompatible conversion
            return amount;
        }

        if (from.equals("Liters") && (to.equals("Gallons (US)"))) {
            return amount / 3.785;
        } else if (from.equals("Gallons (US)") && (to.equals("Liters"))) {
            return amount * 3.785;
        }

        if (from.equals("Nautical Miles") && (to.equals("Kilometers"))) {
            return amount * 1.852;
        } else if (from.equals("Kilometers") && (to.equals("Nautical Miles"))) {
            return amount / 1.852;
        }

        return amount;
    }
}
