package Tests;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Iterator;
import java.util.Set;

public class TestClass {

    final WebDriver driver;

    public TestClass() {
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\stefa\\Downloads\\chromedriver.exe");

        driver = new ChromeDriver();

        driver.manage().window().maximize();
    }

    private void redirectToSignUpPage() {
        driver.get("https://politrip.com/account/sign-up");
    }

    private void completeSignUpFields(SignUpContent signUpContent) {
        driver.findElement(By.name("first-name")).sendKeys(signUpContent.get_firstName());

        driver.findElement(By.name("last-name")).sendKeys(signUpContent.get_lastName());

        driver.findElement(By.name("email")).sendKeys(signUpContent.get_email());

        driver.findElement(By.id("sign-up-password-input")).sendKeys(signUpContent.get_password());

        driver.findElement(By.id("sign-up-confirm-password-input")).sendKeys(signUpContent.get_confirmPassword());

        WebElement heard = driver.findElement(By.name("heard"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", heard);

        heard.sendKeys(signUpContent.get_heard());
    }

    // Registration with all valid data
    @Test
    public void allValidDataSingUp() {
        {

            redirectToSignUpPage();

            String randomNumber = String.valueOf(Math.random());
            SignUpContent signUpContent = new SignUpContent("firstName", "lastName", randomNumber+"@yahoo.com",
                    "Password1", "Password1", "From a friend");

            completeSignUpFields(signUpContent);

            driver.findElement(By.id("cookiescript_close")).click();

            driver.findElement(By.id(" qa_loader-button")).click();

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            ((JavascriptExecutor) driver).executeScript("window.scrollBy(0,350)", "");

            driver.findElement(By.id("qa_signup-participant")).click();
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            String expected_url = "https://politrip.com/";
            String actual_url = driver.getCurrentUrl();
            Assert.assertEquals(actual_url,expected_url);

        }
    }


    // Registration without providing First Name field
    @Test
    public void emptyFirstNameTest() {

        redirectToSignUpPage();

        SignUpContent signUpContent = new SignUpContent("", "lastName", "email@yahoo.com",
                "Password1", "Password1", "From a friend");


        completeSignUpFields(signUpContent);
        driver.findElement(By.id("cookiescript_close")).click();
        driver.findElement(By.id(" qa_loader-button")).click();

        String expectedErrorMsg = "This field can not be empty";
        WebElement exp = driver.findElement(By.xpath("//span[contains(text(), ' This field can not be empty ')]"));
        String actualErrorMsg = exp.getText();

        Assert.assertEquals(actualErrorMsg, expectedErrorMsg);
    }

    // Registration without providing Last Name field
    @Test
    public void emptyLastNameTest() {

        redirectToSignUpPage();
        SignUpContent signUpContent = new SignUpContent("firstName", "", "email@yahoo.com",
                "Password1", "Password1", "From a friend");

        completeSignUpFields(signUpContent);
        driver.findElement(By.id("cookiescript_close")).click();

        driver.findElement(By.id(" qa_loader-button")).click();

        String expectedErrorMsg = "This field can not be empty";
        WebElement exp = driver.findElement(By.xpath("//span[contains(text(), ' This field can not be empty ')]"));
        String actualErrorMsg = exp.getText();

        Assert.assertEquals(actualErrorMsg, expectedErrorMsg);
    }

    // Registration without providing email field
    @Test
    public void emptyEmailTest() {

        redirectToSignUpPage();
        SignUpContent signUpContent = new SignUpContent("firstName", "lastName", "",
                "Password1", "Password1", "From a friend");


        completeSignUpFields(signUpContent);
        driver.findElement(By.id("cookiescript_close")).click();

        driver.findElement(By.id(" qa_loader-button")).click();

        String expectedErrorMsg = "This field can not be empty";
        WebElement exp = driver.findElement(By.xpath("//span[contains(text(), ' This field can not be empty ')]"));
        String actualErrorMsg = exp.getText();

        Assert.assertEquals(actualErrorMsg, expectedErrorMsg);
    }

    // Registration with email id which already have account
    @Test
    public void alreadyExistingEmailTest() {

        redirectToSignUpPage();
        SignUpContent signUpContent = new SignUpContent("firstName", "lastName", "email@yahoo.com",
                "Password1", "Password1", "From a friend");


        completeSignUpFields(signUpContent);
        driver.findElement(By.id("cookiescript_close")).click();

        driver.findElement(By.id(" qa_loader-button")).click();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String expectedErrorMsg = "An user with this email is already registered";

        WebElement exp = driver.findElement(By.xpath("//div[@class='response response-error error-container']"));

        String actualErrorMsg = exp.getText();

        Assert.assertEquals(actualErrorMsg, expectedErrorMsg);
    }

    // Registration with invalid email
    @Test
    public void invalidEmailTest() {

        redirectToSignUpPage();
        SignUpContent signUpContent = new SignUpContent("firstName", "lastName", "email",
                "Password1", "Password1", "From a friend");

        completeSignUpFields(signUpContent);
        driver.findElement(By.id("cookiescript_close")).click();

        driver.findElement(By.id(" qa_loader-button")).click();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String expectedErrorMsg = "Please enter a valid email address";

        WebElement exp = driver.findElement(By.xpath("//span[contains(text(), ' Please enter a valid email address ')]"));

        String actualErrorMsg = exp.getText();

        Assert.assertEquals(actualErrorMsg, expectedErrorMsg);
    }

    // Registration without providing password
    @Test
    public void emptyPasswordTest() {

        redirectToSignUpPage();
        SignUpContent signUpContent = new SignUpContent("firstName", "lastName", "email@yahoo.com",
                "", "Password1", "From a friend");

        completeSignUpFields(signUpContent);
        driver.findElement(By.id("cookiescript_close")).click();

        driver.findElement(By.id(" qa_loader-button")).click();

        String expectedErrorMsg = "This field is required";
        WebElement exp = driver.findElement(By.xpath("//span[contains(text(), ' This field is required ')]"));
        String actualErrorMsg = exp.getText();

        Assert.assertEquals(actualErrorMsg, expectedErrorMsg);
    }

    // Registration with  invalid password < 8 characters
    @Test
    public void invalidNrCharactersPasswordTest() {

        redirectToSignUpPage();
        SignUpContent signUpContent = new SignUpContent("firstName", "lastName", "email@yahoo.com",
                "pass", "Password1", "From a friend");

        completeSignUpFields(signUpContent);
        driver.findElement(By.id("cookiescript_close")).click();

        driver.findElement(By.id(" qa_loader-button")).click();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        String expectedErrorMsg = "Password must contain at least 8 characters, 1 uppercase letter, 1 lowercase letter and 1 digit";
        WebElement exp = driver.findElement(By.xpath("//span[contains(text(), ' Password must contain at least 8 characters, 1 uppercase letter, 1 lowercase letter and 1 digit ')]"));
        String actualErrorMsg = exp.getText();
        Assert.assertEquals(actualErrorMsg, expectedErrorMsg);
    }

    // Registration with  invalid password - no uppercase letter
    @Test
    public void invalidUpperLetterPasswordTest() {

        redirectToSignUpPage();
        SignUpContent signUpContent = new SignUpContent("firstName", "lastName", "email@yahoo.com",
                "password1", "password1", "From a friend");

        completeSignUpFields(signUpContent);
        driver.findElement(By.id("cookiescript_close")).click();

        driver.findElement(By.id(" qa_loader-button")).click();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        String expectedErrorMsg = "Password must contain at least 8 characters, 1 uppercase letter, 1 lowercase letter and 1 digit";
        WebElement exp = driver.findElement(By.xpath("//span[contains(text(), ' Password must contain at least 8 characters, 1 uppercase letter, 1 lowercase letter and 1 digit ')]"));
        String actualErrorMsg = exp.getText();
        Assert.assertEquals(actualErrorMsg, expectedErrorMsg);
    }

    // Registration with  invalid password -no lowercase letter
    @Test
    public void invalidLowerLetterPasswordTest() {

        redirectToSignUpPage();
        SignUpContent signUpContent = new SignUpContent("firstName", "lastName", "email@yahoo.com",
                "PASSWORD1", "PASSWORD1", "From a friend");

        completeSignUpFields(signUpContent);
        driver.findElement(By.id("cookiescript_close")).click();

        driver.findElement(By.id(" qa_loader-button")).click();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        String expectedErrorMsg = "Password must contain at least 8 characters, 1 uppercase letter, 1 lowercase letter and 1 digit";
        WebElement exp = driver.findElement(By.xpath("//span[contains(text(), ' Password must contain at least 8 characters, 1 uppercase letter, 1 lowercase letter and 1 digit ')]"));
        String actualErrorMsg = exp.getText();
        Assert.assertEquals(actualErrorMsg, expectedErrorMsg);
    }

    // Registration with  invalid password - no digit
    @Test
    public void invalidDigitPasswordTest() {

        redirectToSignUpPage();
        SignUpContent signUpContent = new SignUpContent("firstName", "lastName", "email@yahoo.com",
                "Password", "Password", "From a friend");

        completeSignUpFields(signUpContent);
        driver.findElement(By.id("cookiescript_close")).click();

        driver.findElement(By.id(" qa_loader-button")).click();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        String expectedErrorMsg = "Password must contain at least 8 characters, 1 uppercase letter, 1 lowercase letter and 1 digit";
        WebElement exp = driver.findElement(By.xpath("//span[contains(text(), ' Password must contain at least 8 characters, 1 uppercase letter, 1 lowercase letter and 1 digit ')]"));
        String actualErrorMsg = exp.getText();
        Assert.assertEquals(actualErrorMsg, expectedErrorMsg);
    }


    // Registration without providing confirm password
    @Test
    public void emptyComfirmPasswordTest() {

        redirectToSignUpPage();
        SignUpContent signUpContent = new SignUpContent("firstName", "lastName", "email@yahoo.com",
                "Password", "", "From a friend");


        completeSignUpFields(signUpContent);
        driver.findElement(By.id("cookiescript_close")).click();

        driver.findElement(By.id(" qa_loader-button")).click();

        String expectedErrorMsg = "This field is required";
        WebElement exp = driver.findElement(By.xpath("//span[contains(text(), ' This field is required ')]"));
        String actualErrorMsg = exp.getText();

        Assert.assertEquals(actualErrorMsg, expectedErrorMsg);
    }

    // Registration with  invalid confirm password
    @Test
    public void invalidConfirmPasswordTest() {

        redirectToSignUpPage();
        SignUpContent signUpContent = new SignUpContent("firstName", "lastName", "email@yahoo.com",
                "Password1", "Password", "From a friend");

        completeSignUpFields(signUpContent);
        driver.findElement(By.id("cookiescript_close")).click();

        driver.findElement(By.id(" qa_loader-button")).click();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        String expectedErrorMsg = "Passwords must match";
        WebElement exp = driver.findElement(By.xpath("//span[contains(text(), ' Passwords must match ')]"));
        String actualErrorMsg = exp.getText();
        Assert.assertEquals(actualErrorMsg, expectedErrorMsg);
    }

    //Section Sing up with
    //Verifying redirection to the Login page from Registration page
    @Test
    public void loginRedirectionTest() {

        redirectToSignUpPage();

        driver.findElement(By.id("cookiescript_close")).click();


        driver.findElement(By.xpath("//span[@class='font-weight-bold']")).click();


        String expectedURL = "https://politrip.com/account/login";
        String actualURL = driver.getCurrentUrl();
        Assert.assertEquals(actualURL, expectedURL);

        String expectedTitle = "Log in | Politrip";
        String actualTitle = driver.getTitle();
        Assert.assertEquals(actualTitle, expectedTitle);
    }

    private boolean titleExists(String title) {
        String parent = driver.getWindowHandle();

        Set<String> s = driver.getWindowHandles();

        Iterator<String> I1 = s.iterator();
        String actualTitle;
        while (I1.hasNext()) {

            String child_window = I1.next();

            if (!parent.equals(child_window)) {
                actualTitle = driver.switchTo().window(child_window).getTitle();
                driver.switchTo().window(parent);
                if (actualTitle.equals(title)) {
                    return true;
                }
            }

        }
        return false;
    }

    //Verifying sing up with Facebook
    @Test
    public void signUpFacebookTest() {

        redirectToSignUpPage();

        driver.findElement(By.id("cookiescript_close")).click();

        WebElement signUpWithText = driver.findElement(By.xpath("//p[@class='separator-text']"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", signUpWithText);

        driver.findElement(By.xpath("//span[@class='social-label']")).click();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        String expectedTitle = "Facebook";
        Assert.assertTrue(titleExists(expectedTitle));
    }

    //Verifying sing up with Google
    @Test
    public void signUpGoogleTest() {

        redirectToSignUpPage();

        driver.findElement(By.id("cookiescript_close")).click();

        WebElement signUpWithText = driver.findElement(By.xpath("//p[@class='separator-text']"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", signUpWithText);

        driver.findElement(By.xpath("//span[contains(text(), 'Google')]")).click();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        String expectedTitle = "Conectați-vă – Conturi Google";
        Assert.assertTrue(titleExists(expectedTitle));
    }

    //Verifying sing up with Instagram
    @Test
    public void signUpInstagramTest() {

        redirectToSignUpPage();

        driver.findElement(By.id("cookiescript_close")).click();
        WebElement signUpWithText = driver.findElement(By.xpath("//p[@class='separator-text']"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", signUpWithText);

        driver.findElement(By.id("socialmedia-account-instagram-button")).click();

        String expectedURL = "https://www.instagram.com/accounts/login/?force_authentication=1&enable_fb_login=1&next=/oauth/authorize/%3Fclient_id%3D78e35ea8eafe469e85ac859369ff9d3c%26redirect_uri%3Dhttps%3A//politrip.com/sign-up%26response_type%3Dtoken";
        String actualURL = driver.getCurrentUrl();
        Assert.assertEquals(actualURL, expectedURL);

        String expectedTitle = "Login • Instagram";
        String actualTitle = driver.getTitle();
        Assert.assertEquals(actualTitle, expectedTitle);
    }

    //Verifying sing up with VK
    @Test
    public void signUpVKTest() {

        redirectToSignUpPage();

        driver.findElement(By.id("cookiescript_close")).click();

        WebElement signUpWithText = driver.findElement(By.xpath("//p[@class='separator-text']"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", signUpWithText);

        driver.findElement(By.xpath("//span[contains(text(), 'VK')]")).click();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        String expectedTitle = "VK | Login";
        Assert.assertTrue(titleExists(expectedTitle));
    }


}
