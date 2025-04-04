package com.mhr.mobile.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;
import com.mhr.mobile.adapter.AkunAdapter.MyViewHolder;
import com.mhr.mobile.adapter.base.InjectAdapter;
import com.mhr.mobile.databinding.ItemListLinearBinding;
import com.mhr.mobile.databinding.ItemListWithImageBinding;
import com.mhr.mobile.model.Akun;
import com.mhr.mobile.model.MenuList;
import java.util.List;

public class AkunAdapter extends InjectAdapter<AkunAdapter.MyViewHolder> {
  private List<Akun> mData;
  private OnClickListener listener;

  public interface OnClickListener {
    void onClick(int position);
  }

  public void setOnClickListener(OnClickListener listener) {
    this.listener = listener;
  }

  public AkunAdapter(List<Akun> data) {
    this.mData = data;
  }

  @Override
  public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    Context context = parent.getContext();
    ItemListWithImageBinding binding =
        ItemListWithImageBinding.inflate(LayoutInflater.from(context), parent, false);
    return new MyViewHolder(binding);
  }

  @Override
  public void onBindViewHolder(MyViewHolder holder, int position) {
    Akun model = mData.get(position);
	
	holder.binding.icon.setImageResource(model.getIconResId());
    holder.binding.produkName.setTypeface(null, Typeface.BOLD);
    holder.binding.produkName.setText(model.getLabelName());
    holder.binding.wrapperChecked.setVisibility(View.GONE);
    holder
        .binding
        .getRoot()
        .setOnClickListener(
            v -> {
              if (listener != null) {
                listener.onClick(position);
              }
            });
  }

  @Override
  public int getItemCount() {
    return mData.size();
  }

  public void updateData(List<Akun> data) {
	  mData.clear();
	  mData.addAll(data);
	  notifyDataSetChanged();
  }

  public class MyViewHolder extends RecyclerView.ViewHolder {
    ItemListWithImageBinding binding;

    public MyViewHolder(ItemListWithImageBinding binding) {
      super(binding.getRoot());
      this.binding = binding;
    }
  }
}
