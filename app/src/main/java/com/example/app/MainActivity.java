package com.example.app;

import android.app.Activity;
import android.os.Bundle;
import android.content.pm.PackageManager;
import android.widget.TextView;

import rikka.shizuku.Shizuku;

public class MainActivity extends Activity {

    private final int REQUEST = 10;

    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);

        TextView t = new TextView(this);
        t.setText("Abrindo...");
        setContentView(t);

        if (!Shizuku.pingBinder()) {
            t.setText("Shizuku não conectado");
            return;
        }

        if (Shizuku.checkSelfPermission()
                != PackageManager.PERMISSION_GRANTED) {

            Shizuku.requestPermission(REQUEST);

        } else {
            t.setText("Permissão já dada");
        }
    }
}
