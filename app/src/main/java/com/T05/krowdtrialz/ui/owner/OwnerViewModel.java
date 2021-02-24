package com.T05.krowdtrialz.ui.owner;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class OwnerViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public OwnerViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is notifications fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}