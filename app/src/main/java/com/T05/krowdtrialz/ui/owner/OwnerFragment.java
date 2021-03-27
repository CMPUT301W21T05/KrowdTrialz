package com.T05.krowdtrialz.ui.owner;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.T05.krowdtrialz.EditUsername;
import com.T05.krowdtrialz.R;
import com.T05.krowdtrialz.model.experiment.Experiment;
import com.T05.krowdtrialz.model.user.User;
import com.T05.krowdtrialz.util.Database;
import com.T05.krowdtrialz.util.ExperimentList;

import java.util.ArrayList;

public class OwnerFragment extends Fragment {

    private final String TAG = "OwnerFragment";

    private OwnerViewModel ownerViewModel;
    private User user;
    private Database db;
    private ArrayAdapter<Experiment> experimentAdapter;

    public OwnerFragment() {
        super();
        db = Database.getInstance();
        user = db.getDeviceUser();
    }

    public OwnerFragment(User user) {
        super();
        this.user = user;
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ownerViewModel =
                new ViewModelProvider(this).get(OwnerViewModel.class);
        View root = inflater.inflate(R.layout.fragment_owner, container, false);

        final Button editUsernameButton = root.findViewById(R.id.edit_profile_button);
        final EditText profileUsername = root.findViewById(R.id.profile_username);
        final ListView usersExperiments = root.findViewById(R.id.owner_exp_list);

        experimentAdapter = new ExperimentList(this.getContext(), new ArrayList<>());

        usersExperiments.setAdapter(experimentAdapter);

        if (user.equals(db.getDeviceUser())) {
            editUsernameButton.setVisibility(Button.VISIBLE);
            editUsernameButton.setOnClickListener(new View.OnClickListener() {
                @SuppressLint("ResourceType")
                @Override
                public void onClick(View v) {
                    getActivity().getFragmentManager();
                    Fragment nextFragment = EditUsername.newInstance(user);
                    FragmentManager manager = getActivity().getSupportFragmentManager();
                    FragmentTransaction transaction = manager.beginTransaction();
                    transaction.replace(R.layout.fragment_owner, nextFragment);
                    transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    transaction.commit();
                }
            });
        } else {
            editUsernameButton.setVisibility(Button.GONE);
        }

        profileUsername.setText(user.getUserName());

        db.getExperimentsByOwner(user, new Database.QueryExperimentsCallback() {
            @Override
            public void onSuccess(ArrayList<Experiment> experiments) {
                Log.d(TAG, "Got owned experiments");
                experimentAdapter.clear();
                experimentAdapter.addAll(experiments);
            }

            @Override
            public void onFailure() {
                Log.e(TAG, "Could not get owned experiments");
            }
        });

        return root;
    }
}