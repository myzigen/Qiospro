package com.mhr.mobile.adapter.pager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.mhr.mobile.R;
import com.mhr.mobile.databinding.ItemAktivasiBinding;
import com.mhr.mobile.manage.response.PricelistResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemGridAdapter extends RecyclerView.Adapter<ItemGridAdapter.ItemGridVH> {
  private List<PricelistResponse.Product> mData;
  private OnItemClickAktivasi listener;

  public interface OnItemClickAktivasi {
    void onItemAktivasiListener(int position);
  }

  public void setOnItemClickItemAktivasiListener(OnItemClickAktivasi listener) {
    this.listener = listener;
  }

  public ItemGridAdapter(List<PricelistResponse.Product> list) {
    this.mData = removeDuplicates(list);
  }

  @Override
  public ItemGridVH onCreateViewHolder(ViewGroup parent, int viewType) {
    ItemAktivasiBinding binding =
        ItemAktivasiBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
    return new ItemGridVH(binding);
  }

  @Override
  public void onBindViewHolder(ItemGridVH holder, int position) {
    PricelistResponse.Product model = mData.get(position);

    Glide.with(holder.itemView)
        .load(model.getIconUrl())
        .diskCacheStrategy(DiskCacheStrategy.ALL)
        .placeholder(R.drawable.ic_no_image)
        .into(holder.icon);

    holder.txtLabel.setText(model.getProductDescription());

    holder.itemView.setOnClickListener(
        v -> {
          if (listener != null) {
            listener.onItemAktivasiListener(position);
          }
        });
  }

  public void perbaruiData(List<PricelistResponse.Product> newData) {
    mData.clear();
    mData.addAll(removeDuplicates(newData));
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

  public class ItemGridVH extends RecyclerView.ViewHolder {
    ImageView icon;
    TextView txtLabel;
    View itemView;

    public ItemGridVH(ItemAktivasiBinding binding) {
      super(binding.getRoot());

      icon = binding.icon;
      txtLabel = binding.txtLabel;
      itemView = binding.root;
    }
  }
}
