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

import android.annotation.TargetApi;
import android.app.Fragment;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class CameraFragment extends Fragment {
  private CameraView cameraView=null;
  private CameraHost host=null;

  @Override
  public View onCreateView(LayoutInflater inflater,
                           ViewGroup container,
                           Bundle savedInstanceState) {
    cameraView=new CameraView(getActivity());
    cameraView.setHost(getHost());
    
    return(cameraView);
  }

  @Override
  public void onResume() {
    super.onResume();

    cameraView.onResume();
  }

  @Override
  public void onPause() {
    cameraView.onPause();

    super.onPause();
  }
  
  public CameraHost getHost() {
    if (host==null) {
      host=new SimpleCameraHost(getActivity());
    }
    
    return(host);
  }
 
  public void setHost(CameraHost host) {
    this.host=host;
  }
  
  public void takePicture() {
    takePicture(false, true);
  }
  
  public void takePicture(boolean needBitmap, boolean needByteArray) {
    cameraView.takePicture(needBitmap, needByteArray);
  }

  public boolean isRecording() {
    return(cameraView==null ? false : cameraView.isRecording());
  }

  public void record() throws Exception {
    cameraView.record();
  }

  public void stopRecording() throws Exception {
    cameraView.stopRecording();
  }

  public int getDisplayOrientation() {
    return(cameraView.getDisplayOrientation());
  }

  public void lockToLandscape(boolean enable) {
    cameraView.lockToLandscape(enable);
  }
  
  public void autoFocus() {
    cameraView.autoFocus();
  }
  
  public void cancelAutoFocus() {
    cameraView.cancelAutoFocus();
  }
  
  public void restartPreview() {
    cameraView.restartPreview();
  }
}
