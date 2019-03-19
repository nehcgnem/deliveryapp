package com.plusbueno.plusbueno.view;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Context;


import com.plusbueno.plusbueno.R;
import com.plusbueno.plusbueno.data.LocalCart;
import com.plusbueno.plusbueno.data.PaymentInfo;
import com.plusbueno.plusbueno.parser.LoginManager;
import com.plusbueno.plusbueno.parser.UniversalParser;
import com.plusbueno.plusbueno.parser.exception.AuthorizationException;
import com.plusbueno.plusbueno.parser.util.HttpUtil;
import com.stripe.android.Stripe;
import com.stripe.android.TokenCallback;
import com.stripe.android.model.Card;
import com.stripe.android.model.Token;
import com.stripe.android.view.CardInputWidget;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;



public class PaymentActivity extends AppCompatActivity {
    CardInputWidget mCardInputWidget;
    private Context mContext;
    private int total = 0;
    private String mOrderName;

    //    ErrorDialogHandler mErrorDialogHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext= getApplicationContext();
        setContentView(R.layout.activity_payment);
        total = getIntent().getIntExtra("TOTAL", 0);  // get total payment
        mOrderName = getIntent().getStringExtra("ORDER_NAME");
        mCardInputWidget = findViewById(R.id.card_input_widget);
        Button payButton = findViewById(R.id.payButton);
        payButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Card cardToSave = mCardInputWidget.getCard();
                if (cardToSave == null) {
                    Context context = getApplicationContext();
                    CharSequence text = "Invalid credit card!";
                    int duration = Toast.LENGTH_SHORT;

                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                }
                else {
                    Stripe stripe = new Stripe(mContext, "pk_test_iopGxht28qhyvwfPH7INqjF8");
                    stripe.createToken(
                            cardToSave,
                            new TokenCallback() {
                                public void onSuccess(Token token) {
                                    // Send token to your server
                                    // price to server
                                    PaymentInfo paymentInfo = new PaymentInfo();
                                    paymentInfo.setPrice(total);  // price
                                    paymentInfo.setToken(token);

                                    PostPaymentTask task = new PostPaymentTask();
                                    task.execute(paymentInfo);

                                    // Show success message
//                                    Context context = getApplicationContext();
//                                    CharSequence text = "Success to generate token!" + token.toString();
//                                    Log.e("PAYMTTKN", text.toString());
                                }
                                public void onError(Exception error) {
                                    // Show error message
                                    Context context = getApplicationContext();
                                    CharSequence text = "Failed to generate token!";
                                    int duration = Toast.LENGTH_SHORT;

                                    Toast toast = Toast.makeText(context, text, duration);
                                    toast.show();

                                }
                            }
                    );
                }
            }
        });




//        cardToSave.setName("Customer Name");
//        cardToSave.setAddressZip("12345");

    }

    public void toastMe(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    public void jumpToTracking() {
        toastMe("Payment successful");
        Intent i = new Intent(this, MapsActivity.class);
        i.putExtra("ORDER_NAME", mOrderName);
        startActivity(i);
        finish();

    }



    private class PostPaymentTask extends AsyncTask<PaymentInfo, String, String> { // TODO returning type
        private Exception exception;

        @Override
        protected String doInBackground(PaymentInfo... paymentInfos) {
            try {
                String result = HttpUtil.post(UniversalParser.BASE_URL_RESTFUL + "/payment/" + LoginManager.getUsername() + "/" + mOrderName + "/", paymentInfos[0], PaymentInfo.class );
                if (result.contains("False")) throw new AuthorizationException();  // check result

            } catch (Exception e) {
                exception = e;
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (exception != null) {
//                if (exception instanceof AuthorizationException) {
                    toastMe("Payment failed!");

//                }

            }
            else  {
                // on payment success
                // clear cart
                // show success
                jumpToTracking();
            }
        }
    }

    private class SubmitOrderTask extends AsyncTask<String, String, String> {
        private String orderName;
        private Exception exception;
        private PaymentActivity activity;

        public SubmitOrderTask(PaymentActivity activity) {
            this.activity = activity;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (exception != null) {
                activity.jumpToTracking();
            } else {
                if(exception.getMessage() != null) {
                    activity.toastMe(exception.getMessage());
                } else {
                    activity.toastMe("Payment failed");
                }
            }
        }


        @Override
        protected String doInBackground(String... strings) {
            try{
                orderName = LocalCart.checkout();
            } catch (Exception e) {
                exception = e;
            }
            return orderName;
        }
    }
}
