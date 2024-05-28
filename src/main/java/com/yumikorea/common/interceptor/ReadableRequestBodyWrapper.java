package com.yumikorea.common.interceptor;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

public class ReadableRequestBodyWrapper extends HttpServletRequestWrapper {
	  class ServletInputStreamImpl extends ServletInputStream {
			private InputStream inputStream;
			
			public ServletInputStreamImpl(final InputStream inputStream) {
			  this.inputStream = inputStream;
			}
			
			@Override
			public boolean isFinished() {
			  // TODO Auto-generated method stub
			  return false;
			}
			
			@Override
			public boolean isReady() {
			  // TODO Auto-generated method stub
			  return false;
			}
			
			@Override
			public int read() throws IOException {
			  return this.inputStream.read();
			}
			
			@Override
			public int read(final byte[] b) throws IOException {
			  return this.inputStream.read(b);
			}
			
			@Override
			public void setReadListener(final ReadListener listener) {
			  // TODO Auto-generated method stub
			}
	  }

	  private byte[] bytes;
	  private String requestBody;
	  Map<String, String[]> params;

	  public ReadableRequestBodyWrapper(final HttpServletRequest request) throws IOException {
		  	super(request);
		  	this.params = new HashMap<String, String[]>(request.getParameterMap());
		
			// request의 InputStream의 content를 byte array로 
			StringBuilder textBuilder = new StringBuilder();
			InputStream in = null;
			Reader reader = null;
			
			try {
				in = super.getInputStream();
//				reader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
				reader = new InputStreamReader(in, StandardCharsets.UTF_8);
				int c = 0;
				while ((c = reader.read()) != -1) {
					textBuilder.append((char) c);
				}
				in.close();
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				this.bytes = textBuilder.toString().getBytes();
				// 데이터는 따로 저장한다
				this.requestBody = textBuilder.toString();
				if( in != null ) in.close();
				if( reader != null ) reader.close();
			}
	  }

		@Override
		public ServletInputStream getInputStream() throws IOException {
		  // InputStream을 반환해야하면 미리 구해둔 byte array 로
		  // 새 InputStream을 만들고 이걸로 ServletInputStream을 새로 만들어 반환
		  final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(this.bytes);
		  return new ServletInputStreamImpl(byteArrayInputStream);
		}
		
	    @Override
	    public String getParameter(String name) {

	        String[] paramArray = getParameterValues(name);

	        if (paramArray != null && paramArray.length > 0) {

	            return paramArray[0];

	        } else {

	            return null;

	        }

	    }

	    @Override
	    public Map<String, String[]> getParameterMap() {

	        return Collections.unmodifiableMap(params);

	    }
	    
	    @Override
	    public Enumeration<String> getParameterNames() {

	        return (Enumeration<String>) Collections.enumeration(params.keySet());

	    }

	    @Override
	    public String[] getParameterValues(String name) {

	        String[] result = null;
	        String[] temp = params.get(name);

	        if (temp != null) {

	            result = new String[temp.length];
	            System.arraycopy(temp, 0, result, 0, temp.length);

	        }

	        return result;
	    }
	    

		public String getRequestBody() {
			return this.requestBody;
		}
		
		public void setParameter(String name, String value) {
			String[] oneParam = {value};
			setParameter(name, oneParam);
		}
  
		public void setParameter(String name, String[] values) {
			// 여기 에러... java.lang.IllegalStateException: No modifications are allowed to a locked ParameterMap ParameterMap.java:175
			params.put(name, values);
		}

		@Override
		public String toString() {
			return this.getRequestBody();
		}
		
		
}