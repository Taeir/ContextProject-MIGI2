package nl.tudelft.contextproject.util;

import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.File;
import java.io.IOException;

import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Test for the JSONUtil class.
 */
public class JSONUtilTest {

    private JSONObject mockedJSONObject;
    private File testFile;

    @Rule
    public ExpectedException thrown = ExpectedException.none();


    /**
     * Set up all objects used in testing.
     */
    @Before
    public void setUp() {
        mockedJSONObject = mock(JSONObject.class);
        testFile = new File("UtilTestFile.json");
    }

    /**
     * Remove the test file if it has been created.
     */
    @After
    public void tearDown() {
        if(testFile.exists()) {
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
     * Test saving of a file.
     * @throws IOException - If file writing goes wrong.
     */
    @Test
    public void testSaveFile() throws IOException {
        when(mockedJSONObject.toString(anyInt())).thenReturn("{}");
        JSONUtil.save(mockedJSONObject, testFile);
    }
}
