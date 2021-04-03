package com.T05.krowdtrialz.ui.contributors;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.T05.krowdtrialz.R;
import com.T05.krowdtrialz.model.experiment.Experiment;
import com.T05.krowdtrialz.model.user.User;
import com.T05.krowdtrialz.util.Database;
import com.google.firebase.firestore.ListenerRegistration;

import java.util.ArrayList;

/**
 * Custom array adapter for ListView to show the list of contributors of an experiment
 * along with a checkbox that indicated if they are ignored by the experiment.
 */
public class ContributorList extends ArrayAdapter<User> {
    private static final String TAG = "CONTRIBUTOR LIST";

    private ArrayList<User> contributors;
    private Context context;
    private Experiment experiment;
    private Database db;

    public ContributorList(Context context, ArrayList<User> contributors, Experiment experiment, Database db){
        super(context,0, contributors);
        this.contributors = contributors;
        this.context = context;
        this.experiment = experiment;
        this.db = db;
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
        Button ignoreButton = view.findViewById(R.id.ignore_contributor_button);

        contributorName.setText(contributor.getUserName());

        if(experiment.isIgnored(contributor)){
            ignoreButton.setText("Un-Ignore");
        } else {
            ignoreButton.setText("Ignore");
        }

        ignoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(experiment.isIgnored(contributor)){
                    experiment.removeIgnoredUser(contributor);
                    db.updateExperiment(experiment);
                    ignoreButton.setText("Ignore");
                    Log.d(TAG, "Un-Ignored User");
                } else{
                    experiment.ignoreUser(contributor);
                    db.updateExperiment(experiment);
                    ignoreButton.setText("Un-Ignore");
                    Log.d(TAG, "Ignored User");
                }
            }
        });

        return view;
    }
}