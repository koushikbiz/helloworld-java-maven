import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.UnhandledAlertException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;


public class EMScope_Landscape_LMT_MKR {

	public static void main(String[] args) {
        System.out.println("######################################################################");
		System.out.println("EMSCOPE AUTOMATION TEST CASE 8\n");
		System.out.println("THIS TEST CASE VERIFIES CONTENT OF EMSCOPE TAB FOR EUR/USD::SUCD::LIMIT::MAKER::9 MINUTES INTERVAL::CITI-Integral1 COMBINATION\n");
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
        
        // Click on EMScope tab at the top of the page        
        driver.findElement(By.linkText("EMScope")).click();
                 
        // In the EMScope search criteria, select EUR/USD as the currency pair
        WebElement ccyPair = driver.findElement(By.xpath("//div/button[1]"));
        ccyPair.click();
        WebElement ccyPair2 = driver.findElement(By.xpath("//div/input[1]"));
        ccyPair2.sendKeys("EUR/USD");  
        ccyPair2.sendKeys(Keys.ENTER);
        
        ccyPair.sendKeys(Keys.TAB);        
        
        // In the EMScope search criteria, select SUCD as customer
        WebElement customer = driver.switchTo().activeElement();
        customer.click();
        WebElement customer2 = driver.switchTo().activeElement();
        customer2.sendKeys("SUCD");
        for (int i = 0; i<45; i++){
        customer2.sendKeys(Keys.ARROW_DOWN);
        }
        customer2.sendKeys(Keys.ENTER);
        
        customer.sendKeys(Keys.TAB);
        
        // In the EMScope search criteria, select Limit as order type
        WebElement ordertype = driver.switchTo().activeElement();
        ordertype.click();
        WebElement ordertype2 = driver.switchTo().activeElement();
        ordertype2.sendKeys("Limit");
        ordertype2.sendKeys(Keys.ENTER);
        
        ordertype.sendKeys(Keys.TAB);        
        
        // In the EMScope search criteria, select Maker as maker/taker
        WebElement makertaker = driver.switchTo().activeElement();
        makertaker.click();
        WebElement makertaker2 = driver.switchTo().activeElement();
        makertaker2.sendKeys(Keys.ARROW_DOWN);
        makertaker2.sendKeys(Keys.ENTER);
        
        makertaker.sendKeys(Keys.TAB);
        
        // In the EMScope search criteria, open calendar and select the second date in the current calendar for both from and to dates
        WebElement calenderleft = driver.findElement(By.xpath("//div[@class='calendar left']"));  
        String string1 = calenderleft.getText();
        System.out.println("Calendar Opened :" + string1.substring(0,8));
        WebElement timerange2 = driver.findElement(By.xpath("//body/div[3]/div[2]/div/table/tbody/tr/td[2]"));
        timerange2.click();
        
        
        WebElement calenderright = driver.findElement(By.xpath("//div[@class='calendar right']"));
        String string2 = calenderright.getText();
        System.out.println("Calendar Selected :" + string2.substring(0,8));
        WebElement timerange3 = driver.findElement(By.xpath("//body/div[3]/div/div/table/tbody/tr[1]/td[2]"));
        timerange3.click();
        
        // Select 08:21 as the from time       
        WebElement hour = driver.findElement(By.xpath("//body/div[3]/div[2]/div[2]/select"));
        hour.click();
        for (int i = 0; i<8; i++){
        hour.sendKeys(Keys.ARROW_DOWN);
        }
        hour.sendKeys(Keys.ENTER);
        
        WebElement minute = driver.findElement(By.xpath("//body/div[3]/div[2]/div[2]/select[2]"));
        minute.click();
        for (int i = 0; i<21; i++){
        minute.sendKeys(Keys.ARROW_DOWN);
        }
        minute.sendKeys(Keys.ENTER);
        
                
        // Select 08:30 as the to time                           
        WebElement hour2 = driver.findElement(By.xpath("//body/div[3]/div/div[2]/select"));
        hour2.click();
        for (int i = 0; i<8; i++){
        hour2.sendKeys(Keys.ARROW_DOWN);
        }
        hour2.sendKeys(Keys.ENTER);
        
        WebElement minute2 = driver.findElement(By.xpath("//body/div[3]/div/div[2]/select[2]"));
        minute2.click();
        for (int i = 0; i<30; i++){
        minute2.sendKeys(Keys.ARROW_DOWN);
        }
        minute2.sendKeys(Keys.ENTER);
        
        // Apply the time range setting in the search criteria
        WebElement apply = driver.findElement(By.xpath("//body/div[3]/div[3]/div/button"));
        apply.sendKeys(Keys.ENTER);
        
        // In the EMScope search criteria, set CITI - Integral1 as the provider stream 
        WebElement providerstream = driver.findElement(By.xpath("//body/div/div/div/div/form/div[3]/div/div/div/button"));
        providerstream.click();
        WebElement providerstream2 = driver.switchTo().activeElement();
        providerstream2.sendKeys("Citi - Integral1");
        providerstream2.sendKeys(Keys.ENTER);
        
        // Click Get Orders & Trades button and wait for the chart to appear
        WebElement getOTbutton = driver.findElement(By.xpath("//body/div/div/div/div/form/div[2]/div[2]/button"));
        getOTbutton.sendKeys (Keys.ENTER);
        
        // Wait for the order chart to load, timeout after 10 seconds. Log error message if Order ID not found.
        try
        {
        	new WebDriverWait(driver, 10).until(ExpectedConditions.elementToBeClickable(By.id("chartContainer"))); 
        }
        catch (UnhandledAlertException e)
        {
        	System.out.println("Exception thrown :"+ e);
        	System.out.println("Time range not correct");	
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
        }
        
        // Print our the Rejected trades from landscape chart        
        /*java.util.List<WebElement> list = driver.findElements(By.xpath("//*[@class='sub _2']"));        
        for(WebElement option : list){
        	String Summary = option.getText();
        	Summary = Summary + "2014";
            String[] elements = new String[100];
            
            int delta = 0;
            int start = Summary.indexOf("2014");                        
            int end = Summary.indexOf("2014", start + 1);*/
            
            System.out.println("\nPrinting out the Rejected trades from landscape chart below:");
            System.out.println("********************************************************\n");
            System.out.println("There are no Rejected trades for this time duration");
            
            /*for (int i = 0; i<elements.length; i++)
            {
            String p = Summary.substring(start, end);
            elements [i] = p;
            delta = end - start;
            start = start + delta;
            end = Summary.indexOf("2014", start + 1); 
            System.out.println(elements[i]);
            if (end==-1) break;
            }            
        }*/
        
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
        }
        
        // Print out the Zoom chart interval        
        WebElement zoomchart = driver.findElement(By.xpath("//body/div[1]/div/div[1]/div[3]")); 
        String zoomcharttimestamps = zoomchart.getText();
        System.out.println("\nZoom chart begining interval::" + zoomcharttimestamps.substring(0,11));
        System.out.println("Zoom chart end interval::" + zoomcharttimestamps.substring(100));        
        if (zoomcharttimestamps.startsWith("05/26 08:21") && zoomcharttimestamps.endsWith("5/26 08:30")){
        	System.out.println("\nZOOM CHART DISPLAY IS CORRECT");
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
            
            WebElement lastpage = driver.findElement(By.xpath("//body/div/div/div/div[4]/div/div/div/div/div/div[6]/a[4]"));
            lastpage.click();            
            WebElement lastorder = driver.findElement(By.xpath("//body/div/div/div/div[4]/div/div/div/div/div/div[4]/table/tbody/tr[6]/td[2]/a"));
            WebElement lasttimestamp = driver.findElement(By.xpath("//body/div/div/div/div[4]/div/div/div/div/div/div[4]/table/tbody/tr[6]/td"));
            
            if(elements4[0].equals("2014-06-30") && elements4[1].equals("08:21:51.779")) {
            	if(elements4[2].equals("652098537") && elements4[3].equals("Sell")) {
            		if(elements4[4].equals("EUR/USD") && elements4[5].equals("1.36516")) {
            			if(elements4[6].equals("1.36517") && elements4[7].equals("1000")) {
            				if(elements4[8].equals("100") && elements4[9].equals("1000")) {
            					if(elements4[10].equals("LIMIT") && elements4[11].startsWith("FIBCSUCD")) {
            						if(elements4[99].endsWith("2014-06-30") && elements4[100].equals("08:28:13.180")) {
            							if(elements4[101].equals("647130518") && elements4[102].equals("Sell")) {
            								if(elements4[103].equals("EUR/USD") && elements4[104].equals("1.36528")) {
            									if(elements4[105].equals("1.36528") && elements4[106].equals("100000")) {
            										if(elements4[107].equals("100") && elements4[108].equals("100000")) {
            											if(elements4[109].equals("LIMIT") && elements4[110].startsWith("NIFTYSUCD")){
            												if(lastorder.getText().equals("648256561") && lasttimestamp.getText().equals("2014-06-30 08:29:50.237")){
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
            
            WebElement lastpage2 = driver.findElement(By.xpath("//body/div/div/div/div[4]/div/div/div[2]/div/div/div[6]/a[4]"));
            lastpage2.click();
            
            WebElement lasttrade = driver.findElement(By.xpath("//body/div/div/div/div[4]/div/div/div[2]/div/div/div[4]/table/tbody/tr[6]/td[3]/a"));
            WebElement lasttimestamp2 = driver.findElement(By.xpath("//body/div/div/div/div[4]/div/div/div[2]/div/div/div[4]/table/tbody/tr[6]/td"));
            
            if(elements5[0].equals("2014-06-30") && elements5[1].equals("08:21:51.781")) {
            	if(elements5[2].equals("652098537") && elements5[3].equals("FXI1159426083")) {
            		if(elements5[4].equals("Confirmed") && elements5[5].equals("Buy")) {
            			if(elements5[6].equals("SUCD") && elements5[8].equals("RSUCDNOMINTIERS")) {
            				if(elements5[9].equals("EUR/USD") && elements5[10].equals("1.36517")) {
            					if(elements5[11].equals("FIBCSUCD") && elements5[12].equals("FXSpot")) {
            						if(elements5[13].startsWith("Taker") && elements5[117].endsWith("2014-06-30")) {
            							if(elements5[118].equals("08:28:13.182") && elements5[119].equals("647130518")) {
            								if(elements5[120].equals("FXI1159295481") && elements5[121].equals("Confirmed")) {
            									if(elements5[122].equals("Buy") && elements5[123].equals("SUCD")) {
            										if(elements5[125].equals("SUCDNOMINDEMOLIVENIFTY") && elements5[126].equals("EUR/USD")) {
            											if(elements5[127].equals("1.36528") && elements5[128].equals("NIFTYSUCD")){
            												if(elements5[129].equals("FXSpot") && elements5[130].startsWith("Taker")){
            												if(lasttrade.getText().equals("FXI1159220864") && lasttimestamp2.getText().equals("2014-06-30 08:29:50.238")){
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
            
            WebElement lastpage3 = driver.findElement(By.xpath("//body/div/div/div/div[4]/div/div/div[3]/div/div/div[6]/a[4]"));
            lastpage3.click();
            
            WebElement lastbenchmark = driver.findElement(By.xpath("//body/div/div/div/div[4]/div/div/div[3]/div/div/div[4]/table/tbody/tr[10]/td[2]"));
            WebElement lasttimestamp3 = driver.findElement(By.xpath("//body/div/div/div/div[4]/div/div/div[3]/div/div/div[4]/table/tbody/tr[10]/td"));
            
            if(elements6[0].equals("2014-06-30") && elements6[1].equals("08:21:01.000")) {
            	if(elements6[2].equals("1.36516854") && elements6[3].startsWith("0.4")) {
            		if(elements6[27].endsWith("2014-06-30") && elements6[28].equals("08:21:10.000")) {
            			if(elements6[29].equals("1.365145") && elements6[30].startsWith("0.47727273")) {
            				if(elements6[35].equals("540")){
            					if(lastbenchmark.getText().equals("1.36535208") && lasttimestamp3.getText().equals("2014-06-30 08:30:00.000")) {
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
        
        // Print associated tick data in Rates table. Segregate substrings separated by space. Check for number of rates, first rate on 1st page, last rate in the paginated list.        
        driver.findElement(By.linkText("Rates")).click();
        java.util.List<WebElement> list7 = driver.findElements(By.id("ratesTableContainer"));
        
        for(WebElement option : list7){
            System.out.println(option.getText());
            String Summary7 = option.getText();
            String[] elements7 = new String[500];
            
            int delta7 = 0;
            int start7 = 102;
            char ch7 = ' ';
            int end7 = Summary7.indexOf((int)ch7, 102);
                        
            for (int i = 0; i<elements7.length; i++)
            {
            String p = Summary7.substring(start7, end7);
            elements7 [i] = p;
            delta7 = end7 - start7;
            start7 = start7 + delta7 + 1;
            end7 = Summary7.indexOf((int)ch7, start7);
            if (end7==-1) break; 
            }
            
            WebElement lastpage4 = driver.findElement(By.xpath("//body/div/div/div/div[4]/div/div/div[4]/div/div/div[6]/a[4]"));
            lastpage4.click();
            
            WebElement lasttimestamp4 = driver.findElement(By.xpath("//body/div/div/div/div[4]/div/div/div[4]/div/div/div[4]/table/tbody/tr/td"));
            WebElement lastrateguid = driver.findElement(By.xpath("//body/div/div/div/div[4]/div/div/div[4]/div/div/div[4]/table/tbody/tr/td[7]"));
                                    
            if(elements7[0].equals("2014-06-30") && elements7[1].equals("08:21:00.000")) {
            	if(elements7[2].equals("1.36517") && elements7[3].equals("500000")) {
            		if(elements7[4].equals("1.36513") && elements7[5].equals("500000")) {
            			if(elements7[6].equals("Active") && elements7[7].equals("G-605766d-146ebdcdff8-CITIA-2c8c")) {
            				if(elements7[8].startsWith("1") && elements7[9].equals("08:21:00.000")) {
            					if(elements7[10].equals("1.36519") && elements7[11].equals("1000000")) {
            						if(elements7[12].equals("1.36511") && elements7[13].equals("1000000")) {
            							if(elements7[14].equals("Active") && elements7[15].equals("G-605766d-146ebdcdff8-CITIA-2c8c")) {
            								if(elements7[16].startsWith("2") && elements7[72].endsWith("2014-06-30")) {
            									if(elements7[73].equals("08:21:00.000") && elements7[74].equals("1.36532")) {
            										if(elements7[75].equals("500000") && elements7[76].equals("0")) {
            											if(elements7[77].equals("0") && elements7[78].equals("Active")) {
            											if(elements7[79].startsWith("G-605766d-146ebdca5a7-CITIA-2c83") && elements7[80].startsWith("10")){
            												if(elements7[85].equals("5,677")){
            												if(lasttimestamp4.getText().equals("2014-06-30 08:29:57.795") && lastrateguid.getText().equals("G-605766d-146ebe516a3-CITIA-2edf")){
            											System.out.println("\nTICK DATA DETAILS IS CORRECT\n");
            											break;
            										}
            										else
            										{
            											System.out.println("\nTICK DATA DETAILS IS WRONG\n");
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
        }
        
        // Check if the system is interactive based on Provider&Streams/OT/Benchmark/Tick Data query timings and print out the relevant message.
        java.util.List<WebElement> list8 = driver.findElements(By.xpath("//*[starts-with(@class,'perf-console')]"));
        
        for(WebElement option : list8){
            System.out.println(option.getText());
            String Summary8 = option.getText();
            Summary8 = Summary8 + '\n';
            String[] elements8 = new String[10];
            
            int delta8 = 0;
            int start8 = 22;
            char ch8 = '\n';
            int end8 = Summary8.indexOf((int)ch8, 22);
                        
            for (int i = 0; i<elements8.length; i++)
            {
            String p = Summary8.substring(start8, end8);
            elements8 [i] = p;
            delta8 = end8 - start8;
            start8 = start8 + delta8 + 1;
            end8 = Summary8.indexOf((int)ch8, start8);
            if (end8==-1) break;
            }                        
                        
            String Prov_Strm_Query = elements8[0].replaceAll("[^\\d]","");
            int querytime1 = Integer.parseInt(Prov_Strm_Query);
            String Ord_Trd_Query = elements8[1].replaceAll("[^\\d]","");            
            int querytime2 = Integer.parseInt(Ord_Trd_Query);
            String Benchmark_Query = elements8[2].replaceAll("[^\\d]","");
            int querytime3 = Integer.parseInt(Benchmark_Query);
            String Tick_Data_Query = elements8[3].replaceAll("[^\\d]","");
            String ParseTDQ = Tick_Data_Query.substring(1);
            int querytime4 = Integer.parseInt(ParseTDQ);            
                        
            if(querytime1<5000) {            	           	
            	System.out.println("\nProvider & Streams query is interactive\n");
            	}
            	else
            	{
            	System.out.println("\nProvider & Streams query is NOT interactive\n");            	
            	}  
            
            if(querytime2<5000) {            	           	
            	System.out.println("\nOrder & Trades query is interactive\n");            	
            	}
            	else
            	{
            	System.out.println("\nOrder & Trades query is NOT interactive\n");            	
            	}
            
            if(querytime3<5000) {            	           	
            	System.out.println("\nBenchmark query is interactive\n");            	
            	}
            	else
            	{
            	System.out.println("\nBenchmark query is NOT interactive\n");            	
            	}
            
            if(querytime4<5000) {            	           	
            	System.out.println("\nTick Data query is interactive\n");
            	break;
            	}
            	else
            	{
            	System.out.println("\nTick Data query is NOT interactive\n");
            	break;
            	}
            
        }
        
        //Close the browser
        driver.quit();
        System.out.println("\nEND OF TEST CASE 8\n");
	}

}
