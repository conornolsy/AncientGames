package conor.nolan.ancientgames.onthisday.RSS;

public class FeedItem {

    public final String title;
    public final String link;
    public final String description;

    public FeedItem(String title, String summary, String link) {
        this.title = title;
        this.description = summary;
        this.link = link;
    }
}
