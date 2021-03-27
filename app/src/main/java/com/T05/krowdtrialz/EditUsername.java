package com.T05.krowdtrialz;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.T05.krowdtrialz.model.user.User;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EditUsername#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditUsername extends Fragment {
    private final String TAG = "EditUsername";

    private User user;

    public EditUsername() {
        // Required empty public constructor
    }

    public void injectUser(User user) {
        this.user = user;
    }

    public static EditUsername newInstance(User user) {
        EditUsername fragment = new EditUsername();
        fragment.injectUser(user);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_edit_username, container, false);

        Button submit = root.findViewById(R.id.submit_username);
        EditText editUsernameText = root.findViewById(R.id.edit_username_text);

        editUsernameText.setText(user.getUserName());

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editUsernameText.getText().toString().isEmpty()) {
                    // if it's empty, do nothing
                    getFragmentManager().popBackStack();
                }
            }
        });

        return root;
    }
}