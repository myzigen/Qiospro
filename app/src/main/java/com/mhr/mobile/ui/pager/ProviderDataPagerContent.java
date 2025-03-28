package com.mhr.mobile.ui.pager;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.mhr.mobile.ui.inject.InjectionFragment;

public class ProviderDataPagerContent extends InjectionFragment {

  /*

  private static final String ARG_BRAND = "product_description";
  private ProvDataPagerContentBinding binding;
  private List<PricelistResponse.Product> mData = new ArrayList<>();
  private DataInternetAdapter adapter;
  private Map<String, Integer> selectedPositionsMap = new HashMap<>();
  private String currentFilter = "Semua";

  public static ProviderDataPagerContent newInstance(String brand) {
    ProviderDataPagerContent fragment = new ProviderDataPagerContent();
    Bundle args = new Bundle();
    args.putString(ARG_BRAND, brand);
    fragment.setArguments(args);
    return fragment;
  }

  @Override
  public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
    binding = ProvDataPagerContentBinding.inflate(layoutInflater, viewGroup, false);
    return binding.getRoot();
  }

  @Override
  public void onViewCreated(View arg0, Bundle savedInstanceState) {
    super.onViewCreated(arg0, savedInstanceState);
    if (savedInstanceState != null) {
      mData = (List<PricelistResponse.Product>) savedInstanceState.getSerializable("Data");
    }

    if (mData == null || mData.isEmpty()) {
      String brand = getArguments() != null ? getArguments().getString(ARG_BRAND) : "";
      muatData(brand);
    } else {
      applyFilter(currentFilter);
    }
  }

  private void initRecyclerView() {
    binding.recyclerview.addItemDecoration(new SpacingItemDecoration(1, AndroidUtilities.dpToPx(17, getActivity()), true));
    binding.recyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
    adapter = new DataInternetAdapter(new ArrayList<>());
    binding.recyclerview.setAdapter(adapter);
    adapter.setOnDataClickListener(klik -> saveSelectedPosition(klik));
  }

  private void muatData(String brand) {
    initRecyclerView();
    PricelistLoader.loadPricelist(
        getActivity(),
        "data",
        new PricelistLoader.LoaderExecute() {
          @Override
          public void onStartLoading() {
            binding.recyclerview.showShimmerAdapter();
          }

          @Override
          public void onSucces(List<PricelistResponse.Product> produk) {
            binding.recyclerview.hideShimmerAdapter();
            if (produk == null || produk.isEmpty()) return;
            if (mData == null) mData = new ArrayList<>();
            for (PricelistResponse.Product newProduk : produk) {
              if (newProduk.getProductDescription().equalsIgnoreCase(brand)) {
                mData.add(newProduk);
              }
            }
            adapter.updateData(mData);
            perbaruiData(mData);
          }
        });
  }

  public void applyFilter(String filter) {
    List<PricelistResponse.Product> filterData = new ArrayList<>();
    int simpanPosisi = selectedPositionsMap.getOrDefault(filter, -1);
    if (filter == null || filter.equalsIgnoreCase("Semua")) {
      if (mData != null) {
        filterData.addAll(mData);
      }
    } else {
      for (PricelistResponse.Product product : mData) {
        if (product.getProductNominal().equalsIgnoreCase(filter)) {
          filterData.add(product);
        }
      }
    }
    // update
    adapter.updateData(filterData);
    // fungsi agat ketika produk dipilih tidak memengaruhi produk di filter lain
    adapter.updateSelectedPosition(simpanPosisi);
  }

  public void resetFilter() {
    applyFilter("Semua");
  }

  private void perbaruiData(List<PricelistResponse.Product> data) {
    // ambil dari ProviderPaketData.class
    if (getActivity() instanceof ProviderData) {
      ((ProviderData) getActivity()).tambahkanItemFilter(data);
    }
  }

  private void saveSelectedPosition(int position) {
    selectedPositionsMap.put(currentFilter, position);
  }

  public List<PricelistResponse.Product> dapatkanFilterData() {
    return mData != null ? new ArrayList<>(mData) : new ArrayList<>();
  }

  @Override
  public void onResume() {
    super.onResume();
    applyFilter("Semua");
  }
  */
  @Override
  protected View onCreateQiosFragment(LayoutInflater i, ViewGroup v, Bundle b) {
    return null;
  }
}
