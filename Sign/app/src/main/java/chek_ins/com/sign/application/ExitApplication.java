package chek_ins.com.sign.application;

import android.app.Activity;
import android.app.Application;

import java.util.LinkedList;

/**
 * Created by Administrator on 2016/4/4.
 */
public class ExitApplication extends Application {
    private LinkedList<Activity> activities = new LinkedList<>();
    private static ExitApplication instance;

    public static ExitApplication getIntance() {
        if (instance == null) {
            instance = new ExitApplication();
        }
        return instance;
    }

    public void addActivity(Activity activity) {
        activities.add(activity);
    }

    public void exitActivity() {
        for (Activity activity : activities) {
            activity.finish();
        }
    }

}
