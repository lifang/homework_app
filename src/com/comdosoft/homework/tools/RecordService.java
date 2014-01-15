package com.comdosoft.homework.tools;

import java.io.File;

import android.app.Service;
import android.content.Intent;
import android.media.MediaRecorder;
import android.media.MediaRecorder.OnErrorListener;
import android.os.IBinder;
import android.util.Log;

public class RecordService extends Service implements Urlinterface {
	private MediaRecorder mediaRecorder;
	private static String SDFile = "/sdcard/homework/";

	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	public void onCreate() {
		super.onCreate();
		Log.i(tag, "开启service");
		mediaRecorder = new MediaRecorder();
		start();
	}

	protected void start() {
		try {
			Log.i(tag, "初始化录音");
			File file = new File(SDFile + 1 + ".amr");
			// 设置音频录入源
			mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
			// 设置录制音频的输出格式
			mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
			// 设置音频的编码格式
			mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
			// 设置录制音频文件输出文件路径
			mediaRecorder.setOutputFile(file.getAbsolutePath());

			mediaRecorder.setOnErrorListener(new OnErrorListener() {

				public void onError(MediaRecorder mr, int what, int extra) {
					// 发生错误，停止录制
					stop();
					Log.i("homework", "录音发生错误");
				}
			});

			// 准备、开始
			mediaRecorder.prepare();
			mediaRecorder.start();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 录音结束
	 */
	protected void stop() {
		if (mediaRecorder != null) {
			// 如果正在录音，停止并释放资源
			mediaRecorder.stop();
			mediaRecorder.release();
			mediaRecorder = null;
		}
	}

	@Override
	public void onDestroy() {
		if (mediaRecorder != null) {
			Log.i(tag, "关闭录音");
			mediaRecorder.stop();
			mediaRecorder.release();
			mediaRecorder = null;
			super.onDestroy();
		}
	}

}
