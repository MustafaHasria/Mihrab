package com.izzedineeita.mihrab.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.izzedineeita.mihrab.MainActivity;
import com.izzedineeita.mihrab.R;
import com.izzedineeita.mihrab.constants.Constants;
import com.izzedineeita.mihrab.constants.DateHigri;
import com.izzedineeita.mihrab.database.DataBaseHelper;
import com.izzedineeita.mihrab.utils.ImagesArrays;
import com.izzedineeita.mihrab.utils.Pref;
import com.izzedineeita.mihrab.utils.Utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ShowAzkarActivity extends AppCompatActivity {

    private TextView tv_masged_name, tv_fajr, tv_sun, tv_thahr, tv_asr, tv_magrb, tv_isha,
            tv_day, tv_time, tv_time_sec, tv_data_hajre, tv_data_melade,
            tv_fajet_name, tv_sun_name, tv_thahr_name, tv_asr_name, tv_magrb_name, tv_isha_name;
    private LinearLayout linear_pray_background;
    //    private DataBaseHelper db;
//    public String cfajr, csunrise, cdhohr, casr, cmaghrib, cisha;
//    public String cfajr1, csunrise1, cdhohr1, casr1, cmaghrib1, cisha1;
    private String dateM, dateH;
    private Activity activity;

    private Runnable adsRunnable;
    private Handler AdsHandler = new Handler();
    public Thread myThread = null;

//    TextView textView1;
//    TextView textView2;
//    TextView textView3;
//    TextView textView4;
//    TextView textView5;
//    TextView textView6;
//    TextView textView7;
//    TextView textView8;
//    TextView textView9;
//    TextView textView10;
//    TextView textView11;
//    TextView textView12;
//    TextView textView13;
//    TextView textView14;
//    TextView textView15;
//    TextView textView16;
//    TextView textView17;
//    TextView textView18;

    int theme = 5;
    LinearLayout.LayoutParams lp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // setContentView(R.layout.activity_show_azkar);
        theme = Pref.getValue(getApplicationContext(), Constants.PREF_THEM_POSITION_SELECTED, 0);
        boolean b = Pref.getValue(getApplicationContext(), Constants.PREF_CHECK_BOX_2, false);

        switch (theme) {
            case 4:
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                if (b) {
                    setContentView(R.layout.activity_show_azkar_0_5);
                } else {
                    setContentView(R.layout.activity_show_azkar_5);
                    linear_pray_background = findViewById(R.id.linear_pray_background);

                    lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                            250);
                    linear_pray_background.setLayoutParams(lp);
                }

                break;
            case 5:
            case 6:
            case 8:
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
                setContentView(R.layout.activity_show_azkar_6);
                break;
            case 7:
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
                setContentView(R.layout.activity_show_azkar_8);
                break;
            case 9:
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
                setContentView(R.layout.activity_show_azkar_10);
                break;
            default:
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                if (b) {
                    setContentView(R.layout.activity_show_azkar_0_1);
                } else {
                    setContentView(R.layout.activity_show_azkar);
                    linear_pray_background = findViewById(R.id.linear_pray_background);

                    lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                            250);
                    linear_pray_background.setLayoutParams(lp);
                }


                break;
        }


        activity = ShowAzkarActivity.this;

        int pray = 1;
//        if (savedInstanceState == null) {
//            Bundle extras = getIntent().getExtras();
//            pray = extras.getInt("PRAY");
//        } else {
//            pray = (int) savedInstanceState.getSerializable("PRAY");
//        }

        Runnable runnable = new ShowAzkarActivity.CountDownRunner();
        myThread = new Thread(runnable);
        myThread.start();

        DateHigri hd = new DateHigri();
        switch (theme) {
            case 5:
                dateM = Utils.writeMDate1(activity, hd);
                dateH = Utils.writeHDate1(activity, hd);
                break;
            default:
                dateM = Utils.writeMDate(activity, hd);
                dateH = Utils.writeHDate(activity, hd);
                break;
        }

        init();
        setData();

    //    setTextSize();

        checkPray(pray);
    }

    private void checkPray(int pray) {
        switch (pray) {
            case 1:
                closeActivity(Pref.getValue(getApplicationContext(), Constants.PREF_FAJR_SHOW_AZKAR_TIME, 10));
                Log.e("XXX1", "startActivity " + Pref.getValue(getApplicationContext(), Constants.PREF_FAJR_SHOW_AZKAR_TIME, 10));
                break;
            case 2:
                closeActivity(Pref.getValue(getApplicationContext(), Constants.PREF_SUNRISE_SHOW_AZKAR_TIME, 10));
                break;
            case 3:
                closeActivity(Pref.getValue(getApplicationContext(), Constants.PREF_DHOHR_SHOW_AZKAR_TIME, 10));
                break;
            case 4:
                closeActivity(Pref.getValue(getApplicationContext(), Constants.PREF_ASR_SHOW_AZKAR_TIME, 10));
                break;
            case 5:
                closeActivity(Pref.getValue(getApplicationContext(), Constants.PREF_MAGHRIB_SHOW_AZKAR_TIME, 10));
                break;
            case 6:
                closeActivity(Pref.getValue(getApplicationContext(), Constants.PREF_ISHA_SHOW_AZKAR_TIME, 10));
                break;
        }
    }

    private void closeActivity(int mint) {

        int millis = mint * 60 * 1000;

        new CountDownTimer(millis, 1000) {
            public void onFinish() {
                MainActivity.isOpenAzkarPhone = false;
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


        Calendar calendar = Calendar.getInstance();
        String[] days = new String[]{"الاحد", "الاثنين", "الثلاثاء", "الاربعاء", "الخميس", "الجمعة", "السبت"};
        String day = days[calendar.get(Calendar.DAY_OF_WEEK) - 1];
        tv_day.setText(day);

    }

//    private void setTextSize() {
//        tv_day.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                showDialog(tv_day, null);
//            }
//        });
//        tv_time.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                showDialog(tv_time, null);
//            }
//        });
//        tv_time_sec.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                showDialog(tv_time_sec, null);
//            }
//        });
//        tv_fajr.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                showDialog(tv_fajr, null);
//            }
//        });
//        tv_fajet_name.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                showDialog(tv_fajet_name, null);
//            }
//        });
//        linear_pray_background.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                showDialog(null, linear_pray_background);
//            }
//        });
//        tv_data_hajre.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                showDialog(tv_data_hajre, null);
//            }
//        });
//        tv_data_melade.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                showDialog(tv_data_melade, null);
//            }
//        });
//        tv_data_melade.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                showDialog(tv_data_melade, null);
//            }
//        });
//        tv_masged_name.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                showDialog(tv_masged_name, null);
//            }
//        });
//
////        textView1 = findViewById(R.id.tv_1);
////        textView2 = findViewById(R.id.tv_2);
////        textView3 = findViewById(R.id.tv_3);
////        textView4 = findViewById(R.id.tv_4);
////        textView5 = findViewById(R.id.tv_5);
////        textView6 = findViewById(R.id.tv_6);
////        textView7 = findViewById(R.id.tv_7);
////        textView8 = findViewById(R.id.tv_8);
////        textView9 = findViewById(R.id.tv_9);
////        textView10 = findViewById(R.id.tv_10);
////        textView11 = findViewById(R.id.tv_11);
////        textView12 = findViewById(R.id.tv_12);
////        textView13 = findViewById(R.id.tv_13);
////        textView14 = findViewById(R.id.tv_14);
////        textView15 = findViewById(R.id.tv_15);
////        textView16 = findViewById(R.id.tv_16);
////        textView17 = findViewById(R.id.tv_17);
////        textView18 = findViewById(R.id.tv_18);
////
////        textView1.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View v) {
////                showDialog(null, null);
////            }
////        });
//    }

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
        tv_data_melade.setText(dateM +" - ");
    }

    private void showDialog(TextView textView, LinearLayout linearLayout1) {
        RelativeLayout linearLayout = new RelativeLayout(activity);
        final NumberPicker aNumberPicker = new NumberPicker(activity);
        aNumberPicker.setMaxValue(800);
        aNumberPicker.setMinValue(20);

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(50, 50);
        RelativeLayout.LayoutParams numPicerParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        numPicerParams.addRule(RelativeLayout.CENTER_HORIZONTAL);

        linearLayout.setLayoutParams(params);
        linearLayout.addView(aNumberPicker, numPicerParams);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity);
        alertDialogBuilder.setTitle("Select the number");
        alertDialogBuilder.setView(linearLayout);
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("Ok",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int id) {
                                if (linearLayout1 == null && textView != null) {
                                    if (textView.getId() == R.id.tv_fajet_name) {
                                        tv_fajet_name.setTextSize(aNumberPicker.getValue());
                                        tv_sun_name.setTextSize(aNumberPicker.getValue());
                                        tv_thahr_name.setTextSize(aNumberPicker.getValue());
                                        tv_asr_name.setTextSize(aNumberPicker.getValue());
                                        tv_magrb_name.setTextSize(aNumberPicker.getValue());
                                        tv_isha_name.setTextSize(aNumberPicker.getValue());
                                    } else if (textView.getId() == R.id.tv_fajr) {
                                        tv_fajr.setTextSize(aNumberPicker.getValue());
                                        tv_sun.setTextSize(aNumberPicker.getValue());
                                        tv_thahr.setTextSize(aNumberPicker.getValue());
                                        tv_asr.setTextSize(aNumberPicker.getValue());
                                        tv_magrb.setTextSize(aNumberPicker.getValue());
                                        tv_isha.setTextSize(aNumberPicker.getValue());
                                    } else {
                                        textView.setTextSize(aNumberPicker.getValue());
                                    }

                                } else if (textView == null && linearLayout1 != null) {
                                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                                            aNumberPicker.getValue());
                                    linearLayout1.setLayoutParams(lp);
                                }

//                                if (textView == null && linearLayout1 == null) {
//                                    textView1.setTextSize(aNumberPicker.getValue());
//                                    textView2.setTextSize(aNumberPicker.getValue());
//                                    textView3.setTextSize(aNumberPicker.getValue());
//                                    textView4.setTextSize(aNumberPicker.getValue());
//                                    textView5.setTextSize(aNumberPicker.getValue());
//                                    textView6.setTextSize(aNumberPicker.getValue());
//                                    textView7.setTextSize(aNumberPicker.getValue());
//                                    textView8.setTextSize(aNumberPicker.getValue());
//                                    textView9.setTextSize(aNumberPicker.getValue());
//                                    textView10.setTextSize(aNumberPicker.getValue());
//                                    textView11.setTextSize(aNumberPicker.getValue());
//                                    textView12.setTextSize(aNumberPicker.getValue());
//                                    textView13.setTextSize(aNumberPicker.getValue());
//                                    textView14.setTextSize(aNumberPicker.getValue());
//                                    textView15.setTextSize(aNumberPicker.getValue());
//                                    textView16.setTextSize(aNumberPicker.getValue());
//                                    textView17.setTextSize(aNumberPicker.getValue());
//                                    textView18.setTextSize(aNumberPicker.getValue());
//                                }

                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int id) {
                                dialog.cancel();
                            }
                        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
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

}