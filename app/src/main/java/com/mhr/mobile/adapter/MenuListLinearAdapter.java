package com.mhr.mobile.adapter;

import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.mhr.mobile.R;
import com.mhr.mobile.adapter.MenuListLinearAdapter.MenuListVH;
import com.mhr.mobile.databinding.ItemListLinearBinding;
import com.mhr.mobile.manage.response.ListPascaResponse;
import com.mhr.mobile.util.AndroidViews;
import com.mhr.mobile.util.QiosColor;
import java.util.List;

public class MenuListLinearAdapter extends RecyclerView.Adapter<MenuListLinearAdapter.MenuListVH> {

  private List<ListPascaResponse.Pasca> mData;
  private OnItemClickListener listener;
  private String queryText = "";
  private int selectedPosition = -1;
  private boolean showChecked = true;

  public void setShowChecked(boolean showChecked) {
    this.showChecked = showChecked;
    notifyDataSetChanged();
  }

  public void setQueryText(String query) {
    this.queryText = query.toLowerCase();
  }

  public interface OnItemClickListener {
    void OnItemClik(ListPascaResponse.Pasca providerName);
  }

  public void setOnItemClickListener(OnItemClickListener listener) {
    this.listener = listener;
  }

  public MenuListLinearAdapter(List<ListPascaResponse.Pasca> mData) {
    this.mData = mData;
  }

  @Override
  public MenuListVH onCreateViewHolder(ViewGroup parent, int arg1) {
    ItemListLinearBinding binding =
        ItemListLinearBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
    return new MenuListVH(binding);
  }

  @Override
  public void onBindViewHolder(MenuListVH holder, int position) {
    ListPascaResponse.Pasca model = mData.get(position);

    SpannableString highlight =
        AndroidViews.getHighlightedText(
            model.getName(),
            queryText,
            QiosColor.getColor(holder.itemView.getContext(), R.color.status_canceled));
    holder.produkName.setText(highlight);

    if (showChecked && selectedPosition == position) {
      holder.checked.setVisibility(View.VISIBLE);
    } else {
      holder.checked.setVisibility(View.GONE);
    }

    holder.wrapperChecked.setVisibility(showChecked ? View.VISIBLE : View.GONE);

    holder.itemView.setOnClickListener(
        v -> {
          updateSelectedPosition(position);
          if (listener != null) {
            listener.OnItemClik(model);
          }
        });
  }

  @Override
  public int getItemCount() {
    return mData.size();
  }

  public void perbaruiData(List<ListPascaResponse.Pasca> data) {
    if (data != null) {
      this.mData.clear();
      this.mData.addAll(data);
	  resetSelectedPosition();
    }
    notifyDataSetChanged();
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

  public class MenuListVH extends RecyclerView.ViewHolder {
    TextView produkName;
    ImageView checked;
    View itemView, wrapperChecked;

    public MenuListVH(ItemListLinearBinding binding) {
      super(binding.getRoot());
      itemView = binding.getRoot();
      produkName = binding.produkName;
      checked = binding.checked;
      wrapperChecked = binding.wrapperChecked;
    }
  }
}
