package com.mhr.mobile.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.mhr.mobile.model.ItemSliderHome;
import com.mhr.mobile.model.MenuKategoriModel;
import com.mhr.mobile.repository.HomeRepository;
import java.util.List;

public class HomeViewModel extends ViewModel {
  private final HomeRepository homeRepository;
  private final MutableLiveData<Boolean> isRefreshing = new MutableLiveData<>(false);
  private final MutableLiveData<List<MenuKategoriModel>> kategori = new MutableLiveData<>();

  public HomeViewModel() {
    homeRepository = new HomeRepository();
  }

  public LiveData<Boolean> getIsRefreshing() {
    return isRefreshing;
  }

  public void setIsRefreshing(Boolean refreshing) {
    isRefreshing.setValue(refreshing);
  }


  public LiveData<List<MenuKategoriModel>> getKategoriData(RequestData callback) {
    return homeRepository.getKategoriData(callback);
  }
  
  public LiveData<List<MenuKategoriModel>> getKategoriData2(RequestData callback){
	  return homeRepository.getKategoriData2(callback);
  }

  public interface RequestData {
    void onStartLoading();

    void onSucess(List<?> data);
  }
}
