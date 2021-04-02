package com.T05.krowdtrialz.ui.owner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.T05.krowdtrialz.R;
import com.T05.krowdtrialz.model.user.User;
import com.T05.krowdtrialz.util.Database;

/**
 * Open the user fragment
 */
public class UserActivity extends AppCompatActivity {

    public static final String USER_ID_EXTRA = "USERID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String userID = getIntent().getStringExtra(USER_ID_EXTRA);

        Database db = Database.getInstance();

        db.getUserById(userID, new Database.GetUserCallback() {
            @Override
            public void onSuccess(User user) {
                /**
                 * Open the owner fragment
                 */
                Fragment fragment = new OwnerFragment(user);
                FragmentManager manager = getSupportFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.add(R.id.user_activity, fragment);
                transaction.commit();
            }

            @Override
            public void onFailure() {

            }
        });
        setContentView(R.layout.activity_user);
    }
}