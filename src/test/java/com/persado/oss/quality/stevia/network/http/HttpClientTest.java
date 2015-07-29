package com.persado.oss.quality.stevia.network.http;

/*
 * #%L
 * Stevia QA Framework - Core
 * %%
 * Copyright (C) 2013 - 2014 Persado
 * %%
 * Copyright (c) Persado Intellectual Property Limited. All rights reserved.
 *  
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *  
 * * Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer.
 *  
 * * Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 *  
 * * Neither the name of the Persado Intellectual Property Limited nor the names
 * of its contributors may be used to endorse or promote products derived from
 * this software without specific prior written permission.
 *  
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 * #L%
 */

import java.io.IOException;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.Test;

public class HttpClientTest {
	
	@Test
	public void testHttpClient() throws IOException, InterruptedException {
		
		// we enable redirects
		System.setProperty("httpclient.followredirects", "true");

		
		SteviaHttpClient client = new SteviaHttpClient();
		List<HttpResponse> list = client.get("http://www.google.com", 2);
		Assert.assertNotNull(list, "list cannot be null");
		Assert.assertTrue(list.size() == 2, " list should have 2 elements ");
		
		Assert.assertNotNull(list.get(0).getBody(), "body cannot be null");
		Assert.assertEquals(list.get(0).getStatus(), 200);
		
		System.out.println("\n\nStatus="+list.get(0).getStatus()+"\n\n"+list.get(0).getBody());
		
		
	}

}
