package com.example.app;

import android.app.Activity;
import android.os.Bundle;
import android.content.pm.PackageManager;
import android.widget.TextView;

import rikka.shizuku.Shizuku;

public class MainActivity extends Activity {

    private final int CODE = 100;
    private TextView tv;

    private final Shizuku.OnRequestPermissionResultListener permissionListener = 
        new Shizuku.OnRequestPermissionResultListener() {
            @Override
            public void onRequestPermissionResult(int requestCode, int grantResult) {
                if (requestCode == CODE) {
                    if (grantResult == PackageManager.PERMISSION_GRANTED) {
                        tv.setText("Permitido com sucesso!");
                    } else {
                        tv.setText("Permissão recusada pelo usuário.");
                    }
                }
            }
        };

    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);

        tv = new TextView(this);
        tv.setText("Testando Shizuku...");
        setContentView(tv);

        Shizuku.addRequestPermissionResultListener(permissionListener);

        if (!Shizuku.pingBinder()) {
            tv.setText("Shizuku não conectado. Ative o serviço no app do Shizuku primeiro!");
            return;
        }

        if (Shizuku.checkSelfPermission() != PackageManager.PERMISSION_GRANTED) {
            Shizuku.requestPermission(CODE);
        } else {
            tv.setText("Permitido");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Shizuku.removeRequestPermissionResultListener(permissionListener);
    }
}
