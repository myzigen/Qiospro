package com.mhr.mobile.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.mhr.mobile.R;
import com.mhr.mobile.adapter.PricelistAdapter.ViewHolder;
import com.mhr.mobile.adapter.base.HolderHelper;
import com.mhr.mobile.adapter.base.MediaAdapter;
import com.mhr.mobile.adapter.base.MediaViewHolder;
import com.mhr.mobile.databinding.ItemPricelistBinding;
import com.mhr.mobile.manage.response.PricelistResponse;
import com.mhr.mobile.util.FormatUtils;
import com.mhr.mobile.util.QiosColor;
import java.util.Collections;
import java.util.List;

public class PricelistAdapter
    extends MediaAdapter<PricelistAdapter.ViewHolder, PricelistResponse.Data> {
  List<PricelistResponse.Product> mData;
  private int selectedPosition = -1;
  public boolean isInputValid = false;

  public OnDataClickListener select;

  public interface OnDataClickListener {
    void onDataClick(PricelistResponse.Product data);
  }

  public void setOnDataClickListener(OnDataClickListener selected) {
    this.select = selected;
  }

  public void setInputValid(boolean isInputValid) {
    this.isInputValid = isInputValid;
    notifyDataSetChanged();
  }

  public PricelistAdapter(Context context, List<PricelistResponse.Product> list) {
    super(context);
    this.mData = list;
  }

  @Override
  public ViewHolder onCreateViewHolder(ViewGroup parent, int arg1) {
    ItemPricelistBinding binding =
        ItemPricelistBinding.inflate(LayoutInflater.from(context), parent, false);
    return new ViewHolder(binding);
  }

  @Override
  public void onBindViewHolder(ViewHolder holder, int position) {
    PricelistResponse.Product model = mData.get(position);
   
    holder.produkName.setText(model.getProductNominal());
    if ("non active".equalsIgnoreCase(model.getStatus())) {
      holder.cardView.setAlpha(0.5f);
      holder.cardView.setEnabled(false);
    } else {
      holder.cardView.setAlpha(1f);
      holder.cardView.setEnabled(true);
    }

    if (model.getDiskon() <= 0) { // Jika hargaDiskon nol
      holder.hargaJual.setText(FormatUtils.formatRupiah(model.getHargaJual()));
      holder.hargaJual.setTypeface(null, Typeface.BOLD);
      holder.hargaJual.setTextColor(QiosColor.getColor(context, R.color.qiospay_blue_dop));
      holder.hargaJual.setPaintFlags(
          holder.hargaJual.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
      holder.hargaJual.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
      holder.totalDiskon.setVisibility(View.GONE); // Sembunyikan diskon
      holder.hargaDiskon.setVisibility(View.GONE); // Sembunyikan hargaDiskon
    } else {
      holder.hargaJual.setText(FormatUtils.formatRupiah(model.getHargaJual()));
      holder.hargaJual.setTypeface(null, Typeface.NORMAL);
      holder.hargaJual.setTextColor(QiosColor.getColor(context, R.color.colorTextLight));
      holder.hargaJual.setPaintFlags(holder.hargaJual.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
      holder.hargaJual.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
      holder.totalDiskon.setVisibility(View.VISIBLE);
      holder.totalDiskon.setText((int) model.getDiskon() + " %");
      holder.hargaDiskon.setText(FormatUtils.formatRupiah(model.getHargaDiskon()));
      holder.hargaDiskon.setVisibility(View.VISIBLE);
    }

    // Highlight jika item dipilih
    Context context = holder.itemView.getContext();

    if (isInputValid) {
      if (selectedPosition == position) {
        holder.cardView.setStrokeColor(QiosColor.getActiveColor(context));

      } else {
        holder.cardView.setStrokeColor(QiosColor.getDisableColor(context));
      }
    } else {
      holder.cardView.setStrokeColor(QiosColor.getDisableColor(context));
    }

    holder.cardView.setOnClickListener(
        v -> {
          updateSelectedPosition(position);
          if (select != null) {
            select.onDataClick(model);
          }
        });
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

  public void perbaruiData(List<PricelistResponse.Product> newData) {
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

  public class ViewHolder extends MediaViewHolder {
    public ViewHolder(ItemPricelistBinding binding) {
      super(binding);

      View itemView = binding.getRoot();
    }
  }
}
