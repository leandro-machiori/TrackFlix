package com.leandro.trackflix.Bd;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.leandro.trackflix.Model.TrackFlix;

public class TrackFlixDBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "trackflix.db";
    private static final int DATABASE_VERSION = 2;
    // Criação da tabela trackflix
    private static final String TABLE_CREATE =
            "CREATE TABLE tracflix (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "titulo TEXT NOT NULL, " +
                    "imagemUri TEXT, " +
                    "status TEXT, " +
                    "comentario TEXT, " +
                    "nota REAL" +
                    ");";
    public TrackFlixDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE);
    }
    // Para atualizações futuras do banco de dados
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Exemplo simples: apagar e recriar a tabela
        db.execSQL("DROP TABLE IF EXISTS tracFlix");
        onCreate(db);
    }
    public TrackFlix buscarItemPorId(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM trackflix WHERE id = ?", new String[]{String.valueOf(id)});

        TrackFlix item = null;

        if (cursor.moveToFirst()) {
            item = new TrackFlix();
            item.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
            item.setTitulo(cursor.getString(cursor.getColumnIndexOrThrow("titulo")));
            item.setNota(cursor.getFloat(cursor.getColumnIndexOrThrow("nota")));
            item.setStatus(cursor.getString(cursor.getColumnIndexOrThrow("status")));
            item.setComentario(cursor.getString(cursor.getColumnIndexOrThrow("comentario")));
            item.setImagemUri(cursor.getString(cursor.getColumnIndexOrThrow("imagemUri")));
        }
        cursor.close();
        db.close();
        return item;
    }
    public void atualizar(TrackFlix item) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("titulo", item.getTitulo());
        values.put("nota", item.getNota());
        values.put("status", item.getStatus());
        values.put("comentario", item.getComentario());
        values.put("imagemUri", item.getImagemUri());

        // Atualiza o item no banco onde o ID for igual
        db.update("trackflix", values, "id = ?", new String[]{String.valueOf(item.getId())});

        db.close(); // Fecha o banco depois da atualização
    }

}