package geekgames.delichus2.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import geekgames.delichus2.MainApplication;
import geekgames.delichus2.R;
import geekgames.delichus2.Receta;
import geekgames.delichus2.adapters.RecipeAdapter;
import geekgames.delichus2.customObjects.Recipe;


public class Todas extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.todas, container, false);

        return rootView;
    }

    private ListView listViewLeft;
    private ListView listViewRight;
    private RecipeAdapter leftAdapter;
    private RecipeAdapter rightAdapter;
    List<Recipe> lista1;
    List<Recipe> lista2;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        leftAdapter = new RecipeAdapter(getActivity());
        rightAdapter = new RecipeAdapter(getActivity());

        listViewLeft = (ListView) getView().findViewById(R.id.all_list_view_left);
        listViewRight = (ListView) getView().findViewById(R.id.all_list_view_right);

        listViewLeft.setAdapter(leftAdapter);
        listViewRight.setAdapter(rightAdapter);

        listViewLeft.setOnTouchListener(touchListener);
        listViewRight.setOnTouchListener(touchListener);
        //listViewLeft.setOnScrollListener(scrollListener);
        //listViewRight.setOnScrollListener(scrollListener);

        Spinner spinner = (Spinner) getView().findViewById(R.id.filtro_receta);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.filtros_recetas, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner

        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        fetch();
    }


    // Passing the touch event to the opposite list
    View.OnTouchListener touchListener = new View.OnTouchListener() {
        boolean dispatched = false;

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (v.equals(listViewLeft) && !dispatched) {
                dispatched = true;
                listViewRight.dispatchTouchEvent(event);
            } else if (v.equals(listViewRight) && !dispatched) {
                dispatched = true;
                listViewLeft.dispatchTouchEvent(event);
            } // similarly for listViewThree & listViewFour
            dispatched = false;
            return false;
        }
    };


    private void fetch() {
        JsonObjectRequest request = new JsonObjectRequest(
                "http://www.geekgames.info/dbadmin/test.php?v=1",
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        try {
                            lista1 = new ArrayList<Recipe>();
                            lista2 = new ArrayList<Recipe>();


                            JSONArray jsonImages = jsonObject.getJSONArray("recipes");

                            for(int i =0; i < jsonImages.length(); i++) {
                                JSONObject jsonImage = jsonImages.getJSONObject(i);
                                int id = jsonImage.getInt("id");
                                String receta = jsonImage.getString("receta");
                                String larga = jsonImage.getString("larga");
                                String imagen = jsonImage.getString("imagen");
                               int idautor = Integer.parseInt(jsonImage.getString("idAutor"));
                                String autor = jsonImage.getString("autor");
                                String foto = jsonImage.getString("foto");
                                String puntuacion = jsonImage.getString("puntuacion");
                                String descripcion = jsonImage.getString("descripcion");
                                int pasos = jsonImage.getInt("pasos");
                                int cantidad = jsonImage.getInt("personas");
                                int dificultad = jsonImage.getInt("dificultad");
                                int tiempo = jsonImage.getInt("tiempoTotal");
                                JSONArray steps = jsonImage.getJSONArray("steps");

                                Recipe record = new Recipe(id, receta, larga, imagen,idautor, autor, foto, puntuacion, descripcion, pasos,cantidad, dificultad, tiempo,  steps);
                                if(i%2 == 0) {
                                    lista1.add(record);
                                }else{
                                    lista2.add(record);
                                }
                            }

                            leftAdapter.swapRecipeRecords(lista1);
                            rightAdapter.swapRecipeRecords(lista2);
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

    private List<Recipe> parse(JSONObject json) throws JSONException {
        ArrayList<Recipe> records = new ArrayList<Recipe>();



        return records;
    }

    
}