package com.example.cryptographyutility.ui.AES;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.cryptographyutility.MainActivity;
import com.example.cryptographyutility.R;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class AESFragment extends Fragment {

    private AESViewModel AES;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        AES =
                new ViewModelProvider(this).get(AESViewModel.class);
        View root = inflater.inflate(R.layout.fragment_aes, container, false);
        TextInputEditText inputKey = root.findViewById(R.id.inputKeyAES);
        TextInputEditText inputMessage = root.findViewById(R.id.inputMessageAES);
        TextInputEditText inputIv = root.findViewById(R.id.inputIvAES);
        TextInputEditText output = root.findViewById(R.id.outputAES);
        RadioButton encode = root.findViewById(R.id.encodeAES);
        RadioButton decode = root.findViewById(R.id.decodeAES);
        final TextView textView = root.findViewById(R.id.text_aes);
        AES.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        Button btn = root.findViewById(R.id.btnAES);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(inputIv.getText().length()<16 || inputIv.length()>16 ||inputKey.getText().length()<16 || inputKey.getText().length()<16){
                    Toast.makeText(getContext(), R.string.wrongInput, Toast.LENGTH_LONG ).show();
                }
                else {
                    if (decode.isChecked()) {
                        output.setText(decryptAES(inputKey.getText().toString(),inputIv.getText().toString(),inputMessage.getText().toString()));
                    } else {
                        output.setText(encryptAES(inputKey.getText().toString(),inputIv.getText().toString(),inputMessage.getText().toString()));
                    }
                }
            }
        });
        return root;
    }

    public String encryptAES(String key, String initVector, String value) {
        try {
            IvParameterSpec iv = new IvParameterSpec(initVector.getBytes("UTF-8"));
            SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);

            byte[] encrypted = cipher.doFinal(value.getBytes());
            return encodeBase64(encrypted);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }

    public String decryptAES(String key, String initVector, String encrypted) {
        try {
            IvParameterSpec iv = new IvParameterSpec(initVector.getBytes("UTF-8"));
            SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);

            byte[] original = cipher.doFinal(decodeBase64(encrypted.getBytes()));

            return new String(original);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }

    public String encodeBase64(byte[] text)
    {
        String encoded = Base64.getEncoder().encodeToString(text);
        return encoded;
    }


    public byte[] decodeBase64(byte[] text) {
        byte[] decodedBytes = Base64.getDecoder().decode(text);
        return decodedBytes;
    }
}