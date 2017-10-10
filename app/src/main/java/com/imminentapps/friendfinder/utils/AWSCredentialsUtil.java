package com.imminentapps.friendfinder.utils;

import android.content.Context;
import android.content.res.AssetManager;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Class to load AWS credentials from property file
 * Created by mburke on 10/7/17.
 */
public class AWSCredentialsUtil {
    public static String getCreds(String key, Context context) throws IOException {
        Properties properties = new Properties();
        AssetManager assetManager = context.getAssets();
        InputStream inputStream = assetManager.open("credentials.properties");
        properties.load(inputStream);
        return properties.getProperty(key);
    }
}
