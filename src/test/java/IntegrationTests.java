import com.codeborne.selenide.SelenideConfig;
import com.codeborne.selenide.SelenideDriver;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.javalin.Javalin;
import io.qameta.allure.okhttp3.AllureOkHttp3;
import io.qameta.allure.selenide.AllureSelenide;
import okhttp3.OkHttpClient;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;

/**
 * @author Aliaksandr Rasolka
 * @since 1.0.0
 */
@DisplayName("Integration tests")
class IntegrationTests {
    private static Javalin server;

    @BeforeAll
    static void setUpApiServer() {
        server = Javalin.create();
        server.get("/", ctx -> ctx.html("<!DOCTYPE html><html><head><title>example</title></head><body>Content</body></html>"));
        server.get("/owner", ctx -> ctx.json(new User("example")));
        server.start(8080);
    }

    @AfterAll
    static void tearDownWebServer() {
        server.stop();
    }

    @Nested
    @DisplayName("Retrofit")
    class RetrofitTests {
        private ApiService service;

        @BeforeEach
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

    @Nested
    @DisplayName("Selenide")
    class SelenideTests {
        private SelenideDriver driver;

        @BeforeEach
        void setUpSelenide() {
            driver = new SelenideDriver(new SelenideConfig().headless(true));
            SelenideLogger.addListener("allure", new AllureSelenide().savePageSource(false));
        }

        @AfterEach
        void tearDownSelenide() {
            SelenideLogger.removeListener("allure");
        }

        @Test
        @DisplayName("Able to open web application")
        void openTest() {
            driver.open("http://127.0.0.1:8080/");
            Assertions.assertEquals("example", driver.title());
        }
    }
}
