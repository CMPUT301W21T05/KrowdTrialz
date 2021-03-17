package com.T05.krowdtrialz.ui.experimentDetails;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.T05.krowdtrialz.R;
import com.T05.krowdtrialz.model.experiment.Experiment;
import com.T05.krowdtrialz.model.user.User;
import com.T05.krowdtrialz.util.Database;

import org.apache.commons.math3.analysis.function.Exp;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ExperimentMore#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ExperimentMore extends Fragment {
    public static final String tabName = "More", TAG = "EXPERIMENT MORE";

    private Experiment experiment = null;

    private Database db;

    public ExperimentMore() {
        // Required empty public constructor
    }

    /**
     * Inject experiment into the fragment
     * @param experiment
     */
    public void injectExperiment(Experiment experiment) {
        this.experiment = experiment;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param experiment
     * @return A new instance of fragment ExperimentMore.
     */
    public static ExperimentMore newInstance(Experiment experiment) {
        ExperimentMore fragment = new ExperimentMore();
        fragment.injectExperiment(experiment);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_experiment_more, container, false);
        db = Database.getInstance();
        Button viewMapButton = root.findViewById(R.id.view_map_button);
        Button viewQandAButton = root.findViewById(R.id.view_qanda_button);
        Button endExperimentButton = root.findViewById(R.id.end_experiment_button);
        Button unpublishExperimentButton = root.findViewById(R.id.unpublish_experiment_button);

        User user = db.getDeviceUser();
        if (experiment.isOwner(user)) {
            endExperimentButton.setVisibility(Button.VISIBLE);
            unpublishExperimentButton.setVisibility(Button.VISIBLE);
        } else {
            endExperimentButton.setVisibility(Button.GONE);
            unpublishExperimentButton.setVisibility(Button.GONE);
        }

        viewMapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewMap(v);
            }
        });

        viewQandAButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewQnA(v);
            }
        });

        return root;
    }

    /**
     * This method allows user to view map of experiment
     * @author
     *  Ricky Au
     */
    public void viewMap(View view){
        Log.d(TAG, "view map");
        Toast.makeText(view.getContext(), "pressed view map",Toast.LENGTH_SHORT).show();
        // TODO: open map activity
    }

    /**
     * This method allows user to view map Questions and Answers for Experiment
     * @author
     *  Ricky Au
     */
    public void viewQnA(View view){
        Log.d(TAG, "view Q&A");
        Toast.makeText(view.getContext(), "pressed view Q&A",Toast.LENGTH_SHORT).show();
        // TODO: open Q&A activity
    }
}