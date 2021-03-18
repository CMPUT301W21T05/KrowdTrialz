package com.T05.krowdtrialz.ui.experimentDetails;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.T05.krowdtrialz.R;
import com.T05.krowdtrialz.model.experiment.Experiment;
import com.T05.krowdtrialz.model.experiment.IntegerExperiment;
import com.T05.krowdtrialz.model.experiment.MeasurementExperiment;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ExperimentStatistics#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ExperimentStatistics extends Fragment {
    public static final String tabName = "Stats";

    private Experiment experiment = null;

    private TextView mean;
    private TextView stdev;
    private TextView median;
    private TextView quartile1;
    private TextView quartile2;
    private TextView quartile3;
    private TextView quartile4;

    public ExperimentStatistics() {
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
     * @return A new instance of fragment ExperimentStatistics.
     */
    public static ExperimentStatistics newInstance(Experiment experiment) {
        ExperimentStatistics fragment = new ExperimentStatistics();
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
        View root = inflater.inflate(R.layout.fragment_experiment_statistics, container, false);

        populateTrialResults(root);

        return root;
    }

    /**
     * This method fills out Mean, Standard deviation, Median and maybe some trial results
     * @author
     *  Ricky Au
     */
    private void populateTrialResults(View view) {
        String type = experiment.getType();
        Double d = Double.NaN;
        String nan = d.toString();
        String stdString = nan;
        String meanString = nan;
        String medianString = nan;
        String q1String = nan;
        String q2String = nan;
        String q3String = nan;
        String q4String = nan;


        Double zero = 0.0;
        Double stdDouble = zero;
        Double meanDouble = zero;
        Double medianDouble = zero;
        Double q1Double = zero;
        Double q2Double = zero;
        Double q3Double = zero;
        Double q4Double = zero;

        if (type.equals("Measurement")) {
            Log.d("test", "Type is measurement");
            meanDouble = ((MeasurementExperiment) experiment).getMean();
            stdDouble = ((MeasurementExperiment) experiment).getStdDev();
            medianDouble = ((MeasurementExperiment) experiment).getMedian();

            // in the case not enough experiments for getQuartile
            try {
                q1Double = ((MeasurementExperiment) experiment).getQuartile(1);
                q2Double = ((MeasurementExperiment) experiment).getQuartile(2);
                q3Double = ((MeasurementExperiment) experiment).getQuartile(3);
                q4Double = ((MeasurementExperiment) experiment).getQuartile(4);
            }
            catch(Exception e){
                q1Double = Double.NaN;
                q2Double = Double.NaN;
                q3Double = Double.NaN;
                q4Double = Double.NaN;
            }

        } else if (type.equals("Integer")) {
            meanDouble = ((IntegerExperiment) experiment).getMean();
            stdDouble = ((IntegerExperiment) experiment).getStdDev();
            medianDouble = ((IntegerExperiment) experiment).getMedian();

            // in the case not enough experiments for getQuartile
            try {
                q1Double = ((IntegerExperiment) experiment).getQuartile(1);
                q2Double = ((IntegerExperiment) experiment).getQuartile(2);
                q3Double = ((IntegerExperiment) experiment).getQuartile(3);
                q4Double = ((IntegerExperiment) experiment).getQuartile(4);
            }
            catch(Exception e){
                Log.e("Test",e.toString());
                q1Double = Double.NaN;
                q2Double = Double.NaN;
                q3Double = Double.NaN;
                q4Double = Double.NaN;
            }
        }else{
            meanDouble = Double.NaN;
            stdDouble = Double.NaN;
            q1Double = Double.NaN;
            q2Double = Double.NaN;
            q3Double = Double.NaN;
            q4Double = Double.NaN;
        }

        if (meanDouble.equals(Double.NaN)){
            meanString = Double.toString(meanDouble);

        }else{
            BigDecimal meanRounded = BigDecimal.valueOf(meanDouble);
            meanRounded = meanRounded.setScale(5, RoundingMode.HALF_UP);
            meanString = Double.toString(meanRounded.doubleValue());
        }
        if (medianDouble.equals(Double.NaN)){

            medianString = Double.toString(medianDouble);

        }else{
            BigDecimal medianRounded = BigDecimal.valueOf(medianDouble);
            medianRounded = medianRounded.setScale(5, RoundingMode.HALF_UP);
            medianString = Double.toString(medianRounded.doubleValue());
        }
        if (stdDouble.equals(Double.NaN)){

            stdString = Double.toString(stdDouble);

        }else{
            BigDecimal stdRounded = BigDecimal.valueOf(stdDouble);
            stdRounded = stdRounded.setScale(5, RoundingMode.HALF_UP);
            stdString = Double.toString(stdRounded.doubleValue());
        }


        q1String = Double.toString(q1Double);
        q2String = Double.toString(q2Double);
        q3String = Double.toString(q3Double);
        q4String = Double.toString(q4Double);


        // fill out Mean
        mean = view.findViewById(R.id.mean);
        mean.setText(meanString);

        // fill out Standard deviation
        stdev = view.findViewById(R.id.stddev);
        stdev.setText(stdString);

        // fill out Median
        median = view.findViewById(R.id.median);
        median.setText(medianString);

        // fill out Quartiles
        quartile1 = view.findViewById(R.id.q1);
        quartile1.setText(q1String);
        quartile2 = view.findViewById(R.id.q2);
        quartile2.setText(q2String);
        quartile3 = view.findViewById(R.id.q3);
        quartile3.setText(q3String);
        quartile4 = view.findViewById(R.id.q4);
        quartile4.setText(q4String);

    }
}