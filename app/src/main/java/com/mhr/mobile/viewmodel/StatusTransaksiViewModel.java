package com.mhr.mobile.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class StatusTransaksiViewModel extends ViewModel {

  private MutableLiveData<String> statusLiveData = new MutableLiveData<>();

  public LiveData<String> getStatus() {
    return statusLiveData;
  }

  public void updateStatus(String status) {
    statusLiveData.postValue(status);
  }
}
