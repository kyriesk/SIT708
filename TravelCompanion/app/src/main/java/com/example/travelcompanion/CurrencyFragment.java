package com.example.travelcompanion;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ArrayAdapter;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CurrencyFragment#newInstance} factory method to
 * create an instance of this fragment.
 */

public class CurrencyFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    Button button;
    EditText editTextNumber;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CurrencyFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CurrencyFragment newInstance(String param1, String param2) {
        CurrencyFragment fragment = new CurrencyFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public CurrencyFragment() {
        // Required empty public constructor
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
        View view = inflater.inflate(R.layout.fragment_currency, container, false);

        // Inflate the layout for this fragment
        button = view.findViewById(R.id.button2);
        editTextNumber = view.findViewById(R.id.editTextNumber);
        TextView resultText = view.findViewById(R.id.textView5);


        Spinner spinnerFrom = view.findViewById(R.id.spinner);
        Spinner spinnerTo = view.findViewById(R.id.spinner2);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.currency_array, android.R.layout.simple_spinner_item);

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
                    Toast.makeText(getActivity(), "Amount cannot be negative", Toast.LENGTH_SHORT).show();
                    return;
                }

                String fromCurrency = spinnerFrom.getSelectedItem().toString();
                String toCurrency = spinnerTo.getSelectedItem().toString();

                if (fromCurrency.equals(toCurrency)) {
                    Toast.makeText(getActivity(), "Source and target currencies are the same", Toast.LENGTH_SHORT).show();
                    resultText.setText("Result: " + amount);
                    return;
                }

                double result = convertCurrency(amount, fromCurrency, toCurrency);
                resultText.setText("Result: " + String.format("%.2f", result));
            }
        });
        return view;
    }

    private double convertCurrency(double amount, String fromCurrency, String toCurrency) {
        double audRate = 1.55;
        double eurRate = 0.92;
        double jpyRate = 148.50;
        double gbpRate = 0.78;

        double amountInUsd = 0;

        if (fromCurrency.equals("USD")) {
            amountInUsd = amount;
        } else if (fromCurrency.equals("AUD")) {
            amountInUsd = amount / audRate;
        } else if (fromCurrency.equals("EUR")) {
            amountInUsd = amount / eurRate;
        } else if (fromCurrency.equals("JPY")) {
            amountInUsd = amount / jpyRate;
        } else if (fromCurrency.equals("GBP")) {
            amountInUsd = amount / gbpRate;
        }

        if (toCurrency.equals("USD")) {
            return amountInUsd;
        } else if (toCurrency.equals("AUD")) {
            return amountInUsd * audRate;
        } else if (toCurrency.equals("EUR")) {
            return amountInUsd * eurRate;
        } else if (toCurrency.equals("JPY")) {
            return amountInUsd * jpyRate;
        } else if (toCurrency.equals("GBP")) {
            return amountInUsd * gbpRate;
        }
        return amountInUsd;
    }
}
