package com.nodes.plugin;

import android.util.Log;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.PluginResult;
import org.apache.cordova.CallbackContext;

import org.apache.cordova.*;
import org.json.JSONArray;
import org.json.JSONException;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.honeywell.sdk.android.diy.scanlib.IScan;
import com.honeywell.sdk.android.diy.scanlib.ScanLib;


import java.io.*;

public class Autoid9Plugin extends CordovaPlugin {

	private IntentFilter filter;
  private ScanLib scanLib;// 扫码库
	// A very important class used to communicate with driver and service.
	private DataReceiver mDataReceiver;
	private CallbackContext receiveScanCallback;

	public Autoid9Plugin() {
	}

	@Override
	public boolean execute(String action, JSONArray data, CallbackContext callbackContext) throws JSONException {

		Log.v("Autoid9Plugin", "============== execute ===========: " + action);

		if (action.equals("echo")) {

			String message = data.getString(0);

			this.echo(message, callbackContext);

			return true;

		}

		if (action.equals("initialise")) {
			Log.v("Autoid9Plugin", "==== initialise ===");
			this.initialise(callbackContext);
			return true;
		}

		if (action.equals("setReceiveScanCallback")) {
			Log.v("Autoid9Plugin", " ==== setReceiveScanCallback ===");
			receiveScanCallback = callbackContext;

			if (callbackContext == null) {
				Log.v("Autoid9Plugin", "callbackContext is null.");
			} else {
				Log.v("Autoid9Plugin", "callbackContext is not null");
			}

			return true;
		}
		if (action.equals("destroy")) {

			// Call this from window.onbeforeunload
			Log.v("Autoid9Plugin", "destroy(): cleaning up.");

			cordova.getActivity().unregisterReceiver(mDataReceiver);

			mDataReceiver = null;

			return true;
		}

		Log.v("Autoid9Plugin", "============== done   ===========: " + action);

		return false;
	}

	public void receieveScan(String data) {
		PluginResult progressResult = new PluginResult(PluginResult.Status.OK, data);
		progressResult.setKeepCallback(true);

		if (receiveScanCallback == null) {
			Log.v("Autoid9Plugin", "receiveScanCallback is null.");
		} else {
			receiveScanCallback.sendPluginResult(progressResult);
		}
	}

	private void echo(String message, CallbackContext callbackContext) {

		if (message != null && message.length() > 0) {

			callbackContext.success(message);

		} else {

			callbackContext.error("Expected one non-empty string argument.");

		}

	}

	public void initialise(CallbackContext callbackContext) {
		try {
			if (cordova.getActivity() == null) {
				Log.v("Autoid9Plugin", "cordova.getActivity() is null");
			} else {
				Log.v("Autoid9Plugin", "cordova.getActivity() is something");
			}

//			filter = new IntentFilter();
//			filter.addAction("com.honeywell.scan");
//
//			// // 发送广播到扫描工具内的应用设置项
//			// Intent intent = new Intent("com.android.scanner.service_settings");
//			// // 修改扫描工具内应用设置中的开发者项下的广播名称
//			// intent.putExtra("action_barcode_broadcast", "android.intent.action.SCANRESULT");
//			// // 修改扫描工具内应用设置下的条码发送方式为 "广播"
//			// intent.putExtra("barcode_send_mode", "BROADCAST");
//			// // 修改扫描工具内应用设置下的结束符为 "NONE"
//			// intent.putExtra("endchar", "NONE");
//			Intent intent = new Intent();
//            intent.setAction("com.honeywell.scan");
//			cordova.getActivity().sendBroadcast(intent);
//
//			mDataReceiver = new DataReceiver(this);
//			cordova.getActivity().registerReceiver(mDataReceiver, filter);

			Log.v("Autoid9Plugin", "Got reader");


      // 初始化扫码库
      ScanLib.init(cordova.getActivity().getApplicationContext());
      scanLib = ScanLib.getIntance();
      scanLib.Encoding_UTF_8 = true;
      //scanLib.setSuffix("\n");
      scanLib.setIScan(iScan);

      //注册广播
      IntentFilter intentFilter = new IntentFilter(scanLib.getAction());
      cordova.getActivity().registerReceiver(broadcastReceiver, intentFilter);

		} catch (Exception e) {
			StringWriter writer = new StringWriter();
			PrintWriter printWriter = new PrintWriter(writer);
			e.printStackTrace(printWriter);
			printWriter.flush();

			String stackTrace = writer.toString();

			Log.v("Autoid9Plugin", "Error starting reader manager: " + stackTrace);
		}

		Log.v("Autoid9Plugin", "Autoid9Plugin.create() Done");

		callbackContext.success();
	}

  /**
   * 扫描接口回调
   */
  IScan iScan = new IScan() {
    @Override
    public void onErrMsg(String errMsg, String sdkMsg) {

    }

    @Override
    public void onInitSuccess() {

    }


    //获得扫码数据
    @Override
    public void onDecode(final String barcode) {
      String s =
        barcode;
    }
  };

  /**
   * 广播接收器
   */
  private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
    @Override
    public void onReceive(Context context, final Intent intent) {
      if (intent.getAction() == scanLib.getAction()) {
        receieveScan(intent.getStringExtra("data"));
      }
    }
  };
}
