package com.re.reverb.network;

import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.google.gson.Gson;
import com.re.reverb.androidBackend.Reverb;
import com.re.reverb.androidBackend.account.CreateUserDto;
import com.re.reverb.androidBackend.account.UserProfile;
import com.re.reverb.ui.SplashScreenActivity;

import org.json.JSONObject;

public class AccountManagerImpl extends PersistenceManagerImpl
{
    private static final String profilePictureURL = "https://www.googleapis.com/plus/v1/people/me?fields=image&key=";

    private static final String key = "AIzaSyCAxP36YmffW458--oYRGzeTNExcUwNOb4";

    public static void getUserExists(final String email, final String token, final SplashScreenActivity activity)
    {
        final String params = "?commandtype=get&command=getUserExistsByEmail";
        final String url = baseURL + params;

        Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>()
        {
            @Override
            public void onResponse(JSONObject response)
            {
                Gson gson = new Gson();
                UserExistsDto userExists = gson.fromJson(response.toString(), UserExistsDto.class);
                if(userExists.user_exists)
                {
                    System.out.println("User Exists:" + userExists.toString());
                    loginUser(email, token, activity);
                }
                else
                {
                    System.out.println("User Exists:" + userExists.toString());
                    activity.onUserDoesNotExist(email, token);
                    //TODO: prompt for user info
                        //Activity with form for inputing user info
                            //username, handle, description, picture?
                        //Once input, AccountManagerImpl.createUser(userProfile)
                }
            }
        };

        requestJson(listener, new UserExistsDto(email, true), Request.Method.POST, url);
    }

    public static class UserExistsDto
    {
        public final String Email;
        public final Boolean user_exists;

        public UserExistsDto(String Email, Boolean user_exists)
        {
            this.user_exists = user_exists;
            this.Email = Email;
        }
    }

    public static void createUser(final CreateUserDto createUserDto,
                                  final SplashScreenActivity activity)
    {
        final String params = "?commandtype=post&command=postUser";
        final String url = baseURL + params;

        Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>()
        {
            @Override
            public void onResponse(JSONObject response)
            {
                Gson gson = new Gson();
                UserExistsDto successfulCreated = gson.fromJson(response.toString(), UserExistsDto.class);
                if(successfulCreated.user_exists)
                {
                    loginUser(createUserDto.Email, createUserDto.Token, activity);
                    activity.removeOverlays();
                }
                else
                {
                    Toast.makeText(activity, "Provided handle is already taken.", Toast.LENGTH_SHORT).show();
                }
            }
        };

        requestJson(listener, createUserDto, Request.Method.POST, url);
    }

    public static void loginUser(final String email, final String token, final SplashScreenActivity activity)
    {
        final String params = "?commandtype=get&command=getLoginUser";
        final String url = baseURL + params;

        Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>()
        {
            @Override
            public void onResponse(JSONObject response)
            {
                //TODO: add handling for invalid token
                Gson gson = new Gson();
                UserProfile userProfile = gson.fromJson(response.toString(), UserProfile.class);
                userProfile.Token = token;
                Reverb.getInstance().signInUser(userProfile);
                getUserProfilePicture();
                activity.onScreenClick(null);
            }
        };

        requestJson(listener, new LoginUserDto(email,token), Request.Method.POST, url);
    }

    public static class LoginUserDto
    {
        private final String Email;
        private final String Token;

        public LoginUserDto(String Email, String Token)
        {
            this.Email = Email;
            this.Token = Token;
        }
    }

    public static void getUserProfilePicture()
    {
        final String url = profilePictureURL + key;

        Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>()
        {
            @Override
            public void onResponse(JSONObject response)
            {
                Gson gson = new Gson();
                String imageURL = gson.fromJson(response.toString(), String.class);
                //TODO: use the image url to fetch the image or send it to store in the database?
                System.out.println(imageURL);
            }
        };

        requestJson(listener, null, Request.Method.GET, url);
    }
}
