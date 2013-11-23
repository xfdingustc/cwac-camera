/***
  Copyright (c) 2013 CommonsWare, LLC
  
  Licensed under the Apache License, Version 2.0 (the "License"); you may
  not use this file except in compliance with the License. You may obtain
  a copy of the License at
    http://www.apache.org/licenses/LICENSE-2.0
  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
 */

package com.commonsware.cwac.camera;

import android.graphics.Bitmap;
import android.hardware.Camera;
import android.media.MediaRecorder;

public interface CameraHost extends Camera.AutoFocusCallback {
  public enum RecordingHint {
    STILL_ONLY, VIDEO_ONLY, ANY
  }

  public enum FailureReason {
    NO_CAMERAS_REPORTED(1), UNKNOWN(2);

    int value;

    private FailureReason(int value) {
      this.value=value;
    }
  }

  Camera.Parameters adjustPictureParameters(Camera.Parameters parameters);

  Camera.Parameters adjustPreviewParameters(Camera.Parameters parameters);

  void autoFocusAvailable();

  void autoFocusUnavailable();

  void configureRecorderAudio(int cameraId, MediaRecorder recorder);

  void configureRecorderOutput(int cameraId, MediaRecorder recorder);

  void configureRecorderProfile(int cameraId, MediaRecorder recorder);

  int getCameraId();

  DeviceProfile getDeviceProfile();

  Camera.Size getPictureSize(Camera.Parameters parameters);

  Camera.Size getPreviewSize(int displayOrientation, int width,
                             int height, Camera.Parameters parameters);

  Camera.Size getPreferredPreviewSizeForVideo(int displayOrientation,
                                              int width,
                                              int height,
                                              Camera.Parameters parameters,
                                              Camera.Size deviceHint);

  Camera.ShutterCallback getShutterCallback();

  void handleException(Exception e);

  boolean mirrorFFC();

  boolean rotateBasedOnExif();

  void saveImage(Bitmap bitmap);

  void saveImage(byte[] image);

  boolean useSingleShotMode();

  RecordingHint getRecordingHint();

  void onCameraFail(FailureReason reason);
}
