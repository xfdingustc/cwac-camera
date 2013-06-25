package com.commonsware.cwac.camera;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.media.ExifInterface;
import android.os.Environment;
import android.util.Log;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ImageCleanupTask extends Thread {
  private byte[] data;
  private Bitmap workingCopy=null;
  private int cameraId;
  private CameraHost host;
  private File cacheDir=null;

  ImageCleanupTask(byte[] data, int cameraId, CameraHost host,
                   File cacheDir) {
    this.data=data;
    this.cameraId=cameraId;
    this.host=host;
    this.cacheDir=cacheDir;
  }

  @Override
  public void run() {
    Camera.CameraInfo info=new Camera.CameraInfo();

    Camera.getCameraInfo(cameraId, info);

    if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT
        && host.mirrorFFC()) {
      applyMirror();
    }

    if (host.getDeviceProfile().encodesRotationToExif()) {
      rotateForRealz();
    }

    synchronizeModels(true, false);
    host.saveImage(data);

    if (workingCopy != null) {
      workingCopy.recycle();
    }
  }

  void applyMirror() {
    Log.i(CameraView.TAG, "begin applyMirror()");

    synchronizeModels(false, true);

    // from http://stackoverflow.com/a/8347956/115145

    float[] mirrorY= { -1, 0, 0, 0, 1, 0, 0, 0, 1 };
    Matrix matrix=new Matrix();
    Matrix matrixMirrorY=new Matrix();

    matrixMirrorY.setValues(mirrorY);
    matrix.postConcat(matrixMirrorY);

    Bitmap mirrored=
        Bitmap.createBitmap(workingCopy, 0, 0, workingCopy.getWidth(),
                            workingCopy.getHeight(), matrix, true);

    workingCopy.recycle();
    workingCopy=mirrored;
    data=null;

    Log.i(CameraView.TAG, "end applyMirror()");
  }

  void rotateForRealz() {
    Log.i(CameraView.TAG, "begin rotateForRealz()");

    try {
      synchronizeModels(true, true);

      File dcim=new File(cacheDir, Environment.DIRECTORY_DCIM);

      dcim.mkdirs();

      File photo=new File(dcim, "photo.jpg");

      if (photo.exists()) {
        photo.delete();
      }

      try {
        FileOutputStream fos=new FileOutputStream(photo.getPath());

        fos.write(data);
        fos.close();

        ExifInterface exif=new ExifInterface(photo.getAbsolutePath());
        Bitmap rotated=null;
        data=null;

        try {
          if ("6".equals(exif.getAttribute(ExifInterface.TAG_ORIENTATION))) {
            rotated=rotate(workingCopy, 90);
          }
          else if ("8".equals(exif.getAttribute(ExifInterface.TAG_ORIENTATION))) {
            rotated=rotate(workingCopy, 270);
          }
          else if ("3".equals(exif.getAttribute(ExifInterface.TAG_ORIENTATION))) {
            rotated=rotate(workingCopy, 180);
          }

          if (rotated != null) {
            workingCopy.recycle();
            workingCopy=rotated;
          }
        }
        catch (OutOfMemoryError e) {
          Log.e(CameraView.TAG, "OOM in rotate() call", e);
        }
        finally {
          photo.delete();
        }
      }
      catch (java.io.IOException e) {
        Log.e(CameraView.TAG,
              "Exception in saving photo in rotateForRealz()", e);
      }
    }
    catch (OutOfMemoryError e) {
      Log.e(CameraView.TAG, "OOM in synchronizeModels() call", e);
    }

    Log.i(CameraView.TAG, "end rotateForRealz()");
  }

  private static Bitmap rotate(Bitmap bitmap, int degree) {
    Matrix mtx=new Matrix();

    mtx.setRotate(degree);

    return(Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
                               bitmap.getHeight(), mtx, true));
  }

  private void synchronizeModels(boolean needData, boolean needBitmap) {
    if (data == null && needData) {
      ByteArrayOutputStream out=
          new ByteArrayOutputStream(workingCopy.getWidth()
              * workingCopy.getHeight());

      workingCopy.compress(Bitmap.CompressFormat.JPEG, 100, out);
      data=out.toByteArray();

      try {
        out.close();
      }
      catch (IOException e) {
        Log.e(CameraView.TAG, "Exception in closing a BAOS???", e);
      }
    }

    if (workingCopy == null && needBitmap) {
      workingCopy=BitmapFactory.decodeByteArray(data, 0, data.length);
    }

    if (!needBitmap && workingCopy != null) {
      workingCopy.recycle();
      workingCopy=null;
    }

    System.gc();
  }
}