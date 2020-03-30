package conor.nolan.ancientgames.quiz.LearningMode;

import androidx.appcompat.app.AppCompatActivity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import java.util.ArrayList;
import conor.nolan.ancientgames.R;
import conor.nolan.ancientgames.quiz.Question;
import conor.nolan.ancientgames.quiz.QuizResults;

public class QuizGameLearnMode extends AppCompatActivity {
    private ArrayList<Question> questions;
    private ArrayList<Facts> facts;
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
    private Handler delay;
    private Handler handlerGetResults;
    private int numCorrect=0;
    private int qNo;
    private boolean answerCorrect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_game_learn_mode);
        getWindow().getDecorView().setBackgroundColor(Color.BLACK);
        questions = new ArrayList<Question>();
        questions = getIntent().getParcelableArrayListExtra("questionsArr");
        facts = getIntent().getParcelableArrayListExtra("factsArr");
        pointsView = (TextView) findViewById(R.id.points);
        pointsView.setText("");
        questionNo = (TextView) findViewById(R.id.question_number);
        question = (TextView) findViewById(R.id.question);
        optionA = (Button) findViewById(R.id.buttonA);
        optionB = (Button) findViewById(R.id.buttonB);
        optionC = (Button) findViewById(R.id.buttonC);
        optionD = (Button) findViewById(R.id.buttonD);
        context =this;
        qNo=0;
        QGLearningThread qThread = new QGLearningThread(this);
        qThread.start();

        handlerGetResults = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message message) {
                Intent intent = new Intent(context, QuizResults.class).putParcelableArrayListExtra("questionsArr", questions);
                intent.putExtra("points", points);
                intent.putExtra("numberCorrect",numCorrect);
                startActivity(intent);
                finish();
            }
        };

    }

    public void startRound(int j) {
        if(j>=1) {
            pointsView.setText(points + "/" + j + " correct");
        }
        qNo++;
        questionNo.setText("Question " + qNo);
        question.setText(questions.get(j).getQuestion());
        optionA.setText(questions.get(j).getOption_A());
        optionB.setText(questions.get(j).getOption_B());
        optionC.setText(questions.get(j).getOption_C());
        optionD.setText(questions.get(j).getOption_D());
        correctAnswer = questions.get(j).getCorrect_Answer();
    }




    private class QGLearningThread extends Thread implements View.OnClickListener
    {
        private QuizGameLearnMode view;

        public QGLearningThread(QuizGameLearnMode view)
        {
            this.view = view;
        }


        @Override
        public void run() {

            for(int i =0; i<questions.size(); i++)
            {
                running=true;
                startRound(i);
                optionA.setOnClickListener(this);
                optionB.setOnClickListener(this);
                optionC.setOnClickListener(this);
                optionD.setOnClickListener(this);
                final String fact = facts.get(i).getFact();
                final String correctOption = questions.get(i).getCorrect_Answer();
                final String answer;
                if(correctOption.equals("A"))
                    answer=questions.get(i).getOption_A();
                else  if(correctOption.equals("B"))
                    answer=questions.get(i).getOption_B();
                else  if(correctOption.equals("C"))
                    answer=questions.get(i).getOption_C();
                else
                    answer=questions.get(i).getOption_D();


                while (running) {


                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(answerCorrect) {
                            alertDialog = new AlertDialog.Builder(context).create();
                            alertDialog.setTitle("Correct!");
                            alertDialog.setMessage("Did you Know: "+fact);
                            alertDialog.show();
                        }

                        else {
                            alertDialog = new AlertDialog.Builder(context).create();
                            alertDialog.setTitle("Incorrect!");
                            alertDialog.setMessage("Correct Answer: "+ answer+"\nDid you Know: "+fact);
                            alertDialog.show();
                        }
                    }
                });

            }
            delay = new Handler(Looper.getMainLooper());
            delay.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Message message = handlerGetResults.obtainMessage();
                    message.sendToTarget();
                }
            }, 3000);


        }

        @Override
        public void onClick(View v) {

            switch (v.getId()) {
                case R.id.buttonA:
                    if (correctAnswer.equals("A")) {
                        points += 1;
                        numCorrect++;
                        answerCorrect=true;
                        running = false;
                        break;
                    } else {
                        answerCorrect=false;
                        running = false;
                        break;
                    }

                case R.id.buttonB:
                    if (correctAnswer.equals("B")) {
                        points += 1;
                        numCorrect++;
                        answerCorrect=true;
                        running = false;
                        break;
                    } else {
                        answerCorrect=false;
                        running = false;
                        break;
                    }

                case R.id.buttonC:
                    if (correctAnswer.equals("C")) {
                        points += 1;
                        numCorrect++;
                        answerCorrect=true;
                        running = false;
                        break;
                    } else {
                        answerCorrect=false;
                        running = false;
                        break;
                    }

                case R.id.buttonD:
                    if (correctAnswer.equals("D")) {
                        points += 1;
                        numCorrect++;
                        answerCorrect=true;
                        running = false;
                        break;
                    } else {
                        answerCorrect=false;
                        running = false;
                        break;
                    }


            }

        }
    }
}
