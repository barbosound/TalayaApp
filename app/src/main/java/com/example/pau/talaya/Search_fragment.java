package com.example.pau.talaya;

import android.app.DatePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import cz.msebera.android.httpclient.Header;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Search_fragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Search_fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Search_fragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private Calendar dateCalendar = Calendar.getInstance();

    private DatePickerDialog CalendarPicker;

    private EditText textEntrada;
    private EditText textSortida;

    private MultiSpinner.MultiSpinnerListener listener;

    private List<String> items;
    public boolean[] seleccio;

    private AsyncHttpClient client;
    private String url = " http://talaiaapi.azurewebsites.net/api/casa";

    private ArrayList<String> id = new ArrayList<>();
    private ArrayList<String> nom = new ArrayList<>();
    private ArrayList<String> desc = new ArrayList<>();

    private OnFragmentInteractionListener mListener;

    public Search_fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Search_fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Search_fragment newInstance(String param1, String param2) {
        Search_fragment fragment = new Search_fragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.filtre, container, false);

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        items = Arrays.asList(getResources().getStringArray(R.array.multispinner_entries));

        ImageView DataEntrada = (ImageView)view.findViewById(R.id.imageEntrada);
        ImageView DataSortida = (ImageView)view.findViewById(R.id.imageSortida);

        textEntrada = (EditText) view.findViewById(R.id.editEntrada);
        textSortida = (EditText) view.findViewById(R.id.editSortida);

        Button cerca = (Button)view.findViewById(R.id.button3);

        final RatingBar valoracio = (RatingBar)view.findViewById(R.id.ratingBar);

        LayerDrawable stars = (LayerDrawable) valoracio.getProgressDrawable();
        stars.getDrawable(2).setColorFilter(Color.parseColor("#57a639" +
                ""), PorterDuff.Mode.SRC_ATOP);

        DataEntrada.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                CalendarPicker = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                        java.util.Calendar dateCalendar = java.util.Calendar.getInstance();
                        dateCalendar.set(java.util.Calendar.YEAR, year);
                        dateCalendar.set(java.util.Calendar.MONTH, monthOfYear);
                        dateCalendar.set(java.util.Calendar.DAY_OF_MONTH, dayOfMonth);

                        String dateString = DateUtils.formatDateTime(getContext(), dateCalendar.getTimeInMillis(), DateUtils.FORMAT_NUMERIC_DATE);
                        textEntrada.setText(dateString);

                    }
                }, dateCalendar.get(java.util.Calendar.YEAR), dateCalendar.get(java.util.Calendar.MONTH), dateCalendar.get(java.util.Calendar.DAY_OF_MONTH));

                CalendarPicker.show();

            }
        });

        DataSortida.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                CalendarPicker = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                        java.util.Calendar dateCalendar = java.util.Calendar.getInstance();
                        dateCalendar.set(java.util.Calendar.YEAR, year);
                        dateCalendar.set(java.util.Calendar.MONTH, monthOfYear);
                        dateCalendar.set(java.util.Calendar.DAY_OF_MONTH, dayOfMonth);

                        String dateString = DateUtils.formatDateTime(getContext(), dateCalendar.getTimeInMillis(), DateUtils.FORMAT_NUMERIC_DATE);
                        textSortida.setText(dateString);

                    }
                }, dateCalendar.get(java.util.Calendar.YEAR), dateCalendar.get(java.util.Calendar.MONTH), dateCalendar.get(java.util.Calendar.DAY_OF_MONTH));

                CalendarPicker.show();

            }
        });

        MultiSpinner multiSpinner = (MultiSpinner)view.findViewById(R.id.multispinner);
        multiSpinner.setItems(items, "Selecciona les instal·lacions", listener);

        seleccio = multiSpinner.selected;

        //per obtenir la valoracio que han posat
        valoracio.getRating();

        cerca.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               client = new AsyncHttpClient();
                client.setMaxRetriesAndTimeout(0,10000);

                client.get(getContext(), url, new AsyncHttpResponseHandler() {
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
                                desc.add(casa.getString("Descripcio"));

                            }

                        }catch (JSONException e){

                            e.printStackTrace();

                        }


                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                    }
                });



            }
        });

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
