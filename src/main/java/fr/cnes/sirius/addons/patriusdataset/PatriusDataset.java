package fr.cnes.sirius.addons.patriusdataset;

import java.util.regex.Pattern;

import fr.cnes.sirius.patrius.data.ClasspathCrawler;
import fr.cnes.sirius.patrius.data.DataProvider;
import fr.cnes.sirius.patrius.data.DataProvidersManager;
import fr.cnes.sirius.patrius.utils.exception.PatriusException;

/**
 * Helper class for adding patrius dataset resources to patrius with
 * {@link #addResourcesFromPatriusDataset()} method
 * 
 * @see #addResourcesFromPatriusDataset()
 * 
 * @author LacotteM
 * 
 */
public final class PatriusDataset {

	private PatriusDataset() {
		// prevent instantiation
	}

	/**
	 * Adds resources of patriusdataset found in the classpath to the Data Providers
	 * Manager
	 * 
	 */
	public static void addResourcesFromPatriusDataset() {
		addResourcesFromPatriusDataset(PatriusDataset.class.getClassLoader(), null);
	}

	/**
	 * Adds resources of patriusdataset found in the classpath to the Data Providers
	 * Manager
	 * 
	 * @param cl
	 *            the class loader to use
	 * @param urlResolver
	 *            the url resolver needed to transform classpath URL to native java
	 *            classpath library, in non classic environments such as OSGI <br>
	 *            If unset null, no resolver is used
	 * 
	 * @see URLResolver
	 */
	public static void addResourcesFromPatriusDataset(final ClassLoader cl, final URLResolver urlResolver) {
		addResourcesFromPatriusDataset(cl, urlResolver, "fr/cnes/sirius/addons/patriusdataset/", Pattern.compile(".*"));
	}

	/**
	 * Adds resources of patriusdataset found in the classpath to the Data Providers
	 * Manager
	 * 
	 * @param cl
	 *            the class loader to use
	 * @param urlResolver
	 *            the url resolver needed to transform classpath URL to native java
	 *            classpath library, in non classic environments such as OSGI <br>
	 *            If unset null, no resolver is used
	 * @param resourcePath
	 *            the parent path of resources in classpath
	 * @param resourcePattern
	 *            the pattern that must match file names of resources
	 * 
	 * @see URLResolver
	 */
	public static void addResourcesFromPatriusDataset(final ClassLoader cl, final URLResolver urlResolver,
			final String resourcePath, final Pattern resourcePattern) {

		String[] list = null;
		try {
			list = ResourceList.getResourcesArray(cl, urlResolver, resourcePath, resourcePattern);
		} catch (Exception e) {
			// may happen
			throw new Error("Error while getting resources from classpath", e);
		}

		DataProvider cp = null;
		try {
			cp = new ClasspathCrawler(cl, list);
		} catch (PatriusException e) {
			// should never happen
			throw new Error("Error while adding classpath resources to patrius", e);
		}

		DataProvidersManager.getInstance().addProvider(cp);
	}
}
