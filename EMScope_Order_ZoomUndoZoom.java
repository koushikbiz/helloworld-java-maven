import org.openqa.selenium.By;
import org.openqa.selenium.UnhandledAlertException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class EMScope_Order_ZoomUndoZoom  {
    public static void main(String[] args) {
    	System.out.println("######################################################################");
		System.out.println("EMSCOPE AUTOMATION TEST CASE 2\n");
		System.out.println("THIS TEST CASE VERIFIES ZOOMING & UNDO ZOOMING OF ORDER CHART FOR A GIVEN ORDER\n");
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
        
        //Verify the chart content for Order/Trades
        System.out.println("\nENTERING ORDER CHART::\n");
        
        java.util.List<WebElement> list12 = driver.findElements(By.xpath("//*[@class='sub _0']")); 
        System.out.println("Printing out the Order details from order chart below:");
        System.out.println("********************************************************\n");
        for(WebElement option : list12){
        	String Summary8 = option.getText();
        	System.out.println(Summary8);
        }
                       
        // Print our the Rejected trades from order chart        
        java.util.List<WebElement> list2 = driver.findElements(By.xpath("//*[@class='sub _2']"));        
        for(WebElement option : list2){
        	String Summary7 = option.getText();
        	Summary7 = Summary7 + "2014";
            String[] elements7 = new String[100];
            
            int delta7 = 0;
            int start7 = Summary7.indexOf("2014");                        
            int end7 = Summary7.indexOf("2014", start7 + 1);
            
            System.out.println("\nPrinting out the Rejected trades from order chart below:");
            System.out.println("********************************************************\n");
            
            for (int i = 0; i<elements7.length; i++)
            {
            String p = Summary7.substring(start7, end7);
            elements7 [i] = p;
            delta7 = end7 - start7;
            start7 = start7 + delta7;
            end7 = Summary7.indexOf("2014", start7 + 1); 
            System.out.println(elements7[i]);
            if (end7==-1) break;
            }            
        }
        
        // Print our the Confirmed trades from order chart        
        java.util.List<WebElement> list3 = driver.findElements(By.xpath("//*[@class='sub _1']"));        
        for(WebElement option : list3){
        	String Summary8 = option.getText();
        	Summary8 = Summary8 + "2014";
            String[] elements8 = new String[100];
            
            int delta8 = 0;
            int start8 = Summary8.indexOf("2014");                        
            int end8 = Summary8.indexOf("2014", start8 + 1);
            
            System.out.println("\nPrinting out the Confirmed trades from order chart below:");
            System.out.println("********************************************************\n");
            
            for (int i = 0; i<elements8.length; i++)
            {
            String p = Summary8.substring(start8, end8);
            elements8 [i] = p;
            delta8 = end8 - start8;
            start8 = start8 + delta8;
            end8 = Summary8.indexOf("2014", start8 + 1); 
            System.out.println(elements8[i]);
            if (end8==-1) break;
            }            
        }        

        // Print out the Order chart interval        
        WebElement orderchart = driver.findElement(By.xpath("//body/div[1]/div/div[2]/div[2]/div[3]")); 
        String ordercharttext = orderchart.getText();
        int index1 = ordercharttext.indexOf("Benchmark Price");
        int j = index1+1;        
        for (int i = 0; i < 10; i++) {         
        int index2 = ordercharttext.indexOf("Benchmark Price",j);        
        int delta = index2 - j;
        if (delta<0) {delta = -delta; j = j + 1; break;}
        j = j + delta + 1;        
        if (index2==-1) break;
        }
        
        int k = ordercharttext.indexOf("12:44",j);
        int l = ordercharttext.indexOf("1.08",k);
        System.out.println("The first timestamp on the Time axis of order chart::" + ordercharttext.substring(k,k+12));
        System.out.println("The last timestamp on the Time axis of order chart::" + ordercharttext.substring(l-12,l));        
        
        // Click on the Zoom-out 2 seconds button
        System.out.println("\nPressing Zoom-out 2 seconds button");
        WebElement zoomout2sec = driver.findElement(By.xpath("//body/div[1]/div/div[2]/div[2]/div[2]/div[1]/button[1]"));
        zoomout2sec.click();
                        
        // Print out the new Order chart interval        
        WebElement orderchart2 = driver.findElement(By.xpath("//body/div[1]/div/div[2]/div[2]/div[3]")); 
        String ordercharttext2 = orderchart2.getText();        
        int index3 = ordercharttext2.indexOf("Benchmark Price");
        int j2 = index3+1;         
        for (int i = 0; i < 10; i++) {         
        int index4 = ordercharttext2.indexOf("Benchmark Price",j2);        
        int delta = index4 - j2;
        if (delta<0) {delta = -delta; j2 = j2 + 1; break;}
        j2 = j2 + delta + 1;        
        if (index4==-1) break;
        }
        
        int k2 = ordercharttext2.indexOf("12:44",j2);
        int l2 = ordercharttext2.indexOf("1.08",k2);
        System.out.println("\nThe first timestamp on the Time axis of new order chart::" + ordercharttext2.substring(k2,k2+12));
        System.out.println("The last timestamp on the Time axis of new order chart::" + ordercharttext2.substring(l2-12,l2));        
        
        // Click on the Undo Zoom button
        System.out.println("\nPressing Undo Zoom button");
        WebElement undozoom = driver.findElement(By.xpath("//body/div[1]/div/div[2]/div[2]/div[2]/div[1]/button[2]"));
        undozoom.click();
        
        // Print out the new Order chart interval        
        WebElement orderchart3 = driver.findElement(By.xpath("//body/div[1]/div/div[2]/div[2]/div[3]")); 
        String ordercharttext3 = orderchart3.getText();        
        int index5 = ordercharttext3.indexOf("Benchmark Price");
        int j3 = index5+1;         
        for (int i = 0; i < 10; i++) {         
        int index6 = ordercharttext3.indexOf("Benchmark Price",j3);        
        int delta = index6 - j3;
        if (delta<0) {delta = -delta; j3 = j3 + 1; break;}
        j3 = j3 + delta + 1;        
        if (index6==-1) break;
        }
        
        int k3 = ordercharttext3.indexOf("12:44",j3);
        int l3 = ordercharttext3.indexOf("0.001",k3);        
        System.out.println("\nThe first timestamp on the Time axis of new order chart::" + ordercharttext3.substring(k3,k3+12));
        System.out.println("The last timestamp on the Time axis of new order chart::" + ordercharttext3.substring(l3-12,l3));
        
        driver.quit();
        System.out.println("\nEND OF TEST CASE 2\n");
        
    }
}
