
package com.izzedineeita.mihrab.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.izzedineeita.mihrab.MainActivity;
import com.izzedineeita.mihrab.R;
import com.izzedineeita.mihrab.constants.Constants;
import com.izzedineeita.mihrab.utils.Pref;
import com.izzedineeita.mihrab.utils.Utils;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText edUserName;
    private EditText edPassword;
    private Button btnLogin;
    private ProgressBar progress;
    Activity activity;
    private FirebaseAuth mAuth;
    private LinearLayout layout;
    private DatabaseReference mDatabase;


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
        setContentView(R.layout.activity_login);


        activity = this;

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();


        findViews();
    }

    private void findViews() {
        progress = (ProgressBar) findViewById(R.id.progress);
        progress.getIndeterminateDrawable()
                .setColorFilter(Color.parseColor("#B57C2F"), android.graphics.PorterDuff.Mode.MULTIPLY);
        edUserName = findViewById(R.id.edUserName);
        edPassword = findViewById(R.id.edPassword);
        btnLogin = findViewById(R.id.btnLogin);
        layout = findViewById(R.id.layout);

        btnLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == btnLogin) {
            if (TextUtils.isEmpty(edUserName.getText().toString().trim())) {
                edUserName.setError("أدخل الايميل ..");
                return;
            }
            if (TextUtils.isEmpty(edPassword.getText().toString().trim())) {
                edPassword.setError("أدخل كلمة المرور ..");
                return;
            }
            login();
        }
    }

    private void login() {
        progress.setVisibility(View.VISIBLE);
        layout.setVisibility(View.GONE);
        mAuth.signInWithEmailAndPassword(edUserName.getText().toString().trim(),
                edPassword.getText().toString().trim())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("XXX", "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();

                            progress.setVisibility(View.GONE);
                           // String isLogin = "0";

                            assert user != null;
                            DatabaseReference userNameRef = mDatabase.child("Users").child(Objects.requireNonNull(user.getUid()));
                            ValueEventListener eventListener = new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if(dataSnapshot.exists()) {
                                        //create new user
                                        String s =  Objects.requireNonNull(dataSnapshot.getValue()).toString();
                                      Log.e("ASAS", s);
                                       if (s.equals("1")) {
                                           layout.setVisibility(View.VISIBLE);
                                           Pref.setValue(LoginActivity.this,Constants.PREF_IS_USER_LOGIN, false);

                                           Toast.makeText(LoginActivity.this, "هذا الحساب تم تسجيل الدخول مسبقاً لا يمكن تسجيل الدخول من جديد!",
                                                   Toast.LENGTH_SHORT).show();
                                       } else {
                                           mDatabase.child("Users").child(Objects.requireNonNull(user.getUid()))
                                                   .setValue("1").addOnCompleteListener(new OnCompleteListener() {
                                                       @Override
                                                       public void onComplete(@NonNull Task task) {
                                                           Pref.setValue(LoginActivity.this,Constants.PREF_IS_USER_LOGIN, true);
                                                           Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                                           startActivity(intent);
                                                           finish();
                                                       }
                                                   });
                                       }
                                    } else {
                                        mDatabase.child("Users").child(Objects.requireNonNull(user.getUid()))
                                                .setValue("1").addOnCompleteListener(new OnCompleteListener() {
                                                    @Override
                                                    public void onComplete(@NonNull Task task) {
                                                        Pref.setValue(LoginActivity.this,Constants.PREF_IS_USER_LOGIN, true);
                                                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                                        startActivity(intent);
                                                        finish();
                                                    }
                                                });                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                    Log.d("XXXZZ", databaseError.getMessage()); //Don't ignore errors!
                                }
                            };
                            userNameRef.addListenerForSingleValueEvent(eventListener);

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("XXX", "signInWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            progress.setVisibility(View.GONE);
                            layout.setVisibility(View.VISIBLE);
                        }
                    }
                });

    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }
}