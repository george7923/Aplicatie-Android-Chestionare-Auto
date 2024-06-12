package com.example.autoexpert;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;

public class ChestionarulPropriuZis extends AppCompatActivity {

    private ArrayList<Intrebare> I;
    private ArrayList<Intrebare> I26;
    private BazaDeDate bd;
    private Intrebare Q;
    private int contor, RaspCorecte, RaspGresite;
    private Timer timer;
    private long timpRamas = 1800000; // timpul total în milisecunde (30 minute)
    private TextView Cronometru;
    private Boolean primaintrebare = true;
    private Boolean IsSelectedSUBMIT = false;
    private Boolean invatareRaspunsAratat = false;
    private int state = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_chestionarul_propriu_zis);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        I = new ArrayList<>();
        TextView modDeJocLabel = findViewById(R.id.modDeJocLabel);
        modDeJocLabel.setText(ManagementVariabileGlobale.getMod());
        bd = new BazaDeDate(this, "CHESTIONAR_AUTO.db", 2);
        I = bd.getAllIntrebare();
        FormareIntrebari();
        bd.executaSiAfiseazaQuery();
        Q = getRandomQuestion(I26,false);
        contor = 0;
        RaspCorecte = 0;
        RaspGresite = 0;
        GestorChestionar(Q);
        Cronometru = findViewById(R.id.timerLabel);
        countdownTimer();
        Button SUBMIT = findViewById(R.id.submitButton);
        Button skipButton = findViewById(R.id.skipButton);
        skipButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {


                Q = getRandomQuestion(I26,true);
                GestorChestionar(Q);
                DeselecteazaButoanele();
                IsSelectedSUBMIT = false;
            }



            });
        SUBMIT.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                CheckBox A = findViewById(R.id.R_A);
                CheckBox B = findViewById(R.id.R_B);
                CheckBox C = findViewById(R.id.R_C);
                IsSelectedSUBMIT = true;
                String ModDeJoc = ManagementVariabileGlobale.getMod();
                if(ModDeJoc=="EXAMEN") {

                    if (primaintrebare) {
                        primaintrebare = false;
                        Punctajul(CheckCorectitudine(A, B, C, Q));
                        Q = getRandomQuestion(I26,false);
                        GestorChestionar(Q);
                        //DeselecteazaButoanele();
                        IsSelectedSUBMIT = false;
                    } else {
                        while (IsSelectedSUBMIT) {

                            Punctajul(CheckCorectitudine(A, B, C, Q));


                            if (RaspGresite <= 4) {
                                Q = getRandomQuestion(I26,false);
                                GestorChestionar(Q);
                                //DeselecteazaButoanele();
                                IsSelectedSUBMIT = false;

                            }
                            if (RaspGresite > 4 || Cronometru.getText().equals("00:00")) {
                                bd.insertHighscore(ManagementVariabileGlobale.getUserName(), Cronometru.getText().toString(), contor, RaspGresite, RaspCorecte, "RESPINS");
                                ManagementVariabileGlobale.setCalif("RESPINS");
                                Intent intent = new Intent(ChestionarulPropriuZis.this, Rezultat.class);
                                startActivity(intent);
                                break;
                            }
                            if (RaspCorecte >= 22 && contor >= 26) {
                                bd.insertHighscore(ManagementVariabileGlobale.getUserName(), Cronometru.getText().toString(), contor, RaspGresite, RaspCorecte, "ADMIS");
                                ManagementVariabileGlobale.setCalif("ADMIS");
                                Intent intent = new Intent(ChestionarulPropriuZis.this, Rezultat.class);
                                startActivity(intent);
                                break;
                            } else {
                                //Nimic nu se intampla;

                            }

                        }
                    }
                }
                else{
                    switch (state) {
                        case 0:
                            if (primaintrebare) {
                                primaintrebare = false;
                                Punctajul(CheckCorectitudine(A, B, C, Q));
                                state = 1;
                                // Deselecteaza butoanele
                                IsSelectedSUBMIT = false;
                            } else {
                                Punctajul(CheckCorectitudine(A, B, C, Q));
                                if (RaspGresite <= 4 && !Cronometru.getText().equals("00:00") && (RaspCorecte < 22 || contor <= 26)) {
                                    DeselecteazaButoanele();
                                    IsSelectedSUBMIT = false;
                                } else {
                                    String calificativ = (RaspGresite > 4 || Cronometru.getText().equals("00:00")) ? "RESPINS" : "ADMIS";
                                    bd.insertHighscore(ManagementVariabileGlobale.getUserName(), Cronometru.getText().toString(), contor, RaspGresite, RaspCorecte, calificativ);
                                    ManagementVariabileGlobale.setCalif(calificativ);
                                    Intent intent = new Intent(ChestionarulPropriuZis.this, Rezultat.class);
                                    startActivity(intent);
                                    break;
                                     // Iesim din functie pentru a evita schimbarea starii
                                }
                            }
                            arataRaspunsuriCorecte(A, B, C, Q);
                            state = 1;
                            break;

                        case 1:
                            // Încărcăm următoarea întrebare
                            Q = getRandomQuestion(I26, false);
                            GestorChestionar(Q);
                            state = 0;
                            break;
                    }

/*
                    switch(state){
                        case 0:
                            if(!primaintrebare) {
                                DeselecteazaButoanele();
                                arataRaspunsuriCorecte(A, B, C, Q);
                                state = 1;
                                Q = getRandomQuestion(I26);
                            }
                            else{
                                state = 0;
                                Punctajul(CheckCorectitudine(A, B, C, Q));
                                primaintrebare = false;
                            }
                            break;

                        case 1:
                            while (IsSelectedSUBMIT) {
                                GestorChestionar(Q);
                                Punctajul(CheckCorectitudine(A, B, C, Q));


                                if (RaspGresite <= 4) {
                                    state = 0;

                                    //DeselecteazaButoanele();
                                    IsSelectedSUBMIT = false;

                                }
                                if (RaspGresite > 4 || Cronometru.getText().equals("00:00")) {
                                    bd.insertHighscore(ManagementVariabileGlobale.getUserName(), Cronometru.getText().toString(), contor, RaspGresite, RaspCorecte, "RESPINS");
                                    ManagementVariabileGlobale.setCalif("RESPINS");
                                    Intent intent = new Intent(ChestionarulPropriuZis.this, Rezultat.class);
                                    startActivity(intent);
                                }
                                if (RaspCorecte >= 22 && contor > 26) {
                                    bd.insertHighscore(ManagementVariabileGlobale.getUserName(), Cronometru.getText().toString(), contor, RaspGresite, RaspCorecte, "ADMIS");
                                    ManagementVariabileGlobale.setCalif("ADMIS");
                                    Intent intent = new Intent(ChestionarulPropriuZis.this, Rezultat.class);
                                    startActivity(intent);
                                } else {
                                    //Nimic nu se intampla;

                                }

                            }
                        break;
                    }*/

                }
            }
        });
    }

    private void DeselecteazaButoanele() {
        CheckBox A = findViewById(R.id.R_A);
        CheckBox B = findViewById(R.id.R_B);
        CheckBox C = findViewById(R.id.R_C);
        A.setChecked(false);
        B.setChecked(false);
        C.setChecked(false);
    }

    private void Punctajul(Boolean A_RaspunsCorect) {
        contor++;
        if (!A_RaspunsCorect) {
            RaspGresite++;
        } else {
            RaspCorecte++;

        }
    }

    private void FormareIntrebari() {
        I26 = new ArrayList<>();
        if (I.isEmpty()) {
            throw new IllegalStateException("Lista de întrebări este goală.");
        } else {
            for (int i = 0; i < 26; i++) {
                I26.add(getRandomQuestion(I,false));
            }
        }
    }

    private Intrebare getRandomQuestion(ArrayList<Intrebare> I, boolean isSkip) {
        if (I.isEmpty()) {
            Raspuns R1 = new Raspuns("Raspuns1", false);
            Raspuns R2 = new Raspuns("Raspuns1", false);
            Raspuns R3 = new Raspuns("Raspuns1", false);
            return new Intrebare("Intrebare default", R1, R2, R3, "Random", -1, (Bitmap) null);
        }

        Random r = new Random();
        int nr = r.nextInt(I.size());
        Intrebare INTR = I.get(nr);
        if(!isSkip) {
            I.remove(nr);
        }
        return INTR;
    }

    /*private Boolean CheckCorectitudine(CheckBox A, CheckBox B, CheckBox C, Intrebare Q) {
        ArrayList<Boolean> Selected = new ArrayList<>();
        Selected.add(A.isChecked());
        Selected.add(B.isChecked());
        Selected.add(C.isChecked());
        ArrayList<Raspuns> R = new ArrayList<>();
        R.add(Q.getRaspuns1());
        R.add(Q.getRaspuns2());
        R.add(Q.getRaspuns3());

        for (int i = 0; i < R.size(); i++) {
            Log.e("CheckCorectitudine", "CheckBox " + i + ": selected=" + Selected.get(i) + ", corect=" + R.get(i).isCorect());
            if (Selected.get(i) != R.get(i).isCorect()) {
                return false;
            }
        }
        return true;
    }*/
    private Boolean CheckCorectitudine(CheckBox A, CheckBox B, CheckBox C, Intrebare Q) {
        Boolean[][] matrice = new Boolean[2][3];

        matrice[0][0] = A.isChecked();
        matrice[0][1] = B.isChecked();
        matrice[0][2] = C.isChecked();

        matrice[1][0] = Q.getRaspuns1().isCorect();
        matrice[1][1] = Q.getRaspuns2().isCorect();
        matrice[1][2] = Q.getRaspuns3().isCorect();

        for (int i = 0; i < 3; i++) {
            //Log.e("CheckCorectitudine", "Coloana " + i + ": selected=" + matrice[0][i] + ", corect=" + matrice[1][i]);
            if (!matrice[0][i].equals(matrice[1][i])) {
                DeselecteazaButoanele();
                return false;
            }
        }
        DeselecteazaButoanele();
        return true;
    }

    private void arataRaspunsuriCorecte(CheckBox A, CheckBox B, CheckBox C, Intrebare Q) {
        if (Q.getRaspuns1().isCorect()) {
            A.setTextColor(Color.GREEN);
        } else {
            A.setTextColor(Color.RED);
        }

        if (Q.getRaspuns2().isCorect()) {
            B.setTextColor(Color.GREEN);
        } else {
            B.setTextColor(Color.RED);
        }

        if (Q.getRaspuns3().isCorect()) {
            C.setTextColor(Color.GREEN);
        } else {
            C.setTextColor(Color.RED);
        }
    }

    private void GestorChestionar(Intrebare Q) {
        TextView INTREBAREA = findViewById(R.id.IntrebareLabel);
        ImageView IMG = findViewById(R.id.pictureBox);
        INTREBAREA.setText(Q.getIntrebare());
        INTREBAREA.setTextColor(Color.WHITE);

        CheckBox A = findViewById(R.id.R_A);
        A.setText(Q.getRaspuns1().getText());
        A.setTextColor(Color.WHITE);

        CheckBox B = findViewById(R.id.R_B);
        B.setText(Q.getRaspuns2().getText());
        B.setTextColor(Color.WHITE);

        CheckBox C = findViewById(R.id.R_C);
        C.setText(Q.getRaspuns3().getText());
        C.setTextColor(Color.WHITE);
        if (Q.getImagine() != null) {
            IMG.setVisibility(View.VISIBLE);
            IMG.setImageBitmap(Q.getImagine());
        } else {
            IMG.setVisibility(View.INVISIBLE);
        }
        TextView LabelContor = findViewById(R.id.LabelContor);
        TextView LabelRaspCorecte = findViewById(R.id.LabelCorecte);
        TextView LabelRaspGresite = findViewById(R.id.LabelGreseli);
        LabelContor.setText("Intrebari raspunse: "+contor);
        LabelRaspCorecte.setText("Intrebari corecte: "+RaspCorecte);
        LabelRaspGresite.setText("Intrebari gresite: "+RaspGresite);

    }

    public void countdownTimer() {
        new CountDownTimer(timpRamas, 1000) { 

            public void onTick(long millisUntilFinished) {
                timpRamas = millisUntilFinished;
                updateTimerText();
            }

            public void onFinish() {
                timpRamas = 0;
                updateTimerText();
            }
        }.start();
    }

    private void updateTimerText() {
        long minute = timpRamas / 60000;
        long secunde = timpRamas % 60000 / 1000;
        String timpRamasString = String.format("%02d:%02d", minute, secunde);
        Cronometru.setText(timpRamasString);
    }
}
