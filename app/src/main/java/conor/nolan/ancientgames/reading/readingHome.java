package conor.nolan.ancientgames.reading;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import conor.nolan.ancientgames.R;

public class readingHome extends AppCompatActivity {
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reading_home);
        textView = (TextView) findViewById(R.id.textView);
        textView.setVisibility(View.VISIBLE);
        textView.setTextColor(Color.WHITE);
        textView.setTextSize(20);
        textView.bringToFront();
    }
}
