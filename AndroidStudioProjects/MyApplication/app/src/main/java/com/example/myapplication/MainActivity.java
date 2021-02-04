package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    Button button;
    TextView textView;
    int i;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button =findViewById(R.id.button);
        textView =findViewById(R.id.text);
        String hello = getString(R.string.hello);
        String bye = getString(R.string.bye);
        i=0;
        button.setOnClickListener(v -> {
            if(i==0){
                textView.setText(hello);
                i=1;
            }else{
                textView.setText(bye);
                i=0;
            }
        });
    }
}