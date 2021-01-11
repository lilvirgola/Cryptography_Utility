package com.example.cryptographyutility.ui.Morse;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.cryptographyutility.R;

public class MorseViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public MorseViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Morse");
    }

    public LiveData<String> getText() {
        return mText;
    }
}