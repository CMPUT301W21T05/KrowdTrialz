package com.T05.krowdtrialz.ui.QnA;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.T05.krowdtrialz.MainActivity;
import com.T05.krowdtrialz.R;
import com.T05.krowdtrialz.model.QnA.Answer;
import com.T05.krowdtrialz.model.QnA.Question;
import com.T05.krowdtrialz.model.experiment.Experiment;
import com.T05.krowdtrialz.model.user.User;
import com.T05.krowdtrialz.util.AnswerList;
import com.T05.krowdtrialz.util.Database;
import com.T05.krowdtrialz.util.QuestionList;
import com.google.firebase.firestore.ListenerRegistration;

import java.util.ArrayList;

public class QuestionDetailsActivity extends AppCompatActivity {
    private final String TAG = "QuestionDetail";
    private AnswerViewModel answerViewModel;
    private Database db;
    private TextView questionText;
    private TextView usernameText;
    private Button submitButton;
    private EditText replyField;
    private ListenerRegistration expRegistration;

    private ArrayList<Answer> answersDataList;
    private ArrayAdapter<Answer> answersArrayAdapter;
    private ListView answerList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_details);
        answerViewModel = new ViewModelProvider(this).get(AnswerViewModel.class);
        db = Database.getInstance();

        Intent intent = getIntent();
        String experimentID = intent.getStringExtra(MainActivity.EXTRA_EXPERIMENT_ID);
        int position = intent.getIntExtra("Position",-1);

        usernameText = findViewById(R.id.questions_user_name_textView);
        questionText = findViewById(R.id.question_textView);
        answerList = findViewById(R.id.answers_listView);
        submitButton = findViewById(R.id.submit_answer_button);
        replyField = findViewById(R.id.submit_answer_editText);

        answersDataList = new ArrayList<Answer>();
        answersArrayAdapter = new AnswerList(this, answersDataList, experimentID);
        answerList.setAdapter(answersArrayAdapter);

        answerViewModel.getAnswerList(experimentID,position).observe(this, new Observer<ArrayList<Answer>>() {
            @Override
            public void onChanged(ArrayList<Answer> answers) {

                answersArrayAdapter.clear();
                answersArrayAdapter.addAll(answers);
                answersArrayAdapter.notifyDataSetChanged();
            }
        });

        expRegistration = db.getExperimentByID(experimentID, new Database.GetExperimentCallback() {
            @Override
            public void onSuccess(Experiment exp) {
                int positionFinal = position;
                if (positionFinal == -1){
                    positionFinal = exp.getQuestions().size() - 1;
                }

                ArrayList<Question> questionList = exp.getQuestions();
                Question question = questionList.get(positionFinal);
                questionText.setText(question.getQuestion());

                db.getUserByIdNotLive(question.getAskedBy().getId(), new Database.GetUserCallback() {
                    @Override
                    public void onSuccess(User user) {
                        usernameText.setText(user.getUserName());
                    }

                    @Override
                    public void onFailure() {
                        Log.e(TAG, "COULD NOT GET USER");
                    }
                });

                submitButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (replyField.getText().length() != 0){
                            Answer answer = new Answer(replyField.getText().toString(), db.getDeviceUser());
                            Log.e(TAG, String.valueOf(question.getAnswers()));
                            question.addAnswer(answer);
                            db.updateExperiment(exp);
                            // Toast to confirm adding Question
                            Context context = getApplicationContext();
                            CharSequence text = "Answered";
                            int duration = Toast.LENGTH_SHORT;
                            Toast toast = Toast.makeText(context, text, duration);
                            toast.show();

                            replyField.setText("");
                        }
                    }
                });
            }

            @Override
            public void onFailure() {
                Log.e(TAG, "Error Searching Database for experiment");
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Stop listening to changes in the Database
        expRegistration.remove();
    }
}