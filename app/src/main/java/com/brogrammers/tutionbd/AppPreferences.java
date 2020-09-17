package com.brogrammers.tutionbd;

import android.content.Context;
import android.content.SharedPreferences;

import com.brogrammers.tutionbd.beans.User;

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

    public static class UserInfo {
        static final String MOBILE_NUMBER = "mobile_number";
        static final String USER_NAME = "user_name";
        static final String USER_IMAGE = "user_image";
        static final String USER_UID = "user_uid";
        static final String USER_DOC_ID = "user_doc_id";
        public static void setUserMobileNumber(Context context, String mobileNumber){
            SharedPreferences sharedPreferences = context.getSharedPreferences(DATABASE_NAME,Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(MOBILE_NUMBER,mobileNumber);
            editor.apply();
        }
        public static String getUserMobileNumber(Context context){
            return context.getSharedPreferences(DATABASE_NAME,Context.MODE_PRIVATE).getString(MOBILE_NUMBER,"");
        }

        public static void setUserInfo(Context context, User user){
            SharedPreferences sharedPreferences = context.getSharedPreferences(DATABASE_NAME,Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();

            editor.putString(USER_NAME,user.getUserName());
            editor.putString(USER_IMAGE,user.getUserImageLink());
            editor.putString(USER_UID,user.getUserUid());
            editor.putString(USER_DOC_ID,user.getDocumentId());
            editor.putString(MOBILE_NUMBER,user.getUserMobile());
            editor.apply();
        }
        public static String getUserName(Context context){
            return context.getSharedPreferences(DATABASE_NAME,Context.MODE_PRIVATE).getString(USER_NAME,"");
        }
        public static String getUserImage(Context context){
            return context.getSharedPreferences(DATABASE_NAME,Context.MODE_PRIVATE).getString(USER_IMAGE,"");
        }
        public static String getUserDocId(Context context){
            return context.getSharedPreferences(DATABASE_NAME,Context.MODE_PRIVATE).getString(USER_DOC_ID,"");
        }

    }

}
