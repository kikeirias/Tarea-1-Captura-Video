package com.example.guardarvideo.configuracion;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import androidx.annotation.Nullable;

public class SQLliteConexion extends SQLiteOpenHelper {

    public SQLliteConexion(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Crear la tabla de videos
        db.execSQL(Transacciones.CreateTableVideos);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Eliminar la tabla si existe y crearla de nuevo
        db.execSQL(Transacciones.DropTableVideos);
        onCreate(db);
    }
}