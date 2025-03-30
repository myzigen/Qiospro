package com.mhr.mobile;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mhr.mobile.databinding.ActivityMainBinding;
import com.mhr.mobile.ui.content.ContentNotification;
import com.mhr.mobile.ui.dialog.PopupDialog;
import com.mhr.mobile.ui.navigation.BottomNavPagerAdapter;
import com.mhr.mobile.widget.viewpager.GestureControlViewPager;
import com.onesignal.OneSignal;
import com.onesignal.inAppMessages.IInAppMessageClickEvent;
import com.onesignal.inAppMessages.IInAppMessageClickListener;

public class MainActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener {
  private static final String APP_ID = "0d416693-a7ec-4aa7-b598-a7600bd45732";
  private ActivityMainBinding binding;
  private BottomNavPagerAdapter adapter;
  private GestureControlViewPager viewPager;
  private BottomNavigationView bottomNavigationView;
  private MenuItem prevMenuItem;
  private FirebaseAuth mAuth;
  private DatabaseReference usersRef;
  private String userId;
  private long confirmBackPress;
  private Toast toast;
  // Variabel izin
  private ActivityResultLauncher<String> requestPermissionLauncher;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    binding = ActivityMainBinding.inflate(getLayoutInflater());
    setContentView(binding.getRoot());

    // Inisialisasi izin
    requestPermissionLauncher =
        registerForActivityResult(
            new ActivityResultContracts.RequestPermission(),
            isGranted -> {
              if (!isGranted) {
                showPermissionDeniedDialog();
              } else {
                recreate();
              }
            });

    checkGalleryPermission();

    PopupDialog popupDialog = new PopupDialog();
    popupDialog.show(getSupportFragmentManager(), "TAG");

    viewPager = binding.viewpager;
    bottomNavigationView = binding.bottomNavigationMenu;
    // toolbar.setVisibility(View.GONE);
    adapter = new BottomNavPagerAdapter(this, getSupportFragmentManager());
    viewPager.isSwipeGestureEnabled = false;
    viewPager.setOffscreenPageLimit(4);
    viewPager.setAdapter(adapter);
    bottomNavigationView.setItemIconTintList(null);
    bottomNavigationView.setOnNavigationItemSelectedListener(navigationSelectedListener);
    viewPager.addOnPageChangeListener(this);
    hideToltipBottomNav();

    //
    mAuth = FirebaseAuth.getInstance();
    userId = mAuth.getCurrentUser().getUid();
    usersRef = FirebaseDatabase.getInstance().getReference("users");
    updateUserStatusToOnline();
    detectActiveUsers();

    // OneSignal.initWithContext(this, APP_ID);
    // OneSignal.getNotifications().requestPermission(false, Continue.none());
    OneSignal.getInAppMessages()
        .addClickListener(
            new IInAppMessageClickListener() {
              @Override
              public void onClick(@Nullable IInAppMessageClickEvent event) {
                // Log.v(Tag.LOG_TAG, "INotificationClickListener.inAppMessageClicked");
                // startActivity(new Intent(MainActivity.this, ContentNotification.class));
              }
            });

    OneSignal.getNotifications()
        .addClickListener(
            event -> {
              // Log.v(Tag.LOG_TAG,"INotificationClickListener.onClick fired" + " with event: " +
              // event);
              startActivity(new Intent(MainActivity.this, ContentNotification.class));
            });
  }

  @Override
  public void onPageScrolled(int arg0, float arg1, int arg2) {}

  @Override
  public void onPageSelected(int position) {
    if (bottomNavigationView != null) {
      if (prevMenuItem != null) {
        prevMenuItem.setChecked(false); // Menonaktifkan item sebelumnya
      } else {
        bottomNavigationView
            .getMenu()
            .getItem(0)
            .setChecked(false); // Menonaktifkan item pertama jika belum ada item sebelumnya
      }
      bottomNavigationView
          .getMenu()
          .getItem(position)
          .setChecked(true); // Menandai item yang dipilih
      prevMenuItem = bottomNavigationView.getMenu().getItem(position); // Menyimpan item yang aktif
    }
  }

  @Override
  public void onPageScrollStateChanged(int arg0) {}

  private BottomNavigationView.OnNavigationItemSelectedListener navigationSelectedListener =
      item -> {
        switch (item.getItemId()) {
          case R.id.nav_home:
            if (viewPager.getCurrentItem() != 0) {
              viewPager.setCurrentItem(0);
            }
            return true;
          case R.id.nav_transaksi:
            if (viewPager.getCurrentItem() != 1) {
              viewPager.setCurrentItem(1);
            }
            return true;
          case R.id.nav_promosi:
            if (viewPager.getCurrentItem() != 2) {
              viewPager.setCurrentItem(2);
            }
            return true;
          case R.id.nav_account:
            if (viewPager.getCurrentItem() != 3) {
              viewPager.setCurrentItem(3);
            }
            return true;
        }
        return false;
      };

  private void hideToltipBottomNav() {
    for (int i = 0; i < bottomNavigationView.getMenu().size(); i++) {
      bottomNavigationView
          .findViewById(bottomNavigationView.getMenu().getItem(i).getItemId())
          .setOnLongClickListener(
              new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                  return true;
                }
              });
    }
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    updateUserStatusToOffline();
  }

  private void detectActiveUsers() {
    usersRef
        .orderByChild("status")
        .equalTo("online")
        .addValueEventListener(
            new ValueEventListener() {
              @Override
              public void onDataChange(DataSnapshot dataSnapshot) {
                // Cek apakah ada pengguna dengan status online
                if (dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0) {
                  // Ada pengguna yang sedang aktif
                  // Anda bisa memberikan notifikasi atau melakukan tindakan lainnya
                  Log.d("MainActivity", "Ada pengguna yang aktif.");
                } else {
                  // Tidak ada pengguna yang aktif
                  Log.d("MainActivity", "Tidak ada pengguna yang aktif.");
                }
              }

              @Override
              public void onCancelled(DatabaseError databaseError) {
                Log.e(
                    "MainActivity",
                    "Error mendeteksi pengguna aktif: " + databaseError.getMessage());
              }
            });
  }

  private void updateUserStatusToOnline() {
    DatabaseReference userRef = usersRef.child(userId).child("status");
    userRef.setValue("online");

    // Set status offline saat koneksi hilang atau aplikasi ditutup
    userRef.onDisconnect().setValue("offline");
  }

  private void updateUserStatusToOffline() {
    DatabaseReference userRef = usersRef.child(userId).child("status");
    userRef.setValue("offline");
  }

  @Override
  public void onBackPressed() {
    if (confirmBackPress + 2000 > System.currentTimeMillis()) {
      if (toast != null) toast.cancel();
      super.onBackPressed();
      return;
    } else {
      toast = Toast.makeText(this, "Tekan sekali lagi untuk keluar", Toast.LENGTH_SHORT);
      toast.show();
    }
    confirmBackPress = System.currentTimeMillis();
  }

  // Cek izin galeri
  private void checkGalleryPermission() {
    String permission =
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU
            ? Manifest.permission.READ_MEDIA_IMAGES
            : Manifest.permission.READ_EXTERNAL_STORAGE;

    if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
      requestPermissionLauncher.launch(permission);
    }
  }

  // Tampilkan dialog jika izin ditolak
  private void showPermissionDeniedDialog() {
    new AlertDialog.Builder(this)
        .setTitle("Izin Diperlukan")
        .setMessage("Aplikasi ini memerlukan izin " + " melanjutkan.")
        .setPositiveButton(
            "OK",
            new DialogInterface.OnClickListener() {
              @Override
              public void onClick(DialogInterface dialog, int which) {
                checkGalleryPermission();
              }
            })
        .setCancelable(false)
        .show();
  }
}

/*

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;

// Variabel izin
private ActivityResultLauncher<String> requestPermissionLauncher;

@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    binding = ActivityMainBinding.inflate(getLayoutInflater());
    setContentView(binding.getRoot());

    // Inisialisasi izin
    requestPermissionLauncher = registerForActivityResult(
        new ActivityResultContracts.RequestPermission(),
        isGranted -> {
            if (!isGranted) {
                showPermissionDeniedDialog();
            }
        });

    checkGalleryPermission();
}

// Cek izin galeri
private void checkGalleryPermission() {
    String permission = Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU
        ? Manifest.permission.READ_MEDIA_IMAGES
        : Manifest.permission.READ_EXTERNAL_STORAGE;

    if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
        requestPermissionLauncher.launch(permission);
    }
}

// Tampilkan dialog jika izin ditolak
private void showPermissionDeniedDialog() {
    new AlertDialog.Builder(this)
        .setTitle("Izin Diperlukan")
        .setMessage("Aplikasi ini memerlukan izin untuk mengakses galeri. Harap izinkan akses untuk melanjutkan.")
        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                checkGalleryPermission();
            }
        })
        .setCancelable(false)
        .show();
}


//
@Override
public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.toolbar_menu, menu);

    // Ambil item dari menu untuk menambahkan badge
    MenuItem menuItem = menu.findItem(R.id.action_notifications);
    View actionView = menuItem.getActionView();

    TextView badge = actionView.findViewById(R.id.badge_text_view);
    int notificationCount = getNotificationCount();  // Ambil jumlah notifikasi dari SharedPreferences

    // Perbarui badge
    if (notificationCount > 0) {
        badge.setText(String.valueOf(notificationCount));
        badge.setVisibility(View.VISIBLE);
    } else {
        badge.setVisibility(View.GONE);
    }

    return true;
}

private int getNotificationCount() {
    SharedPreferences prefs = getSharedPreferences("notifications", Context.MODE_PRIVATE);
    return prefs.getInt("notification_count", 0);
}
*/
