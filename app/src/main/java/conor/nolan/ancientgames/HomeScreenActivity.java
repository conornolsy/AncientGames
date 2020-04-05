package conor.nolan.ancientgames;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import conor.nolan.ancientgames.onthisday.OnThisDay;
import conor.nolan.ancientgames.quiz.QuizSelectMode;
import conor.nolan.ancientgames.reading.readingHome;

public class HomeScreenActivity extends AppCompatActivity {
    private ImageView background1;
    private ImageView background2;
    private ImageView background3;
    private ImageView background6;
    private ImageView background5;
    private ImageView background4;
    private ImageView background7;
    private ImageView display;
    private TextView textView;
    private ArrayList<ImageView> images = new ArrayList<>();
    private String msg;
    private int i =0;
    private Handler handler;
    private Animation animFadeIn,animFadeOut;
    private Button reading;
    private Button onThisDay;
    private Button quiz;
    private Handler buttonClicked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        getWindow().getDecorView().setBackgroundColor(Color.BLACK);
        background1 = (ImageView) findViewById(R.id.background_sulla);
        background1.setVisibility(View.VISIBLE);
        background2 = (ImageView) findViewById(R.id.background_hannibal);
        background2.setVisibility(View.INVISIBLE);
        background3 = (ImageView) findViewById(R.id.background_scipio);
        background3.setVisibility(View.INVISIBLE);
        background4 = (ImageView) findViewById(R.id.background_alexander);
        background4.setVisibility(View.INVISIBLE);
        background5 = (ImageView) findViewById(R.id.background_cyrus);
        background5.setVisibility(View.INVISIBLE);
        background6 = (ImageView) findViewById(R.id.background_leonidas);
        background6.setVisibility(View.INVISIBLE);
        background7 = (ImageView) findViewById(R.id.background_thucydides);
        background7.setVisibility(View.INVISIBLE);
        textView = (TextView) findViewById(R.id.textViewQ);
        textView.setVisibility(View.VISIBLE);
        textView.setTextColor(Color.WHITE);
        textView.setTextSize(20);
        images.add(background1);
        images.add(background2);
        images.add(background3);
        images.add(background4);
        images.add(background5);
        images.add(background6);
        images.add(background7);
        go();

    }

    private void go() {

        handler = new Handler();
        new Thread(new Runnable() {
            @Override
            public void run() {
                background1.setVisibility(View.INVISIBLE);
                int sleep =0;
                while(true) {
                    try {
                        Thread.sleep(sleep);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        msg = e.getMessage();
                    } catch (Exception e) {
                        e.printStackTrace();
                        msg = e.getMessage();
                    }

                    display = images.get(i);
                    animFadeIn = AnimationUtils.loadAnimation(getApplicationContext(),
                            R.anim.fade_in);
                    animFadeOut = AnimationUtils.loadAnimation(getApplicationContext(),
                            R.anim.fade_out);


                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            display.startAnimation(animFadeIn);
                            textView.bringToFront();

                        }
                    });

                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        msg = e.getMessage();
                    } catch (Exception e) {
                        e.printStackTrace();
                        msg = e.getMessage();
                    }

                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            display.startAnimation(animFadeOut);
                        }
                    });

                    if(i==images.size()-1) {i=0;}
                    else{ i++;}
                    sleep=1500;
                }
            }
        }).start();
    }


    public void quizClicked(View view) {
        Intent intent = new Intent(this, QuizSelectMode.class);
        startActivity(intent);

    }

    public void readingClick(View view) {
        Intent intent = new Intent(this, readingHome.class);
        startActivity(intent);
    }

    public void onThisDayClicked(View view) {
        Intent intent = new Intent(this, OnThisDay.class);
        startActivity(intent);
    }

}