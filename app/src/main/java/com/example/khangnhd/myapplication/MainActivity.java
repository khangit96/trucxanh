package com.example.khangnhd.myapplication;

import android.content.res.AssetManager;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.khangnhd.myapplication.model.Item;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    Item[][] boardGame;
    List<Item> itemList;
    String imgPathList[];

    private static final int MY_BUTTON = 9000;

    LinearLayout lnMain;
    List<Integer> listRowClick;
    List<Integer> listColumnClick;

    //Size boardgame
    int m = 4, n = 4;

    int countClickItem = 0;
    String imgName = "";

    String imgPathDefault = "file:///android_asset/default/default.png";
    String imgPath = "file:///android_asset/img/";

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
            // Log.d("test", "index:" + itemListTemp.get(index).img);


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

//            Log.d("test", "[x1,y1]=" + x1 + "," + y1);
//            Log.d("test", "[x2,y2]=" + x2 + "," + y2);

            itemListTemp.remove(index);

        }
        //Load item into board game
        loadItemIntoBoardGame();
    }


    /*
    * Load item into board game
    * */
    public void loadItemIntoBoardGame() {
        listRowClick = new ArrayList<>();
        listColumnClick = new ArrayList<>();


        //Display to board game.
        // Create layout dynamic
        for (int i = 0; i < m; i++) {
            final LinearLayout lnRow = new LinearLayout(this);
            lnRow.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            lnRow.setOrientation(LinearLayout.HORIZONTAL);
            lnRow.setGravity(Gravity.CENTER);
            lnMain.addView(lnRow);
            lnRow.setTag(i);

            for (int j = 0; j < n; j++) {
                final ImageButton imgButton = new ImageButton(this);

                final Item item = boardGame[i][j];
                loadImageUsingGlide(imgPathDefault, imgButton);

                imgButton.setLayoutParams(new LinearLayout.LayoutParams(170, 170));

                imgButton.setScaleType(ImageView.ScaleType.FIT_XY);
                imgButton.setId(j);

                imgButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        //Load image into image button
                        loadImageUsingGlide(imgPath + item.img, imgButton);

                        listRowClick.add(Integer.parseInt(lnRow.getTag().toString()));
                        listColumnClick.add(imgButton.getId());

                        countClickItem++;

                        //Check click twice only
                        if (countClickItem == 2) {
                            //prevent one item click twice
                            if (imgButton.getId() == listColumnClick.get(0) && Integer.parseInt(lnRow.getTag().toString()) == listRowClick.get(0)) {
                                countClickItem = 1;
                                //Clear
                                listColumnClick.remove(1);
                                listRowClick.remove(1);
                                Toast.makeText(getApplicationContext(), "cannot click twice", Toast.LENGTH_LONG).show();
                            } else {
                                countClickItem = 0;
                                //Check if two item equal
                                if (item.img.equals(imgName)) {
                                    //  loadImageUsingGlide(imgPath + item.img, imgButton);
                                    Toast.makeText(getApplicationContext(), "ok", Toast.LENGTH_LONG).show();
//                                    try {
//                                        Thread.sleep(2000);
//                                    } catch (InterruptedException e) {
//                                        e.printStackTrace();
//                                    }
                                    for (int i = 0; i < listColumnClick.size(); i++) {
                                        ViewGroup row = lnMain.findViewWithTag(listRowClick.get(i));
                                        View viewRemove = row.findViewById(listColumnClick.get(i));
                                        row.removeView(viewRemove);

                                    }
                                }
                                //Set item default
                                else {

                                    for (int i = 0; i < listColumnClick.size(); i++) {
                                        ViewGroup row = lnMain.findViewWithTag(listRowClick.get(i));
                                        ImageButton imgBt = row.findViewById(listColumnClick.get(i));
                                        loadImageUsingGlide(imgPathDefault, imgBt);
                                    }
                                }
                                //Clear
                                listColumnClick.clear();
                                listRowClick.clear();
                            }


                        }
                        //Track one item before
                        else {
                            imgName = item.img;
                        }

                    }
                });
                lnRow.addView(imgButton);
            }

        }
    }

    /*
    * load image using Glide library
    * */
    public void loadImageUsingGlide(String imgPath, ImageButton imgButton) {
        Glide.with(this)
                .load(Uri.parse(imgPath))
                .override(200, 200)
                .into(imgButton);


    }
}
