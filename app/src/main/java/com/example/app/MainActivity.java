package com.example.app;

import android.app.Activity;
import android.os.Bundle;
import android.content.pm.PackageManager;
import android.widget.TextView;

import rikka.shizuku.Shizuku;

public class MainActivity extends Activity {

    private static final int CODE = 100;

    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);

        TextView tv = new TextView(this);
        tv.setText("Testando Shizuku...");
        setContentView(tv);

        if (!Shizuku.pingBinder()) {
            tv.setText("Abra o Shizuku primeiro");
            return;
        }

        if (Shizuku.checkSelfPermission()
                != PackageManager.PERMISSION_GRANTED) {

            tv.setText("Pedindo permissão...");
            Shizuku.requestPermission(CODE);

        } else {
            tv.setText("Shizuku autorizado");
        }
    }
}
