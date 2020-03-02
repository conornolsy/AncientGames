package conor.nolan.ancientgames.quiz;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import java.sql.*;


import conor.nolan.ancientgames.MainActivity;
import conor.nolan.ancientgames.R;

public class quizHome extends AppCompatActivity {
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_home);
        textView = (TextView) findViewById(R.id.textView);
        textView.setVisibility(View.VISIBLE);
        textView.setTextColor(Color.WHITE);
        textView.setTextSize(20);
        textView.bringToFront();
    }


    public void quizClicked(View view) {
        Intent intent = new Intent(this, quizHome.class);
        startActivity(intent);

    }
}


