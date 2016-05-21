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

/**
 * Test class for {@link Log}.
 */
public class TestLog {

	@Rule
	public TemporaryFolder temp = new TemporaryFolder();
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
		log = new Log("test", temp.getRoot().getAbsolutePath(), false);
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
		ArgumentCaptor<LogRecord> ac = ArgumentCaptor.forClass(LogRecord.class);
		verify(handler, times(1)).publish(ac.capture());

		assertEquals(level, ac.getValue().getLevel());
		assertEquals(msg, ac.getValue().getMessage());
		assertSame(ex, ac.getValue().getThrown());
	}
	
	/**
	 * Test the constructor {@link Log#Log(String, boolean)}.
	 * 
	 * @throws IOException
	 * 		if an IOException is thrown while creating the file
	 */
	@Test
	public void testLog() throws IOException {
		Log nlog = new Log("hello", true);

		assertEquals(2, nlog.getLogger().getHandlers().length);
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
		Exception ex = new Exception();
		log.log(Level.CONFIG, "specific level message + exception", ex);
		verifyLogged(Level.CONFIG, "specific level message + exception", ex);
	}

	/**
	 * Tests if {@link Log#log(Level, String)} works correctly.
	 */
	@Test
	public void testLogLevelString() {
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
		Log rlog = Log.getLog("aRandomName");
		
		assertNotNull(rlog);
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

		assertEquals("IMPORTANT", level.getName());
		assertEquals(950, level.intValue());

		log.log(level, "important message");
		verifyLogged(level, "important message", null);
	}

	/**
	 * Tests if {@link Log#shutdown()} correctly closes all FileHandlers.
	 */
	@Test
	public void testShutdown() {
		Log.shutdown();

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
		Log logToConsole = new Log("test2", temp.getRoot().getAbsolutePath(), true);

		ConsoleHandler ch = null;
		for (Handler h : logToConsole.getLogger().getHandlers()) {
			if (h instanceof ConsoleHandler) {
				ch = (ConsoleHandler) h;
			}
		}

		assertNotNull(ch);

		Log.setConsoleLevel(Level.CONFIG);

		assertEquals(Level.CONFIG, ch.getLevel());
	}

}
