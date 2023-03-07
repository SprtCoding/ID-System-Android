package com.sprtcoding.semiraraidapp;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class StepOneFragment extends Fragment {
    private TextInputEditText _fNameET,_mNameET,_surnameET,_suffixET,_nationalityET,_addressET;
    private AutoCompleteTextView _civilStatCTV;
    private MaterialButton _dateBtn;

    public StepOneFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_step_one, container, false);
        _fNameET = view.findViewById(R.id.fNameET);
        _mNameET = view.findViewById(R.id.mNameET);
        _surnameET = view.findViewById(R.id.surnameET);
        _suffixET = view.findViewById(R.id.suffixET);
        _nationalityET = view.findViewById(R.id.nationalityET);
        _addressET = view.findViewById(R.id.addressET);
        _civilStatCTV = view.findViewById(R.id.civilStatCTV);
        _dateBtn = view.findViewById(R.id.dateBtn);

        String[] data = new String[] {
                "Single",
                "Married",
                "Widowed",
                "Divorced",
                "Separated",
                "In certain cases",
                "Registered partnership",
        };

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                getContext(),
                R.layout.dropdown_item,
                data
        );

        _civilStatCTV.setAdapter(adapter);

        final Calendar calendar = Calendar.getInstance();

        final DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, day);

                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                _dateBtn.setText(simpleDateFormat.format(calendar.getTime()));
            }
        };

        _dateBtn.setOnClickListener(view1 -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    requireContext(),
                    dateSetListener,
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
            );

            datePickerDialog.show();
        });

        return view;
    }

    public String getFirstName() {
        return _fNameET.getText().toString();
    }

    public String getMiddleName() {
        return _mNameET.getText().toString();
    }

    public String getSurname() {
        return _surnameET.getText().toString();
    }

    public String getSuffix() {
        return _suffixET.getText().toString();
    }

    public String getCivilStat() {
        return _civilStatCTV.getText().toString();
    }

    public String getNationality() {
        return _nationalityET.getText().toString();
    }

    public String getAddress() {
        return _addressET.getText().toString();
    }

    public String getDateBirth() {
        return _dateBtn.getText().toString();
    }
}