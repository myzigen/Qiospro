package com.mhr.mobile.ui.inject;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.mhr.mobile.MainActivity;
import com.mhr.mobile.databinding.InjectionFragmentBinding;
import com.mhr.mobile.interfaces.ConnectivityListener;
import com.mhr.mobile.widget.recyclerview.SpacingItemDecoration;

public abstract class InjectionFragment extends Fragment implements ConnectivityListener {
  private InjectionFragmentBinding binding;
  private ViewGroup fragmentContainer;
  private InjectionConnectivity injectionConnectivity;
  private SpacingItemDecoration spacingItemDecoration;
  private LinearLayoutManager linearLayoutManager;
  private GridLayoutManager gridLayoutManager;
  private View applyPay;

  public MainActivity getMainActivity() {
    return (MainActivity) getActivity();
  }

  @Override
  public void onAttach(Context context) {
    super.onAttach(context);
    // Pastikan konteks aktivitas adalah `InjectionConnectivity`
    if (context instanceof InjectionConnectivity) {
      injectionConnectivity = (InjectionConnectivity) context;
    }
  }

  @Override
  public void onDetach() {
    super.onDetach();
    injectionConnectivity = null;
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setHasOptionsMenu(true); // Agar bisa menampilkan menu di fragment
  }

  @Override
  public View onCreateView(LayoutInflater i, ViewGroup v, Bundle b) {
    binding = InjectionFragmentBinding.inflate(i, v, false);
    View contentFragment = onCreateQiosFragment(i, v, b);
    binding.fragmentContainer.addView(contentFragment);
	applyPay = binding.pay.containerBottom;
    applyIncludePembayaran(applyPay);
    return binding.getRoot();
  }

  @Override
  public void onViewCreated(View arg0, Bundle arg1) {
    super.onViewCreated(arg0, arg1);
  }

  protected abstract View onCreateQiosFragment(LayoutInflater i, ViewGroup v, Bundle b);

  protected void applyIncludePembayaran(View includeView) {}

  protected SpacingItemDecoration getSpacingDecoration(int column, int count, boolean spacing) {
    return spacingItemDecoration = new SpacingItemDecoration(column, count, spacing);
  }

  protected LinearLayoutManager getLinearLayoutManager() {
    return linearLayoutManager = new LinearLayoutManager(getActivity());
  }

  protected GridLayoutManager getGridLayoutManager(int columnt) {
    return gridLayoutManager = new GridLayoutManager(getActivity(), columnt);
  }

  @Override
  public void onNetworkConnected(boolean isConnected) {
    // Menangani status koneksi di sini
    if (isConnected) {
      onDataReload(); // Memanggil data reload jika terhubung ke jaringan
    } else {
      // Bisa menangani ketika tidak ada koneksi di sini
    }
  }

  @Override
  public void onDataReload() {
    // Override di fragment yang membutuhkan data reload
  }
}
