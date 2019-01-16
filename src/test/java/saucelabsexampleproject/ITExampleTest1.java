package saucelabsexampleproject;

import static org.junit.Assert.*;

import org.junit.Test;

public class ITExampleTest1 extends SauceLabsBaseClass {
	

	public ITExampleTest1(String os,
            			String version, String browser, String deviceName, String deviceOrientation)
	{
		super(os, version, browser, deviceName, deviceOrientation);
	}
	
	@Test
	public void verifyPageTitleLoads(){
		getDriver().get(getAppbaseurl());
		assertEquals("Google", getDriver().getTitle());
	}
}
