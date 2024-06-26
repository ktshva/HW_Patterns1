package ru.netology;

import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;
import org.openqa.selenium.Keys;
import ru.netology.DataGenerator;

import java.time.Duration;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;

class ReplanCardDeliveryTest {

    @BeforeAll
    static void setUpAll() {
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @AfterAll
    static void tearDownAll() {
        SelenideLogger.removeListener("allure");
    }
    @BeforeEach
    void setup() {
        open("http://localhost:9999");
    }

    @Test
    @DisplayName("Should successful plan and replan meeting")
    void shouldSuccessfulPlanAndReplanMeeting() {
        var validUser = DataGenerator.Registration.generateUser("ru");
        var daysToAddForFirstMeeting = 4;
        var firstMeetingDate = DataGenerator.generateDate(daysToAddForFirstMeeting);
        var daysToAddForSecondMeeting = 7;
        var secondMeetingDate = DataGenerator.generateDate(daysToAddForSecondMeeting);

        $("[data-test-id=date] input").sendKeys(Keys.CONTROL + "a", Keys.BACK_SPACE);
        $("[data-test-id=city] input").setValue(validUser.getCity());
        $("[data-test-id=date] input").setValue(firstMeetingDate);
        $("[data-test-id=name] input").setValue(validUser.getName());
        $("[data-test-id=phone] input").setValue(validUser.getPhone());
        $("[data-test-id=agreement].checkbox").click();
        $("button.button ").click();
        $("[data-test-id=success-notification] .notification__content")
                .shouldBe(visible, Duration.ofSeconds(15))
                .shouldHave(matchText(firstMeetingDate));

        $("[data-test-id=date] input").sendKeys(Keys.CONTROL + "a", Keys.BACK_SPACE);

        $("[data-test-id=date] input").setValue(secondMeetingDate);
        $("button.button ").click();
        $("[data-test-id=replan-notification] .notification__content")
                .shouldBe(visible, Duration.ofSeconds(15))
                .shouldHave(matchText("Перепланировать?"));
        $$("button").find(exactText("Перепланировать")).click();
        $("[data-test-id=success-notification] .notification__content")
                .shouldBe(visible, Duration.ofSeconds(15))
                .shouldHave(matchText(secondMeetingDate));
    }
}
