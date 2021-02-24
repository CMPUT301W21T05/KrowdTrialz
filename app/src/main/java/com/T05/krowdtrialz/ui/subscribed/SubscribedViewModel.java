package com.T05.krowdtrialz.ui.subscribed;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SubscribedViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public SubscribedViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is home fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}