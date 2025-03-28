package com.mhr.mobile.ui.navigation;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.lifecycle.ViewModelProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mhr.mobile.R;
import com.mhr.mobile.adapter.TransaksiIAKStatusAdapter;
import com.mhr.mobile.databinding.NavQiosproTransaksiBinding;
import com.mhr.mobile.loader.QiosFirebaseHelper;
import com.mhr.mobile.manage.response.TransaksiIAKResponse;
import com.mhr.mobile.manage.response.TransaksiIAKResponse.Data;
import com.mhr.mobile.manage.response.WebhookResponse;
import com.mhr.mobile.model.RiwayatTransaksi;
import com.mhr.mobile.ui.content.ContentPaymentStatus;
import com.mhr.mobile.ui.content.ContentTopupStatus;
import com.mhr.mobile.ui.inject.InjectionFragment;
import com.mhr.mobile.ui.sheet.SheetFilterHistory;
import com.mhr.mobile.util.AndroidViews;
import com.mhr.mobile.viewmodel.HomeViewModel;
import java.util.ArrayList;
import java.util.List;

public class NavQiosproTransaksi extends InjectionFragment {
  private NavQiosproTransaksiBinding binding;
  private TransaksiIAKStatusAdapter adapter;
  private TransaksiIAKResponse.Data mDataStatus;
  private DatabaseReference db, dbStatus;
  private FirebaseAuth auth;
  private HomeViewModel viewModel;

  @Override
  public View onCreateQiosFragment(LayoutInflater inflater, ViewGroup viewGroup, Bundle bundle) {
    binding = NavQiosproTransaksiBinding.inflate(getLayoutInflater());
    initUi();
    return binding.getRoot();
  }

  private void initUi() {
    viewModel = new ViewModelProvider(this).get(HomeViewModel.class);
    auth = FirebaseAuth.getInstance();
    getHistoryTransaksi();
    refreshUi();
    applyRecycler();
    binding.toolbar.setOnMenuItemClickListener(
        item -> {
          switch (item.getItemId()) {
            case R.id.action_filter:
              SheetFilterHistory sheet = new SheetFilterHistory();
              sheet.show(getChildFragmentManager(), "TAG");
              break;
          }

          return false;
        });
  }

  private void refreshUi() {
    binding.refresh.setOnRefreshListener(
        () -> {
          viewModel.setIsRefreshing(true);
          onDataReload();
          binding.refresh.postDelayed(() -> viewModel.setIsRefreshing(false), 1000);
        });

    viewModel
        .getIsRefreshing()
        .observe(
            getViewLifecycleOwner(), isRefreshing -> binding.refresh.setRefreshing(isRefreshing));
  }

  private void applyRecycler() {
    binding.recyclerview.addItemDecoration(getSpacingDecoration(1, 50, true));
    binding.recyclerview.setLayoutManager(getLinearLayoutManager());
    adapter = new TransaksiIAKStatusAdapter(new ArrayList<>());
    binding.recyclerview.setAdapter(adapter);
    adapter.setOnProdukClickListener(
        model -> {
          Bundle args = new Bundle();
          Intent intent = null;
          if ("apiIak".equalsIgnoreCase(model.getTypeApi())) {
            args.putParcelable("kirim", model);
            intent = new Intent(requireActivity(), ContentPaymentStatus.class);
            intent.putExtras(args);
          } else if ("apiDigiflazz".equalsIgnoreCase(model.getTypeApi())) {
            args.putParcelable("kirim", model);
            intent = new Intent(requireActivity(), ContentPaymentStatus.class);
            intent.putExtras(args);
          } else if ("apiMidtrans".equalsIgnoreCase(model.getTypeApi())) {
            args.putParcelable("riwayat_topup", model);
            intent = new Intent(requireActivity(), ContentTopupStatus.class);
            intent.putExtras(args);
          }
          if (intent != null) startActivity(intent);
        });
    adapter.setOnProdukDeleteListener(
        new TransaksiIAKStatusAdapter.OnProdukDeleteListener() {
          @Override
          public void onProdukMore(Data data) {}

          @Override
          public void onProdukDelete(String refId) {
            deleteTransaksi(refId);
          }
        });
  }

  private void getHistoryTransaksi() {
    QiosFirebaseHelper firebaseHelper = new QiosFirebaseHelper("history_transaksi");
    firebaseHelper.getDataTransaksi(
        new QiosFirebaseHelper.FirebaseCallback() {
          @Override
          public void onStartRequest() {
            binding.recyclerview.showShimmerAdapter();
          }

          @Override
          public void onDataChanged(List<RiwayatTransaksi> list) {
            binding.recyclerview.hideShimmerAdapter();
            if (list.isEmpty()) {
              binding.tvNoProduk.setVisibility(View.VISIBLE);
            } else {
              binding.tvNoProduk.setVisibility(View.GONE);
              adapter.updateData(list);
            }
          }

          @Override
          public void onUpdateStatus(String refId) {
            checkStatusTransaksi(refId);
          }
        });
  }

  private void checkStatusTransaksi(String refId) {
    dbStatus = FirebaseDatabase.getInstance().getReference("status_transaksi").child(refId);
    dbStatus.addValueEventListener(
        new ValueEventListener() {
          @Override
          public void onDataChange(DataSnapshot snapshot) {

            WebhookResponse.Data statusTransaksi = snapshot.getValue(WebhookResponse.Data.class);
            if (statusTransaksi != null) {
              String message = statusTransaksi.getMessage();
              if (message.isEmpty()) {
                adapter.updateStatus(refId, "PROSES", statusTransaksi.getStatus());
              } else {
                adapter.updateStatus(refId, message, statusTransaksi.getStatus());
              }
            } else {
              adapter.updateStatus(refId, "PROSES", 0);
            }
          }

          @Override
          public void onCancelled(DatabaseError error) {
            AndroidViews.showSnackbar(binding.getRoot(), error.getDetails());
          }
        });
  }

  private void deleteTransaksi(String refId) {
    String userName =
        auth.getCurrentUser() != null
            ? auth.getCurrentUser().getDisplayName()
            : auth.getCurrentUser().getUid();

    DatabaseReference transaksiRef =
        FirebaseDatabase.getInstance()
            .getReference("history_transaksi")
            .child(userName)
            .child(refId);

    transaksiRef
        .removeValue()
        .addOnCompleteListener(
            task -> {
              if (task.isSuccessful()) {
                adapter.removeItem(refId);
              } else {
                // AndroidViews.showToast("Gagal menghapus history" + task.getException(),
                // activity);
              }
            });
  }

  @Override
  public void onDataReload() {
    getHistoryTransaksi();
  }
}
