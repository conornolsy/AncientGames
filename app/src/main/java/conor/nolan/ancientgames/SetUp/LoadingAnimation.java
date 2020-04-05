package conor.nolan.ancientgames.SetUp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import conor.nolan.ancientgames.R;

public class LoadingAnimation extends AppCompatActivity implements BackgroundRunner.OnMessageListener {
    private String username;
    private String password;
    private Handler handler;
    private String response;
    private Context context;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_loading_animation);
            tryAutoLogin();
        }


        public void tryAutoLogin()
        {
            SharedPreferences pref = this.getSharedPreferences("UserData", 0);

            if((!pref.contains("username") || (!pref.contains("password"))))
            {
                handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(LoadingAnimation.this, SignInActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }, 3000);
            }

            else
            {
                username = pref.getString("username", null);
                password = pref.getString("password", null);
                MessageController autoLogin= new MessageController(this);
                autoLogin.login("auto_login",username,password);
            }
        }

    @Override
    public void messageCallback(String response) {
            this.response = response;
           // System.out.println(response);

    }
}

