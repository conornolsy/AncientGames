package conor.nolan.ancientgames.quiz;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import java.util.ArrayList;
import conor.nolan.ancientgames.R;

public class QuizResults extends AppCompatActivity {

    private int numberCorrect;
    private ArrayList<Question> questions;
    private int points;
    private TextView results;
    private Context context;
    private int highscore;
    private String username;

    public QuizResults()
    {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_results);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        context=this;
        questions = getIntent().getParcelableArrayListExtra("questionsArr");
        points = getIntent().getIntExtra("points",0);
        numberCorrect = getIntent().getIntExtra("numberCorrect",0);
        results = (TextView) findViewById(R.id.q_result);
        results.append("Total Points: "+points+"\n");
        results.append(numberCorrect+" out of 10 questions answered correctly\n\n");
        int i =1;
        for(Question question: questions)
        {
            results.append("Question number "+i+": ");
            results.append(question.getQuestion()+"\n");
            results.append("Answer: ");
            String answer = question.getCorrect_Answer();
            if(answer.equals("A")) {
                results.append(question.getOption_A() + "\n");
            }
            else if(answer.equals("B")) {
                results.append(question.getOption_B() + "\n");
            }
            else if(answer.equals("C")) {
                results.append(question.getOption_C() + "\n");
            }
            else if(answer.equals("D")) {
                results.append(question.getOption_D() + "\n");
            }

            else
            {
                results.append("Error");
            }

            i++;
        }


        SharedPreferences pref = this.getSharedPreferences("UserData", 0);
        highscore = pref.getInt("highScore", -1); // getting Integer
        username = pref.getString("username", null);
        if(points > highscore)
        {
            UpdateHighScore updateHighScore = new UpdateHighScore(this);
            updateHighScore.execute(username,Integer.toString(points));
        }




        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }
}
