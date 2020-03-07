package conor.nolan.ancientgames.quiz;

import android.content.Context;
import android.content.Intent;

import java.util.ArrayList;

public class QuizSetUp {

    private int q_number;

    private ArrayList<Integer> usedQuestions = new ArrayList<Integer>();
    private Context context;

    public QuizSetUp(Context context) {
        this.context = context;
        setUp();

    }

    public void setUp()
    {

    while(usedQuestions.size()<= 2) {
        q_number = getRandom();
        if (!usedQuestions.contains(q_number)) {
            usedQuestions.add(q_number);
        }

    }

        //String q_no = String.valueOf(q_number);
        QuizBackgroundRunner quizBackgroundRunner = new QuizBackgroundRunner(context);
        quizBackgroundRunner.execute(usedQuestions);



}


    public int getRandom()
    {
        double x = (Math.random() * ((4 - 1) + 1)) + 1;
        int y = (int)x;
        return y;
    }

}
