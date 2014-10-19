package com.re.reverb.ui;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.re.reverb.R;
import com.re.reverb.androidBackend.Post;
import com.re.reverb.androidBackend.PostFactory;
import com.re.reverb.androidBackend.Reverb;
import com.re.reverb.androidBackend.errorHandling.NotSignedInException;

/**
 * Created by Bill on 2014-09-27.
 */
public class CreatePostActivity extends Activity {

    static final int REQUEST_IMAGE_CAPTURE = 1;
    ImageView attachedPhoto;
    boolean postSubmitted = false;
    //UserProfile user;

    Post post;
    private boolean anonymous = true;

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
        //user = Reverb.getInstance().getCurrentUser();
        fillInAutomaticFields();
        if(validatePost())
        {
            Post post = buildPost();
            Reverb.getInstance().submitPost(post);
            postSubmitted = true;
            finishActivity();
        }
        else{
            postSubmitted = false;
        }
    }

    private void fillInAutomaticFields() {

    }

    private Post buildPost() {
        Post post;
        TextView contentText = (TextView)findViewById(R.id.editPostTextContentView);
        String text = contentText.getText().toString();

        try {
            post = PostFactory.createPost(text,anonymous);
        } catch (NotSignedInException e) {
            Toast.makeText(this,"Could not create post",Toast.LENGTH_SHORT);
            return null;
        }
        return post;
    }

    private boolean validatePost() {
        boolean postValid = false;
        TextView contentText = (TextView)findViewById(R.id.editPostTextContentView);
        String text = contentText.getText().toString();
        if(text == null || text.replaceAll("\\s+","").length() == 0) {
            return false;
        }
        return true;
    }

    public void toggleAnonymity(View view)
    {
        CheckBox anonymousCheckbox = (CheckBox)findViewById(R.id.anonymousCheckbox);
        anonymous = anonymousCheckbox.isChecked();
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

    private void finishActivity(){
        Intent resultIntent = new Intent();
        resultIntent.putExtra("postSubmitted", postSubmitted);
        setResult(Activity.RESULT_OK, resultIntent);
        finish();
    }
}