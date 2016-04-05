package com.studios.samad.homebudget;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Omer on 21-Mar-16.
 */
public class Preferences extends Activity {

    private Context c = null;

    public Preferences(Context  context) {
        c= context;
    }

    public String setPrefVal(String key, String val) {
        String res = "";
        try
        {

            SharedPreferences settings = c.getSharedPreferences("MYPREFF", MODE_PRIVATE);
            SharedPreferences.Editor editor = settings.edit();
            editor.putString(key, val);
            editor.commit();
        }
        catch (Exception e)
        {
            res = e.toString();
        }

        return res;

    }


    public String getPrefVal(String key, String val) {
        String res = "";
        try {
            SharedPreferences settings = c.getSharedPreferences("MYPREFF", MODE_PRIVATE);
            res = settings.getString(key, val);

        } catch (Exception e) {
            res = "";
        }

        return res;

    }

}
