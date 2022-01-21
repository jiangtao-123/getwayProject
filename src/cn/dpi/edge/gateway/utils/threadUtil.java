package cn.dpi.edge.gateway.utils;

import java.io.InputStream;
import java.io.OutputStream;

import javax.microedition.io.Connection;

import cn.dpi.edge.gateway.api.AutoCloseable;

public class threadUtil {

	public static boolean sleep(long millis) {
		try {
			Thread.sleep(millis);
			return true;
		} catch (InterruptedException e) {
			return false;
		}
	}

	public static void close(AutoCloseable item) {
		if (item == null) {
			return;
		}
		try {
			item.close();
		} catch (Exception e) {
		}
		item = null;
	}

	public static void close(Connection item) {
		if (item == null) {
			return;
		}
		try {
			item.close();
		} catch (Exception e) {
		}
		item = null;
	}

	public static void close(InputStream item) {
		if (item == null) {
			return;
		}
		try {
			item.close();
		} catch (Exception e) {
		}
		item = null;
	}

	public static void close(OutputStream item) {
		if (item == null) {
			return;
		}
		try {
			item.close();
		} catch (Exception e) {
		}
		item = null;
	}
}
