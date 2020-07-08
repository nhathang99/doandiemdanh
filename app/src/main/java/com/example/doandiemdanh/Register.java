package com.example.doandiemdanh;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Register extends AppCompatActivity {

    private ImageView avataruser;
    private EditText emaileditText,passwordeditText,fullnameeditText,professioneditText,locationeditText,phoneeditText;
    private CheckBox malecheckBox, femalecheckBox;
    private Button buttondk;
    private FirebaseDatabase database;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private static  final String USER ="user";
    private  static final  String TAG = "Register";
    private User users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth= FirebaseAuth.getInstance();
        setContentView(R.layout.activity_register);
        avataruser = findViewById(R.id.avatar);
        emaileditText = findViewById(R.id.email_editText);
        passwordeditText = findViewById(R.id.password_editText);
        fullnameeditText = findViewById(R.id.fullname_editText);
        professioneditText= findViewById(R.id.profession_editText);
        locationeditText = findViewById(R.id.location_editText);
        phoneeditText = findViewById(R.id.phone_editText);

        buttondk = findViewById(R.id.button);

        database = FirebaseDatabase.getInstance();
        mDatabase = database.getReference(USER);
        Log.d(TAG, "createUserWithEmail");

        buttondk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "createUserWithEmail");
                String email = emaileditText.getText().toString();
                String password = passwordeditText.getText().toString();
                if(TextUtils.isEmpty(email)|| TextUtils.isEmpty(password)){
                    Toast.makeText(getApplicationContext(), "enter email and password",
                            Toast.LENGTH_LONG).show();
                    return;
                }
                String fullname = fullnameeditText.getText().toString();
                String occupation = professioneditText.getText().toString();
                String location = locationeditText.getText().toString();
                String phone = phoneeditText.getText().toString();

                 users = new User(email, fullname,phone,occupation,location);
                registerUser(email, password, users);

            }
        });

    }
    public void registerUser(String email, String password, final User users){
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            writeNewUser(user.getUid(),users);


                            Intent intent = new Intent(getApplicationContext() ,MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(Register.this, "Authentication failed."+task.getException(),
                                    Toast.LENGTH_SHORT).show();

                        }

                        // ...
                    }
                });
    }

    private void writeNewUser(String userId,User user) {

        mDatabase= FirebaseDatabase.getInstance().getReference("user").child(userId);
        mDatabase.setValue(user);

    }

}
