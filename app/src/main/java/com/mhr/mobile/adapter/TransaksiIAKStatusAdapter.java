package com.mhr.mobile.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.card.MaterialCardView;
import com.mhr.mobile.R;
import com.mhr.mobile.adapter.TransaksiIAKStatusAdapter.MyViewHolder;
import com.mhr.mobile.adapter.base.InjectAdapter;
import com.mhr.mobile.databinding.ItemStatusTransaksiBinding;
import com.mhr.mobile.manage.response.TransaksiIAKResponse;
import com.mhr.mobile.model.RiwayatTransaksi;
import com.mhr.mobile.util.FormatUtils;
import com.mhr.mobile.util.QiosColor;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TransaksiIAKStatusAdapter
    extends InjectAdapter<TransaksiIAKStatusAdapter.MyViewHolder> {

  private ItemStatusTransaksiBinding binding;
  private List<RiwayatTransaksi> mData;
  private OnProdukClickListener listener;
  private OnProdukDeleteListener deleteListener;
  private Context mContext;

  public interface OnProdukDeleteListener {
    void onProdukMore(TransaksiIAKResponse.Data data);

    void onProdukDelete(String ref_id);
  }

  public interface OnProdukClickListener {
    void onProdukClick(RiwayatTransaksi model);
  }

  public void setOnProdukClickListener(OnProdukClickListener listener) {
    this.listener = listener;
  }

  public void setOnProdukDeleteListener(OnProdukDeleteListener deleteListener) {
    this.deleteListener = deleteListener;
  }

  public TransaksiIAKStatusAdapter(List<RiwayatTransaksi> mData) {
    this.mData = mData != null ? mData : new ArrayList<>();
  }

  @Override
  public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    Context context = parent.getContext();
    binding = ItemStatusTransaksiBinding.inflate(LayoutInflater.from(context), parent, false);
    return new MyViewHolder(binding);
  }

  @Override
  public void onBindViewHolder(MyViewHolder holder, int position) {
    RiwayatTransaksi model = mData.get(position);
    Context context = holder.cardView.getContext();
    mContext = context;

    long timestamp = model.getTimestamp();
    String date = FormatUtils.formatTimestamp(timestamp);

    holder.binding.customerId.setText(model.getCustomerId());
    holder.binding.tvDate.setText(date);
    holder.binding.tvProduk.setText(model.getBrand());
    holder.binding.price.setText(FormatUtils.formatRupiah(model.getHarga()));
    setStatus(holder.binding.tvStatus, model.getUpdateStatusMessage(), model.getStatus());

    holder.cardView.setOnClickListener(
        v -> {
          if (listener != null) {
            listener.onProdukClick(model);
          }
        });

    holder.binding.menuMore.setOnClickListener(
        v -> {
          showMenuOption(v, model.getRefId());
        });
  }

  public void updateStatus(String refId, String status, double statusCode) {
    boolean isUpdated = false;
    for (RiwayatTransaksi item : mData) {
      if (item.getRefId().equals(refId)) { // Sekarang tidak akan menyebabkan NullPointerException
        item.setStatus(statusCode);
        item.setUpdateStatusMessage(status);
        isUpdated = true;
      }
    }
    if (isUpdated) {
      notifyDataSetChanged(); // Perbarui tampilan setelah semua item diperbarui
    }
  }

  public void setStatus(TextView textView, String status, double statusCode) {
    if (statusCode == 0) {
      textView.setTextColor(QiosColor.getColor(mContext, R.color.status_pending));
      textView.setText(status);
    } else if (statusCode == 1) {
      textView.setTextColor(QiosColor.getColor(mContext, R.color.status_approved));
      textView.setText(status);
    } else if (statusCode == 2) {
      textView.setTextColor(QiosColor.getColor(mContext, R.color.status_canceled));
      textView.setText(status);
    }
  }

  @Override
  public int getItemCount() {
    return mData.size();
  }

  public void updateData(List<RiwayatTransaksi> data) {
    // Urutkan berdasarkan timestamp, descending order
    if (data != null) {
      this.mData.clear();
      this.mData.addAll(data);
      Collections.sort(this.mData, (h1, h2) -> Long.compare(h2.getTimestamp(), h1.getTimestamp()));
    }

    notifyDataSetChanged();
  }

  public void removeItem(String ref_id) {
    for (int i = 0; i < mData.size(); i++) {
      if (mData.get(i).getRefId().equals(ref_id)) {
        mData.remove(i); // Hapus item dari list
        notifyItemRemoved(i); // Beri tahu RecyclerView bahwa item dihapus
        break;
      }
    }
  }

  private void showMenuOption(View view, String ref_id) {
    PopupMenu popupMenu = new PopupMenu(view.getContext(), view);
    popupMenu.getMenuInflater().inflate(R.menu.option_menu_delete, popupMenu.getMenu());
    popupMenu.setOnMenuItemClickListener(
        item -> {
          if (item.getItemId() == R.id.menu_delete) {
            if (deleteListener != null) {
              deleteListener.onProdukDelete(ref_id);
            }
            return true;
          }
          return false;
        });
    popupMenu.show();
  }

  public class MyViewHolder extends RecyclerView.ViewHolder {
    TextView status;
    MaterialCardView cardView;
    ItemStatusTransaksiBinding binding;

    public MyViewHolder(ItemStatusTransaksiBinding binding) {
      super(binding.getRoot());
      this.binding = binding;
      cardView = binding.getRoot();
      status = binding.tvStatus;
    }
  }
}
