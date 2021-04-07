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
import com.T05.krowdtrialz.model.QnA.Question;
import com.T05.krowdtrialz.model.experiment.Experiment;
import com.T05.krowdtrialz.model.user.User;
import com.T05.krowdtrialz.ui.QnA.QuestionDetailsActivity;
import com.T05.krowdtrialz.ui.experimentDetails.ExperimentDetailsActivity;

import org.apache.commons.math3.analysis.function.Exp;

import java.util.ArrayList;

/**
 * Custom Array Adapter for ListView to show brief question info such as user asking question
 * and the question.
 */
public class QuestionList extends ArrayAdapter<Question> {

    private static final String TAG = "QuestionList";
    private ArrayList<Question> questions;
    private Context context;
    private Experiment experiment;

    private com.google.android.material.textview.MaterialTextView askedBy;
    private com.google.android.material.textview.MaterialTextView questionAsked;

    public QuestionList (Context context, ArrayList<Question> questions, Experiment experiment) {
        super(context, 0, questions);
        this.questions = questions;
        this.context = context;
        this.experiment = experiment;
    }

    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;

        if (view == null){
            view = LayoutInflater.from(context).inflate(R.layout.question_list_element, parent, false);
        }

        Question question = questions.get(position);

        askedBy = view.findViewById(R.id.question_list_user);
        questionAsked = view.findViewById(R.id.question_list_description);

        askedBy.setText(question.getAskedBy().getUserName());
        questionAsked.setText(question.getQuestion());

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;

                intent = new Intent(context, QuestionDetailsActivity.class);

                int questionIndex = experiment.getQuestions().indexOf(question);

                intent.putExtra(MainActivity.EXTRA_EXPERIMENT_ID, experiment.getId());
                intent.putExtra("Position", questionIndex);
                context.startActivity(intent);
            }
        });

        return view;
    }


}
