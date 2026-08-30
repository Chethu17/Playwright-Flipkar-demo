package com.phase2.playwright_dem;


import java.util.regex.Pattern;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.assertions.PlaywrightAssertions;
import com.microsoft.playwright.options.AriaRole;

public class AppTest {
	
	Playwright play;
	Browser browser;
	Page page;
	
	@BeforeMethod
	public void setup() {
		play = Playwright.create();
        browser = play.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
        //browser = play.firefox().launch(new BrowserType.LaunchOptions().setHeadless(false));
        //browser = play.chromium().launch(new BrowserType.LaunchOptions().setChannel("chrome").setHeadless(false));
        page = browser.newPage();
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
    public void verifyPageTitle() {   
    	
        System.out.println(page.title());
        PlaywrightAssertions.assertThat(page).hasTitle(Pattern.compile(".*Shopping.*")); 
        page.locator("a[href='/flights-travel-uhp-at-store?marketplace=FKT']").click();
        
        //select round
        page.getByText("Round Trip",new Page.GetByTextOptions().setExact(true)).click();
        page.waitForTimeout(2000);
        page.getByText("One Way",new Page.GetByTextOptions().setExact(true)).click();

        //select one way and verify 
        Locator addForDiscounts = page.getByText("Add for discounts",new Page.GetByTextOptions().setExact(true));
        PlaywrightAssertions.assertThat(addForDiscounts).isVisible();
        
        page.locator(".css-g5y9jx.r-13awgt0.r-1kb76zh").click();      
        page.getByPlaceholder("Search origin city/airport").fill("ben");
        page.getByText("Bengaluru" , new Page.GetByTextOptions().setExact(true)).click();
        
        page.locator(".css-g5y9jx.r-13awgt0.r-1jkjb").click();
        page.getByPlaceholder("Search destination city/airport").fill("mum");
        page.getByText("Mumbai" , new Page.GetByTextOptions().setExact(true)).click();
        page.waitForTimeout(2000);
        
        
        String monthYear="September 2026";
		String day = "20";
        page.getByText("Departure").click();
        
        boolean isInFirst = false;
        while (true) {
            Locator Month1 = page.locator(".css-g5y9jx.r-13awgt0.r-r2y082.r-1kb76zh");
            Locator Month2 = page.locator("div[class='css-g5y9jx r-13awgt0 r-1jkjb'] div[class='css-146c3p1']");
            if (Month1.innerText().contains(monthYear)) {
            	isInFirst = true;
                break;
            }
            else if(Month2.innerText().contains(monthYear)){
            	isInFirst = false;
            	break;
            }
            else {
                page.locator("svg[width='20']").click();
            }
        }
        
        if (isInFirst) {
        	page.locator("(//div[@class='css-146c3p1'][normalize-space()='"+ day + "'])[1]").click();
        	}
        else {
        	page.locator("(//div[@class='css-146c3p1'][normalize-space()='"+ day + "'])[2]").click();
        	}
        
        page.waitForTimeout(2000);
        
        //search flights
        page.getByText("Search flights").click();
        page.waitForTimeout(2000);
    }
    
    @AfterMethod
    public void teardown() {
    	page.close();
    }
}
