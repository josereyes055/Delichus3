package geekgames.delichus2.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import geekgames.delichus2.MainApplication;
import geekgames.delichus2.R;

public class PasoFragment extends Fragment{
    View laView;
    Bundle saved;

    /**
     * Create a new instance of DetailsFragment, initialized to
     * show the text at 'index'.
     */
    public static PasoFragment newInstance(int index) {
        PasoFragment f = new PasoFragment();
        // Supply index input as an argument.
        Bundle args = new Bundle();
        args.putInt("index", index);
        f.setArguments(args);
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.receta_paso, container, false);
        laView = rootView;
        return rootView;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        TextView tipo = (TextView)laView.findViewById(R.id.paso_tipo);
        TextView descripcion = (TextView)laView.findViewById(R.id.paso_descripcion);
        TextView tiempo = (TextView)laView.findViewById(R.id.paso_tiempo);

        Bundle args = getArguments();
        int index = args.getInt("index", 0);

        JSONArray pasos = MainApplication.getInstance().laReceta.steps;
        try {
            JSONObject paso = pasos.getJSONObject(index);
            JSONObject unArray = MainApplication.getInstance().tipo_pasos;
            String indexTipo = String.valueOf(paso.getInt("tipo"));
            String elTipo = unArray.getString(indexTipo);

            tipo.setText(elTipo);
            descripcion.setText(paso.getString("paso"));
            tiempo.setText("tiempo: "+paso.getInt("tiempo"));

        } catch (JSONException e) {
            Toast.makeText(getActivity(), "Unable to parse data: " + e, Toast.LENGTH_LONG).show();
        }

    }




}
