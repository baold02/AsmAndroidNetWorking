package com.example.asmandroidnetworking;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.biometrics.BiometricManager;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class FlickrAPI {

    static final String BASE_URL = "https://api.flickr.com/services/rest";
    static final String API_KEY = "a011e75c0284b99c8a586c667e408e76";
    static final String TAG = "WebServicesFunTag";

    MainActivity mainActivity;

    public FlickrAPI(MainActivity mainActivity){
        this.mainActivity = mainActivity;
    }

    public void fetchInterstingPhotos(){
        String url  = contructInterestingPhotoListURL();
        //Log.d(TAG,"fetchInterstingPhotos:"+url);

        FetchInterestingPhotoListAsyncTask asyncTask = new FetchInterestingPhotoListAsyncTask();
        asyncTask.execute(url);

    }

    private String contructInterestingPhotoListURL() {
        String  url =   BASE_URL;
        url += "?method=flickr.interestingness.getList";
        url += "&api_key=" +API_KEY;
        url += "&format=json";
        url += "&nojsoncallback=1";
        url += "&extras=date_taken,url_h";

        return url;
    }

    class  FetchInterestingPhotoListAsyncTask extends AsyncTask<String, Void, List<InterestingPhoto>>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            ProgressBar progressBar =mainActivity.findViewById(R.id.progressBar);
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected List<InterestingPhoto> doInBackground(String... strings) {
            String url = strings[0];
            List<InterestingPhoto>  interestingPhotoList =  new ArrayList<>();
            try {
                URL urlObject = new URL(url);
                HttpURLConnection urlConnection =  (HttpURLConnection) urlObject.openConnection();

                String jsonResult = "";
                InputStream stream = urlConnection.getInputStream();
                InputStreamReader streamReader = new InputStreamReader(stream);
                int data  = streamReader.read();
                while (data != -1){
                    jsonResult += (char) data;
                    data = streamReader.read();
                }
                Log.d(TAG,"doInBackground"+jsonResult);
                JSONObject jsonObject = new JSONObject(jsonResult);
                JSONObject photosObject = jsonObject.getJSONObject("photos");
                JSONArray jsonArray = photosObject.getJSONArray("photo");
                for (int i=0;i<jsonArray.length();i++){
                    JSONObject singlePhotoObjet = jsonArray.getJSONObject(i);
                    InterestingPhoto interestingPhoto = parseInTerestingPhoto(singlePhotoObjet);
                    if (interestingPhoto != null){
                        interestingPhotoList.add(interestingPhoto);
                    }
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return interestingPhotoList;
        }
        private InterestingPhoto parseInTerestingPhoto(JSONObject singlePhotoObject){
            InterestingPhoto interestingPhoto = null;
            try {
                String id = singlePhotoObject.getString("id");
                String title = singlePhotoObject.getString("title");
                String dateTaken = singlePhotoObject.getString("datetaken");
                String photoURL = singlePhotoObject.getString("url_h");
                interestingPhoto = new InterestingPhoto(id,title,dateTaken,photoURL);
            }
            catch (JSONException e){

            }
            return interestingPhoto;
        }

        @Override
        protected void onPostExecute(List<InterestingPhoto> interestingPhotos) {
            super.onPostExecute(interestingPhotos);
            Log.d(TAG,"onPostExecute:"+ interestingPhotos.size());
            mainActivity.receivedInterestingPhotos(interestingPhotos);
            ProgressBar progressBar =mainActivity.findViewById(R.id.progressBar);
            progressBar.setVisibility(View.GONE);
        }
    }
    public void fetchPhototiBitmap(String photoURL){
        PhotoRequestAsyncTask asyncTask = new PhotoRequestAsyncTask();
        asyncTask.execute(photoURL);
    }

    class PhotoRequestAsyncTask extends AsyncTask<String,Void, Bitmap>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            ProgressBar progressBar =mainActivity.findViewById(R.id.progressBar);
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Bitmap doInBackground(String... strings) {
            Bitmap bitmap = null;
            try {
                URL url =new URL(strings[0]);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in =urlConnection.getInputStream();
                bitmap = BitmapFactory.decodeStream(in);


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            ProgressBar progressBar =mainActivity.findViewById(R.id.progressBar);
            progressBar.setVisibility(View.GONE);
            mainActivity.receivedPhotoBitmap(bitmap);
        }
    }
}
