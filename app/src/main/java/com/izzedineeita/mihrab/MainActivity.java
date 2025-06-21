package com.izzedineeita.mihrab;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;
import com.izzedineeita.mihrab.activity.LoginActivity;
import com.izzedineeita.mihrab.activity.SettingActivity;
import com.izzedineeita.mihrab.activity.ShowAdsActivity;
import com.izzedineeita.mihrab.activity.ShowAlkhushueActivity;
import com.izzedineeita.mihrab.activity.ShowAzkarActivity;
import com.izzedineeita.mihrab.activity.ShowClosePhoneActivity;
import com.izzedineeita.mihrab.activity.ShowKatebActivity;
import com.izzedineeita.mihrab.activity.ShowKhotabActivity;
import com.izzedineeita.mihrab.constants.Constants;
import com.izzedineeita.mihrab.constants.DateHigri;
import com.izzedineeita.mihrab.database.DataBaseHelper;
import com.izzedineeita.mihrab.model.Ads;
import com.izzedineeita.mihrab.model.Khotab;
import com.izzedineeita.mihrab.utils.ImagesArrays;
import com.izzedineeita.mihrab.utils.Pref;
import com.izzedineeita.mihrab.utils.Utils;
import com.minew.beaconplus.sdk.MTCentralManager;
import com.minew.beaconplus.sdk.MTFrameHandler;
import com.minew.beaconplus.sdk.MTPeripheral;
import com.minew.beaconplus.sdk.enums.BluetoothState;
import com.minew.beaconplus.sdk.enums.ConnectionStatus;
import com.minew.beaconplus.sdk.enums.FrameType;
import com.minew.beaconplus.sdk.exception.MTException;
import com.minew.beaconplus.sdk.frames.AccFrame;
import com.minew.beaconplus.sdk.frames.ForceFrame;
import com.minew.beaconplus.sdk.frames.HTFrame;
import com.minew.beaconplus.sdk.frames.IBeaconFrame;
import com.minew.beaconplus.sdk.frames.LightFrame;
import com.minew.beaconplus.sdk.frames.MinewFrame;
import com.minew.beaconplus.sdk.frames.TemperatureFrame;
import com.minew.beaconplus.sdk.frames.TlmFrame;
import com.minew.beaconplus.sdk.frames.TvocFrame;
import com.minew.beaconplus.sdk.frames.UidFrame;
import com.minew.beaconplus.sdk.frames.UrlFrame;
import com.minew.beaconplus.sdk.interfaces.ConnectionStatueListener;
import com.minew.beaconplus.sdk.interfaces.GetPasswordListener;
import com.minew.beaconplus.sdk.interfaces.MTCentralManagerListener;
import com.minew.beaconplus.sdk.interfaces.OnBluetoothStateChangedListener;

import java.io.IOException;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class MainActivity extends AppCompatActivity {


    //region Variables
    private ImageView img_time_hour_1, img_time_hour_2, img_time_mint_1, img_time_sec_2, img_time_sec_1, img_date_day, img_date_month_m_2, img_date_month_m_1, img_date_month_m, img_date_years_4, img_date_years_3, img_date_years_2, img_date_years_1, img_date_month_h_2, img_date_month_h_1, img_date_month_h, img_date_years_h_4, img_date_years_h_3, img_date_years_h_2, img_date_years_h_1, img_time_mint_2;
    private ImageView img_fjr_azan_h_1, img_fjr_azan_h_2, img_fjr_azan_m_1, img_fjr_azan_m_2;
    private ImageView img_shroq_azan_h_1, img_shroq_azan_h_2, img_shroq_azan_m_1, img_shroq_azan_m_2;
    private ImageView img_dahr_azan_h_1, img_dahr_azan_h_2, img_dahr_azan_m_1, img_dahr_azan_m_2;
    private ImageView img_asr_azan_h_1, img_asr_azan_h_2, img_asr_azan_m_1, img_asr_azan_m_2;
    private ImageView img_mgrb_azan_h_1, img_mgrb_azan_h_2, img_mgrb_azan_m_1, img_mgrb_azan_m_2;
    private ImageView img_isha_azan_h_1, img_isha_azan_h_2, img_isha_azan_m_1, img_isha_azan_m_2;
    private ImageView img_fjr_iqamh_h_1, img_fjr_iqamh_h_2, img_fjr_iqamh_m_1, img_fjr_iqamh_m_2;
    private ImageView img_shroq_iqamh_h_1, img_shroq_iqamh_h_2, img_shroq_iqamh_m_1, img_shroq_iqamh_m_2;
    private ImageView img_dahr_iqamg_h_1, img_dahr_iqamg_h_2, img_dahr_iqamg_m_1, img_dahr_iqamg_m_2;
    private ImageView img_asr_iqamg_h_1, img_asr_iqamg_h_2, img_asr_iqamg_m_1, img_asr_iqamg_m_2;
    private ImageView img_mgrb_iqamg_h_1, img_mgrb_iqamg_h_2, img_mgrb_iqamg_m_1, img_mgrb_iqamg_m_2;
    private ImageView img_isha_iqamh_h_1, img_isha_iqamh_h_2, img_isha_iqamh_m_1, img_isha_iqamh_m_2;
    private ImageView img_time_left_h_1, img_time_left_h_2, img_time_left_m_1, img_time_left_m_2, img_time_left_s_1, img_time_left_s_2;
    private ImageView img_next_azan;
    private ImageView img_iqamh_time_left_m_1, img_iqamh_time_left_m_2, img_iqamh_time_left_s_1, img_iqamh_time_left_s_2;
    private ImageView img_azan_jm3a_time_h_1, img_azan_jm3a_time_h_2, img_azan_jm3a_time_m_1, img_azan_jm3a_time_m_2;
    private ImageView img_next_azan1;
    private LinearLayout lay_img_azan_time_left;
    private LinearLayout lay_img_iqamh_time_left;
    private TextView tv_masged_name, tv_internal_heat;
    private String[] date;
    public Thread myThread = null;

    public String cfajr, csunrise, cdhohr, casr, cmaghrib, cisha;
    public String cfajr1, csunrise1, cdhohr1, casr1, cmaghrib1, cisha1;

    private DataBaseHelper db;
    public static int[] daysImage;
    public static int[] monthImage;
    public static int[] monthImageHijri;
    public static int[] dateNumber;
    public static int[] timeNumber;
    public static int[] timeNumberIqamhLeft;
    public static int[] timeNumberAzanLeft;
    public static int[] timeNumberAzan;
    public static int[] timeNumberIqamh;
    public static int[] timeNumberSec;
    private String[] friady;
    private LinearLayout img_bottom;

    private MediaPlayer mp = new MediaPlayer();
    public static boolean isOpenAds = false;
    public static boolean isOpenClosePhone = false;
    public static boolean isOpenAzkarPhone = false;
    public static boolean isOpenAlkhushuePhone = false;
    public static boolean isOpenKhotabActivity = false;
    public boolean isTextNewsShow = false;
    String timeNow1;
    RelativeLayout relativeLayout;

    private static final int REQUEST_ENABLE_BT = 3;
    private static final int PERMISSION_COARSE_LOCATION = 2;
    public static float TEMPERATURE = 0;
    public static int BATTERY = 0;
    private MTCentralManager mMtCentralManager;
    private TextView tv;
    public static Activity activity;
    int theme;
    //endregion

    //region Life cycle
    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLocale("en");
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        theme = Pref.getValue(getApplicationContext(), Constants.PREF_THEM_POSITION_SELECTED, 0);

        switch (theme) {
            case 1:
                setContentView(R.layout.activity_main);
                Resources res = getResources();

                tv = findViewById(R.id.textviewNews);
                tv.setTextColor(Color.parseColor("#ED0606"));
                relativeLayout = findViewById(R.id.relativeLayout);

                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

                Drawable drawable1 = res.getDrawable(R.drawable.background_main_them_1);
                relativeLayout.setBackground(drawable1);

                daysImage = ImagesArrays.daysImageTheme1;
                monthImage = ImagesArrays.monthImageTheme1;
                monthImageHijri = ImagesArrays.monthImageHijriTheme1;
                dateNumber = ImagesArrays.dateNumberTheme1;
                timeNumber = ImagesArrays.timeNumberTheme1;
                timeNumberAzan = ImagesArrays.timeNumberAzanTheme1;
                timeNumberIqamh = ImagesArrays.timeNumberIqamhTheme1;
                timeNumberIqamhLeft = ImagesArrays.timeNumberTheme1;
                timeNumberAzanLeft = ImagesArrays.timeNumberTheme1;
                timeNumberSec = ImagesArrays.timeNumberTheme1;
                break;
            case 2:
                setContentView(R.layout.activity_main);
                Resources res1 = getResources();
                relativeLayout = findViewById(R.id.relativeLayout);

                tv = findViewById(R.id.textviewNews);
                tv.setTextColor(Color.parseColor("#ED0606"));

                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

                Drawable drawable2 = res1.getDrawable(R.drawable.background_main_them_2);
                relativeLayout.setBackground(drawable2);
                daysImage = ImagesArrays.daysImageTheme1;
                monthImage = ImagesArrays.monthImageTheme1;
                monthImageHijri = ImagesArrays.monthImageHijriTheme1;
                dateNumber = ImagesArrays.dateNumberTheme1;
                timeNumber = ImagesArrays.timeNumberTheme1;
                timeNumberAzan = ImagesArrays.timeNumberAzanTheme1;
                timeNumberIqamh = ImagesArrays.timeNumberIqamhTheme1;
                timeNumberIqamhLeft = ImagesArrays.timeNumberTheme1;
                timeNumberAzanLeft = ImagesArrays.timeNumberTheme1;
                timeNumberSec = ImagesArrays.timeNumberTheme1;
                break;
            case 3:
                setContentView(R.layout.activity_main_4);
                tv = findViewById(R.id.textviewNews);
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

                img_next_azan1 = findViewById(R.id.img_next_azan1);
                daysImage = ImagesArrays.daysImageTheme4;
                monthImage = ImagesArrays.monthImageTheme4;
                monthImageHijri = ImagesArrays.monthImageHijriTheme4;
                dateNumber = ImagesArrays.timeNumberTheme4;
                timeNumber = ImagesArrays.timeNumberTheme4;
                timeNumberAzan = ImagesArrays.timeNumberIqamhTheme4;
                timeNumberIqamh = ImagesArrays.timeNumberIqamhTheme4;
                timeNumberIqamhLeft = ImagesArrays.timeNumberIqamhLeft4;
                timeNumberAzanLeft = ImagesArrays.timeNumberIqamhLeft4;
                timeNumberSec = ImagesArrays.timeNumberIqamhTheme4;
                break;
            case 4:
                setContentView(R.layout.activity_main_5);
                tv = findViewById(R.id.textviewNews);
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

                daysImage = ImagesArrays.daysImageTheme4;
                monthImage = ImagesArrays.monthImageTheme5;
                monthImageHijri = ImagesArrays.monthImageHijriTheme5;
                dateNumber = ImagesArrays.timeNumberTheme5;
                timeNumber = ImagesArrays.timeNumberTheme5;
                timeNumberAzan = ImagesArrays.timeNumberIqamhTheme4;
                timeNumberIqamh = ImagesArrays.timeNumberIqamhTheme4;
                timeNumberIqamhLeft = ImagesArrays.timeNumberIqamhLeft4;
                timeNumberAzanLeft = ImagesArrays.timeNumberIqamhLeft4;
                timeNumberSec = ImagesArrays.timeNumberIqamhTheme4;
                break;
            case 5:
                setContentView(R.layout.activity_main_6);
                tv = findViewById(R.id.textviewNews);
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);

                daysImage = ImagesArrays.daysImageTheme6;
                monthImage = ImagesArrays.monthImageTheme6;
                monthImageHijri = ImagesArrays.monthImageHijriTheme6;
                dateNumber = ImagesArrays.timeNumberIqamhLeft4;
                timeNumber = ImagesArrays.timeNumberTheme6;
                timeNumberAzan = ImagesArrays.timeNumberAzanTheme6;
                timeNumberIqamh = ImagesArrays.timeNumberIqama6;
                timeNumberIqamhLeft = ImagesArrays.timeNumberTheme6;
                timeNumberAzanLeft = ImagesArrays.timeNumberTheme6;
                timeNumberSec = ImagesArrays.timeNumberTheme6;
                break;
            case 6:
                setContentView(R.layout.activity_main_7);
                tv = findViewById(R.id.textviewNews);
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
                img_next_azan1 = findViewById(R.id.img_next_azan1);

                daysImage = ImagesArrays.daysImageTheme1;
                monthImage = ImagesArrays.monthImageTheme1;
                monthImageHijri = ImagesArrays.monthImageHijriTheme1;
                dateNumber = ImagesArrays.dateNumberTheme1;
                timeNumber = ImagesArrays.timeNumberIqamhLeft7;
                timeNumberAzan = ImagesArrays.timeNumberAzanTheme6;
                timeNumberIqamh = ImagesArrays.timeNumberIqama6;
                timeNumberIqamhLeft = ImagesArrays.timeNumberIqamhLeft7;
                timeNumberAzanLeft = ImagesArrays.timeNumberIqamhLeft7;
                timeNumberSec = ImagesArrays.timeNumberIqamhLeft7;

                break;
            case 7:
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
                setContentView(R.layout.activity_main_8);
                tv = findViewById(R.id.textviewNews);
                img_next_azan1 = findViewById(R.id.img_next_azan1);

                daysImage = ImagesArrays.daysImageTheme8;
                monthImage = ImagesArrays.monthImageTheme8;
                monthImageHijri = ImagesArrays.monthImageHijriTheme8;
                dateNumber = ImagesArrays.timeNumberDate8;
                timeNumber = ImagesArrays.timeNumberTime8;
                timeNumberAzan = ImagesArrays.timeNumberAzIq8;
                timeNumberIqamh = ImagesArrays.timeNumberAzIq8;
                timeNumberIqamhLeft = ImagesArrays.timeNumberTime8;
                timeNumberAzanLeft = ImagesArrays.timeNumberTime8;
                timeNumberSec = ImagesArrays.timeNumberTime8;
                break;
            case 8:
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
                setContentView(R.layout.activity_main_9);
                tv = findViewById(R.id.textviewNews);
                daysImage = ImagesArrays.daysImageTheme1;
                monthImage = ImagesArrays.monthImageTheme1;
                monthImageHijri = ImagesArrays.monthImageHijriTheme1;
                dateNumber = ImagesArrays.dateNumberTheme1;
                timeNumber = ImagesArrays.timeNumberTheme6;
                timeNumberAzan = ImagesArrays.timeNumberAzanTheme6;
                timeNumberIqamh = ImagesArrays.timeNumberIqama6;
                timeNumberIqamhLeft = ImagesArrays.timeNumberTheme6;
                timeNumberAzanLeft = ImagesArrays.timeNumberTheme6;
                timeNumberSec = ImagesArrays.timeNumberTheme6;
                break;
            case 9:
                setContentView(R.layout.activity_main_10);
                tv = findViewById(R.id.textviewNews);
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
                img_next_azan1 = findViewById(R.id.img_next_azan1);
                daysImage = ImagesArrays.daysImageTheme10;
                monthImage = ImagesArrays.monthImageTheme10;
                monthImageHijri = ImagesArrays.monthImageHijriTheme10;
                dateNumber = ImagesArrays.timeNumberDate10;
                timeNumber = ImagesArrays.timeNumberIqamhLeft4;
                timeNumberAzan = ImagesArrays.timeNumberAzIq8;
                timeNumberIqamh = ImagesArrays.timeNumberAzIq8;
                timeNumberIqamhLeft = ImagesArrays.timeNumberIqamhLeft4;
                timeNumberAzanLeft = ImagesArrays.timeNumberIqamhLeft4;
                timeNumberSec = ImagesArrays.timeNumberDate10;
                break;
            default:
                setContentView(R.layout.activity_main);
                Resources res4 = getResources();
                relativeLayout = findViewById(R.id.relativeLayout);
                tv = findViewById(R.id.textviewNews);
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

                Drawable drawable0 = res4.getDrawable(R.drawable.background_main);
                relativeLayout.setBackground(drawable0);
                img_next_azan1 = findViewById(R.id.img_next_azan1);

                daysImage = ImagesArrays.daysImageTheme1;
                monthImage = ImagesArrays.monthImageTheme1;
                monthImageHijri = ImagesArrays.monthImageHijriTheme1;
                dateNumber = ImagesArrays.dateNumberTheme1;
                timeNumber = ImagesArrays.timeNumberTheme1;
                timeNumberAzan = ImagesArrays.timeNumberAzanTheme1;
                timeNumberIqamh = ImagesArrays.timeNumberIqamhTheme1;
                timeNumberIqamhLeft = ImagesArrays.timeNumberTheme1;
                timeNumberAzanLeft = ImagesArrays.timeNumberTheme1;
                timeNumberSec = ImagesArrays.timeNumberTheme1;
                break;
        }

        activity = MainActivity.this;

        DateHigri hd = new DateHigri();
        date = Utils.writeIslamicDate(MainActivity.this, hd);

        db = new DataBaseHelper(getApplicationContext());

        tv.setSelected(true);

        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter != null) {
            if (!isBLEEnabled()) {
                showBLEDialog();
            }
        }

        initializeViewsAndListeners();
        requestLocationPermissions();
        updatePrayerAndDateDisplays();
        Runnable runnable = new CountDownRunner();
        myThread = new Thread(runnable);
        myThread.start();
    }

    @Override
    public void onBackPressed() {
        if (myThread.isAlive()) {
            myThread.interrupt();
        }
        super.onBackPressed();
    }

    @Override
    public void onPause() {
        super.onPause();
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        tv_masged_name.setText(Pref.getValue(getApplicationContext(), Constants.PREF_MASGED_NAME, "اسم المسجد"));
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        if (myThread.isAlive()) {
            myThread.interrupt();
        }
        if (mp.isPlaying()) {
            mp.stop();
        }
        mMtCentralManager.stopService();
        super.onDestroy();
    }

    @Override
    public void onRequestPermissionsResult(int code, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(code, permissions, grantResults);
        if (code == PERMISSION_COARSE_LOCATION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                initData();
            } else {
                finish();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_ENABLE_BT) {
            if (resultCode == Activity.RESULT_OK) {
                initData();
            } else {
                finish();
            }
        }
    }
    //endregion

    protected boolean isBLEEnabled() {
        final BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        final BluetoothAdapter adapter = bluetoothManager.getAdapter();
        return adapter != null && adapter.isEnabled();
    }

    private void showBLEDialog() {
        final Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
    }

    public void setLocale(String lang) {
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
    }

    private void initializeViewsAndListeners() {
        findViewById(R.id.img_settings).setOnClickListener(v -> {
            try {
                startActivity(new Intent(MainActivity.this, SettingActivity.class));
            } catch (Exception ignored) {
            }
        });

        img_bottom = findViewById(R.id.img_bottom);
        tv_masged_name = findViewById(R.id.tv_masged_name);
        tv_internal_heat = findViewById(R.id.tv_internal_heat);

        LinearLayout lay_sin = findViewById(R.id.lay_sin);
        LinearLayout lay_friday = findViewById(R.id.lay_friday);

        if (Pref.getValue(getApplicationContext(), Constants.PREF_SINSER_SHOW, false)) {
            lay_sin.setVisibility(View.VISIBLE);
            lay_friday.setVisibility(View.GONE);
        } else {
            lay_sin.setVisibility(View.GONE);
            lay_friday.setVisibility(View.VISIBLE);
        }

        tv_masged_name.setText(Pref.getValue(getApplicationContext(), Constants.PREF_MASGED_NAME, "اسم المسجد"));

        img_time_hour_1 = findViewById(R.id.img_time_hour_1);
        img_time_hour_2 = findViewById(R.id.img_time_hour_2);
        img_time_mint_1 = findViewById(R.id.img_time_mint_1);
        img_time_mint_2 = findViewById(R.id.img_time_mint_2);
        img_time_sec_2 = findViewById(R.id.img_time_sec_2);
        img_time_sec_1 = findViewById(R.id.img_time_sec_1);
        img_date_day = findViewById(R.id.img_date_day);
        img_date_month_m_2 = findViewById(R.id.img_date_month_m_2);
        img_date_month_m_1 = findViewById(R.id.img_date_month_m_1);
        img_date_month_m = findViewById(R.id.img_date_month_m);
        img_date_years_4 = findViewById(R.id.img_date_years_4);
        img_date_years_3 = findViewById(R.id.img_date_years_3);
        img_date_years_2 = findViewById(R.id.img_date_years_2);
        img_date_years_1 = findViewById(R.id.img_date_years_1);
        img_date_month_h_2 = findViewById(R.id.img_date_month_h_2);
        img_date_month_h_1 = findViewById(R.id.img_date_month_h_1);
        img_date_month_h = findViewById(R.id.img_date_month_h);
        img_date_years_h_4 = findViewById(R.id.img_date_years_h_4);
        img_date_years_h_3 = findViewById(R.id.img_date_years_h_3);
        img_date_years_h_2 = findViewById(R.id.img_date_years_h_2);
        img_date_years_h_1 = findViewById(R.id.img_date_years_h_1);

        img_next_azan = findViewById(R.id.img_next_azan);

        img_fjr_azan_h_1 = findViewById(R.id.img_fjr_azan_h_1);
        img_fjr_azan_h_2 = findViewById(R.id.img_fjr_azan_h_2);
        img_fjr_azan_m_1 = findViewById(R.id.img_fjr_azan_m_1);
        img_fjr_azan_m_2 = findViewById(R.id.img_fjr_azan_m_2);

        img_shroq_azan_h_1 = findViewById(R.id.img_shroq_azan_h_1);
        img_shroq_azan_h_2 = findViewById(R.id.img_shroq_azan_h_2);
        img_shroq_azan_m_1 = findViewById(R.id.img_shroq_azan_m_1);
        img_shroq_azan_m_2 = findViewById(R.id.img_shroq_azan_m_2);

        img_dahr_azan_h_1 = findViewById(R.id.img_daheq_azan_h_1);
        img_dahr_azan_h_2 = findViewById(R.id.img_daheq_azan_h_2);
        img_dahr_azan_m_1 = findViewById(R.id.img_daheq_azan_m_1);
        img_dahr_azan_m_2 = findViewById(R.id.img_daheq_azan_m_2);

        img_asr_azan_h_1 = findViewById(R.id.img_asr_azan_h_1);
        img_asr_azan_h_2 = findViewById(R.id.img_asr_azan_h_2);
        img_asr_azan_m_1 = findViewById(R.id.img_asr_azan_m_1);
        img_asr_azan_m_2 = findViewById(R.id.img_asr_azan_m_2);

        img_mgrb_azan_h_1 = findViewById(R.id.img_mgrb_azan_h_1);
        img_mgrb_azan_h_2 = findViewById(R.id.img_mgrb_azan_h_2);
        img_mgrb_azan_m_1 = findViewById(R.id.img_mgrb_azan_m_1);
        img_mgrb_azan_m_2 = findViewById(R.id.img_mgrb_azan_m_2);

        img_isha_azan_h_1 = findViewById(R.id.img_isha_azan_h_1);
        img_isha_azan_h_2 = findViewById(R.id.img_isha_azan_h_2);
        img_isha_azan_m_1 = findViewById(R.id.img_isha_azan_m_1);
        img_isha_azan_m_2 = findViewById(R.id.img_isha_azan_m_2);

        /* init fjr iqamh images */
        img_fjr_iqamh_h_1 = findViewById(R.id.img_fjr_iqamh_h_1);
        img_fjr_iqamh_h_2 = findViewById(R.id.img_fjr_iqamh_h_2);
        img_fjr_iqamh_m_1 = findViewById(R.id.img_fjr_iqamh_m_1);
        img_fjr_iqamh_m_2 = findViewById(R.id.img_fjr_iqamh_m_2);

        /* init shroq iqamh images */
        img_shroq_iqamh_h_1 = findViewById(R.id.img_shroq_iqamh_h_1);
        img_shroq_iqamh_h_2 = findViewById(R.id.img_shroq_iqamh_h_2);
        img_shroq_iqamh_m_1 = findViewById(R.id.img_shroq_iqamh_m_1);
        img_shroq_iqamh_m_2 = findViewById(R.id.img_shroq_iqamh_m_2);

        /* init dahr iqamg images */
        img_dahr_iqamg_h_1 = findViewById(R.id.img_daheq_iqamg_h_1);
        img_dahr_iqamg_h_2 = findViewById(R.id.img_daheq_iqamg_h_2);
        img_dahr_iqamg_m_1 = findViewById(R.id.img_daheq_iqamg_m_1);
        img_dahr_iqamg_m_2 = findViewById(R.id.img_daheq_iqamg_m_2);

        /* init asr iqamg images */
        img_asr_iqamg_h_1 = findViewById(R.id.img_asr_iqamg_h_1);
        img_asr_iqamg_h_2 = findViewById(R.id.img_asr_iqamg_h_2);
        img_asr_iqamg_m_1 = findViewById(R.id.img_asr_iqamg_m_1);
        img_asr_iqamg_m_2 = findViewById(R.id.img_asr_iqamg_m_2);

        /* init mgrb iqamg images */
        img_mgrb_iqamg_h_1 = findViewById(R.id.img_mgrb_iqamg_h_1);
        img_mgrb_iqamg_h_2 = findViewById(R.id.img_mgrb_iqamg_h_2);
        img_mgrb_iqamg_m_1 = findViewById(R.id.img_mgrb_iqamg_m_1);
        img_mgrb_iqamg_m_2 = findViewById(R.id.img_mgrb_iqamg_m_2);

        /* init isha iqamg images */
        img_isha_iqamh_h_1 = findViewById(R.id.img_isha_iqamh_h_1);
        img_isha_iqamh_h_2 = findViewById(R.id.img_isha_iqamh_h_2);
        img_isha_iqamh_m_1 = findViewById(R.id.img_isha_iqamh_m_1);
        img_isha_iqamh_m_2 = findViewById(R.id.img_isha_iqamh_m_2);

        /* init azan time left image */
        img_time_left_h_1 = findViewById(R.id.img_time_left_h_1);
        img_time_left_h_2 = findViewById(R.id.img_time_left_h_2);
        img_time_left_m_1 = findViewById(R.id.img_time_left_m_1);
        img_time_left_m_2 = findViewById(R.id.img_time_left_m_2);
        img_time_left_s_1 = findViewById(R.id.img_time_left_s_1);
        img_time_left_s_2 = findViewById(R.id.img_time_left_s_2);

        /* init iqamh time left image */
        img_iqamh_time_left_m_1 = findViewById(R.id.img_iqamh_time_left_m_1);
        img_iqamh_time_left_m_2 = findViewById(R.id.img_iqamh_time_left_m_2);
        img_iqamh_time_left_s_1 = findViewById(R.id.img_iqamh_time_left_s_1);
        img_iqamh_time_left_s_2 = findViewById(R.id.img_iqamh_time_left_s_2);

        /* init LinearLayout left */
        lay_img_azan_time_left = findViewById(R.id.lay_img_azan_time_left);
        lay_img_iqamh_time_left = findViewById(R.id.lay_img_iqamh_time_left);

        /* next azan jm3a    */
        img_azan_jm3a_time_h_1 = findViewById(R.id.img_azan_jm3a_time_h_1);
        img_azan_jm3a_time_h_2 = findViewById(R.id.img_azan_jm3a_time_h_2);
        img_azan_jm3a_time_m_1 = findViewById(R.id.img_azan_jm3a_time_m_1);
        img_azan_jm3a_time_m_2 = findViewById(R.id.img_azan_jm3a_time_m_2);
    }

    private void updatePrayerAndDateDisplays() {
        tv.setSelected(true);

        String text = db.getNews(Utils.getFormattedCurrentDate());

        if (text != null) {
            img_bottom.setVisibility(View.GONE);
            tv.setVisibility(View.VISIBLE);
            if (!isTextNewsShow) {
                if (text.length() <= 50) {
                    text = "                                            " + text;
                }
                tv.setText(text);
                isTextNewsShow = true;
            }
        } else {
            img_bottom.setVisibility(View.VISIBLE);
            tv.setVisibility(View.GONE);
            isTextNewsShow = false;
        }

        Calendar today1 = Calendar.getInstance();

        SimpleDateFormat format2 = new SimpleDateFormat("M/d", Locale.ENGLISH);
        SimpleDateFormat format1 = new SimpleDateFormat("yy", Locale.ENGLISH);

        db = new DataBaseHelper(getApplicationContext());
        String[] cityEn = getResources().getStringArray(R.array.city_name_en);
        String[] d = null;
        int cityChose = Pref.getValue(getApplicationContext(), Constants.PREF_CITY_POSITION_SELECTED, 90);
        if (cityChose != 0) {
            d = db.getPrayerTimes(cityEn[cityChose], format2.format(today1.getTime()) + "/" + getYears(Integer.parseInt(format1.format(today1.getTime()))));
        }

        assert d != null;
        cfajr1 = d[1];
        csunrise1 = d[2];
        cdhohr1 = d[3];
        casr1 = d[4];
        cmaghrib1 = d[5];
        cisha1 = d[6];

        cfajr = d[1];
        csunrise = d[2];
        cdhohr = d[3];
        casr = d[4];
        cmaghrib = d[5];
        cisha = d[6];

        SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mmaa", Locale.ENGLISH);
        SimpleDateFormat dateFormat2 = new SimpleDateFormat("hh:mm", Locale.ENGLISH);
        try {
            Date date = dateFormat2.parse(cfajr1);
            assert date != null;
            cfajr1 = dateFormat.format(date);
            Date date1 = dateFormat2.parse(csunrise1);
            assert date1 != null;
            csunrise1 = dateFormat.format(date1);
            Date date2 = dateFormat2.parse(cdhohr1);
            assert date2 != null;
            cdhohr1 = dateFormat.format(date2);
            Date date3 = dateFormat2.parse(casr1);
            assert date3 != null;
            casr1 = dateFormat.format(date3);
            Date date4 = dateFormat2.parse(cmaghrib1);
            assert date4 != null;
            cmaghrib1 = dateFormat.format(date4);
            Date date5 = dateFormat2.parse(cisha1);
            assert date5 != null;
            cisha1 = dateFormat.format(date5);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Pref.setValue(getApplicationContext(), Constants.PREF_FAJR_TIME_24, cfajr);
        Pref.setValue(getApplicationContext(), Constants.PREF_FAJR_TIME, cfajr1);
        Pref.setValue(getApplicationContext(), Constants.PREF_SUNRISE_TIME_24, csunrise);
        Pref.setValue(getApplicationContext(), Constants.PREF_SUNRISE_TIME, csunrise1);
        Pref.setValue(getApplicationContext(), Constants.PREF_DHOHR_TIME_24, cdhohr);
        Pref.setValue(getApplicationContext(), Constants.PREF_DHOHR_TIME, cdhohr1);
        Pref.setValue(getApplicationContext(), Constants.PREF_ASR_TIME_24, casr);
        Pref.setValue(getApplicationContext(), Constants.PREF_ASR_TIME, casr1);
        Pref.setValue(getApplicationContext(), Constants.PREF_MAGHRIB_TIME_24, cmaghrib);
        Pref.setValue(getApplicationContext(), Constants.PREF_MAGHRIB_TIME, cmaghrib1);
        Pref.setValue(getApplicationContext(), Constants.PREF_ISHA_TIME_24, cisha);
        Pref.setValue(getApplicationContext(), Constants.PREF_ISHA_TIME, cisha1);

        if (cfajr.length() == 4) {
            cfajr = "0" + cfajr;
        }
        if (csunrise.length() == 4) {
            csunrise = "0" + csunrise;
        }
        if (cdhohr.length() == 4) {
            cdhohr = "0" + cdhohr;
        }
        if (casr.length() == 4) {
            casr = "0" + casr;
        }
        if (cmaghrib.length() == 4) {
            cmaghrib = "0" + cmaghrib;
        }
        if (cisha.length() == 4) {
            cisha = "0" + cisha;
        }


        DateHigri hd = new DateHigri();
        date = Utils.writeIslamicDate(MainActivity.this, hd);

        int hijriDiff1 = Pref.getValue(MainActivity.this, Constants.PREF_HEJRY_INT1, 0);
        int iii = Integer.parseInt(date[4]);
        date[4] = String.valueOf(iii + hijriDiff1);


        /* set images date */
        img_date_day.setImageResource(daysImage[Integer.parseInt(date[0])]);
        img_date_month_m_1.setImageResource(dateNumber[Integer.parseInt(String.valueOf(date[1].charAt(0)))]);
        if (date[1].length() != 1) {
            img_date_month_m_2.setVisibility(View.VISIBLE);
            img_date_month_m_2.setImageResource(dateNumber[Integer.parseInt(String.valueOf(date[1].charAt(1)))]);
        } else {
            img_date_month_m_2.setVisibility(View.GONE);
        }
        img_date_month_m.setImageResource(monthImage[Integer.parseInt(date[2])]);
        img_date_years_1.setImageResource(dateNumber[Integer.parseInt(String.valueOf(date[3].charAt(0)))]);
        img_date_years_2.setImageResource(dateNumber[Integer.parseInt(String.valueOf(date[3].charAt(1)))]);
        img_date_years_3.setImageResource(dateNumber[Integer.parseInt(String.valueOf(date[3].charAt(2)))]);
        img_date_years_4.setImageResource(dateNumber[Integer.parseInt(String.valueOf(date[3].charAt(3)))]);
        img_date_month_h_1.setImageResource(dateNumber[Integer.parseInt(String.valueOf(date[4].charAt(0)))]);
        if (date[4].length() != 1) {
            img_date_month_h_2.setVisibility(View.VISIBLE);
            img_date_month_h_2.setImageResource(dateNumber[Integer.parseInt(String.valueOf(date[4].charAt(1)))]);
        } else {
            img_date_month_h_2.setVisibility(View.GONE);
        }
        img_date_month_h.setImageResource(monthImageHijri[Integer.parseInt(date[5]) - 1]);
        img_date_years_h_1.setImageResource(dateNumber[Integer.parseInt(String.valueOf(date[6].charAt(0)))]);
        img_date_years_h_2.setImageResource(dateNumber[Integer.parseInt(String.valueOf(date[6].charAt(1)))]);
        img_date_years_h_3.setImageResource(dateNumber[Integer.parseInt(String.valueOf(date[6].charAt(2)))]);
        img_date_years_h_4.setImageResource(dateNumber[Integer.parseInt(String.valueOf(date[6].charAt(3)))]);

        /* set images azan fajr */
        img_fjr_azan_h_1.setImageResource(timeNumberAzan[Integer.parseInt(String.valueOf(cfajr1.charAt(0)))]);
        img_fjr_azan_h_2.setImageResource(timeNumberAzan[Integer.parseInt(String.valueOf(cfajr1.charAt(1)))]);

        img_fjr_azan_m_1.setImageResource(timeNumberAzan[Integer.parseInt(String.valueOf(cfajr1.charAt(3)))]);
        img_fjr_azan_m_2.setImageResource(timeNumberAzan[Integer.parseInt(String.valueOf(cfajr1.charAt(4)))]);

        /* set images azan shroq */
        img_shroq_azan_h_1.setImageResource(timeNumberAzan[Integer.parseInt(String.valueOf(csunrise1.charAt(0)))]);
        img_shroq_azan_h_2.setImageResource(timeNumberAzan[Integer.parseInt(String.valueOf(csunrise1.charAt(1)))]);

        img_shroq_azan_m_1.setImageResource(timeNumberAzan[Integer.parseInt(String.valueOf(csunrise1.charAt(3)))]);
        img_shroq_azan_m_2.setImageResource(timeNumberAzan[Integer.parseInt(String.valueOf(csunrise1.charAt(4)))]);


        /* set images azan dahr */
        img_dahr_azan_h_1.setImageResource(timeNumberAzan[Integer.parseInt(String.valueOf(cdhohr1.charAt(0)))]);
        img_dahr_azan_h_2.setImageResource(timeNumberAzan[Integer.parseInt(String.valueOf(cdhohr1.charAt(1)))]);

        img_dahr_azan_m_1.setImageResource(timeNumberAzan[Integer.parseInt(String.valueOf(cdhohr1.charAt(3)))]);
        img_dahr_azan_m_2.setImageResource(timeNumberAzan[Integer.parseInt(String.valueOf(cdhohr1.charAt(4)))]);

        /* set images azan asr */
        img_asr_azan_h_1.setImageResource(timeNumberAzan[Integer.parseInt(String.valueOf(casr1.charAt(0)))]);
        img_asr_azan_h_2.setImageResource(timeNumberAzan[Integer.parseInt(String.valueOf(casr1.charAt(1)))]);

        img_asr_azan_m_1.setImageResource(timeNumberAzan[Integer.parseInt(String.valueOf(casr1.charAt(3)))]);
        img_asr_azan_m_2.setImageResource(timeNumberAzan[Integer.parseInt(String.valueOf(casr1.charAt(4)))]);

        /* set images azan mgrb */
        img_mgrb_azan_h_1.setImageResource(timeNumberAzan[Integer.parseInt(String.valueOf(cmaghrib1.charAt(0)))]);
        img_mgrb_azan_h_2.setImageResource(timeNumberAzan[Integer.parseInt(String.valueOf(cmaghrib1.charAt(1)))]);

        img_mgrb_azan_m_1.setImageResource(timeNumberAzan[Integer.parseInt(String.valueOf(cmaghrib1.charAt(3)))]);
        img_mgrb_azan_m_2.setImageResource(timeNumberAzan[Integer.parseInt(String.valueOf(cmaghrib1.charAt(4)))]);

        /* set images azan isha */
        img_isha_azan_h_1.setImageResource(timeNumberAzan[Integer.parseInt(String.valueOf(cisha1.charAt(0)))]);
        img_isha_azan_h_2.setImageResource(timeNumberAzan[Integer.parseInt(String.valueOf(cisha1.charAt(1)))]);

        img_isha_azan_m_1.setImageResource(timeNumberAzan[Integer.parseInt(String.valueOf(cisha1.charAt(3)))]);
        img_isha_azan_m_2.setImageResource(timeNumberAzan[Integer.parseInt(String.valueOf(cisha1.charAt(4)))]);

        long fajrIqamh, fajrSun, thuhrIqamh, assrIqamh, maghribIqamh, ishaaIqamh;
        String iqamhFjrTime, iqamhShroqTime, iqamhDahrTime, iqamhAsrTime, iqamhMgrmTime, iqamhIshaTime;
        if (!Pref.getValue(getApplicationContext(), Constants.PREF_FAJER_RELATIVE, false)) {
            fajrIqamh = Pref.getValue(getApplicationContext(), Constants.PREF_IQAMH_FAJR_RELATIVE_TIME_SELECTED, 25);
            iqamhFjrTime = getIqamh(String.valueOf(cfajr), fajrIqamh);
        } else {
            iqamhFjrTime = Pref.getValue(getApplicationContext(), Constants.PREF_IQAMH_FAJR_CONSTANT_TIME_SELECTED, "20:20");
        }
        if (!Pref.getValue(getApplicationContext(), Constants.PREF_SUNRISE_RELATIVE, false)) {
            fajrSun = Pref.getValue(getApplicationContext(), Constants.PREF_IQAMH_SUNRISE_RELATIVE_TIME_SELECTED, 20);
            iqamhShroqTime = getIqamh(String.valueOf(csunrise), fajrSun);
        } else {
            iqamhShroqTime = Pref.getValue(getApplicationContext(), Constants.PREF_IQAMH_SUNRISE_CONSTANT_TIME_SELECTED, "20:20");
        }
        if (!Pref.getValue(getApplicationContext(), Constants.PREF_DHOHR_RELATIVE, false)) {
            thuhrIqamh = Pref.getValue(getApplicationContext(), Constants.PREF_IQAMH_DHOHR_RELATIVE_TIME_SELECTED, 20);
            iqamhDahrTime = getIqamh(String.valueOf(cdhohr), thuhrIqamh);
        } else {
            iqamhDahrTime = Pref.getValue(getApplicationContext(), Constants.PREF_IQAMH_DHOHR_CONSTANT_TIME_SELECTED, "20:20");
        }
        if (!Pref.getValue(getApplicationContext(), Constants.PREF_ASR_RELATIVE, false)) {
            assrIqamh = Pref.getValue(getApplicationContext(), Constants.PREF_IQAMH_ASR_RELATIVE_TIME_SELECTED, 20);
            iqamhAsrTime = getIqamh(String.valueOf(casr), assrIqamh);
        } else {
            iqamhAsrTime = Pref.getValue(getApplicationContext(), Constants.PREF_IQAMH_ASR_CONSTANT_TIME_SELECTED, "20:20");
        }
        if (!Pref.getValue(getApplicationContext(), Constants.PREF_MAGHRIB_RELATIVE, false)) {
            maghribIqamh = Pref.getValue(getApplicationContext(), Constants.PREF_IQAMH_MAGHRIB_RELATIVE_TIME_SELECTED, 10);
            iqamhMgrmTime = getIqamh(String.valueOf(cmaghrib), maghribIqamh);
        } else {
            iqamhMgrmTime = Pref.getValue(getApplicationContext(), Constants.PREF_IQAMH_MAGHRIB_CONSTANT_TIME_SELECTED, "20:20");
        }
        if (!Pref.getValue(getApplicationContext(), Constants.PREF_ISHA_RELATIVE, false)) {
            ishaaIqamh = Pref.getValue(getApplicationContext(), Constants.PREF_IQAMH_ISHA_RELATIVE_TIME_SELECTED, 20);
            iqamhIshaTime = getIqamh(String.valueOf(cisha), ishaaIqamh);
        } else {
            iqamhIshaTime = Pref.getValue(getApplicationContext(), Constants.PREF_IQAMH_ISHA_CONSTANT_TIME_SELECTED, "20:20");
        }

        /* set iqamh fjr */
        img_fjr_iqamh_h_1.setImageResource(timeNumberIqamh[Integer.parseInt(String.valueOf(iqamhFjrTime.charAt(0)))]);
        img_fjr_iqamh_h_2.setImageResource(timeNumberIqamh[Integer.parseInt(String.valueOf(iqamhFjrTime.charAt(1)))]);
        img_fjr_iqamh_m_1.setImageResource(timeNumberIqamh[Integer.parseInt(String.valueOf(iqamhFjrTime.charAt(3)))]);
        img_fjr_iqamh_m_2.setImageResource(timeNumberIqamh[Integer.parseInt(String.valueOf(iqamhFjrTime.charAt(4)))]);

        /* set iqamh shroq */
        img_shroq_iqamh_h_1.setImageResource(timeNumberIqamh[Integer.parseInt(String.valueOf(iqamhShroqTime.charAt(0)))]);
        img_shroq_iqamh_h_2.setImageResource(timeNumberIqamh[Integer.parseInt(String.valueOf(iqamhShroqTime.charAt(1)))]);
        img_shroq_iqamh_m_1.setImageResource(timeNumberIqamh[Integer.parseInt(String.valueOf(iqamhShroqTime.charAt(3)))]);
        img_shroq_iqamh_m_2.setImageResource(timeNumberIqamh[Integer.parseInt(String.valueOf(iqamhShroqTime.charAt(4)))]);

        /* set dahr iqamg */
        img_dahr_iqamg_h_1.setImageResource(timeNumberIqamh[Integer.parseInt(String.valueOf(iqamhDahrTime.charAt(0)))]);
        img_dahr_iqamg_h_2.setImageResource(timeNumberIqamh[Integer.parseInt(String.valueOf(iqamhDahrTime.charAt(1)))]);
        img_dahr_iqamg_m_1.setImageResource(timeNumberIqamh[Integer.parseInt(String.valueOf(iqamhDahrTime.charAt(3)))]);
        img_dahr_iqamg_m_2.setImageResource(timeNumberIqamh[Integer.parseInt(String.valueOf(iqamhDahrTime.charAt(4)))]);

        /* set asr iqamg */
        img_asr_iqamg_h_1.setImageResource(timeNumberIqamh[Integer.parseInt(String.valueOf(iqamhAsrTime.charAt(0)))]);
        img_asr_iqamg_h_2.setImageResource(timeNumberIqamh[Integer.parseInt(String.valueOf(iqamhAsrTime.charAt(1)))]);
        img_asr_iqamg_m_1.setImageResource(timeNumberIqamh[Integer.parseInt(String.valueOf(iqamhAsrTime.charAt(3)))]);
        img_asr_iqamg_m_2.setImageResource(timeNumberIqamh[Integer.parseInt(String.valueOf(iqamhAsrTime.charAt(4)))]);

        /* set mgrb iqamg */
        img_mgrb_iqamg_h_1.setImageResource(timeNumberIqamh[Integer.parseInt(String.valueOf(iqamhMgrmTime.charAt(0)))]);
        img_mgrb_iqamg_h_2.setImageResource(timeNumberIqamh[Integer.parseInt(String.valueOf(iqamhMgrmTime.charAt(1)))]);
        img_mgrb_iqamg_m_1.setImageResource(timeNumberIqamh[Integer.parseInt(String.valueOf(iqamhMgrmTime.charAt(3)))]);
        img_mgrb_iqamg_m_2.setImageResource(timeNumberIqamh[Integer.parseInt(String.valueOf(iqamhMgrmTime.charAt(4)))]);

        /* set isha iqamg */
        img_isha_iqamh_h_1.setImageResource(timeNumberIqamh[Integer.parseInt(String.valueOf(iqamhIshaTime.charAt(0)))]);
        img_isha_iqamh_h_2.setImageResource(timeNumberIqamh[Integer.parseInt(String.valueOf(iqamhIshaTime.charAt(1)))]);
        img_isha_iqamh_m_1.setImageResource(timeNumberIqamh[Integer.parseInt(String.valueOf(iqamhIshaTime.charAt(3)))]);
        img_isha_iqamh_m_2.setImageResource(timeNumberIqamh[Integer.parseInt(String.valueOf(iqamhIshaTime.charAt(4)))]);


        getFridayTime();
        img_azan_jm3a_time_h_1.setImageResource(timeNumberAzanLeft[Integer.parseInt(String.valueOf(friady[3].charAt(0)))]);
        img_azan_jm3a_time_h_2.setImageResource(timeNumberAzanLeft[Integer.parseInt(String.valueOf(friady[3].charAt(1)))]);
        img_azan_jm3a_time_m_1.setImageResource(timeNumberAzanLeft[Integer.parseInt(String.valueOf(friady[3].charAt(3)))]);
        img_azan_jm3a_time_m_2.setImageResource(timeNumberAzanLeft[Integer.parseInt(String.valueOf(friady[3].charAt(4)))]);


    }

    private void updateNextPrayerIndicator() {


        long fajr = 0, sunrise = 0, thuhr = 0, assr = 0, maghrib = 0, ishaa = 0, fajrIqamh = 0, sunriseIqamh = 0, thuhrIqamh = 0, assrIqamh = 0, maghribIqamh = 0, ishaaIqamh = 0;


        SimpleDateFormat h_mm_a = new SimpleDateFormat("hh:mmaa", Locale.ENGLISH);
        SimpleDateFormat hh_mm_ss = new SimpleDateFormat("HH:mm:ss", Locale.ENGLISH);

        if (!Pref.getValue(getApplicationContext(), Constants.PREF_FAJER_RELATIVE, false)) {
            fajrIqamh = Pref.getValue(getApplicationContext(), Constants.PREF_IQAMH_FAJR_RELATIVE_TIME_SELECTED, 25);
            fajr = getMilliseconds(String.valueOf(cfajr)) + fajrIqamh * 60000;
        } else {


            String date2323 = Pref.getValue(getApplicationContext(), Constants.PREF_IQAMH_FAJR_CONSTANT_TIME_SELECTED, "20:20am");
            Date d1 = null;
            try {
                d1 = h_mm_a.parse(date2323);
            } catch (Exception e) {
                e.printStackTrace();
            }
            fajr = getMilliseconds((hh_mm_ss.format(d1)));
        }
        if (!Pref.getValue(getApplicationContext(), Constants.PREF_SUNRISE_RELATIVE, false)) {
            sunriseIqamh = Pref.getValue(getApplicationContext(), Constants.PREF_IQAMH_SUNRISE_RELATIVE_TIME_SELECTED, 20);
            sunrise = getMilliseconds(String.valueOf(csunrise)) + sunriseIqamh * 60000;
        } else {
            sunrise = getMilliseconds1(String.valueOf(csunrise), String.valueOf(Pref.getValue(getApplicationContext(), Constants.PREF_IQAMH_SUNRISE_CONSTANT_TIME_SELECTED, "20:20am")));
        }
        if (!Pref.getValue(getApplicationContext(), Constants.PREF_DHOHR_RELATIVE, false)) {
            thuhrIqamh = Pref.getValue(getApplicationContext(), Constants.PREF_IQAMH_DHOHR_RELATIVE_TIME_SELECTED, 20);
            thuhr = getMilliseconds(String.valueOf(cdhohr)) + thuhrIqamh * 60000;
        } else {
            String date2323 = Pref.getValue(getApplicationContext(), Constants.PREF_IQAMH_DHOHR_CONSTANT_TIME_SELECTED, "20:20am");
            Date d1 = null;
            try {
                d1 = h_mm_a.parse(date2323);
            } catch (Exception e) {
                e.printStackTrace();

            }
            thuhr = getMilliseconds((hh_mm_ss.format(d1)));

        }
        if (!Pref.getValue(getApplicationContext(), Constants.PREF_ASR_RELATIVE, false)) {
            assrIqamh = Pref.getValue(getApplicationContext(), Constants.PREF_IQAMH_ASR_RELATIVE_TIME_SELECTED, 20);
            assr = getMilliseconds(String.valueOf(casr)) + assrIqamh * 60000;

        } else {


            String date2323 = Pref.getValue(getApplicationContext(), Constants.PREF_IQAMH_ASR_CONSTANT_TIME_SELECTED, "20:20am");
            Date d1 = null;
            try {
                d1 = h_mm_a.parse(date2323);
            } catch (Exception e) {
                e.printStackTrace();
            }
            assr = getMilliseconds((hh_mm_ss.format(d1)));
        }
        if (!Pref.getValue(getApplicationContext(), Constants.PREF_MAGHRIB_RELATIVE, false)) {
            maghribIqamh = Pref.getValue(getApplicationContext(), Constants.PREF_IQAMH_MAGHRIB_RELATIVE_TIME_SELECTED, 20);
            maghrib = getMilliseconds(String.valueOf(cmaghrib)) + maghribIqamh * 60000;
        } else {


            String date2323 = Pref.getValue(getApplicationContext(), Constants.PREF_IQAMH_MAGHRIB_CONSTANT_TIME_SELECTED, "20:20am");
            Date d1 = null;
            try {
                d1 = h_mm_a.parse(date2323);
            } catch (Exception e) {
                e.printStackTrace();
            }
            maghrib = getMilliseconds((hh_mm_ss.format(d1)));

        }
        if (!Pref.getValue(getApplicationContext(), Constants.PREF_ISHA_RELATIVE, false)) {
            ishaaIqamh = Pref.getValue(getApplicationContext(), Constants.PREF_IQAMH_ISHA_RELATIVE_TIME_SELECTED, 20);
            ishaa = getMilliseconds(String.valueOf(cisha)) + ishaaIqamh * 60000;
        } else {


            String date2323 = Pref.getValue(getApplicationContext(), Constants.PREF_IQAMH_ISHA_CONSTANT_TIME_SELECTED, "20:20am");
            Date d1 = null;
            try {
                d1 = h_mm_a.parse(date2323);
            } catch (Exception e) {
                e.printStackTrace();
            }
            ishaa = getMilliseconds((hh_mm_ss.format(d1)));
        }

        String time = (String) android.text.format.DateFormat.format("HH:mm:aa", new Date());

        ViewGroup.MarginLayoutParams marginParams;


        if (getMilliseconds(time) >= getMilliseconds(String.valueOf(cfajr)) && getMilliseconds(time) < fajr) {


            switch (theme) {
                case 3:
                    img_next_azan1.setVisibility(View.GONE);
                    img_next_azan.setImageResource(R.drawable.img_to_iqamh_4);
                    marginParams = (ViewGroup.MarginLayoutParams) img_next_azan.getLayoutParams();
                    marginParams.setMargins(marginParams.leftMargin, marginParams.topMargin, 0, marginParams.bottomMargin);
                    break;
                case 4:
                    img_next_azan.setImageResource(R.drawable.img_to_iqamh_5);
                    break;
                case 5:
                    img_next_azan.setImageResource(R.drawable.img_to_iqamh6);
                    break;
                case 6:
                    img_next_azan1.setVisibility(View.GONE);
                    img_next_azan.setImageResource(R.drawable.img_to_iqamh7);
                    break;
                case 7:
                    img_next_azan1.setVisibility(View.GONE);
                    img_next_azan.setImageResource(R.drawable.img_to_iqamh8);
                    break;
                case 9:
                    img_next_azan1.setVisibility(View.GONE);
                    img_next_azan.setImageResource(R.drawable.img_to_iqamh10);
                    break;
                default:
                    img_next_azan.setImageResource(R.drawable.img_to_iqamh);
                    break;
            }
            getTimeLeftForIqamh(cfajr, fajrIqamh, 1);

            lay_img_azan_time_left.setVisibility(View.GONE);
            lay_img_iqamh_time_left.setVisibility(View.VISIBLE);
        } else if (getMilliseconds(time) > getMilliseconds(String.valueOf(cfajr)) && getMilliseconds(time) < getMilliseconds(String.valueOf(cdhohr))) {

            getTimeLeftForAzan(cdhohr, 3);

            Calendar calendar = Calendar.getInstance();
            if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY) {
                System.out.println("FRIDAY!");

                switch (theme) {
                    case 3:
                        img_next_azan1.setVisibility(View.VISIBLE);
                        img_next_azan.setImageResource(R.drawable.icon_next_azan_freday_4);
                        marginParams = (ViewGroup.MarginLayoutParams) img_next_azan.getLayoutParams();
                        marginParams.setMargins(marginParams.leftMargin, marginParams.topMargin, -30, marginParams.bottomMargin);
                        break;
                    case 4:
                        img_next_azan.setImageResource(R.drawable.icon_fri_1_theme_5_1);
                        break;
                    case 5:
                        img_next_azan.setImageResource(R.drawable.icon_fri_1_theme_6);
                        break;
                    case 6:
                        img_next_azan1.setVisibility(View.VISIBLE);
                        img_next_azan.setImageResource(R.drawable.icon_next_azan_freday_7);
                        break;
                    case 7:
                        img_next_azan1.setVisibility(View.VISIBLE);
                        img_next_azan.setImageResource(R.drawable.icon_next_azan_freday_8);
                        break;
                    case 9:
                        img_next_azan1.setVisibility(View.VISIBLE);
                        img_next_azan.setImageResource(R.drawable.icon_next_azan_freday_10);
                        break;
                    default:
                        img_next_azan.setImageResource(R.drawable.icon_next_azan_freday);


                        break;
                }
            } else {


                switch (theme) {

                    case 3:
                        img_next_azan1.setVisibility(View.VISIBLE);
                        img_next_azan.setImageResource(R.drawable.icon_next_azan_2_4);
                        break;
                    case 4:
                        img_next_azan.setImageResource(R.drawable.icon_next_azan_2_5);
                        break;
                    case 5:
                        img_next_azan.setImageResource(R.drawable.icon_next_azan_2_6);
                        break;
                    case 6:
                        img_next_azan1.setVisibility(View.VISIBLE);
                        img_next_azan.setImageResource(R.drawable.icon_next_azan_2_7);
                        break;
                    case 7:
                        img_next_azan1.setVisibility(View.VISIBLE);
                        img_next_azan.setImageResource(R.drawable.icon_next_azan_2_8);
                        break;
                    case 9:
                        img_next_azan1.setVisibility(View.VISIBLE);
                        img_next_azan.setImageResource(R.drawable.icon_next_azan_2_10);
                        break;
                    default:
                        img_next_azan.setImageResource(R.drawable.icon_next_azan_2);
                        break;
                }
            }

            lay_img_azan_time_left.setVisibility(View.VISIBLE);
            lay_img_iqamh_time_left.setVisibility(View.GONE);
        } else if (getMilliseconds(time) >= getMilliseconds(String.valueOf(cdhohr)) && getMilliseconds(time) < thuhr) {

            Calendar calendar = Calendar.getInstance();
            if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY) {
                System.out.println("FRIDAY!");


                switch (theme) {
                    case 3:
                        img_next_azan1.setVisibility(View.GONE);
                        img_next_azan.setImageResource(R.drawable.icon_fri_1_theme_4);
                        marginParams = (ViewGroup.MarginLayoutParams) img_next_azan.getLayoutParams();
                        marginParams.setMargins(marginParams.leftMargin, marginParams.topMargin, 30, marginParams.bottomMargin);
                        break;
                    case 4:
                        img_next_azan.setImageResource(R.drawable.icon_fri_1_theme_5);
                        break;
                    case 5:
                        img_next_azan.setImageResource(R.drawable.ic_pray_friday3);
                        break;
                    case 6:

                        img_next_azan1.setVisibility(View.GONE);
                        img_next_azan.setImageResource(R.drawable.ic_pray_friday3);
                        break;
                    case 8:

                        marginParams = (ViewGroup.MarginLayoutParams) img_next_azan.getLayoutParams();
                        marginParams.setMargins(marginParams.leftMargin, marginParams.topMargin, 10, marginParams.bottomMargin);
                        img_next_azan.setImageResource(R.drawable.ic_pray_friday);
                        break;
                    case 7:


                        img_next_azan1.setVisibility(View.GONE);
                        img_next_azan.setImageResource(R.drawable.icon_fri_1_theme_5);
                        break;
                    case 9:


                        img_next_azan1.setVisibility(View.GONE);
                        img_next_azan.setImageResource(R.drawable.ic_pray_friday3);
                        break;
                    default:
                        img_next_azan.setImageResource(R.drawable.ic_pray_friday);


                        break;
                }
            } else {
                switch (theme) {
                    case 3:
                        img_next_azan1.setVisibility(View.GONE);
                        img_next_azan.setImageResource(R.drawable.img_to_iqamh_4);
                        marginParams = (ViewGroup.MarginLayoutParams) img_next_azan.getLayoutParams();
                        marginParams.setMargins(marginParams.leftMargin, marginParams.topMargin, 0, marginParams.bottomMargin);
                        break;
                    case 4:
                        img_next_azan.setImageResource(R.drawable.img_to_iqamh_5);
                        break;
                    case 5:
                        img_next_azan.setImageResource(R.drawable.img_to_iqamh6);
                        break;
                    case 6:
                        img_next_azan1.setVisibility(View.GONE);
                        img_next_azan.setImageResource(R.drawable.img_to_iqamh7);
                        break;
                    case 7:
                        img_next_azan1.setVisibility(View.GONE);
                        img_next_azan.setImageResource(R.drawable.img_to_iqamh8);
                        break;
                    case 9:
                        img_next_azan1.setVisibility(View.GONE);
                        img_next_azan.setImageResource(R.drawable.img_to_iqamh10);
                        break;
                    default:
                        img_next_azan.setImageResource(R.drawable.img_to_iqamh);
                        break;
                }
            }

            getTimeLeftForIqamh(cdhohr, thuhrIqamh, 3);

            lay_img_azan_time_left.setVisibility(View.GONE);
            lay_img_iqamh_time_left.setVisibility(View.VISIBLE);
        } else if (getMilliseconds(time) > getMilliseconds(String.valueOf(cdhohr)) && getMilliseconds(time) < getMilliseconds(String.valueOf(casr))) {


            switch (theme) {
                case 3:
                    img_next_azan1.setVisibility(View.VISIBLE);
                    img_next_azan.setImageResource(R.drawable.icon_next_azan_3_4);
                    marginParams = (ViewGroup.MarginLayoutParams) img_next_azan.getLayoutParams();
                    marginParams.setMargins(marginParams.leftMargin, marginParams.topMargin, -30, marginParams.bottomMargin);
                    break;
                case 4:
                    img_next_azan.setImageResource(R.drawable.icon_next_azan_3_5);
                    break;
                case 5:
                    img_next_azan.setImageResource(R.drawable.icon_next_azan_3_6);
                    break;
                case 6:
                    img_next_azan1.setVisibility(View.VISIBLE);
                    img_next_azan.setImageResource(R.drawable.icon_next_azan_3_7);
                    break;
                case 7:
                    img_next_azan1.setVisibility(View.VISIBLE);
                    img_next_azan.setImageResource(R.drawable.icon_next_azan_3_8);
                    break;
                case 9:
                    img_next_azan1.setVisibility(View.VISIBLE);
                    img_next_azan.setImageResource(R.drawable.icon_next_azan_3_10);
                    break;
                default:
                    img_next_azan.setImageResource(R.drawable.icon_next_azan_3);
                    break;
            }


            getTimeLeftForAzan(casr1, 4);

            lay_img_azan_time_left.setVisibility(View.VISIBLE);
            lay_img_iqamh_time_left.setVisibility(View.GONE);
        } else if (getMilliseconds(time) >= getMilliseconds(String.valueOf(casr)) && getMilliseconds(time) < assr) {


            switch (theme) {
                case 3:
                    img_next_azan1.setVisibility(View.GONE);
                    img_next_azan.setImageResource(R.drawable.img_to_iqamh_4);
                    marginParams = (ViewGroup.MarginLayoutParams) img_next_azan.getLayoutParams();
                    marginParams.setMargins(marginParams.leftMargin, marginParams.topMargin, -0, marginParams.bottomMargin);
                    break;
                case 4:
                    img_next_azan.setImageResource(R.drawable.img_to_iqamh_5);
                    break;
                case 5:
                    img_next_azan.setImageResource(R.drawable.img_to_iqamh6);
                    break;
                case 6:
                    img_next_azan1.setVisibility(View.GONE);
                    img_next_azan.setImageResource(R.drawable.img_to_iqamh7);
                    break;
                case 7:
                    img_next_azan1.setVisibility(View.GONE);
                    img_next_azan.setImageResource(R.drawable.img_to_iqamh8);
                    break;
                case 9:
                    img_next_azan1.setVisibility(View.GONE);
                    img_next_azan.setImageResource(R.drawable.img_to_iqamh10);
                    break;
                default:
                    img_next_azan.setImageResource(R.drawable.img_to_iqamh);
                    break;
            }

            getTimeLeftForIqamh(casr, assrIqamh, 4);

            lay_img_azan_time_left.setVisibility(View.GONE);
            lay_img_iqamh_time_left.setVisibility(View.VISIBLE);
        } else if (getMilliseconds(time) > getMilliseconds(String.valueOf(casr)) && getMilliseconds(time) < getMilliseconds(String.valueOf(cmaghrib))) {


            switch (theme) {
                case 3:
                    img_next_azan1.setVisibility(View.VISIBLE);
                    img_next_azan.setImageResource(R.drawable.icon_next_azan_4_4);
                    marginParams = (ViewGroup.MarginLayoutParams) img_next_azan.getLayoutParams();
                    marginParams.setMargins(marginParams.leftMargin, marginParams.topMargin, -30, marginParams.bottomMargin);
                    break;
                case 4:
                    img_next_azan.setImageResource(R.drawable.icon_next_azan_4_5);
                    break;
                case 5:
                    img_next_azan.setImageResource(R.drawable.icon_next_azan_4_6);
                    break;
                case 6:
                    img_next_azan1.setVisibility(View.VISIBLE);
                    img_next_azan.setImageResource(R.drawable.icon_next_azan_4_7);
                    break;
                case 7:
                    img_next_azan1.setVisibility(View.VISIBLE);
                    img_next_azan.setImageResource(R.drawable.icon_next_azan_4_8);
                    break;
                case 9:
                    img_next_azan1.setVisibility(View.VISIBLE);
                    img_next_azan.setImageResource(R.drawable.icon_next_azan_4_10);
                    break;
                default:
                    img_next_azan.setImageResource(R.drawable.icon_next_azan_4);
                    break;
            }


            getTimeLeftForAzan(cmaghrib1, 5);

            lay_img_azan_time_left.setVisibility(View.VISIBLE);
            lay_img_iqamh_time_left.setVisibility(View.GONE);
        } else if (getMilliseconds(time) >= getMilliseconds(String.valueOf(cmaghrib)) && getMilliseconds(time) < maghrib) {


            switch (theme) {
                case 3:
                    img_next_azan1.setVisibility(View.GONE);
                    img_next_azan.setImageResource(R.drawable.img_to_iqamh_4);
                    marginParams = (ViewGroup.MarginLayoutParams) img_next_azan.getLayoutParams();
                    marginParams.setMargins(marginParams.leftMargin, marginParams.topMargin, 0, marginParams.bottomMargin);
                    break;
                case 4:
                    img_next_azan.setImageResource(R.drawable.img_to_iqamh_5);
                    break;
                case 5:
                    img_next_azan.setImageResource(R.drawable.img_to_iqamh6);
                    break;
                case 6:
                    img_next_azan1.setVisibility(View.GONE);
                    img_next_azan.setImageResource(R.drawable.img_to_iqamh7);
                    break;
                case 7:
                    img_next_azan1.setVisibility(View.GONE);
                    img_next_azan.setImageResource(R.drawable.img_to_iqamh8);
                    break;
                case 9:
                    img_next_azan1.setVisibility(View.GONE);
                    img_next_azan.setImageResource(R.drawable.img_to_iqamh10);
                    break;
                default:
                    img_next_azan.setImageResource(R.drawable.img_to_iqamh);
                    break;
            }


            getTimeLeftForIqamh(cmaghrib, maghribIqamh, 5);

            lay_img_azan_time_left.setVisibility(View.GONE);
            lay_img_iqamh_time_left.setVisibility(View.VISIBLE);
        } else if (getMilliseconds(time) > getMilliseconds(String.valueOf(cmaghrib)) && getMilliseconds(time) < getMilliseconds(String.valueOf(cisha))) {


            switch (theme) {
                case 3:
                    img_next_azan1.setVisibility(View.VISIBLE);
                    img_next_azan.setImageResource(R.drawable.icon_next_azan_5_4);
                    marginParams = (ViewGroup.MarginLayoutParams) img_next_azan.getLayoutParams();
                    marginParams.setMargins(marginParams.leftMargin, marginParams.topMargin, -30, marginParams.bottomMargin);
                    break;
                case 4:
                    img_next_azan.setImageResource(R.drawable.icon_next_azan_5_5);
                    break;
                case 5:
                    img_next_azan.setImageResource(R.drawable.icon_next_azan_5_6);
                    break;
                case 6:
                    img_next_azan1.setVisibility(View.VISIBLE);
                    img_next_azan.setImageResource(R.drawable.icon_next_azan_5_7);
                    break;
                case 7:
                    img_next_azan1.setVisibility(View.VISIBLE);
                    img_next_azan.setImageResource(R.drawable.icon_next_azan_5_8);
                    break;
                case 9:
                    img_next_azan1.setVisibility(View.VISIBLE);
                    img_next_azan.setImageResource(R.drawable.icon_next_azan_5_10);
                    break;
                default:
                    img_next_azan.setImageResource(R.drawable.icon_next_azan_5);
                    break;
            }


            getTimeLeftForAzan(cisha1, 6);

            lay_img_azan_time_left.setVisibility(View.VISIBLE);
            lay_img_iqamh_time_left.setVisibility(View.GONE);
        } else if (getMilliseconds(time) >= getMilliseconds(String.valueOf(cisha)) && getMilliseconds(time) < ishaa) {


            switch (theme) {
                case 3:
                    img_next_azan1.setVisibility(View.GONE);
                    img_next_azan.setImageResource(R.drawable.img_to_iqamh_4);
                    marginParams = (ViewGroup.MarginLayoutParams) img_next_azan.getLayoutParams();
                    marginParams.setMargins(marginParams.leftMargin, marginParams.topMargin, 0, marginParams.bottomMargin);
                    break;
                case 4:
                    img_next_azan.setImageResource(R.drawable.img_to_iqamh_5);
                    break;
                case 5:
                    img_next_azan.setImageResource(R.drawable.img_to_iqamh6);
                    break;
                case 6:
                    img_next_azan1.setVisibility(View.GONE);
                    img_next_azan.setImageResource(R.drawable.img_to_iqamh7);
                    break;
                case 7:
                    img_next_azan1.setVisibility(View.GONE);
                    img_next_azan.setImageResource(R.drawable.img_to_iqamh8);
                    break;
                case 9:
                    img_next_azan1.setVisibility(View.GONE);
                    img_next_azan.setImageResource(R.drawable.img_to_iqamh10);
                    break;
                default:
                    img_next_azan.setImageResource(R.drawable.img_to_iqamh);
                    break;
            }


            getTimeLeftForIqamh(cisha, ishaaIqamh, 6);

            lay_img_azan_time_left.setVisibility(View.GONE);
            lay_img_iqamh_time_left.setVisibility(View.VISIBLE);
        } else if (getMilliseconds(time) > getMilliseconds(String.valueOf(cfajr)) || getMilliseconds(time) < getMilliseconds(String.valueOf(cfajr))) {


            switch (theme) {
                case 3:
                    img_next_azan1.setVisibility(View.VISIBLE);
                    img_next_azan.setImageResource(R.drawable.icon_next_azan_1_4);
                    marginParams = (ViewGroup.MarginLayoutParams) img_next_azan.getLayoutParams();
                    marginParams.setMargins(marginParams.leftMargin, marginParams.topMargin, -30, marginParams.bottomMargin);
                    break;
                case 4:
                    img_next_azan.setImageResource(R.drawable.icon_next_azan_1_5);
                    break;
                case 5:
                    img_next_azan.setImageResource(R.drawable.icon_next_azan_1_6);
                    break;
                case 6:
                    img_next_azan1.setVisibility(View.VISIBLE);
                    img_next_azan.setImageResource(R.drawable.icon_next_azan_1_7);
                    break;
                case 7:
                    img_next_azan1.setVisibility(View.VISIBLE);
                    img_next_azan.setImageResource(R.drawable.icon_next_azan_1_8);
                    break;
                case 9:
                    img_next_azan1.setVisibility(View.VISIBLE);
                    img_next_azan.setImageResource(R.drawable.icon_next_azan_1_10);
                    break;
                default:
                    img_next_azan.setImageResource(R.drawable.icon_next_azan_1);
                    break;
            }

            getTimeLeftForAzan(cfajr1, 1);

            lay_img_azan_time_left.setVisibility(View.VISIBLE);
            lay_img_iqamh_time_left.setVisibility(View.GONE);
        } else {
            img_iqamh_time_left_m_1.setImageResource(timeNumber[0]);
            img_iqamh_time_left_m_2.setImageResource(timeNumber[0]);
            img_iqamh_time_left_s_1.setImageResource(timeNumber[0]);
            img_iqamh_time_left_s_2.setImageResource(timeNumber[0]);

            img_time_left_h_1.setImageResource(timeNumber[0]);
            img_time_left_h_2.setImageResource(timeNumber[0]);
            img_time_left_m_1.setImageResource(timeNumber[0]);
            img_time_left_m_2.setImageResource(timeNumber[0]);
            img_time_left_s_1.setImageResource(timeNumber[0]);
            img_time_left_s_2.setImageResource(timeNumber[0]);
        }


    }

    private void refreshUIEverySecond() {
        runOnUiThread(() -> {
            updatePrayerAndDateDisplays();
            updateNextPrayerIndicator();
            checkAds();
            DateFormat timeNow = new SimpleDateFormat("hh:mmass", Locale.ENGLISH);
            Calendar c = Calendar.getInstance();
            String timeText = timeNow.format(c.getTime());
            img_time_hour_1.setImageResource(timeNumber[Integer.parseInt(String.valueOf(timeText.charAt(0)))]);

            img_time_hour_2.setImageResource(timeNumber[Integer.parseInt(String.valueOf(timeText.charAt(1)))]);
            img_time_mint_1.setImageResource(timeNumber[Integer.parseInt(String.valueOf(timeText.charAt(3)))]);
            img_time_mint_2.setImageResource(timeNumber[Integer.parseInt(String.valueOf(timeText.charAt(4)))]);
            img_time_sec_1.setImageResource(timeNumberSec[Integer.parseInt(String.valueOf(timeText.charAt(7)))]);
            img_time_sec_2.setImageResource(timeNumberSec[Integer.parseInt(String.valueOf(timeText.charAt(8)))]);


        });
    }

    class CountDownRunner implements Runnable {

        public void run() {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    refreshUIEverySecond();
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } catch (Exception e) {

                }
            }
        }

    }

    private String getIqamh(String azanTime, long aqamhTime) {
        SimpleDateFormat formatter = new SimpleDateFormat("hh:mmaa", Locale.ENGLISH);
        try {
            SimpleDateFormat dateFormat2 = new SimpleDateFormat("hh:mm", Locale.ENGLISH);
            Date mDate = dateFormat2.parse(azanTime);
            long timeInMilliseconds = mDate.getTime();
            long minutes = aqamhTime * 60000;
            String dateString = formatter.format(new Date(timeInMilliseconds + minutes));
            return dateString;
        } catch (ParseException e) {


            e.printStackTrace();
            return null;
        }
    }

    private long getMilliseconds(String azanTime) {
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm", Locale.ENGLISH);

        Date mDate = null;
        try {
            mDate = formatter.parse(String.valueOf(azanTime));
        } catch (ParseException e) {

            e.printStackTrace();
        }
        return mDate.getTime();

    }

    private long getMilliseconds1(String azanTime, String iqamhTIme) {
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm", Locale.ENGLISH);
        SimpleDateFormat formatter1 = new SimpleDateFormat("hh:mmaa", Locale.ENGLISH);

        Date mDate = null;
        Date mDate1 = null;
        try {
            mDate = formatter.parse(String.valueOf(azanTime));
            mDate1 = formatter1.parse(String.valueOf(iqamhTIme));
        } catch (ParseException e) {

            e.printStackTrace();
        }
        return mDate1.getTime() - mDate.getTime();

    }

    private void getTimeLeftForAzan(String time, int check) {
        Calendar c = Calendar.getInstance();
        Calendar c1 = Calendar.getInstance();

        String hour = time.charAt(0) + "" + time.charAt(1);
        String mint = time.charAt(3) + "" + time.charAt(4);
        String sec = 0 + "" + 0;

        c.set(c1.get(Calendar.YEAR), c1.get(Calendar.MONTH) + 1, c1.get(Calendar.DATE), Integer.parseInt(hour), Integer.parseInt(mint), Integer.parseInt(sec));


        String givenDateString = time;
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm", Locale.ENGLISH);

        long timeInMilliseconds = 0;
        try {
            Date mDate = sdf.parse(givenDateString);
            timeInMilliseconds = mDate.getTime();

        } catch (ParseException e) {
            e.printStackTrace();
        }

        DateFormat timeNow = new SimpleDateFormat("hh:mmss", Locale.ENGLISH);

        Calendar c2 = Calendar.getInstance();
        String timeText = timeNow.format(c2.getTime());

        String givenDateString1 = timeText;
        SimpleDateFormat sdf1 = new SimpleDateFormat("hh:mmss", Locale.ENGLISH);
        long timeInMilliseconds1 = 0;
        try {
            Date mDate1 = sdf1.parse(givenDateString1);
            timeInMilliseconds1 = mDate1.getTime();

        } catch (ParseException e) {
            e.printStackTrace();
        }

        long millis = c.getTimeInMillis();
        long millis1;
        switch (check) {

            case 4:
            case 5:
            case 6:
                millis1 = timeInMilliseconds - timeInMilliseconds1;
                break;
            case 3:
            case 1:
                millis1 = millis - c1.getTimeInMillis();
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + check);
        }


        DateFormat formatter = new SimpleDateFormat("HH:mm:ss", Locale.ENGLISH);
        formatter.setTimeZone(TimeZone.getTimeZone("GMT"));
        String s11 = formatter.format(new Date(millis1));
        String s = String.valueOf(s11.charAt(0));
        String s0 = String.valueOf(s11.charAt(1));
        String s1 = String.valueOf(s11.charAt(3));
        String s2 = String.valueOf(s11.charAt(4));
        String s3 = String.valueOf(s11.charAt(6));
        String s4 = String.valueOf(s11.charAt(7));


        if (s.equals("0") && s0.equals("0") && s1.equals("0") && s2.equals("1") && s3.equals("0") && s4.equals("0")) {
            switch (check) {
                case 1:

                    if (Pref.getValue(getApplicationContext(), Constants.PREF_FAJER_SOUND_AZAN, false)) {
                        if (Pref.getValue(getApplicationContext(), Constants.PREF_FAJER_SOUND_AZAN_PATH, null) != null) {
                            if (!mp.isPlaying()) {
                                mp = new MediaPlayer();
                                String uriSound = Pref.getValue(getApplicationContext(), Constants.PREF_FAJER_SOUND_AZAN_PATH, null);
                                if (!mp.isPlaying()) play(uriSound);
                            }
                        }
                    }
                    break;
                case 3:


                    if (Pref.getValue(getApplicationContext(), Constants.PREF_DHOHR_SOUND_AZAN, false)) {
                        if (Pref.getValue(getApplicationContext(), Constants.PREF_DHOHR_SOUND_AZAN_PATH, null) != null) {
                            if (!mp.isPlaying()) {
                                Calendar calendar = Calendar.getInstance();
                                if (calendar.get(Calendar.DAY_OF_WEEK) != Calendar.FRIDAY) {
                                    mp = new MediaPlayer();
                                    String uriSound = Pref.getValue(getApplicationContext(), Constants.PREF_DHOHR_SOUND_AZAN_PATH, null);
                                    if (!mp.isPlaying()) play(uriSound);
                                }
                            }
                        }
                    }
                    break;
                case 4:

                    if (Pref.getValue(getApplicationContext(), Constants.PREF_ASR_SOUND_AZAN, false)) {
                        if (Pref.getValue(getApplicationContext(), Constants.PREF_ASR_SOUND_AZAN_PATH, null) != null) {
                            if (!mp.isPlaying()) {
                                mp = new MediaPlayer();
                                String uriSound = Pref.getValue(getApplicationContext(), Constants.PREF_ASR_SOUND_AZAN_PATH, null);
                                if (!mp.isPlaying()) play(uriSound);
                            }
                        }
                    }
                    break;
                case 5:

                    if (Pref.getValue(getApplicationContext(), Constants.PREF_MAGHRIB_SOUND_AZAN, false)) {
                        if (Pref.getValue(getApplicationContext(), Constants.PREF_MAGHRIB_SOUND_AZAN_PATH, null) != null) {
                            if (!mp.isPlaying()) {
                                mp = new MediaPlayer();
                                String uriSound = Pref.getValue(getApplicationContext(), Constants.PREF_MAGHRIB_SOUND_AZAN_PATH, null);
                                if (!mp.isPlaying()) play(uriSound);
                            }
                        }
                    }
                    break;
                case 6:

                    if (Pref.getValue(getApplicationContext(), Constants.PREF_ISHA_SOUND_AZAN, false)) {
                        if (Pref.getValue(getApplicationContext(), Constants.PREF_ISHA_SOUND_AZAN_PATH, null) != null) {
                            if (!mp.isPlaying()) {
                                mp = new MediaPlayer();
                                String uriSound = Pref.getValue(getApplicationContext(), Constants.PREF_ISHA_SOUND_AZAN_PATH, null);
                                if (!mp.isPlaying()) play(uriSound);
                            }
                        }
                    }
                    break;
            }
        }


        Calendar calendar = Calendar.getInstance();

        if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY && check == 3) {
            long seconds = 60 * 1000;

            if (seconds == millis1) {
                if (Pref.getValue(getApplicationContext(), Constants.PREF_CLOSE_NOTIFICATION_SCREEN, true)) {

                    if (!isOpenClosePhone) {
                        Intent intent = new Intent(MainActivity.this, ShowClosePhoneActivity.class);
                        intent.putExtra("PRAY", check);
                        startActivity(intent);
                        isOpenClosePhone = true;
                    }
                }
            } else {


            }
        }

        img_time_left_h_1.setImageResource(timeNumberAzanLeft[Integer.parseInt(String.valueOf(s11.charAt(0)))]);
        img_time_left_h_2.setImageResource(timeNumberAzanLeft[Integer.parseInt(String.valueOf(s11.charAt(1)))]);
        img_time_left_m_1.setImageResource(timeNumberAzanLeft[Integer.parseInt(String.valueOf(s11.charAt(3)))]);
        img_time_left_m_2.setImageResource(timeNumberAzanLeft[Integer.parseInt(String.valueOf(s11.charAt(4)))]);
        img_time_left_s_1.setImageResource(timeNumberAzanLeft[Integer.parseInt(String.valueOf(s11.charAt(6)))]);
        img_time_left_s_2.setImageResource(timeNumberAzanLeft[Integer.parseInt(String.valueOf(s11.charAt(7)))]);
    }

    private void getTimeLeftForIqamh(String time, long iqmahTime, int check) {
        long minutes;
        long millis;
        Calendar c;
        Calendar c1;
        long millis1 = 0;
        String s = null;
        switch (check) {
            case 1:
                if (!Pref.getValue(getApplicationContext(), Constants.PREF_FAJER_RELATIVE, false)) {
                    c = Calendar.getInstance();
                    c1 = Calendar.getInstance();
                    String hour = time.charAt(0) + "" + time.charAt(1);
                    String mint = time.charAt(3) + "" + time.charAt(4);
                    String sec = 0 + "" + 0;
                    c.set(c1.get(Calendar.YEAR), c1.get(Calendar.MONDAY), c1.get(Calendar.DAY_OF_MONTH), Integer.parseInt(hour), Integer.parseInt(mint), Integer.parseInt(sec));

                    minutes = iqmahTime * 60000;
                    millis = c.getTimeInMillis() + minutes;
                    millis1 = millis - c1.getTimeInMillis();
                } else {
                    String time1 = Pref.getValue(getApplicationContext(), Constants.PREF_IQAMH_FAJR_CONSTANT_TIME_SELECTED, "20:20am");
                    c = Calendar.getInstance();
                    c1 = Calendar.getInstance();
                    String hour = time1.charAt(0) + "" + time1.charAt(1);
                    String mint = time1.charAt(3) + "" + time1.charAt(4);
                    String sec = 0 + "" + 0;
                    c.set(c1.get(Calendar.YEAR), c1.get(Calendar.MONDAY), c1.get(Calendar.DAY_OF_MONTH), Integer.parseInt(hour), Integer.parseInt(mint), Integer.parseInt(sec));
                    millis1 = c.getTimeInMillis() - c1.getTimeInMillis();
                }
                s = String.format("%1$tM:%1$tS", millis1);
                break;
            case 3:
                if (!Pref.getValue(getApplicationContext(), Constants.PREF_DHOHR_RELATIVE, false)) {
                    c = Calendar.getInstance();
                    c1 = Calendar.getInstance();
                    String hour = time.charAt(0) + "" + time.charAt(1);
                    String mint = time.charAt(3) + "" + time.charAt(4);
                    String sec = 0 + "" + 0;
                    c.set(c1.get(Calendar.YEAR), c1.get(Calendar.MONDAY), c1.get(Calendar.DAY_OF_MONTH), Integer.parseInt(hour), Integer.parseInt(mint), Integer.parseInt(sec));

                    minutes = iqmahTime * 60000;
                    millis = c.getTimeInMillis() + minutes;
                    millis1 = millis - c1.getTimeInMillis();
                } else {
                    String time1 = Pref.getValue(getApplicationContext(), Constants.PREF_IQAMH_DHOHR_CONSTANT_TIME_SELECTED, "20:20am");
                    c = Calendar.getInstance();
                    c1 = Calendar.getInstance();
                    String hour = time1.charAt(0) + "" + time1.charAt(1);
                    String mint = time1.charAt(3) + "" + time1.charAt(4);
                    String sec = 0 + "" + 0;
                    c.set(c1.get(Calendar.YEAR), c1.get(Calendar.MONDAY), c1.get(Calendar.DAY_OF_MONTH), Integer.parseInt(hour), Integer.parseInt(mint), Integer.parseInt(sec));
                    millis1 = c.getTimeInMillis() - c1.getTimeInMillis();
                }
                s = String.format("%1$tM:%1$tS", millis1);
                break;
            case 4:
                if (!Pref.getValue(getApplicationContext(), Constants.PREF_ASR_RELATIVE, false)) {
                    c = Calendar.getInstance();
                    c1 = Calendar.getInstance();
                    String hour = time.charAt(0) + "" + time.charAt(1);
                    String mint = time.charAt(3) + "" + time.charAt(4);
                    String sec = 0 + "" + 0;
                    c.set(c1.get(Calendar.YEAR), c1.get(Calendar.MONDAY), c1.get(Calendar.DAY_OF_MONTH), Integer.parseInt(hour), Integer.parseInt(mint), Integer.parseInt(sec));

                    minutes = iqmahTime * 60000;
                    millis = c.getTimeInMillis() + minutes;
                    millis1 = millis - c1.getTimeInMillis();
                } else {
                    String time1 = Pref.getValue(getApplicationContext(), Constants.PREF_IQAMH_ASR_CONSTANT_TIME_SELECTED, "20:20am");
                    c = Calendar.getInstance();
                    c1 = Calendar.getInstance();
                    String hour = time1.charAt(0) + "" + time1.charAt(1);
                    String mint = time1.charAt(3) + "" + time1.charAt(4);
                    String sec = 0 + "" + 0;
                    c.set(c1.get(Calendar.YEAR), c1.get(Calendar.MONDAY), c1.get(Calendar.DAY_OF_MONTH), Integer.parseInt(hour), Integer.parseInt(mint), Integer.parseInt(sec));
                    millis1 = c.getTimeInMillis() - c1.getTimeInMillis() + 43200000;
                }
                s = String.format("%1$tM:%1$tS", millis1);
                break;
            case 5:
                if (!Pref.getValue(getApplicationContext(), Constants.PREF_MAGHRIB_RELATIVE, false)) {
                    c = Calendar.getInstance();
                    c1 = Calendar.getInstance();
                    String hour = time.charAt(0) + "" + time.charAt(1);
                    String mint = time.charAt(3) + "" + time.charAt(4);
                    String sec = 0 + "" + 0;
                    c.set(c1.get(Calendar.YEAR), c1.get(Calendar.MONDAY), c1.get(Calendar.DAY_OF_MONTH), Integer.parseInt(hour), Integer.parseInt(mint), Integer.parseInt(sec));

                    minutes = iqmahTime * 60000;
                    millis = c.getTimeInMillis() + minutes;
                    millis1 = millis - c1.getTimeInMillis();
                } else {
                    String time1 = Pref.getValue(getApplicationContext(), Constants.PREF_IQAMH_MAGHRIB_CONSTANT_TIME_SELECTED, "20:20am");
                    c = Calendar.getInstance();
                    c1 = Calendar.getInstance();
                    String hour = time1.charAt(0) + "" + time1.charAt(1);
                    String mint = time1.charAt(3) + "" + time1.charAt(4);
                    String sec = 0 + "" + 0;
                    c.set(c1.get(Calendar.YEAR), c1.get(Calendar.MONDAY), c1.get(Calendar.DAY_OF_MONTH), Integer.parseInt(hour), Integer.parseInt(mint), Integer.parseInt(sec));
                    millis1 = c.getTimeInMillis() - c1.getTimeInMillis() + 43200000;
                }
                s = String.format("%1$tM:%1$tS", millis1);
                break;
            case 6:
                if (!Pref.getValue(getApplicationContext(), Constants.PREF_ISHA_RELATIVE, false)) {
                    c = Calendar.getInstance();
                    c1 = Calendar.getInstance();
                    String hour = time.charAt(0) + "" + time.charAt(1);
                    String mint = time.charAt(3) + "" + time.charAt(4);
                    String sec = 0 + "" + 0;
                    c.set(c1.get(Calendar.YEAR), c1.get(Calendar.MONDAY), c1.get(Calendar.DAY_OF_MONTH), Integer.parseInt(hour), Integer.parseInt(mint), Integer.parseInt(sec));

                    minutes = iqmahTime * 60000;
                    millis = c.getTimeInMillis() + minutes;
                    millis1 = millis - c1.getTimeInMillis();
                } else {
                    String time1 = Pref.getValue(getApplicationContext(), Constants.PREF_IQAMH_ISHA_CONSTANT_TIME_SELECTED, "20:20am");
                    c = Calendar.getInstance();
                    c1 = Calendar.getInstance();
                    String hour = time1.charAt(0) + "" + time1.charAt(1);
                    String mint = time1.charAt(3) + "" + time1.charAt(4);
                    String sec = 0 + "" + 0;
                    c.set(c1.get(Calendar.YEAR), c1.get(Calendar.MONDAY), c1.get(Calendar.DAY_OF_MONTH), Integer.parseInt(hour), Integer.parseInt(mint), Integer.parseInt(sec));
                    millis1 = c.getTimeInMillis() - c1.getTimeInMillis() + 43200000;
                }
                s = String.format("%1$tM:%1$tS", millis1);
                break;
        }

        Calendar calendar = Calendar.getInstance();

        if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY && check == 3) {
            img_iqamh_time_left_m_1.setVisibility(View.GONE);
            img_iqamh_time_left_m_2.setVisibility(View.GONE);
            img_iqamh_time_left_s_1.setVisibility(View.GONE);
            img_iqamh_time_left_s_2.setVisibility(View.GONE);


            if (!isOpenKhotabActivity) {

                SimpleDateFormat format2 = new SimpleDateFormat("yyyy/M/d", Locale.ENGLISH);
                Calendar today1 = Calendar.getInstance();

                db.openDataBase();
                final Khotab khotba = db.getKhotba(format2.format(today1.getTime()));
                db.close();

                if (khotba != null) {
                    if (khotba.getIsException() == 0) {
                        Intent cp = new Intent(getApplicationContext(), ShowKhotabActivity.class);
                        cp.putExtra("khotba", khotba);
                        startActivity(cp);
                        isOpenKhotabActivity = true;
                    }
                } else if (Pref.getValue(getApplicationContext(), Constants.PREF_SHOW_KATEB, false)) {
                    Intent cp = new Intent(getApplicationContext(), ShowKatebActivity.class);
                    startActivity(cp);
                    isOpenKhotabActivity = true;
                } else {

                }
            }
        } else {
            img_iqamh_time_left_m_1.setVisibility(View.VISIBLE);
            img_iqamh_time_left_m_2.setVisibility(View.VISIBLE);
            img_iqamh_time_left_s_1.setVisibility(View.VISIBLE);
            img_iqamh_time_left_s_2.setVisibility(View.VISIBLE);

            long seconds = 60 * 1000;


            if (seconds >= millis1) {


                if (Pref.getValue(getApplicationContext(), Constants.PREF_CLOSE_NOTIFICATION_SCREEN, true)) {

                    if (!isOpenClosePhone) {

                        Intent intent = new Intent(MainActivity.this, ShowClosePhoneActivity.class);
                        intent.putExtra("PRAY", check);
                        startActivity(intent);
                        isOpenClosePhone = true;

                    }
                }
            } else {


            }

        }

        img_iqamh_time_left_m_1.setImageResource(timeNumberIqamhLeft[Integer.parseInt(String.valueOf(s.charAt(0)))]);
        img_iqamh_time_left_m_2.setImageResource(timeNumberIqamhLeft[Integer.parseInt(String.valueOf(s.charAt(1)))]);
        img_iqamh_time_left_s_1.setImageResource(timeNumberIqamhLeft[Integer.parseInt(String.valueOf(s.charAt(3)))]);
        img_iqamh_time_left_s_2.setImageResource(timeNumberIqamhLeft[Integer.parseInt(String.valueOf(s.charAt(4)))]);


        if (millis1 <= 1000) {

            if (calendar.get(Calendar.DAY_OF_WEEK) != Calendar.FRIDAY && check != 3) {

                boolean b = shouldShowAlkhushueScreen(check);

                if (!Pref.getValue(getApplicationContext(), Constants.PREF_CLOSE_NOTIFICATION_SCREEN, true)) {


                    if (b) {

                        if (!isOpenAlkhushuePhone) {

                            Intent intent = new Intent(MainActivity.this, ShowAlkhushueActivity.class);
                            intent.putExtra("PRAY", check);
                            startActivity(intent);

                            isOpenAlkhushuePhone = true;
                        }
                    } else {

                        if (!isOpenAzkarPhone) {
                            scheduleAzkarScreen(check);
                            isOpenAzkarPhone = true;
                        }
                    }
                }

            }

            switch (check) {
                case 1:


                    if (Pref.getValue(getApplicationContext(), Constants.PREF_FAJER_SOUND_IQAMAH, false)) {
                        if (Pref.getValue(getApplicationContext(), Constants.PREF_FAJER_SOUND_IQAMAH_PATH, null) != null) {
                            if (!mp.isPlaying()) {
                                mp = new MediaPlayer();
                                String uriSound = Pref.getValue(getApplicationContext(), Constants.PREF_FAJER_SOUND_IQAMAH_PATH, null);
                                if (!mp.isPlaying()) play(uriSound);

                            }
                        }
                    }
                    break;

                case 3:


                    if (calendar.get(Calendar.DAY_OF_WEEK) != Calendar.FRIDAY && check != 3) {
                        if (Pref.getValue(getApplicationContext(), Constants.PREF_DHOHR_SOUND_IQAMAH, false)) {
                            if (Pref.getValue(getApplicationContext(), Constants.PREF_DHOHR_SOUND_IQAMAH_PATH, null) != null) {
                                if (!mp.isPlaying()) {
                                    mp = new MediaPlayer();
                                    String uriSound = Pref.getValue(getApplicationContext(), Constants.PREF_DHOHR_SOUND_IQAMAH_PATH, null);
                                    if (!mp.isPlaying()) play(uriSound);
                                }
                            }
                        }
                    }
                    break;
                case 4:


                    if (Pref.getValue(getApplicationContext(), Constants.PREF_ASR_SOUND_IQAMAH, false)) {
                        if (Pref.getValue(getApplicationContext(), Constants.PREF_ASR_SOUND_IQAMAH_PATH, null) != null) {
                            if (!mp.isPlaying()) {
                                mp = new MediaPlayer();
                                String uriSound = Pref.getValue(getApplicationContext(), Constants.PREF_ASR_SOUND_IQAMAH_PATH, null);
                                if (!mp.isPlaying()) play(uriSound);
                            }
                        }
                    }
                    break;
                case 5:


                    if (Pref.getValue(getApplicationContext(), Constants.PREF_MAGHRIB_SOUND_IQAMAH, false)) {
                        if (Pref.getValue(getApplicationContext(), Constants.PREF_MAGHRIB_SOUND_IQAMAH_PATH, null) != null) {
                            if (!mp.isPlaying()) {
                                mp = new MediaPlayer();
                                String uriSound = Pref.getValue(getApplicationContext(), Constants.PREF_MAGHRIB_SOUND_IQAMAH_PATH, null);
                                if (!mp.isPlaying()) play(uriSound);
                            }
                        }
                    }
                    break;
                case 6:


                    if (Pref.getValue(getApplicationContext(), Constants.PREF_ISHA_SOUND_IQAMAH, false)) {
                        if (Pref.getValue(getApplicationContext(), Constants.PREF_ISHA_SOUND_IQAMAH_PATH, null) != null) {
                            if (!mp.isPlaying()) {
                                mp = new MediaPlayer();
                                String uriSound = Pref.getValue(getApplicationContext(), Constants.PREF_ISHA_SOUND_IQAMAH_PATH, null);
                                if (!mp.isPlaying()) play(uriSound);
                            }
                        }
                    }
                    break;
            }
        }
    }

    private boolean shouldShowAlkhushueScreen(int pray) {
        boolean check = false;
        switch (pray) {
            case 1:
                check = Pref.getValue(getApplicationContext(), Constants.PREF_FAJR_SHOW_ALKHUSHUE, true);
                break;
            case 2:
                check = Pref.getValue(getApplicationContext(), Constants.PREF_SUNRISE_SHOW_ALKHUSHUE, true);
                break;
            case 3:
                check = Pref.getValue(getApplicationContext(), Constants.PREF_DHOHR_SHOW_ALKHUSHUE, true);
                break;
            case 4:
                check = Pref.getValue(getApplicationContext(), Constants.PREF_ASR_SHOW_ALKHUSHUE, true);
                break;
            case 5:
                check = Pref.getValue(getApplicationContext(), Constants.PREF_MAGHRIB_SHOW_ALKHUSHUE, true);
                break;
            case 6:
                check = Pref.getValue(getApplicationContext(), Constants.PREF_ISHA_SHOW_ALKHUSHUE, true);
                break;
        }
        return check;
    }

    private void scheduleAzkarScreen(int pray) {
        int mint = 1;
        switch (pray) {
            case 1:
                mint = Pref.getValue(getApplicationContext(), Constants.PREF_FAJER_AZKAR_SHOW_IN, 10);
                break;
            case 2:
                mint = Pref.getValue(getApplicationContext(), Constants.PREF_SUNRISE_AZKAR_SHOW_IN, 10);
                break;
            case 3:
                mint = Pref.getValue(getApplicationContext(), Constants.PREF_DHOHR_AZKAR_SHOW_IN, 10);
                break;
            case 4:
                mint = Pref.getValue(getApplicationContext(), Constants.PREF_ASR_AZKAR_SHOW_IN, 10);
                break;
            case 5:
                mint = Pref.getValue(getApplicationContext(), Constants.PREF_MAGHRIB_AZKAR_SHOW_IN, 10);
                break;
            case 6:
                mint = Pref.getValue(getApplicationContext(), Constants.PREF_ISHA_AZKAR_SHOW_IN, 10);
                break;
        }
        if (mint != 0) {
            int after = mint * 1000 * 60;
            final Handler handler = new Handler(Looper.getMainLooper());
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {

                    Intent intent = new Intent(MainActivity.this, ShowAzkarActivity.class);
                    intent.putExtra("PRAY", pray);
                    startActivity(intent);
                }
            }, after);
        }

    }

    private void getFridayTime() {
        SimpleDateFormat format2 = new SimpleDateFormat("M/d", Locale.ENGLISH);
        SimpleDateFormat format1 = new SimpleDateFormat("yy", Locale.ENGLISH);
        Calendar calendar = Calendar.getInstance();

        if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY) {
            System.out.println("FRIDAY!");

        }


        int weekday = calendar.get(Calendar.DAY_OF_WEEK);
        int days = Calendar.FRIDAY - weekday;
        if (days < 0) {
            days += 7;
        }
        calendar.add(Calendar.DAY_OF_YEAR, days);
        String[] cityEn = getResources().getStringArray(R.array.city_name_en);
        int cityChose = Pref.getValue(getApplicationContext(), Constants.PREF_CITY_POSITION_SELECTED, 0);
        if (cityChose != 0) {
            friady = db.getPrayerTimesFriday(cityEn[cityChose], format2.format(calendar.getTime()) + "/" + getYears(Integer.parseInt(format1.format(calendar.getTime()))));
        } else {
            friady = db.getPrayerTimesFriday("masqat", format2.format(calendar.getTime()) + "/" + getYears(Integer.parseInt(format1.format(calendar.getTime()))));
        }
    }

    private void play(String path) {


        if (path.startsWith("/document/raw:")) {
            path = path.replaceFirst("/document/raw:", "");
        }

        try {
            mp.setDataSource(path);
            mp.prepareAsync();
            mp.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mp.start();
                }
            });
        } catch (IllegalArgumentException e) {

            e.printStackTrace();

        } catch (SecurityException e) {

            e.printStackTrace();

        } catch (IllegalStateException e) {

            e.printStackTrace();

        } catch (IOException e) {

            e.printStackTrace();

        }
    }

    private void checkAds() {
        SimpleDateFormat df = new SimpleDateFormat("HH:mm", Locale.ENGLISH);
        SimpleDateFormat dfDate = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        Date date = new Date();
        String currentTime = df.format(date);
        String currentDate = dfDate.format(date);
        int day = 0;
        if (Utils.isSaturday()) day = 1;
        if (Utils.isSunday()) day = 2;
        if (Utils.isMonday()) day = 3;
        if (Utils.isTuesday()) day = 4;
        if (Utils.isWednesday()) day = 5;
        if (Utils.isThursday()) day = 6;
        if (Utils.isFriday()) day = 7;

        db.openDataBase();
        ArrayList<Ads> adsList = db.getAdsByDate(-1, currentDate, currentTime, day);
        db.close();
        if (adsList.size() > 0) {
            for (int i = 0; i < adsList.size(); i++) {
                Ads ads = adsList.get(i);
                if (!isOpenAds) {
                    isOpenAds = true;
                    Intent intent = new Intent(MainActivity.this, ShowAdsActivity.class);
                    intent.putExtra("ads", ads);
                    intent.setAction("main");
                    startActivity(intent);
                }
            }
        }
    }

    private int getYears(int year) {
        int returnYear = 0;
        if (year == 18 || year == 22 || year == 26 || year == 30 || year == 34 || year == 38 || year == 42 || year == 46 || year == 50 || year == 54 || year == 58 || year == 62 || year == 66 || year == 70 || year == 74 || year == 78 || year == 82 || year == 86 || year == 90 || year == 94 || year == 98 || year == 2 || year == 6 || year == 10 || year == 14) {
            returnYear = 18;
        } else if (year == 19 || year == 23 || year == 27 || year == 31 || year == 35 || year == 39 || year == 43 || year == 47 || year == 51 || year == 55 || year == 59 || year == 63 || year == 67 || year == 71 || year == 75 || year == 79 || year == 83 || year == 87 || year == 91 || year == 95 || year == 99 || year == 3 || year == 7 || year == 11 || year == 15) {
            returnYear = 19;
        } else if (year == 20 || year == 24 || year == 28 || year == 32 || year == 36 || year == 40 || year == 44 || year == 48 || year == 52 || year == 56 || year == 60 || year == 64 || year == 68 || year == 72 || year == 76 || year == 80 || year == 84 || year == 88 || year == 92 || year == 96 || year == 4 || year == 8 || year == 12 || year == 16) {
            returnYear = 20;
        } else if (year == 21 || year == 25 || year == 29 || year == 33 || year == 37 || year == 41 || year == 45 || year == 49 || year == 53 || year == 57 || year == 61 || year == 65 || year == 69 || year == 73 || year == 77 || year == 81 || year == 85 || year == 89 || year == 93 || year == 97 || year == 1 || year == 5 || year == 9 || year == 13 || year == 17) {
            returnYear = 21;
        }
        return returnYear;
    }

    private boolean isBleFeatureAvailable() {
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(this, "Phone does not support BLE", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    private void initializeBluetoothListener() {

        mMtCentralManager.setMTCentralManagerListener(new MTCentralManagerListener() {
            @Override
            public void onScanedPeripheral(final List<MTPeripheral> peripherals) {


                for (MTPeripheral mtPeripheral : peripherals) {

                    MTFrameHandler mtFrameHandler = mtPeripheral.mMTFrameHandler;


                    int battery = mtFrameHandler.getBattery();


                    ArrayList<MinewFrame> advFrames = mtFrameHandler.getAdvFrames();

                    for (MinewFrame minewFrame : advFrames) {
                        FrameType frameType = minewFrame.getFrameType();
                        switch (frameType) {
                            case FrameiBeacon:
                                IBeaconFrame iBeaconFrame = (IBeaconFrame) minewFrame;

                                break;
                            case FrameUID:
                                UidFrame uidFrame = (UidFrame) minewFrame;

                                break;
                            case FrameAccSensor:
                                AccFrame accFrame = (AccFrame) minewFrame;

                                break;
                            case FrameHTSensor:
                                HTFrame htFrame = (HTFrame) minewFrame;


                                break;
                            case FrameTLM:
                                TlmFrame tlmFrame = (TlmFrame) minewFrame;


                                break;
                            case FrameURL:
                                UrlFrame urlFrame = (UrlFrame) minewFrame;

                                break;
                            case FrameLightSensor:
                                LightFrame lightFrame = (LightFrame) minewFrame;

                                break;
                            case FrameForceSensor:
                                ForceFrame forceFrame = ((ForceFrame) minewFrame);

                                break;
                            case FrameTempSensor:
                                TemperatureFrame temperatureFrame = (TemperatureFrame) minewFrame;

                                TEMPERATURE = temperatureFrame.getValue();
                                NumberFormat.getInstance(Locale.ENGLISH).format(TEMPERATURE);

                                tv_internal_heat.setText(String.format("%.1f", TEMPERATURE));
                                BATTERY = temperatureFrame.getBattery();
                                break;
                            case FrameTVOCSensor:
                                TvocFrame tvocFrame = (TvocFrame) minewFrame;

                                break;
                            default:
                                break;
                        }
                    }
                }

            }
        });


    }

    private final ConnectionStatueListener connectionStatueListener = new ConnectionStatueListener() {
        @Override
        public void onUpdateConnectionStatus(final ConnectionStatus connectionStatus, final GetPasswordListener getPasswordListener) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    switch (connectionStatus) {
                        case CONNECTING:

                            Toast.makeText(MainActivity.this, "CONNECTING", Toast.LENGTH_SHORT).show();
                            break;
                        case CONNECTED:

                            Toast.makeText(MainActivity.this, "CONNECTED", Toast.LENGTH_SHORT).show();
                            break;
                        case READINGINFO:

                            Toast.makeText(MainActivity.this, "READINGINFO", Toast.LENGTH_SHORT).show();
                            break;
                        case DEVICEVALIDATING:

                            Toast.makeText(MainActivity.this, "DEVICEVALIDATING", Toast.LENGTH_SHORT).show();
                            break;
                        case PASSWORDVALIDATING:

                            Toast.makeText(MainActivity.this, "PASSWORDVALIDATING", Toast.LENGTH_SHORT).show();
                            String password = "minew123";
                            getPasswordListener.getPassword(password);
                            break;
                        case SYNCHRONIZINGTIME:

                            Toast.makeText(MainActivity.this, "SYNCHRONIZINGTIME", Toast.LENGTH_SHORT).show();
                            break;
                        case READINGCONNECTABLE:

                            Toast.makeText(MainActivity.this, "READINGCONNECTABLE", Toast.LENGTH_SHORT).show();
                            break;
                        case READINGFEATURE:

                            Toast.makeText(MainActivity.this, "READINGFEATURE", Toast.LENGTH_SHORT).show();
                            break;
                        case READINGFRAMES:

                            Toast.makeText(MainActivity.this, "READINGFRAMES", Toast.LENGTH_SHORT).show();
                            break;
                        case READINGTRIGGERS:

                            Toast.makeText(MainActivity.this, "READINGTRIGGERS", Toast.LENGTH_SHORT).show();
                            break;
                        case COMPLETED:

                            Toast.makeText(MainActivity.this, "COMPLETED", Toast.LENGTH_SHORT).show();
                            break;
                        case CONNECTFAILED:
                        case DISCONNECTED:

                            Toast.makeText(MainActivity.this, "DISCONNECTED", Toast.LENGTH_SHORT).show();
                            break;
                    }

                }
            });
        }

        @Override
        public void onError(MTException e) {

        }
    };

    private void initializeBluetoothManager() {
        mMtCentralManager = MTCentralManager.getInstance(this);

        mMtCentralManager.startService();
        BluetoothState bluetoothState = mMtCentralManager.getBluetoothState(this);
        switch (bluetoothState) {
            case BluetoothStateNotSupported:

                break;
            case BluetoothStatePowerOff:

                break;
            case BluetoothStatePowerOn:

                break;
        }

        mMtCentralManager.setBluetoothChangedListener(new OnBluetoothStateChangedListener() {
            @Override
            public void onStateChanged(BluetoothState state) {
                switch (state) {
                    case BluetoothStateNotSupported:
                    case BluetoothStatePowerOff:
                    case BluetoothStatePowerOn:
                        break;
                }
            }
        });
    }

    private void requestLocationPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_COARSE_LOCATION);
        } else {
            initData();
        }
    }

    private void initData() {

        try {
            mMtCentralManager.startScan();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Phone does not have Bluetooth!!", Toast.LENGTH_LONG).show();
        }
    }

    private final BroadcastReceiver networkReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
                NetworkInfo networkInfo = intent.getParcelableExtra(ConnectivityManager.EXTRA_NETWORK_INFO);
                if (networkInfo != null && networkInfo.getDetailedState() == NetworkInfo.DetailedState.CONNECTED) {
                    FirebaseAuth mAuth = FirebaseAuth.getInstance();
                    FirebaseUser currentUser = mAuth.getCurrentUser();
                    currentUser.reload().addOnFailureListener(e -> {
                        if (e instanceof FirebaseAuthInvalidUserException) {
                            Log.d("LOG_TAG", "user doesn't exist anymore");

                            Pref.setValue(MainActivity.this, Constants.PREF_IS_USER_LOGIN, false);
                            Intent intent1 = new Intent(MainActivity.this, LoginActivity.class);
                            startActivity(intent1);
                            finish();
                        }
                    });
                } else if (networkInfo != null && networkInfo.getDetailedState() == NetworkInfo.DetailedState.DISCONNECTED) {
                }
            }
        }
    };
}