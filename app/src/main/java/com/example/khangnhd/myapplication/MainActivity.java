package com.example.khangnhd.myapplication;

import android.content.res.AssetManager;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.khangnhd.myapplication.model.Item;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class MainActivity extends AppCompatActivity {
    Item[][] boardGame;
    List<Item> itemList;
    String imgPathList[];

    private static final int MY_BUTTON = 9000;

    LinearLayout lnMain;

    //Size boardgame
    int m = 2, n = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lnMain = (LinearLayout) findViewById(R.id.lnMain);

        itemList = loadImagesFromAssets();

        chooseListItem();

    }

    /*
    Load image from assets into itemlist
    * */
    public List<Item> loadImagesFromAssets() {
        List<Item> list = new ArrayList<>();

        //Load image from assets into itemlist
        AssetManager assetManager = getAssets();
        try {
            imgPathList = assetManager.list("img");
            for (int i = 0; i < imgPathList.length; i++) {
                list.add(new Item(i + 1, imgPathList[i]));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    /*
        Choose List item
    * */
    public void chooseListItem() {
        boardGame = new Item[m][n];
        List<Item> itemListTemp = loadImagesFromAssets();
        ArrayList<Integer> arrayTempt = new ArrayList<>();
        int quantity = (m * n) / 2;

        for (int j = 0; j < m * n; j++) {
            arrayTempt.add(j);
        }

        for (int i = 0; i < quantity; i++) {
            Random rand = new Random();
            int index = rand.nextInt(itemListTemp.size()) + 0;
            Log.d("test", "index:" + itemListTemp.get(index).img);


            //Index1
            int index1 = rand.nextInt(arrayTempt.size());
            int gt1 = arrayTempt.get(index1);

            int x1 = gt1 / n;
            int y1 = gt1 % n;

            //gan vo board

            boardGame[x1][y1] = itemListTemp.get(index);
            arrayTempt.remove(index1);

            //Index2
            int index2 = rand.nextInt(arrayTempt.size());
            int gt2 = arrayTempt.get(index2);

            int x2 = gt2 / n;
            int y2 = gt2 % n;

            boardGame[x2][y2] = itemListTemp.get(index);

            //gan vo board
            arrayTempt.remove(index2);

            Log.d("test", "[x1,y1]=" + x1 + "," + y1);
            Log.d("test", "[x2,y2]=" + x2 + "," + y2);

            itemListTemp.remove(index);

        }
        //Load item into board game
        loadItemIntoBoardGame();
    }


    /*
    * Load item into board game
    * */
    public void loadItemIntoBoardGame() {
        //Display to board game
        for (int i = 0; i < m; i++) {
            LinearLayout lnRow = new LinearLayout(this);
            lnRow.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            lnRow.setOrientation(LinearLayout.HORIZONTAL);
            lnRow.setGravity(Gravity.CENTER);
            lnMain.addView(lnRow);

            for (int j = 0; j < n; j++) {
                final ImageButton imgButton = new ImageButton(this);

                final Item item = boardGame[i][j];
                loadImageUsingGlide("default.png", imgButton);

                LinearLayout.LayoutParams layoutparams = (LinearLayout.LayoutParams) imgButton.getLayoutParams();
                imgButton.setLayoutParams(new LinearLayout.LayoutParams(250, 250));
                imgButton.setScaleType(ImageView.ScaleType.FIT_XY);

                imgButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        loadImageUsingGlide(item.img, imgButton);
                        Toast.makeText(getApplicationContext(), "" + item.img, Toast.LENGTH_LONG).show();
                    }
                });
                lnRow.addView(imgButton);
            }

        }
    }

    /*
    * load image using Glide library
    * */
    public void loadImageUsingGlide(String imgName, ImageButton imgButton) {
        Glide.with(this)
                .load(Uri.parse("file:///android_asset/img/" + imgName))
                .override(200, 200)
                .into(imgButton);


    }
}
