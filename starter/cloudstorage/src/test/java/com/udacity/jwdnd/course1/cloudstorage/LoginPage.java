package com.udacity.jwdnd.course1.cloudstorage;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.function.Function;

public class LoginPage {
    private WebDriver driver;
    private WebDriverWait wait;

    @FindBy(id = "success-msg")
    private WebElement successMessage;

    @FindBy(id = "inputUsername")
    private WebElement username;

    @FindBy(id = "inputPassword")
    private WebElement password;

    @FindBy(id = "submit-button")
    private WebElement submit;

    @FindBy(id = "error-div")
    private WebElement errorMsg;

    public LoginPage(WebDriver driver)
    {
        this.driver = driver;
        PageFactory.initElements(driver,this);
        wait = new WebDriverWait(driver, 1000);
        waitForPageToLoad(username);
    }

    public void waitForPageToLoad(WebElement element)
    {
        wait.until(ExpectedConditions.visibilityOf(element));
    }

    private boolean existsElement(String id) {
        try {
            driver.findElement(By.id(id));
        } catch (NoSuchElementException e) {
            return false;
        }
        return true;
    }

    public String signupSuccess()
    {
       if(existsElement("success-msg"))
       {
            return successMessage.getText();
       }

       return null;
    }

    public String loginUser(String username, String password)
    {
        String result = "success";
        this.username.sendKeys(username);
        this.password.sendKeys(password);
        this.submit.click();

        if(existsElement("error-div") && errorMsg.isEnabled())
        {
            result = errorMsg.getText();
        }

        return result;
    }
}
