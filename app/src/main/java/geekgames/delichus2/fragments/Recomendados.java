package geekgames.delichus2.fragments;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.PowerManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import geekgames.delichus2.MainActivity;
import geekgames.delichus2.MainApplication;
import geekgames.delichus2.R;
import geekgames.delichus2.adapters.FichaAdapter;
import geekgames.delichus2.customObjects.Ficha;


public class Recomendados extends Fragment {

    MediaPlayer mp;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.recomendados, container, false);

        return rootView;
    }

    private FichaAdapter mAdapter;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mp = MediaPlayer.create(getActivity(),R.raw.sonido_boton);
        fetch();
    }

    private void fetch() {
        JsonObjectRequest request = new JsonObjectRequest(
                "http://www.geekgames.info/dbadmin/test.php?v=5",
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

    private void setLabels(JSONObject ficha) throws JSONException{

        int id = ficha.getInt("id");
        String nombre = ficha.getString("receta");
        String imagen = ficha.getString("imagen");
        int idAutor = Integer.parseInt(ficha.getString("idAutor"));
        String autor = ficha.getString("autor");
        String foto = ficha.getString("foto");
        float puntuacion = Float.parseFloat(ficha.getString("puntuacion"));
        String descripcion = ficha.getString("descripcion");
        int pasos = ficha.getInt("pasos");

        final Ficha unaFicha = new Ficha(id, nombre, imagen, idAutor, autor, foto, puntuacion, descripcion, pasos);


        TextView tnombre = (TextView) getView().findViewById(R.id.recipe_nombre);
        ImageView imagenV = (ImageView) getView().findViewById(R.id.recipe_imagen);
        imagenV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainApplication.getInstance().laReceta = unaFicha;
                Log.i("FUCKING DEBUG", "la receta es " + MainApplication.getInstance().laReceta.nombre);
                ((MainActivity)getActivity()).exploreRecipe(v);
            }
        });
        ImageView favBtn = (ImageView)getView().findViewById(R.id.recomendado_fav);
        favBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Log.i("FUCKING DEBUG", "se va a adherir " + unaFicha.nombre +" como favorito" );
               mp.start();
               MainApplication.getInstance().addFav(MainApplication.getInstance().usuario.id, unaFicha.id );

            }
        });
        ImageView fotoV = (ImageView) getView().findViewById(R.id.recipe_foto);
        TextView autorV = (TextView) getView().findViewById(R.id.recipe_autor);
        RatingBar puntuacionV = (RatingBar) getView().findViewById(R.id.recipe_puntuacion);
        TextView descripcionV = (TextView) getView().findViewById(R.id.recipe_descripcion);



        tnombre.setText(unaFicha.nombre);

        Picasso.with(getActivity()).load(unaFicha.imagen).fit().centerCrop().into(imagenV);
        Picasso.with(getActivity()).load(unaFicha.foto).fit().centerCrop().into(fotoV);
        autorV.setText(unaFicha.autor);
        puntuacionV.setRating(unaFicha.puntuacion);

    }


}
