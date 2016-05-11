/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: D:\\02. Projects\\03. GearWallet\\01. Program\\GearWallet - ver2.0\\GearWallet - Android\\src\\com\\samsung\\android\\sdk\\accessory\\example\\galleryhost\\IGalleryHostAction.aidl
 */
package com.samsung.android.sdk.accessory.example.galleryhost;
public interface IGalleryHostAction extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements com.samsung.android.sdk.accessory.example.galleryhost.IGalleryHostAction
{
private static final java.lang.String DESCRIPTOR = "com.samsung.android.sdk.accessory.example.galleryhost.IGalleryHostAction";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an com.samsung.android.sdk.accessory.example.galleryhost.IGalleryHostAction interface,
 * generating a proxy if needed.
 */
public static com.samsung.android.sdk.accessory.example.galleryhost.IGalleryHostAction asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof com.samsung.android.sdk.accessory.example.galleryhost.IGalleryHostAction))) {
return ((com.samsung.android.sdk.accessory.example.galleryhost.IGalleryHostAction)iin);
}
return new com.samsung.android.sdk.accessory.example.galleryhost.IGalleryHostAction.Stub.Proxy(obj);
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
case TRANSACTION_registerCallback:
{
data.enforceInterface(DESCRIPTOR);
com.samsung.android.sdk.accessory.example.galleryhost.IGalleryHostCallback _arg0;
_arg0 = com.samsung.android.sdk.accessory.example.galleryhost.IGalleryHostCallback.Stub.asInterface(data.readStrongBinder());
this.registerCallback(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_disconnect:
{
data.enforceInterface(DESCRIPTOR);
this.disconnect();
reply.writeNoException();
return true;
}
case TRANSACTION_push:
{
data.enforceInterface(DESCRIPTOR);
this.push();
reply.writeNoException();
return true;
}
case TRANSACTION_next:
{
data.enforceInterface(DESCRIPTOR);
this.next();
reply.writeNoException();
return true;
}
case TRANSACTION_unregisterCallback:
{
data.enforceInterface(DESCRIPTOR);
this.unregisterCallback();
reply.writeNoException();
return true;
}
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements com.samsung.android.sdk.accessory.example.galleryhost.IGalleryHostAction
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
@Override public void registerCallback(com.samsung.android.sdk.accessory.example.galleryhost.IGalleryHostCallback callback) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeStrongBinder((((callback!=null))?(callback.asBinder()):(null)));
mRemote.transact(Stub.TRANSACTION_registerCallback, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void disconnect() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_disconnect, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void push() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_push, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void next() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_next, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void unregisterCallback() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_unregisterCallback, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
}
static final int TRANSACTION_registerCallback = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
static final int TRANSACTION_disconnect = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
static final int TRANSACTION_push = (android.os.IBinder.FIRST_CALL_TRANSACTION + 2);
static final int TRANSACTION_next = (android.os.IBinder.FIRST_CALL_TRANSACTION + 3);
static final int TRANSACTION_unregisterCallback = (android.os.IBinder.FIRST_CALL_TRANSACTION + 4);
}
public void registerCallback(com.samsung.android.sdk.accessory.example.galleryhost.IGalleryHostCallback callback) throws android.os.RemoteException;
public void disconnect() throws android.os.RemoteException;
public void push() throws android.os.RemoteException;
public void next() throws android.os.RemoteException;
public void unregisterCallback() throws android.os.RemoteException;
}
