import org.openqa.selenium.By;
import org.openqa.selenium.UnhandledAlertException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class EMScope_Login_LMTRNGOrder  {
    public static void main(String[] args) {
    	System.out.println("######################################################################");
		System.out.println("EMSCOPE AUTOMATION TEST CASE 10\n");
		System.out.println("THIS TEST CASE VERIFIES CONTENT OF ORDER ID TAB FOR A GIVEN LIMIT RANGE ORDER\n");
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
        
        //With the Default Stream in the drop down, click on Get Stream button
        java.util.List<WebElement> list = driver.findElements(By.xpath("//*[starts-with(@class,'btn btn-primary')]"));
                
        for(WebElement option : list){                        
            if(option.getText().equals("Get Stream")) {
                option.click();
                break;
            }  
        }
        
        // Wait for the tick data to load, timeout after 10 seconds. Log error message if stream not found.
        try
        {
        new WebDriverWait(driver, 10).until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@class='sub _7']"))); 
        }
        catch (UnhandledAlertException e)
        {
        	System.out.println("Exception thrown :"+ e);
        	System.out.println("Stream not available");	
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
        
        // Verify the presence of Range line in the order chart
        WebElement orderchartcontents = driver.findElement(By.xpath("//body/div[1]/div/div[2]/div[2]/div[3]"));
        if (orderchartcontents.getText().contains("Order Range")) System.out.println("Range is shown in the chart");
        else System.out.println("Range is NOT shown in the chart");
        
        // Print our the Failed trades from order chart        
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
        
        
        //Switch ON the Filter Trades By Stream: toggle button. Verify if chart content is updated for Trades
        WebElement togglebutton = driver.findElement(By.xpath("//body/div/div/div[2]/div[2]/div[2]/div[2]/div/div/span[2]"));
        togglebutton.click();
        WebElement orderchart2 = driver.findElement(By.xpath("//body/div/div/div[2]/div[2]/div[3]"));
        String chartcontent2 = orderchart2.getText();
        if (chartcontent2.contains("FXI1115109616")) {        	
        	System.out.println("\nToggle button not working correctly\n");        }
        else {
        	System.out.println("\nToggle button working correctly\n");
        }

        // Print out the Zoom chart interval        
        WebElement zoomchart = driver.findElement(By.xpath("//body/div[1]/div/div[2]/div[2]/div[4]")); 
        String zoomcharttimestamps = zoomchart.getText();
        System.out.println("Zoom chart begining interval::" + zoomcharttimestamps.substring(0,12));
        System.out.println("Zoom chart end interval::" + zoomcharttimestamps.substring(120)); 
        System.out.println("Mid Point of Zoom chart::" + zoomcharttimestamps.substring(60,72)); 
        WebElement orderpoint = driver.findElement(By.xpath("//*[@class='sub _0']"));
        String ordertext = orderpoint.getText();
        if (ordertext.contains(zoomcharttimestamps.substring(60,72))){
        	System.out.println("\nZOOM CHART MID POINT IS ORDER SUBMITTED TIME - DISPLAY IS CORRECT\n");
        }
                        
        // Print out the narratives in the console. Print out if narratives is correct or wrong based on match with a reference string.
        java.util.List<WebElement> list4 = driver.findElements(By.xpath("//*[starts-with(@class,'narrativeContainer moduleContainer')]"));
        
        for(WebElement option : list4){
            System.out.println(option.getText());
            String Narratives = "SUCD Order ID: 633433819\nThe SUCD:SUCDQuoter placed a/an LIMIT order to Buy USD/CAD @ Range 1 from 1.07546 for an amount of 100,000 USD.\nTime in Force: GTD The order was placed at 22-06-2014 21:06:37.806, GMT\nIt was completely filled @ 1.075498.";
            if(option.getText().endsWith(Narratives)) {
            	System.out.println("\nNARRATIVES IS CORRECT\n");
                break;
            }  
            else {
            	System.out.println("\nNARRATIVES IS WRONG\n");
            	break;
            }
        }
        
               
        // Print out the execution summary. Segregate substrings separated by space. Check for confirmed/rejected/cancelled/failed amount.
        java.util.List<WebElement> list5 = driver.findElements(By.xpath("//*[starts-with(@class,'moduleContainer execSummaryContainer')]"));
        
        for(WebElement option : list5){
            System.out.println(option.getText());
            String Summary = option.getText();
            String[] elements = new String[500];
            
            int delta = 0;
            int start = 211;
            char ch = ' ';
            int end_point = 0;
            int end = Summary.indexOf((int)ch, 211);
                        
            for (int i = 0; i<elements.length; i++)
            {
            String p = Summary.substring(start, end);
            elements [i] = p;
            delta = end - start;
            start = start + delta + 1;
            end = Summary.indexOf((int)ch, start);
            if (end==-1) { end_point = i; break; }
            }
            
            int j = 2;
            double k = 0.0;
            
            do
            {            
            k += Double.parseDouble(elements[j].replaceAll("[^\\d.]",""));
            j += 9;
            } while (j<=end_point);
            
            System.out.println("\nTotal Confirmed amount:" + String.valueOf(k));
            
            int l = 4;
            double m = 0.0;
            
            do
            {            
            m += Double.parseDouble(elements[l].replaceAll("[^\\d.]",""));
            l += 9;
            } while (l<=end_point);
            
            System.out.println("Total Rejected amount:" + String.valueOf(m));
            
            int n = 6;
            double o = 0.0;
            
            do
            {            
            o += Double.parseDouble(elements[n].replaceAll("[^\\d.]",""));
            n += 9;
            } while (n<=end_point);
            System.out.println("Total Cancelled amount:" + String.valueOf(o));
            
            int p = 8;
            double q = 0.0;
            
            do
            {            
            q += Double.parseDouble(elements[p].replaceAll("[^\\d.]",""));
            p += 9;
            } while (p<=end_point);
            System.out.println("Total Failed amount:" + String.valueOf(q));
            
                                
            if((k==100000.0 && m==0.0) && (o==0.0 && q==80000.0)) {
            	System.out.println("\nEXECUTION SUMMARY IS CORRECT\n");
                break;
            } 
            else {
            	System.out.println("\nEXECUTION SUMMARY IS WRONG\n");
            	break;
            }
        }
        
        // Print out the order details. Segregate substrings separated by newline. Check for various attribute values.
        java.util.List<WebElement> list6 = driver.findElements(By.xpath("//*[starts-with(@class,'orderDetailsContainer moduleContainer')]"));
        
        for(WebElement option : list6){
            System.out.println(option.getText());
            String Summary2 = option.getText();
            String[] elements2 = new String[40];
            
            int delta2 = 0;
            int start2 = 14;
            char ch2 = '\n';
            int end2 = Summary2.indexOf((int)ch2, 14);
                        
            for (int i = 0; i<elements2.length; i++)
            {
            String p = Summary2.substring(start2, end2);
            elements2 [i] = p;
            delta2 = end2 - start2;
            start2 = start2 + delta2 + 1;
            end2 = Summary2.indexOf((int)ch2, start2);
            if (end2==-1) break;
            }
            
            if(elements2[0].endsWith("FXI1115131269") && elements2[1].endsWith("633433819")) {
            	if(elements2[3].endsWith("1.07546") && elements2[4].endsWith("100000")) {
            		if(elements2[5].endsWith("USD") && elements2[6].endsWith("100000")) {
            			if(elements2[7].endsWith("100000") && elements2[8].endsWith("Filled")) {
            				if(elements2[9].endsWith("SUCDQuoter") && elements2[10].endsWith("2014-06-22 21:06:37.806")) {
            					if(elements2[11].endsWith("1.075498") && elements2[12].endsWith("GTD")) {
            						if(elements2[14].endsWith("100") && elements2[15].endsWith("SUCD")) {
            							if(elements2[16].endsWith("LIMIT") && elements2[17].endsWith("100000")) {
            								if(elements2[18].endsWith("Buy") && elements2[19].endsWith("USD/CAD")) {
            									if(elements2[20].endsWith("N") && elements2[22].endsWith("1")) {
            										if(Summary2.endsWith("More Info >>      GM ")) {
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
        
        // Verify the contents of Confirmed trades pie chart. Print out correct wrong message
        WebElement confirmedtrades = driver.findElement(By.xpath("//body/div/div/div[2]/div[2]/div[8]/div/div"));
            System.out.println ("Inside Pie Chart for Confirmed trades:: The trades are:");
            System.out.println(confirmedtrades.getText());            
                
        // Verify the contents of Rejected trades pie chart. Print out correct wrong message
        WebElement rejectedtrades = driver.findElement(By.xpath("//body/div/div/div[2]/div[2]/div[8]/div[2]/div"));
            System.out.println ("\nInside Pie Chart for Rejected trades:: The trades are:");
            System.out.println(rejectedtrades.getText());                   
        
        // Print out the associated trades in Trades table. Segregate substrings separated by space. Check for number of trades, first/last trades on 1st page, last trade in the paginated list.
        driver.findElement(By.linkText("Trades")).click();
        java.util.List<WebElement> list8 = driver.findElements(By.id("oTTradesTableContainer"));
        
        System.out.println("\n");
        for(WebElement option : list8){
            System.out.println(option.getText());
            String Summary3 = option.getText();
            String[] elements3 = new String[500];
            
            int delta3 = 0;
            int start3 = 110;
            char ch3 = ' ';
            int end3 = Summary3.indexOf((int)ch3, 110);
                        
            for (int i = 0; i<elements3.length; i++)
            {
            String p = Summary3.substring(start3, end3);
            elements3 [i] = p;
            delta3 = end3 - start3;
            start3 = start3 + delta3 + 1;
            end3 = Summary3.indexOf((int)ch3, start3);
            if (end3==-1) break; 
            }
            
            WebElement lastpage = driver.findElement(By.xpath("//body/div/div/div[2]/div[2]/div[10]/div/div/div/div/div/div[6]/a[4]"));
            lastpage.click();
            
            WebElement lasttrade = driver.findElement(By.xpath("//body/div/div/div[2]/div[2]/div[10]/div/div/div/div/div/div[4]/table/tbody/tr[3]/td/a"));
            WebElement lasttimestamp = driver.findElement(By.xpath("//body/div/div/div[2]/div[2]/div[10]/div/div/div/div/div/div[4]/table/tbody/tr[3]/td[2]"));
            
            if(elements3[0].equals("FXI1115109613") && elements3[4].equals("2014-06-22")) {
            	if(elements3[5].equals("21:06:37.807") && elements3[6].equals("1.07549")) {
            		if(elements3[7].equals("20000") && elements3[8].equals("DB")) {
            			if(elements3[9].equals("PRD010") && elements3[10].equals("Confirmed")) {
            				if(elements3[11].equals("Buy") && elements3[12].equals("FXSpot")) {
            					if(elements3[13].startsWith("SUCD") && elements3[26].endsWith("FXI1115109616")) {
            						if(elements3[30].equals("2014-06-22") && elements3[31].equals("21:06:37.811")) {
            							if(elements3[32].equals("1.0755") && elements3[33].equals("80000")) {
            								if(elements3[34].equals("BOAN") && elements3[35].equals("BandB")) {
            									if(elements3[36].equals("Confirmed") && elements3[37].equals("Buy")) {
            										if(elements3[38].equals("FXSpot") && elements3[39].startsWith("SUCD")) {
            											if(elements3[44].equals("3")){
            												if(lasttrade.getText().equals("FXI1115109616") && lasttimestamp.getText().equals("2014-06-22 21:06:37.811")){
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
                       
        // Print associated benchmark rates in Benchmark table. Segregate substrings separated by space. Check for number of rates, first/last rates on 1st page, last rate in the paginated list.
        driver.findElement(By.linkText("Benchmark")).click();
        java.util.List<WebElement> list9 = driver.findElements(By.id("oTBenchmarkTableContainer"));
        
        for(WebElement option : list9){
            System.out.println(option.getText());
            String Summary4 = option.getText();
            String[] elements4 = new String[500];
            
            int delta4 = 0;
            int start4 = 60;
            char ch4 = ' ';
            int end4 = Summary4.indexOf((int)ch4, 60);
                        
            for (int i = 0; i<elements4.length; i++)
            {
            String p = Summary4.substring(start4, end4);
            elements4 [i] = p;
            delta4 = end4 - start4;
            start4 = start4 + delta4 + 1;
            end4 = Summary4.indexOf((int)ch4, start4);
            if (end4==-1) break; 
            }
            
            WebElement lastpage2 = driver.findElement(By.xpath("//body/div/div/div[2]/div[2]/div[10]/div/div/div[2]/div/div/div[6]/a[4]"));
            lastpage2.click();
            
            WebElement lasttimestamp2 = driver.findElement(By.xpath("//body/div/div/div[2]/div[2]/div[10]/div/div/div[2]/div/div/div[4]/table/tbody/tr[2]/td"));
            WebElement lastbenchmark = driver.findElement(By.xpath("//body/div/div/div[2]/div[2]/div[10]/div/div/div[2]/div/div/div[4]/table/tbody/tr[2]/td[2]"));
            
            if(elements4[0].equals("2014-06-22") && elements4[1].equals("21:06:37.000")) {
            	if(elements4[2].equals("1.07544") && elements4[3].startsWith("2.7")) {
            		if(elements4[3].endsWith("2014-06-22") && elements4[4].equals("21:06:38.000")) {
            			if(elements4[5].equals("1.07544") && elements4[6].startsWith("2.7")) {
            				if(elements4[11].equals("2")){
            					if(lasttimestamp2.getText().equals("2014-06-22 21:06:38.000") && lastbenchmark.getText().equals("1.07544")) {
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
        java.util.List<WebElement> list10 = driver.findElements(By.id("oTRatesTableContainer"));
        
        for(WebElement option : list10){
            System.out.println(option.getText());
            String Summary5 = option.getText();
            String[] elements5 = new String[500];
            
            int delta5 = 0;
            int start5 = 102;
            char ch5 = ' ';
            int end5 = Summary5.indexOf((int)ch5, 102);
                        
            for (int i = 0; i<elements5.length; i++)
            {
            String p = Summary5.substring(start5, end5);
            elements5 [i] = p;
            delta5 = end5 - start5;
            start5 = start5 + delta5 + 1;
            end5 = Summary5.indexOf((int)ch5, start5);
            if (end5==-1) break; 
            }
            
            WebElement lastpage3 = driver.findElement(By.xpath("//body/div/div/div[2]/div[2]/div[10]/div/div/div[3]/div/div/div[6]/a[4]"));
            lastpage3.click();
            
            WebElement lasttimestamp3 = driver.findElement(By.xpath("//body/div/div/div[2]/div[2]/div[10]/div/div/div[3]/div/div/div[4]/table/tbody/tr[3]/td"));
            WebElement lastrateguid = driver.findElement(By.xpath("//body/div/div/div[2]/div[2]/div[10]/div/div/div[3]/div/div/div[4]/table/tbody/tr[3]/td[7]"));
                                    
            if(elements5[0].equals("2014-06-22") && elements5[1].equals("21:06:37.000")) {
            	if(elements5[2].equals("1.07546") && elements5[3].equals("500000")) {
            		if(elements5[4].equals("1.07535") && elements5[5].equals("1500000")) {
            			if(elements5[6].equals("Active") && elements5[7].equals("G-3c369d51-146c566d719-DBK-40")) {
            				if(elements5[8].startsWith("1") && elements5[9].equals("21:06:37.000")) {
            					if(elements5[10].equals("1.07549") && elements5[11].equals("500000")) {
            						if(elements5[12].equals("1.07531") && elements5[13].equals("1000000")) {
            							if(elements5[14].equals("Active") && elements5[15].equals("G-3c369d51-146c566d719-DBK-40")) {
            								if(elements5[16].startsWith("2") && elements5[17].equals("21:06:37.000")) {
            									if(elements5[18].equals("1.0755") && elements5[19].equals("500000")) {
            										if(elements5[20].equals("1.07525") && elements5[21].equals("1000000")) {
            											if(elements5[22].equals("Active") && elements5[23].equals("G-3c369d51-146c566d719-DBK-40")) {
            											if(elements5[24].startsWith("3")){
            												if(lasttimestamp3.getText().equals("2014-06-22 21:06:37.815") && lastrateguid.getText().equals("G-3c369d51-146c566f677-DBK-43")){
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
        
        // Check if the system is interactive based on Provider&Streams/OT/Benchmark/Tick Data query timings and print out the relevant message.
        java.util.List<WebElement> list11 = driver.findElements(By.xpath("//*[starts-with(@class,'perf-console')]"));
        
        for(WebElement option : list11){
            System.out.println(option.getText());
            String Summary6 = option.getText();
            Summary6 = Summary6 + '\n';
            String[] elements6 = new String[10];
            
            int delta6 = 0;
            int start6 = 22;
            char ch6 = '\n';
            int end6 = Summary6.indexOf((int)ch6, 22);
                        
            for (int i = 0; i<elements6.length; i++)
            {
            String p = Summary6.substring(start6, end6);
            elements6 [i] = p;
            delta6 = end6 - start6;
            start6 = start6 + delta6 + 1;
            end6 = Summary6.indexOf((int)ch6, start6);
            if (end6==-1) break;
            }                        
                        
            String Prov_Strm_Query = elements6[0].replaceAll("[^\\d]","");
            int querytime1 = Integer.parseInt(Prov_Strm_Query);
            String Ord_Trd_Query = elements6[1].replaceAll("[^\\d]","");
            String ParseOTQ = Ord_Trd_Query.substring(9);
            int querytime2 = Integer.parseInt(ParseOTQ);
            String Benchmark_Query = elements6[2].replaceAll("[^\\d]","");
            int querytime3 = Integer.parseInt(Benchmark_Query);
            String Tick_Data_Query = elements6[3].replaceAll("[^\\d]","");
            String ParseTDQ = Tick_Data_Query.substring(3);
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
            	System.out.println("\nProvider & Streams query is interactive\n");
            	break;
            	}
            	else
            	{
            	System.out.println("\nProvider & Streams query is NOT interactive\n");
            	break;
            	}
            
        }
      //Close the browser
        driver.quit();
        System.out.println("\nEND OF TEST CASE 10\n");
    }
}