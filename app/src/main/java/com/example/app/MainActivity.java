package com.example.app;

import android.app.Activity;
import android.os.Bundle;
import android.content.pm.PackageManager;

import moe.shizuku.api.Shizuku;

public class MainActivity extends Activity {

    int code = 100;

    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);

        if (Shizuku.checkSelfPermission() 
            != PackageManager.PERMISSION_GRANTED) {

            Shizuku.requestPermission(code);
        }
    }
}
