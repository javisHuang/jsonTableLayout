package com.javis.android.util;

import java.util.Timer;
import java.util.TimerTask;

import android.os.Handler;
import android.os.Message;

/**
 * 使用在等待時間，只要設置setOnTimeoutListener，並且在onTimeout需執行的命令便可.
 */
public class waitTask{
	private int sec=1000;
	public waitTask(){}
	private OnTimeoutListener mOnTimeoutListener;
	public interface OnTimeoutListener{
        void onTimeout();
    }
	public void setOnTimeoutListener(OnTimeoutListener l){
		mOnTimeoutListener = l;
    }

	public class waitRun extends TimerTask{ 
		public Timer time;
		@Override
		public void run() {
			// TODO Auto-generated method stub
			time.cancel();
			sendMsg(0);
	}};

    public void setTimeout(OnTimeoutListener onTimeoutListener, int sec){
    	setOnTimeoutListener(onTimeoutListener);
    	waitRun waitrun = new waitRun();
    	Timer timer = new Timer();
    	waitrun.time=timer;
		timer.schedule(waitrun, sec);
    }
    public void setTimeout(OnTimeoutListener onTimeoutListener){
    	setOnTimeoutListener(onTimeoutListener);
    	waitRun waitrun = new waitRun();
    	Timer timer = new Timer();
    	waitrun.time=timer;
		timer.schedule(waitrun, sec);
    }
    
    private Handler handler = new Handler(){
	    @Override
	    public void handleMessage(Message msg){	//定義一個Handler，用於處理下載線程與UI間通訊
	        if (!Thread.currentThread().isInterrupted()){
	        	mOnTimeoutListener.onTimeout();
	        }
	    super.handleMessage(msg);
	    }
	};
	private void sendMsg(int flag){
	    Message msg = new Message();
	    msg.what = flag;
	    handler.sendMessage(msg);
	}
}
