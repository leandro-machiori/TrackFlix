package com.leandro.trackflix.View;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.leandro.trackflix.Controller.TrackController;
import com.leandro.trackflix.Model.TrackFlix;
import com.leandro.trackflix.R;
import java.util.List;

public class detalhes extends AppCompatActivity {
    private ImageView imgCapaDetalhe;
    private TextView txtTituloDetalhe, txtStatusDetalhe, txtNotaDetalhe, txtComentarioDetalhe;
    private Button btnExcluir, btnVoltarDetalhe, btnEditar;
    private TrackController controller;
    private TrackFlix itemSelecionado;
    private int itemId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhes);
        imgCapaDetalhe = findViewById(R.id.imgCapaDetalhe);
        txtTituloDetalhe = findViewById(R.id.txtTituloDetalhe);
        txtStatusDetalhe = findViewById(R.id.txtStatusDetalhe);
        txtNotaDetalhe = findViewById(R.id.txtNotaDetalhe);
        txtComentarioDetalhe = findViewById(R.id.txtComentarioDetalhe);
        btnExcluir = findViewById(R.id.btnExcluir);
        btnEditar = findViewById(R.id.btnEditar);
        btnVoltarDetalhe = findViewById(R.id.btnVoltarDetalhe);
        controller = new TrackController(this);
        Intent intent = getIntent();
        itemId = intent.getIntExtra("itemId", -1);

        if (itemId == -1) {
            Toast.makeText(this, "Erro ao carregar detalhes.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        carregarDetalhes();
        btnExcluir.setOnClickListener(v -> {
            controller.excluirItem(itemId);
            Toast.makeText(this, "Item excluído com sucesso!", Toast.LENGTH_SHORT).show();
            finish();
        });

        btnEditar.setOnClickListener(v -> {
            Intent intentEditar = new Intent(detalhes.this, Cadastro.class);
            intentEditar.putExtra("itemId", itemId);
            startActivityForResult(intentEditar, 100);
        });

        btnVoltarDetalhe.setOnClickListener(v -> finish());
    }
    private void carregarDetalhes() {
        List<TrackFlix> lista = controller.listarItens();

        for (TrackFlix item : lista) {
            if (item.getId() == itemId) {
                itemSelecionado = item;
                break;
            }
        }
        if (itemSelecionado != null) {
            txtTituloDetalhe.setText(itemSelecionado.getTitulo());
            txtStatusDetalhe.setText("Status: " + itemSelecionado.getStatus());

            // Exibir "Sem nota" se a nota for 0 (caso de status pendente)
            if (itemSelecionado.getNota() == 0f && itemSelecionado.getStatus().equalsIgnoreCase("Pendente")) {
                txtNotaDetalhe.setText("Nota: Sem nota");
            } else {
                txtNotaDetalhe.setText("Nota: " + itemSelecionado.getNota());
            }

            txtComentarioDetalhe.setText("Comentário: " + itemSelecionado.getComentario());

            if (itemSelecionado.getImagemUri() != null && !itemSelecionado.getImagemUri().isEmpty()) {
                imgCapaDetalhe.setImageURI(Uri.parse(itemSelecionado.getImagemUri()));
            } else {
                imgCapaDetalhe.setImageResource(R.drawable.trackflix3); // imagem padrão caso não tenha URI
            }
        } else {
            Toast.makeText(this, "Item não encontrado.", Toast.LENGTH_SHORT).show();
            finish();
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100 && resultCode == RESULT_OK) {
            carregarDetalhes(); // Recarrega os dados após edição
        }
    }
}