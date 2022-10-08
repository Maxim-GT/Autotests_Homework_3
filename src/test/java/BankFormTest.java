import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.List;

public class BankFormTest {


    private WebDriver driver;

    @BeforeAll
    static void setUpAll() {
        System.setProperty("webdriver.chrome.driver", "driver/Win 10/chromedriver.exe");
    }

    @BeforeEach
    void setUp() {
        driver = new ChromeDriver();
    }

    @AfterEach
    void tearsDown() {
        driver.quit();
        driver = null;
    }

    @Test
    void shouldSendFormAndAnswer() {
        driver.get("http://localhost:9999/");
        driver.findElement(By.cssSelector("[type=\"text\"]")).sendKeys("Иван Иванов");
        driver.findElement(By.cssSelector("[type=\"tel\"]")).sendKeys("+78007654635");
        driver.findElement(By.className("checkbox")).click();
        driver.findElement(By.className("button__text")).click();
        String text = driver.findElement(By.className("paragraph")).getText();
        Assertions.assertEquals("Ваша заявка успешно отправлена! Наш менеджер свяжется с вами в ближайшее время.", text.trim());
    }

    @Test
    void shouldWarnIfNameAndSurnameIsInvalid() {
        driver.get("http://localhost:9999/");
        driver.findElement(By.cssSelector("[type=\"text\"]")).sendKeys("Dwayne Johnson");
        driver.findElement(By.className("button__text")).click();
        String text = driver.findElement(By.className("input__sub")).getText();
        Assertions.assertEquals("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.", text.trim());
    }

    @Test
    void shouldWarnIfOverLimitInNameAndSurname() {
        driver.get("http://localhost:9999/");
        List <WebElement> list = driver.findElements(By.className("input__sub"));
        driver.findElement(By.cssSelector("[type=\"text\"]")).sendKeys("Иван Ивановвввввввввввввввввввввввввввввввввввввввввввввввввввввввввввввввввввввввввввввввввввввввввввввввввввввввввввввввввввввввввввввввввввввввввввввввввввввввввввввввввввввввввввввввввввввввввввввввввввввввввввввввввввввввввввввввввввввввввввввввввввввввввввввввввввввввввввввввввввввввввввввввввввввввввв");
        driver.findElement(By.cssSelector("[type=\"tel\"]")).sendKeys("+78007654635");
        driver.findElement(By.className("checkbox")).click();
        driver.findElement(By.className("button__text")).click();
        String text = driver.findElement(By.className("paragraph")).getText();
        Assertions.assertEquals("Ваша заявка успешно отправлена! Наш менеджер свяжется с вами в ближайшее время.", text.trim());
    }

    @Test
    void shouldWarnIfPhoneNumberIsInvalid() {
        driver.get("http://localhost:9999/");
        List<WebElement> list = driver.findElements(By.className("input__sub"));
        driver.findElement(By.cssSelector("[type=\"text\"]")).sendKeys("Иван Иванов");
        driver.findElement(By.cssSelector("[type=\"tel\"]")).sendKeys("88007654635");
        driver.findElement(By.className("button__text")).click();
        String text = list.get(1).getText();
        Assertions.assertEquals("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.", text);
    }

    @Test
    void shouldWarnIfOverLimitInPhone() {
        driver.get("http://localhost:9999/");
        List<WebElement> list = driver.findElements(By.className("input__sub"));
        driver.findElement(By.cssSelector("[type=\"text\"]")).sendKeys("Иван Иванов");
        driver.findElement(By.cssSelector("[type=\"tel\"]")).sendKeys("+780076546358");
        driver.findElement(By.className("button__text")).click();
        String text = list.get(1).getText();
        Assertions.assertEquals("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.", text);
    }

    @Test
    void shouldWarnIfThereIsNoTick() {
        driver.get("http://localhost:9999/");
        driver.findElement(By.cssSelector("[type=\"text\"]")).sendKeys("Иван Иванов");
        driver.findElement(By.cssSelector("[type=\"tel\"]")).sendKeys("+78007654635");
        driver.findElement(By.className("button__text")).click();
        String text = driver.findElement(By.className("input_invalid")).getText();
        Assertions.assertEquals("Я соглашаюсь с условиями обработки и использования моих персональных данных и разрешаю сделать запрос в бюро кредитных историй", text);
    }

    @Test
    void shouldWarnIfTelephoneNotFilled() {
        driver.get("http://localhost:9999/");
        List<WebElement> list = driver.findElements(By.className("input__sub"));
        driver.findElement(By.className("button__text")).click();
        String text = list.get(0).getText();
        Assertions.assertEquals("Поле обязательно для заполнения", text);
    }

    @Test
    void shouldWarnWhatToWriteInNameAndSurname() {
        driver.get("http://localhost:9999/");
        List<WebElement> list = driver.findElements(By.className("input__sub"));
        String text = list.get(0).getText();
        Assertions.assertEquals("Укажите точно как в паспорте", text);
    }

    @Test
    void shouldWarnWhatToWriteInPhone() {
        driver.get("http://localhost:9999/");
        List<WebElement> list = driver.findElements(By.className("input__sub"));
        String text = list.get(1).getText();
        Assertions.assertEquals("На указанный номер моб. тел. будет отправлен смс-код для подтверждения заявки на карту. Проверьте, что номер ваш и введен корректно.", text);
    }
}
