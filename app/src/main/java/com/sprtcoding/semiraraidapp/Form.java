package com.sprtcoding.semiraraidapp;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.shuhart.stepview.StepView;

import java.util.HashMap;

public class Form extends BaseActivity {

    private LinearLayout _back, _next;
    private TextView _back_text,_next_text;
    private ImageView _next_img;
    private StepView _stepView;
    private int _currentStep = 0;

    private String _fName,_mName,_Surname,_Suffix, _civilStatus, _nationality, _address, _regNo, _preNo, _validation, _idType, _dateOOfBirth;
    private Uri _photoUri;
    private String myUri;
    private boolean isValid;
    private FirebaseAuth _mAuth;
    private FirebaseUser _mUser;
    private FirebaseDatabase _db;
    private DatabaseReference _myRef;
    private StorageReference _myStorageRef;
    private ProgressDialog loadingBar;
    AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);
        _var();

        loadingBar = new ProgressDialog(this);
        loadingBar.setTitle("Saving!");
        loadingBar.setMessage("Please wait...");
        loadingBar.setCanceledOnTouchOutside(true);

        _mAuth = FirebaseAuth.getInstance();
        _mUser = _mAuth.getCurrentUser();
        _db = FirebaseDatabase.getInstance();
        _myRef = _db.getReference("barangayResidentID");
        _myStorageRef = FirebaseStorage.getInstance().getReference("Photos");

        _stepView.setStepsNumber(3); // Change this to the number of steps in your form

        // Show the first step fragment
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new StepOneFragment())
                .commit();

        if (_currentStep == 0) {
            _back.setVisibility(View.GONE);
        }

        _next.setOnClickListener(this::onNextButtonClicked);
        _back.setOnClickListener(this::onBackButtonClicked);


    }
    public void onNextButtonClicked(View view) {
        // Increment the current step counter
        _currentStep++;

        // Update the StepView to show the current step
        _stepView.go(_currentStep, true);

        // Replace the current fragment with the next one
        if (_currentStep == 0) {
            _back.setVisibility(View.GONE);
            _next_img.setVisibility(View.VISIBLE);
        } else if (_currentStep == 1) {
            StepOneFragment stepOneFragment = (StepOneFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container);
            _fName = stepOneFragment.getFirstName();
            _mName = stepOneFragment.getMiddleName();
            _Surname = stepOneFragment.getSurname();
            _Suffix = stepOneFragment.getSuffix();
            _civilStatus = stepOneFragment.getCivilStat();
            _nationality = stepOneFragment.getNationality();
            _dateOOfBirth = stepOneFragment.getDateBirth();
            _address = stepOneFragment.getAddress();
            replaceFragment(new StepTwoFragment());
            _next_img.setVisibility(View.VISIBLE);
            _back.setVisibility(View.VISIBLE);
        } else if (_currentStep == 2) {
            StepTwoFragment stepTwoFragment = (StepTwoFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container);
            _regNo = stepTwoFragment.getRegNo();
            _preNo = stepTwoFragment.getPreNo();
            _validation = stepTwoFragment.getValidation();
            replaceFragment(new StepThreeFragment());
            _back.setVisibility(View.VISIBLE);
            _next_text.setText("Submit");
            _next_img.setVisibility(View.GONE);
        } else {
            StepThreeFragment stepThreeFragment = (StepThreeFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container);
            _idType = stepThreeFragment.getIdType();
            _photoUri = stepThreeFragment.getPhoto();
            _saveData();
        }
    }

    private void _saveData() {
        loadingBar.show();
        if(_fName.isEmpty()) {
            Toast.makeText(this, "First Name is empty!", Toast.LENGTH_SHORT).show();
            loadingBar.dismiss();
        }else if(_mName.isEmpty()) {
            Toast.makeText(this, "Middle Name is empty!", Toast.LENGTH_SHORT).show();
            loadingBar.dismiss();
        }else if(_Surname.isEmpty()) {
            Toast.makeText(this, "Surname is empty!", Toast.LENGTH_SHORT).show();
            loadingBar.dismiss();
        }else if(_civilStatus.isEmpty()) {
            Toast.makeText(this, "Civil Status is empty!", Toast.LENGTH_SHORT).show();
            loadingBar.dismiss();
        }else if(_nationality.isEmpty()) {
            Toast.makeText(this, "Nationality is empty!", Toast.LENGTH_SHORT).show();
            loadingBar.dismiss();
        }else if(_address.isEmpty()) {
            Toast.makeText(this, "Address is empty!", Toast.LENGTH_SHORT).show();
            loadingBar.dismiss();
        }else if(_dateOOfBirth.isEmpty()) {
            Toast.makeText(this, "Date of Birth is empty!", Toast.LENGTH_SHORT).show();
            loadingBar.dismiss();
        }else if(_regNo.isEmpty()) {
            Toast.makeText(this, "Registration Number is empty!", Toast.LENGTH_SHORT).show();
            loadingBar.dismiss();
        }else if(_validation.isEmpty()) {
            Toast.makeText(this, "Valid Until is empty!", Toast.LENGTH_SHORT).show();
            loadingBar.dismiss();
        }else if(_idType == null) {
            Toast.makeText(this, "Please select Type of ID!", Toast.LENGTH_SHORT).show();
            loadingBar.dismiss();
        }else {
            uploadImage();
            String selectedOption = "";
            if(_idType.equals("Registered Voters")) {
                selectedOption = "Green Card";
            }else if(_idType.equals("Not Registered Voters")) {
                selectedOption = "Yellow Card";
            }else if(_idType.equals("Below 6 Months")) {
                selectedOption = "White Card";
            }
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("ID", _regNo);
            hashMap.put("FirstName", _fName);
            hashMap.put("MiddleName", _mName);
            hashMap.put("Surname", _Surname);
            hashMap.put("Suffix", _Suffix);
            hashMap.put("CivilStatus", _civilStatus);
            hashMap.put("DateOfBirth", _dateOOfBirth);
            hashMap.put("RegistrationNumber", _regNo);
            hashMap.put("PrecinctNumber", _preNo);
            hashMap.put("ValidUntil", _validation);
            hashMap.put("Nationality", _nationality);
            hashMap.put("Address", _address);
            hashMap.put("IDType", selectedOption);
            hashMap.put("isPrinted", "Need Signature");

            _myRef.child(_regNo).updateChildren(hashMap).addOnCompleteListener(task -> {
                if(task.isSuccessful()) {
                    loadingBar.dismiss();
                    Toast.makeText(this, "Saved Successfully!", Toast.LENGTH_SHORT).show();
                    _currentStep = 0;
                    // Mark the previous step as completed
                    _stepView.done(false);

                    // Update the StepView to show the current step
                    _stepView.go(_currentStep, true);
                    _back.setVisibility(View.GONE);
                    _next_img.setVisibility(View.VISIBLE);
                    _next_text.setText("Next");
                    replaceFragment(new StepOneFragment());
                }
            });

        }
    }

    public void onBackButtonClicked(View view) {
        // Decrement the current step counter
        _currentStep--;

        // Update the StepView to show the current step
        _stepView.done(false);
        _stepView.go(_currentStep, true);

        // Pop the current fragment off the back stack
        getSupportFragmentManager().popBackStack();
        _next_text.setText("Next");
        if (_currentStep == 0) {
            _back.setVisibility(View.GONE);
            _next_img.setVisibility(View.VISIBLE);
            replaceFragment(new StepOneFragment());
        } else if (_currentStep == 1) {
            replaceFragment(new StepTwoFragment());
            _back.setVisibility(View.VISIBLE);
            _next_img.setVisibility(View.VISIBLE);
        } else if (_currentStep == 2) {
            replaceFragment(new StepThreeFragment());
            _back.setVisibility(View.VISIBLE);
            _next_text.setText("Submit");
            _next_img.setVisibility(View.GONE);
        }
    }

    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void uploadImage() {
        if(_photoUri != null) {
            StorageReference pics =_myStorageRef.child(_fName + "_" + _Surname + " ." + getFileExtension(_photoUri));
            pics.putFile(_photoUri)
                    .addOnCompleteListener(task -> {
                        UploadTask uploadTask = pics.putFile(_photoUri);

                        uploadTask.continueWithTask(task1 -> {
                            if(!task.isSuccessful()) {
                                throw task.getException();
                            }
                            return pics.getDownloadUrl();
                        }).addOnCompleteListener(task1 -> {
                            if(task1.isSuccessful()) {
                                Uri downloadURI = (Uri) task1.getResult();
                                assert downloadURI != null;
                                myUri = downloadURI.toString();

                                HashMap<String, Object> hashMap = new HashMap<>();
                                hashMap.put("Photo", myUri);
                                _myRef.child(_regNo).updateChildren(hashMap);
                            }
                        });
                    }).addOnFailureListener(e -> {
                        Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        }else {
            Toast.makeText(this, "Please take a Picture!", Toast.LENGTH_SHORT).show();
        }
    }

    private void replaceFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit();
    }

    private void _var() {
        _stepView = findViewById(R.id.step_view);
        _back = findViewById(R.id.back);
        _next = findViewById(R.id.next);
        _back_text = findViewById(R.id.back_text);
        _next_text = findViewById(R.id.next_text);
        _next_img = findViewById(R.id.next_img);
    }

    @Override
    protected void onStart() {
        super.onStart();
        overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(Form.this);
        alertDialog = builder.create();
        builder.setTitle("Exit")
                .setMessage("Are you sure you want to Exit?")
                .setCancelable(true)
                .setPositiveButton("Yes", (dialogInterface, i) -> {
                    alertDialog.show();
                    finish();
                })
                .setNegativeButton("No", (dialogInterface, i) -> {
                    dialogInterface.cancel();
                    super.onBackPressed();
                    // Decrement the current step counter
                    _currentStep--;

                    // Update the StepView to show the current step
                    _stepView.done(false);
                    _stepView.go(_currentStep, true);

                    // Pop the current fragment off the back stack
                    getSupportFragmentManager().popBackStack();
                    _next_text.setText("Next");
                    if (_currentStep == 0) {
                        _back.setVisibility(View.GONE);
                        _next_img.setVisibility(View.VISIBLE);
                        replaceFragment(new StepOneFragment());
                    } else if (_currentStep == 1) {
                        replaceFragment(new StepTwoFragment());
                        _back.setVisibility(View.VISIBLE);
                        _next_img.setVisibility(View.VISIBLE);
                    } else if (_currentStep == 2) {
                        replaceFragment(new StepThreeFragment());
                        _back.setVisibility(View.VISIBLE);
                        _next_text.setText("Submit");
                        _next_img.setVisibility(View.GONE);
                    }
                })
                .show();
    }
}