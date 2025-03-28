package com.mhr.mobile.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.mhr.mobile.databinding.ItemWifiBinding;
import com.mhr.mobile.manage.response.ListPascaResponse;
import java.util.List;

public class ListPascaAdapter extends RecyclerView.Adapter<ListPascaAdapter.ListPascaVH> {
  private ItemWifiBinding binding;
  private List<ListPascaResponse.Pasca> mData;
  private OnItemClickListener listener;

  public interface OnItemClickListener {
    void OnItemClik(String imgUrl, String provider);
  }

  public void setOnItemClickListener(OnItemClickListener listener) {
    this.listener = listener;
  }

  // Konstruktor
  public ListPascaAdapter(List<ListPascaResponse.Pasca> list) {
    this.mData = list;
  }

  @Override
  public ListPascaVH onCreateViewHolder(ViewGroup parent, int arg1) {
    binding = ItemWifiBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
    return new ListPascaVH(binding);
  }

  @Override
  public void onBindViewHolder(ListPascaVH holder, int position) {
    ListPascaResponse.Pasca model = mData.get(position);

    // Menampilkan gambar dengan Glide
    Glide.with(holder.itemView)
        .load(model.getLogoUrl()) // URL gambar yang ingin diambil
        .into(holder.icon); // Gambar akan ditampilkan di ImageView

    holder.name.setText(model.getCode());
    holder.name2.setText(model.getName());

    holder.itemView.setOnClickListener(
        v -> {
          if (listener != null) {
            listener.OnItemClik(model.getLogoUrl(), model.getCode());
          }
        });
  }

  // Metode untuk memperbarui data
  public void perbaruiData(List<ListPascaResponse.Pasca> newData) {
    if (newData != null) {
      mData.clear();
      mData.addAll(newData);
    }
    notifyDataSetChanged();
  }

  @Override
  public int getItemCount() {
    return mData.size();
  }

  // ViewHolder untuk item
  public class ListPascaVH extends RecyclerView.ViewHolder {
    TextView name, name2, name3;
    View itemView;
    ImageView icon;

    public ListPascaVH(ItemWifiBinding binding) {
      super(binding.getRoot());
      itemView = binding.getRoot();
      name = binding.txtLabel;
      name2 = binding.test;
      icon = binding.icon;
    }
  }
}
