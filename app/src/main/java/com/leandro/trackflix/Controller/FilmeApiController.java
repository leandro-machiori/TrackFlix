package com.leandro.trackflix.Controller;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.widget.ImageView;

import com.leandro.trackflix.R;
import com.leandro.trackflix.Utils.ImagesUtils;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class FilmeApiController {
    private static final String API_KEY = "f139a0c2";

    public interface OnImageDownloadedListener {
        void onImageDownloaded(Uri imageUri);
    }

    public static void buscarCapaPorTitulo(String titulo, Context context, ImageView imageView, OnImageDownloadedListener listener) {
        new Thread(() -> {
            Uri finalUri = null;
            try {
                String urlString = "https://www.omdbapi.com/?t=" + titulo + "&apikey=" + API_KEY;
                URL url = new URL(urlString);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");

                InputStream in = new BufferedInputStream(conn.getInputStream());
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                StringBuilder result = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }

                JSONObject jsonObject = new JSONObject(result.toString());
                String response = jsonObject.getString("Response");

                if (response.equals("True") && jsonObject.has("Poster")) {
                    String posterUrl = jsonObject.getString("Poster");
                    Bitmap bitmap = ImagesUtils.getBitmapFromURL(posterUrl);

                    if (bitmap != null) {
                        // Salva o bitmap e obtém a URI
                        finalUri = ImagesUtils.saveBitmapToCache(bitmap, context, "poster_" + titulo + ".png");

                        // Atualiza a ImageView na thread principal
                        new Handler(Looper.getMainLooper()).post(() -> {
                            if (imageView != null) {
                                imageView.setImageBitmap(bitmap);
                            }
                        });
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                // Chama o listener com a URI, mesmo se for nula
                if (listener != null) {
                    Uri finalUri1 = finalUri;
                    new Handler(Looper.getMainLooper()).post(() -> {
                        listener.onImageDownloaded(finalUri1);
                    });
                }
            }
        }).start();
    }
}