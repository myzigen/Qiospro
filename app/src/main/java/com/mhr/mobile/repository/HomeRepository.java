package com.mhr.mobile.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.mhr.mobile.R;
import com.mhr.mobile.manage.call.BukaolshopEndpoint;
import com.mhr.mobile.manage.client.BukaolshopClient;
import com.mhr.mobile.manage.response.SliderHomeResponse;
import com.mhr.mobile.model.ItemSliderHome;
import com.mhr.mobile.model.MenuKategoriModel;
import com.mhr.mobile.util.Config;
import com.mhr.mobile.viewmodel.HomeViewModel;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeRepository {
  private BukaolshopEndpoint endpoint;

  public HomeRepository() {
    //endpoint = BukaolshopClient.getBukaolshopApi().create(BukaolshopEndpoint.class);
  }
  // Ambil data gambar slider
 

  // Ambil kategori produk
  public LiveData<List<MenuKategoriModel>> getKategoriData(HomeViewModel.RequestData callback) {
    // Simulasi data kategori atau ambil dari API
    MutableLiveData<List<MenuKategoriModel>> liveData = new MutableLiveData<>();
    callback.onStartLoading();
    List<MenuKategoriModel> itemKategori = new ArrayList<>();
    itemKategori.add(new MenuKategoriModel(0, R.drawable.itm_pulsa, "Pulsa"));
    itemKategori.add(new MenuKategoriModel(1, R.drawable.itm_data, "Paket Data"));
    itemKategori.add(new MenuKategoriModel(2, R.drawable.itm_pln, "Listrik"));
    itemKategori.add(new MenuKategoriModel(6, R.drawable.itm_wallet, "E-Wallet"));
    itemKategori.add(new MenuKategoriModel(4, R.drawable.itm_tv, "Tagihan\nWifi"));
    itemKategori.add(new MenuKategoriModel(5, R.drawable.itm_bpjs, "BPJS"));
    itemKategori.add(new MenuKategoriModel(3, R.drawable.itm_pdam, "PDAM"));
    itemKategori.add(new MenuKategoriModel(7, R.drawable.itm_game, "Games"));
    liveData.setValue(itemKategori); // Ganti dengan data nyata
    callback.onSucess(itemKategori);
    return liveData;
  }

  public LiveData<List<MenuKategoriModel>> getKategoriData2(HomeViewModel.RequestData callback) {
    MutableLiveData<List<MenuKategoriModel>> liveData = new MutableLiveData<>();
    callback.onStartLoading();
    List<MenuKategoriModel> itemKategori = new ArrayList<>();
    itemKategori.add(new MenuKategoriModel(0, R.drawable.itm_pulsa, "Streaming"));
    itemKategori.add(new MenuKategoriModel(1, R.drawable.itm_data, "E-Materai"));
    itemKategori.add(new MenuKategoriModel(2, R.drawable.itm_pln, "Listrik"));
    itemKategori.add(new MenuKategoriModel(6, R.drawable.itm_wallet, "E-Wallet"));
    itemKategori.add(new MenuKategoriModel(4, R.drawable.itm_tv, "Tagihan\nWifi"));
    itemKategori.add(new MenuKategoriModel(5, R.drawable.itm_bpjs, "BPJS"));
    itemKategori.add(new MenuKategoriModel(3, R.drawable.itm_pdam, "PDAM"));
    itemKategori.add(new MenuKategoriModel(7, R.drawable.itm_game, "Games"));
    liveData.setValue(itemKategori); // Ganti dengan data nyata
    callback.onSucess(itemKategori);
    return liveData;
  }
}
