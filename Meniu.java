package com.example.autoexpert;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Meniu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_meniu);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        TextView WelcomeWindow = findViewById(R.id.welcomeMessage);
        String label = "Bine ati venit "+ManagementVariabileGlobale.getUserName()+" la AUTOEXPERT!";
        WelcomeWindow.setText(label);
        Button highScoreButton = findViewById(R.id.buttonHighscore);
        Button FotoButton = findViewById(R.id.indicator);
        FotoButton.setOnClickListener(new View.OnClickListener(){




            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(Meniu.this,FotoIndicator.class);
                startActivity(intent);

            }


        });
        highScoreButton.setOnClickListener(new View.OnClickListener(){




            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(Meniu.this,HighScoreView.class);
                startActivity(intent);

            }


        });
        Button ModInvatareButton = findViewById(R.id.buttonModInvatare);
        ModInvatareButton.setOnClickListener(new View.OnClickListener(){




            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                ManagementVariabileGlobale.setMod("INVATARE");
                Intent intent = new Intent(Meniu.this,Pregatire.class);
                startActivity(intent);

            }


        });
        Button ModExamenButton = findViewById(R.id.buttonModExamen);
        ModExamenButton.setOnClickListener(new View.OnClickListener(){




            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                ManagementVariabileGlobale.setMod("EXAMEN");
                Intent intent = new Intent(Meniu.this,Pregatire.class);
                startActivity(intent);

            }


        });

    }
}