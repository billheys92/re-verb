package com.re.reverb.androidBackend.post.dto;

public class CreateReplyPostDto extends CreatePostDto
{
    public int Reply_link; //message id of the post being replied to

    public CreateReplyPostDto(int userId,
                         int messageId,
                         boolean anonymous,
                         double latitude,
                         double longitude,
                         int regionId,
                         String content)
    {
        super(userId, anonymous, latitude, longitude, regionId, content);
        this.Reply_link = messageId;
    }
}
