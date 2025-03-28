package com.mhr.mobile.ui.sheet;

import android.app.Dialog;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.mhr.mobile.R;
import com.mhr.mobile.adapter.MenuListLinearAdapter;
import com.mhr.mobile.databinding.SheetPdamBinding;
import com.mhr.mobile.loader.PricelistLoader;
import com.mhr.mobile.manage.response.ListPascaResponse;
import com.mhr.mobile.ui.inject.InjectionSheetFragment;
import com.mhr.mobile.util.WindowPreferencesManager;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;

public class SheetPdam extends InjectionSheetFragment implements SearchView.OnQueryTextListener {
  private SheetPdamBinding binding;
  private MenuListLinearAdapter adapter;
  private List<ListPascaResponse.Pasca> mData = new ArrayList<>();
  private SelectProviderListener listener;

  public interface SelectProviderListener {
    void onSelectProvider(String providerName);
  }

  public void setOnSelectProvider(SelectProviderListener listener) {
    this.listener = listener;
  }

  public static SheetPdam newInstance() {
    SheetPdam fragment = new SheetPdam();
    Bundle args = new Bundle();
    fragment.setArguments(args);
    return fragment;
  }

  @Override
  protected String getSheetTitle() {
    return "Pilih Wilayah";
  }

  @Override
  protected View onCreateSheetView(LayoutInflater inflater, ViewGroup viewGroup, Bundle bundle) {
    binding = SheetPdamBinding.inflate(getLayoutInflater());
    View view = binding.getRoot();
    view.setBackgroundResource(R.drawable.shape_corners_top);
    applyWindowInsets(view);

    initialize();
    return view;
  }

  @Override
  public Dialog onCreateDialog(Bundle bundle) {
    BottomSheetDialog dialog =
        new BottomSheetDialog(requireContext(), R.style.FullscreenBottomSheetDialogFragment);
    new WindowPreferencesManager(requireContext()).applyEdgeToEdgePreference(dialog.getWindow());

    // Menambahkan tint pada background dialog
    dialog.setOnShowListener(
        dialogInterface -> {
          BottomSheetDialog bottomSheetDialog = (BottomSheetDialog) dialogInterface;
          FrameLayout bottomSheet =
              bottomSheetDialog.findViewById(com.google.android.material.R.id.design_bottom_sheet);

          if (bottomSheet != null) {
            // Ubah backgroundTint

            bottomSheet.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FFFFFF")));
          }
        });

    return dialog;
  }

  private void initialize() {
    binding.searchview.setOnQueryTextListener(this);
    applyRecyclerview();
    listPDAM(); // :menampilkan daftar produk pdam
    binding.btnfinish.setOnClickListener(v -> dismiss());
  }

  private void applyRecyclerview() {
    binding.recyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
    adapter = new MenuListLinearAdapter(mData);
    binding.recyclerview.setAdapter(adapter);
    adapter.setOnItemClickListener(
        providerName -> {
          if (listener != null) {
            listener.onSelectProvider(providerName.getName());
          }
          // dismiss();
          binding.providerName.setText(providerName.getName());
          binding.expandable.expand();
        });
  }

  private void listPDAM() {
    PricelistLoader.loadPricePasca(
        getActivity(),
        "pdam",
        new PricelistLoader.LoaderExecutePasca() {
          @Override
          public void onStartLoading() {
            binding.recyclerview.showShimmerAdapter();
			binding.searchview.setEnabled(false);
          }

          @Override
          public void onSucces(List<ListPascaResponse.Pasca> produk) {
            binding.recyclerview.hideShimmerAdapter();
			binding.searchview.setEnabled(true);
            mData = produk;

            if (produk.isEmpty()) {
              binding.tvEmpty.setVisibility(View.VISIBLE);
              // binding.recyclerview.setVisibility(View.GONE);
            } else {
              binding.tvEmpty.setVisibility(View.GONE);
              // binding.recyclerview.setVisibility(View.VISIBLE);
            }

            adapter.perbaruiData(mData);
          }

          @Override
          public void onFailure(String error) {}
        });
  }

  @Override
  public boolean onQueryTextSubmit(String arg0) {
    return false;
  }

  @Override
  public boolean onQueryTextChange(String query) {
    // Eksekusi pencarian di thread background
    searchQueryInBackground(query);
    return true;
  }

  private void searchQueryInBackground(final String query) {
    ExecutorService executorService = Executors.newSingleThreadExecutor();
    executorService.execute(
        new Runnable() {
          @Override
          public void run() {
            // Proses pencarian
            final List<ListPascaResponse.Pasca> filterWilayah = new ArrayList<>();
            for (ListPascaResponse.Pasca pasca : mData) {
              if (pasca.getName().toLowerCase().contains(query.toLowerCase())) {
                filterWilayah.add(pasca);
              }
            }

            // Update UI setelah pencarian selesai
            requireActivity().runOnUiThread(
                new Runnable() {
                  @Override
                  public void run() {
                    updateUIWithSearchResults(filterWilayah, query);
                  }
                });
          }
        });
  }

  private void updateUIWithSearchResults(
      List<ListPascaResponse.Pasca> filterWilayah, String query) {
    adapter.setQueryText(query);
    adapter.perbaruiData(filterWilayah);
    binding.expandable.collapse();

    if (filterWilayah.isEmpty()) {
      binding.tvEmpty.setVisibility(View.VISIBLE);
    } else {
      binding.tvEmpty.setVisibility(View.GONE);
    }
  }

  @Override
  public void onStart() {
    super.onStart();
    // Set BottomSheet ke mode full screen
    View view = getView();

    if (view != null) {
      ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
      layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
      view.setLayoutParams(layoutParams);
    }
    // Mengatur behavior BottomSheet
    BottomSheetBehavior<?> behavior = BottomSheetBehavior.from((View) getView().getParent());
    behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    behavior.setSkipCollapsed(true);
    behavior.setDraggable(false);
  }
}
