package com.kony.geppetto.connectors;

import static org.testng.Assert.*;

import org.testng.annotations.*;

import java.util.HashMap;
import java.util.Map;

@Test(suiteName = "Fabric Connector")
public class FabricConnectorTest {

	private String host;
	private String fabricKey;
	private String fabricSecret;
	private String fabricAuth;

	@BeforeTest
	public void beforeTest() throws Exception {
		host = "novartis-eu-dev.konycloud.com";
		fabricKey = "112233";
		fabricSecret = "112233";
		fabricAuth = "";
	}

	@AfterTest
	public void afterTest() throws Exception {
	}

	@AfterMethod
	public void afterMethod() throws Exception {
	}

	@Test(testName = "Get Time")
	public void testGetTime() throws Exception {

		String endPoint = "HcpES/get-time";
		FabricConnector fabric = new FabricConnector(host, endPoint, fabricKey, fabricSecret, fabricAuth);

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("foo", "foo1");

		String response = "";
		try {
			response = fabric.callService(map);
		}
		catch (Exception e){
			System.out.println(e.getMessage());
		}

		assertEquals(response, "");
	}

	@Test(testName = "Get Doctor")
	public void testGetDoctor() throws Exception {

		String endPoint = "HcpES/get-doctor";
		FabricConnector fabric = new FabricConnector(host, endPoint, fabricKey, fabricSecret, fabricAuth);

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("foo", "foo1");

		String response = "";
		try {
			response = fabric.callService(map);
		}
		catch (Exception e){
			System.out.println(e.getMessage());
		}

		assertEquals(response, "");
	}
}