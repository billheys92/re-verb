package com.re.reverb.androidBackend.post.content.dto;

import android.graphics.Bitmap;

public class PhotoPostContentDto implements PostContentDto
{
    public String postBody;
    public Bitmap photo;

    @Override
    public String getPostBody()
    {
        return postBody;
    }
}
