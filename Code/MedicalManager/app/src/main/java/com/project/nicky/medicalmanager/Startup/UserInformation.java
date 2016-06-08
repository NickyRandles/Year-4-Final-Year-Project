package com.project.nicky.medicalmanager.Startup;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.project.nicky.medicalmanager.Home.HomeActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class UserInformation implements BackgroundResponse {

    private Context context;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private BackgroundTask backgroundTask;

    public UserInformation(Context context){
        this.context = context;
        sharedPreferences = context.getSharedPreferences("userInformation", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void save(String username){
        String type = "getUserInformation";
        backgroundTask = new BackgroundTask(context);
        backgroundTask.backgroundResponse = this;
        backgroundTask.execute(type, username);
    }

    public boolean loggedIn(){
        String username = sharedPreferences.getString("username", "");
        String password = sharedPreferences.getString("password", "");
        if(username != "" && password != ""){
            return true;
        }
        else{
            return false;
        }
    }

    public void logOut(){
        editor.remove("username");
        editor.remove("password");
        editor.apply();
    }

    public String getId(){
        return sharedPreferences.getString("id", "");
    }

    public String getUsername(){
        return sharedPreferences.getString("username", "");
    }

    public String getPassword(){
        return sharedPreferences.getString("password", "");
    }

    public String getUserType(){
        return sharedPreferences.getString("userType", "");
    }

    public String getAddress(){
        return sharedPreferences.getString("address", "");
    }

    @Override
    public void processResponse(String result) {
        if(result != null){
            Log.d("testing", result);
            editor.clear();
            editor.apply();
            try {
                JSONObject root = new JSONObject(result);
                JSONArray list = root.getJSONArray("result");
                for(int i = 0; i < list.length(); i++){
                    JSONObject listItem = list.getJSONObject(i);
                    int id = listItem.getInt("id");
                    String username = listItem.getString("username");
                    String password = listItem.getString("password");
                    String userType = listItem.getString("userType");
                    String addressLine = listItem.getString("address_line_1_2");
                    String city = listItem.getString("city");
                    String county = listItem.getString("county");
                    String country = listItem.getString("country");
                    String address = addressLine + ", " + city + ", " + county + ", " + country;
                    editor.putString("id", Integer.toString(id));
                    editor.putString("username", username);
                    editor.putString("password", password);
                    editor.putString("userType", userType);
                    editor.putString("address", address);
                    editor.apply();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            ((Activity)context).finish();
            Intent intent = new Intent(context, HomeActivity.class);
            context.startActivity(intent);
        }
    }
}
