package conor.nolan.ancientgames.SetUp;


import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Patterns;
import conor.nolan.ancientgames.R;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;



import conor.nolan.ancientgames.MainActivity;

public class BackgroundRunner extends AsyncTask<String,Void,String> {

    Context context;
    AlertDialog alertDialog;
    private boolean acceptedEmail;
    private boolean acceptedPassword;
    private Pattern pattern;
    private Matcher matcher;
    final String PASSWORD_PATTERN = "^(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{8,}$";

    public BackgroundRunner(Context con)
    {
        context = con;
    }

    @Override
    protected  String doInBackground(String... params)
    {

        String type = params[0];
        String username = params[1];
        String password = params[2];

        String login_url = "https://danu6.it.nuigalway.ie/ancientgames/login.php";
        String sign_up_url = "https://danu6.it.nuigalway.ie/ancientgames/register.php";
        if(type.equals("login"))
        {
            try {
                URL url = new URL(login_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("username","UTF-8")+"="+URLEncoder.encode(username,"UTF-8")
                        +"&"+URLEncoder.encode("password","UTF-8")+"="+URLEncoder.encode(password,"UTF-8");
                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();


                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader((new InputStreamReader(inputStream,"iso-8859-1")));
                String result="";
                String line;
                while ((line = bufferedReader.readLine())!=null)
                {
                    result += line;
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return result;
            }
            catch (MalformedURLException e)
            {
                e.printStackTrace();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }

        else if(type.equals("register")) {

            String email = params[3];
            String confirmPassword = params[4];

            if(!password.equals(confirmPassword))
            {
                String result = "Error! Password doesn't match!";
                return result;
            }

            pattern = Pattern.compile(PASSWORD_PATTERN);
            acceptedEmail = isValidEmail(email);
            acceptedPassword = isValidPassword(password);

            if (acceptedEmail && acceptedPassword) {

                try {
                    URL url = new URL(sign_up_url);
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setDoInput(true);
                    OutputStream outputStream = httpURLConnection.getOutputStream();
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                    String post_data = URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(email, "UTF-8")
                            + "&" + URLEncoder.encode("username", "UTF-8") + "=" + URLEncoder.encode(username, "UTF-8")
                            + "&" + URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(password, "UTF-8");

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
                    return result;
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            else if(acceptedEmail)
            {
                String result="Password not accepted: \n Password must contain uppercase, lowercase and a capital letter.";
                return result;
            }

            else if(acceptedPassword)
            {
                String result="Email format not accepted: \n Please try again.";
                return result;
            }

            else{
                String result= "Error logging in \n Please try again.";
                return result;

            }

        }

        return null;
    }

    @Override
    protected void onPreExecute() {
        // super.onPreExecute();
        alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle("Login Status");

    }

    @Override
    protected void onPostExecute(String result) {
        // super.onPostExecute(aVoid);
        alertDialog.setMessage(result);
        alertDialog.show();
        if(result.equals("Registration Successful") || result.equals("Login Successful"))
        {
            context.startActivity(new Intent(context, MainActivity.class));
        }
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

    public boolean isValidPassword(String password)
    {
        matcher = pattern.matcher(password);
        return matcher.matches();
    }


}
