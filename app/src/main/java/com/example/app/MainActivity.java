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

        // Verifica se o serviço Shizuku está disponível
        if (!Shizuku.pingBinder()) {
            tv.setText("Shizuku não conectado");
            return;
        }

        // Verifica permissão
        if (Shizuku.checkSelfPermission() != PackageManager.PERMISSION_GRANTED) {
            // Método atualizado com callback (obrigatório na API 13+)
            Shizuku.requestPermission(CODE, new Shizuku.OnRequestPermissionResultListener() {
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
