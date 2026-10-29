package com.l4d2.android;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.graphics.Color;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.provider.DocumentsContract;

public class MainActivity extends Activity {
    private static final int PICK_FOLDER = 100;
    private TextView status;
    @Override public void onCreate(Bundle state) {
        super.onCreate(state);
        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(28, 55, 28, 28);
        root.setBackgroundColor(Color.rgb(18, 22, 25));
        TextView title = new TextView(this);
        title.setText("LEFT 4 DEAD 2\nANDROID LAUNCHER v0.1");
        title.setTextColor(Color.WHITE); title.setTextSize(25);
        root.addView(title);
        status = new TextView(this); status.setTextColor(Color.LTGRAY); status.setTextSize(16);
        root.addView(status);
        addButton(root, "SELECT GAME FOLDER", v -> {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
            startActivityForResult(intent, PICK_FOLDER);
        });
        addButton(root, "PLAY", v -> Toast.makeText(this, "Runtime Wine + Box64 belum tersedia (v0.1)", Toast.LENGTH_LONG).show());
        addButton(root, "SETTINGS: 960 x 540 (target)", v -> Toast.makeText(this, "Pengaturan grafis akan hadir pada versi berikutnya", Toast.LENGTH_SHORT).show());
        setContentView(root);
        refresh();
    }
    private void addButton(LinearLayout root, String label, View.OnClickListener click) {
        Button button = new Button(this); button.setText(label); button.setOnClickListener(click); root.addView(button);
    }
    private void refresh() {
        String uri = getPreferences(MODE_PRIVATE).getString("gameFolder", "");
        status.setText("\nGame folder: " + (uri.isEmpty() ? "Belum dipilih" : Uri.parse(uri).getLastPathSegment()) +
            "\nWine: belum terpasang\nBox64: belum terpasang\nGame executable: belum diverifikasi\n");
    }
    @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_FOLDER && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();
            try { getContentResolver().takePersistableUriPermission(uri, Intent.FLAG_GRANT_READ_URI_PERMISSION); }
            catch (SecurityException ignored) { }
            getPreferences(MODE_PRIVATE).edit().putString("gameFolder", uri.toString()).apply();
            refresh();
        }
    }
}
