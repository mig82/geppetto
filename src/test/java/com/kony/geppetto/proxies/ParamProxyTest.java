package com.kony.geppetto.proxies;

import com.konylabs.middleware.dataobject.Param;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class ParamProxyTest {

	@BeforeMethod
	public void setUp() throws Exception {
	}

	@AfterMethod
	public void tearDown() throws Exception {
	}

	@Test
	public void testSetName() throws Exception {
		ParamProxy param = new ParamProxy();
		param.setName("foo");
		assertEquals(param.getParam().getName(), "foo");
	}

	@Test
	public void testSetStringValue() throws Exception {
		ParamProxy param = new ParamProxy();
		param.setName("foo");
		param.setValue("foo-1");
		assertEquals(param.getParam().getValue(), "foo-1");
	}

	@Test
	public void testSetBooleanValue() throws Exception {
		ParamProxy param = new ParamProxy();
		param.setName("foo");
		Boolean myBool = true;
		param.setValue(myBool);
		assertEquals(param.getParam().getValue(), myBool);
	}

	@Test
	public void testSetIntegerValue() throws Exception {
		ParamProxy param = new ParamProxy();
		param.setName("foo");
		Integer myInt = 999999999;
		param.setValue(myInt);
		assertEquals(param.getParam().getValue(), myInt);
	}

	@Test
	public void testSetFloatValue() throws Exception {
		ParamProxy param = new ParamProxy();
		param.setName("foo");
		Float myFloat = 3.141516f;
		param.setValue(myFloat);
		assertEquals(param.getParam().getValue(), myFloat);
	}

	@Test
	public void testGetParam() throws Exception {
	}

	@Test
	public void testCreateIntParam() throws Exception{
		//Param param = new Param("foo", 1, "number"); //WTF doesn't this accept anything other than String?
		Param param = new Param("foo", "999999999", "number");
		//assertEquals((Integer)param.getObjectValue(), new Integer(999999999));
		//assertEquals(Integer.getInteger(param.getValue()), new Integer(999999999));
		assertEquals(param.getValue(), "999999999");
	}
}