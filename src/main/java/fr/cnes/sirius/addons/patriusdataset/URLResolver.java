package fr.cnes.sirius.addons.patriusdataset;

import java.io.IOException;
import java.net.URL;

/**
 * interface for URL resolver
 *
 *
 */
public interface URLResolver {

	/**
	 * Converts a URL that uses a client-defined protocol into a URL that uses a
	 * protocol which is native to the Java class library (file, jar, http, etc).
	 * <p>
	 * Note however that users of this API should not assume too much about the
	 * results of this method. While it may consistently return a file: URL in
	 * certain installation configurations, others may result in jar: or http: URLs.
	 * </p>
	 * <p>
	 * If the protocol is not recognized by this converter, then the original URL is
	 * returned as-is.
	 * </p>
	 * 
	 * @param url
	 *            the original URL
	 * @return the resolved URL or the original if the protocol is unknown to this
	 *         converter
	 * @exception IOException
	 *                if unable to resolve URL
	 * @throws IOException
	 *             if an error occurs during the resolution
	 */
	public URL resolve(final URL url) throws IOException;

}
