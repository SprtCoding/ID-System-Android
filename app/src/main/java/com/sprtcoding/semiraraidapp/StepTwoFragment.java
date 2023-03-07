package com.sprtcoding.semiraraidapp;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class StepTwoFragment extends Fragment {
    private TextInputEditText _regNoET,_preNoET;
    private MaterialButton _validBtn;

    public StepTwoFragment() {

    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_step_two, container, false);
        _regNoET = v.findViewById(R.id.regNoET);
        _preNoET = v.findViewById(R.id.preNoET);
        _validBtn = v.findViewById(R.id.validBtn);

        final Calendar calendar = Calendar.getInstance();

        final DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, day);

                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                _validBtn.setText(simpleDateFormat.format(calendar.getTime()));
            }
        };

        _validBtn.setOnClickListener(view -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    requireContext(),
                    dateSetListener,
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
            );

            datePickerDialog.show();
        });

        return v;
    }

    public String getRegNo() {
        return _regNoET.getText().toString();
    }

    public String getPreNo() {
        return _preNoET.getText().toString();
    }

    public String getValidation() {
        return _validBtn.getText().toString();
    }
}