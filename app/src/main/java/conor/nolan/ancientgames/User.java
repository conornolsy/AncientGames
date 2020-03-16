package conor.nolan.ancientgames;

public class User {

    private String username;
    private int highScore;


    public User(String username, int highScore)
    {
        this.username = username;
        this.highScore = highScore;
    }


    public void setHighScore(int newHighScore)
    {
        this.highScore = newHighScore;
    }


    public int getHighScore()
    {
        return highScore;
    }


}
