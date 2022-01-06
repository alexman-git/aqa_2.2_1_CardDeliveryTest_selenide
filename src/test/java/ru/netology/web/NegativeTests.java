package ru.netology.web;

import com.codeborne.selenide.Condition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static org.openqa.selenium.Keys.BACK_SPACE;

public class NegativeTests {

    @BeforeEach
    public void setupTest() {
        open("http://localhost:9999");
    }

    String bookDate(int days) {
        return LocalDate.now().plusDays(days).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
    }

    String meetingDateNearest = bookDate(3);

    @Test
    public void shouldNotSendFormWithEmptyCityName() {
        $("[data-test-id='date'] input").doubleClick().sendKeys(BACK_SPACE);
        $("[data-test-id='date'] input").setValue(meetingDateNearest);
        $("[data-test-id='name'] input").setValue("Иван Иванов");
        $("[data-test-id='phone'] input").setValue("+71234567891");
        $("[data-test-id='agreement'] .checkbox__box").click();
        $(byText("Забронировать")).click();
        $("[data-test-id='city'].input_invalid .input__sub")
                .shouldBe(Condition.visible)
                .shouldHave(text("Поле обязательно для заполнения"));
    }

    @ParameterizedTest
    @CsvSource(value = {"latinCityNameRuRegCenter,Moscow",
            "cyrillicCityNameRuRegCenterWithError,ПетропавловскКамчатский",
            "cyrillicCityNameRuRegCenterRestrictedSymbols,Петропавловск_К@мчатский",
            "foreignCityCyrillic,Таллинн",
            "foreignCityLatin,Berlin",
            "cyrillicCityNameRuNotRegCenter,Гатчина",
            "unrealCityName,ghfjghjfgf"})
    public void shouldNotSendFormWithInvalidCityName(String testcase, String cityName) {
        $("[data-test-id='city'] input").setValue(cityName);
        $("[data-test-id='date'] input").doubleClick().sendKeys(BACK_SPACE);
        $("[data-test-id='date'] input").setValue(meetingDateNearest);
        $("[data-test-id='name'] input").setValue("Иван Иванов");
        $("[data-test-id='phone'] input").setValue("+71234567891");
        $("[data-test-id='agreement'] .checkbox__box").click();
        $(byText("Забронировать")).click();
        $("[data-test-id='city'].input_invalid .input__sub")
                .shouldBe(Condition.visible)
                .shouldHave(text("Доставка в выбранный город недоступна"));
    }

    @Test
    public void shouldNotSendFormWithEmptyDate() {
        $("[data-test-id='city'] input").setValue("Тула");
        $("[data-test-id='name'] input").setValue("Иван Иванов");
        $("[data-test-id='phone'] input").setValue("+71234567891");
        $("[data-test-id='agreement'] .checkbox__box").click();
        $("[data-test-id='date'] input").doubleClick().sendKeys(BACK_SPACE);
        $(byText("Забронировать")).click();
        $("[data-test-id='date'] .input_invalid .input__sub")
                .shouldBe(Condition.visible)
                .shouldHave(text("Неверно введена дата"));
    }

    @ParameterizedTest
    @CsvSource(value = {"meetingInTwoDays,2",
            "meetingTomorrow,1",
            "meetingToday,0",
            "meetingInThePast,-1",
            "meetingYearAgo,-365"})
    public void shouldNotSendFormWithRestrictedDate(String testcase, int days) {
        $("[data-test-id='city'] input").setValue("Екатеринбург");
        $("[data-test-id='date'] input").doubleClick().sendKeys(BACK_SPACE);
        $("[data-test-id='date'] input").setValue(bookDate(days));
        $("[data-test-id='name'] input").setValue("Иван Иванов");
        $("[data-test-id='phone'] input").setValue("+71234567891");
        $("[data-test-id='agreement'] .checkbox__box").click();
        $(byText("Забронировать")).click();
        $("[data-test-id='date'] .input_invalid .input__sub")
                .shouldBe(Condition.visible)
                .shouldHave(text("Заказ на выбранную дату невозможен"));
    }

    @ParameterizedTest
    @CsvSource(value = {"meetingInTwoYears,730",
            "meetingIn_100_Years,36500",
            "meetingIn_150_Years,54750"})
    public void shouldNotSendFormWithValidButRedundantDate(String testcase, int days) {
        $("[data-test-id='city'] input").setValue("Екатеринбург");
        $("[data-test-id='date'] input").doubleClick().sendKeys(BACK_SPACE);
        $("[data-test-id='date'] input").setValue(bookDate(days));
        $("[data-test-id='name'] input").setValue("Иван Иванов");
        $("[data-test-id='phone'] input").setValue("+71234567891");
        $("[data-test-id='agreement'] .checkbox__box").click();
        $(byText("Забронировать")).click();
        $("[data-test-id='date'] .input_invalid .input__sub")
                .shouldBe(Condition.visible)
                .shouldHave(text("Заказ на выбранную дату невозможен"));
    }

    @ParameterizedTest
    @CsvSource(value = {"wrongDays,33122022",
            "wrongMonths,25152022",
            "wrongDaysMonths,35172022",
            "wrongLeapYearDate,29022022"})
    public void shouldNotSendFormWithNonExistentDate(String testcase, String date) {
        $("[data-test-id='city'] input").setValue("Екатеринбург");
        $("[data-test-id='date'] input").doubleClick().sendKeys(BACK_SPACE);
        $("[data-test-id='date'] input").setValue(date);
        $("[data-test-id='name'] input").setValue("Иван Иванов");
        $("[data-test-id='phone'] input").setValue("+71234567891");
        $("[data-test-id='agreement'] .checkbox__box").click();
        $(byText("Забронировать")).click();
        $("[data-test-id='date'] .input_invalid .input__sub")
                .shouldBe(Condition.visible)
                .shouldHave(text("Неверно введена дата"));
    }

    @Test
    public void shouldNotSendFormWithEmptyName() {
        $("[data-test-id='city'] input").setValue("Москва");
        $("[data-test-id='date'] input").doubleClick().sendKeys(BACK_SPACE);
        $("[data-test-id='date'] input").setValue(meetingDateNearest);
        $("[data-test-id='phone'] input").setValue("+71234567891");
        $("[data-test-id='agreement'] .checkbox__box").click();
        $(byText("Забронировать")).click();
        $("[data-test-id='name'].input_invalid .input__sub")
                .shouldBe(Condition.visible)
                .shouldHave(text("Поле обязательно для заполнения"));
    }

    @ParameterizedTest
    @CsvSource(value = {"oneLetterNameCyrillic(minBoundaryTestInField),а",
            "extremelyLongNameCyrillic(maxBoundaryTestInField),Аббббббббббббббббббббббббббббббббб Ввввввввввввввввввввввввввввввввввввввв",
            "oneLetterNameSurnameLatin,q a",
            "nameWithDigits,Павел Иванов0",
            "nameWithSpecialSymbols,Павел Иванов_@#$",
            "nameWithTwoHyphensInRowCyrillic,Анна--Мария Петрова",
            "nameWithHyphenAtTheBeginning,-Анна Петрова",
            "nameWithHyphenAtTheEnd,Анна- Петрова-",
            "nameWithOnlySpacesAndHyphensWithoutLetters,--  --",
            "nameSurnameWithUpperOrLowerCaseErrorsCyrillic,иВаН пЕтров"})
    public void shouldNotSendFormWithInvalidName(String testcase, String name) {
        $("[data-test-id='city'] input").setValue("Екатеринбург");
        $("[data-test-id='date'] input").doubleClick().sendKeys(BACK_SPACE);
        $("[data-test-id='date'] input").setValue(meetingDateNearest);
        $("[data-test-id='name'] input").setValue(name);
        $("[data-test-id='phone'] input").setValue("+71234567891");
        $("[data-test-id='agreement'] .checkbox__box").click();
        $(byText("Забронировать")).click();
        $("[data-test-id='name'].input_invalid .input__sub")
                .shouldBe(Condition.visible)
                .shouldHave(text("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы."));
    }

    @ParameterizedTest
    @CsvSource(value = {"phoneNumberElevenDigitsWithLetter,+78989898989q",
            "tooShortNumber,+7",
            "numberOfTenDigits,+7898989899",
            "numberOfTwelveDigits(tooLongNumber or MaxBoundaryTestInField),+789898989999",
            "numberElevenDigitsWithoutPlus,78989898998",
            "numberWithRestrictedSymbols,-78989898999",
            "numberWithRestrictedSymbols2,-78989898_@#$",
            "elevenDigitsPlusInTheMiddle,789898+98998",
            "elevenDigitsPlusAtTheEnd,78989898998+",
            "nonExistentNumberElevenDigitsAllNullsPlusAtFirst,+00000000000"})
    public void shouldNotSendFormWithInvalidPhoneNumber(String testcase, String phone) {
        $("[data-test-id='city'] input").setValue("Екатеринбург");
        $("[data-test-id='date'] input").doubleClick().sendKeys(BACK_SPACE);
        $("[data-test-id='date'] input").setValue(meetingDateNearest);
        $("[data-test-id='name'] input").setValue("Иван Иванов");
        $("[data-test-id='phone'] input").setValue(phone);
        $("[data-test-id='agreement'] .checkbox__box").click();
        $(byText("Забронировать")).click();
        $("[data-test-id='phone'].input_invalid .input__sub")
                .shouldBe(Condition.visible)
                .shouldHave(text("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678."));
    }

    @Test
    public void shouldNotSendFormWithEmptyPhoneNumber() {
        $("[data-test-id='city'] input").setValue("Москва");
        $("[data-test-id='date'] input").doubleClick().sendKeys(BACK_SPACE);
        $("[data-test-id='date'] input").setValue(meetingDateNearest);
        $("[data-test-id='name'] input").setValue("Иван Иванов");
        $("[data-test-id='agreement'] .checkbox__box").click();
        $(byText("Забронировать")).click();
        $("[data-test-id='phone'].input_invalid .input__sub")
                .shouldBe(Condition.visible)
                .shouldHave(text("Поле обязательно для заполнения"));
    }

    @Test
    public void shouldNotSendFormWithBoxNotClicked() {
        $("[data-test-id='city'] input").setValue("Екатеринбург");
        $("[data-test-id='date'] input").doubleClick().sendKeys(BACK_SPACE);
        $("[data-test-id='date'] input").setValue(meetingDateNearest);
        $("[data-test-id='name'] input").setValue("Иван Иванов");
        $("[data-test-id='phone'] input").setValue("+71234567891");
        $(byText("Забронировать")).click();
        $("[data-test-id='agreement'] .checkbox__text")
                .shouldHave(Condition.cssValue("color", "rgba(255, 92, 92, 1)"));
    }
}
