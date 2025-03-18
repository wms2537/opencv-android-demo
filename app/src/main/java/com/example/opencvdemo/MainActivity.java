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

    private static final String TAG = "OpenCVDemo";
    private static final int CAMERA_PERMISSION_REQUEST = 100;
    private JavaCameraView cameraView;
    private Button btnRaw, btnGrayscale, btnCanny;
    private int currentProcessingMode = 0; // 0: grayscale, 1: canny, 2: raw

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Initialize OpenCV before setting content view
        if (!OpenCVLoader.initDebug()) {
            Log.e(TAG, "OpenCV initialization failed");
            Toast.makeText(this, "OpenCV initialization failed!", Toast.LENGTH_LONG).show();
            finish();
            return;
        } else {
            Log.d(TAG, "OpenCV initialization successful");
        }
        
        setContentView(R.layout.activity_main);
        
        // Check and request camera permission
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) 
                != PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "Requesting camera permission");
            ActivityCompat.requestPermissions(this, 
                    new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST);
        } else {
            Log.d(TAG, "Camera permission already granted");
            initializeOpenCV();
        }
    }
    
    private void initializeOpenCV() {
        // Initialize OpenCV camera view
        cameraView = findViewById(R.id.camera_view);
        
        // Explicitly set camera ID and visibility
        cameraView.setCameraPermissionGranted();
        cameraView.setCameraIndex(CameraBridgeViewBase.CAMERA_ID_BACK); // or CAMERA_ID_FRONT
        cameraView.setVisibility(View.VISIBLE);
        
        // Set listeners
        cameraView.setCvCameraViewListener(this);
        
        // Set up buttons
        btnRaw = findViewById(R.id.btn_raw);
        btnGrayscale = findViewById(R.id.btn_grayscale);
        btnCanny = findViewById(R.id.btn_canny);
        
        btnRaw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentProcessingMode = 2; // Raw video mode
                Log.d(TAG, "Switched to raw video mode");
            }
        });
        
        btnGrayscale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentProcessingMode = 0; // Grayscale mode
                Log.d(TAG, "Switched to grayscale mode");
            }
        });
        
        btnCanny.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentProcessingMode = 1; // Canny edge detection mode
                Log.d(TAG, "Switched to Canny edge mode");
            }
        });
        
        // Start the camera view after everything is set up
        cameraView.enableView();
        Log.d(TAG, "Camera view enabled");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, 
                                          @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION_REQUEST) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d(TAG, "Camera permission granted in onRequestPermissionsResult");
                initializeOpenCV();
            } else {
                Log.e(TAG, "Camera permission denied");
                Toast.makeText(this, "Camera permission is required", Toast.LENGTH_LONG).show();
                finish();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (cameraView != null) {
            Log.d(TAG, "Enabling camera view in onResume");
            cameraView.enableView();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (cameraView != null) {
            Log.d(TAG, "Disabling camera view in onPause");
            cameraView.disableView();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (cameraView != null) {
            Log.d(TAG, "Disabling camera view in onDestroy");
            cameraView.disableView();
        }
    }

    @Override
    public void onCameraViewStarted(int width, int height) {
        Log.d(TAG, "Camera view started: " + width + "x" + height);
    }

    @Override
    public void onCameraViewStopped() {
        Log.d(TAG, "Camera view stopped");
    }

    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        Mat rgba = inputFrame.rgba();
        
        switch (currentProcessingMode) {
            case 0: // Grayscale
                Mat grayImage = new Mat();
                Imgproc.cvtColor(rgba, grayImage, Imgproc.COLOR_RGBA2GRAY);
                return grayImage;
            case 1: // Canny edge detection
                Mat edgeImage = new Mat();
                Mat grayForEdge = new Mat();
                Imgproc.cvtColor(rgba, grayForEdge, Imgproc.COLOR_RGBA2GRAY);
                Imgproc.Canny(grayForEdge, edgeImage, 50, 150);
                grayForEdge.release();
                return edgeImage;
            case 2: // Raw video
            default:
                return rgba; // Return the original frame without processing
        }
    }
}