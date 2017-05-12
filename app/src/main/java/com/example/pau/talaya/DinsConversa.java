package com.example.pau.talaya;

import android.app.ListActivity;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import static com.example.pau.talaya.R.id.list;

public class DinsConversa extends AppCompatActivity {
    public Cursor cursor,comprovar;
    public SimpleCursorAdapter adapterdreta;
    static AdapterChat ad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dins_conversa);

        Window window = getWindow();
        window.setStatusBarColor(Color.parseColor("#4C9141"));

        Toolbar toolbar = (Toolbar) findViewById(R.id.tool);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_flecha_back));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("Missatges");

        TextView missatgedreta = (TextView) findViewById(R.id.msg_text_message);
        TextView horamissatge = (TextView) findViewById(R.id.msg_text_time_message);
        final TextView missatge = (TextView) findViewById(R.id.missatge);

        //LinearLayout layout = (LinearLayout) findViewById(R.id.msg_bubble_background_textesquerra);

        final ImageButton enviarmissatge = (ImageButton) findViewById(R.id.enviarmissatge);
        final DataBaseManager d = new DataBaseManager(this);
        final String[] from = new String[]{OpenHelper.M_Missatge, OpenHelper.M_Data,OpenHelper.M_Perfil, OpenHelper.M_Missatge, OpenHelper.M_Data};
        final int[] to = new int[]{R.id.msg_text_message, R.id.msg_text_time_message, R.id.perfilmissatge, R.id.msg_text_messageesquerra, R.id.msg_text_time_messageesquerra};

        cursor = d.getMissatges(1,2);

        mostraListdreta(cursor, from, to);
//      d.insertarusuari('1',"asa@gmail.com", "Albert", "1234", "ajaj", '2', "123");
        //String date = (String) android.text.format.DateFormat.format("yyyy-MM-dd kk:mm:ss", new java.util.Date());
        //d.insertarmissatge( date,"dreta",2,1);

        enviarmissatge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!missatge.getText().toString().equals("")){

                    String date = (String) android.text.format.DateFormat.format("yyyy-MM-dd kk:mm:ss", new java.util.Date());
                    d.insertarmissatge(date, missatge.getText().toString(), 1, 2);
                    cursor = d.getMissatges(1,2);
                    mostraListdreta(cursor, from, to);
                    missatge.setText("");
                }

            }
        });
    }
    public void mostraListdreta(Cursor cursor, String[] from, int[] to) {

        ListView llista = (ListView)findViewById(R.id.listConversa);

        adapterdreta = new AdapterChat(this, R.layout.bombolladreta, cursor, from, to, 1);
        llista.setAdapter(adapterdreta);

        llista.setSelection(llista.getAdapter().getCount()-1);


    }
}
