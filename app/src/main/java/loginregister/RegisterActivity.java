package loginregister;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.jakegrace.exercyse.R;

import org.json.JSONException;
import org.json.JSONObject;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        final EditText etFirstname = (EditText) findViewById(R.id.etFirstname);
        final EditText etLastname = (EditText) findViewById(R.id.etLastname);
        final EditText etEmail = (EditText) findViewById(R.id.etEmail);
        final EditText etUsername = (EditText) findViewById(R.id.user_name);
        final EditText etPassword = (EditText) findViewById(R.id.etPassword);
        final Button bRegister = (Button) findViewById(R.id.bRegister);
        final TextView loginLink = (TextView) findViewById(R.id.tvLogin);

        if (etFirstname.getText().length() == 0) {
            etFirstname.requestFocus();
        } else if (etLastname.getText().length() == 0) {
            etLastname.requestFocus();
        } else if (etEmail.getText().length() == 0) {
            etEmail.requestFocus();
        } else if (etUsername.getText().length() == 0) {
            etUsername.requestFocus();
        } else {
            etPassword.requestFocus();
        }

        loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loginIntent = new Intent(RegisterActivity.this, LoginActivity.class);
                RegisterActivity.this.startActivity(loginIntent);
            }
        });

        bRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String firstname = etFirstname.getText().toString();
                final String lastname = etLastname.getText().toString();
                final String username = etUsername.getText().toString();
                final String email = etEmail.getText().toString();
                final String password = etPassword.getText().toString();

                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");
                            boolean empty = jsonResponse.getBoolean("empty");
                            boolean nameError = jsonResponse.getBoolean("name");
                            boolean userError = jsonResponse.getBoolean("username");
                            boolean emailError = jsonResponse.getBoolean("email");
                            boolean available = jsonResponse.getBoolean("available");

                            if (success){
                                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                RegisterActivity.this.startActivity(intent);
                            }
                            if (empty){
                                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                                builder.setMessage("Field let empty")
                                        .setNegativeButton("Retry", null)
                                        .create()
                                        .show();
                            }
                            if (nameError){
                                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                                builder.setMessage("Invalid Name. Alphabetic only!")
                                        .setNegativeButton("Retry", null)
                                        .create()
                                        .show();
                            }
                            if (userError){
                                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                                builder.setMessage("Invalid username. Please provide a username of at least 8 characters. Alphanumeric only!")
                                        .setNegativeButton("Retry", null)
                                        .create()
                                        .show();
                            }
                            if (emailError) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                                builder.setMessage("ISU emails only please!")
                                        .setNegativeButton("Retry", null)
                                        .create()
                                        .show();
                            }
                            if (!available) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                                builder.setMessage("Username/email already registered.")
                                        .setNegativeButton("Retry", null)
                                        .create()
                                        .show();
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };

                RegisterRequest registerRequest = new RegisterRequest(firstname, lastname, username, email, password, responseListener);
                RequestQueue queue = Volley.newRequestQueue(RegisterActivity.this);
                queue.add(registerRequest);
            }
        });

    }
}