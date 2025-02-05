package com.example.test2025;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileActivity extends AppCompatActivity {
    private EditText userName, email, cin;
    private Button btnUpdate, btnLogOut;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser loggedUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        userName = findViewById(R.id.user_name_profile);
        email = findViewById(R.id.email_profile);
        cin = findViewById(R.id.cin_profile);
        btnUpdate = findViewById(R.id.btn_update);
        btnLogOut = findViewById(R.id.btn_log_out);


        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        loggedUser = firebaseAuth.getCurrentUser();
        databaseReference = firebaseDatabase.getReference().child("users").child(loggedUser.getUid());

        btnUpdate.setOnClickListener(v -> {
            String newUserName = userName.getText().toString().trim();
            String newCin = cin.getText().toString().trim();
            databaseReference.child("userName").setValue(newUserName);
            databaseReference.child("cin").setValue(newCin);
            Toast.makeText(this, "Your data has been changed successfuly", Toast.LENGTH_SHORT).show();
        });

        btnLogOut.setOnClickListener(v -> {
            SharedPreferences sharedPreferences = getSharedPreferences("remeberMeCheckBox", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("rememberMe", false);
            editor.apply();
            firebaseAuth.signOut();
            startActivity(new Intent(this, SignInActivity.class));
            Toast.makeText(this, "Sign out successfuly", Toast.LENGTH_SHORT).show();
            finish();
        });


        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String userNameS = snapshot.child("userName").getValue().toString();
                String emailS = snapshot.child("email").getValue().toString();
                String cinS = snapshot.child("cin").getValue().toString();
                userName.setText(userNameS);
                email.setText(emailS);
                cin.setText(cinS);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ProfileActivity.this, "" + error, Toast.LENGTH_SHORT).show();
            }
        });


    }
}