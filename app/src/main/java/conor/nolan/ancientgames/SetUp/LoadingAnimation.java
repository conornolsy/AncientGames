package conor.nolan.ancientgames.SetUp;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import conor.nolan.ancientgames.MainActivity;
import conor.nolan.ancientgames.R;

public class LoadingAnimation extends AppCompatActivity {
    private String username;
    private String password;
    private Handler handler;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_loading_animation);


            SharedPreferences pref = this.getSharedPreferences("UserData", 0); // 0 - for private mode


            if((!pref.contains("username") || (!pref.contains("password"))))
            {
                handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(LoadingAnimation.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }, 3000);
            }

            else
                {
                    username = pref.getString("username", null);
                    password = pref.getString("password", null);
                    System.out.println("Stored Username    "+username);
                    System.out.println("Stored Password    "+password);
                    BackgroundRunner backgroundRunner = new BackgroundRunner(this);
                    backgroundRunner.execute("auto_login",username,password);

            }
        }
    }

