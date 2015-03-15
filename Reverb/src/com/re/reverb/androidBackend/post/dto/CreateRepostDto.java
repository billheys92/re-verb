package com.re.reverb.androidBackend.post.dto;

/**
 * Created by Jake on 08/03/2015.
 */
public class CreateRepostDto extends CreatePostDto
{
    public int Repost_link; //message id of the post being replied to

    public CreateRepostDto(int userId,
                              int messageId,
                              boolean anonymous,
                              double latitude,
                              double longitude,
                              int regionId,
                              String content)
    {
        super(userId, anonymous, latitude, longitude, regionId, content);
        this.Repost_link = messageId;
    }
}
