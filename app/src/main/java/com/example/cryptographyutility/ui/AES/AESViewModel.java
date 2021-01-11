package com.example.cryptographyutility.ui.AES;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class AESViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public AESViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("AES 128");
    }

    public LiveData<String> getText() {
        return mText;
    }
}