package cn.zaratustra.axmlparser.core;

import cn.zaratustra.axmlparser.utils.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

/**
 * Created by zaratustra on 2017/12/12.
 */
public class ValuePool {
    private ArrayList<String> mStringPools;
    private ArrayList<Integer> mResourceIds;
    private HashMap<String, String> mUriToNameSpaceMaps;
    private HashMap<String, String> mNamespaceToUriMaps;


    public String[] mStringCache = {"versionCode",
            "versionName",
            "minSdkVersion",
            "targetSdkVersion",
            "name",
            "value",
            "allowBackup",
            "icon",
            "label",
            "roundIcon",
            "supportsRtl",
            "theme",
            "android",
            "http://schemas.android.com/apk/res/android",
            "",
            "package",
            "platformBuildVersionCode",
            "platformBuildVersionName",
            "manifest",
            "cn.zarathustra.checkvirtualapk",
            "1.0",
            "25",
            "7.1.1",
            "uses-sdk",
            "meta-data",
            "android.support.VERSION",
            "25.3.1",
            "application",
            "activity",
            "cn.zarathustra.checkvirtualapk.MainActivity",
            "intent-filter",
            "action",
            "android.intent.action.MAIN",
            "category",
            "android.intent.category.LAUNCHER"};

    public ValuePool() {
        mStringPools = new ArrayList<>();
//        for (String str : mStringCache) {
//            mStringPools.add(str);
//        }

        mResourceIds = new ArrayList<>();
        mUriToNameSpaceMaps = new HashMap<>();
        mNamespaceToUriMaps = new HashMap<>();
    }

    public int getStringIndex(String string) {
        return mStringPools.indexOf(string);
    }

    public void addString(String string) {
        if(!mStringPools.contains(string)) {
            mStringPools.add(string);
        }
    }

    public String getString(int index) {
        if (index < 0 || index >= mStringPools.size()) {
            if (index == -1) {
                return "";
            }
            return "out of string bounds: " + index;
        }
        return mStringPools.get(index);
    }

    public int getStringSize() {
        return mStringPools.size();
    }

    public void addResourceId(int id) {
        mResourceIds.add(id);
    }

    public int getResourceId(int index) {
        return mResourceIds.get(index);
    }

    public int getResourceIdSize() {
        return mResourceIds.size();
    }


    public ArrayList<Integer> getResourceIds() {
        return mResourceIds;
    }

    public void putUriToNamespace(String uri, String nameSpace) {
        mUriToNameSpaceMaps.put(uri, nameSpace);
    }

    public void putNamespaceToUri(String uri, String nameSpace) {
        mNamespaceToUriMaps.put(nameSpace, uri);
    }

    public int getUri(String namespace) {
        return checkAndAddString(mNamespaceToUriMaps.get(namespace));
    }

    public String getNamespace(String uri) {
        return mUriToNameSpaceMaps.get(uri);
    }

    public HashMap<String, String> getUriToNameSpaceMaps() {
        return mUriToNameSpaceMaps;
    }

    public int checkAndAddString(String string) {
        if (string == null) {
            return -1;
        }
        int index = mStringPools.indexOf(string);
        if (index >= 0) {
            return index;
        } else {
            mStringPools.add(string);
            return mStringPools.size() - 1;
        }
    }

}
