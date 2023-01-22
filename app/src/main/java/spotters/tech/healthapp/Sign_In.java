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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Sign_In extends AppCompatActivity implements View.OnClickListener {
    EditText ph,p;
    TextView text;
    Button Btn;
    ProgressBar loading;

    SessionManager sessionManager;

    private static String url_signin = "https://spotters.tech/health/android/user_login.php";

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        sessionManager = new SessionManager(this);

        ph = findViewById(R.id.phone);
        p = findViewById(R.id.password);
        Btn = findViewById(R.id.btn);
        loading = findViewById(R.id.load);
        text = findViewById(R.id.text);

        Btn.setOnClickListener(this);
        text.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        String Mphone = ph.getText().toString().trim();
        String Mpassword = p.getText().toString().trim();

        if(Mphone.isEmpty() && Mpassword.isEmpty()){
            ph.setError("Fill in the details");
            p.setError("Fill in the details");
        }else{
            ProceedLogin();
        }

        Intent register = new Intent(Sign_In.this, Sign_Up.class);
        startActivity(register);
        finish();
    }

    private void ProceedLogin() {
        Btn.setVisibility(View.GONE);
        loading.setVisibility(View.VISIBLE);

        final String phone = this.ph.getText().toString().trim();
        final String password = this.p.getText().toString().trim();


        StringRequest stringRequest = new StringRequest(Request.Method.POST, url_signin, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");
                    JSONArray jsonArray = jsonObject.getJSONArray("login");

                    if(success.equals("1")){
                        for(int i = 0; i < jsonObject.length(); i++){
                            JSONObject object = jsonArray.getJSONObject(i);
                            String firstname = object.getString("firstname").trim();
                            String lastname = object.getString("lastname").trim();
                            String email = object.getString("email").trim();
                            String phone = object.getString("phone").trim();
                            String id = object.getString("id").trim();
                            String sign_up_date = object.getString("sign_up_date").trim();

                            sessionManager.createSession(firstname, lastname, email,phone,id);

                            Toast.makeText(Sign_In.this, "Login Successful", Toast.LENGTH_SHORT).show();
                            Intent welcome = new Intent(Sign_In.this, Dashboard.class);
                            welcome.putExtra("firstname", firstname);
                            welcome.putExtra("lastname", lastname);
                            welcome.putExtra("email", email);
                            welcome.putExtra("phone", phone);
                            welcome.putExtra("id", id);
                            startActivity(welcome);
                            finish();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Btn.setVisibility(View.VISIBLE);
                    loading.setVisibility(View.GONE);
                    Toast.makeText(Sign_In.this, "Login Failed!!!!!!!!!!", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Btn.setVisibility(View.VISIBLE);
                loading.setVisibility(View.GONE);
                Toast.makeText(Sign_In.this, "Login Failed!!!!!!!!!!", Toast.LENGTH_SHORT).show();
            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> login = new HashMap<>();
                login.put("phone", phone);
                login.put("password", password);
                return login;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}