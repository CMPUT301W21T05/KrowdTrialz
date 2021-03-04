package com.T05.krowdtrialz.ui.publish;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.T05.krowdtrialz.R;

public class PublishFragment extends Fragment {

    private PublishViewModel publishViewModel;
    private final String TAG = "PublishFragment";


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        publishViewModel = new ViewModelProvider(this).get(PublishViewModel.class);
        View root = inflater.inflate(R.layout.fragment_publish, container, false);

        clearExperimentSettings(root);

        RadioGroup radioGroup = (RadioGroup) root.findViewById(R.id.experimentTypeRadioGroup);
        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            Log.d(TAG, "Radio changed: " + ((Integer) checkedId).toString());
            switch (checkedId) {
                case R.id.countExperimentRadio:
                    Log.d(TAG, "Count Experiment Selected");
                    countExperimentSelected(root);
                    break;
                case R.id.integerExperimentRadio:
                    Log.d(TAG, "Integer Exeperiment Selected");
                    integerExperimentSelected(root);
                    break;
                case R.id.binomialExperimentRadio:
                    Log.d(TAG, "Binomial Experiment Selected");
                    binomialExperimentSelected(root);
                    break;
                case R.id.measurementExperimentRadio:
                    Log.d(TAG, "Measurement Experiment Selected");
                    measurementExperimentSelected(root);
                    break;
                default:
                    Log.e(TAG, "Unknown Selection");
                    clearExperimentSettings(root);
            }
        });
        return root;
    }

    /**
     * Update the view to get binomial experiment settings
     *
     * @param view
     *  publish experiment view to operate on
     */
    private void binomialExperimentSelected(View view) {
        // adjust input box visibility
        view.findViewById(R.id.binomialPassCriteriaLayout).setVisibility(View.VISIBLE);
        view.findViewById(R.id.binomialPassCriteriaInput).setVisibility(View.VISIBLE);
        view.findViewById(R.id.binomialFailCriteriaLayout).setVisibility(View.VISIBLE);
        view.findViewById(R.id.binomialFailCriteriaInput).setVisibility(View.VISIBLE);
        view.findViewById(R.id.experimentVariableNameLayout).setVisibility(View.GONE);
        view.findViewById(R.id.experimentVariableNameInput).setVisibility(View.GONE);
    }

    /**
     * Adjust view to get count experiment settings
     *
     * @param view
     *  publish experiment view to operate on
     */
    private void countExperimentSelected(View view) {
        // Adjust visibility and change hint text
        view.findViewById(R.id.binomialPassCriteriaLayout).setVisibility(View.GONE);
        view.findViewById(R.id.binomialPassCriteriaInput).setVisibility(View.GONE);
        view.findViewById(R.id.binomialFailCriteriaLayout).setVisibility(View.GONE);
        view.findViewById(R.id.binomialFailCriteriaInput).setVisibility(View.GONE);
        com.google.android.material.textfield.TextInputLayout variableNameLayout = view.findViewById(R.id.experimentVariableNameLayout);
        variableNameLayout.setVisibility(View.VISIBLE);
        com.google.android.material.textfield.TextInputEditText variableNameInput = view.findViewById(R.id.experimentVariableNameInput);
        variableNameInput.setVisibility(View.VISIBLE);

        variableNameLayout.setHint("Name of variable to count");
    }

    /**
     * Adjust view to get integer experiment settings
     *
     * @param view
     *  publish experiment view to operate on
     */
    private void integerExperimentSelected(View view) {
        // Adjust visibility and change hint text
        view.findViewById(R.id.binomialPassCriteriaLayout).setVisibility(View.GONE);
        view.findViewById(R.id.binomialPassCriteriaInput).setVisibility(View.GONE);
        view.findViewById(R.id.binomialFailCriteriaLayout).setVisibility(View.GONE);
        view.findViewById(R.id.binomialFailCriteriaInput).setVisibility(View.GONE);
        com.google.android.material.textfield.TextInputLayout variableNameLayout = view.findViewById(R.id.experimentVariableNameLayout);
        variableNameLayout.setVisibility(View.VISIBLE);
        com.google.android.material.textfield.TextInputEditText variableNameInput = view.findViewById(R.id.experimentVariableNameInput);
        variableNameInput.setVisibility(View.VISIBLE);

        variableNameLayout.setHint("Name of integer result");
    }

    /**
     * Adjust view to get measurement experiment settings
     * @param view
     *  publish experiment view to operate on
     */
    private void measurementExperimentSelected(View view) {
        // Adjust visibility and change hint text
        view.findViewById(R.id.binomialPassCriteriaLayout).setVisibility(View.GONE);
        view.findViewById(R.id.binomialPassCriteriaInput).setVisibility(View.GONE);
        view.findViewById(R.id.binomialFailCriteriaLayout).setVisibility(View.GONE);
        view.findViewById(R.id.binomialFailCriteriaInput).setVisibility(View.GONE);
        com.google.android.material.textfield.TextInputLayout variableNameLayout = view.findViewById(R.id.experimentVariableNameLayout);
        variableNameLayout.setVisibility(View.VISIBLE);
        com.google.android.material.textfield.TextInputEditText variableNameInput = view.findViewById(R.id.experimentVariableNameInput);
        variableNameInput.setVisibility(View.VISIBLE);

        variableNameLayout.setHint("Name of measurement");
    }

    /**
     * Clear experiment-specific UI elements
     * @param view
     *  publish experiment view to operate on
     */
    private void clearExperimentSettings(View view) {
        view.findViewById(R.id.binomialPassCriteriaLayout).setVisibility(View.GONE);
        view.findViewById(R.id.binomialPassCriteriaInput).setVisibility(View.GONE);
        view.findViewById(R.id.binomialFailCriteriaLayout).setVisibility(View.GONE);
        view.findViewById(R.id.binomialFailCriteriaInput).setVisibility(View.GONE);
        view.findViewById(R.id.experimentVariableNameLayout).setVisibility(View.GONE);
        view.findViewById(R.id.experimentVariableNameInput).setVisibility(View.GONE);
    }
}