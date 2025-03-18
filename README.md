# OpenCV Demo

A simple Android application demonstrating real-time image processing using OpenCV. This project shows how to integrate OpenCV with Android and implement basic image processing features.

## Features

### 1. Real-time Grayscale Conversion
- Converts the camera feed to grayscale in real-time
- Demonstrates basic color space transformation

### 2. Canny Edge Detection
- Detects edges in the camera feed using the Canny algorithm
- Shows advanced image processing capabilities

## Requirements

- Android Studio
- Android SDK 24+ (Android 7.0 or higher)
- OpenCV for Android
- Device with camera

## Setup Instructions

1. Clone this repository
2. Open the project in Android Studio
3. Build and run the application on a device (emulators may not work properly with camera features)

## Usage

1. Launch the app
2. Grant camera permissions when prompted
3. The app will open with the camera view in grayscale mode by default
4. Use the buttons at the bottom of the screen to switch between:
   - **Grayscale**: Converts the camera feed to grayscale
   - **Canny Edge**: Detects and displays edges in the camera feed

## Implementation Details

### OpenCV Integration
The app uses the OpenCV Android SDK (4.8.0) for image processing operations. OpenCV is initialized at app startup, and camera frames are processed using OpenCV's native functions.

### Camera Handling
- Uses JavaCameraView from OpenCV
- Implements CvCameraViewListener2 for frame processing
- Properly handles camera lifecycle (enable/disable during resume/pause)

### Image Processing
- Grayscale conversion: `Imgproc.cvtColor(rgba, processedImage, Imgproc.COLOR_RGBA2GRAY)`
- Edge detection: `Imgproc.Canny(grayImage, processedImage, 50, 150)`

## Permissions

- Camera: Required for accessing the device camera

## License

This project is for educational purposes.

## Acknowledgments

- OpenCV Team for providing the OpenCV library
- Android Open Source Project 