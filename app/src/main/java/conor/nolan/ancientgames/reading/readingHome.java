package conor.nolan.ancientgames.reading;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.os.AsyncTask;
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
import conor.nolan.ancientgames.onthisday.RSS.FeedItem;

public class readingHome extends AppCompatActivity {
    private String URL = "https://www.history.com/.rss/excerpt/news";
    private String title = null;
    private String url = null;
    private String summary = null;
    private  List<FeedItem> items = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reading_home);
        new RSSRunner().execute(URL);
    }

    private String loadArticlesFeed(String urlString) throws XmlPullParserException, IOException {
        InputStream stream = null;
        FeaturedArticlesParser featuredArticlesParser = new FeaturedArticlesParser();
        items = null;
        title = null;
        url = null;
        summary = null;
        Calendar rightNow = Calendar.getInstance();
        DateFormat formatter = new SimpleDateFormat("MMM dd h:mmaa");
        StringBuilder html = new StringBuilder();
        html.append("" +
                "<body style=\"background-color:rgba(0,0,0,.01); " +
                "color: white;\">" +
                "<style>\n" +
                "table, th, td {\n" +
                "  border: 1px solid white;\n" +
                "}\n" +
                "a:link {\n" +
                        "  color: green;\n" +
                        "  background-color: transparent;\n" +
                        "  text-decoration: none;\n" +
                        "}\n" +
                        "a:visited {\n" +
                        "  color: pink;\n" +
                        "  background-color: transparent;\n" +
                        "  text-decoration: none;\n" +
                        "}\n" +

                "</style>Featured Articles<br>");


        try
        {
            stream = downloadFeed(urlString);
            items = featuredArticlesParser.parseXML(stream);
        }
        finally
        {
            if (stream != null)
            {
                stream.close();
            }
        }
        for (FeedItem item : items)
        {
            html.append("<table style=\"width:100%\">\n" +
                    "  <tr>\n" +
                    "    <th>");
            html.append("<p style=\"color:#FFFFFF\"><a href='");
            html.append(item.link);
            html.append("'>" + item.title + "</a></p></th>  </tr>\n" +
                    "</table><br>");
        }
        html.append("<em>" + getResources().getString(R.string.updated) + " " +
                formatter.format(rightNow.getTime()) + "</em>");
        return html.toString();
    }

    private InputStream downloadFeed(String urlString) throws IOException {
        java.net.URL url = new URL(urlString);
        HttpURLConnection connect = (HttpURLConnection) url.openConnection();
        connect.setReadTimeout(10000);
        connect.setConnectTimeout(15000);
        connect.setRequestMethod("GET");
        connect.setDoInput(true);
        connect.connect();
        return connect.getInputStream();
    }

    private class RSSRunner extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            try {
                return loadArticlesFeed(urls[0]);
            } catch (IOException e) {
                return getResources().getString(R.string.connection_error);
            } catch (XmlPullParserException e) {
                e.printStackTrace();
                return getResources().getString(R.string.xml_error);
            }
        }

        @Override
        protected void onPostExecute(String result) {
            WebView featuredArticlesLinks = (WebView) findViewById(R.id.webView2);
            featuredArticlesLinks.setBackgroundColor(android.graphics.Color.TRANSPARENT);
            featuredArticlesLinks.setBackgroundResource(R.drawable.webviewbg );
            featuredArticlesLinks.loadData(result, "text/html", null);

        }
    }

}
