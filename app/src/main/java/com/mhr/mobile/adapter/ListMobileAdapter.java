package com.mhr.mobile.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;
import com.mhr.mobile.adapter.ListMobileAdapter.MyViewHolder;
import com.mhr.mobile.adapter.base.HolderHelper;
import com.mhr.mobile.adapter.base.InjectAdapter;
import com.mhr.mobile.databinding.ItemPricelistBinding;
import com.mhr.mobile.manage.response.PricelistResponse;
import com.mhr.mobile.util.QiosColor;
import java.util.Collections;
import java.util.List;

public class ListMobileAdapter extends InjectAdapter<ListMobileAdapter.MyViewHolder> {
  private List<PricelistResponse.Product> mData;
  private ItemPricelistBinding binding;
  private OnProdukClickListener listener;
  private int selectedPosition;
  private boolean isInputValid = false;

  public ListMobileAdapter(List<PricelistResponse.Product> data) {
    this.mData = data;
  }

  public void setInputValid(boolean isInputValid) {
    this.isInputValid = isInputValid;
    notifyDataSetChanged();
  }

  public interface OnProdukClickListener {
    void onDataClick(PricelistResponse.Product data);
  }

  public void setOnDataClickListener(OnProdukClickListener listener) {
    this.listener = listener;
  }

  @Override
  public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    context = parent.getContext();
    binding = ItemPricelistBinding.inflate(LayoutInflater.from(context), parent, false);
    return new MyViewHolder(binding);
  }

  @Override
  public void onBindViewHolder(MyViewHolder holder, int position) {
    final PricelistResponse.Product model = mData.get(position);
    context = holder.binding.rootCard.getContext();

    if (isInputValid) {
      if (selectedPosition == position) {
        holder.binding.rootCard.setStrokeColor(QiosColor.getActiveColor(context));
      } else {
        holder.binding.rootCard.setStrokeColor(QiosColor.getDisableColor(context));
      }
    } else {
      holder.binding.rootCard.setStrokeColor(QiosColor.getDisableColor(context));
    }

    holder.binding.rootCard.setOnClickListener(
        v -> {
          updateSelectedPosition(position);
          if (listener != null) {
            listener.onDataClick(model);
          }
        });

    HolderHelper.applyHolder(context, holder, model);
  }

  public void updateSelectedPosition(int newSelectedPosition) {
    int previousPosition = selectedPosition;
    selectedPosition = newSelectedPosition;
    // Refresh tampilan item yang berubah
    notifyItemChanged(previousPosition);
    notifyItemChanged(newSelectedPosition);
  }

  public void resetSelectedPosition() {
    int previousPosition = selectedPosition;
    selectedPosition = -1;
    if (previousPosition != -1) {
      notifyItemChanged(previousPosition); // Perbarui tampilan posisi sebelumnya
    }
  }

  public void updateData(List<PricelistResponse.Product> newData) {
    if (newData != null) {
      mData.clear();
      mData.addAll(newData);
      sortProductListByPrice();
    }
    notifyDataSetChanged();
  }

  private void sortProductListByPrice() {
    if (mData != null) {
      Collections.sort(
          mData, (p1, p2) -> Double.compare(p1.getProductPrice(), p2.getProductPrice()));
    }
  }

  @Override
  public int getItemCount() {
    return mData.size();
  }

  public class MyViewHolder extends RecyclerView.ViewHolder {
    public ItemPricelistBinding binding;

    public MyViewHolder(ItemPricelistBinding binding) {
      super(binding.getRoot());
      this.binding = binding;
    }
  }
}
