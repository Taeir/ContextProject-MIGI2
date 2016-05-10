package nl.tudelft.contextproject.util;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;

import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * Test for the JSONUtil class.
 */
public class JSONUtilTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private JSONObject mockedJSONObject;
    private File testFile;

    /**
     * Set up all objects used in testing.
     */
    @Before
    public void setUp() {
        mockedJSONObject = mock(JSONObject.class);
        testFile = new File("UtilTestFile.json");
        when(mockedJSONObject.toString(anyInt())).thenReturn("{}");
    }

    /**
     * Remove the test file if it has been created.
     */
    @After
    public void tearDown() {
        if (testFile.exists()) {
            testFile.delete();
        }
    }

    /**
     * Test for checking if an IOException occurs if one tries
     * to load a non-existing file.
     * @throws IOException - The expected exception.
     */
    @Test
    public void testLoadFileNotFound() throws IOException {
        thrown.expect(IOException.class);
        File notFound = new File("NotARealFile.json");
        JSONUtil.load(notFound);
    }

    /**
     * Test for loading an existing file.
     * @throws IOException - File not found.
     */
    @Test
    public void testLoadExistingFile() throws IOException {
        JSONUtil.save(mockedJSONObject, testFile);
        assertNotNull(JSONUtil.load(testFile));
    }

    /**
     * Test saving of a file.
     * @throws IOException - If file writing goes wrong.
     */
    @Test
    public void testSaveFile() throws IOException {
        JSONUtil.save(mockedJSONObject, testFile);
    }
}
