package conor.nolan.ancientgames.quiz;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import java.util.ArrayList;
import conor.nolan.ancientgames.R;

public class QuizGame extends AppCompatActivity{
    private ArrayList<Question> questions;
    private TextView questionNo;
    private TextView question;
    private Button optionA;
    private Button optionB;
    private Button optionC;
    private Button optionD;
    private String correctAnswer;
    private AlertDialog alertDialog;
    private Context context;
    private int points = 0;
    private TextView pointsView;
    private long startTime;
    private boolean running = true;
    private Handler handlerTimeUp;
    private Handler handlerGetResults;
    private int numCorrect=0;

    public QuizGame() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_game);
        getWindow().getDecorView().setBackgroundColor(Color.BLACK);
        questions = new ArrayList<Question>();
        questions = getIntent().getParcelableArrayListExtra("questionsArr");
        pointsView = (TextView) findViewById(R.id.points);
        questionNo = (TextView) findViewById(R.id.question_number);
        question = (TextView) findViewById(R.id.question);
        optionA = (Button) findViewById(R.id.buttonA);
        optionB = (Button) findViewById(R.id.buttonB);
        optionC = (Button) findViewById(R.id.buttonC);
        optionD = (Button) findViewById(R.id.buttonD);
        context = this;
        handlerTimeUp = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message message) {
                alertDialog = new AlertDialog.Builder(context).create();
                alertDialog.setTitle("Out of Time!");
                alertDialog.show();
            }
        };

        handlerGetResults = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message message) {
                Intent intent = new Intent(context, QuizResults.class).putParcelableArrayListExtra("questionsArr", questions);
                intent.putExtra("points", points);
                intent.putExtra("numberCorrect",numCorrect);
                startActivity(intent);
            }
        };

        Task task = new Task(this);
        task.start();

    }

    public void startRound(int j) {
        pointsView.setText("Points: " + points);
        questionNo.setText("Question " + j);
        question.setText(questions.get(j).getQuestion());
        optionA.setText(questions.get(j).getOption_A());
        optionB.setText(questions.get(j).getOption_B());
        optionC.setText(questions.get(j).getOption_C());
        optionD.setText(questions.get(j).getOption_D());
        correctAnswer = questions.get(j).getCorrect_Answer();
    }

private class Task extends Thread implements View.OnClickListener
{
    private QuizGame view;

    public Task(QuizGame view)
    {
        this.view = view;
    }


    @Override
    public void run() {

       for(int i =0; i<questions.size(); i++)
       {
           startTime = System.currentTimeMillis();
           running=true;
           startRound(i);
           optionA.setOnClickListener(this);
           optionB.setOnClickListener(this);
           optionC.setOnClickListener(this);
           optionD.setOnClickListener(this);

           while (running) {

               if((System.currentTimeMillis()-startTime) > 10000)
               {
                   running =false;
                   Message message = handlerTimeUp.obtainMessage();
                   message.sendToTarget();
               }


           }

       }

        Message message = handlerGetResults.obtainMessage();
        message.sendToTarget();

       }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.buttonA:
                if (correctAnswer.equals("B")) {
                    points += (int)(System.currentTimeMillis()-startTime);
                    numCorrect++;
                    running = false;
                    break;
                } else {
                    alertDialog = new AlertDialog.Builder(context).create();
                    alertDialog.setTitle("Incorrect! The correct answer is: " + optionA);
                    running = false;
                    break;
                }

            case R.id.buttonB:
                if (correctAnswer.equals("B")) {
                    points += (int)(System.currentTimeMillis()-startTime);
                    numCorrect++;
                    running = false;
                    break;
                } else {
                    running = false;
                    break;
                }

            case R.id.buttonC:
                if (correctAnswer.equals("C")) {
                    points += (int)(System.currentTimeMillis()-startTime);
                    numCorrect++;
                    running = false;
                    break;
                } else {
                    running = false;
                    break;
                }

            case R.id.buttonD:
                if (correctAnswer.equals("D")) {
                    points += (int)(System.currentTimeMillis()-startTime);
                    numCorrect++;
                    running = false;
                    break;
                } else {
                  //  alertDialog = new AlertDialog.Builder(context).create();
                  //  alertDialog.setTitle("Incorrect! The correct answer is: " + optionD);
                    running = false;
                    break;
                }


        }

    }
}
}
