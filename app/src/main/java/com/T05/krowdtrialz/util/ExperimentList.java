package com.T05.krowdtrialz.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.T05.krowdtrialz.R;
import com.T05.krowdtrialz.model.experiment.Experiment;

import java.util.ArrayList;

public class ExperimentList extends ArrayAdapter<Experiment> {
    private ArrayList<Experiment> experiments;
    private Context context;

    private com.google.android.material.textview.MaterialTextView experimentOwner;
    private com.google.android.material.textview.MaterialTextView experimentDescription;
    private com.google.android.material.checkbox.MaterialCheckBox experimentStatusCheck;

    public ExperimentList (Context context, ArrayList<Experiment> experiments) {
        super(context, 0, experiments);
        this.experiments = experiments;
        this.context = context;
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
        
        return view;
    }
}
