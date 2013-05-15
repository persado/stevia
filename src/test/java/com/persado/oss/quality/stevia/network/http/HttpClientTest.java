package com.persado.oss.quality.stevia.network.http;

import java.io.IOException;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.Test;

public class HttpClientTest {
	
	@Test
	public void testHttpClient() throws IOException, InterruptedException {
		System.setProperty("httpclient.followredirects", "true");

		
		HttpClient client = new HttpClient();
		List<HttpResponse> list = client.get("http://www.google.com", 2);
		Assert.assertNotNull(list, "list cannot be null");
		Assert.assertTrue(list.size() == 2, " list should have 2 elements ");
		
		Assert.assertNotNull(list.get(0).getBody(), "body cannot be null");
		Assert.assertEquals(list.get(0).getStatus(), 200);
		
		System.out.println("\n\nStatus="+list.get(0).getStatus()+"\n\n"+list.get(0).getBody());
	}

}
