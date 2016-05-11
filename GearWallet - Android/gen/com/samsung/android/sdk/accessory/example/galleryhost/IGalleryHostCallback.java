/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: D:\\02. Projects\\03. GearWallet\\01. Program\\GearWallet - ver2.0\\GearWallet - Android\\src\\com\\samsung\\android\\sdk\\accessory\\example\\galleryhost\\IGalleryHostCallback.aidl
 */
package com.samsung.android.sdk.accessory.example.galleryhost;
public interface IGalleryHostCallback extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements com.samsung.android.sdk.accessory.example.galleryhost.IGalleryHostCallback
{
private static final java.lang.String DESCRIPTOR = "com.samsung.android.sdk.accessory.example.galleryhost.IGalleryHostCallback";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an com.samsung.android.sdk.accessory.example.galleryhost.IGalleryHostCallback interface,
 * generating a proxy if needed.
 */
public static com.samsung.android.sdk.accessory.example.galleryhost.IGalleryHostCallback asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof com.samsung.android.sdk.accessory.example.galleryhost.IGalleryHostCallback))) {
return ((com.samsung.android.sdk.accessory.example.galleryhost.IGalleryHostCallback)iin);
}
return new com.samsung.android.sdk.accessory.example.galleryhost.IGalleryHostCallback.Stub.Proxy(obj);
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
case TRANSACTION_onMessage:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
java.lang.String _arg1;
_arg1 = data.readString();
java.lang.String _arg2;
_arg2 = data.readString();
this.onMessage(_arg0, _arg1, _arg2);
reply.writeNoException();
return true;
}
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements com.samsung.android.sdk.accessory.example.galleryhost.IGalleryHostCallback
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
@Override public void onMessage(java.lang.String message, java.lang.String peerApp, java.lang.String accessoryName) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(message);
_data.writeString(peerApp);
_data.writeString(accessoryName);
mRemote.transact(Stub.TRANSACTION_onMessage, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
}
static final int TRANSACTION_onMessage = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
}
public void onMessage(java.lang.String message, java.lang.String peerApp, java.lang.String accessoryName) throws android.os.RemoteException;
}
