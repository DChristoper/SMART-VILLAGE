package id.koom.app.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefManager {
    public static final String SP_APP = "spKOOMApp";
    public static final String SP_NIK = "spNIK";
    public static final String SP_TOTAL_H = "spTotalH";
    public static final String SP_CICIL = "spCicil";

    SharedPreferences sp;
    SharedPreferences.Editor spEditor;

    public SharedPrefManager(Context context){
        sp = context.getSharedPreferences(SP_APP, Context.MODE_PRIVATE);
        spEditor = sp.edit();
    }

    public void saveSPString(String key, String value){
        spEditor.putString(key, value);
        spEditor.commit();
    }

    public void saveSPNIK(String value){
        spEditor.putString(SP_NIK, value);
        spEditor.commit();
    }

    public void saveSPTotal(int value){
        spEditor.putInt(SP_TOTAL_H, value);
        spEditor.commit();
    }

    public void saveSPCicil(int value){
        spEditor.putInt(SP_CICIL, value);
        spEditor.commit();
    }

    public void saveSPBoolean(String keySP, boolean value){
        spEditor.putBoolean(keySP, value);
        spEditor.commit();
    }

    public String getSPString(String key){
        return sp.getString(key, "");
    }

    public String getSPNIK(){
        return sp.getString(SP_NIK, "");
    }

    public int getSPTotal(){
        return sp.getInt(SP_TOTAL_H, -1);
    }

    public int getSPCicil(){
        return sp.getInt(SP_CICIL, -1);
    }

    public void delSPString(String key){
        spEditor.remove(key);
        spEditor.commit();
    }
}
