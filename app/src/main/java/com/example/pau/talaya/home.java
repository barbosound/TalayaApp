package com.example.pau.talaya;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

import static com.example.pau.talaya.LoginActivity.usuariActiu;

/**
 * Created by Pau on 15/3/17.
 */

public class home extends AppCompatActivity implements ListCases.OnFragmentInteractionListener,
        pendents.OnFragmentInteractionListener,
        missatges.OnFragmentInteractionListener,Perfil.OnFragmentInteractionListener,
        finalitzades.OnFragmentInteractionListener, favorits.OnFragmentInteractionListener,
        Notificacions.OnFragmentInteractionListener, QuiSom.OnFragmentInteractionListener,
        AvisLegal.OnFragmentInteractionListener{

    ListCases listCases = new ListCases();
    missatges miss = new missatges();
    Perfil perfil = new Perfil();

    FragmentManager fM = getSupportFragmentManager();

    private ArrayList<String> id = new ArrayList<>();
    private ArrayList<String> nom = new ArrayList<>();
    private ArrayList<String> capacitat = new ArrayList<>();
    private ArrayList<String> comarca = new ArrayList<>();
    private ArrayList<String> rating = new ArrayList<>();

    private ArrayList<String> idReserva = new ArrayList<>();
    private ArrayList<String> preuReserva = new ArrayList<>();
    private ArrayList<String> diesReserva = new ArrayList<>();
    private ArrayList<String> DataEntrada = new ArrayList<>();
    private ArrayList<String> DataSortida = new ArrayList<>();
    private ArrayList<String> FKUsuari = new ArrayList<>();
    private ArrayList<String> FKCasa = new ArrayList<>();
    private ArrayList<String> Estat = new ArrayList<>();

    private String IdUser;

    public static boolean teReserves = false;

    private AsyncHttpClient clientCasa, clientReserva;

    public Bundle bCasa = new Bundle();
    public Bundle bReserva = new Bundle();
    public ProgressDialog progress;

    private View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

        Window window = getWindow();
        window.setStatusBarColor(Color.parseColor("#4C9141"));

        Toolbar toolbar = (Toolbar) findViewById(R.id.tool);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_flecha_back));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("Resultats");

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigation);
        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);

        view = getWindow().getDecorView().getRootView();

        consultaApiCasa(view);

        consultaApiReserves(view);

        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.home:

                                getSupportActionBar().setTitle("Resultats");

                                if(!listCases.isAdded()){
                                    listCases.setArguments(bCasa);
                                    fM.beginTransaction().replace(R.id.frame,listCases).commit();
                                }else{
                                    fM.beginTransaction().show(listCases).commit();
                                }

                                break;
                            case R.id.missatges:

                                getSupportActionBar().setTitle("Missatges");

                                if(!miss.isAdded()){
                                    fM.beginTransaction().replace(R.id.frame,miss).commit();
                                }else{
                                    fM.beginTransaction().show(miss).commit();
                                }

                                break;
                            case R.id.perfil:

                                getSupportActionBar().setTitle("Perfil");

                                if(!perfil.isAdded()){
                                    perfil.setArguments(bReserva);
                                    fM.beginTransaction().replace(R.id.frame,perfil).commit();
                                }else{
                                    fM.beginTransaction().show(perfil).commit();
                                }
                                break;
                            case R.id.surt:

                                View v = getWindow().getDecorView().getRootView();

                                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());

                                builder.setTitle("Confirm");
                                builder.setMessage("Estas segur que vols sortir?");

                                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                                    public void onClick(DialogInterface dialog, int which) {

                                        Intent intencio = new Intent(home.this,LoginActivity.class);
                                        startActivity(intencio);
                                        dialog.dismiss();
                                    }
                                });

                                builder.setNegativeButton("NO",
                                        new DialogInterface.OnClickListener() {

                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {

                                                // Do nothing
                                                dialog.dismiss();
                                            }
                                        });

                                AlertDialog alert = builder.create();
                                alert.show();


                                break;

                        }
                        return true;
                    }
                });

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_toolbar,menu);

        return super.onCreateOptionsMenu(menu);
    }

    //MENU SETTINGS\\
    //carreguem els fragments al frame de home\\
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.notificacions:

                Notificacions notis = new Notificacions();

                if(!notis.isAdded())
                {fM.beginTransaction().replace(R.id.frame, notis).commit();}
                else
                {fM.beginTransaction().show(notis).commit();}

                break;
            case R.id.qui_som:

                QuiSom qui = new QuiSom();

                if(!qui.isAdded())
                {fM.beginTransaction().replace(R.id.frame, qui).commit();}
                else
                {fM.beginTransaction().show(qui).commit();}

                break;

            case R.id.avis_legal:

                AvisLegal avis = new AvisLegal();

                if(!avis.isAdded())
                {fM.beginTransaction().replace(R.id.frame, avis).commit();}
                else
                {fM.beginTransaction().show(avis).commit();}

                break;
        }



        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    private void consultaApiCasa(final View view){

        String url = " http://talaiaapi.azurewebsites.net/api/casa";

        id.clear();
        nom.clear();
        capacitat.clear();
        comarca.clear();

        clientCasa = new AsyncHttpClient();
        clientCasa.setMaxRetriesAndTimeout(0,10000);

        clientCasa.get(home.this, url, new AsyncHttpResponseHandler() {

            @Override
            public void onStart() {

                progress = ProgressDialog.show(view.getContext(), "Progrés",
                        "Obtenint dades...", true);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                JSONArray jsonArray = null;
                JSONObject casa = null;
                String str = new String(responseBody);


                try {

                    jsonArray = new JSONArray(str);

                    for (int i = 0; i < jsonArray.length();i++){

                        casa = jsonArray.getJSONObject(i);

                        id.add(String.valueOf(casa.getInt("IdCasa")));
                        nom.add(casa.getString("Nom"));
                        capacitat.add(String.valueOf(casa.getInt("Capacitat")));
                        comarca.add(casa.getString("Comarca"));
                        rating.add(String.valueOf(casa.getDouble("Mitjana")));

                    }

                }catch (JSONException e) {

                    e.printStackTrace();

                }

                bCasa.putStringArrayList("id",id);
                bCasa.putStringArrayList("nom",nom);
                bCasa.putStringArrayList("capacitat",capacitat);
                bCasa.putStringArrayList("comarca",comarca);
                bCasa.putStringArrayList("rating",rating);

                listCases.setArguments(bCasa);


                fM.beginTransaction().add(R.id.frame,listCases).commit();

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                Snackbar.make(view, "Error de conexió", Snackbar.LENGTH_LONG)
                        .show();

            }
        });

    }

    private void consultaApiReserves(final View view){

        String url = " http://talaiaapi.azurewebsites.net/api/reserva";

        idReserva.clear();
        preuReserva.clear();
        DataEntrada.clear();
        DataSortida.clear();
        FKCasa.clear();
        Estat.clear();

        clientReserva = new AsyncHttpClient();
        clientReserva.setMaxRetriesAndTimeout(0,10000);

        clientReserva.get(home.this, url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                JSONArray jsonArray = null;
                JSONObject reserva = null;
                String str = new String(responseBody);

                try {

                    jsonArray = new JSONArray(str);

                    for (int i = 0; i < jsonArray.length();i++){

                        reserva = jsonArray.getJSONObject(i);

                        idReserva.add(String.valueOf(reserva.getInt("IdReserva")));
                        preuReserva.add(String.valueOf(reserva.getInt("Preu")));
                        DataEntrada.add(String.valueOf(reserva.get("DataEntrada")));
                        DataSortida.add(String.valueOf(reserva.get("DataSortida")));
                        FKUsuari.add(String.valueOf(reserva.getInt("FKUsuari")));
                        FKCasa.add(String.valueOf(reserva.getInt("FKCasa")));
                        Estat.add(reserva.getString("Estat"));

                    }

                }catch (JSONException e) {

                    e.printStackTrace();

                }

                bReserva.putStringArrayList("id",idReserva);
                bReserva.putStringArrayList("preu",preuReserva);
                bReserva.putStringArrayList("nom",nom);
                bReserva.putStringArrayList("DE",DataEntrada);
                bReserva.putStringArrayList("DS",DataSortida);
                bReserva.putStringArrayList("FKUsuari",FKUsuari);
                bReserva.putStringArrayList("FKCasa",FKCasa);
                bReserva.putStringArrayList("Estat",Estat);

                bReserva.putString("IdUser",IdUser);

                perfil.setArguments(bReserva);

                progress.dismiss();

                for (int i = 0; i < FKUsuari.size();i++){

                    if (FKUsuari.get(i).equals(String.valueOf(usuariActiu.getIdUsuari()))){

                        teReserves = true;
                    }
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                Snackbar.make(view, "Error de conexió", Snackbar.LENGTH_LONG)
                        .show();

                progress.dismiss();
            }
        });

    }
}
