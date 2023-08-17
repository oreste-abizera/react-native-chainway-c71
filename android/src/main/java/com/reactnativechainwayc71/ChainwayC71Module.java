package com.reactnativechainwayc71;

import androidx.annotation.NonNull;

import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.module.annotations.ReactModule;
import com.facebook.react.bridge.LifecycleEventListener;
import com.facebook.react.modules.core.DeviceEventManagerModule;

import com.rscja.barcode.BarcodeDecoder;
import com.rscja.barcode.BarcodeFactory;
import com.rscja.deviceapi.entity.BarcodeEntity;

@ReactModule(name = ChainwayC71Module.NAME)
public class ChainwayC71Module extends ReactContextBaseJavaModule implements LifecycleEventListener {
    public static final String NAME = "ChainwayC71";
    private final ReactApplicationContext reactContext;
    private static BarcodeDecoder barcodeDecoder = BarcodeFactory.getInstance().getBarcodeDecoder();

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
            barcodeDecoder.open(this.reactContext);

            barcodeDecoder.setDecodeCallback(new BarcodeDecoder.DecodeCallback() {
              @Override
              public void onDecodeComplete(BarcodeEntity barcodeEntity) {
                if(barcodeEntity.getResultCode() == BarcodeDecoder.DECODE_SUCCESS) {
                  sendEvent("BARCODE", barcodeEntity.getBarcodeData());
                } else {
                  sendEvent("BARCODE", "fail");
                }
              }
            });
    }

    private void disconnect() {
        barcodeDecoder.stopScan();
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
    public void isReaderInit(Promise promise){
      try {
        promise.resolve(barcodeDecoder.isOpen());
      } catch (Exception err){
        promise.reject("CHECK_INIT_READER_ERROR",err.getLocalizedMessage());
      }
    }

    @ReactMethod
    public void barcodeRead(Promise promise) {
      barcodeDecoder.startScan();
    }

    @ReactMethod
    public void barcodeCancel(Promise promise) {
        barcodeDecoder.stopScan();
    }

    private void sendEvent(String eventName, String msg) {
        getReactApplicationContext()
                .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                .emit(eventName, msg);
    }
}
