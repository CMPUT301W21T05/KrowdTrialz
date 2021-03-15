package com.T05.krowdtrialz.ui.contributors;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.T05.krowdtrialz.R;
import com.T05.krowdtrialz.model.experiment.Experiment;
import com.T05.krowdtrialz.model.user.User;
import com.T05.krowdtrialz.util.Database;

import java.util.ArrayList;

public class ContributorList extends ArrayAdapter<User> {

    private ArrayList<User> contributors;
    private Context context;
    private Experiment experiment;

    public ContributorList(Context context, ArrayList<User> contributors, Experiment experiment){
        super(context,0, contributors);
        this.contributors = contributors;
        this.context = context;
        this.experiment = experiment;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;

        if(view == null){
            view = LayoutInflater.from(context).inflate(R.layout.contributor_row, parent,false);
        }

        User contributor = contributors.get(position);

        TextView contributorName = view.findViewById(R.id.contributor_name_textView);
        CheckBox ignoreCheckBox = view.findViewById(R.id.ignore_contributor_checkbox);

        contributorName.setText(contributor.getName());

        //set checkbox if user is ignored
        if(experiment.isIgnored(contributor)){
            ignoreCheckBox.setChecked(true);
        } else {
            ignoreCheckBox.setChecked(false);
        }

        ignoreCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    if(!experiment.isIgnored(contributor)){
                        experiment.ignoreUser(contributor);
                        Database.getInstance().updateExperiment(experiment);
                    }
                } else {
                    if(experiment.isIgnored(contributor)){
                        experiment.removeIgnoredUser(contributor);
                        Database.getInstance().updateExperiment(experiment);
                    }
                }
            }
        });

        return view;
    }
}