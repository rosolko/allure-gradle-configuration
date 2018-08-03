import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

/**
 * @author Aliaksandr Rasolka
 * @since 1.0.0
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class SelenideTests {
    @BeforeAll
    void setUp() {
        SelenideLogger.addListener("allure", new AllureSelenide().savePageSource(false));
    }

    @AfterAll
    void tearDown() {
        SelenideLogger.removeListener("allure");
    }

    @Test
    void openTest() {
        Selenide.open("https://www.google.com");
        Assertions.assertEquals(Selenide.title(), "Google");
    }
}
