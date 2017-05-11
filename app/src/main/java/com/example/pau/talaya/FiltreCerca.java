package com.example.pau.talaya;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.DateUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Toast;


import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import cz.msebera.android.httpclient.Header;

import static com.example.pau.talaya.LoginActivity.usuariActiu;

/**
 * Created by Pau on 26/4/17.
 */

public class FiltreCerca extends AppCompatActivity implements MultiSpinner.OnItemClickListener {

    private Calendar dateCalendar = Calendar.getInstance();

    private DatePickerDialog CalendarPicker;

    private EditText textEntrada;
    private EditText textSortida;

    private MultiSpinner.MultiSpinnerListener listener;

    private List<String> items;
    public boolean[] seleccio;

    private AsyncHttpClient client;
    private String url = " http://talaiaapi.azurewebsites.net/api/casa";

    private ArrayList<String> ArrayId = new ArrayList<>();
    private ArrayList<String> ArrayNom = new ArrayList<>();
    private ArrayList<String> ArrayCapacitat = new ArrayList<>();
    private ArrayList<String> ArrayComarca = new ArrayList<>();
    private ArrayList<String> ArrayRating = new ArrayList<>();

    private Search_fragment.OnFragmentInteractionListener mListener;

    private int billar = 0, campfut = 0, campten = 0, internet = 0, piscina = 0, projector = 0,sala = 0,tenistaula = 0;

    private String accessibilitat, capacitat, comarca, dataEntrara, dataSortida;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.filtre);

        Window window = getWindow();
        window.setStatusBarColor(Color.parseColor("#4C9141"));

        Toolbar toolbar = (Toolbar) findViewById(R.id.tool);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_flecha_back));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Filtre de cerca");

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        items = Arrays.asList(getResources().getStringArray(R.array.multispinner_entries));

        ImageView DataEntrada = (ImageView)findViewById(R.id.imageEntrada);
        ImageView DataSortida = (ImageView)findViewById(R.id.imageSortida);

        textEntrada = (EditText)findViewById(R.id.editEntrada);
        textSortida = (EditText)findViewById(R.id.editSortida);

        final Button cerca = (Button)findViewById(R.id.button3);

        final RatingBar valoracio = (RatingBar)findViewById(R.id.ratingBar);

        LayerDrawable stars = (LayerDrawable) valoracio.getProgressDrawable();
        stars.getDrawable(2).setColorFilter(Color.parseColor("#57a639" +
                ""), PorterDuff.Mode.SRC_ATOP);

        DataEntrada.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                CalendarPicker = new DatePickerDialog(FiltreCerca.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                        java.util.Calendar dateCalendar = java.util.Calendar.getInstance();
                        dateCalendar.set(java.util.Calendar.YEAR, year);
                        dateCalendar.set(java.util.Calendar.MONTH, monthOfYear);
                        dateCalendar.set(java.util.Calendar.DAY_OF_MONTH, dayOfMonth);

                        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

                        Date date = dateCalendar.getTime();

                        String dateString;
                        dateString = formatter.format(date);
                        textEntrada.setText(dateString);

                    }
                }, dateCalendar.get(java.util.Calendar.YEAR), dateCalendar.get(java.util.Calendar.MONTH), dateCalendar.get(java.util.Calendar.DAY_OF_MONTH));

                CalendarPicker.show();

            }
        });

        DataSortida.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                CalendarPicker = new DatePickerDialog(FiltreCerca.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                        java.util.Calendar dateCalendar = java.util.Calendar.getInstance();
                        dateCalendar.set(java.util.Calendar.YEAR, year);
                        dateCalendar.set(java.util.Calendar.MONTH, monthOfYear);
                        dateCalendar.set(java.util.Calendar.DAY_OF_MONTH, dayOfMonth);

                        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

                        Date date = dateCalendar.getTime();

                        String dateString;
                        dateString = formatter.format(date);
                        textSortida.setText(dateString);

                    }
                }, dateCalendar.get(java.util.Calendar.YEAR), dateCalendar.get(java.util.Calendar.MONTH), dateCalendar.get(java.util.Calendar.DAY_OF_MONTH));

                CalendarPicker.show();

            }
        });

        MultiSpinner multiSpinner = (MultiSpinner)findViewById(R.id.multispinner);
        multiSpinner.setItems(items, "Selecciona les instal·lacions", listener);

        seleccio = multiSpinner.selected;

        //per obtenir la valoracio que han posat
        valoracio.getRating();

        cerca.setEnabled(true);

        cerca.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {

                Intent intencio = new Intent(FiltreCerca.this, home.class);

                startActivity(intencio);

            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

        switch (i){
            case 0:
                if (billar == 0){
                    billar = 1;
                }else {
                    billar = 0;
                }
                break;
            case 1:
                if (campfut == 0){
                    campfut = 1;
                }else {
                    campfut = 0;
                }
                break;
            case 2:
                if (campten == 0){
                    campten = 1;
                }else {
                    campten = 0;
                }
                break;
            case 3:
                if (internet == 0){
                    internet = 1;
                }else {
                    internet = 0;
                }
                break;
            case 4:
                if (piscina == 0){
                    piscina = 1;
                }else {
                    piscina = 0;
                }
                break;
            case 5:
                if (projector == 0){
                    projector = 1;
                }else {
                    projector = 0;
                }
                break;
            case 6:
                if (sala == 0){
                    sala = 1;
                }else {
                    sala = 0;
                }
                break;
            case 7:
                if (tenistaula == 0){
                    tenistaula = 1;
                }else {
                    tenistaula = 0;
                }
                break;
        }

    }
}
