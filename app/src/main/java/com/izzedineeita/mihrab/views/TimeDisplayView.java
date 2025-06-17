package com.izzedineeita.mihrab.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.Nullable;
import com.izzedineeita.mihrab.R;

public class TimeDisplayView extends LinearLayout {
    private TextView hourTens;
    private TextView hourOnes;
    private TextView minuteTens;
    private TextView minuteOnes;
    private TextView secondTens;
    private TextView secondOnes;
    private TextView colon;

    public TimeDisplayView(Context context) {
        super(context);
        init();
    }

    public TimeDisplayView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.view_time_display, this);
        hourTens = findViewById(R.id.hour_tens);
        hourOnes = findViewById(R.id.hour_ones);
        minuteTens = findViewById(R.id.minute_tens);
        minuteOnes = findViewById(R.id.minute_ones);
        secondTens = findViewById(R.id.second_tens);
        secondOnes = findViewById(R.id.second_ones);
        colon = findViewById(R.id.colon);
    }

    public void setTime(int hours, int minutes, int seconds) {
        hourTens.setText(String.valueOf(hours / 10));
        hourOnes.setText(String.valueOf(hours % 10));
        minuteTens.setText(String.valueOf(minutes / 10));
        minuteOnes.setText(String.valueOf(minutes % 10));
        secondTens.setText(String.valueOf(seconds / 10));
        secondOnes.setText(String.valueOf(seconds % 10));
    }

    public void setTime(String time) {
        String[] parts = time.split(":");
        if (parts.length >= 2) {
            int hours = Integer.parseInt(parts[0]);
            int minutes = Integer.parseInt(parts[1]);
            int seconds = parts.length > 2 ? Integer.parseInt(parts[2]) : 0;
            setTime(hours, minutes, seconds);
        }
    }

    public void showSeconds(boolean show) {
        secondTens.setVisibility(show ? VISIBLE : GONE);
        secondOnes.setVisibility(show ? VISIBLE : GONE);
    }
} 