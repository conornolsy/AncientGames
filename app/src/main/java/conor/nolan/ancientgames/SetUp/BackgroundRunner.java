package conor.nolan.ancientgames.SetUp;


import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Patterns;

import androidx.annotation.RequiresApi;

import com.google.android.gms.common.util.Hex;

import conor.nolan.ancientgames.R;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import conor.nolan.ancientgames.MainActivity;
import conor.nolan.ancientgames.User;

public class BackgroundRunner extends AsyncTask<String,Void,String> {

    Context context;
    AlertDialog alertDialog;
    private boolean acceptedEmail;
    private boolean acceptedPassword;
    private Pattern pattern;
    private Matcher matcher;
    final String PASSWORD_PATTERN = "^(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{8,}$";
    private String userN;
    private int highScore;
    private String[] splitString;
    private SecureRandom random;
    private KeySpec keySpec;
    private SecretKeyFactory keyFactory;
    private byte[] saltByte;
    private String salt ="";
    private MessageDigest md;
    private String msg;
    private byte[] hash;
    private String usernameOK="";
    private String emailOK="";
    private String password1 ="";

    public BackgroundRunner(Context con)
    {
        context = con;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected  String doInBackground(String... params)
    {

        userN = params[1];
        String type = params[0];
        String username = params[1];
        String password = params[2];


        String login_url = "https://danu6.it.nuigalway.ie/ancientgames/login.php";
        String sign_up_url = "https://danu6.it.nuigalway.ie/ancientgames/register.php";
        String get_salt_url = "https://danu6.it.nuigalway.ie/ancientgames/fetchSalt.php";
        String checkUsername = "https://danu6.it.nuigalway.ie/ancientgames/checkUsername.php";
        String checkEmail = "https://danu6.it.nuigalway.ie/ancientgames/checkEmail.php";
        // Login

        if(type.equals("auto_login"))
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
                splitString = result.split("#");
                result = splitString[0];
                if(splitString.length==2) {
                    highScore = Integer.parseInt(splitString[1]);
                }
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

        if(type.equals("login"))
        {

            try {
                URL url = new URL(get_salt_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("username","UTF-8")+"="+URLEncoder.encode(username,"UTF-8");
                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();


                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader((new InputStreamReader(inputStream,"iso-8859-1")));
                String line;
                while ((line = bufferedReader.readLine())!=null)
                {
                    salt += line;
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
            }
            catch (MalformedURLException e)
            {
                e.printStackTrace();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }

            byte[] s = Base64.getEncoder().encode(salt.getBytes());
            keySpec = new PBEKeySpec(password.toCharArray(), s, 65536, 512);
            try {
                keyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512");
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
            try {
                hash = keyFactory.generateSecret(keySpec).getEncoded();
            } catch (InvalidKeySpecException e) {
                e.printStackTrace();
            }
            password = Hex.bytesToStringUppercase(hash);
            password1=password;

            System.out.println(password + "                                     HERE LOGIN !!!\n\n\n\n\n");

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
                splitString = result.split("#");
                result = splitString[0];
                if(splitString.length==2) {
                    highScore = Integer.parseInt(splitString[1]);
                }
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


            //check username not already taken

            try {
                URL url = new URL(checkUsername);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("username","UTF-8")+"="+URLEncoder.encode(username,"UTF-8");
                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();


                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader((new InputStreamReader(inputStream,"iso-8859-1")));
                String line;
                while ((line = bufferedReader.readLine())!=null)
                {
                     usernameOK+= line;
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
            }
            catch (MalformedURLException e)
            {
                e.printStackTrace();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
           // System.out.println(usernameOK+ "  Message");

            if(usernameOK.equals("Sorry username already taken!")){
                return usernameOK;}


            try {
                URL url = new URL(checkEmail);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("email","UTF-8")+"="+URLEncoder.encode(email,"UTF-8");
                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();


                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader((new InputStreamReader(inputStream,"iso-8859-1")));
                String line;
                while ((line = bufferedReader.readLine())!=null)
                {
                    emailOK+= line;
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
            }
            catch (MalformedURLException e)
            {
                e.printStackTrace();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }

            System.out.println(emailOK+ "  Message");

             if(emailOK.equals("Sorry email already taken!")){
                return emailOK;}



            pattern = Pattern.compile(PASSWORD_PATTERN);
            acceptedEmail = isValidEmail(email);
            acceptedPassword = isValidPassword(password);
            random = new SecureRandom();
            saltByte = new byte[16];
            random.nextBytes(saltByte);
            String encoded = Base64.getEncoder().encodeToString(saltByte);
            byte[] s = Base64.getEncoder().encode(encoded.getBytes());
            salt = encoded;
            keySpec = new PBEKeySpec(password.toCharArray(), s, 65536, 512);
            try {
                keyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512");
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
            try {
                hash = keyFactory.generateSecret(keySpec).getEncoded();
            } catch (InvalidKeySpecException e) {
                e.printStackTrace();
            }
            password = Hex.bytesToStringUppercase(hash);
            password1=password;

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
                            + "&" + URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(password, "UTF-8")
                            + "&" + URLEncoder.encode("salt", "UTF-8") + "=" + URLEncoder.encode(salt, "UTF-8");

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
            SharedPreferences pref = context.getSharedPreferences("UserData", 0); // 0 - for private mode
            SharedPreferences.Editor editor = pref.edit();
            editor.putString("username", userN); // Storing string
            editor.putInt("highScore", highScore); // Storing integer
            editor.putString("password", password1); // Storing integer
            editor.commit(); // commit changes
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
