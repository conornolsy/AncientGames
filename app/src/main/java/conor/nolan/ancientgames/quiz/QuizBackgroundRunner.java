package conor.nolan.ancientgames.quiz;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
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
import conor.nolan.ancientgames.quiz.CompetitiveMode.QuizGame;
import conor.nolan.ancientgames.quiz.LearningMode.Facts;
import conor.nolan.ancientgames.quiz.LearningMode.QuizGameLearnMode;

public class QuizBackgroundRunner extends AsyncTask<ArrayList,String,String> {

    private final String fetchQURL = "https://danu6.it.nuigalway.ie/ancientgames/fetchQuestion.php";
    private final String fetchFactsURL = "https://danu6.it.nuigalway.ie/ancientgames/fetchFacts.php";
    private Context context;
    private String[] splitString;
    private ArrayList<Question> questionsArrayList;
    private ArrayList<Facts> factArrayList;
    private String q_number;
    private int mode;
    private String message;
    private int numberOfItems;
    private Handler handler;
    private AlertDialog alertDialog;

    public QuizBackgroundRunner(Context context, int mode)
    {
        questionsArrayList = new ArrayList<Question>();
        factArrayList = new ArrayList<Facts>();
        this.context = context;
        this.mode = mode;
    }


    @Override
    protected void onPreExecute() {
        //super.onPreExecute();
        alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle("Loading");
        alertDialog.setMessage("Fetching Questions...");
        alertDialog.show();


    }

    @Override
    protected  String doInBackground(ArrayList... params)
    {

        numberOfItems = params[0].size();
        System.out.println("    itermsd:   "+numberOfItems);
        if(mode==0) {
            message = fetchQuestions(params);
            return message;

        }

        else if(mode==1) {

            message = fetchQuestions(params);
            if(message==null)
            {
                return null;
            }

            message = fetchFacts(params);
            return message;

        }

        return message;
    }


    public String fetchQuestions(ArrayList[] params)
    {
        for (int i = 0; i <= params[0].size()-1; i++)
        {
            q_number = params[0].get(i).toString();

            try {
                URL url = new URL(fetchQURL);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("q_number", "UTF-8") + "=" + URLEncoder.encode(q_number, "UTF-8");
                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
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
                splitString = result.split("#");
                if (splitString.length == 6) {
                    Question question = new Question(splitString[0], splitString[1], splitString[2], splitString[3], splitString[4], splitString[5]);
                    questionsArrayList.add(question);
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        System.out.println("    question size:   "+questionsArrayList.size());

        if(questionsArrayList.size()==numberOfItems)
        {
            return "success";
        }

        else return null;

    }

    public String fetchFacts(ArrayList[] params)
    {
        for (int i = 0; i <= params[0].size()-1; i++) {
            q_number = params[0].get(i).toString();

            try {
                URL url = new URL(fetchFactsURL);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("q_number", "UTF-8") + "=" + URLEncoder.encode(q_number, "UTF-8");
                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
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
                Facts fact = new Facts(result);
                factArrayList.add(fact);


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        System.out.println("    facts size:   "+factArrayList.size());
        if(factArrayList.size()==numberOfItems)
        {
            return "success";
        }

        else return null;

    }

    @Override
    protected void onPostExecute(String result) {
        alertDialog.hide();
       if(result =="success") {
           if (mode == 0) {
               context.startActivity(
                       new Intent(context, QuizGame.class).putParcelableArrayListExtra("questionsArr", getQuestion())
               );
           } else if (mode == 1) {
               context.startActivity(
                       new Intent(context, QuizGameLearnMode.class).putParcelableArrayListExtra("questionsArr", getQuestion())
                               .putParcelableArrayListExtra("factsArr", getFacts())
               );
           }
       }

       else
       {
           alertDialog = new AlertDialog.Builder(context).create();
           alertDialog.setTitle("Error!");
           alertDialog.setMessage("Error connecting to database. Check your internet connection!");
           handler = new Handler();
           handler.postDelayed(new Runnable() {
               @Override
               public void run() {
                   context.startActivity(new Intent(context, QuizSelectMode.class));
               }
           }, 3000);
       }
    }


    public ArrayList<Question> getQuestion()
    {
       return questionsArrayList;
    }

    public ArrayList<Facts> getFacts()
    {
        return factArrayList;
    }



}
