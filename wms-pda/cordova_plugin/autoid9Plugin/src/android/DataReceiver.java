package com.nodes.plugin;


import android.util.Log;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.*;

/// Create a broadcast object to receive the intent coming from service.
public class DataReceiver extends BroadcastReceiver {

	private Autoid9Plugin plugin;

	public DataReceiver(Autoid9Plugin _plugin)
	{
		this.plugin = _plugin;
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		if (intent.getAction().equals("com.honeywell.scan")) {
				
		String data = intent.getStringExtra("data");
				
			Log.v("Autoid9Plugin", "got data, 1: " + data);
			this.plugin.receieveScan(data);
		}

	}
};