package nl.tudelft.contextproject.util;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Test class for {@link FileUtil}.
 */
public class FileUtilTest {
	private static boolean jar;
	private static String path;
	private static File testFile;
	
	/**
	 * Saves the values of the fields in FileUtil, so the values can be restored after the tests.
	 * 
	 * @throws Exception
	 * 		if an exception occurs in finding the testjar file.
	 */
	@BeforeClass
	public static void setUpClass() throws Exception {
		jar = FileUtil.jar;
		path = FileUtil.path;
		
		testFile = new File(FileUtilTest.class.getResource("/filemanager_jar.jar").toURI());
	}
	
	/**
	 * Restores the variables in FileUtil to the correct values.
	 */
	@AfterClass
	public static void tearDownClass() {
		FileUtil.jar = jar;
		FileUtil.path = path;
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
	 * Test for {@link FileUtil#init()}.
	 */
	@Test
	public void testInit() {
		FileUtil.setJar(true);
		FileUtil.init();
		
		assertFalse(FileUtil.jar);
		assertNotNull(FileUtil.path);
	}

	/**
	 * Tests {@link FileUtil#getFile(String)}, when running from a jar, and using a start slash.
	 */
	@Test
	public void testGetFile_jar_startslash() {
		FileUtil.setJar(true);
		FileUtil.setPath(testFile.getPath());
		
		assertEquals("file0.txt", FileUtil.getFile("/filemanager_jar/file0.txt").getName());
	}
	
	/**
	 * Tests {@link FileUtil#getFile(String)}, when running from a jar, without a start slash.
	 */
	@Test
	public void testGetFile_jar_noslash() {
		FileUtil.setJar(true);
		FileUtil.setPath(testFile.getPath());
		
		assertEquals("file3.txt", FileUtil.getFile("filemanager_jar/file3.txt").getName());
	}
	
	/**
	 * Tests {@link FileUtil#getFile(String)}, when running from a jar, when getting a folder.
	 */
	@Test
	public void testGetFile_jar_folder() {
		FileUtil.setJar(true);
		FileUtil.setPath(testFile.getPath());
		
		File file = FileUtil.getFile("filemanager_jar/folder/");
		
		assertNotNull(file);
		assertEquals(2, file.listFiles().length);
	}
	
	/**
	 * Tests {@link FileUtil#getFile(String)}, when running from files, and using a start slash.
	 */
	@Test
	public void testGetFile_file_startslash() {
		FileUtil.setJar(false);
		
		assertEquals("file0.txt", FileUtil.getFile("/filemanager_file/file0.txt").getName());
	}
	
	/**
	 * Tests {@link FileUtil#getFile(String)}, when running from files, without a start slash.
	 */
	@Test
	public void testGetFile_file_noslash() {
		FileUtil.setJar(false);
		
		assertEquals("file3.txt", FileUtil.getFile("filemanager_file/file3.txt").getName());
	}
	
	/**
	 * Tests {@link FileUtil#getFile(String)}, when running from files, when getting a folder.
	 */
	@Test
	public void testGetFile_file_folder() {
		FileUtil.setJar(false);
		
		File file = FileUtil.getFile("filemanager_file/folder/");

		assertNotNull(file);
		assertEquals(2, file.listFiles().length);
	}

	/**
	 * Test for {@link FileUtil#getFileNames(String)}, when running from a jar.
	 */
	@Test
	public void testGetFileNames_jar() {
		FileUtil.setJar(true);
		FileUtil.setPath(testFile.getPath());
		
		assertEquals(0, FileUtil.getFileNames("/filemanager_jar/emptyfolder").length);
		assertEquals(2, FileUtil.getFileNames("filemanager_jar/folder/").length);
	}
	
	/**
	 * Test for {@link FileUtil#getFileNames(String)}, when running from files.
	 */
	@Test
	public void testGetFileNames_file() {
		FileUtil.setJar(false);
		
		String[] names = FileUtil.getFileNames("filemanager_file/emptyfolder");
		assertTrue(names == null || names.length == 0);
		assertEquals(2, FileUtil.getFileNames("/filemanager_file/folder/").length);
	}

}
