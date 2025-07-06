package com.izzedineeita.mihrab.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.izzedineeita.mihrab.Adapters.NewsAdapter;
import com.izzedineeita.mihrab.DateTimePicker.CustomDateTimePicker1;
import com.izzedineeita.mihrab.R;
import com.izzedineeita.mihrab.constants.Constants;
import com.izzedineeita.mihrab.database.DataBaseHelper;
import com.izzedineeita.mihrab.model.News;
import com.izzedineeita.mihrab.utils.Pref;
import com.izzedineeita.mihrab.utils.Utils;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class NewsActivity extends AppCompatActivity {

    Activity activity;
    private RecyclerView rv_ads;
    NewsAdapter newsAdapter;
    ArrayList<News> adsArrayList;
    private DataBaseHelper DBO;
    private ImageView iv_back;
    private int id;
    private SharedPreferences sp;
    private SharedPreferences.Editor spedit;
    private EditText ed_end, ed_start, ed_sort, ed_newsText;
    private Dialog dialog;
    private ProgressDialog pd;


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

        activity = this;
        setContentView(R.layout.activity_news);
        DBO = new DataBaseHelper(this);
        try {
            DBO.createDataBase();
        } catch (IOException e) {
            e.printStackTrace();
        }
        sp = getSharedPreferences(Utils.PREFS, MODE_PRIVATE);
        spedit = sp.edit();


        TextView tv_tittle = (TextView) findViewById(R.id.tv_tittle);
        tv_tittle.setText(getString(R.string.adv));
        ImageView iv_addAds = (ImageView) findViewById(R.id.iv_addAds);
        iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        iv_addAds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addNewsDialog(null);
            }
        });

        rv_ads = (RecyclerView) findViewById(R.id.rv_ads);
        rv_ads.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);

        DBO.openDataBase();
        adsArrayList = DBO.getNews();
        DBO.close();
        rv_ads.setLayoutManager(llm);
        setAdapter(adsArrayList);

    }

    private void setAdapter(final ArrayList<News> list) {
        newsAdapter = new NewsAdapter(this, list, new NewsAdapter.OnRecycleViewItemClicked() {
            @Override
            public void onItemClicked(View view, int position) {
                deleteAds(position);
            }

            @Override
            public void onItemClick(View view, int position) {
                addNewsDialog(list.get(position));
            }
        });
        rv_ads.setAdapter(newsAdapter);
        newsAdapter.notifyDataSetChanged();
    }

//    private void addUpdateNews(final int action, final News object) {
//        if (Utils.isOnline(activity)) {
//            pd = new ProgressDialog(activity);
//            pd.setMessage(getString(R.string.wait));
//            pd.show();
//            pd.setCanceledOnTouchOutside(false);
//            WS.addUpdateNews(activity, object, new OnLoadedFinished() {
//                @Override
//                public void onSuccess(String response) {
//                    getNews(action);
//                }
//
//                @Override
//                public void onFail(String error) {
//                    if (pd.isShowing()) pd.dismiss();
//                    Utils.showCustomToast(activity, error);
//                }
//            });
//        } else {
//            Utils.showCustomToast(activity, getString(R.string.no_internet));
//
//        }
//
//    }

    private void getNews(final int action) {

        if (action == 0) Utils.showCustomToast(activity, getString(R.string.success_add));
        else Utils.showCustomToast(activity, getString(R.string.success_edit));
        updateAdapter();
        if (dialog.isShowing()) dialog.dismiss();
        if (pd.isShowing()) pd.dismiss();


        if (pd.isShowing()) pd.dismiss();


    }

    private void updateAdapter() {
        DBO.openDataBase();
        adsArrayList = DBO.getNews();
        DBO.close();
        setAdapter(adsArrayList);
    }

    private void deleteAds(final int position) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity);
        alertDialogBuilder.setTitle(getString(R.string.tv_delTitle)).setMessage(getString(R.string.tv_delAttention)).setCancelable(false).setPositiveButton(R.string.confirm_delete, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
                delete(position);

            }
        }).setNegativeButton(R.string.cancel_delete, new DialogInterface.OnClickListener() {
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
        DBO.deleteAdById(adsArrayList.get(pos).getId());
        adsArrayList.remove(pos);
        newsAdapter.notifyDataSetChanged();
        DBO.close();

    }

    private void addNewsDialog(final News objectNews) {
        final SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss", Locale.US);
        final SimpleDateFormat spf = new SimpleDateFormat("yyyy/MM/dd HH:mm", Locale.US);

        @SuppressLint("InflateParams") View view = getLayoutInflater().inflate(R.layout.add_edit_ads, null);
        ed_newsText = (EditText) view.findViewById(R.id.ed_newsText);
        ed_end = (EditText) view.findViewById(R.id.ed_end);
        ed_start = (EditText) view.findViewById(R.id.ed_start);
        ed_sort = (EditText) view.findViewById(R.id.ed_sort);
        Button btn_add = (Button) view.findViewById(R.id.btn_add);
        Button btn_cancel = (Button) view.findViewById(R.id.btn_cancel);
        dialog = new Dialog(this);

        dialog.setTitle(getString(R.string.add_adv));
        if (objectNews == null) {
            btn_add.setText(getString(R.string.add));
        } else {
            btn_add.setText(getString(R.string.edit));
            ed_sort.setText(String.valueOf(objectNews.getSort()));
            String endDate = objectNews.getToDate();
            String startDate = objectNews.getFromDate();
            String start = startDate;
            String end = endDate;

            try {
                Date endD = sdf.parse(endDate);
                Date startD = sdf.parse(startDate);
                end = spf.format(endD.getTime());
                start = spf.format(startD.getTime());
            } catch (ParseException e) {
                e.printStackTrace();
            }

            ed_end.setText(end);
            ed_start.setText(start);
            ed_newsText.setText(objectNews.getTextAds());
        }

        ed_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.hideSoftKeyboard(activity);
                showDateTimePicker(ed_start);
            }
        });
        ed_end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.hideSoftKeyboard(activity);
                showDateTimePicker(ed_end);
            }
        });
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.hideKeyboard(activity);
                ed_newsText.setError(null);
                // ed_sort.setError(null);
                ed_start.setError(null);
                ed_end.setError(null);
                if (TextUtils.isEmpty(ed_newsText.getText().toString())) {
                    ed_newsText.setError(getString(R.string.newstext));
                } else if (TextUtils.isEmpty(ed_start.getText().toString())) {
                    ed_start.setError(getString(R.string.start));
                } else if (TextUtils.isEmpty(ed_end.getText().toString())) {
                    ed_end.setError(getString(R.string.end));
                } else if (!Utils.compareDates(ed_start.getText().toString(), ed_end.getText().toString())) {
                    Utils.showCustomToast(activity, getString(R.string.error_date));
                    ed_end.setError(getString(R.string.error_date));
                }
//                else if (TextUtils.isEmpty(ed_sort.getText().toString())) {
//                    ed_sort.setError(getString(R.string.sort));
//                }
                else {
                    Utils.hideSoftKeyboard(activity);
                    News object = new News();
                    int id = Utils.random9();
                    DBO.openDataBase();
                    if (sp.getInt("priority", 0) == 1) {
                        id = 0;
                    } else {
                        if (DBO.getNewsById(id)) {
                            id = Utils.random9();
                        }
                    }
                    DBO.close();
                    object.setId((objectNews == null) ? id : objectNews.getId());
                    object.setTextAds(ed_newsText.getText().toString().trim());
                    object.setFromDate(ed_start.getText().toString().trim());
                    object.setToDate(ed_end.getText().toString().trim());
                    //object.setSort(Integer.parseInt(ed_sort.getText().toString()));
                    object.setDeleted(false);
                    object.setUpdatedAt(Utils.getFormattedCurrentDate());
                    if (sp.getInt("priority", 0) == 1) {
                        //addUpdateNews((objectNews == null) ? 0 : 1, object);
                    } else {
                        try {
                            Date endD = spf.parse(ed_end.getText().toString().trim());
                            Date startD = spf.parse(ed_start.getText().toString().trim());
                            object.setFromDate(sdf.format(startD.getTime()));
                            object.setToDate(sdf.format(endD.getTime()));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        pd = new ProgressDialog(activity);
                        pd.setMessage(getString(R.string.wait));
                        pd.show();
                        pd.setCanceledOnTouchOutside(false);
                        DataBaseHelper db = new DataBaseHelper(activity);
                        ArrayList<News> newsList = new ArrayList<>();
                        newsList.add(object);
                        db.insertNews(newsList);
                        if ((objectNews == null)) Utils.showCustomToast(activity, getString(R.string.success_add));
                        else Utils.showCustomToast(activity, getString(R.string.success_edit));

                        updateAdapter();
                        if (dialog.isShowing()) dialog.dismiss();
                        if (pd.isShowing()) pd.dismiss();

                    }
                }
            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.hideKeyboard(activity);
                dialog.dismiss();
            }
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
        CustomDateTimePicker1 custom = new CustomDateTimePicker1(this, new CustomDateTimePicker1.ICustomDateTimeListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onSet(Dialog dialog, Calendar calendarSelected, Date dateSelected, int year, String monthFullName, String monthShortName, int monthNumber, int date, String weekDayFullName, String weekDayShortName, int hour24, int hour12, int min, int sec, String AM_PM) {
                editText.setText(year + "/" + (monthNumber + 1) + "/" + calendarSelected.get(Calendar.DAY_OF_MONTH) + " " + hour24 + ":" + min);
            }

            @Override
            public void onCancel() {

            }
        });

        custom.set24HourFormat(false);
        custom.setDate(Calendar.getInstance());
        custom.showDialog();
    }
}
