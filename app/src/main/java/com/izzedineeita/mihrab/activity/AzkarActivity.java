package com.izzedineeita.mihrab.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;

import com.izzedineeita.mihrab.Adapters.AzkarAdapter;
import com.izzedineeita.mihrab.R;
import com.izzedineeita.mihrab.constants.Constants;
import com.izzedineeita.mihrab.database.DataBaseHelper;
import com.izzedineeita.mihrab.model.AzkarModel;
import com.izzedineeita.mihrab.utils.Pref;
import com.izzedineeita.mihrab.utils.Utils;

import java.util.ArrayList;

public class AzkarActivity extends AppCompatActivity {

    private Activity activity;
    private RecyclerView recyclerView;
    private EditText ed_count, ed_sort, ed_newsText;
    private Dialog dialog;
    private ProgressDialog pd;
    private CheckBox cb_isha, cb_magrib, cb_asr, cb_duhr, cb_fajer;
    private DataBaseHelper DBO;
    private AzkarAdapter adapter;
    private ArrayList<AzkarModel> azkarModels;
    private SharedPreferences sp;


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
        setContentView(R.layout.activity_azkar);

      try {
          activity = this;

          sp = getSharedPreferences(Utils.PREFS, MODE_PRIVATE);
          SharedPreferences.Editor spedit = sp.edit();

          DBO = new DataBaseHelper(this);
          DBO.openDataBase();

          ImageView iv_addAds = findViewById(R.id.iv_addAds);
          ImageView iv_back = findViewById(R.id.iv_back);

          iv_back.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View view) {
                  finish();
              }
          });
          iv_addAds.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View view) {
                  addAzkarDialog(null);
              }
          });

          recyclerView = (RecyclerView) findViewById(R.id.rv_ads);
          recyclerView.setHasFixedSize(true);
          LinearLayoutManager llm = new LinearLayoutManager(this);
          llm.setOrientation(LinearLayoutManager.VERTICAL);

          DBO.openDataBase();
          azkarModels = DBO.getAzkar("azkar");
          DBO.close();
          recyclerView.setLayoutManager(llm);
          setAdapter(azkarModels);
      } catch (Exception e) {
          Log.e("XXX", e.getLocalizedMessage());
      }
    }

    private void addAzkarDialog(final AzkarModel objectAzkar) {
        @SuppressLint("InflateParams") View view = getLayoutInflater().inflate(R.layout.add_edit_azkar, null);
        ed_newsText = (EditText) view.findViewById(R.id.ed_newsText);
        ed_count = (EditText) view.findViewById(R.id.ed_count);
        ed_sort = (EditText) view.findViewById(R.id.ed_sort);
        Button btn_add = (Button) view.findViewById(R.id.btn_add);
        cb_fajer = (CheckBox) view.findViewById(R.id.cb_fajer);
        cb_duhr = (CheckBox) view.findViewById(R.id.cb_duhr);
        cb_asr = (CheckBox) view.findViewById(R.id.cb_asr);
        cb_magrib = (CheckBox) view.findViewById(R.id.cb_magrib);
        cb_isha = (CheckBox) view.findViewById(R.id.cb_isha);
        Button btn_cancel = (Button) view.findViewById(R.id.btn_cancel);
        dialog = new Dialog(this);

        dialog.setTitle(getString(R.string.add_azkar));
        if (objectAzkar == null) {
            btn_add.setText(getString(R.string.add));
        } else {
            btn_add.setText(getString(R.string.edit_azkar));
            ed_sort.setText(String.valueOf(objectAzkar.getSort()));
            ed_count.setText(String.valueOf(objectAzkar.getCount()));
            cb_fajer.setChecked(objectAzkar.isFajr());
            cb_duhr.setChecked(objectAzkar.isDhuhr());
            cb_asr.setChecked(objectAzkar.isAsr());
            cb_magrib.setChecked(objectAzkar.isMagrib());
            cb_isha.setChecked(objectAzkar.isha());
            ed_newsText.setText(objectAzkar.getTextAzakar());
        }

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ed_newsText.setError(null);
                ed_sort.setError(null);
                ed_count.setError(null);
                if (TextUtils.isEmpty(ed_newsText.getText().toString())) {
                    ed_newsText.setError("getString(R.string.newstext)");
                } else if (TextUtils.isEmpty(ed_sort.getText().toString())) {
                    ed_sort.setError("getString(R.string.sort)");
                } else if (TextUtils.isEmpty(ed_count.getText().toString())) {
                    ed_count.setError("getString(R.string.count)");
                } else {
                    Utils.hideSoftKeyboard(activity);
                    AzkarModel object = new AzkarModel();
                    int id = Utils.random9();
                    DBO.openDataBase();
                    if (sp.getInt("priority", 0) == 1) {
                        id = 0;
                    } else {
                        if (DBO.getAzkarById(id)) {
                            id = Utils.random9();
                        }
                    }
                    DBO.close();
                    object.setId((objectAzkar == null) ? id : objectAzkar.getId());
                    object.setTextAzakar(ed_newsText.getText().toString().trim());
                    object.setSort(Integer.parseInt(ed_sort.getText().toString()));
                    object.setCount(Integer.parseInt(ed_count.getText().toString()));
                    object.setFajr(cb_fajer.isChecked());
                    object.setDhuhr(cb_duhr.isChecked());
                    object.setAsr(cb_asr.isChecked());
                    object.setMagrib(cb_magrib.isChecked());
                    object.setIsha(cb_isha.isChecked());
                    object.setDeleted(false);

                    pd = new ProgressDialog(activity);
                    pd.setMessage(getString(R.string.wait));
                    pd.show();
                    pd.setCanceledOnTouchOutside(false);
                    DataBaseHelper db = new DataBaseHelper(activity);
                    ArrayList<AzkarModel> azkarList = new ArrayList<>();
                    azkarList.add(object);
                    db.insertAzkar(azkarList);
                    if ((objectAzkar == null))
                        Utils.showCustomToast(activity, getString(R.string.success_add));
                    else Utils.showCustomToast(activity, getString(R.string.success_edit));

                    updateAdapter();
                    if (dialog.isShowing()) dialog.dismiss();
                    if (pd.isShowing()) pd.dismiss();


                }
            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.setContentView(view);
        dialog.show();

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = dialog.getWindow();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        if (window != null) {
            lp.copyFrom(window.getAttributes());
            window.setAttributes(lp);
        }
    }

    private void setAdapter(final ArrayList<AzkarModel> list) {
        adapter = new AzkarAdapter(this, list, new AzkarAdapter.OnRecycleViewItemClicked() {
            @Override
            public void onItemClicked(View view, int position) {
                deleteAzkar(position);
            }

            @Override
            public void onItemClick(View view, int position) {
                addAzkarDialog(list.get(position));
            }
        });
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private void deleteAzkar(final int position) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity);
        alertDialogBuilder.setTitle(getString(R.string.confirm_delete)).
                setMessage(getString(R.string.tv_delAttention1))
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
        DBO.delAzkar(azkarModels.get(pos).getId());
        azkarModels.remove(pos);
        adapter.notifyDataSetChanged();
        DBO.close();

    }

    private void updateAdapter() {
        DBO.openDataBase();
        azkarModels = DBO.getAzkar("azkar");
        DBO.close();
        setAdapter(azkarModels);
    }
}