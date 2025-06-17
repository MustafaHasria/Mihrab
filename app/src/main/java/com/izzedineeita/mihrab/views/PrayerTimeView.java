package com.izzedineeita.mihrab.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.Nullable;
import com.izzedineeita.mihrab.R;

public class PrayerTimeView extends LinearLayout {
    private TextView iqamahHourTens;
    private TextView iqamahHourOnes;
    private TextView iqamahMinuteTens;
    private TextView iqamahMinuteOnes;
    private TextView azanHourTens;
    private TextView azanHourOnes;
    private TextView azanMinuteTens;
    private TextView azanMinuteOnes;
    private TextView prayerName;

    public PrayerTimeView(Context context) {
        super(context);
        init();
    }

    public PrayerTimeView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.view_prayer_time, this);
        iqamahHourTens = findViewById(R.id.iqamah_hour_tens);
        iqamahHourOnes = findViewById(R.id.iqamah_hour_ones);
        iqamahMinuteTens = findViewById(R.id.iqamah_minute_tens);
        iqamahMinuteOnes = findViewById(R.id.iqamah_minute_ones);
        azanHourTens = findViewById(R.id.azan_hour_tens);
        azanHourOnes = findViewById(R.id.azan_hour_ones);
        azanMinuteTens = findViewById(R.id.azan_minute_tens);
        azanMinuteOnes = findViewById(R.id.azan_minute_ones);
        prayerName = findViewById(R.id.prayer_name);
    }

    public void setPrayerName(String name) {
        prayerName.setText(name);
    }

    public void setIqamahTime(String time) {
        String[] parts = time.split(":");
        if (parts.length >= 2) {
            int hours = Integer.parseInt(parts[0]);
            int minutes = Integer.parseInt(parts[1]);
            iqamahHourTens.setText(String.valueOf(hours / 10));
            iqamahHourOnes.setText(String.valueOf(hours % 10));
            iqamahMinuteTens.setText(String.valueOf(minutes / 10));
            iqamahMinuteOnes.setText(String.valueOf(minutes % 10));
        }
    }

    public void setAzanTime(String time) {
        String[] parts = time.split(":");
        if (parts.length >= 2) {
            int hours = Integer.parseInt(parts[0]);
            int minutes = Integer.parseInt(parts[1]);
            azanHourTens.setText(String.valueOf(hours / 10));
            azanHourOnes.setText(String.valueOf(hours % 10));
            azanMinuteTens.setText(String.valueOf(minutes / 10));
            azanMinuteOnes.setText(String.valueOf(minutes % 10));
        }
    }
} 