package com.example.cryptographyutility.ui.ROT13;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.cryptographyutility.R;
import com.google.android.material.textfield.TextInputEditText;

public class ROT13Fragment extends Fragment {

    private ROT13ViewModel ROT13;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ROT13 =
                new ViewModelProvider(this).get(ROT13ViewModel.class);
        View root = inflater.inflate(R.layout.fragment_rot13, container, false);
        TextInputEditText input = root.findViewById(R.id.inputRot13);
        TextInputEditText output = root.findViewById(R.id.outputRot13);
        final TextView textView = root.findViewById(R.id.text_aes);
        ROT13.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        Button btn = root.findViewById(R.id.btnRot13);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                output.setText(rot13(input.getText().toString()));
            }
        });
        return root;
    }

    public CharSequence rot13(String s){
        char[] letters= { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l',
                'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x',
                'y', 'z'};
        CharSequence converted;
        char[] aux =  new char[s.length()];
        for(int i = 0; i < s.length(); i++) {
            for(int j = 0; j< letters.length; j++) {
                if (s.charAt(i) == letters[j]) {
                    if (j > 12) {
                        aux[i] = letters[j - 13];
                    } else {
                        aux[i] = letters[j + 13];
                    }
                }
                else if(s.charAt(i) == ' '){
                    aux[i] = ' ';
                }
            }
        }
        converted = new String(aux);
        return converted;
    }

}