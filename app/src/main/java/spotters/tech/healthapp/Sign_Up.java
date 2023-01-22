package spotters.tech.healthapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Sign_Up extends AppCompatActivity {
    EditText fn,ln,em,ph,p;
    TextView text;
    Button Btn;
    ProgressBar loading;

    private static String url_signup = "https://spotters.tech/health/android/userreg.php";

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        //Initialize Variabvles
        fn = findViewById(R.id.firstname);
        ln = findViewById(R.id.lastname);
        em = findViewById(R.id.email);
        ph = findViewById(R.id.phone);
        p = findViewById(R.id.password);
        Btn = findViewById(R.id.btn);
        loading = findViewById(R.id.load);
        text = findViewById(R.id.text);

        text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent login = new Intent(Sign_Up.this, Sign_In.class);
                startActivity(login);
                finish();
            }
        });


        Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Mfirstname = fn.getText().toString().trim();
                String Mlastname = ln.getText().toString().trim();
                String Memail = em.getText().toString().trim();
                String Mphone = ph.getText().toString().trim();
                String Mpassword = p.getText().toString().trim();

                if(Mfirstname.isEmpty() && Mlastname.isEmpty() && Memail.isEmpty() && Mphone.isEmpty() && Mpassword.isEmpty()){
                    fn.setError("Fill in the details");
                    ln.setError("Fill in the details");
                    em.setError("Fill in the details");
                    ph.setError("Fill in the details");
                    p.setError("Fill in the details");
                }else{
                    ProceedRegistration();
                }
            }
        });
    }

    private void ProceedRegistration() {
        Btn.setVisibility(View.GONE);
        loading.setVisibility(View.VISIBLE);

        final String firstname = this.fn.getText().toString().trim();
        final String lastname = this.ln.getText().toString().trim();
        final String email = this.em.getText().toString().trim();
        final String phone = this.ph.getText().toString().trim();
        final String password = this.p.getText().toString().trim();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url_signup, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try{
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");

                    if(success.equals("1")){
                        Toast.makeText(Sign_Up.this, "Registration Succesful", Toast.LENGTH_SHORT).show();
                        Intent register = new Intent(Sign_Up.this, Sign_In.class);
                        startActivity(register);
                        finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(Sign_Up.this, "Registration Failed", Toast.LENGTH_SHORT).show();
                    Btn.setVisibility(View.VISIBLE);
                    loading.setVisibility(View.GONE);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Sign_Up.this, "Registration Failed", Toast.LENGTH_SHORT).show();
                Btn.setVisibility(View.VISIBLE);
                loading.setVisibility(View.GONE);
            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> register = new HashMap<>();
                register.put("firstname", firstname);
                register.put("lastname", lastname);
                register.put("email", email);
                register.put("phone", phone);
                register.put("password", password);
                return register;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}