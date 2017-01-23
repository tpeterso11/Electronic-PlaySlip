package com.shoutz.toussaintpeterson.electronicpayslip;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.StringTokenizer;

import models.Game;
import models.User;

/**
 * Created by toussaintpeterson on 1/7/16.
 */
public class AbstractEPSFragment extends Fragment {
    private EPSApplication application;
    public SharedPreferences prefs;
    public ArrayList<Game> favoriteGames;
    private ArrayList<Game> games;
    private ArrayList<String> masterFaves;
    public User user;
    public AbstractEPSFragment() {
        super();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        masterFaves = getEpsApplication().masterFavorites;
        user = getEpsApplication().getUser();
        prefs = getActivity().getSharedPreferences(user.getPhoneNumber(), 0);
        favoriteGames = new ArrayList<Game>();
    }


    public EPSApplication getEpsApplication() {
        if(application==null)
            application = (EPSApplication)getActivity().getApplicationContext();

        return application;
    }

    public void populateFavorites(){
        prefs = getEpsApplication().prefs;
        favoriteGames = new ArrayList<Game>();
        games = getEpsApplication().games;


        if(!(prefs.contains("favorites"))){
            SharedPreferences.Editor edit = prefs.edit();
            edit.putString("favorites", "null").apply();
        }
        else if(!(prefs.getString("favorites","null").equals("null"))){
            String faves = prefs.getString("favorites","null");
            StringTokenizer tokenizer = new StringTokenizer(faves, ",");
            ArrayList<String> test = new ArrayList<String>();
            while(tokenizer.hasMoreTokens()){
                test.add(tokenizer.nextToken());
                /*Game game = new Game();
                game.setGameName(tokenizer.nextToken());
                game.setWager("1");
                favoriteGames.add(game);*/
            }
            for(String name : test){
                for(Game game : games){
                    if(game.getGameName().equals(name)){
                        favoriteGames.add(game);
                    }
                }
            }
        }
    }

    public void setToFavorites(String gameName){
        StringBuilder str = new StringBuilder();
        if(prefs.getString("favorites", "null").equals("null") || prefs.getString("favorites", "null").equals("")){
            str.append(gameName);
            String newFaves = str.toString();

            SharedPreferences.Editor edit = prefs.edit();
            edit.putString("favorites", newFaves).apply();
            masterFaves.add(gameName);
        }
        else if(!(getEpsApplication().masterFavorites.contains(gameName))){
            String old = prefs.getString("favorites", "null");
            str.append(old);
            str.append(",");
            str.append(gameName);
            String newStr = str.toString();
            String[] test = newStr.split(",");
            /*StringTokenizer tokenizer = new StringTokenizer(newStr, " ");
            getEpsApplication().masterFavorites.clear();
            while(tokenizer.hasMoreTokens()){
                getEpsApplication().masterFavorites.add(tokenizer.nextToken());
            }*/
            getEpsApplication().masterFavorites.clear();
            getEpsApplication().masterFavorites.addAll(Arrays.asList(test));
            SharedPreferences.Editor edit = prefs.edit();
            edit.putString("favorites", newStr).apply();
        }
    }

    public void removeFromFavorites(String name){
        StringBuilder str = new StringBuilder();
        String old = prefs.getString("favorites", "null");
        str.append(old);
        String newString = str.toString();
        StringTokenizer tokenizer = new StringTokenizer(newString, ",");
        while(tokenizer.hasMoreTokens()){
            if(name.equals(tokenizer.nextToken())){
                int i = str.indexOf(name);
                if(i != 0){
                    str.delete(i, i + name.length() + 1);
                }
                else {
                    str.delete(i, i + name.length() + 1);
                }
            }
        }
        String[] test = str.toString().split(",");
        String newFaves = str.toString();
        SharedPreferences.Editor edit = prefs.edit();
        StringBuilder newStr = new StringBuilder();
        for(String gameName : test){
            newStr.append(gameName);
        }
        edit.putString("favorites", newStr.toString()).apply();
        masterFaves.clear();
        masterFaves.addAll(Arrays.asList(test));
    }

    public void alertLogin(Context context){
        final String contextString = context.toString();
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(getResources().getString(R.string.login_required));
        builder.setMessage(getResources().getString(R.string.must_be));
        builder.setPositiveButton("Login/Register", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent i = new Intent(getActivity(), SplashLandingActivity.class);
                i.putExtra("previous", contextString);
                startActivity(i);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                ((LandingActivity)getActivity()).launchLanding();
            }
        });

        //builder.setNegativeButton(getResources().getString(R.string.cancel), null);

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
