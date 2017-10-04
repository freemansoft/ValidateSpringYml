package org.springframework.beans.factory.config;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.yaml.snakeyaml.parser.ParserException;

import javax.imageio.IIOException;
import java.io.IOException;
import java.util.Map;
import java.util.Properties;

/**
 * Written by FreemanSoft.  See https://joe.blog.freemansoft.com for more information.
 * <p></p>
 * Unit test class that validates yml files.
 * <ul>
 *     <li>Copy thit class into your project</li>
 *     <li>Delete the tests you don't need like the one validating "duplicate-sections"</li>
 *	   <li>Run your unit tests</li>
 * </ul>
 */
public class ValidateSpringYml {

	public static final Logger log = LoggerFactory.getLogger(ValidateSpringYml.class);

	/**
	 * Utility method used by various tests.  We may test individual files so that we can find
	 * all broken yaml files in a single build.
	 * @param yamlResourceName the resource file name on the classpath.
	 * @throws IIOException hoepfully will never occur
	 * @throws ParserException when a file fails to validate.  Will be  last file error if multiple files have errors.
     */
	protected void validateYmlResources(String yamlResourceName)  {
		YamlProcessor validator= new YamlProcessor(){};
		// classpath* means find all on the classpath
		ResourcePatternResolver resourceResolver = new PathMatchingResourcePatternResolver();
		Resource[] foundResources = null;
		try {
			foundResources = resourceResolver.getResources("classpath*:" + yamlResourceName);
		} catch (IOException e){
			Assert.fail("cought IOException loading "+yamlResourceName);
		}
		Assert.assertTrue("Resource search failure",foundResources != null);
		Assert.assertTrue("Test invalid. No resources found",foundResources.length >0);
		log.info(String.format("ymlPattern={} resulted in numYmlFiles={}",yamlResourceName, foundResources.length));

		ParserException lastException = null;
		// now loop across the found resources loading each one
		for (Resource oneResource: foundResources ){
			validator.setResources(oneResource);
			// this will thorw an exception if the file fails to validate
			try {
				validator.process(new YamlProcessor.MatchCallback() {
					public void process(Properties properties, Map<String, Object> map) {
					}
				});
			} catch (ParserException e){
				lastException = e;
				// search the test log output flies if they aren't showing up in your console
				log.warn("Failed validating {}",oneResource.getFilename());
			}
		}
		if (lastException != null){
			throw lastException;
		}
	}

	/**
	 * Validate just the Application.yml
	 */
	@Test
	public void validateApplicationYml() {
		validateYmlResources("Application.yml");
	}

	/**
	 * Validate all yml files
	 */
	@Test
	public void validateAllYml() {
		validateYmlResources("*.yml");
	}

	/**
	 * Verify this fails when it should
	 */
	@Test(expected = ParserException.class)
	public void FindBadYml() {
		validateYmlResources("duplicate-sections.yml");
	}

}
