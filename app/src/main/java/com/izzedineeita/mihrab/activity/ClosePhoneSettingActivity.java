package com.izzedineeita.mihrab.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.izzedineeita.mihrab.R;
import com.izzedineeita.mihrab.constants.Constants;
import com.izzedineeita.mihrab.database.DataBaseHelper;
import com.izzedineeita.mihrab.utils.Pref;
import com.izzedineeita.mihrab.utils.Utils;


public class ClosePhoneSettingActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView ivBack;
    private LinearLayout llCloseNotifSound, llCloseNotifSoundFajr, llCloseNotifSoundShrouk, llCloseNotifSoundDuhr, llCloseNotifSoundAsr, llCloseNotifSoundMaghrib, llCloseNotifSoundIsha, llCloseNotifSoundGomaa;
    private CheckBox cbCloseNotifSound, cbCloseNotifSoundFajr, cbCloseNotifSoundShrouk, cbCloseNotifSoundDuhr, cbCloseNotifSoundAsr, cbCloseNotifSoundMaghrib, cbCloseNotifSoundIsha, cbCloseNotifSoundGomaa;
    private View view;
    private CheckBox cbCloseNotifScreen;
    private EditText edNotifTimer;
    private EditText edArNotif;
    private EditText edEnNotif;
    private EditText edUrNotif;
    private Button btnSave;
    private Activity activity;
    private DataBaseHelper DBO;
    //    private SharedPreferences sp;
//    private SharedPreferences.Editor spedit;
    // private OptionSiteClass settings;
    //private int notifTimer;
    private String urNotif, enNotif, arNotif, notifTimer;

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
        setContentView(R.layout.activity_close_phone_setting);

        activity = this;

        arNotif = Pref.getValue(activity, Constants.PREF_CLOSE_PHONE_ALERT_ARABIC, "الرجاء إغلاق الهاتف");
        enNotif = Pref.getValue(activity, Constants.PREF_CLOSE_PHONE_ALERT_ENGLISH, "Plz close the mobile");
        urNotif = Pref.getValue(activity, Constants.PREF_CLOSE_PHONE_ALERT_URDU, "মোবাইল বন্ধ করুন");

        findViews();
        fillData();
    }

    private void findViews() {
        ivBack = findViewById(R.id.iv_back);

        llCloseNotifSound = findViewById(R.id.ll_closeNotifSound);
        cbCloseNotifSound = findViewById(R.id.cb_closeNotifSound);

        llCloseNotifSoundFajr = findViewById(R.id.ll_closeNotifSoundFajr);
        cbCloseNotifSoundFajr = findViewById(R.id.cb_closeNotifSoundFajr);

        llCloseNotifSoundShrouk = findViewById(R.id.ll_closeNotifSoundShrouk);
        cbCloseNotifSoundShrouk = findViewById(R.id.cb_closeNotifSoundShrouk);

        llCloseNotifSoundDuhr = findViewById(R.id.ll_closeNotifSoundDuhr);
        cbCloseNotifSoundDuhr = findViewById(R.id.cb_closeNotifSoundDuhr);

        llCloseNotifSoundAsr = findViewById(R.id.ll_closeNotifSoundAsr);
        cbCloseNotifSoundAsr = findViewById(R.id.cb_closeNotifSoundAsr);

        llCloseNotifSoundMaghrib = findViewById(R.id.ll_closeNotifSoundMaghrib);
        cbCloseNotifSoundMaghrib = findViewById(R.id.cb_closeNotifSoundMaghrib);

        llCloseNotifSoundIsha = findViewById(R.id.ll_closeNotifSoundIsha);
        cbCloseNotifSoundIsha = findViewById(R.id.cb_closeNotifSoundIsha);

        llCloseNotifSoundGomaa = findViewById(R.id.ll_closeNotifSoundGomaa);
        cbCloseNotifSoundGomaa = findViewById(R.id.cb_closeNotifSoundGomaa);

        view = findViewById(R.id.view);
        LinearLayout llCloseNotifScreen = findViewById(R.id.ll_closeNotifScreen);
        cbCloseNotifScreen = findViewById(R.id.cb_closeNotifScreen);
        edNotifTimer = findViewById(R.id.ed_notifTimer);
        edArNotif = findViewById(R.id.ed_arNotif);
        edEnNotif = findViewById(R.id.ed_enNotif);
        edUrNotif = findViewById(R.id.ed_urNotif);
        btnSave = findViewById(R.id.btn_save);

        btnSave.setOnClickListener(this);
        ivBack.setOnClickListener(this);

        /*
          Notification Sound Settings - Start
          */
        llCloseNotifSound.setOnClickListener(v -> {
            Boolean on = true;
            if (cbCloseNotifSound.isChecked()) {
                Pref.setValue(activity, Constants.PREF_CLOSE_PHONE_NOTIFICATION_SOUND, false);
                cbCloseNotifSound.setChecked(false);
            } else {
                cbCloseNotifSound.setChecked(true);
                Pref.setValue(activity, Constants.PREF_CLOSE_PHONE_NOTIFICATION_SOUND, true);
            }
        });
        cbCloseNotifSound.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (cbCloseNotifSound.isChecked()) {
                Pref.setValue(activity, Constants.PREF_CLOSE_PHONE_NOTIFICATION_SOUND, true);
            } else {
                Pref.setValue(activity, Constants.PREF_CLOSE_PHONE_NOTIFICATION_SOUND, false);
            }
        });

        llCloseNotifSoundFajr.setOnClickListener(v -> {
            Boolean on = true;
            if (cbCloseNotifSoundFajr.isChecked()) {
                Pref.setValue(activity, Constants.PREF_CLOSE_NOTIFICATION_SOUND_FAJR, false);
                cbCloseNotifSoundFajr.setChecked(false);
            } else {
                cbCloseNotifSoundFajr.setChecked(true);
                Pref.setValue(activity, Constants.PREF_CLOSE_NOTIFICATION_SOUND_FAJR, true);
            }
        });
        cbCloseNotifSoundFajr.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (cbCloseNotifSoundFajr.isChecked()) {
                Pref.setValue(activity, Constants.PREF_CLOSE_NOTIFICATION_SOUND_FAJR, true);
            } else {
                Pref.setValue(activity, Constants.PREF_CLOSE_NOTIFICATION_SOUND_FAJR, false);
            }
        });

        llCloseNotifSoundShrouk.setOnClickListener(v -> {
            Boolean on = true;
            if (cbCloseNotifSoundShrouk.isChecked()) {
                Pref.setValue(activity, Constants.PREF_CLOSE_NOTIFICATION_SOUND_SHROUK, false);
                cbCloseNotifSoundShrouk.setChecked(false);
            } else {
                cbCloseNotifSoundShrouk.setChecked(true);
                Pref.setValue(activity, Constants.PREF_CLOSE_NOTIFICATION_SOUND_SHROUK, true);
            }
        });
        cbCloseNotifSoundShrouk.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (cbCloseNotifSoundShrouk.isChecked()) {
                Pref.setValue(activity, Constants.PREF_CLOSE_NOTIFICATION_SOUND_SHROUK, true);
            } else {
                Pref.setValue(activity, Constants.PREF_CLOSE_NOTIFICATION_SOUND_SHROUK, false);
            }
        });

        llCloseNotifSoundDuhr.setOnClickListener(v -> {
            Boolean on = true;
            if (cbCloseNotifSoundDuhr.isChecked()) {
                Pref.setValue(activity, Constants.PREF_CLOSE_NOTIFICATION_SOUND_DUHR, false);
                cbCloseNotifSoundDuhr.setChecked(false);
            } else {
                cbCloseNotifSoundDuhr.setChecked(true);
                Pref.setValue(activity, Constants.PREF_CLOSE_NOTIFICATION_SOUND_DUHR, true);
            }
        });
        cbCloseNotifSoundDuhr.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (cbCloseNotifSoundDuhr.isChecked()) {
                Pref.setValue(activity, Constants.PREF_CLOSE_NOTIFICATION_SOUND_DUHR, true);
            } else {
                Pref.setValue(activity, Constants.PREF_CLOSE_NOTIFICATION_SOUND_DUHR, false);
            }
        });

        llCloseNotifSoundAsr.setOnClickListener(v -> {
            Boolean on = true;
            if (cbCloseNotifSoundAsr.isChecked()) {
                Pref.setValue(activity, Constants.PREF_CLOSE_NOTIFICATION_SOUND_ASR, false);
                cbCloseNotifSoundAsr.setChecked(false);
            } else {
                cbCloseNotifSoundAsr.setChecked(true);
                Pref.setValue(activity, Constants.PREF_CLOSE_NOTIFICATION_SOUND_ASR, true);
            }
        });
        cbCloseNotifSoundAsr.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (cbCloseNotifSoundAsr.isChecked()) {
                Pref.setValue(activity, Constants.PREF_CLOSE_NOTIFICATION_SOUND_ASR, true);
            } else {
                Pref.setValue(activity, Constants.PREF_CLOSE_NOTIFICATION_SOUND_ASR, false);
            }
        });

        llCloseNotifSoundMaghrib.setOnClickListener(v -> {
            Boolean on = true;
            if (cbCloseNotifSoundMaghrib.isChecked()) {
                Pref.setValue(activity, Constants.PREF_CLOSE_NOTIFICATION_SOUND_MAGHRIB, false);
                cbCloseNotifSoundMaghrib.setChecked(false);
            } else {
                cbCloseNotifSoundMaghrib.setChecked(true);
                Pref.setValue(activity, Constants.PREF_CLOSE_NOTIFICATION_SOUND_MAGHRIB, true);
            }
        });
        cbCloseNotifSoundMaghrib.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (cbCloseNotifSoundMaghrib.isChecked()) {
                Pref.setValue(activity, Constants.PREF_CLOSE_NOTIFICATION_SOUND_MAGHRIB, true);
            } else {
                Pref.setValue(activity, Constants.PREF_CLOSE_NOTIFICATION_SOUND_MAGHRIB, false);
            }
        });

        llCloseNotifSoundIsha.setOnClickListener(v -> {
            Boolean on = true;
            if (cbCloseNotifSoundIsha.isChecked()) {
                Pref.setValue(activity, Constants.PREF_CLOSE_NOTIFICATION_SOUND_ISHA, false);
                cbCloseNotifSoundIsha.setChecked(false);
            } else {
                cbCloseNotifSoundIsha.setChecked(true);
                Pref.setValue(activity, Constants.PREF_CLOSE_NOTIFICATION_SOUND_ISHA, true);
            }
        });
        cbCloseNotifSoundIsha.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (cbCloseNotifSoundIsha.isChecked()) {
                Pref.setValue(activity, Constants.PREF_CLOSE_NOTIFICATION_SOUND_ISHA, true);
            } else {
                Pref.setValue(activity, Constants.PREF_CLOSE_NOTIFICATION_SOUND_ISHA, false);
            }
        });

        llCloseNotifSoundGomaa.setOnClickListener(v -> {
            Boolean on = true;
            if (cbCloseNotifSoundGomaa.isChecked()) {
                Pref.setValue(activity, Constants.PREF_CLOSE_NOTIFICATION_SOUND_GOMAA, false);
                cbCloseNotifSoundGomaa.setChecked(false);
            } else {
                cbCloseNotifSoundGomaa.setChecked(true);
                Pref.setValue(activity, Constants.PREF_CLOSE_NOTIFICATION_SOUND_GOMAA, true);
            }
        });
        cbCloseNotifSoundGomaa.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (cbCloseNotifSoundGomaa.isChecked()) {
                Pref.setValue(activity, Constants.PREF_CLOSE_NOTIFICATION_SOUND_GOMAA, true);
            } else {
                Pref.setValue(activity, Constants.PREF_CLOSE_NOTIFICATION_SOUND_GOMAA, false);
            }
        });

        //  Notification Screen Settings
        cbCloseNotifScreen.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (cbCloseNotifScreen.isChecked()) {
                Pref.setValue(activity, Constants.PREF_CLOSE_NOTIFICATION_SCREEN, true);
                view.setVisibility(View.VISIBLE);
                llCloseNotifSound.setVisibility(View.VISIBLE);
            } else {
                Pref.setValue(activity, Constants.PREF_CLOSE_NOTIFICATION_SCREEN, false);
                Pref.setValue(activity, "close_voice", false);
                cbCloseNotifSound.setChecked(false);
                llCloseNotifSound.setVisibility(View.GONE);
                view.setVisibility(View.GONE);
            }
        });
    }

    private void fillData() {
        edNotifTimer.setText(Pref.getValue(activity, Constants.PREF_CLOSE_PHONE_ALERT_SHOW_BEFORE_IQAMH, "59"));
        edUrNotif.setText(urNotif);
        edArNotif.setText(arNotif);
        edEnNotif.setText(enNotif);

        if (Pref.getValue(activity, Constants.PREF_CLOSE_PHONE_NOTIFICATION_SOUND, true)) {
            cbCloseNotifSound.setChecked(true);
        } else {
            cbCloseNotifSound.setChecked(false);
        }
        if (Pref.getValue(activity, Constants.PREF_CLOSE_NOTIFICATION_SOUND_FAJR, true)) {
            cbCloseNotifSoundFajr.setChecked(true);
        } else {
            cbCloseNotifSoundFajr.setChecked(false);
        }
        if (Pref.getValue(activity, Constants.PREF_CLOSE_NOTIFICATION_SOUND_SHROUK, true)) {
            cbCloseNotifSoundShrouk.setChecked(true);
        } else {
            cbCloseNotifSoundShrouk.setChecked(false);
        }
        if (Pref.getValue(activity, Constants.PREF_CLOSE_NOTIFICATION_SOUND_DUHR, true)) {
            cbCloseNotifSoundDuhr.setChecked(true);
        } else {
            cbCloseNotifSoundDuhr.setChecked(false);
        }
        if (Pref.getValue(activity, Constants.PREF_CLOSE_NOTIFICATION_SOUND_ASR, true)) {
            cbCloseNotifSoundAsr.setChecked(true);
        } else {
            cbCloseNotifSoundAsr.setChecked(false);
        }
        if (Pref.getValue(activity, Constants.PREF_CLOSE_NOTIFICATION_SOUND_MAGHRIB, true)) {
            cbCloseNotifSoundMaghrib.setChecked(true);
        } else {
            cbCloseNotifSoundMaghrib.setChecked(false);
        }
        if (Pref.getValue(activity, Constants.PREF_CLOSE_NOTIFICATION_SOUND_ISHA, true)) {
            cbCloseNotifSoundIsha.setChecked(true);
        } else {
            cbCloseNotifSoundIsha.setChecked(false);
        }
        if (Pref.getValue(activity, Constants.PREF_CLOSE_NOTIFICATION_SOUND_GOMAA, true)) {
            cbCloseNotifSoundGomaa.setChecked(true);
        } else {
            cbCloseNotifSoundGomaa.setChecked(false);
        }

        if (Pref.getValue(activity, Constants.PREF_CLOSE_NOTIFICATION_SCREEN, true)) {
            cbCloseNotifScreen.setChecked(true);
        } else {
            cbCloseNotifScreen.setChecked(false);
        }
    }

    @Override
    public void onClick(View v) {
        if (v == btnSave) {
            Pref.setValue(activity, Constants.PREF_CLOSE_PHONE_ALERT_ARABIC, edArNotif.getText().toString().trim());
            Pref.setValue(activity, Constants.PREF_CLOSE_PHONE_ALERT_ENGLISH, edEnNotif.getText().toString().trim());
            Pref.setValue(activity, Constants.PREF_CLOSE_PHONE_ALERT_URDU, edUrNotif.getText().toString().trim());
            Pref.setValue(activity, Constants.PREF_CLOSE_PHONE_ALERT_SHOW_BEFORE_IQAMH, edNotifTimer.getText().toString().trim());

            arNotif = Pref.getValue(activity, Constants.PREF_CLOSE_PHONE_ALERT_ARABIC, edArNotif.getText().toString().trim());
            enNotif = Pref.getValue(activity, Constants.PREF_CLOSE_PHONE_ALERT_ENGLISH, edEnNotif.getText().toString().trim());
            urNotif = Pref.getValue(activity, Constants.PREF_CLOSE_PHONE_ALERT_URDU, edUrNotif.getText().toString().trim());
            notifTimer = Pref.getValue(activity, Constants.PREF_CLOSE_PHONE_ALERT_SHOW_BEFORE_IQAMH, edNotifTimer.getText().toString().trim());

            Utils.showCustomToast(activity, getString(R.string.success_edit));
        } else if (v == ivBack) {
            onBackPressed();
        }
    }

}