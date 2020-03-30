package conor.nolan.ancientgames.SetUp;

import android.content.Context;

public class MessageController {
    private Context context;
    private String username;
    private String password;
    private String type;
    public MessageController(Context context) {
        this.context = context;

    }

    public void login(String type, String username, String password) {
        BackgroundRunner backgroundRunner = new BackgroundRunner(context);
        backgroundRunner.execute(type,username,password);
    }
}
