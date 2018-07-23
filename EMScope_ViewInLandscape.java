import org.openqa.selenium.By;
import org.openqa.selenium.UnhandledAlertException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class EMScope_ViewInLandscape  {
    public static void main(String[] args) {
    	System.out.println("######################################################################");
		System.out.println("EMSCOPE AUTOMATION TEST CASE 3\n");
		System.out.println("THIS TEST CASE VERIFIES VIEW IN LANDSCAPE BUTTON FUNCTIONALITY FOR A GIVEN MAKRKET ORDER\n");
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
        new WebDriverWait(driver, 10).until(ExpectedConditions.elementToBeClickable(By.id("logout")));;
               
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
        new WebDriverWait(driver, 10).until(ExpectedConditions.visibilityOfElementLocated(By.id("orderChartContainer")));; 
        }
        catch (UnhandledAlertException e)
        {
        	System.out.println("Exception thrown :"+ e);
        	System.out.println("Order ID not found");	
        }
                               
        // In the Trades table, click on the first verified trade
        WebElement viewinlandscapebutton = driver.findElement(By.xpath("//body/div[1]/div/div[2]/div[2]/div[2]/div[1]/button[3]"));
        viewinlandscapebutton.click();              
        
        // Wait for 10 second for order details to load, timeout after 10 seconds
        try
        {
        	new WebDriverWait(driver, 10).until(ExpectedConditions.elementToBeClickable(By.id("chartContainer")));
        }
        catch (UnhandledAlertException e)
        {
        	System.out.println("Exception thrown :"+ e);
        	System.out.println("Problem fetching Landscape VIew");	
        }
        
        //Verify the landscape chart content for Order/Trades
        System.out.println("\nENTERING LANDSCAPE CHART::\n");
        
        java.util.List<WebElement> list3 = driver.findElements(By.xpath("//*[@class='sub _0']"));         
        for(WebElement option : list3){        	
        	String Summary3 = option.getText();
        	Summary3 = Summary3 + "2014";        	
            String[] elements3 = new String[100];
            
            int delta3 = 0;
            int start3 = Summary3.indexOf("2014");                        
            int end3 = Summary3.indexOf("2014", start3 + 1);
            
            System.out.println("Printing out the Order details from landscape chart below:");
            System.out.println("********************************************************\n");
            
            for (int i = 0; i<elements3.length; i++)
            {
            String p = Summary3.substring(start3, end3);
            elements3 [i] = p;
            delta3 = end3 - start3;
            start3 = start3 + delta3;
            end3 = Summary3.indexOf("2014", start3 + 1); 
            System.out.println(elements3[i]);
            if (end3==-1) break;
            } 
            if (end3==-1) break;
        }
        
        // Print our the Rejected trades from landscape chart        
        java.util.List<WebElement> list = driver.findElements(By.xpath("//*[@class='sub _2']"));        
        for(WebElement option : list){
        	String Summary = option.getText();        
        	Summary = Summary + "2014";
            String[] elements = new String[100];
            
            int delta = 0;
            int start = Summary.indexOf("2014");                        
            int end = Summary.indexOf("2014", start + 1);
            
            System.out.println("\nPrinting out the Rejected trades from landscape chart below:");
            System.out.println("********************************************************\n");
            
            for (int i = 0; i<elements.length; i++)
            {
            String p = Summary.substring(start, end);
            elements [i] = p;
            delta = end - start;
            start = start + delta;
            end = Summary.indexOf("2014", start + 1); 
            System.out.println(elements[i]);
            if (end==-1) break;
            } 
            if (end==-1) break;
        }
        
        // Print our the Confirmed trades from order chart        
        java.util.List<WebElement> list2 = driver.findElements(By.xpath("//*[@class='sub _1']"));        
        for(WebElement option : list2){
        	String Summary2 = option.getText();
        	Summary2 = Summary2 + "2014";
            String[] elements2 = new String[100];
            
            int delta2 = 0;
            int start2 = Summary2.indexOf("2014");                        
            int end2 = Summary2.indexOf("2014", start2 + 1);
            
            System.out.println("\nPrinting out the Confirmed trades from landscape chart below:");
            System.out.println("********************************************************\n");
            
            for (int i = 0; i<elements2.length; i++)
            {
            String p = Summary2.substring(start2, end2);
            elements2 [i] = p;
            delta2 = end2 - start2;
            start2 = start2 + delta2;
            end2 = Summary2.indexOf("2014", start2 + 1); 
            System.out.println(elements2[i]);
            if (end2==-1) break;
            }  
            if (end2==-1) break;
        }
        
        // Print out the associated orders in Orders table. Segregate substrings separated by space. Check for number of orders, first/last orders on 1st page, last order in the paginated list.
        driver.findElement(By.linkText("Orders")).click();
        java.util.List<WebElement> list4 = driver.findElements(By.id("ordersTableContainer"));        
        System.out.println("\n");
        for(WebElement option : list4){
            System.out.println(option.getText());
            String Summary4 = option.getText();
            String[] elements4 = new String[500];
            
            int delta4 = 0;
            int start4 = 154;
            char ch4 = ' ';
            int end4 = Summary4.indexOf((int)ch4, 154);
                        
            for (int i = 0; i<elements4.length; i++)
            {
            String p = Summary4.substring(start4, end4);
            elements4 [i] = p;
            delta4 = end4 - start4;
            start4 = start4 + delta4 + 1;
            end4 = Summary4.indexOf((int)ch4, start4);
            if (end4==-1) break; 
            }
            
            WebElement lastpage = driver.findElement(By.xpath("//body/div[1]/div/div[1]/div[4]/div/div/div[1]/div/div/div[6]/a[4]"));
            lastpage.click();            
            WebElement lastorder = driver.findElement(By.xpath("//body/div[1]/div/div[1]/div[4]/div/div/div[1]/div/div/div[4]/table/tbody/tr/td[2]/a"));
            WebElement lasttimestamp = driver.findElement(By.xpath("//body/div[1]/div/div[1]/div[4]/div/div/div[1]/div/div/div[4]/table/tbody/tr/td[1]"));
            
            if(elements4[0].equals("2014-05-12") && elements4[1].equals("12:44:30.227")) {
            	if(elements4[2].equals("543576781") && elements4[3].equals("Sell")) {
            		if(elements4[4].equals("USD/CAD") && elements4[5].equals("1.08809")) {
            			if(elements4[6].equals("1.0880222") && elements4[7].equals("7500000")) {
            				if(elements4[8].equals("75") && elements4[9].equals("10000000")) {
            					if(elements4[10].equals("MARKET") && elements4[11].startsWith("CITIP")) {            						
            										if(lastorder.getText().equals("543576781") && lasttimestamp.getText().equals("2014-05-12 12:44:30.227")){
            										    System.out.println("\nORDER DETAILS IS CORRECT\n");
            											break;
            										}
            										else
            										{
            											System.out.println("\nORDER DETAILS IS WRONG\n");
            											break;
            										}
            									}
            								}
            							}
            						}
            					}
            				}        
        }
        
        // Print out the associated trades in Trades table. Segregate substrings separated by space. Check for number of trades, first/last trades on 1st page, last trade in the paginated list.
        driver.findElement(By.linkText("Trades")).click();
        java.util.List<WebElement> list5 = driver.findElements(By.id("tradesTableContainer"));
        
        System.out.println("\n");
        for(WebElement option : list5){
            System.out.println(option.getText());
            String Summary5 = option.getText();
            String[] elements5 = new String[500];
            
            int delta5 = 0;
            int start5 = 139;
            char ch5 = ' ';
            int end5 = Summary5.indexOf((int)ch5, 139);
                        
            for (int i = 0; i<elements5.length; i++)
            {
            String p = Summary5.substring(start5, end5);
            elements5 [i] = p;
            delta5 = end5 - start5;
            start5 = start5 + delta5 + 1;
            end5 = Summary5.indexOf((int)ch5, start5);
            if (end5==-1) break; 
            }
            
            WebElement lastpage2 = driver.findElement(By.xpath("//body/div[1]/div/div[1]/div[4]/div/div/div[2]/div/div/div[6]/a[4]"));
            lastpage2.click();            
            WebElement lasttrade = driver.findElement(By.xpath("//body/div[1]/div/div[1]/div[4]/div/div/div[2]/div/div/div[4]/table/tbody/tr[10]/td[3]/a"));
            WebElement lasttimestamp2 = driver.findElement(By.xpath("//body/div[1]/div/div[1]/div[4]/div/div/div[2]/div/div/div[4]/table/tbody/tr[10]/td[1]"));
            
            if(elements5[0].equals("2014-05-12") && elements5[1].equals("12:44:30.465")) {
            	if(elements5[2].equals("543576781") && elements5[3].equals("FXI905425278C")) {
            		if(elements5[4].equals("Confirmed") && elements5[5].equals("Sell")) {
            			if(elements5[6].equals("CHPT") && elements5[8].equals("StreamNY")) {
            				if(elements5[9].equals("USD/CAD") && elements5[10].equals("1.08809")) {
            					if(elements5[11].equals("CITIP") && elements5[12].equals("FXSpot")) {
            						if(elements5[13].startsWith("Taker") && elements5[117].endsWith("2014-05-12")) {
            							if(elements5[118].equals("12:44:30.465") && elements5[119].equals("543576781")) {
            								if(elements5[120].equals("FXI905425275C") && elements5[121].equals("Confirmed")) {
            									if(elements5[122].equals("Sell") && elements5[123].equals("KMDO")) {
            										if(elements5[125].equals("Stream1") && elements5[126].equals("USD/CAD")) {
            											if(elements5[127].equals("1.0881") && elements5[128].equals("CITIP")){
            												if(elements5[129].equals("FXSpot") && elements5[130].startsWith("Taker")){
            												if(lasttrade.getText().equals("FXI905425323C") && lasttimestamp2.getText().equals("2014-05-12 12:44:31.520")){
            											    System.out.println("\nTRADE DETAILS IS CORRECT\n");
            											    break;
            										}
            										else
            										{
            											System.out.println("\nTRADE DETAILS IS WRONG\n");
            											break;
            										}
            									}
            								}
            							}
            						}
            					}
            				}
            			}
            		}
            	}            	               
            }
        }
        }
        }
        }
        
        // Print associated benchmark rates in Benchmark table. Segregate substrings separated by space. Check for number of rates, first/last rates on 1st page, last rate in the paginated list.
        driver.findElement(By.linkText("Benchmark")).click();
        java.util.List<WebElement> list6 = driver.findElements(By.id("benchmarkTableContainer"));
        
        for(WebElement option : list6){
            System.out.println(option.getText());
            String Summary6 = option.getText();
            String[] elements6 = new String[500];
            
            int delta6 = 0;
            int start6 = 60;
            char ch6 = ' ';
            int end6 = Summary6.indexOf((int)ch6, 60);
                        
            for (int i = 0; i<elements6.length; i++)
            {
            String p = Summary6.substring(start6, end6);
            elements6 [i] = p;
            delta6 = end6 - start6;
            start6 = start6 + delta6 + 1;
            end6 = Summary6.indexOf((int)ch6, start6);
            if (end6==-1) break; 
            }
            
            WebElement lastpage3 = driver.findElement(By.xpath("//body/div[1]/div/div[1]/div[4]/div/div/div[3]/div/div/div[6]/a[4]"));
            lastpage3.click();            
            WebElement lastbenchmark = driver.findElement(By.xpath("//body/div[1]/div/div[1]/div[4]/div/div/div[3]/div/div/div[4]/table/tbody/tr[10]/td[2]"));
            WebElement lasttimestamp3 = driver.findElement(By.xpath("//body/div[1]/div/div[1]/div[4]/div/div/div[3]/div/div/div[4]/table/tbody/tr[10]/td[1]"));
            
            if(elements6[0].equals("2014-05-12") && elements6[1].equals("12:39:01.000")) {
            	if(elements6[2].equals("1.088285") && elements6[3].startsWith("0.7")) {
            		if(elements6[27].endsWith("2014-05-12") && elements6[28].equals("12:39:11.000")) {
            			if(elements6[29].equals("1.088295") && elements6[30].startsWith("0.65")) {
            				if(elements6[35].equals("600")){
            					if(lastbenchmark.getText().equals("1.087995") && lasttimestamp3.getText().equals("2014-05-12 12:50:00.000")) {
            				System.out.println("\nBENCHMARK DETAILS IS CORRECT\n");
            				break;
            				}            							
            				else
            				{
            				System.out.println("\nBENCHMARK DETAILS IS WRONG\n");
            				break;            										
            				}
            			}
            		}
            	}
            }
        }
        }
        
        driver.quit();
        System.out.println("\nEND OF TEST CASE 3\n");
        
    }
}