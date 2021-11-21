package com.example.myseboadminstaff;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    private EditText eName, eEmail, ePassword, ePhone, eStaffId;
    private Button eRegister;
    private TextView eLogin;
    private ProgressBar progressBar;

    private FirebaseAuth fAuth;
    private FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        firebaseFirestore = FirebaseFirestore.getInstance();

        eName = findViewById(R.id.etName);
        eEmail = findViewById(R.id.etEmail);
        ePassword = findViewById(R.id.etPassword);
        ePhone = findViewById(R.id.etPhone);
        eRegister = findViewById(R.id.btnRegister);
        eLogin = findViewById(R.id.tvNewAcc);
        eStaffId = findViewById(R.id.etStaffID);

        fAuth = FirebaseAuth.getInstance();
        progressBar = findViewById(R.id.progressBar);

        if (fAuth.getCurrentUser() != null) {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        }

        eRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = eEmail.getText().toString().trim();
                String password = ePassword.getText().toString().trim();
                String staffId = eStaffId.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    eEmail.setError("Email is Required.");
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    ePassword.setError("Password is Required.");
                    return;
                }

                if (password.length() < 8) {
                    ePassword.setError("Password must be at least 8 character");
                    return;
                }

                if (TextUtils.isEmpty(staffId)) {
                    eStaffId.setError("Staff ID is Required.");
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);

                //register the user in firebase

                fAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            String name =eName.getText().toString();
                            String phone = ePhone.getText().toString();
                            String staffId = eStaffId.getText().toString();

                            Map<String, Object> newData = new HashMap<>();
                            final FirebaseUser currentUser = fAuth.getCurrentUser();

                            newData.put("id", currentUser.getUid());
                            newData.put("name",name);
                            newData.put("email",email);
                            newData.put("staffId", staffId);
                            newData.put("phone",phone);
                            newData.put("verify", false);
                            newData.put("type", "admin");

                            currentUser.sendEmailVerification();

                            firebaseFirestore.collection("user").document(currentUser.getUid()).set(newData);
                            Toast.makeText(RegisterActivity.this, "User Created.", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), LoginActivity.class));

                        } else {
                            Toast.makeText(RegisterActivity.this, "Error!" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });
            }
        });

        eLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            }
        });
    }
}