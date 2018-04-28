package com.example.khangnhd.myapplication;

import android.content.res.AssetManager;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    ImageView img;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        img=findViewById(R.id.img);
        String[] imgPath =null;
        AssetManager assetManager = getAssets();
        try {
            imgPath = assetManager.list("img");
            Toast.makeText(getApplicationContext(),""+imgPath[1],Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
        }


        Glide.with(this)
                .load(Uri.parse("file:///android_asset/img/pikachu.png"))
                .into(img);
    }
}
