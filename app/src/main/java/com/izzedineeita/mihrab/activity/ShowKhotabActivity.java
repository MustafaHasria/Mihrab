package com.izzedineeita.mihrab.activity;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.RequiresApi;
import androidx.core.content.res.ResourcesCompat;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.izzedineeita.mihrab.MainActivity;
import com.izzedineeita.mihrab.R;
import com.izzedineeita.mihrab.constants.Constants;
import com.izzedineeita.mihrab.constants.DateHigri;
import com.izzedineeita.mihrab.model.Khotab;
import com.izzedineeita.mihrab.utils.Pref;
import com.izzedineeita.mihrab.utils.Utils;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class ShowKhotabActivity extends YouTubeBaseActivity
       implements YouTubePlayer.OnInitializedListener
{

    private TextView tv_masged_name, tv_fajr, tv_sun, tv_thahr, tv_asr, tv_magrb, tv_isha,
            tv_day, tv_time, tv_time_sec, tv_data_hajre, tv_data_melade,
            tv_fajet_name, tv_sun_name, tv_thahr_name, tv_asr_name, tv_magrb_name, tv_isha_name;
    private LinearLayout linear_pray_background, layout_title;

    private String dateM, dateH;
    private Activity activity;

    private Runnable adsRunnable;
    private Handler AdsHandler = new Handler();
    public Thread myThread = null;

    private static final int RECOVERY_REQUEST = 1;
    public YouTubePlayerView youTubeView;
    private Khotab khotab;
    private VideoView videoView;
    private TextView tvUrdText, tvFarText, tvEnText, tvTitle, tvEnTextTitel, tvFarTextTitel, tvUrdTextTitel;
    private LinearLayout layout_video;
    List<String> splitedarray1 = null, splitedarray2 = null, splitedarray3 = null;
    List<String> finalSplitedarray = null, finalSplitedarray1 = null, finalSplitedarray2 = null;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.activity_show_khotab);

        boolean b = Pref.getValue(getApplicationContext(), Constants.PREF_CHECK_BOX_2, false);
        int theme = Pref.getValue(getApplicationContext(), Constants.PREF_THEM_POSITION_SELECTED, 0);
        switch (theme) {
            case 4:
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                if (b) {
                    setContentView(R.layout.activity_show_khotab_0_5);
                } else {
                    setContentView(R.layout.activity_show_khotab_5);
                    linear_pray_background = findViewById(R.id.linear_pray_background);
                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                            250);
                    linear_pray_background.setLayoutParams(lp);
                }


                break;

            case 7:
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
                setContentView(R.layout.activity_show_khotab_8);
                break;
            case 5:
            case 6:
            case 8:
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
                setContentView(R.layout.activity_show_khotab_6);
                break;
            case 9:
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
                setContentView(R.layout.activity_show_khotab_10);
                break;
            default:
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                if (b) {
                    setContentView(R.layout.activity_show_khotab_0_1);
                } else {
                    setContentView(R.layout.activity_show_khotab);
                    linear_pray_background = findViewById(R.id.linear_pray_background);
                    LinearLayout.LayoutParams lp1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                            250);
                    linear_pray_background.setLayoutParams(lp1);
                }


                break;
        }

        activity = ShowKhotabActivity.this;

        khotab = (Khotab) getIntent().getSerializableExtra("khotba");

        adsRunnable = new ShowKhotabActivity.CountDownRunner();
        myThread = new Thread(adsRunnable);
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

        Log.e("XXX", "" + khotab.getUrlVideoDeaf());

    }

    private void setFontToText(TextView textView, int fontName ) {
//        Typeface face = Typeface.createFromAsset(getAssets(),
//                "fonts/"+fontName+".ttf");
//        textView.setTypeface(face);

        Typeface typeface = ResourcesCompat.getFont(getApplicationContext(), fontName);
        textView.setTypeface(typeface);
    }

    private void init() {
        youTubeView = findViewById(R.id.youtube_view);
        youTubeView.initialize("AIzaSyAnyP3IEprU5KiqnOzUsdCUjtx4ukx-KL4", this);

        videoView = findViewById(R.id.vvAdsVideo);

        layout_title = findViewById(R.id.layout_title);

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


        tvUrdText = findViewById(R.id.tvUrdText);
        tvFarText = findViewById(R.id.tvFarText);
        tvEnText = findViewById(R.id.tvEnText);
        tvEnTextTitel = findViewById(R.id.tvEnTextTitel);
        tvFarTextTitel = findViewById(R.id.tvFarTextTitel);
        tvUrdTextTitel = findViewById(R.id.tvUrdTextTitel);
        layout_video = findViewById(R.id.layout_video);

        tvTitle = findViewById(R.id.tvTitle);

//        Typeface face = Typeface.createFromAsset(getAssets(),
//                "fonts/khalid_art_bold.ttf");
//        tv_masged_name.setTypeface(face);

//        Typeface typeface = ResourcesCompat.getFont(getApplicationContext(), R.font.khalid_art_bold);
//        tv_masged_name.setTypeface(typeface);

        setFontToText(tv_masged_name, R.font.khalid_art_bold);
        setFontToText(tv_day, R.font.khalid_art_bold);
        setFontToText(tv_isha_name, R.font.khalid_art_bold);
        setFontToText(tv_magrb_name, R.font.khalid_art_bold);
        setFontToText(tv_asr_name, R.font.khalid_art_bold);
        setFontToText(tv_thahr_name, R.font.khalid_art_bold);
        setFontToText(tv_sun_name, R.font.khalid_art_bold);
        setFontToText(tv_fajet_name, R.font.khalid_art_bold);

        setFontToText(tvTitle, R.font.khalid_art_bold);
        setFontToText(tvUrdTextTitel, R.font.nirmala);
        setFontToText(tvUrdText, R.font.nirmala);
        setFontToText(tvEnTextTitel, R.font.tahoma);
        setFontToText(tvEnText, R.font.tahoma);

        setFontToText(tv_time, R.font.century_gothic_bold);
        setFontToText(tv_time_sec, R.font.century_gothic_bold);
        setFontToText(tv_data_hajre, R.font.century_gothic_bold);
        setFontToText(tv_data_melade, R.font.century_gothic_bold);
        setFontToText(tv_isha, R.font.century_gothic_bold);
        setFontToText(tv_magrb, R.font.century_gothic_bold);
        setFontToText(tv_asr, R.font.century_gothic_bold);
        setFontToText(tv_thahr, R.font.century_gothic_bold);
        setFontToText(tv_sun, R.font.century_gothic_bold);
        setFontToText(tv_fajr, R.font.century_gothic_bold);



        Calendar calendar = Calendar.getInstance();
        String[] days = new String[]{"الاحد", "الاثنين", "الثلاثاء", "الاربعاء", "الخميس", "الجمعة", "السبت"};
        String day = days[calendar.get(Calendar.DAY_OF_WEEK) - 1];
        tv_day.setText(day);


        if (khotab.getTitle() != null) {
            tvTitle.setText(khotab.getTitle());
        } else {
            tvTitle.setVisibility(View.GONE);
        }

        if (khotab.getBody1() != null) {
            splitedarray1 = Arrays.asList(khotab.getBody1().split("#"));
            finalSplitedarray = splitedarray1;
        } else {
            tvUrdText.setVisibility(View.GONE);
        }
        if (khotab.getBody2() != null) {
            splitedarray2 = Arrays.asList(khotab.getBody2().split("#"));
            finalSplitedarray1 = splitedarray2;
        } else {
            tvFarText.setVisibility(View.GONE);
        }
        if (khotab.getBody3() != null) {
            splitedarray3 = Arrays.asList(khotab.getBody3().split("#"));
            finalSplitedarray2 = splitedarray3;
        } else {
            tvEnText.setVisibility(View.GONE);
        }

        if (khotab.getTitle1() == null && khotab.getTitle2() == null && khotab.getTitle3() == null) {
            layout_title.setVisibility(View.GONE);
        } else {
            layout_title.setVisibility(View.VISIBLE);
        }

        if (khotab.getTitle1() != null) {
            tvUrdTextTitel.setText(khotab.getTitle1());
        }

        if (khotab.getTitle2() != null) {
            tvFarTextTitel.setText(khotab.getTitle2());
        }

        if (khotab.getTitle3() != null) {
            tvEnTextTitel.setText(khotab.getTitle3());
        }

        final int[] i = {0};

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            if (khotab.getBody1() != null) {
                                if (i[0] < finalSplitedarray.size()) {
                                    tvUrdText.setText("");
                                    tvUrdText.setText(finalSplitedarray.get(i[0]));
                                    Log.e("XXX12", "1111");
                                } else {
                                    tvUrdText.setText("ختم");
                                }
                            }
                            if (khotab.getBody2() != null) {
                                if (i[0] < finalSplitedarray1.size()) {
                                    tvFarText.setText("");
                                    tvFarText.setText(finalSplitedarray1.get(i[0]));
                                } else {
                                    tvFarText.setText("শেষ");
                                }
                                Log.e("XXX12", "22222");
                            }
                            if (khotab.getBody3() != null) {
                                if (i[0] < finalSplitedarray2.size()) {
                                    tvEnText.setText("");
                                    tvEnText.setText(finalSplitedarray2.get(i[0]));
                                } else {
                                    tvEnText.setText("End");
                                }
                                Log.e("XXX12", "3333");
                            }
                            i[0]++;
                        } catch (Exception e) {
                            Log.e("XXX12", e.getMessage());
                        }
                    }

                    ;
                });
            }
        }, 0, khotab.getTextShowTime() * 1000);//1000 is a Refreshing Time (1second)

    }

    private void setData() {

        tv_masged_name.setText(Pref.getValue(getApplicationContext(), Constants.PREF_MASGED_NAME, "اسم المسجد"));

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
        MainActivity.isOpenKhotabActivity = false;
    }

    @Override
    public void onBackPressed() {
        finish();
        if (myThread.isAlive()) {
            myThread.interrupt();
        }
        super.onBackPressed();
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer player, boolean wasRestored) {
//            khotab = new Khotab();
//            khotab.setUrlVideoDeaf("https://youtu.be/EeCn9FN94RU");
        //player.loadVideo("EeCn9FN94RU");
        if (khotab.getUrlVideoDeaf() != null) {
            if (khotab.getUrlVideoDeaf().contains("youtu")) {
                layout_video.setVisibility(View.VISIBLE);
                youTubeView.setVisibility(View.VISIBLE);
                try {
                    String id = null;
                    String prefix = "https://youtu.be/";
                    String prefix1 = "http://youtu.be/";
                    if (khotab.getUrlVideoDeaf().contains(prefix) || khotab.getUrlVideoDeaf().contains(prefix1)) {
                        id = khotab.getUrlVideoDeaf().split("youtu.be/")[1];
                    } else id = extractYoutubeId(khotab.getUrlVideoDeaf());
                    player.loadVideo(id);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            } else if (!khotab.getUrlVideoDeaf().isEmpty()) {
                layout_video.setVisibility(View.VISIBLE);
                videoView.setVisibility(View.VISIBLE);
                try {
                    File dirPath = Environment.getExternalStoragePublicDirectory(
                            Environment.DIRECTORY_DOWNLOADS + khotab.getUrlVideoDeaf());

                    Uri uri = Uri.parse(String.valueOf(dirPath));
                    videoView.setVideoURI(uri);
                    videoView.start();
                } catch (Exception e) {
                    Log.e("XXX", e.getLocalizedMessage());
                }
            } else {
                layout_video.setVisibility(View.GONE);
                youTubeView.setVisibility(View.GONE);
                videoView.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult errorReason) {
        if (errorReason.isUserRecoverableError()) {
            errorReason.getErrorDialog(this, RECOVERY_REQUEST).show();
        } else {
            String error = errorReason.toString();
            Toast.makeText(this, error, Toast.LENGTH_LONG).show();
        }
    }

    @SuppressLint("MissingSuperCall")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RECOVERY_REQUEST) {
            // Retry initialization if user performed a recovery action
           getYouTubePlayerProvider().initialize("AIzaSyAnyP3IEprU5KiqnOzUsdCUjtx4ukx-KL4", this);
        }
    }

    public YouTubePlayerView getYouTubePlayerProvider() {
        return youTubeView;
    }

    public String extractYoutubeId(String url) throws MalformedURLException {
        String query = new URL(url).getQuery();
        String[] param = query.split("&");
        String id = null;
        for (String row : param) {
            String[] param1 = row.split("=");
            if (param1[0].equals("v")) {
                id = param1[1];
            }
        }
        return id;
    }
}