package com.brogrammers.tutionbd;

import android.content.Context;
import android.content.SharedPreferences;

public class AppPreferences {
    private static final String DATABASE_NAME = "@@*--*@@";

    public static class Login{
        static String IS_FIRST_TIME_LOGIN = "is_first_time_login";
        static String IS_LOGIN = "is_login";
        public static void setIsFirstTimeLogin(Context context, boolean isFirst){
            SharedPreferences sharedPreferences = context.getSharedPreferences(DATABASE_NAME,Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean(IS_FIRST_TIME_LOGIN,isFirst);
            editor.apply();
        }

        public static boolean isFirstTimeLogin(Context context){
            return context.getSharedPreferences(DATABASE_NAME,Context.MODE_PRIVATE).getBoolean(IS_FIRST_TIME_LOGIN,false);
        }

        public static void setIsLogin(Context context, boolean isLogin){
            SharedPreferences sharedPreferences = context.getSharedPreferences(DATABASE_NAME,Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean(IS_LOGIN,isLogin);
            editor.apply();
        }

        public static boolean isLogin(Context context){
            return context.getSharedPreferences(DATABASE_NAME,Context.MODE_PRIVATE).getBoolean(IS_LOGIN,false);
        }

    }
}
