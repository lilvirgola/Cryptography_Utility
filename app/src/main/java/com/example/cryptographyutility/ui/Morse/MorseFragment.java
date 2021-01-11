package com.example.cryptographyutility.ui.Morse;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.cryptographyutility.R;
import com.google.android.material.textfield.TextInputEditText;

public class MorseFragment extends Fragment {

    private MorseViewModel Morse;

    private char[] letters= { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l',
            'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x',
            'y', 'z', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0',
            ',', '.', '?' };

    private String[] morse = { ".-", "-...", "-.-.", "-..", ".", "..-.", "--.", "....", "..",
            ".---", "-.-", ".-..", "--", "-.", "---", ".---.", "--.-", ".-.",
            "...", "-", "..-", "...-", ".--", "-..-", "-.--", "--..", ".----",
            "..---", "...--", "....-", ".....", "-....", "--...", "---..", "----.",
            "-----", "--..--", ".-.-.-", "..--.." };

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        Morse =
                new ViewModelProvider(this).get(MorseViewModel.class);
        View root = inflater.inflate(R.layout.fragment_morse, container, false);
        TextInputEditText input = root.findViewById(R.id.inputMorse);
        TextInputEditText output = root.findViewById(R.id.outputMorse);
        RadioButton encode = root.findViewById(R.id.encodeMorse);
        RadioButton decode = root.findViewById(R.id.decodeMorse);
        final TextView textView = root.findViewById(R.id.text_morse);
        Morse.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        Button btn = root.findViewById(R.id.btnMorse);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(decode.isChecked()){
                    output.setText(DecodeMorse(input.getText().toString()));
                }
                else{
                    output.setText(EncodeMorse(input.getText().toString()));
                }
            }
        });
        return root;
    }


    public String EncodeMorse(String text)
    {
        char[] chars = text.toCharArray();

        String encoded= "";
        for (int i = 0; i < chars.length; i++){
            for (int j = 0; j < letters.length; j++){

                if (letters[j] == chars[i]){
                    encoded= encoded+ morse[j] + " ";
                }
            }
        }
        return encoded;
    }

    public String DecodeMorse(String text) {
        String decoded = "";
        String[] morseChars = text.split(" ");
        for (int i = 0; i < morseChars.length; i++) {
            for (int j = 0; j < morse.length; j++) {
                if (morse[j].equals(morseChars[i])) {
                    decoded = decoded + letters[j];
                }
            }
        }
        return decoded;
    }
}