import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;


public class BankFormTest {

    WebDriver driver;

    @BeforeAll
    static void setup() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    public void setUp() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--no-sandbox");
        options.addArguments("--headless");
        driver = new ChromeDriver(options);
        driver.get("http://localhost:9999/");
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
    void shouldWarnIfNameAndSurnameAreInvalid() {
        driver.findElement(By.cssSelector("[type='text']")).sendKeys("Dwayne Johnson");
        driver.findElement(By.className("button__text")).click();
        driver.findElement(By.cssSelector("[type= 'tel']")).sendKeys("+78007654635");
        driver.findElement(By.className("checkbox")).click();
        driver.findElement(By.className("button__text")).click();
        String text = driver.findElement(By.cssSelector("[data-test-id='name'].input_invalid .input__sub")).getText();
        Assertions.assertEquals("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.", text.trim());
    }

    @Test
    void shouldWarnIfOverLimitInNameAndSurname() {
        driver.findElement(By.cssSelector("[type='text']")).sendKeys("Иван Ивановвввввввввввввввввввввввввввввввввввввввввввввввввввввввввввввввввввввввввввввввввввввввввввввввввввввввввввввввввввввввввввввввввввввввввввввввввввввввввввввввввввввввввввввввввввввввввввввввввввввввввввввввввввввввввввввввввввввввввввввввввввввввввввввввввввввввввввввввввввввввввввввввввввввввввв");
        driver.findElement(By.cssSelector("[type='tel']")).sendKeys("+78007654635");
        driver.findElement(By.className("checkbox")).click();
        driver.findElement(By.className("button__text")).click();
        String text = driver.findElement(By.cssSelector("[data-test-id='name'].input_invalid .input__sub")).getText();
        Assertions.assertEquals("Поле должно включать менее 64 символов", text.trim());
    }

    @Test
    void shouldWarnIfPhoneNumberIsInvalid() {
        driver.findElement(By.cssSelector("[type='text']")).sendKeys("Иван Иванов");
        driver.findElement(By.cssSelector("[type='tel']")).sendKeys("88007654635");
        driver.findElement(By.className("checkbox")).click();
        driver.findElement(By.className("button__text")).click();
        String text = driver.findElement(By.cssSelector("[data-test-id='phone'].input_invalid .input__sub")).getText();
        Assertions.assertEquals("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.", text);
    }

    @Test
    void shouldWarnIfOverLimitInPhone() {
        driver.findElement(By.cssSelector("[type='text']")).sendKeys("Иван Иванов");
        driver.findElement(By.cssSelector("[type='tel']")).sendKeys("+780076546358");
        driver.findElement(By.className("checkbox")).click();
        driver.findElement(By.className("button__text")).click();
        String text = driver.findElement(By.cssSelector("[data-test-id='phone'].input_invalid .input__sub")).getText();
        Assertions.assertEquals("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.", text);
    }

    @Test
    void shouldWarnIfThereIsNoTick() {
        driver.findElement(By.cssSelector("[type='text']")).sendKeys("Иван Иванов");
        driver.findElement(By.cssSelector("[type='tel']")).sendKeys("+78007654635");
        driver.findElement(By.className("button__text")).click();
        String text = driver.findElement(By.className("input_invalid")).getText();
        Assertions.assertEquals("Я соглашаюсь с условиями обработки и использования моих персональных данных и разрешаю сделать запрос в бюро кредитных историй", text);
    }
    @Test
    void shouldWarnIfNameNotFilled() {
        driver.findElement(By.cssSelector("[type='tel']")).sendKeys("+78007654635");
        driver.findElement(By.className("checkbox")).click();
        driver.findElement(By.className("button__text")).click();
        String text = driver.findElement(By.cssSelector("[data-test-id='name'].input_invalid .input__sub")).getText();
        Assertions.assertEquals("Поле обязательно для заполнения", text);
    }

    @Test
    void shouldWarnIfTelephoneNotFilled() {
        driver.findElement(By.cssSelector("[type='text']")).sendKeys("Иван Иванов");
        driver.findElement(By.className("checkbox")).click();
        driver.findElement(By.className("button__text")).click();
        String text = driver.findElement(By.cssSelector("[data-test-id='phone'].input_invalid .input__sub")).getText();
        Assertions.assertEquals("Поле обязательно для заполнения", text);
    }

    @Test
    void shouldWarnWhatToWriteInNameAndSurname() {
        String text = driver.findElement(By.cssSelector("[data-test-id='name'] .input__sub")).getText();
        Assertions.assertEquals("Укажите точно как в паспорте", text);
    }

    @Test
    void shouldWarnWhatToWriteInPhone() {
        String text = driver.findElement(By.cssSelector("[data-test-id='phone'] .input__sub")).getText();
        Assertions.assertEquals("На указанный номер моб. тел. будет отправлен смс-код для подтверждения заявки на карту. Проверьте, что номер ваш и введен корректно.", text);
    }
}
