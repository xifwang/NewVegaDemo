package com.polycom.vega.prototype;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.AlphaAnimation;
import android.widget.TextView;

import com.polycom.vega.fundamental.IActivity;
import com.polycom.vega.fundamental.VegaActivity;
import com.polycom.vega.localstorage.LocalStorageHelper;

import java.util.Locale;

public class SplashActivity extends VegaActivity implements IActivity {
    private TextView appNameTextView;
    private TextView copyrightTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Thread.currentThread().setUncaughtExceptionHandler(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        getSupportActionBar().hide();

        initUILanguage();
        initComponent();
        initAnimation();

        new Handler(getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(getApplicationContext(), PairActivity.class);
                startActivity(intent);
            }
        }, 3000);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_splash, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void initComponent() {
        this.appNameTextView = (TextView) findViewById(R.id.activitysplash_appNameTextView);
        this.copyrightTextView = (TextView) findViewById(R.id.activitysplash_copyrightTextView);
    }

    @Override
    public void initComponentState() {

    }

    @Override
    public void initAnimation() {
        AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
        alphaAnimation.setDuration(2000);

        this.appNameTextView.setAnimation(alphaAnimation);
        this.copyrightTextView.setAnimation(alphaAnimation);
    }

    private void initUILanguage() {
        Resources resources = getResources();
        Configuration configuration = resources.getConfiguration();
        DisplayMetrics displayMetrics = resources.getDisplayMetrics();
        Context context = getApplicationContext();
        String language = LocalStorageHelper.getInstance().getLanguage(getApplicationContext());

        if (TextUtils.isEmpty(language) && configuration.locale != null) {
            LocalStorageHelper.getInstance().saveLanguage(context, configuration.locale.toString());
        } else if (!TextUtils.equals(configuration.locale.toString(), language)) {
            configuration.locale = new Locale(language);
            resources.updateConfiguration(configuration, displayMetrics);
            finish();
            startActivity(this.getIntent());
        }
    }

    @Override
    public void registerNotification() {

    }
}
