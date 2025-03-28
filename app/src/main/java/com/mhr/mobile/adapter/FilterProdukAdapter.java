package com.mhr.mobile.adapter;

import android.content.Context;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.card.MaterialCardView;
import com.mhr.mobile.databinding.ItemFilterProdukBinding;
import com.mhr.mobile.manage.response.PricelistResponse;
import com.mhr.mobile.util.QiosColor;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FilterProdukAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

  private List<PricelistResponse.Product> mData;
  private OnFilterClickListener listener;
  private int selectedPosition = 0;
  private SparseIntArray selectedPositionsForPages = new SparseIntArray();

  public interface OnFilterClickListener {
    void onFilterClick(PricelistResponse.Product filter, int position);
  }

  public FilterProdukAdapter(List<PricelistResponse.Product> list, OnFilterClickListener listener) {
    this.mData = removeDuplicates(list);
    this.listener = listener;
  }

  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    ItemFilterProdukBinding binding =
        ItemFilterProdukBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
    return new FilterVH(binding);
  }

  @Override
  public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
    PricelistResponse.Product model = mData.get(position);
    if (holder instanceof FilterVH) {
      ((FilterVH) holder).bind(model, position);
    }
  }

  public void updateData(List<PricelistResponse.Product> newData) {
    mData.clear();
    mData.addAll(removeDuplicates(newData));
    notifyDataSetChanged();
  }

  public void updateSelectedPosition(int newSelectedPosition) {
    int previousPosition = selectedPosition; // Simpan posisi sebelumnya
    selectedPosition = newSelectedPosition; // Perbarui posisi baru
    // Perbarui tampilan item sebelumnya dan yang baru
    notifyItemChanged(previousPosition);
    notifyItemChanged(newSelectedPosition);
  }

  public void updateSelectedPositionForPage(int page, int position) {
    selectedPositionsForPages.put(page, position);
    selectedPosition = position; // Perbarui posisi untuk halaman saat ini
    notifyDataSetChanged();
  }

  public int getSelectedPositionForPage(int page) {
    return selectedPositionsForPages.get(page, 0); // Default posisi 0
  }

  public String getFilterType(int posisi) {
    if (posisi >= 0 && posisi < mData.size()) {
      return mData.get(posisi).getProductNominal();
    }
    return "Semua"; // Default
  }

  public PricelistResponse.Product getDataAt(int position) {
    if (position >= 0 && position < mData.size()) {
      return mData.get(position);
    }
    return null; // Kembalikan null jika posisi tidak valid
  }

  private List<PricelistResponse.Product> removeDuplicates(
      List<PricelistResponse.Product> productList) {
    Set<String> type = new HashSet<>();
    List<PricelistResponse.Product> filterList = new ArrayList<>();

    for (PricelistResponse.Product product : productList) {
      if (!type.contains(product.getProductNominal())) {
        type.add(product.getProductNominal());
        filterList.add(product);
      }
    }

    return filterList;
  }

  @Override
  public int getItemCount() {
    return mData.size();
  }

  public class FilterVH extends RecyclerView.ViewHolder {
    TextView txtFilter;
    MaterialCardView itemView;

    public FilterVH(ItemFilterProdukBinding binding) {
      super(binding.getRoot());
      itemView = binding.root;
      txtFilter = binding.txtFilter;
    }

    public void bind(PricelistResponse.Product model, int position) {
      String displayText =
          (position == 0 && model.getProductNominal().equals("Semua")) ? "Semua" : model.getProductNominal();
      txtFilter.setText(displayText);

      Context context = itemView.getContext();

      if (selectedPosition == position) {
        itemView.setStrokeColor(QiosColor.getActiveColor(context));
      } else {
        itemView.setStrokeColor(QiosColor.getDisableColor(context));
      }
      itemView.setOnClickListener(
          v -> {
            updateSelectedPosition(position);
            if (listener != null) {
              if (position == 0) {
                listener.onFilterClick(null, position); // Null untuk mereset filter
              } else {
                listener.onFilterClick(model, position);
              }
            }
          });
    }
  }
}
