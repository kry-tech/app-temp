package com.example.app;

import android.app.Activity;
import android.os.Bundle;
import android.content.pm.PackageManager;
import android.widget.TextView;

import rikka.shizuku.Shizuku;

public class MainActivity extends Activity {

    TextView tv;
    final int CODE = 100;

    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);

        tv = new TextView(this);
        tv.setText("Testando Shizuku...");
        setContentView(tv);

        if (!Shizuku.pingBinder()) {
            tv.setText("Shizuku não iniciado");
            return;
        }

        if (Shizuku.checkSelfPermission()
                != PackageManager.PERMISSION_GRANTED) {

            tv.setText("Pedindo permissão...");
            Shizuku.requestPermission(CODE);

        } else {

            tv.setText("Permissão OK");

            try {
                Shizuku.newProcess(
                    new String[]{"sh", "-c", "echo Shizuku_OK"},
                    null,
                    null
                );

            } catch (Exception e) {
                tv.setText("Erro: " + e.getMessage());
            }
        }
    }
}
