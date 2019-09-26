package com.deep.gradhack;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.deep.gradhack.Adapter.CardDetailsAdapter;
import com.deep.gradhack.Domain.CardDetails;
import com.deep.gradhack.database.DatabaseHelper;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {

    private DatabaseHelper db;
    ImageView addCardImage;
    ImageView payImage;
    ImageView receiveImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        db = new DatabaseHelper(this);

        ArrayList<CardDetails> cardDetails = db.getAllCards();

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        CardDetailsAdapter adapter = new CardDetailsAdapter(cardDetails);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(adapter);


        addCardImage = findViewById(R.id.addcard);
        payImage = findViewById(R.id.pay);
        receiveImage = findViewById(R.id.receive);

        addCardImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, GetCardDetailsActivity.class);
                startActivity(intent);
            }
        });

        payImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, QRCodeScannerActivity.class);
                startActivity(intent);
            }
        });

        receiveImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, QRCodeGeneratorActivity.class);
                startActivity(intent);
            }
        });
    }
}
