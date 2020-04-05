package conor.nolan.ancientgames.SetUp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import java.util.ArrayList;


import conor.nolan.ancientgames.R;

public class RegistrationActivity extends AppCompatActivity  implements BackgroundRunner.OnMessageListener {

    private EditText email, username, password, confrimPassword;
    private ArrayList<ImageView> images = new ArrayList<>();
    private ImageView display;
    private ImageView background1;
    private ImageView background2;
    private ImageView background3;
    private ImageView background6;
    private ImageView background5;
    private ImageView background4;
    private ImageView background7;
    private Context context;
    private String msg;
    private int i =0;
    private Handler handler;
    private Animation animFadeIn,animFadeOut;
    private Button login;
    private Handler buttonClicked;
    private Handler regUnsuccessful;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        getWindow().getDecorView().setBackgroundColor(Color.BLACK);
        email = (EditText) findViewById(R.id.email);
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        login = (Button) findViewById(R.id.button4);
        login.setEnabled(true);
        login.setVisibility(View.VISIBLE);
        confrimPassword = (EditText) findViewById(R.id.confirmPassword);
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
        username.setVisibility(View.VISIBLE);
        password.setVisibility(View.VISIBLE);
        confrimPassword.setVisibility(View.VISIBLE);
        password.setVisibility(View.VISIBLE);
        context =this;
        go();
        buttonClicked = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message message) {
                login.setEnabled(false);
                login.setVisibility(View.INVISIBLE);
                username.setVisibility(View.INVISIBLE);
                password.setVisibility(View.INVISIBLE);
                confrimPassword.setVisibility(View.INVISIBLE);
                email.setVisibility(View.INVISIBLE);
            }
        };
        regUnsuccessful = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message message) {
                login.setEnabled(true);
                login.setVisibility(View.VISIBLE);
                username.setVisibility(View.VISIBLE);
                password.setVisibility(View.VISIBLE);
                confrimPassword.setVisibility(View.VISIBLE);
                email.setVisibility(View.VISIBLE);
            }
        };

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
                        email.bringToFront();
                        username.bringToFront();
                        password.bringToFront();
                        confrimPassword.bringToFront();
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

    public void onRegister(View view)
    {
        Message message = buttonClicked.obtainMessage();
        message.sendToTarget();
        String e = email.getText().toString();
        String u = username.getText().toString();
        String p = password.getText().toString();
        String pC = confrimPassword.getText().toString();
        String t = "register";
        MessageController register= new MessageController(context);
        register.register(t, u, p, e, pC);

    }
    @Override
    public void messageCallback(String response) {
        Log.i("MainActivity", "Response: " + response);
        System.out.println("Repsonse:    !!!   :   "+response);

        if(response=="Sign In activity successful") {
            finish();
        }
        else{

            Message message = regUnsuccessful.obtainMessage();
            message.sendToTarget();
        }
    }
}
