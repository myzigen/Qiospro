package com.mhr.mobile.ui.menu;


public class MenuBpjs /*extends InjectionActivity implements TextWatcher*/ {
  /*
  private MenuBpjsBinding binding;
  private ListPascaAdapter adapter;
  private String noPelanggan;
  private InquiryResponse.Data mData;
  private String selectedCodeProduk = null;
  private String mImageUrl;

  @Override
  protected String getTitleToolbar() {
    return "BPJS";
  }

  @Override
  public View onCreateQiosView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
    binding = MenuBpjsBinding.inflate(layoutInflater, viewGroup, false);

    initUI();
    showBottomSheet();
    return binding.getRoot();
  }

  private void initUI() {

    // Inisialisasi text watcher untuk EditText
    binding.editText.addTextChangedListener(this);
    binding.editNominal.addTextChangedListener(this); // Menambahkan watcher untuk editNominal

    // Inisialisasi tombol Cek Tagihan
    binding.btnCekTagihan.setText("Cek Pengguna");
    binding.btnCekTagihan.setEnabled(false);
    binding.btnCekTagihan.setOnClickListener(
        v -> {
          if (mData != null && noPelanggan.equals(mData.getHp())) {
            openDetailTagihan(); // Data pengguna valid, buka detail tagihan
          } else {
            checkUser(binding.editText.getText().toString().trim()); // Lakukan validasi ulang
          }
        });
  }

  @Override
  public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

  @Override
  public void onTextChanged(CharSequence s, int start, int before, int count) {}

  @Override
  public void afterTextChanged(Editable s) {
    noPelanggan = binding.editText.getText().toString().trim();
    String nominalText = binding.editNominal.getText().toString().trim();
    validateForm(noPelanggan, nominalText); // Validasi menggunakan nomor pelanggan dan nominal
  }

  private void validateForm(String noPelanggan, String nominalText) {
    // Ambil provider yang dipilih
    String provider = binding.txtLabelProvider.getText().toString().trim();

    // Tombol dinonaktifkan jika nomor pelanggan kosong, kode produk belum dipilih, atau nominal
    // tidak valid
    if (noPelanggan.isEmpty()
        || selectedCodeProduk == null
        || nominalText.isEmpty()
        || Integer.parseInt(nominalText) <= 0) {
      binding.btnCekTagihan.setEnabled(false);
    }
    // Tombol aktif hanya jika nomor pelanggan sudah tepat 12 angka, kode produk sudah dipilih, dan
    // nominal valid
    else if (noPelanggan.length() < 6
        || selectedCodeProduk == null
        || Integer.parseInt(nominalText) <= 0) {
      binding.btnCekTagihan.setEnabled(false);
    }

    // Tombol diaktifkan untuk cek pengguna, tetapi hanya jika data pengguna belum ditemukan
    else {
      binding.btnCekTagihan.setText("Cek Pengguna");
      binding.btnCekTagihan.setEnabled(true);
    }
  }

  private void checkUser(String noPelanggan) {
    if (binding.expandable.isExpanded()) {
      binding.expandable.collapse();
      showSuccess();
    }

    // Ambil nilai nominal dari EditText
    int nominal = Integer.parseInt(binding.editNominal.getText().toString().trim());

    InquiryRequest request = new InquiryRequest(this);
    request.setUsername();
    request.setApiKey();
    request.setCodeProduk(selectedCodeProduk);
    request.setHp(noPelanggan);
    request.setAmount(nominal);
    request.startInquiryRequest(
        new InquiryRequest.InquiryCallback() {
          @Override
          public void onStartLoading() {
            // dialogFragment.show(getSupportFragmentManager(), "loading");
            binding.expandable.expand();
          }

          @Override
          public void onResponse(InquiryResponse response) {
            // dialogFragment.dismiss();
            InquiryResponse.Data data = response.getData();

            if (data != null && data.getTrName() != null && !data.getTrName().isEmpty()) {
              // Nama pengguna ditemukan
              mData = data;
              binding.txtCekPengguna.setText(mData.getTrName());
              binding.btnCekTagihan.setText("Lanjut Bayar Tagihan"); // Ubah teks tombol
              validateForm(noPelanggan, binding.editNominal.getText().toString().trim()); // Validasi ulang
            } else {
              // Nama pengguna tidak ditemukan
              mData = null;
              showError();
              String message = data != null ? data.getMessage() : "Nama pengguna tidak ditemukan";
              binding.txtCekPengguna.setText(message);
              binding.btnCekTagihan.setText("Cek Pengguna"); // Kembalikan teks tombol ke awal
              binding.btnCekTagihan.setEnabled(false); // Nonaktifkan tombol
            }
          }

          @Override
          public void onFailure(String errorMessage) {
            mData = null;
            showError();
            binding.txtCekPengguna.setText("Terjadi kesalahan: " + errorMessage);
            binding.btnCekTagihan.setText("Cek Pengguna"); // Kembalikan teks tombol ke awal
            binding.btnCekTagihan.setEnabled(false); // Nonaktifkan tombol
          }
        });
  }

  private void openDetailTagihan() {
    if (mData != null && noPelanggan.equals(mData.getHp())) {
      Intent intent = new Intent(MenuBpjs.this, DetailTagihanInternet.class);
      intent.putExtra("data", mData); // Kirim objek Data yang sudah Parcelable
      intent.putExtra("imgUrl", mImageUrl);
      startActivity(intent);
    } else {
      binding.txtCekPengguna.setText("Data pengguna tidak valid.");
    }
  }

  private void showSuccess() {
    binding.expandable.setBackgroundColor(Color.parseColor("#ECFEF4"));
    binding.txtCekPengguna.setTextColor(Color.parseColor("#00AA5B"));
    binding.txtCekPengguna.setText("Mencari pengguna...");
  }

  private void showError() {
    binding.expandable.setBackgroundColor(Color.parseColor("#FFF5F6"));
    binding.txtCekPengguna.setTextColor(QiosColor.getErrorColor(this));
  }

  private void showBottomSheet() {
    binding.provider.setOnClickListener(
        v -> {
          BottomSheetBpjs sheetWifi = new BottomSheetBpjs();
          sheetWifi.setOnProdukItemSelected(
              (providerName) -> {
                updateProvider(providerName);
              });
          sheetWifi.show(getSupportFragmentManager(), "TAG");
        });
  }

  private void updateProvider(String providerName) {
    binding.txtLabelProvider.setText(providerName);
    selectedCodeProduk = providerName;
    String nomorPelanggan = binding.editText.getText().toString().trim();
    validateForm(nomorPelanggan, binding.editNominal.getText().toString().trim());


  }
  */
}
