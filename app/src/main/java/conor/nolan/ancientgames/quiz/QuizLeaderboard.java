package conor.nolan.ancientgames.quiz;

import android.app.AlertDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.widget.TextView;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import conor.nolan.ancientgames.R;

public class QuizLeaderboard extends AppCompatActivity {

    private TextView leaderboard;
    private final String fetchLeaderboardURL = "https://danu6.it.nuigalway.ie/ancientgames/getLeaderboard.php";
    private String[] splitString;
    private ArrayList<String> usernames;
    private ArrayList<String> highscores;
    private AlertDialog alertError;
    private int rank;
    private Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        usernames = new ArrayList<>();
        highscores = new ArrayList<>();
        setContentView(R.layout.activity_quiz_leaderboard);
        leaderboard = (TextView) findViewById(R.id.leaderboard);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        GetLeaderboard getLeaderboard = new GetLeaderboard();
        getLeaderboard.execute();
    }

    public void setLeaderboard() {
        leaderboard.append("Ancient Quiz Leaderboard:\n\n");
        rank=1;
        for(int j=0; j<usernames.size(); j++)
        {
            leaderboard.append(rank+". "+ usernames.get(j)+"\n    - Score: "+highscores.get(j)+"\n\n");
            rank++;
        }
    }


    private class GetLeaderboard extends AsyncTask<ArrayList,String,String> {

        protected String doInBackground(ArrayList... params) {
            try {
                URL url = new URL(fetchLeaderboardURL);
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
                return result;


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(String result) {

            if (result == null) {
                alertError = new AlertDialog.Builder(context).create();
                alertError.setTitle("Error");
                alertError.setMessage("Error retrieving leaderboard. Check your internet connection!");
            }
            else {
                splitString = result.split("#");
                System.out.println("Split string length!!!!!!      :"+splitString.length);
                for (int i = 0; i < splitString.length; i++)
                    if ((i % 2) == 0) {
                        usernames.add(splitString[i]);
                    } else {
                        highscores.add(splitString[i]);
                    }

            }
            setLeaderboard();
        }
        }

}
