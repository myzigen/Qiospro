package com.mhr.mobile.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.ImageViewTarget;
import com.mhr.mobile.adapter.Grid3Adapter.Grid3VH;
import com.mhr.mobile.databinding.ItemGrid3Binding;
import com.mhr.mobile.manage.response.PricelistResponse;
import com.mhr.mobile.util.QiosColor;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Grid3Adapter extends RecyclerView.Adapter<Grid3Adapter.Grid3VH> {
  private List<PricelistResponse.Product> mData;
  private OnSelectedProduk listener;
  
  public interface OnSelectedProduk{
	  void onSelected(String type);
  }
  
  public void setOnSelectedProduk(OnSelectedProduk produk){
	  this.listener = produk;
  }

  public Grid3Adapter(List<PricelistResponse.Product> list) {
    this.mData = removeDuplicates(list);
  }

  @Override
  public Grid3VH onCreateViewHolder(ViewGroup parent, int arg1) {
    ItemGrid3Binding binding =
        ItemGrid3Binding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
    return new Grid3VH(binding);
  }

  @Override
  public void onBindViewHolder(Grid3VH holder, int position) {
    PricelistResponse.Product model = mData.get(position);
    Context context = holder.itemView.getContext();

    Glide.with(holder.itemView)
        .asBitmap()
        .load(model.getIconUrl())
        .diskCacheStrategy(DiskCacheStrategy.ALL) // Atur caching
        .into(
            new ImageViewTarget<Bitmap>(holder.icon) {
              @Override
              protected void setResource(Bitmap resource) {
                if (resource != null) {
                  holder.icon.setImageBitmap(resource);
                  QiosColor.applyDominantColorToView(resource, holder.itemView);
                }
              }
            });
    holder.txtLabelName.setText(model.getProductDescription());
	holder.itemView.setOnClickListener(v ->{
		if (listener != null){
			listener.onSelected(model.getProductDescription());
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
    Set<String> type = new HashSet<>();
    List<PricelistResponse.Product> filterList = new ArrayList<>();

    for (PricelistResponse.Product product : productList) {
      if (!type.contains(product.getProductDescription())) {
        type.add(product.getProductDescription());
        filterList.add(product);
      }
    }

    return filterList;
  }

  @Override
  public int getItemCount() {
    return mData.size();
  }

  public class Grid3VH extends RecyclerView.ViewHolder {
    ImageView icon;
    TextView txtLabelName;
    View itemView;

    public Grid3VH(ItemGrid3Binding binding) {
      super(binding.getRoot());
      itemView = binding.root;
      icon = binding.icon;
      txtLabelName = binding.txtLabel;
    }
  }
}
