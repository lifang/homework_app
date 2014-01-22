package com.comdosoft.homework.tools;

import java.lang.ref.SoftReference;  
import java.net.URL;  
import java.util.HashMap;  
import android.graphics.Bitmap;  
import android.graphics.BitmapFactory;  
import android.os.Handler;  
import android.os.Message;  
import android.util.Log;
  
  
public class AsyncImageLoader{  
    Bitmap bitmap = null;  
    HashMap<String, SoftReference<Bitmap>> imageCache;  
    BitmapFactory.Options opts;  
    // 1.根据URL返回Drawable的函数  
    // 2.工具类的核心函数,包含handler+thread  
    
    
    public AsyncImageLoader() {  
        imageCache = new HashMap<String, SoftReference<Bitmap>>();  
        opts = new BitmapFactory.Options();     
        opts.inSampleSize = 0;    //这个的值压缩的倍数（2的整数倍），数值越小，压缩率越小，图片越清晰      
    } 
	public Bitmap asyncLoadImage(final String strUrl, final int i, final LoadFinishCallBack loadFinishCallBack) {  
        Bitmap bitmap = null;  
        //首先判断Map中是否有这种图片的缓存，若有，直接返回该图片的引用  
        if(imageCache.containsKey(strUrl)) {  
            SoftReference<Bitmap> softReference = imageCache.get(strUrl);  
            bitmap = softReference.get();  
            if(bitmap != null) {  
                return bitmap;  
            }  
        }  
          
        final Handler handler = new Handler() {  
            @Override  
            public void handleMessage(Message msg) {  
                // 回调接口  
                loadFinishCallBack.loadFinish(strUrl, i, (Bitmap) msg.obj);  
            }  
        };  
  
        new Thread() {  
            @Override  
            public void run() {  
                Bitmap bitmap = null;  
                try {  
                    bitmap = BitmapFactory.decodeStream(new URL(strUrl.substring(0,strUrl.lastIndexOf("g")+1)).openConnection().getInputStream(), null, opts);   
                } catch (Exception e) {  
                    e.printStackTrace();  
                }  
                imageCache.put(strUrl, new SoftReference<Bitmap>(bitmap));//第一次拿某张图片，把该图片的引用放入缓存中  
                Message msg = new Message();  
                msg.obj = bitmap;  
                handler.sendMessage(msg);  
            }  
        }.start();  
  
        return bitmap;  
    }  
  
    public interface LoadFinishCallBack {  
        public void loadFinish(String strUrl, int i, Bitmap bitmap);  
    }  
}  