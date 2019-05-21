import java.io.IOException;

import io.qameta.allure.okhttp3.AllureOkHttp3;
import io.undertow.Undertow;
import io.undertow.server.RoutingHandler;
import io.undertow.util.Headers;
import io.undertow.util.Methods;
import okhttp3.OkHttpClient;
import org.junit.jupiter.api.AfterAll;
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
    private ApiService service;
    private Undertow server;

    @BeforeAll
    void setUpApiServer() {
        server = Undertow.builder()
                .addHttpListener(8080, "127.0.0.1")
                .setHandler(new RoutingHandler()
                        .add(Methods.GET, "/owner",
                                exchange -> {
                                    exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "application/json");
                                    exchange.getResponseSender().send("{\"name\":\"example\"}");
                                }))
                .build();
        server.start();
    }

    @AfterAll
    void tearDownApiServer() {
        server.stop();
    }

    @BeforeAll
    void setUpApiClient() {
        final AllureOkHttp3 allureOkHttp3 = new AllureOkHttp3();
        final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(allureOkHttp3)
                .build();
        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://127.0.0.1:8080/")
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = retrofit.create(ApiService.class);
    }

    @Test
    @DisplayName("Able to get owner using api")
    void apiTest() throws IOException {
        final Call<User> ownerCall = service.owner();
        final User owner = ownerCall.execute().body();
        Assertions.assertNotNull(owner);
        Assertions.assertEquals("example", owner.name);
    }
}
