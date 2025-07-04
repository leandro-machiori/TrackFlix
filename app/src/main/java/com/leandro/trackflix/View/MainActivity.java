package com.leandro.trackflix.View;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.leandro.trackflix.Adapter.FlixAdapter;
import com.leandro.trackflix.Controller.TrackController;
import com.leandro.trackflix.Model.TrackFlix;
import com.leandro.trackflix.R;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ListView listView;
    private Button btnAdicionar;
    private TrackController controller;
    private FlixAdapter adapter;
    Button btnFinalizar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        listView = findViewById(R.id.listView);
        btnAdicionar = findViewById(R.id.btnAdicionar);
        controller = new TrackController(this);
        btnFinalizar = findViewById(R.id.btnFinalizar);

        btnAdicionar.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, Cadastro.class);
            startActivity(intent);
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TrackFlix item = (TrackFlix) adapter.getItem(position);
                Intent intent = new Intent(MainActivity.this, detalhes.class);
                intent.putExtra("itemId", item.getId());
                startActivity(intent);
            }
        });
        btnFinalizar.setOnClickListener(v -> {
            finish();
        });
    }
    @Override
    protected void onResume() {
        super.onResume();
        carregarLista();
    }

    private void carregarLista() {
        List<TrackFlix> lista = controller.listarItens();
        adapter = new FlixAdapter(this, lista);
        controller.setAdapter(adapter); // Caso queira controlar o adapter via Controller no futuro
        listView.setAdapter(adapter);
    }
}