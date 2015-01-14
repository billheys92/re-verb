package com.re.reverb.androidBackend.post.content.dto;

public class StandardPostContentDto implements PostContentDto
{
    public String postBody;

    public StandardPostContentDto(String postBody)
    {
        this.postBody = postBody;
    }

    @Override
    public String getPostBody()
    {
        return postBody;
    }
}
