package com.mhr.mobile.ui.navcontent;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.card.MaterialCardView;
import com.mhr.mobile.R;
import com.mhr.mobile.databinding.ItemDashboardBinding;
import com.mhr.mobile.databinding.QiosDashboardFragmentBinding;
import com.mhr.mobile.manage.request.CekSaldoRequest;
import com.mhr.mobile.manage.response.CekSaldoResponse;
import com.mhr.mobile.ui.content.ContentKasir;
import com.mhr.mobile.ui.content.ContentTopup;
import com.mhr.mobile.ui.inject.InjectionFragment;
import com.mhr.mobile.util.FormatUtils;
import com.mhr.mobile.util.QiosPreferences;
import java.util.ArrayList;
import java.util.List;

public class QiosDashboardFragment extends InjectionFragment {
  private QiosDashboardFragmentBinding binding;
  private List<MyAdapter.ItemModel> list = new ArrayList<>();
  private MyAdapter adapter;
  private QiosPreferences preferences;
  private boolean isHidenSaldo = false;

  @Override
  public View onCreateQiosFragment(LayoutInflater inflater, ViewGroup viewGroup, Bundle bundle) {
    binding = QiosDashboardFragmentBinding.inflate(getLayoutInflater());
    preferences = new QiosPreferences(getActivity());
    isHidenSaldo = preferences.isHiddenSaldo();

    loadSaldo();

    return binding.getRoot();
  }

  public void loadSaldo() {
    CekSaldoRequest request = new CekSaldoRequest(getMainActivity());
    request.startCekSaldo(
        new CekSaldoRequest.RequestSaldoCallback() {
          @Override
          public void onStartLoading() {
            binding.shimmerLayout.setVisibility(View.VISIBLE);
            binding.shimmerLayout.startShimmer();
            binding.totalSaldo.setTextColor(Color.TRANSPARENT);
            binding.recyclerviewDashboard.setVisibility(View.GONE);
            binding.titleSaldo.setVisibility(View.GONE);
            binding.hideSaldo.setVisibility(View.GONE);
          }

          @Override
          public void onResponse(CekSaldoResponse saldo) {
            binding.shimmerLayout.stopShimmer();
            binding.shimmerLayout.setVisibility(View.GONE);
            binding.recyclerviewDashboard.setVisibility(View.VISIBLE);
            binding.titleSaldo.setVisibility(View.VISIBLE);
            binding.hideSaldo.setVisibility(View.VISIBLE);
            initRecyclerview();
            CekSaldoResponse.Saldo data = saldo.getSaldo();
            String balance = FormatUtils.formatRupiah(data.getBalance());
            binding.totalSaldo.setTextColor(Color.WHITE);

            // Simpan saldo asli ke SharedPreferences
            preferences.setSaldo(balance);

            // Tampilkan saldo berdasarkan status isHidenSaldo
            if (isHidenSaldo) {
              binding.totalSaldo.setText(convertToAsterisks(balance)); // Tampilkan *
            } else {
              binding.totalSaldo.setText(balance); // Tampilkan angka
            }

            binding.hideSaldo.setOnClickListener(
                v -> {
                  if (isHidenSaldo) {
                    // Tampilkan saldo asli
                    binding.hideSaldo.setImageResource(R.drawable.eye_on);
                    binding.totalSaldo.setText(balance);
                  } else {
                    // Sembunyikan saldo dengan *
                    binding.hideSaldo.setImageResource(R.drawable.ic_eye_off);
                    binding.totalSaldo.setText(convertToAsterisks(balance));
                  }
                  isHidenSaldo = !isHidenSaldo;
                  preferences.setHiddenSaldo(isHidenSaldo); // Simpan status ke preferences
                });
          }

          @Override
          public void onFailure(String errorMessage) {}
        });
  }

  @Override
  public void onDataReload() {
    loadSaldo();
  }

  private void initRecyclerview() {
    binding.recyclerviewDashboard.setLayoutManager(new GridLayoutManager(getMainActivity(), 3));
    binding.recyclerviewDashboard.showShimmerAdapter();
    list = new ArrayList<>();

    list.add(new MyAdapter.ItemModel(R.drawable.itm_topup, "Top Up"));
    list.add(new MyAdapter.ItemModel(R.drawable.dash_rekap, "Kasir"));
    list.add(new MyAdapter.ItemModel(R.drawable.itm_voucher, "Promo"));

    adapter = new MyAdapter(getActivity(), list);
    binding.recyclerviewDashboard.setAdapter(adapter);
    binding.recyclerviewDashboard.hideShimmerAdapter();
  }

  private String convertToAsterisks(String saldo) {
    if (saldo == null || saldo.isEmpty()) {
      return ""; // Jika saldo kosong, kembalikan string kosong
    }
    return saldo.replaceAll(".", "*"); // Ganti setiap karakter dengan *
  }

  public static class MyAdapter extends RecyclerView.Adapter<MyAdapter.DashboardVH> {

    private Context ctx;
    private List<ItemModel> mData = new ArrayList<>();

    public MyAdapter(Context context, List<ItemModel> list) {
      this.ctx = context;
      this.mData = list;
    }

    @Override
    public DashboardVH onCreateViewHolder(ViewGroup parent, int viewType) {
      ItemDashboardBinding binding =
          ItemDashboardBinding.inflate(LayoutInflater.from(ctx), parent, false);

      return new DashboardVH(binding);
    }

    @Override
    public void onBindViewHolder(DashboardVH holder, int position) {
      ItemModel model = mData.get(position);
      Context context = holder.card.getContext();

      // holder.card.setCardBackgroundColor(color);
      holder.image.setImageResource(model.getIconDashboard());
      holder.text.setText(model.getText());

      holder.itemView.setOnClickListener(
          v -> {
            Intent intent = null;
            switch (position) {
              case 0:
                intent = new Intent(context, ContentTopup.class);
                break;
              case 1:
                intent = new Intent(context, ContentKasir.class);
                break;
            }
            if (intent != null) context.startActivity(intent);
          });
    }

    @Override
    public int getItemCount() {
      return mData.size();
    }

    public class DashboardVH extends RecyclerView.ViewHolder {
      ImageView image;
      TextView text;
      MaterialCardView card;
      View itemView;

      public DashboardVH(ItemDashboardBinding binding) {
        super(binding.getRoot());
        itemView = binding.getRoot();
        image = binding.imgIconKategori;
        text = binding.itemTextIcon;
        card = binding.ch;
      }
    }

    public static class ItemModel {
      int image;
      String text;

      public ItemModel(int image, String text) {
        this.image = image;
        this.text = text;
      }

      public int getIconDashboard() {
        return image;
      }

      public String getText() {
        return text;
      }
    }
  }
}
