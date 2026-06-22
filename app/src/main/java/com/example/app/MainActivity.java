package com.example.app;

import android.app.Activity;
import android.os.Bundle;
import android.content.pm.PackageManager;
import android.widget.TextView;

import rikka.shizuku.Shizuku;

public class MainActivity extends Activity {

    private final int CODE = 100;
    private TextView tv;

    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);

        tv = new TextView(this);
        tv.setText("Shizuku test");
        setContentView(tv);

        if (!Shizuku.pingBinder()) {
            tv.setText("Shizuku não conectado");
            return;
        }

        if (Shizuku.checkSelfPermission() != PackageManager.PERMISSION_GRANTED) {
            // Agora funciona com a API 13.2.0+
            Shizuku.requestPermission(this, CODE, new Shizuku.OnRequestPermissionResultListener() {
                @Override
                public void onRequestPermissionResult(int requestCode, int grantResult) {
                    runOnUiThread(() -> {
                        if (requestCode == CODE) {
                            if (grantResult == PackageManager.PERMISSION_GRANTED) {
                                tv.setText("Permitido");
                            } else {
                                tv.setText("Permissão negada");
                            }
                        }
                    });
                }
            });
        } else {
            tv.setText("Permitido");
        }
    }
}
