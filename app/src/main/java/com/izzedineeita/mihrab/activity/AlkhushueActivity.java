package com.izzedineeita.mihrab.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.NumberPicker;

import com.izzedineeita.mihrab.R;
import com.izzedineeita.mihrab.constants.Constants;
import com.izzedineeita.mihrab.utils.Pref;


public class AlkhushueActivity extends AppCompatActivity {

    private CheckBox cb_sun;
    private EditText ed_sun;
    private Activity activity;
    private NumberPicker np_fajer, np_sunrise, np_thahr, np_asr, np_mg, np_ish;
    private EditText ed_fajer, ed_thahr, ed_asr, ed_magrb, ed_isha;

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
        setContentView(R.layout.activity_alkhushue);

        activity = this;

        init();
    }

    private void init() {
        findViewById(R.id.iv_back).setOnClickListener(view -> finish());

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

        cb_sun = findViewById(R.id.cb_sun);

        ed_sun = findViewById(R.id.ed_sun);

        cb_sun.setChecked(Pref.getValue(activity, Constants.PREF_SUNRISE_SHOW_ALKHUSHUE, true));

        ed_sun.setText(Pref.getValue(activity, Constants.PREF_SUNRISE_TEXT_ALKHUSHUE, ""));

        np_fajer.setValue(Pref.getValue(getApplicationContext(), Constants.PREF_FAJR_SHOW_ALKHUSHUE_TIME, 7));
        np_sunrise.setValue(Pref.getValue(getApplicationContext(), Constants.PREF_SUNRISE_SHOW_ALKHUSHUE_TIME, 7));
        np_thahr.setValue(Pref.getValue(getApplicationContext(), Constants.PREF_DHOHR_SHOW_ALKHUSHUE_TIME, 7));
        np_asr.setValue(Pref.getValue(getApplicationContext(), Constants.PREF_ASR_SHOW_ALKHUSHUE_TIME, 7));
        np_mg.setValue(Pref.getValue(getApplicationContext(), Constants.PREF_MAGHRIB_SHOW_ALKHUSHUE_TIME, 7));
        np_ish.setValue(Pref.getValue(getApplicationContext(), Constants.PREF_ISHA_SHOW_ALKHUSHUE_TIME, 7));

        np_fajer.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                int valuePicker1 = np_fajer.getValue();
                Pref.setValue(getApplicationContext(), Constants.PREF_FAJR_SHOW_ALKHUSHUE_TIME, valuePicker1);
            }
        });
        np_sunrise.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                int valuePicker1 = np_sunrise.getValue();
                Pref.setValue(getApplicationContext(), Constants.PREF_SUNRISE_SHOW_ALKHUSHUE_TIME, valuePicker1);
            }
        });
        np_thahr.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                int valuePicker1 = np_thahr.getValue();
                Pref.setValue(getApplicationContext(), Constants.PREF_DHOHR_SHOW_ALKHUSHUE_TIME, valuePicker1);
            }
        });
        np_asr.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                int valuePicker1 = np_asr.getValue();
                Pref.setValue(getApplicationContext(), Constants.PREF_ASR_SHOW_ALKHUSHUE_TIME, valuePicker1);
            }
        });
        np_mg.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                int valuePicker1 = np_mg.getValue();
                Pref.setValue(getApplicationContext(), Constants.PREF_MAGHRIB_SHOW_ALKHUSHUE_TIME, valuePicker1);
            }
        });
        np_ish.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                int valuePicker1 = np_ish.getValue();
                Pref.setValue(getApplicationContext(), Constants.PREF_ISHA_SHOW_ALKHUSHUE_TIME, valuePicker1);
            }
        });


        ed_fajer = findViewById(R.id.ed_fajer);
        ed_thahr = findViewById(R.id.ed_thahr);
        ed_asr = findViewById(R.id.ed_asr);
        ed_magrb = findViewById(R.id.ed_magrb);
        ed_isha = findViewById(R.id.ed_isha);

        ed_fajer.setText(Pref.getValue(activity, Constants.PREF_FAJR_TEXT_ALKHUSHUE, ""));
        ed_thahr.setText(Pref.getValue(activity, Constants.PREF_DHOHR_TEXT_ALKHUSHUE, ""));
        ed_asr.setText(Pref.getValue(activity, Constants.PREF_ASR_TEXT_ALKHUSHUE, ""));
        ed_magrb.setText(Pref.getValue(activity, Constants.PREF_MAGHRIB_TEXT_ALKHUSHUE, ""));
        ed_isha.setText(Pref.getValue(activity, Constants.PREF_ISHA_TEXT_ALKHUSHUE, ""));

        findViewById(R.id.btn_save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Pref.setValue(activity, Constants.PREF_SUNRISE_SHOW_ALKHUSHUE, cb_sun.isChecked());
                Pref.setValue(activity, Constants.PREF_SUNRISE_TEXT_ALKHUSHUE, ed_sun.getText().toString());

                Pref.setValue(activity, Constants.PREF_FAJR_TEXT_ALKHUSHUE, ed_fajer.getText().toString());
                Pref.setValue(activity, Constants.PREF_DHOHR_TEXT_ALKHUSHUE, ed_thahr.getText().toString());
                Pref.setValue(activity, Constants.PREF_ASR_TEXT_ALKHUSHUE, ed_asr.getText().toString());
                Pref.setValue(activity, Constants.PREF_MAGHRIB_TEXT_ALKHUSHUE, ed_magrb.getText().toString());
                Pref.setValue(activity, Constants.PREF_ISHA_TEXT_ALKHUSHUE, ed_isha.getText().toString());
            }
        });



    }


}