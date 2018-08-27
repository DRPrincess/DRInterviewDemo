package com.example.demo.myapplication.utils;

import android.content.Context;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by zhengjipeng on 2017/3/18.
 */

public class AssetsHelper {

    private AssetsHelper() {
    }

    public static final AssetsHelper getInstance() {
        return AssetsHelperFactory.INSTANCE;
    }

    private static class AssetsHelperFactory {
        private static final AssetsHelper INSTANCE = new AssetsHelper();
    }

    public InputStream openAssets(Context context, String fileName) {
        InputStream inputStream = null;
        try {
            inputStream = context.getAssets().open(fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return inputStream;
    }

}
