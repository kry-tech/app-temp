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
            // Registra o listener ANTES de pedir permissão
            Shizuku.addRequestPermissionResultListener(new Shizuku.OnRequestPermissionResultListener() {
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

            // Método que abre o diálogo (funciona na 13.1.5)
            Shizuku.requestPermission(CODE);
        } else {
            tv.setText("Permitido");
        }
    }
}
