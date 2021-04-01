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
import com.T05.krowdtrialz.model.QnA.Answer;
import com.T05.krowdtrialz.model.QnA.Question;
import com.T05.krowdtrialz.model.experiment.Experiment;
import com.T05.krowdtrialz.model.user.User;
import com.T05.krowdtrialz.ui.QnA.QuestionDetailsActivity;
import com.T05.krowdtrialz.ui.experimentDetails.ExperimentDetailsActivity;

import java.util.ArrayList;

/**
 * Custom Array Adapter for ListView to show brief answer info such as user answering and answer
 */
public class AnswerList extends ArrayAdapter<Answer> {

    private static final String TAG = "AnswerList";
    private ArrayList<Answer> answers;
    private Context context;
    private String experimentID;

    private com.google.android.material.textview.MaterialTextView answeredBy;
    private com.google.android.material.textview.MaterialTextView answerView;

    public AnswerList (Context context, ArrayList<Answer> answers, String experimentID) {
        super(context, 0, answers);
        this.answers = answers;
        this.context = context;
        this.experimentID = experimentID;
    }

    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;

        if (view == null){
            view = LayoutInflater.from(context).inflate(R.layout.question_list_element, parent, false);
        }

        Answer answer = answers.get(position);

        answeredBy = view.findViewById(R.id.question_list_user);
        answerView = view.findViewById(R.id.question_list_description);

        answeredBy.setText(answer.getResponder().getUserName());
        answerView.setText(answer.getAnswer());


        return view;
    }


}
