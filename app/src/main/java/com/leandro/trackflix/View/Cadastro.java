package com.leandro.trackflix.View;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.leandro.trackflix.Controller.TrackController;
import com.leandro.trackflix.Model.TrackFlix;
import com.leandro.trackflix.R;
import com.leandro.trackflix.Utils.ImagesUtils;

import java.net.URL;

public class Cadastro extends AppCompatActivity {
    private static final int PICK_IMAGE = 1;
    private ImageView imgCapa;
    private EditText edtTitulo, edtNota, edtComentario, urlEditText;
    private Spinner spnStatus;
    private Button btnSalvar, btnSelecionarImagem, btnBuscarCapaUrl;
    private Uri imagemSelecionada;
    private TrackController controller;
    private int itemId = -1;
    private TrackFlix itemParaEditar = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        imgCapa = findViewById(R.id.imgCapa);
        edtTitulo = findViewById(R.id.edtTitulo);
        edtNota = findViewById(R.id.edtNota);
        edtComentario = findViewById(R.id.edtComentario);
        urlEditText = findViewById(R.id.urlEditText); // Adicionei o novo campo
        spnStatus = findViewById(R.id.spnStatus);
        btnSalvar = findViewById(R.id.btnSalvar);
        btnSelecionarImagem = findViewById(R.id.btnSelecionarImagem);
        btnBuscarCapaUrl = findViewById(R.id.btnBuscarCapaUrl); // Adicionei um novo botão
        controller = new TrackController(this);

        // Verifica se é modo de edição para carregar dados
        itemId = getIntent().getIntExtra("itemId", -1);
        if (itemId != -1) {
            itemParaEditar = controller.buscarItemPorId(itemId);
            if (itemParaEditar != null) {
                edtTitulo.setText(itemParaEditar.getTitulo());
                edtNota.setText(String.valueOf(itemParaEditar.getNota()));
                edtComentario.setText(itemParaEditar.getComentario());
                if (itemParaEditar.getImagemUri() != null && !itemParaEditar.getImagemUri().isEmpty()) {
                    imagemSelecionada = Uri.parse(itemParaEditar.getImagemUri());
                    imgCapa.setImageURI(imagemSelecionada);
                }
                String[] statusArray = getResources().getStringArray(R.array.status_array);
                for (int i = 0; i < statusArray.length; i++) {
                    if (statusArray[i].equals(itemParaEditar.getStatus())) {
                        spnStatus.setSelection(i);
                        break;
                    }
                }
            }
        }

        ArrayAdapter<CharSequence> adapterStatus = ArrayAdapter.createFromResource(
                this,
                R.array.status_array,
                android.R.layout.simple_spinner_item);
        adapterStatus.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnStatus.setAdapter(adapterStatus);

        // Listener para o botão de selecionar imagem da galeria
        btnSelecionarImagem.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            startActivityForResult(intent, PICK_IMAGE);
        });

        // NOVO: Listener para buscar imagem via URL
        btnBuscarCapaUrl.setOnClickListener(v -> {
            String url = urlEditText.getText().toString().trim();
            if (!url.isEmpty()) {
                new Thread(() -> {
                    try {
                        Uri imageUri = ImagesUtils.saveBitmapFromUrl(url, Cadastro.this, "manual_poster_" + System.currentTimeMillis() + ".png");
                        if (imageUri != null) {
                            runOnUiThread(() -> {
                                imgCapa.setImageURI(imageUri);
                                imagemSelecionada = imageUri;
                                Toast.makeText(Cadastro.this, "Imagem carregada com sucesso!", Toast.LENGTH_SHORT).show();
                            });
                        } else {
                            runOnUiThread(() -> Toast.makeText(Cadastro.this, "Falha ao carregar a imagem da URL.", Toast.LENGTH_SHORT).show());
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        runOnUiThread(() -> Toast.makeText(Cadastro.this, "Erro: " + e.getMessage(), Toast.LENGTH_SHORT).show());
                    }
                }).start();
            } else {
                Toast.makeText(this, "Por favor, cole uma URL válida.", Toast.LENGTH_SHORT).show();
            }
        });

        // Listener para o botão de salvar
        btnSalvar.setOnClickListener(v -> {
            String titulo = edtTitulo.getText().toString().trim();
            String status = spnStatus.getSelectedItem().toString();
            String comentario = edtComentario.getText().toString().trim();
            String notaStr = edtNota.getText().toString().trim();

            if (titulo.isEmpty()) {
                Toast.makeText(this, "Preencha o título.", Toast.LENGTH_SHORT).show();
                return;
            }
            if (spnStatus.getSelectedItemPosition() == 0) {
                Toast.makeText(this, "Selecione um status válido.", Toast.LENGTH_SHORT).show();
                return;
            }

            float nota = 0f;
            if (!notaStr.isEmpty()) {
                try {
                    nota = Float.parseFloat(notaStr);
                } catch (NumberFormatException e) {
                    Toast.makeText(this, "Nota inválida.", Toast.LENGTH_SHORT).show();
                    return;
                }
            }

            String finalImageUri = (imagemSelecionada != null) ? imagemSelecionada.toString() : "android.resource://" + getPackageName() + "/" + R.drawable.trackflix3;

            if (itemParaEditar != null) {
                itemParaEditar.setTitulo(titulo);
                itemParaEditar.setStatus(status);
                itemParaEditar.setComentario(comentario);
                itemParaEditar.setNota(nota);
                itemParaEditar.setImagemUri(finalImageUri);
                controller.atualizarItem(itemParaEditar);
                Toast.makeText(this, "Item atualizado com sucesso!", Toast.LENGTH_SHORT).show();
            } else {
                TrackFlix item = new TrackFlix();
                item.setTitulo(titulo);
                item.setStatus(status);
                item.setComentario(comentario);
                item.setNota(nota);
                item.setImagemUri(finalImageUri);
                controller.adicionarItem(item);
                Toast.makeText(this, "Item salvo com sucesso!", Toast.LENGTH_SHORT).show();
            }

            setResult(RESULT_OK);
            finish();
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null) {
            imagemSelecionada = data.getData();
            imgCapa.setImageURI(imagemSelecionada);
        }
    }
}