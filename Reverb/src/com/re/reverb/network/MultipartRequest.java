package com.re.reverb.network;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;

import org.apache.http.HttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class MultipartRequest extends Request<String> {
    public static final String KEY_PICTURE = "mypicture";
    public static final String KEY_PICTURE_NAME = "filename";
    public static final String KEY_ROUTE_ID = "route_id";

    private HttpEntity mHttpEntity;

    private String mRouteId;
    private Response.Listener mListener;

    public MultipartRequest(String url, String filePath, String routeId,
                            Response.Listener<String> listener,
                            Response.ErrorListener errorListener) {
        super(Method.POST, url, errorListener);

        mRouteId = routeId;
        mListener = listener;
        mHttpEntity = buildMultipartEntity(filePath);
    }

    public MultipartRequest(String url, File file, String routeId,
                            Response.Listener<String> listener,
                            Response.ErrorListener errorListener) {
        super(Method.POST, url, errorListener);

        mRouteId = routeId;
        mListener = listener;
        mHttpEntity = buildMultipartEntity(file);
    }

    private HttpEntity buildMultipartEntity(String filePath) {
        File file = new File(filePath);
        return buildMultipartEntity(file);
    }

    private HttpEntity buildMultipartEntity(File file) {
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        String fileName = file.getName();
        FileBody fileBody = new FileBody(file);
        System.out.println("File Total Space: " + file.getTotalSpace());
        System.out.println("File Length: " + file.length());
        System.out.println("File Name: " + file.getPath());
        //builder.addPart(KEY_PICTURE, fileBody);
        //builder.addTextBody(KEY_PICTURE_NAME, fileName);
        //builder.addTextBody(KEY_PICTURE_NAME, "mypicture");
        //builder.addTextBody(KEY_ROUTE_ID, mRouteId);
        //builder.addBinaryBody(KEY_PICTURE, file, ContentType.create("image/jpeg"), fileName);
        builder.addBinaryBody(KEY_PICTURE, file, ContentType.create("image/jpeg"), fileName);
        return builder.build();
    }

    @Override
    public String getBodyContentType() {
        return mHttpEntity.getContentType().getValue();
    }

    @Override
    public byte[] getBody() throws AuthFailureError {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            mHttpEntity.writeTo(bos);
        } catch (IOException e) {
            VolleyLog.e("IOException writing to ByteArrayOutputStream");
        }
        return bos.toByteArray();
    }

    @Override
    protected Response<String> parseNetworkResponse(NetworkResponse response) {
        //return Response.success("Uploaded", getCacheEntry());
        String testString = "Failed";
        try {
            testString = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return Response.success(testString, getCacheEntry());
    }

    @Override
    protected void deliverResponse(String response) {
        mListener.onResponse(response);
    }
}