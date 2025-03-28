package com.mhr.mobile.ui.intro;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface WhatsappService {

  @Headers({
    "Authorization: Bearer EAASPlcBRGM4BO3OCWVUf4IBJIDB5ZAsQa02rgeAUFHEwuRrZB0wZCyeuPo9jCmm3saKyMSFZCrWVs6qN64Y6wF2NZB8AhxBkv0qkAmV2brlT6kIL3XZA1jvf3ITSkr4qfezwZCUB9xWS5bw2Qq6TNQD8WUvvQ6UPkrhFqevEvvQE5FXwEvZBomGmQZCs2LFZAJDMC2psVZAhb3ZBx2aHN2TzJBIZD", // Ganti dengan token akses Anda
    "Content-Type: application/json"
  })
  @POST("v21.0/{phone_number_id}/messages")
  Call<Void> sendOtp(@Path("phone_number_id") String phoneNumberId, @Body WhatsappMessage body);
}
