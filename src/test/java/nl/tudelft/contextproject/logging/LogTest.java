package nl.tudelft.contextproject.logging;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.mockito.ArgumentCaptor;

import nl.tudelft.contextproject.TestBase;

/**
 * Test class for {@link Log}.
 */
public class LogTest extends TestBase {

	@Rule
	public TemporaryFolder tempFolder = new TemporaryFolder();
	public Log log;
	public Handler handler;
	
	/**
	 * Creates a new Log and handler before every test.
	 * 
	 * @throws Exception
	 * 		if an exception occurs while setting up the logger
	 */
	@Before
	public void setUp() throws Exception {
		log = new Log("test", tempFolder.getRoot().getAbsolutePath(), false);
		log.setLevel(Level.ALL);
		
		//Create a mock handler, that accepts any message as logable.
		handler = mock(Handler.class);
		when(handler.isLoggable(any())).thenReturn(true);
		when(handler.getLevel()).thenReturn(Level.ALL);
		
		log.getLogger().addHandler(handler);
	}
	
	/**
	 * Verifies that a message was logged on the given level, with the given exception (can be null).
	 * 
	 * @param level
	 * 		the level the message should have been logged at
	 * @param msg
	 * 		the message that should have been logged
	 * @param ex
	 * 		the exception logged with the message, or null for no exception
	 */
	public void verifyLogged(Level level, String msg, Exception ex) {
		//We need to capture arguments
		ArgumentCaptor<LogRecord> argumentCaptor = ArgumentCaptor.forClass(LogRecord.class);
		verify(handler, times(1)).publish(argumentCaptor.capture());

		//Check if the level was correct
		assertEquals(level, argumentCaptor.getValue().getLevel());

		//Check if the message was correct
		assertEquals(msg, argumentCaptor.getValue().getMessage());

		//Check if the trowable was the same
		assertSame(ex, argumentCaptor.getValue().getThrown());
	}
	
	/**
	 * Test the constructor {@link Log#Log(String, boolean)}.
	 * 
	 * @throws IOException
	 * 		if an IOException is thrown while creating the file
	 */
	@Test
	public void testLog() throws IOException {
		Log otherlog = new Log("hello", true);

		//There should be a filehandler and a consolehandler.
		assertEquals(2, otherlog.getLogger().getHandlers().length);
	}

	/**
	 * Tests if {@link Log#setLevel(Level)} changes the level, such that messages of lower levels
	 * are not propagated to the handlers.
	 */
	@Test
	public void testSetLevel_notLogged() {
		log.setLevel(Level.SEVERE);
		log.warning("1");
		
		//Since the logger only accepts messages of level SEVERE or higher, we should not have been called.
		verify(handler, never()).publish(any());
	}
	
	/**
	 * Tests if {@link Log#setLevel(Level)} changes the level, such that messages of the same or higher levels
	 * are propagated to the handlers.
	 */
	@Test
	public void testSetLevel_logged() {
		log.setLevel(Level.INFO);
		log.info("2");
		
		//Since our the logger accepts messages of our level, we should have been called once.
		verify(handler, times(1)).publish(any());
	}

	/**
	 * Tests if {@link Log#finest(String)} works correctly.
	 */
	@Test
	public void testFinest() {
		log.finest("finest message");
		verifyLogged(Level.FINEST, "finest message", null);
	}

	/**
	 * Tests if {@link Log#finer(String)} works correctly.
	 */
	@Test
	public void testFiner() {
		log.finer("finer message");
		verifyLogged(Level.FINER, "finer message", null);
	}

	/**
	 * Tests if {@link Log#fine(String)} works correctly.
	 */
	@Test
	public void testFine() {
		log.fine("fine message");
		verifyLogged(Level.FINE, "fine message", null);
	}

	/**
	 * Tests if {@link Log#info(String)} works correctly.
	 */
	@Test
	public void testInfoString() {
		log.info("info message");
		verifyLogged(Level.INFO, "info message", null);
	}
	
	/**
	 * Tests if {@link Log#info(String, Throwable)} works correctly.
	 */
	@Test
	public void testInfoStringThrowable() {
		Exception ex = new Exception();
		log.info("info message + exception", ex);
		verifyLogged(Level.INFO, "info message + exception", ex);
	}

	/**
	 * Tests if {@link Log#warning(String)} works correctly.
	 */
	@Test
	public void testWarningString() {
		log.warning("warning message");
		verifyLogged(Level.WARNING, "warning message", null);
	}

	/**
	 * Tests if {@link Log#warning(String, Throwable)} works correctly.
	 */
	@Test
	public void testWarningStringThrowable() {
		Exception ex = new Exception();
		log.warning("warning message + exception", ex);
		verifyLogged(Level.WARNING, "warning message + exception", ex);
	}

	/**
	 * Tests if {@link Log#severe(String)} works correctly.
	 */
	@Test
	public void testSevereString() {
		log.severe("severe message");
		verifyLogged(Level.SEVERE, "severe message", null);
	}

	/**
	 * Tests if {@link Log#severe(String, Throwable)} works correctly.
	 */
	@Test
	public void testSevereStringThrowable() {
		Exception ex = new Exception();
		log.severe("severe message + exception", ex);
		verifyLogged(Level.SEVERE, "severe message + exception", ex);
	}

	/**
	 * Tests if {@link Log#fatal(String)} works correctly.
	 */
	@Test
	public void testFatalString() {
		log.fatal("fatal message");
		verifyLogged(Log.FATAL, "fatal message", null);
	}

	/**
	 * Tests if {@link Log#fatal(String, Throwable)} works correctly.
	 */
	@Test
	public void testFatalStringThrowable() {
		Exception ex = new Exception();
		log.fatal("fatal message + exception", ex);
		verifyLogged(Log.FATAL, "fatal message + exception", ex);
	}

	/**
	 * Tests if {@link Log#log(Level, String, Throwable)} works correctly.
	 */
	@Test
	public void testLogLevelStringThrowable() {
		//Test the method by Logging on the CONFIG level
		Exception ex = new Exception();
		log.log(Level.CONFIG, "specific level message + exception", ex);
		verifyLogged(Level.CONFIG, "specific level message + exception", ex);
	}

	/**
	 * Tests if {@link Log#log(Level, String)} works correctly.
	 */
	@Test
	public void testLogLevelString() {
		//Test the method by logging on the CONFIG level
		log.log(Level.CONFIG, "specific level message");
		verifyLogged(Level.CONFIG, "specific level message", null);
	}
	
	/**
	 * Tests if {@link Log#getLog(String)} returns already created logs when applicable.
	 */
	@Test
	public void testGetLog_alreadyCreated() {
		assertSame(log, Log.getLog("test"));
	}
	
	/**
	 * Tests if {@link Log#getLog(String)} creates new logs when applicable.
	 */
	@Test
	public void testGetLog_create() {
		Log createlog = Log.getLog("aRandomName");
		
		assertNotNull(createlog);
	}
	
	/**
	 * Tests if {@link Log#getLog(String)} throws an IOException for invalid logger names.
	 */
	@Test(expected = IOException.class)
	public void testGetLog_invalidName() {
		Log.getLog("/k/");
	}

	/**
	 * Tests if {@link Log#createLogLevel(String, int)} correctly creates levels.
	 */
	@Test
	public void testCreateLogLevel() {
		Level level = Log.createLogLevel("IMPORTANT", 950);

		//Verify that the level was created correctly
		assertEquals("IMPORTANT", level.getName());
		assertEquals(950, level.intValue());

		//Verify that the level works with log
		log.log(level, "important message");
		verifyLogged(level, "important message", null);
	}

	/**
	 * Tests if {@link Log#shutdown()} correctly closes all FileHandlers.
	 */
	@Test
	public void testShutdown() {
		//Shutdown the log
		Log.shutdown();

		//A file handler is closed, when isLoggable returns false for messages that should otherwise be logged.
		assertFalse(log.getFileHandler().isLoggable(new LogRecord(Level.SEVERE, "shutdown")));
	}

	/**
	 * Tests if {@link Log#setConsoleLevel(Level)} correctly changes the console log level.
	 * 
	 * @throws IOException
	 * 		if an IOException occurs when creating the log
	 */
	@Test
	public void testSetConsoleLevel() throws IOException {
		//Create a new log that logs to the console
		Log logToConsole = new Log("test2", tempFolder.getRoot().getAbsolutePath(), true);

		//Create the console handler
		ConsoleHandler consoleHandler = null;
		for (Handler handler : logToConsole.getLogger().getHandlers()) {
			if (handler instanceof ConsoleHandler) {
				consoleHandler = (ConsoleHandler) handler;
			}
		}

		//The console handler should not be null
		assertNotNull(consoleHandler);

		//Change the level
		Log.setConsoleLevel(Level.CONFIG);

		//Verify that the level was changed
		assertEquals(Level.CONFIG, consoleHandler.getLevel());
	}

}
