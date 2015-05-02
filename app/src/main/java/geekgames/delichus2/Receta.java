package geekgames.delichus2;

import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.ToggleButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

import geekgames.delichus2.adapters.PasoAdapter;
import geekgames.delichus2.customObjects.Recipe;
import geekgames.delichus2.fragments.ActivarAyudante;
import geekgames.delichus2.fragments.CompleteReceta;
import geekgames.delichus2.fragments.DescripcionReceta;
import geekgames.delichus2.fragments.PasoFragment;
import geekgames.delichus2.seconds.OtherUserPage;

public class Receta extends ActionBarActivity{

    Recipe laReceta = MainApplication.getInstance().laReceta;

    SectionsPagerAdapter mSectionsPagerAdapter;
    PasoAdapter mAdapter;
    //CustomPager mViewPager;
    ViewPager mViewPager;
    TextToSpeech ttobj;
    boolean laVieja = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.receta);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        setTitle(laReceta.nombre);


        //getActionBar().setDisplayHomeAsUpEnabled(true);
        ttobj=new TextToSpeech(getApplicationContext(),
                new TextToSpeech.OnInitListener() {
                    @Override
                    public void onInit(int status) {
                        if(status != TextToSpeech.ERROR){
                            Locale locSpanish = new Locale("spa", "MEX");
                            ttobj.setLanguage(locSpanish);
                        }
                    }
                });

    }

    @Override
    public void onPause(){
        if(ttobj !=null){
            ttobj.stop();
            ttobj.shutdown();
        }
        super.onPause();
    }

    public void viewOtherUser(View elView){
        Intent mainIntent = new Intent().setClass(
                Receta.this, OtherUserPage.class);
        startActivity(mainIntent);
    }

    public void shutUpVieja(View view){
        laVieja = false;
        ToggleButton tb = (ToggleButton)findViewById(R.id.viejaToggle);
        tb.setChecked(false);
    }
    public void letItBeVieja(View view){
        laVieja = true;
        ToggleButton tb = (ToggleButton)findViewById(R.id.viejaToggle);
        tb.setChecked(true);
    }
    public void toggleVieja(View view){
        laVieja = !laVieja;
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

    public void beginRecipe(View view) {
        mViewPager.setCurrentItem(1);

    }

    public void selectAllIngredients(View view){
        ListView listaIngrediente = (ListView) findViewById(R.id.lista_ingredientes);
        int totalIngredientes = listaIngrediente.getChildCount();
        Button selectAllButton = (Button) findViewById(R.id.select_all);
        CheckBox checkBox = null;
        boolean allChecked = true;

        for (int i = 0; i < totalIngredientes; i++){
            checkBox = (CheckBox) listaIngrediente.getChildAt(i).findViewById(R.id.checkBox);
            allChecked = allChecked && checkBox.isChecked();
        }

        for (int i = 0; i < totalIngredientes; i++){
            checkBox = (CheckBox) listaIngrediente.getChildAt(i).findViewById(R.id.checkBox);
            checkBox.setChecked(!allChecked);
        }

        if(allChecked) {
            selectAllButton.setBackground(this.getResources().getDrawable(R.color.verde));
        } else {
            selectAllButton.setBackground(this.getResources().getDrawable(R.color.naranja));
        }
    }

    public void carrito_ingredientes(View view) {
        ListView listaIngrediente = (ListView) findViewById(R.id.lista_ingredientes);
        int totalIngredientes = listaIngrediente.getChildCount();
        for (int i = 0; i < totalIngredientes; i++){
            CheckBox checkBox = (CheckBox) listaIngrediente.getChildAt(i).findViewById(R.id.checkBox);
            if(!checkBox.isChecked()){
                Log.i("FUCKING DEBUG", "agregando item " + i+" a la shoppingList");
                Recipe laReceta = MainApplication.getInstance().laReceta;
                //MainApplication.getInstance().shoppingList.add(laReceta.ingredientes.get(i));
            }
        }
        Toast.makeText(this, "Ingredientes agregados a la lista de compras", Toast.LENGTH_SHORT).show();
    }

    public void validateCheckbox(){
        ListView listaIngrediente = (ListView) findViewById(R.id.lista_ingredientes);
        int totalIngredientes = listaIngrediente.getChildCount();
        Button selectAllButton = (Button) findViewById(R.id.select_all);
        Button comenzar = (Button) findViewById((R.id.botonComenzar));
        CheckBox checkBox = null;
        boolean allChecked = true;

        for (int i = 0; i < totalIngredientes; i++){
            checkBox = (CheckBox) listaIngrediente.getChildAt(i).findViewById(R.id.checkBox);
            allChecked = allChecked && checkBox.isChecked();
        }

        if(allChecked) {
            selectAllButton.setBackground(this.getResources().getDrawable(R.color.naranja));
            comenzar.setBackground(this.getResources().getDrawable(R.color.verde));
            comenzar.setEnabled(true);
        } else {
            selectAllButton.setBackground(this.getResources().getDrawable(R.color.verde));
            comenzar.setBackground(this.getResources().getDrawable(R.color.disable));
            comenzar.setEnabled(false);
        }
    }

    public void onCameraClick(View view){

        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        startActivity(intent);
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }


        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).

            String text = laReceta.larga;
            int totalPasos = laReceta.pasos+1;

            switch (position){
                case 0:
                    //text = laReceta.getLarga();
                    //ttobj.speak(text, TextToSpeech.QUEUE_FLUSH, null);
                    return new DescripcionReceta();


                case 1:
                    return new ActivarAyudante();

                default:
                    if(position == totalPasos){
                        //Log.i("FUCKING DEBUG", "se sumo el puntaje");
                        MainApplication.getInstance().usuario.sumarPuntaje(10);
                        return new CompleteReceta();
                    }else{
                        JSONArray pasos = MainApplication.getInstance().laReceta.steps;
                        try {
                            JSONObject paso = pasos.getJSONObject(position-3);
                            text = paso.getString("paso");
                            if(laVieja) {
                                ttobj.speak(text, TextToSpeech.QUEUE_FLUSH, null);
                            }

                        } catch (JSONException e) {

                        }
                        return PasoFragment.newInstance(position-2);
                    }

            }//end case

        }//end getitem

        @Override
        public int getCount() {
            return laReceta.pasos+2;
        }
    }




}
