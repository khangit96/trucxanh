package com.example.khangnhd.myapplication;

import android.content.res.AssetManager;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
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
    ImageView img;

    Item[][] boardGame;
    List<Item> itemList;
    String imgPathList[];
    Integer[] listColIndex;
    Integer[] listRowIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Init control
        img = findViewById(R.id.img);

        itemList = loadImages();
        chooseListItem(2, 2);

//        listColIndex = new Integer[2];
//        listRowIndex = new Integer[2];
//
//        int countCol = 0;
//
//        boardGame = new Item[2][2];
//
//        for (int i = 0; i < 2; i++) {
//            int count = 1;
//            Item item = itemList[i];
//
//            while (count <= 2) {
//                //Random col index
//                int col = (int) (Math.random() * 2 + 0);
//
//                if (listColIndex.length == 0) {
//                    listColIndex[countCol] = col;
//                    countCol++;
//                } else {
//                    while (contains(listColIndex, col)) {
//                        col = (int) (Math.random() * 2 + 0);
//                    }
//                    countCol++;
//                    listColIndex[countCol] = col;
//                }
//
//                Log.d("test", "col:" + col);
//
//
////                //Push col into listColIndex
////
//
//
////                int row = (int) (Math.random() * 2 + 0);
////                boardGame[row][col] = item;
////
////
////                Log.d("test", "[" + row + "]" + "[" + col + "]" + boardGame[row][col].img);
//                count++;
//            }
//        }
//
//
//        Glide.with(this)
//                .load(Uri.parse("file:///android_asset/img/pikachu.png"))
//                .into(img);

    }

    /*
    Load image from assets into itemlist
    * */
    public List<Item> loadImages() {
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
    * */
    public void level(int n, int m) {

    }

    /*
        Choose List item
    * */
    public void chooseListItem(int m, int n) {
        List<Item> itemListTemp = loadImages();
        ArrayList<Integer> arrayTempt = new ArrayList<>();
        int quantity = (m * n) / 2;

        int len = m * n;
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
            arrayTempt.remove(index1);
//
//            //Index2
            int index2 = rand.nextInt(arrayTempt.size());
            int gt2 = arrayTempt.get(index2);

            int x2 = gt2 / n;
            int y2 = gt2 % n;
            //gan vo board
            arrayTempt.remove(index2);

            Log.d("test", "[x1,y1]=" + x1 + "," + y1);
            Log.d("test", "[x2,y2]=" + x2 + "," + y2);
            itemListTemp.remove(index);

        }


    }

    /*
    Check if exist value in array interger
    * */
    public static boolean contains(Integer[] arr, int item) {
        List<Integer> list = Arrays.asList(arr);
        Set<Integer> set = new HashSet<Integer>(list);
        return set.contains(item);
    }
}
