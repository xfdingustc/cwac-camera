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

package com.commonsware.cwac.camera.acl.demo;

import android.content.Context;
import android.os.Bundle;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.commonsware.cwac.camera.SimpleCameraHost;
import com.commonsware.cwac.camera.acl.CameraFragment;

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
  public void onCreate(Bundle state) {
    super.onCreate(state);

    setHasOptionsMenu(true);
    setHost(new DemoCameraHost(getActivity()));
  }

  @Override
  public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    inflater.inflate(R.menu.camera, menu);
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case R.id.camera:
        takePicture();
  
        return(true);
        
      case R.id.autofocus:
        autoFocus();

        return(true);
    }

    return(super.onOptionsItemSelected(item));
  }

  class DemoCameraHost extends SimpleCameraHost {
    public DemoCameraHost(Context _ctxt) {
      super(_ctxt);
    }

    @Override
    public boolean useFrontFacingCamera() {
      return(getArguments().getBoolean(KEY_USE_FFC));
    }
  }
}