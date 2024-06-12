package com.example.autoexpert.ui.login;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class LogInViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public LogInViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Va rog sa va logati!");
    }

    public LiveData<String> getText() {
        return mText;
    }
}