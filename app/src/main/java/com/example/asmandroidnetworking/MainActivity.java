package com.example.asmandroidnetworking;

import android.graphics.Bitmap;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.asmandroidnetworking.databinding.ActivityMainBinding;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;

    List<InterestingPhoto> interestingPhotoList;
    int currPhotoIndex = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);


        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Fetching next photo", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                nextPhoto();
            }
        });

       FlickrAPI flickrAPI = new FlickrAPI(this);
       flickrAPI.fetchInterstingPhotos();
    }

    public void receivedInterestingPhotos(List<InterestingPhoto> interestingPhotoList){
     this.interestingPhotoList= interestingPhotoList;
     nextPhoto();
    }

    private void nextPhoto() {
        if(interestingPhotoList != null &&  interestingPhotoList.size() >0){
            currPhotoIndex++;
            currPhotoIndex %= interestingPhotoList.size();
            TextView textView = findViewById(R.id.textViewTitle);
            TextView dateTakenTV = findViewById(R.id.dateTakenTv);

            InterestingPhoto interestingPhoto = interestingPhotoList.get(currPhotoIndex);
            textView.setText(interestingPhoto.getTitle());
            dateTakenTV.setText(interestingPhoto.getDatetaken());

            FlickrAPI flickrAPI = new FlickrAPI(this);
            flickrAPI.fetchPhototiBitmap(interestingPhoto.getPhotoURL());
        }
    }

    public void receivedPhotoBitmap(Bitmap bitmap){
        ImageView imageView = findViewById(R.id.imageView);
        imageView.setImageBitmap(bitmap);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}