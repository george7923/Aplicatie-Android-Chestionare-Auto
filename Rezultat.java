package com.example.autoexpert;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Rezultat extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_rezultat);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        TextView Rezultatul = findViewById(R.id.largeTextView);
        Button back = findViewById(R.id.backToMainButton);
        String a = ManagementVariabileGlobale.getCalif();

        if (a.equals("RESPINS")) {
            Rezultatul.setText("RESPINS");
            Rezultatul.setTextColor(Color.RED);
        }
        else{
            Rezultatul.setText("ADMIS! FELICITARI");
            Rezultatul.setTextColor(Color.GREEN);
        }
        back.setOnClickListener(new View.OnClickListener(){




            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(Rezultat.this,Meniu.class);
                startActivity(intent);

            }


        });
    }
}