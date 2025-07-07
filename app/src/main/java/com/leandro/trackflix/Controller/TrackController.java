package com.leandro.trackflix.Controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.leandro.trackflix.Adapter.FlixAdapter;
import com.leandro.trackflix.Bd.TrackFlixDBHelper;
import com.leandro.trackflix.Model.TrackFlix;
import java.util.ArrayList;
import java.util.List;

public class TrackController {
    private TrackFlixDBHelper dbHelper;
    private Context context;
    private FlixAdapter adapter;

    public TrackController(Context context) {
        this.context = context;
        dbHelper = new TrackFlixDBHelper(context);
    }
    public void setAdapter(FlixAdapter adapter) {
        this.adapter = adapter;
    }
    // Adicionar item no banco de dados de forma segura
    public void adicionarItem(TrackFlix item) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("titulo", item.getTitulo());
        values.put("imagemUri", item.getImagemUri());
        values.put("status", item.getStatus());
        values.put("comentario", item.getComentario());
        values.put("nota", item.getNota());
        db.insert("trackflix", null, values);
        db.close();
    }
    // Listar todos os itens
    public List<TrackFlix> listarItens() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        List<TrackFlix> lista = new ArrayList<>();

        Cursor cursor = db.query("trackflix",
                null, // Seleciona todas as colunas
                null, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                TrackFlix item = new TrackFlix();
                item.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
                item.setTitulo(cursor.getString(cursor.getColumnIndexOrThrow("titulo")));
                item.setImagemUri(cursor.getString(cursor.getColumnIndexOrThrow("imagemUri")));
                item.setStatus(cursor.getString(cursor.getColumnIndexOrThrow("status")));
                item.setComentario(cursor.getString(cursor.getColumnIndexOrThrow("comentario")));
                item.setNota(cursor.getFloat(cursor.getColumnIndexOrThrow("nota")));

                lista.add(item);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return lista;
    }
    // Excluir item pelo ID de forma segura
    public void excluirItem(int id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete("trackflix", "id = ?", new String[]{String.valueOf(id)});
        db.close();
    }
    // Buscar item por ID (reutiliza o método do DBHelper)
    public TrackFlix buscarItemPorId(int id) {
        return dbHelper.buscarItemPorId(id);
    }
    // Atualizar item
    public void atualizarItem(TrackFlix item) {
        dbHelper.atualizar(item);
    }
}