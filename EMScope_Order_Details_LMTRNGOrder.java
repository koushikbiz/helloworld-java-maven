import org.openqa.selenium.By;
import org.openqa.selenium.UnhandledAlertException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class EMScope_Order_Details_LMTRNGOrder  {
    public static void main(String[] args) {
    	System.out.println("######################################################################");
		System.out.println("EMSCOPE AUTOMATION TEST CASE 11\n");
		System.out.println("THIS TEST CASE VERIFIES CONTENT OF ORDER DETAILS PAGE FOR A GIVEN LIMIT RANGE ORDER\n");
        // Create a new instance of the FireFox driver        
        WebDriver driver = new FirefoxDriver();
        driver.manage().window().maximize();

        // And now use this to visit EMScope login page
        driver.get("http://54.83.48.161:8080/emscope/web1/login.jsp");
        // Alternatively the same thing can be done like this
        // driver.navigate().to("http://54.83.48.161:8080/emscope/web1/login.jsp");

        // Find the input elements by their id
        WebElement username = driver.findElement(By.id("username"));
        WebElement password = driver.findElement(By.id("password"));

        // Set values for the elements
        username.sendKeys("user2");
        password.sendKeys("Integral4320");
        
        // Now submit the form. WebDriver will find the form for us from the element
        driver.findElement(By.tagName("button")).click();
        
        // Wait for 10 seconds for the home page (with logout link) to load, timeout after 10 seconds  
        new WebDriverWait(driver, 10).until(ExpectedConditions.elementToBeClickable(By.id("logout")));
               
        // Print successful login message and title of the page
        System.out.println("Login Successful");
        System.out.println("Page title is: " + driver.getTitle());
        
        // Enter Order ID and click Go!
        WebElement orderId = driver.findElement(By.id("orderId"));
        orderId.sendKeys("633433819");
        orderId.submit();
                
        // Wait for the order chart to load, timeout after 10 seconds. Log error message if Order ID not found.
        try
        {
        new WebDriverWait(driver, 10).until(ExpectedConditions.visibilityOfElementLocated(By.id("orderChartContainer"))); 
        }
        catch (UnhandledAlertException e)
        {
        	System.out.println("Exception thrown :"+ e);
        	System.out.println("Order ID not found");	
        }
        
        // Store the current window handle
        String winHandleBefore = driver.getWindowHandle();
                
        // In the Order Details section, click on More Info >> link
        WebElement orderdetailslink = driver.findElement(By.xpath("//body/div/div/div[2]/div[2]/div[7]/table[2]/tbody/tr[12]/td/span"));
        orderdetailslink.click();
        
        // Switch to new window opened for Order Details
        for(String winHandle : driver.getWindowHandles()){
            driver.switchTo().window(winHandle);
        }
        
        // Wait for 10 second for order details to load, timeout after 10 seconds
        try
        {
        new WebDriverWait(driver, 10).until(ExpectedConditions.elementToBeClickable(By.id("tData")));; 
        }
        catch (UnhandledAlertException e)
        {
        	System.out.println("Exception thrown :"+ e);
        	System.out.println("Problem fetching Order Details");	
        }
        
        // Validate contents of Order Details panel
        System.out.println("ENTERING ORDER DETAILS WINDOW\n");
        System.out.println("Panel Name:" + driver.findElement(By.xpath("//body/div/div")).getText());
        System.out.println("************************");
        
        String orderid = driver.findElement(By.xpath("//body/div/div[2]/table/tbody/tr/td[2]")).getText();
        if (orderid.equals("633433819")) System.out.println ("Order ID " + orderid + " is correct");
        else System.out.println ("Order ID " + orderid + " is incorrect");
        
        String submittedtime = driver.findElement(By.xpath("//body/div/div[2]/table/tbody/tr/td[4]")).getText();
        if (submittedtime.equals("2014-06-22 21:06:37.806")) System.out.println ("Submitted Time " + submittedtime + " is correct");
        else System.out.println ("Submitted Time " + submittedtime + " is incorrect");
        
        String lastevent = driver.findElement(By.xpath("//body/div/div[2]/table/tbody/tr/td[6]")).getText();
        if (lastevent.equals("2014-06-22 21:06:38.084")) System.out.println ("Last Event Time " + lastevent + " is correct");
        else System.out.println ("Last Event Time " + lastevent + " is incorrect");
        
        String orderstatus = driver.findElement(By.xpath("//body/div/div[2]/table/tbody/tr[2]/td[2]")).getText();
        if (orderstatus.equals("Filled")) System.out.println ("Order Status " + orderstatus + " is correct");
        else System.out.println ("Order Status " + orderstatus + " is incorrect");
        
        String ccypair = driver.findElement(By.xpath("//body/div/div[2]/table/tbody/tr[2]/td[4]")).getText();
        if (ccypair.equals("USD/CAD")) System.out.println ("Ccy Pair " + ccypair + " is correct");
        else System.out.println ("Ccy Pair " + ccypair + " is incorrect");
        
        String buysell = driver.findElement(By.xpath("//body/div/div[2]/table/tbody/tr[2]/td[6]")).getText();
        if (buysell.equals("Buy")) System.out.println ("Buy/Sell " + buysell + " is correct");
        else System.out.println ("Buy/Sell " + buysell + " is incorrect");
        
        String orderamount = driver.findElement(By.xpath("//body/div/div[2]/table/tbody/tr[3]/td[2]")).getText();
        if (orderamount.equals("100,000.00")) System.out.println ("Order Amount " + orderamount + " is correct");
        else System.out.println ("Order Amount " + orderamount + " is incorrect");
        
        String dealtccy = driver.findElement(By.xpath("//body/div/div[2]/table/tbody/tr[3]/td[4]")).getText();
        if (dealtccy.equals("USD")) System.out.println ("Dealt Currency " + dealtccy + " is correct");
        else System.out.println ("Dealt Currency " + dealtccy + " is incorrect");
        
        String tif = driver.findElement(By.xpath("//body/div/div[2]/table/tbody/tr[3]/td[6]")).getText();
        if (tif.equals("GTD")) System.out.println ("TIF " + tif + " is correct");
        else System.out.println ("TIF " + tif + " is incorrect");
        
        String marketrange = driver.findElement(By.xpath("//body/div/div[2]/table/tbody/tr[4]/td[2]")).getText();
        if (marketrange.equals("1")) System.out.println ("Market Range " + marketrange + " is correct");
        else System.out.println ("Market Range " + marketrange + " is incorrect");
        
        String orderrate = driver.findElement(By.xpath("//body/div/div[2]/table/tbody/tr[4]/td[4]")).getText();
        if (orderrate.equals("1.07546")) System.out.println ("Order Rate " + orderrate + " is correct");
        else System.out.println ("Order Rate " + orderrate + " is incorrect");
        
        String orderamountUSD = driver.findElement(By.xpath("//body/div/div[2]/table/tbody/tr[4]/td[6]")).getText();
        if (orderamountUSD.equals("100,000.00")) System.out.println ("Order Amount in USD " + orderamountUSD + " is correct");
        else System.out.println ("Order Amount in USD " + orderamountUSD + " is incorrect");
        
        String matchedrate = driver.findElement(By.xpath("//body/div/div[2]/table/tbody/tr[5]/td[2]")).getText();
        if (matchedrate.equals("1.075498")) System.out.println ("Matched Rate " + matchedrate + " is correct");
        else System.out.println ("Matched Rate " + matchedrate + " is incorrect");
        
        String ordertype = driver.findElement(By.xpath("//body/div/div[2]/table/tbody/tr[5]/td[4]")).getText();
        if (ordertype.equals("LIMIT")) System.out.println ("Order Type " + ordertype + " is correct");
        else System.out.println ("Order Type " + ordertype + " is incorrect");
        
        String matchedamountUSD = driver.findElement(By.xpath("//body/div/div[2]/table/tbody/tr[5]/td[6]")).getText();
        if (matchedamountUSD.equals("100,000.00")) System.out.println ("Matched Amount in USD " + matchedamountUSD + " is correct");
        else System.out.println ("Matched Amount in USD " + matchedamountUSD + " is incorrect");
        
        String matchedpercentage = driver.findElement(By.xpath("//body/div/div[2]/table/tbody/tr[6]/td[2]")).getText();
        if (matchedpercentage.equals("100")) System.out.println ("Matched Percentage " + matchedpercentage + " is correct");
        else System.out.println ("Matched Percentage " + matchedpercentage + " is incorrect");
        
        String matchedamount = driver.findElement(By.xpath("//body/div/div[2]/table/tbody/tr[6]/td[4]")).getText();
        if (matchedamount.equals("100,000.00")) System.out.println ("Matched Amount " + matchedamount + " is correct");
        else System.out.println ("Matched Amount " + matchedamount + " is incorrect");
        
        String persistent = driver.findElement(By.xpath("//body/div/div[2]/table/tbody/tr[6]/td[6]")).getText();
        if (persistent.equals("N")) System.out.println ("Persistent " + persistent + " is correct");
        else System.out.println ("Persistent " + persistent + " is incorrect");
        
        String dealer = driver.findElement(By.xpath("//body/div/div[2]/table/tbody/tr[7]/td[2]")).getText();
        if (dealer.equals("SUCDQuoter")) System.out.println ("Dealer " + dealer + " is correct");
        else System.out.println ("Dealer " + dealer + " is incorrect");
        
        String organization = driver.findElement(By.xpath("//body/div/div[2]/table/tbody/tr[7]/td[4]")).getText();
        if (organization.equals("SUCD")) System.out.println ("Organization " + organization + " is correct");
        else System.out.println ("Organization " + organization + " is incorrect");
        
        String externalorderid = driver.findElement(By.xpath("//body/div/div[2]/table/tbody/tr[7]/td[6]")).getText();
        if (externalorderid.equals("FXI1115131269")) System.out.println ("External Order ID " + externalorderid + " is correct");
        else System.out.println ("External Order ID " + externalorderid + " is incorrect");
        
        String expirytime = driver.findElement(By.xpath("//body/div/div[2]/table/tbody/tr[8]/td[2]")).getText();
        if (expirytime.equals("2014-06-23 02:36:38.805")) System.out.println ("Expiry Time " + expirytime + " is correct");
        else System.out.println ("Expiry Time " + expirytime + " is incorrect");
        
        String visibility = driver.findElement(By.xpath("//body/div/div[2]/table/tbody/tr[8]/td[4]")).getText();
        if (visibility.equals("Hidden")) System.out.println ("Visibility " + visibility + " is correct");
        else System.out.println ("Visibility " + visibility + " is incorrect");
        
        String stream = driver.findElement(By.xpath("//body/div/div[2]/table/tbody/tr[8]/td[6]")).getText();
        if (stream.equals("-")) System.out.println ("Stream " + stream + " is correct");
        else System.out.println ("Stream " + stream + " is incorrect");
        
        String server = driver.findElement(By.xpath("//body/div/div[2]/table/tbody/tr[9]/td[2]")).getText();
        if (server.equals("ppfxiadp105")) System.out.println ("Server " + server + " is correct");
        else System.out.println ("Server " + server + " is incorrect");
        
        String servername = driver.findElement(By.xpath("//body/div/div[2]/table/tbody/tr[9]/td[4]")).getText();
        if (servername.equals("-")) System.out.println ("Server Name " + servername + " is correct");
        else System.out.println ("Server Name " + servername + " is incorrect");
        
        String servermanaged = driver.findElement(By.xpath("//body/div/div[2]/table/tbody/tr[9]/td[6]")).getText();
        if (servermanaged.equals("Y")) System.out.println ("Server Managed " + servermanaged + " is correct");
        else System.out.println ("Server Managed " + servermanaged + " is incorrect");
        
        String fixsession = driver.findElement(By.xpath("//body/div/div[2]/table/tbody/tr[10]/td[2]")).getText();
        if (fixsession.equals("-")) System.out.println ("Fix Session " + fixsession + " is correct");
        else System.out.println ("Fix Session " + fixsession + " is incorrect");
        
        String coveredcpty = driver.findElement(By.xpath("//body/div/div[2]/table/tbody/tr[10]/td[4]")).getText();
        if (coveredcpty.equals("ICM4SUCD")) System.out.println ("Covered Counterparty " + coveredcpty + " is correct");
        else System.out.println ("Covered Counterparty " + coveredcpty + " is incorrect");
        
        String coveredcptyuser = driver.findElement(By.xpath("//body/div/div[2]/table/tbody/tr[10]/td[6]")).getText();
        if (coveredcptyuser.equals("API1")) System.out.println ("Covered Counterparty User " + coveredcptyuser + " is correct");
        else System.out.println ("Covered Counterparty User " + coveredcptyuser + " is incorrect");
        
        String originatinguser = driver.findElement(By.xpath("//body/div/div[2]/table/tbody/tr[11]/td[2]")).getText();
        if (originatinguser.equals("API1")) System.out.println ("Originating User " + originatinguser + " is correct");
        else System.out.println ("Originating User " + originatinguser + " is incorrect");
        
        String originatingorg = driver.findElement(By.xpath("//body/div/div[2]/table/tbody/tr[11]/td[4]")).getText();
        if (originatingorg.equals("ICM4SUCD")) System.out.println ("Originating Org " + originatingorg + " is correct");
        else System.out.println ("Originating Org " + originatingorg + " is incorrect");
        
        String coverexecmethod = driver.findElement(By.xpath("//body/div/div[2]/table/tbody/tr[11]/td[6]")).getText();
        if (coverexecmethod.equals("LimitRangeIOC")) System.out.println ("Cover Execution Method " + coverexecmethod + " is correct");
        else System.out.println ("Cover Execution Method " + coverexecmethod + " is incorrect");
        
        String channel = driver.findElement(By.xpath("//body/div/div[2]/table/tbody/tr[12]/td[2]")).getText();
        if (channel.equals("BA/ESP")) System.out.println ("Channel " + channel + " is correct");
        else System.out.println ("Channel " + channel + " is incorrect");
        
        String pricedisplay = driver.findElement(By.xpath("//body/div/div[2]/table/tbody/tr[12]/td[4]")).getText();
        if (pricedisplay.equals("N/A")) System.out.println ("Price Display " + pricedisplay + " is correct");
        else System.out.println ("Price Display " + pricedisplay + " is incorrect");
        
        String execstrategy = driver.findElement(By.xpath("//body/div/div[2]/table/tbody/tr[12]/td[6]")).getText();
        if (execstrategy.equals("")) System.out.println ("Execution Strategy " + execstrategy + " is correct");
        else System.out.println ("Execution Strategy " + execstrategy + " is incorrect");
        
        String fixingdate = driver.findElement(By.xpath("//body/div/div[2]/table/tbody/tr[13]/td[2]")).getText();
        if (fixingdate.equals("-")) System.out.println ("Fixing Date " + fixingdate + " is correct");
        else System.out.println ("Fixing Date " + fixingdate + " is incorrect");
        
        String portfolioid = driver.findElement(By.xpath("//body/div/div[2]/table/tbody/tr[13]/td[4]")).getText();
        if (portfolioid.equals("-")) System.out.println ("Portfolio ID " + portfolioid + " is correct");
        else System.out.println ("Portfolio ID " + portfolioid + " is incorrect");
        
        String minfillqty = driver.findElement(By.xpath("//body/div/div[2]/table/tbody/tr[13]/td[6]")).getText();
        if (minfillqty.equals("0")) System.out.println ("Min Fill Qty " + minfillqty + " is correct");
        else System.out.println ("Min Fill Qty " + minfillqty + " is incorrect");
        
        String execinstruction = driver.findElement(By.xpath("//body/div/div[2]/table/tbody/tr[14]/td[2]")).getText();
        if (execinstruction.equals("-")) System.out.println ("Execution Instruction " + execinstruction + " is correct");
        else System.out.println ("Execution Instruction " + execinstruction + " is incorrect");
        
        String exectype = driver.findElement(By.xpath("//body/div/div[2]/table/tbody/tr[14]/td[4]")).getText();
        if (exectype.equals("-")) System.out.println ("Execution Type " + exectype + " is correct");
        else System.out.println ("Execution Type " + exectype + " is incorrect");
        
        String showamount = driver.findElement(By.xpath("//body/div/div[2]/table/tbody/tr[14]/td[6]")).getText();
        if (showamount.equals("0")) System.out.println ("Show Amount " + showamount + " is correct");
        else System.out.println ("Show Amount " + showamount + " is incorrect");
        
        String preferredproviders = driver.findElement(By.xpath("//body/div/div[2]/table/tbody/tr[15]/td[2]")).getText();
        if (preferredproviders.equals("-")) System.out.println ("Preferred Providers " + preferredproviders + " is correct");
        else System.out.println ("Preferred Providers " + preferredproviders + " is incorrect");
        
        String customespotspreads = driver.findElement(By.xpath("//body/div/div[2]/table/tbody/tr[15]/td[4]")).getText();
        if (customespotspreads.equals("-")) System.out.println ("Custom Spot Spreads " + customespotspreads + " is correct");
        else System.out.println ("Custom Spot Spreads " + customespotspreads + " is incorrect");
                
        // Validate contents of Originating Orders panel        
        System.out.println("\nPanel Name:" + driver.findElement(By.xpath("//body/div[4]/div")).getText());
        System.out.println("*****************************");
        
        String column11 = driver.findElement(By.xpath("//body/div[4]/div[2]/table/thead/tr/td")).getText();
        if (column11.equals("Order ID")) System.out.println ("Column1 Heading " + column11 + " is correct");
        else System.out.println ("Column1 Heading " + column11 + " is incorrect");
        
        String column12 = driver.findElement(By.xpath("//body/div[4]/div[2]/table/thead/tr/td[2]")).getText();
        if (column12.equals("Submitted")) System.out.println ("Column2 Heading " + column12 + " is correct");
        else System.out.println ("Column2 Heading " + column12 + " is incorrect");
        
        String column13 = driver.findElement(By.xpath("//body/div[4]/div[2]/table/thead/tr/td[3]")).getText();
        if (column13.equals("Order Amount")) System.out.println ("Column3 Heading " + column13 + " is correct");
        else System.out.println ("Column3 Heading " + column13 + " is incorrect");
        
        String column14 = driver.findElement(By.xpath("//body/div[4]/div[2]/table/thead/tr/td[4]")).getText();
        if (column14.equals("Order Rate")) System.out.println ("Column4 Heading " + column14 + " is correct");
        else System.out.println ("Column4 Heading " + column14 + " is incorrect");
        
        String order11 = driver.findElement(By.xpath("//body/div[4]/div[2]/table/tbody/tr/td")).getText();
        if (order11.equals("628659112")) System.out.println ("Order1 ID " + order11 + " is correct");
        else System.out.println ("Order1 ID " + order11 + " is incorrect");
        
        String submitted11 = driver.findElement(By.xpath("//body/div[4]/div[2]/table/tbody/tr/td[2]")).getText();
        if (submitted11.equals("2014-06-22 21:06:37.806")) System.out.println ("Order1 Submitted Time " + submitted11 + " is correct");
        else System.out.println ("Order1 Submitted Time " + submitted11 + " is incorrect");
        
        String orderamount11 = driver.findElement(By.xpath("//body/div[4]/div[2]/table/tbody/tr/td[3]")).getText();
        if (orderamount11.equals("100000")) System.out.println ("Order1 Amount " + orderamount11 + " is correct");
        else System.out.println ("Order1 Amount " + orderamount11 + " is incorrect");
        
        String orderrate11 = driver.findElement(By.xpath("//body/div[4]/div[2]/table/tbody/tr/td[4]")).getText();
        if (orderrate11.equals("1.07546")) System.out.println ("Order1 Rate " + orderrate11 + " is correct");
        else System.out.println ("Order1 Rate " + orderrate11 + " is incorrect");
        
        driver.close();
        driver.switchTo().window(winHandleBefore);
        driver.quit();
        System.out.println("\nEND OF TEST CASE 11\n");
        
    }
}