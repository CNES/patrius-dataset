package fr.cnes.sirius.addons.patriusdataset;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.junit.Test;

import fr.cnes.sirius.patrius.utils.exception.PatriusException;

public class GenerateResourceListTest {

	/**
	 * List generator
	 * 
	 * @throws PatriusException
	 * @throws URISyntaxException
	 * @throws IOException
	 * 
	 */
	@Test
	public void testAddResourcesFromPatriusDataset() throws PatriusException, IOException, URISyntaxException {
		String[] strings = ResourceList.getResourcesArray(this.getClass().getClassLoader(), null,
				"fr/cnes/sirius/addons/patriusdataset/", Pattern.compile(".*"));
		List<String> list = Arrays.asList(strings).stream().filter(s -> !s.matches(".*strange folder.*"))
				.filter(s -> !s.matches(".*/resources-list.txt$")).collect(Collectors.toList());
		Files.write(Paths.get("src", "main", "resources", "fr", "cnes", "sirius", "addons", "patriusdataset",
				"resources-list.txt"), list, StandardCharsets.UTF_8);
	}

}
