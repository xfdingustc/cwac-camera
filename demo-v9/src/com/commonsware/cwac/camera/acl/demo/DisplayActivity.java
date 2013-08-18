package com.commonsware.cwac.camera.acl.demo;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;
import com.actionbarsherlock.app.SherlockActivity;

public class DisplayActivity extends SherlockActivity {
  static byte[] imageToShow=null;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    if (imageToShow == null) {
      Toast.makeText(this, R.string.no_image, Toast.LENGTH_LONG).show();
      finish();
    }
    else {
      ImageView iv=new ImageView(this);

      iv.setImageBitmap(BitmapFactory.decodeByteArray(imageToShow,
                                                      0,
                                                      imageToShow.length));
      imageToShow=null;

      iv.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
      setContentView(iv);
    }
  }
}
