package nl.tudelft.contextproject.logging;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import lombok.SneakyThrows;

/**
 * Class for conveniently logging messages.
 */
public class Log {
	//The default format for files: "HH:MM:SS [LEVEL]: Message", optionally followed by an exception and its stacktrace.
	public static final String FORMAT_FILE = "%1$tH:%1$tM:%1$tS [%3$s]: %4$s%5$s%n";
	public static final Level FATAL = createLogLevel("FATAL", 2000);

	//Global set of loggers, used to properly close FileHandlers.
	private static final Map<String, Log> LOGGERS = new ConcurrentHashMap<>();
	private static final ConsoleHandler CONSOLE_HANDLER;
	
	static {
		CONSOLE_HANDLER = new ConsoleHandler();

		//Log messages to the console like:
		//14:59:26 [WARNING] [ENGINE]: There is a problem!
		//Exception - Stacktrace
		LogFormatter formatter = new LogFormatter("%1$tH:%1$tM:%1$tS [%3$s] [%2$s]: %4$s%5$s%n");
		CONSOLE_HANDLER.setFormatter(formatter);
		CONSOLE_HANDLER.setLevel(Level.INFO);
		new File("logs").mkdir();
	}
	
	private Logger logger;
	private FileHandler fileHandler;
	
	/**
	 * Creates a new Log with the given name. Messages published to the log will be written to a file,
	 * located in a folder called "logs", and optionally to the console. 
	 * 
	 * @param name
	 * 		the name of the logger
	 * @param console
	 * 		if true, messages logged to this Log will be sent to the console
	 * @throws IOException 
	 * 		if opening a FileHandler for the Log throws an exception
	 */
	public Log(String name, boolean console) throws IOException {
		this(name, "logs", console);
	}
	
	/**
	 * Creates a new Log with the given name. Messages published to the log will be written to a file,
	 * located at the given location, and optionally to the console. 
	 * 
	 * @param name
	 * 		the name of the logger
	 * @param location
	 * 		the location of the folder where the file will be saved
	 * @param console
	 * 		if true, messages logged to this Log will be sent to the console
	 * @throws IOException 
	 * 		if opening a FileHandler for the Log throws an exception
	 */
	public Log(String name, String location, boolean console) throws IOException {
		this.logger = Logger.getLogger(name);
		this.logger.setUseParentHandlers(false);

		//The file handler will only create one file, and it will be at most 5 MB.
		this.fileHandler = new FileHandler(location + "/" + name + ".log", 5 * 1024 * 1024, 1, false);
		this.fileHandler.setFormatter(new LogFormatter(FORMAT_FILE));
		this.logger.addHandler(this.fileHandler);

		if (console) {
			this.logger.addHandler(CONSOLE_HANDLER);
		}

		LOGGERS.put(name, this);
	}
	
	/**
	 * @return
	 * 		the java.util.logging.Logger of the Log
	 */
	public Logger getLogger() {
		return this.logger;
	}
	
	/**
	 * @return
	 * 		the file handler of this Log
	 */
	public FileHandler getFileHandler() {
		return this.fileHandler;
	}
	
	/**
	 * Sets the level of the current logger to the given level.
	 * 
	 * @param level
	 * 		the level to set to
	 * @see Logger#setLevel(Level)
	 */
	public void setLevel(Level level) {
		logger.setLevel(level);
		fileHandler.setLevel(level);
	}

	/**
	 * Log message at level FINEST.
	 * 
	 * @param msg
	 * 		the message to log
	 */
	public void finest(String msg) {
		logger.finest(msg);
	}
	
	/**
	 * Log message at level FINER.
	 * 
	 * @param msg
	 * 		the message to log
	 */
	public void finer(String msg) {
		logger.finer(msg);
	}
	
	/**
	 * Log message at level FINE.
	 * 
	 * @param msg
	 * 		the message to log
	 */
	public void fine(String msg) {
		logger.fine(msg);
	}
	
	/**
	 * Log message at level INFO.
	 * 
	 * @param msg
	 * 		the message to log
	 */
	public void info(String msg) {
		logger.info(msg);
	}
	
	/**
	 * Log message and throwable at level INFO.
	 * 
	 * @param msg
	 * 		the message to log
	 * @param ex
	 * 		the throwable (exception) to log
	 */
	public void info(String msg, Throwable ex) {
		logger.log(Level.INFO, msg, ex);
	}
	
	/**
	 * Log message at level WARNING.
	 * 
	 * @param msg
	 * 		the message to log
	 */
	public void warning(String msg) {
		logger.warning(msg);
	}
	
	/**
	 * Log message and throwable at level WARNING.
	 * 
	 * @param msg
	 * 		the message to log
	 * @param ex
	 * 		the throwable (exception) to log
	 */
	public void warning(String msg, Throwable ex) {
		logger.log(Level.WARNING, msg, ex);
	}
	
	/**
	 * Log message at level SEVERE.
	 * 
	 * @param msg
	 * 		the message to log
	 */
	public void severe(String msg) {
		logger.severe(msg);
	}
	
	/**
	 * Log message and throwable at level SEVERE.
	 * 
	 * @param msg
	 * 		the message to log
	 * @param ex
	 * 		the throwable (exception) to log
	 */
	public void severe(String msg, Throwable ex) {
		logger.log(Level.SEVERE, msg, ex);
	}
	
	/**
	 * Log message at level FATAL.
	 * 
	 * @param msg
	 * 		the message to log
	 */
	public void fatal(String msg) {
		logger.log(FATAL, msg);
	}
	
	/**
	 * Log message and throwable at level FATAL.
	 * 
	 * @param msg
	 * 		the message to log
	 * @param ex
	 * 		the throwable (exception) to log
	 */
	public void fatal(String msg, Throwable ex) {
		logger.log(FATAL, msg, ex);
	}

	/**
	 * Logs the given message and throwable at the given level.
	 * 
	 * @param lvl
	 * 		the level to log at
	 * @param msg
	 * 		the message to log
	 * @param ex
	 * 		the throable (exception) to log
	 */
	public void log(Level lvl, String msg, Throwable ex) {
		logger.log(lvl, msg, ex);
	}

	/**
	 * Logs the given message at the given level.
	 * 
	 * @param lvl
	 * 		the level to log at
	 * @param msg
	 * 		the message to log
	 */
	public void log(Level lvl, String msg) {
		logger.log(lvl, msg);
	}
	
	/**
	 * Sets the minimal level for messages to be logged to the console.
	 * 
	 * @param level
	 * 		the Level to set
	 */
	public static void setConsoleLevel(Level level) {
		CONSOLE_HANDLER.setLevel(level);
	}
	
	/**
	 * Returns a Log with the given name. If no Log with the given name exists, this method creates
	 * one with console logging enabled and returns it.
	 * 
	 * @param name
	 * 		the name of the Log
	 * 
	 * @return
	 * 		a Log with the given name
	 */
	@SneakyThrows(IOException.class)
	public static Log getLog(String name) {
		Log tbr = LOGGERS.get(name);
		
		if (tbr == null) {
			return new Log(name, true);
		} else {
			return tbr;
		}
	}
	
	/**
	 * Shuts down all Logs by closing all the file handlers, and clearing the {@link #LOGGERS} map.
	 */
	public static synchronized void shutdown() {
		for (Log log : LOGGERS.values()) {
			//First remove the file handler, then close it
			log.logger.removeHandler(log.fileHandler);
			log.fileHandler.close();
		}
		
		LOGGERS.clear();
	}
	
	/**
	 * Creates a new {@link java.util.logging.Level Level} with the given name and
	 * value.
	 * 
	 * @param name
	 * 		the name of the level to create
	 * @param value
	 * 		the value of the level to create
	 * @return
	 * 		the newly created log level
	 */
	public static Level createLogLevel(String name, int value) {
		//Level has a protected constructor, so we define an anonymous extending class to be able to create levels.
		return new Level(name, value) {
			private static final long serialVersionUID = 1L;
		};
	}
}
