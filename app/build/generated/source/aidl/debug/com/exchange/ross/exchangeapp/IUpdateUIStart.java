/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: /Users/ross/Work/AndroidStudioProjects/Meetings/app/src/main/aidl/com/exchange/ross/exchangeapp/IUpdateUIStart.aidl
 */
package com.exchange.ross.exchangeapp;
// Declare any non-default types here with import statements

public interface IUpdateUIStart extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements com.exchange.ross.exchangeapp.IUpdateUIStart
{
private static final java.lang.String DESCRIPTOR = "com.exchange.ross.exchangeapp.IUpdateUIStart";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an com.exchange.ross.exchangeapp.IUpdateUIStart interface,
 * generating a proxy if needed.
 */
public static com.exchange.ross.exchangeapp.IUpdateUIStart asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof com.exchange.ross.exchangeapp.IUpdateUIStart))) {
return ((com.exchange.ross.exchangeapp.IUpdateUIStart)iin);
}
return new com.exchange.ross.exchangeapp.IUpdateUIStart.Stub.Proxy(obj);
}
@Override public android.os.IBinder asBinder()
{
return this;
}
@Override public boolean onTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags) throws android.os.RemoteException
{
switch (code)
{
case INTERFACE_TRANSACTION:
{
reply.writeString(DESCRIPTOR);
return true;
}
case TRANSACTION_updateUI:
{
data.enforceInterface(DESCRIPTOR);
this.updateUI();
reply.writeNoException();
return true;
}
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements com.exchange.ross.exchangeapp.IUpdateUIStart
{
private android.os.IBinder mRemote;
Proxy(android.os.IBinder remote)
{
mRemote = remote;
}
@Override public android.os.IBinder asBinder()
{
return mRemote;
}
public java.lang.String getInterfaceDescriptor()
{
return DESCRIPTOR;
}
/**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
@Override public void updateUI() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_updateUI, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
}
static final int TRANSACTION_updateUI = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
}
/**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
public void updateUI() throws android.os.RemoteException;
}
