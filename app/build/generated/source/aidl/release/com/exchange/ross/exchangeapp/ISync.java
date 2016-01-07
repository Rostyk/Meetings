/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: /Users/rstepanyak/Work/android/Meetings/app/src/main/aidl/com/exchange/ross/exchangeapp/ISync.aidl
 */
package com.exchange.ross.exchangeapp;
// Declare any non-default types here with import statements

public interface ISync extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements com.exchange.ross.exchangeapp.ISync
{
private static final java.lang.String DESCRIPTOR = "com.exchange.ross.exchangeapp.ISync";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an com.exchange.ross.exchangeapp.ISync interface,
 * generating a proxy if needed.
 */
public static com.exchange.ross.exchangeapp.ISync asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof com.exchange.ross.exchangeapp.ISync))) {
return ((com.exchange.ross.exchangeapp.ISync)iin);
}
return new com.exchange.ross.exchangeapp.ISync.Stub.Proxy(obj);
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
case TRANSACTION_sync:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
this.sync(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_attachUIUpdate:
{
data.enforceInterface(DESCRIPTOR);
com.exchange.ross.exchangeapp.IUpdateUIStart _arg0;
_arg0 = com.exchange.ross.exchangeapp.IUpdateUIStart.Stub.asInterface(data.readStrongBinder());
this.attachUIUpdate(_arg0);
reply.writeNoException();
return true;
}
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements com.exchange.ross.exchangeapp.ISync
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
//force sync

@Override public void sync(java.lang.String accountName) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(accountName);
mRemote.transact(Stub.TRANSACTION_sync, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
//UI update callback

@Override public void attachUIUpdate(com.exchange.ross.exchangeapp.IUpdateUIStart uiUpdater) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeStrongBinder((((uiUpdater!=null))?(uiUpdater.asBinder()):(null)));
mRemote.transact(Stub.TRANSACTION_attachUIUpdate, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
}
static final int TRANSACTION_sync = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
static final int TRANSACTION_attachUIUpdate = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
}
//force sync

public void sync(java.lang.String accountName) throws android.os.RemoteException;
//UI update callback

public void attachUIUpdate(com.exchange.ross.exchangeapp.IUpdateUIStart uiUpdater) throws android.os.RemoteException;
}
