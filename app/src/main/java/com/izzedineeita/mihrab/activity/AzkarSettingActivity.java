package com.izzedineeita.mihrab.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.NumberPicker;

import com.izzedineeita.mihrab.R;
import com.izzedineeita.mihrab.constants.Constants;
import com.izzedineeita.mihrab.utils.Pref;

public class AzkarSettingActivity extends AppCompatActivity {

    private Activity activity;
    private NumberPicker np_fajer, np_sunrise, np_thahr, np_asr, np_mg, np_ish;
    private CheckBox cb_fajer, cb_thahr, cb_asr, cb_magrb, cb_isha;

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
        setContentView(R.layout.activity_azkar_setting);

        activity = this;

        init();
    }

    private void init() {

        ImageView iv_back = findViewById(R.id.iv_back);
        iv_back.setOnClickListener(view -> finish());

        np_fajer = findViewById(R.id.np_fajer);
        np_sunrise = findViewById(R.id.np_sunrise);
        np_thahr = findViewById(R.id.np_thahr);
        np_asr = findViewById(R.id.np_asr);
        np_mg = findViewById(R.id.np_mg);
        np_ish = findViewById(R.id.np_ish);


        np_fajer.setMinValue(1);
        np_fajer.setMaxValue(20);
        np_sunrise.setMinValue(1);
        np_sunrise.setMaxValue(20);
        np_thahr.setMinValue(1);
        np_thahr.setMaxValue(20);
        np_asr.setMinValue(1);
        np_asr.setMaxValue(20);
        np_mg.setMinValue(1);
        np_mg.setMaxValue(20);
        np_ish.setMinValue(1);
        np_ish.setMaxValue(20);


        np_fajer.setValue(Pref.getValue(getApplicationContext(), Constants.PREF_FAJR_SHOW_AZKAR_TIME, 10));
        np_sunrise.setValue(Pref.getValue(getApplicationContext(), Constants.PREF_SUNRISE_SHOW_AZKAR_TIME, 10));
        np_thahr.setValue(Pref.getValue(getApplicationContext(), Constants.PREF_DHOHR_SHOW_AZKAR_TIME, 10));
        np_asr.setValue(Pref.getValue(getApplicationContext(), Constants.PREF_ASR_SHOW_AZKAR_TIME, 10));
        np_mg.setValue(Pref.getValue(getApplicationContext(), Constants.PREF_MAGHRIB_SHOW_AZKAR_TIME, 10));
        np_ish.setValue(Pref.getValue(getApplicationContext(), Constants.PREF_ISHA_SHOW_AZKAR_TIME, 10));

        cb_fajer = findViewById(R.id.cb_fajer);
        cb_thahr = findViewById(R.id.cb_thahr);
        cb_asr = findViewById(R.id.cb_asr);
        cb_magrb = findViewById(R.id.cb_magrb);
        cb_isha = findViewById(R.id.cb_isha);

        cb_fajer.setChecked(Pref.getValue(activity, Constants.PREF_FAJR_SHOW_ALKHUSHUE, true));
        cb_thahr.setChecked(Pref.getValue(activity, Constants.PREF_DHOHR_SHOW_ALKHUSHUE, true));
        cb_asr.setChecked(Pref.getValue(activity, Constants.PREF_ASR_SHOW_ALKHUSHUE, true));
        cb_magrb.setChecked(Pref.getValue(activity, Constants.PREF_MAGHRIB_SHOW_ALKHUSHUE, true));
        cb_isha.setChecked(Pref.getValue(activity, Constants.PREF_ISHA_SHOW_ALKHUSHUE, true));

        findViewById(R.id.btn_save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Pref.setValue(activity, Constants.PREF_FAJR_SHOW_ALKHUSHUE, cb_fajer.isChecked());
                Pref.setValue(activity, Constants.PREF_DHOHR_SHOW_ALKHUSHUE, cb_thahr.isChecked());
                Pref.setValue(activity, Constants.PREF_ASR_SHOW_ALKHUSHUE, cb_asr.isChecked());
                Pref.setValue(activity, Constants.PREF_MAGHRIB_SHOW_ALKHUSHUE, cb_magrb.isChecked());
                Pref.setValue(activity, Constants.PREF_ISHA_SHOW_ALKHUSHUE, cb_isha.isChecked());
            }
        });

        np_fajer.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                int valuePicker1 = np_fajer.getValue();
                Pref.setValue(getApplicationContext(), Constants.PREF_FAJR_SHOW_AZKAR_TIME, valuePicker1);
            }
        });
        np_sunrise.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                int valuePicker1 = np_sunrise.getValue();
                Pref.setValue(getApplicationContext(), Constants.PREF_SUNRISE_SHOW_AZKAR_TIME, valuePicker1);
            }
        });
        np_thahr.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                int valuePicker1 = np_thahr.getValue();
                Pref.setValue(getApplicationContext(), Constants.PREF_DHOHR_SHOW_AZKAR_TIME, valuePicker1);
            }
        });
        np_asr.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                int valuePicker1 = np_asr.getValue();
                Pref.setValue(getApplicationContext(), Constants.PREF_ASR_SHOW_AZKAR_TIME, valuePicker1);
            }
        });
        np_mg.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                int valuePicker1 = np_mg.getValue();
                Pref.setValue(getApplicationContext(), Constants.PREF_MAGHRIB_SHOW_AZKAR_TIME, valuePicker1);
            }
        });
        np_ish.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                int valuePicker1 = np_ish.getValue();
                Pref.setValue(getApplicationContext(), Constants.PREF_ISHA_SHOW_AZKAR_TIME, valuePicker1);
            }
        });
    }


}