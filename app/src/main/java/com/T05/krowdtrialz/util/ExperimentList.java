package com.T05.krowdtrialz.util;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.T05.krowdtrialz.MainActivity;
import com.T05.krowdtrialz.R;
import com.T05.krowdtrialz.model.experiment.Experiment;
import com.T05.krowdtrialz.model.user.User;
import com.T05.krowdtrialz.ui.experimentDetails.ExperimentDetailsActivity;

import java.util.ArrayList;

public class ExperimentList extends ArrayAdapter<Experiment> {

    private static final String TAG = "ExperimentList";
    private ArrayList<Experiment> experiments;
    private User deviceUser;
    private Context context;

    private com.google.android.material.textview.MaterialTextView experimentOwner;
    private com.google.android.material.textview.MaterialTextView experimentDescription;
    private com.google.android.material.checkbox.MaterialCheckBox experimentStatusCheck;

    public ExperimentList (Context context, ArrayList<Experiment> experiments, User deviceUser) {
        super(context, 0, experiments);
        this.experiments = experiments;
        this.context = context;
        this.deviceUser = deviceUser;
    }

    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;

        if (view == null){
            view = LayoutInflater.from(context).inflate(R.layout.experiment_list_element, parent, false);
        }

        Experiment experiment = experiments.get(position);

        experimentOwner = view.findViewById(R.id.experiment_list_user);
        experimentDescription = view.findViewById(R.id.experiment_list_description);
        experimentStatusCheck = view.findViewById(R.id.experiment_status_checkbox);

        experimentOwner.setText(experiment.getOwner().getUserName().toString());
        experimentDescription.setText(experiment.getDescription().toString());
        experimentStatusCheck.setChecked(experiment.isActive());

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;

                intent = new Intent(context, ExperimentDetailsActivity.class);

                intent.putExtra(MainActivity.EXTRA_EXPERIMENT_ID, experiment.getId());
                context.startActivity(intent);
            }
        });

        return view;
    }


}
