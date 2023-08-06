package com.udacity.jwdnd.course1.cloudstorage;

import com.udacity.jwdnd.course1.cloudstorage.Model.Form.CredentialForm;
import com.udacity.jwdnd.course1.cloudstorage.Model.Form.NoteForm;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import java.io.File;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CloudStorageApplicationTests {

    @LocalServerPort
    private int port;

    private WebDriver driver;

    @BeforeAll
    static void beforeAll() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    public void beforeEach() {
        this.driver = new ChromeDriver();
    }

    @AfterEach
    public void afterEach() {
        if (this.driver != null) {
            driver.quit();
        }
    }

    @Test
    public void getLoginPage() {
        driver.get("http://localhost:" + this.port + "/login");
        Assertions.assertEquals("Login", driver.getTitle());
    }

    /**
     * PLEASE DO NOT DELETE THIS method.
     * Helper method for Udacity-supplied sanity checks.
     **/
    private void doMockSignUp(String firstName, String lastName, String userName, String password) {
        // Create a dummy account for logging in later.

        // Visit the sign-up page.
        WebDriverWait webDriverWait = new WebDriverWait(driver, 2);
        driver.get("http://localhost:" + this.port + "/signup");
        webDriverWait.until(ExpectedConditions.titleContains("Sign Up"));

        // Fill out credentials
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputFirstName")));
        WebElement inputFirstName = driver.findElement(By.id("inputFirstName"));
        inputFirstName.click();
        inputFirstName.sendKeys(firstName);

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputLastName")));
        WebElement inputLastName = driver.findElement(By.id("inputLastName"));
        inputLastName.click();
        inputLastName.sendKeys(lastName);

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputUsername")));
        WebElement inputUsername = driver.findElement(By.id("inputUsername"));
        inputUsername.click();
        inputUsername.sendKeys(userName);

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputPassword")));
        WebElement inputPassword = driver.findElement(By.id("inputPassword"));
        inputPassword.click();
        inputPassword.sendKeys(password);

        // Attempt to sign up.
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("buttonSignUp")));
        WebElement buttonSignUp = driver.findElement(By.id("buttonSignUp"));
        buttonSignUp.click();

		/* Check that the sign up was successful.
		// You may have to modify the element "success-msg" and the sign-up
		// success message below depening on the rest of your code.
		*/
        Assertions.assertTrue(driver.findElement(By.id("success-msg")).getText().contains("You successfully signed up!"));
    }


    /**
     * PLEASE DO NOT DELETE THIS method.
     * Helper method for Udacity-supplied sanity checks.
     **/
    private void doLogIn(String userName, String password) {
        // Log in to our dummy account.
        driver.get("http://localhost:" + this.port + "/login");
        WebDriverWait webDriverWait = new WebDriverWait(driver, 2);

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputUsername")));
        WebElement loginUserName = driver.findElement(By.id("inputUsername"));
        loginUserName.click();
        loginUserName.sendKeys(userName);

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputPassword")));
        WebElement loginPassword = driver.findElement(By.id("inputPassword"));
        loginPassword.click();
        loginPassword.sendKeys(password);

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("login-button")));
        WebElement loginButton = driver.findElement(By.id("login-button"));
        loginButton.click();

        webDriverWait.until(ExpectedConditions.titleContains("Home"));

    }

    /**
     * PLEASE DO NOT DELETE THIS TEST. You may modify this test to work with the
     * rest of your code.
     * This test is provided by Udacity to perform some basic sanity testing of
     * your code to ensure that it meets certain rubric criteria.
     * <p>
     * If this test is failing, please ensure that you are handling redirecting users
     * back to the login page after a succesful sign up.
     * Read more about the requirement in the rubric:
     * https://review.udacity.com/#!/rubrics/2724/view
     */
    @Test
    public void testRedirection() {
        // Create a test account
        doMockSignUp("Redirection", "Test", "RT", "123");

        // Check if we have been redirected to the log in page.
        Assertions.assertEquals("http://localhost:" + this.port + "/login", driver.getCurrentUrl());
    }

    /**
     * PLEASE DO NOT DELETE THIS TEST. You may modify this test to work with the
     * rest of your code.
     * This test is provided by Udacity to perform some basic sanity testing of
     * your code to ensure that it meets certain rubric criteria.
     * <p>
     * If this test is failing, please ensure that you are handling bad URLs
     * gracefully, for example with a custom error page.
     * <p>
     * Read more about custom error pages at:
     * https://attacomsian.com/blog/spring-boot-custom-error-page#displaying-custom-error-page
     */
    @Test
    public void testBadUrl() {
        // Create a test account
        doMockSignUp("URL", "Test", "UT", "123");
        doLogIn("UT", "123");

        // Try to access a random made-up URL.
        driver.get("http://localhost:" + this.port + "/some-random-page");
        Assertions.assertFalse(driver.getPageSource().contains("Whitelabel Error Page"));
    }


    /**
     * PLEASE DO NOT DELETE THIS TEST. You may modify this test to work with the
     * rest of your code.
     * This test is provided by Udacity to perform some basic sanity testing of
     * your code to ensure that it meets certain rubric criteria.
     * <p>
     * If this test is failing, please ensure that you are handling uploading large files (>1MB),
     * gracefully in your code.
     * <p>
     * Read more about file size limits here:
     * https://spring.io/guides/gs/uploading-files/ under the "Tuning File Upload Limits" section.
     */
    @Test
    public void testLargeUpload() {
        // Create a test account
        doMockSignUp("Large File", "Test", "LFT", "123");
        doLogIn("LFT", "123");

        // Try to upload an arbitrary large file
        WebDriverWait webDriverWait = new WebDriverWait(driver, 2);
        String fileName = "upload5m.zip";

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("fileUpload")));
        WebElement fileSelectButton = driver.findElement(By.id("fileUpload"));
        fileSelectButton.sendKeys(new File(fileName).getAbsolutePath());

        WebElement uploadButton = driver.findElement(By.id("uploadButton"));
        uploadButton.click();
        try {
            webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.id("success")));
        } catch (org.openqa.selenium.TimeoutException e) {
            System.out.println("Large File upload failed");
        }
        Assertions.assertFalse(driver.getPageSource().contains("HTTP Status 403 – Forbidden"));

    }

    @Test
    public void testAuthentication() {
        // Write a test that verifies that an unauthorized user can only access the login and signup pages.
        driver.get("http://localhost:" + this.port + "/login");
        Assertions.assertEquals("http://localhost:" + this.port + "/login", driver.getCurrentUrl());
        driver.get("http://localhost:" + this.port + "/signup");
        Assertions.assertEquals("http://localhost:" + this.port + "/signup", driver.getCurrentUrl());
        driver.get("http://localhost:" + this.port + "/home");
        Assertions.assertEquals("http://localhost:" + this.port + "/login", driver.getCurrentUrl());

        //Write a test that signs up a new user, logs in, verifies that the home page is accessible, logs out,
        // and verifies that the home page is no longer accessible.
        doMockSignUp("Tran", "Vuong", "Vuong001", "123");
        doLogIn("Vuong001", "123");
        Assertions.assertEquals("http://localhost:" + this.port + "/home", driver.getCurrentUrl());

        WebDriverWait webDriverWait = new WebDriverWait(driver, 2);
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("logout")));
        driver.findElement(By.id("logout")).click();

        driver.get("http://localhost:" + this.port + "/home");
        Assertions.assertEquals("http://localhost:" + this.port + "/login", driver.getCurrentUrl());
    }

    public void doMockupNote(NoteForm noteForm, boolean isUpdate) {
        WebDriverWait webDriverWait = new WebDriverWait(driver, 2);
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-notes-tab")));
        WebElement showTab = driver.findElement(By.id("nav-notes-tab"));
        showTab.click();

        if (isUpdate) {
            webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#noteTable button")));
            WebElement showModal = driver.findElement(By.cssSelector("#noteTable button"));
            showModal.click();
        } else {
            webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("btn-add-note")));
            WebElement showModal = driver.findElement(By.id("btn-add-note"));
            showModal.click();
        }

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("note-title")));
        WebElement inputTitle = driver.findElement(By.id("note-title"));
        inputTitle.clear();
        inputTitle.sendKeys(noteForm.getNoteTitle());

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("note-description")));
        WebElement inputDescription = driver.findElement(By.id("note-description"));
        inputDescription.clear();
        inputDescription.sendKeys(noteForm.getNoteDescription());

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("saveNote")));
        WebElement saveChangeButton = driver.findElement(By.id("saveNote"));
        saveChangeButton.click();

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-notes-tab")));
        WebElement reFocusTab = driver.findElement(By.id("nav-notes-tab"));
        reFocusTab.click();
    }

    @Test
    public void testForNote() {
        doMockSignUp("Tran", "Vuong", "Vuong002", "123");
        doLogIn("Vuong002", "123");
        WebDriverWait webDriverWait = new WebDriverWait(driver, 2);

        NoteForm noteAfter = new NoteForm();
        noteAfter.setNoteTitle("To do");
        noteAfter.setNoteDescription("Design model");

        NoteForm noteBefore = new NoteForm();
        noteBefore.setNoteTitle("Config");
        noteBefore.setNoteDescription("Config spring security");

        // Write a test that creates a note, and verifies it is displayed.
        doMockupNote(noteAfter, false);

        Assertions.assertTrue(driver.getPageSource().contains(noteAfter.getNoteTitle()));
        Assertions.assertTrue(driver.getPageSource().contains(noteAfter.getNoteDescription()));

        // Write a test that edits an existing note and verifies that the changes are displayed
        doMockupNote(noteBefore, true);

        Assertions.assertFalse(driver.getPageSource().contains(noteAfter.getNoteTitle()));
        Assertions.assertFalse(driver.getPageSource().contains(noteAfter.getNoteDescription()));
        Assertions.assertTrue(driver.getPageSource().contains(noteBefore.getNoteTitle()));
        Assertions.assertTrue(driver.getPageSource().contains(noteBefore.getNoteDescription()));

        // Write a test that deletes a note and verifies that the note is no longer displayed.
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#noteTable a")));
        WebElement deleteButton = driver.findElement(By.cssSelector("#noteTable a"));
        deleteButton.click();

        Assertions.assertFalse(driver.getPageSource().contains(noteAfter.getNoteTitle()));
        Assertions.assertFalse(driver.getPageSource().contains(noteAfter.getNoteDescription()));
        Assertions.assertFalse(driver.getPageSource().contains(noteBefore.getNoteTitle()));
        Assertions.assertFalse(driver.getPageSource().contains(noteAfter.getNoteDescription()));
    }

    public void doMockupCrendential(CredentialForm credentialForm, boolean isUpdate) {
        WebDriverWait webDriverWait = new WebDriverWait(driver, 2);

        if (!isUpdate) {
            webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-credentials-tab")));
            WebElement showTab = driver.findElement(By.id("nav-credentials-tab"));
            showTab.click();

            webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#nav-credentials button")));
            WebElement showModel = driver.findElement(By.cssSelector("#nav-credentials button"));
            showModel.click();
        }

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-url")));
        WebElement inputUrl = driver.findElement(By.id("credential-url"));
        inputUrl.clear();
        inputUrl.sendKeys(credentialForm.getUrl());

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-username")));
        WebElement inputUsername = driver.findElement(By.id("credential-username"));
        inputUsername.clear();
        inputUsername.sendKeys(credentialForm.getUsername());

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-password")));
        WebElement inputPassword = driver.findElement(By.id("credential-password"));
        inputPassword.clear();
        inputPassword.sendKeys(credentialForm.getPassword());

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("save-credential")));
        WebElement saveChangeButton = driver.findElement(By.id("save-credential"));
        saveChangeButton.click();

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-credentials-tab")));
        WebElement reFocusTab = driver.findElement(By.id("nav-credentials-tab"));
        reFocusTab.click();
    }

    public void confirmCredentialSaveChanged(CredentialForm credentialAfter) {
        WebDriverWait webDriverWait = new WebDriverWait(driver, 2);

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#credentialTable tbody tr th")));
        String actualUrl = driver.findElement(By.cssSelector("#credentialTable tbody tr th")).getText();

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#credentialTable tbody tr td:nth-child(3)")));
        String actualUsername = driver.findElement(By.cssSelector("#credentialTable tbody tr td:nth-child(3)")).getText();

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#credentialTable tbody tr td:nth-child(4)")));
        String actualPass = driver.findElement(By.cssSelector("#credentialTable tbody tr td:nth-child(4)")).getText();

        Assertions.assertEquals(credentialAfter.getUrl(), actualUrl);
        Assertions.assertEquals(credentialAfter.getUsername(), actualUsername);
        Assertions.assertNotEquals(credentialAfter.getPassword(), actualPass);
    }

    @Test
    public void testForCredential() {
        doMockSignUp("Tran", "Vuong", "Vuong003", "123");
        doLogIn("Vuong003", "123");
        WebDriverWait webDriverWait = new WebDriverWait(driver, 2);

        CredentialForm credentialAfter = new CredentialForm();
        credentialAfter.setUrl("http://localhost:8080/login");
        credentialAfter.setUsername("admin001");
        credentialAfter.setPassword("123");

        CredentialForm credentialBefore = new CredentialForm();
        credentialBefore.setUrl("http://localhost:8080/home");
        credentialBefore.setUsername("user002");
        credentialBefore.setPassword("456");

        // Write a test that creates a set of credentials, verifies that they are displayed,
        // and verifies that the displayed password is encrypted.
        doMockupCrendential(credentialAfter, false);
        confirmCredentialSaveChanged(credentialAfter);

        //  Write a test that views an existing set of credentials, verifies that the viewable password is unencrypted,
        //  edits the credentials, and verifies that the changes are displayed.
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#credentialTable button")));
        WebElement showModel = driver.findElement(By.cssSelector("#credentialTable button"));
        showModel.click();

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-password")));
        String viewPass = driver.findElement(By.id("credential-password")).getAttribute("value");

        Assertions.assertEquals(credentialAfter.getPassword(), viewPass);

        doMockupCrendential(credentialBefore, true);
        confirmCredentialSaveChanged(credentialBefore);

        // Write a test that deletes an existing set of credentials and verifies that the credentials are no longer displayed.
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#credentialTable a")));
        WebElement deleteButton = driver.findElement(By.cssSelector("#credentialTable a"));
        deleteButton.click();

        Assertions.assertFalse(driver.getPageSource().contains(credentialAfter.getUrl()));
        Assertions.assertFalse(driver.getPageSource().contains(credentialAfter.getUsername()));
        Assertions.assertFalse(driver.getPageSource().contains(credentialBefore.getUrl()));
        Assertions.assertFalse(driver.getPageSource().contains(credentialBefore.getUsername()));
    }
}
