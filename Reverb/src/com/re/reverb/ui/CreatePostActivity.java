package com.re.reverb.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.re.reverb.R;
import com.re.reverb.androidBackend.Location;
import com.re.reverb.androidBackend.Reverb;
import com.re.reverb.androidBackend.errorHandling.NotSignedInException;
import com.re.reverb.androidBackend.post.Post;
import com.re.reverb.androidBackend.post.dto.CreatePostDto;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CreatePostActivity extends Activity {

    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int CHARACTER_LIMIT = 1024;
    protected ImageView attachedPhoto;
    protected boolean postSubmitted = false;
    protected  String currentPhotoPath;
    //UserProfile user;

    Post post;
    protected boolean anonymous = true;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.slide_activity_up, R.anim.activity_stay);
        setContentView(R.layout.activity_create_post);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE|WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        attachedPhoto = (ImageView) findViewById(R.id.editPostIncludedImageView);
    }

    @Override
    public void finish() {
        super.finish();
        if(postSubmitted)
        {
            overridePendingTransition(R.anim.activity_stay, R.anim.slide_activity_up_out);
        }
        else
        {
            overridePendingTransition(R.anim.activity_stay, R.anim.slide_activity_down);
        }
    }

    public void submitPost(View view) {
        if(validatePost())
        {
            CreatePostDto postDto = buildPost();
            if(attachedPhoto.getDrawable() != null)
            {
                Reverb.getInstance().submitPost(postDto, attachPhoto());
            }
            else
            {
                Reverb.getInstance().submitPost(postDto);
            }
            postSubmitted = true;
            finishActivity();
        }
        else{
            postSubmitted = false;
        }
    }

    private CreatePostDto buildPost() {
        TextView contentText = (TextView)findViewById(R.id.editPostTextContentView);
        String text = contentText.getText().toString();// + " lat("+Reverb.getInstance().locationManager.getCurrentLatitude()+") long("+Reverb.getInstance().locationManager.getCurrentLongitude()+")";
        Location location = Reverb.getInstance().getCurrentLocation();

        CreatePostDto postDto;

        try
        {
            postDto = new CreatePostDto(Reverb.getInstance().getCurrentUserId(),
                    anonymous,
                    location.getLatitude(),
                    location.getLongitude(),
                    Reverb.getInstance().getRegionManager().getCurrentRegion().getRegionId(),
                    //new StandardPostContentDto(text));
                    text);
        } catch (NotSignedInException e)
        {
            Toast.makeText(this, "Could not create post because " + e.getMessage(), Toast.LENGTH_SHORT).show();
            return null;
        }

        return postDto;
    }

    protected boolean validatePost()
    {
        TextView contentText = (TextView)findViewById(R.id.editPostTextContentView);
        String text = contentText.getText().toString();
        if(text == null || text.replaceAll("\\s+","").length() == 0 || text.length() > CHARACTER_LIMIT) {
            return false;
        }
        return true;
    }

    public void toggleAnonymity(View view)
    {
        CheckBox anonymousCheckbox = (CheckBox)findViewById(R.id.anonymousCheckbox);
        anonymous = anonymousCheckbox.isChecked();

        if(anonymous)
        {
//            ((ImageView) findViewById(R.id.editPostProfilePicThumbnail)).setImageResource(R.drawable.anonymous_pp);
        }
        else
        {
            //TODO: Get profile picture from current user
//            ((ImageView) findViewById(R.id.editPostProfilePicThumbnail)).setImageResource(R.drawable.chris_pp);
        }
    }

    public void includePhoto(View view)
    {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                Log.d("Reverb", "Error creating image file");
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(photoFile));
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
//            Bundle extras = data.getExtras();
//            Bitmap imageBitmap = (Bitmap) extras.get("data");
//            attachedPhoto.setImageBitmap(imageBitmap);
            setPic();
        }
    }

    private void setPic() {
        // Get the dimensions of the View
        int targetW = attachedPhoto.getWidth();
//        int targetH = attachedPhoto.getHeight();

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(currentPhotoPath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
//        int scaleFactor = Math.min(photoW/targetW, photoH/targetH);
        int scaleFactor = photoW/targetW;

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(currentPhotoPath, bmOptions);
        if(bitmap != null)
        {
            attachedPhoto.setImageBitmap(bitmap);
        } else
        {
            Toast.makeText(this, "There was a problem attaching your photo", Toast.LENGTH_SHORT).show();
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "PNG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".png",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    protected void finishActivity(){
        Intent resultIntent = new Intent();
        resultIntent.putExtra("postSubmitted", postSubmitted);
        setResult(Activity.RESULT_OK, resultIntent);
        finish();
    }

    protected File attachPhoto()
    {
        File f = new File(getCacheDir(), "testImage");
        //Convert bitmap to byte array
        Bitmap bitmap = ((BitmapDrawable)attachedPhoto.getDrawable()).getBitmap();
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0 /*ignored for PNG*/, bos);
        byte[] bitmapdata = bos.toByteArray();

        //write the bytes in file
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(f);
            try {
                fos.write(bitmapdata);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return f;
    }
}