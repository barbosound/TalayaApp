package com.example.pau.talaya;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.support.annotation.FloatRange;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.pau.talaya.R;

import java.util.ArrayList;

/**
 * Created by Pau on 26/4/17.
 */

public class AdapterCasa extends ArrayAdapter<String> {

    private ArrayList<String> nom = new ArrayList<>();
    private ArrayList<String> capacitat = new ArrayList<>();
    private ArrayList<String> comarca = new ArrayList<>();
    private ArrayList<String> rating = new ArrayList<>();


    public AdapterCasa(Context context, int layoutResourceId, ArrayList<String> nom, ArrayList<String> capacitat, ArrayList<String> comarca, ArrayList<String> rating) {
        super(context, layoutResourceId, nom);

        this.nom = nom;
        this.capacitat = capacitat;
        this.comarca = comarca;
        this.rating = rating;

    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        Float avg;

        LayoutInflater inflater = LayoutInflater.from(getContext());

        view = inflater.inflate(R.layout.casa_row,null);

        RatingBar avgRating = (RatingBar)view.findViewById(R.id.avgRating);

        LayerDrawable stars = (LayerDrawable) avgRating.getProgressDrawable();
        stars.getDrawable(2).setColorFilter(Color.parseColor("#57a639" +
                ""), PorterDuff.Mode.SRC_ATOP);

        TextView txtnom = (TextView)view.findViewById(R.id.textNom);
        TextView txtdesc = (TextView)view.findViewById(R.id.textCap);
        TextView txtcom = (TextView)view.findViewById(R.id.textComarca);

        txtnom.setText(nom.get(position));
        txtdesc.setText(capacitat.get(position));
        txtcom.setText("("+comarca.get(position)+")");

        avg = Float.parseFloat(rating.get(position));

        avgRating.setEnabled(false);

        avgRating.setRating(avg);

        return view;

    }

}
