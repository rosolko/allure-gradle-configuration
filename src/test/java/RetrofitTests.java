import java.io.IOException;
import java.util.List;

import io.qameta.allure.okhttp3.AllureOkHttp3;
import okhttp3.OkHttpClient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author Aliaksandr Rasolka
 * @since 1.0.0
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("Retrofit tests")
class RetrofitTests {
    private GitHubService service;

    @BeforeAll
    void setUp() {
        final AllureOkHttp3 allureOkHttp3 = new AllureOkHttp3();
        final OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .addInterceptor(allureOkHttp3)
            .build();
        final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://api.github.com/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build();
        service = retrofit.create(GitHubService.class);
    }

    @Test
    @DisplayName("Able to get github repository list by name")
    void gitHubRepoTest() throws IOException {
        final Call<List<Repo>> reposCall = service.listRepos("octocat");
        final List<Repo> repos = reposCall.execute().body();
        Assertions.assertNotNull(repos);
    }
}
