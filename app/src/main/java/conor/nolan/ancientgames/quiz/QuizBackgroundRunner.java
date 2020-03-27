package conor.nolan.ancientgames.quiz;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
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

public class QuizBackgroundRunner extends AsyncTask<ArrayList,String,String> {

    private final String fetchQURL = "https://danu6.it.nuigalway.ie/ancientgames/fetchQuestion.php";
    private Context context;
    private String[] splitString;
    private ArrayList<Question> questionsArrayList;
    private String q_number;
    private int mode;

    public QuizBackgroundRunner(Context context, int mode)
    {
        questionsArrayList = new ArrayList<Question>();
        this.context = context;
        this.mode = mode;
    }


    @Override
    protected  String doInBackground(ArrayList... params)
    {
            for (int i = 0; i < params[0].size(); i++)
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
                    if(splitString.length==6) {
                        Question question = new Question(splitString[0], splitString[1], splitString[2], splitString[3], splitString[4], splitString[5]);
                        questionsArrayList.add(question);
                    }

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        return null;

    }

    @Override
    protected void onPostExecute(String result) {
        if(mode==0) {
            context.startActivity(
                    new Intent(context, QuizGame.class).putParcelableArrayListExtra("questionsArr", getQuestion())
            );
        }

        else{
            context.startActivity(
                    new Intent(context, QuizGameLearnMode.class).putParcelableArrayListExtra("questionsArr", getQuestion())
            );
        }
    }


    public ArrayList<Question> getQuestion()
    {
       return questionsArrayList;
    }



}
