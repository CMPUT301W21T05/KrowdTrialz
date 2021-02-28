package com.T05.krowdtrialz.ui.contributors;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.T05.krowdtrialz.R;
import com.T05.krowdtrialz.model.user.User;

import java.util.ArrayList;

public class ContributorList extends ArrayAdapter<User> {

    private ArrayList<User> contributors;
    private Context context;

    public ContributorList(Context context, ArrayList<User> contributors){
        super(context,0, contributors);
        this.contributors = contributors;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;

        if(view == null){
            view = LayoutInflater.from(context).inflate(R.layout.contributor_row, parent,false);
        }

        User contributor = contributors.get(position);

        TextView contributorName = view.findViewById(R.id.contributor_name_text_view);

        contributorName.setText(contributor.getName());

        return view;

    }
}