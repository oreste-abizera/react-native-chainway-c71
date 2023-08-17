package com.reactnativechainwayc71;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.Context;

import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.module.annotations.ReactModule;
import com.facebook.react.bridge.LifecycleEventListener;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;

import com.barcode.BarcodeUtility;

@ReactModule(name = ChainwayC71Module.NAME)
public class ChainwayC71Module extends ReactContextBaseJavaModule implements LifecycleEventListener {
    public static final String NAME = "ChainwayC71";
    private final ReactApplicationContext reactContext;
    private static BarcodeUtility barcodeUtility = null;
    private static BarcodeDataReceiver barcodeDataReceiver = null;

    public ChainwayC71Module(ReactApplicationContext reactContext) {
        super(reactContext);
        this.reactContext = reactContext;
        this.reactContext.addLifecycleEventListener(this);
    }

    @Override
    @NonNull
    public String getName() {
        return NAME;
    }

    @Override
    public void onHostDestroy() {
        disconnect();
    }

    @Override
    public void onHostResume() {
    }

    @Override
    public void onHostPause() {
    }

    private void connect() {
            // Barcode
            if (barcodeUtility == null) {
                barcodeUtility = BarcodeUtility.getInstance();
            }

            barcodeUtility.setOutputMode(this.reactContext, 2);// Broadcast receive data
            barcodeUtility.setScanResultBroadcast(this.reactContext, "com.scanner.broadcast", "data"); // Set Broadcast
            barcodeUtility.open(this.reactContext, BarcodeUtility.ModuleType.BARCODE_2D);
            barcodeUtility.setReleaseScan(this.reactContext, true);
            barcodeUtility.setScanFailureBroadcast(this.reactContext, false);
            barcodeUtility.enableContinuousScan(this.reactContext, true);
            barcodeUtility.enablePlayFailureSound(this.reactContext, true);
            barcodeUtility.enablePlaySuccessSound(this.reactContext, true);
            barcodeUtility.enableEnter(this.reactContext, false);
            barcodeUtility.setBarcodeEncodingFormat(this.reactContext, 1);

            if (barcodeDataReceiver == null) {
                barcodeDataReceiver = new BarcodeDataReceiver();
                IntentFilter intentFilter = new IntentFilter();
                intentFilter.addAction("com.scanner.broadcast");
                this.reactContext.registerReceiver(barcodeDataReceiver, intentFilter);
            }
    }

    private void disconnect() {
        if (barcodeUtility != null) {
            barcodeUtility.close(this.reactContext, BarcodeUtility.ModuleType.BARCODE_2D);
            barcodeUtility = null;
        }

        if (barcodeDataReceiver != null) {
            this.reactContext.unregisterReceiver(barcodeDataReceiver);
            barcodeDataReceiver = null;
        }
    }

    @ReactMethod
    public void initReader(Promise promise) {
        try {
            connect();
            promise.resolve(true);
        } catch (Exception err) {
            promise.reject("INIT_READER_ERROR",err.getLocalizedMessage());
        }
    }

    @ReactMethod
    public void deinitReader(Promise promise) {
        try {
            disconnect();
            promise.resolve(true);
        } catch (Exception err) {
            promise.reject("DEINIT_READER_ERROR",err.getLocalizedMessage());
        }
    }

    @ReactMethod
    public void barcodeRead(Promise promise) {

        try {
            if (barcodeUtility != null) {
                barcodeUtility.startScan(this.reactContext, BarcodeUtility.ModuleType.BARCODE_2D);
                promise.resolve(true);
            }
            promise.resolve(false);
        } catch (Exception err) {
            promise.reject("BARCODE_READ_ERROR",err.getLocalizedMessage());
        }
    }

    @ReactMethod
    public void barcodeCancel(Promise promise) {
        try {
            if (barcodeUtility != null) {
                barcodeUtility.stopScan(this.reactContext, BarcodeUtility.ModuleType.BARCODE_2D);
                promise.resolve(true);
            }
            promise.resolve(false);
        } catch (Exception err) {
            promise.reject("BARCODE_CANCEL_ERROR",err.getLocalizedMessage());
        }
    }

    private void sendEvent(String eventName, @Nullable WritableMap map) {
        getReactApplicationContext()
                .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                .emit(eventName, map);
    }

    private void sendEvent(String eventName, String msg) {
        getReactApplicationContext()
                .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                .emit(eventName, msg);
    }

    private void sendEvent(String eventName, Boolean msg) {
        getReactApplicationContext()
                .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                .emit(eventName, msg);
    }

    class BarcodeDataReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String barCode = intent.getStringExtra("data");
            String status = intent.getStringExtra("SCAN_STATE");

            if (status != null && (status.equals("cancel") || status.equals("failure"))) {
                sendEvent("BARCODE", false);
            } else {
                if (barCode.length() == 0) {
                    sendEvent("BARCODE", false);
                } else {
                    sendEvent("BARCODE", barCode);
                }
            }
        }
    }
}
