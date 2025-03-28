package com.mhr.mobile.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.google.android.material.card.MaterialCardView;

import com.mhr.mobile.databinding.ItemQiosKategoriBinding;
import com.mhr.mobile.model.MenuKategoriModel;
import java.util.Arrays;
import java.util.List;

public class QiosMenuAdapter extends RecyclerView.Adapter<QiosMenuAdapter.KategoriVH> {

  private Context ctx;
  private List<MenuKategoriModel> mData;

  private OnItemClickListener onItemClickListener;

  public interface OnItemClickListener {
    void onItemClick(int position);
  }

  public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
    this.onItemClickListener = onItemClickListener;
  }

  public QiosMenuAdapter(Context context, List<MenuKategoriModel> list) {
    this.ctx = context;
    this.mData = list;
  }

  @Override
  public KategoriVH onCreateViewHolder(ViewGroup parent, int viewType) {
    ItemQiosKategoriBinding binding =
        ItemQiosKategoriBinding.inflate(LayoutInflater.from(ctx), parent, false);

    return new KategoriVH(binding);
  }

  @Override
  public void onBindViewHolder(KategoriVH holder, int position) {
    MenuKategoriModel model = mData.get(position);

    // holder.card.setCardBackgroundColor(color);
	if (model.getImageUrl() != null && !model.getImageUrl().isEmpty()){
		Glide.with(holder.itemView).load(model.getImageUrl()).into(holder.image);
	} else {
		holder.image.setImageResource(model.getIconKategori());
	}
   
    holder.text.setText(model.getNamaKategori());

    holder.itemView.setOnClickListener(
        v -> {
          if (onItemClickListener != null) {
            onItemClickListener.onItemClick(position);
          }
        });
  }

  // Method untuk memperbarui data
  public void updateData(List<MenuKategoriModel> newItemList) {
    this.mData = newItemList;
    notifyDataSetChanged();
  }

  @Override
  public int getItemCount() {
    return mData.size();
  }

  public class KategoriVH extends RecyclerView.ViewHolder {
    ImageView image;
    TextView text;
    View itemView;

    public KategoriVH(ItemQiosKategoriBinding binding) {
      super(binding.getRoot());
      itemView = binding.getRoot();
      image = binding.imgIconKategori;
      text = binding.itemTextIcon;
    }
  }
}
