package net.technearts.remote.enforcer;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Map;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

/**
 * Verifica a existência de uma dependência remota.
 * 
 * @author psude
 *
 */
@Mojo(name = "check", defaultPhase = LifecyclePhase.VALIDATE)
public class CheckMojo extends AbstractMojo {

	@Parameter(property = "resources")
	private Map<String, URL> resources;
	@Parameter(property = "showOnlyInvalidPaths", defaultValue = "false")
	private boolean showOnlyInvalidPaths;

	/**
	 * Executa a verificação em todas as dependências listadas no pom.
	 */
	public void execute() throws MojoExecutionException {
		resources.entrySet().parallelStream().forEach((entry) -> {
			String dependency = entry.getKey();
			try {
				URI uri = entry.getValue().toURI();
				DependencyChecker dependencyChecker = CheckerBuilder.build(uri);
				if (dependencyChecker.test(uri)) {
					if (!showOnlyInvalidPaths) {
						getLog().info(dependency + " is OK!");
					}
				} else {
					getLog().warn(dependency + " is not OK");
				}
			} catch (URISyntaxException e) {
				getLog().error(dependency + " is not a valid URI");
			} catch (IllegalStateException e) {
				getLog().error(dependency + " could not be reached through this scheme");
			}
		});
	}
}
