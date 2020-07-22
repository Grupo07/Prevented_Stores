package com.grupo07.preventedstores.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.grupo07.preventedstores.R;

public class LoginActivity extends AppCompatActivity implements View.OnKeyListener, View.OnClickListener {

    private EditText usernameEdit;
    private EditText passwordEdit;
    private String mode = "login";

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usernameEdit = (EditText) findViewById(R.id.username);
        passwordEdit = (EditText) findViewById(R.id.password);

        ((ConstraintLayout) findViewById(R.id.loginLayout)).setOnClickListener(this);
        passwordEdit.setOnKeyListener(this);

        setUserLocationPermission();

        mAuth = FirebaseAuth.getInstance();

        if(mAuth.getCurrentUser() != null)
            toMapActivity();
    }

    private void setUserLocationPermission() {
        // ask for location permission
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_DENIED) {
            finish();
        }
    }

    public void processUser(View view) {
        if (dataIsValid()) {
            String username = usernameEdit.getText().toString();
            String password = passwordEdit.getText().toString();
            if (mode == "login")
                login(username, password);
            else
                signup(username, password);
        }
    }

    private boolean dataIsValid() {
        String username = usernameEdit.getText().toString();
        String password = passwordEdit.getText().toString();
        if (username.isEmpty()) {
            Toast.makeText(this, "Please enter username", Toast.LENGTH_SHORT).show();
            return false;
        } else if (password.isEmpty()) {
            Toast.makeText(this, "Please enter password", Toast.LENGTH_SHORT).show();
            return false;
        } else if (username.length() < 6 || password.length() < 6) {
            Toast.makeText(this, "Username/password must be at least 6 characters", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void login(String username, String password) {
        Toast.makeText(this, "Login in!", Toast.LENGTH_SHORT).show();
        mAuth.signInWithEmailAndPassword(username + "@user.com", password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            toMapActivity();
                        } else {
                            Toast.makeText(LoginActivity.this, "Username or password incorrect", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void signup(String username, String password) {
        Toast.makeText(this, "Signing up!", Toast.LENGTH_SHORT).show();
        mAuth.createUserWithEmailAndPassword(username + "@user.com", password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful())
                            toMapActivity();
                        else
                            Toast.makeText(LoginActivity.this, "Username already registered", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void toMapActivity() {
        startActivity(new Intent(getApplicationContext(), MapsActivity.class));
    }

    public void toggleMode(View view) {
        Button processUserButton = (Button) findViewById(R.id.processUserButton);
        Button toggleModeButton = (Button) view;

        if(mode == "login") {
            mode = "signup";
            processUserButton.setText("Sign Up");
            toggleModeButton.setText("or login");

        } else {
            mode = "login";
            processUserButton.setText("Login");
            toggleModeButton.setText("or sign up");
        }
    }

    @Override
    public void onClick(View view) {
        if(getCurrentFocus() != null) {
            // hide keyboard upon clicking background
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

    @Override
    public boolean onKey(View view, int keyCode, KeyEvent event) {
        // process login/sign up in passwordEditText when pressing done in keyboard
        if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN)
            processUser(view);
        return false;
    }
}

