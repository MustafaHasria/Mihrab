package com.izzedineeita.mihrab.activity;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;

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
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

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

    private Activity activity;
    private ImageView ivAdsVideoThumb, ivSelectVideo,ivSelectVideo1, ivSelectVideo2,
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
        setContentView(R.layout.activity_add_ads);



        try {
            activity = this;
            DBO = new DataBaseHelper(activity);
            sp = getSharedPreferences(Utils.PREFS, MODE_PRIVATE);
            askForPermissions(new String[]{
                            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            android.Manifest.permission.READ_EXTERNAL_STORAGE},
                    REQUEST_PERMISSIONS);

            prayerTimes.add(Pref.getValue(activity, Constants.PREF_FAJR_TIME_24, ""));
            prayerTimes.add(Pref.getValue(activity, Constants.PREF_SUNRISE_TIME_24, ""));
            prayerTimes.add(Pref.getValue(activity, Constants.PREF_DHOHR_TIME_24, ""));
            prayerTimes.add(Pref.getValue(activity, Constants.PREF_ASR_TIME_24, ""));
            prayerTimes.add(Pref.getValue(activity, Constants.PREF_MAGHRIB_TIME_24, ""));
            prayerTimes.add(Pref.getValue(activity, Constants.PREF_ISHA_TIME_24, ""));

            iv_back = (ImageView) findViewById(R.id.iv_back);
            ivThumb = (ImageView) findViewById(R.id.ivThumb);
            ivSelectImg = (ImageView) findViewById(R.id.ivSelectImg);
            ivSelectImg1 = (ImageView) findViewById(R.id.ivSelectImg1);
            ivSelectImg2 = (ImageView) findViewById(R.id.ivSelectImg2);
            ivSelectImg3 = (ImageView) findViewById(R.id.ivSelectImg3);
            ivSelectImg4 = (ImageView) findViewById(R.id.ivSelectImg4);
            ivSelectImg5 = (ImageView) findViewById(R.id.ivSelectImg5);
            ivSelectImg6 = (ImageView) findViewById(R.id.ivSelectImg6);
        //    ivAdsImg = (ImageView) findViewById(R.id.ivAdsImg);
            ivAdsVideoThumb = (ImageView) findViewById(R.id.ivAdsVideoThumb);
            ivSelectVideo = (ImageView) findViewById(R.id.ivSelectVideo);
            ivSelectVideo1 = (ImageView) findViewById(R.id.ivSelectVideo1);
            ivSelectVideo2 = (ImageView) findViewById(R.id.ivSelectVideo2);

            ivSelectVideo.setEnabled(true);
            ivSelectVideo1.setEnabled(false);
            ivSelectVideo2.setEnabled(false);

            RadioGroup rgAdsType = (RadioGroup) findViewById(R.id.rgAdsType);
            rbImage = (RadioButton) findViewById(R.id.rbImage);
            rbVideo = (RadioButton) findViewById(R.id.rbVideo);
            rbText = (RadioButton) findViewById(R.id.rbText);
            rlImage = (RelativeLayout) findViewById(R.id.rlImage);
            rlVideo = (RelativeLayout) findViewById(R.id.rlVideo);
            rlText = (RelativeLayout) findViewById(R.id.rlText);
            edAdsText = (EditText) findViewById(R.id.edAdsText);
            edAdsTitle = (EditText) findViewById(R.id.edAdsTitle);
            edAddAppearance = (EditText) findViewById(R.id.edAddAppearance);
            edAddAppearance.setFocusable(true);
            llAdsPeriods = (LinearLayout) findViewById(R.id.llAdsPeriods);
            ed_start = (EditText) findViewById(R.id.ed_start);
            ed_end = (EditText) findViewById(R.id.ed_end);
            img_sec_show = (EditText) findViewById(R.id.img_sec_show);
            tvSave = (TextView) findViewById(R.id.tvSave);
            tvSave.setVisibility(View.GONE);
            tittleA = (TextView) findViewById(R.id.tittleA);

            Log.e("XXXd", "" + img_sec_show.getText().toString());

            iv_back.setOnClickListener(this);
            ivSelectImg.setOnClickListener(this);
            ivSelectImg1.setOnClickListener(this);
            ivSelectImg2.setOnClickListener(this);
            ivSelectImg3.setOnClickListener(this);
            ivSelectImg4.setOnClickListener(this);
            ivSelectImg5.setOnClickListener(this);
            ivSelectImg6.setOnClickListener(this);
            ivSelectVideo.setOnClickListener(this);
            ivSelectVideo1.setOnClickListener(this);
            ivSelectVideo2.setOnClickListener(this);
            ivAdsVideoThumb.setOnClickListener(this);
            tvSave.setOnClickListener(this);
            rgAdsType.setOnCheckedChangeListener(this);
            ed_start.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Utils.hideSoftKeyboard(activity);
                    showDatePicker(ed_start);
                }
            });
            ed_end.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Utils.hideSoftKeyboard(activity);
                    showDatePicker(ed_end);
                }
            });

            edAddAppearance.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    tvSave.setVisibility(View.GONE);
                    if (!TextUtils.isEmpty(edAddAppearance.getText().toString().trim())) {
                        int size = Integer.parseInt(edAddAppearance.getText().toString().trim());
                        llAdsPeriods.removeAllViews();
                        tittleA.setVisibility(View.VISIBLE);
                        if (size < checkList.size()) {
                            size = checkList.size();
                            edAddAppearance.setText(String.valueOf(size));
                        }
                        if (size > 0) {
                            llAdsPeriods.removeAllViews();
                            for (int i = 0; i < size; i++) {
                                AdsPeriods adsPeriods = null;
                                if (i < adsList.size()) {
                                    adsPeriods = adsList.get(i);
                                    llAdsPeriods.addView(getItem(i, adsPeriods));
                                } else
                                    llAdsPeriods.addView(getItem(i, null));
                            }
                        } else {
                            llAdsPeriods.removeAllViews();
                            tittleA.setVisibility(View.GONE);
                            for (int i = 0; i < adsList.size(); i++) {
                                AdsPeriods adsPeriods = null;
                                tittleA.setVisibility(View.VISIBLE);
                                adsPeriods = adsList.get(i);
                                llAdsPeriods.addView(getItem(i, adsPeriods));
                            }
                        }
                    } else {
                        llAdsPeriods.removeAllViews();
                        tittleA.setVisibility(View.GONE);
                        for (int i = 0; i < adsList.size(); i++) {
                            AdsPeriods adsPeriods = null;
                            tittleA.setVisibility(View.VISIBLE);
                            adsPeriods = adsList.get(i);
                            llAdsPeriods.addView(getItem(i, adsPeriods));
                        }
                    }

                }
            });
        } catch (Exception e) {
            Log.e("XXX", e.getLocalizedMessage());
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
            ed_startTime.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Utils.hideSoftKeyboard(activity);
                    showDateTimePicker((EditText) ((llAdsPeriods.getChildAt(pos)).findViewById(R.id.ed_start)));
                }
            });
            ed_endTime.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Utils.hideSoftKeyboard(activity);
                    showDateTimePicker((EditText) ((llAdsPeriods.getChildAt(pos)).findViewById(R.id.ed_end)));
                }
            });
            btnCheck.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
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
                        AdsPeriods adsPeriods = new AdsPeriods();
                        adsPeriods.setStartTime(edStartTime.getText().toString().trim());
                        adsPeriods.setEndTime(edEndTime.getText().toString().trim());
                        adsPeriods.setStartDate(ed_start.getText().toString().trim());
                        adsPeriods.setEndDate(ed_end.getText().toString().trim());
                        days = dayList.get(x) + "," + days;
                        adsPeriods.setDay(dayList.get(x));
                        DBO.openDataBase();
                        boolean hasConflict = DBO.itHasConflict(sp.getInt("masjedId", -1), adsPeriods);
                        DBO.close();
                        if (hasConflict) {
                            isConflictAds = true;
                            Utils.showCustomToast(activity, getString(R.string.conflictWithAnotherAdv));
                            break;
                        }
                        if (inPrayPeriod(prayerTimes, adsPeriods)) {
                            Utils.showCustomToast(activity, getString(R.string.conflictWithPray));
                            break;
                        }
                        adsPeriodsList.add(adsPeriods);
                        if (x == dayList.size() - 1) {
                            if (!isConflictAds) {
                                int repeatNo = Integer.parseInt(edAddAppearance.getText().toString().trim());
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
                                String msg = getString(R.string.AdvAddedSuccess);

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
                                    adsPeriods.setDays(days);
                                    DBO.openDataBase();
                                    String AdsDay = DBO.getAdvPeriods(advId, adsPeriods.getStartTime(), adsPeriods.getEndTime());
                                    String AdsIds = DBO.getAdvPeriodsIds(advId, adsPeriods.getStartTime(), adsPeriods.getEndTime());
                                    DBO.close();
                                    adsList.add(new AdsPeriods(adsPeriods.getAdvId(), adsPeriods.getStartTime(), adsPeriods.getEndTime(),
                                            adsPeriods.getStartDate(), adsPeriods.getEndDate(), adsPeriods.getDays(), AdsIds, true));

                                    btn.setVisibility(View.GONE);
                                    checkList.add(pos);
                                    ed_start.setEnabled(false);
                                    ed_end.setEnabled(false);
                                    rbImage.setEnabled(false);
                                    rbText.setEnabled(false);
                                    rbVideo.setEnabled(false);
                                    edAdsText.setEnabled(false);
                                    edAdsTitle.setEnabled(false);
                                    ivSelectImg.setClickable(false);
                                    ivSelectImg1.setClickable(false);
                                    ivSelectImg2.setClickable(false);
                                    ivSelectImg3.setClickable(false);
                                    ivSelectImg4.setClickable(false);
                                    ivSelectImg5.setClickable(false);
                                    ivSelectImg6.setClickable(false);
                                    ivSelectVideo.setClickable(false);
                                    ivSelectVideo1.setClickable(false);
                                    ivSelectVideo2.setClickable(false);
                                    cbSat.setEnabled(false);
                                    cbSun.setEnabled(false);
                                    cbMon.setEnabled(false);
                                    cbTue.setEnabled(false);
                                    cbWed.setEnabled(false);
                                    cbThu.setEnabled(false);
                                    cbFri.setEnabled(false);
                                    edEndTime.setEnabled(false);
                                    edStartTime.setEnabled(false);
                                    Utils.showCustomToast(activity, msg);
                                } else {
                                    Utils.showCustomToast(activity, getString(R.string.AdvNotAdded) + advId);
                                }

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
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        DatePickerDialog mDatePicker;
        Calendar cal = Calendar.getInstance();
        int calYear = cal.get(Calendar.YEAR);
        int calMonth = cal.get(Calendar.MONTH);
        int calDay = cal.get(Calendar.DAY_OF_MONTH);
        mDatePicker = new DatePickerDialog(activity, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                int MONTH = (month + 1);
                int DAY = day;
                String monthS = MONTH + "";
                String dayS = DAY + "";
                if (MONTH < 10)
                    monthS = "0" + MONTH;
                if (DAY < 10)
                    dayS = "0" + DAY;
                editText.setText(String.format(Locale.ENGLISH, getString(R.string.formatDate), String.valueOf(year), String.valueOf(monthS)
                        , String.valueOf(dayS)));

            }
        }, calYear, calMonth, calDay);
        mDatePicker.show();
    }

    public void showDateTimePicker(final EditText editText) {
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(activity, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                String hours = "" + selectedHour;
                if (selectedHour < 10)
                    hours = "0" + selectedHour;

                String minute = "" + selectedMinute;
                if (selectedMinute < 10)
                    minute = "0" + selectedMinute;
                editText.setText(String.format(Locale.ENGLISH, getString(R.string.formatTime), hours, minute));
            }
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

            List<String> permissionsToRequest = new ArrayList<>();
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED) {
                    permissionsToRequest.add(permission);
                }
            }
            if (!permissionsToRequest.isEmpty()) {
                ActivityCompat.requestPermissions(activity,
                        permissionsToRequest.toArray(new String[permissionsToRequest.size()]), requestCode);
            }
        } catch (Exception e) {
            Log.e("XXX", e.getLocalizedMessage());
        }
    }

    @Override
    public final void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                                 @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_PERMISSIONS) {
            if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                finish();
            }
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


    public abstract class AbstractCustomCursorLoader extends CursorLoader
    {
        private final Loader.ForceLoadContentObserver mObserver = new Loader.ForceLoadContentObserver();

        public AbstractCustomCursorLoader(Context context)
        {
            super(context);
        }

        @Override
        public Cursor loadInBackground()
        {
            Cursor cursor = getCursor();

            if (cursor != null)
            {
                // Ensure the cursor window is filled
                cursor.getCount();
                cursor.registerContentObserver(mObserver);
            }

            cursor.setNotificationUri(getContext().getContentResolver(), getContentUri());
            return cursor;
        }

        protected abstract Cursor getCursor();
        protected abstract Uri getContentUri();
    }


}

