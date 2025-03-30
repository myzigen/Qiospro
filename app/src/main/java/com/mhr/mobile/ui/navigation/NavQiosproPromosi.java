package com.mhr.mobile.ui.navigation;

import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.work.*;
import com.mhr.mobile.adapter.GalleryAdapter;
import com.mhr.mobile.databinding.NavQiosproPromosiBinding;
import com.mhr.mobile.model.Gallery;
import com.mhr.mobile.ui.inject.InjectionFragment;
import com.mhr.mobile.worker.UploadGalleryWork;
import java.io.*;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NavQiosproPromosi extends InjectionFragment {
    NavQiosproPromosiBinding binding;
    private final List<Gallery> imageList = new ArrayList<>();
    private GalleryAdapter imageAdapter;
    private final ExecutorService executorService = Executors.newFixedThreadPool(3);
    private final MutableLiveData<List<Gallery>> liveGallery = new MutableLiveData<>();
    private Handler mainHandler = new Handler(Looper.getMainLooper());

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateQiosFragment(LayoutInflater inflater, ViewGroup viewGroup, Bundle bundle) {
        binding = NavQiosproPromosiBinding.inflate(inflater, viewGroup, false);
        setupRecyclerView();
        observeGalleryData();
        loadGalleryImages();
        return binding.getRoot();
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100
                && grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            loadGalleryImages(); // Muat ulang gambar setelah izin diberikan
        }
    }

    private void setupRecyclerView() {
        binding.recyclerview.setLayoutManager(new GridLayoutManager(requireContext(), 3));
        imageAdapter = new GalleryAdapter(requireContext(), imageList);
        binding.recyclerview.setAdapter(imageAdapter);
    }

    private void observeGalleryData() {
        liveGallery.observe(
                getViewLifecycleOwner(),
                new Observer<List<Gallery>>() {
                    @Override
                    public void onChanged(List<Gallery> galleries) {
                        imageList.clear();
                        imageList.addAll(galleries);
                        imageAdapter.notifyDataSetChanged();
                    }
                });
    }

    private void loadGalleryImages() {
        executorService.execute(() -> {
            List<Gallery> tempList = new ArrayList<>();
            Uri collection = (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
                    ? MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL)
                    : MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

            String[] projection = new String[]{MediaStore.Images.Media._ID};
            Cursor cursor = requireActivity().getContentResolver().query(
                    collection, projection, null, null,
                    MediaStore.Images.Media.DATE_ADDED + " DESC"
            );

            if (cursor != null) {
                int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID);

                while (cursor.moveToNext()) {
                    long imageId = cursor.getLong(columnIndex);
                    Uri imageUri = Uri.withAppendedPath(
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI, String.valueOf(imageId));

                    tempList.add(new Gallery(imageUri));
                }
                cursor.close();

                // Perbarui LiveData agar RecyclerView diperbarui setelah semua data selesai dimuat
                mainHandler.post(() -> liveGallery.setValue(tempList));
            }
        });
    }

    private String getRealPathFromURI(Uri uri) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            return copyFileToCache(uri);
        } else {
            String[] projection = {MediaStore.Images.Media.DATA};
            Cursor cursor =
                    requireContext().getContentResolver().query(uri, projection, null, null, null);
            if (cursor != null) {
                int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                cursor.moveToFirst();
                String path = cursor.getString(columnIndex);
                cursor.close();
                return path;
            }
            return null;
        }
    }

    private String copyFileToCache(Uri uri) {
        try {
            String mimeType = requireContext().getContentResolver().getType(uri);
            String extension = MimeTypeMap.getSingleton().getExtensionFromMimeType(mimeType);

            if (extension == null) {
                extension = "jpg";
            }

            String fileName =
                    System.currentTimeMillis() + "_" + UUID.randomUUID().toString() + "." + extension;
            File file = new File(requireContext().getCacheDir(), fileName);

            InputStream inputStream = requireContext().getContentResolver().openInputStream(uri);
            OutputStream outputStream = new FileOutputStream(file);

            byte[] buffer = new byte[4096];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }

            outputStream.close();
            inputStream.close();
            return file.getAbsolutePath();
        } catch (IOException e) {
            Log.e("Upload", "Gagal menyalin file ke cache", e);
            return null;
        }
    }
}