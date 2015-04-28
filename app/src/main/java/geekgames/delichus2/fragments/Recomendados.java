package geekgames.delichus2.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import geekgames.delichus2.MainActivity;
import geekgames.delichus2.MainApplication;
import geekgames.delichus2.R;
import geekgames.delichus2.adapters.RecipeAdapter;
import geekgames.delichus2.customObjects.Recipe;


public class Recomendados extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.recomendados, container, false);

        return rootView;
    }

    private RecipeAdapter mAdapter;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

       /* mAdapter = new RecipeAdapter(getActivity());

        ListView listView = (ListView) getView().findViewById(R.id.recomendadosList);
        listView.setAdapter(mAdapter);*/
        fetch();
    }

    private void fetch() {
        JsonObjectRequest request = new JsonObjectRequest(
                "http://www.geekgames.info/dbadmin/test.php?v=10",
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        try {
                            //List<Recipe> recipeRecords = parse(jsonObject);
                            setLabels(jsonObject);
                            //mAdapter.swapRecipeRecords(recipeRecords);
                        }
                        catch(JSONException e) {
                            Toast.makeText(getActivity(), "Unable to parse data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(getActivity(), "Unable to fetch data: " + volleyError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        MainApplication.getInstance().getRequestQueue().add(request);
    }

    private void setLabels(JSONObject jsonImage) throws JSONException{

        int id = jsonImage.getInt("id");
        String receta = jsonImage.getString("receta");
        String larga = jsonImage.getString("larga");
        String imagen = jsonImage.getString("imagen");
        int idAutor = Integer.parseInt(jsonImage.getString("idAutor"));
        String autor = jsonImage.getString("autor");
        String foto = jsonImage.getString("foto");
        String puntuacion = jsonImage.getString("puntuacion");
        String descripcion = jsonImage.getString("descripcion");
        int pasos = jsonImage.getInt("pasos");
        JSONArray steps = jsonImage.getJSONArray("steps");

        final Recipe recipeRecord = new Recipe(id, receta, larga, imagen, idAutor, autor, foto, puntuacion, descripcion, pasos, steps);

        TextView nombre = (TextView) getView().findViewById(R.id.recipe_nombre);
        ImageView imagenV = (ImageView) getView().findViewById(R.id.recipe_imagen);
        imagenV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainApplication.getInstance().laReceta = recipeRecord;
                Log.i("FUCKING DEBUG", "la receta es " + MainApplication.getInstance().laReceta.nombre);
                ((MainActivity)getActivity()).exploreRecipe(v);
            }
        });
        ImageView favBtn = (ImageView)getView().findViewById(R.id.recomendado_fav);
        favBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("FUCKING DEBUG", "se va a adherir " + recipeRecord.nombre +" como favorito" );
                MainApplication.getInstance().addFav(MainApplication.getInstance().usuario.id, recipeRecord.id );
            }
        });
        ImageView fotoV = (ImageView) getView().findViewById(R.id.recipe_foto);
        TextView autorV = (TextView) getView().findViewById(R.id.recipe_autor);
        RatingBar puntuacionV = (RatingBar) getView().findViewById(R.id.recipe_puntuacion);
        TextView descripcionV = (TextView) getView().findViewById(R.id.recipe_descripcion);



        nombre.setText(recipeRecord.nombre);

        Picasso.with(getActivity()).load(recipeRecord.imagen).fit().centerCrop().into(imagenV);
        Picasso.with(getActivity()).load(recipeRecord.foto).fit().centerCrop().into(fotoV);
        autorV.setText(recipeRecord.autor);
        puntuacionV.setRating(Float.parseFloat(recipeRecord.puntuacion));
        descripcionV.setText(recipeRecord.descripcion);

    }


}
