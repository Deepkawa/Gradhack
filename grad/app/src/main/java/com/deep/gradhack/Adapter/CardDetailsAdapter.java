package com.deep.gradhack.Adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.deep.gradhack.Domain.CardDetails;
import com.deep.gradhack.R;

import java.util.ArrayList;

public class CardDetailsAdapter extends RecyclerView.Adapter<CardDetailsAdapter.ViewHolder> {

    private ArrayList<CardDetails> cardDetailsArrayList;

    public CardDetailsAdapter(ArrayList<CardDetails> cardDetailsArrayList) {
        this.cardDetailsArrayList = cardDetailsArrayList;
    }

    @NonNull
    @Override
    public CardDetailsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.list_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final CardDetailsAdapter.ViewHolder viewHolder, int position) {

        CardDetails cardDetails = cardDetailsArrayList.get(position);
        String tp = viewHolder.textView.getText().toString();
        Log.e("Deep",tp);
        viewHolder.textView.setText(cardDetails.getCardNumber());
        viewHolder.expiryDate.setText(cardDetails.getExpiryDate());
    }

    @Override
    public int getItemCount() {
        return cardDetailsArrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public TextView textView;
        public TextView expiryDate;
        public RelativeLayout relativeLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            this.expiryDate = itemView.findViewById(R.id.expiryDate);
            this.imageView = itemView.findViewById(R.id.image_view);
            this.textView = itemView.findViewById(R.id.textView);
            relativeLayout = (RelativeLayout) itemView.findViewById(R.id.relativeLayout);
        }
    }
}
