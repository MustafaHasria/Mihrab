package com.izzedineeita.mihrab.activity;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.izzedineeita.mihrab.MainActivity;
import com.izzedineeita.mihrab.R;
import com.izzedineeita.mihrab.constants.Constants;
import com.izzedineeita.mihrab.database.DataBaseHelper;
import com.izzedineeita.mihrab.utils.Pref;

import java.io.IOException;

public class SplashActivity extends AppCompatActivity {

    private Activity activity;
    private static final int REQUEST_ENABLE_BT = 3;
    private static final int PERMISSION_COARSE_LOCATION = 2;
    private FirebaseAuth mAuth;

    @Override
    protected void onStart() {
        super.onStart();
        Log.e("DDD", "onStart");
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState == null) {
            int theme = Pref.getValue(getApplicationContext(), Constants.PREF_THEM_POSITION_SELECTED, 0);
            switch (theme) {
                case 5:
                case 6:
                case 7:
                case 8:
                case 9:
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
                    break;
                default:
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                    break;
            }

            setContentView(R.layout.activity_splash);
//        DisplayMetrics displayMetrics = new DisplayMetrics();
//        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
//        int height = displayMetrics.heightPixels;
//        int width = displayMetrics.widthPixels;
//        if (width < height) {
//            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//        } else {
//            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
//        }
//
            Log.e("DDD", "onCreate");


            activity = this;

            mAuth = FirebaseAuth.getInstance();
            DataBaseHelper db = new DataBaseHelper(getApplicationContext());
            try {
                db.createDataBase();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (ContextCompat.checkSelfPermission(activity,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                // This is Case 1. Now we need to check further if permission was shown before or not

                if (ActivityCompat.shouldShowRequestPermissionRationale(activity,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                    // This is Case 4.
                } else {
                    // This is Case 3. Request for permission here
                    ActivityCompat.requestPermissions(activity,
                            new String[]{
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                    Manifest.permission.READ_EXTERNAL_STORAGE,
                                    Manifest.permission.CAMERA,
                                    Manifest.permission.ACCESS_COARSE_LOCATION
                            },
                            1);
                }

            } else {
                // This is Case 2. You have permission now you can do anything related to it
                int SPLASH_TIME_OUT = 2000;

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

//                        Intent intent;
//
//                        intent = new Intent(SplashActivity.this, MainActivity.class);
//
//                        startActivity(intent);
//                        finish();

                        boolean check = Pref.getValue(SplashActivity.this,Constants.PREF_IS_USER_LOGIN, false);
                        Intent intent;
                        if (check) {
                            intent = new Intent(SplashActivity.this, MainActivity.class);
                        } else {
                            intent = new Intent(SplashActivity.this, LoginActivity.class);
                        }
                    startActivity(intent);
                    finish();
                    }
                }, SPLASH_TIME_OUT);
            }

            if (!ensureBleExists())
                //   finish();

                if (!isBLEEnabled()) {
                    showBLEDialog();
                }
        }

    }

    private boolean ensureBleExists() {
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(this, "Phone does not support BLE", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    protected boolean isBLEEnabled() {
        final BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        final BluetoothAdapter adapter = bluetoothManager.getAdapter();
        return adapter != null && adapter.isEnabled();
    }

    private void showBLEDialog() {
        final Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    Intent intent = new Intent(SplashActivity.this, ShowAzkarActivity.class);
//                    FirebaseUser currentUser = mAuth.getCurrentUser();
//                    Intent intent;
//                    if(currentUser != null){
//                        intent = new Intent(SplashActivity.this, MainActivity.class);
//                    } else {
//                        intent = new Intent(SplashActivity.this, LoginActivity.class);
//                    }
//                    startActivity(intent);
//                    finish();

                    boolean check = Pref.getValue(SplashActivity.this,Constants.PREF_IS_USER_LOGIN, false);
                    Intent intent;
                    if (check) {
                        intent = new Intent(SplashActivity.this, MainActivity.class);
                    } else {
                        intent = new Intent(SplashActivity.this, LoginActivity.class);
                    }
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(activity, "Permission denied to read your External storage", Toast.LENGTH_SHORT).show();
                }
                return;
            }
            default:
                throw new IllegalStateException("Unexpected value: " + requestCode);
        }
    }
}