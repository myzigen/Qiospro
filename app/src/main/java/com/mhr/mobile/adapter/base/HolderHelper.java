package com.mhr.mobile.adapter.base;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.TypedValue;
import android.view.View;
import com.mhr.mobile.R;
import com.mhr.mobile.adapter.ListMobileAdapter;
import com.mhr.mobile.manage.response.PricelistResponse;
import com.mhr.mobile.util.FormatUtils;
import com.mhr.mobile.util.QiosColor;

public class HolderHelper {

  public static void applyHolder(Context context, ListMobileAdapter.MyViewHolder holder, PricelistResponse.Product model) {
	holder.binding.productName.setText(model.getProductNominal());

    if ("non active".equalsIgnoreCase(model.getStatus())) {
      holder.binding.rootCard.setAlpha(0.5f);
      holder.binding.rootCard.setEnabled(false);
    } else {
      holder.binding.rootCard.setAlpha(1f);
      holder.binding.rootCard.setEnabled(true);
    }

    if (model.getDiskon() <= 0) {
      holder.binding.tvHargaJual.setText(FormatUtils.formatRupiah(model.getHargaDiskon()));
      holder.binding.tvHargaJual.setTypeface(null, Typeface.BOLD);
      holder.binding.tvHargaJual.setTextColor(QiosColor.getColor(context, R.color.qiospay_blue_dop));
      holder.binding.tvHargaJual.setPaintFlags(holder.binding.tvHargaJual.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
      holder.binding.tvHargaJual.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
      holder.binding.tvTotalDiskon.setVisibility(View.GONE);
      holder.binding.tvHargaDiskon.setVisibility(View.GONE);
    } else {
      holder.binding.tvHargaJual.setText(FormatUtils.formatRupiah(model.getHargaJual()));
      holder.binding.tvHargaJual.setTypeface(null, Typeface.NORMAL);
      holder.binding.tvHargaJual.setTextColor(QiosColor.getColor(context, R.color.colorTextLight));
      holder.binding.tvHargaJual.setPaintFlags(holder.binding.tvHargaJual.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
      holder.binding.tvHargaJual.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
      holder.binding.tvTotalDiskon.setVisibility(View.VISIBLE);
      holder.binding.tvTotalDiskon.setText((int) model.getDiskon() + " %");
      holder.binding.tvHargaDiskon.setText(FormatUtils.formatRupiah(model.getHargaDiskon()));
      holder.binding.tvHargaDiskon.setVisibility(View.VISIBLE);
    }
  }
}
