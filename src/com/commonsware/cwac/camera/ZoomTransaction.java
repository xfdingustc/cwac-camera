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
import android.hardware.Camera.OnZoomChangeListener;

final public class ZoomTransaction implements OnZoomChangeListener {
  private Camera camera;
  private int level;
  private Runnable onComplete=null;
  private OnZoomChangeListener onChange=null;

  ZoomTransaction(Camera camera, int level) {
    this.camera=camera;
    this.level=level;
  }

  public ZoomTransaction onComplete(Runnable onComplete) {
    this.onComplete=onComplete;

    return(this);
  }

  public ZoomTransaction onChange(Camera.OnZoomChangeListener onChange) {
    this.onChange=onChange;

    return(this);
  }

  public void go() {
    Camera.Parameters params=camera.getParameters();

    if (params.isSmoothZoomSupported()) {
      camera.setZoomChangeListener(this);
      camera.startSmoothZoom(level);
    }
    else {
      params.setZoom(level);
      camera.setParameters(params);
      onZoomChange(level, true, camera);
    }
  }

  public void cancel() {
    camera.stopSmoothZoom();
  }

  @Override
  public void onZoomChange(int zoomValue, boolean stopped, Camera camera) {
    if (onChange != null) {
      onChange.onZoomChange(zoomValue, stopped, camera);
    }

    if (stopped && onComplete != null) {
      onComplete.run();
    }
  }
}
