package xyz.hanks.draw;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private SmokeUp text;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        text = (SmokeUp) findViewById(R.id.text);
    }

    public void click(View view) {
        Toast.makeText(this, "dsasd", Toast.LENGTH_SHORT).show();
        text.anim();
    }

}
