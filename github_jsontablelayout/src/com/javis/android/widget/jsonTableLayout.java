package com.javis.android.widget;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.javis.android.util.waitTask;
import com.javis.android.widget.jsontablelayout.R;

import android.content.Context;
import android.graphics.Color;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.DigitsKeyListener;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.EditText;;


public class jsonTableLayout extends LinearLayout{
	public static final String JSONType="type";
	public static final String FieldWidth="width";
	public static final String JSONEditText="JSONEditText";
	public static final String JSONSelect="JSONSelect";
	public enum EditTextType {
		Number("Number"),
		Text("Text");
		private String text;
		EditTextType(String text) {
			this.text = text;
		}
		public String getText() {
			return this.text;
		}
		public static EditTextType fromString(String text) {
			if (text != null) {
				for (EditTextType b : EditTextType.values()) {
					if (text.equalsIgnoreCase(b.text)) {
						return b;
					}
				}
			}
			return null;
		}
	}
	public enum Type {
		EditText("editText"),
		Select("select");
		private String text;
		Type(String text) {
			this.text = text;
		}
		public String getText() {
			return this.text;
		}
		public static Type fromString(String text) {
			if (text != null) {
				for (Type b : Type.values()) {
					if (text.equalsIgnoreCase(b.text)) {
						return b;
					}
				}
			}
			return null;
		}
	}
	private deleteOnTouchListener mdeleteOnTouchListener;
	private OnTouchListener mOnTouchListener;
    private JSONArray data;
	private ArrayList<HashMap<String, String>> columnDefs;
	private int headerTextSize=-1;
	private int headerTextColor=-1;
	private int bodyTextSize=-1;
	private int bodyTextColor=-1;
	private ScrollView scroll;
	private int headerBgColor=-1;
	private int bodyItemTouchColor;
	public jsonTableLayout(Context context){
        super(context);
    }
	public jsonTableLayout(Context context, AttributeSet attrs){
        super(context, attrs);
    }
	public void init(ArrayList<HashMap<String,String>> columnDefs){
		this.columnDefs = columnDefs;
		this.setOrientation(LinearLayout.VERTICAL);
		LinearLayout header = new LinearLayout(getContext());
		header.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		header.setId(23456789);
		for(int i=0;i<columnDefs.size();i=i+1){
			HashMap<String,String> h = columnDefs.get(i);
			TextView t = new TextView(getContext());
			t.setLayoutParams(new LayoutParams(0, LayoutParams.WRAP_CONTENT, 1f));
			t.setText(h.get("displayName"));
			t.setTextColor(headerTextColor==-1?Color.parseColor("#FFFFFF"):headerTextColor);
			if(headerTextSize!=-1){
				t.setTextSize(headerTextSize);
			}
			header.addView(t);
		}
		header.setBackgroundColor(headerBgColor==-1?Color.parseColor("#555555"):headerBgColor);
		
		LinearLayout body = new LinearLayout(getContext());
		body.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, 0, 1f));
		
		scroll = new ScrollView(getContext());
		scroll.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		
		TableLayout table = new TableLayout(getContext());
		table.setId(123456789);
		table.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		
		scroll.addView(table);
		body.addView(scroll);
		addView(header);
		addView(body);
	}
	public void addItem(JSONObject tag) throws JSONException {
		final int x = getScrollViewX();
		getData().put(tag);
		clear();
		init(columnDefs);
		run();
		waitTask waittask = new waitTask();
		waittask.setTimeout(new waitTask.OnTimeoutListener() {
			public void onTimeout() {
				int totalHeight = jsonTableLayout.this.scroll.getChildAt(0)
						.getHeight();
				jsonTableLayout.this.setScrollViewXY(x, totalHeight);
			}
		}, 1);
	}
	public void removeItem(int idx) throws JSONException {
		final int x = getScrollViewX();
		final int y = getScrollViewY();
		this.data = removeJSONArray(idx,getData());
		clear();
		init(columnDefs);
		run();
		waitTask waittask = new waitTask();
		waittask.setTimeout(new waitTask.OnTimeoutListener() {
			public void onTimeout() {
				jsonTableLayout.this.setScrollViewXY(x, y);
			}
		}, 1);
	}
	private JSONArray removeJSONArray(int idx, JSONArray from) {
		int len = from.length();
	    ArrayList<JSONObject> result = new ArrayList<JSONObject>(len);
	    for (int i = 0; i < len; i++) {
	        JSONObject obj = from.optJSONObject(i);
	        if (obj != null) {
	            result.add(obj);
	        }
	    }
	    result.remove(idx);
	    JSONArray ja = new JSONArray();
	    for (JSONObject obj : result) {
	        ja.put(obj);
	    }
	    return ja;
	}
	public void clear(){
		this.removeAllViews();
	}
	public void setData(JSONArray jObjArr){
		this.data = jObjArr;
	}
	public JSONArray getData(){
		return data;
	}
	public JSONObject getData(int postion){
		int len = data.length();
	    ArrayList<JSONObject> result = new ArrayList<JSONObject>(len);
	    for (int i = 0; i < len; i++) {
	        JSONObject obj = data.optJSONObject(i);
	        if (obj != null) {
	            result.add(obj);
	        }
	    }
		return result.get(postion);
	}
	public void setScrollViewXY(int x,int y){
		scroll.scrollTo(x,y);
	}
	public int getScrollViewX(){
		return scroll.getScrollX();
	}
	public int getScrollViewY(){
		return scroll.getScrollY();
	}
	public void run() throws JSONException {
		LinearLayout header = (LinearLayout)findViewById(23456789);
		if(mdeleteOnTouchListener != null){
			TextView t = new TextView(getContext());
			t.setLayoutParams(new LayoutParams(0, LayoutParams.WRAP_CONTENT, 1f));
			t.setText(" ");
			t.setTextColor(headerTextColor==-1?Color.parseColor("#FFFFFF"):headerTextColor);
			if(headerTextSize!=-1){
				t.setTextSize(headerTextSize);
			}
			header.addView(t);
		}
		TableLayout table = (TableLayout)findViewById(123456789);
		for(int j=0;j<data.length();j=j+1){
			LinearLayout row = new LinearLayout(getContext());
			row.setClickable(true);
			row.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
			row.setOnTouchListener(this.new rowView());
			row.setTag(j);
			JSONObject rowData = (JSONObject) data.get(j);
			for(int i=0;i<columnDefs.size();i=i+1){
				HashMap<String,String> h = columnDefs.get(i);
				String field = h.get("field");
				View view = null;
				if(Type.fromString(h.get(JSONType)) == Type.EditText){
					if(!h.containsKey(JSONEditText)){
						h.put(JSONEditText, EditTextType.Number.getText());
					}
					view = new EditText(getContext());
					((EditText) view).setSingleLine(true);
					if(EditTextType.fromString(h.get(JSONEditText)) == EditTextType.Number){
						double xx = Double.parseDouble(rowData.getString(field));
						((EditText) view).setText(Integer.toString((int) Math.floor(xx)));
						textWatcher _textWatcher = new textWatcher();
						_textWatcher.setObj(this);
						_textWatcher.setJsonObj(rowData);
						_textWatcher.setField(field);
						_textWatcher.setPosition(j);
						((EditText) view).addTextChangedListener(_textWatcher);
						((EditText) view).setInputType(InputType.TYPE_CLASS_NUMBER);
						((EditText) view).setKeyListener(DigitsKeyListener.getInstance("0123456789"));
					}
					if(EditTextType.fromString(h.get(JSONEditText)) == EditTextType.Text){
						((EditText) view).setText(rowData.getString(field));
						textWatcher _textWatcher = new textWatcher();
						_textWatcher.setObj(this);
						_textWatcher.setJsonObj(rowData);
						_textWatcher.setField(field);
						_textWatcher.setPosition(j);
						((EditText) view).addTextChangedListener(_textWatcher);
					}

					((EditText) view).setTextColor(bodyTextColor==-1?Color.parseColor("#000000"):bodyTextColor);
					if(bodyTextSize!=-1){
						((EditText) view).setTextSize(bodyTextSize);
					}
					view.setLayoutParams(new LayoutParams(0, LayoutParams.WRAP_CONTENT, 1f));
					row.addView(view);
				}else if(Type.fromString(h.get(JSONType)) == Type.Select){
					class runnable implements Runnable{
						private View view;
						public int width;
						protected Spinner sview;
						public int height;
						public runnable(View view, Spinner sview){
							this.view = view;
							this.sview = sview;
						}
						@Override
						public void run() {
				            this.width = view.getWidth();
				            this.height = view.getHeight();
				            run2();
						}
						
						public void run2(){}
					}
					final JSONObject _rowData = rowData;
					final String _field = field;
					final int index = j;
					view = new FrameLayout(getContext());
					view.setLayoutParams(new LayoutParams(0, LayoutParams.MATCH_PARENT, 1f));
					row.addView(view);
					Spinner sview = new Spinner(getContext());
					Spinner spinner = (Spinner) sview;
					String[] items = h.get(JSONSelect).toString().split(",");
					SpinnerAdapter adapter = new SpinnerAdapter(getContext(), android.R.layout.simple_spinner_dropdown_item, items);
					adapter.setBodyTextColor(bodyTextColor==-1?Color.parseColor("#000000"):bodyTextColor);
					if(bodyTextSize!=-1){
						adapter.setBodyTextSize(bodyTextSize);
					}
					spinner.setAdapter(adapter);
					spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
						@Override
						public void onItemSelected(AdapterView<?> parent,
								View view, int position, long id) {
				        	try {
				        		_rowData.put(_field, ((TextView) view).getText());
								data.put(index, _rowData);
								setData(data);
							} catch (JSONException e) {
								e.printStackTrace();
							}
						}

						@Override
						public void onNothingSelected(AdapterView<?> parent) {
						}
					});
					if(rowData.getString(field) != ""){
					    int spinnerPosition = adapter.getPosition(rowData.getString(field));
					    spinner.setSelection(spinnerPosition, true);
					}else{
						spinner.setSelection(0, true);
					}
					if(h.containsKey(FieldWidth)){
						sview.setLayoutParams(new LinearLayout.LayoutParams(Integer.valueOf(h.get(FieldWidth)),
								 LayoutParams.WRAP_CONTENT, 0));
						((ViewGroup)view).addView(sview);
					}else{
						((ViewGroup)view).addView(sview);
						view.post(new runnable(view,sview){
							@Override
							public void run2() {
								if(this.width==0 || this.height==0){
									return;
								}
								FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(this.width, this.height, 0);
								lp.gravity = Gravity.CENTER;
								this.sview.setLayoutParams(lp);
							}
						});
					}
				}else{
					view = new TextView(getContext());
					view.setPadding(5, 5, 5, 5);
					((TextView) view).setText(rowData.getString(field));
					((TextView) view).setTextColor(bodyTextColor==-1?Color.parseColor("#000000"):bodyTextColor);
					if(bodyTextSize!=-1){
						((TextView) view).setTextSize(bodyTextSize);
					}
					view.setLayoutParams(new LayoutParams(0, LayoutParams.WRAP_CONTENT, 1f));
					row.addView(view);
				}
			}
			if(mdeleteOnTouchListener != null){
				ImageButton ib = new ImageButton(getContext());
				ib.setLayoutParams(new LayoutParams(0, LayoutParams.WRAP_CONTENT, 1f));
				ib.setImageResource(android.R.drawable.ic_menu_delete);
				ib.setTag(j);
				ib.setOnTouchListener(this.new view());
				row.addView(ib);
			}
			row.setBackgroundDrawable(getResources().getDrawable(R.drawable.border));
			table.addView(row, new TableLayout.LayoutParams(
                    LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		}
		invalidate();
	}
	public void setDeleteOnTouchListener(deleteOnTouchListener mdeleteOnTouchListener){
		this.mdeleteOnTouchListener = (deleteOnTouchListener) mdeleteOnTouchListener;
	}
	public interface deleteOnTouchListener{
		public void ACTION_UP(View v, MotionEvent event,int postion);
		public boolean OnTouch(View v, MotionEvent event,int postion);
	}
	private class view implements View.OnTouchListener{
		@Override
		public boolean onTouch(final View v, final MotionEvent event) {
			if(mdeleteOnTouchListener != null){
				boolean ontouch = mdeleteOnTouchListener.OnTouch(v, event,(Integer) v.getTag());
				if(!ontouch){
					if(event.getAction()==MotionEvent.ACTION_DOWN){
					}else if(event.getAction()==MotionEvent.ACTION_UP){
						mdeleteOnTouchListener.ACTION_UP(v,event,(Integer) v.getTag());
					}else if(event.getAction()==MotionEvent.ACTION_CANCEL){
					}
				}
			}
			return false;
		}	
	}
	public void setOnTouchListener(OnTouchListener mOnTouchListener){
		this.mOnTouchListener = (OnTouchListener) mOnTouchListener;
	}
	public interface OnTouchListener{
		public void ACTION_UP(View v, MotionEvent event,int postion);
		public boolean OnTouch(View v, MotionEvent event,int postion);
	}
    private class rowView implements View.OnTouchListener{
		@Override
		public boolean onTouch(final View v, final MotionEvent event) {
			if(mOnTouchListener != null){
				boolean ontouch = mOnTouchListener.OnTouch(v, event,(Integer) v.getTag());
				if(!ontouch){
					if(event.getAction()==MotionEvent.ACTION_DOWN){
						v.setBackgroundColor(bodyItemTouchColor==-1?Color.GRAY:bodyItemTouchColor);
					}else if(event.getAction()==MotionEvent.ACTION_UP){
						v.setBackgroundColor(0xfffffff);
						v.setBackgroundDrawable(getResources().getDrawable(R.drawable.border));
						mOnTouchListener.ACTION_UP(v,event,(Integer) v.getTag());
					}else if(event.getAction()==MotionEvent.ACTION_CANCEL){
						v.setBackgroundColor(0xfffffff);
						v.setBackgroundDrawable(getResources().getDrawable(R.drawable.border));
					}
				}
			}
			return false;
		}	
	}
    private class textWatcher implements TextWatcher{
		private jsonTableLayout jsonTableLayout;
		private JSONArray data;
		private int position;
		private JSONObject rowData;
		private String field;
		public void afterTextChanged(Editable s) {
        	try {
        		rowData.put(field, s.toString());
				data.put(position, rowData);
				jsonTableLayout.setData(data);
			} catch (JSONException e) {
				e.printStackTrace();
			}
        }
        public void setField(String field) {
			this.field = field;
		}
		public void setJsonObj(JSONObject rowData) {
			this.rowData = rowData;
		}
		public void setPosition(int j) {
			this.position = j;
		}
		public void setObj(jsonTableLayout jsonTableLayout) {
        	this.jsonTableLayout = jsonTableLayout;
        	this.data = jsonTableLayout.getData();
		}
		public void beforeTextChanged(CharSequence s, int start, int count, int after){}
        public void onTextChanged(CharSequence s, int start, int before, int count){}
    }; 
    public void setHeaderTextSize(int size){
    	this.headerTextSize=size;
    }
    public void setHeaderTextColor(String color){
    	this.headerTextColor=Color.parseColor(color);
    }
    public void setHeaderBgColor(String color){
    	this.headerBgColor=Color.parseColor(color);
    }
    public void setBodyTextSize(int size){
    	this.bodyTextSize=size;
    }
    public void setBodyTextColor(String color){
    	this.bodyTextColor=Color.parseColor(color);
    }
    public void setBodyItemTouchDownColor(String color){
    	this.bodyItemTouchColor=Color.parseColor(color);
    }
    
	private class SpinnerAdapter extends ArrayAdapter<String> {
		Context context;
		String[] items = new String[] {};
		private int bodyTextSize=30;
		private int bodyTextColor=Color.BLUE;

		public SpinnerAdapter(final Context context,
				final int textViewResourceId, final String[] objects) {
			super(context, textViewResourceId, objects);
			this.items = objects;
			this.context = context;
		}

		public void setBodyTextSize(int bodyTextSize) {
			this.bodyTextSize = bodyTextSize;
		}

		public void setBodyTextColor(int bodyTextColor) {
			this.bodyTextColor = bodyTextColor;
		}

		@Override
		public View getDropDownView(int position, View convertView,
				ViewGroup parent) {

			if (convertView == null) {
				LayoutInflater inflater = LayoutInflater.from(context);
				convertView = inflater.inflate(
						android.R.layout.simple_spinner_item, parent, false);
			}
			convertView.setBackgroundColor(Color.parseColor("#FFFFFF"));
			TextView tv = (TextView) convertView
					.findViewById(android.R.id.text1);
			tv.setText(items[position]);
			tv.setTextColor(bodyTextColor);
			tv.setTextSize(bodyTextSize);
			return convertView;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				LayoutInflater inflater = LayoutInflater.from(context);
				convertView = inflater.inflate(
						android.R.layout.simple_spinner_item, parent, false);
			}
			TextView tv = (TextView) convertView
					.findViewById(android.R.id.text1);
			tv.setText(items[position]);
			tv.setTextColor(bodyTextColor);
			tv.setTextSize(bodyTextSize);
			return convertView;
		}
	}
}
