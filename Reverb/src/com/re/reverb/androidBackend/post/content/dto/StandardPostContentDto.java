package com.re.reverb.androidBackend.post.content.dto;

public class StandardPostContentDto implements PostContentDto
{
    public String Message_body;

    public StandardPostContentDto(String postBody)
    {
        this.Message_body = postBody;
    }

    @Override
    public String getPostBody()
    {
        return Message_body;
    }
}
