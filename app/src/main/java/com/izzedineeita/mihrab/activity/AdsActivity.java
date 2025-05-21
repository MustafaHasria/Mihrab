package com.izzedineeita.mihrab.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Build;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.izzedineeita.mihrab.Adapters.AdsAdapter;
import com.izzedineeita.mihrab.R;
import com.izzedineeita.mihrab.constants.Constants;
import com.izzedineeita.mihrab.database.DataBaseHelper;
import com.izzedineeita.mihrab.model.Ads;
import com.izzedineeita.mihrab.utils.Pref;
import com.izzedineeita.mihrab.utils.Utils;

import java.io.IOException;
import java.util.ArrayList;

public class AdsActivity extends AppCompatActivity {

    private Activity activity;
    private RecyclerView rv_ads;
    private AdsAdapter adsAdapter;
    private ArrayList<Ads> adsArrayList;
    private DataBaseHelper DBO;
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

        setContentView(R.layout.activity_ads);

        activity = this;

        DBO = new DataBaseHelper(this);
        try {
            DBO.createDataBase();
        } catch (IOException e) {
            e.printStackTrace();
        }
        sp = getSharedPreferences(Utils.PREFS, MODE_PRIVATE);
        TextView tv_tittle = findViewById(R.id.tv_tittle);
        tv_tittle.setText(getString(R.string.advertisement));
        rv_ads = findViewById(R.id.rv_ads);
        ImageView iv_addAds = findViewById(R.id.iv_addAds);
        ImageView iv_back = findViewById(R.id.iv_back);
        iv_back.setOnClickListener(view -> finish());
        try {
            iv_addAds.setOnClickListener(view -> startActivity(new Intent(activity, AddAdsActivity.class)));

        } catch (Exception e) {
            Log.e("XXX", e.getLocalizedMessage());
        }
        rv_ads.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        rv_ads.setLayoutManager(llm);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getData();
    }

    private void getData() {
        DBO.openDataBase();
        adsArrayList = DBO.getAdsList(sp.getInt("masjedId", -1));
        Log.i("+++ads", adsArrayList.size() + "  ");
        DBO.close();
        setAdapter(adsArrayList);
    }

    private void setAdapter(final ArrayList<Ads> list) {
        adsAdapter = new AdsAdapter(this, list, new AdsAdapter.OnRecycleViewItemClicked() {

            @Override
            public void onItemClicked(View view, int position) {
                deleteAds(position);
            }

            @Override
            public void onItemClick(View view, int position) {
                editAds(list.get(position));
            }

            @Override
            public void onView(View view, int position) {
                showAds(list.get(position));
            }
        });
        rv_ads.setAdapter(adsAdapter);
        adsAdapter.notifyDataSetChanged();
    }

    private void showAds(Ads ads) {
        Intent intent = new Intent(activity, ShowAdsActivity.class);
        intent.setAction("view");
        intent.putExtra("ads", ads);
        startActivity(intent);
    }

    private void editAds(Ads ads) {
        Intent intent = new Intent(activity, EditAdsActivity.class);
        intent.putExtra("ads", ads);
        startActivity(intent);
    }

    private void deleteAds(final int position) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity);
        alertDialogBuilder.setTitle(getString(R.string.tv_delTitle)).
                setMessage(getString(R.string.tv_delAttention))
                .setCancelable(false)
                .setPositiveButton(R.string.confirm_delete, (dialog, id) -> {
                    dialog.dismiss();
                    delete(position);
                })
                .setNegativeButton(R.string.cancel_delete, (dialog, id) -> dialog.cancel());
        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }

    private void delete(final int pos) {
        DBO.openDataBase();
        DBO.delAdvertisement(adsArrayList.get(pos).getId(), sp.getInt("masjedId", -1));
        adsArrayList.remove(pos);
        adsAdapter.notifyDataSetChanged();
        DBO.close();
    }
}