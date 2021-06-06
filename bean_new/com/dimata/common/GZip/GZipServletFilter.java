/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.common.GZip;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author dimata005
 */
public class GZipServletFilter implements Filter {

  @Override
  public void init(FilterConfig filterConfig) throws ServletException {
  }

  @Override
  public void destroy() {
  }

  public void doFilter(ServletRequest request, 
                       ServletResponse response,
                       FilterChain chain) 
  throws IOException, ServletException {

    HttpServletRequest  httpRequest  = (HttpServletRequest)  request;
    HttpServletResponse httpResponse = (HttpServletResponse) response;

    if ( acceptsGZipEncoding(httpRequest) ) {
      httpResponse.addHeader("Content-Encoding", "gzip");
      GZipServletResponseWrapper gzipResponse =
        new GZipServletResponseWrapper(httpResponse);
      chain.doFilter(request, gzipResponse);
      gzipResponse.close();
    } else {
      chain.doFilter(request, response);
    }
  }

  private boolean acceptsGZipEncoding(HttpServletRequest httpRequest) {
      String acceptEncoding = 
        httpRequest.getHeader("Accept-Encoding");

      return acceptEncoding != null && 
             acceptEncoding.indexOf("gzip") != -1;
  }

}