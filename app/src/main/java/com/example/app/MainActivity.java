package com.example.app;

import android.app.Activity;
import android.os.Bundle;
import android.content.pm.PackageManager;
import android.widget.TextView;

import rikka.shizuku.Shizuku;

public class MainActivity extends Activity {

    private static final int REQUEST_CODE = 1;
    TextView tv;

    private final Shizuku.OnRequestPermissionResultListener listener =
            (requestCode, grantResult) -> {
                if (requestCode == REQUEST_CODE) {
                    if (grantResult == PackageManager.PERMISSION_GRANTED) {
                        tv.setText("Shizuku autorizado");
                    } else {
                        tv.setText("Permissão negada");
                    }
                }
            };

    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);

        tv = new TextView(this);
        tv.setText("Verificando Shizuku...");
        setContentView(tv);

        Shizuku.addRequestPermissionResultListener(listener);

        if (!Shizuku.pingBinder()) {
            tv.setText("Inicie o Shizuku primeiro");
            return;
        }

        if (Shizuku.checkSelfPermission()
                != PackageManager.PERMISSION_GRANTED) {

            tv.setText("Pedindo permissão...");
            Shizuku.requestPermission(REQUEST_CODE);

        } else {
            tv.setText("Já autorizado");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Shizuku.removeRequestPermissionResultListener(listener);
    }
}
