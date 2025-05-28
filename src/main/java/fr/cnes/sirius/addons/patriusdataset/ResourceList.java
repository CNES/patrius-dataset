package fr.cnes.sirius.addons.patriusdataset;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * Helper class that get a list resources available in the classpath
 * 
 * @author LacotteM
 */
public final class ResourceList {
	private static final String FILE_PROTOCOL = "file";
	private static final String JAR_PROTOCOL = "jar";
	/*
	 * class files exclusion
	 */
	private static final Pattern CLASS_EXCLUSION = Pattern.compile(".*\\.class$");

	public static String FALLBACK_RESOURCES_LIST_PATH = "fr/cnes/sirius/addons/patriusdataset/resources-list.txt";

	private ResourceList() {
		// prevent instantiation
	}

	/**
	 * For all non-class elements of java classpath with path resourcePath, gets a
	 * Collection of resources with pattern resourcePattern
	 * 
	 * @param cl              the class loader to use
	 * @param urlResolver     the url resolver needed to transform classpath URL to
	 *                        native java classpath library, in non classic
	 *                        environments such as OSGI <br>
	 *                        If unset null, no resolver is used
	 * @param resourcePath    the parent path of resources in classpath
	 * @param resourcePattern the pattern that must match file names of resources
	 * @return the resources array in the order they are found
	 * @throws IOException        if an error occurs during the resolution
	 * @throws URISyntaxException if url is not well formatted
	 * @see URLResolver
	 */
	public static String[] getResourcesArray(ClassLoader cl, URLResolver urlResolver, final String resourcePath,
			final Pattern resourcePattern) throws IOException, URISyntaxException {
		return getResourcesList(cl, urlResolver, resourcePath, resourcePattern).toArray(new String[] {});
	}

	/**
	 * for all elements of java.class.path get a list of resources Pattern<br>
	 * pattern = Pattern.compile(".*"); gets all resources
	 * 
	 * @param cl              the class loader to use
	 * @param urlResolver     the url resolver needed to transform classpath URL to
	 *                        native java classpath library, in non classic
	 *                        environments such as OSGI <br>
	 *                        If unset null, no resolver is used
	 * @param resourcePath    the parent path of resources in classpath
	 * @param resourcePattern the pattern that must match file names of resources
	 * @return the resources list in the order they are found
	 * @throws IOException        if an error occurs during the resolution
	 * @throws URISyntaxException if a classpath url is not well formatted
	 * @see URLResolver
	 */
	public static List<String> getResourcesList(final ClassLoader cl, final URLResolver urlResolver,
			final String resourcePath, final Pattern resourcePattern) throws IOException, URISyntaxException {
		final List<String> retval = new ArrayList<String>();
		final String path;

		// trim last slash
		if (resourcePath.length() > 1 && resourcePath.endsWith("/")) {
			path = resourcePath.substring(0, resourcePath.length() - 1);
		} else {
			path = resourcePath;
		}

		Enumeration<URL> urls = cl.getResources(path);

		while (urls.hasMoreElements()) {
			URL url = urls.nextElement();
			if (urlResolver != null) {
				url = urlResolver.resolve(url);
			}
			retval.addAll(getResources(cl, url, path, resourcePattern));
		}

		return retval;
	}

	/**
	 * Gets a collection of resources from an url
	 * 
	 * @param cl              the class loader to use
	 * @param url             url of classpath resource
	 * @param resourcePath    the parent path of resources in classpath
	 * @param resourcePattern the pattern that must match file names of resources
	 * @return a list of paths in classpath
	 * @throws URISyntaxException if url is not well formatted
	 * @throws IOException        if an error occurs during the resolution
	 */
	public static List<String> getResources(final ClassLoader cl, final URL url, final String resourcePath,
			final Pattern resourcePattern) throws URISyntaxException, IOException {

		List<String> list = null;
		final String protocol = url.getProtocol();
		switch (protocol) {
		case FILE_PROTOCOL: {
			File file = new File(url.toURI());
			list = ResourceList.getResourcesFromDirectory(file, resourcePath, resourcePattern);
		}
			break;

		case JAR_PROTOCOL: {
			String jarPath = url.getPath().substring(0, url.getPath().indexOf("!")); // strip out only the JAR file
			File file = new File(new URI(jarPath));
			list = ResourceList.getResourcesFromJarFile(file, resourcePath, resourcePattern);
		}
			break;

		default:
			// fallback : use list of resources in resource
			InputStream is = cl.getResourceAsStream(FALLBACK_RESOURCES_LIST_PATH);
			list = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8)).lines()
					.filter(s -> s.startsWith(resourcePath)).filter(s -> nameMatches(s, resourcePattern))
					.collect(Collectors.toList());
			is.close();
		}

		return list;

	}

	/**
	 * gets resources from jar file<br>
	 * The resources paths match resourcePath and files names resourcePattern
	 * 
	 * @param file            jar file
	 * @param resourcePath    the parent path of resources in classpath
	 * @param resourcePattern the pattern that must match file names of resources
	 * @return a list of paths in classpath
	 * @throws IOException if an error occurs during the resolution
	 */
	public static List<String> getResourcesFromJarFile(final File file, final String resourcePath,
			final Pattern resourcePattern) throws IOException {
		final List<String> retval = new ArrayList<String>();
		final ZipFile zf = new ZipFile(file);

		final Enumeration<? extends ZipEntry> e = zf.entries();
		while (e.hasMoreElements()) {
			final ZipEntry ze = e.nextElement();
			final String path = ze.getName();

			if (!ze.isDirectory() && path.startsWith(resourcePath)) {
				int index = path.lastIndexOf("/");
				final String fileName = path.substring(index + 1);
				if (nameMatches(fileName, resourcePattern)) {
					retval.add(path);
				}
			}
		}
		zf.close();

		return retval;
	}

	/**
	 * Gets resources recursively from directory
	 * 
	 * @param directory       the directory where to look for
	 * @param resourcePath    the parent path of resources in classpath
	 * @param resourcePattern the pattern that must match file names of resources
	 * @return a list of paths in classpath
	 * @throws IOException if an error occurs during the resolution
	 */
	public static List<String> getResourcesFromDirectory(final File directory, final String resourcePath,
			final Pattern resourcePattern) throws IOException {

		final String path = directory.getCanonicalPath().replace('\\', '/');
		final int classpathRootLength = path.length() - resourcePath.length();

		return getResourcesFromDirectory(directory, resourcePattern, classpathRootLength);
	}

	public static List<String> getResourcesFromDirectory(final File directory, final Pattern resourcePattern,
			final int classpathRootLength) throws IOException {
		final List<String> retval = new ArrayList<String>();
		final File[] fileList = directory.listFiles();
		if (fileList != null) {
			for (final File file : fileList) {
				if (file.isDirectory()) {
					retval.addAll(getResourcesFromDirectory(file, resourcePattern, classpathRootLength));
				} else {
					final String fileName = file.getName();
					if (nameMatches(fileName, resourcePattern)) {
						String path = file.getCanonicalPath().replace('\\', '/');
						retval.add(path.substring(classpathRootLength));
					}
				}
			}
		}
		return retval;
	}

	/**
	 * helper method
	 * 
	 * @param fileName        file name
	 * @param resourcePattern pattern to match
	 * @return true if fileName matches and is not a java class
	 */
	private static boolean nameMatches(final String fileName, final Pattern resourcePattern) {
		return resourcePattern.matcher(fileName).matches() && !(CLASS_EXCLUSION.matcher(fileName).matches());
	}
}
