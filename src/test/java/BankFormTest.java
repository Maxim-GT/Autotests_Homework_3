import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import java.util.List;


public class BankFormTest {

    WebDriver driver;

    @BeforeEach
    void setup() {
        driver = WebDriverManager.chromedriver().create();
    }
    @BeforeEach
    void setUpOpenPort(){
        driver.get("http://localhost:9999/");
    }
    @BeforeEach
    public void setUp() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--no-sandbox");
        options.addArguments("--headless");
        driver = new ChromeDriver(options);
    }
    @AfterEach
    void teardown() {
        driver.quit();
    }

    @Test
    void shouldSendFormAndAnswer() {
        driver.findElement(By.cssSelector("[type='text']")).sendKeys("Иван Иванов");
        driver.findElement(By.cssSelector("[type= 'tel']")).sendKeys("+78007654635");
        driver.findElement(By.className("checkbox")).click();
        driver.findElement(By.className("button__text")).click();
        String text = driver.findElement(By.cssSelector("[data-test-id='order-success']")).getText();
        Assertions.assertEquals("Ваша заявка успешно отправлена! Наш менеджер свяжется с вами в ближайшее время.", text.trim());
    }

    @Test
    void shouldWarnIfNameAndSurnameIsInvalid() {
        driver.findElement(By.cssSelector("[type=\"text\"]")).sendKeys("Dwayne Johnson");
        driver.findElement(By.className("button__text")).click();
        String text = driver.findElement(By.className("input__sub")).getText();
        Assertions.assertEquals("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.", text.trim());
    }

    @Test
    void shouldWarnIfOverLimitInNameAndSurname() {
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
        List<WebElement> list = driver.findElements(By.className("input__sub"));
        driver.findElement(By.cssSelector("[type=\"text\"]")).sendKeys("Иван Иванов");
        driver.findElement(By.cssSelector("[type=\"tel\"]")).sendKeys("88007654635");
        driver.findElement(By.className("button__text")).click();
        String text = list.get(1).getText();
        Assertions.assertEquals("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.", text);
    }

    @Test
    void shouldWarnIfOverLimitInPhone() {
        List<WebElement> list = driver.findElements(By.className("input__sub"));
        driver.findElement(By.cssSelector("[type=\"text\"]")).sendKeys("Иван Иванов");
        driver.findElement(By.cssSelector("[type=\"tel\"]")).sendKeys("+780076546358");
        driver.findElement(By.className("button__text")).click();
        String text = list.get(1).getText();
        Assertions.assertEquals("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.", text);
    }

    @Test
    void shouldWarnIfThereIsNoTick() {
        driver.findElement(By.cssSelector("[type=\"text\"]")).sendKeys("Иван Иванов");
        driver.findElement(By.cssSelector("[type=\"tel\"]")).sendKeys("+78007654635");
        driver.findElement(By.className("button__text")).click();
        String text = driver.findElement(By.className("input_invalid")).getText();
        Assertions.assertEquals("Я соглашаюсь с условиями обработки и использования моих персональных данных и разрешаю сделать запрос в бюро кредитных историй", text);
    }

    @Test
    void shouldWarnIfTelephoneNotFilled() {
        List<WebElement> list = driver.findElements(By.className("input__sub"));
        driver.findElement(By.className("button__text")).click();
        String text = list.get(0).getText();
        Assertions.assertEquals("Поле обязательно для заполнения", text);
    }

    @Test
    void shouldWarnWhatToWriteInNameAndSurname() {
        List<WebElement> list = driver.findElements(By.className("input__sub"));
        String text = list.get(0).getText();
        Assertions.assertEquals("Укажите точно как в паспорте", text);
    }

    @Test
    void shouldWarnWhatToWriteInPhone() {
        List<WebElement> list = driver.findElements(By.className("input__sub"));
        String text = list.get(1).getText();
        Assertions.assertEquals("На указанный номер моб. тел. будет отправлен смс-код для подтверждения заявки на карту. Проверьте, что номер ваш и введен корректно.", text);
    }
}
