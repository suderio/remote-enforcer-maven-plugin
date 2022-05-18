package net.technearts.remote_dependency;

import static org.junit.Assert.assertTrue;

import net.technearts.remote.enforcer.CheckerBuilder;
import net.technearts.remote.enforcer.DependencyChecker;

import java.net.URI;
import java.net.URISyntaxException;

import org.junit.Test;

public class WebCheckerTest {
	@Test
	public void checkedTest() throws URISyntaxException {
		URI uri = new URI("https://www.google.com");
		DependencyChecker dependencyChecker = CheckerBuilder.build(uri);
		assertTrue(dependencyChecker.test(uri));
	}
}
