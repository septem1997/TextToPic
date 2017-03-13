package com.lambda.texttopic;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity  {

    private EditText mPasswordView;

    private SharedPreferences mSharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setupActionBar();
        // Set up the login form.
        //populateAutoComplete();

        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        mSharedPreferences = getSharedPreferences("TestSharedPreferences",0);
        boolean b = mSharedPreferences.getBoolean("isPassed",false);

        if (b)
        {
            this.startActivity(new Intent(LoginActivity.this,MainActivity.class));
            finish();
        }
    }




    /**
     * Set up the {@link android.app.ActionBar}, if the API is available.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void setupActionBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            // Show the Up button in the action bar.
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {

        mPasswordView.setError(null);

        String password = mPasswordView.getText().toString();

        if (Objects.equals(password, "我婷最萌")) {
            SharedPreferences.Editor mEditor = mSharedPreferences.edit();
            mEditor.putBoolean("isPassed",true);
            mEditor.apply();
            this.startActivity(new Intent(LoginActivity.this,MainActivity.class));
            finish();
            return;
        }

        mPasswordView.setError(getString(R.string.error_invalid_password));


        if (Objects.equals(password, "我喜欢你")) {
            mPasswordView.setError("我也喜欢你^_^");
        }
        if (Objects.equals(password, "我爱你")) {
            mPasswordView.setError("我也爱你^_^");
        }
        if (Objects.equals(password, "傻逼校培")) {
            mPasswordView.setError("你才傻逼");
        }

        if (Objects.equals(password, "校培傻逼")) {
            mPasswordView.setError("你才傻逼");
        }
        if (Objects.equals(password, "123")) {
            mPasswordView.setError("骗你的，其实密码是“我婷最萌”");
        }

        mPasswordView.requestFocus();
    }



}

