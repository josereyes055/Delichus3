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
import geekgames.delichus2.customObjects.Ficha;
import geekgames.delichus2.customObjects.Ingrediente;
import geekgames.delichus2.customObjects.RecetaExtended;
import geekgames.delichus2.customObjects.Recipe;
import geekgames.delichus2.customObjects.Usuario;

public class DescripcionReceta extends Fragment {

    public IngredienteAdapter mAdapter;
    public ComentarioAdapter mAdapter2;
    public ListView listView;
    public ListView listComentarios;
    public JSONObject fullReceta;

    TextView nombre;
    TextView autor;
    TextView larga;
    RatingBar rating;
    ImageView imagen;
    ImageView foto;
    ImageView myPhoto;
    TextView myName;
    ImageView difImg;
    TextView difText;
    TextView tiempoReceta;
    TextView cantidadPersonas;

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
        Ficha laReceta = MainApplication.getInstance().laReceta;

        nombre = (TextView)getView().findViewById(R.id.receta_nombre);
        autor = (TextView)getView().findViewById(R.id.receta_autor);
        larga = (TextView)getView().findViewById(R.id.receta_larga);
        rating = (RatingBar)getView().findViewById(R.id.receta_rating);
        imagen = (ImageView)getView().findViewById(R.id.receta_imagen);
        foto = (ImageView)getView().findViewById(R.id.receta_foto);
        myPhoto = (ImageView) getView().findViewById(R.id.my_photo);
        myName = (TextView)getView().findViewById(R.id.my_name);

        difImg = (ImageView)getView().findViewById(R.id.dificultad_medidor);
        difText = (TextView)getView().findViewById(R.id.dificultad_receta);
        tiempoReceta = (TextView)getView().findViewById(R.id.tiempo_receta);
        cantidadPersonas = (TextView)getView().findViewById(R.id.cantidad_personas_receta);


        fetchReceta(laReceta.id);
        //fetchIngredientes(laReceta.id);
        //fetchComentarios(laReceta.id);
    }

    private void fetchReceta(int idReceta){
        JsonObjectRequest request = new JsonObjectRequest(
                "http://www.geekgames.info/dbadmin/test.php?v=6&recipeId="+idReceta,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        try {

                            setLabels(jsonObject);
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

    private void setLabels(JSONObject laReceta) throws JSONException {

        Usuario me = MainApplication.getInstance().usuario;

        nombre.setText(laReceta.getString("receta"));
        autor.setText(laReceta.getString("autor"));
        larga.setText(laReceta.getString("larga"));

        cantidadPersonas.setText( Integer.toString( laReceta.getInt("personas") ) );
        tiempoReceta.setText( laReceta.getInt("tiempoTotal")/60+" mins" );

        int dificultad = laReceta.getInt("dificultad");
        Drawable mecagoenDios = null;
        if(dificultad < 3 ){
            mecagoenDios = getResources().getDrawable(R.drawable.dificultad_facil);
            difText.setText("facil");
        }
        if(dificultad>2 && dificultad<5 ){
            mecagoenDios = getResources().getDrawable(R.drawable.dificultad_media);
            difText.setText("media");
        }
        if(dificultad>4){
            mecagoenDios = getResources().getDrawable(R.drawable.dificultad_dificil);
            difText.setText("difícil");
        }
        difImg.setImageDrawable(mecagoenDios);

        myName.setText(me.nombre + " - " + me.titulo);
        rating.setRating(Float.parseFloat( laReceta.getString("puntuacion") ) );
        Picasso.with(getActivity()).load(laReceta.getString("imagen")).fit().centerCrop().into(imagen);
        Picasso.with(getActivity()).load(laReceta.getString("foto")).fit().centerCrop().into(foto);
        Picasso.with(getActivity()).load(me.foto).fit().centerCrop().into(myPhoto);

        List<Ingrediente> ingredientes = parse( laReceta.getJSONArray("ingredientes") );
        mAdapter.swapRecords(ingredientes);
        setListViewHeightBasedOnChildren(listView);

        List<Comentario> comentarios = parseComentarios( laReceta.getJSONArray("comentarios") );
        mAdapter2.swapRecords(comentarios);
        setListViewHeightBasedOnChildren(listComentarios);

        MainApplication.getInstance().losPasos = laReceta.getJSONArray("steps");
    }


    private List<Ingrediente> parse(JSONArray json) throws JSONException {
        ArrayList<Ingrediente> records = new ArrayList<Ingrediente>();

        JSONArray holder= json;

        for (int i=0; i<holder.length(); i++){
            JSONObject ing = holder.getJSONObject(i);
            String nombre = ing.getString("nombre");
            Double cantidad = ing.getDouble("cantidad");
            String unidad = ing.getString("medida");

            Ingrediente ingrediente = new Ingrediente(nombre, cantidad, unidad);
            Log.i("FUCKING DEBUG", "ingrediente añadido: "+nombre);
            records.add(ingrediente);
        }

        //MainApplication.getInstance().laReceta.ingredientes = records;
        return records;
    }

    private List<Comentario> parseComentarios(JSONArray json) throws JSONException {
        ArrayList<Comentario> records = new ArrayList<Comentario>();

        JSONArray holder= json;

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
