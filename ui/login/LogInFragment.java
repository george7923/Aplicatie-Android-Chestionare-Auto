package com.example.autoexpert.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.autoexpert.BazaDeDate;
import com.example.autoexpert.MainActivity;
import com.example.autoexpert.ManagementVariabileGlobale;
import com.example.autoexpert.Meniu;
import com.example.autoexpert.R;
import com.example.autoexpert.databinding.FragmentLoginBinding;


public class LogInFragment extends Fragment {

    private FragmentLoginBinding binding;
    private EditText Username;
    private EditText Password;
    private BazaDeDate bd;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        LogInViewModel logInViewModel =
                new ViewModelProvider(this).get(LogInViewModel.class);

        binding = FragmentLoginBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textLogin;
        logInViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        bd = new BazaDeDate(this.getContext(),"CHESTIONAR_AUTO.db",2);
        bd.getDatabaseInfo();

        try{
            bd.CheckDb();
        }
        catch(Exception e){
            e.printStackTrace();
        }

        Button LogInButton = root.findViewById(R.id.buttonLogIn);
        LogInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Username = root.findViewById(R.id.TextBoxUsername_LogIn);
                Password = root.findViewById(R.id.TextBoxPassword_LogIn);
                String user = Username.getText().toString();
                String pass = Password.getText().toString();

                try{

                String result = bd.findUsername(user,pass);
                if(result=="GASIT"){
                    ManagementVariabileGlobale.setUserName(user);
                    Toast.makeText(getContext(), "Date introduse cu succes!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getActivity(), Meniu.class);
                    startActivity(intent);


                }

                else if(result=="NOPASSWORD"){
                    Toast.makeText(getContext(), "Parola Incorecta!", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(getContext(), "Utilizator inexistent!", Toast.LENGTH_SHORT).show();
                }
                }
                catch(Exception e){
                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
