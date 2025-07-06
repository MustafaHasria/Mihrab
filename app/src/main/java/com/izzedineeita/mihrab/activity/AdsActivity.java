package com.izzedineeita.mihrab.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

    private static final String TAG = "AdsActivity";
    private static final String EXTRA_ADS = "ads";
    private static final String ACTION_VIEW = "view";
    private static final String PREF_MASJED_ID = "masjedId";
    private static final int DEFAULT_MASJED_ID = -1;

    // UI Components
    private RecyclerView recyclerViewAds;
    private AdsAdapter adsAdapter;
    private TextView textViewTitle;
    private ImageView imageViewAddAds;
    private ImageView imageViewBack;

    // Data
    private ArrayList<Ads> adsList;
    private DataBaseHelper databaseHelper;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupOrientation();
        setContentView(R.layout.activity_ads);

        initializeComponents();
        setupDatabase();
        setupRecyclerView();
        setupClickListeners();
        setupTitle();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadAdsData();
    }

    /**
     * Sets up the screen orientation based on the selected theme.
     */
    private void setupOrientation() {
        int selectedTheme = Pref.getValue(getApplicationContext(), Constants.PREF_THEM_POSITION_SELECTED, 0);
        boolean isLandscapeTheme = selectedTheme >= 5 && selectedTheme <= 9;

        int orientation = isLandscapeTheme ?
                ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE :
                ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;

        setRequestedOrientation(orientation);
    }

    /**
     * Initializes UI components by finding views by their IDs.
     */
    private void initializeComponents() {
        recyclerViewAds = findViewById(R.id.rv_ads);
        textViewTitle = findViewById(R.id.tv_tittle);
        imageViewAddAds = findViewById(R.id.iv_addAds);
        imageViewBack = findViewById(R.id.iv_back);

        adsList = new ArrayList<>();
        sharedPreferences = getSharedPreferences(Utils.PREFS, MODE_PRIVATE);
    }

    /**
     * Sets up the database helper and creates the database if needed.
     */
    private void setupDatabase() {
        databaseHelper = new DataBaseHelper(this);
        try {
            databaseHelper.createDataBase();
        } catch (IOException e) {
            Log.e(TAG, "Failed to create database", e);
            showDatabaseErrorDialog();
        }
    }

    /**
     * Sets up the RecyclerView with proper layout manager and adapter.
     */
    private void setupRecyclerView() {
        recyclerViewAds.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerViewAds.setLayoutManager(layoutManager);

        createAndSetAdapter();
    }

    /**
     * Creates and sets the adapter for the RecyclerView.
     */
    private void createAndSetAdapter() {
        adsAdapter = new AdsAdapter(this, adsList, new AdsAdapter.OnRecycleViewItemClicked() {
            @Override
            public void onItemClicked(View view, int position) {
                handleDeleteAds(position);
            }

            @Override
            public void onItemClick(View view, int position) {
                handleEditAds(adsList.get(position));
            }

            @Override
            public void onView(View view, int position) {
                handleViewAds(adsList.get(position));
            }
        });
        recyclerViewAds.setAdapter(adsAdapter);
    }

    /**
     * Sets up click listeners for UI components.
     */
    private void setupClickListeners() {
        imageViewBack.setOnClickListener(view -> finish());

        imageViewAddAds.setOnClickListener(view -> {
            try {
                Log.d(TAG, "Attempting to start AddAdsActivity...");
                Intent intent = new Intent(AdsActivity.this, AddAdsActivity.class);
                startActivity(intent);
                Log.d(TAG, "AddAdsActivity started successfully");
            } catch (Exception e) {
                Log.e(TAG, "Failed to start AddAdsActivity", e);
                showErrorDialog("خطأ في إضافة الإعلان: " + e.getMessage());
                
                // Try to start test activity instead
                try {
                    Log.d(TAG, "Attempting to start TestAddAdsActivity as fallback...");
                    Intent testIntent = new Intent(AdsActivity.this, TestAddAdsActivity.class);
                    startActivity(testIntent);
                    Log.d(TAG, "TestAddAdsActivity started successfully");
                } catch (Exception testException) {
                    Log.e(TAG, "Failed to start TestAddAdsActivity as well", testException);
                    showErrorDialog("Both activities failed to start");
                }
            }
        });
    }

    /**
     * Sets up the title text for the activity.
     */
    private void setupTitle() {
        textViewTitle.setText(getString(R.string.advertisement));
    }

    /**
     * Loads advertisement data from the database.
     */
    private void loadAdsData() {
        if (databaseHelper == null) {
            Log.e(TAG, "Database helper is null");
            return;
        }

        try {
            databaseHelper.openDataBase();
            int masjedId = sharedPreferences.getInt(PREF_MASJED_ID, DEFAULT_MASJED_ID);
            adsList = databaseHelper.getAdsList(masjedId);
            Log.d(TAG, "Loaded " + adsList.size() + " advertisements");
            databaseHelper.close();

            updateAdapter();
        } catch (Exception e) {
            Log.e(TAG, "Failed to load ads data", e);
            showErrorDialog("خطأ في تحميل الإعلانات");
        }
    }

    /**
     * Updates the adapter with new data.
     */
    private void updateAdapter() {
        if (adsAdapter != null) {
            adsAdapter.notifyDataSetChanged();
        }
    }

    /**
     * Handles viewing an advertisement.
     *
     * @param ads The advertisement to view
     */
    private void handleViewAds(Ads ads) {
        if (ads == null) {
            Log.w(TAG, "Attempted to view null advertisement");
            return;
        }

        try {
            Intent intent = new Intent(this, ShowAdsActivity.class);
            intent.setAction(ACTION_VIEW);
            intent.putExtra(EXTRA_ADS, ads);
            startActivity(intent);
        } catch (Exception e) {
            Log.e(TAG, "Failed to start ShowAdsActivity", e);
            showErrorDialog("خطأ في عرض الإعلان");
        }
    }

    /**
     * Handles editing an advertisement.
     *
     * @param ads The advertisement to edit
     */
    private void handleEditAds(Ads ads) {
        if (ads == null) {
            Log.w(TAG, "Attempted to edit null advertisement");
            return;
        }

        try {
            Intent intent = new Intent(this, EditAdsActivity.class);
            intent.putExtra(EXTRA_ADS, ads);
            startActivity(intent);
        } catch (Exception e) {
            Log.e(TAG, "Failed to start EditAdsActivity", e);
            showErrorDialog("خطأ في تعديل الإعلان");
        }
    }

    /**
     * Handles deleting an advertisement with confirmation dialog.
     *
     * @param position The position of the advertisement in the list
     */
    private void handleDeleteAds(int position) {
        if (position < 0 || position >= adsList.size()) {
            Log.w(TAG, "Invalid position for deletion: " + position);
            return;
        }

        showDeleteConfirmationDialog(position);
    }

    /**
     * Shows a confirmation dialog before deleting an advertisement.
     *
     * @param position The position of the advertisement to delete
     */
    private void showDeleteConfirmationDialog(int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.tv_delTitle))
                .setMessage(getString(R.string.tv_delAttention))
                .setCancelable(false)
                .setPositiveButton(R.string.confirm_delete, (dialog, id) -> {
                    dialog.dismiss();
                    performDelete(position);
                })
                .setNegativeButton(R.string.cancel_delete, (dialog, id) -> dialog.cancel());

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    /**
     * Performs the actual deletion of an advertisement.
     *
     * @param position The position of the advertisement to delete
     */
    private void performDelete(int position) {
        if (position < 0 || position >= adsList.size()) {
            Log.w(TAG, "Invalid position for deletion: " + position);
            return;
        }

        try {
            databaseHelper.openDataBase();
            Ads adsToDelete = adsList.get(position);
            int masjedId = sharedPreferences.getInt(PREF_MASJED_ID, DEFAULT_MASJED_ID);

            // Delete from database (method returns void)
            databaseHelper.delAdvertisement(adsToDelete.getId(), masjedId);

            // Remove from local list and update adapter
            adsList.remove(position);
            updateAdapter();
            Log.d(TAG, "Successfully deleted advertisement at position: " + position);

            databaseHelper.close();
        } catch (Exception e) {
            Log.e(TAG, "Error deleting advertisement", e);
            showErrorDialog("خطأ في حذف الإعلان");
        }
    }

    /**
     * Shows a generic error dialog with the given message.
     *
     * @param message The error message to display
     */
    private void showErrorDialog(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("خطأ")
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, null);
        builder.create().show();
    }

    /**
     * Shows a database error dialog.
     */
    private void showDatabaseErrorDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("خطأ")
                .setMessage("خطأ في تهيئة قاعدة البيانات")
                .setPositiveButton(android.R.string.ok, (dialog, which) -> finish());
        builder.create().show();
    }
}