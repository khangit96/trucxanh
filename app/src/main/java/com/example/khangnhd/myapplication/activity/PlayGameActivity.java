package com.example.khangnhd.myapplication.activity;

import android.content.res.AssetManager;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.SoundPool;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.ImageViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.akexorcist.roundcornerprogressbar.IconRoundCornerProgressBar;
import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;
import com.bumptech.glide.Glide;
import com.example.khangnhd.myapplication.R;
import com.example.khangnhd.myapplication.model.Item;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class PlayGameActivity extends AppCompatActivity {
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

    String imgPathDefault = "file:///android_asset/default/question1.png";
    String imgPath = "file:///android_asset/img/";
    boolean check = false;

    private Timer mTimer1;
    private TimerTask mTt1;
    int count = 0;
    private Handler mTimerHandler = new Handler();
    String imgNameClick = "";

    //Sound
    public AudioManager audioManager;
    private int soundIDGameOver;
    private int soundIDPause;
    private int soundIDChooseItem;
    private int soundIDSuccess;
    private SoundPool soundPool;
    private float maxVolume;
    private float actVolume;
    private float volume;

    //Timer play game
    private Timer timerPlaygame;
    private TimerTask timerTaskPlayGame;
    private float timeEscape;

    //Progressbar timer
    private RoundCornerProgressBar progressTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set fullscreen
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        startTimerPlaygame();

        lnMain = (LinearLayout) findViewById(R.id.lnMain);
        itemList = loadImagesFromAssets();
        chooseListItem();

        initSound();
    }

    /*
    * Init sound
    * */
    public void initSound() {
        // AudioManager audio settings for adjusting the volume
        audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        actVolume = (float) audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        maxVolume = (float) audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        volume = actVolume / maxVolume;

        //Hardware buttons setting to adjust the media sound
        this.setVolumeControlStream(AudioManager.STREAM_MUSIC);
        soundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
        //  soundIDSuccess = soundPool.load(this, R.raw.success, 1);
        //    soundIDGameOver = soundPool.load(this, R.raw.gover, 1);
        //   soundIDPause = soundPool.load(this, R.raw.pause, 1);
        soundIDChooseItem = soundPool.load(this, R.raw.choose_item, 1);
    }

    /*
    * Play sound
    * */
    public void playSound(int soundID) {
        soundPool.play(soundID, volume, volume, 1, 0, 1.0f);
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
                final Item item = boardGame[i][j];
                final ImageButton imgButton = new ImageButton(this);

                loadImageUsingGlide(imgPathDefault, imgButton);
                imgButton.setBackgroundResource(R.drawable.customize_image_button);
                imgButton.setLayoutParams(new LinearLayout.LayoutParams(210, 220));
                imgButton.setScaleType(ImageView.ScaleType.FIT_XY);
                imgButton.setId(j);
                imgButton.setPadding(10, 10, 10, 10);

                imgButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        playSound(soundIDChooseItem);

                        //Load image into image button
                        loadImageUsingGlide(imgPath + item.img, imgButton);

                        listRowClick.add(Integer.parseInt(lnRow.getTag().toString()));
                        listColumnClick.add(imgButton.getId());

                        countClickItem++;
                        imgNameClick = item.img;

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
                                startTimer();
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
    Load Image default into boardgame
    * */
    public void loadImageDefault() {
        for (int i = 0; i < listColumnClick.size(); i++) {
            ViewGroup row = lnMain.findViewWithTag(listRowClick.get(i));
            ImageButton imgBt = row.findViewById(listColumnClick.get(i));
            loadImageUsingGlide(imgPathDefault, imgBt);
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

    /*
    * Start timer play game
    * */
    private void startTimerPlaygame() {
        timeEscape = 0;
        progressTimer = (RoundCornerProgressBar) findViewById(R.id.progressTime);
        progressTimer.setProgressColor(Color.parseColor("#56d2c2"));

        progressTimer.setMax(100);
        timerPlaygame = new Timer();
        timerTaskPlayGame = new TimerTask() {
            public void run() {
                mTimerHandler.post(new Runnable() {
                    public void run() {
                        timeEscape += 1.666;
                        progressTimer.setProgress((float) 100 - timeEscape);
                        if (timeEscape >= 100) {
                            Toast.makeText(getApplicationContext(), "Game Over", Toast.LENGTH_LONG).show();
                            stopTimer(timerPlaygame);
                        }
                    }
                });
            }
        };

        timerPlaygame.schedule(timerTaskPlayGame, 1, 1000);
    }

    /*
    *Stop timer
    * */
    private void stopTimer(Timer timer) {
        if (timer != null) {
            timer.cancel();
            timer.purge();
        }
    }

    /*
    Start timer click item
    * */
    private void startTimer() {
        mTimer1 = new Timer();
        mTt1 = new TimerTask() {
            public void run() {
                mTimerHandler.post(new Runnable() {
                    public void run() {
                        count++;
                        if (count == 2) {
                            count = 0;
                            stopTimer(mTimer1);
                            if (imgNameClick.equals(imgName)) {
                                Toast.makeText(getApplicationContext(), "ok", Toast.LENGTH_LONG).show();

                                for (int i = 0; i < listColumnClick.size(); i++) {
                                    ViewGroup row = lnMain.findViewWithTag(listRowClick.get(i));
                                    View viewRemove = row.findViewById(listColumnClick.get(i));
                                    row.removeView(viewRemove);

                                }
                            } else {
                                loadImageDefault();
                            }

                            //Clear
                            listColumnClick.clear();
                            listRowClick.clear();
                        }
                    }
                });
            }
        };

        mTimer1.schedule(mTt1, 1, 300);
    }

//    private void threadMsg(String msg) {
//
//        if (!msg.equals(null) && !msg.equals("")) {
//            Message msgObj = handler.obtainMessage();
//            Bundle b = new Bundle();
//            b.putString("message", msg);
//            msgObj.setData(b);
//            handler.sendMessage(msgObj);
//        }
//    }

    // Define the Handler that receives messages from the thread and update the progress
//    private final Handler handler = new Handler() {
//
//        public void handleMessage(Message msg) {
//            check = true;
//            Toast.makeText(getApplicationContext(), "Received", Toast.LENGTH_LONG).show();
//
////            String aResponse = msg.getData().getString("message");
////
////            if ((null != aResponse)) {
////
////                // ALERT MESSAGE
////                Toast.makeText(
////                        getBaseContext(),
////                        "Server Response: "+aResponse,
////                        Toast.LENGTH_SHORT).show();
////            }
////            else
////            {
////
////                // ALERT MESSAGE
////                Toast.makeText(
////                        getBaseContext(),
////                        "Not Got Response From Server.",
////                        Toast.LENGTH_SHORT).show();
////            }
//
//        }
//    };

}
