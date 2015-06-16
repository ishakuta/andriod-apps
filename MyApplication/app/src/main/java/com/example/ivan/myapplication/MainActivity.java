package com.example.ivan.myapplication;

import android.content.Context;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity {

    private Toast mAppToast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * First button on click
     *
     * @param view
     */
    public void sendMessageOne(View view)
    {
        Button button = (Button)view;
        CharSequence text = button.getText();

        Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;

        // stop existing toasts
        if(mAppToast != null) {
            mAppToast.cancel();
        }

        // Make and display new toast
        mAppToast = Toast.makeText(context, text, duration);
        mAppToast.show();
    }
}
