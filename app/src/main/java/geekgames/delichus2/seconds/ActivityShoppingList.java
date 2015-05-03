package geekgames.delichus2.seconds;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
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

import java.util.ArrayList;
import java.util.List;

import geekgames.delichus2.MainApplication;
import geekgames.delichus2.R;
import geekgames.delichus2.adapters.IngredienteAdapter;
import geekgames.delichus2.adapters.SeguidoAdapter;
import geekgames.delichus2.customObjects.Ingrediente;
import geekgames.delichus2.customObjects.Seguido;

public class ActivityShoppingList extends ActionBarActivity {

    private IngredienteAdapter mAdapter;
    int idUser;
    private String currentHeader = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lista_ingredientes);

        //getActionBar().setDisplayHomeAsUpEnabled(true);

        mAdapter = new IngredienteAdapter(this);

        ListView listView = (ListView) findViewById(R.id.list_shopping);
        listView.setAdapter(mAdapter);
        setTitle("Lista de compras");



        //fetch();
        //List<Ingrediente> shoppingList = MainApplication.getInstance().shoppingList;

       // mAdapter.swapRecords(shoppingList);*/



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
                onBackPressed();
                return true;


            /*case R.id.action_settings:
                return true;*/

        }

        return super.onOptionsItemSelected(item);
    }



}
