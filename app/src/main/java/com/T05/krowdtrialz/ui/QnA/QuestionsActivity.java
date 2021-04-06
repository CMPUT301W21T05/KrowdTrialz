package com.T05.krowdtrialz.ui.QnA;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;

import com.T05.krowdtrialz.MainActivity;
import com.T05.krowdtrialz.R;
import com.T05.krowdtrialz.model.QnA.Question;
import com.T05.krowdtrialz.model.experiment.Experiment;
import com.T05.krowdtrialz.model.user.User;
import com.T05.krowdtrialz.ui.subscribed.ScanActivity;
import com.T05.krowdtrialz.ui.subscribed.SubscribedViewModel;
import com.T05.krowdtrialz.util.Database;
import com.T05.krowdtrialz.util.ExperimentList;
import com.T05.krowdtrialz.util.QuestionList;

import java.util.ArrayList;

public class QuestionsActivity extends AppCompatActivity {

    private QuestionViewModel questionViewModel;
    private ListView questionsList;
    private ArrayAdapter<Question> questionsArrayAdapter;
    private ArrayList<Question> questionsDataList;
    private Database db;
    private ImageButton addButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions);

        questionViewModel = new ViewModelProvider(this).get(QuestionViewModel.class);

        db = Database.getInstance();

        Intent intent = getIntent();
        String experimentID = intent.getStringExtra(MainActivity.EXTRA_EXPERIMENT_ID);

        questionsList = findViewById(R.id.question_listView);
        addButton = findViewById(R.id.add_question_button);

        questionsDataList = new ArrayList<Question>();

        db.getExperimentByIDNotLive(experimentID, new Database.GetExperimentCallback() {
            @Override
            public void onSuccess(Experiment experiment) {

                questionsArrayAdapter = new QuestionList(QuestionsActivity.this, questionsDataList, experiment);
                questionsList.setAdapter(questionsArrayAdapter);

                questionViewModel.getQuestionList(experimentID).observe(QuestionsActivity.this, new Observer<ArrayList<Question>>() {
                    @Override
                    public void onChanged(ArrayList<Question> questions) {
                        questionsArrayAdapter.clear();
                        questionsArrayAdapter.addAll(questions);
                        questionsArrayAdapter.notifyDataSetChanged();
                    }
                });



                addButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(v.getContext(), AskQuestionActivity.class);
                        intent.putExtra(MainActivity.EXTRA_EXPERIMENT_ID, experimentID);
                        startActivity(intent);
                    }
                });
            }

            @Override
            public void onFailure() {

            }
        });
    }
}