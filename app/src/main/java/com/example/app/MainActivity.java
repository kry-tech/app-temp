package com.example.app;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import android.content.pm.PackageManager;

import rikka.shizuku.Shizuku;

public class MainActivity extends Activity {

    int code = 100;

    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);

        TextView t = new TextView(this);
        t.setText("Shizuku teste");
        setContentView(t);

        if (Shizuku.pingBinder()) {

            if (Shizuku.checkSelfPermission()
                    != PackageManager.PERMISSION_GRANTED) {

                Shizuku.requestPermission(code);
            }

        } else {
            t.setText("Shizuku não está iniciado");
        }
    }
}
