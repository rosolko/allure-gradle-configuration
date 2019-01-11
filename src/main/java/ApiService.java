import retrofit2.Call;
import retrofit2.http.GET;

/**
 * @author Aliaksandr Rasolka
 * @since 1.0.0
 */
public interface ApiService {
    @GET("/owner")
    Call<User> owner();
}
