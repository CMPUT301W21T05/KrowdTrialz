package com.T05.krowdtrialz.ui.subscribed;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.T05.krowdtrialz.R;
import com.T05.krowdtrialz.model.experiment.BinomialExperiment;
import com.T05.krowdtrialz.model.experiment.CountExperiment;
import com.T05.krowdtrialz.model.experiment.Experiment;
import com.T05.krowdtrialz.model.experiment.IntegerExperiment;
import com.T05.krowdtrialz.model.experiment.MeasurementExperiment;
import com.T05.krowdtrialz.model.trial.BinomialTrial;
import com.T05.krowdtrialz.model.trial.CountTrial;
import com.T05.krowdtrialz.model.trial.IntegerTrial;
import com.T05.krowdtrialz.model.trial.MeasurementTrial;
import com.T05.krowdtrialz.model.trial.Trial;
import com.T05.krowdtrialz.model.user.User;
import com.T05.krowdtrialz.util.Database;
import com.budiyev.android.codescanner.AutoFocusMode;
import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.budiyev.android.codescanner.ScanMode;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.zxing.Result;

public class ScanActivity extends AppCompatActivity {
    private final String TAG = "ScanCode Activity";

    private final int CAMERA_REQUEST_CODE = 101;
    private CodeScanner codeScanner;
    private CodeScannerView scannerView;
    private Database db;
    private ListenerRegistration expRegistration = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_q_r_code);

        setupPermissions();
    }

    /**
     * Scan code using phone camera
     */
    private void codeScanner() {

        scannerView = findViewById(R.id.scanner_codeScannerView);
        codeScanner = new CodeScanner(this, scannerView);
        // setup preferences
        codeScanner.setCamera(CodeScanner.CAMERA_BACK);
        codeScanner.setFormats(CodeScanner.ALL_FORMATS);
        codeScanner.setAutoFocusMode(AutoFocusMode.SAFE);
        codeScanner.setScanMode(ScanMode.SINGLE);
        codeScanner.setAutoFocusEnabled(true);
        codeScanner.setFlashEnabled(false);
        codeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull Result result) {
                // When Barcode or QrCode is detected give appropriate popup
                // resultArray has the following format {experimentID, type, Pass Count, Fail Count, Value, Longitude, latitude}
                String[] resultArray = result.getText().split("/");

                if (resultArray.length > 1) { // QRCode
                    db = Database.getInstance();
                    db.getExperimentByIDNotLive(resultArray[0], new Database.GetExperimentCallback() {
                        @Override
                        public void onSuccess(Experiment experiment) {
                            if (experiment.isInactive()) {
                                Log.e(TAG, "inactive experiment");

                                final Dialog dialog = new Dialog(ScanActivity.this);
                                dialog.setTitle("Attention");
                                dialog.setContentView(R.layout.barcode_not_found);
                                TextView error_message = dialog.findViewById(R.id.scanner_error_text);
                                error_message.setText("Experiment is not accepting trials");
                                Button okButton = dialog.findViewById(R.id.ok_barcode_button);

                                okButton.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dialog.dismiss();
                                        codeScanner.startPreview();
                                    }
                                });
                                dialog.show();
                                return;
                            }
                            Trial trial = makeTrial(experiment, resultArray);
                            fillDialog(experiment, trial, resultArray);
                        }

                        @Override
                        public void onFailure() {
                            Log.e(TAG, "Could not get experiment");
                            final Dialog dialog = new Dialog(ScanActivity.this);
                            dialog.setTitle("Edit Experiment Info");
                            dialog.setContentView(R.layout.barcode_not_found);
                            TextView error_message = dialog.findViewById(R.id.scanner_error_text);
                            error_message.setText("QR/Barcode not linked");
                            Button okButton = dialog.findViewById(R.id.ok_barcode_button);

                            okButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog.dismiss();
                                    codeScanner.startPreview();
                                }
                            });
                            dialog.show();
                        }
                    });
                } else { // Barcode
                    db = Database.getInstance();
                    db.getTrialInfoByBarcode(result.getText(), new Database.GetTrialInfoCallback() {
                        @Override
                        public void onSuccess(String[] trialInfo) {
                            db.getExperimentByIDNotLive(trialInfo[0], new Database.GetExperimentCallback() {
                                @Override
                                public void onSuccess(Experiment experiment) {
                                    if (experiment.isInactive()) {
                                        Log.e(TAG, "inactive experiment");

                                        final Dialog dialog = new Dialog(ScanActivity.this);
                                        dialog.setTitle("Attention");
                                        dialog.setContentView(R.layout.barcode_not_found);
                                        TextView error_message = dialog.findViewById(R.id.scanner_error_text);
                                        error_message.setText("Experiment is not accepting trials");
                                        Button okButton = dialog.findViewById(R.id.ok_barcode_button);

                                        okButton.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                dialog.dismiss();
                                                codeScanner.startPreview();
                                            }
                                        });
                                        dialog.show();
                                        return;
                                    }
                                    Trial trial = makeTrial(experiment, trialInfo);
                                    fillDialog(experiment, trial, trialInfo);
                                }

                                @Override
                                public void onFailure() {
                                    Log.e(TAG, "Could not get experiment");
                                }
                            });
                        }

                        @Override
                        public void onFailure() {
                            Log.e(TAG, "Barcode Does Not Exist in Database");
                            // Dialog to show error message
                            final Dialog dialog = new Dialog(ScanActivity.this);
                            dialog.setTitle("Edit Experiment Info");
                            dialog.setContentView(R.layout.barcode_not_found);

                            Button okButton = dialog.findViewById(R.id.ok_barcode_button);

                            okButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog.dismiss();
                                    codeScanner.startPreview();
                                }
                            });

                            dialog.show();
                        }
                    });
                }
            }
        });

        scannerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                codeScanner.startPreview();
            }
        });

        codeScanner.startPreview();
    }

    /**
     * Get required device permissions
     */
    private void setupPermissions() {
        int permission = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);

        if(permission != PackageManager.PERMISSION_GRANTED){
            makeRequest();
        }else{
            codeScanner();
        }
    }

    /**
     * Package permissions request
     */
    private void makeRequest() {
        String[] permissionArray = {Manifest.permission.CAMERA};
        ActivityCompat.requestPermissions(this, permissionArray, CAMERA_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults){
        if (requestCode == CAMERA_REQUEST_CODE) {
            if(grantResults.length == 0 || grantResults[0] != PackageManager.PERMISSION_GRANTED){
                // If user has not given permission to use camera
            }else{
                // User has given permission to use camera
                codeScanner();
            }
        }
    }

    /**
     * Make trial out of the scanned code
     * @param experiment Experiment to add results to
     * @param resultArray Retults(s) to add to the trial
     * @return
     *   trial to be added
     */
    private Trial makeTrial(Experiment experiment, String[] resultArray){
        String type = experiment.getType();
        User user = db.getDeviceUser();
        double longitude;
        double latitude;
        if (resultArray[5].equals("None") || resultArray[6].equals("None")) {
            longitude = 999d;
            latitude = 999d;
        } else {
            longitude = Double.parseDouble(resultArray[5]);
            latitude = Double.parseDouble(resultArray[6]);
        }

        Trial trial = null;
        if (type == BinomialExperiment.type) {

            trial = new BinomialTrial(user, longitude, latitude);

            // set the pass and the fail counts
            int passText = Integer.parseInt(resultArray[2]);
            int failText = Integer.parseInt(resultArray[3]);
            ((BinomialTrial) trial).setPassCount(passText);
            ((BinomialTrial) trial).setFailCount(failText);
        } else if (type == CountExperiment.type) {
            trial = new CountTrial(user, longitude, latitude);
        } else if (type == MeasurementExperiment.type) {
            trial = new MeasurementTrial(user, longitude, latitude);
            float valueText = Float.parseFloat(resultArray[4]);
            ((MeasurementTrial) trial).setMeasurementValue(valueText);
        } else if (type == IntegerExperiment.type) {
            trial = new IntegerTrial(user, longitude, latitude);
            int valueText = Integer.parseInt(resultArray[4]);
            ((IntegerTrial) trial).setValue(valueText);
        }

        return trial;

    }

    /**
     * Populate dialog box for user confirmation
     * @param experiment
     *      experiment to add to
     * @param trial
     *      Trial to be added
     * @param resultArray
     *      Results to be added to experiment
     */
    private void fillDialog(Experiment experiment, Trial trial, String[] resultArray){
        String type = experiment.getType();
        if (trial != null) {
            // Ask user to confirm that they want to add the trial
            final Dialog dialog = new Dialog(ScanActivity.this);
            dialog.setTitle("Edit Experiment Info");
            dialog.setContentView(R.layout.confirm_qr_scan);

            final TextView desc = dialog.findViewById(R.id.desc_textView);
            final TextView passText = dialog.findViewById(R.id.pass_count_textView);
            final TextView failText = dialog.findViewById(R.id.fail_count_textView);
            final TextView passTitle = dialog.findViewById(R.id.pass_title_textView);
            final TextView failTitle = dialog.findViewById(R.id.fail_title_textView);
            final TextView valueTitle = dialog.findViewById(R.id.value_title_textView);
            final TextView valueText = dialog.findViewById(R.id.value_textView);

            desc.setText(experiment.getDescription());

            if (type == "Binomial") {
                BinomialExperiment binomialExperiment = (BinomialExperiment) experiment;
                valueText.setVisibility(View.GONE);
                valueTitle.setVisibility(View.GONE);
                passTitle.setText(binomialExperiment.getPassUnit());
                failTitle.setText(binomialExperiment.getFailUnit());
                passText.setText(resultArray[2]);
                failText.setText(resultArray[3]);
            } else if (type == "Count") {
                valueText.setVisibility(View.GONE);
                valueTitle.setVisibility(View.GONE);
                passTitle.setVisibility(View.GONE);
                passText.setVisibility(View.GONE);
                failTitle.setVisibility(View.GONE);
                failText.setVisibility(View.GONE);
            } else {
                passTitle.setVisibility(View.GONE);
                passText.setVisibility(View.GONE);
                failTitle.setVisibility(View.GONE);
                failText.setVisibility(View.GONE);
                if (type == "Measurement") {
                    MeasurementExperiment measurementExperiment = (MeasurementExperiment) experiment;
                    valueTitle.setText(measurementExperiment.getUnit());
                } else {
                    IntegerExperiment integerExperiment = (IntegerExperiment) experiment;
                    valueTitle.setText(integerExperiment.getUnit());
                }
                valueText.setText(resultArray[4]);
            }

            Button confirmButton = dialog.findViewById(R.id.confirm_button);
            Button cancelButton = dialog.findViewById(R.id.cancel_button);

            // saves the edited text to resultDataList and displays result
            Trial finalTrial = trial;
            confirmButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (expRegistration != null) {
                        expRegistration.remove();
                    }
                    db.addTrial(finalTrial, experiment);
                    dialog.dismiss();
                    codeScanner.startPreview();
                    Context context = getApplicationContext();
                    CharSequence text = "Submitted";
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                }
            });

            cancelButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    codeScanner.startPreview();
                    Context context = getApplicationContext();
                    CharSequence text = "Canceled";
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                }
            });
            dialog.show();
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Stop listening to changes in the Database.
        if(expRegistration != null){
            expRegistration.remove();
        }
        codeScanner.releaseResources();
    }
}