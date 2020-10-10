package com.brogrammers.tuitionapp;

import android.content.Context;
import android.content.SharedPreferences;

import com.brogrammers.tuitionapp.beans.User;

public class AppPreferences {
    private static final String DATABASE_NAME = "@@*--*@@";
    private static final String LOGIN_DATABASE_NAME = "@@__login__@@";
    private static final String PROFILE_TYPE = "profiling_activity";
    private static final String USER_LATITUDE = "latitude";
    private static final String USER_LONGITUDE = "longitude";

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
        static final String USER_COLLEGE = "user_college";
        static final String USER_SUBJECT = "user_subject";
        static final String USER_NID_SID = "user_sid_nid";
        static final String USER_YEAR = "user_semester";
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
            editor.putString(USER_COLLEGE,user.getCollege());
            editor.putString(USER_SUBJECT,user.getSubject());
            editor.putString(USER_NID_SID,user.getIdCardLink());
            editor.putString(USER_YEAR,user.getYear());
            editor.apply();
        }
        public static String getUserName(Context context){
            return context.getSharedPreferences(DATABASE_NAME,Context.MODE_PRIVATE).getString(USER_NAME,"");
        }
        public static String getUserImage(Context context){
            return context.getSharedPreferences(DATABASE_NAME,Context.MODE_PRIVATE).getString(USER_IMAGE,"");
        }
        public static String getUserYear(Context context){
            return context.getSharedPreferences(DATABASE_NAME,Context.MODE_PRIVATE).getString(USER_YEAR,"");
        }
        public static String getUserDocId(Context context){
            return context.getSharedPreferences(DATABASE_NAME,Context.MODE_PRIVATE).getString(USER_DOC_ID,"");
        }
        public static String getUserCollete(Context context){
            return context.getSharedPreferences(DATABASE_NAME,Context.MODE_PRIVATE).getString(USER_COLLEGE,"");
        }
        public static String getUserSubject(Context context){
            return context.getSharedPreferences(DATABASE_NAME,Context.MODE_PRIVATE).getString(USER_SUBJECT,"");
        }
        public static String getUserStudentIDorNID(Context context){
            return context.getSharedPreferences(DATABASE_NAME,Context.MODE_PRIVATE).getString(USER_NID_SID,"");
        }

    }

    public static void setProfileType(Context context, int profile){
        SharedPreferences sharedPreferences = context.getSharedPreferences(DATABASE_NAME,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(PROFILE_TYPE,profile);
        editor.apply();
    }

    public static int getProfileType(Context context){
        return context.getSharedPreferences(DATABASE_NAME,Context.MODE_PRIVATE).getInt(PROFILE_TYPE,-1);
    }

    public static void setUserLocation(Context context, double latitude, double longitude){
        SharedPreferences sharedPreferences = context.getSharedPreferences(DATABASE_NAME,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(USER_LATITUDE,String.valueOf(latitude));
        editor.putString(USER_LONGITUDE,String.valueOf(longitude));
        editor.apply();
    }

    public static double getUserLatitude(Context context){
        return Double.parseDouble(context.getSharedPreferences(DATABASE_NAME,Context.MODE_PRIVATE).getString(USER_LATITUDE,"23.7104"));
    }

    public static double getUserLongitude(Context context){
        return Double.parseDouble(context.getSharedPreferences(DATABASE_NAME,Context.MODE_PRIVATE).getString(USER_LONGITUDE,"90.40744"));
    }

}
