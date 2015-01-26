package com.example.github_android_test;

import java.util.ArrayList;
import java.util.HashMap;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.javis.android.widget.jsonTableLayout;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class main extends Activity {
	private jsonTableLayout jTb;
	private ArrayList<HashMap<String, String>> columnDefs;

	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.main);
		columnDefs = new ArrayList<HashMap<String, String>>();
		columnDefs.add(new HashMap<String, String>() {
			private static final long serialVersionUID = 1L;
			{
				put("field", "id");
				put("displayName", "編號");
			}
		});
		columnDefs.add(new HashMap<String, String>() {
			private static final long serialVersionUID = 1L;
			{
				put("field", "name");
				put("displayName", "姓名");
			}
		});
		columnDefs.add(new HashMap<String, String>() {
			private static final long serialVersionUID = 1L;
			{
				put("field", "age");
				put("displayName", "年齡");
				put("type", "editText");
			}
		});
		jTb = (jsonTableLayout) findViewById(R.id.jsonTablelayout);
		jTb.setHeaderBgColor("#46CC1C");
		jTb.setHeaderTextSize(22);
		jTb.setBodyTextSize(18);
		jTb.setBodyItemTouchDownColor("#ffff00");
		jTb.init(columnDefs);
		try {
			JSONArray arr = new JSONArray(
					"[{'id':1,'name':'測試者1','age':25},{'id':2,'name':'測試者2','age':11}"
							+ ",{'id':3,'name':'測試者3','age':25},{'id':4,'name':'測試者4','age':12}"
							+ ",{'id':5,'name':'測試者5','age':15},{'id':6,'name':'測試者6','age':25}"
							+ ",{'id':7,'name':'測試者7','age':25},{'id':8,'name':'測試者8','age':45}"
							+ ",{'id':9,'name':'測試者9','age':21},{'id':10,'name':'測試者10','age':35}"
							+ ",{'id':11,'name':'測試者11','age':25},{'id':12,'name':'測試者12','age':25}"
							+ ",{'id':13,'name':'測試者13','age':25},{'id':14,'name':'測試者14','age':13}"
							+ ",{'id':15,'name':'測試者15','age':17},{'id':16,'name':'測試者16','age':25}]");
			jTb.setData(arr);
			jTb.setOnTouchListener(new jsonTableLayout.OnTouchListener() {
				@Override
				public void ACTION_UP(View v, MotionEvent event, int postion) {
					Toast.makeText(main.this, postion+"", Toast.LENGTH_SHORT).show();
				}

				@Override
				public boolean OnTouch(View v, MotionEvent event, int postion) {
					return false;
				}
			});
			jTb.setDeleteOnTouchListener(new jsonTableLayout.deleteOnTouchListener() {
				@Override
				public void ACTION_UP(View v, MotionEvent event, int postion) {
					try {
						jTb.removeItem(postion);
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}

				@Override
				public boolean OnTouch(View v, MotionEvent event, int postion) {
					return false;
				}
			});
			jTb.run();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		Button button1 = (Button) findViewById(R.id.button1);
		button1.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				JSONObject obj;
				try {
					int total = jTb.getData().length()+1;
					obj = new JSONObject("{'id':"+total+",'name':'測試者"+total+"','age':25}");
					jTb.addItem(obj);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});
	}
}
