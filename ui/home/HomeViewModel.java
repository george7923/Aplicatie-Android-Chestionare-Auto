package com.example.autoexpert.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class HomeViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public HomeViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Bine ati venit la AUTOEXPERT! Noi suntem solutia pentru 26/26!");
    }

    public LiveData<String> getText() {
        return mText;
    }
}