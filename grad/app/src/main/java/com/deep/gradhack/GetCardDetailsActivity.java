package com.deep.gradhack;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.deep.gradhack.Domain.CardDetails;
import com.deep.gradhack.Helper.MyHelper;
import com.deep.gradhack.database.DatabaseHelper;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.cloud.FirebaseVisionCloudDetectorOptions;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.text.FirebaseVisionCloudTextRecognizerOptions;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.google.firebase.ml.vision.text.FirebaseVisionTextRecognizer;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GetCardDetailsActivity extends BaseActivity implements View.OnClickListener {
    private Bitmap mBitmap;
    private ImageView mImageView;
    private TextView mTextView;
    private DatabaseHelper db;

    private String cardNumber;
    private String expiryDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_card_details);

        mTextView = findViewById(R.id.text_view);
        mImageView = findViewById(R.id.image_view);
        findViewById(R.id.btn_cloud).setOnClickListener(this);
        findViewById(R.id.btn_device).setOnClickListener(this);

        db = new DatabaseHelper(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_device:
                Toast.makeText(this, "1234", Toast.LENGTH_SHORT).show();
                if (mTextView.getText().toString() != ""){
                    addDetailsToDb();
                }
                    break;
            case R.id.btn_cloud:
                if (mBitmap != null) {
                    runCloudTextRecognition();
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case RC_STORAGE_PERMS1:
                case RC_STORAGE_PERMS2:
                    checkStoragePermission(requestCode);
                    break;
                case RC_SELECT_PICTURE:
                    Uri dataUri = data.getData();
                    String path = MyHelper.getPath(this, dataUri);
                    if (path == null) {
                        mBitmap = MyHelper.resizeImage(imageFile, this, dataUri, mImageView);
                    } else {
                        mBitmap = MyHelper.resizeImage(imageFile, path, mImageView);
                    }
                    if (mBitmap != null) {
                        mTextView.setText(null);
                        mImageView.setImageBitmap(mBitmap);
                    }
                    break;
                case RC_TAKE_PICTURE:
                    mBitmap = MyHelper.resizeImage(imageFile, imageFile.getPath(), mImageView);
                    if (mBitmap != null) {
                        mTextView.setText(null);
                        mImageView.setImageBitmap(mBitmap);
                    }
                    break;
            }
        }
    }

    private void runTextRecognition() {
        FirebaseVisionImage image = FirebaseVisionImage.fromBitmap(mBitmap);
        FirebaseVisionTextRecognizer detector = FirebaseVision.getInstance().getOnDeviceTextRecognizer();
        detector.processImage(image).addOnSuccessListener(new OnSuccessListener<FirebaseVisionText>() {
            @Override
            public void onSuccess(FirebaseVisionText texts) {
                processTextRecognitionResult(texts);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                e.printStackTrace();
            }
        });
    }

    private void runCloudTextRecognition() {
        MyHelper.showDialog(this);

        FirebaseVisionCloudTextRecognizerOptions options = new FirebaseVisionCloudTextRecognizerOptions.Builder()
                .setLanguageHints(Arrays.asList("en", "hi"))
                .setModelType(FirebaseVisionCloudDetectorOptions.LATEST_MODEL)
                .build();

        FirebaseVisionImage image = FirebaseVisionImage.fromBitmap(mBitmap);
        FirebaseVisionTextRecognizer detector = FirebaseVision.getInstance().getCloudTextRecognizer(options);

        detector.processImage(image).addOnSuccessListener(new OnSuccessListener<FirebaseVisionText>() {
            @Override
            public void onSuccess(FirebaseVisionText texts) {
                MyHelper.dismissDialog();
                processTextRecognitionResult(texts);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                MyHelper.dismissDialog();
                e.printStackTrace();
            }
        });
    }

    private void processTextRecognitionResult(FirebaseVisionText firebaseVisionText) {
        mTextView.setText(null);
        if (firebaseVisionText.getTextBlocks().size() == 0) {
            mTextView.setText(R.string.error_not_found);
            return;
        }
        for (FirebaseVisionText.TextBlock block : firebaseVisionText.getTextBlocks()) {

            Pattern pattern = Pattern.compile("\\d+");
            String cardData = block.getText();
            Matcher matcher = pattern.matcher(cardData);
            while (matcher.find()) {
                mTextView.append(matcher.group());
            }

        }
        detectCardDetails();
    }

    private void detectCardDetails() {
        String temp = mTextView.getText().toString();
        cardNumber = temp.substring(0, 16);
        expiryDate = temp.substring(temp.length() - 4);

        StringBuilder a = new StringBuilder();
        a.append(cardNumber.substring(0, 4) + " ");
        a.append(cardNumber.substring(4, 8) + " ");
        a.append(cardNumber.substring(8, 12) + " ");
        a.append(cardNumber.substring(12, 16));

        cardNumber = a.toString();

        StringBuilder b = new StringBuilder();
        b.append(expiryDate.substring(0, 2) + "/" + expiryDate.substring(2, 4));
        expiryDate = b.toString();

        mTextView.setText("Card Number = " + cardNumber + "\nExpiry Date = " + expiryDate);


    }

    public void addDetailsToDb() {

        Toast.makeText(this, "Hello", Toast.LENGTH_SHORT).show();
        db.insertCard(new CardDetails(cardNumber, expiryDate));

        Intent intent = new Intent(GetCardDetailsActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();

    }
}