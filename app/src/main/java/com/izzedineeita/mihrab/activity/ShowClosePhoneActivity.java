package com.izzedineeita.mihrab.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.AssetFileDescriptor;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

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

public class ShowClosePhoneActivity extends AppCompatActivity {

    public static int[] daysImage;
    public static int[] monthImage;
    public static int[] monthImageHijri;
    public static int[] dateNumber;
    public static int[] timeNumber;
    public static int[] timeNumberIqamhLeft;
    public static int[] timeNumberAzan;
    public static int[] timeNumberIqamh;
    public static int[] timeNumberSec;
    public Thread myThread = null;
    Animation animation;
    boolean conEnd = false;
    boolean conStop = false;
    //int sec;
    int pray = 0;
    Animation animFadeIn;
    
    // Timer and blinking variables
    private CountDownTimer countDownTimer;
    private CountDownTimer blinkTimer;
    private Handler blinkHandler = new Handler();
    private boolean isBlinking = false;
    private boolean isInBlinkingPhase = false;
    private static final int BLINK_DURATION = 10000; // 10 seconds in milliseconds
    private static final int BLINK_INTERVAL = 500; // 500ms for each blink cycle
    
    private ImageView img_time_hour_1, img_time_hour_2, img_time_mint_1, img_time_sec_2,
            img_time_sec_1, img_date_day, img_date_month_m_2, img_date_month_m_1,
            img_date_month_m, img_date_years_4, img_date_years_3, img_date_years_2,
            img_date_years_1, img_date_month_h_2, img_date_month_h_1, img_date_month_h,
            img_date_years_h_4, img_date_years_h_3, img_date_years_h_2, img_date_years_h_1,
            img_time_mint_2, img_iqamh_time_left_m_1, img_iqamh_time_left_m_2;
    private String[] date;
    private Activity activity;
    private ImageView img_phone_photo,
            img_ud, img_en, img_ar;
    private MediaPlayer mp = new MediaPlayer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_close_phone);

        if (savedInstanceState == null) {

            RelativeLayout relativeLayout;

            int theme = Pref.getValue(getApplicationContext(), Constants.PREF_THEM_POSITION_SELECTED, 0);
            //int theme = 7;
            TextView tv_masged_name;
            switch (theme) {
                case 1:
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

                    setContentView(R.layout.activity_show_close_phone);
                    Resources res = getResources();
                    relativeLayout = findViewById(R.id.relativeLayout);

                    img_ud = findViewById(R.id.img_ud);
                    img_en = findViewById(R.id.img_en);
                    img_ar = findViewById(R.id.img_ar);

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

                    setContentView(R.layout.activity_show_close_phone);
                    Resources res1 = getResources();
                    relativeLayout = findViewById(R.id.relativeLayout);
                    img_ud = findViewById(R.id.img_ud);
                    img_en = findViewById(R.id.img_en);
                    img_ar = findViewById(R.id.img_ar);

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

                    setContentView(R.layout.activity_show_close_phone);
                    Resources res3 = getResources();
                    relativeLayout = findViewById(R.id.relativeLayout);
                    img_ud = findViewById(R.id.img_ud);
                    img_en = findViewById(R.id.img_en);
                    img_ar = findViewById(R.id.img_ar);
                    img_ud.setVisibility(View.GONE);
                    img_ar.setVisibility(View.GONE);
                    Drawable drawable3 = res3.getDrawable(R.drawable.ic_background_thme_4_v);
                    relativeLayout.setBackground(drawable3);

                    ImageView img_sassel = findViewById(R.id.img_sassel);
                    img_sassel.setImageResource(R.drawable.ic_number_thme_4_v_time_fasel);
                    ImageView img_fasl = findViewById(R.id.img_fasl);
                    img_fasl.setImageResource(R.drawable.background_fasel_4);
                    ImageView img_phone_photo = findViewById(R.id.img_phone_photo);
                    img_phone_photo.setImageResource(R.drawable.ic_close_phone_4);
                    img_en.setImageResource(R.drawable.ic_close_phone_en_4);

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

                    setContentView(R.layout.activity_show_close_phone_5);
                    Resources res4 = getResources();
                    relativeLayout = findViewById(R.id.relativeLayout);
                    img_ud = findViewById(R.id.img_ud);
                    img_en = findViewById(R.id.img_en);
                    img_ar = findViewById(R.id.img_ar);
                    img_ud.setVisibility(View.GONE);
                    img_ar.setVisibility(View.GONE);
                    Drawable drawable4 = res4.getDrawable(R.drawable.ic_background_thme_5_v);
                    relativeLayout.setBackground(drawable4);

                    ImageView img_sassel1 = findViewById(R.id.img_sassel);
                    img_sassel1.setImageResource(R.drawable.ic_number_thme_5_v_time_fasel);
                    ImageView img_fasl1 = findViewById(R.id.img_fasl);
                    img_fasl1.setImageResource(R.drawable.background_fasel_5);
                    ImageView img_phone_photo1 = findViewById(R.id.img_phone_photo);
                    img_phone_photo1.setImageResource(R.drawable.ic_close_phone_4);
                    img_en.setImageResource(R.drawable.ic_close_phone_en_4);

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

                    setContentView(R.layout.activity_show_close_phone_6);

                    tv_masged_name = findViewById(R.id.tv_masged_name);
                    tv_masged_name.setText(Pref.getValue(getApplicationContext(), Constants.PREF_MASGED_NAME, "اسم المسجد"));

                    img_ud = findViewById(R.id.img_ud);
                    img_en = findViewById(R.id.img_en);
                    img_ar = findViewById(R.id.img_ar);


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

                    setContentView(R.layout.activity_show_close_phone_7);

                    tv_masged_name = findViewById(R.id.tv_masged_name);
                    tv_masged_name.setText(Pref.getValue(getApplicationContext(), Constants.PREF_MASGED_NAME, "اسم المسجد"));


                    img_ud = findViewById(R.id.img_ud);
                    img_en = findViewById(R.id.img_en);
                    img_ar = findViewById(R.id.img_ar);

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
                    setContentView(R.layout.activity_show_close_phone_8);

                    tv_masged_name = findViewById(R.id.tv_masged_name);
                    tv_masged_name.setText(Pref.getValue(getApplicationContext(), Constants.PREF_MASGED_NAME, "اسم المسجد"));


                    img_ud = findViewById(R.id.img_ud);
                    img_en = findViewById(R.id.img_en);
                    img_ar = findViewById(R.id.img_ar);

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
                    setContentView(R.layout.activity_show_close_phone_9);

                    tv_masged_name = findViewById(R.id.tv_masged_name);
                    tv_masged_name.setText(Pref.getValue(getApplicationContext(), Constants.PREF_MASGED_NAME, "اسم المسجد"));


                    img_ud = findViewById(R.id.img_ud);
                    img_en = findViewById(R.id.img_en);
                    img_ar = findViewById(R.id.img_ar);

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

                    setContentView(R.layout.activity_show_close_phone_10);


                    tv_masged_name = findViewById(R.id.tv_masged_name);
                    tv_masged_name.setText(Pref.getValue(getApplicationContext(), Constants.PREF_MASGED_NAME, "اسم المسجد"));


                    img_ud = findViewById(R.id.img_ud);
                    img_en = findViewById(R.id.img_en);
                    img_ar = findViewById(R.id.img_ar);

                    daysImage = ImagesArrays.daysImageTheme10;
                    monthImage = ImagesArrays.monthImageTheme10;
                    monthImageHijri = ImagesArrays.monthImageHijriTheme10;
                    dateNumber = ImagesArrays.timeNumberDate10;
                    timeNumber = ImagesArrays.timeNumberDate10;
                    timeNumberAzan = ImagesArrays.timeNumberTheme6;
                    timeNumberIqamh = ImagesArrays.timeNumberTheme6;
                    timeNumberIqamhLeft = ImagesArrays.timeNumberIqamhLeft10;
                    timeNumberSec = ImagesArrays.timeNumberDate10;
                    break;
                default:
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

                    setContentView(R.layout.activity_show_close_phone);
                    Resources res0 = getResources();
                    relativeLayout = findViewById(R.id.relativeLayout);
                    img_ud = findViewById(R.id.img_ud);
                    img_en = findViewById(R.id.img_en);
                    img_ar = findViewById(R.id.img_ar);

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

            activity = ShowClosePhoneActivity.this;

            DateHigri hd = new DateHigri();
            date = Utils.writeIslamicDate(ShowClosePhoneActivity.this, hd);

            try {
                init();


                if (savedInstanceState == null) {
                    Bundle extras = getIntent().getExtras();
                    pray = extras.getInt("PRAY");
                } else {
                    pray = (int) savedInstanceState.getSerializable("PRAY");
                }

                checkPlaySound(pray);
                Runnable runnable = new CountDownRunner();
                myThread = new

                        Thread(runnable);
                myThread.start();
                setDataToImage();

                startCountdownTimer();

            } catch (Exception e) {

            }

        }


    }


    private void init() {

        /* init date images */
        img_time_hour_1 = findViewById(R.id.image_current_time_hour_tens);
        img_time_hour_2 = findViewById(R.id.image_current_time_hour_ones);
        img_time_mint_1 = findViewById(R.id.image_current_time_minute_tens);
        img_time_mint_2 = findViewById(R.id.image_current_time_minute_ones);
        img_time_sec_2 = findViewById(R.id.image_current_time_second_ones);
        img_time_sec_1 = findViewById(R.id.image_current_time_second_tens);
        img_date_day = findViewById(R.id.image_current_day);
        img_date_month_m_2 = findViewById(R.id.image_current_date_month_ones);
        img_date_month_m_1 = findViewById(R.id.image_current_date_month_tens);
        img_date_month_m = findViewById(R.id.image_current_date_month);
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
        img_iqamh_time_left_m_1 = findViewById(R.id.img_iqamh_time_left_m_1);
        img_iqamh_time_left_m_2 = findViewById(R.id.img_iqamh_time_left_m_2);

        /* -------------------------------------------------------- */

        img_phone_photo = findViewById(R.id.img_phone_photo);

        animation = new AlphaAnimation(1, 0); //to change visibility from visible to invisible
        animation.setDuration(1000); //1 second duration for each animation cycle
        animation.setInterpolator(new LinearInterpolator());
        animation.setRepeatCount(Animation.INFINITE); //repeating indefinitely
        animation.setRepeatMode(Animation.REVERSE); //animation will start from end point once ended.
        img_phone_photo.startAnimation(animation); //to start animation
        img_ar.startAnimation(animation); //to start animation
        img_en.startAnimation(animation); //to start animation
        img_ud.startAnimation(animation); //to start animation

        // lay_img_iqamh_time_left_s = findViewById(R.id.lay_img_iqamh_time_left_s);

//        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
//                250);

//        tv_external_heat.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                showDialog(tv_external_heat, null, null);
//            }
//        });
//        tv_internal_heat.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                showDialog(tv_internal_heat, null, null);
//            }
//        });
//        img_external_heat.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                showDialog(null, null, img_external_heat);
//            }
//        });
//        img_internal_heat.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                showDialog(null, null, img_internal_heat);
//            }
//        });
//        img_fasl.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                showDialog(null, null, img_fasl);
//            }
//        });
//        img_phone_photo.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                showDialog(null, null, img_phone_photo);
//            }
//        });
//
//        img_ud.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                showDialog(null, null, img_ud);
//            }
//        });
//        img_en.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                showDialog(null, null, img_en);
//            }
//        });
//        img_ar.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                showDialog(null, null, img_ar);
//            }
//        });
//
//        linear_time.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                showDialog(null, linear_time, null);
//            }
//        });
//        linear_date1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                showDialog(null, linear_date1, null);
//            }
//        });
//        linear_date2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                showDialog(null, linear_date2, null);
//            }
//        });
//        linear_time_sec.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                showDialog(null, linear_time_sec, null);
//            }
//        });
//        lay_img_iqamh_time_left_m.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                showDialog(null, lay_img_iqamh_time_left_m, null);
//            }
//        });
//        lay_img_iqamh_time_left_s.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                showDialog(null, lay_img_iqamh_time_left_s, null);
//            }
//        });
    }

    private void setDataToImage() {
        /* set images date */
        int hijriDiff1 = Pref.getValue(ShowClosePhoneActivity.this, Constants.PREF_HEJRY_INT1, 0);
        int iii = Integer.parseInt(date[4]);
        date[4] = String.valueOf(iii + hijriDiff1);
        
        // Apply the same offset to Hijri year if needed
        int hijriYearOffset = Pref.getValue(ShowClosePhoneActivity.this, Constants.PREF_HEJRY_INT1, 0);
        if (hijriYearOffset != 0) {
            int hijriYear = Integer.parseInt(date[6]);
            date[6] = String.valueOf(hijriYear + hijriYearOffset);
        }
        
        // Debug: Let's see what values we're actually getting

        img_date_day.setImageResource(daysImage[Integer.parseInt(date[0])]);
        // For RTL reading order, assign Gregorian day digits in reverse order
        if (date[1].length() != 1) {
            int digit1 = Integer.parseInt(String.valueOf(date[1].charAt(1)));
            int digit2 = Integer.parseInt(String.valueOf(date[1].charAt(0)));
            img_date_month_m_1.setImageResource(dateNumber[digit1]);
            img_date_month_m_2.setVisibility(View.VISIBLE);
            img_date_month_m_2.setImageResource(dateNumber[digit2]);
        } else {
            int digit = Integer.parseInt(String.valueOf(date[1].charAt(0)));
            img_date_month_m_1.setImageResource(dateNumber[digit]);
            img_date_month_m_2.setVisibility(View.GONE);
        }
        img_date_month_m.setImageResource(monthImage[Integer.parseInt(date[2])]);
        // For RTL reading order, assign digits in reverse order
        int year1 = Integer.parseInt(String.valueOf(date[3].charAt(3)));
        int year2 = Integer.parseInt(String.valueOf(date[3].charAt(2)));
        int year3 = Integer.parseInt(String.valueOf(date[3].charAt(1)));
        int year4 = Integer.parseInt(String.valueOf(date[3].charAt(0)));
        img_date_years_1.setImageResource(dateNumber[year1]);
        img_date_years_2.setImageResource(dateNumber[year2]);
        img_date_years_3.setImageResource(dateNumber[year3]);
        img_date_years_4.setImageResource(dateNumber[year4]);
        // For RTL reading order, assign Hijri day digits in reverse order
        if (date[4].length() != 1) {
            img_date_month_h_1.setImageResource(dateNumber[Integer.parseInt(String.valueOf(date[4].charAt(1)))]);
            img_date_month_h_2.setVisibility(View.VISIBLE);
            img_date_month_h_2.setImageResource(dateNumber[Integer.parseInt(String.valueOf(date[4].charAt(0)))]);
        } else {
            img_date_month_h_1.setImageResource(dateNumber[Integer.parseInt(String.valueOf(date[4].charAt(0)))]);
            img_date_month_h_2.setVisibility(View.GONE);
        }
        img_date_month_h.setImageResource(monthImageHijri[Integer.parseInt(date[5]) - 1]);
        // For RTL reading order, assign digits in reverse order
        img_date_years_h_1.setImageResource(dateNumber[Integer.parseInt(String.valueOf(date[6].charAt(3)))]);
        img_date_years_h_2.setImageResource(dateNumber[Integer.parseInt(String.valueOf(date[6].charAt(2)))]);
        img_date_years_h_3.setImageResource(dateNumber[Integer.parseInt(String.valueOf(date[6].charAt(1)))]);
        img_date_years_h_4.setImageResource(dateNumber[Integer.parseInt(String.valueOf(date[6].charAt(0)))]);
    }

    private void checkPlaySound(int pray) {
        switch (pray) {
            case 1:
                if (Pref.getValue(getApplicationContext(), Constants.PREF_CLOSE_NOTIFICATION_SOUND_FAJR, true)) {
                    if (!mp.isPlaying()) {
                        mp = new MediaPlayer();
                        String uriSound = Pref.getValue(getApplicationContext(), Constants.PREF_FAJER_SOUND_CLOSE_PHONE_PATH, null);
                        if (uriSound != null) {
                            if (!mp.isPlaying()) play(uriSound);
                        } else {
                            if (!mp.isPlaying()) play();
                        }

                    }
                }
                break;
            case 3:
                if (Pref.getValue(getApplicationContext(), Constants.PREF_CLOSE_NOTIFICATION_SOUND_DUHR, true)) {
                    if (!mp.isPlaying()) {
                        mp = new MediaPlayer();
                        String uriSound = Pref.getValue(getApplicationContext(), Constants.PREF_DHOHR_SOUND_CLOSE_PHONE_PATH, null);
                        if (uriSound != null) {
                            if (!mp.isPlaying()) play(uriSound);

                        } else {
                            if (!mp.isPlaying()) play();

                        }
                    }
                }
                break;
            case 4:
                if (Pref.getValue(getApplicationContext(), Constants.PREF_CLOSE_NOTIFICATION_SOUND_ASR, true)) {
                    if (!mp.isPlaying()) {
                        mp = new MediaPlayer();
                        String uriSound = Pref.getValue(getApplicationContext(), Constants.PREF_ASR_SOUND_CLOSE_PHONE_PATH, null);
                        if (uriSound != null) {
                            if (!mp.isPlaying()) play(uriSound);

                        } else {
                            if (!mp.isPlaying()) play();

                        }
                    }
                }
                break;
            case 5:
                if (Pref.getValue(getApplicationContext(), Constants.PREF_CLOSE_NOTIFICATION_SOUND_MAGHRIB, true)) {
                    if (!mp.isPlaying()) {
                        mp = new MediaPlayer();
                        String uriSound = Pref.getValue(getApplicationContext(), Constants.PREF_MAGHRIB_SOUND_CLOSE_PHONE_PATH, null);
                        if (uriSound != null) {
                            if (!mp.isPlaying()) play(uriSound);

                        } else {
                            if (!mp.isPlaying()) play();

                        }
                    }
                }
                break;
            case 6:
                if (Pref.getValue(getApplicationContext(), Constants.PREF_CLOSE_NOTIFICATION_SOUND_ISHA, true)) {
                    if (!mp.isPlaying()) {
                        mp = new MediaPlayer();
                        String uriSound = Pref.getValue(getApplicationContext(), Constants.PREF_ISHA_SOUND_CLOSE_PHONE_PATH, null);
                        if (uriSound != null) {
                            if (!mp.isPlaying()) play(uriSound);

                        } else {
                            if (!mp.isPlaying()) play();

                        }
                    }
                }
                break;
            case 7:
                // Friday prayer - check if Friday sound notification is enabled
                if (Pref.getValue(getApplicationContext(), Constants.PREF_CLOSE_NOTIFICATION_SOUND_GOMAA, true)) {
                    if (!mp.isPlaying()) {
                        mp = new MediaPlayer();
                        String uriSound = Pref.getValue(getApplicationContext(), Constants.PREF_DHOHR_SOUND_CLOSE_PHONE_PATH, null);
                        if (uriSound != null) {
                            if (!mp.isPlaying()) play(uriSound);
                        } else {
                            if (!mp.isPlaying()) play();
                        }
                    }
                }
                break;
        }
    }

    private void doWork() {
        runOnUiThread(new Runnable() {
            public void run() {
                try {
                    // Update current time display
                    DateFormat timeNow = new SimpleDateFormat("hh:mmass", Locale.ENGLISH);
                    Calendar c = Calendar.getInstance();
                    String timeText = timeNow.format(c.getTime());
                    img_time_hour_1.setImageResource(timeNumber[Integer.parseInt(String.valueOf(timeText.charAt(0)))]);
                    img_time_hour_2.setImageResource(timeNumber[Integer.parseInt(String.valueOf(timeText.charAt(1)))]);
                    img_time_mint_1.setImageResource(timeNumber[Integer.parseInt(String.valueOf(timeText.charAt(3)))]);
                    img_time_mint_2.setImageResource(timeNumber[Integer.parseInt(String.valueOf(timeText.charAt(4)))]);
                    img_time_sec_1.setImageResource(timeNumberSec[Integer.parseInt(String.valueOf(timeText.charAt(7)))]);
                    img_time_sec_2.setImageResource(timeNumberSec[Integer.parseInt(String.valueOf(timeText.charAt(8)))]);

                    // Timer display is now handled by the CountDownTimer in startCountdownTimer()
                    // The blinking effect is also handled separately
                    
                } catch (Exception e) {
                    // Handle exception
                }
            }
        });
    }

    private void showDialog(TextView textView, LinearLayout linearLayout1, ImageView imageView) {
        RelativeLayout linearLayout = new RelativeLayout(activity);
        final NumberPicker aNumberPicker = new NumberPicker(activity);
        aNumberPicker.setMaxValue(400);
        aNumberPicker.setMinValue(40);

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
//                                    if (textView.getId() == R.id.tv_fajet_name) {
//                                        tv_fajet_name.setTextSize(aNumberPicker.getValue());
//                                        tv_sun_name.setTextSize(aNumberPicker.getValue());
//                                        tv_thahr_name.setTextSize(aNumberPicker.getValue());
//                                        tv_asr_name.setTextSize(aNumberPicker.getValue());
//                                        tv_magrb_name.setTextSize(aNumberPicker.getValue());
//                                        tv_isha_name.setTextSize(aNumberPicker.getValue());
//                                    } else if (textView.getId() == R.id.tv_fajr) {
//                                        tv_fajr.setTextSize(aNumberPicker.getValue());
//                                        tv_sun.setTextSize(aNumberPicker.getValue());
//                                        tv_thahr.setTextSize(aNumberPicker.getValue());
//                                        tv_asr.setTextSize(aNumberPicker.getValue());
//                                        tv_magrb.setTextSize(aNumberPicker.getValue());
//                                        tv_isha.setTextSize(aNumberPicker.getValue());
//                                    } else {
//                                        textView.setTextSize(aNumberPicker.getValue());
//                                    }
                                    textView.setTextSize(aNumberPicker.getValue());

                                } else if (textView == null && linearLayout1 != null) {
                                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                                            aNumberPicker.getValue());
                                    lp.gravity = Gravity.CENTER_HORIZONTAL;

                                    linearLayout1.setLayoutParams(lp);
                                } else {
                                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, aNumberPicker.getValue());
                                    layoutParams.gravity = Gravity.CENTER_HORIZONTAL;
                                    imageView.setLayoutParams(layoutParams);

                                    // imageView.getLayoutParams().height =  aNumberPicker.getValue();
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

    private void play(String path) {

//        File path = Environment.getExternalStoragePublicDirectory(
//                Environment.DIRECTO cRY_DOWNLOADS);
//        File file = new File(path, "/document/audio:44");
        //String path = "/document/raw:/storage/emulated/0/Download/close_phone.mp3";
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
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();

            if (!mp.isPlaying()) play();
        }
    }

    private void play() {

//        File path = Environment.getExternalStoragePublicDirectory(
//                Environment.DIRECTORY_DOWNLOADS);
//        File file = new File(path, uri);


//        try {
//            AssetFileDescriptor descriptor = getAssets().openFd("close_phone.mp3");
//            mp.setDataSource(descriptor.getFileDescriptor(), descriptor.getStartOffset(), descriptor.getLength());
//            descriptor.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }


        try {
            AssetFileDescriptor descriptor = getAssets().openFd("close_phone.mp3");
            mp.setDataSource(descriptor.getFileDescriptor(), descriptor.getStartOffset(), descriptor.getLength());
            descriptor.close();
            // mp.setDataSource(context, Uri.parse(file.toString()));
            mp.prepareAsync();
            mp.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mp.start();
                }
            });
        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();

        } catch (SecurityException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();

        } catch (IllegalStateException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();

        }
    }

    private void startCountdownTimer() {
        String showTime = Pref.getValue(activity, Constants.PREF_CLOSE_PHONE_ALERT_SHOW_BEFORE_IQAMH, "59");
        int millis = 1000;
        try {
            millis = Integer.parseInt(showTime) * 1000;
        } catch (Exception e) {
            // Use default value if parsing fails
        }
        
        // Add 10 seconds to the total timer for blinking phase
        int totalMillis = millis + BLINK_DURATION;
        
        countDownTimer = new CountDownTimer(totalMillis, 1000) {
            @Override
            public void onFinish() {
                // Timer finished, close activity
                finishActivity();
            }

            @Override
            public void onTick(long millisUntilFinished) {
                // Check if we're in the blinking phase (last 10 seconds)
                if (millisUntilFinished <= BLINK_DURATION) {
                    if (!isInBlinkingPhase) {
                        // Start blinking phase
                        isInBlinkingPhase = true;
                        startBlinkingEffect();
                    }
                    // During blinking phase, keep showing 00
                    img_iqamh_time_left_m_1.setImageResource(timeNumberIqamhLeft[0]);
                    img_iqamh_time_left_m_2.setImageResource(timeNumberIqamhLeft[0]);
                } else {
                    // Normal countdown phase
                    updateTimerDisplay(millisUntilFinished - BLINK_DURATION);
                }
            }
        }.start();
    }
    
    private void updateTimerDisplay(long millisUntilFinished) {
        long seconds = millisUntilFinished / 1000;
        if (seconds <= 0) {
            // Show 00
            img_iqamh_time_left_m_1.setImageResource(timeNumberIqamhLeft[0]);
            img_iqamh_time_left_m_2.setImageResource(timeNumberIqamhLeft[0]);
        } else {
            // Show countdown
            String timeText = String.valueOf(seconds);
            if (timeText.length() == 2) {
                img_iqamh_time_left_m_1.setImageResource(timeNumberIqamhLeft[Integer.parseInt(String.valueOf(timeText.charAt(0)))]);
                img_iqamh_time_left_m_2.setImageResource(timeNumberIqamhLeft[Integer.parseInt(String.valueOf(timeText.charAt(1)))]);
            } else {
                img_iqamh_time_left_m_1.setImageResource(timeNumberIqamhLeft[0]);
                img_iqamh_time_left_m_2.setImageResource(timeNumberIqamhLeft[Integer.parseInt(String.valueOf(timeText.charAt(0)))]);
            }
        }
    }
    
    private void startBlinkingEffect() {
        isBlinking = true;
        
        // Set timer to show 00
        img_iqamh_time_left_m_1.setImageResource(timeNumberIqamhLeft[0]);
        img_iqamh_time_left_m_2.setImageResource(timeNumberIqamhLeft[0]);
        
        // Make sure elements are visible initially
        img_iqamh_time_left_m_1.setVisibility(View.VISIBLE);
        img_iqamh_time_left_m_2.setVisibility(View.VISIBLE);
        
        // Use a separate CountDownTimer for blinking to ensure it runs for exactly 10 seconds
        blinkTimer = new CountDownTimer(BLINK_DURATION, BLINK_INTERVAL) {
            @Override
            public void onFinish() {
                // Blinking finished, activity will be closed by main CountDownTimer
                isBlinking = false;
            }

            @Override
            public void onTick(long millisUntilFinished) {
                // Toggle visibility for blinking effect
                if (img_iqamh_time_left_m_1.getVisibility() == View.VISIBLE) {
                    img_iqamh_time_left_m_1.setVisibility(View.INVISIBLE);
                    img_iqamh_time_left_m_2.setVisibility(View.INVISIBLE);
                } else {
                    img_iqamh_time_left_m_1.setVisibility(View.VISIBLE);
                    img_iqamh_time_left_m_2.setVisibility(View.VISIBLE);
                }
            }
        }.start();
    }
    
    private void finishActivity() {
        // Check if it's Friday Dhuhr prayer (pray = 3 for Dhuhr)
        Calendar calendar = Calendar.getInstance();
        boolean isFriday = calendar.get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY;
        boolean isDhuhrPrayer = (pray == 3);
        
        // If it's Friday Dhuhr prayer, go back to main screen
        if (isFriday && isDhuhrPrayer) {
            MainActivity.isOpenClosePhone = false;
            finish();
            // Return to main screen
            try {
                Intent intent = new Intent(activity, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            } catch (Exception e) {
                // Handle exception
            }
        } else {
            // For all other prayers, continue with normal flow
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

            MainActivity.isOpenClosePhone = false;
            finish();
            if (check) {
                try {
                    Intent intent = new Intent(activity, ShowAlkhushueActivity.class);
                    intent.putExtra("PRAY", pray);
                    startActivity(intent);
                } catch (Exception e) {
                    // Handle exception
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        if (myThread.isAlive()) {
            myThread.interrupt();
        }
        if (mp.isPlaying()) {
            mp.stop();
        }
        
        // Clean up countdown timer and blinking timer
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        if (blinkTimer != null) {
            blinkTimer.cancel();
        }
        if (blinkHandler != null) {
            blinkHandler.removeCallbacksAndMessages(null);
        }
        
        super.onDestroy();
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

                }
            }
        }

    }
}