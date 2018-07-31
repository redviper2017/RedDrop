package app.clairvoyant.reddrop;

import app.clairvoyant.reddrop.model.Location;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GetLocationDataService {
    @GET("json")
    Call<Location> getLocationData(@Query("address") String donationAddress);
}
