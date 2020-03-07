package conor.nolan.ancientgames.SetUp;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import conor.nolan.ancientgames.MainActivity;
import conor.nolan.ancientgames.R;
import conor.nolan.ancientgames.onthisday.OnThisDay;

public class LoadingAnimation extends AppCompatActivity {

        Handler handler;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_loading_animation);

            handler=new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent=new Intent(LoadingAnimation.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            },3000);

        }
    }

