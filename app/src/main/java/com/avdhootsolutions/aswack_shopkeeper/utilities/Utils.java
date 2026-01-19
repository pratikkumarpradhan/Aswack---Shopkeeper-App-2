package com.avdhootsolutions.aswack_shopkeeper.utilities;

import android.content.Context;
import android.content.SharedPreferences;

public class Utils {

    public static boolean isNetworkAvailable(Context context) {
        // Using ConnectivityManager to check for Network Connection
//        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
//        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return true;
    }

    public static String getPrefData(String prefName, Context context) {
        SharedPreferences pref;
        String prefValue;
        pref = context.getSharedPreferences(prefName, Context.MODE_PRIVATE);
        prefValue = pref.getString(prefName, "");
        return prefValue;
    }

    public static void setPrefData(String prefName, String value, Context context) {
        if (context != null) {
            SharedPreferences pref;
            SharedPreferences.Editor editor;
            pref = context.getSharedPreferences(prefName, Context.MODE_PRIVATE);
            editor = pref.edit();
            editor.putString(prefName, value);
            editor.commit();
        }
    }

    public static Integer getPrefIntData(String prefName, Context context) {
        SharedPreferences pref;
        Integer prefValue;
        pref = context.getSharedPreferences(prefName, Context.MODE_PRIVATE);
        prefValue = pref.getInt(prefName, 0);
        return prefValue;
    }

    public static void setPrefIntData(String prefName, Integer value, Context context) {
        SharedPreferences pref;
        SharedPreferences.Editor editor;
        pref = context.getSharedPreferences(prefName, Context.MODE_PRIVATE);
        editor = pref.edit();
        editor.putInt(prefName, value);
        editor.commit();
    }

    public static void setBooleanPrefData(String prefName, boolean value, Context context) {
        SharedPreferences pref;
        SharedPreferences.Editor editor;
        pref = context.getSharedPreferences(prefName, Context.MODE_PRIVATE);
        editor = pref.edit();
        editor.putBoolean(prefName, value);
        editor.commit();
    }

    public static boolean getBooleanPrefData(String prefName, Context context) {
        SharedPreferences pref;
        boolean prefValue;
        pref = context.getSharedPreferences(prefName, Context.MODE_PRIVATE);
        prefValue = pref.getBoolean(prefName, false);
        return prefValue;
    }

    /*public static void setUserData(ArrayList<User> userData, Context mContext) {
        Utils.setPrefData(Prefrences.CUS_ID, userData.get(0).getCusId(), mContext);
        Utils.setPrefData(Prefrences.CUS_UNIQUE_ID, userData.get(0).getCusUniqueId(), mContext);
        Utils.setPrefData(Prefrences.CUS_SPON_ID, userData.get(0).getCusSponId(), mContext);
        Utils.setPrefData(Prefrences.CUS_NAME, userData.get(0).getCusName(), mContext);
        Utils.setPrefData(Prefrences.CUS_MOBILE, userData.get(0).getCusMobile(), mContext);
        Utils.setPrefData(Prefrences.CUS_CITY, userData.get(0).getCusCity(), mContext);
        Utils.setPrefData(Prefrences.CUS_EMAIL, userData.get(0).getCusEmail(), mContext);
        Utils.setPrefData(Prefrences.CUS_PHOTO, userData.get(0).getCusPhoto(), mContext);
        Utils.setPrefData(Prefrences.CUS_STATUS, userData.get(0).getCusStatus(), mContext);
    }*/

    public static void clearUserData(Context mContext) {
        Utils.setPrefData(Prefrences.CUS_ID, "", mContext);
        Utils.setPrefData(Prefrences.CUS_UNIQUE_ID, "", mContext);
        Utils.setPrefData(Prefrences.CUS_SPON_ID, "", mContext);
        Utils.setPrefData(Prefrences.CUS_NAME, "", mContext);
        Utils.setPrefData(Prefrences.CUS_MOBILE, "", mContext);
        Utils.setPrefData(Prefrences.CUS_CITY, "", mContext);
        Utils.setPrefData(Prefrences.CUS_EMAIL, "", mContext);
        Utils.setPrefData(Prefrences.CUS_PHOTO, "", mContext);
        Utils.setPrefData(Prefrences.CUS_STATUS, "", mContext);
    }
}
