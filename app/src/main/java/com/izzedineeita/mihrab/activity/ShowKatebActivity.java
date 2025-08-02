package com.izzedineeita.mihrab.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;

// import com.camerakit.CameraKitView; // Removed CameraKit dependency
import com.izzedineeita.mihrab.R;
import com.izzedineeita.mihrab.constants.Constants;
import com.izzedineeita.mihrab.constants.DateHigri;
import com.izzedineeita.mihrab.utils.Pref;
import com.izzedineeita.mihrab.utils.Utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import static android.Manifest.permission.CAMERA;

public class ShowKatebActivity extends AppCompatActivity {

    private TextView tv_masged_name, tv_fajr, tv_sun, tv_thahr, tv_asr, tv_magrb, tv_isha,
            tv_day, tv_time, tv_time_sec, tv_data_hajre, tv_data_melade,
            tv_fajet_name, tv_sun_name, tv_thahr_name, tv_asr_name, tv_magrb_name, tv_isha_name;
    private LinearLayout linear_pray_background;

    private String dateM, dateH;
    private Activity activity;

    private Runnable adsRunnable;
    private Handler AdsHandler = new Handler();
    public Thread myThread = null;

    private static final String[] CAMERA_PERMISSION = new String[]{Manifest.permission.CAMERA};
    private static final int CAMERA_REQUEST_CODE = 10;
    // private CameraKitView cameraKitView; // Removed CameraKit dependency

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_kateb);

        boolean b = Pref.getValue(getApplicationContext(), Constants.PREF_CHECK_BOX_2, false);
        int theme = Pref.getValue(getApplicationContext(), Constants.PREF_THEM_POSITION_SELECTED, 0);
        switch (theme) {
            case 4:
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                if (b) {
                    setContentView(R.layout.activity_show_kateb_0_5);
                } else {
                    setContentView(R.layout.activity_show_kateb_5);
                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                            250);
                    linear_pray_background.setLayoutParams(lp);
                }


                break;
            case 5:
            case 6:
            case 8:
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
                setContentView(R.layout.activity_show_kateb_6);
                break;
            case 7:
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
                setContentView(R.layout.activity_show_kateb_8);
                break;
            default:
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                if (b) {
                    setContentView(R.layout.activity_show_kateb_0_1);
                } else {
                    setContentView(R.layout.activity_show_kateb);
                    LinearLayout.LayoutParams lp1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                            250);
                    linear_pray_background.setLayoutParams(lp1);
                }


                break;
        }

        activity = ShowKatebActivity.this;

        Runnable runnable = new ShowKatebActivity.CountDownRunner();
        myThread = new Thread(runnable);
        myThread.start();

        DateHigri hd = new DateHigri();
        switch (theme) {
            case 5:
                dateM = Utils.writeMDate1(activity);
                dateH = Utils.writeHDate1(activity);
                break;
            default:
                dateM = Utils.writeMDate(activity);
                dateH = Utils.writeHDate(activity);
                break;
        }

        init();
        setData();


        // cameraKitView = findViewById(R.id.camera); // Removed CameraKit dependency
        // if (hasCameraPermission()) {
        //     //enableCamera();
        // } else {
        //     requestPermission();
        // }

        closeActivity(Integer.parseInt(Pref.getValue(getApplicationContext(), Constants.PREF_SHOW_KATEB_TIME, "5")));
    }

    private void closeActivity(int mint) {

        int millis = mint * 60 * 1000;

        new CountDownTimer(millis, 1000) {
            public void onFinish() {
                finish();
            }

            public void onTick(long millisUntilFinished) {
            }
        }.start();
    }

    private void init() {
        tv_masged_name = findViewById(R.id.tv_masged_name);
        tv_fajr = findViewById(R.id.tv_fajr);
        tv_sun = findViewById(R.id.tv_sun);
        tv_thahr = findViewById(R.id.tv_thahr);
        tv_asr = findViewById(R.id.tv_asr);
        tv_magrb = findViewById(R.id.tv_magrb);
        tv_isha = findViewById(R.id.tv_isha);
        tv_day = findViewById(R.id.tv_day);
        tv_time = findViewById(R.id.tv_time);
        tv_time_sec = findViewById(R.id.tv_time_sec);
        tv_data_hajre = findViewById(R.id.tv_data_hajre);
        tv_data_melade = findViewById(R.id.tv_data_melade);
        tv_fajet_name = findViewById(R.id.tv_fajet_name);
        tv_sun_name = findViewById(R.id.tv_sun_name);
        tv_thahr_name = findViewById(R.id.tv_thahr_name);
        tv_asr_name = findViewById(R.id.tv_asr_name);
        tv_magrb_name = findViewById(R.id.tv_magrb_name);
        tv_isha_name = findViewById(R.id.tv_isha_name);
        linear_pray_background = findViewById(R.id.linear_pray_background);



        Calendar calendar = Calendar.getInstance();
        String[] days = new String[]{"الاحد", "الاثنين", "الثلاثاء", "الاربعاء", "الخميس", "الجمعة", "السبت"};
        String day = days[calendar.get(Calendar.DAY_OF_WEEK) - 1];
        tv_day.setText(day);

    }

    private void setData() {

        tv_masged_name.setText(Pref.getValue(getApplicationContext(), Constants.PREF_MASGED_NAME, "اسم المسجد"));

//        Calendar today1 = Calendar.getInstance();
//        SimpleDateFormat format = new SimpleDateFormat("M/d/yy", Locale.ENGLISH);

//        db = new DataBaseHelper(getApplicationContext());
//        String[] cityEn = getResources().getStringArray(R.array.city_name_en);
//        String[] d = null;
//        int cityChose = Pref.getValue(getApplicationContext(), Constants.PREF_CITY_POSITION_SELECTED, 0);
//
//        Log.e("XXX", "" + today1.getTime());
//        if (cityChose != 0) {
//            d = db.getPrayerTimes(cityEn[cityChose], format.format(today1.getTime()));
//        } else {
//            d = db.getPrayerTimes("masqat", format.format(today1.getTime()));
//        }
//
//        cfajr = d[1];
//        csunrise = d[2];
//        cdhohr = d[3];
//        casr = d[4];
//        cmaghrib = d[5];
//        cisha = d[6];
//
//        SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm", Locale.ENGLISH);
//        SimpleDateFormat dateFormat2 = new SimpleDateFormat("hh:mm", Locale.ENGLISH);
//        try {
//            Date date = dateFormat2.parse(cfajr);
//            cfajr = dateFormat.format(date);
//            Date date1 = dateFormat2.parse(csunrise);
//            csunrise = dateFormat.format(date1);
//            Date date2 = dateFormat2.parse(cdhohr);
//            cdhohr = dateFormat.format(date2);
//            Date date3 = dateFormat2.parse(casr);
//            casr = dateFormat.format(date3);
//            Date date4 = dateFormat2.parse(cmaghrib);
//            cmaghrib = dateFormat.format(date4);
//            Date date5 = dateFormat2.parse(cisha);
//            cisha = dateFormat.format(date5);
//            //Log.e("Time", cfajr);
//        } catch (ParseException e) {
//        }
//
//        if (cfajr.length() == 4) {
//            cfajr = "0" + cfajr;
//        }
//        if (csunrise.length() == 4) {
//            csunrise = "0" + csunrise;
//        }
//        if (cdhohr.length() == 4) {
//            cdhohr = "0" + cdhohr;
//        }
//        if (casr.length() == 4) {
//            casr = "0" + casr;
//        }
//        if (cmaghrib.length() == 4) {
//            cmaghrib = "0" + cmaghrib;
//        }
//        if (cisha.length() == 4) {
//            cisha = "0" + cisha;
//        }

        String f = Pref.getValue(activity, Constants.PREF_FAJR_TIME, "");
        String f1;
        if (f.endsWith("AM")) {
            f1 = f.replace("AM", "");
        } else {
            f1 = f.replace("PM", "");
        }
        f = f1;
        String s = Pref.getValue(activity, Constants.PREF_SUNRISE_TIME, "");
        String s1;
        if (s.endsWith("AM")) {
            s1 = s.replace("AM", "");
        } else {
            s1 = s.replace("PM", "");
        }
        s = s1;
        String d = Pref.getValue(activity, Constants.PREF_DHOHR_TIME, "");
        String d1;
        if (d.endsWith("AM")) {
            d1 = d.replace("AM", "");
        } else {
            d1 = d.replace("PM", "");
        }
        d = d1;
        String a = Pref.getValue(activity, Constants.PREF_ASR_TIME, "");
        String a1;
        if (a.endsWith("AM")) {
            a1 = a.replace("AM", "");
        } else {
            a1 = a.replace("PM", "");
        }
        a = a1;
        String m = Pref.getValue(activity, Constants.PREF_MAGHRIB_TIME, "");
        String m1;
        if (m.endsWith("AM")) {
            m1 = m.replace("AM", "");
        } else {
            m1 = m.replace("PM", "");
        }
        m = m1;
        String i = Pref.getValue(activity, Constants.PREF_ISHA_TIME, "");
        String i1;
        if (i.endsWith("AM")) {
            i1 = i.replace("AM", "");
        } else {
            i1 = i.replace("PM", "");
        }
        i = i1;

        tv_fajr.setText(f);
        tv_sun.setText(s);
        tv_thahr.setText(d);
        tv_asr.setText(a);
        tv_magrb.setText(m);
        tv_isha.setText(i);

        tv_data_hajre.setText(dateH);
        tv_data_melade.setText(dateM);
    }

    private void doWork() {
        runOnUiThread(new Runnable() {
            public void run() {
                try {
                    DateFormat timeNow = new SimpleDateFormat("hh:mm", Locale.ENGLISH);
                    DateFormat timeNow1 = new SimpleDateFormat("ss", Locale.ENGLISH);
                    Calendar c = Calendar.getInstance();
                    String timeText = timeNow.format(c.getTime());
                    String timeText1 = timeNow1.format(c.getTime());

                    tv_time.setText(timeText);
                    tv_time_sec.setText(timeText1);
                } catch (Exception e) {
                    Log.e("XXX", e.getLocalizedMessage());
                }
            }
        });
    }

    class CountDownRunner implements Runnable {
        // @Override
        public void run() {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    doWork();
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } catch (Exception e) {
                    Log.e("XXX", e.getLocalizedMessage());
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (myThread.isAlive()) {
            myThread.interrupt();
        }
        AdsHandler.removeCallbacks(adsRunnable);
    }

    @Override
    public void onBackPressed() {
        finish();
        if (myThread.isAlive()) {
            myThread.interrupt();
        }
        super.onBackPressed();
    }

    private boolean hasCameraPermission() {
        return ContextCompat.checkSelfPermission(
                this,
                CAMERA
        ) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(
                this,
                CAMERA_PERMISSION,
                CAMERA_REQUEST_CODE
        );
    }

    //    private void enableCamera() {
//        Intent intent = new Intent(this, MainActivity.class);
//        startActivity(intent);
//    }
    @Override
    protected void onStart() {
        super.onStart();
        // cameraKitView.onStart(); // Removed CameraKit dependency
    }

    @Override
    protected void onResume() {
        super.onResume();
        // cameraKitView.onResume(); // Removed CameraKit dependency
    }

    @Override
    protected void onPause() {
        // cameraKitView.onPause(); // Removed CameraKit dependency
        super.onPause();
    }

    @Override
    protected void onStop() {
        // cameraKitView.onStop(); // Removed CameraKit dependency
        super.onStop();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // cameraKitView.onRequestPermissionsResult(requestCode, permissions, grantResults); // Removed CameraKit dependency
    }

}