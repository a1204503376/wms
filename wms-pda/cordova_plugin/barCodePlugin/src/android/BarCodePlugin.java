package com.nodes.plugin;

import org.apache.cordova.*;
import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.honeywell.aidc.*;
import com.honeywell.aidc.AidcManager.CreatedCallback;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

/**
 * This class echoes a string called from JavaScript.
 */
public class BarCodePlugin extends CordovaPlugin implements BarcodeReader.BarcodeListener,
        BarcodeReader.TriggerListener{

    private BarcodeReader barcodeReader;
    private CallbackContext receiveScanCallback;
    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        if (action.equals("setReceiveScanCallback")) {
          receiveScanCallback = callbackContext;
          return true;
        }
        return false;
    }
    @Override
    public void initialize(CordovaInterface cordova, CordovaWebView webView) {
        super.initialize(cordova, webView);
         AidcManager.create(cordova.getActivity(), new CreatedCallback() {

            @Override
            public void onCreated(AidcManager aidcManager) {
                try{
                    barcodeReader = aidcManager.createBarcodeReader();
                  if (barcodeReader != null) {

                    // register bar code event listener
                    barcodeReader.addBarcodeListener(BarCodePlugin.this);

                    // set the trigger mode to client control
                    try {
                      barcodeReader.setProperty(BarcodeReader.PROPERTY_TRIGGER_CONTROL_MODE,
                        BarcodeReader.TRIGGER_CONTROL_MODE_CLIENT_CONTROL);
                    } catch (UnsupportedPropertyException e) {
//                      Toast.makeText(cordova.getActivity(), "Failed to apply properties", Toast.LENGTH_SHORT).show();
                    }
                    // register trigger state change listener
                    barcodeReader.addTriggerListener(BarCodePlugin.this);

                    Map<String, Object> properties = new HashMap<String, Object>();
                    // Set Symbologies On/Off
                    properties.put(BarcodeReader.PROPERTY_CODE_128_ENABLED, true);
                    properties.put(BarcodeReader.PROPERTY_GS1_128_ENABLED, true);
                    properties.put(BarcodeReader.PROPERTY_QR_CODE_ENABLED, true);
                    properties.put(BarcodeReader.PROPERTY_CODE_39_ENABLED, true);
                    properties.put(BarcodeReader.PROPERTY_DATAMATRIX_ENABLED, true);
                    properties.put(BarcodeReader.PROPERTY_UPC_A_ENABLE, true);
                    properties.put(BarcodeReader.PROPERTY_EAN_13_ENABLED, false);
                    properties.put(BarcodeReader.PROPERTY_DATA_PROCESSOR_CHARSET,"utf-8");
                    properties.put(BarcodeReader.PROPERTY_AZTEC_ENABLED, false);
                    properties.put(BarcodeReader.PROPERTY_CODABAR_ENABLED, false);
                    properties.put(BarcodeReader.PROPERTY_INTERLEAVED_25_ENABLED, false);
                    properties.put(BarcodeReader.PROPERTY_PDF_417_ENABLED, false);
                    // Set Max Code 39 barcode length
                    properties.put(BarcodeReader.PROPERTY_CODE_39_MAXIMUM_LENGTH, 10);
                    // Turn on center decoding
                    properties.put(BarcodeReader.PROPERTY_CENTER_DECODE, true);
                    // Disable bad read response, handle in onFailureEvent
                    properties.put(BarcodeReader.PROPERTY_NOTIFICATION_BAD_READ_ENABLED, false);
                    // Apply the settings
                    barcodeReader.setProperties(properties);
                    barcodeReader.claim();
                  }
                }
                catch (InvalidScannerNameException e){
//                    Toast.makeText(cordova.getActivity(), "Invalid Scanner Name Exception: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                catch (Exception e){
//                    Toast.makeText(cordova.getActivity(), "Exception: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    @Override
    public void onBarcodeEvent(final BarcodeReadEvent event) {
        cordova.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                  PluginResult progressResult = new PluginResult(PluginResult.Status.OK, event.getBarcodeData());
                  progressResult.setKeepCallback(true);
                  receiveScanCallback.sendPluginResult(progressResult);
            }
        });
    }

    @Override
    public void onTriggerEvent(TriggerStateChangeEvent event) {
        try {
            // only handle trigger presses
            // turn on/off aimer, illumination and decoding
            barcodeReader.aim(event.getState());
            barcodeReader.light(event.getState());
            barcodeReader.decode(event.getState());

        } catch (ScannerNotClaimedException e) {
            e.printStackTrace();
//            Toast.makeText(cordova.getActivity(), "Scanner is not claimed", Toast.LENGTH_SHORT).show();
        } catch (ScannerUnavailableException e) {
            e.printStackTrace();
//            Toast.makeText(cordova.getActivity(), "Scanner unavailable", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onFailureEvent(BarcodeFailureEvent arg0) {
        cordova.getActivity().runOnUiThread(new Runnable() {

            @Override
            public void run() {
//                Toast.makeText(cordova.getActivity(), "没有扫描到数据，请多尝试几次！", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onResume(boolean multitasking) {
        super.onResume(multitasking);

        if (barcodeReader != null) {
            try {
                barcodeReader.claim();
            } catch (ScannerUnavailableException e) {
                e.printStackTrace();
//                Toast.makeText(cordova.getActivity(), "Scanner unavailable", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onPause(boolean multitasking) {
        super.onPause(multitasking);
        if (barcodeReader != null) {
            // release the scanner claim so we don't get any scanner
            // notifications while paused.
            barcodeReader.release();
        }
    }

    @Override
    public void onDestroy() {
        if (barcodeReader != null) {
            // unregister barcode event listener
            barcodeReader.removeBarcodeListener(this);

            // unregister trigger state change listener
            barcodeReader.removeTriggerListener(this);
        }
    }
  
    
}
