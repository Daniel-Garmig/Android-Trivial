package com.daniel.androidtrivial;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.room.Room;

import com.daniel.androidtrivial.Model.MatchRecord.MatchRecordDatabase;
import com.daniel.androidtrivial.Model.MatchRecord.MatchStatsWithPlayersStats;
import com.daniel.androidtrivial.Model.SavedMatch;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;

public class MatchManager
{
    private static final String TAG = "MatchManager";

    // SINGLETON.
    private static MatchManager instance;

    public static MatchManager getInstance()
    {
        //Singleton with double-checked locking (DCL).

        MatchManager matchMng = instance;
        if(matchMng != null) { return matchMng; }

        synchronized (MatchManager.class)
        {
            if(instance == null)
            {
                instance = new MatchManager();
            }
            return instance;
        }
    }

    private MatchManager() {}

    // END SINGLETON.

    private Context appContext;

    private boolean isInit = false;

    private File saveDirectory;
    private Gson json;
    private MatchRecordDatabase db;

    public String matchFileToLoad = null;
    public int matchStatsIDToLoad;

    public void init(Context context)
    {
        if(isInit) { return; }
        isInit = true;

        this.appContext = context;

        //Create Stuff.
        json = new Gson();

        saveDirectory = new File(appContext.getFilesDir(), "saves");
        if(!saveDirectory.exists())
        {
            saveDirectory.mkdir();
        }

        db = Room.databaseBuilder(appContext, MatchRecordDatabase.class, "matches").build();
    }


    /**
     * Save given save data to the file.
     * @param save
     */
    public void saveMatchData(SavedMatch save)
    {
        //Serialize to JSON.
        String saveJson = json.toJson(save);

        File f = new File(saveDirectory, save.name + ".json");
        if(f.exists())
        {
            //TODO: Error!!!
        }
        try
        {
            f.createNewFile();
            FileOutputStream fos = new FileOutputStream(f, false);
            fos.write(saveJson.getBytes());
        }
        catch (Exception e)
        {
            Log.e(TAG, e.toString());
        }

        Log.i("MatchManager", "Guardada la partida " + save.name);
    }


    public SavedMatch loadMatchData()
    {
        File f = new File(saveDirectory, matchFileToLoad);
        if(!f.exists()) { return null; }

        //Load JSON.
        StringBuilder strb = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(f))))
        {
            String line = reader.readLine();
            while(line != null)
            {
                strb.append(line).append("\n");
                line = reader.readLine();
            }
        }
        catch (Exception e)
        {
            Log.e(TAG, e.toString());
        }

        //Deserialize into SavedMatch.
        String jsonData = strb.toString();
        SavedMatch save = json.fromJson(jsonData, SavedMatch.class);

        return save;
    }


    public void removeSavedMatch(SavedMatch savedMatch)
    {
        removeSavedMatch(savedMatch.name + ".json");
    }

    public void removeSavedMatch(String filename)
    {
        File f = new File(saveDirectory, filename);
        if(!f.exists()) { return; }

        try {
            if(!f.delete())
            {
                Toast.makeText(appContext, "Error al eliminar!", Toast.LENGTH_LONG).show();
                return;
            }
            Toast.makeText(appContext, "Game Ended!", Toast.LENGTH_LONG).show();
        }
        catch (Exception e) {
            Log.e(TAG, e.toString());
        }
    }

    /**
     * Get a list of match save files.
     */
    public List<String> getMatchSavesList()
    {

        String[] files = saveDirectory.list();

        return Arrays.asList(files);
    }



    public MatchStatsWithPlayersStats loadGameSummary()
    {
        return null;
    }


    public void setMatchToLoad(String saveFileName) { matchFileToLoad = saveFileName; }

    public void setMatchStatsIDToLoad(int matchStatsIDToLoad) { this.matchStatsIDToLoad = matchStatsIDToLoad; }

    public Gson getJson() { return json; }
    public MatchRecordDatabase getDb() { return db; }
}
