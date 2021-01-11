package com.example.cryptographyutility.ui.BASE64;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.cryptographyutility.R;

public class BASE64ViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public BASE64ViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("BASE64");
    }

    public LiveData<String> getText() {
        return mText;
    }
}