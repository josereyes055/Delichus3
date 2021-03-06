package geekgames.delichus2.adapters;


import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import geekgames.delichus2.MainActivity;
import geekgames.delichus2.MainApplication;
import geekgames.delichus2.R;
import geekgames.delichus2.customObjects.Comentario;
import geekgames.delichus2.customObjects.Recipe;

public class ComentarioAdapter extends ArrayAdapter<Comentario> {

    private Context idkContext ;

    public ComentarioAdapter(Context context){
        super(context, R.layout.comentario);
        idkContext = context;
    }

    public void swapRecords(List<Comentario> objects) {
        clear();

        for(Comentario object : objects) {
            add(object);
        }

        notifyDataSetChanged();
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.comentario, parent, false);
        }

        // NOTE: You would normally use the ViewHolder pattern here
        final Comentario comentarioRecord = getItem(position);


        TextView nombre = (TextView) convertView.findViewById(R.id.comentario_nombre);
        ImageView foto = (ImageView) convertView.findViewById(R.id.comentario_imagen);
        TextView comentario = (TextView) convertView.findViewById(R.id.comentario_otro_usuario);



        nombre.setText(comentarioRecord.autor);
        Picasso.with(idkContext).load(comentarioRecord.foto).fit().centerCrop().into(foto);
        comentario.setText(comentarioRecord.comentario);

        return convertView;
    }

}
