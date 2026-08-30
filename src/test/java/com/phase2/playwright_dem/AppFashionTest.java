package com.phase2.playwright_dem;

import java.util.List;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.FrameLocator;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.assertions.PlaywrightAssertions;
import com.microsoft.playwright.options.AriaRole;

public class AppFashionTest {
	Playwright play;
	Browser browser;
	Page page;
	BrowserContext context;
	
	@BeforeMethod
	public void setup() {
		play = Playwright.create();
        browser = play.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
        context = browser.newContext();
        page = context.newPage();
        page.navigate("https://www.flipkart.com/");
        
      //close pop up
      	try {
      		page.getByRole(AriaRole.BUTTON).filter(new Locator.FilterOptions().setHasText("✕")).click();	
      		}
      	catch (Exception e) {
      		System.out.println("No login pop up");
      	}	
	}
	
	@Test
	public void Verify() {
		page.getByPlaceholder("Search for Products, Brands and More").first().fill("Shir");
		//dynamic drop down
		page.locator(".Swx5kP").filter(new Locator.FilterOptions().setHasText("Shirt for women")).first().click();
		page.waitForTimeout(2000);
		
		page.locator("//div[@class='_6odwB UHMz4K'][normalize-space()='fabric']").click();
		Locator cottonBlend = page.locator("label").filter(new Locator.FilterOptions().setHasText("Cotton Blend"));
		cottonBlend.click();
		page.locator("//div[@class='_6odwB UHMz4K'][normalize-space()='fabric']").click();
		//verify check box
		PlaywrightAssertions.assertThat(cottonBlend.locator("input[type='checkbox']")).isChecked();
		
		// if it is selection drop down - page.locator(".tx4xZf.StZidb").selectOption("Cotton Blend");
		page.waitForTimeout(2000);
		
		//locating list of items found
		Locator products= page.locator("div[data-id]");
		System.out.println("total - "+products.count());
		System.out.println(products.nth(0).innerText());
		
		//navigating to different window
		Page newpage = context.waitForPage(()-> products.nth(1).click());
		newpage.getByText("L", new Page.GetByTextOptions().setExact(true)).click();
		newpage.waitForTimeout(2000);	
		
		//bring page to front and mouse hover
		page.bringToFront();
		page.getByText("Login").hover();
		
		Locator items= page.locator(".Li60rs");
		List<Locator> itemsList = items.all();
		for(Locator i : itemsList) {
			System.out.println(i.innerText());
		}
		
		//alert and pop up
		page.waitForTimeout(2000);
		page.navigate("https://rahulshettyacademy.com/AutomationPractice/");
		page.onDialog(pop -> pop.accept());
		page.getByText("Alert", new Page.GetByTextOptions().setExact(true)).click();
		
		//frames 
		page.navigate("https://demo.automationtesting.in/Frames.html");
		FrameLocator framepage= page.frameLocator("#singleframe");
		framepage.locator("input[type='text']").fill("Chethan M");
		page.waitForTimeout(2000);
		
	}

}
