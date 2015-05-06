// ISync.aidl
package com.exchange.ross.exchangeapp;

import com.exchange.ross.exchangeapp.IUpdateUIStart;
// Declare any non-default types here with import statements

interface ISync {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    void attachUIUpdate(IUpdateUIStart uiUpdater);
}
