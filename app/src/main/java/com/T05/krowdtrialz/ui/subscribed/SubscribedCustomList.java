package com.T05.krowdtrialz.ui.subscribed;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.T05.krowdtrialz.R;
import com.T05.krowdtrialz.model.experiment.Experiment;

import java.util.ArrayList;

public class SubscribedCustomList extends ArrayAdapter<Experiment> {

    private Context context;
    private ArrayList<Experiment> experiments;

    public SubscribedCustomList(Context context, ArrayList<Experiment> experiments){
        super(context, 0, experiments);
        this.context = context;
        this.experiments = experiments;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;

        if(view == null){
            view = LayoutInflater.from(context).inflate(R.layout.subscribed_list_content, parent, false);
        }

        Experiment experiment = experiments.get(position);

        TextView expStatus = view.findViewById(R.id.exp_status_textView);
        TextView expOwner = view.findViewById(R.id.owner_username_textView);
        TextView expDesc = view.findViewById(R.id.exp_details_textView);

        // Need to add status field to experiments class
        //expStatus.setText();

        expOwner.setText(experiment.getOwner().getUserName());
        expDesc.setText(experiment.getDescription());

        return view;
    }
}
