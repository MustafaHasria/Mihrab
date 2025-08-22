package com.izzedineeita.mihrab.activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.izzedineeita.mihrab.MainActivity;
import com.izzedineeita.mihrab.R;
import com.izzedineeita.mihrab.constants.Constants;
import com.izzedineeita.mihrab.constants.DateHigri;
import com.izzedineeita.mihrab.database.DataBaseHelper;
import com.izzedineeita.mihrab.model.Ads;
import com.izzedineeita.mihrab.utils.Pref;
import com.izzedineeita.mihrab.utils.Utils;

import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ShowAdsActivity extends AppCompatActivity {


    private TextView tv_masged_name, tv_fajr, tv_sun, tv_thahr, tv_asr, tv_magrb, tv_isha,
            tv_day, tv_time, tv_time_sec, tv_data_hajre, tv_data_melade,
            tv_fajet_name, tv_sun_name, tv_thahr_name, tv_asr_name, tv_magrb_name, tv_isha_name;
    private LinearLayout linear_pray_background;
    private DataBaseHelper db;
    public String cfajr, csunrise, cdhohr, casr, cmaghrib, cisha;
    private String dateM, dateH;
    private Activity activity;
    private TextView tvAdsText;
    private ImageView ivAdsImage;
    private VideoView vvAdsVideo;
    private Ads ads;
    private Runnable adsRunnable;
    private Handler AdsHandler = new Handler();
    public Thread myThread = null;
    int theme;
    int showVideo = 0;
    public CardView card;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_ads);

        theme = Pref.getValue(getApplicationContext(), Constants.PREF_THEM_POSITION_SELECTED, 0);
        boolean b = Pref.getValue(getApplicationContext(), Constants.PREF_CHECK_BOX_2, false);

        switch (theme) {
            case 4:
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                if (b) {
                    setContentView(R.layout.activity_show_ads_0_5);
                } else {
                    setContentView(R.layout.activity_show_ads_5);
                    linear_pray_background = findViewById(R.id.linear_pray_background);

                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                            250);
                    linear_pray_background.setLayoutParams(lp);
                }

                break;
            case 5:
            case 6:
            case 8:
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);

                setContentView(R.layout.activity_show_ads_6);
                break;
            case 7:
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);

                setContentView(R.layout.activity_show_ads_8);
                break;
            case 9:
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);

                setContentView(R.layout.activity_show_ads_10);
                break;
            default:
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                if (b) {
                    setContentView(R.layout.activity_show_ads_0_1);
                } else {
                    setContentView(R.layout.activity_show_ads);
                    linear_pray_background = findViewById(R.id.linear_pray_background);

                    LinearLayout.LayoutParams lp1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                            250);
                    linear_pray_background.setLayoutParams(lp1);
                }


                break;
        }

        MainActivity.isOpenAds = true;
        activity = ShowAdsActivity.this;

        Runnable runnable = new ShowAdsActivity.CountDownRunner();
        myThread = new Thread(runnable);
        myThread.start();

        ads = (Ads) getIntent().getSerializableExtra("ads");

        DateHigri hd = new DateHigri();

//        switch (theme) {
//            case 5:
//                dateM = Utils.writeMDate1(activity, hd);
//                dateH = Utils.writeHDate1(activity, hd);
//                break;
//            default:
//                dateM = Utils.writeMDate(activity, hd);
//                dateH = Utils.writeHDate(activity, hd);
//                break;
//        }
        dateM = Utils.writeMDate(activity);
        dateH = Utils.writeHDate(activity);

        ImageView ivPulpitAdsBox = findViewById(R.id.ivPulpitAdsBox);
        if (ivPulpitAdsBox != null) {
            // Use the same SharedPreferences instance as AddAdsActivity
            SharedPreferences sp = getSharedPreferences("Mhrab", MODE_PRIVATE);

            int visibilityState = sp.getInt(Constants.PREF_HIDE_PULPIT_ADS_BOX, View.VISIBLE);
            ivPulpitAdsBox.setVisibility(visibilityState);
        }
//        Log.e("XXX9", "" + ads.getMasjedID());
        init();
        card = findViewById(R.id.card);
        setData();
        fillData();
        if (getIntent().getAction().equals("main")) checkAds();
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


        tvAdsText = findViewById(R.id.tvAdsText);
        card = findViewById(R.id.card);
        vvAdsVideo = findViewById(R.id.vvAdsVideo);

        Calendar calendar = Calendar.getInstance();
        String[] days = new String[]{"الاحد", "الاثنين", "الثلاثاء", "الاربعاء", "الخميس", "الجمعة", "السبت"};
        String day = days[calendar.get(Calendar.DAY_OF_WEEK) - 1];
        tv_day.setText(day);

    }

    private void setData() {

        tv_masged_name.setText(Pref.getValue(getApplicationContext(), Constants.PREF_MASGED_NAME, "اسم المسجد"));

//        Calendar today1 = Calendar.getInstance();
//        SimpleDateFormat format = new SimpleDateFormat("M/d/yy", Locale.ENGLISH);
//
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

    private void fillData() {
        int type = ads.getType();
        String title = ads.getTitle();
        String text = ads.getText();
        String image = ads.getImage();
        String image1 = ads.getImage1();
        String image2 = ads.getImage2();
        String image3 = ads.getImage3();
        String image4 = ads.getImage4();
        String image5 = ads.getImage5();
        String image6 = ads.getImage6();
        final String video = ads.getVideo();
        final String video1 = ads.getVideo1();
        final String video2 = ads.getVideo2();
        final int sec = ads.getImageSec();


        Log.e("XXX1", "" + type);

        List<String> mSliderItems = new ArrayList<>();
        if (!image.isEmpty()) {
            mSliderItems.add(image);
        }
        if (!image1.isEmpty()) {
            mSliderItems.add(image1);
        }
        if (!image2.isEmpty()) {
            mSliderItems.add(image2);
        }
        if (!image3.isEmpty()) {
            mSliderItems.add(image3);
        }
        if (!image4.isEmpty()) {
            mSliderItems.add(image4);
        }
        if (!image5.isEmpty()) {
            mSliderItems.add(image5);
        }
        if (!image6.isEmpty()) {
            mSliderItems.add(image6);
        }

        // SliderView sliderView = findViewById(R.id.imageSlider); // Removed Image Slider dependency
        // sliderView.setSliderAdapter(new SliderAdapter(mSliderItems)); // Removed Image Slider dependency


        // tvTitle.setText(title);
        try {
            if (type == 1) {
                card.setVisibility(View.VISIBLE);
                vvAdsVideo.setVisibility(View.GONE);
                tvAdsText.setVisibility(View.GONE);
                File f = new File(image);
                Bitmap bmp = BitmapFactory.decodeFile(f.getAbsolutePath());
//                ivAdsImage.setImageBitmap(bmp);
//                Glide.with(activity).load(f).into(ivAdsImage);
                // sliderView.setIndicatorAnimation(IndicatorAnimationType.WORM); // Removed Image Slider dependency
                // sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION); // Removed Image Slider dependency
                Log.e("XXX", "" + sec);
                // sliderView.setScrollTimeInSec(sec); // Removed Image Slider dependency
                // sliderView.startAutoCycle(); // Removed Image Slider dependency
            } else if (type == 2) {
                card.setVisibility(View.GONE);
                vvAdsVideo.setVisibility(View.VISIBLE);
                tvAdsText.setVisibility(View.GONE);
                tvAdsText.setText(text);

                vvAdsVideo.setVideoURI(Uri.parse(video));
                vvAdsVideo.start();

                vvAdsVideo.setOnCompletionListener(mediaPlayer -> {
                    if (showVideo == 0) {
                        vvAdsVideo.setVideoURI(Uri.parse(video));
                        vvAdsVideo.start();
                        if (!video1.isEmpty()) {
                            vvAdsVideo.setVideoURI(Uri.parse(video1));
                            vvAdsVideo.start();
                            showVideo = 1;
                        } else {
                            vvAdsVideo.setVideoURI(Uri.parse(video));
                            vvAdsVideo.start();
                            showVideo = 0;
                        }

                    } else if (showVideo == 1) {
                        if (!video2.isEmpty()) {
                            vvAdsVideo.setVideoURI(Uri.parse(video2));
                            vvAdsVideo.start();
                            showVideo = 2;
                        } else {
                            vvAdsVideo.setVideoURI(Uri.parse(video));
                            vvAdsVideo.start();
                            showVideo = 0;
                        }
                    } else if (showVideo == 2) {
                        vvAdsVideo.setVideoURI(Uri.parse(video));
                        vvAdsVideo.start();
                        showVideo = 0;
                    }


                });

//                vvAdsVideo.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
//
//                    @Override
//                    public void onCompletion(MediaPlayer vmp) {
//
//                    }
//                });
            } else if (type == 3) {
                card.setVisibility(View.GONE);
                vvAdsVideo.setVisibility(View.GONE);
                tvAdsText.setText(text);
                tvAdsText.setVisibility(View.VISIBLE);
                
                // Apply text ads styling and animation based on preferences
                applyTextAdsPreferences();
            }

        } catch (
                OutOfMemoryError e) {
            e.printStackTrace();
            Utils.showCustomToast(activity, getString(R.string.advError));
        } catch (
                Exception e) {
            e.printStackTrace();
        }

    }

    private void checkAds() {
        if (ads != null) {
            String adsEndTime = ads.getEndTime();
            SimpleDateFormat df = new SimpleDateFormat("HH:mm", Locale.ENGLISH);
            Date date = new Date();
            String currentTime = df.format(date);
            try {
                Date end = df.parse(adsEndTime);
                Date now = df.parse(currentTime);
                Log.i("XXX" + " End: ", end.toString());
                Log.i("XXX" + " now: ", now.toString());
                if (now.after(end)) {
                    finish();
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        adsRunnable = () -> checkAds();
        AdsHandler.postDelayed(adsRunnable, 1000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (myThread.isAlive()) {
            myThread.interrupt();
        }
        AdsHandler.removeCallbacks(adsRunnable);
        MainActivity.isOpenAds = false;
    }

    @Override
    public void onBackPressed() {
        MainActivity.isOpenAds = false;
        finish();
        if (myThread.isAlive()) {
            myThread.interrupt();
        }
        super.onBackPressed();
    }

    /**
     * Apply text ads preferences for font size and movement speed
     */
    private void applyTextAdsPreferences() {
        try {
            SharedPreferences sp = getSharedPreferences("Mhrab", MODE_PRIVATE);
            
            // Apply font size based on preference
            int fontSize = sp.getInt(Constants.PREF_TEXT_ADS_FONT_SIZE, 1); // Default: medium
            float textSize = 24f; // Default size
            
            switch (fontSize) {
                case 0: // Small
                    textSize = 18f;
                    break;
                case 1: // Medium
                    textSize = 24f;
                    break;
                case 2: // Large
                    textSize = 32f;
                    break;
            }
            
            tvAdsText.setTextSize(textSize);

            // Apply movement speed based on preference
            int movementSpeed = sp.getInt(Constants.PREF_TEXT_ADS_MOVEMENT_SPEED, 1); // Default: medium
            long animationDuration = 10000; // Default duration in milliseconds
            
            switch (movementSpeed) {
                case 0: // Slow
                    animationDuration = 15000; // 15 seconds
                    break;
                case 1: // Medium
                    animationDuration = 10000; // 10 seconds
                    break;
                case 2: // Fast
                    animationDuration = 5000; // 5 seconds
                    break;
            }
            
            // Apply scrolling animation for text movement
            applyTextScrollingAnimation(animationDuration);
            Log.d("ShowAdsActivity", "Applied movement speed: " + animationDuration + "ms");
            
        } catch (Exception e) {
            Log.e("ShowAdsActivity", "Error applying text ads preferences: " + e.getMessage(), e);
        }
    }

    /**
     * Apply scrolling animation to text ads
     */
    private void applyTextScrollingAnimation(long duration) {
        try {
            // Start a simple marquee effect by enabling scrolling
            tvAdsText.setSelected(true);
            tvAdsText.setSingleLine(true);
            
            // Create a custom animation handler for text movement
            Handler animationHandler = new Handler();
            Runnable scrollRunnable = new Runnable() {
                @Override
                public void run() {
                    if (tvAdsText != null && tvAdsText.getVisibility() == View.VISIBLE) {
                        // Simple scroll effect - this can be enhanced with more complex animations
                        tvAdsText.scrollBy(1, 0);
                        animationHandler.postDelayed(this, duration / 1000); // Adjust scroll speed
                    }
                }
            };
            
            // Start the scrolling animation
            animationHandler.post(scrollRunnable);
            
        } catch (Exception e) {
            Log.e("ShowAdsActivity", "Error applying text scrolling animation: " + e.getMessage(), e);
        }
    }

}