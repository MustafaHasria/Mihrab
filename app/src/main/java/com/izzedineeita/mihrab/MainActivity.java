package com.izzedineeita.mihrab;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.izzedineeita.mihrab.activity.SettingActivity;
import com.izzedineeita.mihrab.activity.ShowAdsActivity;
import com.izzedineeita.mihrab.activity.ShowAlkhushueActivity;
import com.izzedineeita.mihrab.activity.ShowAzkarActivity;
import com.izzedineeita.mihrab.activity.ShowClosePhoneActivity;
import com.izzedineeita.mihrab.activity.ShowKatebActivity;
import com.izzedineeita.mihrab.activity.ShowKhotabActivity;
import com.izzedineeita.mihrab.constants.Constants;
import com.izzedineeita.mihrab.constants.DateHigri;
import com.izzedineeita.mihrab.database.DataBaseHelper;
import com.izzedineeita.mihrab.model.Ads;
import com.izzedineeita.mihrab.model.Khotab;
import com.izzedineeita.mihrab.utils.ImagesArrays;
import com.izzedineeita.mihrab.utils.Pref;
import com.izzedineeita.mihrab.utils.Utils;
import com.minew.beaconplus.sdk.MTCentralManager;
import com.minew.beaconplus.sdk.MTFrameHandler;
import com.minew.beaconplus.sdk.MTPeripheral;
import com.minew.beaconplus.sdk.enums.BluetoothState;
import com.minew.beaconplus.sdk.enums.ConnectionStatus;
import com.minew.beaconplus.sdk.enums.FrameType;
import com.minew.beaconplus.sdk.exception.MTException;
import com.minew.beaconplus.sdk.frames.AccFrame;
import com.minew.beaconplus.sdk.frames.ForceFrame;
import com.minew.beaconplus.sdk.frames.HTFrame;
import com.minew.beaconplus.sdk.frames.IBeaconFrame;
import com.minew.beaconplus.sdk.frames.LightFrame;
import com.minew.beaconplus.sdk.frames.MinewFrame;
import com.minew.beaconplus.sdk.frames.TemperatureFrame;
import com.minew.beaconplus.sdk.frames.TlmFrame;
import com.minew.beaconplus.sdk.frames.TvocFrame;
import com.minew.beaconplus.sdk.frames.UidFrame;
import com.minew.beaconplus.sdk.frames.UrlFrame;
import com.minew.beaconplus.sdk.interfaces.ConnectionStatueListener;
import com.minew.beaconplus.sdk.interfaces.GetPasswordListener;
import com.minew.beaconplus.sdk.interfaces.MTCentralManagerListener;
import com.minew.beaconplus.sdk.interfaces.OnBluetoothStateChangedListener;

import java.io.IOException;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

public class MainActivity extends AppCompatActivity {
    //region Variables
    // Time display ImageViews
    private ImageView imageCurrentTimeHourTens, imageCurrentTimeHourOnes, imageCurrentTimeMinuteTens, imageCurrentTimeMinuteOnes, imageCurrentTimeSecondOnes, imageCurrentTimeSecondTens;

    // Date display ImageViews
    private ImageView imageCurrentDay, imageCurrentDateMonthOnes, imageCureentDateMonthTens, imageCurrentDateMonth, dateYearThousandsDigit, dateYearHundredsDigit, dateYearTensDigit, dateYearOnesDigit;

    // Hijri date display ImageViews
    private ImageView hijriMonthTensDigit, hijriMonthOnesDigit, hijriMonthImage, hijriYearThousandsDigit, hijriYearHundredsDigit, hijriYearTensDigit, hijriYearOnesDigit;

    // Fajr prayer time ImageViews
    private ImageView fajrAzanHourTensDigit, fajrAzanHourOnesDigit, fajrAzanMinuteTensDigit, fajrAzanMinuteOnesDigit;
    private ImageView fajrIqamahHourTensDigit, fajrIqamahHourOnesDigit, fajrIqamahMinuteTensDigit, fajrIqamahMinuteOnesDigit;

    // Sunrise prayer time ImageViews
    private ImageView sunriseAzanHourTensDigit, sunriseAzanHourOnesDigit, sunriseAzanMinuteTensDigit, sunriseAzanMinuteOnesDigit;
    private ImageView sunriseIqamahHourTensDigit, sunriseIqamahHourOnesDigit, sunriseIqamahMinuteTensDigit, sunriseIqamahMinuteOnesDigit;

    // Dhuhr prayer time ImageViews
    private ImageView dhuhrAzanHourTensDigit, dhuhrAzanHourOnesDigit, dhuhrAzanMinuteTensDigit, dhuhrAzanMinuteOnesDigit;
    private ImageView dhuhrIqamahHourTensDigit, dhuhrIqamahHourOnesDigit, dhuhrIqamahMinuteTensDigit, dhuhrIqamahMinuteOnesDigit;

    // Asr prayer time ImageViews
    private ImageView asrAzanHourTensDigit, asrAzanHourOnesDigit, asrAzanMinuteTensDigit, asrAzanMinuteOnesDigit;
    private ImageView asrIqamahHourTensDigit, asrIqamahHourOnesDigit, asrIqamahMinuteTensDigit, asrIqamahMinuteOnesDigit;

    // Maghrib prayer time ImageViews
    private ImageView maghribAzanHourTensDigit, maghribAzanHourOnesDigit, maghribAzanMinuteTensDigit, maghribAzanMinuteOnesDigit;
    private ImageView maghribIqamahHourTensDigit, maghribIqamahHourOnesDigit, maghribIqamahMinuteTensDigit, maghribIqamahMinuteOnesDigit;

    // Isha prayer time ImageViews
    private ImageView ishaAzanHourTensDigit, ishaAzanHourOnesDigit, ishaAzanMinuteTensDigit, ishaAzanMinuteOnesDigit;
    private ImageView ishaIqamahHourTensDigit, ishaIqamahHourOnesDigit, ishaIqamahMinuteTensDigit, ishaIqamahMinuteOnesDigit;

    // Countdown timer ImageViews
    private ImageView azanCountdownHourTensDigit, azanCountdownHourOnesDigit, azanCountdownMinuteTensDigit, azanCountdownMinuteOnesDigit, azanCountdownSecondTensDigit, azanCountdownSecondOnesDigit;
    private ImageView iqamahCountdownMinuteTensDigit, iqamahCountdownMinuteOnesDigit, iqamahCountdownSecondTensDigit, iqamahCountdownSecondOnesDigit;

    // Friday prayer time ImageViews
    private ImageView imageAzanFridayTimeHourTens, imageAzanFridayTimeHourOnes, imageAzanFridayTimeMinuteTens, imageAzanFridayTimeMinuteOnes;

    // Next prayer indicator ImageViews
    private ImageView nextPrayerIndicator, nextPrayerIndicatorSecondary;

    // Layout containers
    private LinearLayout azanCountdownContainer, iqamahCountdownContainer;
    private TextView mosqueNameTextView, temperatureTextView;
    public Thread updateThread = null;

    // Prayer times (24-hour format)
    public String fajrTime24h, sunriseTime24h, dhuhrTime24h, asrTime24h, maghribTime24h, ishaTime24h;
    // Prayer times (12-hour format with AM/PM)
    public String fajrTime12h, sunriseTime12h, dhuhrTime12h, asrTime12h, maghribTime12h, ishaTime12h;

    private DataBaseHelper databaseHelper;
    public static int[] dayImages = new int[8];
    public static int[] monthImages = new int[13];
    public static int[] hijriMonthImages = new int[13];
    public static int[] digitImages = new int[10];
    public static int[] timeDigitImages = new int[10];
    public static int[] iqamahCountdownDigitImages = new int[10];
    public static int[] azanCountdownDigitImages = new int[10];
    public static int[] azanTimeDigitImages = new int[10];
    public static int[] iqamahTimeDigitImages = new int[10];
    public static int[] secondDigitImages = new int[10];
    private String[] fridayPrayerTimes = new String[6];
    private LinearLayout bottomLayout;

    private MediaPlayer mediaPlayer = new MediaPlayer();
    public static boolean isAdsScreenOpen = false;
    public static boolean isClosePhoneScreenOpen = false;
    public static boolean isAzkarScreenOpen = false;
    public static boolean isAlkhushueScreenOpen = false;
    public static boolean isKhotabScreenOpen = false;
    public boolean isNewsTextDisplayed = false;
    public static boolean isOpenClosePhone = false;
    public static boolean isOpenKhotabActivity = false;
    public static boolean isOpenAlkhushuePhone = false;
    public static boolean isOpenAzkarPhone = false;
    public static boolean isOpenAds = false;
    RelativeLayout mainLayout;
    String[] date = new String[7];
    int theme;
    float TEMPERATURE;
    public static int BATTERY;
    private MTCentralManager bluetoothCentralManager;
    private TextView newsTextView;
    public static Activity currentActivity;
    int selectedTheme;

    private static final int REQUEST_ENABLE_BT = 3;
    private static final int PERMISSION_COARSE_LOCATION = 2;
    public static float CURRENT_TEMPERATURE = 0;
    //endregion

    /**
     * Theme configuration data class to hold theme-specific settings
     * Optimized for memory efficiency with primitive types and minimal object overhead
     */
    private static class ThemeConfiguration {
        final int layoutResId;
        final int orientation;
        final int backgroundResId;
        final String newsTextColor;
        final int[] dayImages;
        final int[] monthImages;
        final int[] hijriMonthImages;
        final int[] digitImages;
        final int[] timeDigitImages;
        final int[] azanTimeDigitImages;
        final int[] iqamahTimeDigitImages;
        final int[] iqamahCountdownDigitImages;
        final int[] azanCountdownDigitImages;
        final int[] secondDigitImages;
        final byte hasNextPrayerIndicator; // Using byte instead of boolean for memory efficiency
        final byte hasNextPrayerIndicatorSecondary;

        ThemeConfiguration(int layoutResId, int orientation, int backgroundResId, String newsTextColor, int[] dayImages, int[] monthImages, int[] hijriMonthImages, int[] digitImages, int[] timeDigitImages, int[] azanTimeDigitImages, int[] iqamahTimeDigitImages, int[] iqamahCountdownDigitImages, int[] azanCountdownDigitImages, int[] secondDigitImages, boolean hasNextPrayerIndicator, boolean hasNextPrayerIndicatorSecondary) {
            this.layoutResId = layoutResId;
            this.orientation = orientation;
            this.backgroundResId = backgroundResId;
            this.newsTextColor = newsTextColor;
            this.dayImages = dayImages;
            this.monthImages = monthImages;
            this.hijriMonthImages = hijriMonthImages;
            this.digitImages = digitImages;
            this.timeDigitImages = timeDigitImages;
            this.azanTimeDigitImages = azanTimeDigitImages;
            this.iqamahTimeDigitImages = iqamahTimeDigitImages;
            this.iqamahCountdownDigitImages = iqamahCountdownDigitImages;
            this.azanCountdownDigitImages = azanCountdownDigitImages;
            this.secondDigitImages = secondDigitImages;
            this.hasNextPrayerIndicator = (byte) (hasNextPrayerIndicator ? 1 : 0);
            this.hasNextPrayerIndicatorSecondary = (byte) (hasNextPrayerIndicatorSecondary ? 1 : 0);
        }
    }

    /**
     * Theme configurations map for easy lookup
     * Using lazy initialization for better startup performance
     */
    private static volatile Map<Integer, ThemeConfiguration> THEME_CONFIGURATIONS;

    /**
     * Thread-safe lazy initialization of theme configurations
     * This improves app startup time by deferring configuration creation
     */
    private static Map<Integer, ThemeConfiguration> getThemeConfigurations() {
        if (THEME_CONFIGURATIONS == null) {
            synchronized (MainActivity.class) {
                if (THEME_CONFIGURATIONS == null) {
                    THEME_CONFIGURATIONS = new HashMap<>();
                    initializeThemeConfigurations();
                }
            }
        }
        return THEME_CONFIGURATIONS;
    }

    /**
     * Initialize theme configurations - called only once when first needed
     */
    private static void initializeThemeConfigurations() {
        // Theme 0 (default)
        THEME_CONFIGURATIONS.put(0, new ThemeConfiguration(R.layout.activity_main, ActivityInfo.SCREEN_ORIENTATION_PORTRAIT, R.drawable.background_main, null, ImagesArrays.daysImageTheme1, ImagesArrays.monthImageTheme1, ImagesArrays.monthImageHijriTheme1, ImagesArrays.dateNumberTheme1, ImagesArrays.timeNumberTheme1, ImagesArrays.timeNumberAzanTheme1, ImagesArrays.timeNumberIqamhTheme1, ImagesArrays.timeNumberTheme1, ImagesArrays.timeNumberTheme1, ImagesArrays.timeNumberTheme1, false, true));

        // Theme 1
        THEME_CONFIGURATIONS.put(1, new ThemeConfiguration(R.layout.activity_main, ActivityInfo.SCREEN_ORIENTATION_PORTRAIT, R.drawable.background_main_them_1, "#ED0606", ImagesArrays.daysImageTheme1, ImagesArrays.monthImageTheme1, ImagesArrays.monthImageHijriTheme1, ImagesArrays.dateNumberTheme1, ImagesArrays.timeNumberTheme1, ImagesArrays.timeNumberAzanTheme1, ImagesArrays.timeNumberIqamhTheme1, ImagesArrays.timeNumberTheme1, ImagesArrays.timeNumberTheme1, ImagesArrays.timeNumberTheme1, false, false));

        // Theme 2
        THEME_CONFIGURATIONS.put(2, new ThemeConfiguration(R.layout.activity_main, ActivityInfo.SCREEN_ORIENTATION_PORTRAIT, R.drawable.background_main_them_2, "#ED0606", ImagesArrays.daysImageTheme1, ImagesArrays.monthImageTheme1, ImagesArrays.monthImageHijriTheme1, ImagesArrays.dateNumberTheme1, ImagesArrays.timeNumberTheme1, ImagesArrays.timeNumberAzanTheme1, ImagesArrays.timeNumberIqamhTheme1, ImagesArrays.timeNumberTheme1, ImagesArrays.timeNumberTheme1, ImagesArrays.timeNumberTheme1, false, false));

        // Theme 3
        THEME_CONFIGURATIONS.put(3, new ThemeConfiguration(R.layout.activity_main_4, ActivityInfo.SCREEN_ORIENTATION_PORTRAIT, 0, // No background drawable
                null, ImagesArrays.daysImageTheme4, ImagesArrays.monthImageTheme4, ImagesArrays.monthImageHijriTheme4, ImagesArrays.timeNumberTheme4, ImagesArrays.timeNumberTheme4, ImagesArrays.timeNumberIqamhTheme4, ImagesArrays.timeNumberIqamhTheme4, ImagesArrays.timeNumberIqamhLeft4, ImagesArrays.timeNumberIqamhLeft4, ImagesArrays.timeNumberIqamhTheme4, true, false));

        // Theme 4
        THEME_CONFIGURATIONS.put(4, new ThemeConfiguration(R.layout.activity_main_5, ActivityInfo.SCREEN_ORIENTATION_PORTRAIT, 0, // No background drawable
                null, ImagesArrays.daysImageTheme4, ImagesArrays.monthImageTheme5, ImagesArrays.monthImageHijriTheme5, ImagesArrays.timeNumberTheme5, ImagesArrays.timeNumberTheme5, ImagesArrays.timeNumberIqamhTheme4, ImagesArrays.timeNumberIqamhTheme4, ImagesArrays.timeNumberIqamhLeft4, ImagesArrays.timeNumberIqamhLeft4, ImagesArrays.timeNumberIqamhTheme4, false, false));

        // Theme 5
        THEME_CONFIGURATIONS.put(5, new ThemeConfiguration(R.layout.activity_main_6, ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE, 0, // No background drawable
                null, ImagesArrays.daysImageTheme6, ImagesArrays.monthImageTheme6, ImagesArrays.monthImageHijriTheme6, ImagesArrays.timeNumberIqamhLeft4, ImagesArrays.timeNumberTheme6, ImagesArrays.timeNumberAzanTheme6, ImagesArrays.timeNumberIqama6, ImagesArrays.timeNumberTheme6, ImagesArrays.timeNumberTheme6, ImagesArrays.timeNumberTheme6, false, false));

        // Theme 6
        THEME_CONFIGURATIONS.put(6, new ThemeConfiguration(R.layout.activity_main_7, ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE, 0, // No background drawable
                null, ImagesArrays.daysImageTheme1, ImagesArrays.monthImageTheme1, ImagesArrays.monthImageHijriTheme1, ImagesArrays.dateNumberTheme1, ImagesArrays.timeNumberIqamhLeft7, ImagesArrays.timeNumberAzanTheme6, ImagesArrays.timeNumberIqama6, ImagesArrays.timeNumberIqamhLeft7, ImagesArrays.timeNumberIqamhLeft7, ImagesArrays.timeNumberIqamhLeft7, true, false));

        // Theme 7
        THEME_CONFIGURATIONS.put(7, new ThemeConfiguration(R.layout.activity_main_8, ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE, 0, // No background drawable
                null, ImagesArrays.daysImageTheme8, ImagesArrays.monthImageTheme8, ImagesArrays.monthImageHijriTheme8, ImagesArrays.timeNumberDate8, ImagesArrays.timeNumberTime8, ImagesArrays.timeNumberAzIq8, ImagesArrays.timeNumberAzIq8, ImagesArrays.timeNumberTime8, ImagesArrays.timeNumberTime8, ImagesArrays.timeNumberTime8, true, false));

        // Theme 8
        THEME_CONFIGURATIONS.put(8, new ThemeConfiguration(R.layout.activity_main_9, ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE, 0, // No background drawable
                null, ImagesArrays.daysImageTheme1, ImagesArrays.monthImageTheme1, ImagesArrays.monthImageHijriTheme1, ImagesArrays.dateNumberTheme1, ImagesArrays.timeNumberTheme6, ImagesArrays.timeNumberAzanTheme6, ImagesArrays.timeNumberIqama6, ImagesArrays.timeNumberTheme6, ImagesArrays.timeNumberTheme6, ImagesArrays.timeNumberTheme6, false, false));

        // Theme 9
        THEME_CONFIGURATIONS.put(9, new ThemeConfiguration(R.layout.activity_main_10, ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE, 0, // No background drawable
                null, ImagesArrays.daysImageTheme10, ImagesArrays.monthImageTheme10, ImagesArrays.monthImageHijriTheme10, ImagesArrays.timeNumberDate10, ImagesArrays.timeNumberIqamhLeft4, ImagesArrays.timeNumberAzIq8, ImagesArrays.timeNumberAzIq8, ImagesArrays.timeNumberIqamhLeft4, ImagesArrays.timeNumberIqamhLeft4, ImagesArrays.timeNumberDate10, false, true));
        THEME_CONFIGURATIONS.put(10, new ThemeConfiguration(R.layout.activity_main_new_theme_11, ActivityInfo.SCREEN_ORIENTATION_PORTRAIT, 0, // No background drawable
                null, ImagesArrays.daysImageTheme10, ImagesArrays.monthImageTheme10, ImagesArrays.monthImageHijriTheme10, ImagesArrays.timeNumberDate10, ImagesArrays.timeNumberIqamhLeft4, ImagesArrays.timeNumberAzIq8, ImagesArrays.timeNumberAzIq8, ImagesArrays.timeNumberIqamhLeft4, ImagesArrays.timeNumberIqamhLeft4, ImagesArrays.timeNumberDate10, false, true));
    }

    class CountDownRunner implements Runnable {

        public void run() {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    refreshUIEverySecond();
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } catch (Exception e) {

                }
            }
        }

    }

    //region Life cycle
    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLocale("en");
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        // Reset flags for testing
        resetActivityFlags();

        // Force open close phone activity for testing

        selectedTheme = Pref.getValue(getApplicationContext(), Constants.PREF_THEM_POSITION_SELECTED, 11);

//        selectedTheme =11;
        // Apply theme configuration only if not theme 11
        if (selectedTheme != 11) {
            applyThemeConfiguration(selectedTheme);
        } else {
            // For theme 11, set up the layout directly without theme configuration
            setContentView(R.layout.activity_main_new_theme_11);
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

            // Initialize common views for theme 11
            newsTextView = findViewById(R.id.activity_main_new_theme_11_text_view_news);
        }

        currentActivity = MainActivity.this;

        DateHigri dateHigri = new DateHigri();
        date = Utils.writeIslamicDate(MainActivity.this, dateHigri);

        // Add null check for date array
        if (date.length < 7) {
            date = new String[]{"1", "1", "1", "2024", "1", "1", "2024"};
        }

        int hijriDiff1 = Pref.getValue(MainActivity.this, Constants.PREF_HEJRY_INT1, 0);
        int iii = Integer.parseInt(date[4]);
        date[4] = String.valueOf(iii + hijriDiff1);

        databaseHelper = new DataBaseHelper(getApplicationContext());

        newsTextView.setSelected(true);

        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter != null) {
            if (!isBLEEnabled()) {
                showBLEDialog();
            }
        }

        initializeBluetoothManager();
        requestLocationPermissions();
        initializeBluetoothListener();
        initializeViewsAndListeners();
        updatePrayerAndDateDisplays();

        // Set up back button handling using the modern approach
        getOnBackPressedDispatcher().addCallback(this, new androidx.activity.OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                // Show confirmation dialog for app exit
                new android.app.AlertDialog.Builder(MainActivity.this).setTitle("Exit App").setMessage("Do you want to exit the app?").setPositiveButton("Exit", (dialog, which) -> {
                    // Properly clean up resources before finishing
                    cleanupAndExit();
                }).setNegativeButton("Cancel", null).show();
            }
        });

        Runnable runnable = new CountDownRunner();
        updateThread = new Thread(runnable);
        updateThread.start();
    }

    // Note: onBackPressed() is deprecated. Using OnBackPressedDispatcher in onCreate() instead.

    /**
     * Clean up all resources and exit the app
     */
    private void cleanupAndExit() {
        // Clean up the update thread
        if (updateThread != null && updateThread.isAlive()) {
            updateThread.interrupt();
            try {
                updateThread.join(1000); // Wait up to 1 second for thread to finish
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        // Stop media player if playing
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer.release();
        }

        // Stop bluetooth service
        if (bluetoothCentralManager != null) {
            bluetoothCentralManager.stopService();
        }

        // Reset static flags to prevent issues with new instances
        resetActivityFlags();

        // Clear current activity reference
        currentActivity = null;

        // Finish the activity properly
        finish();

        // Force close the app
        System.exit(0);
    }

    @Override
    public void onPause() {
        super.onPause();
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        mosqueNameTextView.setText(Pref.getValue(getApplicationContext(), Constants.PREF_MASGED_NAME, "اسم المسجد"));
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        // Clean up the update thread
        if (updateThread != null && updateThread.isAlive()) {
            updateThread.interrupt();
            try {
                updateThread.join(1000); // Wait up to 1 second for thread to finish
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        // Clean up media player
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
            }
            mediaPlayer.release();
            mediaPlayer = null;
        }

        // Stop bluetooth service
        if (bluetoothCentralManager != null) {
            bluetoothCentralManager.stopService();
            bluetoothCentralManager = null;
        }

        // Reset static flags
        resetActivityFlags();

        // Clear current activity reference
        currentActivity = null;

        super.onDestroy();
    }

    @Override
    public void onRequestPermissionsResult(int code, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(code, permissions, grantResults);
        if (code == PERMISSION_COARSE_LOCATION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                initData();
            } else {
                finish();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_ENABLE_BT) {
            if (resultCode == Activity.RESULT_OK) {
                initData();
            } else {
                finish();
            }
        }
    }

    //endregion
    protected boolean isBLEEnabled() {
        final BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        final BluetoothAdapter adapter = bluetoothManager.getAdapter();
        return adapter != null && adapter.isEnabled();
    }

    private void showBLEDialog() {
        final Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
    }

    private void setLocale(String lang) {
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
    }

    private void initializeViewsAndListeners() {
        findViewById(R.id.img_settings).setOnClickListener(v -> {
            try {
                startActivity(new Intent(MainActivity.this, SettingActivity.class));
            } catch (Exception ignored) {
            }
        });

        // Skip old theme views for theme 11
        if (selectedTheme != 11) {
            bottomLayout = findViewById(R.id.img_bottom);
            mosqueNameTextView = findViewById(R.id.tv_masged_name);
            temperatureTextView = findViewById(R.id.tv_internal_heat);

            LinearLayout linearLayoutShowSensor = findViewById(R.id.linear_layout_show_sensor);
            LinearLayout linearLayoutShowFridayTime = findViewById(R.id.linear_layout_show_friday_time);

            if (Pref.getValue(getApplicationContext(), Constants.PREF_SENSOR_SHOW, false)) {
                linearLayoutShowSensor.setVisibility(View.VISIBLE);
                linearLayoutShowFridayTime.setVisibility(View.GONE);
            } else {
                linearLayoutShowSensor.setVisibility(View.GONE);
                linearLayoutShowFridayTime.setVisibility(View.VISIBLE);
            }

            mosqueNameTextView.setText(Pref.getValue(getApplicationContext(), Constants.PREF_MASGED_NAME, "اسم المسجد"));

            imageCurrentTimeHourTens = findViewById(R.id.image_current_time_hour_tens);
            imageCurrentTimeHourOnes = findViewById(R.id.image_current_time_hour_ones);
            imageCurrentTimeMinuteTens = findViewById(R.id.image_current_time_minute_tens);
            imageCurrentTimeMinuteOnes = findViewById(R.id.image_current_time_minute_ones);
            imageCurrentTimeSecondOnes = findViewById(R.id.image_current_time_second_ones);
            imageCurrentTimeSecondTens = findViewById(R.id.image_current_time_second_tens);

            imageCurrentDay = findViewById(R.id.image_current_day);

            imageCurrentDateMonthOnes = findViewById(R.id.image_current_date_month_ones);
            imageCureentDateMonthTens = findViewById(R.id.image_current_date_month_tens);
            imageCurrentDateMonth = findViewById(R.id.image_current_date_month);
            dateYearThousandsDigit = findViewById(R.id.img_date_years_4);
            dateYearHundredsDigit = findViewById(R.id.img_date_years_3);
            dateYearTensDigit = findViewById(R.id.img_date_years_2);
            dateYearOnesDigit = findViewById(R.id.img_date_years_1);

            hijriMonthTensDigit = findViewById(R.id.img_date_month_h_2);
            hijriMonthOnesDigit = findViewById(R.id.img_date_month_h_1);
            hijriMonthImage = findViewById(R.id.img_date_month_h);
            hijriYearThousandsDigit = findViewById(R.id.img_date_years_h_4);
            hijriYearHundredsDigit = findViewById(R.id.img_date_years_h_3);
            hijriYearTensDigit = findViewById(R.id.img_date_years_h_2);
            hijriYearOnesDigit = findViewById(R.id.img_date_years_h_1);

            nextPrayerIndicator = findViewById(R.id.img_next_azan);

            fajrAzanHourTensDigit = findViewById(R.id.img_fjr_azan_h_1);
            fajrAzanHourOnesDigit = findViewById(R.id.img_fjr_azan_h_2);
            fajrAzanMinuteTensDigit = findViewById(R.id.img_fjr_azan_m_1);
            fajrAzanMinuteOnesDigit = findViewById(R.id.img_fjr_azan_m_2);

            sunriseAzanHourTensDigit = findViewById(R.id.img_shroq_azan_h_1);
            sunriseAzanHourOnesDigit = findViewById(R.id.img_shroq_azan_h_2);
            sunriseAzanMinuteTensDigit = findViewById(R.id.img_shroq_azan_m_1);
            sunriseAzanMinuteOnesDigit = findViewById(R.id.img_shroq_azan_m_2);

            dhuhrAzanHourTensDigit = findViewById(R.id.img_daheq_azan_h_1);
            dhuhrAzanHourOnesDigit = findViewById(R.id.img_daheq_azan_h_2);
            dhuhrAzanMinuteTensDigit = findViewById(R.id.img_daheq_azan_m_1);
            dhuhrAzanMinuteOnesDigit = findViewById(R.id.img_daheq_azan_m_2);

            asrAzanHourTensDigit = findViewById(R.id.img_asr_azan_h_1);
            asrAzanHourOnesDigit = findViewById(R.id.img_asr_azan_h_2);
            asrAzanMinuteTensDigit = findViewById(R.id.img_asr_azan_m_1);
            asrAzanMinuteOnesDigit = findViewById(R.id.img_asr_azan_m_2);

            maghribAzanHourTensDigit = findViewById(R.id.img_mgrb_azan_h_1);
            maghribAzanHourOnesDigit = findViewById(R.id.img_mgrb_azan_h_2);
            maghribAzanMinuteTensDigit = findViewById(R.id.img_mgrb_azan_m_1);
            maghribAzanMinuteOnesDigit = findViewById(R.id.img_mgrb_azan_m_2);

            ishaAzanHourTensDigit = findViewById(R.id.img_isha_azan_h_1);
            ishaAzanHourOnesDigit = findViewById(R.id.img_isha_azan_h_2);
            ishaAzanMinuteTensDigit = findViewById(R.id.img_isha_azan_m_1);
            ishaAzanMinuteOnesDigit = findViewById(R.id.img_isha_azan_m_2);

            /* init fjr iqamh images */
            fajrIqamahHourTensDigit = findViewById(R.id.img_fjr_iqamh_h_1);
            fajrIqamahHourOnesDigit = findViewById(R.id.img_fjr_iqamh_h_2);
            fajrIqamahMinuteTensDigit = findViewById(R.id.img_fjr_iqamh_m_1);
            fajrIqamahMinuteOnesDigit = findViewById(R.id.img_fjr_iqamh_m_2);

            /* init shroq iqamh images */
            sunriseIqamahHourTensDigit = findViewById(R.id.img_shroq_iqamh_h_1);
            sunriseIqamahHourOnesDigit = findViewById(R.id.img_shroq_iqamh_h_2);
            sunriseIqamahMinuteTensDigit = findViewById(R.id.img_shroq_iqamh_m_1);
            sunriseIqamahMinuteOnesDigit = findViewById(R.id.img_shroq_iqamh_m_2);

            /* init dahr iqamg images */
            dhuhrIqamahHourTensDigit = findViewById(R.id.img_daheq_iqamg_h_1);
            dhuhrIqamahHourOnesDigit = findViewById(R.id.img_daheq_iqamg_h_2);
            dhuhrIqamahMinuteTensDigit = findViewById(R.id.img_daheq_iqamg_m_1);
            dhuhrIqamahMinuteOnesDigit = findViewById(R.id.img_daheq_iqamg_m_2);

            /* init asr iqamg images */
            asrIqamahHourTensDigit = findViewById(R.id.img_asr_iqamg_h_1);
            asrIqamahHourOnesDigit = findViewById(R.id.img_asr_iqamg_h_2);
            asrIqamahMinuteTensDigit = findViewById(R.id.img_asr_iqamg_m_1);
            asrIqamahMinuteOnesDigit = findViewById(R.id.img_asr_iqamg_m_2);

            /* init mgrb iqamg images */
            maghribIqamahHourTensDigit = findViewById(R.id.img_mgrb_iqamg_h_1);
            maghribIqamahHourOnesDigit = findViewById(R.id.img_mgrb_iqamg_h_2);
            maghribIqamahMinuteTensDigit = findViewById(R.id.img_mgrb_iqamg_m_1);
            maghribIqamahMinuteOnesDigit = findViewById(R.id.img_mgrb_iqamg_m_2);

            /* init isha iqamg images */
            ishaIqamahHourTensDigit = findViewById(R.id.img_isha_iqamh_h_1);
            ishaIqamahHourOnesDigit = findViewById(R.id.img_isha_iqamh_h_2);
            ishaIqamahMinuteTensDigit = findViewById(R.id.img_isha_iqamh_m_1);
            ishaIqamahMinuteOnesDigit = findViewById(R.id.img_isha_iqamh_m_2);

            /* init azan time left image */
            azanCountdownHourTensDigit = findViewById(R.id.img_time_left_h_1);
            azanCountdownHourOnesDigit = findViewById(R.id.img_time_left_h_2);
            azanCountdownMinuteTensDigit = findViewById(R.id.img_time_left_m_1);
            azanCountdownMinuteOnesDigit = findViewById(R.id.img_time_left_m_2);
            azanCountdownSecondTensDigit = findViewById(R.id.img_time_left_s_1);
            azanCountdownSecondOnesDigit = findViewById(R.id.img_time_left_s_2);

            /* init iqamh time left image */
            iqamahCountdownMinuteTensDigit = findViewById(R.id.img_iqamh_time_left_m_1);
            iqamahCountdownMinuteOnesDigit = findViewById(R.id.img_iqamh_time_left_m_2);
            iqamahCountdownSecondTensDigit = findViewById(R.id.img_iqamh_time_left_s_1);
            iqamahCountdownSecondOnesDigit = findViewById(R.id.img_iqamh_time_left_s_2);

            /* init LinearLayout left */
            azanCountdownContainer = findViewById(R.id.lay_img_azan_time_left);
            iqamahCountdownContainer = findViewById(R.id.lay_img_iqamh_time_left);

            imageAzanFridayTimeHourTens = findViewById(R.id.image_azan_friday_time_hour_tens);
            imageAzanFridayTimeHourOnes = findViewById(R.id.image_azan_friday_time_hour_ones);
            imageAzanFridayTimeMinuteTens = findViewById(R.id.image_azan_friday_time_minute_tens);
            imageAzanFridayTimeMinuteOnes = findViewById(R.id.image_azan_friday_time_minute_ones);
        } else {
            // For theme 11, initialize only the views that exist
            mosqueNameTextView = findViewById(R.id.activity_main_new_theme_11_text_view_masjid_name);
            temperatureTextView = findViewById(R.id.tv_internal_heat);

            if (mosqueNameTextView != null) {
                mosqueNameTextView.setText(Pref.getValue(getApplicationContext(), Constants.PREF_MASGED_NAME, "اسم المسجد"));
            }
        }
    }

    private void updatePrayerAndDateDisplays() {
        try {
            // For theme 11, skip the old image-based updates since it uses TextViews
            if (selectedTheme == 11) {
                // Theme 11 uses TextViews, so we don't need to update ImageViews
                // Just handle news text if needed
                if (newsTextView != null) {
                    newsTextView.setSelected(true);
                }
                return;
            }

            // Check if views are initialized
            if (newsTextView == null || bottomLayout == null) {
                return;
            }

            newsTextView.setSelected(true);

            // Initialize databaseHelper if null
            if (databaseHelper == null) {
                databaseHelper = new DataBaseHelper(getApplicationContext());
            }

            String text = databaseHelper.getNews(Utils.getFormattedCurrentDate());

            if (text != null) {
                bottomLayout.setVisibility(View.GONE);
                newsTextView.setVisibility(View.VISIBLE);
                if (!isNewsTextDisplayed) {
                    if (text.length() <= 50) {
                        text = "                                            " + text;
                    }
                    newsTextView.setText(text);
                    isNewsTextDisplayed = true;
                }
            } else {
                bottomLayout.setVisibility(View.VISIBLE);
                newsTextView.setVisibility(View.GONE);
                isNewsTextDisplayed = false;
            }

            Calendar today1 = Calendar.getInstance();

            SimpleDateFormat format2 = new SimpleDateFormat("M/d", Locale.ENGLISH);
            SimpleDateFormat format1 = new SimpleDateFormat("yy", Locale.ENGLISH);

            databaseHelper = new DataBaseHelper(getApplicationContext());
            String[] cityEn = getResources().getStringArray(R.array.city_name_en);
            String[] d = null;
            int cityChose = Pref.getValue(getApplicationContext(), Constants.PREF_CITY_POSITION_SELECTED, 90);
            if (cityChose != 0) {
                d = databaseHelper.getPrayerTimes(cityEn[cityChose], format2.format(today1.getTime()) + "/" + getYears(Integer.parseInt(format1.format(today1.getTime()))));
            }

            // Add null check for prayer times
            if (d == null || d.length < 7) {
                // Set default prayer times if database returns null
                d = new String[]{"", "05:30", "06:30", "12:30", "15:30", "18:30", "20:30"};
            }

            fajrTime12h = d[1];
            sunriseTime12h = d[2];
            dhuhrTime12h = d[3];
            asrTime12h = d[4];
            maghribTime12h = d[5];
            ishaTime12h = d[6];

            fajrTime24h = d[1];
            sunriseTime24h = d[2];
            dhuhrTime24h = d[3];
            asrTime24h = d[4];
            maghribTime24h = d[5];
            ishaTime24h = d[6];

            SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mmaa", Locale.ENGLISH);
            SimpleDateFormat dateFormat2 = new SimpleDateFormat("hh:mm", Locale.ENGLISH);
            try {
                Date date = dateFormat2.parse(fajrTime12h);
                assert date != null;
                fajrTime12h = dateFormat.format(date);
                Date date1 = dateFormat2.parse(sunriseTime12h);
                assert date1 != null;
                sunriseTime12h = dateFormat.format(date1);
                Date date2 = dateFormat2.parse(dhuhrTime12h);
                assert date2 != null;
                dhuhrTime12h = dateFormat.format(date2);
                Date date3 = dateFormat2.parse(asrTime12h);
                assert date3 != null;
                asrTime12h = dateFormat.format(date3);
                Date date4 = dateFormat2.parse(maghribTime12h);
                assert date4 != null;
                maghribTime12h = dateFormat.format(date4);
                Date date5 = dateFormat2.parse(ishaTime12h);
                assert date5 != null;
                ishaTime12h = dateFormat.format(date5);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            Pref.setValue(getApplicationContext(), Constants.PREF_FAJR_TIME_24, fajrTime24h);
            Pref.setValue(getApplicationContext(), Constants.PREF_FAJR_TIME, fajrTime12h);
            Pref.setValue(getApplicationContext(), Constants.PREF_SUNRISE_TIME_24, sunriseTime24h);
            Pref.setValue(getApplicationContext(), Constants.PREF_SUNRISE_TIME, sunriseTime12h);
            Pref.setValue(getApplicationContext(), Constants.PREF_DHOHR_TIME_24, dhuhrTime24h);
            Pref.setValue(getApplicationContext(), Constants.PREF_DHOHR_TIME, dhuhrTime12h);
            Pref.setValue(getApplicationContext(), Constants.PREF_ASR_TIME_24, asrTime24h);
            Pref.setValue(getApplicationContext(), Constants.PREF_ASR_TIME, asrTime12h);
            Pref.setValue(getApplicationContext(), Constants.PREF_MAGHRIB_TIME_24, maghribTime24h);
            Pref.setValue(getApplicationContext(), Constants.PREF_MAGHRIB_TIME, maghribTime12h);
            Pref.setValue(getApplicationContext(), Constants.PREF_ISHA_TIME_24, ishaTime24h);
            Pref.setValue(getApplicationContext(), Constants.PREF_ISHA_TIME, ishaTime12h);

            if (fajrTime24h.length() == 4) {
                fajrTime24h = "0" + fajrTime24h;
            }
            if (sunriseTime24h.length() == 4) {
                sunriseTime24h = "0" + sunriseTime24h;
            }
            if (dhuhrTime24h.length() == 4) {
                dhuhrTime24h = "0" + dhuhrTime24h;
            }
            if (asrTime24h.length() == 4) {
                asrTime24h = "0" + asrTime24h;
            }
            if (maghribTime24h.length() == 4) {
                maghribTime24h = "0" + maghribTime24h;
            }
            if (ishaTime24h.length() == 4) {
                ishaTime24h = "0" + ishaTime24h;
            }


            DateHigri hd = new DateHigri();
            date = Utils.writeIslamicDate(MainActivity.this, hd);

            // Add null check for date array
            if (date.length < 7) {
                date = new String[]{"1", "1", "1", "2024", "1", "1", "2024"};
            }

            int hijriDiff1 = Pref.getValue(MainActivity.this, Constants.PREF_HEJRY_INT1, 0);
            int iii = Integer.parseInt(date[4]);
            date[4] = String.valueOf(iii + hijriDiff1);


            /* set images date */
            if (dayImages != null && date != null && date.length > 0 && imageCurrentDay != null) {
                imageCurrentDay.setImageResource(dayImages[Integer.parseInt(date[0])]);
            }
            if (digitImages != null && date != null && date.length > 1 && imageCurrentDateMonthOnes != null) {
                imageCurrentDateMonthOnes.setImageResource(digitImages[Integer.parseInt(String.valueOf(date[1].charAt(0)))]);
                if (date[1].length() != 1 && imageCureentDateMonthTens != null) {
                    imageCureentDateMonthTens.setVisibility(View.VISIBLE);
                    imageCureentDateMonthTens.setImageResource(digitImages[Integer.parseInt(String.valueOf(date[1].charAt(1)))]);
                } else if (imageCureentDateMonthTens != null) {
                    imageCureentDateMonthTens.setVisibility(View.GONE);
                }
            }
            if (monthImages != null && date != null && date.length > 2 && imageCurrentDateMonth != null) {
                imageCurrentDateMonth.setImageResource(monthImages[Integer.parseInt(date[2])]);
            }
            if (digitImages != null && date != null && date.length > 3 && dateYearThousandsDigit != null && dateYearHundredsDigit != null && dateYearTensDigit != null && dateYearOnesDigit != null) {
                dateYearThousandsDigit.setImageResource(digitImages[Integer.parseInt(String.valueOf(date[3].charAt(0)))]);
                dateYearHundredsDigit.setImageResource(digitImages[Integer.parseInt(String.valueOf(date[3].charAt(1)))]);
                dateYearTensDigit.setImageResource(digitImages[Integer.parseInt(String.valueOf(date[3].charAt(2)))]);
                dateYearOnesDigit.setImageResource(digitImages[Integer.parseInt(String.valueOf(date[3].charAt(3)))]);
            }
            if (digitImages != null && date != null && date.length > 4 && hijriMonthTensDigit != null) {
                hijriMonthTensDigit.setImageResource(digitImages[Integer.parseInt(String.valueOf(date[4].charAt(0)))]);
                if (date[4].length() != 1 && hijriMonthOnesDigit != null) {
                    hijriMonthOnesDigit.setVisibility(View.VISIBLE);
                    hijriMonthOnesDigit.setImageResource(digitImages[Integer.parseInt(String.valueOf(date[4].charAt(1)))]);
                } else if (hijriMonthOnesDigit != null) {
                    hijriMonthOnesDigit.setVisibility(View.GONE);
                }
            }
            if (hijriMonthImages != null && date != null && date.length > 5 && hijriMonthImage != null) {
                hijriMonthImage.setImageResource(hijriMonthImages[Integer.parseInt(date[5]) - 1]);
            }
            if (digitImages != null && date != null && date.length > 6 && hijriYearThousandsDigit != null && hijriYearHundredsDigit != null && hijriYearTensDigit != null && hijriYearOnesDigit != null) {
                hijriYearThousandsDigit.setImageResource(digitImages[Integer.parseInt(String.valueOf(date[6].charAt(0)))]);
                hijriYearHundredsDigit.setImageResource(digitImages[Integer.parseInt(String.valueOf(date[6].charAt(1)))]);
                hijriYearTensDigit.setImageResource(digitImages[Integer.parseInt(String.valueOf(date[6].charAt(2)))]);
                hijriYearOnesDigit.setImageResource(digitImages[Integer.parseInt(String.valueOf(date[6].charAt(3)))]);
            }

            /* set images azan fajr */
            if (azanTimeDigitImages != null && fajrTime12h != null && fajrTime12h.length() >= 5 && fajrAzanHourTensDigit != null && fajrAzanHourOnesDigit != null && fajrAzanMinuteTensDigit != null && fajrAzanMinuteOnesDigit != null) {
                fajrAzanHourTensDigit.setImageResource(azanTimeDigitImages[Integer.parseInt(String.valueOf(fajrTime12h.charAt(0)))]);
                fajrAzanHourOnesDigit.setImageResource(azanTimeDigitImages[Integer.parseInt(String.valueOf(fajrTime12h.charAt(1)))]);

                fajrAzanMinuteTensDigit.setImageResource(azanTimeDigitImages[Integer.parseInt(String.valueOf(fajrTime12h.charAt(3)))]);
                fajrAzanMinuteOnesDigit.setImageResource(azanTimeDigitImages[Integer.parseInt(String.valueOf(fajrTime12h.charAt(4)))]);
            }

            /* set images azan shroq */
            if (azanTimeDigitImages != null && sunriseTime12h != null && sunriseTime12h.length() >= 5 && sunriseAzanHourTensDigit != null && sunriseAzanHourOnesDigit != null && sunriseAzanMinuteTensDigit != null && sunriseAzanMinuteOnesDigit != null) {
                sunriseAzanHourTensDigit.setImageResource(azanTimeDigitImages[Integer.parseInt(String.valueOf(sunriseTime12h.charAt(0)))]);
                sunriseAzanHourOnesDigit.setImageResource(azanTimeDigitImages[Integer.parseInt(String.valueOf(sunriseTime12h.charAt(1)))]);

                sunriseAzanMinuteTensDigit.setImageResource(azanTimeDigitImages[Integer.parseInt(String.valueOf(sunriseTime12h.charAt(3)))]);
                sunriseAzanMinuteOnesDigit.setImageResource(azanTimeDigitImages[Integer.parseInt(String.valueOf(sunriseTime12h.charAt(4)))]);
            }


            /* set images azan dahr */
            if (azanTimeDigitImages != null && dhuhrTime12h != null && dhuhrTime12h.length() >= 5 && dhuhrAzanHourTensDigit != null && dhuhrAzanHourOnesDigit != null && dhuhrAzanMinuteTensDigit != null && dhuhrAzanMinuteOnesDigit != null) {
                dhuhrAzanHourTensDigit.setImageResource(azanTimeDigitImages[Integer.parseInt(String.valueOf(dhuhrTime12h.charAt(0)))]);
                dhuhrAzanHourOnesDigit.setImageResource(azanTimeDigitImages[Integer.parseInt(String.valueOf(dhuhrTime12h.charAt(1)))]);

                dhuhrAzanMinuteTensDigit.setImageResource(azanTimeDigitImages[Integer.parseInt(String.valueOf(dhuhrTime12h.charAt(3)))]);
                dhuhrAzanMinuteOnesDigit.setImageResource(azanTimeDigitImages[Integer.parseInt(String.valueOf(dhuhrTime12h.charAt(4)))]);
            }

            /* set images azan asr */
            if (azanTimeDigitImages != null && asrTime12h != null && asrTime12h.length() >= 5) {
                asrAzanHourTensDigit.setImageResource(azanTimeDigitImages[Integer.parseInt(String.valueOf(asrTime12h.charAt(0)))]);
                asrAzanHourOnesDigit.setImageResource(azanTimeDigitImages[Integer.parseInt(String.valueOf(asrTime12h.charAt(1)))]);

                asrAzanMinuteTensDigit.setImageResource(azanTimeDigitImages[Integer.parseInt(String.valueOf(asrTime12h.charAt(3)))]);
                asrAzanMinuteOnesDigit.setImageResource(azanTimeDigitImages[Integer.parseInt(String.valueOf(asrTime12h.charAt(4)))]);
            }

            /* set images azan mgrb */
            if (azanTimeDigitImages != null && maghribTime12h != null && maghribTime12h.length() >= 5) {
                maghribAzanHourTensDigit.setImageResource(azanTimeDigitImages[Integer.parseInt(String.valueOf(maghribTime12h.charAt(0)))]);
                maghribAzanHourOnesDigit.setImageResource(azanTimeDigitImages[Integer.parseInt(String.valueOf(maghribTime12h.charAt(1)))]);

                maghribAzanMinuteTensDigit.setImageResource(azanTimeDigitImages[Integer.parseInt(String.valueOf(maghribTime12h.charAt(3)))]);
                maghribAzanMinuteOnesDigit.setImageResource(azanTimeDigitImages[Integer.parseInt(String.valueOf(maghribTime12h.charAt(4)))]);
            }

            /* set images azan isha */
            if (azanTimeDigitImages != null && ishaTime12h != null && ishaTime12h.length() >= 5) {
                ishaAzanHourTensDigit.setImageResource(azanTimeDigitImages[Integer.parseInt(String.valueOf(ishaTime12h.charAt(0)))]);
                ishaAzanHourOnesDigit.setImageResource(azanTimeDigitImages[Integer.parseInt(String.valueOf(ishaTime12h.charAt(1)))]);

                ishaAzanMinuteTensDigit.setImageResource(azanTimeDigitImages[Integer.parseInt(String.valueOf(ishaTime12h.charAt(3)))]);
                ishaAzanMinuteOnesDigit.setImageResource(azanTimeDigitImages[Integer.parseInt(String.valueOf(ishaTime12h.charAt(4)))]);
            }

            long fajrIqamh, fajrSun, thuhrIqamh, assrIqamh, maghribIqamh, ishaaIqamh;
            String iqamhFjrTime, iqamhShroqTime, iqamhDahrTime, iqamhAsrTime, iqamhMgrmTime, iqamhIshaTime;
            if (!Pref.getValue(getApplicationContext(), Constants.PREF_FAJER_RELATIVE, false)) {
                fajrIqamh = Pref.getValue(getApplicationContext(), Constants.PREF_IQAMH_FAJR_RELATIVE_TIME_SELECTED, 25);
                iqamhFjrTime = getIqamh(String.valueOf(fajrTime12h), fajrIqamh);
            } else {
                iqamhFjrTime = Pref.getValue(getApplicationContext(), Constants.PREF_IQAMH_FAJR_CONSTANT_TIME_SELECTED, "20:20");
            }
            if (!Pref.getValue(getApplicationContext(), Constants.PREF_SUNRISE_RELATIVE, false)) {
                fajrSun = Pref.getValue(getApplicationContext(), Constants.PREF_IQAMH_SUNRISE_RELATIVE_TIME_SELECTED, 20);
                iqamhShroqTime = getIqamh(String.valueOf(sunriseTime12h), fajrSun);
            } else {
                iqamhShroqTime = Pref.getValue(getApplicationContext(), Constants.PREF_IQAMH_SUNRISE_CONSTANT_TIME_SELECTED, "20:20");
            }
            if (!Pref.getValue(getApplicationContext(), Constants.PREF_DHOHR_RELATIVE, false)) {
                thuhrIqamh = Pref.getValue(getApplicationContext(), Constants.PREF_IQAMH_DHOHR_RELATIVE_TIME_SELECTED, 20);
                iqamhDahrTime = getIqamh(String.valueOf(dhuhrTime12h), thuhrIqamh);
            } else {
                iqamhDahrTime = Pref.getValue(getApplicationContext(), Constants.PREF_IQAMH_DHOHR_CONSTANT_TIME_SELECTED, "20:20");
            }
            if (!Pref.getValue(getApplicationContext(), Constants.PREF_ASR_RELATIVE, false)) {
                assrIqamh = Pref.getValue(getApplicationContext(), Constants.PREF_IQAMH_ASR_RELATIVE_TIME_SELECTED, 20);
                iqamhAsrTime = getIqamh(String.valueOf(asrTime12h), assrIqamh);
            } else {
                iqamhAsrTime = Pref.getValue(getApplicationContext(), Constants.PREF_IQAMH_ASR_CONSTANT_TIME_SELECTED, "20:20");
            }
            if (!Pref.getValue(getApplicationContext(), Constants.PREF_MAGHRIB_RELATIVE, false)) {
                maghribIqamh = Pref.getValue(getApplicationContext(), Constants.PREF_IQAMH_MAGHRIB_RELATIVE_TIME_SELECTED, 10);
                iqamhMgrmTime = getIqamh(String.valueOf(maghribTime12h), maghribIqamh);
            } else {
                iqamhMgrmTime = Pref.getValue(getApplicationContext(), Constants.PREF_IQAMH_MAGHRIB_CONSTANT_TIME_SELECTED, "20:20");
            }
            if (!Pref.getValue(getApplicationContext(), Constants.PREF_ISHA_RELATIVE, false)) {
                ishaaIqamh = Pref.getValue(getApplicationContext(), Constants.PREF_IQAMH_ISHA_RELATIVE_TIME_SELECTED, 20);
                iqamhIshaTime = getIqamh(String.valueOf(ishaTime12h), ishaaIqamh);
            } else {
                iqamhIshaTime = Pref.getValue(getApplicationContext(), Constants.PREF_IQAMH_ISHA_CONSTANT_TIME_SELECTED, "20:20");
            }

            /* set iqamh fjr */
            if (iqamahTimeDigitImages != null && iqamhFjrTime != null && iqamhFjrTime.length() >= 5 && fajrIqamahHourTensDigit != null && fajrIqamahHourOnesDigit != null && fajrIqamahMinuteTensDigit != null && fajrIqamahMinuteOnesDigit != null) {
                fajrIqamahHourTensDigit.setImageResource(iqamahTimeDigitImages[Integer.parseInt(String.valueOf(iqamhFjrTime.charAt(0)))]);
                fajrIqamahHourOnesDigit.setImageResource(iqamahTimeDigitImages[Integer.parseInt(String.valueOf(iqamhFjrTime.charAt(1)))]);
                fajrIqamahMinuteTensDigit.setImageResource(iqamahTimeDigitImages[Integer.parseInt(String.valueOf(iqamhFjrTime.charAt(3)))]);
                fajrIqamahMinuteOnesDigit.setImageResource(iqamahTimeDigitImages[Integer.parseInt(String.valueOf(iqamhFjrTime.charAt(4)))]);
            }

            /* set iqamh shroq */
            if (iqamahTimeDigitImages != null && iqamhShroqTime != null && iqamhShroqTime.length() >= 5 && sunriseIqamahHourTensDigit != null && sunriseIqamahHourOnesDigit != null && sunriseIqamahMinuteTensDigit != null && sunriseIqamahMinuteOnesDigit != null) {
                sunriseIqamahHourTensDigit.setImageResource(iqamahTimeDigitImages[Integer.parseInt(String.valueOf(iqamhShroqTime.charAt(0)))]);
                sunriseIqamahHourOnesDigit.setImageResource(iqamahTimeDigitImages[Integer.parseInt(String.valueOf(iqamhShroqTime.charAt(1)))]);
                sunriseIqamahMinuteTensDigit.setImageResource(iqamahTimeDigitImages[Integer.parseInt(String.valueOf(iqamhShroqTime.charAt(3)))]);
                sunriseIqamahMinuteOnesDigit.setImageResource(iqamahTimeDigitImages[Integer.parseInt(String.valueOf(iqamhShroqTime.charAt(4)))]);
            }

            /* set dahr iqamg */
            if (iqamahTimeDigitImages != null && iqamhDahrTime != null && iqamhDahrTime.length() >= 5 && dhuhrIqamahHourTensDigit != null && dhuhrIqamahHourOnesDigit != null && dhuhrIqamahMinuteTensDigit != null && dhuhrIqamahMinuteOnesDigit != null) {
                dhuhrIqamahHourTensDigit.setImageResource(iqamahTimeDigitImages[Integer.parseInt(String.valueOf(iqamhDahrTime.charAt(0)))]);
                dhuhrIqamahHourOnesDigit.setImageResource(iqamahTimeDigitImages[Integer.parseInt(String.valueOf(iqamhDahrTime.charAt(1)))]);
                dhuhrIqamahMinuteTensDigit.setImageResource(iqamahTimeDigitImages[Integer.parseInt(String.valueOf(iqamhDahrTime.charAt(3)))]);
                dhuhrIqamahMinuteOnesDigit.setImageResource(iqamahTimeDigitImages[Integer.parseInt(String.valueOf(iqamhDahrTime.charAt(4)))]);
            }

            /* set asr iqamg */
            if (iqamahTimeDigitImages != null && iqamhAsrTime != null && iqamhAsrTime.length() >= 5 && asrIqamahHourTensDigit != null && asrIqamahHourOnesDigit != null && asrIqamahMinuteTensDigit != null && asrIqamahMinuteOnesDigit != null) {
                asrIqamahHourTensDigit.setImageResource(iqamahTimeDigitImages[Integer.parseInt(String.valueOf(iqamhAsrTime.charAt(0)))]);
                asrIqamahHourOnesDigit.setImageResource(iqamahTimeDigitImages[Integer.parseInt(String.valueOf(iqamhAsrTime.charAt(1)))]);
                asrIqamahMinuteTensDigit.setImageResource(iqamahTimeDigitImages[Integer.parseInt(String.valueOf(iqamhAsrTime.charAt(3)))]);
                asrIqamahMinuteOnesDigit.setImageResource(iqamahTimeDigitImages[Integer.parseInt(String.valueOf(iqamhAsrTime.charAt(4)))]);
            }

            /* set mgrb iqamg */
            if (iqamahTimeDigitImages != null && iqamhMgrmTime != null && iqamhMgrmTime.length() >= 5 && maghribIqamahHourTensDigit != null && maghribIqamahHourOnesDigit != null && maghribIqamahMinuteTensDigit != null && maghribIqamahMinuteOnesDigit != null) {
                maghribIqamahHourTensDigit.setImageResource(iqamahTimeDigitImages[Integer.parseInt(String.valueOf(iqamhMgrmTime.charAt(0)))]);
                maghribIqamahHourOnesDigit.setImageResource(iqamahTimeDigitImages[Integer.parseInt(String.valueOf(iqamhMgrmTime.charAt(1)))]);
                maghribIqamahMinuteTensDigit.setImageResource(iqamahTimeDigitImages[Integer.parseInt(String.valueOf(iqamhMgrmTime.charAt(3)))]);
                maghribIqamahMinuteOnesDigit.setImageResource(iqamahTimeDigitImages[Integer.parseInt(String.valueOf(iqamhMgrmTime.charAt(4)))]);
            }

            /* set isha iqamg */
            if (iqamahTimeDigitImages != null && iqamhIshaTime != null && iqamhIshaTime.length() >= 5 && ishaIqamahHourTensDigit != null && ishaIqamahHourOnesDigit != null && ishaIqamahMinuteTensDigit != null && ishaIqamahMinuteOnesDigit != null) {
                ishaIqamahHourTensDigit.setImageResource(iqamahTimeDigitImages[Integer.parseInt(String.valueOf(iqamhIshaTime.charAt(0)))]);
                ishaIqamahHourOnesDigit.setImageResource(iqamahTimeDigitImages[Integer.parseInt(String.valueOf(iqamhIshaTime.charAt(1)))]);
                ishaIqamahMinuteTensDigit.setImageResource(iqamahTimeDigitImages[Integer.parseInt(String.valueOf(iqamhIshaTime.charAt(3)))]);
                ishaIqamahMinuteOnesDigit.setImageResource(iqamahTimeDigitImages[Integer.parseInt(String.valueOf(iqamhIshaTime.charAt(4)))]);
            }


            fetchFridayPrayerTimes();
            if (azanCountdownDigitImages != null && fridayPrayerTimes != null && fridayPrayerTimes.length > 3 && fridayPrayerTimes[3] != null && fridayPrayerTimes[3].length() >= 5 && imageAzanFridayTimeHourTens != null && imageAzanFridayTimeHourOnes != null && imageAzanFridayTimeMinuteTens != null && imageAzanFridayTimeMinuteOnes != null) {
                imageAzanFridayTimeHourTens.setImageResource(azanCountdownDigitImages[Integer.parseInt(String.valueOf(fridayPrayerTimes[3].charAt(0)))]);
                imageAzanFridayTimeHourOnes.setImageResource(azanCountdownDigitImages[Integer.parseInt(String.valueOf(fridayPrayerTimes[3].charAt(1)))]);
                imageAzanFridayTimeMinuteTens.setImageResource(azanCountdownDigitImages[Integer.parseInt(String.valueOf(fridayPrayerTimes[3].charAt(3)))]);
                imageAzanFridayTimeMinuteOnes.setImageResource(azanCountdownDigitImages[Integer.parseInt(String.valueOf(fridayPrayerTimes[3].charAt(4)))]);
            }

        } catch (Exception e) {
            Log.e("MainActivity", "Error in updatePrayerAndDateDisplays: " + e.getMessage(), e);
        }
    }

    /**
     * Safely set visibility for nextPrayerIndicatorSecondary
     */
    private void setNextPrayerIndicatorSecondaryVisibility(int visibility) {
        if (nextPrayerIndicatorSecondary != null) {
            nextPrayerIndicatorSecondary.setVisibility(visibility);
        }
    }

    private void updateNextPrayerIndicator() {
        // Add null checks at the beginning
        if (nextPrayerIndicator == null) {
            return; // Exit early if nextPrayerIndicator is null
        }

        long fajrIqamahTime = 0, sunriseIqamahTime = 0, dhuhrIqamahTime = 0, asrIqamahTime = 0, maghribIqamahTime = 0, ishaIqamahTime = 0;
        long fajrIqamahDelay = 0, sunriseIqamahDelay = 0, dhuhrIqamahDelay = 0, asrIqamahDelay = 0, maghribIqamahDelay = 0, ishaIqamahDelay = 0;

        SimpleDateFormat timeFormat12Hour = new SimpleDateFormat("hh:mmaa", Locale.ENGLISH);
        SimpleDateFormat timeFormat24Hour = new SimpleDateFormat("HH:mm:ss", Locale.ENGLISH);

        if (!Pref.getValue(getApplicationContext(), Constants.PREF_FAJER_RELATIVE, false)) {
            fajrIqamahDelay = Pref.getValue(getApplicationContext(), Constants.PREF_IQAMH_FAJR_RELATIVE_TIME_SELECTED, 25);
            fajrIqamahTime = getMilliseconds(String.valueOf(fajrTime12h)) + fajrIqamahDelay * 60000;
        } else {
            String fajrConstantTime = Pref.getValue(getApplicationContext(), Constants.PREF_IQAMH_FAJR_CONSTANT_TIME_SELECTED, "20:20am");
            Date fajrDate = null;
            try {
                fajrDate = timeFormat12Hour.parse(fajrConstantTime);
            } catch (Exception e) {
                e.printStackTrace();
            }
            fajrIqamahTime = getMilliseconds((timeFormat24Hour.format(fajrDate)));
        }

        if (!Pref.getValue(getApplicationContext(), Constants.PREF_SUNRISE_RELATIVE, false)) {
            sunriseIqamahDelay = Pref.getValue(getApplicationContext(), Constants.PREF_IQAMH_SUNRISE_RELATIVE_TIME_SELECTED, 20);
            sunriseIqamahTime = getMilliseconds(String.valueOf(sunriseTime12h)) + sunriseIqamahDelay * 60000;
        } else {
            sunriseIqamahTime = getMilliseconds1(String.valueOf(sunriseTime12h), String.valueOf(Pref.getValue(getApplicationContext(), Constants.PREF_IQAMH_SUNRISE_CONSTANT_TIME_SELECTED, "20:20am")));
        }

        if (!Pref.getValue(getApplicationContext(), Constants.PREF_DHOHR_RELATIVE, false)) {
            dhuhrIqamahDelay = Pref.getValue(getApplicationContext(), Constants.PREF_IQAMH_DHOHR_RELATIVE_TIME_SELECTED, 20);
            dhuhrIqamahTime = getMilliseconds(String.valueOf(dhuhrTime12h)) + dhuhrIqamahDelay * 60000;
        } else {
            String dhuhrConstantTime = Pref.getValue(getApplicationContext(), Constants.PREF_IQAMH_DHOHR_CONSTANT_TIME_SELECTED, "20:20am");
            Date dhuhrDate = null;
            try {
                dhuhrDate = timeFormat12Hour.parse(dhuhrConstantTime);
            } catch (Exception e) {
                e.printStackTrace();

            }
            dhuhrIqamahTime = getMilliseconds((timeFormat24Hour.format(dhuhrDate)));

        }

        if (!Pref.getValue(getApplicationContext(), Constants.PREF_ASR_RELATIVE, false)) {
            asrIqamahDelay = Pref.getValue(getApplicationContext(), Constants.PREF_IQAMH_ASR_RELATIVE_TIME_SELECTED, 20);
            asrIqamahTime = getMilliseconds(String.valueOf(asrTime12h)) + asrIqamahDelay * 60000;

        } else {
            String asrConstantTime = Pref.getValue(getApplicationContext(), Constants.PREF_IQAMH_ASR_CONSTANT_TIME_SELECTED, "20:20am");
            Date asrDate = null;
            try {
                asrDate = timeFormat12Hour.parse(asrConstantTime);
            } catch (Exception e) {
                e.printStackTrace();
            }
            asrIqamahTime = getMilliseconds((timeFormat24Hour.format(asrDate)));
        }

        if (!Pref.getValue(getApplicationContext(), Constants.PREF_MAGHRIB_RELATIVE, false)) {
            maghribIqamahDelay = Pref.getValue(getApplicationContext(), Constants.PREF_IQAMH_MAGHRIB_RELATIVE_TIME_SELECTED, 20);
            maghribIqamahTime = getMilliseconds(String.valueOf(maghribTime12h)) + maghribIqamahDelay * 60000;
        } else {
            String maghribConstantTime = Pref.getValue(getApplicationContext(), Constants.PREF_IQAMH_MAGHRIB_CONSTANT_TIME_SELECTED, "20:20am");
            Date maghribDate = null;
            try {
                maghribDate = timeFormat12Hour.parse(maghribConstantTime);
            } catch (Exception e) {
                e.printStackTrace();
            }
            maghribIqamahTime = getMilliseconds((timeFormat24Hour.format(maghribDate)));

        }

        if (!Pref.getValue(getApplicationContext(), Constants.PREF_ISHA_RELATIVE, false)) {
            ishaIqamahDelay = Pref.getValue(getApplicationContext(), Constants.PREF_IQAMH_ISHA_RELATIVE_TIME_SELECTED, 20);
            ishaIqamahTime = getMilliseconds(String.valueOf(ishaTime12h)) + ishaIqamahDelay * 60000;
        } else {
            String ishaConstantTime = Pref.getValue(getApplicationContext(), Constants.PREF_IQAMH_ISHA_CONSTANT_TIME_SELECTED, "20:20am");
            Date ishaDate = null;
            try {
                ishaDate = timeFormat12Hour.parse(ishaConstantTime);
            } catch (Exception e) {
                e.printStackTrace();
            }
            ishaIqamahTime = getMilliseconds((timeFormat24Hour.format(ishaDate)));
        }

        String currentTime = (String) android.text.format.DateFormat.format("HH:mm:aa", new Date());

        ViewGroup.MarginLayoutParams marginLayoutParams;


        if (getMilliseconds(currentTime) >= getMilliseconds(String.valueOf(fajrTime12h)) && getMilliseconds(currentTime) < fajrIqamahTime) {


            switch (selectedTheme) {
                case 3:
                    setNextPrayerIndicatorSecondaryVisibility(View.GONE);
                    nextPrayerIndicator.setImageResource(R.drawable.img_to_iqamh_4);
                    marginLayoutParams = (ViewGroup.MarginLayoutParams) nextPrayerIndicator.getLayoutParams();
                    marginLayoutParams.setMargins(marginLayoutParams.leftMargin, marginLayoutParams.topMargin, 0, marginLayoutParams.bottomMargin);
                    break;
                case 4:
                    nextPrayerIndicator.setImageResource(R.drawable.img_to_iqamh_5);
                    break;
                case 5:
                    nextPrayerIndicator.setImageResource(R.drawable.img_to_iqamh6);
                    break;
                case 6:
                    setNextPrayerIndicatorSecondaryVisibility(View.GONE);
                    nextPrayerIndicator.setImageResource(R.drawable.img_to_iqamh7);
                    break;
                case 7:
                    setNextPrayerIndicatorSecondaryVisibility(View.GONE);
                    nextPrayerIndicator.setImageResource(R.drawable.img_to_iqamh8);
                    break;
                case 9:
                    setNextPrayerIndicatorSecondaryVisibility(View.GONE);
                    nextPrayerIndicator.setImageResource(R.drawable.img_to_iqamh10);
                    break;
                default:
                    nextPrayerIndicator.setImageResource(R.drawable.img_to_iqamh);
                    break;
            }
            getTimeLeftForIqamh(fajrTime12h, fajrIqamahDelay, 1);

            azanCountdownContainer.setVisibility(View.GONE);
            iqamahCountdownContainer.setVisibility(View.VISIBLE);
        } else if (getMilliseconds(currentTime) > getMilliseconds(String.valueOf(fajrTime12h)) && getMilliseconds(currentTime) < getMilliseconds(String.valueOf(dhuhrTime12h))) {

            getTimeLeftForAzan(dhuhrTime12h, 3);

            Calendar calendar = Calendar.getInstance();
            if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY) {
                System.out.println("FRIDAY!");

                switch (selectedTheme) {
                    case 3:
                        setNextPrayerIndicatorSecondaryVisibility(View.VISIBLE);
                        nextPrayerIndicator.setImageResource(R.drawable.icon_next_azan_freday_4);
                        marginLayoutParams = (ViewGroup.MarginLayoutParams) nextPrayerIndicator.getLayoutParams();
                        marginLayoutParams.setMargins(marginLayoutParams.leftMargin, marginLayoutParams.topMargin, -30, marginLayoutParams.bottomMargin);
                        break;
                    case 4:
                        nextPrayerIndicator.setImageResource(R.drawable.icon_fri_1_theme_5_1);
                        break;
                    case 5:
                        nextPrayerIndicator.setImageResource(R.drawable.icon_fri_1_theme_6);
                        break;
                    case 6:
                        setNextPrayerIndicatorSecondaryVisibility(View.VISIBLE);
                        nextPrayerIndicator.setImageResource(R.drawable.icon_next_azan_freday_7);
                        break;
                    case 7:
                        setNextPrayerIndicatorSecondaryVisibility(View.VISIBLE);
                        nextPrayerIndicator.setImageResource(R.drawable.icon_next_azan_freday_8);
                        break;
                    case 9:
                        setNextPrayerIndicatorSecondaryVisibility(View.VISIBLE);
                        nextPrayerIndicator.setImageResource(R.drawable.icon_next_azan_freday_10);
                        break;
                    default:
                        nextPrayerIndicator.setImageResource(R.drawable.icon_next_azan_freday);


                        break;
                }
            } else {


                switch (selectedTheme) {

                    case 3:
                        setNextPrayerIndicatorSecondaryVisibility(View.VISIBLE);
                        nextPrayerIndicator.setImageResource(R.drawable.icon_next_azan_2_4);
                        break;
                    case 4:
                        nextPrayerIndicator.setImageResource(R.drawable.icon_next_azan_2_5);
                        break;
                    case 5:
                        nextPrayerIndicator.setImageResource(R.drawable.icon_next_azan_2_6);
                        break;
                    case 6:
                        setNextPrayerIndicatorSecondaryVisibility(View.VISIBLE);
                        nextPrayerIndicator.setImageResource(R.drawable.icon_next_azan_2_7);
                        break;
                    case 7:
                        setNextPrayerIndicatorSecondaryVisibility(View.VISIBLE);
                        nextPrayerIndicator.setImageResource(R.drawable.icon_next_azan_2_8);
                        break;
                    case 9:
                        setNextPrayerIndicatorSecondaryVisibility(View.VISIBLE);
                        nextPrayerIndicator.setImageResource(R.drawable.icon_next_azan_2_10);
                        break;
                    default:
                        nextPrayerIndicator.setImageResource(R.drawable.icon_next_azan_2);
                        break;
                }
            }

            azanCountdownContainer.setVisibility(View.VISIBLE);
            iqamahCountdownContainer.setVisibility(View.GONE);
        } else if (getMilliseconds(currentTime) >= getMilliseconds(String.valueOf(dhuhrTime12h)) && getMilliseconds(currentTime) < dhuhrIqamahTime) {

            Calendar calendar = Calendar.getInstance();
            if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY) {
                System.out.println("FRIDAY!");


                switch (selectedTheme) {
                    case 3:
                        setNextPrayerIndicatorSecondaryVisibility(View.GONE);
                        nextPrayerIndicator.setImageResource(R.drawable.icon_fri_1_theme_4);
                        marginLayoutParams = (ViewGroup.MarginLayoutParams) nextPrayerIndicator.getLayoutParams();
                        marginLayoutParams.setMargins(marginLayoutParams.leftMargin, marginLayoutParams.topMargin, 30, marginLayoutParams.bottomMargin);
                        break;
                    case 4:
                        nextPrayerIndicator.setImageResource(R.drawable.icon_fri_1_theme_5);
                        break;
                    case 5:
                        nextPrayerIndicator.setImageResource(R.drawable.ic_pray_friday3);
                        break;
                    case 6:

                        setNextPrayerIndicatorSecondaryVisibility(View.GONE);
                        nextPrayerIndicator.setImageResource(R.drawable.ic_pray_friday3);
                        break;
                    case 8:

                        marginLayoutParams = (ViewGroup.MarginLayoutParams) nextPrayerIndicator.getLayoutParams();
                        marginLayoutParams.setMargins(marginLayoutParams.leftMargin, marginLayoutParams.topMargin, 10, marginLayoutParams.bottomMargin);
                        nextPrayerIndicator.setImageResource(R.drawable.ic_pray_friday);
                        break;
                    case 7:


                        setNextPrayerIndicatorSecondaryVisibility(View.GONE);
                        nextPrayerIndicator.setImageResource(R.drawable.icon_fri_1_theme_5);
                        break;
                    case 9:


                        setNextPrayerIndicatorSecondaryVisibility(View.GONE);
                        nextPrayerIndicator.setImageResource(R.drawable.ic_pray_friday3);
                        break;
                    default:
                        nextPrayerIndicator.setImageResource(R.drawable.ic_pray_friday);


                        break;
                }
            } else {
                switch (selectedTheme) {
                    case 3:
                        setNextPrayerIndicatorSecondaryVisibility(View.GONE);
                        nextPrayerIndicator.setImageResource(R.drawable.img_to_iqamh_4);
                        marginLayoutParams = (ViewGroup.MarginLayoutParams) nextPrayerIndicator.getLayoutParams();
                        marginLayoutParams.setMargins(marginLayoutParams.leftMargin, marginLayoutParams.topMargin, 0, marginLayoutParams.bottomMargin);
                        break;
                    case 4:
                        nextPrayerIndicator.setImageResource(R.drawable.img_to_iqamh_5);
                        break;
                    case 5:
                        nextPrayerIndicator.setImageResource(R.drawable.img_to_iqamh6);
                        break;
                    case 6:
                        setNextPrayerIndicatorSecondaryVisibility(View.GONE);
                        nextPrayerIndicator.setImageResource(R.drawable.img_to_iqamh7);
                        break;
                    case 7:
                        setNextPrayerIndicatorSecondaryVisibility(View.GONE);
                        nextPrayerIndicator.setImageResource(R.drawable.img_to_iqamh8);
                        break;
                    case 9:
                        setNextPrayerIndicatorSecondaryVisibility(View.GONE);
                        nextPrayerIndicator.setImageResource(R.drawable.img_to_iqamh10);
                        break;
                    default:
                        nextPrayerIndicator.setImageResource(R.drawable.img_to_iqamh);
                        break;
                }
            }

            getTimeLeftForIqamh(dhuhrTime12h, dhuhrIqamahDelay, 3);

            azanCountdownContainer.setVisibility(View.GONE);
            iqamahCountdownContainer.setVisibility(View.VISIBLE);
        } else if (getMilliseconds(currentTime) >= getMilliseconds(String.valueOf(dhuhrTime12h)) && getMilliseconds(currentTime) < getMilliseconds(String.valueOf(asrTime12h))) {


            switch (selectedTheme) {
                case 3:
                    setNextPrayerIndicatorSecondaryVisibility(View.VISIBLE);
                    nextPrayerIndicator.setImageResource(R.drawable.icon_next_azan_3_4);
                    marginLayoutParams = (ViewGroup.MarginLayoutParams) nextPrayerIndicator.getLayoutParams();
                    marginLayoutParams.setMargins(marginLayoutParams.leftMargin, marginLayoutParams.topMargin, -30, marginLayoutParams.bottomMargin);
                    break;
                case 4:
                    nextPrayerIndicator.setImageResource(R.drawable.icon_next_azan_3_5);
                    break;
                case 5:
                    nextPrayerIndicator.setImageResource(R.drawable.icon_next_azan_3_6);
                    break;
                case 6:
                    setNextPrayerIndicatorSecondaryVisibility(View.VISIBLE);
                    nextPrayerIndicator.setImageResource(R.drawable.icon_next_azan_3_7);
                    break;
                case 7:
                    setNextPrayerIndicatorSecondaryVisibility(View.VISIBLE);
                    nextPrayerIndicator.setImageResource(R.drawable.icon_next_azan_3_8);
                    break;
                case 9:
                    setNextPrayerIndicatorSecondaryVisibility(View.VISIBLE);
                    nextPrayerIndicator.setImageResource(R.drawable.icon_next_azan_3_10);
                    break;
                default:
                    nextPrayerIndicator.setImageResource(R.drawable.icon_next_azan_3);
                    break;
            }


            getTimeLeftForAzan(asrTime12h, 4);

            azanCountdownContainer.setVisibility(View.VISIBLE);
            iqamahCountdownContainer.setVisibility(View.GONE);
        } else if (getMilliseconds(currentTime) >= getMilliseconds(String.valueOf(asrTime12h)) && getMilliseconds(currentTime) < asrIqamahTime) {


            switch (selectedTheme) {
                case 3:
                    setNextPrayerIndicatorSecondaryVisibility(View.GONE);
                    nextPrayerIndicator.setImageResource(R.drawable.img_to_iqamh_4);
                    marginLayoutParams = (ViewGroup.MarginLayoutParams) nextPrayerIndicator.getLayoutParams();
                    marginLayoutParams.setMargins(marginLayoutParams.leftMargin, marginLayoutParams.topMargin, -0, marginLayoutParams.bottomMargin);
                    break;
                case 4:
                    nextPrayerIndicator.setImageResource(R.drawable.img_to_iqamh_5);
                    break;
                case 5:
                    nextPrayerIndicator.setImageResource(R.drawable.img_to_iqamh6);
                    break;
                case 6:
                    setNextPrayerIndicatorSecondaryVisibility(View.GONE);
                    nextPrayerIndicator.setImageResource(R.drawable.img_to_iqamh7);
                    break;
                case 7:
                    setNextPrayerIndicatorSecondaryVisibility(View.GONE);
                    nextPrayerIndicator.setImageResource(R.drawable.img_to_iqamh8);
                    break;
                case 9:
                    setNextPrayerIndicatorSecondaryVisibility(View.GONE);
                    nextPrayerIndicator.setImageResource(R.drawable.img_to_iqamh10);
                    break;
                default:
                    nextPrayerIndicator.setImageResource(R.drawable.img_to_iqamh);
                    break;
            }

            getTimeLeftForIqamh(asrTime12h, asrIqamahDelay, 4);

            azanCountdownContainer.setVisibility(View.GONE);
            iqamahCountdownContainer.setVisibility(View.VISIBLE);
        } else if (getMilliseconds(currentTime) > getMilliseconds(String.valueOf(asrTime12h)) && getMilliseconds(currentTime) < getMilliseconds(String.valueOf(maghribTime12h))) {


            switch (selectedTheme) {
                case 3:
                    setNextPrayerIndicatorSecondaryVisibility(View.VISIBLE);
                    nextPrayerIndicator.setImageResource(R.drawable.icon_next_azan_4_4);
                    marginLayoutParams = (ViewGroup.MarginLayoutParams) nextPrayerIndicator.getLayoutParams();
                    marginLayoutParams.setMargins(marginLayoutParams.leftMargin, marginLayoutParams.topMargin, -30, marginLayoutParams.bottomMargin);
                    break;
                case 4:
                    nextPrayerIndicator.setImageResource(R.drawable.icon_next_azan_4_5);
                    break;
                case 5:
                    nextPrayerIndicator.setImageResource(R.drawable.icon_next_azan_4_6);
                    break;
                case 6:
                    setNextPrayerIndicatorSecondaryVisibility(View.VISIBLE);
                    nextPrayerIndicator.setImageResource(R.drawable.icon_next_azan_4_7);
                    break;
                case 7:
                    setNextPrayerIndicatorSecondaryVisibility(View.VISIBLE);
                    nextPrayerIndicator.setImageResource(R.drawable.icon_next_azan_4_8);
                    break;
                case 9:
                    setNextPrayerIndicatorSecondaryVisibility(View.VISIBLE);
                    nextPrayerIndicator.setImageResource(R.drawable.icon_next_azan_4_10);
                    break;
                default:
                    nextPrayerIndicator.setImageResource(R.drawable.icon_next_azan_4);
                    break;
            }


            getTimeLeftForAzan(maghribTime12h, 5);

            azanCountdownContainer.setVisibility(View.VISIBLE);
            iqamahCountdownContainer.setVisibility(View.GONE);
        } else if (getMilliseconds(currentTime) >= getMilliseconds(String.valueOf(maghribTime12h)) && getMilliseconds(currentTime) < maghribIqamahTime) {


            switch (selectedTheme) {
                case 3:
                    setNextPrayerIndicatorSecondaryVisibility(View.GONE);
                    nextPrayerIndicator.setImageResource(R.drawable.img_to_iqamh_4);
                    marginLayoutParams = (ViewGroup.MarginLayoutParams) nextPrayerIndicator.getLayoutParams();
                    marginLayoutParams.setMargins(marginLayoutParams.leftMargin, marginLayoutParams.topMargin, 0, marginLayoutParams.bottomMargin);
                    break;
                case 4:
                    nextPrayerIndicator.setImageResource(R.drawable.img_to_iqamh_5);
                    break;
                case 5:
                    nextPrayerIndicator.setImageResource(R.drawable.img_to_iqamh6);
                    break;
                case 6:
                    setNextPrayerIndicatorSecondaryVisibility(View.GONE);
                    nextPrayerIndicator.setImageResource(R.drawable.img_to_iqamh7);
                    break;
                case 7:
                    setNextPrayerIndicatorSecondaryVisibility(View.GONE);
                    nextPrayerIndicator.setImageResource(R.drawable.img_to_iqamh8);
                    break;
                case 9:
                    setNextPrayerIndicatorSecondaryVisibility(View.GONE);
                    nextPrayerIndicator.setImageResource(R.drawable.img_to_iqamh10);
                    break;
                default:
                    nextPrayerIndicator.setImageResource(R.drawable.img_to_iqamh);
                    break;
            }


            getTimeLeftForIqamh(maghribTime12h, maghribIqamahDelay, 5);

            azanCountdownContainer.setVisibility(View.GONE);
            iqamahCountdownContainer.setVisibility(View.VISIBLE);
        } else if (getMilliseconds(currentTime) > getMilliseconds(String.valueOf(maghribTime12h)) && getMilliseconds(currentTime) < getMilliseconds(String.valueOf(ishaTime12h))) {


            switch (selectedTheme) {
                case 3:
                    setNextPrayerIndicatorSecondaryVisibility(View.VISIBLE);
                    nextPrayerIndicator.setImageResource(R.drawable.icon_next_azan_5_4);
                    marginLayoutParams = (ViewGroup.MarginLayoutParams) nextPrayerIndicator.getLayoutParams();
                    marginLayoutParams.setMargins(marginLayoutParams.leftMargin, marginLayoutParams.topMargin, -30, marginLayoutParams.bottomMargin);
                    break;
                case 4:
                    nextPrayerIndicator.setImageResource(R.drawable.icon_next_azan_5_5);
                    break;
                case 5:
                    nextPrayerIndicator.setImageResource(R.drawable.icon_next_azan_5_6);
                    break;
                case 6:
                    setNextPrayerIndicatorSecondaryVisibility(View.VISIBLE);
                    nextPrayerIndicator.setImageResource(R.drawable.icon_next_azan_5_7);
                    break;
                case 7:
                    setNextPrayerIndicatorSecondaryVisibility(View.VISIBLE);
                    nextPrayerIndicator.setImageResource(R.drawable.icon_next_azan_5_8);
                    break;
                case 9:
                    setNextPrayerIndicatorSecondaryVisibility(View.VISIBLE);
                    nextPrayerIndicator.setImageResource(R.drawable.icon_next_azan_5_10);
                    break;
                default:
                    nextPrayerIndicator.setImageResource(R.drawable.icon_next_azan_5);
                    break;
            }


            getTimeLeftForAzan(ishaTime12h, 6);

            azanCountdownContainer.setVisibility(View.VISIBLE);
            iqamahCountdownContainer.setVisibility(View.GONE);
        } else if (getMilliseconds(currentTime) >= getMilliseconds(String.valueOf(ishaTime12h)) && getMilliseconds(currentTime) < ishaIqamahTime) {


            switch (selectedTheme) {
                case 3:
                    setNextPrayerIndicatorSecondaryVisibility(View.GONE);
                    nextPrayerIndicator.setImageResource(R.drawable.img_to_iqamh_4);
                    marginLayoutParams = (ViewGroup.MarginLayoutParams) nextPrayerIndicator.getLayoutParams();
                    marginLayoutParams.setMargins(marginLayoutParams.leftMargin, marginLayoutParams.topMargin, 0, marginLayoutParams.bottomMargin);
                    break;
                case 4:
                    nextPrayerIndicator.setImageResource(R.drawable.img_to_iqamh_5);
                    break;
                case 5:
                    nextPrayerIndicator.setImageResource(R.drawable.img_to_iqamh6);
                    break;
                case 6:
                    setNextPrayerIndicatorSecondaryVisibility(View.GONE);
                    nextPrayerIndicator.setImageResource(R.drawable.img_to_iqamh7);
                    break;
                case 7:
                    setNextPrayerIndicatorSecondaryVisibility(View.GONE);
                    nextPrayerIndicator.setImageResource(R.drawable.img_to_iqamh8);
                    break;
                case 9:
                    setNextPrayerIndicatorSecondaryVisibility(View.GONE);
                    nextPrayerIndicator.setImageResource(R.drawable.img_to_iqamh10);
                    break;
                default:
                    nextPrayerIndicator.setImageResource(R.drawable.img_to_iqamh);
                    break;
            }


            getTimeLeftForIqamh(ishaTime12h, ishaIqamahDelay, 6);

            azanCountdownContainer.setVisibility(View.GONE);
            iqamahCountdownContainer.setVisibility(View.VISIBLE);
        } else if (getMilliseconds(currentTime) > getMilliseconds(String.valueOf(fajrTime12h)) || getMilliseconds(currentTime) < getMilliseconds(String.valueOf(fajrTime12h))) {


            switch (selectedTheme) {
                case 3:
                    setNextPrayerIndicatorSecondaryVisibility(View.VISIBLE);
                    nextPrayerIndicator.setImageResource(R.drawable.icon_next_azan_1_4);
                    marginLayoutParams = (ViewGroup.MarginLayoutParams) nextPrayerIndicator.getLayoutParams();
                    marginLayoutParams.setMargins(marginLayoutParams.leftMargin, marginLayoutParams.topMargin, -30, marginLayoutParams.bottomMargin);
                    break;
                case 4:
                    nextPrayerIndicator.setImageResource(R.drawable.icon_next_azan_1_5);
                    break;
                case 5:
                    nextPrayerIndicator.setImageResource(R.drawable.icon_next_azan_1_6);
                    break;
                case 6:
                    setNextPrayerIndicatorSecondaryVisibility(View.VISIBLE);
                    nextPrayerIndicator.setImageResource(R.drawable.icon_next_azan_1_7);
                    break;
                case 7:
                    setNextPrayerIndicatorSecondaryVisibility(View.VISIBLE);
                    nextPrayerIndicator.setImageResource(R.drawable.icon_next_azan_1_8);
                    break;
                case 9:
                    setNextPrayerIndicatorSecondaryVisibility(View.VISIBLE);
                    nextPrayerIndicator.setImageResource(R.drawable.icon_next_azan_1_10);
                    break;
                default:
                    nextPrayerIndicator.setImageResource(R.drawable.icon_next_azan_1);
                    break;
            }

            getTimeLeftForAzan(fajrTime12h, 1);

            azanCountdownContainer.setVisibility(View.VISIBLE);
            iqamahCountdownContainer.setVisibility(View.GONE);
        } else {
            iqamahCountdownMinuteTensDigit.setImageResource(timeDigitImages[0]);
            iqamahCountdownMinuteOnesDigit.setImageResource(timeDigitImages[0]);
            iqamahCountdownSecondTensDigit.setImageResource(timeDigitImages[0]);
            iqamahCountdownSecondOnesDigit.setImageResource(timeDigitImages[0]);

            azanCountdownHourTensDigit.setImageResource(timeDigitImages[0]);
            azanCountdownHourOnesDigit.setImageResource(timeDigitImages[0]);
            azanCountdownMinuteTensDigit.setImageResource(timeDigitImages[0]);
            azanCountdownMinuteOnesDigit.setImageResource(timeDigitImages[0]);
            azanCountdownSecondTensDigit.setImageResource(timeDigitImages[0]);
            azanCountdownSecondOnesDigit.setImageResource(timeDigitImages[0]);
        }


    }

    private void refreshUIEverySecond() {
        runOnUiThread(() -> {
            updatePrayerAndDateDisplays();
            updateNextPrayerIndicator();
            showAdsIfScheduled();

            // Skip ImageView updates for theme 11 since it uses TextViews
            if (selectedTheme == 11) {
                return;
            }

            DateFormat timeNow = new SimpleDateFormat("hh:mmass", Locale.ENGLISH);
            Calendar c = Calendar.getInstance();
            String timeText = timeNow.format(c.getTime());
            imageCurrentTimeHourTens.setImageResource(timeDigitImages[Integer.parseInt(String.valueOf(timeText.charAt(0)))]);

            imageCurrentTimeHourOnes.setImageResource(timeDigitImages[Integer.parseInt(String.valueOf(timeText.charAt(1)))]);
            imageCurrentTimeMinuteTens.setImageResource(timeDigitImages[Integer.parseInt(String.valueOf(timeText.charAt(3)))]);
            imageCurrentTimeMinuteOnes.setImageResource(timeDigitImages[Integer.parseInt(String.valueOf(timeText.charAt(4)))]);
            imageCurrentTimeSecondTens.setImageResource(secondDigitImages[Integer.parseInt(String.valueOf(timeText.charAt(7)))]);
            imageCurrentTimeSecondOnes.setImageResource(secondDigitImages[Integer.parseInt(String.valueOf(timeText.charAt(8)))]);


        });
    }

    private String getIqamh(String azanTime, long aqamhTime) {
        SimpleDateFormat formatter = new SimpleDateFormat("hh:mmaa", Locale.ENGLISH);
        try {
            SimpleDateFormat dateFormat2 = new SimpleDateFormat("hh:mm", Locale.ENGLISH);
            Date mDate = dateFormat2.parse(azanTime);
            long timeInMilliseconds = mDate.getTime();
            long minutes = aqamhTime * 60000;
            String dateString = formatter.format(new Date(timeInMilliseconds + minutes));
            return dateString;
        } catch (ParseException e) {


            e.printStackTrace();
            return null;
        }
    }

    private long getMilliseconds(String azanTime) {
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm", Locale.ENGLISH);

        Date mDate = null;
        try {
            mDate = formatter.parse(String.valueOf(azanTime));
        } catch (ParseException e) {

            e.printStackTrace();
        }
        return mDate.getTime();

    }

    private long getMilliseconds1(String azanTime, String iqamhTIme) {
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm", Locale.ENGLISH);
        SimpleDateFormat formatter1 = new SimpleDateFormat("hh:mmaa", Locale.ENGLISH);

        Date mDate = null;
        Date mDate1 = null;
        try {
            mDate = formatter.parse(String.valueOf(azanTime));
            mDate1 = formatter1.parse(String.valueOf(iqamhTIme));
        } catch (ParseException e) {

            e.printStackTrace();
        }
        return mDate1.getTime() - mDate.getTime();

    }

    private void getTimeLeftForAzan(String time, int check) {
        Calendar c = Calendar.getInstance();
        Calendar c1 = Calendar.getInstance();

        String hour = time.charAt(0) + "" + time.charAt(1);
        String mint = time.charAt(3) + "" + time.charAt(4);
        String sec = 0 + "" + 0;

        c.set(c1.get(Calendar.YEAR), c1.get(Calendar.MONTH) + 1, c1.get(Calendar.DATE), Integer.parseInt(hour), Integer.parseInt(mint), Integer.parseInt(sec));


        String givenDateString = time;
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm", Locale.ENGLISH);

        long timeInMilliseconds = 0;
        try {
            Date mDate = sdf.parse(givenDateString);
            timeInMilliseconds = mDate.getTime();

        } catch (ParseException e) {
            e.printStackTrace();
        }

        DateFormat timeNow = new SimpleDateFormat("hh:mmss", Locale.ENGLISH);

        Calendar c2 = Calendar.getInstance();
        String timeText = timeNow.format(c2.getTime());

        String givenDateString1 = timeText;
        SimpleDateFormat sdf1 = new SimpleDateFormat("hh:mmss", Locale.ENGLISH);
        long timeInMilliseconds1 = 0;
        try {
            Date mDate1 = sdf1.parse(givenDateString1);
            timeInMilliseconds1 = mDate1.getTime();

        } catch (ParseException e) {
            e.printStackTrace();
        }

        long millis = c.getTimeInMillis();
        long millis1;
        switch (check) {

            case 4:
            case 5:
            case 6:
                millis1 = timeInMilliseconds - timeInMilliseconds1;
                break;
            case 3:
            case 1:
                millis1 = millis - c1.getTimeInMillis();
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + check);
        }


        DateFormat formatter = new SimpleDateFormat("HH:mm:ss", Locale.ENGLISH);
        formatter.setTimeZone(TimeZone.getTimeZone("GMT"));
        String s11 = formatter.format(new Date(millis1));
        String s = String.valueOf(s11.charAt(0));
        String s0 = String.valueOf(s11.charAt(1));
        String s1 = String.valueOf(s11.charAt(3));
        String s2 = String.valueOf(s11.charAt(4));
        String s3 = String.valueOf(s11.charAt(6));
        String s4 = String.valueOf(s11.charAt(7));


        if (s.equals("0") && s0.equals("0") && s1.equals("0") && s2.equals("1") && s3.equals("0") && s4.equals("0")) {
            switch (check) {
                case 1:

                    if (Pref.getValue(getApplicationContext(), Constants.PREF_FAJER_SOUND_AZAN, false)) {
                        if (Pref.getValue(getApplicationContext(), Constants.PREF_FAJER_SOUND_AZAN_PATH, null) != null) {
                            if (!mediaPlayer.isPlaying()) {
                                mediaPlayer = new MediaPlayer();
                                String uriSound = Pref.getValue(getApplicationContext(), Constants.PREF_FAJER_SOUND_AZAN_PATH, null);
                                if (!mediaPlayer.isPlaying()) playAudioFromPath(uriSound);
                            }
                        }
                    }
                    break;
                case 3:


                    if (Pref.getValue(getApplicationContext(), Constants.PREF_DHOHR_SOUND_AZAN, false)) {
                        if (Pref.getValue(getApplicationContext(), Constants.PREF_DHOHR_SOUND_AZAN_PATH, null) != null) {
                            if (!mediaPlayer.isPlaying()) {
                                Calendar calendar = Calendar.getInstance();
                                if (calendar.get(Calendar.DAY_OF_WEEK) != Calendar.FRIDAY) {
                                    mediaPlayer = new MediaPlayer();
                                    String uriSound = Pref.getValue(getApplicationContext(), Constants.PREF_DHOHR_SOUND_AZAN_PATH, null);
                                    if (!mediaPlayer.isPlaying()) playAudioFromPath(uriSound);
                                }
                            }
                        }
                    }
                    break;
                case 4:

                    if (Pref.getValue(getApplicationContext(), Constants.PREF_ASR_SOUND_AZAN, false)) {
                        if (Pref.getValue(getApplicationContext(), Constants.PREF_ASR_SOUND_AZAN_PATH, null) != null) {
                            if (!mediaPlayer.isPlaying()) {
                                mediaPlayer = new MediaPlayer();
                                String uriSound = Pref.getValue(getApplicationContext(), Constants.PREF_ASR_SOUND_AZAN_PATH, null);
                                if (!mediaPlayer.isPlaying()) playAudioFromPath(uriSound);
                            }
                        }
                    }
                    break;
                case 5:

                    if (Pref.getValue(getApplicationContext(), Constants.PREF_MAGHRIB_SOUND_AZAN, false)) {
                        if (Pref.getValue(getApplicationContext(), Constants.PREF_MAGHRIB_SOUND_AZAN_PATH, null) != null) {
                            if (!mediaPlayer.isPlaying()) {
                                mediaPlayer = new MediaPlayer();
                                String uriSound = Pref.getValue(getApplicationContext(), Constants.PREF_MAGHRIB_SOUND_AZAN_PATH, null);
                                if (!mediaPlayer.isPlaying()) playAudioFromPath(uriSound);
                            }
                        }
                    }
                    break;
                case 6:

                    if (Pref.getValue(getApplicationContext(), Constants.PREF_ISHA_SOUND_AZAN, false)) {
                        if (Pref.getValue(getApplicationContext(), Constants.PREF_ISHA_SOUND_AZAN_PATH, null) != null) {
                            if (!mediaPlayer.isPlaying()) {
                                mediaPlayer = new MediaPlayer();
                                String uriSound = Pref.getValue(getApplicationContext(), Constants.PREF_ISHA_SOUND_AZAN_PATH, null);
                                if (!mediaPlayer.isPlaying()) playAudioFromPath(uriSound);
                            }
                        }
                    }
                    break;
            }
        }

        azanCountdownHourTensDigit.setImageResource(azanCountdownDigitImages[Integer.parseInt(String.valueOf(s11.charAt(0)))]);
        azanCountdownHourOnesDigit.setImageResource(azanCountdownDigitImages[Integer.parseInt(String.valueOf(s11.charAt(1)))]);
        azanCountdownMinuteTensDigit.setImageResource(azanCountdownDigitImages[Integer.parseInt(String.valueOf(s11.charAt(3)))]);
        azanCountdownMinuteOnesDigit.setImageResource(azanCountdownDigitImages[Integer.parseInt(String.valueOf(s11.charAt(4)))]);
        azanCountdownSecondTensDigit.setImageResource(azanCountdownDigitImages[Integer.parseInt(String.valueOf(s11.charAt(6)))]);
        azanCountdownSecondOnesDigit.setImageResource(azanCountdownDigitImages[Integer.parseInt(String.valueOf(s11.charAt(7)))]);
    }

    private void getTimeLeftForIqamh(String azanTime, long iqamahDelayMinutes, int prayerType) {

        long iqamahDelayMilliseconds;
        long iqamahTimeInMillis;
        Calendar iqamahCalendar;
        Calendar currentTimeCalendar;
        long timeRemainingMilliseconds = 0;
        String formattedCountdown = null;

        switch (prayerType) {
            case 1: // Fajr
                if (!Pref.getValue(getApplicationContext(), Constants.PREF_FAJER_RELATIVE, false)) {
                    iqamahCalendar = Calendar.getInstance();
                    currentTimeCalendar = Calendar.getInstance();
                    String azanHour = azanTime.charAt(0) + "" + azanTime.charAt(1);
                    String azanMinute = azanTime.charAt(3) + "" + azanTime.charAt(4);
                    String azanSecond = 0 + "" + 0;
                    iqamahCalendar.set(currentTimeCalendar.get(Calendar.YEAR), currentTimeCalendar.get(Calendar.MONDAY), currentTimeCalendar.get(Calendar.DAY_OF_MONTH), Integer.parseInt(azanHour), Integer.parseInt(azanMinute), Integer.parseInt(azanSecond));

                    iqamahDelayMilliseconds = iqamahDelayMinutes * 60000;
                    iqamahTimeInMillis = iqamahCalendar.getTimeInMillis() + iqamahDelayMilliseconds;
                    timeRemainingMilliseconds = iqamahTimeInMillis - currentTimeCalendar.getTimeInMillis();
                } else {
                    String constantIqamahTime = Pref.getValue(getApplicationContext(), Constants.PREF_IQAMH_FAJR_CONSTANT_TIME_SELECTED, "20:20am");
                    iqamahCalendar = Calendar.getInstance();
                    currentTimeCalendar = Calendar.getInstance();
                    String iqamahHour = constantIqamahTime.charAt(0) + "" + constantIqamahTime.charAt(1);
                    String iqamahMinute = constantIqamahTime.charAt(3) + "" + constantIqamahTime.charAt(4);
                    String iqamahSecond = 0 + "" + 0;
                    iqamahCalendar.set(currentTimeCalendar.get(Calendar.YEAR), currentTimeCalendar.get(Calendar.MONDAY), currentTimeCalendar.get(Calendar.DAY_OF_MONTH), Integer.parseInt(iqamahHour), Integer.parseInt(iqamahMinute), Integer.parseInt(iqamahSecond));
                    timeRemainingMilliseconds = iqamahCalendar.getTimeInMillis() - currentTimeCalendar.getTimeInMillis();
                }
                formattedCountdown = String.format("%1$tM:%1$tS", timeRemainingMilliseconds);
                break;
            case 3: // Dhuhr
                if (!Pref.getValue(getApplicationContext(), Constants.PREF_DHOHR_RELATIVE, false)) {
                    iqamahCalendar = Calendar.getInstance();
                    currentTimeCalendar = Calendar.getInstance();
                    String azanHour = azanTime.charAt(0) + "" + azanTime.charAt(1);
                    String azanMinute = azanTime.charAt(3) + "" + azanTime.charAt(4);
                    String azanSecond = 0 + "" + 0;
                    iqamahCalendar.set(currentTimeCalendar.get(Calendar.YEAR), currentTimeCalendar.get(Calendar.MONDAY), currentTimeCalendar.get(Calendar.DAY_OF_MONTH), Integer.parseInt(azanHour), Integer.parseInt(azanMinute), Integer.parseInt(azanSecond));

                    iqamahDelayMilliseconds = iqamahDelayMinutes * 60000;
                    iqamahTimeInMillis = iqamahCalendar.getTimeInMillis() + iqamahDelayMilliseconds;
                    timeRemainingMilliseconds = iqamahTimeInMillis - currentTimeCalendar.getTimeInMillis();
                } else {
                    String constantIqamahTime = Pref.getValue(getApplicationContext(), Constants.PREF_IQAMH_DHOHR_CONSTANT_TIME_SELECTED, "20:20am");
                    iqamahCalendar = Calendar.getInstance();
                    currentTimeCalendar = Calendar.getInstance();
                    String iqamahHour = constantIqamahTime.charAt(0) + "" + constantIqamahTime.charAt(1);
                    String iqamahMinute = constantIqamahTime.charAt(3) + "" + constantIqamahTime.charAt(4);
                    String iqamahSecond = 0 + "" + 0;
                    iqamahCalendar.set(currentTimeCalendar.get(Calendar.YEAR), currentTimeCalendar.get(Calendar.MONDAY), currentTimeCalendar.get(Calendar.DAY_OF_MONTH), Integer.parseInt(iqamahHour), Integer.parseInt(iqamahMinute), Integer.parseInt(iqamahSecond));
                    timeRemainingMilliseconds = iqamahCalendar.getTimeInMillis() - currentTimeCalendar.getTimeInMillis();
                }
                formattedCountdown = String.format("%1$tM:%1$tS", timeRemainingMilliseconds);
                break;
            case 4: // Asr
                if (!Pref.getValue(getApplicationContext(), Constants.PREF_ASR_RELATIVE, false)) {
                    iqamahCalendar = Calendar.getInstance();
                    currentTimeCalendar = Calendar.getInstance();
                    String azanHour = azanTime.charAt(0) + "" + azanTime.charAt(1);
                    String azanMinute = azanTime.charAt(3) + "" + azanTime.charAt(4);
                    String azanSecond = 0 + "" + 0;
                    iqamahCalendar.set(currentTimeCalendar.get(Calendar.YEAR), currentTimeCalendar.get(Calendar.MONDAY), currentTimeCalendar.get(Calendar.DAY_OF_MONTH), Integer.parseInt(azanHour), Integer.parseInt(azanMinute), Integer.parseInt(azanSecond));

                    iqamahDelayMilliseconds = iqamahDelayMinutes * 60000;
                    iqamahTimeInMillis = iqamahCalendar.getTimeInMillis() + iqamahDelayMilliseconds;
                    timeRemainingMilliseconds = iqamahTimeInMillis - currentTimeCalendar.getTimeInMillis();
                } else {
                    String constantIqamahTime = Pref.getValue(getApplicationContext(), Constants.PREF_IQAMH_ASR_CONSTANT_TIME_SELECTED, "20:20am");
                    iqamahCalendar = Calendar.getInstance();
                    currentTimeCalendar = Calendar.getInstance();
                    String iqamahHour = constantIqamahTime.charAt(0) + "" + constantIqamahTime.charAt(1);
                    String iqamahMinute = constantIqamahTime.charAt(3) + "" + constantIqamahTime.charAt(4);
                    String iqamahSecond = 0 + "" + 0;
                    iqamahCalendar.set(currentTimeCalendar.get(Calendar.YEAR), currentTimeCalendar.get(Calendar.MONDAY), currentTimeCalendar.get(Calendar.DAY_OF_MONTH), Integer.parseInt(iqamahHour), Integer.parseInt(iqamahMinute), Integer.parseInt(iqamahSecond));
                    timeRemainingMilliseconds = iqamahCalendar.getTimeInMillis() - currentTimeCalendar.getTimeInMillis() + 43200000;
                }
                formattedCountdown = String.format("%1$tM:%1$tS", timeRemainingMilliseconds);
                break;
            case 5: // Maghrib
                if (!Pref.getValue(getApplicationContext(), Constants.PREF_MAGHRIB_RELATIVE, false)) {
                    iqamahCalendar = Calendar.getInstance();
                    currentTimeCalendar = Calendar.getInstance();
                    String azanHour = azanTime.charAt(0) + "" + azanTime.charAt(1);
                    String azanMinute = azanTime.charAt(3) + "" + azanTime.charAt(4);
                    String azanSecond = 0 + "" + 0;
                    iqamahCalendar.set(currentTimeCalendar.get(Calendar.YEAR), currentTimeCalendar.get(Calendar.MONDAY), currentTimeCalendar.get(Calendar.DAY_OF_MONTH), Integer.parseInt(azanHour), Integer.parseInt(azanMinute), Integer.parseInt(azanSecond));

                    iqamahDelayMilliseconds = iqamahDelayMinutes * 60000;
                    iqamahTimeInMillis = iqamahCalendar.getTimeInMillis() + iqamahDelayMilliseconds;
                    timeRemainingMilliseconds = iqamahTimeInMillis - currentTimeCalendar.getTimeInMillis();
                } else {
                    String constantIqamahTime = Pref.getValue(getApplicationContext(), Constants.PREF_IQAMH_MAGHRIB_CONSTANT_TIME_SELECTED, "20:20am");
                    iqamahCalendar = Calendar.getInstance();
                    currentTimeCalendar = Calendar.getInstance();
                    String iqamahHour = constantIqamahTime.charAt(0) + "" + constantIqamahTime.charAt(1);
                    String iqamahMinute = constantIqamahTime.charAt(3) + "" + constantIqamahTime.charAt(4);
                    String iqamahSecond = 0 + "" + 0;
                    iqamahCalendar.set(currentTimeCalendar.get(Calendar.YEAR), currentTimeCalendar.get(Calendar.MONDAY), currentTimeCalendar.get(Calendar.DAY_OF_MONTH), Integer.parseInt(iqamahHour), Integer.parseInt(iqamahMinute), Integer.parseInt(iqamahSecond));
                    timeRemainingMilliseconds = iqamahCalendar.getTimeInMillis() - currentTimeCalendar.getTimeInMillis() + 43200000;
                }
                formattedCountdown = String.format("%1$tM:%1$tS", timeRemainingMilliseconds);
                break;
            case 6: // Isha
                if (!Pref.getValue(getApplicationContext(), Constants.PREF_ISHA_RELATIVE, false)) {
                    iqamahCalendar = Calendar.getInstance();
                    currentTimeCalendar = Calendar.getInstance();
                    String azanHour = azanTime.charAt(0) + "" + azanTime.charAt(1);
                    String azanMinute = azanTime.charAt(3) + "" + azanTime.charAt(4);
                    String azanSecond = 0 + "" + 0;
                    iqamahCalendar.set(currentTimeCalendar.get(Calendar.YEAR), currentTimeCalendar.get(Calendar.MONDAY), currentTimeCalendar.get(Calendar.DAY_OF_MONTH), Integer.parseInt(azanHour), Integer.parseInt(azanMinute), Integer.parseInt(azanSecond));

                    iqamahDelayMilliseconds = iqamahDelayMinutes * 60000;
                    iqamahTimeInMillis = iqamahCalendar.getTimeInMillis() + iqamahDelayMilliseconds;
                    timeRemainingMilliseconds = iqamahTimeInMillis - currentTimeCalendar.getTimeInMillis();
                } else {
                    String constantIqamahTime = Pref.getValue(getApplicationContext(), Constants.PREF_IQAMH_ISHA_CONSTANT_TIME_SELECTED, "20:20am");
                    iqamahCalendar = Calendar.getInstance();
                    currentTimeCalendar = Calendar.getInstance();
                    String iqamahHour = constantIqamahTime.charAt(0) + "" + constantIqamahTime.charAt(1);
                    String iqamahMinute = constantIqamahTime.charAt(3) + "" + constantIqamahTime.charAt(4);
                    String iqamahSecond = 0 + "" + 0;
                    iqamahCalendar.set(currentTimeCalendar.get(Calendar.YEAR), currentTimeCalendar.get(Calendar.MONDAY), currentTimeCalendar.get(Calendar.DAY_OF_MONTH), Integer.parseInt(iqamahHour), Integer.parseInt(iqamahMinute), Integer.parseInt(iqamahSecond));
                    timeRemainingMilliseconds = iqamahCalendar.getTimeInMillis() - currentTimeCalendar.getTimeInMillis() + 43200000;
                }
                formattedCountdown = String.format("%1$tM:%1$tS", timeRemainingMilliseconds);
                break;
        }

        Calendar currentCalendar = Calendar.getInstance();

        // Handle countdown display
        if (prayerType == 3) { // Dhuhr prayer - special handling for Friday sermon
            iqamahCountdownMinuteTensDigit.setVisibility(View.GONE);
            iqamahCountdownMinuteOnesDigit.setVisibility(View.GONE);
            iqamahCountdownSecondTensDigit.setVisibility(View.GONE);
            iqamahCountdownSecondOnesDigit.setVisibility(View.GONE);

            if (!isOpenKhotabActivity) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/M/d", Locale.ENGLISH);
                Calendar todayCalendar = Calendar.getInstance();

                databaseHelper.openDataBase();
                final Khotab khotba = databaseHelper.getKhotba(dateFormat.format(todayCalendar.getTime()));
                databaseHelper.close();

                if (khotba != null) {
                    if (khotba.getIsException() == 0) {
                        Intent sermonIntent = new Intent(getApplicationContext(), ShowKhotabActivity.class);
                        sermonIntent.putExtra("khotba", khotba);
                        startActivity(sermonIntent);
                        isOpenKhotabActivity = true;
                    }
                } else if (Pref.getValue(getApplicationContext(), Constants.PREF_SHOW_KATEB, false)) {
                    Intent katebIntent = new Intent(getApplicationContext(), ShowKatebActivity.class);
                    startActivity(katebIntent);
                    isOpenKhotabActivity = true;
                }
            }
        } else {
            iqamahCountdownMinuteTensDigit.setVisibility(View.VISIBLE);
            iqamahCountdownMinuteOnesDigit.setVisibility(View.VISIBLE);
            iqamahCountdownSecondTensDigit.setVisibility(View.VISIBLE);
            iqamahCountdownSecondOnesDigit.setVisibility(View.VISIBLE);
        }

        // Handle close phone activity for ALL prayers (including Dhuhr)
        long oneMinuteInMilliseconds = 60 * 1000;
        if (timeRemainingMilliseconds > 0 && timeRemainingMilliseconds <= oneMinuteInMilliseconds) {
            if (Pref.getValue(getApplicationContext(), Constants.PREF_CLOSE_NOTIFICATION_SCREEN, true)) {
                if (!isOpenClosePhone) {
                    Intent closePhoneIntent = new Intent(MainActivity.this, ShowClosePhoneActivity.class);
                    closePhoneIntent.putExtra("PRAY", prayerType);
                    startActivity(closePhoneIntent);
                    isOpenClosePhone = true;
                }
            }
        }

        // Update countdown display
        iqamahCountdownMinuteTensDigit.setImageResource(iqamahCountdownDigitImages[Integer.parseInt(String.valueOf(formattedCountdown.charAt(0)))]);
        iqamahCountdownMinuteOnesDigit.setImageResource(iqamahCountdownDigitImages[Integer.parseInt(String.valueOf(formattedCountdown.charAt(1)))]);
        iqamahCountdownSecondTensDigit.setImageResource(iqamahCountdownDigitImages[Integer.parseInt(String.valueOf(formattedCountdown.charAt(3)))]);
        iqamahCountdownSecondOnesDigit.setImageResource(iqamahCountdownDigitImages[Integer.parseInt(String.valueOf(formattedCountdown.charAt(4)))]);

        // Handle Iqamah time reached (1 second or less remaining)
        if (timeRemainingMilliseconds <= 1000) {
            if (currentCalendar.get(Calendar.DAY_OF_WEEK) != Calendar.FRIDAY && prayerType != 3) {
                boolean shouldShowAlkhushue = shouldShowAlkhushueScreen(prayerType);

                if (!Pref.getValue(getApplicationContext(), Constants.PREF_CLOSE_NOTIFICATION_SCREEN, true)) {
                    if (shouldShowAlkhushue) {
                        if (!isOpenAlkhushuePhone) {
                            Intent alkhushueIntent = new Intent(MainActivity.this, ShowAlkhushueActivity.class);
                            alkhushueIntent.putExtra("PRAY", prayerType);
                            startActivity(alkhushueIntent);
                            isOpenAlkhushuePhone = true;
                        }
                    } else {
                        if (!isOpenAzkarPhone) {
                            scheduleAzkarScreen(prayerType);
                            isOpenAzkarPhone = true;
                        }
                    }
                }
            }

            // Play Iqamah audio for each prayer
            switch (prayerType) {
                case 1: // Fajr
                    if (Pref.getValue(getApplicationContext(), Constants.PREF_FAJER_SOUND_IQAMAH, false)) {
                        if (Pref.getValue(getApplicationContext(), Constants.PREF_FAJER_SOUND_IQAMAH_PATH, null) != null) {
                            if (!mediaPlayer.isPlaying()) {
                                mediaPlayer = new MediaPlayer();
                                String fajrIqamahAudioPath = Pref.getValue(getApplicationContext(), Constants.PREF_FAJER_SOUND_IQAMAH_PATH, null);
                                if (!mediaPlayer.isPlaying()) playAudioFromPath(fajrIqamahAudioPath);
                            }
                        }
                    }
                    break;

                case 3: // Dhuhr
                    if (currentCalendar.get(Calendar.DAY_OF_WEEK) != Calendar.FRIDAY && prayerType != 3) {
                        if (Pref.getValue(getApplicationContext(), Constants.PREF_DHOHR_SOUND_IQAMAH, false)) {
                            if (Pref.getValue(getApplicationContext(), Constants.PREF_DHOHR_SOUND_IQAMAH_PATH, null) != null) {
                                if (!mediaPlayer.isPlaying()) {
                                    mediaPlayer = new MediaPlayer();
                                    String dhuhrIqamahAudioPath = Pref.getValue(getApplicationContext(), Constants.PREF_DHOHR_SOUND_IQAMAH_PATH, null);
                                    if (!mediaPlayer.isPlaying()) playAudioFromPath(dhuhrIqamahAudioPath);
                                }
                            }
                        }
                    }
                    break;
                case 4: // Asr
                    if (Pref.getValue(getApplicationContext(), Constants.PREF_ASR_SOUND_IQAMAH, false)) {
                        if (Pref.getValue(getApplicationContext(), Constants.PREF_ASR_SOUND_IQAMAH_PATH, null) != null) {
                            if (!mediaPlayer.isPlaying()) {
                                mediaPlayer = new MediaPlayer();
                                String asrIqamahAudioPath = Pref.getValue(getApplicationContext(), Constants.PREF_ASR_SOUND_IQAMAH_PATH, null);
                                if (!mediaPlayer.isPlaying()) playAudioFromPath(asrIqamahAudioPath);
                            }
                        }
                    }
                    break;
                case 5: // Maghrib
                    if (Pref.getValue(getApplicationContext(), Constants.PREF_MAGHRIB_SOUND_IQAMAH, false)) {
                        if (Pref.getValue(getApplicationContext(), Constants.PREF_MAGHRIB_SOUND_IQAMAH_PATH, null) != null) {
                            if (!mediaPlayer.isPlaying()) {
                                mediaPlayer = new MediaPlayer();
                                String maghribIqamahAudioPath = Pref.getValue(getApplicationContext(), Constants.PREF_MAGHRIB_SOUND_IQAMAH_PATH, null);
                                if (!mediaPlayer.isPlaying()) playAudioFromPath(maghribIqamahAudioPath);
                            }
                        }
                    }
                    break;
                case 6: // Isha
                    if (Pref.getValue(getApplicationContext(), Constants.PREF_ISHA_SOUND_IQAMAH, false)) {
                        if (Pref.getValue(getApplicationContext(), Constants.PREF_ISHA_SOUND_IQAMAH_PATH, null) != null) {
                            if (!mediaPlayer.isPlaying()) {
                                mediaPlayer = new MediaPlayer();
                                String ishaIqamahAudioPath = Pref.getValue(getApplicationContext(), Constants.PREF_ISHA_SOUND_IQAMAH_PATH, null);
                                if (!mediaPlayer.isPlaying()) playAudioFromPath(ishaIqamahAudioPath);
                            }
                        }
                    }
                    break;
            }
        }
    }

    private boolean shouldShowAlkhushueScreen(int pray) {
        boolean check = false;
        switch (pray) {
            case 1:
                check = Pref.getValue(getApplicationContext(), Constants.PREF_FAJR_SHOW_ALKHUSHUE, true);
                break;
            case 2:
                check = Pref.getValue(getApplicationContext(), Constants.PREF_SUNRISE_SHOW_ALKHUSHUE, true);
                break;
            case 3:
                check = Pref.getValue(getApplicationContext(), Constants.PREF_DHOHR_SHOW_ALKHUSHUE, true);
                break;
            case 4:
                check = Pref.getValue(getApplicationContext(), Constants.PREF_ASR_SHOW_ALKHUSHUE, true);
                break;
            case 5:
                check = Pref.getValue(getApplicationContext(), Constants.PREF_MAGHRIB_SHOW_ALKHUSHUE, true);
                break;
            case 6:
                check = Pref.getValue(getApplicationContext(), Constants.PREF_ISHA_SHOW_ALKHUSHUE, true);
                break;
        }
        return check;
    }

    private void scheduleAzkarScreen(int pray) {
        int mint = 1;
        switch (pray) {
            case 1:
                mint = Pref.getValue(getApplicationContext(), Constants.PREF_FAJER_AZKAR_SHOW_IN, 10);
                break;
            case 2:
                mint = Pref.getValue(getApplicationContext(), Constants.PREF_SUNRISE_AZKAR_SHOW_IN, 10);
                break;
            case 3:
                mint = Pref.getValue(getApplicationContext(), Constants.PREF_DHOHR_AZKAR_SHOW_IN, 10);
                break;
            case 4:
                mint = Pref.getValue(getApplicationContext(), Constants.PREF_ASR_AZKAR_SHOW_IN, 10);
                break;
            case 5:
                mint = Pref.getValue(getApplicationContext(), Constants.PREF_MAGHRIB_AZKAR_SHOW_IN, 10);
                break;
            case 6:
                mint = Pref.getValue(getApplicationContext(), Constants.PREF_ISHA_AZKAR_SHOW_IN, 10);
                break;
        }
        if (mint != 0) {
            int after = mint * 1000 * 60;
            final Handler handler = new Handler(Looper.getMainLooper());
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {

                    Intent intent = new Intent(MainActivity.this, ShowAzkarActivity.class);
                    intent.putExtra("PRAY", pray);
                    startActivity(intent);
                }
            }, after);
        }

    }

    private void fetchFridayPrayerTimes() {

        SimpleDateFormat format1 = new SimpleDateFormat("yy", Locale.ENGLISH);
        SimpleDateFormat format2 = new SimpleDateFormat("M/d", Locale.ENGLISH);
        Calendar calendar = Calendar.getInstance();

        int weekday = calendar.get(Calendar.DAY_OF_WEEK);
        int days = Calendar.FRIDAY - weekday;
        if (days < 0) {
            days += 7;
        }

        calendar.add(Calendar.DAY_OF_YEAR, days);

        String[] cityEn = getResources().getStringArray(R.array.city_name_en);
        int cityChose = Pref.getValue(getApplicationContext(), Constants.PREF_CITY_POSITION_SELECTED, 0);
        if (cityChose != 0) {
            fridayPrayerTimes = databaseHelper.getPrayerTimesFriday(cityEn[cityChose], format2.format(calendar.getTime()) + "/" + getYears(Integer.parseInt(format1.format(calendar.getTime()))));
        } else {
            fridayPrayerTimes = databaseHelper.getPrayerTimesFriday("masqat", format2.format(calendar.getTime()) + "/" + getYears(Integer.parseInt(format1.format(calendar.getTime()))));
        }

        // Add null check for Friday prayer times
        if (fridayPrayerTimes == null || fridayPrayerTimes.length < 4) {
            fridayPrayerTimes = new String[]{"", "", "", "12:30", "", ""};
        }
    }

    private void showAdsIfScheduled() {
        SimpleDateFormat df = new SimpleDateFormat("HH:mm", Locale.ENGLISH);
        SimpleDateFormat dfDate = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        Date currentDate = new Date();
        String currentTime = df.format(currentDate);
        String currentDateString = dfDate.format(currentDate);
        int day = 0;
        if (Utils.isSaturday()) day = 1;
        if (Utils.isSunday()) day = 2;
        if (Utils.isMonday()) day = 3;
        if (Utils.isTuesday()) day = 4;
        if (Utils.isWednesday()) day = 5;
        if (Utils.isThursday()) day = 6;
        if (Utils.isFriday()) day = 7;

        databaseHelper.openDataBase();
        ArrayList<Ads> adsList = databaseHelper.getAdsByDate(-1, currentDateString, currentTime, day);
        databaseHelper.close();
        if (adsList.size() > 0) {
            for (int i = 0; i < adsList.size(); i++) {
                Ads ads = adsList.get(i);
                if (!isOpenAds) {
                    isOpenAds = true;
                    Intent intent = new Intent(MainActivity.this, ShowAdsActivity.class);
                    intent.putExtra("ads", ads);
                    intent.setAction("main");
                    startActivity(intent);
                }
            }
        }
    }

    private int getYears(int year) {
        int returnYear = 0;
        if (year == 18 || year == 22 || year == 26 || year == 30 || year == 34 || year == 38 || year == 42 || year == 46 || year == 50 || year == 54 || year == 58 || year == 62 || year == 66 || year == 70 || year == 74 || year == 78 || year == 82 || year == 86 || year == 90 || year == 94 || year == 98 || year == 2 || year == 6 || year == 10 || year == 14) {
            returnYear = 18;
        } else if (year == 19 || year == 23 || year == 27 || year == 31 || year == 35 || year == 39 || year == 43 || year == 47 || year == 51 || year == 55 || year == 59 || year == 63 || year == 67 || year == 71 || year == 75 || year == 79 || year == 83 || year == 87 || year == 91 || year == 95 || year == 99 || year == 3 || year == 7 || year == 11 || year == 15) {
            returnYear = 19;
        } else if (year == 20 || year == 24 || year == 28 || year == 32 || year == 36 || year == 40 || year == 44 || year == 48 || year == 52 || year == 56 || year == 60 || year == 64 || year == 68 || year == 72 || year == 76 || year == 80 || year == 84 || year == 88 || year == 92 || year == 96 || year == 4 || year == 8 || year == 12 || year == 16) {
            returnYear = 20;
        } else if (year == 21 || year == 25 || year == 29 || year == 33 || year == 37 || year == 41 || year == 45 || year == 49 || year == 53 || year == 57 || year == 61 || year == 65 || year == 69 || year == 73 || year == 77 || year == 81 || year == 85 || year == 89 || year == 93 || year == 97 || year == 1 || year == 5 || year == 9 || year == 13 || year == 17) {
            returnYear = 21;
        }
        return returnYear;
    }

    private void initializeBluetoothListener() {

        bluetoothCentralManager.setMTCentralManagerListener(new MTCentralManagerListener() {
            @Override
            public void onScanedPeripheral(final List<MTPeripheral> peripherals) {


                for (MTPeripheral mtPeripheral : peripherals) {

                    MTFrameHandler mtFrameHandler = mtPeripheral.mMTFrameHandler;


                    int battery = mtFrameHandler.getBattery();


                    ArrayList<MinewFrame> advFrames = mtFrameHandler.getAdvFrames();

                    for (MinewFrame minewFrame : advFrames) {
                        FrameType frameType = minewFrame.getFrameType();
                        switch (frameType) {
                            case FrameiBeacon:
                                IBeaconFrame iBeaconFrame = (IBeaconFrame) minewFrame;

                                break;
                            case FrameUID:
                                UidFrame uidFrame = (UidFrame) minewFrame;

                                break;
                            case FrameAccSensor:
                                AccFrame accFrame = (AccFrame) minewFrame;

                                break;
                            case FrameHTSensor:
                                HTFrame htFrame = (HTFrame) minewFrame;


                                break;
                            case FrameTLM:
                                TlmFrame tlmFrame = (TlmFrame) minewFrame;


                                break;
                            case FrameURL:
                                UrlFrame urlFrame = (UrlFrame) minewFrame;

                                break;
                            case FrameLightSensor:
                                LightFrame lightFrame = (LightFrame) minewFrame;

                                break;
                            case FrameForceSensor:
                                ForceFrame forceFrame = ((ForceFrame) minewFrame);

                                break;
                            case FrameTempSensor:
                                TemperatureFrame temperatureFrame = (TemperatureFrame) minewFrame;

                                TEMPERATURE = temperatureFrame.getValue();
                                NumberFormat.getInstance(Locale.ENGLISH).format(TEMPERATURE);

                                temperatureTextView.setText(String.format("%.1f", TEMPERATURE));
                                BATTERY = temperatureFrame.getBattery();
                                break;
                            case FrameTVOCSensor:
                                TvocFrame tvocFrame = (TvocFrame) minewFrame;

                                break;
                            default:
                                break;
                        }
                    }
                }

            }
        });


    }

    private final ConnectionStatueListener connectionStatueListener = new ConnectionStatueListener() {
        @Override
        public void onUpdateConnectionStatus(final ConnectionStatus connectionStatus, final GetPasswordListener getPasswordListener) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    switch (connectionStatus) {
                        case CONNECTING:

                            Toast.makeText(MainActivity.this, "CONNECTING", Toast.LENGTH_SHORT).show();
                            break;
                        case CONNECTED:

                            Toast.makeText(MainActivity.this, "CONNECTED", Toast.LENGTH_SHORT).show();
                            break;
                        case READINGINFO:

                            Toast.makeText(MainActivity.this, "READINGINFO", Toast.LENGTH_SHORT).show();
                            break;
                        case DEVICEVALIDATING:

                            Toast.makeText(MainActivity.this, "DEVICEVALIDATING", Toast.LENGTH_SHORT).show();
                            break;
                        case PASSWORDVALIDATING:

                            Toast.makeText(MainActivity.this, "PASSWORDVALIDATING", Toast.LENGTH_SHORT).show();
                            String password = "minew123";
                            getPasswordListener.getPassword(password);
                            break;
                        case SYNCHRONIZINGTIME:

                            Toast.makeText(MainActivity.this, "SYNCHRONIZINGTIME", Toast.LENGTH_SHORT).show();
                            break;
                        case READINGCONNECTABLE:

                            Toast.makeText(MainActivity.this, "READINGCONNECTABLE", Toast.LENGTH_SHORT).show();
                            break;
                        case READINGFEATURE:

                            Toast.makeText(MainActivity.this, "READINGFEATURE", Toast.LENGTH_SHORT).show();
                            break;
                        case READINGFRAMES:

                            Toast.makeText(MainActivity.this, "READINGFRAMES", Toast.LENGTH_SHORT).show();
                            break;
                        case READINGTRIGGERS:

                            Toast.makeText(MainActivity.this, "READINGTRIGGERS", Toast.LENGTH_SHORT).show();
                            break;
                        case COMPLETED:

                            Toast.makeText(MainActivity.this, "COMPLETED", Toast.LENGTH_SHORT).show();
                            break;
                        case CONNECTFAILED:
                        case DISCONNECTED:

                            Toast.makeText(MainActivity.this, "DISCONNECTED", Toast.LENGTH_SHORT).show();
                            break;
                    }

                }
            });
        }

        @Override
        public void onError(MTException e) {

        }
    };

    private void initializeBluetoothManager() {
        bluetoothCentralManager = MTCentralManager.getInstance(this);

        bluetoothCentralManager.startService();
        BluetoothState bluetoothState = bluetoothCentralManager.getBluetoothState(this);
        switch (bluetoothState) {
            case BluetoothStateNotSupported:

                break;
            case BluetoothStatePowerOff:

                break;
            case BluetoothStatePowerOn:

                break;
        }

        bluetoothCentralManager.setBluetoothChangedListener(new OnBluetoothStateChangedListener() {
            @Override
            public void onStateChanged(BluetoothState state) {
                switch (state) {
                    case BluetoothStateNotSupported:
                    case BluetoothStatePowerOff:
                    case BluetoothStatePowerOn:
                        break;
                }
            }
        });
    }

    private void requestLocationPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_COARSE_LOCATION);
        } else {
            initData();
        }
    }

    private void initData() {

        try {
            bluetoothCentralManager.startScan();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Phone does not have Bluetooth!!", Toast.LENGTH_LONG).show();
        }
    }

    private void playAudioFromPath(String path) {


        if (path.startsWith("/document/raw:")) {
            path = path.replaceFirst("/document/raw:", "");
        }

        try {
            mediaPlayer.setDataSource(path);
            mediaPlayer.prepareAsync();
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mediaPlayer.start();
                }
            });
        } catch (IllegalArgumentException e) {

            e.printStackTrace();

        } catch (SecurityException e) {

            e.printStackTrace();

        } catch (IllegalStateException e) {

            e.printStackTrace();

        } catch (IOException e) {

            e.printStackTrace();

        }
    }

    /**
     * Reset all activity flags for testing purposes
     */
    private void resetActivityFlags() {
        isOpenKhotabActivity = false;
        isOpenClosePhone = false;
        isOpenAlkhushuePhone = false;
        isOpenAzkarPhone = false;
        isOpenAds = false;
        isAdsScreenOpen = false;
        isClosePhoneScreenOpen = false;
        isAzkarScreenOpen = false;
        isAlkhushueScreenOpen = false;
        isKhotabScreenOpen = false;
    }

    private void openClosePhoneActivity() {
        if (!isOpenClosePhone) {
            Intent intent = new Intent(MainActivity.this, ShowClosePhoneActivity.class);
            intent.putExtra("PRAY", 1);
            startActivity(intent);
            isOpenClosePhone = true;
        }
    }

    /**
     * Apply theme configuration based on selected theme
     *
     * @param themeId The theme ID to apply
     */
    private void applyThemeConfiguration(int themeId) {
        ThemeConfiguration config = getThemeConfigurations().get(themeId);
        if (config == null) {
            // Fallback to default theme (0)
            config = getThemeConfigurations().get(0);
        }

        // Set content view
        assert config != null;
        setContentView(config.layoutResId);

        // Set orientation
        setRequestedOrientation(config.orientation);

        // Initialize common views
        newsTextView = findViewById(R.id.textviewNews);

        if (themeId != 10) {
            mainLayout = findViewById(R.id.relativeLayout);
            // Apply background if specified
            if (config.backgroundResId != 0 && mainLayout != null) {
                Resources res = getResources();
                Drawable background = res.getDrawable(config.backgroundResId);
                mainLayout.setBackground(background);
            }
        }

        // Apply news text color if specified
        if (config.newsTextColor != null && newsTextView != null) {
            newsTextView.setTextColor(Color.parseColor(config.newsTextColor));
        }

        // Initialize prayer indicator views if needed
        if (config.hasNextPrayerIndicator != 0) {
            nextPrayerIndicator = findViewById(R.id.img_next_azan);
        }
        if (config.hasNextPrayerIndicatorSecondary != 0) {
            nextPrayerIndicatorSecondary = findViewById(R.id.img_next_azan1);
        }

        // Apply image arrays
        dayImages = config.dayImages;
        monthImages = config.monthImages;
        hijriMonthImages = config.hijriMonthImages;
        digitImages = config.digitImages;
        timeDigitImages = config.timeDigitImages;
        azanTimeDigitImages = config.azanTimeDigitImages;
        iqamahTimeDigitImages = config.iqamahTimeDigitImages;
        iqamahCountdownDigitImages = config.iqamahCountdownDigitImages;
        azanCountdownDigitImages = config.azanCountdownDigitImages;
        secondDigitImages = config.secondDigitImages;
    }
}