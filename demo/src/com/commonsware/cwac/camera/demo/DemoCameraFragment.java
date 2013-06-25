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

package com.commonsware.cwac.camera.demo;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;
import com.commonsware.cwac.camera.CameraFragment;
import com.commonsware.cwac.camera.SimpleCameraHost;

public class DemoCameraFragment extends CameraFragment {
  private static final String KEY_USE_FFC=
      "com.commonsware.cwac.camera.demo.USE_FFC";

  static DemoCameraFragment newInstance(boolean useFFC) {
    DemoCameraFragment f=new DemoCameraFragment();
    Bundle args=new Bundle();

    args.putBoolean(KEY_USE_FFC, useFFC);
    f.setArguments(args);

    return(f);
  }

  @Override
  public void onActivityCreated(Bundle state) {
    super.onActivityCreated(state);

    setHasOptionsMenu(true);
    setHost(new DemoCameraHost());
  }

  @Override
  public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    inflater.inflate(R.menu.camera, menu);

    if (isRecording()) {
      menu.findItem(R.id.record).setVisible(false);
      menu.findItem(R.id.stop).setVisible(true);
    }
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    if (item.getItemId() == R.id.camera) {
      takePicture();

      return(true);
    }
    else if (item.getItemId() == R.id.record) {
      try {
        record();
        getActivity().invalidateOptionsMenu();
      }
      catch (Exception e) {
        Log.e(getClass().getSimpleName(), "Exception trying to record",
              e);
        Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG)
             .show();
      }

      return(true);
    }
    else if (item.getItemId() == R.id.stop) {
      try {
        stopRecording();
        getActivity().invalidateOptionsMenu();
      }
      catch (Exception e) {
        Log.e(getClass().getSimpleName(),
              "Exception trying to stop recording", e);
        Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG)
             .show();
      }

      return(true);
    }

    return(super.onOptionsItemSelected(item));
  }
  
  class DemoCameraHost extends SimpleCameraHost {
    @Override
    public boolean useFrontFacingCamera() {
      return(getArguments().getBoolean(KEY_USE_FFC));
    }
  }
}