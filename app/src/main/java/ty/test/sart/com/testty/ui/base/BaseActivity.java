package ty.test.sart.com.testty.ui.base;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import ty.test.sart.com.testty.R;

/**
 * Created by sart1 on 11/27/2017.
 */

public abstract class BaseActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final int REQUEST_LOCATION_PERMISSION = 1054;

    private GoogleMap mMap;
    private AlertDialog mRationaleLocationPermissionsDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bindViews();
    }

    protected void bindViews() {
        mRationaleLocationPermissionsDialog = makeRationaleLocationPermissionsDialog();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        requestMapsPermissions();
        mMap = googleMap;
        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

    }

    private void requestMapsPermissions() {
        String locationPermission = Manifest.permission.ACCESS_FINE_LOCATION;
        String[] permissions = new String[] { locationPermission };

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, locationPermission)) {
                mRationaleLocationPermissionsDialog.show();
            } else {
                ActivityCompat.requestPermissions(this, permissions, REQUEST_LOCATION_PERMISSION);
            }
        }
    }

    private AlertDialog makeRationaleLocationPermissionsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.permissionsLocation_rationale_title);
        builder.setMessage(R.string.permissionsLocation_rationale_content);
        builder.setPositiveButton(R.string.permissionsLocation_rationale_positiveButton,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        requestMapsPermissions();
                    }
                });
        return builder.create();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_LOCATION_PERMISSION:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    workOnMap(mMap);
                } else {
                    mRationaleLocationPermissionsDialog.show();
                    onLocationPermissionDenied();
                }
                break;
        }
    }

    protected abstract void workOnMap(GoogleMap googleMap);

    protected abstract void onLocationPermissionDenied();
}
