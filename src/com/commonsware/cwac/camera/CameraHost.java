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

import android.hardware.Camera;
import android.media.MediaRecorder;

public interface CameraHost {
  Camera.Parameters adjustPictureParameters(Camera.Parameters parameters);

  Camera.Parameters adjustPreviewParameters(Camera.Parameters parameters);

  void configureRecorderAudio(int cameraId, MediaRecorder recorder);

  void configureRecorderOutput(int cameraId, MediaRecorder recorder);

  void configureRecorderProfile(int cameraId, MediaRecorder recorder);

  int getCameraId();

  DeviceProfile getDeviceProfile();

  Camera.Size getPictureSize(Camera.Parameters parameters);

  Camera.Size getPreviewSize(int displayOrientation, int width,
                             int height, Camera.Parameters parameters);

  void handleException(Exception e);
  
  boolean mirrorFFC();
  
  void saveImage(byte[] image);
}
