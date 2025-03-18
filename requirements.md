##### 2.2 两个功能展示

###### 功能一：实时图像灰度化
- **描述**：将相机捕获的彩色图像实时转换为灰度图像，展示基本的图像处理能力。  
- **实现**：  
  1. 在布局文件中添加 `JavaCameraView`：  
     ```xml
     <org.opencv.android.JavaCameraView
         android:id="@+id/camera_view"
         android:layout_width="match_parent"
         android:layout_height="match_parent" />
     ```  
  2. 在 Activity 中实现 `CvCameraViewListener2` 接口，处理相机帧：  
     ```java
     import org.opencv.android.BaseLoaderCallback;
     import org.opencv.android.CameraBridgeViewBase;
     import org.opencv.android.OpenCVLoader;
     import org.opencv.core.Mat;
     import org.opencv.imgproc.Imgproc;

     public class MainActivity extends AppCompatActivity implements CameraBridgeViewBase.CvCameraViewListener2 {
         private CameraBridgeViewBase cameraView;

         @Override
         protected void onCreate(Bundle savedInstanceState) {
             super.onCreate(savedInstanceState);
             setContentView(R.layout.activity_main);

             cameraView = findViewById(R.id.camera_view);
             cameraView.setCvCameraViewListener(this);
             cameraView.enableView();
         }

         @Override
         public void onCameraViewStarted(int width, int height) {}

         @Override
         public void onCameraViewStopped() {}

         @Override
         public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
             Mat rgba = inputFrame.rgba();
             Mat gray = new Mat();
             Imgproc.cvtColor(rgba, gray, Imgproc.COLOR_RGBA2GRAY);
             return gray;
         }
     }
     ```

###### 功能二：边缘检测
- **描述**：使用 Canny 边缘检测算法实时检测相机图像中的边缘，展示高级图像处理功能。  
- **实现**：  
  - 修改 `onCameraFrame` 方法以应用 Canny 算法：  
    ```java
    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        Mat rgba = inputFrame.rgba();
        Mat edges = new Mat();
        Imgproc.Canny(rgba, edges, 50, 150); // 阈值可调整
        return edges;
    }
    ```

---

