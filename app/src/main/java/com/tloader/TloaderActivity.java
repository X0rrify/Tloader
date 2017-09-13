package com.tloader;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;


public class TloaderActivity extends AppCompatActivity {

    private static final int VITAL_PERMISSIONS_CODE = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tloader);
        requestPermissions();
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(TloaderActivity.this, new String[]{
                Manifest.permission.INTERNET,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
        }, VITAL_PERMISSIONS_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == VITAL_PERMISSIONS_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_DENIED ||
                    grantResults[1] == PackageManager.PERMISSION_DENIED ||
                    grantResults[2] == PackageManager.PERMISSION_DENIED) {
                requestPermissions();
            } else {
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.container, ListFragment.newInstance(), ListFragment.TAG)
                        .commit();
            }
        }
    }
}
