package com.deep.gradhack;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class PaymentActivity extends AppCompatActivity {

    private String userID;
    private String name;
    private String accountNumber;
    private String jsonData;
    private String amount;
    private String cvv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        Intent intent = getIntent();
        jsonData = intent.getStringExtra("qrcodedata");


        try {
            JSONObject jsonObject = new JSONObject(jsonData);
            userID = jsonObject.getString("userID");
            name = jsonObject.getString("name");
            accountNumber = jsonObject.getString("accountNumber");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        createAlertDialog();
        //        CreateAlertDialog();
    }

    public void createAlertDialog() {

        AlertDialog.Builder alertbox = new AlertDialog.Builder(this);

        LinearLayout ll_alert_layout = new LinearLayout(this);
        ll_alert_layout.setOrientation(LinearLayout.VERTICAL);


        final EditText ed_text = new EditText(this);
        ed_text.setInputType(InputType.TYPE_CLASS_NUMBER);
        ed_text.setHint("Amount");

        final EditText ed_text_cvv = new EditText(this);
        ed_text_cvv.setInputType(InputType.TYPE_CLASS_NUMBER);
        ed_text_cvv.setHint("CVV");

        ll_alert_layout.addView(ed_text);
        ll_alert_layout.addView(ed_text_cvv);

        alertbox.setTitle("Enter money to be paid and cvv\n\n");

        //setting linear layout to alert dialog

        alertbox.setView(ll_alert_layout);

        alertbox.setNegativeButton("CANCEL",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                    }
                });

        alertbox.setPositiveButton("PAY",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        amount = ed_text.getText().toString();
                        cvv = ed_text_cvv.getText().toString();
                        Intent intent = new Intent(PaymentActivity.this, FingerPaymentActivity.class);
                        intent.putExtra("jsondata", jsonData);
                        intent.putExtra("amount", amount);
                        intent.putExtra("cvv", cvv);
                        startActivity(intent);
                        finish();
                    }
                });
        alertbox.show();
    }
}
