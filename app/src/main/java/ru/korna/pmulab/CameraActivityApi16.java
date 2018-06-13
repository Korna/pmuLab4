package ru.korna.pmulab;


import android.Manifest;
import android.annotation.TargetApi;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;

import butterknife.BindView;
import butterknife.ButterKnife;


@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
public class CameraActivityApi16 extends AppCompatActivity {
    @BindView(R.id.surfaceView)
    SurfaceView surfaceView;
    @BindView(R.id.button2)
    Button button;


    SurfaceHolder surfaceHolder;
    Camera camera;

    File file;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        ButterKnife.bind(this);
        surfaceHolder = surfaceView.getHolder();

        file = new File(getCacheDir(), "photo.jpg");

        button.setOnClickListener(this::onClickPicture);

        SurfaceHolder holder = surfaceView.getHolder();
        holder.addCallback(new SurfaceHolder.Callback() {
            @Override public void surfaceCreated(SurfaceHolder holder) {
                try {
                    camera.setPreviewDisplay(holder);
                    camera.startPreview();
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(CameraActivityApi16.this, e.toString(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder surfaceHolder) {

            }

        });
}
    @Override protected void onPause() {
        super.onPause();
        if (camera != null)
            camera.release();
        camera = null;
    }

    public void onClickPicture(View view) {
        if(camera != null){
            try {
                if(checkPermission())
                camera.takePicture(null, null, (data, camera) -> {
                    try {
                        FileOutputStream fos = new FileOutputStream(file);
                        fos.write(data);
                        fos.close();
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                });
                else
                    askPermission();

            }catch (RuntimeException re){
                Log.e("photoMake", re.toString());
            }
        }
    }

    @Override protected void onResume() {
        super.onResume();
        try {
            if(!checkPermission())
                askPermission();
            else
                camera = Camera.open();
        }catch (RuntimeException re){
            Log.e("onResume", re.toString());
        }

    }

    public static final int REQ_PERMISSION = 1;
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.d("onRequestPermissions", "onRequestPermissionsResult()");
        switch (requestCode) {
            case REQ_PERMISSION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    if (checkPermission())
                        camera = Camera.open();
                break;
            default:
                break;
        }
    }
    public boolean checkPermission() {
        Log.d("checkPermission", "checkPermission()");
        return ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED ;
    }

    private void askPermission() {
        Log.d("askPermission", "askPermission()");
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, REQ_PERMISSION);
    }

}
