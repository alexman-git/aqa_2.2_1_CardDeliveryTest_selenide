package ru.netology.web;

import com.codeborne.selenide.Condition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static org.openqa.selenium.Keys.BACK_SPACE;

public class PositiveTests {

    @BeforeEach
    public void setupTest() {
        open("http://localhost:9999");
    }

    String bookDate(int days) {
        return LocalDate.now().plusDays(days).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
    }

    String meetingDateNearest = bookDate(3);

    @Test
    public void HappyPathTest() {
        $("[data-test-id='city'] input").setValue("Ростов-на-Дону");
        $("[data-test-id='date'] input").doubleClick().sendKeys(BACK_SPACE);
        $("[data-test-id='date'] input").setValue(meetingDateNearest);
        $("[data-test-id='name'] input").setValue("Иван Иванов");
        $("[data-test-id='phone'] input").setValue("+71234567891");
        $("[data-test-id='agreement'] .checkbox__box").click();
        $(byText("Забронировать")).click();
        $("[data-test-id='notification'] .notification__title")
                .shouldBe(Condition.appear, Duration.ofSeconds(15))
                .shouldHave((text("Успешно!")));
        $("[data-test-id='notification'] .notification__content")
                .shouldBe(Condition.visible)
                .shouldHave(text("Встреча успешно забронирована на " + meetingDateNearest));
    }

    @ParameterizedTest
    @CsvSource(value = {"regularCyrillicCityNameRuRegCenter,Москва",
            "cyrillicCityNameWithHyphenRuRegCenter,Петропавловск-Камчатский",
            "cyrillicCityNameWithHyphenRuRegCenterUpperCase,ПЕТРОПАВЛОВСК-КАМЧАТСКИЙ",
            "cyrillicCityNameWithHyphenRuRegCenterLowerCase,москва",
            "cyrillicCityNameWithHyphenRuRegCenterWithCaseErrors,мОсква"})
    public void shouldSendFormWithValidCityName(String testcase, String cityName) {
        $("[data-test-id='city'] input").setValue(cityName);
        $("[data-test-id='date'] input").doubleClick().sendKeys(BACK_SPACE);
        $("[data-test-id='date'] input").setValue(meetingDateNearest);
        $("[data-test-id='name'] input").setValue("Иван Иванов");
        $("[data-test-id='phone'] input").setValue("+71234567891");
        $("[data-test-id='agreement'] .checkbox__box").click();
        $(byText("Забронировать")).click();
        $("[data-test-id='notification'] .notification__title")
                .shouldBe(Condition.appear, Duration.ofSeconds(15))
                .shouldHave((text("Успешно!")));
        $("[data-test-id='notification'] .notification__content")
                .shouldBe(Condition.visible)
                .shouldHave(text("Встреча успешно забронирована на " + meetingDateNearest));
    }

    @ParameterizedTest
    @CsvSource(value = {"meetingInOneWeek,7",
            "meetingIn_365_Days,365",
            "meetingIn_30_Days,30"})
    public void shouldSendFormWithValidDate(String testcase, int days) {
        $("[data-test-id='city'] input").setValue("Екатеринбург");
        $("[data-test-id='date'] input").doubleClick().sendKeys(BACK_SPACE);
        $("[data-test-id='date'] input").setValue(bookDate(days));
        $("[data-test-id='name'] input").setValue("Иван Иванов");
        $("[data-test-id='phone'] input").setValue("+71234567891");
        $("[data-test-id='agreement'] .checkbox__box").click();
        $(byText("Забронировать")).click();
        $("[data-test-id='notification'] .notification__title")
                .shouldBe(Condition.appear, Duration.ofSeconds(15))
                .shouldHave((text("Успешно!")));
        $("[data-test-id='notification'] .notification__content")
                .shouldBe(Condition.visible)
                .shouldHave(text("Встреча успешно забронирована на " + bookDate(days)));
    }

    @ParameterizedTest
    @CsvSource(value = {"nameWithOneHyphenCyrillic,Анна-Мария Иванова",
            "nameSurnameWithTwoHyphensCyrillic,Анна-Мария-Ремарк Иванова",
            "twoLetterNameAndSurnameCyrillic,Ян Бо",
            "regularNameSurnameWithSpaceCyrillic,Иван Петров",
            "regularNameSecondNameSurnameWithSpaceCyrillic,Иван Николаевич Петров",
            "nameSurnameAllUpperCaseCyrillic,ИВАН ПЕТРОВ",
            "nameSurnameAllLowerCaseCyrillic,иван петров"})
    public void shouldSendFormWithValidName(String testcase, String name) {
        $("[data-test-id='city'] input").setValue("Екатеринбург");
        $("[data-test-id='date'] input").doubleClick().sendKeys(BACK_SPACE);
        $("[data-test-id='date'] input").setValue(meetingDateNearest);
        $("[data-test-id='name'] input").setValue(name);
        $("[data-test-id='phone'] input").setValue("+71234567891");
        $("[data-test-id='agreement'] .checkbox__box").click();
        $(byText("Забронировать")).click();
        $("[data-test-id='notification'] .notification__title")
                .shouldBe(Condition.appear, Duration.ofSeconds(15))
                .shouldHave((text("Успешно!")));
        $("[data-test-id='notification'] .notification__content")
                .shouldBe(Condition.visible)
                .shouldHave(text("Встреча успешно забронирована на " + meetingDateNearest));
    }
}
