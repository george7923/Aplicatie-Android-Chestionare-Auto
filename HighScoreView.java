package com.example.autoexpert;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class HighScoreView extends AppCompatActivity {
    private BazaDeDate bd;
    private TableLayout tableLayout;
    private Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_high_score_view);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        String username = ManagementVariabileGlobale.getUserName();
        BazaDeDate bd = new BazaDeDate(this, "CHESTIONAR_AUTO.db", 2);
        tableLayout = findViewById(R.id.tableLayout);
        cursor = bd.getHighScoresByUsername(username);
        displayHighScores();
        Button BackButton = findViewById(R.id.backButton);
        BackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HighScoreView.this, Meniu.class);
                startActivity(intent);
            }
        });
    }

    private void displayHighScores() {
        // Clear existing rows
        tableLayout.removeAllViews();

        // Add headers row
        TableRow headersRow = new TableRow(this);
        String[] headers = {"Username", "Time", "Questions", "Mistakes", "Correct", "Rating"};
        for (String header : headers) {
            TextView textView = new TextView(this);
            textView.setText(header);
            headersRow.addView(textView);
        }
        tableLayout.addView(headersRow);

        // Add data rows
        if (cursor != null && cursor.moveToFirst()) {
            do {
                TableRow dataRow = new TableRow(this);
                int usernameColumnIndex = cursor.getColumnIndex("UTILIZATOR");
                int timeColumnIndex = cursor.getColumnIndex("Timpul_Ramas");
                int questionsColumnIndex = cursor.getColumnIndex("INTREBARILE_PARCURSE");
                int mistakesColumnIndex = cursor.getColumnIndex("GRESELI");
                int correctColumnIndex = cursor.getColumnIndex("CORECTE");
                int ratingColumnIndex = cursor.getColumnIndex("Calificativ");

                if (usernameColumnIndex != -1 && timeColumnIndex != -1 && questionsColumnIndex != -1 &&
                        mistakesColumnIndex != -1 && correctColumnIndex != -1 && ratingColumnIndex != -1) {
                    String username = cursor.getString(usernameColumnIndex);
                    int time = cursor.getInt(timeColumnIndex);
                    int questions = cursor.getInt(questionsColumnIndex);
                    int mistakes = cursor.getInt(mistakesColumnIndex);
                    int correct = cursor.getInt(correctColumnIndex);
                    String rating = cursor.getString(ratingColumnIndex);
                    addTextViewToRow(dataRow, username);
                    addTextViewToRow(dataRow, String.valueOf(time));
                    addTextViewToRow(dataRow, String.valueOf(questions));
                    addTextViewToRow(dataRow, String.valueOf(mistakes));
                    addTextViewToRow(dataRow, String.valueOf(correct));
                    addTextViewToRow(dataRow, rating);
                    // Rest of your code...
                } else {
                    // Handle the case where one or more columns don't exist
                    continue;
                }



                tableLayout.addView(dataRow);
            } while (cursor.moveToNext());
        }
    }

    private void addTextViewToRow(TableRow row, String text) {
        TextView textView = new TextView(this);
        textView.setText(text);
        row.addView(textView);
    }
}
