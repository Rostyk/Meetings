package com.exchange.ross.exchangeapp.Utils;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.ContextMenu;

import com.exchange.ross.exchangeapp.Utils.billing.IabHelper;
import com.exchange.ross.exchangeapp.Utils.billing.IabResult;
import com.exchange.ross.exchangeapp.Utils.billing.Purchase;

/**
 * Created by ross on 4/23/15.
 */
public class PurchaseManager {

    private static final String TAG =
            "billings";

    static final String ITEM_SKU = "android.test.purchased";
    public IabHelper mHelper;

    private static PurchaseManager instance;
    public static synchronized PurchaseManager sharedManager() {
        if (instance == null) {
            instance = new PurchaseManager();
        }
        return instance;
    }

    public void init(Context context) {
        String base64EncodedPublicKey =
                "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAp6Y6lfvOv7vIXtBmz3ZqGCM6QffjWD5giQxWAGAYI9Dp1mzKgb2chE8sP7exzVfJXHnfvELDv9TtX9Y3qpOx0Ekg5cWGjCZ/MChY8Oy5Hq9fyAvh5ohzPIOQjlcLJuJeAWRDeenjMnQsQkkCIvfkuyeokwTr3w+qKvexqUcERBedfrEzV5My6YOajMXa3ypZnxC7LkO2QpgH0I72gdmaZAZ2wQLDGzZw3k7XpJy8rOiEqLbsAuVje/Tvtq/xuStgNEEbY21QKby7OlZ9PccAuAAgvLixL9tOihdz+awDxiMeHeZBkmCJt9AL7Y6voyH83I+3jZvjJWAjzLdq+G9kgwIDAQAB";

        mHelper = new IabHelper(context, base64EncodedPublicKey);

        mHelper.startSetup(new
                                   IabHelper.OnIabSetupFinishedListener() {
                                       public void onIabSetupFinished(IabResult result)
                                       {
                                           if (!result.isSuccess()) {
                                               Log.d(TAG, "In-app Billing setup failed: " +
                                                       result);
                                           } else {
                                               Log.d(TAG, "In-app Billing is set up OK");
                                           }
                                       }
                                   });
    }

    public void buy(Activity activity) {
        mHelper.launchPurchaseFlow(activity, ITEM_SKU, 10001,
                mPurchaseFinishedListener, "mypurchasetoken");
    }

    IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener
            = new IabHelper.OnIabPurchaseFinishedListener() {
        public void onIabPurchaseFinished(IabResult result,
                                          Purchase purchase) {

            int res = result.getResponse();
            if(res == 7) {
                Log.v("Purchase", "Already owned");
            }

            if (result.isFailure()) {
                return;
            }


        }

    };
}
