package com.example.guardarvideo.configuracion;

public class Transacciones {
    // Nombre de la base de datos
    public static final String NameDB = "VideoDB";

    // Nombre de la tabla
    public static final String tabla = "videos";

    // Nombre del campo
    public static final String videoPath = "videoPath";

    // DDL para crear la tabla
    public static final String CreateTableVideos = "CREATE TABLE videos (" +
            "videoPath TEXT PRIMARY KEY)";

    // DDL para eliminar la tabla
    public static final String DropTableVideos = "DROP TABLE IF EXISTS videos";
}
