package com.example.calculator;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    boolean isEqualResult;
    ArrayList<Double> numbers = new ArrayList<>();
    ArrayList<Character> operations = new ArrayList<>();
    String currNo = "";
    TextView result;
    Button buttonReview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        result = findViewById(R.id.result);
        buttonReview = findViewById(R.id.buttonReview);
        buttonReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "tel:9625325185";
                Uri uri = Uri.parse(url);
                Intent i = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(i);
            }
        });
    }

    public void clicked(View v) {
        Button clickedButton = (Button) v;
        String txt = clickedButton.getText().toString();
        String resultText = result.getText().toString();

        if (txt.charAt(0) >= '0' && txt.charAt(0) <= '9') {
            if (isEqualResult || (resultText.equals("0") && txt.equals("0")) || currNo.equals("0"))
                return;
            else if (resultText.equals("0"))
                result.setText(txt);
            else result.setText(resultText + txt);

            currNo += txt;
        }else if(txt.equals(".")){
            if (resultText.charAt(0) == 'C') {
                return;
            }

            if (isEqualResult) {
                isEqualResult = false;
            }

            if(resultText.equals("0") || (currNo.length()>0 && currNo.indexOf('.')==-1)) {
                result.setText(resultText+txt);
                currNo += txt;
            }
        } else if (txt.equals("CL")) {
            result.setText("0");
            currNo = "";
            isEqualResult = false;
            numbers = new ArrayList<>();
            operations = new ArrayList<>();
        } else if (txt.equals("=")) {
            if (isEqualResult || resultText.equals("0")) {
                return;
            }

            if (currNo.equals("")) {
                operations.remove(operations.size() - 1);
            } else {
                double val = Double.valueOf(currNo);
                numbers.add(val);
            }

            for (int i = 0; i < operations.size(); i++) {
                if (operations.get(i) == '/') {
                    if (numbers.get(i + 1) == 0) {
                        result.setText("Cannot divide a no by 0");
                        isEqualResult = true;
                        return;
                    }
                    numbers.set(i, numbers.get(i) / numbers.get(i + 1));
                    numbers.remove(i + 1);
                    operations.remove(i);
                    i--;
                } else if (operations.get(i) == 'x') {
                    numbers.set(i, numbers.get(i) * numbers.get(i + 1));
                    numbers.remove(i + 1);
                    operations.remove(i);
                    i--;
                }
            }

            double ans = numbers.get(0);
            for (int i = 1; i < numbers.size(); i++) {
                if (operations.get(i - 1) == '+') {
                    ans += numbers.get(i);
                } else if (operations.get(i - 1) == '-') {
                    ans -= numbers.get(i);
                }
            }

            String Ans = String.valueOf(ans);
            int decimalPos = Ans.indexOf('.');
            if(Ans.lastIndexOf('E')==-1 && Long.valueOf(Ans.substring(decimalPos+1))==0){
                Ans = Ans.substring(0,decimalPos);
            }

            result.setText(Ans);
            currNo = Ans;
            numbers = new ArrayList<>();
            operations = new ArrayList<>();
            isEqualResult = true;
        } else {
            if (resultText.charAt(0) == 'C') {
                return;
            }

            if (isEqualResult) {
                isEqualResult = false;
            }

            if (resultText.equals("0")) {
                currNo = "0";
            }

            if (currNo.equals("")) {
                operations.set(operations.size() - 1, txt.charAt(0));
                result.setText(resultText.substring(0, resultText.length() - 1) + txt);
                return;
            }
            double val = Double.valueOf(currNo);
            currNo = "";
            numbers.add(val);
            operations.add(txt.charAt(0));
            result.setText(resultText + txt);
        }
    }
}