package com.re.reverb.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;

import com.re.reverb.R;

/**
 * Created by Bill on 2014-09-27.
 */
public class CreatePostActivity extends Activity {

    static final int REQUEST_IMAGE_CAPTURE = 1;
    ImageView attachedPhoto;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);
        attachedPhoto = (ImageView) findViewById(R.id.postAttachedImage);
    }

    public void includePhoto(View view)
    {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            attachedPhoto.setImageBitmap(imageBitmap);
        }
    }
}