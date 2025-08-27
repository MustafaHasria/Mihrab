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
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.izzedineeita.mihrab.Adapters.SliderAdapter;
import com.izzedineeita.mihrab.MainActivity;
import com.izzedineeita.mihrab.R;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;
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

        SliderView sliderView = findViewById(R.id.imageSlider);
        sliderView.setSliderAdapter(new SliderAdapter(mSliderItems));


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
                sliderView.setIndicatorAnimation(IndicatorAnimationType.WORM);
                sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
                Log.e("XXX", "" + sec);
                sliderView.setScrollTimeInSec(sec);
                sliderView.startAutoCycle();
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
                
                // Make sure text is not empty
                if (text != null && !text.isEmpty()) {
                    Log.d("ShowAdsActivity", "Setting text ad content: " + text);
                    tvAdsText.setText(text);
                } else {
                    Log.e("ShowAdsActivity", "Text ad content is empty or null");
                    tvAdsText.setText("No content available");
                }
                
                tvAdsText.setVisibility(View.VISIBLE);
                
                // Make ScrollView visible
                ScrollView scrollViewAdsText = findViewById(R.id.scrollViewAdsText);
                scrollViewAdsText.setVisibility(View.VISIBLE);
                
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
     * Convert percentage (0-100) to font size in sp (10sp to 150sp)
     * @param percentage The percentage value from SeekBar (0-100)
     * @return Font size in sp
     */
    private int getFontSizeFromPercentage(int percentage) {
        // Map 0% to 10sp, 100% to 150sp
        return 10 + (int) ((percentage / 100.0) * 140);
    }

    /**
     * Convert percentage (0-100) to movement speed multiplier
     * @param percentage The percentage value from SeekBar (0-100)
     * @return Speed multiplier (0.5x to 2.0x)
     */
    private float getSpeedMultiplierFromPercentage(int percentage) {
        // Map 0% to 0.5x (slow), 50% to 1.0x (normal), 100% to 2.0x (fast)
        if (percentage <= 50) {
            // 0-50% maps to 0.5x - 1.0x
            return 0.5f + (percentage / 50.0f) * 0.5f;
        } else {
            // 51-100% maps to 1.0x - 2.0x
            return 1.0f + ((percentage - 50) / 50.0f) * 1.0f;
        }
    }

    /**
     * Apply text ads preferences for font size and movement speed
     */
    private void applyTextAdsPreferences() {
        try {
            SharedPreferences sp = getSharedPreferences("Mhrab", MODE_PRIVATE);
            
            // Apply font size based on percentage preference (0-100)
            int fontSizePercentage = sp.getInt(Constants.PREF_TEXT_ADS_FONT_SIZE, 50); // Default: 50%
            int fontSizeSp = getFontSizeFromPercentage(fontSizePercentage);
            
            // Cap font size at 80sp to ensure text is visible
            if (fontSizeSp > 80) {
                fontSizeSp = 80;
            }
            
            tvAdsText.setTextSize(TypedValue.COMPLEX_UNIT_SP, fontSizeSp);
            // Don't limit lines for scrolling text
            tvAdsText.setMaxLines(Integer.MAX_VALUE);
            tvAdsText.setEllipsize(null);

            // Apply movement speed based on percentage preference (0-100)
            int speedPercentage = sp.getInt(Constants.PREF_TEXT_ADS_MOVEMENT_SPEED, 50); // Default: 50%
            float speedMultiplier = getSpeedMultiplierFromPercentage(speedPercentage);
            
            // Apply scrolling animation for text movement
            applyTextScrollingAnimation(speedMultiplier);
            Log.d("ShowAdsActivity", "Applied font size: " + fontSizeSp + "sp, speed multiplier: " + speedMultiplier + "x");
            
        } catch (Exception e) {
            Log.e("ShowAdsActivity", "Error applying text ads preferences: " + e.getMessage(), e);
        }
    }

    /**
     * Apply scrolling animation to text ads
     */
    private void applyTextScrollingAnimation(float speedMultiplier) {
        try {
            // Find the ScrollView that contains the text
            ScrollView scrollViewAdsText = findViewById(R.id.scrollViewAdsText);
            
            // Configure TextView for multi-line display
            tvAdsText.setSingleLine(false);
            tvAdsText.setEllipsize(null);
            
            // Ensure text is visible
            tvAdsText.setVisibility(View.VISIBLE);
            scrollViewAdsText.setVisibility(View.VISIBLE);
            
            // Log the text content to verify it's not empty
            Log.d("ShowAdsActivity", "Text content: " + tvAdsText.getText());
            
            // Set up auto-scrolling behavior based on speed multiplier
            final int scrollDelay = (int) (2000 / speedMultiplier); // Base delay of 2 seconds, adjusted by speed
            final Handler handler = new Handler();
            final Runnable scrollRunnable = new Runnable() {
                @Override
                public void run() {
                    // Calculate scroll amount based on text height and available space
                    int maxScroll = tvAdsText.getHeight() - scrollViewAdsText.getHeight();
                    if (maxScroll > 0) {
                        // Get current scroll position
                        int currentScroll = scrollViewAdsText.getScrollY();
                        
                        // If we're at the bottom, scroll back to top
                        if (currentScroll >= maxScroll) {
                            scrollViewAdsText.smoothScrollTo(0, 0);
                        } else {
                            // Otherwise scroll down a bit more
                            scrollViewAdsText.smoothScrollBy(0, 10);
                        }
                    }
                    
                    // Schedule the next scroll
                    handler.postDelayed(this, 100);
                }
            };
            
            // Start scrolling after a short delay to allow layout to complete
            handler.postDelayed(scrollRunnable, scrollDelay);
            
            Log.d("ShowAdsActivity", "Applied vertical scrolling with speed multiplier: " + speedMultiplier + "x, delay: " + scrollDelay + "ms");
            
        } catch (Exception e) {
            Log.e("ShowAdsActivity", "Error applying text scrolling animation: " + e.getMessage(), e);
        }
    }

}