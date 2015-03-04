package com.re.reverb.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.re.reverb.R;
import com.re.reverb.androidBackend.Location;
import com.re.reverb.androidBackend.Reverb;
import com.re.reverb.androidBackend.errorHandling.NotSignedInException;
import com.re.reverb.androidBackend.post.dto.CreateReplyPostDto;
import com.re.reverb.network.PostManagerImpl;

public class CreateReplyPostActivity extends CreatePostActivity
{
    public static final String POST_ID_EXTRA = "POST_ID_EXTRA";
    private int postId = -1;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Bundle extras = getIntent().getExtras();
        if(extras != null && extras.getInt(POST_ID_EXTRA) > -1)
        {
            postId = extras.getInt(POST_ID_EXTRA);
        }
    }

    @Override
    public void submitPost(View view)
    {
        if(validatePost())
        {
            CreateReplyPostDto replyPostDto = buildPost();
            if(attachedPhoto.getDrawable() != null)
            {
                PostManagerImpl.submitReplyPost(replyPostDto, attachPhoto());
            }
            else
            {
                PostManagerImpl.submitReplyPost(replyPostDto);
            }
            postSubmitted = true;
            finishActivity();
        }
        else
        {
            postSubmitted = false;
        }
    }

    private CreateReplyPostDto buildPost()
    {
        TextView contentText = (TextView)findViewById(R.id.editPostTextContentView);
        String text = contentText.getText().toString();
        Location location = Reverb.getInstance().getCurrentLocation();

        CreateReplyPostDto replyPostDto;

        try
        {
            replyPostDto = new CreateReplyPostDto(Reverb.getInstance().getCurrentUserId(),
                    postId,
                    anonymous,
                    location.getLatitude(),
                    location.getLongitude(),
                    text);
        } catch (NotSignedInException e)
        {
            Toast.makeText(this, "Could not create reply post because " + e.getMessage(), Toast.LENGTH_SHORT).show();
            return null;
        }

        return replyPostDto;
    }
}
