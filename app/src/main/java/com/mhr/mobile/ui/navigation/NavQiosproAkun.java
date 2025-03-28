package com.mhr.mobile.ui.navigation;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.firebase.auth.FirebaseAuth;
import com.mhr.mobile.R;
import com.mhr.mobile.adapter.AkunAdapter;
import com.mhr.mobile.databinding.NavQiosproAkunBinding;
import com.mhr.mobile.loader.QiosFirebaseHelper;
import com.mhr.mobile.model.Akun;
import com.mhr.mobile.ui.inject.InjectionFragment;
import com.mhr.mobile.ui.user.UserDevices;
import com.mhr.mobile.ui.user.UserSettingPin;
import com.mhr.mobile.ui.user.UserSettingProfile;
import com.mhr.mobile.util.UtilsManager;
import com.mikelau.shimmerrecyclerviewx.ShimmerRecyclerViewX;
import java.util.ArrayList;
import java.util.List;

public class NavQiosproAkun extends InjectionFragment {
  private NavQiosproAkunBinding binding;
  private ShimmerRecyclerViewX recyclerViewX;
  private AkunAdapter adapter;
  private List<Akun> mData = new ArrayList<>();

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Override
  public View onCreateQiosFragment(LayoutInflater inflater, ViewGroup viewGroup, Bundle bundle) {
    binding = NavQiosproAkunBinding.inflate(getLayoutInflater());
    initUi();
    return binding.getRoot();
  }

  private void initUi() {
    userAkun();
    recyclerViewX = binding.includeRecycler.recyclerview;
	binding.tvVersion.setText("Version V" + UtilsManager.getVersionApp(requireActivity()));
    applyRecycler();
  }

  private void applyRecycler() {
    recyclerViewX.setLayoutManager(getLinearLayoutManager());
    mData.add(new Akun(R.drawable.unlock, "Atur Pin Keamanan"));
    mData.add(new Akun(R.drawable.nav_pelanggan_outline, "Undang Downline"));
    mData.add(new Akun(R.drawable.itm_pulsa, "Perangkat Terhubung"));
    mData.add(new Akun(R.drawable.ic_ketentuan, "Syarat Ketentuan"));
    mData.add(new Akun(R.drawable.privacy_policy, "Kebijakan Privasi"));
    adapter = new AkunAdapter(mData);
    recyclerViewX.setAdapter(adapter);
    adapter.setOnClickListener(position -> onClickRecycler(position));
  }

  private void onClickRecycler(int position) {
    Intent intent = null;
    switch (position) {
      case 0:
        intent = new Intent(requireActivity(), UserSettingPin.class);
        break;
      case 2:
        intent = new Intent(requireActivity(), UserDevices.class);
        break;
    }
    if (intent != null) startActivity(intent);
  }

  private void userAkun() {
    FirebaseAuth auth = FirebaseAuth.getInstance();
    String username = auth.getCurrentUser().getDisplayName();
    String userEmail = auth.getCurrentUser().getEmail();
    binding.userDisplayName.setText(username);
    binding.userEmail.setText(userEmail);
    QiosFirebaseHelper logout = new QiosFirebaseHelper(requireActivity());

    binding.btnUserSetting.setOnClickListener(
        v -> {
          startActivity(new Intent(requireActivity(), UserSettingProfile.class));
        });

    binding.logout.setOnClickListener(
        v -> {
          logout.getLogout();
        });
  }

  @Override
  public void onDataReload() {
    super.onDataReload();
  }
}
