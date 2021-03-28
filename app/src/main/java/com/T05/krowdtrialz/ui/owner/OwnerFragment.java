package com.T05.krowdtrialz.ui.owner;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

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

    public static final int DIALOG_FRAGMENT = 1;

    public OwnerFragment() {
        super();
        db = Database.getInstance();
        user = db.getDeviceUser();
    }

    public OwnerFragment(User user) {
        super();
        db = Database.getInstance();
        this.user = user;
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ownerViewModel =
                new ViewModelProvider(this).get(OwnerViewModel.class);
        View root = inflater.inflate(R.layout.fragment_owner, container, false);

        final com.google.android.material.textview.MaterialTextView profileUsername = root.findViewById(R.id.profile_username);
        final com.google.android.material.textview.MaterialTextView profileName = root.findViewById(R.id.profile_name);
        final com.google.android.material.textview.MaterialTextView profileEmail = root.findViewById(R.id.profile_email);
        final ListView usersExperiments = root.findViewById(R.id.owner_exp_list);

        profileUsername.setText(user.getUserName());
        profileEmail.setText(user.getEmail());
        profileName.setText(user.getName());

        experimentAdapter = new ExperimentList(this.getContext(), new ArrayList<>());

        usersExperiments.setAdapter(experimentAdapter);

        if (user.equals(db.getDeviceUser())) {
            /**
             * This is the device user - let all items be changed.
             */
            profileUsername.setClickable(true);
            profileName.setClickable(true);
            profileEmail.setClickable(true);

            profileUsername.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DialogFragment dialog = EditUserDialogFragment.newInstance(user.getUserName(), "Edit Username", new OnChangeCallback() {
                        @Override
                        public void onDone(String info) {
                            if (info.isEmpty()) {
                                return;
                            }

                            user.setUserName(info);

                            db.updateDeviceUser(user);

                            profileUsername.setText(info);

                            Log.d(TAG, "UPDATED USER");
                        }
                    });
                    dialog.setTargetFragment(OwnerFragment.this, DIALOG_FRAGMENT);
                    dialog.show(getFragmentManager().beginTransaction(), "Edit");
                }
            });

            profileName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DialogFragment dialog = EditUserDialogFragment.newInstance(user.getName(), "Edit Name", new OnChangeCallback() {
                        @Override
                        public void onDone(String info) {
                            if (info.isEmpty()) {
                                return;
                            }

                            user.setName(info);

                            db.updateDeviceUser(user);

                            profileName.setText(info);

                            Log.d(TAG, "UPDATED USER");
                        }
                    });
                    dialog.setTargetFragment(OwnerFragment.this, DIALOG_FRAGMENT);
                    dialog.show(getFragmentManager().beginTransaction(), "Edit");
                }
            });

            profileEmail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DialogFragment dialog = EditUserDialogFragment.newInstance(user.getEmail(), "Edit Email", new OnChangeCallback() {
                        @Override
                        public void onDone(String info) {
                            if (info.isEmpty()) {
                                return;
                            }

                            user.setEmail(info);

                            db.updateDeviceUser(user);

                            profileEmail.setText(info);

                            Log.d(TAG, "UPDATED USER");
                        }
                    });
                    dialog.setTargetFragment(OwnerFragment.this, DIALOG_FRAGMENT);
                    dialog.show(getFragmentManager().beginTransaction(), "Edit");
                }
            });

        } else {
            /**
             * Not owner, don't let these be clicked.
             */
            profileUsername.setClickable(false);
            profileName.setClickable(false);
            profileEmail.setClickable(false);
        }

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

    /**
     * callback interface for editor dialog
     */
    public interface OnChangeCallback {
        public void onDone(String info);
    }

}