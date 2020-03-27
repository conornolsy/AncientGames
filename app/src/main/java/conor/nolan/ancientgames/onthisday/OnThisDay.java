package conor.nolan.ancientgames.onthisday;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.webkit.WebView;
import org.xmlpull.v1.XmlPullParserException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import conor.nolan.ancientgames.R;
import conor.nolan.ancientgames.onthisday.RSS.OTDItem;
import conor.nolan.ancientgames.onthisday.RSS.OnThisDayParser;

public class OnThisDay extends AppCompatActivity {
    private String URL = "https://www.history.com/.rss/excerpt/this-day-in-history";
    //private List items = new ArrayList();
    private String title = null;
    private String url = null;
    private String summary = null;
    private  List<OTDItem> items = null;
    private static final String ns = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_this_day);
        new RSSRunner().execute(URL);
    }


    private String loadOnThisDayFeed(String urlString) throws XmlPullParserException, IOException {
        InputStream stream = null;
        OnThisDayParser onThisDayParser = new OnThisDayParser();
        items = null;
        title = null;
        url = null;
        summary = null;
        Calendar rightNow = Calendar.getInstance();
        DateFormat formatter = new SimpleDateFormat("MMM dd h:mmaa");


        StringBuilder htmlString = new StringBuilder();
        htmlString.append("<body style=\"background-color:rgba(0,0,0,.01); color: white;\"><style>\n" +
                "table, th, td {\n" +
                "  border: 1px solid white;\n" +
                "}\n" +
                "</style>On This Day in History <br>");


        try {
            stream = downloadFeed(urlString);
            items = onThisDayParser.parseXML(stream);
            // Makes sure that the InputStream is closed after the app is
            // finished using it.
        } finally {
            if (stream != null) {
                stream.close();
            }
        }

        // StackOverflowXmlParser returns a List (called "entries") of Entry objects.
        // Each Entry object represents a single post in the XML feed.
        // This section processes the entries list to combine each entry with HTML markup.
        // Each entry is displayed in the UI as a link that optionally includes
        // a text summary.
        for (OTDItem item : items) {
            htmlString.append("<table style=\"width:100%\">\n" +
                    "  <tr>\n" +
                    "    <th>");
            htmlString.append("<p style=\"color:#FFFFFF\"><a href='");
            htmlString.append(item.link);
            htmlString.append("'>" + item.title + "</a></p></th>  </tr>\n" +
                    "</table><br>");
            // If the user set the preference to include summary text,
            // adds it to the display.

        }
        htmlString.append("<em>" + getResources().getString(R.string.updated) + " " +
                formatter.format(rightNow.getTime()) + "</em>");
        return htmlString.toString();
    }

    // Given a string representation of a URL, sets up a connection and gets
// an input stream.
    private InputStream downloadFeed(String urlString) throws IOException {
        java.net.URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setReadTimeout(10000 /* milliseconds */);
        conn.setConnectTimeout(15000 /* milliseconds */);
        conn.setRequestMethod("GET");
        conn.setDoInput(true);
        // Starts the query
        conn.connect();
        return conn.getInputStream();
    }

    private class RSSRunner extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            try {
                return loadOnThisDayFeed(urls[0]);
            } catch (IOException e) {
                return getResources().getString(R.string.connection_error);
            } catch (XmlPullParserException e) {
                e.printStackTrace();
                return getResources().getString(R.string.xml_error);
            }
        }

        @Override
        protected void onPostExecute(String result) {
            WebView onThisDayLinks = (WebView) findViewById(R.id.webView1);
            onThisDayLinks.setBackgroundResource(R.drawable.webviewbackg);
            onThisDayLinks.setBackgroundColor(android.graphics.Color.TRANSPARENT);
            onThisDayLinks.loadData(result, "text/html", null);
        }
    }

}
