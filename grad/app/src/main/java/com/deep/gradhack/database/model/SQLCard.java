package com.deep.gradhack.database.model;

public class SQLCard  {

    public static final String TABLE_NAME = "cards";
    public static final String CARD_NUMBER = "cardnumber";
    public static final String EXPIRY_DATE = "expirydate";

    private String cardNumber;
    private String expiryDate;

    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + CARD_NUMBER + " TEXT PRIMARY KEY ,"
                    + EXPIRY_DATE + " TEXT"
                    + ")";

    public SQLCard() {}

    public SQLCard(String cardNumber, String expiryDate) {
        this.cardNumber = cardNumber;
        this.expiryDate = expiryDate;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

}
