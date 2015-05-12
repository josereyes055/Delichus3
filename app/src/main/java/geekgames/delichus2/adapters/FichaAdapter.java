package geekgames.delichus2.adapters;

import android.content.Context;
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
import geekgames.delichus2.customObjects.Ficha;

public class FichaAdapter extends ArrayAdapter<Ficha> {

    private Context idkContext ;

    public FichaAdapter(Context context){
        super(context, R.layout.recipe);
        idkContext = context;
    }

    public void swapRecipeRecords(List<Ficha> objects) {
        clear();

        for(Ficha object : objects) {
            add(object);
        }

        notifyDataSetChanged();
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.recipe, parent, false);
        }

        final Ficha unaFicha = getItem(position);


        TextView nombre = (TextView) convertView.findViewById(R.id.recipe_nombre);
        ImageView imagen = (ImageView) convertView.findViewById(R.id.recipe_imagen);
        imagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainApplication.getInstance().laReceta = unaFicha;
                //Log.i("FUCKING DEBUG", "la receta es " + MainApplication.getInstance().laReceta.nombre);
                ((MainActivity)getContext()).exploreRecipe(v);
            }
        });
        ImageView favBtn = (ImageView)convertView.findViewById(R.id.recipe_fav);
        favBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Log.i("FUCKING DEBUG", "se va a adherir " + unaFicha.nombre +" como favorito" );
                MainApplication.getInstance().addFav(MainApplication.getInstance().usuario.id, unaFicha.id );
            }
        });
        ImageView foto = (ImageView) convertView.findViewById(R.id.recipe_foto);
        TextView autor = (TextView) convertView.findViewById(R.id.recipe_autor);
        RatingBar puntuacion = (RatingBar) convertView.findViewById(R.id.recipe_puntuacion);
        TextView descripcion = (TextView) convertView.findViewById(R.id.recipe_descripcion);

        /*if(unaFicha.pasos == 4) {
            convertView.findViewById(R.id.background).setBackgroundColor(Color.RED);
        }*/

        nombre.setText(unaFicha.nombre);
        Picasso.with(idkContext).load(unaFicha.imagen).fit().centerCrop().into(imagen);
        Picasso.with(idkContext).load(unaFicha.foto).fit().centerCrop().into(foto);
        autor.setText(unaFicha.autor);
        puntuacion.setRating(unaFicha.puntuacion);
        descripcion.setText(unaFicha.descripcion);

        return convertView;
    }

}
