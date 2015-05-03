package geekgames.delichus2.fragments;


import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListAdapter;
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

import geekgames.delichus2.MainApplication;
import geekgames.delichus2.R;
import geekgames.delichus2.Receta;
import geekgames.delichus2.adapters.ComentarioAdapter;
import geekgames.delichus2.adapters.IngredienteAdapter;
import geekgames.delichus2.adapters.LogroAdapter;
import geekgames.delichus2.customObjects.Comentario;
import geekgames.delichus2.customObjects.Ingrediente;
import geekgames.delichus2.customObjects.RecetaExtended;
import geekgames.delichus2.customObjects.Recipe;
import geekgames.delichus2.customObjects.Usuario;

public class DescripcionReceta extends Fragment {

    public IngredienteAdapter mAdapter;
    public ComentarioAdapter mAdapter2;
    public ListView listView;
    public ListView listComentarios;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.receta_intro, container, false);
        listView = (ListView) rootView.findViewById(R.id.lista_ingredientes);
        mAdapter = new IngredienteAdapter(getActivity());
        listView.setAdapter(mAdapter);

        listComentarios = (ListView) rootView.findViewById((R.id.lista_comentarios));
        mAdapter2 = new ComentarioAdapter(getActivity());
        listComentarios.setAdapter(mAdapter2);

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        TextView nombre = (TextView)getView().findViewById(R.id.receta_nombre);
        TextView autor = (TextView)getView().findViewById(R.id.receta_autor);
        TextView larga = (TextView)getView().findViewById(R.id.receta_larga);
        RatingBar rating = (RatingBar)getView().findViewById(R.id.receta_rating);
        ImageView imagen = (ImageView)getView().findViewById(R.id.receta_imagen);
        ImageView foto = (ImageView)getView().findViewById(R.id.receta_foto);
        ImageView myPhoto = (ImageView) getView().findViewById(R.id.my_photo);
        TextView myName = (TextView)getView().findViewById(R.id.my_name);

        ImageView difImg = (ImageView)getView().findViewById(R.id.dificultad_medidor);
        TextView difText = (TextView)getView().findViewById(R.id.dificultad_receta);
        TextView tiempoReceta = (TextView)getView().findViewById(R.id.tiempo_receta);
        TextView cantidadPersonas = (TextView)getView().findViewById(R.id.cantidad_personas_receta);


        Recipe laReceta = MainApplication.getInstance().laReceta;
        Usuario me = MainApplication.getInstance().usuario;

        nombre.setText(laReceta.nombre);
        autor.setText(laReceta.autor);
        larga.setText(laReceta.larga);

        cantidadPersonas.setText(Integer.toString(laReceta.cantidad));
        tiempoReceta.setText(laReceta.tiempo+" mins");

        Drawable mecagoenDios = null;
        if(laReceta.dificultad < 3 ){
             mecagoenDios = getResources().getDrawable(R.drawable.dificultad_facil);
            difText.setText("facil");
        }
        if(laReceta.dificultad>2 && laReceta.dificultad<5 ){
            mecagoenDios = getResources().getDrawable(R.drawable.dificultad_media);
            difText.setText("media");
        }
        if(laReceta.dificultad>4){
            mecagoenDios = getResources().getDrawable(R.drawable.dificultad_dificil);
            difText.setText("difícil");
        }
        difImg.setImageDrawable(mecagoenDios);

        myName.setText(me.nombre + " - " + me.titulo);
        rating.setRating(Float.parseFloat(laReceta.puntuacion));
        Picasso.with(getActivity()).load(laReceta.imagen).fit().centerCrop().into(imagen);
        Picasso.with(getActivity()).load(laReceta.foto).fit().centerCrop().into(foto);
        Picasso.with(getActivity()).load(me.foto).fit().centerCrop().into(myPhoto);

        fetch(laReceta.id);
        fetchComentarios(laReceta.id);



    }

    private void fetch(int idReceta) {
        JsonObjectRequest request = new JsonObjectRequest(
                "http://www.geekgames.info/dbadmin/test.php?v=16&recipeId="+idReceta,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        try {
                            List<Ingrediente> recipeRecords = parse(jsonObject);

                            mAdapter.swapRecords(recipeRecords);
                            setListViewHeightBasedOnChildren(listView);
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

    private void fetchComentarios(int idReceta) {
        JsonObjectRequest request = new JsonObjectRequest(
                "http://www.geekgames.info/dbadmin/test.php?v=19&recipeId="+idReceta,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        try {
                            List<Comentario> comentariosRecords = parseComentarios(jsonObject);

                            mAdapter2.swapRecords(comentariosRecords);
                            setListViewHeightBasedOnChildren(listComentarios);
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

    private List<Ingrediente> parse(JSONObject json) throws JSONException {
        ArrayList<Ingrediente> records = new ArrayList<Ingrediente>();

        JSONArray holder= json.getJSONArray("ingredientes");

        for (int i=0; i<holder.length(); i++){
            JSONObject ing = holder.getJSONObject(i);
            String nombre = ing.getString("nombre");
            Double cantidad = ing.getDouble("cantidad");
            String unidad = ing.getString("medida");

            Ingrediente ingrediente = new Ingrediente(nombre, cantidad, unidad);
            Log.i("FUCKING DEBUG", "ingrediente añadido: "+nombre);
            records.add(ingrediente);
        }

        MainApplication.getInstance().laReceta.ingredientes = records;
        return records;
    }

    private List<Comentario> parseComentarios(JSONObject json) throws JSONException {
        ArrayList<Comentario> records = new ArrayList<Comentario>();

        JSONArray holder= json.getJSONArray("comentarios");

        for (int i=0; i<holder.length(); i++){
            JSONObject ing = holder.getJSONObject(i);
            String foto = ing.getString("foto");
            String autor = ing.getString("autor");
            String coment = ing.getString("comentario");

            Comentario comentario = new Comentario(foto, autor, coment);
            Log.i("FUCKING DEBUG", "comentario añadido: "+autor);
            records.add(comentario);
        }


        return records;
    }

    /**** Method for Setting the Height of the ListView dynamically.
     **** Hack to fix the issue of not showing all the items of the ListView
     **** when placed inside a ScrollView  ****/
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null)
            return;

        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        View view = null;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i, view, listView);
            if (i == 0)
                view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, AbsListView.LayoutParams.WRAP_CONTENT));

            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
            Log.i("FUCKING DEBUG", "alto: "+totalHeight);
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1))+80;
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

}
