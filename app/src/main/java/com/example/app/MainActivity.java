package com.example.app;

import android.app.Activity;
import android.os.Bundle;
import android.content.pm.PackageManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.LinearLayout;
import android.widget.Toast;

import rikka.shizuku.Shizuku;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class MainActivity extends Activity {

    private final int CODE = 100;
    private TextView output;
    private EditText input;
    private Button runBtn;
    private boolean permissionOk = false;

    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);

        input = new EditText(this);
        input.setHint("comando (ex: wm size)");
        layout.addView(input);

        runBtn = new Button(this);
        runBtn.setText("Executar");
        layout.addView(runBtn);

        output = new TextView(this);
        ScrollView sv = new ScrollView(this);
        sv.addView(output);
        layout.addView(sv, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                0, 1f));

        setContentView(layout);

        if (!Shizuku.pingBinder()) {
            output.setText("Shizuku não está em execução.\nAbra o app Shizuku e inicie o serviço.");
            return;
        }

        if (Shizuku.checkSelfPermission() == PackageManager.PERMISSION_GRANTED) {
            permissionOk = true;
            output.setText("Permissão OK. Pronto para comandos.");
        } else {
            output.setText("Solicitando permissão...");
            Shizuku.addRequestPermissionResultListener(new Shizuku.OnRequestPermissionResultListener() {
                @Override
                public void onRequestPermissionResult(int requestCode, int grantResult) {
                    runOnUiThread(() -> {
                        if (requestCode == CODE) {
                            if (grantResult == PackageManager.PERMISSION_GRANTED) {
                                permissionOk = true;
                                output.append("\nPermissão concedida! Pode usar o terminal.");
                            } else {
                                output.append("\nPermissão negada.");
                            }
                        }
                    });
                }
            });
            Shizuku.requestPermission(CODE);
        }

        runBtn.setOnClickListener(v -> {
            if (!permissionOk) {
                Toast.makeText(this, "Permissão necessária", Toast.LENGTH_SHORT).show();
                return;
            }

            String cmd = input.getText().toString().trim();
            if (cmd.isEmpty()) return;

            output.append("\n$ " + cmd + "\n");
            executeCommand(cmd);
        });
    }

    private void executeCommand(String command) {
        new Thread(() -> {
            try {
                // Método público na API 13.2.1
                Process process = Shizuku.newProcess(
                        new String[]{"sh", "-c", command},
                        null,
                        null
                );
                BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                BufferedReader errReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
                StringBuilder sb = new StringBuilder();

                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line).append("\n");
                }
                while ((line = errReader.readLine()) != null) {
                    sb.append("[ERR] ").append(line).append("\n");
                }

                process.waitFor();
                int exitCode = process.exitValue();
                if (exitCode != 0) sb.append("exit code: ").append(exitCode);

                final String result = sb.toString();
                runOnUiThread(() -> output.append(result));

            } catch (Exception e) {
                runOnUiThread(() -> output.append("Erro: " + e.getMessage()));
            }
        }).start();
    }
}
