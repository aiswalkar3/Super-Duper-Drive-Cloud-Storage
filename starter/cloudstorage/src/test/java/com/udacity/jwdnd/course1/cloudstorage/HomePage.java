package com.udacity.jwdnd.course1.cloudstorage;


import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.util.ArrayList;
import java.util.List;

public class HomePage {
    private WebDriver driver;
    private WebDriverWait wait;

    @FindBy(id = "logout-button")
    private WebElement logout;

    @FindBy(id = "nav-files-tab")
    private WebElement filesTab;

    @FindBy(xpath = "//*[@id=\"nav-notes-tab\"]")
    private WebElement notesTab;

    @FindBy(id = "nav-credentials-tab")
    private WebElement credentialsTab;

    @FindBy(xpath = "//*[@id=\"add-note\"]")
    private WebElement addNote;

    @FindBy(id = "note-title")
    private WebElement noteTitle;

    @FindBy(id = "note-description")
    private WebElement noteDescription;

    @FindBy(id="noteSubmit")
    private WebElement noteSubmit;

    @FindBy(id = "userTable")
    private WebElement noteTable;

    @FindBy(id = "nav-notes")
    private WebElement notesDiv;

    @FindBy(id = "nav-credentials")
    private WebElement credentialsDiv;

    @FindBy(xpath = "//*[@id=\"nav-credentials\"]/button")
    private WebElement addCredentialsButton;

    @FindBy(id = "credential-url")
    private WebElement credentialUrl;

    @FindBy(id = "credential-username")
    private WebElement credentialUsername;

    @FindBy(id = "credential-password")
    private WebElement credentialPassword;

    @FindBy(id = "credentialSubmit")
    private WebElement credentialSaveButton;

    public HomePage(WebDriver driver)
    {
        this.driver = driver;
        PageFactory.initElements(driver,this);
        wait = new WebDriverWait(driver, 1000);
        waitForPageToLoad(logout);
    }

    public void waitForPageToLoad(WebElement element)
    {
        wait.until(ExpectedConditions.visibilityOf(element));
    }

    public String[] addNote(String noteTitle, String noteDescription) throws InterruptedException {

        Thread.sleep(1000);

        wait.until(ExpectedConditions.visibilityOf(notesTab));
        wait.until(ExpectedConditions.elementToBeClickable(By.id("nav-notes-tab")));

        notesTab.click();

        wait.until(ExpectedConditions.visibilityOf(notesDiv));
        wait.until(ExpectedConditions.visibilityOf(addNote));
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"add-note\"]")));

        addNote.click();

        wait.until(ExpectedConditions.visibilityOf(this.noteTitle));
        wait.until(driver -> driver.findElement(By.id("note-title")));
        this.noteTitle.sendKeys(noteTitle);
        this.noteDescription.sendKeys(noteDescription);
        this.noteSubmit.submit();

        wait.until(driver -> driver.findElement(By.xpath("//*[@id=\"userTable\"]/tbody/tr[last()]/th")));
        WebElement cell1 = wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.xpath("//*[@id=\"userTable\"]/tbody/tr[last()]/th"))));

        WebElement cell2 =  driver.findElement(By.xpath("//*[@id=\"userTable\"]/tbody/tr[last()]/td[2]"));

        String actualNoteText = cell1.getText();
        String actualNoteDescription = cell2.getText();

        return new String[]{actualNoteText, actualNoteDescription};
    }


    public String[] editNote(String updatedNoteTitle, String updatedNoteDescription) throws InterruptedException {
        Thread.sleep(1000);

        wait.until(ExpectedConditions.visibilityOf(notesTab));
        wait.until(ExpectedConditions.elementToBeClickable(By.id("nav-notes-tab")));

        notesTab.click();

        wait.until(ExpectedConditions.visibilityOf(notesDiv));
        wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.xpath("//*[@id=\"userTable\"]/tbody/tr[last()]/td[1]/button"))));
        WebElement button1 = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"userTable\"]/tbody/tr[last()]/td[1]/button")));
        button1.click();

        wait.until(ExpectedConditions.visibilityOf(this.noteTitle));
        wait.until(driver -> driver.findElement(By.id("note-title")));
        this.noteTitle.clear();
        this.noteTitle.sendKeys(updatedNoteTitle);
        this.noteDescription.clear();
        this.noteDescription.sendKeys(updatedNoteDescription);
        this.noteSubmit.submit();

        wait.until(driver -> driver.findElement(By.xpath("//*[@id=\"userTable\"]/tbody/tr[last()]/th")));
        wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.xpath("//*[@id=\"userTable\"]/tbody/tr[last()]/th"))));

        WebElement cell1 =  driver.findElement(By.xpath("//*[@id=\"userTable\"]/tbody/tr[last()]/th"));
        WebElement cell2 =  driver.findElement(By.xpath("//*[@id=\"userTable\"]/tbody/tr[last()]/td[2]"));

        String actualNoteText = cell1.getText();
        String actualNoteDescription = cell2.getText();

        return new String[]{actualNoteText, actualNoteDescription};
    }

    public List<String[]> deleteNote() throws InterruptedException {
        Thread.sleep(1000);

        List<String[]> listPairs = new ArrayList<>();
        wait.until(ExpectedConditions.visibilityOf(notesTab));
        wait.until(ExpectedConditions.elementToBeClickable(By.id("nav-notes-tab")));

        notesTab.click();

        wait.until(ExpectedConditions.visibilityOf(notesDiv));

        List<WebElement> noRowsBeforeDelete = driver.findElements(By.xpath("//*[@id=\"userTable\"]/tbody/tr"));
        int initialRows = noRowsBeforeDelete.size();

        WebElement cell1 =  driver.findElement(By.xpath("//*[@id=\"userTable\"]/tbody/tr[last()]/th"));
        WebElement cell2 =  driver.findElement(By.xpath("//*[@id=\"userTable\"]/tbody/tr[last()]/td[2]"));

        String actualNoteText = cell1.getText();
        String actualNoteDescription = cell2.getText();

        wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.xpath("//*[@id=\"userTable\"]/tbody/tr[last()]/td[1]/a"))));
        WebElement button1 = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"userTable\"]/tbody/tr[last()]/td[1]/a")));
        button1.click();

        wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.xpath("//*[@id=\"userTable\"]"))));

        List<WebElement> noRowsAfterDelete = driver.findElements(By.xpath("//*[@id=\"userTable\"]/tbody/tr"));
        int finalRows = noRowsAfterDelete.size();

        String []rows = {initialRows+"", finalRows+""};
        listPairs.add(rows);
        String []noteTextActualExpected = new String[2];
        noteTextActualExpected[0] = actualNoteText;
        listPairs.add(noteTextActualExpected);
        String []noteDescriptionActualExpected = new String[2];
        noteDescriptionActualExpected[0] = actualNoteDescription;
        listPairs.add(noteDescriptionActualExpected);

        noteTextActualExpected[1] = "";
        noteDescriptionActualExpected[1] = "";

        if(noRowsAfterDelete.size() == 1)
        {
            String noteText = driver.findElement(By.xpath("//*[@id=\"userTable\"]/tbody/tr/th")).getText();
            String noteDescription = driver.findElement(By.xpath("//*[@id=\"userTable\"]/tbody/tr/td[2]")).getText();

            if (noteText.equals(actualNoteText) && noteDescription.equals(actualNoteDescription)) {
                noteTextActualExpected[1] = noteText;
                noteDescriptionActualExpected[1] = noteDescription;
            }
        }
        else {
            for (int i = 0; i < noRowsAfterDelete.size(); i++) {
                String noteText = driver.findElement(By.xpath("//*[@id=\"userTable\"]/tbody/tr[i+1]/th")).getText();
                String noteDescription = driver.findElement(By.xpath("//*[@id=\"userTable\"]/tbody/tr[i+1]/td[2]")).getText();

                if (noteText.equals(actualNoteText) && noteDescription.equals(actualNoteDescription)) {
                    noteTextActualExpected[1] = noteText;
                    noteDescriptionActualExpected[1] = noteDescription;
                }
            }
        }

        return listPairs;
    }

    public String[] addCredential(String url, String username, String password) throws InterruptedException {

        Thread.sleep(1000);

        wait.until(ExpectedConditions.visibilityOf(credentialsTab));
        wait.until(ExpectedConditions.elementToBeClickable(By.id("nav-credentials-tab")));

        credentialsTab.click();

        wait.until(ExpectedConditions.visibilityOf(credentialsDiv));
        wait.until(ExpectedConditions.visibilityOf(addCredentialsButton));
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"nav-credentials\"]/button")));

        addCredentialsButton.click();

        wait.until(ExpectedConditions.visibilityOf(this.credentialUrl));
        wait.until(driver -> driver.findElement(By.id("credential-url")));
        this.credentialUrl.sendKeys(url);
        this.credentialUsername.sendKeys(username);
        this.credentialPassword.sendKeys(password);
        this.credentialSaveButton.submit();

        wait.until(driver -> driver.findElement(By.xpath("//*[@id=\"credentialTable\"]/tbody/tr[last()]/th")));

        WebElement cell1 = wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.xpath("//*[@id=\"credentialTable\"]/tbody/tr[last()]/th"))));
        WebElement cell2 =  driver.findElement(By.xpath("//*[@id=\"credentialTable\"]/tbody/tr[last()]/td[2]"));

        String actualUrl = cell1.getText();
        String actualUsername = cell2.getText();

        return new String[]{actualUrl, actualUsername};
    }

    public String[] editCredential(String url, String username, String password) throws InterruptedException {
        Thread.sleep(1000);

        wait.until(ExpectedConditions.visibilityOf(credentialsTab));
        wait.until(ExpectedConditions.elementToBeClickable(By.id("nav-credentials-tab")));

        credentialsTab.click();

        wait.until(ExpectedConditions.visibilityOf(credentialsDiv));
        wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.xpath("//*[@id=\"credentialTable\"]/tbody/tr[last()]/td[1]/button"))));
        WebElement button1 = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"credentialTable\"]/tbody/tr[last()]/td[1]/button")));

        button1.click();

        wait.until(ExpectedConditions.visibilityOf(this.credentialUrl));
        wait.until(driver -> driver.findElement(By.id("credential-url")));
        this.credentialUrl.clear();
        this.credentialUrl.sendKeys(url);
        this.credentialUsername.clear();
        this.credentialUsername.sendKeys(username);
        this.credentialPassword.clear();
        this.credentialPassword.sendKeys(password);
        this.credentialSaveButton.submit();

        wait.until(driver -> driver.findElement(By.xpath("//*[@id=\"credentialTable\"]/tbody/tr[last()]/th")));

        WebElement cell1 = wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.xpath("//*[@id=\"credentialTable\"]/tbody/tr[last()]/th"))));
        WebElement cell2 =  driver.findElement(By.xpath("//*[@id=\"credentialTable\"]/tbody/tr[last()]/td[2]"));

        String actualUrl = cell1.getText();
        String actualUsername = cell2.getText();

        return new String[]{actualUrl, actualUsername};
    }

    public List<String[]> deleteCredential() throws InterruptedException {
        Thread.sleep(1000);

        List<String[]> listPairs = new ArrayList<>();
        wait.until(ExpectedConditions.visibilityOf(credentialsTab));
        wait.until(ExpectedConditions.elementToBeClickable(By.id("nav-credentials-tab")));

        credentialsTab.click();

        wait.until(ExpectedConditions.visibilityOf(credentialsDiv));

        List<WebElement> noRowsBeforeDelete = driver.findElements(By.xpath("//*[@id=\"credentialTable\"]/tbody/tr"));
        int initialRows = noRowsBeforeDelete.size();

        WebElement cell1 =  driver.findElement(By.xpath("//*[@id=\"credentialTable\"]/tbody/tr[last()]/th"));
        WebElement cell2 =  driver.findElement(By.xpath("//*[@id=\"credentialTable\"]/tbody/tr[last()]/td[2]"));

        String actualUrl = cell1.getText();
        String actualUsername = cell2.getText();

        wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.xpath("//*[@id=\"credentialTable\"]/tbody/tr[last()]/td[1]/a"))));
        WebElement button1 = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"credentialTable\"]/tbody/tr[last()]/td[1]/a")));
        button1.click();

        wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.xpath("//*[@id=\"credentialTable\"]"))));

        List<WebElement> noRowsAfterDelete = driver.findElements(By.xpath("//*[@id=\"credentialTable\"]/tbody/tr"));
        int finalRows = noRowsAfterDelete.size();

        String []rows = {initialRows+"", finalRows+""};
        listPairs.add(rows);
        String []urlActualExpected = new String[2];
        urlActualExpected[0] = actualUrl;
        listPairs.add(urlActualExpected);
        String []usernameActualExpected = new String[2];
        usernameActualExpected[0] = actualUsername;
        listPairs.add(usernameActualExpected);

        urlActualExpected[1] = "";
        usernameActualExpected[1] = "";

        if(noRowsAfterDelete.size() == 1)
        {
            String url = driver.findElement(By.xpath("//*[@id=\"credentialTable\"]/tbody/tr/th")).getText();
            String username = driver.findElement(By.xpath("//*[@id=\"credentialTable\"]/tbody/tr/td[2]")).getText();

            if (url.equals(actualUrl) && username.equals(actualUsername)) {
                urlActualExpected[1] = url;
                usernameActualExpected[1] = username;
            }
        }
        else {
            for (int i = 0; i < noRowsAfterDelete.size(); i++) {
                String url = driver.findElement(By.xpath("//*[@id=\"credentialTable\"]/tbody/tr[i+1]/th")).getText();
                String username = driver.findElement(By.xpath("//*[@id=\"credentialTable\"]/tbody/tr[i+1]/td[2]")).getText();

                if (url.equals(actualUrl) && username.equals(actualUsername)) {
                    urlActualExpected[1] = url;
                    usernameActualExpected[1] = username;
                }
            }
        }

        return listPairs;
    }

    public void logout()
    {
        waitForPageToLoad(logout);
        this.logout.click();
    }
}
