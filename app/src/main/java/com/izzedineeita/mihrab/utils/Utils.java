package com.izzedineeita.mihrab.utils;// Created by Izzedine Eita on 10/29/2020.

// Created by Izzedine Eita on 10/29/2020.


import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.izzedineeita.mihrab.R;
import com.izzedineeita.mihrab.constants.Constants;
import com.izzedineeita.mihrab.constants.DateHigri;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Random;


public class Utils {
    public static String PREFS = "Mhrab";
    public static final String ENGLISH = "English";

    private final static String[] iMONTHS = {"", "محرم", "صفر", "ربيع الأول", "ربيع الثاني", "جمادى الأولى", "جمادى الآخر", "رجب",
            "شعبان", "رمضان", "شوال", "ذو القعدة", " ذو الحجة"};

    public static long getTime(Calendar cal, String time) {

        if (!TextUtils.isEmpty(time)) {
            String[] convTime = time.split(":");
            cal.set(Calendar.HOUR_OF_DAY, Integer.valueOf(convTime[0]));
            cal.set(Calendar.MINUTE, Integer.valueOf(convTime[1]));
            if (convTime.length > 2)
                cal.set(Calendar.SECOND, Integer.valueOf(convTime[2]));
            else
                cal.set(Calendar.SECOND, 0);

            cal.set(Calendar.MILLISECOND, 0);
        }
        return cal.getTimeInMillis();
    }

    private static DateFormat df = new SimpleDateFormat("yyyyMMddHHmmss", Locale.US);

    public static String getFormattedCurrentDate() {
        Calendar c = Calendar.getInstance();
        return df.format(c.getTime());
    }


    public static String getTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss", Locale.ENGLISH);
        Date date = new Date();
        return dateFormat.format(date);
    }

    public static boolean isSaturday() {
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        return day == Calendar.SATURDAY;
    }

    public static boolean isSunday() {
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        return day == Calendar.SUNDAY;
    }

    public static boolean isMonday() {
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        return day == Calendar.MONDAY;
    }

    public static boolean isTuesday() {
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        return day == Calendar.TUESDAY;
    }

    public static boolean isWednesday() {
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        return day == Calendar.WEDNESDAY;
    }

    public static boolean isThursday() {
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        return day == Calendar.THURSDAY;
    }

    public static boolean isFriday() {
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        return day == Calendar.FRIDAY;
    }


    public static int random9() {
        Random r = new Random(System.currentTimeMillis());
        return ((1 + r.nextInt(2)) * 100000000 + r.nextInt(100000000));
    }

    public static void hideKeyboard(Activity activity) {
        View view = activity.getCurrentFocus();
        if (view != null) {
            ((InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE)).
                    hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    public static boolean compareDates(String d1, String d2) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd hh:mm", Locale.ENGLISH);
            Date date1 = sdf.parse(d1);
            Date date2 = sdf.parse(d2);

            System.out.println("Date1 " + sdf.format(date1));
            System.out.println("Date2 " + sdf.format(date2));
            System.out.println();
            if (date1.after(date2)) {
                //   System.out.println("Date1 is after Date2");
                return false;
            } else if (date1.before(date2)) {
                //   System.out.println("Date1 is before Date2");
                return true;
            } else {
                //   System.out.println("Date1 is equal Date2");
                return true;
            }
        } catch (ParseException ex) {
            ex.printStackTrace();
            return true;
        }
    }

    public static boolean compareDate(String d1, String d2) {
        System.out.println("Date1 * " + d1);
        System.out.println("Date2 *" + d2);
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
            Date date1 = sdf.parse(d1);
            Date date2 = sdf.parse(d2);
            System.out.println("Date1 " + sdf.format(date1));
            System.out.println("Date2 " + sdf.format(date2));
            System.out.println();
            if (date1.after(date2)) {
                //   System.out.println("Date1 is after Date2");
                return false;
            } else if (date1.before(date2)) {
                //    System.out.println("Date1 is before Date2");
                return true;
            } else {
                //    System.out.println("Date1 is equal Date2");
                return true;
            }
        } catch (ParseException ex) {
            ex.printStackTrace();
            return true;
        }
    }

    public static boolean compareTimes(String d1, String d2) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.ENGLISH);
            Date date1 = sdf.parse(d1);
            Date date2 = sdf.parse(d2);

            System.out.println("Date1 " + sdf.format(date1));
            System.out.println("Date2 " + sdf.format(date2));
            System.out.println();
            if (date1.after(date2)) {
                //    System.out.println("Date1 is after Date2");
                return false;
            } else if (date1.before(date2)) {
                //   System.out.println("Date1 is before Date2");
                return true;
            } else {// if(date1.equals(date2)){
                //    System.out.println("Date1 is equal Date2");
                return false;
            }
        } catch (ParseException ex) {
            ex.printStackTrace();
            return true;
        }
    }

    public static void showCustomToast(Context context, String msg) {
        Toast toast = Toast.makeText(context, msg, Toast.LENGTH_LONG);
        toast.show();
    }

    public static String getName(String name) {
        String[] username = name.split("@");
        return username[0];
    }


    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager in = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);

        if (activity.getCurrentFocus() != null)
            if (in != null) {
                in.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
            }
    }

    public static int getColor(Activity activity, int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            return activity.getColor(color);

        } else {
            return activity.getResources().getColor(color);
        }
    }

    public static String[] writeIslamicDate(Activity act, DateHigri hd) {
        String[] wdNames = {act.getString(R.string.sun), act.getString(R.string.mon), act.getString(R.string.tus),
                act.getString(R.string.wes)
                , act.getString(R.string.ths), act.getString(R.string.fri), act.getString(R.string.sat)};

        String[] MonthNames = {act.getString(R.string.em1), act.getString(R.string.em2), act.getString(R.string.em3),
                act.getString(R.string.em4), act.getString(R.string.em5), act.getString(R.string.em6), act.getString(R.string.em7),
                act.getString(R.string.em8), act.getString(R.string.em9), act.getString(R.string.em10), act.getString(R.string.em11)
                , act.getString(R.string.em12)};
        Calendar today = Calendar.getInstance();
        double day = today.get(Calendar.DAY_OF_MONTH);
        double month = today.get(Calendar.MONTH);
        double year = today.get(Calendar.YEAR);

        int iDayN = hd.date1();
        int hijriDiff = Pref.getValue(act, Constants.PREF_HEJRY_INT, 0);

        String[] s = new String[9];
        s[0] = String.valueOf(iDayN);
        s[1] = String.valueOf((int) day);
        s[2] = String.valueOf((int) month);
        s[3] = String.valueOf((int) year);
        int[] dt = HijriCalendar.getSimpleDate(today, hijriDiff);

        s[4] = String.valueOf(dt[1]);
        s[5] = String.valueOf(dt[2]);
        s[6] = String.valueOf(dt[3]);


        return s;
    }


    public static String writeMDate(Activity act) {

        String[] MonthNames = {act.getString(R.string.em1), act.getString(R.string.em2), act.getString(R.string.em3),
                act.getString(R.string.em4), act.getString(R.string.em5), act.getString(R.string.em6), act.getString(R.string.em7),
                act.getString(R.string.em8), act.getString(R.string.em9), act.getString(R.string.em10), act.getString(R.string.em11)
                , act.getString(R.string.em12)};
        boolean dayTest = true;
        Calendar today = Calendar.getInstance();
        double day = today.get(Calendar.DAY_OF_MONTH);
        double month = today.get(Calendar.MONTH);
        double year = today.get(Calendar.YEAR);
        // int iDayN = hd.date1();
        return (int) day + " " + MonthNames[(int) month]
                + " " + (int) year + " " + act.getString(R.string.mt1);
    }

    public static String writeMDate1(Activity act) {
        String[] MonthNames = {act.getString(R.string.em1), act.getString(R.string.em2), act.getString(R.string.em3),
                act.getString(R.string.em4), act.getString(R.string.em5), act.getString(R.string.em6), act.getString(R.string.em7),
                act.getString(R.string.em8), act.getString(R.string.em9), act.getString(R.string.em10), act.getString(R.string.em11)
                , act.getString(R.string.em12)};
        Calendar today = Calendar.getInstance();
        double day = today.get(Calendar.DAY_OF_MONTH);
        double month = today.get(Calendar.MONTH);
        return (int) day + " " + MonthNames[(int) month];
    }

    public static String writeHDate(Activity act) {
        Calendar today = Calendar.getInstance();
        int hijriDiff = Pref.getValue(act, Constants.PREF_HEJRY_INT, 0);
        int[] dt = HijriCalendar.getSimpleDate(today, hijriDiff);

        int hijriDiff1 = Pref.getValue(act, Constants.PREF_HEJRY_INT1, 0);
        dt[1] = dt[1] + hijriDiff1;
        return dt[1] + hijriDiff1 + " " + iMONTHS[dt[2]] + " " + dt[3] + act.getString(R.string.mt);
    }

    public static String writeHDate1(Activity act) {
        Calendar today = Calendar.getInstance();
        int hijriDiff = Pref.getValue(act, Constants.PREF_HEJRY_INT, 0);
        int[] dt = HijriCalendar.getSimpleDate(today, hijriDiff);
        int hijriDiff1 = Pref.getValue(act, Constants.PREF_HEJRY_INT1, 0);
        dt[1] = dt[1] + hijriDiff1;
        return dt[1] + hijriDiff1 + " " + iMONTHS[dt[2]];
    }


}

