package app.clairvoyant.reddrop;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private EditText email, password;
    private Button loginButton;
    private TextView createAccountTextButton, forgotPasswordButton;

    private FirebaseAuth firebaseAuth;

    private static final String TAG = "Login Activity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = findViewById(R.id.donor_login_email);
        password = findViewById(R.id.donor_login_password);
        loginButton = findViewById(R.id.btn_login_donor);
        createAccountTextButton = findViewById(R.id.txt_no_account);
        forgotPasswordButton = findViewById(R.id.txt_forgot_password);

        firebaseAuth = FirebaseAuth.getInstance();

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(email.getText())){
                    Toast.makeText(getApplicationContext(),"Please enter your registered email in order to log in",Toast.LENGTH_SHORT).show();
                }
                if (TextUtils.isEmpty(password.getText())){
                    Toast.makeText(getApplicationContext(),"Please enter your registered password in order to log in",Toast.LENGTH_SHORT).show();
                }
                else {
                    firebaseAuth.signInWithEmailAndPassword(email.getText().toString().trim(),password.getText().toString().trim())
                            .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()){
                                        Toast.makeText(getApplicationContext(),"Congratulations! you're now successfully logged in to Red Drop",Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(LoginActivity.this,DonorHomeActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                    else {
                                        Toast.makeText(getApplicationContext(),"Sorry! login is unsuccessful, please try again.",Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });

        createAccountTextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this,AuthActivity.class));
                finish();
            }
        });

        forgotPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this,ResetPasswordActivity.class));
                finish();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        //check if user is logged in then go to Home Activity bypassing the Auth Activity
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if (currentUser != null) {
            startActivity(new Intent(LoginActivity.this, DonorHomeActivity.class));
            finish();
        }
    }
}
