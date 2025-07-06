package com.izzedineeita.mihrab.activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.content.CursorLoader;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.FileUtils;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.izzedineeita.mihrab.R;
import com.izzedineeita.mihrab.constants.Constants;
import com.izzedineeita.mihrab.utils.Pref;

import java.awt.font.TextAttribute;
import java.io.File;
import java.io.IOException;
import java.net.URI;

public class SoundSettingsActivity extends AppCompatActivity {

    private CheckBox cb_soundF, cb_IqamaSoundF,
            cb_soundTh, cb_IqamaSoundTh,
            cb_soundAsr, cb_IqamaSoundAsr,
            cb_soundMg, cb_IqamaSoundMg,
            cb_soundIsh, cb_IqamaSoundIsh;
    private Activity activity;
    private TextView Iqama_soundF_close, Iqama_soundTh_close, Iqama_soundAsr_close,
            Iqama_soundMg_close, Iqama_soundIsh_close;
    private ImageView img_play_adan_sound, img_play_Iqama_soundF, img_play_Iqama_soundF_close,
            img_play_adan_soundTh, img_play_Iqama_soundTh, img_play_Iqama_soundTh_close,
            img_play_adan_soundAsr, img_play_Iqama_soundAsr, img_play_Iqama_soundAsr_close,
            img_play_adan_soundMg, img_play_Iqama_soundMg, img_play_Iqama_soundMg_close,
            img_play_adan_soundIsh, img_play_Iqama_soundIsh, img_play_Iqama_soundIsh_close;
    String SoundPath = null;
    private MediaPlayer mp = new MediaPlayer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
        setContentView(R.layout.activity_sound_settings);

        activity = this;

        init();
    }

    private void init() {

        img_play_adan_sound = findViewById(R.id.img_play_adan_sound);
        img_play_Iqama_soundF = findViewById(R.id.img_play_Iqama_soundF);
        img_play_Iqama_soundF_close = findViewById(R.id.img_play_Iqama_soundF_close);

        img_play_adan_soundTh = findViewById(R.id.img_play_adan_soundTh);
        img_play_Iqama_soundTh = findViewById(R.id.img_play_Iqama_soundTh);
        img_play_Iqama_soundTh_close = findViewById(R.id.img_play_Iqama_soundTh_close);

        img_play_adan_soundAsr = findViewById(R.id.img_play_adan_soundAsr);
        img_play_Iqama_soundAsr = findViewById(R.id.img_play_Iqama_soundAsr);
        img_play_Iqama_soundAsr_close = findViewById(R.id.img_play_Iqama_soundAsr_close);

        img_play_adan_soundMg = findViewById(R.id.img_play_adan_soundMg);
        img_play_Iqama_soundMg = findViewById(R.id.img_play_Iqama_soundMg);
        img_play_Iqama_soundMg_close = findViewById(R.id.img_play_Iqama_soundMg_close);

        img_play_adan_soundIsh = findViewById(R.id.img_play_adan_soundIsh);
        img_play_Iqama_soundIsh = findViewById(R.id.img_play_Iqama_soundIsh);
        img_play_Iqama_soundIsh_close = findViewById(R.id.img_play_Iqama_soundIsh_close);

        img_play_adan_sound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mp.isPlaying()) {
                    mp = new MediaPlayer();
                    String uriSound = Pref.getValue(getApplicationContext(), Constants.PREF_FAJER_SOUND_AZAN_PATH, null);
                    if (uriSound != null) {
                        if (!mp.isPlaying()) play(uriSound, img_play_adan_sound);
                    } else {
                        Toast.makeText(getApplicationContext(), "No File Selected...!", Toast.LENGTH_LONG).show();
                    }
                } else {
                    mp.stop();
                    img_play_adan_sound.setImageResource(R.drawable.ic_baseline_play_arrow_24);
                    //mp.release();
                }
            }
        });
        img_play_Iqama_soundF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mp.isPlaying()) {
                    mp = new MediaPlayer();
                    String uriSound = Pref.getValue(getApplicationContext(), Constants.PREF_FAJER_SOUND_IQAMAH_PATH, null);
                    if (uriSound != null) {
                        if (!mp.isPlaying()) play(uriSound, img_play_Iqama_soundF);
                    } else {
                        Toast.makeText(getApplicationContext(), "No File Selected...!", Toast.LENGTH_LONG).show();
                    }
                } else {
                    mp.stop();
                    img_play_Iqama_soundF.setImageResource(R.drawable.ic_baseline_play_arrow_24);
                    //mp.release();
                }
            }
        });
        img_play_Iqama_soundF_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mp.isPlaying()) {
                    mp = new MediaPlayer();
                    String uriSound = Pref.getValue(getApplicationContext(), Constants.PREF_FAJER_SOUND_CLOSE_PHONE_PATH, null);
                    if (uriSound != null) {
                        if (!mp.isPlaying()) play(uriSound, img_play_Iqama_soundF_close);
                    } else {
                        Toast.makeText(getApplicationContext(), "No File Selected...!", Toast.LENGTH_LONG).show();
                    }
                } else {
                    mp.stop();
                    img_play_Iqama_soundF_close.setImageResource(R.drawable.ic_baseline_play_arrow_24);
                    //mp.release();
                }
            }
        });

        img_play_adan_soundTh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mp.isPlaying()) {
                    mp = new MediaPlayer();
                    String uriSound = Pref.getValue(getApplicationContext(), Constants.PREF_DHOHR_SOUND_AZAN_PATH, null);
                    if (uriSound != null) {
                        if (!mp.isPlaying()) play(uriSound, img_play_adan_soundTh);
                    } else {
                        Toast.makeText(getApplicationContext(), "No File Selected...!", Toast.LENGTH_LONG).show();
                    }
                } else {
                    mp.stop();
                    img_play_adan_soundTh.setImageResource(R.drawable.ic_baseline_play_arrow_24);
                    //mp.release();
                }
            }
        });
        img_play_Iqama_soundTh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mp.isPlaying()) {
                    mp = new MediaPlayer();
                    String uriSound = Pref.getValue(getApplicationContext(), Constants.PREF_DHOHR_SOUND_IQAMAH_PATH, null);
                    if (uriSound != null) {
                        if (!mp.isPlaying()) play(uriSound, img_play_Iqama_soundTh);
                    } else {
                        Toast.makeText(getApplicationContext(), "No File Selected...!", Toast.LENGTH_LONG).show();
                    }
                } else {
                    mp.stop();
                    img_play_Iqama_soundTh.setImageResource(R.drawable.ic_baseline_play_arrow_24);
                    //mp.release();
                }
            }
        });
        img_play_Iqama_soundTh_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mp.isPlaying()) {
                    mp = new MediaPlayer();
                    String uriSound = Pref.getValue(getApplicationContext(), Constants.PREF_DHOHR_SOUND_CLOSE_PHONE_PATH, null);
                    if (uriSound != null) {
                        if (!mp.isPlaying()) play(uriSound, img_play_Iqama_soundTh_close);
                    } else {
                        Toast.makeText(getApplicationContext(), "No File Selected...!", Toast.LENGTH_LONG).show();
                    }
                } else {
                    mp.stop();
                    img_play_Iqama_soundTh_close.setImageResource(R.drawable.ic_baseline_play_arrow_24);
                    //mp.release();
                }
            }
        });

        img_play_adan_soundAsr.setOnClickListener(v -> {
            if (!mp.isPlaying()) {
                mp = new MediaPlayer();
                String uriSound = Pref.getValue(getApplicationContext(), Constants.PREF_ASR_SOUND_AZAN_PATH, null);
                if (uriSound != null) {
                    if (!mp.isPlaying()) play(uriSound, img_play_adan_soundAsr);
                } else {
                    Toast.makeText(getApplicationContext(), "No File Selected...!", Toast.LENGTH_LONG).show();
                }
            } else {
                mp.stop();
                img_play_adan_soundAsr.setImageResource(R.drawable.ic_baseline_play_arrow_24);
                //mp.release();
            }
        });
        img_play_Iqama_soundAsr.setOnClickListener(v -> {
            if (!mp.isPlaying()) {
                mp = new MediaPlayer();
                String uriSound = Pref.getValue(getApplicationContext(), Constants.PREF_ASR_SOUND_IQAMAH_PATH, null);
                if (uriSound != null) {
                    if (!mp.isPlaying()) play(uriSound, img_play_Iqama_soundAsr);
                } else {
                    Toast.makeText(getApplicationContext(), "No File Selected...!", Toast.LENGTH_LONG).show();
                }
            } else {
                mp.stop();
                img_play_Iqama_soundAsr.setImageResource(R.drawable.ic_baseline_play_arrow_24);
                //mp.release();
            }
        });
        img_play_Iqama_soundAsr_close.setOnClickListener(v -> {
            if (!mp.isPlaying()) {
                mp = new MediaPlayer();
                String uriSound = Pref.getValue(getApplicationContext(), Constants.PREF_ASR_SOUND_CLOSE_PHONE_PATH, null);
                if (uriSound != null) {
                    if (!mp.isPlaying()) play(uriSound, img_play_Iqama_soundAsr_close);
                } else {
                    Toast.makeText(getApplicationContext(), "No File Selected...!", Toast.LENGTH_LONG).show();
                }
            } else {
                mp.stop();
                img_play_Iqama_soundAsr_close.setImageResource(R.drawable.ic_baseline_play_arrow_24);
                //mp.release();
            }
        });

        img_play_adan_soundMg.setOnClickListener(v -> {
            if (!mp.isPlaying()) {
                mp = new MediaPlayer();
                String uriSound = Pref.getValue(getApplicationContext(), Constants.PREF_MAGHRIB_SOUND_AZAN_PATH, null);
                if (uriSound != null) {
                    if (!mp.isPlaying()) play(uriSound, img_play_adan_soundMg);
                } else {
                    Toast.makeText(getApplicationContext(), "No File Selected...!", Toast.LENGTH_LONG).show();
                }
            } else {
                mp.stop();
                img_play_adan_soundMg.setImageResource(R.drawable.ic_baseline_play_arrow_24);
                //mp.release();
            }
        });
        img_play_Iqama_soundMg.setOnClickListener(v -> {
            if (!mp.isPlaying()) {
                mp = new MediaPlayer();
                String uriSound = Pref.getValue(getApplicationContext(), Constants.PREF_MAGHRIB_SOUND_IQAMAH_PATH, null);
                if (uriSound != null) {
                    if (!mp.isPlaying()) play(uriSound, img_play_Iqama_soundMg);
                } else {
                    Toast.makeText(getApplicationContext(), "No File Selected...!", Toast.LENGTH_LONG).show();
                }
            } else {
                mp.stop();
                img_play_Iqama_soundMg.setImageResource(R.drawable.ic_baseline_play_arrow_24);
                //mp.release();
            }
        });
        img_play_Iqama_soundMg_close.setOnClickListener(v -> {
            if (!mp.isPlaying()) {
                mp = new MediaPlayer();
                String uriSound = Pref.getValue(getApplicationContext(), Constants.PREF_MAGHRIB_SOUND_CLOSE_PHONE_PATH, null);
                if (uriSound != null) {
                    if (!mp.isPlaying()) play(uriSound, img_play_Iqama_soundMg_close);
                } else {
                    Toast.makeText(getApplicationContext(), "No File Selected...!", Toast.LENGTH_LONG).show();
                }
            } else {
                mp.stop();
                img_play_Iqama_soundMg_close.setImageResource(R.drawable.ic_baseline_play_arrow_24);
                //mp.release();
            }
        });

        img_play_adan_soundIsh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mp.isPlaying()) {
                    mp = new MediaPlayer();
                    String uriSound = Pref.getValue(getApplicationContext(), Constants.PREF_ISHA_SOUND_AZAN_PATH, null);
                    if (uriSound != null) {
                        if (!mp.isPlaying()) play(uriSound, img_play_adan_soundIsh);
                    } else {
                        Toast.makeText(getApplicationContext(), "No File Selected...!", Toast.LENGTH_LONG).show();
                    }
                } else {
                    mp.stop();
                    img_play_adan_soundIsh.setImageResource(R.drawable.ic_baseline_play_arrow_24);
                    //mp.release();
                }
            }
        });
        img_play_Iqama_soundIsh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mp.isPlaying()) {
                    mp = new MediaPlayer();
                    String uriSound = Pref.getValue(getApplicationContext(), Constants.PREF_ISHA_SOUND_IQAMAH_PATH, null);
                    if (uriSound != null) {
                        if (!mp.isPlaying()) play(uriSound, img_play_Iqama_soundIsh);
                    } else {
                        Toast.makeText(getApplicationContext(), "No File Selected...!", Toast.LENGTH_LONG).show();
                    }
                } else {
                    mp.stop();
                    img_play_Iqama_soundIsh.setImageResource(R.drawable.ic_baseline_play_arrow_24);
                    //mp.release();
                }
            }
        });
        img_play_Iqama_soundIsh_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mp.isPlaying()) {
                    mp = new MediaPlayer();
                    String uriSound = Pref.getValue(getApplicationContext(), Constants.PREF_ISHA_SOUND_CLOSE_PHONE_PATH, null);
                    if (uriSound != null) {
                        if (!mp.isPlaying()) play(uriSound, img_play_Iqama_soundIsh_close);
                    } else {
                        Toast.makeText(getApplicationContext(), "No File Selected...!", Toast.LENGTH_LONG).show();
                    }
                } else {
                    mp.stop();
                    img_play_Iqama_soundIsh_close.setImageResource(R.drawable.ic_baseline_play_arrow_24);
                    //mp.release();
                }
            }
        });

        cb_soundF = findViewById(R.id.cb_soundF);
        cb_IqamaSoundF = findViewById(R.id.cb_IqamaSoundF);
        Iqama_soundF_close = findViewById(R.id.Iqama_soundF_close);

        cb_soundTh = findViewById(R.id.cb_soundTh);
        cb_IqamaSoundTh = findViewById(R.id.cb_IqamaSoundTh);
        Iqama_soundTh_close = findViewById(R.id.Iqama_soundTh_close);

        cb_soundAsr = findViewById(R.id.cb_soundAsr);
        cb_IqamaSoundAsr = findViewById(R.id.cb_IqamaSoundAsr);
        Iqama_soundAsr_close = findViewById(R.id.Iqama_soundAsr_close);

        cb_soundMg = findViewById(R.id.cb_soundMg);
        cb_IqamaSoundMg = findViewById(R.id.cb_IqamaSoundMg);
        Iqama_soundMg_close = findViewById(R.id.Iqama_soundMg_close);

        cb_soundIsh = findViewById(R.id.cb_soundIsh);
        cb_IqamaSoundIsh = findViewById(R.id.cb_IqamaSoundIsh);
        Iqama_soundIsh_close = findViewById(R.id.Iqama_soundIsh_close);


        cb_soundF.setChecked(Pref.getValue(activity, Constants.PREF_FAJER_SOUND_AZAN, false));
        cb_IqamaSoundF.setChecked(Pref.getValue(activity, Constants.PREF_FAJER_SOUND_IQAMAH, false));

        cb_soundTh.setChecked(Pref.getValue(activity, Constants.PREF_DHOHR_SOUND_AZAN, false));
        cb_IqamaSoundTh.setChecked(Pref.getValue(activity, Constants.PREF_DHOHR_SOUND_IQAMAH, false));

        cb_soundAsr.setChecked(Pref.getValue(activity, Constants.PREF_ASR_SOUND_AZAN, false));
        cb_IqamaSoundAsr.setChecked(Pref.getValue(activity, Constants.PREF_ASR_SOUND_IQAMAH, false));

        cb_soundMg.setChecked(Pref.getValue(activity, Constants.PREF_MAGHRIB_SOUND_AZAN, false));
        cb_IqamaSoundMg.setChecked(Pref.getValue(activity, Constants.PREF_MAGHRIB_SOUND_IQAMAH, false));

        cb_soundIsh.setChecked(Pref.getValue(activity, Constants.PREF_ISHA_SOUND_AZAN, false));
        cb_IqamaSoundIsh.setChecked(Pref.getValue(activity, Constants.PREF_ISHA_SOUND_IQAMAH, false));


        cb_soundF.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    try {
                        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                        intent.setType("audio/*");
                        startActivityForResult(intent, 1);
                    } catch (Exception e) {
                        Log.e("XXX", e.getLocalizedMessage());
                    }
                } else {
                    Pref.setValue(getApplicationContext(), Constants.PREF_FAJER_SOUND_AZAN_PATH, null);
                    Pref.setValue(getApplicationContext(), Constants.PREF_FAJER_SOUND_AZAN, false);
                    cb_soundF.setChecked(false);
                }

            }
        });
        cb_IqamaSoundF.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    try {
                        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                        intent.setType("audio/*");
                        startActivityForResult(intent, 2);
                    } catch (Exception e) {
                        Log.e("XXX", e.getLocalizedMessage());
                    }
                } else {
                    Pref.setValue(getApplicationContext(), Constants.PREF_FAJER_SOUND_IQAMAH, false);
                    cb_IqamaSoundF.setChecked(false);
                }
            }
        });
        Iqama_soundF_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                    intent.setType("audio/*");
                    startActivityForResult(intent, 11);
                } catch (Exception e) {
                    Log.e("XXX", e.getLocalizedMessage());
                }
            }
        });


        cb_soundTh.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    try {
                        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                        intent.setType("audio/*");
                        startActivityForResult(intent, 3);
                    } catch (Exception e) {
                        Log.e("XXX", e.getLocalizedMessage());
                    }
                } else {
                    Pref.setValue(getApplicationContext(), Constants.PREF_DHOHR_SOUND_AZAN, false);
                    cb_soundTh.setChecked(false);
                }

            }
        });
        cb_IqamaSoundTh.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    try {
                        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                        intent.setType("audio/*");
                        startActivityForResult(intent, 4);
                    } catch (Exception e) {
                        Log.e("XXX", e.getLocalizedMessage());
                    }
                } else {
                    Pref.setValue(getApplicationContext(), Constants.PREF_DHOHR_SOUND_IQAMAH, false);
                    cb_IqamaSoundTh.setChecked(false);
                }
            }
        });
        Iqama_soundTh_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                    intent.setType("audio/*");
                    startActivityForResult(intent, 22);
                } catch (Exception e) {
                    Log.e("XXX", e.getLocalizedMessage());
                }
            }
        });

        cb_soundAsr.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    try {
                        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                        intent.setType("audio/*");
                        startActivityForResult(intent, 5);
                    } catch (Exception e) {
                        Log.e("XXX", e.getLocalizedMessage());
                    }
                } else {
                    Pref.setValue(getApplicationContext(), Constants.PREF_ASR_SOUND_AZAN, false);
                    cb_soundAsr.setChecked(false);
                }

            }
        });
        cb_IqamaSoundAsr.setOnCheckedChangeListener((buttonView, isChecked) -> {

            if (isChecked) {
                try {
                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                    intent.setType("audio/*");
                    startActivityForResult(intent, 6);
                } catch (Exception e) {
                    Log.e("XXX", e.getLocalizedMessage());
                }
            } else {
                Pref.setValue(getApplicationContext(), Constants.PREF_ASR_SOUND_IQAMAH, false);
                cb_IqamaSoundAsr.setChecked(false);
            }
        });
        Iqama_soundAsr_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                    intent.setType("audio/*");
                    startActivityForResult(intent, 33);
                } catch (Exception e) {
                    Log.e("XXX", e.getLocalizedMessage());
                }
            }
        });


        cb_soundMg.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                try {
                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                    intent.setType("audio/*");
                    startActivityForResult(intent, 7);
                } catch (Exception e) {
                    Log.e("XXX", e.getLocalizedMessage());
                }
            } else {
                Pref.setValue(getApplicationContext(), Constants.PREF_MAGHRIB_SOUND_AZAN, false);
                cb_soundMg.setChecked(false);
            }

        });
        cb_IqamaSoundMg.setOnCheckedChangeListener((buttonView, isChecked) -> {

            if (isChecked) {
                try {
                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                    intent.setType("audio/*");
                    startActivityForResult(intent, 8);
                } catch (Exception ignored) {
                }
            } else {
                Pref.setValue(getApplicationContext(), Constants.PREF_MAGHRIB_SOUND_IQAMAH, false);
                cb_IqamaSoundMg.setChecked(false);
            }
        });
        Iqama_soundMg_close.setOnClickListener(v -> {
            try {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("audio/*");
                startActivityForResult(intent, 44);
            } catch (Exception e) {
                Log.e("XXX", e.getLocalizedMessage());
            }
        });


        cb_soundIsh.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                try {
                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                    intent.setType("audio/*");
                    startActivityForResult(intent, 9);
                } catch (Exception e) {
                    Log.e("XXX", e.getLocalizedMessage());
                }
            } else {
                Pref.setValue(getApplicationContext(), Constants.PREF_ISHA_SOUND_AZAN, false);
                cb_soundIsh.setChecked(false);
            }

        });
        cb_IqamaSoundIsh.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    try {
                        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                        intent.setType("audio/*");
                        startActivityForResult(intent, 10);
                    } catch (Exception e) {
                        Log.e("XXX", e.getLocalizedMessage());
                    }
                } else {
                    Pref.setValue(getApplicationContext(), Constants.PREF_ISHA_SOUND_IQAMAH, false);
                    cb_IqamaSoundIsh.setChecked(false);
                }
            }
        });
        Iqama_soundIsh_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                    intent.setType("audio/*");
                    startActivityForResult(intent, 55);
                } catch (Exception e) {
                    Log.e("XXX", e.getLocalizedMessage());
                }
            }
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @SuppressLint("MissingSuperCall")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        try {
            if (resultCode == RESULT_OK && requestCode == 1) {
                Uri uriSound = data.getData();
                String path;
                try {
                    Log.e("XXXAS1 ", "1212   " + getPathFromUri(getApplicationContext(), uriSound));
                    path = getPathFromUri(getApplicationContext(), uriSound);
                    Pref.setValue(getApplicationContext(), Constants.PREF_FAJER_SOUND_AZAN, true);
                    Pref.setValue(getApplicationContext(), Constants.PREF_FAJER_SOUND_AZAN_PATH, path);
                    cb_soundF.setChecked(Pref.getValue(activity, Constants.PREF_FAJER_SOUND_AZAN, true));
                    Toast.makeText(getApplicationContext(), "PATH ::  " + path, Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    Log.e("XXXAS2 ", "1212   " + uriSound.getPath());
                    path = uriSound.getPath();
                    Pref.setValue(getApplicationContext(), Constants.PREF_FAJER_SOUND_AZAN, true);
                    Pref.setValue(getApplicationContext(), Constants.PREF_FAJER_SOUND_AZAN_PATH, path);
                    cb_soundF.setChecked(Pref.getValue(activity, Constants.PREF_FAJER_SOUND_AZAN, true));
                    Toast.makeText(getApplicationContext(), "PATH catch ::  " + path, Toast.LENGTH_LONG).show();
                }



            } else if (resultCode == RESULT_OK && requestCode == 2) {
                Uri uriSound = data.getData();
                try {
                    Log.e("XXXAS1 ", "1212   " + getPathFromUri(getApplicationContext(), uriSound));
                    String path = getPathFromUri(getApplicationContext(), uriSound);
                    Pref.setValue(getApplicationContext(), Constants.PREF_FAJER_SOUND_IQAMAH, true);
                    Pref.setValue(getApplicationContext(), Constants.PREF_FAJER_SOUND_IQAMAH_PATH, path);
                    cb_IqamaSoundF.setChecked(Pref.getValue(activity, Constants.PREF_FAJER_SOUND_IQAMAH, false));

                } catch (Exception e) {
                    Log.e("XXXAS2 ", "1212   " + uriSound.getPath());
                    String path = uriSound.getPath();
                    Pref.setValue(getApplicationContext(), Constants.PREF_FAJER_SOUND_IQAMAH, true);
                    Pref.setValue(getApplicationContext(), Constants.PREF_FAJER_SOUND_IQAMAH_PATH, path);
                    cb_IqamaSoundF.setChecked(Pref.getValue(activity, Constants.PREF_FAJER_SOUND_IQAMAH, false));

                }
            }
            if (resultCode == RESULT_OK && requestCode == 3) {
                Uri uriSound = data.getData();

                try {
                    Log.e("XXXAS1 ", "1212   " + getPathFromUri(getApplicationContext(), uriSound));
                    String path = getPathFromUri(getApplicationContext(), uriSound);
                    Pref.setValue(getApplicationContext(), Constants.PREF_DHOHR_SOUND_AZAN, true);
                    Pref.setValue(getApplicationContext(), Constants.PREF_DHOHR_SOUND_AZAN_PATH, path);
                    cb_soundTh.setChecked(Pref.getValue(activity, Constants.PREF_DHOHR_SOUND_AZAN, false));

                } catch (Exception e) {
                    Log.e("XXXAS2 ", "1212   " + uriSound.getPath());
                    String path = uriSound.getPath();
                    Pref.setValue(getApplicationContext(), Constants.PREF_DHOHR_SOUND_AZAN, true);
                    Pref.setValue(getApplicationContext(), Constants.PREF_DHOHR_SOUND_AZAN_PATH, path);
                    cb_soundTh.setChecked(Pref.getValue(activity, Constants.PREF_DHOHR_SOUND_AZAN, false));

                }

            } else if (resultCode == RESULT_OK && requestCode == 4) {
                Uri uriSound = data.getData();

                try {
                    Log.e("XXXAS1 ", "1212   " + getPathFromUri(getApplicationContext(), uriSound));
                    String path = getPathFromUri(getApplicationContext(), uriSound);
                    Pref.setValue(getApplicationContext(), Constants.PREF_DHOHR_SOUND_IQAMAH, true);
                    Pref.setValue(getApplicationContext(), Constants.PREF_DHOHR_SOUND_IQAMAH_PATH, path);
                    cb_IqamaSoundTh.setChecked(Pref.getValue(activity, Constants.PREF_DHOHR_SOUND_IQAMAH, false));

                } catch (Exception e) {
                    Log.e("XXXAS2 ", "1212   " + uriSound.getPath());
                    String path = uriSound.getPath();
                    Pref.setValue(getApplicationContext(), Constants.PREF_DHOHR_SOUND_IQAMAH, true);
                    Pref.setValue(getApplicationContext(), Constants.PREF_DHOHR_SOUND_IQAMAH_PATH, path);
                    cb_IqamaSoundTh.setChecked(Pref.getValue(activity, Constants.PREF_DHOHR_SOUND_IQAMAH, false));

                }
            }
            if (resultCode == RESULT_OK && requestCode == 5) {
                Uri uriSound = data.getData();

                try {
                    Log.e("XXXAS1 ", "1212   " + getPathFromUri(getApplicationContext(), uriSound));
                    String path = getPathFromUri(getApplicationContext(), uriSound);
                    Pref.setValue(getApplicationContext(), Constants.PREF_ASR_SOUND_AZAN, true);
                    Pref.setValue(getApplicationContext(), Constants.PREF_ASR_SOUND_AZAN_PATH, path);
                    cb_soundAsr.setChecked(Pref.getValue(activity, Constants.PREF_ASR_SOUND_AZAN, false));

                } catch (Exception e) {
                    Log.e("XXXAS2 ", "1212   " + uriSound.getPath());
                    String path = uriSound.getPath();
                    Pref.setValue(getApplicationContext(), Constants.PREF_ASR_SOUND_AZAN, true);
                    Pref.setValue(getApplicationContext(), Constants.PREF_ASR_SOUND_AZAN_PATH, path);
                    cb_soundAsr.setChecked(Pref.getValue(activity, Constants.PREF_ASR_SOUND_AZAN, false));

                }
            } else if (resultCode == RESULT_OK && requestCode == 6) {
                Uri uriSound = data.getData();

                try {
                    Log.e("XXXAS1 ", "1212   " + getPathFromUri(getApplicationContext(), uriSound));
                    String path = getPathFromUri(getApplicationContext(), uriSound);
                    Pref.setValue(getApplicationContext(), Constants.PREF_ASR_SOUND_IQAMAH, true);
                    Pref.setValue(getApplicationContext(), Constants.PREF_ASR_SOUND_IQAMAH_PATH, path);
                    cb_IqamaSoundAsr.setChecked(Pref.getValue(activity, Constants.PREF_ASR_SOUND_IQAMAH, false));

                } catch (Exception e) {
                    Log.e("XXXAS2 ", "1212   " + uriSound.getPath());
                    String path = uriSound.getPath();
                    Pref.setValue(getApplicationContext(), Constants.PREF_ASR_SOUND_IQAMAH, true);
                    Pref.setValue(getApplicationContext(), Constants.PREF_ASR_SOUND_IQAMAH_PATH, path);
                    cb_IqamaSoundAsr.setChecked(Pref.getValue(activity, Constants.PREF_ASR_SOUND_IQAMAH, false));

                }
            }
            if (resultCode == RESULT_OK && requestCode == 7) {
                Uri uriSound = data.getData();

                try {
                    Log.e("XXXAS1 ", "1212   " + getPathFromUri(getApplicationContext(), uriSound));
                    String path = getPathFromUri(getApplicationContext(), uriSound);
                    Pref.setValue(getApplicationContext(), Constants.PREF_MAGHRIB_SOUND_AZAN, true);
                    Pref.setValue(getApplicationContext(), Constants.PREF_MAGHRIB_SOUND_AZAN_PATH, path);
                    cb_soundMg.setChecked(Pref.getValue(activity, Constants.PREF_MAGHRIB_SOUND_AZAN, false));

                } catch (Exception e) {
                    Log.e("XXXAS2 ", "1212   " + uriSound.getPath());
                    String path = uriSound.getPath();
                    Pref.setValue(getApplicationContext(), Constants.PREF_MAGHRIB_SOUND_AZAN, true);
                    Pref.setValue(getApplicationContext(), Constants.PREF_MAGHRIB_SOUND_AZAN_PATH, path);
                    cb_soundMg.setChecked(Pref.getValue(activity, Constants.PREF_MAGHRIB_SOUND_AZAN, false));

                }
            } else if (resultCode == RESULT_OK && requestCode == 8) {
                Uri uriSound = data.getData();

                try {
                    Log.e("XXXAS1 ", "1212   " + getPathFromUri(getApplicationContext(), uriSound));
                    String path = getPathFromUri(getApplicationContext(), uriSound);
                    Pref.setValue(getApplicationContext(), Constants.PREF_MAGHRIB_SOUND_IQAMAH, true);
                    Pref.setValue(getApplicationContext(), Constants.PREF_MAGHRIB_SOUND_IQAMAH_PATH, path);
                    cb_IqamaSoundMg.setChecked(Pref.getValue(activity, Constants.PREF_MAGHRIB_SOUND_IQAMAH, false));

                } catch (Exception e) {
                    Log.e("XXXAS2 ", "1212   " + uriSound.getPath());
                    String path = uriSound.getPath();
                    Pref.setValue(getApplicationContext(), Constants.PREF_MAGHRIB_SOUND_IQAMAH, true);
                    Pref.setValue(getApplicationContext(), Constants.PREF_MAGHRIB_SOUND_IQAMAH_PATH, path);
                    cb_IqamaSoundMg.setChecked(Pref.getValue(activity, Constants.PREF_MAGHRIB_SOUND_IQAMAH, false));

                }
            }
            if (resultCode == RESULT_OK && requestCode == 9) {
                Uri uriSound = data.getData();

                try {
                    Log.e("XXXAS1 ", "1212   " + getPathFromUri(getApplicationContext(), uriSound));
                    String path = getPathFromUri(getApplicationContext(), uriSound);
                    Pref.setValue(getApplicationContext(), Constants.PREF_ISHA_SOUND_AZAN, true);
                    Pref.setValue(getApplicationContext(), Constants.PREF_ISHA_SOUND_AZAN_PATH, path);
                    cb_soundIsh.setChecked(Pref.getValue(activity, Constants.PREF_ISHA_SOUND_AZAN, false));

                } catch (Exception e) {
                    Log.e("XXXAS2 ", "1212   " + uriSound.getPath());
                    String path = uriSound.getPath();
                    Pref.setValue(getApplicationContext(), Constants.PREF_ISHA_SOUND_AZAN, true);
                    Pref.setValue(getApplicationContext(), Constants.PREF_ISHA_SOUND_AZAN_PATH, path);
                    cb_soundIsh.setChecked(Pref.getValue(activity, Constants.PREF_ISHA_SOUND_AZAN, false));

                }
            } else if (resultCode == RESULT_OK && requestCode == 10) {
                Uri uriSound = data.getData();

                try {
                    Log.e("XXXAS1 ", "1212   " + getPathFromUri(getApplicationContext(), uriSound));
                    String path = getPathFromUri(getApplicationContext(), uriSound);
                    Pref.setValue(getApplicationContext(), Constants.PREF_ISHA_SOUND_IQAMAH, true);
                    Pref.setValue(getApplicationContext(), Constants.PREF_ISHA_SOUND_IQAMAH_PATH, path);
                    cb_IqamaSoundIsh.setChecked(Pref.getValue(activity, Constants.PREF_ISHA_SOUND_IQAMAH, false));

                } catch (Exception e) {
                    Log.e("XXXAS2 ", "1212   " + uriSound.getPath());
                    String path = uriSound.getPath();
                    Pref.setValue(getApplicationContext(), Constants.PREF_ISHA_SOUND_IQAMAH, true);
                    Pref.setValue(getApplicationContext(), Constants.PREF_ISHA_SOUND_IQAMAH_PATH, path);
                    cb_IqamaSoundIsh.setChecked(Pref.getValue(activity, Constants.PREF_ISHA_SOUND_IQAMAH, false));

                }
            } else if (resultCode == RESULT_OK && requestCode == 11) {
                Uri uriSound = data.getData();
                try {
                    Log.e("XXXAS1 ", "1212   " + getPathFromUri(getApplicationContext(), uriSound));
                    String path = getPathFromUri(getApplicationContext(), uriSound);
                    Pref.setValue(getApplicationContext(), Constants.PREF_FAJER_SOUND_CLOSE_PHONE_PATH, path);


                } catch (Exception e) {
                    Log.e("XXXAS2 ", "1212   " + uriSound.getPath());
                    String path = uriSound.getPath();
                    Pref.setValue(getApplicationContext(), Constants.PREF_FAJER_SOUND_CLOSE_PHONE_PATH, path);


                }
            } else if (resultCode == RESULT_OK && requestCode == 22) {
                Uri uriSound = data.getData();
                try {
                    Log.e("XXXAS1 ", "1212   " + getPathFromUri(getApplicationContext(), uriSound));
                    String path = getPathFromUri(getApplicationContext(), uriSound);
                    Pref.setValue(getApplicationContext(), Constants.PREF_DHOHR_SOUND_CLOSE_PHONE_PATH, path);
                } catch (Exception e) {
                    Log.e("XXXAS2 ", "1212   " + uriSound.getPath());
                    String path = uriSound.getPath();
                    Pref.setValue(getApplicationContext(), Constants.PREF_DHOHR_SOUND_CLOSE_PHONE_PATH, path);
                }
            } else if (resultCode == RESULT_OK && requestCode == 33) {
                Uri uriSound = data.getData();
                try {
                    Log.e("XXXAS1 ", "1212   " + getPathFromUri(getApplicationContext(), uriSound));
                    String path = getPathFromUri(getApplicationContext(), uriSound);
                    Pref.setValue(getApplicationContext(), Constants.PREF_ASR_SOUND_CLOSE_PHONE_PATH, path);


                } catch (Exception e) {
                    Log.e("XXXAS2 ", "1212   " + uriSound.getPath());
                    String path = uriSound.getPath();
                    Pref.setValue(getApplicationContext(), Constants.PREF_ASR_SOUND_CLOSE_PHONE_PATH, path);


                }
            } else if (resultCode == RESULT_OK && requestCode == 44) {
                Uri uriSound = data.getData();
                try {
                    Log.e("XXXAS1 ", "1212   " + getPathFromUri(getApplicationContext(), uriSound));
                    String path = getPathFromUri(getApplicationContext(), uriSound);
                    Pref.setValue(getApplicationContext(), Constants.PREF_MAGHRIB_SOUND_CLOSE_PHONE_PATH, path);


                } catch (Exception e) {
                    Log.e("XXXAS2 ", "1212   " + uriSound.getPath());
                    String path = uriSound.getPath();
                    Pref.setValue(getApplicationContext(), Constants.PREF_MAGHRIB_SOUND_CLOSE_PHONE_PATH, path);


                }
            } else if (resultCode == RESULT_OK && requestCode == 55) {
                Uri uriSound = data.getData();
                try {
                    Log.e("XXXAS1 ", "1212   " + getPathFromUri(getApplicationContext(), uriSound));
                    String path = getPathFromUri(getApplicationContext(), uriSound);
                    Pref.setValue(getApplicationContext(), Constants.PREF_ISHA_SOUND_CLOSE_PHONE_PATH, path);


                } catch (Exception e) {
                    Log.e("XXXAS2 ", "1212   " + uriSound.getPath());
                    String path = uriSound.getPath();
                    Pref.setValue(getApplicationContext(), Constants.PREF_ISHA_SOUND_CLOSE_PHONE_PATH, path);


                }
            }
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            Log.e("XXXXX ", "1212   " + e.getLocalizedMessage());
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public String getPathFromUri(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                String id = DocumentsContract.getDocumentId(uri);
                if (id.startsWith("raw:")) {
                    id = id.replaceFirst("raw:", "");
                }
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {

            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();

            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }


    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

    private String getRealPathFromURI(Uri contentUri) {
//        String[] proj = {MediaStore.Audio.Media.DATA};
//        CursorLoader loader = new CursorLoader(getApplicationContext(), contentUri, proj, null, null, null);
//        Cursor cursor = loader.loadInBackground();
//        assert cursor != null;
//        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
//        cursor.moveToFirst();
//        String result = cursor.getString(column_index);
//        cursor.close();
//        return result;
//        Cursor cursor = getContentResolver().query(contentUri, null, null, null, null);
//        cursor.moveToFirst();
//        String document_id = cursor.getString(0);
//        document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
//        cursor.close();
//
//        cursor = getContentResolver().query(
//                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
//                null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
//        cursor.moveToFirst();
//        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
//        cursor.close();
//
//
//        return path;

        String[] proj = {MediaStore.Audio.Media.DATA};
        Cursor cursor = managedQuery(contentUri, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);

    }

    private void play(String path, ImageView imageView) {

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
                    imageView.setImageResource(R.drawable.ic_baseline_stop);
                }
            });
            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    imageView.setImageResource(R.drawable.ic_baseline_play_arrow_24);
                }
            });

        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Log.e("XXX4", e.getLocalizedMessage());
            Toast.makeText(getApplicationContext(), "ERROR CAN'T PLAY :: " + e.getLocalizedMessage(), Toast.LENGTH_LONG).show();

        } catch (SecurityException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Log.e("XXX4", e.getLocalizedMessage());
            Toast.makeText(getApplicationContext(), "ERROR CAN'T PLAY :: " + e.getLocalizedMessage(), Toast.LENGTH_LONG).show();

        } catch (IllegalStateException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Log.e("XXX4", e.getLocalizedMessage());
            Toast.makeText(getApplicationContext(), "ERROR CAN'T PLAY :: " + e.getLocalizedMessage(), Toast.LENGTH_LONG).show();

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Log.e("XXX4", e.getLocalizedMessage());
            Toast.makeText(getApplicationContext(), "ERROR CAN'T PLAY :: " + e.getLocalizedMessage(), Toast.LENGTH_LONG).show();

        }
    }

    @Override
    protected void onDestroy() {
        if (mp.isPlaying()) {
            mp.stop();
        }
        super.onDestroy();
    }

}