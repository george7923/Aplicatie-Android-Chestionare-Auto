package com.example.autoexpert.ui.signup;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import android.util.Log;

import com.example.autoexpert.BazaDeDate;
import com.example.autoexpert.R;
import com.example.autoexpert.databinding.FragmentSignupBinding;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.regex.Pattern;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SignUpFragment extends Fragment {

    private FragmentSignupBinding binding;
    private EditText username;
    private EditText password;
    private EditText email;
    private EditText phone;
    private BazaDeDate bd;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        SignUpViewModel signUpViewModel =
                new ViewModelProvider(this).get(SignUpViewModel.class);

        binding = FragmentSignupBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textSignup;
        signUpViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        bd = new BazaDeDate(this.getContext(),"CHESTIONAR_AUTO.db",2);
        bd.getDatabaseInfo();
        //bd.afiseazaDate();
        try{
            bd.CheckDb();
        }
        catch(Exception e){
            e.printStackTrace();
        }
        try{
            bd.OpenDatabase();
        }
        catch(Exception e){
            e.printStackTrace();
        }
        username = root.findViewById(R.id.TextBoxUser);
        password = root.findViewById(R.id.TextBoxPass);
        email = root.findViewById(R.id.TextBoxEmail);
        phone = root.findViewById(R.id.TextBoxPhone);


        Button signupButton = root.findViewById(R.id.buttonSignUp);
        //String insertQuery = "INSERT INTO cont (username, parola, email, nr_de_telefon) VALUES (?, ?, ?, ?)";
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Verificați dacă datele sunt valide
                String user = username.getText().toString();
                String pass = password.getText().toString();
                String userEmail = email.getText().toString();
                String userPhone = phone.getText().toString();
                boolean isPasswordValid = isValidPassword(pass);
                boolean isEmailValid = isValidEmail(userEmail);
                if (isPasswordValid && isEmailValid) {
                    // Crează o instanță a DBHelper


                    try {
                        // Folosește metoda insertCont pentru a insera datele

                        bd.insertCont(user, pass, userEmail, userPhone);

                        // Verificați dacă inserarea a avut succes

                    } catch (SQLiteException e) {
                        Toast.makeText(getContext(), "Eroare! " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                } else {
                    // Informați utilizatorul că datele introduse nu sunt valide
                    Toast.makeText(getContext(), "Date introduse nevalide!", Toast.LENGTH_SHORT).show();
                }
            }
        });


        return root;
    }
    private boolean isValidEmail(String email) {
        String regexEmail = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        return Pattern.matches(regexEmail, email);
    }


    private boolean isValidPassword(String password) {
        String regexPassword = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{8,}$";
        return Pattern.matches(regexPassword, password);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        executorService.shutdown();
        binding = null;
    }
}