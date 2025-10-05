import org.junit.jupiter.api.Test;
import com.codeborne.selenide.Configuration;
import org.openqa.selenium.chrome.ChromeOptions;
import java.time.Duration;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;

public class SearchTests {

    static {
        Configuration.browser = "chrome";
        Configuration.headless = false;
        Configuration.timeout = 30000;
        Configuration.browserSize = "1920x1080";
        Configuration.pageLoadStrategy = "eager";

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-extensions");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--remote-allow-origins=*");
        // Дополнительные настройки для стабильности
        options.addArguments("--disable-gpu");
        options.addArguments("--disable-software-rasterizer");
        Configuration.browserCapabilities = options;
    }

    @Test
    void directYahooSearch() {
        try {
            System.out.println("🚀 Запуск поиска через Yahoo...");

            // Прямой URL с поисковым запросом
            open("https://search.yahoo.com/search?p=selenide+testing+framework");

            // Ждем загрузки тела страницы
            $("body").shouldBe(visible, Duration.ofSeconds(25));
            System.out.println("✅ Страница загружена");

            // Проверяем наличие результатов разными способами
            checkSearchResults();

            System.out.println("✅ Поиск Yahoo успешен!");
            sleep(2000);
            screenshot("direct_yahoo_search_" + System.currentTimeMillis());

        } catch (Exception e) {
            System.out.println("❌ Ошибка: " + e.getMessage());
            screenshot("error_yahoo_search_" + System.currentTimeMillis());
            throw e;
        }
    }

    private void checkSearchResults() {
        // Способ 1: Проверка по тексту в body
        $("body").shouldHave(text("selenide"), Duration.ofSeconds(10));
        System.out.println("✅ Найдено 'selenide' на странице");

        // Способ 2: Проверка специфичных элементов Yahoo
        String[] yahooSelectors = {
                "#web",
                ".searchCenterMiddle",
                ".compTitle",
                ".algo"
        };

        for (String selector : yahooSelectors) {
            if ($(selector).exists()) {
                System.out.println("✅ Найден элемент Yahoo: " + selector);
                break;
            }
        }

        // Способ 3: Проверка что страница содержит достаточно контента
        String pageText = $("body").getText();
        if (pageText.length() < 500) {
            throw new RuntimeException("Страница содержит слишком мало контента: " + pageText.length() + " символов");
        }
        System.out.println("✅ Страница содержит достаточное количество текста: " + pageText.length() + " символов");
    }
}
