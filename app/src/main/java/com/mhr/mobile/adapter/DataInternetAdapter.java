package com.mhr.mobile.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.card.MaterialCardView;
import com.mhr.mobile.R;
import com.mhr.mobile.adapter.base.HolderHelper;
import com.mhr.mobile.databinding.ItemDataInternetBinding;
import com.mhr.mobile.manage.response.PricelistResponse;
import com.mhr.mobile.util.AndroidViews;
import com.mhr.mobile.util.FormatUtils;
import com.mhr.mobile.util.QiosColor;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DataInternetAdapter extends RecyclerView.Adapter<DataInternetAdapter.DataInternetVH> {
  private List<PricelistResponse.Product> mData;
  private List<PricelistResponse.Product> mOriginalData; // Simpan data asli di sini
  private int selectedPosition = -1; // Posisi item yang dipilih
  public OnDataClickListener select;

  private String queryText = "";

  public void setQueryText(String query) {
    this.queryText = query.toLowerCase();
  }

  public interface OnDataClickListener {
    void onDataClick(PricelistResponse.Product model);
  }

  public void setOnDataClickListener(OnDataClickListener selected) {
    this.select = selected;
  }
  // Constructor
  public DataInternetAdapter(List<PricelistResponse.Product> list) {
    this.mData = list;
    this.mOriginalData = new ArrayList<>(list); // Salin data asli
  }

  @NonNull
  @Override
  public DataInternetVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    ItemDataInternetBinding binding =
        ItemDataInternetBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
    return new DataInternetVH(binding);
  }

  @Override
  public void onBindViewHolder(@NonNull DataInternetVH holder, int position) {
    PricelistResponse.Product product = mData.get(position);
    Context context = holder.itemView.getContext();

    SpannableString highlight =
        AndroidViews.getHighlightedText(
            product.getProductNominal(),
            queryText,
            QiosColor.getColor(holder.itemView.getContext(), R.color.status_canceled));
    holder.namaProduk.setText(highlight);

    if (product.getDiskon() <= 0) { // Jika hargaDiskon nol
      holder.hargaJual.setText(FormatUtils.formatRupiah(product.getHargaJual()));
      holder.hargaJual.setTypeface(null, Typeface.BOLD);
      holder.hargaJual.setTextColor(QiosColor.getColor(context, R.color.qiospay_blue_dop));
      holder.hargaJual.setPaintFlags(holder.hargaJual.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
      holder.totalDiskon.setVisibility(View.GONE); // Sembunyikan diskon
      holder.hargaDiskon.setVisibility(View.GONE); // Sembunyikan hargaDiskon
    } else {
      holder.hargaJual.setText(FormatUtils.formatRupiah(product.getHargaJual()));
      holder.hargaJual.setTypeface(null, Typeface.NORMAL);
      holder.hargaJual.setTextColor(QiosColor.getColor(context, R.color.colorTextLight));
      holder.hargaJual.setPaintFlags(holder.hargaJual.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
      holder.hargaJual.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
      holder.totalDiskon.setVisibility(View.VISIBLE);
      holder.totalDiskon.setText((int) product.getDiskon() + " %");
      holder.hargaDiskon.setText(FormatUtils.formatRupiah(product.getHargaDiskon()));
      holder.hargaDiskon.setVisibility(View.VISIBLE);
    }
	
	//HolderHelper.applyHolder(context,holder,product);

    String produkDetail = product.getProductDetails();

    // Tetapkan maxLines sebelum mengatur teks
    holder.deskripsiProduk.setMaxLines(2);
    holder.deskripsiProduk.setEllipsize(TextUtils.TruncateAt.END);
    holder.deskripsiProduk.setText(produkDetail);

    // Highlight jika item dipilih

    if ("non active".equalsIgnoreCase(product.getStatus())) {
      holder.itemView.setAlpha(0.5f);
      holder.itemView.setEnabled(false);
      holder.statusProduk.setVisibility(View.VISIBLE);
    } else {
      holder.statusProduk.setVisibility(View.GONE);
      holder.itemView.setAlpha(1f);
      holder.itemView.setEnabled(true);
    }

    if (selectedPosition == position) {
      holder.itemView.setStrokeColor(QiosColor.getActiveColor(context));
    } else {
      holder.itemView.setStrokeColor(QiosColor.getDisableColor(context));
    }

    // Klik item untuk memperbarui posisi
    holder.itemView.setOnClickListener(
        v -> {
          updateSelectedPosition(position);
          select.onDataClick(product);
        });
  }

  @Override
  public int getItemCount() {
    return mData != null ? mData.size() : 0;
  }

  // Mengambil data asli
  public List<PricelistResponse.Product> getOriginalData() {
    return mOriginalData;
  }

  // Menambahkan metode untuk mengatur data asli lagi jika perlu
  public void setOriginalData(List<PricelistResponse.Product> data) {
    this.mOriginalData = new ArrayList<>(data);
    this.mData = new ArrayList<>(data);
    sortProductListByPrice();
    notifyDataSetChanged();
  }

  // Menambahkan metode untuk memperbarui data
  public void updateData(List<PricelistResponse.Product> data) {
    this.mData.clear();
    this.mData.addAll(data);
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

  public void sortProductListByPrice() {
    if (mData != null) {
      Collections.sort(
          mData, (p1, p2) -> Double.compare(p1.getProductPrice(), p2.getProductPrice()));
    }
  }

  public class DataInternetVH extends RecyclerView.ViewHolder {
    private final TextView namaProduk, deskripsiProduk, hargaJual, totalDiskon, hargaDiskon;
    private final MaterialCardView itemView;
    private final View statusProduk;

    public DataInternetVH(@NonNull ItemDataInternetBinding binding) {
      super(binding.getRoot());
      itemView = binding.root;
      namaProduk = binding.txtNamaProduk;
      deskripsiProduk = binding.txtDeskripsiProduk;
      totalDiskon = binding.tvDiskon;
      hargaJual = binding.tvHargaJual;
      hargaDiskon = binding.tvHargaDiskon;
      statusProduk = binding.statusProduk;
    }
  }
}
