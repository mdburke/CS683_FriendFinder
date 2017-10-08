package com.imminentapps.friendfinder.utils;

import android.content.Context;
import android.content.res.AssetManager;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by mburke on 10/7/17.
 */

public class PropertiesUtil {
    public static String getProperty(String key,Context context) throws IOException {
        Properties properties = new Properties();
        AssetManager assetManager = context.getAssets();
        InputStream inputStream = assetManager.open("credentials.properties");
        properties.load(inputStream);
        return properties.getProperty(key);

    }
}
