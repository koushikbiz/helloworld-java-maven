import org.openqa.selenium.By;
import org.openqa.selenium.UnhandledAlertException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class EMScope_Trade_Details  {
    public static void main(String[] args) {
    	System.out.println("######################################################################");
		System.out.println("EMSCOPE AUTOMATION TEST CASE 5\n");
		System.out.println("THIS TEST CASE VERIFIES CONTENT OF TRADE DETAILS PAGE FOR A VERIFIED TRADE OF A GIVEN MARKET ORDER\n");
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
        orderId.sendKeys("543576781");
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
                
        // In the Trades table, click on the first verified trade
        WebElement orderdetailslink = driver.findElement(By.xpath("//body/div/div/div[2]/div[2]/div[10]/div/div/div/div/div/div[4]/table/tbody/tr/td/a"));
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
        	System.out.println("Problem fetching Trade Details");	
        }
        
        // Validate contents of Trade Details panel
        System.out.println("ENTERING TRADE DETAILS WINDOW\n");
        System.out.println("Panel Name:" + driver.findElement(By.xpath("//body/div/div")).getText());
        System.out.println("************************");
        
        String ccypair = driver.findElement(By.xpath("//body/div/div[2]/table/tbody/tr/td[2]")).getText();
        if (ccypair.equals("USD/CAD")) System.out.println ("Ccy Pair " + ccypair + " is correct");
        else System.out.println ("Ccy Pair " + ccypair + " is incorrect");
        
        String buysell = driver.findElement(By.xpath("//body/div/div[2]/table/tbody/tr/td[4]")).getText();
        if (buysell.equals("Sell")) System.out.println ("Buy/Sell " + buysell + " is correct");
        else System.out.println ("Buy/Sell " + buysell + " is incorrect");
        
        String rate = driver.findElement(By.xpath("//body/div/div[2]/table/tbody/tr/td[6]")).getText();
        if (rate.equals("1.08809")) System.out.println ("Trade Rate " + rate + " is correct");
        else System.out.println ("Trade Rate " + rate + " is incorrect");
        
        String baseamount = driver.findElement(By.xpath("//body/div/div[2]/table/tbody/tr[2]/td[2]")).getText();
        if (baseamount.equals("1,000,000.00")) System.out.println ("Base Amount " + baseamount + " is correct");
        else System.out.println ("Base Amount " + baseamount + " is incorrect");
        
        String termamount = driver.findElement(By.xpath("//body/div/div[2]/table/tbody/tr[2]/td[4]")).getText();
        if (termamount.equals("1,088,090.00")) System.out.println ("Term Amount " + termamount + " is correct");
        else System.out.println ("Term Amount " + termamount + " is incorrect");
        
        String tradeid = driver.findElement(By.xpath("//body/div/div[2]/table/tbody/tr[2]/td[6]")).getText();
        if (tradeid.equals("FXI905425278C")) System.out.println ("Trade ID " + tradeid + " is correct");
        else System.out.println ("Trade ID " + tradeid + " is incorrect");
        
        String counterparty = driver.findElement(By.xpath("//body/div/div[2]/table/tbody/tr[3]/td[2]")).getText();
        if (counterparty.equals("CHPT")) System.out.println ("Counterparty " + counterparty + " is correct");
        else System.out.println ("Counterparty " + counterparty + " is incorrect");
        
        String exectime = driver.findElement(By.xpath("//body/div/div[2]/table/tbody/tr[3]/td[4]")).getText();
        if (exectime.equals("2014-05-12 12:44:30.465")) System.out.println ("Execution TIme " + exectime + " is correct");
        else System.out.println ("Execution TIme " + exectime + " is incorrect");
        
        String orderid = driver.findElement(By.xpath("//body/div/div[2]/table/tbody/tr[3]/td[6]")).getText();
        if (orderid.equals("543576781")) System.out.println ("Order ID " + orderid + " is correct");
        else System.out.println ("Order ID " + orderid + " is incorrect");
        
        String spotrate = driver.findElement(By.xpath("//body/div/div[2]/table/tbody/tr[4]/td[2]")).getText();
        if (spotrate.equals("1.08809")) System.out.println ("Spot Rate " + spotrate + " is correct");
        else System.out.println ("Spot Rate " + spotrate + " is incorrect");
        
        String type = driver.findElement(By.xpath("//body/div/div[2]/table/tbody/tr[4]/td[4]")).getText();
        if (type.equals("FXSpot")) System.out.println ("Type " + type + " is correct");
        else System.out.println ("Type " + type + " is incorrect");
        
        String tradedate = driver.findElement(By.xpath("//body/div/div[2]/table/tbody/tr[4]/td[6]")).getText();
        if (tradedate.equals("2014-05-12")) System.out.println ("Trade Date " + tradedate + " is correct");
        else System.out.println ("Trade Date " + tradedate + " is incorrect");
        
        String tradestatus = driver.findElement(By.xpath("//body/div/div[2]/table/tbody/tr[5]/td[2]")).getText();
        if (tradestatus.equals("Confirmed")) System.out.println ("Trade Status " + tradestatus + " is correct");
        else System.out.println ("Trade Status " + tradestatus + " is incorrect");
        
        String usdamount = driver.findElement(By.xpath("//body/div/div[2]/table/tbody/tr[5]/td[4]")).getText();
        if (usdamount.equals("1,000,000.00")) System.out.println ("USD Amount " + usdamount + " is correct");
        else System.out.println ("USD Amount " + usdamount + " is incorrect");
        
        String organization = driver.findElement(By.xpath("//body/div/div[2]/table/tbody/tr[5]/td[6]")).getText();
        if (organization.equals("CITIP")) System.out.println ("Organization " + organization + " is correct");
        else System.out.println ("Organization " + organization + " is incorrect");
        
        String matchedrate = driver.findElement(By.xpath("//body/div/div[2]/table/tbody/tr[6]/td[2]")).getText();
        if (matchedrate.equals("1.08809")) System.out.println ("Matched Rate " + matchedrate + " is correct");
        else System.out.println ("Matched Rate " + matchedrate + " is incorrect");
        
        String cptyaccount = driver.findElement(By.xpath("//body/div/div[2]/table/tbody/tr[6]/td[4]")).getText();
        if (cptyaccount.equals("CHPT")) System.out.println ("Counterpaty Account " + cptyaccount + " is correct");
        else System.out.println ("Counterpaty Account " + cptyaccount + " is incorrect");
        
        String workflow = driver.findElement(By.xpath("//body/div/div[2]/table/tbody/tr[6]/td[6]")).getText();
        if (workflow.equals("ESP")) System.out.println ("Trade Workflow " + workflow + " is correct");
        else System.out.println ("Trade Workflow " + workflow + " is incorrect");
        
        String stream = driver.findElement(By.xpath("//body/div/div[2]/table/tbody/tr[7]/td[2]")).getText();
        if (stream.equals("StreamNY")) System.out.println ("Stream " + stream + " is correct");
        else System.out.println ("Stream " + stream + " is incorrect");
        
        String origcptyuser = driver.findElement(By.xpath("//body/div/div[2]/table/tbody/tr[7]/td[4]")).getText();
        if (origcptyuser.equals("jstapleton")) System.out.println ("Originating Cpty User " + origcptyuser + " is correct");
        else System.out.println ("Originating Cpty User " + origcptyuser + " is incorrect");
        
        String valuedate = driver.findElement(By.xpath("//body/div/div[2]/table/tbody/tr[7]/td[6]")).getText();
        if (valuedate.equals("2014-05-13")) System.out.println ("Value Date " + valuedate + " is correct");
        else System.out.println ("Value Date " + valuedate + " is incorrect");
        
        String fixingdate = driver.findElement(By.xpath("//body/div/div[2]/table/tbody/tr[8]/td[2]")).getText();
        if (fixingdate.equals("-")) System.out.println ("Fixing Date " + fixingdate + " is correct");
        else System.out.println ("Fixing Date " + fixingdate + " is incorrect");
        
        String salesdealeraccount = driver.findElement(By.xpath("//body/div/div[2]/table/tbody/tr[8]/td[4]")).getText();
        if (salesdealeraccount.equals("-")) System.out.println ("Sales Dealer Account " + salesdealeraccount + " is correct");
        else System.out.println ("Sales Dealer Account " + salesdealeraccount + " is incorrect");
        
        String salesdealeruser = driver.findElement(By.xpath("//body/div/div[2]/table/tbody/tr[8]/td[6]")).getText();
        if (salesdealeruser.equals("-")) System.out.println ("Sales Dealer User " + salesdealeruser + " is correct");
        else System.out.println ("Sales Dealer User " + salesdealeruser + " is incorrect");
        
        String primebrokeracct = driver.findElement(By.xpath("//body/div/div[2]/table/tbody/tr[9]/td[2]")).getText();
        if (primebrokeracct.equals("-")) System.out.println ("Prime Broker Account " + primebrokeracct + " is correct");
        else System.out.println ("Prime Broker Account " + primebrokeracct + " is incorrect");
        
        String pimebroker = driver.findElement(By.xpath("//body/div/div[2]/table/tbody/tr[9]/td[4]")).getText();
        if (pimebroker.equals("-")) System.out.println ("Prime Broker " + pimebroker + " is correct");
        else System.out.println ("Prime Broker " + pimebroker + " is incorrect");
        
        String makerorg = driver.findElement(By.xpath("//body/div/div[2]/table/tbody/tr[9]/td[6]")).getText();
        if (makerorg.equals("CHPT")) System.out.println ("Maker Org " + makerorg + " is correct");
        else System.out.println ("Maker Org " + makerorg + " is incorrect");
        
        String virtualserver = driver.findElement(By.xpath("//body/div/div[2]/table/tbody/tr[10]/td[2]")).getText();
        if (virtualserver.equals("OA-FMA")) System.out.println ("Virtual Server " + virtualserver + " is correct");
        else System.out.println ("Virtual Server " + virtualserver + " is incorrect");
        
        String tkrrefid = driver.findElement(By.xpath("//body/div/div[2]/table/tbody/tr[10]/td[4]")).getText();
        if (tkrrefid.equals("543576780")) System.out.println ("Taker Ref ID " + tkrrefid + " is correct");
        else System.out.println ("Taker Ref ID " + tkrrefid + " is incorrect");
        
        String mkrrefid = driver.findElement(By.xpath("//body/div/div[2]/table/tbody/tr[10]/td[6]")).getText();
        if (mkrrefid.equals("CHOPPER-FXI905425278C-2")) System.out.println ("Maker Ref ID " + mkrrefid + " is correct");
        else System.out.println ("Maker Ref ID " + mkrrefid + " is incorrect");
        
        String assettype = driver.findElement(By.xpath("//body/div/div[2]/table/tbody/tr[11]/td[2]")).getText();
        if (assettype.equals("FX")) System.out.println ("Asset Type " + assettype + " is correct");
        else System.out.println ("Asset Type " + assettype + " is incorrect");
        
        String ordertype = driver.findElement(By.xpath("//body/div/div[2]/table/tbody/tr[11]/td[4]")).getText();
        if (ordertype.equals("MARKET")) System.out.println ("Order Type " + ordertype + " is correct");
        else System.out.println ("Order Type " + ordertype + " is incorrect");
        
        String classification = driver.findElement(By.xpath("//body/div/div[2]/table/tbody/tr[11]/td[6]")).getText();
        if (classification.equals("FXSpot")) System.out.println ("Trade Classification " + classification + " is correct");
        else System.out.println ("Trade Classification " + classification + " is incorrect");
        
        String extdealid = driver.findElement(By.xpath("//body/div/div[2]/table/tbody/tr[12]/td[2]")).getText();
        if (extdealid.equals("CHOPPER-FXI905425278C-2")) System.out.println ("External Deal ID " + extdealid + " is correct");
        else System.out.println ("External Deal ID " + extdealid + " is incorrect");
        
        String extorderid = driver.findElement(By.xpath("//body/div/div[2]/table/tbody/tr[12]/td[4]")).getText();
        if (extorderid.equals("-")) System.out.println ("External Order ID " + extorderid + " is correct");
        else System.out.println ("External Order ID " + extorderid + " is incorrect");
        
        String cptyFI = driver.findElement(By.xpath("//body/div/div[2]/table/tbody/tr[12]/td[6]")).getText();
        if (cptyFI.equals("CHPT")) System.out.println ("Counter Party(FI) " + cptyFI + " is correct");
        else System.out.println ("Counter Party(FI) " + cptyFI + " is incorrect");
        
        String server = driver.findElement(By.xpath("//body/div/div[2]/table/tbody/tr[13]/td[2]")).getText();
        if (server.equals("ppfxiadp187.nyc.dc.integral.net")) System.out.println ("Server " + server + " is correct");
        else System.out.println ("Server " + server + " is incorrect");
        
        String fresherquote = driver.findElement(By.xpath("//body/div/div[2]/table/tbody/tr[13]/td[4]")).getText();
        if (fresherquote.equals("N")) System.out.println ("Fresher Quote Used " + fresherquote + " is correct");
        else System.out.println ("Fresher Quote Used " + fresherquote + " is incorrect");
        
        String nextraterecvd = driver.findElement(By.xpath("//body/div/div[2]/table/tbody/tr[13]/td[6]")).getText();
        if (nextraterecvd.equals("N/A")) System.out.println ("Next Rate Received " + nextraterecvd + " is correct");
        else System.out.println ("Next Rate Received " + nextraterecvd + " is incorrect");
        
        String stdreason = driver.findElement(By.xpath("//body/div/div[2]/table/tbody/tr[14]/td[2]")).getText();
        if (stdreason.equals("-")) System.out.println ("Standard Reason " + stdreason + " is correct");
        else System.out.println ("Standard Reason " + stdreason + " is incorrect");
        
        String historicrate = driver.findElement(By.xpath("//body/div/div[2]/table/tbody/tr[14]/td[4]")).getText();
        if (historicrate.equals("N")) System.out.println ("Historic Rate " + historicrate + " is correct");
        else System.out.println ("Historic Rate " + historicrate + " is incorrect");
        
        String nextrate = driver.findElement(By.xpath("//body/div/div[2]/table/tbody/tr[14]/td[6]")).getText();
        if (nextrate.equals("N/A")) System.out.println ("Next Rate " + nextrate + " is correct");
        else System.out.println ("Next Rate " + nextrate + " is incorrect");
        
        String clienttag = driver.findElement(By.xpath("//body/div/div[2]/table/tbody/tr[15]/td[2]")).getText();
        if (clienttag.equals("-")) System.out.println ("Client Tag " + clienttag + " is correct");
        else System.out.println ("Client Tag " + clienttag + " is incorrect");
        
        String orgLEI = driver.findElement(By.xpath("//body/div/div[2]/table/tbody/tr[15]/td[4]")).getText();
        if (orgLEI.equals("-")) System.out.println ("Organization LEI " + orgLEI + " is correct");
        else System.out.println ("Organization LEI " + orgLEI + " is incorrect");
        
        String provLEI = driver.findElement(By.xpath("//body/div/div[2]/table/tbody/tr[15]/td[6]")).getText();
        if (provLEI.equals("-")) System.out.println ("Provider LEI " + provLEI + " is correct");
        else System.out.println ("Provider LEI " + provLEI + " is incorrect");
        
        String nettradeid = driver.findElement(By.xpath("//body/div/div[2]/table/tbody/tr[16]/td[2]")).getText();
        if (nettradeid.equals("-")) System.out.println ("Net Trade ID " + nettradeid + " is correct");
        else System.out.println ("Net Trade ID " + nettradeid + " is incorrect");
        
        String portfolioid = driver.findElement(By.xpath("//body/div/div[2]/table/tbody/tr[16]/td[4]")).getText();
        if (portfolioid.equals("OA-FMA")) System.out.println ("Portfolio ID " + portfolioid + " is correct");
        else System.out.println ("Portfolio ID " + portfolioid + " is incorrect");
        
        String comment = driver.findElement(By.xpath("//body/div/div[2]/table/tbody/tr[16]/td[6]")).getText();
        if (comment.equals("-")) System.out.println ("Comment " + comment + " is correct");
        else System.out.println ("Comment " + comment + " is incorrect");
        
        String UPI = driver.findElement(By.xpath("//body/div/div[2]/table/tbody/tr[17]/td[2]")).getText();
        if (UPI.equals("USD_CAD_SPOT")) System.out.println ("UPI " + UPI + " is correct");
        else System.out.println ("UPI " + UPI + " is incorrect");
        
        String USI = driver.findElement(By.xpath("//body/div/div[2]/table/tbody/tr[17]/td[4]")).getText();
        if (USI.equals("-")) System.out.println ("USI " + USI + " is correct");
        else System.out.println ("USI " + USI + " is incorrect");
        
        String UTI = driver.findElement(By.xpath("//body/div/div[2]/table/tbody/tr[17]/td[6]")).getText();
        if (UTI.equals("-")) System.out.println ("UTI " + UTI + " is correct");
        else System.out.println ("UTI " + UTI + " is incorrect");
        
        String channel = driver.findElement(By.xpath("//body/div/div[2]/table/tbody/tr[18]/td[2]")).getText();
        if (channel.equals("DNET/ESP/FXID/FMA")) System.out.println ("Channel " + channel + " is correct");
        else System.out.println ("Channel " + channel + " is incorrect");
        
        String dealer = driver.findElement(By.xpath("//body/div/div[2]/table/tbody/tr[18]/td[4]")).getText();
        if (dealer.equals("CITIPQuoter")) System.out.println ("Dealer " + dealer + " is correct");
        else System.out.println ("Dealer " + dealer + " is incorrect");
        
        // Validate contents of Market Snapshot panel        
        System.out.println("\nPanel Name:" + driver.findElement(By.xpath("//body/div[2]/div")).getText());
        System.out.println("************************");
        
        String column1 = driver.findElement(By.xpath("//body/div[2]/div[2]/table/thead/tr/td")).getText();
        if (column1.equals("Time Stamp")) System.out.println ("Column1 Heading " + column1 + " is correct");
        else System.out.println ("Column1 Heading " + column1 + " is incorrect");
        
        String column2 = driver.findElement(By.xpath("//body/div[2]/div[2]/table/thead/tr/td[2]")).getText();
        if (column2.equals("Provider")) System.out.println ("Column2 Heading " + column2 + " is correct");
        else System.out.println ("Column2 Heading " + column2 + " is incorrect");
        
        String column3 = driver.findElement(By.xpath("//body/div[2]/div[2]/table/thead/tr/td[3]")).getText();
        if (column3.equals("Bid Size")) System.out.println ("Column3 Heading " + column3 + " is correct");
        else System.out.println ("Column3 Heading " + column3 + " is incorrect");
        
        String column4 = driver.findElement(By.xpath("//body/div[2]/div[2]/table/thead/tr/td[4]")).getText();
        if (column4.equals("Tier")) System.out.println ("Column4 Heading " + column4 + " is correct");
        else System.out.println ("Column4 Heading " + column4 + " is incorrect");
        
        String column5 = driver.findElement(By.xpath("//body/div[2]/div[2]/table/thead/tr/td[5]")).getText();
        if (column5.equals("Bid Rate")) System.out.println ("Column5 Heading " + column5 + " is correct");
        else System.out.println ("Column5 Heading " + column5 + " is incorrect");
        
        String column6 = driver.findElement(By.xpath("//body/div[2]/div[2]/table/thead/tr/td[6]")).getText();
        if (column6.equals("Offer Rate")) System.out.println ("Column6 Heading " + column6 + " is correct");
        else System.out.println ("Column6 Heading " + column6 + " is incorrect");
        
        String column7 = driver.findElement(By.xpath("//body/div[2]/div[2]/table/thead/tr/td[7]")).getText();
        if (column7.equals("Tier")) System.out.println ("Column7 Heading " + column7 + " is correct");
        else System.out.println ("Column7 Heading " + column7 + " is incorrect");
        
        String column8 = driver.findElement(By.xpath("//body/div[2]/div[2]/table/thead/tr/td[8]")).getText();
        if (column8.equals("Offer Size")) System.out.println ("Column8 Heading " + column8 + " is correct");
        else System.out.println ("Column8 Heading " + column8 + " is incorrect");
        
        String column9 = driver.findElement(By.xpath("//body/div[2]/div[2]/table/thead/tr/td[9]")).getText();
        if (column9.equals("Provider")) System.out.println ("Column9 Heading " + column9 + " is correct");
        else System.out.println ("Column9 Heading " + column9 + " is incorrect");
        
        String column10 = driver.findElement(By.xpath("//body/div[2]/div[2]/table/thead/tr/td[10]")).getText();
        if (column10.equals("TimeStamp")) System.out.println ("Column10 Heading " + column10 + " is correct");
        else System.out.println ("Column10 Heading " + column10 + " is incorrect"); 
        
        String provrateused = driver.findElement(By.xpath("//body/div[2]/div[2]/table/tbody/tr[8]/td[5]/strong")).getText();
        if (provrateused.equals("1.08809")) System.out.println ("Provier Rate Used for Trade " + provrateused + " is highlighted");
        
        String provused = driver.findElement(By.xpath("//body/div[2]/div[2]/table/tbody/tr[8]/td[2]")).getText();
        if (provused.equals("P491")) System.out.println ("Provier Used for Trade " + provused + " is highlighted");
        
        String usedratetimestmp = driver.findElement(By.xpath("//body/div[2]/div[2]/table/tbody/tr[8]/td")).getText();
        if (usedratetimestmp.equals("2014-05-12 12:44:22.152")) System.out.println ("Timestamp of Rate Used " + usedratetimestmp + " is highlighted");
        
        String bid1timestamp = driver.findElement(By.xpath("//body/div[2]/div[2]/table/tbody/tr/td")).getText();
        if (bid1timestamp.equals("2014-05-12 12:44:21.800")) System.out.println ("Timestamp of Bid1 Rate " + bid1timestamp + " is correct");
        else System.out.println ("Timestamp of Bid1 Rate " + bid1timestamp + " is incorrect");
        
        String bid1provider = driver.findElement(By.xpath("//body/div[2]/div[2]/table/tbody/tr/td[2]")).getText();
        if (bid1provider.equals("P580")) System.out.println ("Provider of Bid1 Rate " + bid1provider + " is correct");
        else System.out.println ("Provider of Bid1 Rate " + bid1provider + " is incorrect");
        
        String bid1size = driver.findElement(By.xpath("//body/div[2]/div[2]/table/tbody/tr/td[3]")).getText();
        if (bid1size.equals("500,000.00")) System.out.println ("Bid1 Size " + bid1size + " is correct");
        else System.out.println ("Bid1 Size " + bid1size + " is incorrect");
        
        String bid1tier = driver.findElement(By.xpath("//body/div[2]/div[2]/table/tbody/tr/td[4]")).getText();
        if (bid1tier.equals("1")) System.out.println ("Bid1 Tier " + bid1tier + " is correct");
        else System.out.println ("Bid1 Tier " + bid1tier + " is incorrect");
        
        String bid1rate = driver.findElement(By.xpath("//body/div[2]/div[2]/table/tbody/tr/td[5]/strong")).getText();
        if (bid1rate.equals("1.0881")) System.out.println ("Bid1 Rate(Top of the book) " + bid1rate + " is correct");
        else System.out.println ("Bid1 Rate(Top of the book) " + bid1rate + " is incorrect");
        
        String offer1rate = driver.findElement(By.xpath("//body/div[2]/div[2]/table/tbody/tr/td[6]/strong")).getText();
        if (offer1rate.equals("1.08819")) System.out.println ("Offer1 Rate(Top of the book) " + offer1rate + " is correct");
        else System.out.println ("Offer1 Rate(Top of the book) " + offer1rate + " is incorrect");
        
        String offer1tier = driver.findElement(By.xpath("//body/div[2]/div[2]/table/tbody/tr/td[7]")).getText();
        if (offer1tier.equals("1")) System.out.println ("Offer1 Tier " + offer1tier + " is correct");
        else System.out.println ("Offer1 Tier " + offer1tier + " is incorrect");
        
        String offer1size = driver.findElement(By.xpath("//body/div[2]/div[2]/table/tbody/tr/td[8]")).getText();
        if (offer1size.equals("3,250,000.00")) System.out.println ("Offer1 Size " + offer1size + " is correct");
        else System.out.println ("Offer1 Size " + offer1size + " is incorrect");
        
        String offer1provider = driver.findElement(By.xpath("//body/div[2]/div[2]/table/tbody/tr/td[9]")).getText();
        if (offer1provider.equals("P820")) System.out.println ("Provider of Offer1 Rate " + offer1provider + " is correct");
        else System.out.println ("Provider of Offer1 Rate " + offer1provider + " is incorrect");
        
        String offer1timestamp = driver.findElement(By.xpath("//body/div[2]/div[2]/table/tbody/tr/td[10]")).getText();
        if (offer1timestamp.equals("2014-05-12 12:44:14.304")) System.out.println ("Timestamp of Offer1 Rate " + offer1timestamp + " is correct");
        else System.out.println ("Timestamp of Offer1 Rate " + offer1timestamp + " is incorrect");
        
        String bid2timestamp = driver.findElement(By.xpath("//body/div[2]/div[2]/table/tbody/tr[2]/td")).getText();
        if (bid2timestamp.equals("2014-05-12 12:44:16.314")) System.out.println ("Timestamp of Bid2 Rate " + bid2timestamp + " is correct");
        else System.out.println ("Timestamp of Bid2 Rate " + bid2timestamp + " is incorrect");
        
        String bid2provider = driver.findElement(By.xpath("//body/div[2]/div[2]/table/tbody/tr[2]/td[2]")).getText();
        if (bid2provider.equals("B140")) System.out.println ("Provider of Bid2 Rate " + bid2provider + " is correct");
        else System.out.println ("Provider of Bid2 Rate " + bid2provider + " is incorrect");
        
        String bid2size = driver.findElement(By.xpath("//body/div[2]/div[2]/table/tbody/tr[2]/td[3]")).getText();
        if (bid2size.equals("1,000,000.00")) System.out.println ("Bid2 Size " + bid2size + " is correct");
        else System.out.println ("Bid2 Size " + bid2size + " is incorrect");
        
        String bid2tier = driver.findElement(By.xpath("//body/div[2]/div[2]/table/tbody/tr[2]/td[4]")).getText();
        if (bid2tier.equals("1")) System.out.println ("Bid2 Tier " + bid2tier + " is correct");
        else System.out.println ("Bid2 Tier " + bid2tier + " is incorrect");
        
        String bid2rate = driver.findElement(By.xpath("//body/div[2]/div[2]/table/tbody/tr[2]/td[5]/strong")).getText();
        if (bid2rate.equals("1.0881")) System.out.println ("Bid2 Rate(Top of the book) " + bid2rate + " is correct");
        else System.out.println ("Bid2 Rate(Top of the book) " + bid2rate + " is incorrect");
        
        String offer2rate = driver.findElement(By.xpath("//body/div[2]/div[2]/table/tbody/tr[2]/td[6]/strong")).getText();
        if (offer2rate.equals("1.08819")) System.out.println ("Offer2 Rate(Top of the book) " + offer2rate + " is correct");
        else System.out.println ("Offer2 Rate(Top of the book) " + offer2rate + " is incorrect");
        
        String offer2tier = driver.findElement(By.xpath("//body/div[2]/div[2]/table/tbody/tr[2]/td[7]")).getText();
        if (offer2tier.equals("1")) System.out.println ("Offer2 Tier " + offer2tier + " is correct");
        else System.out.println ("Offer2 Tier " + offer2tier + " is incorrect");
        
        String offer2size = driver.findElement(By.xpath("//body/div[2]/div[2]/table/tbody/tr[2]/td[8]")).getText();
        if (offer2size.equals("1,000,000.00")) System.out.println ("Offer2 Size " + offer2size + " is correct");
        else System.out.println ("Offer2 Size " + offer2size + " is incorrect");
        
        String offer2provider = driver.findElement(By.xpath("//body/div[2]/div[2]/table/tbody/tr[2]/td[9]")).getText();
        if (offer2provider.equals("P921")) System.out.println ("Provider of Offer2 Rate " + offer2provider + " is correct");
        else System.out.println ("Provider of Offer2 Rate " + offer2provider + " is incorrect");
        
        String offer2timestamp = driver.findElement(By.xpath("//body/div[2]/div[2]/table/tbody/tr[2]/td[10]")).getText();
        if (offer2timestamp.equals("2014-05-12 12:44:24.293")) System.out.println ("Timestamp of Offer2 Rate " + offer2timestamp + " is correct");
        else System.out.println ("Timestamp of Offer2 Rate " + offer2timestamp + " is incorrect");
        
        String bid3timestamp = driver.findElement(By.xpath("//body/div[2]/div[2]/table/tbody/tr[3]/td")).getText();
        if (bid3timestamp.equals("2014-05-12 12:44:24.592")) System.out.println ("Timestamp of Bid3 Rate " + bid3timestamp + " is correct");
        else System.out.println ("Timestamp of Bid3 Rate " + bid3timestamp + " is incorrect");
        
        String bid3provider = driver.findElement(By.xpath("//body/div[2]/div[2]/table/tbody/tr[3]/td[2]")).getText();
        if (bid3provider.equals("B260")) System.out.println ("Provider of Bid3 Rate " + bid3provider + " is correct");
        else System.out.println ("Provider of Bid3 Rate " + bid3provider + " is incorrect");
        
        String bid3size = driver.findElement(By.xpath("//body/div[2]/div[2]/table/tbody/tr[3]/td[3]")).getText();
        if (bid3size.equals("500,000.00")) System.out.println ("Bid3 Size " + bid3size + " is correct");
        else System.out.println ("Bid3 Size " + bid3size + " is incorrect");
        
        String bid3tier = driver.findElement(By.xpath("//body/div[2]/div[2]/table/tbody/tr[3]/td[4]")).getText();
        if (bid3tier.equals("1")) System.out.println ("Bid3 Tier " + bid3tier + " is correct");
        else System.out.println ("Bid3 Tier " + bid3tier + " is incorrect");
        
        String bid3rate = driver.findElement(By.xpath("//body/div[2]/div[2]/table/tbody/tr[3]/td[5]/strong")).getText();
        if (bid3rate.equals("1.0881")) System.out.println ("Bid3 Rate(Top of the book) " + bid3rate + " is correct");
        else System.out.println ("Bid3 Rate(Top of the book) " + bid3rate + " is incorrect");
        
        String offer3rate = driver.findElement(By.xpath("//body/div[2]/div[2]/table/tbody/tr[3]/td[6]/strong")).getText();
        if (offer3rate.equals("1.08819")) System.out.println ("Offer3 Rate(Top of the book) " + offer3rate + " is correct");
        else System.out.println ("Offer3 Rate(Top of the book) " + offer3rate + " is incorrect");
        
        String offer3tier = driver.findElement(By.xpath("//body/div[2]/div[2]/table/tbody/tr[3]/td[7]")).getText();
        if (offer3tier.equals("1")) System.out.println ("Offer3 Tier " + offer3tier + " is correct");
        else System.out.println ("Offer3 Tier " + offer3tier + " is incorrect");
        
        String offer3size = driver.findElement(By.xpath("//body/div[2]/div[2]/table/tbody/tr[3]/td[8]")).getText();
        if (offer3size.equals("1,000,000.00")) System.out.println ("Offer3 Size " + offer3size + " is correct");
        else System.out.println ("Offer3 Size " + offer3size + " is incorrect");
        
        String offer3provider = driver.findElement(By.xpath("//body/div[2]/div[2]/table/tbody/tr[3]/td[9]")).getText();
        if (offer3provider.equals("B930")) System.out.println ("Provider of Offer3 Rate " + offer3provider + " is correct");
        else System.out.println ("Provider of Offer3 Rate " + offer3provider + " is incorrect");
        
        String offer3timestamp = driver.findElement(By.xpath("//body/div[2]/div[2]/table/tbody/tr[3]/td[10]")).getText();
        if (offer3timestamp.equals("2014-05-12 12:44:24.352")) System.out.println ("Timestamp of Offer3 Rate " + offer3timestamp + " is correct");
        else System.out.println ("Timestamp of Offer3 Rate " + offer3timestamp + " is incorrect");
        
        // Validate contents of Audit Trail panel        
        System.out.println("\nPanel Name:" + driver.findElement(By.xpath("//body/div[3]/div[1]")).getText());
        System.out.println("*****************************");
        
        String column11 = driver.findElement(By.xpath("//body/div[3]/div[2]/table/thead/tr/td[1]")).getText();
        if (column11.equals("Event")) System.out.println ("Column1 Heading " + column11 + " is correct");
        else System.out.println ("Column1 Heading " + column11 + " is incorrect");
        
        String column12 = driver.findElement(By.xpath("//body/div[3]/div[2]/table/thead/tr/td[2]")).getText();
        if (column12.equals("Quote ID")) System.out.println ("Column2 Heading " + column12 + " is correct");
        else System.out.println ("Column2 Heading " + column12 + " is incorrect"); 
        
        String column13 = driver.findElement(By.xpath("//body/div[3]/div[2]/table/thead/tr/td[3]")).getText();
        if (column13.equals("Timestamp")) System.out.println ("Column3 Heading " + column13 + " is correct");
        else System.out.println ("Column3 Heading " + column13 + " is incorrect");
        
        String event1 = driver.findElement(By.xpath("//body/div[3]/div[2]/table/tbody/tr[1]/td[1]")).getText();
        if (event1.equals("RateEffective")) System.out.println ("Event1:: " + event1 + " is correct");
        else System.out.println ("Event1:: " + event1 + " is incorrect");
        
        String event2 = driver.findElement(By.xpath("//body/div[3]/div[2]/table/tbody/tr[2]/td[1]")).getText();
        if (event2.equals("RateReceivedByAdapter (1.08809/1.0882)")) System.out.println ("Event2:: " + event2 + " is correct");
        else System.out.println ("Event2:: " + event2 + " is incorrect");
        
        String event3 = driver.findElement(By.xpath("//body/div[3]/div[2]/table/tbody/tr[3]/td[1]")).getText();
        if (event3.equals("RateSentByAdapter")) System.out.println ("Event3:: " + event3 + " is correct");
        else System.out.println ("Event3:: " + event3 + " is incorrect");
        
        String event4 = driver.findElement(By.xpath("//body/div[3]/div[2]/table/tbody/tr[4]/td[1]")).getText();
        if (event4.equals("RateReceivedByIS")) System.out.println ("Event4:: " + event4 + " is correct");
        else System.out.println ("Event4:: " + event4 + " is incorrect");
        
        String event5 = driver.findElement(By.xpath("//body/div[3]/div[2]/table/tbody/tr[5]/td[1]")).getText();
        if (event5.equals("QuoteCreated")) System.out.println ("Event5:: " + event5 + " is correct");
        else System.out.println ("Event5:: " + event5 + " is incorrect");
        
        String event6 = driver.findElement(By.xpath("//body/div[3]/div[2]/table/tbody/tr[6]/td[1]")).getText();
        if (event6.equals("RateSentToClient")) System.out.println ("Event6:: " + event6 + " is correct");
        else System.out.println ("Event6:: " + event6 + " is incorrect");
        
        String event7 = driver.findElement(By.xpath("//body/div[3]/div[2]/table/tbody/tr[7]/td[1]")).getText();
        if (event7.equals("ClientSendAcceptance")) System.out.println ("Event7:: " + event7 + " is correct");
        else System.out.println ("Event7:: " + event7 + " is incorrect");
        
        String event8 = driver.findElement(By.xpath("//body/div[3]/div[2]/table/tbody/tr[8]/td[1]")).getText();
        if (event8.equals("OrderRecivedByServer")) System.out.println ("Event8:: " + event8 + " is correct");
        else System.out.println ("Event8:: " + event8 + " is incorrect");
        
        String event9 = driver.findElement(By.xpath("//body/div[3]/div[2]/table/tbody/tr[9]/td[1]")).getText();
        if (event9.equals("OrderMatchedByServer")) System.out.println ("Event9:: " + event9 + " is correct");
        else System.out.println ("Event9:: " + event9 + " is incorrect");
        
        String event10 = driver.findElement(By.xpath("//body/div[3]/div[2]/table/tbody/tr[10]/td[1]")).getText();
        if (event10.equals("AcceptanceSentByOA")) System.out.println ("Event10:: " + event10 + " is correct");
        else System.out.println ("Event10:: " + event10 + " is incorrect");
        
        String event11 = driver.findElement(By.xpath("//body/div[3]/div[2]/table/tbody/tr[11]/td[1]")).getText();
        if (event11.equals("RateAcceptedByServer")) System.out.println ("Event11:: " + event11 + " is correct");
        else System.out.println ("Event11:: " + event11 + " is incorrect");
        
        String event12 = driver.findElement(By.xpath("//body/div[3]/div[2]/table/tbody/tr[12]/td[1]")).getText();
        if (event12.equals("AcceptanceSentByIS")) System.out.println ("Event12:: " + event12 + " is correct");
        else System.out.println ("Event12:: " + event12 + " is incorrect");
        
        String event13 = driver.findElement(By.xpath("//body/div[3]/div[2]/table/tbody/tr[13]/td[1]")).getText();
        if (event13.equals("AcceptanceReceivedByAdapter")) System.out.println ("Event13:: " + event13 + " is correct");
        else System.out.println ("Event13:: " + event13 + " is incorrect");
        
        String event14 = driver.findElement(By.xpath("//body/div[3]/div[2]/table/tbody/tr[14]/td[1]")).getText();
        if (event14.equals("TradeRequestSentToProvider")) System.out.println ("Event14:: " + event14 + " is correct");
        else System.out.println ("Event14:: " + event14 + " is incorrect");
        
        String event15 = driver.findElement(By.xpath("//body/div[3]/div[2]/table/tbody/tr[15]/td[1]")).getText();
        if (event15.equals("TradeVerifiedReceivedFromProvider")) System.out.println ("Event15:: " + event15 + " is correct");
        else System.out.println ("Event15:: " + event15 + " is incorrect");
        
        String event16 = driver.findElement(By.xpath("//body/div[3]/div[2]/table/tbody/tr[16]/td[1]")).getText();
        if (event16.equals("ResponseSentByAdapter")) System.out.println ("Event16:: " + event16 + " is correct");
        else System.out.println ("Event16:: " + event16 + " is incorrect");
        
        String event17 = driver.findElement(By.xpath("//body/div[3]/div[2]/table/tbody/tr[17]/td[1]")).getText();
        if (event17.equals("VerificationReceivedByIS")) System.out.println ("Event17:: " + event17 + " is correct");
        else System.out.println ("Event17:: " + event17 + " is incorrect");
        
        String event18 = driver.findElement(By.xpath("//body/div[3]/div[2]/table/tbody/tr[18]/td[1]")).getText();
        if (event18.equals("ResponseSentByAdapter")) System.out.println ("Event18:: " + event18 + " is correct");
        else System.out.println ("Event18:: " + event18 + " is incorrect");
        
        String event19 = driver.findElement(By.xpath("//body/div[3]/div[2]/table/tbody/tr[19]/td[1]")).getText();
        if (event19.equals("ResponseSentByAdapter")) System.out.println ("Event19:: " + event19 + " is correct");
        else System.out.println ("Event19:: " + event19 + " is incorrect");
        
        String guidordmatchedevent = driver.findElement(By.xpath("//body/div[3]/div[2]/table/tbody/tr[9]/td[2]")).getText();
        if (guidordmatchedevent.equals("G-3c369d14-145f0766509-CHPTA-48f6-BHINYD-P491-51535")) System.out.println ("GUID for OrderMatchedByServer event:: " + guidordmatchedevent + " is correct");
        else System.out.println ("GUID for OrderMatchedByServer event:: " + guidordmatchedevent + " is incorrect");
        
        String timestampordmatchedevent = driver.findElement(By.xpath("//body/div[3]/div[2]/table/tbody/tr[9]/td[3]")).getText();
        if (timestampordmatchedevent.equals("2014-05-12 12:44:30.465")) System.out.println ("Timestamp for OrderMatchedByServer event:: " + timestampordmatchedevent + " is correct");
        else System.out.println ("Timestamp for OrderMatchedByServer event:: " + timestampordmatchedevent + " is incorrect");
        
        // Validate contents of Covered Trades panel        
        System.out.println("\nPanel Name:" + driver.findElement(By.xpath("//body/div[4]/div[1]")).getText());
        System.out.println("************************");
        
        String column21 = driver.findElement(By.xpath("//body/div[4]/div[2]/table/thead/tr/td[1]")).getText();
        if (column21.equals("Deal ID")) System.out.println ("Column1 Heading:: " + column21 + " is correct");
        else System.out.println ("Column1 Heading:: " + column21 + " is incorrect");
        
        String column22 = driver.findElement(By.xpath("//body/div[4]/div[2]/table/thead/tr/td[2]")).getText();
        if (column22.equals("CCY Pair")) System.out.println ("Column2 Heading:: " + column22 + " is correct");
        else System.out.println ("Column2 Heading:: " + column22 + " is incorrect");
        
        String column23 = driver.findElement(By.xpath("//body/div[4]/div[2]/table/thead/tr/td[3]")).getText();
        if (column23.equals("Execution Time")) System.out.println ("Column3 Heading:: " + column23 + " is correct");
        else System.out.println ("Column3 Heading:: " + column23 + " is incorrect");
        
        String column24 = driver.findElement(By.xpath("//body/div[4]/div[2]/table/thead/tr/td[4]")).getText();
        if (column24.equals("Base Amt")) System.out.println ("Column4 Heading:: " + column24 + " is correct");
        else System.out.println ("Column4 Heading:: " + column24 + " is incorrect");
        
        String column25 = driver.findElement(By.xpath("//body/div[4]/div[2]/table/thead/tr/td[5]")).getText();
        if (column25.equals("Term Amt")) System.out.println ("Column5 Heading:: " + column25 + " is correct");
        else System.out.println ("Column5 Heading:: " + column25 + " is incorrect");
        
        String column26 = driver.findElement(By.xpath("//body/div[4]/div[2]/table/thead/tr/td[6]")).getText();
        if (column26.equals("Rate")) System.out.println ("Column6 Heading:: " + column26 + " is correct");
        else System.out.println ("Column6 Heading:: " + column26 + " is incorrect");
        
        String column27 = driver.findElement(By.xpath("//body/div[4]/div[2]/table/thead/tr/td[7]")).getText();
        if (column27.equals("Buy/Sell")) System.out.println ("Column7 Heading:: " + column27 + " is correct");
        else System.out.println ("Column7 Heading:: " + column27 + " is incorrect");
        
        String column28 = driver.findElement(By.xpath("//body/div[4]/div[2]/table/thead/tr/td[8]")).getText();
        if (column28.equals("Status")) System.out.println ("Column8 Heading:: " + column28 + " is correct");
        else System.out.println ("Column8 Heading:: " + column28 + " is incorrect");  
        
        String ctdealid = driver.findElement(By.xpath("//body/div[4]/div[2]/table/tbody/tr/td[1]")).getText();
        if (ctdealid.equals("FXI905425278")) System.out.println ("Deal ID1 for Covered Trades:: " + ctdealid + " is correct");
        else System.out.println ("Deal ID1 for Covered Trades:: " + ctdealid + " is incorrect");
        
        String ctccypair = driver.findElement(By.xpath("//body/div[4]/div[2]/table/tbody/tr/td[2]")).getText();
        if (ctccypair.equals("USD/CAD")) System.out.println ("Ccy Pair1 for Covered Trades:: " + ctccypair + " is correct");
        else System.out.println ("Ccy Pair1 for Covered Trades:: " + ctccypair + " is incorrect");
        
        String ctexectime = driver.findElement(By.xpath("//body/div[4]/div[2]/table/tbody/tr/td[3]")).getText();
        if (ctexectime.equals("2014-05-12 18:14:30.465")) System.out.println ("Execution Timestamp1 for Covered Trades:: " + ctexectime + " is correct");
        else System.out.println ("Execution Timestamp1 for Covered Trades:: " + ctexectime + " is incorrect");
        
        String ctbaseamount = driver.findElement(By.xpath("//body/div[4]/div[2]/table/tbody/tr/td[4]")).getText();
        if (ctbaseamount.equals("1,000,000.00")) System.out.println ("Base Amount1 for Covered Trades:: " + ctbaseamount + " is correct");
        else System.out.println ("Base Amount1 for Covered Trades:: " + ctbaseamount + " is incorrect");
        
        String cttermamount = driver.findElement(By.xpath("//body/div[4]/div[2]/table/tbody/tr/td[5]")).getText();
        if (cttermamount.equals("1,088,090.00")) System.out.println ("Term Amount1 for Covered Trades:: " + cttermamount + " is correct");
        else System.out.println ("Term Amount1 for Covered Trades:: " + cttermamount + " is incorrect");
        
        String ctrate = driver.findElement(By.xpath("//body/div[4]/div[2]/table/tbody/tr/td[6]")).getText();
        if (ctrate.equals("1.08809")) System.out.println ("Rate1 for Covered Trades:: " + ctrate + " is correct");
        else System.out.println ("Rate1 for Covered Trades:: " + ctrate + " is incorrect");
        
        String ctbuysell = driver.findElement(By.xpath("//body/div[4]/div[2]/table/tbody/tr/td[7]")).getText();
        if (ctbuysell.equals("Sell")) System.out.println ("Buy/Sell1 for Covered Trades:: " + ctbuysell + " is correct");
        else System.out.println ("Buy/Sell1 for Covered Trades:: " + ctbuysell + " is incorrect");
        
        String ctstatus = driver.findElement(By.xpath("//body/div[4]/div[2]/table/tbody/tr/td[8]")).getText();
        if (ctstatus.equals("Confirmed")) System.out.println ("Status1 for Covered Trades:: " + ctstatus + " is correct");        
        else System.out.println ("Status1 for Covered Trades:: " + ctstatus + " is incorrect");
        
        driver.close();
        driver.switchTo().window(winHandleBefore);
        driver.quit();
        System.out.println("\nEND OF TEST CASE 5\n");
        
    }
}