package fr.cnes.sirius.addons.patriusdataset;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;
import java.util.regex.Pattern;

import org.junit.Test;

public class ResourceListTest {

	/**
	 * Error test using an unsupported protocol
	 * 
	 * @throws URISyntaxException
	 * @throws IOException
	 */
	@Test
	public void testGetResources() throws URISyntaxException, IOException {
		URL url = new URL("http", "localhost", "fr/cnes/sirius");
		String resourcePath = "fr/cnes/sirius";
		Pattern resourcePattern = Pattern.compile(".*\\.history$");
		List<String> list = ResourceList.getResources(this.getClass().getClassLoader(), url, resourcePath, resourcePattern);
		assertEquals(1, list.size());
	}

}
