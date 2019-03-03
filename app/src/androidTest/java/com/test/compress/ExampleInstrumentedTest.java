package com.test.compress;

import android.content.Context;
import android.os.Environment;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import com.github.ImageCompress.CompressConfig;
import com.github.ImageCompress.CompressManager;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("com.test.compress", appContext.getPackageName());
    }
    @Test
    public void sf() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("com.test.compress", appContext.getPackageName());

        String absoluteFile = Environment.getExternalStorageDirectory().getAbsoluteFile()+"/2019/";
        /*ui线程压缩*/
        CompressConfig compressConfig=new CompressConfig(appContext);
        CompressManager manager=new CompressManager(compressConfig);
        String compressPath = manager.compressPX(absoluteFile+"1.jpg");//像素压缩
        String compressPath1 = manager.compressQ(absoluteFile+"2.jpg");//质量压缩
        String compressPath2 = manager.compressPXAndQ(absoluteFile+"3.jpg");//像素+质量压缩
        Log.e("===","===1"+compressPath);
        Log.e("===","===2"+compressPath1);
        Log.e("===","===3"+compressPath2);
    }
}
