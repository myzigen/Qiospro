package com.mhr.mobile.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.mhr.mobile.databinding.ItemListBinding;
import com.mhr.mobile.model.MenuList;
import com.mhr.mobile.widget.imageview.RoundedImageView;
import java.util.List;

public class MenuListAdapter extends RecyclerView.Adapter<MenuListAdapter.MenuListVH> {
  private ItemListBinding binding;
  private List<MenuList> mData;
  private OnKlikDetailContent content;

  public interface OnKlikDetailContent {
    void onKlikDetailContent(int position);
  }

  public void setOnKlikDetailContent(OnKlikDetailContent content) {
    this.content = content;
  }

  public MenuListAdapter(List<MenuList> list) {
    this.mData = list;
  }

  @Override
  public MenuListVH onCreateViewHolder(ViewGroup parent, int arg1) {
    binding = ItemListBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
    return new MenuListVH(binding);
  }

  @Override
  public void onBindViewHolder(MenuListVH holder, int position) {
    MenuList model = mData.get(position);
    Glide.with(holder.itemView).load(model.getImageUrl()).into(holder.logo);
    holder.brand.setText(model.getBrand());
    holder.itemView.setOnClickListener(
        v -> {
          if (content != null) {
            content.onKlikDetailContent(position);
          }
        });
  }

  @Override
  public int getItemCount() {
    return mData.size();
  }

  public class MenuListVH extends RecyclerView.ViewHolder {
    RoundedImageView logo;
    TextView brand;
    View itemView;

    public MenuListVH(ItemListBinding binding) {
      super(binding.getRoot());
      itemView = binding.getRoot();
      logo = binding.icon;
      brand = binding.txtLabel;
    }
  }
}
