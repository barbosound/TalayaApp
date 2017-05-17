package com.example.pau.talaya;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.entity.StringEntity;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;

import static com.example.pau.talaya.LoginActivity.usuariActiu;
import static com.example.pau.talaya.home.CasaList;


/**
 * Created by Pau on 28/4/17.
 */

public class DescCasa extends AppCompatActivity{

    private boolean fav = false;

    private int billar = 0;
    private int campFut = 0;
    private int campTen = 0;
    private int internet = 0;
    private int piscina = 0;
    private int projector = 0;
    private int sala = 0;
    private int pingpong = 0;
    private AsyncHttpClient clientUsuari;
    private ProgressDialog progress;
    private int indexCasa;

    private boolean noInstalacions = false;

    private View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.desc_casa);

        String capacitat, preu, nom, id;

        view = getWindow().getDecorView().getRootView();

        Window window = getWindow();
        window.setStatusBarColor(Color.parseColor("#4C9141"));

        final Bundle b = getIntent().getExtras();

        TextView txtNom = (TextView)findViewById(R.id.textNom);
        TextView txtComarca = (TextView)findViewById(R.id.textComarca);
        TextView txtCapacitat = (TextView)findViewById(R.id.textCapacitat);
        TextView txtRating = (TextView)findViewById(R.id.textPuntuacio);

        TextView txtDesc = (TextView)findViewById(R.id.txtDesc);

        ImageView billarImg = (ImageView)findViewById(R.id.image1);
        ImageView campFutImg = (ImageView)findViewById(R.id.image2);
        ImageView campTenImg = (ImageView)findViewById(R.id.image3);
        ImageView internetImg = (ImageView)findViewById(R.id.image4);
        ImageView piscinaImg = (ImageView)findViewById(R.id.image5);
        ImageView projectorImg = (ImageView)findViewById(R.id.image6);
        ImageView salaImg = (ImageView)findViewById(R.id.image7);
        ImageView pingpongImg = (ImageView)findViewById(R.id.image8);

        id = b.getString("id");

        for (int i = 0; i < CasaList.size();i++){

            assert id != null;
            if (id.equals(String.valueOf(CasaList.get(i).getIdCasa()))){

                indexCasa = i;

            }
        }

        nom = CasaList.get(indexCasa).getNom() + ",";
        txtNom.setText(nom);
        txtComarca.setText(CasaList.get(indexCasa).getComarca());

        capacitat = CasaList.get(indexCasa).getCapacitat() + " persones";
        txtCapacitat.setText(capacitat);

        txtRating.setText(String.valueOf(CasaList.get(indexCasa).getMitjana()));

        RelativeLayout layoutDesc = (RelativeLayout)findViewById(R.id.descripcio);
        LinearLayout noData = (LinearLayout)findViewById(R.id.noData);

        txtDesc.setText(CasaList.get(indexCasa).getDescripcio());

        layoutDesc.bringToFront();

        Toolbar toolbar = (Toolbar) findViewById(R.id.tool);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_flecha_back));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle(CasaList.get(indexCasa).getNom());

        RatingBar avg =(RatingBar)findViewById(R.id.avgRating);

        avg.setRating((float) CasaList.get(indexCasa).getMitjana());

        ViewPager mViewPager = (ViewPager) findViewById(R.id.viewPageAndroid);
        AdapterImatges adapterView = new AdapterImatges(this);
        mViewPager.setAdapter(adapterView);

        final FloatingActionButton favorits = (FloatingActionButton)findViewById(R.id.favButton);

        final FloatingActionButton misstge = (FloatingActionButton)findViewById(R.id.missatgeButton);

        misstge.setImageResource(R.drawable.misstage);

        if (CasaList.get(indexCasa).isFavorits()){
            favorits.setImageResource(R.drawable.star_selected);
        }else {
            favorits.setImageResource(R.drawable.star_unselected);
        }

        favorits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (CasaList.get(indexCasa).isFavorits()){

                    favorits.setImageResource(R.drawable.star_unselected);
                    CasaList.get(indexCasa).setFavorits(false);

                }else {

                    favorits.setImageResource(R.drawable.star_selected);
                    CasaList.get(indexCasa).setFavorits(true);

                    posaMarcador(String.valueOf(CasaList.get(indexCasa).getIdCasa()));

                }
            }
        });

        if (CasaList.get(indexCasa).getBillar() == 0){
            billarImg.setVisibility(View.GONE);
        }
        if (CasaList.get(indexCasa).getCampFutbol() == 0){
            campFutImg.setVisibility(View.GONE);
        }
        if (CasaList.get(indexCasa).getCampTenis() == 0){
            campTenImg.setVisibility(View.GONE);
        }
        if (CasaList.get(indexCasa).getInternet() == 0){
            internetImg.setVisibility(View.GONE);
        }
        if (CasaList.get(indexCasa).getPiscina() == 0){
            piscinaImg.setVisibility(View.GONE);
        }
        if (CasaList.get(indexCasa).getProjector() == 0){
            projectorImg.setVisibility(View.GONE);
        }
        if (CasaList.get(indexCasa).getSalaComuna() == 0){
            salaImg.setVisibility(View.GONE);
        }
        if (CasaList.get(indexCasa).getTenisTaula() == 0) {
            pingpongImg.setVisibility(View.GONE);
        }

        if (CasaList.get(indexCasa).getBillar() == 0 && CasaList.get(indexCasa).getCampFutbol() == 0 && CasaList.get(indexCasa).getCampTenis() == 0 && CasaList.get(indexCasa).getInternet() == 0 && CasaList.get(indexCasa).getPiscina() == 0 && CasaList.get(indexCasa).getProjector() == 0 && CasaList.get(indexCasa).getSalaComuna() == 0 && CasaList.get(indexCasa).getTenisTaula() == 0){

            noData.setVisibility(View.VISIBLE);
        }

    }
    private void posaMarcador(String idCasa) {

        HttpResponse response;
        HttpClient client = new DefaultHttpClient();
        String url = "http://talaiaapi.azurewebsites.net/api/marcador/"+usuariActiu.getIdUsuari();
        HttpPost post = new HttpPost(url);

        JSONObject favorit = new JSONObject();


        try {
            favorit.put("FKUsuari",usuariActiu.getIdUsuari());
            favorit.put("FKCasa",idCasa);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String strUser = favorit.toString();

        post.setEntity(new StringEntity(strUser, "UTF-8"));
        post.setHeader("Content-Type", "application/json");

        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        if (SDK_INT > 8)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
            //your codes here

        }

        try {
            response = client.execute(post);
            String sresponse = response.getEntity().toString();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
    public void treuMarcador(final View view){

        final String url = "http://talaiaapi.azurewebsites.net/api/marcador/"+usuariActiu.getIdUsuari();

        final View view2 = view;

        clientUsuari = new AsyncHttpClient();
        clientUsuari.setMaxRetriesAndTimeout(0,10000);

        clientUsuari.get(DescCasa.this, url, new AsyncHttpResponseHandler() {

            @Override
            public void onStart() {

            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                JSONObject casa = null;
                String str = new String(responseBody);

                try {

                    casa = new JSONObject(str);

                    billar = casa.getInt("Billar");
                    campFut = casa.getInt("CampFutbol");
                    campTen = casa.getInt("CampTenis");
                    internet = casa.getInt("Internet");
                    piscina = casa.getInt("Piscina");
                    projector = casa.getInt("Projector");
                    sala = casa.getInt("SalaComuna");
                    pingpong = casa.getInt("TenisTaula");

                }catch (JSONException e) {

                    e.printStackTrace();

                }
                progress.dismiss();
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
