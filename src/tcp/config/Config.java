package tcp.config;

import java.io.IOException;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class Config
{
	public static final String SERVER_ADDRESS = "127.0.0.1";
	public static final int SERVER_PORT = 54811;
	public static final long PING = 2000;
	public static final int LISTEN_PORT = 54812;
	public static final String LOG_FILE = "AllLog.log";
	
	private static Logger logger = null;
	
	public static Logger getLoggerFor(Class<?> target)
	{
		if (logger != null) {
			return logger;
		}
		
		logger = Logger.getLogger(target.getSimpleName());
		
		try {
			logger.setUseParentHandlers(false);
			
			FileHandler fileHandler = new FileHandler(LOG_FILE);
			fileHandler.setFormatter(new SimpleFormatter());
			logger.addHandler(fileHandler);
			
			logger.addHandler(new OutConsoleHandler());
			
		} catch (SecurityException | IOException e) {
			e.printStackTrace();
		}

		return logger;
	}
	
	private static class OutConsoleHandler extends ConsoleHandler
	{
		public OutConsoleHandler() {
			super();
			setOutputStream(System.out);
		}
	}
}