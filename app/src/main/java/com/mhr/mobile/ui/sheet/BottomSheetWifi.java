package com.mhr.mobile.ui.sheet;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.mhr.mobile.R;
import com.mhr.mobile.adapter.ListPascaAdapter;
import com.mhr.mobile.databinding.BottomSheetWifiBinding;
import com.mhr.mobile.loader.PricelistLoader;
import com.mhr.mobile.manage.response.ListPascaResponse;
import com.mhr.mobile.model.ImageResponse;
import com.mhr.mobile.util.AssetJsonReader;
import com.mhr.mobile.util.JsonParser;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BottomSheetWifi extends BottomSheetDialogFragment {
  private BottomSheetWifiBinding binding;
  private List<ListPascaResponse.Pasca> mData = new ArrayList<>();
  private ListPascaAdapter adapter;
  private OnProdukItemSelected selected;

  public interface OnProdukItemSelected {
    void onProdukItem(String imgUrl, String provider);
  }

  public void setOnProdukItemSelected(OnProdukItemSelected selected) {
    this.selected = selected;
  }

  @Override
  public View onCreateView(LayoutInflater arg0, ViewGroup arg1, Bundle arg2) {
    binding = BottomSheetWifiBinding.inflate(getLayoutInflater());
    View view = binding.getRoot();
    // Warna Dialog
    view.setBackgroundResource(R.drawable.shape_corners_top);
    loadProvider();
    return view;
  }

  private void loadProvider() {
    // binding.recyclerview.addItemDecoration(new SpacingItemDecoration(3, 25, true));
    binding.recyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));

    adapter = new ListPascaAdapter(mData);
    binding.recyclerview.setAdapter(adapter);
    adapter.setOnItemClickListener(
        (imgUrl, provider) -> {
          if (selected != null) {
            selected.onProdukItem(imgUrl, provider);
          }
          dismiss();
        });

    loadData();
  }

  private void loadData() {
    PricelistLoader.loadPricePasca(
        getActivity(),
        "internet",
        new PricelistLoader.LoaderExecutePasca() {
          @Override
          public void onStartLoading() {
            binding.recyclerview.showShimmerAdapter();
          }

          @Override
          public void onSucces(List<ListPascaResponse.Pasca> produk) {
            binding.recyclerview.hideShimmerAdapter();

            // Membaca JSON file untuk gambar
            String json = AssetJsonReader.loadJSONFromAsset(getActivity(), "image_url.json");
            List<ImageResponse.Image> imageList = JsonParser.parseJson(json);

            // Membuat map gambar untuk dihubungkan dengan produk
            Map<String, ImageResponse.Image> imageMap = new HashMap<>();
            for (int i = 0; i < imageList.size(); i++) {
              ImageResponse.Image image = imageList.get(i);
              imageMap.put(String.valueOf(i + 1), image); // Menggunakan urutan gambar sebagai key
            }

            // Menambahkan gambar ke produk sesuai dengan urutannya
            for (int i = 0; i < produk.size(); i++) {
              ListPascaResponse.Pasca pasca = produk.get(i);
              String imageKey = String.valueOf(i + 1); // Menggunakan urutan posisi
              ImageResponse.Image image = imageMap.get(imageKey);

              // Menyimpan URL gambar pada produk
              if (image != null) {
                pasca.setLogoUrl(image.getImageUrl()); // Pastikan method ini ada di model Pasca
              }
            }

            // Memperbarui data adapter setelah menambahkan gambar
            adapter.perbaruiData(produk);
          }

          @Override
          public void onFailure(String error) {
            // Handle failure here
          }
        });
  }
}
