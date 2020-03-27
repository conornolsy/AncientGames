package conor.nolan.ancientgames.quiz;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;

import conor.nolan.ancientgames.R;

public class QuizSelectMode extends AppCompatActivity {

    private Context context;
    private ArrayList<ImageView> images = new ArrayList<>();
    private ImageView background1;
    private ImageView background2;
    private ImageView background3;
    private ImageView background6;
    private ImageView background5;
    private ImageView background4;
    private ImageView background7;
    private TextView txt;
    private String msg;
    private int i =0;
    private Handler handler;
    private ImageView display;
    private Animation animFadeIn,animFadeOut;
    private Button learn;
    private Button comp;

    public QuizSelectMode(Context context) {
        this.context = context;
    }

    public QuizSelectMode()
    {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_select_mode);
        getWindow().getDecorView().setBackgroundColor(Color.BLACK);
        txt = (TextView) findViewById(R.id.textViewQ);
        txt.setTextColor(Color.WHITE);
        txt.setTextSize(20);
        comp = (Button) findViewById(R.id.compModeButton);
        learn = (Button) findViewById(R.id.learnModeButton);
        comp.setVisibility(View.VISIBLE);
        learn.setVisibility(View.VISIBLE);
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
                        txt.bringToFront();
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

                        }
                    });

                    try {
                        Thread.sleep(2000);
                        //   display.setVisibility(View.INVISIBLE);
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


    public void compQuizClicked(View view) {
        QuizSetUp quiz = new QuizSetUp(this,0);

    }

    public void learnQuizClicked(View view) {
        QuizSetUp quiz = new QuizSetUp(this,1);

    }
}
