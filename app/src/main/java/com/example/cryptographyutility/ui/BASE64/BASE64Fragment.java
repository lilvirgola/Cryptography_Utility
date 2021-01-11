package com.example.cryptographyutility.ui.BASE64;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.cryptographyutility.R;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Base64;

public class BASE64Fragment extends Fragment {

    private BASE64ViewModel BASE64;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        BASE64 =
                new ViewModelProvider(this).get(BASE64ViewModel.class);
        View root = inflater.inflate(R.layout.fragment_base64, container, false);
        TextInputEditText input = root.findViewById(R.id.inputBase64);
        TextInputEditText output = root.findViewById(R.id.outputBase64);
        RadioButton encode = root.findViewById(R.id.encodeBase64);
        RadioButton decode = root.findViewById(R.id.decodeBase64);
        final TextView textView = root.findViewById(R.id.text_base64);
        BASE64.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        Button btn = root.findViewById(R.id.btnBase64);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(decode.isChecked()){
                    output.setText(DecodeBase64(input.getText().toString()));
                }
                else{
                    output.setText(EncodeBase64(input.getText().toString()));
                }
            }
        });
        return root;
    }


    public String EncodeBase64(String text)
    {
        String encoded = Base64.getEncoder().encodeToString(text.getBytes());
        return encoded;
    }


    public String DecodeBase64(String text) {
        byte[] decodedBytes = Base64.getDecoder().decode(text);
        String decoded = new String(decodedBytes);
        return decoded;
    }
}