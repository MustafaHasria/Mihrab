package com.izzedineeita.mihrab.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.izzedineeita.mihrab.MainActivity;
import com.izzedineeita.mihrab.R;
import com.izzedineeita.mihrab.constants.Constants;
import com.izzedineeita.mihrab.utils.Pref;
import com.izzedineeita.mihrab.utils.Utils;

import java.util.ArrayList;
import java.util.Objects;


public class SettingActivity extends AppCompatActivity {

    private Spinner spinnerCity, spinner_them;
    private EditText editTextMasegdName, edHijriSet1;
    private TextView tv_battery, edHijriSet;
    private CardView card_betarry, card_sinser;
    private CheckBox cb_sinser, box_2;
    private int hijriDiff;
    private ArrayList<String> list;

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
        setContentView(R.layout.activity_setting);

        init();
        initSpinner();
        onClickLisener();

        int v = Pref.getValue(getApplicationContext(), Constants.PREF_CITY_POSITION_SELECTED, 90);
        if (v != 0) {
            spinnerCity.setSelection(v);
        } else {
            spinnerCity.setSelection(0);
        }

        if (theme != 0) {
            spinner_them.setSelection(theme);
        } else {
            spinner_them.setSelection(0);
        }


//        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy");
//        Calendar cal = Calendar.getInstance();
//        cal.setFirstDayOfWeek(Calendar.MONDAY);
//        cal.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);
//        //cal.add(Calendar.DAY_OF_WEEK, +7);
//        cal.add(Calendar.DAY_OF_WEEK, 0);
//
//        Log.e("XXX1", "" + sdf.format(cal.getTime()));

    }

    private void init() {

        findViewById(R.id.card_sing_out).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                FirebaseUser user = mAuth.getCurrentUser();
                DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
                assert user != null;
                mDatabase.child("Users").child(Objects.requireNonNull(user.getUid()))
                        .setValue("0").addOnCompleteListener(new OnCompleteListener() {
                            @Override
                            public void onComplete(@NonNull Task task) {
                                mAuth.signOut();
                                Pref.setValue(SettingActivity.this, Constants.PREF_IS_USER_LOGIN, false);
                                finishAffinity();
                                Intent intent = new Intent(SettingActivity.this, LoginActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                                //finish();
                            }
                        });
                // startActivity(new Intent(SettingActivity.this, AboutAppActivity.class));
            }
        });

        findViewById(R.id.card_about).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SettingActivity.this, AboutAppActivity.class));
            }
        });
        Button btnSaveHijri = findViewById(R.id.btnSaveHijri);

        hijriDiff = Pref.getValue(getApplicationContext(), Constants.PREF_HEJRY_INT, 0);
        edHijriSet = findViewById(R.id.edHijriSet);

        boolean b = Pref.getValue(getApplicationContext(), Constants.PREF_CHECK_BOX_2, false);
        box_2 = findViewById(R.id.box_2);
        box_2.setChecked(b);
        box_2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                             @Override
                                             public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                                 Pref.setValue(getApplicationContext(), Constants.PREF_CHECK_BOX_2, isChecked);
                                             }
                                         }
        );
//        if (hijriDiff == 0)
//            edHijriSet.setText(getString(R.string.noDifferance));
//        else if (hijriDiff == +1)
//            edHijriSet.setText(getString(R.string.addAday));
//        else if (hijriDiff == +2)
//            edHijriSet.setText(getString(R.string.addTwoDay));
//        else if (hijriDiff == -1)
//            edHijriSet.setText(getString(R.string.subtractADay));
//        else if (hijriDiff == -2)
//            edHijriSet.setText(getString(R.string.subtractTwoDay));
//        list = new ArrayList<>();
//        list.add(getString(R.string.noDifferance));
//        list.add(getString(R.string.addAday));
//        list.add(getString(R.string.addTwoDay));
//        list.add(getString(R.string.subtractADay));
//        list.add(getString(R.string.subtractTwoDay));

        if (hijriDiff == 0)
            edHijriSet.setText(getString(R.string.noDifferance));
        else if (hijriDiff >= 1)
            edHijriSet.setText(getString(R.string.addAday));
        else if (hijriDiff <= -1)
            edHijriSet.setText(getString(R.string.subtractADay));

        list = new ArrayList<>();
        list.add(getString(R.string.noDifferance));
        list.add(getString(R.string.addAday));
        list.add(getString(R.string.subtractADay));

        edHijriSet.setOnClickListener(v -> initiat(list));

        btnSaveHijri.setOnClickListener(v -> {
            Pref.setValue(getApplicationContext(), Constants.PREF_HEJRY_INT, hijriDiff);
            if (!edHijriSet1.getText().toString().trim().isEmpty()) {
                int h = Integer.parseInt(edHijriSet1.getText().toString().trim());
                Pref.setValue(getApplicationContext(), Constants.PREF_HEJRY_INT1, h);
            }
            Utils.showCustomToast(getApplicationContext(), getString(R.string.success_edit));
//            if (sp.getInt("priority", 0) == 1) {
//                if (Utils.isOnline(activity)) {
//                    saveChanges();
//                } else {
//                    Utils.showCustomToast(activity, getString(R.string.no_internet));
//                }
//            } else {
//                DBO.insertSettings(settings);
//                spedit.putInt("hijriDiff", settings.getDateHijri()).commit();
//                Utils.showCustomToast(activity, getString(R.string.success_edit));
//            }
        });

        spinnerCity = findViewById(R.id.spinner_city);
        spinner_them = findViewById(R.id.spinner_them);


        edHijriSet1 = findViewById(R.id.edHijriSet1);

        edHijriSet1.setText("" + Pref.getValue(getApplicationContext(), Constants.PREF_HEJRY_INT1, 0));
        tv_battery = findViewById(R.id.tv_battery);
        tv_battery.setText("درجة شحن بطارية الحساس % " + MainActivity.BATTERY);

        card_betarry = findViewById(R.id.card_betarry);
        card_sinser = findViewById(R.id.card_sinser);
        cb_sinser = findViewById(R.id.cb_sinser);

        cb_sinser.setChecked(Pref.getValue(getApplicationContext(), Constants.PREF_SINSER_SHOW, false));
        if (Pref.getValue(getApplicationContext(), Constants.PREF_SINSER_SHOW, false)) {
            card_betarry.setVisibility(View.VISIBLE);
        } else {
            card_betarry.setVisibility(View.GONE);
        }
        cb_sinser.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Pref.setValue(getApplicationContext(), Constants.PREF_SINSER_SHOW, true);
                    card_betarry.setVisibility(View.VISIBLE);
                } else {
                    Pref.setValue(getApplicationContext(), Constants.PREF_SINSER_SHOW, false);
                    card_betarry.setVisibility(View.GONE);
                    cb_sinser.setChecked(false);
                }

            }
        });


        LinearLayout ll_NewAds = findViewById(R.id.ll_NewAds);
        LinearLayout ll_azkar = findViewById(R.id.ll_azkar);
        LinearLayout ll_close_phone = findViewById(R.id.ll_close_phone);
        LinearLayout ll_kateb_show = findViewById(R.id.ll_kateb_show);
        LinearLayout ll_kateb = findViewById(R.id.ll_kateb);
        LinearLayout ll_sound = findViewById(R.id.ll_sound);
        LinearLayout ll_alkhushue_show = findViewById(R.id.ll_alkhushue_show);
        LinearLayout ll_adv = findViewById(R.id.ll_adv);

        ll_adv.setOnClickListener(v -> startActivity(new Intent(SettingActivity.this, NewsActivity.class)));

        ll_sound.setOnClickListener(v -> startActivity(new Intent(SettingActivity.this, SoundSettingsActivity.class)));


        //ll_azkar.setOnClickListener(v -> startActivity(new Intent(SettingActivity.this, AzkarActivity.class)));
        ll_azkar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    startActivity(new Intent(SettingActivity.this, AzkarSettingActivity.class));
                } catch (Exception e) {
                    Log.e("XXX", e.getLocalizedMessage());
                }
            }
        });

        ll_NewAds.setOnClickListener(v -> {
            try {
                startActivity(new Intent(SettingActivity.this, AdsActivity.class));
            } catch (Exception e) {
                Log.e("XXX", e.getLocalizedMessage());
            }
        });

        ll_close_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    startActivity(new Intent(SettingActivity.this, ClosePhoneSettingActivity.class));
                } catch (Exception e) {
                    Log.e("XXX", e.getLocalizedMessage());
                }
            }
        });

//        ll_azkar.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                try {
//                    //startActivity(new Intent(SettingActivity.this, AzkarActivity.class));
//                    startActivity(new Intent(SettingActivity.this, AzkarSettingActivity.class));
//                } catch (Exception e) {
//                    Log.e("XXX", e.getLocalizedMessage());
//                }
//            }
//        });

        ll_kateb_show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    startActivity(new Intent(SettingActivity.this, KhotabSettingActivity.class));
                } catch (Exception e) {
                    Log.e("XXX", e.getLocalizedMessage());
                }
            }
        });

        ll_kateb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPeriodDialog();
            }
        });

        ll_alkhushue_show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SettingActivity.this, AlkhushueActivity.class));
            }
        });

//        spinner_number_fajr = findViewById(R.id.spinner_number_fajr);
//        spinner_number_sunrise = findViewById(R.id.spinner_number_sunrise);
//        spinner_number_dhohr = findViewById(R.id.spinner_number_dhohr);
//        spinner_number_asr = findViewById(R.id.spinner_number_asr);
//        spinner_number_maghrib = findViewById(R.id.spinner_number_maghrib);
//        spinner_number_isha = findViewById(R.id.spinner_number_isha);

        editTextMasegdName = findViewById(R.id.et_masged_name);

        editTextMasegdName.setText(Pref.getValue(getApplicationContext(), Constants.PREF_MASGED_NAME, "ادخل اسم المسجد"));
        findViewById(R.id.btn_save_masged).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String editTextValue = editTextMasegdName.getText().toString();
                if (!editTextValue.isEmpty()) {
                    Pref.setValue(getApplicationContext(), Constants.PREF_MASGED_NAME, editTextValue);
                    Toast.makeText(getApplicationContext(), "تم الحفظ", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "لم يتم ادخال المسجد", Toast.LENGTH_LONG).show();
                }
            }
        });
        findViewById(R.id.card_iqama_setting).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SettingActivity.this, IqamaSettingsActivity.class));
            }
        });
    }

    private void initSpinner() {
        int intSpinnerCity = Pref.getValue(getApplicationContext(), Constants.PREF_CITY_POSITION_SELECTED, 90);
        if (intSpinnerCity != 0) {
            spinnerCity.setSelection(intSpinnerCity);
        }
        int intSpinnerThem = Pref.getValue(getApplicationContext(), Constants.PREF_THEM_POSITION_SELECTED, 0);
        if (intSpinnerThem != 0) {
            spinner_them.setSelection(intSpinnerThem);

        }

//        int intSpinnerFajr = Pref.getValue(getApplicationContext(), Constants.PREF_IQAMH_FAJR_TIME_SELECTED, 0);
//        if (intSpinnerFajr != 0) {
//            spinner_number_fajr.setSelection(intSpinnerFajr);
//        }
//
//        int intSpinnerSunrise = Pref.getValue(getApplicationContext(), Constants.PREF_IQAMH_SUNRISE_TIME_SELECTED, 0);
//        if (intSpinnerSunrise != 0) {
//            spinner_number_sunrise.setSelection(intSpinnerSunrise);
//        }
//
//        int intSpinnerDhohr = Pref.getValue(getApplicationContext(), Constants.PREF_IQAMH_DHOHR_TIME_SELECTED, 0);
//        if (intSpinnerDhohr != 0) {
//            spinner_number_dhohr.setSelection(intSpinnerDhohr);
//        }
//
//        int intSpinnerAsr = Pref.getValue(getApplicationContext(), Constants.PREF_IQAMH_ASR_TIME_SELECTED, 0);
//        if (intSpinnerAsr != 0) {
//            spinner_number_asr.setSelection(intSpinnerAsr);
//        }
//
//        int intSpinnerMaghrib = Pref.getValue(getApplicationContext(), Constants.PREF_IQAMH_MAGHRIB_TIME_SELECTED, 0);
//        if (intSpinnerMaghrib != 0) {
//            spinner_number_maghrib.setSelection(intSpinnerMaghrib);
//        }
//
//        int intSpinnerIsha = Pref.getValue(getApplicationContext(), Constants.PREF_IQAMH_ISHA_TIME_SELECTED, 0);
//        if (intSpinnerIsha != 0) {
//            spinner_number_isha.setSelection(intSpinnerIsha);
//        }
    }

    private void onClickLisener() {
        spinnerCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {
                    Pref.setValue(getApplicationContext(), Constants.PREF_CITY_POSITION_SELECTED, position);
                }
                Log.e("XCXC", "" + position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(getApplicationContext(), "Nothin", Toast.LENGTH_LONG).show();
            }
        });

        spinner_them.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                try {
                    Pref.setValue(getApplicationContext(), Constants.PREF_THEM_POSITION_SELECTED, position);
                    switch (position) {
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
                    MainActivity.currentActivity.recreate();
                } catch (Exception e) {
                    Toast.makeText(SettingActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(getApplicationContext(), "Nothin", Toast.LENGTH_LONG).show();
            }
        });

//        spinner_number_fajr.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                if (position != 0) {
//                    Pref.setValue(getApplicationContext(), Constants.PREF_IQAMH_FAJR_TIME_SELECTED, position);
//                }
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//                Toast.makeText(getApplicationContext(), "Nothin", Toast.LENGTH_LONG).show();
//            }
//        });
//
//        spinner_number_sunrise.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                if (position != 0) {
//                    Pref.setValue(getApplicationContext(), Constants.PREF_IQAMH_SUNRISE_TIME_SELECTED, position);
//                }
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//                Toast.makeText(getApplicationContext(), "Nothin", Toast.LENGTH_LONG).show();
//            }
//        });
//
//        spinner_number_dhohr.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                if (position != 0) {
//                    Pref.setValue(getApplicationContext(), Constants.PREF_IQAMH_DHOHR_TIME_SELECTED, position);
//                }
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//                Toast.makeText(getApplicationContext(), "Nothin", Toast.LENGTH_LONG).show();
//            }
//        });
//
//        spinner_number_asr.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                if (position != 0) {
//                    Pref.setValue(getApplicationContext(), Constants.PREF_IQAMH_ASR_TIME_SELECTED, position);
//                }
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//                Toast.makeText(getApplicationContext(), "Nothin", Toast.LENGTH_LONG).show();
//            }
//        });
//
//        spinner_number_maghrib.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                if (position != 0) {
//                    Pref.setValue(getApplicationContext(), Constants.PREF_IQAMH_MAGHRIB_TIME_SELECTED, position);
//                }
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//                Toast.makeText(getApplicationContext(), "Nothin", Toast.LENGTH_LONG).show();
//            }
//        });
//
//        spinner_number_isha.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                if (position != 0) {
//                    Pref.setValue(getApplicationContext(), Constants.PREF_IQAMH_ISHA_TIME_SELECTED, position);
//                }
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//                Toast.makeText(getApplicationContext(), "Nothin", Toast.LENGTH_LONG).show();
//            }
//        });
    }

    private void showPeriodDialog() {

        @SuppressLint("InflateParams") View view = getLayoutInflater().inflate(R.layout.kateb_dialog, null);
        EditText ed_play = view.findViewById(R.id.ed_stop);
        ed_play.setHint("مدة ظهور الخطيب (دقيقة)");
        Button btn_cancel = view.findViewById(R.id.btn_cancel);
        Button btn_add = view.findViewById(R.id.btn_add);
        Button btn_show = view.findViewById(R.id.btn_show);
        Dialog sleepDialog = new Dialog(this);
        CheckBox cb_show = view.findViewById(R.id.cb_show);
        ed_play.setText(Pref.getValue(getApplicationContext(), Constants.PREF_SHOW_KATEB_TIME, ""));
        cb_show.setChecked(Pref.getValue(getApplicationContext(), Constants.PREF_SHOW_KATEB, false));
        btn_add.setOnClickListener(view1 -> {
            if (TextUtils.isEmpty(ed_play.getText().toString())) {
                ed_play.setError(getString(R.string.requiredField));
                return;
            }

            Pref.setValue(getApplicationContext(), Constants.PREF_SHOW_KATEB, cb_show.isChecked());
            Pref.setValue(getApplicationContext(), Constants.PREF_SHOW_KATEB_TIME, ed_play.getText().toString());
            if (sleepDialog.isShowing()) sleepDialog.dismiss();


        });
        btn_cancel.setOnClickListener(view12 -> {
            if (sleepDialog.isShowing()) sleepDialog.dismiss();
        });
        btn_show.setOnClickListener(view12 -> {
            PackageManager pm = getPackageManager();

            if (pm.hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
                if (sleepDialog.isShowing()) sleepDialog.dismiss();
                startActivity(new Intent(SettingActivity.this, ShowKatebActivity.class));
            } else {
                Toast.makeText(getApplicationContext(), "الكميرا غير متوفرة لهذا الجهاز", Toast.LENGTH_LONG).show();
            }
        });
        sleepDialog.setContentView(view);
        sleepDialog.show();
        sleepDialog.setCancelable(false);
        sleepDialog.setCanceledOnTouchOutside(false);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = sleepDialog.getWindow();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        if (window != null) {
            lp.copyFrom(window.getAttributes());
            window.setAttributes(lp);
        }
    }

    private void initiat(final ArrayList<String> list) {
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(this);
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.select_dialog_singlechoice
        );
        for (int i = 0; i < list.size(); i++) {
            arrayAdapter.add(list.get(i));
        }

        builderSingle.setAdapter(arrayAdapter, (dialog, which) -> {
//            if (which == 0) {
//                hijriDiff = 0;
//                edHijriSet.setText(getString(R.string.noDifferance));
//            } else if (which == 1) {
//                hijriDiff = hijriDiff+1;
//                edHijriSet.setText(getString(R.string.addAday));
//            } else if (which == 2) {
//                hijriDiff = hijriDiff + 2;
//                edHijriSet.setText(getString(R.string.addTwoDay));
//            } else if (which == 3) {
//                hijriDiff = hijriDiff -1;
//                edHijriSet.setText(getString(R.string.subtractADay));
//            } else if (which == 4) {
//                hijriDiff = hijriDiff -2;
//                edHijriSet.setText(getString(R.string.subtractTwoDay));
//            }
            if (which == 0) {
                hijriDiff = 0;
                edHijriSet.setText(getString(R.string.noDifferance));
            } else if (which == 1) {
                hijriDiff = hijriDiff + 1;
                edHijriSet.setText(getString(R.string.addAday));
            } else if (which == 2) {
                hijriDiff = hijriDiff - 1;
                edHijriSet.setText(getString(R.string.subtractADay));
            }
            Pref.setValue(getApplicationContext(), Constants.PREF_HEJRY_INT, hijriDiff);
            String hijriDiffTit = (arrayAdapter.getItem(which));
            edHijriSet.setText(hijriDiffTit);
            dialog.dismiss();
        });
        builderSingle.show();
        builderSingle.setCancelable(true);
    }
}