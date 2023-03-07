package com.sprtcoding.semiraraidapp;

import static android.app.Activity.RESULT_OK;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.github.drjacky.imagepicker.ImagePicker;

public class StepThreeFragment extends Fragment {
    private RadioGroup _radioGroup;
    private String _selectedOption;
    private ImageView _addPhoto;
    private ImageView _userPic;
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    public Uri imageUri;
    private String myUri;

    public StepThreeFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_step_three, container, false);
        _radioGroup = v.findViewById(R.id.radio_group);
        _userPic = v.findViewById(R.id.userPic);
        _addPhoto = v.findViewById(R.id.addPhoto);

        _addPhoto.setOnClickListener(view -> {
            ImagePicker
                    .Companion
                    .with(this)
                    .cameraOnly()
                    .maxResultSize(512, 512)
                    .start(REQUEST_IMAGE_CAPTURE);
        });

        _radioGroup.setOnCheckedChangeListener((radioGroup, checkedId) -> {
            RadioButton radioButton = v.findViewById(checkedId);
            _selectedOption = radioButton.getText().toString();
        });
        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            _userPic.setImageURI(imageUri);
        }
    }

    public String getIdType() {
        return _selectedOption;
    }

    public Uri getPhoto() {
        return imageUri;
    }
}