package com.greore.lgqscreenshots;

import android.accessibilityservice.AccessibilityService;
import android.graphics.Bitmap;
import android.media.Image;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.accessibility.AccessibilityEvent;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import static com.greore.lgqscreenshots.MainActivity.imageReader;
import static com.greore.lgqscreenshots.MainActivity.height;
import static com.greore.lgqscreenshots.MainActivity.width;

public class AccessibilityServiceEx extends AccessibilityService {

    private static String TAG = android.accessibilityservice.AccessibilityService.class.getSimpleName();

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        Log.d(TAG, "service is connected");
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent accessibilityEvent) {

        Log.d(TAG, "onAccessibiltyEvent" + accessibilityEvent.toString());

    }

    @Override
    public void onInterrupt() {

    }

    // here you can intercept the keyevent
    @Override
    protected boolean onKeyEvent(KeyEvent event) {
        int action = event.getAction();
        int keyCode = event.getKeyCode();

        Log.d(TAG, "keyCode: " + keyCode);

        if (keyCode == 165) {
            if ( action == KeyEvent.ACTION_DOWN) {
                (new Thread(new Runnable() {
                    @Override
                    public void run() {
                        savePngFile(getScreenshot());
                    }
                })).start();

                Toast.makeText(
                        this,
                        "LG Q Screenshots saved",
                        Toast.LENGTH_SHORT).show();

            }
            return true;
        }

        return false;
    }

    private Bitmap getScreenshot() {
        Image image = imageReader.acquireLatestImage();
        Image.Plane[] planes = image.getPlanes();
        ByteBuffer buffer = planes[0].getBuffer();

        int pixelStride = planes[0].getPixelStride();
        int rowStride = planes[0].getRowStride();
        int rowPadding = rowStride - pixelStride * width;

        Bitmap bitmap = Bitmap.createBitmap(
                width + rowPadding / pixelStride, height,
                Bitmap.Config.ARGB_8888);
        bitmap.copyPixelsFromBuffer(buffer);
        image.close();

        return bitmap;
    }

    public void savePngFile(Bitmap bitmap){
        try {
            File extStrageDir =
                    Environment.getExternalStorageDirectory();

            File saveDir = new File(extStrageDir.getAbsolutePath()
                    + "/" + Environment.DIRECTORY_DCIM, "qScreenshots");
            if(!saveDir.exists()){
                saveDir.mkdir();
            }

            File file = new File(
                    saveDir.getAbsolutePath(),
                    getFileName());
            FileOutputStream outStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outStream);
            outStream.close();

            MediaScannerConnection.scanFile(this,
                    new String[] { file.getAbsolutePath() }, null,
                    new MediaScannerConnection.OnScanCompletedListener() {
                        @Override
                        public void onScanCompleted(String path, Uri uri) {
                            Log.v("MediaScanWork", "file " + path
                                    + " was scanned seccessfully: " + uri);
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected String getFileName(){
        Calendar cl = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");

        return "Screenshots_" + sdf.format(cl.getTime()) + ".png";
    }
}
