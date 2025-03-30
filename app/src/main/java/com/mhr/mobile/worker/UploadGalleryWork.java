package com.mhr.mobile.worker;

import android.content.Context;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;
import com.mhr.mobile.manage.call.Endpoint;
import java.io.File;
import java.net.URLConnection;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UploadGalleryWork extends Worker {
  private static final String TAG = "UploadWorker";
  private static Retrofit retrofitInstance;

  public UploadGalleryWork(@NonNull Context context, @NonNull WorkerParameters workerParams) {
    super(context, workerParams);
  }

  @NonNull
  @Override
  public Result doWork() {
    String imagePath = getInputData().getString("imagePath");
    if (imagePath == null) {
      Log.e(TAG, "File path is null");
      return Result.failure();
    }

    File file = new File(imagePath);
    if (!file.exists() || file.length() == 0) {
      Log.e(TAG, "File tidak ditemukan atau kosong: " + imagePath);
      return Result.failure();
    }

    // Deteksi MIME Type file secara otomatis
    String mimeType = URLConnection.guessContentTypeFromName(file.getName());
    if (mimeType == null) {
      mimeType = "image/*"; // Default jika tidak dapat dideteksi
    }

    RequestBody requestBody = RequestBody.create(MediaType.parse(mimeType), file);
    MultipartBody.Part body =
        MultipartBody.Part.createFormData("image", file.getName(), requestBody);

    // Gunakan singleton Retrofit agar tidak membuat instance berulang kali
    if (retrofitInstance == null) {
      retrofitInstance =
          new Retrofit.Builder()
              .baseUrl("https://api.qiospro.my.id/")
              .addConverterFactory(GsonConverterFactory.create())
              .build();
    }

    Endpoint apiService = retrofitInstance.create(Endpoint.class);
    Call<ResponseBody> call = apiService.uploadImage(body);

    try {
      retrofit2.Response<ResponseBody> response = call.execute();
      if (response.isSuccessful()) {
        Log.d(TAG, "Sukses mengunggah: " + file.getName());
        return Result.success();
      } else {
        Log.e(TAG, "Gagal mengunggah: " + response.message());
        return Result.retry(); // Coba ulang jika gagal
      }
    } catch (Exception e) {
      Log.e(TAG, "Gagal mengunggah: " + e.getMessage(), e);
      return Result.retry(); // Coba ulang jika ada kesalahan jaringan
    }
  }
}
