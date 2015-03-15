package com.re.reverb.network;

import android.app.Activity;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.re.reverb.androidBackend.Reverb;
import com.re.reverb.androidBackend.account.CreateUserDto;
import com.re.reverb.androidBackend.account.UpdateAboutMeDto;
import com.re.reverb.androidBackend.account.UpdateHandleDto;
import com.re.reverb.androidBackend.account.UpdateProfilePictureDto;
import com.re.reverb.androidBackend.account.UpdateUsernameDto;
import com.re.reverb.androidBackend.account.UserProfile;
import com.re.reverb.ui.SplashScreenActivity;

import org.json.JSONObject;

import java.io.File;

public class AccountManagerImpl extends PersistenceManagerImpl
{
    private static final String profilePictureURL = "https://www.googleapis.com/plus/v1/people/christopher.howse@gmail.com?fields=image&key=";

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
                if( userExists.user_exists)
                {
                    System.out.println("User Exists:" + userExists.toString());
                    loginUser(email, "", activity);
                }
                else
                {
                    System.out.println("User Exists:" + userExists.toString());
                    activity.onUserDoesNotExist(email, "");
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
                    loginUser(createUserDto.Email, "", activity);
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

    public static void createUser(final CreateUserDto createUserDto, final SplashScreenActivity activity, File image)
    {
        RequestQueue queue = RequestQueueSingleton.getInstance().getRequestQueue();

        MultipartRequest multiRequest = new MultipartRequest(uploadImageURL, image, "", new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                // public void onResponse(String response) {
                // Display the response string.
                System.out.println("Picture Sent!");
                if (response.contains("Error"))
                {
                    System.out.println("Picture Response Error is: "+ response);
                }
                else
                {
                    createUserDto.Profile_picture = response;
                    createUser(createUserDto, activity);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                System.out.println("Error Sending Profile Picture");
            }
        });

        // Add the request to the RequestQueue.
        queue.add(multiRequest);

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
                //getUserProfilePicture();
                activity.onScreenClick(null);
            }
        };

        requestJson(listener, new LoginUserDto(email,""), Request.Method.POST, url);
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

    public static void updateUsername(UpdateUsernameDto updateUsernameDto)
    {
        final String url = baseURL + "?commandtype=put&command=updateUserName";
        requestJson(updateUsernameDto, Request.Method.PUT, url);
    }

    public static void updateHandle(final Activity activity, UpdateHandleDto updateHandleDto)
    {
        final String url = baseURL + "?commandtype=put&command=updateUserHandle";
        requestJson(updateHandleDto, Request.Method.PUT, url);
    }

    public static void updateAboutMe(UpdateAboutMeDto updateAboutMeDto)
    {
        final String url = baseURL + "?commandtype=put&command=updateUserAboutMe";
        requestJson(updateAboutMeDto, Request.Method.PUT, url);
    }

    public static void updateProfilePicture(final UpdateProfilePictureDto updateProfilePictureDto, File image)
    {
        RequestQueue queue = RequestQueueSingleton.getInstance().getRequestQueue();

        MultipartRequest multiRequest = new MultipartRequest(uploadImageURL, image, "", new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                // public void onResponse(String response) {
                // Display the response string.
                System.out.println("Picture Sent!");
                if (response.contains("Error"))
                {
                    System.out.println("Picture Response Error is: "+ response);
                }
                else
                {
                    updateProfilePictureDto.Profile_picture = response;
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                System.out.println("Error Sending Profile Picture");
            }
        });

        // Add the request to the RequestQueue.
        queue.add(multiRequest);

    }
}
