package net.technearts.remote.enforcer;

import java.net.URI;

public class CheckerBuilder {

	public static DependencyChecker build(URI uri) {
		switch (uri.getScheme()) {
		case "http":
		case "https":
			return new WebChecker();
		default:
			throw new IllegalArgumentException();
		}
	}
}
