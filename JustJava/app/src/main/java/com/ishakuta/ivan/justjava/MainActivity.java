package com.ishakuta.ivan.justjava;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.NumberFormat;

public class MainActivity extends ActionBarActivity {
    int quantity = 2;
    private Toast toast = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        displayQuantity(quantity);
    }

    /**
     * Submit order, onclick button
     *
     * @param view button view
     */
    public void submitOrder(View view) {

        CheckBox whippedCreamBox = (CheckBox) findViewById(R.id.whipped_cream_chkbox);
        Boolean isWhippedCream = whippedCreamBox.isChecked();

        CheckBox chocolateBox = (CheckBox) findViewById(R.id.chocolate_chkbox);
        Boolean isChocolate = chocolateBox.isChecked();

        // calculate price
        int price = calculatePrice(isWhippedCream, isChocolate);

        // get name
        EditText nameInput = (EditText) findViewById(R.id.name_input);
        String name = nameInput.getText().toString();

        String priceMessage = createOrderSummary(name, price, isWhippedCream, isChocolate);

        composerEmail("ishakuta@gmail.com", "JustJava $" + price + " order for " + name, priceMessage);
    }

    /**
     * Open email app
     *
     * @param address email
     * @param subject text
     * @param message body
     */
    private void composerEmail(String address, String subject, String message) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);

        intent.setType("text/plain");
        intent.setData(Uri.parse("mailto:"));
        intent.putExtra(Intent.EXTRA_EMAIL, address);
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, message);

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    /**
     * Format order summary message
     *
     * @param name           client name
     * @param price          order price
     * @param isWhippedCream should add cream
     * @param isChocolate    should add chocolate
     * @return message
     */
    private String createOrderSummary(String name, int price, boolean isWhippedCream, boolean isChocolate) {
        String summary = "Name: " + name;

        summary += "\nAdd whipped cream: " + (isWhippedCream ? "Yes" : "No");
        summary += "\nAdd chocolate: " + (isChocolate ? "Yes" : "No");
        summary += "\nQuantity: " + quantity;
        summary += "\nTotal: " + NumberFormat.getCurrencyInstance().format(price);
        summary += "\nThank you!";

        return summary;
    }

    /**
     * Show number in text view
     *
     * @param number number to display
     */
    private void displayQuantity(int number) {
        TextView quantityTextView = (TextView) findViewById(R.id.quantity_text_view);
        quantityTextView.setText(String.format("%d", number));
    }

    /**
     * @param view button view
     */
    public void increment(View view) {
        if (quantity == 100) {
            showToast("Cannot increase number more than 100.");
            return;
        }

        quantity++;
        displayQuantity(quantity);
    }

    /**
     * @param view button view
     */
    public void decrement(View view) {
        if (quantity == 1) {
            showToast("Cannot decrease number any further.");
            return;
        }

        quantity--;
        displayQuantity(quantity);
    }

    /**
     * Hide/show toast, manage one instance
     *
     * @param message message
     */
    private void showToast(String message) {
        if (null != toast) {
            toast.cancel();
        }

        toast = Toast.makeText(
                this,
                message,
                Toast.LENGTH_SHORT
        );
        toast.show();
    }

    /**
     * @param isWhippedCream whether or not to add cream
     * @param isChocolate    whether or not to add chocolate
     * @return return order price
     */
    private int calculatePrice(boolean isWhippedCream, boolean isChocolate) {
        int basePricePerCup = 5;

        int pricePerCup = basePricePerCup + (isWhippedCream ? 1 : 0) + (isChocolate ? 2 : 0);

        return quantity * pricePerCup;
    }
}
