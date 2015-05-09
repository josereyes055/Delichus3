package geekgames.delichus2.seconds;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.widget.ListView;

import org.json.JSONException;

import java.util.List;

import geekgames.delichus2.R;
import geekgames.delichus2.adapters.LogroAdapter;
import geekgames.delichus2.customObjects.Logro;


public class RegistroUsuario extends ActionBarActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registro);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

}
