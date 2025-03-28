package com.mhr.mobile.ui.sheet;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.mhr.mobile.adapter.BankAdapter;
import com.mhr.mobile.databinding.SheetRecyclerviewBinding;
import com.mhr.mobile.loader.QiosDatabase;
import com.mhr.mobile.model.Bank;
import com.mhr.mobile.ui.inject.InjectionSheetFragment;
import com.mhr.mobile.util.AndroidViews;
import com.mhr.mobile.widget.recyclerview.SpacingItemDecoration;
import java.util.ArrayList;
import java.util.List;

public class SheetBankList extends InjectionSheetFragment {
  private SheetRecyclerviewBinding binding;
  private BankAdapter adapter;
  private OnSheetClickListener listener;

  public interface OnSheetClickListener {
    void onSheetClick(String logoUrl, String codeProduk, String namaBank, String norek, String type);
  }

  public void setOnSheetClickListener(OnSheetClickListener listener) {
    this.listener = listener;
  }

  @Override
  protected String getSheetTitle() {
    return "Metode Pembayaran";
  }

  @Override
  protected View onCreateSheetView(LayoutInflater inflater, ViewGroup viewGroup, Bundle bundle) {
    binding = SheetRecyclerviewBinding.inflate(getLayoutInflater());
    initUi();
    return binding.getRoot();
  }

  private void initUi() {
    initialize();
    loadDataBank();
    applyRecycler();
  }

  private void initialize() {}

  private void applyRecycler() {
    binding.recyclerview.addItemDecoration(new SpacingItemDecoration(1, 30, true));
    binding.recyclerview.setLayoutManager(new LinearLayoutManager(requireActivity()));
    adapter = new BankAdapter(requireActivity(), new ArrayList<>());
    binding.recyclerview.setAdapter(adapter);
    adapter.setOnBankClickListener(
        (logo, code, namaBank, norek, type) -> {
          listener.onSheetClick(logo, code, namaBank, norek, type);
          dismiss();
        });
  }

  private void loadDataBank() {
    QiosDatabase database = new QiosDatabase(requireActivity());
    database.setDatabaseName("list_bank");
    database.setLoadDataBank(
        new QiosDatabase.DatabaseBank() {

          @Override
          public void onStart() {
            binding.recyclerview.showShimmerAdapter();
          }

          @Override
          public void onDataChange(List<Bank.BankHeader> bank) {
            binding.recyclerview.hideShimmerAdapter();
            adapter.updateData(bank);
          }

          @Override
          public void onFailure(String error) {
            AndroidViews.showSnackbar(binding.getRoot(), error);
          }
        });
  }
}
