package com.example.lab32;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import java.math.BigDecimal;
import java.math.RoundingMode;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    RadioGroup radioGroup1;
    RadioGroup radioGroup2;
    RadioGroup radioGroup3;
    RadioButton radioButton1;
    RadioButton radioButton2;
    RadioButton radioButton3;
    static double speed;
    static double deadline;
    static int iteration;
    static double errorProb;
    static double[] w = new double[]{0, 0};
    static int[][] point = new int[4][];
    static int stop = 0;
    static BigDecimal bd;
    static int P = 4;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        radioGroup1 = findViewById(R.id.radioGroup1);
        radioGroup2 = findViewById(R.id.radioGroup2);
        radioGroup3 = findViewById(R.id.radioGroup3);
    }

    public void checkFirstButton(View v) {
        int radioId = radioGroup1.getCheckedRadioButtonId();
        radioButton1 = findViewById(radioId);
        Toast.makeText(this, "Selected speed = " + radioButton1.getText(),
                Toast.LENGTH_SHORT).show();
    }
    public void checkSecondButton(View v) {
        int radioId = radioGroup2.getCheckedRadioButtonId();
        radioButton2 = findViewById(radioId);
        Toast.makeText(this, "Selected time = " + radioButton2.getText() + "с",
                Toast.LENGTH_SHORT).show();
    }
    public void checkThirdButton(View v) {
        int radioId = radioGroup3.getCheckedRadioButtonId();
        radioButton3 = findViewById(radioId);
        Toast.makeText(this, "Selected iteration = " + radioButton3.getText(),
                Toast.LENGTH_SHORT).show();
    }
    public void startIteration(View v) {
        radioGroup1 = findViewById(R.id.radioGroup1);
        radioGroup3 = findViewById(R.id.radioGroup3);
        int radioId1 = radioGroup1.getCheckedRadioButtonId();
        int radioId3 = radioGroup3.getCheckedRadioButtonId();
        radioButton1 = findViewById(radioId1);
        radioButton3 = findViewById(radioId3);
        pointsInput();
        speed = Double.parseDouble(radioButton1.getText().toString());
        iteration = Integer.parseInt(radioButton3.getText().toString());
        w[0] = w[1] = 0;
        for (int i = 0; i < iteration; i++) {
            errorProb = errorProb - (point[i % 2][0] * w[0] + point[i % 2][1] * w[1]);
            w[0] = w[0] + errorProb * speed * point[i % 2][0];
            w[1] = w[1] + errorProb * speed * point[i % 2][1];
            bd = new BigDecimal(w[0]).setScale(3, RoundingMode.HALF_EVEN);
            w[0] = bd.doubleValue();
            bd = new BigDecimal(w[1]).setScale(3, RoundingMode.HALF_EVEN);
            w[1] = bd.doubleValue();
            if((w[0] * point[0][0] + w[1] * point[0][1] > P && w[0] * point[1][0] + w[1] * point[1][1]< P) ||
                    (w[0] * point[0][0] + w[1] * point[0][1] < P && w[0] * point[1][0] + w[1] * point[1][1] > P)) {
                break;
            }
        }
        Toast.makeText(this, w[0] + " " + w[1],
                Toast.LENGTH_SHORT).show();
    }

    public void startTime(View v) throws InterruptedException {
        radioGroup1 = findViewById(R.id.radioGroup1);
        radioGroup2 = findViewById(R.id.radioGroup2);
        int radioId1 = radioGroup1.getCheckedRadioButtonId();
        int radioId2 = radioGroup2.getCheckedRadioButtonId();
        radioButton1 = findViewById(radioId1);
        radioButton2 = findViewById(radioId2);
        pointsInput();
        speed = Double.parseDouble(radioButton1.getText().toString());
        deadline = Double.parseDouble(radioButton2.getText().toString());
        ThisThreadOfMine Task1 = new ThisThreadOfMine("Task1");
        Task1.start();
        Thread.sleep((long) (deadline * 1000));
        stop = 1;
        Toast.makeText(this, w[0] + " " + w[1],
                Toast.LENGTH_SHORT).show();
    }
    public void pointsInput() {
        errorProb = P;
        point[0] = new int[]{0, 6};
        point[1] = new int[]{1, 5};
        point[2] = new int[]{3, 3};
        point[3] = new int[]{2, 4};
    }
}


class ThisThreadOfMine extends Thread {
    ThisThreadOfMine(String name) {
        super(name);
    }
    public void run() {
        for (int i = 0; MainActivity.stop==0 ; i++) {
            MainActivity.errorProb = MainActivity.errorProb - (MainActivity.point[i % 2][0] * MainActivity.w[0] + MainActivity.point[i % 2][1] * MainActivity.w[1]);
            MainActivity.w[0] = MainActivity.w[0] + MainActivity.errorProb * MainActivity.speed * MainActivity.point[i % 2][0];
            MainActivity.w[1] = MainActivity.w[1] + MainActivity.errorProb * MainActivity.speed * MainActivity.point[i % 2][1];
            MainActivity.bd = new BigDecimal(MainActivity.w[0]).setScale(3, RoundingMode.HALF_EVEN);
            MainActivity.w[0] = MainActivity.bd.doubleValue();
            MainActivity.bd = new BigDecimal(MainActivity.w[1]).setScale(3, RoundingMode.HALF_EVEN);
            MainActivity.w[1] = MainActivity.bd.doubleValue();
            System.out.println(MainActivity.w[0] + " " + MainActivity.w[1]);
            if(MainActivity.w[0]*MainActivity.point[0][0] + MainActivity.w[1]*MainActivity.point[0][1]>MainActivity.P &&
                    MainActivity.w[0]*MainActivity.point[1][0] + MainActivity.w[1]*MainActivity.point[1][1]<MainActivity.P) {
                break;
            }
        }
    }
}
