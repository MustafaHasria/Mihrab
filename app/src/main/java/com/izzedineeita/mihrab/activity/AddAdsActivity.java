package com.izzedineeita.mihrab.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.bumptech.glide.Glide;
import com.izzedineeita.mihrab.R;
import com.izzedineeita.mihrab.constants.Constants;
import com.izzedineeita.mihrab.database.DataBaseHelper;
import com.izzedineeita.mihrab.model.Ads;
import com.izzedineeita.mihrab.model.AdsPeriods;
import com.izzedineeita.mihrab.utils.Pref;
import com.izzedineeita.mihrab.utils.Utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AddAdsActivity extends AppCompatActivity
        implements View.OnClickListener, RadioGroup.OnCheckedChangeListener {

    private static final String TAG = "AddAdsActivity";
    private Activity activity;
    private ImageView ivAdsVideoThumb, ivSelectVideo, ivSelectVideo1, ivSelectVideo2,
    //  ivAdsImg,
    ivThumb,
            ivSelectImg, ivSelectImg1, ivSelectImg2, ivSelectImg3, ivSelectImg4, ivSelectImg5, ivSelectImg6,
            iv_back;
    private RadioButton rbText, rbVideo, rbImage;
    private RelativeLayout rlImage, rlVideo, rlText;
    private EditText edAdsTitle;
    private EditText edAdsText;
    private EditText ed_start;
    private EditText ed_end;
    private EditText img_sec_show;
    private EditText edAddAppearance;
    private TextView tvSave;
    private TextView tittleA;
    private LinearLayout llAdsPeriods;
    private int REQUEST_PERMISSIONS = 100;
    private Uri selectedImage = null;
    private int VIDEO_SELECT = 2;
    private Uri videoData = null;
    private int type = 1;
    private String selectedImagePath = "";
    private String selectedImagePath1 = "";
    private String selectedImagePath2 = "";
    private String selectedImagePath3 = "";
    private String selectedImagePath4 = "";
    private String selectedImagePath5 = "";
    private String selectedImagePath6 = "";
    private String selectedVideoPath = "";
    private String selectedVideoPath1 = "";
    private String selectedVideoPath2 = "";
    private SharedPreferences sp;
    private DataBaseHelper DBO;
    private CheckBox cbSat, cbSun, cbMon, cbTue, cbWed, cbThu, cbFri;
    private CheckBox cbHidePulpitAdsBox;
    private SeekBar sbFontSize, sbMovementSpeed;
    private TextView tvFontSizeValue, tvSpeedValue;
    private boolean isConflictAds = false;
    ArrayList<Integer> checkList = new ArrayList<>();
    ArrayList<AdsPeriods> adsPeriodsList = new ArrayList<>();
    ArrayList<AdsPeriods> adsList = new ArrayList<>();
    ArrayList<String> prayerTimes = new ArrayList<>();
    private int advId = -1;
    int count = 0;
    int RESULT_LOAD_IMAGE = 1;
    int imagePigNumber = 0;
    int videoPigNumber = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        try {
            Log.d(TAG, "=== AddAdsActivity onCreate started ===");
            
            // Set orientation based on theme
            Log.d(TAG, "Setting up orientation...");
            setupOrientation();
            Log.d(TAG, "Orientation setup completed");
            
            // Set content view with error handling
            Log.d(TAG, "Inflating layout...");
            try {
                setContentView(R.layout.activity_add_ads);
                Log.d(TAG, "Layout inflated successfully");
            } catch (Exception e) {
                Log.e(TAG, "Error inflating layout: " + e.getMessage(), e);
                Toast.makeText(this, "Error loading layout: " + e.getMessage(), Toast.LENGTH_LONG).show();
                finish();
                return;
            }
            
            // Initialize activity reference
            Log.d(TAG, "Setting activity reference...");
            activity = this;
            
            // Initialize database with error handling
            Log.d(TAG, "Initializing database...");
            initializeDatabase();
            Log.d(TAG, "Database initialization completed");
            
            // Initialize shared preferences
            Log.d(TAG, "Initializing shared preferences...");
            initializeSharedPreferences();
            Log.d(TAG, "Shared preferences initialization completed");
            
            // Initialize prayer times
            Log.d(TAG, "Initializing prayer times...");
            initializePrayerTimes();
            Log.d(TAG, "Prayer times initialization completed");
            
            // Initialize UI components
            Log.d(TAG, "Initializing UI components...");
            initializeUIComponents();
            Log.d(TAG, "UI components initialization completed");
            
            // Setup click listeners
            Log.d(TAG, "Setting up click listeners...");
            setupClickListeners();
            Log.d(TAG, "Click listeners setup completed");
            
            // Request permissions
            Log.d(TAG, "Requesting permissions...");
            requestPermissions();
            Log.d(TAG, "Permission request completed");
            
            // Log final state
            Log.d(TAG, "Logging final activity state...");
            logActivityState();
            
            // Test basic functionality
            Log.d(TAG, "Running basic functionality test...");
            testBasicFunctionality();
            
            Log.d(TAG, "=== AddAdsActivity onCreate completed successfully ===");
            
        } catch (Exception e) {
            Log.e(TAG, "=== CRITICAL ERROR in onCreate: " + e.getMessage() + " ===", e);
            Toast.makeText(this, "Error initializing activity: " + e.getMessage(), Toast.LENGTH_LONG).show();
            finish();
        }
    }

    private void setupOrientation() {
        try {
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
        } catch (Exception e) {
            Log.e(TAG, "Error setting orientation: " + e.getMessage(), e);
        }
    }

    private void initializeDatabase() {
        try {
            DBO = new DataBaseHelper(activity);
            
            // Check if database is ready
            if (!DBO.isDatabaseReady()) {
                Log.w(TAG, "Database not ready, attempting to ensure it's open...");
                if (!DBO.ensureDatabaseOpen()) {
                    throw new RuntimeException("Failed to initialize database");
                }
            }
            
            Log.d(TAG, "Database initialized successfully");
        } catch (Exception e) {
            Log.e(TAG, "Error initializing database: " + e.getMessage(), e);
            Toast.makeText(this, "Database initialization failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
            throw e;
        }
    }

    private void initializeSharedPreferences() {
        try {
            sp = getSharedPreferences(Utils.PREFS, MODE_PRIVATE);
            Log.d(TAG, "Shared preferences initialized");
        } catch (Exception e) {
            Log.e(TAG, "Error initializing shared preferences: " + e.getMessage(), e);
            throw e;
        }
    }

    private void initializePrayerTimes() {
        try {
            prayerTimes.clear();
            prayerTimes.add(Pref.getValue(activity, Constants.PREF_FAJR_TIME_24, ""));
            prayerTimes.add(Pref.getValue(activity, Constants.PREF_SUNRISE_TIME_24, ""));
            prayerTimes.add(Pref.getValue(activity, Constants.PREF_DHOHR_TIME_24, ""));
            prayerTimes.add(Pref.getValue(activity, Constants.PREF_ASR_TIME_24, ""));
            prayerTimes.add(Pref.getValue(activity, Constants.PREF_MAGHRIB_TIME_24, ""));
            prayerTimes.add(Pref.getValue(activity, Constants.PREF_ISHA_TIME_24, ""));
            Log.d(TAG, "Prayer times initialized: " + prayerTimes.size() + " times loaded");
        } catch (Exception e) {
            Log.e(TAG, "Error initializing prayer times: " + e.getMessage(), e);
            // Don't throw here, continue with empty list
        }
    }

    private void initializeUIComponents() {
        try {
            // Initialize ImageViews
            iv_back = findViewById(R.id.iv_back);
            ivThumb = findViewById(R.id.ivThumb);
            ivSelectImg = findViewById(R.id.ivSelectImg);
            ivSelectImg1 = findViewById(R.id.ivSelectImg1);
            ivSelectImg2 = findViewById(R.id.ivSelectImg2);
            ivSelectImg3 = findViewById(R.id.ivSelectImg3);
            ivSelectImg4 = findViewById(R.id.ivSelectImg4);
            ivSelectImg5 = findViewById(R.id.ivSelectImg5);
            ivSelectImg6 = findViewById(R.id.ivSelectImg6);
            ivAdsVideoThumb = findViewById(R.id.ivAdsVideoThumb);
            ivSelectVideo = findViewById(R.id.ivSelectVideo);
            ivSelectVideo1 = findViewById(R.id.ivSelectVideo1);
            ivSelectVideo2 = findViewById(R.id.ivSelectVideo2);

            // Set video selection states
            if (ivSelectVideo != null) ivSelectVideo.setEnabled(true);
            if (ivSelectVideo1 != null) ivSelectVideo1.setEnabled(false);
            if (ivSelectVideo2 != null) ivSelectVideo2.setEnabled(false);

            // Initialize RadioGroup and RadioButtons
            RadioGroup rgAdsType = findViewById(R.id.rgAdsType);
            rbImage = findViewById(R.id.rbImage);
            rbVideo = findViewById(R.id.rbVideo);
            rbText = findViewById(R.id.rbText);

            // Initialize RelativeLayouts
            rlImage = findViewById(R.id.rlImage);
            rlVideo = findViewById(R.id.rlVideo);
            rlText = findViewById(R.id.rlText);

            // Initialize EditTexts
            edAdsText = findViewById(R.id.edAdsText);
            edAdsTitle = findViewById(R.id.edAdsTitle);
            edAddAppearance = findViewById(R.id.edAddAppearance);
            llAdsPeriods = findViewById(R.id.llAdsPeriods);
            ed_start = findViewById(R.id.ed_start);
            ed_end = findViewById(R.id.ed_end);
            img_sec_show = findViewById(R.id.img_sec_show);

            // Initialize TextViews
            tvSave = findViewById(R.id.tvSave);
            tittleA = findViewById(R.id.tittleA);

            // Initialize CheckBoxes
            cbHidePulpitAdsBox = findViewById(R.id.cbHidePulpitAdsBox);

            // Initialize Font Size and Movement Speed SeekBars
            sbFontSize = findViewById(R.id.sbFontSize);
            sbMovementSpeed = findViewById(R.id.sbMovementSpeed);
            tvFontSizeValue = findViewById(R.id.tvFontSizeValue);
            tvSpeedValue = findViewById(R.id.tvSpeedValue);

            // Load the pulpit ads box visibility state
            // Handle migration from old boolean format to new integer format
            int visibilityState;
            try {
                // Try to read as integer (new format)
                visibilityState = sp.getInt(Constants.PREF_HIDE_PULPIT_ADS_BOX, View.VISIBLE);
            } catch (ClassCastException e) {
                // Handle migration from old boolean format
                Log.d("AddAdsActivity", "Migrating from old boolean format to new integer format");
                boolean oldBooleanValue = sp.getBoolean(Constants.PREF_HIDE_PULPIT_ADS_BOX, false);
                visibilityState = oldBooleanValue ? View.GONE : View.VISIBLE;
                
                // Save the new integer format
                SharedPreferences.Editor editor = sp.edit();
                editor.putInt(Constants.PREF_HIDE_PULPIT_ADS_BOX, visibilityState);
                editor.apply();
                
                // Remove the old boolean key
                editor.remove(Constants.PREF_HIDE_PULPIT_ADS_BOX);
                editor.apply();
            }
            
            boolean hidePulpitAdsBox = (visibilityState == View.GONE);
            cbHidePulpitAdsBox.setChecked(hidePulpitAdsBox);
            Log.d("AddAdsActivity", "Pulpit ads box visibility state loaded: " + visibilityState + ", checkbox checked: " + hidePulpitAdsBox);

            // Debug: Log the current preference value from SharedPreferences
            int currentPref = sp.getInt(Constants.PREF_HIDE_PULPIT_ADS_BOX, View.VISIBLE);
            Log.d("AddAdsActivity", "Current PREF_HIDE_PULPIT_ADS_BOX value from SharedPreferences: " + currentPref);

            // Add checkbox change listener for debugging
            cbHidePulpitAdsBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                Log.d("AddAdsActivity", "Pulpit ads box checkbox changed to: " + isChecked);
            });

            // Load font size and movement speed preferences
            loadTextAdsPreferences();

            // Set initial states
            if (edAddAppearance != null) edAddAppearance.setFocusable(true);
            if (tvSave != null) {
                tvSave.setVisibility(View.GONE);
            }

            Log.d(TAG, "UI components initialized successfully");
            
        } catch (Exception e) {
            Log.e(TAG, "Error initializing UI components: " + e.getMessage(), e);
            throw e;
        }
    }

    private void setupClickListeners() {
        try {
            // Set click listeners for ImageViews
            if (iv_back != null) iv_back.setOnClickListener(this);
            if (ivSelectImg != null) ivSelectImg.setOnClickListener(this);
            if (ivSelectImg1 != null) ivSelectImg1.setOnClickListener(this);
            if (ivSelectImg2 != null) ivSelectImg2.setOnClickListener(this);
            if (ivSelectImg3 != null) ivSelectImg3.setOnClickListener(this);
            if (ivSelectImg4 != null) ivSelectImg4.setOnClickListener(this);
            if (ivSelectImg5 != null) ivSelectImg5.setOnClickListener(this);
            if (ivSelectImg6 != null) ivSelectImg6.setOnClickListener(this);
            if (ivSelectVideo != null) ivSelectVideo.setOnClickListener(this);
            if (ivSelectVideo1 != null) ivSelectVideo1.setOnClickListener(this);
            if (ivSelectVideo2 != null) ivSelectVideo2.setOnClickListener(this);
            if (ivAdsVideoThumb != null) ivAdsVideoThumb.setOnClickListener(this);
            if (tvSave != null) tvSave.setOnClickListener(this);

            // Set RadioGroup listener
            RadioGroup rgAdsType = findViewById(R.id.rgAdsType);
            if (rgAdsType != null) {
                rgAdsType.setOnCheckedChangeListener(this);
            }

            // Set date picker listeners
            if (ed_start != null) {
                ed_start.setOnClickListener(view -> {
                    Utils.hideSoftKeyboard(activity);
                    showDatePicker(ed_start);
                });
            }
            
            if (ed_end != null) {
                ed_end.setOnClickListener(view -> {
                    Utils.hideSoftKeyboard(activity);
                    showDatePicker(ed_end);
                });
            }

            // Set TextWatcher for edAddAppearance
            if (edAddAppearance != null) {
                edAddAppearance.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        handleAppearanceTextChanged();
                    }
                });
            }

            // Set SeekBar change listeners for text ads preferences
            if (sbFontSize != null) {
                sbFontSize.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        // Update font size value display
                        if (tvFontSizeValue != null) {
                            int fontSizeSp = getFontSizeFromPercentage(progress);
                            tvFontSizeValue.setText(fontSizeSp + "sp");
                        }
                        // Update font size preference when user changes the seek bar
                        if (fromUser) {
                            saveTextAdsPreferences();
                        }
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {
                        // Not needed for this implementation
                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                        // Not needed for this implementation
                    }
                });
            }

            if (sbMovementSpeed != null) {
                sbMovementSpeed.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        // Update movement speed value display
                        if (tvSpeedValue != null) {
                            float speedMultiplier = getSpeedMultiplierFromPercentage(progress);
                            tvSpeedValue.setText(String.format("%.1fx", speedMultiplier));
                        }
                        // Update movement speed preference when user changes the seek bar
                        if (fromUser) {
                            saveTextAdsPreferences();
                        }
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {
                        // Not needed for this implementation
                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                        // Not needed for this implementation
                    }
                });
            }

            Log.d(TAG, "Click listeners setup completed");
            
        } catch (Exception e) {
            Log.e(TAG, "Error setting up click listeners: " + e.getMessage(), e);
            throw e;
        }
    }

    private void handleAppearanceTextChanged() {
        try {
            if (tvSave != null) tvSave.setVisibility(View.GONE);
            if (!TextUtils.isEmpty(edAddAppearance.getText().toString().trim())) {
                int size = Integer.parseInt(edAddAppearance.getText().toString().trim());
                if (llAdsPeriods != null) llAdsPeriods.removeAllViews();
                if (tittleA != null) tittleA.setVisibility(View.VISIBLE);
                if (size < checkList.size()) {
                    size = checkList.size();
                    edAddAppearance.setText(String.valueOf(size));
                }
                if (size > 0) {
                    if (llAdsPeriods != null) llAdsPeriods.removeAllViews();
                    for (int i = 0; i < size; i++) {
                        AdsPeriods adsPeriods;
                        if (i < adsList.size()) {
                            adsPeriods = adsList.get(i);
                            llAdsPeriods.addView(getItem(i, adsPeriods));
                        } else
                            llAdsPeriods.addView(getItem(i, null));
                    }
                } else {
                    if (llAdsPeriods != null) llAdsPeriods.removeAllViews();
                    if (tittleA != null) tittleA.setVisibility(View.GONE);
                    for (int i = 0; i < adsList.size(); i++) {
                        AdsPeriods adsPeriods;
                        if (tittleA != null) tittleA.setVisibility(View.VISIBLE);
                        adsPeriods = adsList.get(i);
                        llAdsPeriods.addView(getItem(i, adsPeriods));
                    }
                }
            } else {
                if (llAdsPeriods != null) llAdsPeriods.removeAllViews();
                if (tittleA != null) tittleA.setVisibility(View.GONE);
                for (int i = 0; i < adsList.size(); i++) {
                    AdsPeriods adsPeriods;
                    if (tittleA != null) tittleA.setVisibility(View.VISIBLE);
                    adsPeriods = adsList.get(i);
                    llAdsPeriods.addView(getItem(i, adsPeriods));
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "Error in handleAppearanceTextChanged: " + e.getMessage(), e);
        }
    }

    private void requestPermissions() {
        try {
            askForPermissions(new String[]{
                            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            android.Manifest.permission.READ_EXTERNAL_STORAGE},
                    REQUEST_PERMISSIONS);
        } catch (Exception e) {
            Log.e(TAG, "Error requesting permissions: " + e.getMessage(), e);
        }
    }

    private LinearLayout getItem(final int pos, AdsPeriods adsPeriods) {
        @SuppressLint("InflateParams") final LinearLayout ll =
                (LinearLayout) getLayoutInflater()
                        .inflate(R.layout.ads_period_row, null);

        try {
            EditText ed_startTime = (EditText) ll.findViewById(R.id.ed_start);
            EditText ed_endTime = (EditText) ll.findViewById(R.id.ed_end);
            CheckBox cbSa = (CheckBox) ll.findViewById(R.id.cbSat);
            CheckBox cbSu = (CheckBox) ll.findViewById(R.id.cbSun);
            CheckBox cbMo = (CheckBox) ll.findViewById(R.id.cbMon);
            CheckBox cbTu = (CheckBox) ll.findViewById(R.id.cbTue);
            CheckBox cbWe = (CheckBox) ll.findViewById(R.id.cbWed);
            CheckBox cbTh = (CheckBox) ll.findViewById(R.id.cbThu);
            CheckBox cbFr = (CheckBox) ll.findViewById(R.id.cbFri);
            Button btnCheck = (Button) ll.findViewById(R.id.btnCheck);
            if (adsPeriods != null) {
                if (adsPeriods.getDays().contains("1"))
                    cbSa.setChecked(true);
                if (adsPeriods.getDays().contains("2"))
                    cbSu.setChecked(true);
                if (adsPeriods.getDays().contains("3"))
                    cbMo.setChecked(true);
                if (adsPeriods.getDays().contains("4"))
                    cbTu.setChecked(true);
                if (adsPeriods.getDays().contains("5"))
                    cbWe.setChecked(true);
                if (adsPeriods.getDays().contains("6"))
                    cbTh.setChecked(true);
                if (adsPeriods.getDays().contains("7"))
                    cbFr.setChecked(true);
                cbSa.setEnabled(false);
                cbSu.setEnabled(false);
                cbMo.setEnabled(false);
                cbTu.setEnabled(false);
                cbWe.setEnabled(false);
                cbTh.setEnabled(false);
                cbFr.setEnabled(false);
                ed_startTime.setText(adsPeriods.getStartTime());
                ed_endTime.setText(adsPeriods.getEndTime());
                ed_endTime.setEnabled(false);
                ed_startTime.setEnabled(false);
                btnCheck.setVisibility(View.GONE);

            }
            ed_startTime.setOnClickListener(view -> {
                Utils.hideSoftKeyboard(activity);
                showDateTimePicker((EditText) ((llAdsPeriods.getChildAt(pos)).findViewById(R.id.ed_start)));
            });
            ed_endTime.setOnClickListener(view -> {
                Utils.hideSoftKeyboard(activity);
                showDateTimePicker((EditText) ((llAdsPeriods.getChildAt(pos)).findViewById(R.id.ed_end)));
            });
            btnCheck.setOnClickListener(view -> {
                adsPeriodsList.clear();
                final View v = llAdsPeriods.getChildAt(pos);
                EditText edStartTime = ((EditText) (v.findViewById(R.id.ed_start)));
                EditText edEndTime = ((EditText) (v.findViewById(R.id.ed_end)));
                cbSat = (CheckBox) v.findViewById(R.id.cbSat);
                cbSun = (CheckBox) v.findViewById(R.id.cbSun);
                cbMon = (CheckBox) v.findViewById(R.id.cbMon);
                cbTue = (CheckBox) v.findViewById(R.id.cbTue);
                cbWed = (CheckBox) v.findViewById(R.id.cbWed);
                cbThu = (CheckBox) v.findViewById(R.id.cbThu);
                cbFri = (CheckBox) v.findViewById(R.id.cbFri);
                Button btn = (Button) v.findViewById(R.id.btnCheck);
                isConflictAds = false;
                edAdsTitle.setError(null);
                edAdsText.setError(null);
                ed_start.setError(null);
                ed_end.setError(null);
                edStartTime.setError(null);
                edEndTime.setError(null);
                if (TextUtils.isEmpty(edAdsTitle.getText().toString().toString())) {
                    edAdsTitle.setError(getString(R.string.required));
                    return;
                }
                if (type == 1 && selectedImage == null) {
                    Utils.showCustomToast(activity, getString(R.string.chooseAdvImage));
                    return;
                }
                if (type == 2 && videoData == null) {
                    Utils.showCustomToast(activity, getString(R.string.chooseVideo));
                    return;
                }
                if (type == 3 && TextUtils.isEmpty(edAdsText.getText().toString().toString())) {
                    edAdsText.setError(getString(R.string.addAdvDesc));
                    Utils.showCustomToast(activity, getString(R.string.addAdvDesc));
                    return;
                }
                if (TextUtils.isEmpty(ed_start.getText().toString())) {
                    ed_start.setError(getString(R.string.addStartDate));
                    return;
                }
                if (TextUtils.isEmpty(ed_end.getText().toString())) {
                    ed_end.setError(getString(R.string.addEndDate));
                    return;
                }
                if (!Utils.compareDate(ed_start.getText().toString(), ed_end.getText().toString())) {
                    Utils.showCustomToast(activity, getString(R.string.error_date));
                    ed_end.setError(getString(R.string.error_date));
                    return;
                }
                if (TextUtils.isEmpty(edStartTime.getText().toString())) {
                    edStartTime.setError(getString(R.string.addStartTime));
                }
                if (TextUtils.isEmpty(edEndTime.getText().toString())) {
                    edEndTime.setError(getString(R.string.addEndTime));
                    return;
                }
                if (!Utils.compareTimes(edStartTime.getText().toString(), edEndTime.getText().toString())) {
                    Utils.showCustomToast(activity, getString(R.string.error_time));
                    edEndTime.setError(getString(R.string.error_time));
                    return;
                }
                if (!cbSat.isChecked() && !cbSun.isChecked() && !cbMon.isChecked() && !cbTue.isChecked() && !cbWed.isChecked()
                        && !cbThu.isChecked() && !cbFri.isChecked()) {
                    Utils.showCustomToast(activity, getString(R.string.addDays));
                    return;
                }

                List<Integer> dayList = new ArrayList<>();
                dayList.clear();
                if (cbSat.isChecked()) {
                    dayList.add(1);
                }
                if (cbSun.isChecked()) {
                    dayList.add(2);
                }
                if (cbMon.isChecked()) {
                    dayList.add(3);
                }
                if (cbTue.isChecked()) {
                    dayList.add(4);
                }
                if (cbWed.isChecked()) {
                    dayList.add(5);
                }
                if (cbThu.isChecked()) {
                    dayList.add(6);
                }
                if (cbFri.isChecked()) {
                    dayList.add(7);
                }
                String days = "";

                for (int x = 0; x < dayList.size(); x++) {
                    AdsPeriods adsPeriods1 = new AdsPeriods();
                    adsPeriods1.setStartTime(edStartTime.getText().toString().trim());
                    adsPeriods1.setEndTime(edEndTime.getText().toString().trim());
                    adsPeriods1.setStartDate(ed_start.getText().toString().trim());
                    adsPeriods1.setEndDate(ed_end.getText().toString().trim());
                    days = dayList.get(x) + "," + days;
                    adsPeriods1.setDay(dayList.get(x));
                    DBO.openDataBase();
                    boolean hasConflict = DBO.itHasConflict(sp.getInt("masjedId", -1), adsPeriods1);
                    DBO.close();
                    if (hasConflict) {
                        isConflictAds = true;
                        Utils.showCustomToast(activity, getString(R.string.conflictWithAnotherAdv));
                        break;
                    }
                    if (inPrayPeriod(prayerTimes, adsPeriods1)) {
                        Utils.showCustomToast(activity, getString(R.string.conflictWithPray));
                        break;
                    }
                    adsPeriodsList.add(adsPeriods1);
                    if (x == dayList.size() - 1) {
                        if (!isConflictAds) {
                            if (type == 1) {
                                videoData = null;
                                selectedVideoPath = "";
                                selectedVideoPath1 = "";
                                selectedVideoPath2 = "";
                                edAdsText.setText("");
                            } else if (type == 2) {
                                selectedImage = null;
                                selectedImagePath = "";
                                selectedImagePath1 = "";
                                selectedImagePath2 = "";
                                selectedImagePath3 = "";
                                selectedImagePath4 = "";
                                selectedImagePath5 = "";
                                selectedImagePath6 = "";
                                edAdsText.setText("");
                            } else if (type == 3) {
                                selectedImage = null;
                                selectedImagePath = "";
                                selectedImagePath1 = "";
                                selectedImagePath2 = "";
                                selectedImagePath3 = "";
                                selectedImagePath4 = "";
                                selectedImagePath5 = "";
                                selectedImagePath6 = "";
                                videoData = null;
                                selectedVideoPath = "";
                                selectedVideoPath1 = "";
                                selectedVideoPath2 = "";
                            }
                            Ads ads = new Ads();
                            ads.setMasjedID(sp.getInt("masjedId", -1));
                            ads.setTitle(edAdsTitle.getText().toString().trim());
                            ads.setType(type);
                            ads.setImage(selectedImagePath);
                            ads.setImage1(selectedImagePath1);
                            ads.setImage2(selectedImagePath2);
                            ads.setImage3(selectedImagePath3);
                            ads.setImage4(selectedImagePath4);
                            ads.setImage5(selectedImagePath5);
                            ads.setImage6(selectedImagePath6);
                            ads.setVideo(selectedVideoPath);
                            ads.setVideo1(selectedVideoPath1);
                            ads.setVideo2(selectedVideoPath2);

                            ads.setImageSec(Integer.parseInt(img_sec_show.getText().toString()));
                            ads.setText(edAdsText.getText().toString().trim());
                            ads.setStartDate(ed_start.getText().toString().trim());
                            ads.setEndDate(ed_end.getText().toString().trim());
                            String msg;

                            DBO = new DataBaseHelper(activity);
                            if (advId == -1) {
                                advId = DBO.insertAds(ads);
                            }
                            if (advId != -1) {
                                if (count == 0) {
                                    msg = getString(R.string.AdvAddedSuccess);
                                    count++;
                                } else
                                    msg = getString(R.string.periodAdded);

                                for (int i = 0; i < adsPeriodsList.size(); i++) {
                                    AdsPeriods advPeriod = adsPeriodsList.get(i);
                                    advPeriod.setAdvId(advId);
                                    advPeriod.setAdded(true);
                                }
                                DBO.insertAdsPeriod(adsPeriodsList);
                                adsPeriods1.setDays(days);
                                DBO.openDataBase();
                                String AdsIds = DBO.getAdvPeriodsIds(advId, adsPeriods1.getStartTime(), adsPeriods1.getEndTime());
                                DBO.close();
                                adsList.add(new AdsPeriods(adsPeriods1.getAdvId(), adsPeriods1.getStartTime(), adsPeriods1.getEndTime(),
                                        adsPeriods1.getStartDate(), adsPeriods1.getEndDate(), adsPeriods1.getDays(), AdsIds, true));

                                btn.setVisibility(View.GONE);
                                checkList.add(pos);
                                ed_start.setEnabled(false);
                                ed_end.setEnabled(false);

                                // Save the pulpit ads box visibility state
                                int visibilityState = cbHidePulpitAdsBox.isChecked() ? View.GONE : View.VISIBLE;
                                SharedPreferences.Editor editor = sp.edit();
                                editor.putInt(Constants.PREF_HIDE_PULPIT_ADS_BOX, visibilityState);
                                editor.apply();
                                Log.d("AddAdsActivity", "Pulpit ads box visibility state saved: " + visibilityState);

                                // Save text ads preferences if this is a text ad
                                if (type == 3) {
                                    saveTextAdsPreferences();
                                }
                                edEndTime.setEnabled(false);
                                edStartTime.setEnabled(false);
                                Utils.showCustomToast(activity, msg);
                            } else {
                                Utils.showCustomToast(activity, getString(R.string.AdvNotAdded) + advId);
                            }

                        }
                    }
                }
            });
        } catch (Exception e) {
            Log.e("XXX", e.getMessage());

        }
        return ll;
    }


    public static boolean inPrayPeriod(ArrayList<String> list, AdsPeriods adsPeriods) {
        DateFormat df = new SimpleDateFormat("HH:mm", Locale.ENGLISH);
        for (String object : list) {
            try {
                Date prayTime = df.parse(object);
                Date startTime = df.parse(adsPeriods.getStartTime());
                Date endTime = df.parse(adsPeriods.getEndTime());
                if ((prayTime.after(startTime) || prayTime.equals(startTime))
                        && (prayTime.before(endTime) || prayTime.equals(endTime))) {
                    return true;
                }
            } catch (ParseException e) {
                e.printStackTrace();
                return false;
            }

        }
        return false;
    }

    private void showDatePicker(final EditText editText) {
        Calendar mcurrentTime = Calendar.getInstance();
        DatePickerDialog mDatePicker;
        Calendar cal = Calendar.getInstance();
        int calYear = cal.get(Calendar.YEAR);
        int calMonth = cal.get(Calendar.MONTH);
        int calDay = cal.get(Calendar.DAY_OF_MONTH);
        mDatePicker = new DatePickerDialog(activity, (datePicker, year, month, day) -> {
            int MONTH = (month + 1);
            int DAY = day;
            String monthS = MONTH + "";
            String dayS = DAY + "";
            if (MONTH < 10)
                monthS = "0" + MONTH;
            if (DAY < 10)
                dayS = "0" + DAY;
            editText.setText(String.format(Locale.ENGLISH, getString(R.string.formatDate), String.valueOf(year), String.valueOf(monthS)
                    , dayS));

        }, calYear, calMonth, calDay);
        mDatePicker.show();
    }

    public void showDateTimePicker(final EditText editText) {
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(activity, (timePicker, selectedHour, selectedMinute) -> {
            String hours = "" + selectedHour;
            if (selectedHour < 10)
                hours = "0" + selectedHour;

            String minute1 = "" + selectedMinute;
            if (selectedMinute < 10)
                minute1 = "0" + selectedMinute;
            editText.setText(String.format(Locale.ENGLISH, getString(R.string.formatTime), hours, minute1));
        }, hour, minute, false);//Yes 24 hour time
        mTimePicker.setTitle(getString(R.string.chooseTime));
        mTimePicker.show();
    }

    @Override
    public void onClick(View view) {
        try {
            if (view == iv_back) {
                finish();
            } else if (view == ivSelectImg) {
                imagePigNumber = 0;
                selectImage();
            } else if (view == ivSelectImg1) {
                imagePigNumber = 1;
                selectImage();
            } else if (view == ivSelectImg2) {
                imagePigNumber = 2;
                selectImage();
            } else if (view == ivSelectImg3) {
                imagePigNumber = 3;
                selectImage();
            } else if (view == ivSelectImg4) {
                imagePigNumber = 4;
                selectImage();
            } else if (view == ivSelectImg5) {
                imagePigNumber = 5;
                selectImage();
            } else if (view == ivSelectImg6) {
                imagePigNumber = 6;
                selectImage();
            } else if (view == ivSelectVideo) {
                videoPigNumber = 0;
                selectVideo();
            } else if (view == ivSelectVideo1) {
                videoPigNumber = 1;
                selectVideo();
            } else if (view == ivSelectVideo2) {
                videoPigNumber = 2;
                selectVideo();
            } else if (view == ivAdsVideoThumb) {
                if (videoData != null) {
//                Intent intent = new Intent(activity, VideoViewActivity.class);
//                intent.setAction("uri");
//                intent.putExtra("videoURI", videoData);
//                startActivity(intent);
                }
            }
        } catch (Exception e) {
            Log.e("XXX", e.getLocalizedMessage());
        }
    }


    protected final void askForPermissions(String[] permissions, int requestCode) {
        try {
            Log.d(TAG, "Requesting permissions: " + permissions.length + " permissions");
            
            List<String> permissionsToRequest = new ArrayList<>();
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED) {
                    permissionsToRequest.add(permission);
                    Log.d(TAG, "Permission needed: " + permission);
                } else {
                    Log.d(TAG, "Permission already granted: " + permission);
                }
            }
            
            if (!permissionsToRequest.isEmpty()) {
                Log.d(TAG, "Requesting " + permissionsToRequest.size() + " permissions");
                ActivityCompat.requestPermissions(activity,
                        permissionsToRequest.toArray(new String[permissionsToRequest.size()]), requestCode);
            } else {
                Log.d(TAG, "All permissions already granted");
            }
        } catch (Exception e) {
            Log.e(TAG, "Error requesting permissions: " + e.getMessage(), e);
        }
    }

    @Override
    public final void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                                 @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        try {
            Log.d(TAG, "Permission result received for request code: " + requestCode);
            
            if (requestCode == REQUEST_PERMISSIONS) {
                boolean allGranted = true;
                for (int i = 0; i < grantResults.length; i++) {
                    if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                        Log.w(TAG, "Permission denied: " + permissions[i]);
                        allGranted = false;
                    } else {
                        Log.d(TAG, "Permission granted: " + permissions[i]);
                    }
                }
                
                if (!allGranted) {
                    Log.w(TAG, "Some permissions were denied");
                    Toast.makeText(this, "Storage permissions are required for this feature", Toast.LENGTH_LONG).show();
                    // Don't finish the activity, just show a warning
                } else {
                    Log.d(TAG, "All permissions granted successfully");
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "Error handling permission result: " + e.getMessage(), e);
        }
    }


    private void selectImage() {
        try {
            Intent i = new Intent();
            i.setType("image/*");
            i.setAction(Intent.ACTION_PICK);
            startActivityForResult(Intent.createChooser(i, getString(R.string.chooseImage)), RESULT_LOAD_IMAGE);
        } catch (Exception r) {
            r.printStackTrace();
            Utils.showCustomToast(activity, getString(R.string.NoImages));
        }
    }

    @SuppressLint("IntentReset")
    private void selectVideo() {
        try {
//            @SuppressLint("IntentReset")
//            Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
//            intent.setType("video/*");
//            startActivityForResult(intent, VIDEO_SELECT);

            Intent i = new Intent();
            i.setType("video/*");
            i.setAction(Intent.ACTION_PICK);
            startActivityForResult(Intent.createChooser(i, "اختر فيديو"), VIDEO_SELECT);
        } catch (Exception r) {
            r.printStackTrace();
            Utils.showCustomToast(activity, getString(R.string.noVideo));
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        String image_str;
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            try {
                selectedImage = data.getData();

                switch (imagePigNumber) {
                    case 0:
                        selectedImagePath = getRealPathFromURI(selectedImage);
                        Glide.with(activity).load(selectedImage + "").into(ivSelectImg);
                        break;
                    case 1:
                        selectedImagePath1 = getRealPathFromURI(selectedImage);
                        Glide.with(activity).load(selectedImage + "").into(ivSelectImg1);
                        break;
                    case 2:
                        selectedImagePath2 = getRealPathFromURI(selectedImage);
                        Glide.with(activity).load(selectedImage + "").into(ivSelectImg2);
                        break;
                    case 3:
                        selectedImagePath3 = getRealPathFromURI(selectedImage);
                        Glide.with(activity).load(selectedImage + "").into(ivSelectImg3);
                        break;
                    case 4:
                        selectedImagePath4 = getRealPathFromURI(selectedImage);
                        Glide.with(activity).load(selectedImage + "").into(ivSelectImg4);
                        break;
                    case 5:
                        selectedImagePath5 = getRealPathFromURI(selectedImage);
                        Glide.with(activity).load(selectedImage + "").into(ivSelectImg5);
                        break;
                    case 6:
                        selectedImagePath6 = getRealPathFromURI(selectedImage);
                        Glide.with(activity).load(selectedImage + "").into(ivSelectImg6);
                        break;

                }

                //   image_str = makePictureToBase64(checkImg(selectedImagePath), ivAdsImg);
            } catch (Exception e) {
                Utils.showCustomToast(activity, getString(R.string.failLoadImage));
                e.printStackTrace();
            }
        } else if (requestCode == VIDEO_SELECT && resultCode == RESULT_OK && null != data) {
            videoData = data.getData();
            Log.i("selectVideo", videoData + "");
            switch (videoPigNumber) {
                case 0:
                    selectedVideoPath = createCopyAndReturnRealPath(getApplicationContext(), videoData);
                    ivSelectVideo1.setEnabled(true);
                    break;
                case 1:
                    selectedVideoPath1 = createCopyAndReturnRealPath(getApplicationContext(), videoData);
                    ivSelectVideo2.setEnabled(true);
                    break;
                case 2:
                    selectedVideoPath2 = createCopyAndReturnRealPath(getApplicationContext(), videoData);
                    break;
            }
            Bitmap bitmap = ThumbnailUtils.createVideoThumbnail
                    (createCopyAndReturnRealPath(getApplicationContext(), videoData), MediaStore.Video.Thumbnails.MINI_KIND);
            System.out.println(">>>> data " + createCopyAndReturnRealPath(getApplicationContext(), videoData));
            System.out.println(">>>> bitmap " + bitmap);
            if (bitmap == null)
                return;
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream); //compress to which format you want.
            byte[] byte_arr = stream.toByteArray();
            image_str = Base64.encodeToString(byte_arr, 0);
            ivAdsVideoThumb.setImageBitmap(bitmap);
            ivThumb.setVisibility(View.VISIBLE);
        }
    }

    @Nullable
    public static String createCopyAndReturnRealPath(
            @NonNull Context context, @NonNull Uri uri) {
        final ContentResolver contentResolver = context.getContentResolver();
        if (contentResolver == null)
            return null;

        // Create file path inside app's data dir
        String filePath = context.getApplicationInfo().dataDir + File.separator
                + System.currentTimeMillis();

        File file = new File(filePath);
        try {
            InputStream inputStream = contentResolver.openInputStream(uri);
            if (inputStream == null)
                return null;

            OutputStream outputStream = new FileOutputStream(file);
            byte[] buf = new byte[1024];
            int len;
            while ((len = inputStream.read(buf)) > 0)
                outputStream.write(buf, 0, len);

            outputStream.close();
            inputStream.close();
        } catch (IOException ignore) {
            return null;
        }

        return file.getAbsolutePath();
    }

    public String getRealPath(Uri uri) {


        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        if (cursor != null) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } else return null;
    }

    private String checkImg(String path) {
        String newPath = path;
        if (!TextUtils.isEmpty(path)) {
            File f = new File((Uri.parse(path).getPath()));
            if (f.exists()) {
                Bitmap resized = getResizedBitmap(Uri.parse(path).getPath(), 640, 640);
                if (resized != null) {
                    String npath = saveToFile(resized);
                    if (!TextUtils.isEmpty(npath)) {
                        newPath = npath;
                    }
                }
            }

        }

        return newPath;
    }

    public Bitmap getResizedBitmap(String path, float widthRatio, float heightRatio) {
        float scale = 1;

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(Uri.parse(path).getPath(), options);
        int imageHeight = options.outHeight;
        int imageWidth = options.outWidth;
        if (imageHeight > imageWidth) {
            if (imageHeight > heightRatio) {
                scale = ((float) heightRatio) / imageHeight;
            }

        } else {
            if (imageWidth > widthRatio) {
                scale = ((float) widthRatio) / imageWidth;
            }
        }
        if (scale == 0) return null;
        Matrix matrix = new Matrix();
        matrix.postScale(scale, scale);

        Bitmap bm = BitmapFactory.decodeFile(Uri.parse(path).getPath());
        if (bm != null) {
            Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, false);
            bm.recycle();
            return resizedBitmap;
        }

        return null;
    }

    private String saveToFile(Bitmap bm) {
        File sd = getTempStoreDirectory(activity);
        String path = null;
        FileOutputStream fOut = null;
        try {
            if (sd.canWrite()) {
                File temp = new File(sd, "temp" + System.currentTimeMillis() + ".jpg");
                fOut = new FileOutputStream(temp);
                bm.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
                path = temp.getPath();

                bm.recycle();
                System.gc();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (fOut != null) {
                    fOut.flush();
                    fOut.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return path;
    }

    public static File getTempStoreDirectory(Context context) {
        File root = new File(Environment.getExternalStorageDirectory(), "Notes");
        return context.getExternalFilesDir("temp").getAbsoluteFile();
    }

    public String makePictureToBase64(String image_path, ImageView image) {
        Bitmap bitmap = ShrinkBitmap(image_path, 300, 300);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        String type = "";
        if (image_path.endsWith("jpg")) {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            type = "data:image/jpeg;base64,";
        } else if (image_path.endsWith("png")) {
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
            type = "data:image/png;base64,";
        } else
            Toast.makeText(activity, "make_sure_extension", Toast.LENGTH_LONG).show();

        byte[] byteArrayImage = baos.toByteArray();
        return type + Base64.encodeToString(byteArrayImage, Base64.NO_WRAP);
    }

    private String getRealPathFromURI(Uri contentURI) {
        String result = null;

        Cursor cursor = getContentResolver().query(contentURI, null, null, null, null);

        if (cursor == null) { // Source is Dropbox or other similar local file path
            result = contentURI.getPath();
        } else {
            if (cursor.moveToFirst()) {
                int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                result = cursor.getString(idx);
            }
            cursor.close();
        }
        return result;
    }

    public static Bitmap ShrinkBitmap(String file, int width, int height) {

        BitmapFactory.Options bmpFactoryOptions = new BitmapFactory.Options();
        bmpFactoryOptions.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(file, bmpFactoryOptions);

        int heightRatio = (int) Math.ceil(bmpFactoryOptions.outHeight / (float) height);
        int widthRatio = (int) Math.ceil(bmpFactoryOptions.outWidth / (float) width);

        if (heightRatio > 1 || widthRatio > 1) {
            if (heightRatio > widthRatio) {
                bmpFactoryOptions.inSampleSize = heightRatio;
            } else {
                bmpFactoryOptions.inSampleSize = widthRatio;
            }
        }

        bmpFactoryOptions.inJustDecodeBounds = false;
        bitmap = BitmapFactory.decodeFile(file, bmpFactoryOptions);
        return bitmap;
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, @IdRes int checkedId) {
        try {
            Utils.hideSoftKeyboard(activity);
            ivThumb.setVisibility(View.GONE);
            if (checkedId == R.id.rbImage) {
                rlImage.setVisibility(View.VISIBLE);
                rlVideo.setVisibility(View.GONE);
                rlText.setVisibility(View.GONE);
                type = 1;
            } else if (checkedId == R.id.rbVideo) {
                rlImage.setVisibility(View.GONE);
                rlVideo.setVisibility(View.VISIBLE);
                rlText.setVisibility(View.GONE);
                type = 2;
            } else if (checkedId == R.id.rbText) {
                rlImage.setVisibility(View.GONE);
                rlVideo.setVisibility(View.GONE);
                rlText.setVisibility(View.VISIBLE);
                type = 3;
            }
        } catch (Exception e) {
            Log.e("XXX", e.getLocalizedMessage());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            Log.d(TAG, "AddAdsActivity onResume");
            
            // Ensure database is still available
            if (DBO != null && !DBO.isDatabaseReady()) {
                Log.w(TAG, "Database not ready in onResume, attempting to reopen...");
                if (!DBO.ensureDatabaseOpen()) {
                    Log.e(TAG, "Failed to reopen database in onResume");
                    Toast.makeText(this, "Database connection lost", Toast.LENGTH_SHORT).show();
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "Error in onResume: " + e.getMessage(), e);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        try {
            Log.d(TAG, "AddAdsActivity onPause");
        } catch (Exception e) {
            Log.e(TAG, "Error in onPause: " + e.getMessage(), e);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            Log.d(TAG, "AddAdsActivity onDestroy");
            if (DBO != null) {
                DBO.close();
            }
        } catch (Exception e) {
            Log.e(TAG, "Error in onDestroy: " + e.getMessage(), e);
        }
    }

    private void logActivityState() {
        try {
            Log.d(TAG, "=== Activity State ===");
            Log.d(TAG, "Activity: " + (activity != null ? "Initialized" : "NULL"));
            Log.d(TAG, "Database: " + (DBO != null ? (DBO.isDatabaseReady() ? "Ready" : "Not Ready") : "NULL"));
            Log.d(TAG, "SharedPreferences: " + (sp != null ? "Initialized" : "NULL"));
            Log.d(TAG, "Prayer Times: " + prayerTimes.size() + " loaded");
            Log.d(TAG, "UI Components: " + (iv_back != null ? "Back Button: OK" : "Back Button: NULL"));
            Log.d(TAG, "=====================");
        } catch (Exception e) {
            Log.e(TAG, "Error logging activity state: " + e.getMessage(), e);
        }
    }

    /**
     * Simple test method to verify basic activity functionality
     */
    private void testBasicFunctionality() {
        try {
            Log.d(TAG, "Testing basic functionality...");
            
            // Test if we can show a toast
            Toast.makeText(this, "AddAdsActivity is working!", Toast.LENGTH_SHORT).show();
            
            // Test if we can find basic views
            if (iv_back != null) {
                Log.d(TAG, "Back button found successfully");
            } else {
                Log.w(TAG, "Back button is null");
            }
            
            Log.d(TAG, "Basic functionality test completed");
            
        } catch (Exception e) {
            Log.e(TAG, "Error in basic functionality test: " + e.getMessage(), e);
        }
    }

    /**
     * Load text ads preferences for font size and movement speed
     */
    private void loadTextAdsPreferences() {
        try {
            // Load font size preference (default: 50% = 80sp)
            int fontSizePercentage = sp.getInt(Constants.PREF_TEXT_ADS_FONT_SIZE, 50);
            if (sbFontSize != null) {
                sbFontSize.setProgress(fontSizePercentage);
            }
            if (tvFontSizeValue != null) {
                int fontSizeSp = getFontSizeFromPercentage(fontSizePercentage);
                tvFontSizeValue.setText(fontSizeSp + "sp");
            }

            // Load movement speed preference (default: 50% = 1.0x)
            int speedPercentage = sp.getInt(Constants.PREF_TEXT_ADS_MOVEMENT_SPEED, 50);
            if (sbMovementSpeed != null) {
                sbMovementSpeed.setProgress(speedPercentage);
            }
            if (tvSpeedValue != null) {
                float speedMultiplier = getSpeedMultiplierFromPercentage(speedPercentage);
                tvSpeedValue.setText(String.format("%.1fx", speedMultiplier));
            }

            Log.d("AddAdsActivity", "Text ads preferences loaded - Font Size: " + fontSizePercentage + "%, Movement Speed: " + speedPercentage + "%");
        } catch (Exception e) {
            Log.e("AddAdsActivity", "Error loading text ads preferences: " + e.getMessage(), e);
        }
    }

    /**
     * Save text ads preferences for font size and movement speed
     */
    private void saveTextAdsPreferences() {
        try {
            SharedPreferences.Editor editor = sp.edit();

            // Save font size preference as percentage (0-100)
            int fontSizePercentage = 50; // Default to 50%
            if (sbFontSize != null) {
                fontSizePercentage = sbFontSize.getProgress();
            }
            editor.putInt(Constants.PREF_TEXT_ADS_FONT_SIZE, fontSizePercentage);

            // Save movement speed preference as percentage (0-100)
            int speedPercentage = 50; // Default to 50%
            if (sbMovementSpeed != null) {
                speedPercentage = sbMovementSpeed.getProgress();
            }
            editor.putInt(Constants.PREF_TEXT_ADS_MOVEMENT_SPEED, speedPercentage);

            editor.apply();
            Log.d("AddAdsActivity", "Text ads preferences saved - Font Size: " + fontSizePercentage + "%, Movement Speed: " + speedPercentage + "%");
        } catch (Exception e) {
            Log.e("AddAdsActivity", "Error saving text ads preferences: " + e.getMessage(), e);
        }
    }

    /**
     * Convert percentage (0-100) to font size in sp (10sp to 150sp)
     * @param percentage The percentage value from SeekBar (0-100)
     * @return Font size in sp
     */
    public static int getFontSizeFromPercentage(int percentage) {
        // Map 0% to 10sp, 100% to 150sp
        return 10 + (int) ((percentage / 100.0) * 140);
    }

    /**
     * Convert percentage (0-100) to movement speed multiplier
     * @param percentage The percentage value from SeekBar (0-100)
     * @return Speed multiplier (0.5x to 2.0x)
     */
    public static float getSpeedMultiplierFromPercentage(int percentage) {
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
     * Alternative minimal onCreate for testing purposes
     */
    private void createMinimalActivity() {
        try {
            Log.d(TAG, "Creating minimal activity...");
            
            // Just set a simple layout
            setContentView(R.layout.activity_add_ads);
            
            // Find only the back button
            iv_back = findViewById(R.id.iv_back);
            if (iv_back != null) {
                iv_back.setOnClickListener(v -> finish());
                Log.d(TAG, "Minimal activity created successfully");
                Toast.makeText(this, "Minimal AddAdsActivity loaded", Toast.LENGTH_SHORT).show();
            } else {
                Log.e(TAG, "Back button not found in minimal activity");
            }
            
        } catch (Exception e) {
            Log.e(TAG, "Error creating minimal activity: " + e.getMessage(), e);
            Toast.makeText(this, "Minimal activity failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
            finish();
        }
    }

}

