package geekgames.delichus2.adapters;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
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
import geekgames.delichus2.customObjects.RecetaExtended;
import geekgames.delichus2.customObjects.Recipe;

public class RecipeAdapter extends ArrayAdapter<Recipe> {

    private Context idkContext ;

    public RecipeAdapter(Context context){
        super(context, R.layout.recipe);
        idkContext = context;
    }

    public void swapRecipeRecords(List<Recipe> objects) {
        clear();

        for(Recipe object : objects) {
            add(object);
        }

        notifyDataSetChanged();
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.recipe, parent, false);
        }

        // NOTE: You would normally use the ViewHolder pattern here
        final Recipe recipeRecord = getItem(position);


        TextView nombre = (TextView) convertView.findViewById(R.id.recipe_nombre);
        ImageView imagen = (ImageView) convertView.findViewById(R.id.recipe_imagen);
        imagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainApplication.getInstance().laReceta = recipeRecord;
                Log.i("FUCKING DEBUG", "la receta es " + MainApplication.getInstance().laReceta.nombre);
                ((MainActivity)idkContext).exploreRecipe(v);
            }
        });
        ImageView favBtn = (ImageView)convertView.findViewById(R.id.recipe_fav);
        favBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("FUCKING DEBUG", "se va a adherir " + recipeRecord.nombre +" como favorito" );
                MainApplication.getInstance().addFav(MainApplication.getInstance().usuario.id, recipeRecord.id );
            }
        });
        ImageView foto = (ImageView) convertView.findViewById(R.id.recipe_foto);
        TextView autor = (TextView) convertView.findViewById(R.id.recipe_autor);
        RatingBar puntuacion = (RatingBar) convertView.findViewById(R.id.recipe_puntuacion);
        TextView descripcion = (TextView) convertView.findViewById(R.id.recipe_descripcion);

        if(recipeRecord.pasos == 4) {
            convertView.findViewById(R.id.background).setBackgroundColor(Color.RED);
        }

        nombre.setText(recipeRecord.nombre);

        Picasso.with(idkContext).load(recipeRecord.imagen).fit().centerCrop().into(imagen);
        Picasso.with(idkContext).load(recipeRecord.foto).fit().centerCrop().into(foto);
        autor.setText(recipeRecord.autor);
        puntuacion.setRating(Float.parseFloat(recipeRecord.puntuacion));
        descripcion.setText(recipeRecord.descripcion);

        return convertView;
    }

}
