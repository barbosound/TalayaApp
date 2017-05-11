package com.example.pau.talaya;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;


/**
 * Created by Pau on 28/4/17.
 */

public class DescCasa extends AppCompatActivity {

    private boolean fav = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.desc_casa);

        String capacitat, preu, nom;

        Window window = getWindow();
        window.setStatusBarColor(Color.parseColor("#4C9141"));

        Bundle b = getIntent().getExtras();

        TextView txtNom = (TextView)findViewById(R.id.textNom);
        TextView txtComarca = (TextView)findViewById(R.id.textComarca);
        TextView txtCapacitat = (TextView)findViewById(R.id.textCapacitat);
        TextView txtRating = (TextView)findViewById(R.id.textPuntuacio);

        ImageView billar = (ImageView)findViewById(R.id.image1);
        ImageView campFut = (ImageView)findViewById(R.id.image2);
        ImageView campTen = (ImageView)findViewById(R.id.image3);
        ImageView internet = (ImageView)findViewById(R.id.image4);
        ImageView piscina = (ImageView)findViewById(R.id.image5);
        ImageView projector = (ImageView)findViewById(R.id.image6);
        ImageView sala = (ImageView)findViewById(R.id.image7);
        ImageView pingpong = (ImageView)findViewById(R.id.image8);


        nom = b.getString("nom") + ",";
        txtNom.setText(nom);
        txtComarca.setText(b.getString("comarca"));

        capacitat = b.getString("capacitat") + " persones";
        txtCapacitat.setText(capacitat);

        txtRating.setText(b.getString("rating"));

        RelativeLayout layoutDesc = (RelativeLayout)findViewById(R.id.descripcio);

        layoutDesc.bringToFront();

        Toolbar toolbar = (Toolbar) findViewById(R.id.tool);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_flecha_back));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle(b.getString("nom"));


        RatingBar avg =(RatingBar)findViewById(R.id.avgRating);

        avg.setRating(Float.parseFloat(b.getString("rating")));

        ViewPager mViewPager = (ViewPager) findViewById(R.id.viewPageAndroid);
        AdapterImatges adapterView = new AdapterImatges(this);
        mViewPager.setAdapter(adapterView);

        final FloatingActionButton favorits = (FloatingActionButton)findViewById(R.id.floatingActionButton);

        if (fav){
            favorits.setImageResource(R.drawable.star_selected);
        }else {
            favorits.setImageResource(R.drawable.star_unselected);
        }

        favorits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (fav){
                    favorits.setImageResource(R.drawable.star_unselected);
                    fav = false;
                }else {
                    favorits.setImageResource(R.drawable.star_selected);
                    fav = true;
                }
            }
        });





    }

}
