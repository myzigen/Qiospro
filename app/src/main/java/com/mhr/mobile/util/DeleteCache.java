package com.mhr.mobile.util;

import android.content.Context;
import java.io.File;

public class DeleteCache {

  public static void deleteCache(Context context) {
    try {
      File dir = context.getCacheDir();
      if (dir != null && dir.isDirectory()) {
        deleteDir(dir);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private static boolean deleteDir(File dir) {
    if (dir != null && dir.isDirectory()) {
      String[] children = dir.list();
      for (String child : children) {
        boolean success = deleteDir(new File(dir, child));
        if (!success) {
          return false;
        }
      }
      return dir.delete();
    } else if (dir != null && dir.isFile()) {
      return dir.delete();
    }
    return false;
  }
}
