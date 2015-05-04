package geekgames.delichus2.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
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
import geekgames.delichus2.adapters.FichaAdapter;
import geekgames.delichus2.customObjects.Ficha;
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
    private FichaAdapter leftAdapter;
    private FichaAdapter rightAdapter;
    public JSONArray recetas;
    List<Ficha> lista1;
    List<Ficha> lista2;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getRecetas();
        leftAdapter = new FichaAdapter(getActivity());
        rightAdapter = new FichaAdapter(getActivity());

        listViewLeft = (ListView) getView().findViewById(R.id.all_list_view_left);
        listViewRight = (ListView) getView().findViewById(R.id.all_list_view_right);

        listViewLeft.setAdapter(leftAdapter);
        listViewRight.setAdapter(rightAdapter);

        listViewLeft.setOnTouchListener(touchListener);
        listViewRight.setOnTouchListener(touchListener);
        //listViewLeft.setOnScrollListener(scrollListener);
        //listViewRight.setOnScrollListener(scrollListener);

        // A more complicated dynamic way
        String[] spinnerItems = getResources().getStringArray(R.array.filtros_recetas);
        // create your own spinner array adapter
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(getActivity(), R.layout.spinner_custom,spinnerItems){
            public View getView(int position, View convertView, ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                ((TextView) v).setGravity(Gravity.CENTER);
                ((TextView) v).setTextSize(22);
                return v;
            }
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View v = super.getDropDownView(position, convertView,parent);
                ((TextView) v).setGravity(Gravity.CENTER);
                ((TextView) v).setHeight(69);
                return v;
            }
        };
        Spinner spinner = (Spinner) getView().findViewById(R.id.filtro_receta);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
            R.array.filtros_recetas, R.layout.spinner_custom);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner*/

        //spinner.setAdapter(adapter);
        spinner.setAdapter(adapter2);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        //fetch();
        setAllRecipeList();
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

    private void getRecetas(){
        SharedPreferences app_preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String prefArray = app_preferences.getString("recetas", null);
        Log.i("FUCKING DEBUG", prefArray);
        if( prefArray != null ){
            try {
                recetas = new JSONArray(prefArray);
            } catch (JSONException e) {
                Toast.makeText(getActivity(), "Unable to parse data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }
    private void setAllRecipeList(){

        if(recetas != null){
            try {
                lista1 = new ArrayList<Ficha>();
                lista2 = new ArrayList<Ficha>();

                for(int i =0; i < recetas.length(); i++) {
                    JSONObject ficha = recetas.getJSONObject(i);
                    int id = ficha.getInt("id");
                    String nombre = ficha.getString("receta");
                    String imagen = ficha.getString("imagen");
                    int idAutor = Integer.parseInt(ficha.getString("idAutor"));
                    String autor = ficha.getString("autor");
                    String foto = ficha.getString("foto");
                    float puntuacion = Float.parseFloat( ficha.getString("puntuacion") );
                    String descripcion = ficha.getString("descripcion");
                    int pasos = ficha.getInt("pasos");

                    Ficha unaFicha = new Ficha(id, nombre, imagen, idAutor, autor, foto, puntuacion, descripcion, pasos);
                    if(i%2 == 0) {
                        lista1.add(unaFicha);
                    }else{
                        lista2.add(unaFicha);
                    }
                }

                leftAdapter.swapRecipeRecords(lista1);
                rightAdapter.swapRecipeRecords(lista2);
            }
            catch(JSONException e) {
                Toast.makeText(getActivity(), "Unable to parse data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }

    }// end setAllRecipeList

    private void fetch() {
        JsonObjectRequest request = new JsonObjectRequest(
                "http://www.geekgames.info/dbadmin/test.php?v=1",
                null,
                new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject jsonObject) {
                                try {
                                    lista1 = new ArrayList<Ficha>();
                                    lista2 = new ArrayList<Ficha>();


                            JSONArray Recetas = jsonObject.getJSONArray("recipes");

                            for(int i =0; i < Recetas.length(); i++) {
                                JSONObject ficha = Recetas.getJSONObject(i);
                                int id = ficha.getInt("id");
                                String nombre = ficha.getString("receta");
                                String imagen = ficha.getString("imagen");
                                int idAutor = Integer.parseInt(ficha.getString("idAutor"));
                                String autor = ficha.getString("autor");
                                String foto = ficha.getString("foto");
                                float puntuacion = Float.parseFloat( ficha.getString("puntuacion") );
                                String descripcion = ficha.getString("descripcion");
                                int pasos = ficha.getInt("pasos");

                                Ficha unaFicha = new Ficha(id, nombre, imagen, idAutor, autor, foto, puntuacion, descripcion, pasos);
                                if(i%2 == 0) {
                                    lista1.add(unaFicha);
                                }else{
                                    lista2.add(unaFicha);
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