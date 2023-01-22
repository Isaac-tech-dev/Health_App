package spotters.tech.healthapp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Button Btn;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Btn = findViewById(R.id.start);
        Btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent start = new Intent(MainActivity.this, Sign_In.class);
        startActivity(start);
    }
}