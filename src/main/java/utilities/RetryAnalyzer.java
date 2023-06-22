package main.java.utilities;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

public class RetryAnalyzer implements IRetryAnalyzer  {
    private int count = 0;
    private int maxCount =3; // set your count to re-run test

    public boolean retry(ITestResult result) {
        if(count < maxCount) {
            count++;
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return true;
        }
        return false;
    }
}