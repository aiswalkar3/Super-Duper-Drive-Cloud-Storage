package com.udacity.jwdnd.course1.cloudstorage;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CloudStorageApplicationTests {

	@LocalServerPort
	private int port;

	private WebDriver driver;

	private String baseURL;

	@BeforeAll
	public static void beforeAll() {
		WebDriverManager.chromedriver().setup();
	}

	@BeforeEach
	public void beforeEach() {
		this.driver = new ChromeDriver();
		baseURL = "http://localhost:" + port;
	}

	@AfterEach
	public void afterEach() {
		if (this.driver != null) {
			driver.quit();
		}
	}

	/*
	* Tests that the signup page is accessible and the user is able to register.
	* */
	@Test
	@Order(1)
	public void verifyUserSignup()
	{
		String firstname = "testUserFN1";
		String lastname = "testUserLN1";
		String username = "testUser1";
		String password = "testUser1";

		driver.get(baseURL + "/signup");

		SignupPage signupPage = new SignupPage(driver);
		String result = signupPage.signupUser(firstname, lastname, username, password);
		assertEquals("success",result);
		assertEquals("Login",driver.getTitle());
		LoginPage loginPage = new LoginPage(driver);
		assertEquals("You successfully signed up!",loginPage.signupSuccess());
	}

	/*
	 * Tests that the login page is accessible.
	 * */
	@Test
	@Order(2)
	public void getLoginPage() {
		driver.get("http://localhost:" + this.port + "/login");
		assertEquals("Login", driver.getTitle());
	}

	/*
	 * Tests that the home page is not accessible without logging in.
	 * */
	@Test
	@Order(3)
	public void verifyHomePageNotAccessibleWithoutLogin()
	{
		driver.get(baseURL + "/home");
		assertNotEquals("Home",driver.getTitle());
		assertEquals("Login",driver.getTitle());
	}


	/*
	* Selenium test that signs up a new user, logs that user in, verifies that they can access the home page,
	* then logs out and verifies that the home page is no longer accessible.
	* */
	@Test
	@Order(4)
	public void verifyUserSignupLoginToHomePageLogoutAndHomePageNotAccessibleWithoutLogin() throws InterruptedException {
		//check sign up new user
		String firstname = "testUserFN2";
		String lastname = "testUserLN2";
		String username = "testUser2";
		String password = "testUser2";

		driver.get(baseURL + "/signup");

		SignupPage signupPage = new SignupPage(driver);
		String result = signupPage.signupUser(firstname, lastname, username, password);
		assertEquals("success",result);
		assertEquals("Login",driver.getTitle());
		LoginPage loginPage = new LoginPage(driver);
		assertEquals("You successfully signed up!",loginPage.signupSuccess());

		//check log in user to home page
		driver.get(baseURL + "/login");
		loginPage = new LoginPage(driver);
		String loginResult = loginPage.loginUser(username,password);
		assertEquals("success",loginResult);

		assertEquals("Home", driver.getTitle());

		Thread.sleep(2000);
		//check logout user
		HomePage homePage = new HomePage(driver);
		homePage.logout();
		loginPage = new LoginPage(driver);
		assertEquals("Login", driver.getTitle());

		//chek home page not accessible without login.
		driver.get(baseURL + "/home");
		assertNotEquals("Home",driver.getTitle());
		assertEquals("Login",driver.getTitle());
	}

	/*
	 * Selenium test that logs the existing user in, verifies that they can access the home page,
	 * then adds the new note and checks if the note has been added successfully.
	 * */
	@Test
	@Order(5)
	public void verifyCreateNoteAndNoteDetailsVisibleInNoteList() throws InterruptedException {
		//check sign up new user
		String firstname = "testUserFN3";
		String lastname = "testUserLN3";
		String username = "testUser3";
		String password = "testUser3";

		driver.get(baseURL + "/signup");

		SignupPage signupPage = new SignupPage(driver);
		String result = signupPage.signupUser(firstname, lastname, username, password);
		assertEquals("success",result);
		assertEquals("Login",driver.getTitle());
		LoginPage loginPage = new LoginPage(driver);
		assertEquals("You successfully signed up!",loginPage.signupSuccess());

		//check log in user to home page
		driver.get(baseURL + "/login");
		loginPage = new LoginPage(driver);
		String loginResult = loginPage.loginUser(username,password);
		assertEquals("success",loginResult);

		HomePage homePage = new HomePage(driver);
		String noteTitle = "note1";
		String noteDescription = "design1";

		String []resultNoteAdd = homePage.addNote(noteTitle, noteDescription);

		assertEquals(noteTitle,resultNoteAdd[0]);
		assertEquals(noteDescription, resultNoteAdd[1]);
	}

	/*
	 * Selenium test that logs the existing user in, verifies that they can access the home page,
	 * then edits the last note and checks if the note has been edited successfully.
	 * */
	@Test
	@Order(6)
	public void verifyEditNoteAndNoteDetailsVisibleInNoteList() throws InterruptedException {
		//check log in user to home page
		driver.get(baseURL + "/login");
		LoginPage loginPage = new LoginPage(driver);
		String loginResult = loginPage.loginUser("testUser3","testUser3");
		assertEquals("success",loginResult);

		HomePage homePage = new HomePage(driver);
		String editNoteTitle = "note2";
		String editNoteDescription = "design2";

		String []resultNoteUpdate = homePage.editNote(editNoteTitle, editNoteDescription);

		assertEquals(editNoteTitle,resultNoteUpdate[0]);
		assertEquals(editNoteDescription, resultNoteUpdate[1]);
	}

	/*
	 * Selenium test that logs the existing user in, verifies that they can access the home page,
	 * then deletes the last note and checks if the note does not appear in user note list.
	 * But this can be extended to delete note from any row in the table and verify result.
	 * */
	@Test
	@Order(7)
	public void verifyDeleteNoteAndNoteDetailsNotVisibleInNoteList() throws InterruptedException {
		driver.get(baseURL + "/login");
		LoginPage loginPage = new LoginPage(driver);
		String loginResult = loginPage.loginUser("testUser3","testUser3");
		assertEquals("success",loginResult);

		HomePage homePage = new HomePage(driver);
		List<String[]> result = homePage.deleteNote();

		//verify the user note list has one less row.
		assertEquals(Integer.valueOf(result.get(0)[0]) - 1,Integer.valueOf(result.get(0)[1]));

		//verify that after delete the note details are not present in the user note list
		assertNotEquals(result.get(1)[0], result.get(1)[1]);
		assertNotEquals(result.get(2)[0], result.get(2)[1]);
		assertEquals(0, result.get(1)[1].length());
		assertEquals(0, result.get(2)[1].length());
	}

	/*
	 * Selenium test that logs the existing user in, verifies that they can access the home page,
	 * then adds the new credential and checks if the user credential has been added successfully.
	 * */
	@Test
	@Order(8)
	public void verifyCreateCredentialAndCredentialDetailsVisibleInCredentialList() throws InterruptedException {
		//check log in user to home page
		driver.get(baseURL + "/login");
		LoginPage loginPage = new LoginPage(driver);
		String loginResult = loginPage.loginUser("testUser3","testUser3");
		assertEquals("success",loginResult);

		String testUrl = "www.gmail.com";
		String testUsername = "abc123@gmail.com";
		String testPassword = "password@123";

		HomePage homePage = new HomePage(driver);
		String []resultCredentialAdd = homePage.addCredential(testUrl, testUsername, testPassword);

		assertEquals(testUrl,resultCredentialAdd[0]);
		assertEquals(testUsername, resultCredentialAdd[1]);
	}

	/*
	 * Selenium test that logs the existing user in, verifies that they can access the home page,
	 * then edits the existing credential and checks if the user credential has been edited successfully.
	 * */
	@Test
	@Order(9)
	public void verifyEditCredentialAndCredentialDetailsVisibleInCredentialList() throws InterruptedException {
		//check log in user to home page
		driver.get(baseURL + "/login");
		LoginPage loginPage = new LoginPage(driver);
		String loginResult = loginPage.loginUser("testUser3","testUser3");
		assertEquals("success",loginResult);

		HomePage homePage = new HomePage(driver);
		String testUrl = "www.google.com";
		String testUsername = "pqr123@gmail.com";
		String testPassword = "password@123";

		String []resultCredentialUpdate = homePage.editCredential(testUrl, testUsername, testPassword);

		assertEquals(testUrl,resultCredentialUpdate[0]);
		assertEquals(testUsername, resultCredentialUpdate[1]);
	}

	/*
	 * Selenium test that logs the existing user in, verifies that they can access the home page,
	 * then deletes the existing credential and checks if the user credential has been deleted successfully.
	 * */
	@Test
	@Order(10)
	public void verifyDeleteCredentialAndCredentialDetailsNotVisibleInCredentialList() throws InterruptedException {
		driver.get(baseURL + "/login");
		LoginPage loginPage = new LoginPage(driver);
		String loginResult = loginPage.loginUser("testUser3","testUser3");
		assertEquals("success",loginResult);

		HomePage homePage = new HomePage(driver);
		List<String[]> result = homePage.deleteCredential();

		//verify the user credential list has one less row.
		assertEquals(Integer.valueOf(result.get(0)[0]) - 1,Integer.valueOf(result.get(0)[1]));

		//verify that after delete the credential details are not present in the user credential list
		assertNotEquals(result.get(1)[0], result.get(1)[1]);
		assertNotEquals(result.get(2)[0], result.get(2)[1]);
		assertEquals(0, result.get(1)[1].length());
		assertEquals(0, result.get(2)[1].length());
	}
}
