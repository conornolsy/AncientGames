package conor.nolan.ancientgames.SetUp;

import android.content.Context;

public class MessageController {
    private Context context;

    public MessageController(Context context)
    {
        this.context = context;
    }

    public void login(String type, String username, String password)
    {
        BackgroundRunner backgroundRunner = new BackgroundRunner(context);
        backgroundRunner.execute(type,username,password);
    }


    public void register(String type, String username, String password, String email, String confirmPassword)
    {
        BackgroundRunner backgroundRunner = new BackgroundRunner(context);
        backgroundRunner.execute(type, username, password, email, confirmPassword);
    }
}
