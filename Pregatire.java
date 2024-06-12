package com.example.autoexpert;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class Pregatire extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_pregatire);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        BazaDeDate bd = new BazaDeDate(this, "CHESTIONAR_AUTO.db", 2);
        bd.afiseazaDimensiuneaTabeluluiIntrebari();
        ArrayList<Intrebare> Intr= bd.getAllIntrebare();
        Log.e("INTREBARI COUNT!", Intr.size()+"");

        Button Start = findViewById(R.id.start_button);
        Start.setOnClickListener(new View.OnClickListener(){




            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(Pregatire.this,ChestionarulPropriuZis.class);
                startActivity(intent);

            }


        });
    }
}