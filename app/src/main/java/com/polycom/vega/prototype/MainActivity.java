package com.polycom.vega.prototype;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.polycom.vega.fundamental.CallingInformationObject;
import com.polycom.vega.fundamental.VegaApplication;

public class MainActivity extends AppCompatActivity implements Thread.UncaughtExceptionHandler {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Thread.currentThread().setUncaughtExceptionHandler(this);

        getSupportActionBar().hide();
        findViewById(R.id.activity_main_incall_header).setVisibility(View.GONE);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_main, new OptionListFragment()).commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
    public void uncaughtException(Thread thread, Throwable ex) {
        Toast.makeText(MainActivity.this, ex.getMessage(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onAttachFragment(android.support.v4.app.Fragment fragment) {
        super.onAttachFragment(fragment);

        checkInCallStatus();
    }

    private void checkInCallStatus() {
        CallingInformationObject callingInfo = ((VegaApplication) getApplication()).getCallingInfo();
        final View inCallGlobalHeader = findViewById(R.id.activity_main_incall_header);

        if (callingInfo != null) {
            inCallGlobalHeader.setVisibility(View.VISIBLE);
            inCallGlobalHeader.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getSupportFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.fragment_main, new InCallFragment()).commit();
                }
            });

            ((TextView) inCallGlobalHeader.findViewById(R.id.fragment_incall_global_header_titleTextView)).setText(String.format(getString(R.string.in_call_title_format), callingInfo.getContact().getDisplayName()));
        } else {
            inCallGlobalHeader.setVisibility(View.GONE);
        }
    }
}
