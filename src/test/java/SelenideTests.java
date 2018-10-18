import com.codeborne.selenide.SelenideConfig;
import com.codeborne.selenide.SelenideDriver;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

/**
 * @author Aliaksandr Rasolka
 * @since 1.0.0
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("Selenide tests")
class SelenideTests {
    private SelenideDriver driver;

    @BeforeAll
    void setUp() {
        driver = new SelenideDriver(new SelenideConfig().headless(true));
        SelenideLogger.addListener("allure", new AllureSelenide().savePageSource(false));
    }

    @AfterAll
    void tearDown() {
        SelenideLogger.removeListener("allure");
    }

    @Test
    @DisplayName("Able to open web application")
    void openTest() {
        driver.open("https://www.google.com");
        Assertions.assertEquals(driver.title(), "Google");
    }
}
