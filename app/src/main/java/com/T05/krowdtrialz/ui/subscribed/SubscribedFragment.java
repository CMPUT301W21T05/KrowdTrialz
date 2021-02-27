package com.T05.krowdtrialz.ui.subscribed;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.T05.krowdtrialz.R;

public class SubscribedFragment extends Fragment {

    private SubscribedViewModel subscribedViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        subscribedViewModel =
                new ViewModelProvider(this).get(SubscribedViewModel.class);
        View root = inflater.inflate(R.layout.fragment_subscribed, container, false);
        final TextView textView = root.findViewById(R.id.text_home);
        subscribedViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}