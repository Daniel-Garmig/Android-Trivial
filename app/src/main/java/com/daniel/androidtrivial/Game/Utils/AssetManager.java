package com.daniel.androidtrivial.Game.Utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.daniel.androidtrivial.R;

import java.util.HashMap;

public class AssetManager
{
    //TODO: Utilizaremos un fichero XML para saber qué elementos tenemos que cargar.
    // Estaría bien buscar la forma de que los ID del Hashmap sean accesibles como los ID de Android (algo como R.assetManager.player)
    // Igual se puede utilizar el R.Drawable en vez de crear el nuestro propio.

    // SINGLETON.
    private static AssetManager instance;

    public static AssetManager getInstance()
    {
        //Utilizamos la técnica del double-checked locking (DCL).
        //De esta forma, ahorramos recursos en la sincronización multithread.

        AssetManager assetManager = instance;
        if(assetManager != null) { return assetManager; }

        synchronized (AssetManager.class)
        {
            if(instance == null)
            {
                instance = new AssetManager();
            }
            return instance;
        }
    }
    // END SINGLETON.


    private Context appContext;

    HashMap<String, Bitmap> assets;

    private AssetManager()
    {
        assets = new HashMap<>();
    }


    public void loadAssets(Context appContext)
    {
        this.appContext = appContext;

        loadBitmap("board", R.drawable.trivialboard);
        loadBitmap("playerPiece", R.drawable.piece_green_png);

    }

    /**
     * Get a bitmap from loaded assets.
     * @param name Asset name.
     * @return Returns the asset or null if there is no asset with that name.
     */
    public Bitmap getBitmap(String name)
    {
        return assets.get(name);
    }


    /**
     * Utilizado para cargar todos los Assets.
     * @param name Nombre que se le asigna en el HashMap.
     * @param bitmapAssetID ID del Bitmap a descodificar.
     * @return True si se ha cargado y guardado en el Hashmap correctamente. False en caso de error.
     */
    private boolean loadBitmap(String name, int bitmapAssetID)
    {
        Bitmap img = BitmapFactory.decodeResource(appContext.getResources(), bitmapAssetID);

        //Si no se ha podido cargar, retornamos error.
        if(img == null) { return false; }

        assets.put(name, img);

        return true;
    }

}
