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

public class SignupPage {
    @FindBy(id = "inputFirstName")
    private WebElement firstName;

    @FindBy(id = "inputLastName")
    private WebElement lastName;

    @FindBy(id = "inputUsername")
    private WebElement userName;

    @FindBy(id = "inputPassword")
    private WebElement password;

    @FindBy(id = "submit-button")
    private WebElement submit;

    @FindBy(id = "success-msg")
    private WebElement successMessage;

    @FindBy(id = "error-msg")
    private WebElement errorMessage;

    private WebDriver webDriver;
    private WebDriverWait wait;

    public SignupPage(WebDriver webDriver)
    {
        this.webDriver = webDriver;
        PageFactory.initElements(webDriver,this);
        wait = new WebDriverWait(this.webDriver, 1000);
        waitForPageToLoad(firstName);
    }

    public void waitForPageToLoad(WebElement element)
    {
        wait.until(ExpectedConditions.visibilityOf(element));
    }

    private boolean existsElement(String id) {
        try {
            webDriver.findElement(By.id(id));
        } catch (NoSuchElementException e) {
            return false;
        }
        return true;
    }

    public String signupUser(String firstName, String lastName, String username, String password)
    {
        this.firstName.sendKeys(firstName);
        this.lastName.sendKeys(lastName);
        this.userName.sendKeys(username);
        this.password.sendKeys(password);
        this.submit.click();

        String errorMsg = "";

        if(existsElement("error-msg")) {
            errorMsg = this.errorMessage.getText();
        }

        if(!errorMsg.isEmpty())
        {
            return errorMsg;
        }
        else{
            return "success";
        }
    }

}
