package geekgames.delichus2.seconds;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import geekgames.delichus2.MainApplication;
import geekgames.delichus2.R;
import geekgames.delichus2.adapters.SeguidoAdapter;
import geekgames.delichus2.adapters.SimpleRecipeAdapter;
import geekgames.delichus2.customObjects.Seguido;
import geekgames.delichus2.customObjects.SimpleRecipe;

public class ActivitySeguidos extends ActionBarActivity {

    private SeguidoAdapter mAdapter;
    int idUser;
    private String currentHeader = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.seguidos);

        //getActionBar().setDisplayHomeAsUpEnabled(true);

        mAdapter = new SeguidoAdapter(this);

        ListView listView = (ListView) findViewById(R.id.list_seguidos);
        listView.setAdapter(mAdapter);
        setTitle("Personas que sigues");

        final SharedPreferences app_preferences =
                PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        idUser = app_preferences.getInt("id",0);

        fetch();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        switch (id){
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;


            /*case R.id.action_settings:
                return true;*/

        }

        return super.onOptionsItemSelected(item);
    }

    private void fetch() {

        JsonObjectRequest request = new JsonObjectRequest(
                "http://www.geekgames.info/dbadmin/test.php?v=18&userId="+MainApplication.getInstance().usuario.id,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        try {
                            currentHeader = "";
                            List<Seguido> segsRecords = parse(jsonObject);

                            mAdapter.swapRecords(segsRecords);
                        }
                        catch(JSONException e) {
                            Toast.makeText(getApplicationContext(), "Unable to parse data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(getApplicationContext(), "Unable to fetch data: " + volleyError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        MainApplication.getInstance().getRequestQueue().add(request);
        //MainApplication.getInstance().fetchUserAchievements(  MainApplication.getInstance().getUserId() );
    }

    private List<Seguido> parse(JSONObject json) throws JSONException {
        ArrayList<Seguido> records = new ArrayList<Seguido>();

        JSONArray segs = json.getJSONArray("seguidos");

        for(int i =0; i < segs.length(); i++) {
            JSONObject jsonContent = segs.getJSONObject(i);
            JSONObject jsonImage = jsonContent.getJSONObject("seguido");
            int id = jsonImage.getInt("id");
            String nombre = jsonImage.getString("nombre");
            String foto = jsonImage.getString("foto");
            String titulo = jsonImage.getString("titulo");
            int lvl = Integer.parseInt(jsonImage.getString("nivel"));

            Seguido record = new Seguido(id, foto, nombre, titulo, lvl);
            records.add(record);
        }

        return records;
    }


}
