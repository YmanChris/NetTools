package com.jd.net.netlibrary;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.File;

/**
 * Created by yinxiangyang1 on 2017/8/27.
 */

public class UploadHelper {
    private String TAG = "UploadHelper";
    public static UploadHelper helper;
    public ResponseCallBack callBack;

    public static String SUCCESS_CODE = "1000";

    public static UploadHelper getInstance(){
        if(helper == null)
            helper = new UploadHelper();
        return helper;
    }

    public void setCallBack(ResponseCallBack callBack) {
        this.callBack = callBack;
    }

    public static void setSuccessCode(String successCode) {
        SUCCESS_CODE = successCode;
    }

    public void post(String postUrl, JSONObject json, ResponseCallBack callBack){
        post(postUrl , json , null , callBack);
    }

    public void post(final String postUrl, final JSONObject json , final File file , final ResponseCallBack callBack){
        AsyncTask asyncTask = new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] params) {
                request(postUrl , json , file , callBack);
                return null;
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
            }
        };
        asyncTask.execute((Object[]) null);
    }

    public void request(String url , JSONObject json , File file, ResponseCallBack callBack){
        try {
            MultipartEntity entity = new MultipartEntity();
            if(file != null){
                entity.addPart("img",new FileBody(file));
            }
            entity.addPart("jsonData",new StringBody(json.toString()));
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(url);
            httpPost.setEntity(entity);
            Log.i(TAG, "post总字节数:"+entity.getContentLength());

            HttpResponse httpResponse=httpClient.execute(httpPost);

            int code=httpResponse.getStatusLine().getStatusCode();

            if(code==200)

            {
                String result= EntityUtils.toString(httpResponse.getEntity());
                callBack.onSuccess(result);
                return;

            }
            else{
                String result= EntityUtils.toString(httpResponse.getEntity());
                callBack.onFailed(result);
                return;

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return;
    }

    public interface ResponseCallBack{
        void onSuccess(String msg);
        void onFailed(String msg);
    }
}
