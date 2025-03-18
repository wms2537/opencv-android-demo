package com.example.opencvdemo;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.JavaCameraView;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

public class MainActivity extends AppCompatActivity implements CameraBridgeViewBase.CvCameraViewListener2 {

    private static final int CAMERA_PERMISSION_REQUEST = 100;
    private JavaCameraView cameraView;
    private int currentProcessingMode = 0; // 0: grayscale, 1: canny

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        // Initialize OpenCV
        if (!OpenCVLoader.initDebug()) {
            Log.e(TAG, "OpenCV initialization failed");
            Toast.makeText(this, "OpenCV initialization failed!", Toast.LENGTH_LONG).show();
            finish();
            return;
        }
        
        // Check and request camera permission
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) 
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, 
                    new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST);
        } else {
            initializeOpenCV();
        }
    }
    
    private void initializeOpenCV() {
        // Initialize OpenCV camera view
        cameraView = findViewById(R.id.camera_view);
        cameraView.setCvCameraViewListener(this);
        cameraView.enableView();
        
        // Set up buttons
        Button btnGrayscale = findViewById(R.id.btn_grayscale);
        Button btnCanny = findViewById(R.id.btn_canny);
        
        btnGrayscale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentProcessingMode = 0; // Grayscale mode
            }
        });
        
        btnCanny.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentProcessingMode = 1; // Canny edge detection mode
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, 
                                          @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION_REQUEST) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                initializeOpenCV();
            } else {
                Toast.makeText(this, "Camera permission is required", Toast.LENGTH_LONG).show();
                finish();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (cameraView != null) {
            cameraView.enableView();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (cameraView != null) {
            cameraView.disableView();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (cameraView != null) {
            cameraView.disableView();
        }
    }

    @Override
    public void onCameraViewStarted(int width, int height) {
    }

    @Override
    public void onCameraViewStopped() {
    }

    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        Mat rgba = inputFrame.rgba();
        Mat processedImage = new Mat();
        
        switch (currentProcessingMode) {
            case 0: // Grayscale
                Imgproc.cvtColor(rgba, processedImage, Imgproc.COLOR_RGBA2GRAY);
                break;
            case 1: // Canny edge detection
                Mat grayImage = new Mat();
                Imgproc.cvtColor(rgba, grayImage, Imgproc.COLOR_RGBA2GRAY);
                Imgproc.Canny(grayImage, processedImage, 50, 150);
                grayImage.release();
                break;
        }
        
        return processedImage;
    }
}