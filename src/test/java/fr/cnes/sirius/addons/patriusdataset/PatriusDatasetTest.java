package fr.cnes.sirius.addons.patriusdataset;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.regex.Pattern;

import org.junit.Assert;
import org.junit.Test;

import fr.cnes.sirius.patrius.bodies.CelestialBody;
import fr.cnes.sirius.patrius.bodies.CelestialBodyFactory;
import fr.cnes.sirius.patrius.frames.CelestialBodyFrame;
import fr.cnes.sirius.patrius.frames.FramesFactory;
import fr.cnes.sirius.patrius.frames.transformations.Transform;
import fr.cnes.sirius.patrius.time.AbsoluteDate;
import fr.cnes.sirius.patrius.time.TimeScalesFactory;
import fr.cnes.sirius.patrius.time.UT1Scale;
import fr.cnes.sirius.patrius.time.UTCScale;
import fr.cnes.sirius.patrius.utils.exception.PatriusException;

public class PatriusDatasetTest {

	/**
	 * Nominal test
	 * @throws PatriusException 
	 * 
	 */
	@Test
	public void testAddResourcesFromPatriusDataset() throws PatriusException {
		PatriusDataset.addResourcesFromPatriusDataset();
		
		UTCScale utc = TimeScalesFactory.getUTC();
		AbsoluteDate date = new AbsoluteDate("2020-12-20T05:03:58", utc);
		
		UT1Scale ut1 = TimeScalesFactory.getUT1();
		
		CelestialBodyFrame itrf = FramesFactory.getITRF();
		
		Transform transform = FramesFactory.getGCRF().getTransformTo(itrf, date);
		
		CelestialBody sun = CelestialBodyFactory.getSun();
		
		Assert.assertNotNull(utc);
		Assert.assertNotNull(ut1);
		Assert.assertNotNull(sun);
		Assert.assertNotNull(itrf);
		Assert.assertNotNull(transform);
	}

	/**
	 * Nominal test with an url resolver
	 */
	@Test
	public void testAddResourcesFromPatriusDatasetClassLoaderURLResolver() {
		PatriusDataset.addResourcesFromPatriusDataset(PatriusDataset.class.getClassLoader(), new URLResolver() {

			@Override
			public URL resolve(URL url) throws IOException {
				return url;
			}
		});
	}

	/**
	 * Use of an inconsistent class loader in order to generate a Error
	 * 
	 */
	@Test(expected = Error.class)
	public void testAddResourcesFromPatriusDatasetClassLoaderError() {
		PatriusDataset.addResourcesFromPatriusDataset(new ClassLoader() {
			@Override
			public InputStream getResourceAsStream(String name) {
				return null;
			}
		}, null);
	}

	/**
	 * Use of an inconsistent class loader in order to generate a Error
	 * 
	 */
	@Test
	public void testAddResourcesFromPatriusDatasetClassLoaderErrorBis() {
		PatriusDataset.addResourcesFromPatriusDataset(new ClassLoader() {
			@Override
			public Enumeration<URL> getResources(String name) throws IOException {
				return new Enumeration<URL>() {
					boolean hasMore = true;
					final URL url = new URL("http://fr.cnes.sirius");

					@Override
					public boolean hasMoreElements() {
						return hasMore;
					}

					@Override
					public URL nextElement() {
						hasMore = false;
						return url;
					}

				};
			}
		}, null);
	}

	/**
	 * Nominal test loading data from jar files and directories
	 * 
	 */
	@Test
	public void testAddResourcesFromPatriusDatasetFromJar() {
		// search in classpath for META-INF data, sure we get data from jar files of
		// maven dependencies (and also directories)
		PatriusDataset.addResourcesFromPatriusDataset(PatriusDataset.class.getClassLoader(), null, "META-INF",
				Pattern.compile(".*"));
	}

}
