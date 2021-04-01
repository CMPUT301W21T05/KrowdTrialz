package com.T05.krowdtrialz.ui.QnA;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.T05.krowdtrialz.MainActivity;
import com.T05.krowdtrialz.R;
import com.T05.krowdtrialz.model.QnA.Answer;
import com.T05.krowdtrialz.model.QnA.Question;
import com.T05.krowdtrialz.model.experiment.Experiment;
import com.T05.krowdtrialz.util.Database;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.firestore.ListenerRegistration;

import java.util.ArrayList;

public class AskQuestionActivity extends AppCompatActivity {

    private final String TAG = "AskQuestion";
    private Database db;
    private EditText questionText;
    private Button submitButton;
    private TextView owner;
    private TextView description;
    private ListenerRegistration expRegistration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ask_question);

        db = Database.getInstance();

        Intent intent = getIntent();
        String experimentID = intent.getStringExtra(MainActivity.EXTRA_EXPERIMENT_ID);

        owner = findViewById(R.id.ask_question_owner_textView);
        description = findViewById(R.id.ask_question_description_textView);
        questionText = findViewById(R.id.ask_question_editText);
        submitButton = findViewById(R.id.ask_question_submit_button);

        expRegistration = db.getExperimentByID(experimentID, new Database.GetExperimentCallback() {
            @Override
            public void onSuccess(Experiment exp) {
                owner.setText(exp.getOwner().getUserName());
                description.setText(exp.getDescription());

                submitButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (questionText.getText().length() != 0){
                            Question question = new Question(questionText.getText().toString(), db.getDeviceUser());
                            db.addQuestion(question, exp);
                            // Toast to confirm adding Question
                            Context context = getApplicationContext();
                            CharSequence text = "Asked Question";
                            int duration = Toast.LENGTH_SHORT;
                            Toast toast = Toast.makeText(context, text, duration);
                            toast.show();

                            //finish();
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