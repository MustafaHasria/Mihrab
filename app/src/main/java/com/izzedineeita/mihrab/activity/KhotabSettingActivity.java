package com.izzedineeita.mihrab.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.VideoView;

import com.izzedineeita.mihrab.Adapters.KhotabAdapter;
import com.izzedineeita.mihrab.DateTimePicker.CustomDateTimePicker;
import com.izzedineeita.mihrab.R;
import com.izzedineeita.mihrab.constants.Constants;
import com.izzedineeita.mihrab.database.DataBaseHelper;
import com.izzedineeita.mihrab.model.Ads;
import com.izzedineeita.mihrab.model.Khotab;
import com.izzedineeita.mihrab.utils.Pref;
import com.izzedineeita.mihrab.utils.Utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class KhotabSettingActivity extends AppCompatActivity {

    private Activity activity;
    private RecyclerView rv_khotab;
    KhotabAdapter khotabAdapter;
    ArrayList<Khotab> khotabArrayList;
    private DataBaseHelper DBO;
    private ImageView iv_back;
    private SharedPreferences sp;
    private SharedPreferences.Editor spedit;
    private EditText et_date, et_title, et_body, et_video_url;
    private EditText et_title1, et_body1, et_title2, et_body2, et_title3, et_body3, et_text_show_time, et_show_time;
    private Dialog dialog;
    private ProgressDialog pd;
    private CheckBox cb_chose_video;
    private int PICKFILE_RESULT_CODE_1 = 21511;
    private int PICKFILE_RESULT_CODE_2 = 21512;
    private int PICKFILE_RESULT_CODE_3 = 21513;


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
        setContentView(R.layout.activity_khotab_setting);


        activity = this;

        DBO = new DataBaseHelper(this);
        try {
            DBO.createDataBase();
        } catch (IOException e) {
            e.printStackTrace();
        }
        sp = getSharedPreferences(Utils.PREFS, MODE_PRIVATE);
        spedit = sp.edit();

        ImageView ivAddKhotba = findViewById(R.id.iv_add_khotba);
        iv_back = findViewById(R.id.iv_back);
        iv_back.setOnClickListener(view -> finish());

        ivAddKhotba.setOnClickListener(view -> addKhotbaDialog(null));

        rv_khotab = findViewById(R.id.rv_khotab);
        rv_khotab.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);

        DBO.openDataBase();
        khotabArrayList = DBO.getAllKhotab();
//        Log.d("KHOTAB", khotabArrayList.size() + "");
//        Log.d("KHOTAB", khotabArrayList.get(0).getTitle()+ "");
//        Log.d("KHOTAB", khotabArrayList.get(0).getTitle1()+ "");
//        Log.d("KHOTAB", khotabArrayList.get(0).getTitle2()+ "");
//        Log.d("KHOTAB", khotabArrayList.get(0).getTitle3()+ "");
//        Log.d("KHOTAB", khotabArrayList.get(0).getBody1()+ "");
//        Log.d("KHOTAB", khotabArrayList.get(0).getBody2()+ "");
//        Log.d("KHOTAB", khotabArrayList.get(0).getBody3()+ "");
        DBO.close();
        rv_khotab.setLayoutManager(llm);
        try {
            setAdapter(khotabArrayList);
        } catch (Exception e ) {
            Log.e("XXX", "" + e.getLocalizedMessage());
        }



    }

    private void setAdapter(final ArrayList<Khotab> list) {
        khotabAdapter = new KhotabAdapter(this, list, new KhotabAdapter.OnRecycleViewItemClicked() {
            @Override
            public void onItemClicked(View view, int position) {
                deleteKhotab(position);
            }

            @Override
            public void onItemClick(View view, int position) {

                  addKhotbaDialog(list.get(position));

            }
            @Override
            public void onView(View view, int position) {
                showKhotab(list.get(position));
            }
        });
        rv_khotab.setAdapter(khotabAdapter);
        khotabAdapter.notifyDataSetChanged();
    }

    private void showKhotab(Khotab khotab) {
        Intent cp = new Intent(getApplicationContext(), ShowKhotabActivity.class);
        cp.putExtra("khotba", khotab);
        startActivity(cp);
    }

    private void updateAdapter() {
        DBO.openDataBase();
        khotabArrayList = DBO.getAllKhotab();
        DBO.close();
        setAdapter(khotabArrayList);
    }

    private void deleteKhotab(final int position) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity);
        alertDialogBuilder.setTitle(getString(R.string.tv_delTitle)).
                setMessage(getString(R.string.tv_delAttention))
                .setCancelable(false)
                .setPositiveButton(R.string.confirm_delete, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                        delete(position);

                    }
                })
                .setNegativeButton(R.string.cancel_delete, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }

    private void delete(final int pos) {
        DBO.openDataBase();
        DBO.deleteKhotba(khotabArrayList.get(pos).getId());
        khotabArrayList.remove(pos);
        khotabAdapter.notifyDataSetChanged();
        DBO.close();
    }

    private void addKhotbaDialog(final Khotab objectKhotab) {
        final SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm", Locale.US);
        final SimpleDateFormat spf = new SimpleDateFormat("yyyy/MM/dd HH:mm", Locale.US);

        @SuppressLint("InflateParams") View view = getLayoutInflater().inflate(R.layout.add_edit_khotab, null);
        et_date = view.findViewById(R.id.et_date);
        et_title = view.findViewById(R.id.et_title);
        et_body = view.findViewById(R.id.et_body);
        et_video_url = view.findViewById(R.id.et_video_url);
        cb_chose_video = view.findViewById(R.id.cb_chose_video);

        cb_chose_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (((CompoundButton) view).isChecked()) {
                    System.out.println("Checked");
                    try {
                        Intent intent = new Intent();
                        intent.setType("video/*");
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(Intent.createChooser(intent, "Select Video"), 21212);

                    } catch (Exception e) {
                        Log.e("XXX", e.getLocalizedMessage());
                    }
                } else {
                    System.out.println("Un-Checked");
                    et_video_url.setText("");
                    et_video_url.setEnabled(true);
                }
            }
        });

        et_title1 = view.findViewById(R.id.et_title1);
        et_body1 = view.findViewById(R.id.et_body1);
        et_text_show_time = view.findViewById(R.id.et_text_show_time);
        et_show_time = view.findViewById(R.id.et_show_time);

        et_title2 = view.findViewById(R.id.et_title2);
        et_body2 = view.findViewById(R.id.et_body2);

        et_title3 = view.findViewById(R.id.et_title3);
        et_body3 = view.findViewById(R.id.et_body3);

        Button btn_add = view.findViewById(R.id.btn_add);
        Button btn_cancel = view.findViewById(R.id.btn_cancel);
        dialog = new Dialog(this);

        et_body1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent chooseFile = new Intent(Intent.ACTION_GET_CONTENT);
                chooseFile.setType("*/*");
                chooseFile = Intent.createChooser(chooseFile, "Choose a file");
                startActivityForResult(chooseFile, PICKFILE_RESULT_CODE_1);
            }
        });
        et_body2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent chooseFile = new Intent(Intent.ACTION_GET_CONTENT);
                chooseFile.setType("*/*");
                chooseFile = Intent.createChooser(chooseFile, "Choose a file");
                startActivityForResult(chooseFile, PICKFILE_RESULT_CODE_2);
            }
        });

        et_body3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent chooseFile = new Intent(Intent.ACTION_GET_CONTENT);
                chooseFile.setType("*/*");
                chooseFile = Intent.createChooser(chooseFile, "Choose a file");
                startActivityForResult(chooseFile, PICKFILE_RESULT_CODE_3);
            }
        });

        dialog.setTitle(getString(R.string.settings_add_khotba));
        if (objectKhotab == null) {
            btn_add.setText(getString(R.string.add));
        } else {
            btn_add.setText(getString(R.string.edit));

            String unformattedDate = objectKhotab.getDateKhotab();
//            String formattedDate = "";
//            try {
//                Date parsedDate = sdf.parse(unformattedDate);
//                formattedDate = spf.format(parsedDate.getTime());
//            } catch (ParseException e) {
//                Log.e("XXX", e.getLocalizedMessage());
//            }

            et_date.setText(unformattedDate);
            et_title.setText(objectKhotab.getTitle());
            et_body.setText(objectKhotab.getBody());
            et_video_url.setText(objectKhotab.getUrlVideoDeaf());
            et_title1.setText(objectKhotab.getTitle1());
            et_body1.setText(objectKhotab.getBody1());
            et_title2.setText(objectKhotab.getTitle2());
            et_body2.setText(objectKhotab.getBody2());
            et_title3.setText(objectKhotab.getTitle3());
            et_body3.setText(objectKhotab.getBody3());
            et_text_show_time.setText(""+objectKhotab.getTextShowTime());
            et_show_time.setText(""+objectKhotab.getShowTime());
        }

        et_date.setOnClickListener(view1 -> {
            Utils.hideSoftKeyboard(activity);
            showDateTimePicker(et_date);
        });
        btn_add.setOnClickListener(view12 -> {
            Utils.hideKeyboard(activity);

            if (TextUtils.isEmpty(et_title.getText().toString())) {
                et_title.setError(getString(R.string.required));
                Utils.showCustomToast(activity, getString(R.string.required));
            }
//            else if (TextUtils.isEmpty(et_body.getText().toString())) {
//                et_body.setError(getString(R.string.required));
//                Utils.showCustomToast(activity, getString(R.string.required));
//            }
            else if (TextUtils.isEmpty(et_date.getText().toString())) {
                et_date.setError(getString(R.string.required));
                Utils.showCustomToast(activity, getString(R.string.required));
            }else if (TextUtils.isEmpty(et_text_show_time.getText().toString())) {
                et_text_show_time.setError(getString(R.string.required));
                Utils.showCustomToast(activity, getString(R.string.required));
            }else if (TextUtils.isEmpty(et_show_time.getText().toString())) {
                et_show_time.setError(getString(R.string.required));
                Utils.showCustomToast(activity, getString(R.string.required));
            } else {
                Utils.hideSoftKeyboard(activity);
                Khotab object = new Khotab();
                int id = Utils.random9();

                object.setId((objectKhotab == null) ? id : objectKhotab.getId());
                object.setDateKhotab(et_date.getText().toString().trim());
                object.setTitle(et_title.getText().toString().trim());
                object.setBody(et_body.getText().toString().trim());

                String url = et_video_url.getText().toString().trim();
                if (!url.isEmpty())
                    object.setUrlVideoDeaf(url);

                String title1 = et_title1.getText().toString().trim();
                if (!title1.isEmpty())
                    object.setTitle1(title1);


                String body1 = et_body1.getText().toString().trim();
                if (!body1.isEmpty())
                    object.setBody1(body1);


                String title2 = et_title2.getText().toString().trim();
                if (!title2.isEmpty())
                    object.setTitle2(title2);


                String body2 = et_body2.getText().toString().trim();
                if (!body2.isEmpty())
                    object.setBody2(body2);

                String title3 = et_title3.getText().toString().trim();
                if (!title3.isEmpty())
                    object.setTitle3(title3);


                String body3 = et_body3.getText().toString().trim();
                if (!body3.isEmpty())
                    object.setBody3(body3);

                String et_text_show_time1 = et_text_show_time.getText().toString().trim();
                if (!et_text_show_time1.isEmpty())
                    object.setTextShowTime(Integer.parseInt(et_text_show_time1));

                String et_show_time1 = et_show_time.getText().toString().trim();
                if (!et_text_show_time1.isEmpty())
                    object.setShowTime(Integer.parseInt(et_show_time1));

                object.setIsDeleted(0);
                object.setUpdatedAt(Utils.getFormattedCurrentDate());

                pd = new ProgressDialog(activity);
                pd.setMessage(getString(R.string.wait));
                pd.show();
                pd.setCanceledOnTouchOutside(false);
                DataBaseHelper db = new DataBaseHelper(activity);
               // ArrayList<Khotab> khotabList = new ArrayList<>();
                khotabArrayList.add(object);
               // db.insertAllKhotab(khotabList);
                db.addKhotab(object);
                if ((objectKhotab == null))
                    Utils.showCustomToast(activity, getString(R.string.success_add));
                else Utils.showCustomToast(activity, getString(R.string.success_edit));

                updateAdapter();

                if (dialog.isShowing()) dialog.dismiss();
                if (pd.isShowing()) pd.dismiss();

            }
        });
        btn_cancel.setOnClickListener(view13 -> {
            Utils.hideKeyboard(activity);
            dialog.dismiss();
        });
        dialog.setContentView(view);
        dialog.show();

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = dialog.getWindow();
        lp.copyFrom((window).getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
    }

    public void showDateTimePicker(final EditText editText) {
        CustomDateTimePicker custom = new CustomDateTimePicker(this, new CustomDateTimePicker.ICustomDateTimeListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onSet(Dialog dialog, Calendar calendarSelected,
                              Date dateSelected, int year, String monthFullName,
                              String monthShortName, int monthNumber, int date,
                              String weekDayFullName, String weekDayShortName,
                              int hour24, int hour12, int min, int sec,
                              String AM_PM) {
                editText.setText(year + "/" + (monthNumber + 1) + "/" + calendarSelected
                        .get(Calendar.DAY_OF_MONTH));
            }

            @Override
            public void onCancel() {

            }
        });

        custom.set24HourFormat(false);
        custom.setDate(Calendar.getInstance());
        custom.showDialog();
    }

    @SuppressLint({"NewApi", "MissingSuperCall"})
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == 21212 && data != null) {
                Uri selectedImageUri = data.getData();

                // OI FILE Manager
                String filemanagerstring = selectedImageUri.getPath();

                // MEDIA GALLERY
                String pickedVideoUrl = getPath(data.getData());

                if (pickedVideoUrl != null) {
                    Log.e("XXX1", pickedVideoUrl);
                    Log.e("XXX11", filemanagerstring);
                    et_video_url.setText(pickedVideoUrl);
                    et_video_url.setEnabled(false);
                } else {
                    Log.e("XXX", "selectedImagePath null");
                    Log.e("XXX", "filemanagerstring null");
                    cb_chose_video.setChecked(false);
                    et_video_url.setEnabled(true);
                    et_video_url.setText("");
                }
            } else if (requestCode == PICKFILE_RESULT_CODE_1 && data != null) {
                Uri content_describer = data.getData();
                //get the path
                Log.d("Path???", content_describer.getPath());
                BufferedReader reader = null;
                try {
                    // open the user-picked file for reading:
                    InputStream in = getContentResolver().openInputStream(content_describer);
                    // now read the content:
                    reader = new BufferedReader(new InputStreamReader(in));
                    String line;
                    StringBuilder builder = new StringBuilder();

                    while ((line = reader.readLine()) != null){
                        if (!line.isEmpty())
                            builder.append(line + " #"+ "\n");
                        Log.e("XXX3", line);
                    }
                    // Do something with the content in
                    et_body1.setText("");
                    et_body1.setText(builder.toString());
                    Log.e("XXX4", builder.toString());

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    Log.e("XXX1", e.getLocalizedMessage());
                } catch (IOException e) {
                    Log.e("XXX2", e.getLocalizedMessage());
                } finally {
                    if (reader != null) {
                        try {
                            reader.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

            } else if (requestCode == PICKFILE_RESULT_CODE_2 && data != null ) {
                Uri content_describer = data.getData();
                //get the path
                Log.d("Path???", content_describer.getPath());
                BufferedReader reader = null;
                try {
                    // open the user-picked file for reading:
                    InputStream in = getContentResolver().openInputStream(content_describer);
                    // now read the content:
                    reader = new BufferedReader(new InputStreamReader(in));
                    String line;
                    StringBuilder builder = new StringBuilder();

                    while ((line = reader.readLine()) != null){
                        if (!line.isEmpty())
                            builder.append(line + " #" + "\n");
                        Log.e("XXX3", line);
                    }
                    // Do something with the content in
                    et_body2.setText("");
                    et_body2.setText(builder.toString());
                    Log.e("XXX4", builder.toString());

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    Log.e("XXX1", e.getLocalizedMessage());
                } catch (IOException e) {
                    Log.e("XXX2", e.getLocalizedMessage());
                } finally {
                    if (reader != null) {
                        try {
                            reader.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

            }
        }else if (requestCode == PICKFILE_RESULT_CODE_3 && data != null) {
            Uri content_describer = data.getData();
            //get the path
            Log.d("Path???", content_describer.getPath());
            BufferedReader reader = null;
            try {
                // open the user-picked file for reading:
                InputStream in = getContentResolver().openInputStream(content_describer);
                // now read the content:
                reader = new BufferedReader(new InputStreamReader(in));
                String line;
                StringBuilder builder = new StringBuilder();

                while ((line = reader.readLine()) != null){
                    if (!line.isEmpty())
                        builder.append(line + " #"+ "\n");
                    Log.e("XXX3", line);
                }
                // Do something with the content in
                et_body3.setText("");
                et_body3.setText(builder.toString());
                Log.e("XXX4", builder.toString());

            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Log.e("XXX1", e.getLocalizedMessage());
            } catch (IOException e) {
                Log.e("XXX2", e.getLocalizedMessage());
            } finally {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

        } else {
            cb_chose_video.setChecked(false);
            et_video_url.setEnabled(true);
            et_video_url.setText("");
            Log.e("XXX", "selectedImagePath12");
        }
    }

    public String getPath(Uri uri) {
        String[] projection = {MediaStore.Video.Media.DISPLAY_NAME};
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            // HERE YOU WILL GET A NULLPOINTER IF CURSOR IS NULL
            // THIS CAN BE, IF YOU USED OI FILE MANAGER FOR PICKING THE MEDIA
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } else
            return null;
    }


}