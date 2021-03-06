package nl.tudelft.contextproject.logging;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

/**
 * Formatter for the {@link Log} class.
 * 
 * <p>Based on {@link java.util.logging.SimpleFormatter SimpleFormatter}.
 */
public class LogFormatter extends Formatter {
	private final String format;
	
	/**
	 * Creates a new LogFormatter with the given format.
	 * 
	 * <p>LogRecords will be formatted with {@link String#format} with the following arguments:
	 * <ol>
	 * <li>The date/time</li>
	 * <li>The name of the logger</li>
	 * <li>The localized name of the level</li>
	 * <li>The message</li>
	 * <li>A throwable</li>
	 * </ol>
	 * 
	 * @param format
	 * 		the format to use
	 */
	public LogFormatter(String format) {
		this.format = format;
	}

	/**
	 * Formats the given LogRecord with the format set in this LogFormat.
	 *
	 * @param record
	 * 		the LogRecord to format
	 * @return
	 * 		a String representation of the record, using the format of this LogFormatter.
	 */
    @Override
	public String format(LogRecord record) {
        String message = formatMessage(record);
        String throwable = "";

        if (record.getThrown() != null) {
            StringWriter stringWriter = new StringWriter();
            PrintWriter printWriter = new PrintWriter(stringWriter);
            printWriter.println();
            record.getThrown().printStackTrace(printWriter);
            printWriter.close();
            throwable = stringWriter.toString();
        }

        return String.format(format, record.getMillis(), record.getLoggerName(), record.getLevel().getLocalizedName(), message, throwable);
    }
}
