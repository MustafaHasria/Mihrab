package com.izzedineeita.mihrab.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.AssetFileDescriptor;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.izzedineeita.mihrab.MainActivity;
import com.izzedineeita.mihrab.R;
import com.izzedineeita.mihrab.constants.Constants;
import com.izzedineeita.mihrab.constants.DateHigri;
import com.izzedineeita.mihrab.utils.ImagesArrays;
import com.izzedineeita.mihrab.utils.Pref;
import com.izzedineeita.mihrab.utils.Utils;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class ShowAlkhushueActivity extends AppCompatActivity {

    private ImageView img_time_hour_1, img_time_hour_2, img_time_mint_1, img_time_sec_2,
            img_time_sec_1, img_date_day, img_date_month_m_2, img_date_month_m_1,
            img_date_month_m, img_date_years_4, img_date_years_3, img_date_years_2,
            img_date_years_1, img_date_month_h_2, img_date_month_h_1, img_date_month_h,
            img_date_years_h_4, img_date_years_h_3, img_date_years_h_2, img_date_years_h_1,
            img_time_mint_2, img_phone_photo, img_internal_heat;
    public Thread myThread = null;
    private String[] date;
    private Activity activity;
    private TextView tv_external_heat, tv_internal_heat, tv_1, tv_masged_name;
    //private ImageView img_external_heat, img_internal_heat;
    int pray;
    int sec;
    public static int[] daysImage;
    public static int[] monthImage;
    public static int[] monthImageHijri;
    public static int[] dateNumber;
    public static int[] timeNumber;
    public static int[] timeNumberIqamhLeft;
    public static int[] timeNumberAzan;
    public static int[] timeNumberIqamh;
    public static int[] timeNumberSec;
    public int theme = 0;
    RelativeLayout relativeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState == null) {
          //  RelativeLayout relativeLayout;

            theme = Pref.getValue(getApplicationContext(), Constants.PREF_THEM_POSITION_SELECTED, 0);
           // int theme = 7;
            switch (theme) {
                case 1:
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                    setContentView(R.layout.activity_show_alkhushue);
                    Resources res = getResources();

                    relativeLayout = findViewById(R.id.relativeLayout);

                    Drawable drawable1 = res.getDrawable(R.drawable.background_main_them_1_0);
                    relativeLayout.setBackground(drawable1);

                    daysImage = ImagesArrays.daysImageTheme1;
                    monthImage = ImagesArrays.monthImageTheme1;
                    monthImageHijri = ImagesArrays.monthImageHijriTheme1;
                    dateNumber = ImagesArrays.dateNumberTheme1;
                    timeNumber = ImagesArrays.timeNumberTheme1;
                    timeNumberAzan = ImagesArrays.timeNumberAzanTheme1;
                    timeNumberIqamh = ImagesArrays.timeNumberIqamhTheme1;
                    timeNumberIqamhLeft = ImagesArrays.timeNumberTheme1;
                    timeNumberSec = ImagesArrays.timeNumberTheme1;
                    break;
                case 2:
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

                    setContentView(R.layout.activity_show_alkhushue);
                    Resources res1 = getResources();
                    relativeLayout = findViewById(R.id.relativeLayout);
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
                    timeNumberSec = ImagesArrays.timeNumberTheme1;
                    break;
                case 3:
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

                    setContentView(R.layout.activity_show_alkhushue);
                    Resources res3 = getResources();
                    relativeLayout = findViewById(R.id.relativeLayout);
                    Drawable drawable3 = res3.getDrawable(R.drawable.ic_background_thme_4_v);
                    relativeLayout.setBackground(drawable3);

                    ImageView img_sassel = findViewById(R.id.img_sassel);
                    ImageView img_fasl = findViewById(R.id.img_fasl);
                    //  Drawable drawable4 = res3.getDrawable(R.drawable.ic_number_thme_4_v_time_fasel);
                    img_sassel.setImageResource(R.drawable.ic_number_thme_4_v_time_fasel);
                    img_fasl.setImageResource(R.drawable.background_fasel_4);

                    daysImage = ImagesArrays.daysImageTheme4;
                    monthImage = ImagesArrays.monthImageTheme4;
                    monthImageHijri = ImagesArrays.monthImageHijriTheme4;
                    dateNumber = ImagesArrays.timeNumberTheme4;
                    timeNumber = ImagesArrays.timeNumberTheme4;
                    timeNumberAzan = ImagesArrays.timeNumberIqamhTheme4;
                    timeNumberIqamh = ImagesArrays.timeNumberIqamhTheme4;
                    timeNumberIqamhLeft = ImagesArrays.timeNumberIqamhLeft4;
                    timeNumberSec = ImagesArrays.timeNumberIqamhTheme4;
                    break;
                case 4:
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

                    setContentView(R.layout.activity_show_alkhushue_5);
                    Resources res4 = getResources();
                    relativeLayout = findViewById(R.id.relativeLayout);
                    Drawable drawable4 = res4.getDrawable(R.drawable.ic_background_thme_5_v);
                    relativeLayout.setBackground(drawable4);

                    RelativeLayout relativeLayout_azan = findViewById(R.id.relativeLayout_azan);
                    relativeLayout_azan.setBackground(ContextCompat.getDrawable(this, R.drawable.img_relative_layout_azan_v_5));

                    ImageView img_sassel4 = findViewById(R.id.img_sassel);
                    ImageView img_fasl4 = findViewById(R.id.img_fasl);
                    //  Drawable drawable4 = res3.getDrawable(R.drawable.ic_number_thme_4_v_time_fasel);
                    img_sassel4.setImageResource(R.drawable.ic_number_thme_5_v_time_fasel);
                    img_fasl4.setImageResource(R.drawable.background_fasel_5);

                    daysImage = ImagesArrays.daysImageTheme4;
                    monthImage = ImagesArrays.monthImageTheme5;
                    monthImageHijri = ImagesArrays.monthImageHijriTheme5;
                    dateNumber = ImagesArrays.timeNumberTheme5;
                    timeNumber = ImagesArrays.timeNumberTheme5;
                    timeNumberAzan = ImagesArrays.timeNumberIqamhTheme4;
                    timeNumberIqamh = ImagesArrays.timeNumberIqamhTheme4;
                    timeNumberIqamhLeft = ImagesArrays.timeNumberIqamhLeft4;
                    timeNumberSec = ImagesArrays.timeNumberIqamhTheme4;

                    break;

                case 5:
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);

                    setContentView(R.layout.activity_show_alkhushue_6);



                    daysImage = ImagesArrays.daysImageTheme6;
                    monthImage = ImagesArrays.monthImageTheme6;
                    monthImageHijri = ImagesArrays.monthImageHijriTheme6;
                    dateNumber = ImagesArrays.timeNumberIqamhLeft4;
                    timeNumber = ImagesArrays.timeNumberTheme6;
                    timeNumberAzan = ImagesArrays.timeNumberAzanTheme6;
                    timeNumberIqamh = ImagesArrays.timeNumberIqama6;
                    timeNumberIqamhLeft = ImagesArrays.timeNumberTheme6;
                    timeNumberSec = ImagesArrays.timeNumberTheme6;
                    break;
                case 6:
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);

                    setContentView(R.layout.activity_show_alkhushue_7);


                    daysImage = ImagesArrays.daysImageTheme1;
                    monthImage = ImagesArrays.monthImageTheme1;
                    monthImageHijri = ImagesArrays.monthImageHijriTheme1;
                    dateNumber = ImagesArrays.dateNumberTheme1;
                    timeNumber = ImagesArrays.timeNumberIqamhLeft7;
                    timeNumberAzan = ImagesArrays.timeNumberAzanTheme6;
                    timeNumberIqamh = ImagesArrays.timeNumberIqama6;
                    timeNumberIqamhLeft = ImagesArrays.timeNumberIqamhLeft7;
                    timeNumberSec = ImagesArrays.timeNumberIqamhLeft7;
                    break;
                case 7:
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
                    setContentView(R.layout.activity_show_alkhushue_8);

                    relativeLayout = findViewById(R.id.relativeLayout1);

                    daysImage = ImagesArrays.daysImageTheme8;
                    monthImage = ImagesArrays.monthImageTheme8;
                    monthImageHijri = ImagesArrays.monthImageHijriTheme8;
                    dateNumber = ImagesArrays.timeNumberDate8;
                    timeNumber = ImagesArrays.timeNumberTime8;
                    timeNumberAzan = ImagesArrays.timeNumberAzIq8;
                    timeNumberIqamh = ImagesArrays.timeNumberAzIq8;
                    timeNumberIqamhLeft = ImagesArrays.timeNumberTime8;
                    timeNumberSec = ImagesArrays.timeNumberTime8;
                    break;
                case 8:
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
                    setContentView(R.layout.activity_show_alkhushue_9);

                    daysImage = ImagesArrays.daysImageTheme1;
                    monthImage = ImagesArrays.monthImageTheme1;
                    monthImageHijri = ImagesArrays.monthImageHijriTheme1;
                    dateNumber = ImagesArrays.dateNumberTheme1;
                    timeNumber = ImagesArrays.timeNumberTheme6;
                    timeNumberAzan = ImagesArrays.timeNumberAzanTheme6;
                    timeNumberIqamh = ImagesArrays.timeNumberIqama6;
                    timeNumberIqamhLeft = ImagesArrays.timeNumberTheme6;
                    timeNumberSec = ImagesArrays.timeNumberTheme6;
                    break;
                case 9:
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);

                    setContentView(R.layout.activity_show_alkhushue_10);


                    daysImage = ImagesArrays.daysImageTheme10;
                    monthImage = ImagesArrays.monthImageTheme10;
                    monthImageHijri = ImagesArrays.monthImageHijriTheme10;
                    dateNumber = ImagesArrays.timeNumberDate10;
                    timeNumber = ImagesArrays.timeNumberDate10;
                    timeNumberAzan = ImagesArrays.timeNumberTheme6;
                    timeNumberIqamh = ImagesArrays.timeNumberTheme6;
                    timeNumberIqamhLeft = ImagesArrays.timeNumberIqamhLeft4;
                    timeNumberSec = ImagesArrays.timeNumberDate10;
                    break;
                default:
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

                    setContentView(R.layout.activity_show_alkhushue);
                    Resources res0 = getResources();
                    relativeLayout = findViewById(R.id.relativeLayout);
                    Drawable drawable0 = res0.getDrawable(R.drawable.ic_background_close_phone);
                    relativeLayout.setBackground(drawable0);
                    daysImage = ImagesArrays.daysImageTheme1;
                    monthImage = ImagesArrays.monthImageTheme1;
                    monthImageHijri = ImagesArrays.monthImageHijriTheme1;
                    dateNumber = ImagesArrays.dateNumberTheme1;
                    timeNumber = ImagesArrays.timeNumberTheme1;
                    timeNumberAzan = ImagesArrays.timeNumberAzanTheme1;
                    timeNumberIqamh = ImagesArrays.timeNumberIqamhTheme1;
                    timeNumberIqamhLeft = ImagesArrays.timeNumberTheme1;
                    timeNumberSec = ImagesArrays.timeNumberTheme1;
                    break;
            }

            activity = ShowAlkhushueActivity.this;

            DateHigri hd = new DateHigri();
            date = Utils.writeIslamicDate(activity, hd);

            init();

            if (savedInstanceState == null) {
                Bundle extras = getIntent().getExtras();
                pray = extras.getInt("PRAY");
            } else {
                pray = (int) savedInstanceState.getSerializable("PRAY");
            }
            try {
                Log.e("PRAY3", "true !!!!" + pray);
            } catch (Exception e) {
                Log.e("PRAY3", "Exception" + e.getLocalizedMessage());
            }
           // pray = 5;
            checkPray(pray);
            Runnable runnable = new CountDownRunner();
            myThread = new Thread(runnable);
            myThread.start();
            setDataToImage();


        }

    }

    private void closeActivity(int mint) {

        int millis = mint * 60 * 1000;

        new CountDownTimer(millis, 1000) {
            public void onFinish() {
                switch (pray) {
                    case 1:
                        if (Pref.getValue(activity, Constants.PREF_FAJR_SHOW_ALKHUSHUE, true)) {
                            MainActivity.isOpenAlkhushuePhone = false;
                            Intent intent = new Intent(activity, ShowAzkarActivity.class);
                            intent.putExtra("PRAY", pray);
                            startActivity(intent);
                        }
                        break;
                    case 3:
                        if (Pref.getValue(activity, Constants.PREF_DHOHR_SHOW_ALKHUSHUE, true)) {
                            MainActivity.isOpenAlkhushuePhone = false;
                            Intent intent = new Intent(activity, ShowAzkarActivity.class);
                            intent.putExtra("PRAY", pray);
                            startActivity(intent);
                        }
                        break;
                    case 4:
                        if (Pref.getValue(activity, Constants.PREF_ASR_SHOW_ALKHUSHUE, true)) {
                            MainActivity.isOpenAlkhushuePhone = false;
                            Intent intent = new Intent(activity, ShowAzkarActivity.class);
                            intent.putExtra("PRAY", pray);
                            startActivity(intent);
                        }
                        break;
                    case 5:
                        if (Pref.getValue(activity, Constants.PREF_MAGHRIB_SHOW_ALKHUSHUE, true)) {
                            MainActivity.isOpenAlkhushuePhone = false;
                            Intent intent = new Intent(activity, ShowAzkarActivity.class);
                            intent.putExtra("PRAY", pray);
                            startActivity(intent);
                        }
                        break;
                    case 6:
                        if (Pref.getValue(activity, Constants.PREF_ISHA_SHOW_ALKHUSHUE, true)) {
                            MainActivity.isOpenAlkhushuePhone = false;
                            Intent intent = new Intent(activity, ShowAzkarActivity.class);
                            intent.putExtra("PRAY", pray);
                            startActivity(intent);
                        }
                        break;
                }
                finish();

            }

            public void onTick(long millisUntilFinished) {
            }
        }.start();
    }

    private void init() {

        /* init date images */
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

        /* -------------------------------------------------------- */

        LinearLayout linear_date1 = findViewById(R.id.linear_date1);
        LinearLayout linear_date2 = findViewById(R.id.linear_date2);

        tv_masged_name = findViewById(R.id.tv_masged_name);
        tv_masged_name.setText(Pref.getValue(getApplicationContext(), Constants.PREF_MASGED_NAME, "اسم المسجد"));


        img_phone_photo = findViewById(R.id.img_phone_photo);
        tv_1 = findViewById(R.id.tv_1);

    }

    private void setDataToImage() {
        /* set images date */

        int  hijriDiff1 = Pref.getValue(ShowAlkhushueActivity.this, Constants.PREF_HEJRY_INT1, 0);
        int iii  = Integer.parseInt(date[4]);
        date[4] = String.valueOf(iii + hijriDiff1);

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

    private void doWork() {
        runOnUiThread(new Runnable() {
            public void run() {
                try {
                    DateFormat timeNow = new SimpleDateFormat("hh:mmass", Locale.ENGLISH);
                    Calendar c = Calendar.getInstance();
                    String timeText = timeNow.format(c.getTime());
                    img_time_hour_1.setImageResource(timeNumber[Integer.parseInt(String.valueOf(timeText.charAt(0)))]);
                    img_time_hour_2.setImageResource(timeNumber[Integer.parseInt(String.valueOf(timeText.charAt(1)))]);
                    img_time_mint_1.setImageResource(timeNumber[Integer.parseInt(String.valueOf(timeText.charAt(3)))]);
                    img_time_mint_2.setImageResource(timeNumber[Integer.parseInt(String.valueOf(timeText.charAt(4)))]);
                    img_time_sec_1.setImageResource(timeNumberSec[Integer.parseInt(String.valueOf(timeText.charAt(7)))]);
                    img_time_sec_2.setImageResource(timeNumberSec[Integer.parseInt(String.valueOf(timeText.charAt(8)))]);
                } catch (Exception e) {
                    Log.e("XXX", e.getLocalizedMessage());
                }
            }
        });
    }

    private void checkPray(int pray) {
        Log.e("XXX", "" + theme);
        switch (theme) {
            case 4:
                tv_1.setTextColor(Color.parseColor("#88110C"));
                switch (pray) {
                    case 1:
                        img_phone_photo.setImageResource(R.drawable.img_azan_1_v_5);
                        tv_1.setText(Pref.getValue(activity, Constants.PREF_FAJR_TEXT_ALKHUSHUE, ""));
                        closeActivity(Pref.getValue(getApplicationContext(), Constants.PREF_FAJR_SHOW_ALKHUSHUE_TIME, 7));
                        break;
                    case 2:
                        img_phone_photo.setImageResource(R.drawable.icon_shroq);
                        tv_1.setText(Pref.getValue(activity, Constants.PREF_SUNRISE_TEXT_ALKHUSHUE, ""));
                        closeActivity(Pref.getValue(getApplicationContext(), Constants.PREF_SUNRISE_SHOW_AZKAR_TIME, 7));
                        break;
                    case 3:
                        img_phone_photo.setImageResource(R.drawable.img_azan_2_v_5);
                        tv_1.setText(Pref.getValue(activity, Constants.PREF_DHOHR_TEXT_ALKHUSHUE, ""));
                        closeActivity(Pref.getValue(getApplicationContext(), Constants.PREF_DHOHR_SHOW_ALKHUSHUE_TIME, 7));
                        break;
                    case 4:
                        img_phone_photo.setImageResource(R.drawable.img_azan_3_v_5);
                        tv_1.setText(Pref.getValue(activity, Constants.PREF_ASR_TEXT_ALKHUSHUE, ""));
                        closeActivity(Pref.getValue(getApplicationContext(), Constants.PREF_ASR_SHOW_ALKHUSHUE_TIME, 7));
                        break;
                    case 5:
                        img_phone_photo.setImageResource(R.drawable.img_azan_4_v_5);
                        tv_1.setText(Pref.getValue(activity, Constants.PREF_MAGHRIB_TEXT_ALKHUSHUE, ""));
                        closeActivity(Pref.getValue(getApplicationContext(), Constants.PREF_MAGHRIB_SHOW_ALKHUSHUE_TIME, 7));
                        break;
                    case 6:
                        img_phone_photo.setImageResource(R.drawable.img_azan_5_v_5);
                        tv_1.setText(Pref.getValue(activity, Constants.PREF_ISHA_TEXT_ALKHUSHUE, ""));
                        closeActivity(Pref.getValue(getApplicationContext(), Constants.PREF_ISHA_SHOW_ALKHUSHUE_TIME, 7));
                        break;
                }
                break;
            case 7:

                switch (pray) {
                    case 1:
                        img_phone_photo.setImageResource(R.drawable.img_azan_1_8);
                        if (Pref.getValue(activity, Constants.PREF_FAJR_TEXT_ALKHUSHUE, "").equals("")) {
                            relativeLayout.setVisibility(View.GONE);
                        }
                        tv_1.setText(Pref.getValue(activity, Constants.PREF_FAJR_TEXT_ALKHUSHUE, ""));
                        closeActivity(Pref.getValue(getApplicationContext(), Constants.PREF_FAJR_SHOW_ALKHUSHUE_TIME, 7));
                        break;
                    case 2:
                        img_phone_photo.setImageResource(R.drawable.icon_shroq);
                        tv_1.setText(Pref.getValue(activity, Constants.PREF_SUNRISE_TEXT_ALKHUSHUE, ""));
                        closeActivity(Pref.getValue(getApplicationContext(), Constants.PREF_SUNRISE_SHOW_AZKAR_TIME, 7));
                        if (Pref.getValue(activity, Constants.PREF_SUNRISE_TEXT_ALKHUSHUE, "").equals("")) {
                            relativeLayout.setVisibility(View.GONE);
                        }
                        break;
                    case 3:
                        img_phone_photo.setImageResource(R.drawable.img_azan_2_8);
                        tv_1.setText(Pref.getValue(activity, Constants.PREF_DHOHR_TEXT_ALKHUSHUE, ""));
                        closeActivity(Pref.getValue(getApplicationContext(), Constants.PREF_DHOHR_SHOW_ALKHUSHUE_TIME, 7));
                        if (Pref.getValue(activity, Constants.PREF_DHOHR_TEXT_ALKHUSHUE, "").equals("")) {
                            relativeLayout.setVisibility(View.GONE);
                        }
                        break;
                    case 4:
                        img_phone_photo.setImageResource(R.drawable.img_azan_3_8);
                        tv_1.setText(Pref.getValue(activity, Constants.PREF_ASR_TEXT_ALKHUSHUE, ""));
                        closeActivity(Pref.getValue(getApplicationContext(), Constants.PREF_ASR_SHOW_ALKHUSHUE_TIME, 7));
                        if (Pref.getValue(activity, Constants.PREF_ASR_TEXT_ALKHUSHUE, "").equals("")) {
                            relativeLayout.setVisibility(View.GONE);
                        }
                        break;
                    case 5:
                        img_phone_photo.setImageResource(R.drawable.img_azan_4_8);
                        tv_1.setText(Pref.getValue(activity, Constants.PREF_MAGHRIB_TEXT_ALKHUSHUE, ""));
                        closeActivity(Pref.getValue(getApplicationContext(), Constants.PREF_MAGHRIB_SHOW_ALKHUSHUE_TIME, 7));
                        if (Pref.getValue(activity, Constants.PREF_MAGHRIB_TEXT_ALKHUSHUE, "").equals("")) {
                            relativeLayout.setVisibility(View.GONE);
                        }
                        break;
                    case 6:
                        img_phone_photo.setImageResource(R.drawable.img_azan_5_8);
                        tv_1.setText(Pref.getValue(activity, Constants.PREF_ISHA_TEXT_ALKHUSHUE, ""));
                        closeActivity(Pref.getValue(getApplicationContext(), Constants.PREF_ISHA_SHOW_ALKHUSHUE_TIME, 7));
                        if (Pref.getValue(activity, Constants.PREF_ISHA_TEXT_ALKHUSHUE, "").equals("")) {
                            relativeLayout.setVisibility(View.GONE);
                        }
                        break;
                }
                break;
            case 9:
                switch (pray) {
                    case 1:
                        img_phone_photo.setImageResource(R.drawable.img_azan_1_8);
                        tv_1.setText(Pref.getValue(activity, Constants.PREF_FAJR_TEXT_ALKHUSHUE, ""));
                        closeActivity(Pref.getValue(getApplicationContext(), Constants.PREF_FAJR_SHOW_ALKHUSHUE_TIME, 7));
                        break;
                    case 2:
                        img_phone_photo.setImageResource(R.drawable.icon_shroq);
                        tv_1.setText(Pref.getValue(activity, Constants.PREF_SUNRISE_TEXT_ALKHUSHUE, ""));
                        closeActivity(Pref.getValue(getApplicationContext(), Constants.PREF_SUNRISE_SHOW_AZKAR_TIME, 7));
                        break;
                    case 3:
                        img_phone_photo.setImageResource(R.drawable.img_azan_2_8);
                        tv_1.setText(Pref.getValue(activity, Constants.PREF_DHOHR_TEXT_ALKHUSHUE, ""));
                        closeActivity(Pref.getValue(getApplicationContext(), Constants.PREF_DHOHR_SHOW_ALKHUSHUE_TIME, 7));
                        break;
                    case 4:
                        img_phone_photo.setImageResource(R.drawable.img_azan_3_8);
                        tv_1.setText(Pref.getValue(activity, Constants.PREF_ASR_TEXT_ALKHUSHUE, ""));
                        closeActivity(Pref.getValue(getApplicationContext(), Constants.PREF_ASR_SHOW_ALKHUSHUE_TIME, 7));
                        break;
                    case 5:
                        img_phone_photo.setImageResource(R.drawable.img_azan_4_8);
                        tv_1.setText(Pref.getValue(activity, Constants.PREF_MAGHRIB_TEXT_ALKHUSHUE, ""));
                        closeActivity(Pref.getValue(getApplicationContext(), Constants.PREF_MAGHRIB_SHOW_ALKHUSHUE_TIME, 7));
                        break;
                    case 6:
                        img_phone_photo.setImageResource(R.drawable.img_azan_5_8);
                        tv_1.setText(Pref.getValue(activity, Constants.PREF_ISHA_TEXT_ALKHUSHUE, ""));
                        closeActivity(Pref.getValue(getApplicationContext(), Constants.PREF_ISHA_SHOW_ALKHUSHUE_TIME, 7));
                        break;
                }
                break;
            default:
                switch (pray) {
                    case 1:
                        img_phone_photo.setImageResource(R.drawable.img_azan_1);
                        tv_1.setText(Pref.getValue(activity, Constants.PREF_FAJR_TEXT_ALKHUSHUE, ""));
                        closeActivity(Pref.getValue(getApplicationContext(), Constants.PREF_FAJR_SHOW_ALKHUSHUE_TIME, 7));
                        break;
                    case 2:
                        img_phone_photo.setImageResource(R.drawable.icon_shroq);
                        tv_1.setText(Pref.getValue(activity, Constants.PREF_SUNRISE_TEXT_ALKHUSHUE, ""));
                        closeActivity(Pref.getValue(getApplicationContext(), Constants.PREF_SUNRISE_SHOW_AZKAR_TIME, 7));
                        break;
                    case 3:
                        img_phone_photo.setImageResource(R.drawable.img_azan_2);
                        tv_1.setText(Pref.getValue(activity, Constants.PREF_DHOHR_TEXT_ALKHUSHUE, ""));
                        closeActivity(Pref.getValue(getApplicationContext(), Constants.PREF_DHOHR_SHOW_ALKHUSHUE_TIME, 7));
                        break;
                    case 4:
                        img_phone_photo.setImageResource(R.drawable.img_azan_3);
                        tv_1.setText(Pref.getValue(activity, Constants.PREF_ASR_TEXT_ALKHUSHUE, ""));
                        closeActivity(Pref.getValue(getApplicationContext(), Constants.PREF_ASR_SHOW_ALKHUSHUE_TIME, 7));
                        break;
                    case 5:
                        img_phone_photo.setImageResource(R.drawable.img_azan_4);
                        tv_1.setText(Pref.getValue(activity, Constants.PREF_MAGHRIB_TEXT_ALKHUSHUE, ""));
                        closeActivity(Pref.getValue(getApplicationContext(), Constants.PREF_MAGHRIB_SHOW_ALKHUSHUE_TIME, 7));
                        break;
                    case 6:
                        img_phone_photo.setImageResource(R.drawable.img_azan_5);
                        tv_1.setText(Pref.getValue(activity, Constants.PREF_ISHA_TEXT_ALKHUSHUE, ""));
                        closeActivity(Pref.getValue(getApplicationContext(), Constants.PREF_ISHA_SHOW_ALKHUSHUE_TIME, 7));
                        break;
                }
                break;
        }
    }

    @Override
    protected void onDestroy() {
        if (myThread.isAlive()) {
            myThread.interrupt();
        }
        super.onDestroy();
    }
}