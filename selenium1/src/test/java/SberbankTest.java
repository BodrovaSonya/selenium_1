import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.Set;
import java.util.concurrent.TimeUnit;

public class SberbankTest {
    WebDriver driver;
    String baseUrl;

    @Before
    public void beforeTest(){
        System.setProperty("webdriver.chrome.driver", "drv/chromedriver.exe");
        baseUrl = "http://www.sberbank.ru/ru/person";
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
        driver.manage().window().maximize();
        driver.get(baseUrl);
    }

    @Test
    public void testSberbank(){
        Wait wait = new WebDriverWait(driver, 5, 1000);
        //driver.findElement(By.xpath(".//div[@class=\"sbrf-div-list-inner --area bp-area header-container\"]//a[@aria-label=\"Раздел Застраховать себя  и имущество\"]")).click();
        driver.findElement(By.xpath("//div[contains(@class,'bp-area header-container')]//a[@aria-label='Раздел Застраховать себя  и имущество']")).click();
        WebElement menuItem = driver.findElement(By.xpath("//div[@class=\"sbrf-div-list-inner --area bp-area header-container\"]//a[contains(text(),'Страхование путешественников')]"));
        wait.until(ExpectedConditions.visibilityOf(menuItem));
        driver.findElement(By.xpath("//div[@class=\"sbrf-div-list-inner --area bp-area header-container\"]//a[contains(text(),'Страхование путешественников')]")).click();


        WebElement title = driver.findElement(By.xpath("//*[@class='sbrf-rich-outer']/h1"));
        wait.until(ExpectedConditions.visibilityOf(title));
        Assert.assertEquals("Страхование путешественников",title.getText());

        driver.findElement(By.xpath("//img[@src=\"/portalserver/content/atom/contentRepository/content/person/travel/banner-zashita-traveler.jpg?id=f6c836e1-5c5c-4367-b0d0-bbfb96be9c53\"]")).click();
        String parentWindow = driver.getWindowHandle();
        Set<String> handles =  driver.getWindowHandles();
        for(String windowHandle  : handles) {
            if (!windowHandle.equals(parentWindow)) {
                driver.switchTo().window(windowHandle);
            }
        }
        wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.xpath("//*[contains(text(),\"Минимальная\")]"))));

        driver.findElement(By.xpath("//*[contains(text(),\"Минимальная\")]")).click();
        driver.findElement(By.xpath("//span[@class=\"b-button-block-center\"]//*[contains(text(),\"Оформить\")]")).click();

        //застрахованный
        fillField(By.name("insured0_surname"),"Ivanov");
        fillField(By.name("insured0_name"),"Ivan");
        fillField(By.name("insured0_birthDate"),"01.01.1999");

        //страхователь
        fillField(By.name("surname"),"Петров");
        fillField(By.name("name"),"Петр");
        fillField(By.name("middlename"),"Петрович");
        fillField(By.name("birthDate"),"01.01.1989");
        driver.findElement(By.xpath("//*[@class=\"b-radio-field-entity ng-pristine ng-untouched ng-valid\"][@name=\"male\"]")).click();
        fillField(By.name("passport_series"),"1234");
        fillField(By.name("passport_number"),"222222");
        fillField(By.name("issueDate"),"14.03.2009");
        fillField(By.name("issuePlace"),"Трололо");

        Assert.assertEquals("Ivanov",
                driver.findElement(By.name("insured0_surname")).getAttribute("value"));
        Assert.assertEquals("Ivan",
                driver.findElement(By.name("insured0_name")).getAttribute("value"));
        Assert.assertEquals("01.01.1999",
                driver.findElement(By.name("insured0_birthDate")).getAttribute("value"));
        Assert.assertEquals("Петров",
                driver.findElement(By.name("surname")).getAttribute("value"));
        Assert.assertEquals("Петр",
                driver.findElement(By.name("name")).getAttribute("value"));
        Assert.assertEquals("Петрович",
                driver.findElement(By.name("middlename")).getAttribute("value"));
        Assert.assertEquals("",
                driver.findElement(By.name("birthDate")).getAttribute("value"));
        Assert.assertEquals("1234",
                driver.findElement(By.name("passport_series")).getAttribute("value"));
        Assert.assertEquals("222222",
                driver.findElement(By.name("passport_number")).getAttribute("value"));
        Assert.assertEquals("14.03.2009",
                driver.findElement(By.name("issueDate")).getAttribute("value"));
        Assert.assertEquals("Трололо",
                driver.findElement(By.name("issuePlace")).getAttribute("value"));

        driver.findElement(By.xpath("//*[contains(text(),\"Продолжить\")]")).click();
        Assert.assertEquals("Заполнены не все обязательные поля",
                driver.findElement(By.xpath("//*[@class=\"b-form-center-pos b-form-error-message\"][1]/div")).getText());

    }

    public void fillField(By locator, String value){
        driver.findElement(locator).clear();
        driver.findElement(locator).sendKeys(value);
    }

    @After
    public void afterTest(){
        driver.quit();
    }
}
