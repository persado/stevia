package com.persado.oss.quality.stevia.network.http;

import java.io.IOException;

import org.eclipse.jetty.client.HttpDestination;
import org.eclipse.jetty.client.HttpExchange;
import org.eclipse.jetty.client.RedirectListener;
import org.eclipse.jetty.http.HttpStatus;
import org.eclipse.jetty.io.Buffer;

public class SteviaJettyRedirectListener extends RedirectListener
{
   public SteviaJettyRedirectListener(HttpDestination destination, HttpExchange ex)
   {
      super(destination, ex);
   }

   @Override
   public void onResponseStatus(Buffer version, int status, Buffer reason)
       throws IOException
   {
      // Since the default RedirectListener only cares about http
      // response codes 301 and 302, we override this method and
      // trick the super class into handling this case for us.
      if (status == HttpStatus.SEE_OTHER_303)
         status = HttpStatus.MOVED_TEMPORARILY_302;

      super.onResponseStatus(version,status,reason);
   }
}