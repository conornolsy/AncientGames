package conor.nolan.ancientgames.SetUp;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.OnNmeaMessageListener;
import android.os.AsyncTask;
import android.os.Build;
import android.text.TextUtils;
import android.util.Patterns;

import androidx.annotation.RequiresApi;

import com.google.android.gms.common.util.Hex;
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
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.ArrayList;
import java.util.Base64;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import conor.nolan.ancientgames.HomeScreenActivity;
//import conor.nolan.ancientgames.MainActivity;

public class BackgroundRunner extends AsyncTask<String,Void,String> {

    private Context context;
    private AlertDialog alertDialog;
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
    private boolean isAutoLogin=false;
    public OnMessageListener mListener;

    public BackgroundRunner()
    {

    }
    public interface OnMessageListener {
        void messageCallback(String message); // you can change the parameter here. depends on what you want.
    }

    public BackgroundRunner(Context con)
    {
        context = con;
        mListener = (OnMessageListener) context;
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



        //Auto Login if user details saved in cache

        if(type.equals("auto_login"))
        {
            isAutoLogin =true;

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


        // Manual Login

        if(type.equals("login"))
        {

           salt = getSalt(get_salt_url,username);
           if(salt==null)
            {
                return null;
            }

           password1 = getHash(password,salt);




            try {
                URL url = new URL(login_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("username","UTF-8")+"="+URLEncoder.encode(username,"UTF-8")
                        +"&"+URLEncoder.encode("password","UTF-8")+"="+URLEncoder.encode(password1,"UTF-8");
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



        //Registration for non-existing users

        else if(type.equals("register")) {

            String email = params[3];
            String confirmPassword = params[4];


            //check user inputted passwords match

            if(!password.equals(confirmPassword))
            {
                String result = "Error! Password doesn't match!";
                return result;
            }

            //check email and password match an acceptable format

            pattern = Pattern.compile(PASSWORD_PATTERN);
            acceptedEmail = isValidEmail(email);
            acceptedPassword = isValidPassword(password);

            //check username not already taken

            usernameOK = checkUsername(username,checkUsername);
            if(usernameOK.equals("Sorry username already taken!")){
                return usernameOK;}



          //check email already taken
           emailOK = checkEmail(checkEmail, email);
            if(emailOK.equals("Sorry email already taken!")){
                return emailOK;}


            random = new SecureRandom();
            saltByte = new byte[16];
            random.nextBytes(saltByte);
            String encoded = Base64.getEncoder().encodeToString(saltByte);
            salt = encoded;
            password1=  getHash(password,salt);

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
                            + "&" + URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(password1, "UTF-8")
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

        if(result==null && isAutoLogin==true)
        {
            context.startActivity(new Intent(context, SignInActivity.class));
            mListener.messageCallback("AutoLogin unsuccessful");
        }

       else if(result==null)
        {
            alertDialog.setMessage("Error connecting to server, check your internet connection!");
            alertDialog.show();
        }

        else if(result.equals("Registration Successful") || result.equals("Login Successful"))
        {
            alertDialog.setMessage(result);
            alertDialog.show();
            SharedPreferences pref = context.getSharedPreferences("UserData", 0); // 0 - for private mode
            SharedPreferences.Editor editor = pref.edit();
            editor.putString("username", userN); // Storing string
            editor.putInt("highScore", highScore); // Storing integer
            editor.putString("password", password1); // Storing integer
            editor.commit(); // commit changes
            context.startActivity(new Intent(context, HomeScreenActivity.class));
            mListener.messageCallback("Sign In activity successful");
        }


        else if(result.equals("Login Unsuccessful") && isAutoLogin==true)
        {
            context.startActivity(new Intent(context, SignInActivity.class));
            mListener.messageCallback("AutoLogin unsuccessful");
        }

    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

    public boolean isValidEmail(CharSequence target) {
        if (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
        return true;
    }

    public boolean isValidPassword(String password)
    {
        matcher = pattern.matcher(password);
        return matcher.matches();
    }

    public String getSalt(String get_salt_url, String username)
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
            return salt;
        }
        catch (MalformedURLException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public String getHash(String password, String salt)
    {
        byte[] s = Base64.getEncoder().encode(salt.getBytes());
        String bytes = new String(s);
        System.out.println(bytes);
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
        return password;
    }


    public String checkUsername(String username, String checkUsername)
    {
        String response="";
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
                response+= line;
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


            return response;
    }


    public String checkEmail(String emailUrl, String email)
    {
        String response="";
        try {
            URL url = new URL(emailUrl);
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
                response+= line;
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

        return response;
    }

}
