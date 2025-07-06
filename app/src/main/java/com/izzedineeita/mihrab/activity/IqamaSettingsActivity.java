package com.izzedineeita.mihrab.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import androidx.annotation.IdRes;
import androidx.loader.content.CursorLoader;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TimePicker;

import com.izzedineeita.mihrab.R;
import com.izzedineeita.mihrab.constants.Constants;
import com.izzedineeita.mihrab.utils.Pref;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class IqamaSettingsActivity extends AppCompatActivity {


    private RadioGroup rg_fajer,
            rg_sunrise,
            rg_thahr,
            rg_asr,
            rg_mg,
            rg_ish;
    private RadioButton rbfRelative, rbfConstant,
            rbsRelative, rbsConstant,
            rbthRelative, rbthConstant,
            rbasrRelative, rbasrConstant,
            rbmgRelative, rbmgConstant,
            rbishRelative, rbishConstant;
    private EditText ed_fajerIq,
            ed_sunriseIq,
            ed_thahrIq,
            ed_asrIq,
            ed_mgIq,
            ed_ishIq;
    private LinearLayout llfRelative, llfConstant,
            llsRelative, llsConstant,
            llthRelative, llthConstant,
            llasrRelative, llasrConstant,
            llmgRelative, llmgConstant,
            llishRelative, llishConstant;
    private Activity activity;
    private NumberPicker np_fajer, np_fajerِAzkarP, np_fajerِAzkarApp,
            np_sunrise,
            np_thahr, np_thahrAzkarP, np_thahrAzkarApp,
            np_asr, np_asrAzkarP, np_asrAzkarApp,
            np_mg, np_mgAzkarP, np_mgAzkarApp,
            np_ish, np_ishAzkarP, np_ishAzkarApp;

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
        setContentView(R.layout.activity_iqama_settings);

        activity = this;

        init();

    }

    private void init() {

        initFajer();
        initSunrise();
        initThahr();
        initAsr();
        initMg();
        initIsh();


//        findViewById(R.id.btn_save).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//
//            }
//        });

    }

    private void initFajer() {
        /* init Fajer */

        rg_fajer = findViewById(R.id.rg_fajer);
        rbfRelative = findViewById(R.id.rbfRelative);
        rbfConstant = findViewById(R.id.rbfConstant);
        np_fajer = findViewById(R.id.np_fajer);
        ed_fajerIq = findViewById(R.id.ed_fajerIq);
        np_fajerِAzkarP = findViewById(R.id.np_fajerِAzkarP);
        np_fajerِAzkarApp = findViewById(R.id.np_fajerِAzkarApp);
        llfRelative = findViewById(R.id.llfRelative);
        llfConstant = findViewById(R.id.llfConstant);

        np_fajer.setMaxValue(50);
        np_fajer.setMinValue(1);

        np_fajerِAzkarP.setMaxValue(50);
        np_fajerِAzkarP.setMinValue(1);

        np_fajerِAzkarApp.setMaxValue(50);
        np_fajerِAzkarApp.setMinValue(1);

        /* data init Fajer */
        if (Pref.getValue(getApplicationContext(), Constants.PREF_FAJER_RELATIVE, false)) {
            llfRelative.setVisibility(View.GONE);
            llfConstant.setVisibility(View.VISIBLE);
            rbfConstant.setChecked(true);
        } else {
            llfRelative.setVisibility(View.VISIBLE);
            llfConstant.setVisibility(View.GONE);
            rbfRelative.setChecked(true);
        }

        String sIqamhFajer = Pref.getValue(getApplicationContext(), Constants.PREF_IQAMH_FAJR_CONSTANT_TIME_SELECTED, "00:00");
        ed_fajerIq.setText(sIqamhFajer);


        np_fajer.setValue(Pref.getValue(getApplicationContext(), Constants.PREF_IQAMH_FAJR_RELATIVE_TIME_SELECTED, 25));
        np_fajerِAzkarP.setValue(Pref.getValue(getApplicationContext(), Constants.PREF_FAJER_AZKAR_SHOW_IN, 10));
        np_fajerِAzkarApp.setValue(Pref.getValue(getApplicationContext(), Constants.PREF_FAJR_SHOW_ALKHUSHUE_TIME, 5));

        /* on click init Fajer */
        rg_fajer.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
                if (i == R.id.rbfConstant) {
                    llfRelative.setVisibility(View.GONE);
                    llfConstant.setVisibility(View.VISIBLE);
                    Pref.setValue(activity, Constants.PREF_FAJER_CONSTANT, false);
                    Pref.setValue(activity, Constants.PREF_FAJER_RELATIVE, true);
                    showPicker(ed_fajerIq, Constants.PREF_IQAMH_FAJR_CONSTANT_TIME_SELECTED);
                } else if (i == R.id.rbfRelative) {
                    llfRelative.setVisibility(View.VISIBLE);
                    llfConstant.setVisibility(View.GONE);
                    Pref.setValue(activity, Constants.PREF_FAJER_CONSTANT, true);
                    Pref.setValue(activity, Constants.PREF_FAJER_RELATIVE, false);
                    np_fajer.setValue(Pref.getValue(getApplicationContext(), Constants.PREF_IQAMH_FAJR_RELATIVE_TIME_SELECTED, 25));
                }
            }
        });

        llfConstant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPicker(ed_fajerIq, Constants.PREF_IQAMH_FAJR_CONSTANT_TIME_SELECTED);
            }
        });

        np_fajer.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                int valuePicker1 = np_fajer.getValue();
                Pref.setValue(getApplicationContext(), Constants.PREF_IQAMH_FAJR_RELATIVE_TIME_SELECTED, valuePicker1);
            }
        });

        np_fajerِAzkarP.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                int valuePicker1 = np_fajerِAzkarP.getValue();
                Pref.setValue(getApplicationContext(), Constants.PREF_FAJER_AZKAR_SHOW_IN, valuePicker1);
            }

        });

        np_fajerِAzkarApp.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                int valuePicker1 = np_fajerِAzkarApp.getValue();
                Pref.setValue(getApplicationContext(), Constants.PREF_FAJR_SHOW_AZKAR_TIME, valuePicker1);
            }

        });


    }

    private void initSunrise() {
        rg_sunrise = findViewById(R.id.rg_sunrise);
        rbsRelative = findViewById(R.id.rbsRelative);
        rbsConstant = findViewById(R.id.rbsConstant);
        np_sunrise = findViewById(R.id.np_sunrise);
        ed_sunriseIq = findViewById(R.id.ed_sunriseIq);
        llsRelative = findViewById(R.id.llsRelative);
        llsConstant = findViewById(R.id.llsConstant);

        np_sunrise.setMaxValue(50);
        np_sunrise.setMinValue(1);

        /* data init Sunrise */
        if (!Pref.getValue(getApplicationContext(), Constants.PREF_SUNRISE_RELATIVE, false)) {
            llsRelative.setVisibility(View.VISIBLE);
            llsConstant.setVisibility(View.GONE);
            rbsRelative.setChecked(true);
        } else {

            llsRelative.setVisibility(View.GONE);
            llsConstant.setVisibility(View.VISIBLE);
            rbsConstant.setChecked(true);
        }

        String sIqamhSunrise = Pref.getValue(getApplicationContext(), Constants.PREF_IQAMH_SUNRISE_CONSTANT_TIME_SELECTED, "00:00");
        np_sunrise.setValue(Pref.getValue(getApplicationContext(), Constants.PREF_IQAMH_SUNRISE_RELATIVE_TIME_SELECTED, 20));

        ed_sunriseIq.setText(sIqamhSunrise);

        /* on click init Sunrise */
        rg_sunrise.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
                if (i == R.id.rbsConstant) {
                    llsRelative.setVisibility(View.GONE);
                    llsConstant.setVisibility(View.VISIBLE);
                    Pref.setValue(activity, Constants.PREF_SUNRISE_CONSTANT, false);
                    Pref.setValue(activity, Constants.PREF_SUNRISE_RELATIVE, true);
                    showPicker(ed_sunriseIq, Constants.PREF_IQAMH_SUNRISE_CONSTANT_TIME_SELECTED);
                } else if (i == R.id.rbsRelative) {
                    llsRelative.setVisibility(View.VISIBLE);
                    llsConstant.setVisibility(View.GONE);
                    Pref.setValue(activity, Constants.PREF_SUNRISE_CONSTANT, true);
                    Pref.setValue(activity, Constants.PREF_SUNRISE_RELATIVE, false);
                    np_sunrise.setValue(Pref.getValue(getApplicationContext(), Constants.PREF_IQAMH_SUNRISE_RELATIVE_TIME_SELECTED, 20));
                }
            }
        });

        llsConstant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPicker(ed_sunriseIq, Constants.PREF_IQAMH_SUNRISE_CONSTANT_TIME_SELECTED);
            }
        });

        np_sunrise.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                int valuePicker1 = np_sunrise.getValue();
                Pref.setValue(getApplicationContext(), Constants.PREF_IQAMH_SUNRISE_RELATIVE_TIME_SELECTED, valuePicker1);
            }

        });
    }

    private void initThahr() {
        /* init Fajer */

        rg_thahr = findViewById(R.id.rg_Thahr);
        rbthRelative = findViewById(R.id.rbthRelative);
        rbthConstant = findViewById(R.id.rbthConstant);
        np_thahr = findViewById(R.id.np_thahr);
        ed_thahrIq = findViewById(R.id.ed_thahrIq);
        np_thahrAzkarP = findViewById(R.id.np_thahrAzkarP);
        np_thahrAzkarApp = findViewById(R.id.np_thahrAzkarApp);
        llthRelative = findViewById(R.id.llthRelative);
        llthConstant = findViewById(R.id.llthConstant);

        np_thahr.setMaxValue(50);
        np_thahr.setMinValue(1);

        np_thahrAzkarP.setMaxValue(50);
        np_thahrAzkarP.setMinValue(1);

        np_thahrAzkarApp.setMaxValue(50);
        np_thahrAzkarApp.setMinValue(1);

        /* data init Thahr*/
        if (!Pref.getValue(getApplicationContext(), Constants.PREF_DHOHR_RELATIVE, false)) {
            llthRelative.setVisibility(View.VISIBLE);
            llthConstant.setVisibility(View.GONE);
            rbthRelative.setChecked(true);
        } else {

            llthRelative.setVisibility(View.GONE);
            llthConstant.setVisibility(View.VISIBLE);
            rbthConstant.setChecked(true);
        }

        String sIqamhThahr = Pref.getValue(getApplicationContext(), Constants.PREF_IQAMH_DHOHR_CONSTANT_TIME_SELECTED, "00:00");
        ed_thahrIq.setText(sIqamhThahr);


        np_thahr.setValue(Pref.getValue(getApplicationContext(), Constants.PREF_IQAMH_DHOHR_RELATIVE_TIME_SELECTED, 20));
        np_thahrAzkarP.setValue(Pref.getValue(getApplicationContext(), Constants.PREF_DHOHR_AZKAR_SHOW_IN, 10));
        np_thahrAzkarApp.setValue(Pref.getValue(getApplicationContext(), Constants.PREF_DHOHR_SHOW_ALKHUSHUE_TIME, 5));

        /* on click init Thahr */
        rg_thahr.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
                if (i == R.id.rbthConstant) {
                    llthRelative.setVisibility(View.GONE);
                    llthConstant.setVisibility(View.VISIBLE);
                    Pref.setValue(activity, Constants.PREF_DHOHR_CONSTANT, false);
                    Pref.setValue(activity, Constants.PREF_DHOHR_RELATIVE, true);
                    showPicker(ed_thahrIq, Constants.PREF_IQAMH_DHOHR_CONSTANT_TIME_SELECTED);
                } else if (i == R.id.rbthRelative) {
                    llthRelative.setVisibility(View.VISIBLE);
                    llthConstant.setVisibility(View.GONE);
                    Pref.setValue(activity, Constants.PREF_DHOHR_CONSTANT, true);
                    Pref.setValue(activity, Constants.PREF_DHOHR_RELATIVE, false);
                    np_thahr.setValue(Pref.getValue(getApplicationContext(), Constants.PREF_IQAMH_DHOHR_RELATIVE_TIME_SELECTED, 20));
                }
            }
        });

        llthConstant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //   showPicker(ed_thahrIq, Constants.PREF_IQAMH_DHOHR_CONSTANT_TIME_SELECTED);
            }
        });

        np_thahr.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                int valuePicker1 = np_thahr.getValue();
                Pref.setValue(getApplicationContext(), Constants.PREF_IQAMH_DHOHR_RELATIVE_TIME_SELECTED, valuePicker1);
            }

        });

        np_thahrAzkarP.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                int valuePicker1 = np_thahrAzkarP.getValue();
                Pref.setValue(getApplicationContext(), Constants.PREF_DHOHR_AZKAR_SHOW_IN, valuePicker1);
            }

        });

        np_thahrAzkarApp.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                int valuePicker1 = np_thahrAzkarApp.getValue();
                Pref.setValue(getApplicationContext(), Constants.PREF_DHOHR_SHOW_AZKAR_TIME, valuePicker1);
            }

        });


    }

    private void initAsr() {
        /* init Asr */

        rg_asr = findViewById(R.id.rg_Asr);
        rbasrRelative = findViewById(R.id.rbasrRelative);
        rbasrConstant = findViewById(R.id.rbasrConstant);
        np_asr = findViewById(R.id.np_asr);
        ed_asrIq = findViewById(R.id.ed_asrIq);
        np_asrAzkarP = findViewById(R.id.np_asrAzkarP);
        np_asrAzkarApp = findViewById(R.id.np_asrAzkarApp);
        llasrRelative = findViewById(R.id.llasrRelative);
        llasrConstant = findViewById(R.id.llasrConstant);

        np_asr.setMaxValue(50);
        np_asr.setMinValue(1);

        np_asrAzkarP.setMaxValue(50);
        np_asrAzkarP.setMinValue(1);

        np_asrAzkarApp.setMaxValue(50);
        np_asrAzkarApp.setMinValue(1);

        /* data init Asr*/
        if (Pref.getValue(getApplicationContext(), Constants.PREF_ASR_RELATIVE, false)) {
            llasrRelative.setVisibility(View.GONE);
            llasrConstant.setVisibility(View.VISIBLE);
            rbasrConstant.setChecked(true);
        } else {
            llasrRelative.setVisibility(View.VISIBLE);
            llasrConstant.setVisibility(View.GONE);
            rbasrRelative.setChecked(true);
        }

        String sIqamhAsr = Pref.getValue(getApplicationContext(), Constants.PREF_IQAMH_ASR_CONSTANT_TIME_SELECTED, "00:00");
        ed_asrIq.setText(sIqamhAsr);


        np_asr.setValue(Pref.getValue(getApplicationContext(), Constants.PREF_IQAMH_ASR_RELATIVE_TIME_SELECTED, 20));
        np_asrAzkarP.setValue(Pref.getValue(getApplicationContext(), Constants.PREF_ASR_AZKAR_SHOW_IN, 10));
        np_asrAzkarApp.setValue(Pref.getValue(getApplicationContext(), Constants.PREF_ASR_SHOW_ALKHUSHUE_TIME, 5));

        /* on click init Asr */
        rg_asr.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
                if (i == R.id.rbasrConstant) {
                    llasrRelative.setVisibility(View.GONE);
                    llasrConstant.setVisibility(View.VISIBLE);
                    Pref.setValue(activity, Constants.PREF_ASR_CONSTANT, false);
                    Pref.setValue(activity, Constants.PREF_ASR_RELATIVE, true);
                    showPicker(ed_asrIq, Constants.PREF_IQAMH_ASR_CONSTANT_TIME_SELECTED);
                } else if (i == R.id.rbasrRelative) {
                    llasrRelative.setVisibility(View.VISIBLE);
                    llasrConstant.setVisibility(View.GONE);
                    Pref.setValue(activity, Constants.PREF_ASR_CONSTANT, true);
                    Pref.setValue(activity, Constants.PREF_ASR_RELATIVE, false);
                    np_asr.setValue(Pref.getValue(getApplicationContext(), Constants.PREF_IQAMH_ASR_RELATIVE_TIME_SELECTED, 20));
                }
            }
        });

        llasrConstant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPicker(ed_asrIq, Constants.PREF_IQAMH_ASR_CONSTANT_TIME_SELECTED);
            }
        });

        np_asr.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                int valuePicker1 = np_asr.getValue();
                Pref.setValue(getApplicationContext(), Constants.PREF_IQAMH_ASR_RELATIVE_TIME_SELECTED, valuePicker1);
            }

        });

        np_asrAzkarP.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                int valuePicker1 = np_asrAzkarP.getValue();
                Pref.setValue(getApplicationContext(), Constants.PREF_ASR_AZKAR_SHOW_IN, valuePicker1);
            }

        });

        np_asrAzkarApp.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                int valuePicker1 = np_asrAzkarApp.getValue();
                Pref.setValue(getApplicationContext(), Constants.PREF_ASR_SHOW_AZKAR_TIME, valuePicker1);
            }

        });


    }

    private void initMg() {
        /* init MAGHRIB */

        rg_mg = findViewById(R.id.rg_Mg);
        rbmgRelative = findViewById(R.id.rbmgRelative);
        rbmgConstant = findViewById(R.id.rbmgConstant);
        np_mg = findViewById(R.id.np_mg);
        ed_mgIq = findViewById(R.id.ed_mgIq);
        np_mgAzkarP = findViewById(R.id.np_mgAzkarP);
        np_mgAzkarApp = findViewById(R.id.np_mgAzkarApp);
        llmgRelative = findViewById(R.id.llmgRelative);
        llmgConstant = findViewById(R.id.llmgConstant);

        np_mg.setMaxValue(50);
        np_mg.setMinValue(1);

        np_mgAzkarP.setMaxValue(50);
        np_mgAzkarP.setMinValue(1);

        np_mgAzkarApp.setMaxValue(50);
        np_mgAzkarApp.setMinValue(1);

        /* data init MAGHRIB*/
        if (Pref.getValue(getApplicationContext(), Constants.PREF_MAGHRIB_RELATIVE, false)) {
            llmgRelative.setVisibility(View.GONE);
            llmgConstant.setVisibility(View.VISIBLE);
            rbmgConstant.setChecked(true);
        } else {
            llmgRelative.setVisibility(View.VISIBLE);
            llmgConstant.setVisibility(View.GONE);
            rbmgRelative.setChecked(true);
        }

        String sIqamhMg = Pref.getValue(getApplicationContext(), Constants.PREF_IQAMH_MAGHRIB_CONSTANT_TIME_SELECTED, "00:00");
        ed_mgIq.setText(sIqamhMg);


        np_mg.setValue(Pref.getValue(getApplicationContext(), Constants.PREF_IQAMH_MAGHRIB_RELATIVE_TIME_SELECTED, 10));
        np_mgAzkarP.setValue(Pref.getValue(getApplicationContext(), Constants.PREF_MAGHRIB_AZKAR_SHOW_IN, 10));
        np_mgAzkarApp.setValue(Pref.getValue(getApplicationContext(), Constants.PREF_MAGHRIB_SHOW_ALKHUSHUE_TIME, 5));

        /* on click init MAGHRIB */
        rg_mg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
                if (i == R.id.rbmgConstant) {
                    llmgRelative.setVisibility(View.GONE);
                    llmgConstant.setVisibility(View.VISIBLE);
                    Pref.setValue(activity, Constants.PREF_MAGHRIB_CONSTANT, false);
                    Pref.setValue(activity, Constants.PREF_MAGHRIB_RELATIVE, true);
                    showPicker(ed_mgIq, Constants.PREF_IQAMH_MAGHRIB_CONSTANT_TIME_SELECTED);
                } else if (i == R.id.rbmgRelative) {
                    llmgRelative.setVisibility(View.VISIBLE);
                    llmgConstant.setVisibility(View.GONE);
                    Pref.setValue(activity, Constants.PREF_MAGHRIB_CONSTANT, true);
                    Pref.setValue(activity, Constants.PREF_MAGHRIB_RELATIVE, false);
                    np_mg.setValue(Pref.getValue(getApplicationContext(), Constants.PREF_IQAMH_MAGHRIB_RELATIVE_TIME_SELECTED, 10));
                }
            }
        });

        llmgConstant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPicker(ed_mgIq, Constants.PREF_IQAMH_MAGHRIB_CONSTANT_TIME_SELECTED);
            }
        });

        np_mg.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                int valuePicker1 = np_mg.getValue();
                Pref.setValue(getApplicationContext(), Constants.PREF_IQAMH_MAGHRIB_RELATIVE_TIME_SELECTED, valuePicker1);
            }

        });

        np_mgAzkarP.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                int valuePicker1 = np_mgAzkarP.getValue();
                Pref.setValue(getApplicationContext(), Constants.PREF_MAGHRIB_AZKAR_SHOW_IN, valuePicker1);
            }

        });

        np_mgAzkarApp.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                int valuePicker1 = np_mgAzkarApp.getValue();
                Pref.setValue(getApplicationContext(), Constants.PREF_MAGHRIB_SHOW_AZKAR_TIME, valuePicker1);
            }

        });


    }

    private void initIsh() {
        /* init ISHA */

        rg_ish = findViewById(R.id.rg_Ish);
        rbishRelative = findViewById(R.id.rbishRelative);
        rbishConstant = findViewById(R.id.rbishConstant);
        np_ish = findViewById(R.id.np_ish);
        ed_ishIq = findViewById(R.id.ed_ishIq);
        np_ishAzkarP = findViewById(R.id.np_ishAzkarP);
        np_ishAzkarApp = findViewById(R.id.np_ishAzkarApp);
        llishRelative = findViewById(R.id.llishRelative);
        llishConstant = findViewById(R.id.llishConstant);

        np_ish.setMaxValue(50);
        np_ish.setMinValue(1);

        np_ishAzkarP.setMaxValue(50);
        np_ishAzkarP.setMinValue(1);

        np_ishAzkarApp.setMaxValue(50);
        np_ishAzkarApp.setMinValue(1);

        /* data init ISHA*/
        if (Pref.getValue(getApplicationContext(), Constants.PREF_ISHA_RELATIVE, false)) {
            llishRelative.setVisibility(View.GONE);
            llishConstant.setVisibility(View.VISIBLE);
            rbishConstant.setChecked(true);
        } else {
            llishRelative.setVisibility(View.VISIBLE);
            llishConstant.setVisibility(View.GONE);
            rbishRelative.setChecked(true);
        }

        String sIqamhIsha = Pref.getValue(getApplicationContext(), Constants.PREF_IQAMH_ISHA_CONSTANT_TIME_SELECTED, "00:00");
        ed_ishIq.setText(sIqamhIsha);


        np_ish.setValue(Pref.getValue(getApplicationContext(), Constants.PREF_IQAMH_ISHA_RELATIVE_TIME_SELECTED, 20));
        np_ishAzkarP.setValue(Pref.getValue(getApplicationContext(), Constants.PREF_ISHA_AZKAR_SHOW_IN, 10));
        np_ishAzkarApp.setValue(Pref.getValue(getApplicationContext(), Constants.PREF_ISHA_SHOW_ALKHUSHUE_TIME, 5));

        /* on click init ISHA */
        rg_ish.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
                if (i == R.id.rbishConstant) {
                    llishRelative.setVisibility(View.GONE);
                    llishConstant.setVisibility(View.VISIBLE);
                    Pref.setValue(activity, Constants.PREF_ISHA_CONSTANT, false);
                    Pref.setValue(activity, Constants.PREF_ISHA_RELATIVE, true);
                    showPicker(ed_mgIq, Constants.PREF_IQAMH_ISHA_CONSTANT_TIME_SELECTED);
                } else if (i == R.id.rbishRelative) {
                    llishRelative.setVisibility(View.VISIBLE);
                    llishConstant.setVisibility(View.GONE);
                    Pref.setValue(activity, Constants.PREF_ISHA_CONSTANT, true);
                    Pref.setValue(activity, Constants.PREF_ISHA_RELATIVE, false);
                    np_ish.setValue(Pref.getValue(getApplicationContext(), Constants.PREF_IQAMH_ISHA_RELATIVE_TIME_SELECTED, 20));
                }
            }
        });

        llishConstant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPicker(ed_ishIq, Constants.PREF_IQAMH_ISHA_CONSTANT_TIME_SELECTED);
            }
        });

        np_ish.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                int valuePicker1 = np_ish.getValue();
                Pref.setValue(getApplicationContext(), Constants.PREF_IQAMH_ISHA_RELATIVE_TIME_SELECTED, valuePicker1);
            }

        });

        np_ishAzkarP.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                int valuePicker1 = np_ishAzkarP.getValue();
                Pref.setValue(getApplicationContext(), Constants.PREF_ISHA_AZKAR_SHOW_IN, valuePicker1);
            }

        });

        np_ishAzkarApp.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                int valuePicker1 = np_ishAzkarApp.getValue();
                Pref.setValue(getApplicationContext(), Constants.PREF_ISHA_SHOW_AZKAR_TIME, valuePicker1);
            }

        });


    }


    private void showPicker(final EditText editText, String PrefConstantsString) {
        Calendar c = Calendar.getInstance();
        TimePickerDialog mTimePicker;

        mTimePicker = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                String am_pm;

                Calendar datetime = Calendar.getInstance();
                datetime.set(Calendar.HOUR_OF_DAY, selectedHour);
                datetime.set(Calendar.MINUTE, selectedMinute);

                am_pm = getTime1(selectedHour, selectedMinute);

                Pref.setValue(getApplicationContext(), PrefConstantsString, getTime1(selectedHour, selectedMinute));
                editText.setText(am_pm);
            }
        }, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), false);//Yes 24 hour time
        mTimePicker.show();
    }

    public String getTime1(int hr, int min) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, hr);
        cal.set(Calendar.MINUTE, min);
        Format formatter;
        formatter = new SimpleDateFormat("hh:mmaa", Locale.ENGLISH);
        return formatter.format(cal.getTime());
    }


}