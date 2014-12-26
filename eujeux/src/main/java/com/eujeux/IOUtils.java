package com.eujeux;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.eujeux.android.BaseServlet.BadRequest;

public class IOUtils {
	
	public static boolean writeObject(HttpServletResponse resp, Serializable object) {
		try {
			ObjectOutputStream oos = new ObjectOutputStream(resp.getOutputStream());
			oos.writeObject(object);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	@SuppressWarnings("unchecked")
	public static <T extends Serializable> T readObject(HttpServletRequest req, Class<T> c) {
		try {
			ObjectInputStream ois = new ObjectInputStream(req.getInputStream());
			return (T)ois.readObject();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static byte[] readBytes(HttpServletRequest req) {
		byte[] buffer = new byte[1024];
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		
		try {
			InputStream is = req.getInputStream();
			int read;
			
			while ((read = is.read(buffer)) > 0) {
				baos.write(buffer, 0, read);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return baos.toByteArray();
	}
	
	public static long getLongParameter(HttpServletRequest req, 
			String paramName) throws BadRequest {
		String paramS = req.getParameter(paramName);
		if (paramS != null) {
			try {
				return Long.parseLong(paramS);
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}
		}
		String message = "Could not read long parameter:" + paramName;
		throw new BadRequest(message);
	}
	
	public static int getIntParameter(HttpServletRequest req, 
			String paramName) throws BadRequest {
		String paramS = req.getParameter(paramName);
		if (paramS != null) {
			try {
				return Integer.parseInt(paramS);
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}
		}
		String message = "Could not read int parameter:" + paramName;
		throw new BadRequest(message);
	}

	public static boolean getBoolParameter(HttpServletRequest req,
			String paramName) throws BadRequest {
		String paramS = req.getParameter(paramName);
		if (paramS != null) {
			return Boolean.parseBoolean(paramS);
		}
		String message = "Could not read boolean parameter:" + paramName;
		throw new BadRequest(message);
	}
	
}
