package com.T05.krowdtrialz.ui.owner;

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

public class OwnerFragment extends Fragment {

    private OwnerViewModel ownerViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ownerViewModel =
                new ViewModelProvider(this).get(OwnerViewModel.class);
        View root = inflater.inflate(R.layout.fragment_owner, container, false);
        //final TextView textView = root.findViewById(R.id.text_notifications);
        ownerViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                //textView.setText(s);
            }
        });
        return root;
    }
}