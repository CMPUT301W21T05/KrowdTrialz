package com.T05.krowdtrialz.ui.owner;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.T05.krowdtrialz.R;

/**
 * Dialog fragment to edit user information. New string is passed to provided callback on
 * success.
 *
 * A simple {@link Fragment} subclass.
 * Use the {@link EditUserDialogFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditUserDialogFragment extends DialogFragment {
    private final String TAG = "EditUsername";

    private String dataToChange = null;
    private String prompt = "Edit";

    OwnerFragment.OnChangeCallback callback = null;


    public EditUserDialogFragment() {
        // Required empty public constructor
    }

    /**
     * Inject user data to be changed
     *
     * @param dataToChange
     *  current data to be changed
     */
    public void injectUser(String dataToChange) {
        this.dataToChange = dataToChange;
    }

    /**
     * I nject callback function for when the fragment is done
     *
     * @param callback
     *  callback function
     */
    public void injectCallback(OwnerFragment.OnChangeCallback callback) {
        this.callback = callback;
    }

    /**
     * Set prompt of dialog fragment
     * @param prompt
     */
    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }

    /**
     * Create new instance of the dialog fragment. Inject paramters to the instance
     *
     * @param dataToChange
     *  candidate data to change
     * @param prompt
     *  promt for the dialog
     * @param callback
     *  callback for the 'done' click
     * @return
     *  fragment instance
     */
    public static EditUserDialogFragment newInstance(String dataToChange, String prompt, OwnerFragment.OnChangeCallback callback) {
        EditUserDialogFragment fragment = new EditUserDialogFragment();
        fragment.injectUser(dataToChange);
        fragment.injectCallback(callback);
        fragment.setPrompt(prompt);
        return fragment;
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_edit_username, null);
        EditText editUserText = view.findViewById(R.id.edit_user_text);
        editUserText.setText(dataToChange);


        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        return builder
                .setView(view)
                .setTitle(prompt)
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String changed = editUserText.getText().toString();
                        if (changed.isEmpty() || EditUserDialogFragment.this.callback == null) {
                            return;
                        }

                        EditUserDialogFragment.this.callback.onDone(changed);
                    }
                }).create();
    }

}