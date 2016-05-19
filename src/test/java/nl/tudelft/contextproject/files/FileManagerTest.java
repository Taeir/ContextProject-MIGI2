package nl.tudelft.contextproject.files;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Test class for {@link FileManager}.
 */
public class FileManagerTest {
	private static boolean jar;
	private static String path;
	private static File testFile;
	
	/**
	 * Saves the values of the fields in FileManager, so the values can be restored after the tests.
	 * 
	 * @throws Exception
	 * 		if an exception occurs in finding the testjar file.
	 */
	@BeforeClass
	public static void setUpClass() throws Exception {
		jar = FileManager.jar;
		path = FileManager.path;
		
		testFile = new File(FileManagerTest.class.getResource("/filemanager_jar.jar").toURI());
	}
	
	/**
	 * Restores the variables in FileManager to the correct values.
	 */
	@AfterClass
	public static void tearDownClass() {
		FileManager.jar = jar;
		FileManager.path = path;
	}
	
	/**
	 * Delete remaining files after every test.
	 */
	@After
	public void tearDown() {
		delete(new File("filemanager_jar"));
	}
	
	/**
	 * Deletes the given file/folder recursively.
	 * 
	 * @param file
	 * 		the file to delete
	 */
	private void delete(File file) {
		File[] files = file.listFiles();
		if (files != null) {
			for (File f : files) {
				delete(f);
			}
		}
		
		file.delete();
	}

	/**
	 * Test for {@link FileManager#init()}.
	 */
	@Test
	public void testInit() {
		FileManager.setJar(true);
		FileManager.init();
		
		assertFalse(FileManager.jar);
		assertNotNull(FileManager.path);
	}

	/**
	 * Tests {@link FileManager#getFile(String)}, when running from a jar, and using a start slash.
	 */
	@Test
	public void testGetFile_jar_startslash() {
		FileManager.setJar(true);
		FileManager.setPath(testFile.getPath());
		
		assertEquals("file0.txt", FileManager.getFile("/filemanager_jar/file0.txt").getName());
	}
	
	/**
	 * Tests {@link FileManager#getFile(String)}, when running from a jar, without a start slash.
	 */
	@Test
	public void testGetFile_jar_noslash() {
		FileManager.setJar(true);
		FileManager.setPath(testFile.getPath());
		
		assertEquals("file3.txt", FileManager.getFile("filemanager_jar/file3.txt").getName());
	}
	
	/**
	 * Tests {@link FileManager#getFile(String)}, when running from a jar, when getting a folder.
	 */
	@Test
	public void testGetFile_jar_folder() {
		FileManager.setJar(true);
		FileManager.setPath(testFile.getPath());
		
		File file = FileManager.getFile("filemanager_jar/folder/");
		
		assertNotNull(file);
		assertEquals(2, file.listFiles().length);
	}
	
	/**
	 * Tests {@link FileManager#getFile(String)}, when running from files, and using a start slash.
	 */
	@Test
	public void testGetFile_file_startslash() {
		FileManager.setJar(false);
		
		assertEquals("file0.txt", FileManager.getFile("/filemanager_file/file0.txt").getName());
	}
	
	/**
	 * Tests {@link FileManager#getFile(String)}, when running from files, without a start slash.
	 */
	@Test
	public void testGetFile_file_noslash() {
		FileManager.setJar(false);
		
		assertEquals("file3.txt", FileManager.getFile("filemanager_file/file3.txt").getName());
	}
	
	/**
	 * Tests {@link FileManager#getFile(String)}, when running from files, when getting a folder.
	 */
	@Test
	public void testGetFile_file_folder() {
		FileManager.setJar(false);
		
		File file = FileManager.getFile("filemanager_file/folder/");

		assertNotNull(file);
		assertEquals(2, file.listFiles().length);
	}

	/**
	 * Test for {@link FileManager#getFileNames(String)}, when running from a jar.
	 */
	@Test
	public void testGetFileNames_jar() {
		FileManager.setJar(true);
		FileManager.setPath(testFile.getPath());
		
		assertEquals(0, FileManager.getFileNames("/filemanager_jar/emptyfolder").length);
		assertEquals(2, FileManager.getFileNames("filemanager_jar/folder/").length);
	}
	
	/**
	 * Test for {@link FileManager#getFileNames(String)}, when running from files.
	 */
	@Test
	public void testGetFileNames_file() {
		FileManager.setJar(false);
		
		String[] names = FileManager.getFileNames("filemanager_file/emptyfolder");
		assertTrue(names == null || names.length == 0);
		assertEquals(2, FileManager.getFileNames("/filemanager_file/folder/").length);
	}

}
