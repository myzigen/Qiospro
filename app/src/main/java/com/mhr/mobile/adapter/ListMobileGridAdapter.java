package com.mhr.mobile.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.mhr.mobile.adapter.ListMobileGridAdapter.MyViewHolder;
import com.mhr.mobile.adapter.base.InjectAdapter;
import com.mhr.mobile.databinding.ItemGridBinding;
import com.mhr.mobile.manage.response.PricelistResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListMobileGridAdapter extends InjectAdapter<ListMobileGridAdapter.MyViewHolder> {
  private ItemGridBinding binding;
  private List<PricelistResponse.Product> mData;
  private OnProdukClickListener listener;

  public interface OnProdukClickListener {
    void onProdukClick(PricelistResponse.Product produk);
  }

  public void setOnProdukClickListener(OnProdukClickListener listener) {
    this.listener = listener;
  }

  public ListMobileGridAdapter(List<PricelistResponse.Product> data) {
    this.mData = removeDuplicates(data);
  }

  @Override
  public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    context = parent.getContext();
    binding = ItemGridBinding.inflate(LayoutInflater.from(context), parent, false);
    return new MyViewHolder(binding);
  }

  @Override
  public void onBindViewHolder(MyViewHolder holder, int position) {
    PricelistResponse.Product model = mData.get(position);
    context = holder.binding.root.getContext();
    Glide.with(context).load(model.getIconUrl()).into(holder.binding.icon);
    holder.binding.txtLabel.setText(model.getProductDescription());
    holder.binding.root.setOnClickListener(v -> klik(model));
  }

  private void klik(PricelistResponse.Product model) {
    if (listener != null) {
      listener.onProdukClick(model);
    }
  }

  public void updateData(List<PricelistResponse.Product> data) {
    mData.clear();
    mData.addAll(removeDuplicates(data));
    notifyDataSetChanged();
  }

  private List<PricelistResponse.Product> removeDuplicates(
      List<PricelistResponse.Product> productList) {
    Map<String, PricelistResponse.Product> uniqueProducts = new HashMap<>();

    for (PricelistResponse.Product product : productList) {
      String key = product.getProductDescription(); // Gunakan atribut yang unik untuk identifikasi
      uniqueProducts.put(key, product);
    }

    return new ArrayList<>(uniqueProducts.values());
  }

  @Override
  public int getItemCount() {
    return mData.size();
  }

  public class MyViewHolder extends RecyclerView.ViewHolder {
    ItemGridBinding binding;

    public MyViewHolder(ItemGridBinding binding) {
      super(binding.getRoot());
      this.binding = binding;
    }
  }
}
