package geekgames.delichus2.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import geekgames.delichus2.MainApplication;
import geekgames.delichus2.R;
import geekgames.delichus2.customObjects.Usuario;

public class Perfil extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.perfil, container, false);

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

            setLabels();

    }



    private void setLabels()  {

        final SharedPreferences app_preferences =
                PreferenceManager.getDefaultSharedPreferences(getActivity());

        ImageView profile = (ImageView) getView().findViewById(R.id.perfil_imagen);
        TextView nombre = (TextView) getView().findViewById(R.id.perfil_username);
        TextView titulo = (TextView) getView().findViewById(R.id.perfil_titulo);
        TextView nivel = (TextView) getView().findViewById(R.id.perfil_nivel);
        ProgressBar exp = (ProgressBar)getView().findViewById((R.id.expBar));
        TextView expLayout = (TextView)getView().findViewById(R.id.expLayout);

        int porcentaje = app_preferences.getInt("userPromedio",0);

        Picasso.with(getActivity()).load(app_preferences.getString("userFoto", "null")).resize(100, 100).centerCrop().into(profile);
        nombre.setText(app_preferences.getString("userNombre", "null"));
        titulo.setText(app_preferences.getString("userTitulo", "null"));

        exp.setProgress(porcentaje);
        expLayout.setText("Exp. "+porcentaje+" %");
        nivel.setText("lvl "+app_preferences.getInt("userNivel", 0));

    }


}