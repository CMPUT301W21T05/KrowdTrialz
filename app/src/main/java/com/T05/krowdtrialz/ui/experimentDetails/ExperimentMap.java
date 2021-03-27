package com.T05.krowdtrialz.ui.experimentDetails;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.preference.PreferenceManager;

import com.T05.krowdtrialz.MainActivity;
import com.T05.krowdtrialz.R;
import com.T05.krowdtrialz.model.experiment.Experiment;
import com.T05.krowdtrialz.model.trial.Trial;
import com.T05.krowdtrialz.util.Database;

import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.BoundingBox;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.util.TileSystem;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.ItemizedOverlayWithFocus;
import org.osmdroid.views.overlay.Overlay;
import org.osmdroid.views.overlay.OverlayItem;

import java.util.ArrayList;

public class ExperimentMap extends AppCompatActivity {
    private final int REQUEST_PERMISSIONS_REQUEST_CODE  = 1;
    private MapView map = null;
    private Experiment experiment = null;
    private Database db = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        String experimentID = intent.getStringExtra(MainActivity.EXTRA_EXPERIMENT_ID);
        db = Database.getInstance();
        db.getExperimentByID(experimentID, new Database.GetExperimentCallback(){
            @Override
            public void onSuccess(Experiment experiment) {
                Context ctx = getApplicationContext();
                Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));

                setContentView(R.layout.activity_experiment_map);
                map = (MapView) findViewById(R.id.map);

                map.setTileSource(TileSourceFactory.MAPNIK);
                map.setHorizontalMapRepetitionEnabled(false);
                map.setVerticalMapRepetitionEnabled(false);
                map.getController().setZoom(2.5d);

                requestPermissionsIfNecessary(new String[] {
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                });

                //your items
                ArrayList<OverlayItem> items = new ArrayList<OverlayItem>();

                for (Trial trial: experiment.getTrials()){
                    items.add(new OverlayItem(trial.getExperimenter().getUserName(), trial.getDateCreated(), new GeoPoint(trial.getLatitude(),trial.getLongitude()))); // Lat/Lon decimal degrees
                }
                //the overlay
                ItemizedIconOverlay<OverlayItem> mOverlay = new ItemizedIconOverlay<OverlayItem>(items,
                        new ItemizedIconOverlay.OnItemGestureListener<OverlayItem>() {
                            @Override
                            public boolean onItemSingleTapUp(final int index, final OverlayItem item) {
                                return true;
                            }
                            @Override
                            public boolean onItemLongPress(final int index, final OverlayItem item) {
                                return false;
                            }
                        }, ctx);
                map.getOverlays().add(mOverlay);
            }

            @Override
            public void onFailure() {

            }
        });

    }

    private void requestPermissionsIfNecessary(String[] permissions) {
        ArrayList<String> permissionsToRequest = new ArrayList<>();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission)
                    != PackageManager.PERMISSION_GRANTED) {
                // Permission is not granted
                permissionsToRequest.add(permission);
            }
        }
        if (permissionsToRequest.size() > 0) {
            ActivityCompat.requestPermissions(
                    this,
                    permissionsToRequest.toArray(new String[0]),
                    REQUEST_PERMISSIONS_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        ArrayList<String> permissionsToRequest = new ArrayList<>();
        for (int i = 0; i < grantResults.length; i++) {
            permissionsToRequest.add(permissions[i]);
        }
        if (permissionsToRequest.size() > 0) {
            ActivityCompat.requestPermissions(
                    this,
                    permissionsToRequest.toArray(new String[0]),
                    REQUEST_PERMISSIONS_REQUEST_CODE);
        }
    }
}

