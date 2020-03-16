package conor.nolan.ancientgames.quiz;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

public class QuizSetUp {

    private int q_number;
    private int gameMode;
    private String countQsURL = "https://danu6.it.nuigalway.ie/ancientgames/countQuestions.php";
    private ArrayList<Integer> usedQuestions = new ArrayList<Integer>();
    private Context context;
    private int totalNumberQs;
    private EditText editText;
    private TextView textView;

    public QuizSetUp(Context context, int gameMode) {
        this.context = context;
        this.gameMode = gameMode;
        if(gameMode==0) {
            setUpCompQuiz();
        }
        else if(gameMode ==1) {
        setUpLearnQuiz();
        }

    }

    public void setUpCompQuiz()
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


    public void setUpLearnQuiz()
    {
        CountQuestions countQuestions = new CountQuestions(context);
        countQuestions.execute();

    }


    public int getRandom()
    {
        double x = (Math.random() * ((4 - 1) + 1)) + 1;
        int y = (int)x;
        return y;
    }

    private class CountQuestions extends AsyncTask<Void, String, String> {

        private AlertDialog alertDialog;
        private String numberOfQuestions;
        private Context context;

        public CountQuestions(Context context)
        {
            this.context = context;
        }

        @Override
        protected String doInBackground(Void... voids) {
            try {
                URL url = new URL(countQsURL);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader((new InputStreamReader(inputStream, "iso-8859-1")));
                String result = "";
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    result += line;
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                totalNumberQs = Integer.parseInt(result);
                return result;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return "Error, Questions not available";
        }

        @Override
        protected void onPreExecute() {
            // super.onPreExecute();
            alertDialog = new AlertDialog.Builder(context).create();
            alertDialog.setTitle("Loading...");
            alertDialog.setMessage("Getting total number of available questions.");
            alertDialog.show();

        }


        @Override
        protected void onPostExecute(final String result) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("No. of Available questions: "+ result);
            builder.setMessage("Enter Number of Questions you'd like to be tested on between 1 and "+result);

// Set up the input
            final EditText input = new EditText(context);
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
            input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            builder.setView(input);

// Set up the buttons
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    alertDialog.hide();
                    numberOfQuestions = input.getText().toString();
                    if(Integer.parseInt(numberOfQuestions) <= Integer.parseInt(result))
                    {
                        while(usedQuestions.size()<= Integer.parseInt(numberOfQuestions) ) {
                            q_number = getRandom();
                            if (!usedQuestions.contains(q_number)) {
                                usedQuestions.add(q_number);
                            }

                        }

                        QuizBackgroundRunner quizBackgroundRunner = new QuizBackgroundRunner(context);
                        quizBackgroundRunner.execute(usedQuestions);
                    }
                }
            });

            builder.show();
        }
    }

}
