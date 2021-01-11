package com.example.cryptographyutility.ui.ROT13;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.cryptographyutility.R;

public class ROT13ViewModel extends ViewModel {

    private MutableLiveData<String> mText;



    public ROT13ViewModel() {

        mText = new MutableLiveData<>();
        mText.setValue("ROT13");
    }

    public LiveData<String> getText() {
        return mText;
    }
}