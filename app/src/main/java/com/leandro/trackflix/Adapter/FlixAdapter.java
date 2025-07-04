package com.leandro.trackflix.Adapter;

import com.leandro.trackflix.R;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.leandro.trackflix.Model.TrackFlix;
import java.util.List;

public class FlixAdapter extends BaseAdapter {

    private Context context;
    private List<TrackFlix> lista;
    private LayoutInflater inflater;
    public FlixAdapter(Context context, List<TrackFlix> lista) {
        this.context = context;
        this.lista = lista;
        inflater = LayoutInflater.from(context);
    }
    @Override
    public int getCount() {
        return lista.size();
    }
    @Override
    public Object getItem(int position) {
        return lista.get(position);
    }
    @Override
    public long getItemId(int position) {
        return lista.get(position).getId();
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_lista, parent, false);
            holder = new ViewHolder();
            holder.imgCapa = convertView.findViewById(R.id.imageView);
            holder.txtTitulo = convertView.findViewById(R.id.txtTitulo);
            holder.txtStatus = convertView.findViewById(R.id.txtStatus);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        TrackFlix item = lista.get(position);
        holder.txtTitulo.setText(item.getTitulo());
        holder.txtStatus.setText(item.getStatus());
        if (item.getImagemUri() != null && !item.getImagemUri().isEmpty()) {
            holder.imgCapa.setImageURI(Uri.parse(item.getImagemUri()));
        } else {
            holder.imgCapa.setImageResource(R.drawable.trackflix2); // imagem padrão
        }
        return convertView;
    }
    static class ViewHolder {
        ImageView imgCapa;
        TextView txtTitulo;
        TextView txtStatus;
    }
}