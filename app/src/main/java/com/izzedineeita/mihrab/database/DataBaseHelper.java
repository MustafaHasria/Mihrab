package com.izzedineeita.mihrab.database;// Created by Izzedine Eita on 11/23/2020.

// Created by Izzedine Eita on 11/23/2020.

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.izzedineeita.mihrab.model.Ads;
import com.izzedineeita.mihrab.model.AdsPeriods;
import com.izzedineeita.mihrab.model.AzkarModel;
import com.izzedineeita.mihrab.model.Khotab;
import com.izzedineeita.mihrab.model.News;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class DataBaseHelper extends SQLiteOpenHelper {

    private static String TAG = "DataBaseHelper";
    private static String DB_PATH = "";
    private static String DB_NAME = "prayer_times5.db";//
    private SQLiteDatabase mDataBase;
    private final Context mContext;

    public DataBaseHelper(Context context) {
        super(context, DB_NAME, null, 1);
        if (android.os.Build.VERSION.SDK_INT >= 17) {
            DB_PATH = context.getApplicationInfo().dataDir + "/databases/";
        } else {
            DB_PATH = "/data/data/" + context.getPackageName() + "/databases/";
        }
        this.mContext = context;

        try {
            createDataBase();
            openDataBase();
            mDataBase = getReadableDatabase();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void createDataBase() throws IOException {

        boolean mDataBaseExist = checkDataBase();
        if (!mDataBaseExist) {
            this.getReadableDatabase();
            this.close();
            try {
                copyDataBase();
            } catch (IOException mIOException) {
                throw new Error("ErrorCopyingDataBase");
            }
        }
    }

    private boolean checkDataBase() {
        File dbFile = new File(DB_PATH + DB_NAME);
        return dbFile.exists();
    }

    private void copyDataBase() throws IOException {
        InputStream mInput = mContext.getAssets().open(DB_NAME);
        String outFileName = DB_PATH + DB_NAME;
        OutputStream mOutput = new FileOutputStream(outFileName);
        byte[] mBuffer = new byte[1024];
        int mLength;
        while ((mLength = mInput.read(mBuffer)) > 0) {
            mOutput.write(mBuffer, 0, mLength);
        }
        mOutput.flush();
        mOutput.close();
        mInput.close();
    }

    public boolean openDataBase() throws SQLException {
        String mPath = DB_PATH + DB_NAME;
        mDataBase = SQLiteDatabase.openDatabase(mPath, null, SQLiteDatabase.CREATE_IF_NECESSARY);
        return mDataBase != null;
    }

    @Override
    public synchronized void close() {
        if (mDataBase != null)
            mDataBase.close();
        super.close();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public String[] getPrayerTimes(String tableName, String date) {
        String[] data = new String[7];
        try {
            String selectQuery = "SELECT * FROM " + tableName + " where column_0=?";
            Cursor cursor = mDataBase.rawQuery(selectQuery, new String[]{date});
            if (cursor.moveToFirst()) {
                data[0] = cursor.getString(0);
                data[1] = cursor.getString(1);
                data[2] = cursor.getString(2);
                data[3] = cursor.getString(3);
                data[4] = cursor.getString(4);
                data[5] = cursor.getString(5);
                data[6] = cursor.getString(6);
            }
        } catch (SQLException e) {
            Log.e("SQLException", e.getLocalizedMessage());
        }
        return data;
    }

    public String[] getPrayerTimesFriday(String tableName, String date) {
        String[] data = new String[7];
        try {
            String selectQuery = "SELECT * FROM " + tableName + " where column_0=?";

            Cursor cursor = mDataBase.rawQuery(selectQuery, new String[]{date});
            if (cursor.moveToFirst()) {
                data[0] = cursor.getString(0);
                data[1] = cursor.getString(1);
                data[2] = cursor.getString(2);
                data[3] = cursor.getString(3);
                data[4] = cursor.getString(4);
                data[5] = cursor.getString(5);
                data[6] = cursor.getString(6);
            }
        } catch (SQLException e) {
            Log.e("XXX9", e.getLocalizedMessage());
        }
        return data;
    }

    public boolean getAzkarById(int Id) {
        boolean isExist = false;
        String selectQuery = "SELECT Id FROM azkar WHERE Id=" + Id + "";
        Cursor cursor = mDataBase.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            isExist = true;
        }
        return isExist;
    }

    public void insertAzkar(ArrayList<AzkarModel> a) {
        SQLiteDatabase db = getWritableDatabase();
        db.enableWriteAheadLogging();
        db.beginTransaction();

        int count = 0;
        int len = a.size();
        //Delete services
        for (int i = 0; i < len; i++) {
            AzkarModel current = a.get(i);
            if (!current.isDeleted()) {
                addAzkar(db, current);
                count++;
            }
        }

        db.setTransactionSuccessful();
        db.endTransaction();
        db.close();
    }

    private boolean addAzkar(SQLiteDatabase db, AzkarModel object) {
        ContentValues values = new ContentValues();
        db.delete("azkar", "Id=" + object.getId(), null);
        values.put("Id", object.getId());
        values.put("TextAzakar", object.getTextAzakar());
        values.put("isDeleted", object.isDeleted() ? 1 : 0);
        values.put("Fajr", object.isFajr() ? 1 : 0);
        values.put("Dhuhr", object.isDhuhr() ? 1 : 0);
        values.put("Asr", object.isAsr() ? 1 : 0);
        values.put("Magrib", object.isMagrib() ? 1 : 0);
        values.put("Isha", object.isha() ? 1 : 0);
        values.put("sort", object.getSort());
        values.put("Count", object.getCount());

        long rowid = db.insert("azkar", null, values);
        return rowid != -1;
    }

    public void delAzkar(int id) {

        mDataBase.execSQL("DELETE  FROM azkar where Id=" + id + "");
    }

//    public ArrayList<AzkarModel> getAzkar() {
//        ArrayList<AzkarModel> azkars = new ArrayList<>();
//        String selectQuery = "SELECT * FROM azkar ORDER BY sort ASC ";
//        Cursor cursor = mDataBase.rawQuery(selectQuery, null);
//        if (cursor.moveToFirst()) {
//            do {
//                AzkarModel object = new AzkarModel();
//                object.setId(cursor.getInt(cursor.getColumnIndex("Id")));
//                object.setTextAzakar(cursor.getString(cursor.getColumnIndex("TextAzakar")));
//                object.setDeleted(cursor.getInt(cursor.getColumnIndex("isDeleted")) == 1);
//                object.setFajr(cursor.getInt(cursor.getColumnIndex("Fajr")) == 1);
//                object.setDhuhr(cursor.getInt(cursor.getColumnIndex("Dhuhr")) == 1);
//                object.setAsr(cursor.getInt(cursor.getColumnIndex("Asr")) == 1);
//                object.setMagrib(cursor.getInt(cursor.getColumnIndex("Magrib")) == 1);
//                object.setIsha(cursor.getInt(cursor.getColumnIndex("Isha")) == 1);
//                object.setSort(cursor.getInt(cursor.getColumnIndex("sort")));
//                object.setCount(cursor.getInt(cursor.getColumnIndex("Count")));
//                azkars.add(object);
//            } while (cursor.moveToNext());
//        }
//        return azkars;
//    }

    @SuppressLint("Range")
    public ArrayList<AzkarModel> getAzkar(String tableName) {
        ArrayList<AzkarModel> azkars = new ArrayList<>();
        try {
            String selectQuery = "SELECT * FROM " + tableName + " ORDER BY sort ASC";
            Cursor cursor = mDataBase.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                AzkarModel object = new AzkarModel();
//                data[0] = cursor.getString(0);
//                data[1] = cursor.getString(1);
//                data[2] = cursor.getString(2);
//                data[3] = cursor.getString(3);
//                data[4] = cursor.getString(4);
//                data[5] = cursor.getString(5);
//                data[6] = cursor.getString(6);
                object.setId(cursor.getInt(cursor.getColumnIndex("Id")));
                object.setTextAzakar(cursor.getString(cursor.getColumnIndex("TextAzakar")));
                object.setDeleted(cursor.getInt(cursor.getColumnIndex("isDeleted")) == 1);
                object.setFajr(cursor.getInt(cursor.getColumnIndex("Fajr")) == 1);
                object.setDhuhr(cursor.getInt(cursor.getColumnIndex("Dhuhr")) == 1);
                object.setAsr(cursor.getInt(cursor.getColumnIndex("Asr")) == 1);
                object.setMagrib(cursor.getInt(cursor.getColumnIndex("Magrib")) == 1);
                object.setIsha(cursor.getInt(cursor.getColumnIndex("Isha")) == 1);
                object.setSort(cursor.getInt(cursor.getColumnIndex("sort")));
                object.setCount(cursor.getInt(cursor.getColumnIndex("Count")));
                azkars.add(object);
            }
        } catch (SQLException e) {
            Log.e("SQLException", e.getLocalizedMessage());
        }
        return azkars;
    }

    @SuppressLint("Range")
    public ArrayList<Ads> getAdsList(int masjedID) {
        ArrayList<Ads> adsList = new ArrayList<>();
        String selectQuery = "SELECT * FROM Advertisement WHERE MasjedID=" + masjedID + "";
        Cursor cursor = mDataBase.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                Ads object = new Ads();
                object.setId(cursor.getInt(cursor.getColumnIndex("id")));
                object.setMasjedID(cursor.getInt(cursor.getColumnIndex("MasjedID")));
                object.setType(cursor.getInt(cursor.getColumnIndex("Type")));
                object.setTitle(cursor.getString(cursor.getColumnIndex("Title")));
                object.setText(cursor.getString(cursor.getColumnIndex("Text")));
                object.setImage(cursor.getString(cursor.getColumnIndex("Image")));
                object.setImage1(cursor.getString(cursor.getColumnIndex("Image1")));
                object.setImage2(cursor.getString(cursor.getColumnIndex("Image2")));
                object.setImage3(cursor.getString(cursor.getColumnIndex("Image3")));
                object.setImage4(cursor.getString(cursor.getColumnIndex("Image4")));
                object.setImage5(cursor.getString(cursor.getColumnIndex("Image5")));
                object.setImage6(cursor.getString(cursor.getColumnIndex("Image6")));
                object.setImage6(cursor.getString(cursor.getColumnIndex("Image6")));
                object.setImageSec(cursor.getInt(cursor.getColumnIndex("ImageSec")));
                object.setVideo(cursor.getString(cursor.getColumnIndex("Video")));
                object.setVideo1(cursor.getString(cursor.getColumnIndex("Video1")));
                object.setVideo2(cursor.getString(cursor.getColumnIndex("Video2")));
                object.setStartDate(cursor.getString(cursor.getColumnIndex("StartDate")));
                object.setEndDate(cursor.getString(cursor.getColumnIndex("EndDate")));
                adsList.add(object);
            } while (cursor.moveToNext());
        }
        return adsList;
    }

    public void delAdvertisement(int id, int MasjedID) {
        mDataBase.execSQL("DELETE  FROM Advertisement where id=" + id + " AND MasjedID=" + MasjedID + "");
        mDataBase.execSQL("DELETE  FROM AdsPeriods where AdvId=" + id + "");
    }

    public boolean itHasConflict(int masjedId, AdsPeriods ads) {
        boolean hasConflict = false;
        String startDate = ads.getStartDate();
        String endDate = ads.getEndDate();
        String startTime = ads.getStartTime();
        String endTime = ads.getEndTime();
        int day = ads.getDay();

        String selectQuery = " select * from Advertisement left join  AdsPeriods on\n" +
                " (Advertisement.id = AdsPeriods.AdvId) where Advertisement.MasjedID=" + masjedId +
                " and (\n" +
                " (strftime('%Y-%m-%d', AdsPeriods.StartDate)  between  strftime('%Y-%m-%d','" + startDate +
                "')  and strftime('%Y-%m-%d','" + endDate + "')) \n" +
                "or ( strftime('%Y-%m-%d', AdsPeriods.EndDate)  between  strftime('%Y-%m-%d','" + startDate +
                "')  and strftime('%Y-%m-%d','" + endDate + "')) \n" +
                " or (strftime('%Y-%m-%d','" + startDate +
                "') between strftime('%Y-%m-%d', AdsPeriods.StartDate) and strftime('%Y-%m-%d', AdsPeriods" +
                ".EndDate) ) \n" +
                " or( strftime('%Y-%m-%d','" + endDate +
                "') between strftime('%Y-%m-%d', AdsPeriods.StartDate) and strftime('%Y-%m-%d', AdsPeriods" +
                ".EndDate))\n" +
                " ) and  (AdsPeriods.day =" + day +
                ") and ( (strftime('%H:%M', AdsPeriods.StartTime) between  strftime('%H:%M','" + startTime +
                "') " +
                " and  strftime('%H:%M','" + endTime + "') )\n" +
                " or(  strftime('%H:%M', AdsPeriods.EndTime)  between  strftime('%H:%M','" + startTime +
                "')  and  strftime('%H:%M','" + endTime + "'))\n" +
                " or ( strftime('%H:%M','" + startTime +
                "') between strftime('%H:%M', AdsPeriods.StartTime) and strftime('%H:%M', AdsPeriods" +
                ".EndTime))\n" +
                " or (  strftime('%H:%M','" + endTime +
                "') between strftime('%H:%M', AdsPeriods.StartTime) and strftime('%H:%M', AdsPeriods" +
                ".EndTime))\n" +
                " )";
        Cursor cursor = mDataBase.rawQuery(selectQuery, null);
        if (cursor.getCount() > 0 && cursor.moveToFirst()) {
            do {
                AdsPeriods object = new AdsPeriods();
                object.setId(cursor.getInt(cursor.getColumnIndex("id")));
                object.setAdvId(cursor.getInt(cursor.getColumnIndex("AdvId")));
                object.setDay(cursor.getInt(cursor.getColumnIndex("day")));
                object.setStartTime(cursor.getString(cursor.getColumnIndex("StartTime")));
                object.setEndTime(cursor.getString(cursor.getColumnIndex("EndTime")));
                hasConflict = true;
            } while (cursor.moveToNext());
        }
        return hasConflict;
    }

    public boolean itHasConflict(int masjedId, AdsPeriods ads, AdsPeriods lastObject) {
        boolean hasConflict = false;
        String startDate = ads.getStartDate();
        String endDate = ads.getEndDate();
        String startTime = ads.getStartTime();
        String endTime = ads.getEndTime();
        int day = ads.getDay();

        String selectQuery = " select * from Advertisement left join  AdsPeriods on\n" +
                " (Advertisement.id = AdsPeriods.AdvId) where Advertisement.MasjedID=" + masjedId +
                " and (\n" +
                " (strftime('%Y-%m-%d', AdsPeriods.StartDate)  between  strftime('%Y-%m-%d','" + startDate +
                "')  and strftime('%Y-%m-%d','" + endDate + "')) \n" +
                "or ( strftime('%Y-%m-%d', AdsPeriods.EndDate)  between  strftime('%Y-%m-%d','" + startDate +
                "')  and strftime('%Y-%m-%d','" + endDate + "')) \n" +
                " or (strftime('%Y-%m-%d','" + startDate +
                "') between strftime('%Y-%m-%d', AdsPeriods.StartDate) and strftime('%Y-%m-%d', AdsPeriods" +
                ".EndDate) ) \n" +
                " or( strftime('%Y-%m-%d','" + endDate +
                "') between strftime('%Y-%m-%d', AdsPeriods.StartDate) and strftime('%Y-%m-%d', AdsPeriods" +
                ".EndDate))\n" +
                " ) and  (AdsPeriods.day =" + day +
                ") and ( (strftime('%H:%M', AdsPeriods.StartTime) between  strftime('%H:%M','" + startTime +
                "') " +
                " and  strftime('%H:%M','" + endTime + "') )\n" +
                " or(  strftime('%H:%M', AdsPeriods.EndTime)  between  strftime('%H:%M','" + startTime +
                "')  and  strftime('%H:%M','" + endTime + "'))\n" +
                " or ( strftime('%H:%M','" + startTime +
                "') between strftime('%H:%M', AdsPeriods.StartTime) and strftime('%H:%M', AdsPeriods" +
                ".EndTime))\n" +
                " or (  strftime('%H:%M','" + endTime +
                "') between strftime('%H:%M', AdsPeriods.StartTime) and strftime('%H:%M', AdsPeriods" +
                ".EndTime))\n" +
                " ) and AdsPeriods.id not in " + "( SELECT id FROM AdsPeriods WHERE AdvId=" +
                lastObject.getAdvId()
                + " and  strftime('%H:%M', AdsPeriods.StartTime)=strftime('%H:%M','" +
                lastObject.getStartTime() + "')"
                + " and  strftime('%H:%M', AdsPeriods.EndTime)=strftime('%H:%M','" +
                lastObject.getEndTime() + "')" + ")";
        Cursor cursor = mDataBase.rawQuery(selectQuery, null);
        if (cursor.getCount() > 0 && cursor.moveToFirst()) {
            do {
                AdsPeriods object = new AdsPeriods();
                object.setId(cursor.getInt(cursor.getColumnIndex("id")));
                object.setAdvId(cursor.getInt(cursor.getColumnIndex("AdvId")));
                object.setDay(cursor.getInt(cursor.getColumnIndex("day")));
                object.setStartTime(cursor.getString(cursor.getColumnIndex("StartTime")));
                object.setEndTime(cursor.getString(cursor.getColumnIndex("EndTime")));
                hasConflict = true;
            } while (cursor.moveToNext());
        }
        return hasConflict;
    }


    public boolean updateAds(Ads object) {
        SQLiteDatabase db = getWritableDatabase();
        db.enableWriteAheadLogging();
        db.beginTransaction();
        ContentValues values = new ContentValues();
        values.put("MasjedID", object.getMasjedID());
        values.put("Type", object.getType());
        values.put("Title", object.getTitle());
        values.put("Text", object.getText());
        values.put("Image", object.getImage());
        values.put("Image1", object.getImage1());
        values.put("Image2", object.getImage2());
        values.put("Image3", object.getImage3());
        values.put("Image4", object.getImage4());
        values.put("Image5", object.getImage5());
        values.put("Image6", object.getImage6());
        values.put("ImageSec", object.getImageSec());
        values.put("Video", object.getVideo());
        values.put("Video1", object.getVideo1());
        values.put("Video2", object.getVideo2());
        values.put("StartDate", object.getStartDate());
        values.put("EndDate", object.getEndDate());

        long rowid = db.update("Advertisement", values, "id=" + object.getId(), null);
        db.setTransactionSuccessful();
        db.endTransaction();
        db.close();
        return rowid != -1;
    }

    public void insertAdsPeriod(ArrayList<AdsPeriods> a) {
        SQLiteDatabase db = getWritableDatabase();
        db.enableWriteAheadLogging();
        db.beginTransaction();

        int count = 0;
        int len = a.size();
        for (int i = 0; i < len; i++) {
            AdsPeriods current = a.get(i);
            AddAdsPeriod(db, current);
            count++;
        }

        db.setTransactionSuccessful();
        db.endTransaction();
        db.close();
    }

    private boolean AddAdsPeriod(SQLiteDatabase db, AdsPeriods object) {
        ContentValues values = new ContentValues();
        values.put("AdvId", object.getAdvId());
        values.put("StartTime", object.getStartTime());
        values.put("EndTime", object.getEndTime());
        values.put("day", object.getDay());
        values.put("StartDate", object.getStartDate());
        values.put("EndDate", object.getEndDate());
        long rowid = db.insert("AdsPeriods", null, values);
        return rowid != -1;
    }

    public String getAdvPeriods(int advId, String StartTime, String EndTime) {
        String day = "";
        String selectQuery = "SELECT day FROM AdsPeriods WHERE AdvId=" + advId
                + " and  strftime('%H:%M',StartTime)=strftime('%H:%M','" + StartTime + "') "
                + " and strftime('%H:%M',EndTime)=strftime('%H:%M','" + EndTime + "') ORDER By day ASC";
        Cursor cursor = mDataBase.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                day = cursor.getInt(cursor.getColumnIndex("day")) + "," + day;
            } while (cursor.moveToNext());
        }
        return day;
    }

    public String[] getAllAdvPeriods() {
//        String day = "";
//        String selectQuery = "SELECT day FROM AdsPeriods WHERE AdvId=" + advId
//                + " and  strftime('%H:%M',StartTime)=strftime('%H:%M','" + StartTime + "') "
//                + " and strftime('%H:%M',EndTime)=strftime('%H:%M','" + EndTime + "') ORDER By day ASC";
//        Cursor cursor = mDataBase.rawQuery(selectQuery, null);
//        if (cursor.moveToFirst()) {
//            do {
//                day = cursor.getInt(cursor.getColumnIndex("day")) + "," + day;
//            } while (cursor.moveToNext());
//        }
//        return day;

        String[] data = new String[7];
        try {
            String selectQuery = "SELECT * FROM " + "AdsPeriods" + " ORDER BY id ASC";
            Cursor cursor = mDataBase.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                data[0] = cursor.getString(0);
                data[1] = cursor.getString(1);
                data[2] = cursor.getString(2);
                data[3] = cursor.getString(3);
                data[4] = cursor.getString(4);
                data[5] = cursor.getString(5);
                data[6] = cursor.getString(6);

            }
        } catch (SQLException e) {
            Log.e("SQLException", e.getLocalizedMessage());
        }
        return data;
    }

    public String getAdvPeriodsIds(int advId, String StartTime, String EndTime) {
        String ids = "";
        String selectQuery = "SELECT id FROM AdsPeriods WHERE AdvId=" + advId
                + " and  strftime('%H:%M',StartTime)=strftime('%H:%M','" + StartTime + "') "
                + " and strftime('%H:%M',EndTime)=strftime('%H:%M','" + EndTime + "') ORDER By day ASC";
        Cursor cursor = mDataBase.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                ids = cursor.getInt(cursor.getColumnIndex("id")) + "," + ids;
            } while (cursor.moveToNext());
        }
        return ids;
    }

    public void delAdvPeriod(int advId, AdsPeriods lastObject) {
        mDataBase.execSQL("DELETE  FROM AdsPeriods where AdvId=" + advId + " and id in "
                + "( SELECT id FROM AdsPeriods WHERE AdvId=" + lastObject.getAdvId() + " and  StartTime='"
                + lastObject.getStartTime() + "' and EndTime='" + lastObject.getEndTime() + "')");
        Log.i("DBOperations", "DELETE  FROM AdsPeriods where AdvId=" + advId + " and id in "
                + "( SELECT id FROM AdsPeriods WHERE AdvId=" + lastObject.getAdvId() + " and  StartTime='"
                + lastObject.getStartTime() + "' and EndTime='" + lastObject.getEndTime() + "')");
    }

    public int getAdvPeriodsId(int advId, AdsPeriods adsPeriods) {
        int id = 0;
        String selectQuery = "SELECT id FROM AdsPeriods WHERE AdvId=" + advId
                + " and  strftime('%H:%M',StartTime)=strftime('%H:%M','" + adsPeriods.getStartTime() + "') "
                + " and strftime('%H:%M',EndTime)=strftime('%H:%M','" + adsPeriods.getStartTime() + "') "
                + " and  strftime('%Y-%m-%d',StartDate)= strftime('%Y-%m-%d','" + adsPeriods.getStartDate() +
                "') "
                + " and strftime('%Y-%m-%d',EndDate) =strftime('%Y-%m-%d','" + adsPeriods.getEndDate() +
                "') "
                + " and day=" + adsPeriods.getDay() + "";
        Cursor cursor = mDataBase.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                id = cursor.getInt(cursor.getColumnIndex("id"));
            } while (cursor.moveToNext());
        }
        return id;
    }

    public void delAdvPeriods(AdsPeriods adv) {
        Log.d("DBOperations", "table profile data eraser");
        mDataBase.execSQL("DELETE  FROM AdsPeriods  WHERE AdvId=" + adv.getAdvId() + " and  StartTime='"
                + adv.getStartTime() + "' and EndTime='" + adv.getEndTime() + "'");
        Log.i("DBOperations", "DELETE  FROM AdsPeriods  WHERE AdvId=" + adv.getAdvId() + " and  StartTime='"
                + adv.getStartTime() + "' and EndTime='" + adv.getEndTime() + "'");
    }

    public void delAdsPeriods(int id) {
        Log.d("DBOperations", "table profile data eraser");

        mDataBase.execSQL("DELETE  FROM AdsPeriods where AdvId=" + id + "");
        Log.i("DBOperations", "DELETE  FROM AdsPeriods where AdvId=" + id + "");
    }

    public ArrayList<AdsPeriods> getAdvPeriods(int advId) {
        ArrayList<AdsPeriods> list = new ArrayList<>();
        String selectQuery = "SELECT * FROM AdsPeriods WHERE AdvId=" + advId + "";
        //  Log.i("Query", selectQuery);
        Cursor cursor = mDataBase.rawQuery(selectQuery, null);
        // Log.i("Qu dataBase", "" + cursor.getCount());
        if (cursor.moveToFirst()) {
            do {
                AdsPeriods object = new AdsPeriods();
                object.setId(cursor.getInt(cursor.getColumnIndex("id")));
                object.setAdvId(cursor.getInt(cursor.getColumnIndex("AdvId")));
                object.setStartTime(cursor.getString(cursor.getColumnIndex("StartTime")));
                object.setEndTime(cursor.getString(cursor.getColumnIndex("EndTime")));
                object.setStartDate(cursor.getString(cursor.getColumnIndex("StartDate")));
                object.setEndDate(cursor.getString(cursor.getColumnIndex("EndDate")));
                object.setDay(cursor.getInt(cursor.getColumnIndex("day")));
                list.add(object);
            } while (cursor.moveToNext());
        }
        return list;
    }

    public int insertAds(Ads object) {
        long rowId = 0;
        SQLiteDatabase db = getWritableDatabase();
        db.enableWriteAheadLogging();
        db.beginTransaction();
        int count = 0;
        ContentValues values = new ContentValues();
        values.put("MasjedID", object.getMasjedID());
        values.put("Type", object.getType());
        values.put("Title", object.getTitle());
        values.put("Text", object.getText());
        values.put("Image", object.getImage());
        values.put("Video", object.getVideo());
        values.put("StartDate", object.getStartDate());
        values.put("EndDate", object.getEndDate());
        values.put("StartTime", object.getStartTime());
        values.put("EndTime", object.getEndTime());
        values.put("Image1", object.getImage1());
        values.put("Image2", object.getImage2());
        values.put("Image3", object.getImage3());
        values.put("Image4", object.getImage4());
        values.put("Image5", object.getImage5());
        values.put("Image6", object.getImage6());
        values.put("Video1", object.getVideo1());
        values.put("Video2", object.getVideo2());
        values.put("ImageSec", object.getImageSec());

        rowId = db.insert("Advertisement", null, values);
        //  Log.d("Sync service", "# of set inserted in Advertisement: " + count);
        db.setTransactionSuccessful();
        db.endTransaction();
        db.close();
        return (int) rowId;
    }

    public ArrayList<Ads> getAdsByDate(int masjedID, String date, String time, int day) {
        ArrayList<Ads> adses = new ArrayList<>();
        String selectQuery = "select * from Advertisement left join  AdsPeriods"
                + " on (Advertisement.id = AdsPeriods.AdvId)"
                + " where Advertisement.MasjedID=" + masjedID
                + " and (strftime('%Y-%m-%d','" + date + "')"
                +
                " between strftime('%Y-%m-%d', AdsPeriods.StartDate) and strftime('%Y-%m-%d', AdsPeriods" +
                ".EndDate))"
                + " and  (AdsPeriods.day =" + day + ")"
                + " and ( strftime('%H:%M', AdsPeriods.StartTime) =  strftime('%H:%M','" + time +
                "')) LIMIT 1";
        // Log.i("Query", selectQuery);
        Cursor cursor = mDataBase.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                Ads object = new Ads();
                object.setId(cursor.getInt(cursor.getColumnIndex("id")));
                object.setMasjedID(cursor.getInt(cursor.getColumnIndex("MasjedID")));
                object.setType(cursor.getInt(cursor.getColumnIndex("Type")));
                object.setTitle(cursor.getString(cursor.getColumnIndex("Title")));
                object.setText(cursor.getString(cursor.getColumnIndex("Text")));
                object.setImage(cursor.getString(cursor.getColumnIndex("Image")));
                object.setImage1(cursor.getString(cursor.getColumnIndex("Image1")));
                object.setImage2(cursor.getString(cursor.getColumnIndex("Image2")));
                object.setImage3(cursor.getString(cursor.getColumnIndex("Image3")));
                object.setImage4(cursor.getString(cursor.getColumnIndex("Image4")));
                object.setImage5(cursor.getString(cursor.getColumnIndex("Image5")));
                object.setImage6(cursor.getString(cursor.getColumnIndex("Image6")));
                object.setImageSec(cursor.getInt(cursor.getColumnIndex("ImageSec")));
                object.setVideo(cursor.getString(cursor.getColumnIndex("Video")));
                object.setVideo1(cursor.getString(cursor.getColumnIndex("Video1")));
                object.setVideo2(cursor.getString(cursor.getColumnIndex("Video2")));
                object.setStartDate(cursor.getString(cursor.getColumnIndex("StartDate")));
                object.setEndDate(cursor.getString(cursor.getColumnIndex("EndDate")));
                object.setStartTime(cursor.getString(cursor.getColumnIndex("StartTime")));
                object.setEndTime(cursor.getString(cursor.getColumnIndex("EndTime")));
                adses.add(object);
            } while (cursor.moveToNext());
        }
        return adses;
    }

    /**
     * Khotba
     */
    public void insertAllKhotab(ArrayList<Khotab> a) {
        SQLiteDatabase db = mDataBase;
        db.enableWriteAheadLogging();
        db.beginTransaction();
        if (a != null)
            db.delete("Khotab", "", null);
        int count = 0;
        int len = a.size();
        for (int i = 0; i < len; i++) {
            Khotab current = a.get(i);
            if (current.getIsDeleted() == 0) {
                addKhotab(current);
                count++;
            }
        }
        //   Log.d("Sync service", "# of New Khotab inserted : " + count);

        db.setTransactionSuccessful();
        db.endTransaction();
        db.close();
    }

    public boolean addKhotab(Khotab object) {
        SQLiteDatabase db = mDataBase;
        db.enableWriteAheadLogging();
        db.beginTransaction();
        Log.d("Khotab", "Khotba with ID: " + object.getId() + " is being added.");

        ContentValues values = new ContentValues();
        db.delete("Khotab", "Id=" + object.getId(), null);
        values.put("Id", object.getId());
        values.put("Title", object.getTitle());
        values.put("Title1", object.getTitle1());
        values.put("Title2", object.getTitle2());
        values.put("Title3", object.getTitle3());
        values.put("Body", object.getBody());
        values.put("Body1", object.getBody1());
        values.put("Body2", object.getBody2());
        values.put("Body3", object.getBody3());
        values.put("UpdatedAt", object.getUpdatedAt());
        values.put("DateKhotab", object.getDateKhotab());
        values.put("Description", object.getDescription());
        values.put("UrlVideoDeaf", object.getUrlVideoDeaf());
        values.put("TimeExpected", object.getTimeExpected());
        values.put("isDeleted", object.getIsDeleted());
        values.put("isException", object.getIsException());
        values.put("TranslationSpeed", object.getTranslationSpeed());
        values.put("Direction1RTL", object.isDirection1RTL() ? 1 : 0);
        values.put("Direction2RTL", object.isDirection2RTL() ? 1 : 0);
        values.put("textShowTime", object.getTextShowTime());
        values.put("ShowTime", object.getShowTime());
        long rowid = db.insert("Khotab", null, values);

        Log.d("Khotab", "Khotba Added + RowID: " + rowid);

        db.setTransactionSuccessful();
        db.endTransaction();
        db.close();

        return rowid != -1;
    }

    public ArrayList<Khotab> getAllKhotab() {
        ArrayList<Khotab> khotab = new ArrayList<>();
        String selectQuery = "SELECT * FROM Khotab";
        //  Log.i("Query", selectQuery);

        Cursor cursor = mDataBase.rawQuery(selectQuery, null);
        //  Log.i("Khotab", "getAllKhotab Count:" + cursor.getCount());
        if (cursor.moveToFirst()) {
            do {
                Khotab khotba = new Khotab();
                khotba.setId(cursor.getInt(cursor.getColumnIndex("Id")));
                khotba.setTitle(cursor.getString(cursor.getColumnIndex("Title")));
                khotba.setTitle1(cursor.getString(cursor.getColumnIndex("Title1")));
                khotba.setTitle2(cursor.getString(cursor.getColumnIndex("Title2")));
                khotba.setTitle3(cursor.getString(cursor.getColumnIndex("Title3")));
                khotba.setBody(cursor.getString(cursor.getColumnIndex("Body")));
                khotba.setBody1(cursor.getString(cursor.getColumnIndex("Body1")));
                khotba.setBody2(cursor.getString(cursor.getColumnIndex("Body2")));
                khotba.setBody3(cursor.getString(cursor.getColumnIndex("Body3")));
                khotba.setUpdatedAt(cursor.getString(cursor.getColumnIndex("UpdatedAt")));
                khotba.setDateKhotab(cursor.getString(cursor.getColumnIndex("DateKhotab")));
                khotba.setDescription(cursor.getString(cursor.getColumnIndex("Description")));
                khotba.setUrlVideoDeaf(cursor.getString(cursor.getColumnIndex("UrlVideoDeaf")));
                khotba.setTimeExpected(cursor.getInt(cursor.getColumnIndex("TimeExpected")));
                khotba.setIsDeleted(cursor.getInt(cursor.getColumnIndex("isDeleted")));
                khotba.setIsException(cursor.getInt(cursor.getColumnIndex("isException")));
                khotba.setTranslationSpeed(cursor.getInt(cursor.getColumnIndex("TranslationSpeed")));
                khotba.setDirection1RTL(cursor.getInt(cursor.getColumnIndex("Direction1RTL")) == 1);
                khotba.setDirection2RTL(cursor.getInt(cursor.getColumnIndex("Direction2RTL")) == 1);
                khotba.setTextShowTime(cursor.getInt(cursor.getColumnIndex("textShowTime")));
                khotba.setShowTime(cursor.getInt(cursor.getColumnIndex("ShowTime")));
                Log.d("Khotab", "" + khotba.getTitle());
                khotab.add(khotba);
            } while (cursor.moveToNext());
        }
        return khotab;
    }

    public Khotab getKhotba(String date) {
        Khotab object = new Khotab();
        String selectQuery = "SELECT * FROM Khotab WHERE DateKhotab='" + date + "' LIMIT 1";
        Log.i("Khotab", selectQuery);
        Cursor cursor = mDataBase.rawQuery(selectQuery, null);
        Log.i("Khotab", "" + cursor.getCount());
        if (cursor.getCount() > 0 && cursor.moveToFirst()) {
            do {
                object.setId(cursor.getInt(cursor.getColumnIndex("Id")));
                object.setTitle(cursor.getString(cursor.getColumnIndex("Title")));
                object.setTitle1(cursor.getString(cursor.getColumnIndex("Title1")));
                object.setTitle2(cursor.getString(cursor.getColumnIndex("Title2")));
                object.setTitle2(cursor.getString(cursor.getColumnIndex("Title3")));
                object.setBody(cursor.getString(cursor.getColumnIndex("Body")));
                object.setBody1(cursor.getString(cursor.getColumnIndex("Body1")));
                object.setBody2(cursor.getString(cursor.getColumnIndex("Body2")));
                object.setBody2(cursor.getString(cursor.getColumnIndex("Body3")));
                object.setUpdatedAt(cursor.getString(cursor.getColumnIndex("UpdatedAt")));
                object.setDateKhotab(cursor.getString(cursor.getColumnIndex("DateKhotab")));
                object.setDescription(cursor.getString(cursor.getColumnIndex("Description")));
                object.setUrlVideoDeaf(cursor.getString(cursor.getColumnIndex("UrlVideoDeaf")));
                object.setTimeExpected(cursor.getInt(cursor.getColumnIndex("TimeExpected")));
                object.setIsDeleted(cursor.getInt(cursor.getColumnIndex("isDeleted")));
                object.setIsException(cursor.getInt(cursor.getColumnIndex("isException")));
                object.setTranslationSpeed(cursor.getInt(cursor.getColumnIndex("TranslationSpeed")));
                object.setDirection1RTL(cursor.getInt(cursor.getColumnIndex("Direction1RTL")) == 1);
                object.setDirection2RTL(cursor.getInt(cursor.getColumnIndex("Direction2RTL")) == 1);
                object.setTextShowTime(cursor.getInt(cursor.getColumnIndex("textShowTime")));
                object.setShowTime(cursor.getInt(cursor.getColumnIndex("ShowTime")));
                Log.d("Khotab", "" + object.getTranslationSpeed());
            } while (cursor.moveToNext());
        } else object = null;
        return object;
    }

    public void deleteKhotba(int id) {
        Log.d("DBOperations", "delete khotba");

        mDataBase.execSQL("DELETE  FROM Khotab where Id=" + id + "");
        Log.i("DBOperations", "DELETE  FROM Khotab where Id=" + id + "");
    }

    public ArrayList<News> getNews() {
        ArrayList<News> adses = new ArrayList<>();
        String selectQuery = "SELECT * FROM News  ORDER BY sort ASC";
        Log.i("Query", selectQuery);
        Cursor cursor = mDataBase.rawQuery(selectQuery, null);
        Log.i("dataBase", "" + cursor.getCount());
        if (cursor.moveToFirst()) {
            do {
                News object = new News();
                object.setId(cursor.getInt(cursor.getColumnIndex("Id")));
                object.setTextAds(cursor.getString(cursor.getColumnIndex("TextAds")));
                object.setUpdatedAt(cursor.getString(cursor.getColumnIndex("UpdatedAt")));
                object.setFromDate(cursor.getString(cursor.getColumnIndex("FromDate")));
                object.setToDate(cursor.getString(cursor.getColumnIndex("ToDate")));
                object.setDeleted(cursor.getInt(cursor.getColumnIndex("isDeleted")) == 1);
                object.setSort(cursor.getInt(cursor.getColumnIndex("sort")));
                adses.add(object);
            } while (cursor.moveToNext());
        }
        return adses;
    }

    public String getNews(String date) {
        String newsList = null;
        mDataBase = this.getWritableDatabase();


        mDataBase = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM News where FromDate <='" + date + "' and ToDate >='" + date
                + "' and isDeleted=0  ORDER BY sort ASC";
        //    Log.d("Sync: Query", selectQuery);
        Cursor cursor = mDataBase.rawQuery(selectQuery, null);
        Log.d("Sync:count news ", "" + cursor.getCount());
        if (cursor.moveToFirst()) {
            do {
                String TextAds = cursor.getString(cursor.getColumnIndex("TextAds"));
                newsList = "";
                newsList = newsList + TextAds;
                Log.d("Sync:TextAds: ", TextAds);
                Log.i("dataBase TextAds", "" + TextAds);
            } while (cursor.moveToNext());
        }
        return newsList;
    }

    private boolean addNews(SQLiteDatabase db, News object) {
        ContentValues values = new ContentValues();
        db.delete("News", "Id=" + object.getId(), null);
        values.put("Id", object.getId());
        values.put("TextAds", object.getTextAds());
        values.put("UpdatedAt", object.getUpdatedAt());
        values.put("FromDate", object.getFromDate());
        values.put("ToDate", object.getToDate());
        values.put("isDeleted", object.isDeleted() ? 1 : 0);
        values.put("sort", object.getSort());
        long rowid = db.insert("News", null, values);
        return rowid != -1;
    }

    public boolean getNewsById(int Id) {
        boolean isExist = false;
        String selectQuery = "SELECT Id FROM News WHERE Id=" + Id + "";
        Log.i("Query", selectQuery);
        Cursor cursor = mDataBase.rawQuery(selectQuery, null);
        Log.i("dataBase", "" + cursor.getCount());
        if (cursor.moveToFirst()) {
            isExist = true;
        }
        return isExist;
    }

    public void insertNews(ArrayList<News> a) {
        SQLiteDatabase db = getWritableDatabase();
        db.enableWriteAheadLogging();
        db.beginTransaction();
        int count = 0;
        int len = a.size();
        //Delete services
        for (int i = 0; i < len; i++) {
            News current = a.get(i);
            if (!current.isDeleted()) {
                Log.d("Sync:From ", current.getFromDate());
                Log.d("Sync:to ", current.getToDate());
                addNews(db, current);
                count++;
            }
        }
        Log.d("Sync service", "# of News inserted : " + count);

        db.setTransactionSuccessful();
        db.endTransaction();
        db.close();
    }

    public void delAds(int id) {
        Log.d("DBOperations", "table profile data eraser");

        mDataBase.execSQL("DELETE  FROM News where Id=" + id + "");
        Log.i("DBOperations", "DELETE  FROM News where Id=" + id + "");
    }

}