package net.thumbtack.school.auction.rest.mappers;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.ext.Provider;
import javax.ws.rs.ext.WriterInterceptor;
import javax.ws.rs.ext.WriterInterceptorContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Provider
public class JsonWriterInterceptor implements WriterInterceptor {
	
    private static final Logger LOGGER = LoggerFactory.getLogger(JsonWriterInterceptor.class);
	
    @Override
	public void aroundWriteTo(WriterInterceptorContext context)	throws IOException, WebApplicationException {
		OutputStream originalStream = context.getOutputStream();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		context.setOutputStream(baos);
		try {
			context.proceed();
		} finally {
			BufferedReader reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(baos.toByteArray())));
			String json = reader.readLine();
			if(json != null)
				LOGGER.debug("Json output " + json);
			baos.writeTo(originalStream);
			baos.close();
			reader.close();
			context.setOutputStream(originalStream);
		}
	}
}