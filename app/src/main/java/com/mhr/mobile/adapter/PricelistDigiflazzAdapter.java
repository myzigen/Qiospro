package com.mhr.mobile.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.card.MaterialCardView;
import com.mhr.mobile.R;
import com.mhr.mobile.adapter.base.InjectAdapter;
import com.mhr.mobile.databinding.ItemPricelistBinding;
import com.mhr.mobile.manage.response.PricelistDigiflazzResponse;
import com.mhr.mobile.util.FormatUtils;
import com.mhr.mobile.util.QiosColor;
import java.util.Collections;
import java.util.List;

public class PricelistDigiflazzAdapter
    extends InjectAdapter<PricelistDigiflazzAdapter.MyViewHolder> {

  private final List<PricelistDigiflazzResponse.Data> mData;
  private OnDataClickListener select;
  private int selectedPosition = -1;
  private boolean isInputValid = false;
  private ItemPricelistBinding binding;

  public interface OnDataClickListener {
    void onDataClick(PricelistDigiflazzResponse.Data price);
  }
 

  public PricelistDigiflazzAdapter(List<PricelistDigiflazzResponse.Data> list) {
    this.mData = list;
  }

  public void setOnDataClickListener(OnDataClickListener listener) {
    this.select = listener;
  }

  public void setInputValid(boolean isInputValid) {
    this.isInputValid = isInputValid;
    notifyDataSetChanged();
  }

  @Override
  public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    Context context = parent.getContext();
    binding = ItemPricelistBinding.inflate(LayoutInflater.from(context), parent, false);
    return new MyViewHolder(binding);
  }

  @Override
  public void onBindViewHolder(MyViewHolder holder, int position) {
    PricelistDigiflazzResponse.Data model = mData.get(position);
    Context context = holder.cardView.getContext();

    holder.produkName.setText(model.getProductName());
    // Hitung harga diskon di mana saja sebelum data diteruskan
    // model.setHargaDiskon(model.getHargaJual() - (model.getHargaJual() * (model.getDiskon() /
    // 100)));

    if (model.getDiskon() <= 0) { // Jika hargaDiskon nol
      holder.hargaJual.setText(FormatUtils.formatRupiah(model.getHargaJual()));
      holder.hargaJual.setTypeface(null, Typeface.BOLD);
      holder.hargaJual.setTextColor(QiosColor.getColor(context, R.color.qiospay_blue_dop));
      holder.hargaJual.setPaintFlags(
          holder.hargaJual.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
      holder.totalDiskon.setVisibility(View.GONE); // Sembunyikan diskon
      holder.hargaDiskon.setVisibility(View.GONE); // Sembunyikan hargaDiskon
    } else {
      holder.hargaJual.setText(FormatUtils.formatRupiah(model.getHargaJual()));
      holder.hargaJual.setTypeface(null, Typeface.NORMAL);
      holder.hargaJual.setTextColor(QiosColor.getColor(context, R.color.colorTextLight));
      holder.hargaJual.setPaintFlags(
          holder.hargaJual.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
      holder.hargaJual.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
      holder.totalDiskon.setVisibility(View.VISIBLE);
      holder.totalDiskon.setText((int) model.getDiskon() + " %");
      holder.hargaDiskon.setText(FormatUtils.formatRupiah(model.getHargaDiskon()));
      holder.hargaDiskon.setVisibility(View.VISIBLE);
    }

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

    if (previousPosition != -1) {
      notifyItemChanged(previousPosition);
    }

    notifyItemChanged(newSelectedPosition);
  }

  public void resetSelectedPosition() {
    int previousPosition = selectedPosition;
    selectedPosition = -1;
    if (previousPosition != -1) {
      notifyItemChanged(previousPosition); // Perbarui tampilan posisi sebelumnya
    }
  }

  public void perbaruiData(List<PricelistDigiflazzResponse.Data> newData) {
    if (newData != null) {
      mData.clear();
      mData.addAll(newData);
      sortProductListByPrice();
    }
    notifyDataSetChanged();
  }

  private void sortProductListByPrice() {
    if (mData != null) {
      Collections.sort(mData, (p1, p2) -> Integer.compare(p1.getPrice(), p2.getPrice()));
    }
  }

  @Override
  public int getItemCount() {
    return mData.size();
  }

  public class MyViewHolder extends RecyclerView.ViewHolder {
    MaterialCardView cardView;
    TextView produkName, hargaDiskon, totalDiskon, hargaJual;

    public MyViewHolder(ItemPricelistBinding binding) {
      super(binding.getRoot());
      cardView = binding.getRoot();
      produkName = binding.productName;
      hargaDiskon = binding.tvHargaDiskon;
      totalDiskon = binding.tvTotalDiskon;
      hargaJual = binding.tvHargaJual;
    }
  }
}
