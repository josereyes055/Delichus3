package geekgames.delichus2.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import geekgames.delichus2.R;
import geekgames.delichus2.customObjects.SimpleRecipe;

public class SimpleRecipeAdapter extends ArrayAdapter<SimpleRecipe> {

    private Context idkContext ;

    public SimpleRecipeAdapter(Context context){
        super(context, R.layout.recipe_min);
        idkContext = context;
    }

    public void swapRecipeRecords(List<SimpleRecipe> objects) {
        clear();

        for(SimpleRecipe object : objects) {
            add(object);
        }

        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        SimpleRecipe recipeRecord = getItem(position);

        // Si el id es -1 entonces es un header
        if(recipeRecord.getId() == -1) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.header, parent, false);
            TextView nombre = (TextView) convertView.findViewById(R.id.separator);
            nombre.setText(recipeRecord.getNombre());

        } else {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.recipe_min, parent, false);

            // NOTE: You would normally use the ViewHolder pattern here
            TextView nombre = (TextView) convertView.findViewById(R.id.fav_nombre);
            ImageView imagen = (ImageView) convertView.findViewById(R.id.fav_imagen);
            TextView autor = (TextView) convertView.findViewById(R.id.fav_autor);
            RatingBar puntuacion = (RatingBar) convertView.findViewById(R.id.fav_puntuacion);

            //SimpleRecipe recipeRecord = getItem(position);

            nombre.setText(recipeRecord.getNombre());
            Picasso.with(idkContext).load(recipeRecord.getTipo()).fit().centerCrop().into(imagen);
            autor.setText(recipeRecord.getAutor());
            puntuacion.setRating(Float.parseFloat(recipeRecord.getPuntuacion()));
        }

        return convertView;
    }


}
