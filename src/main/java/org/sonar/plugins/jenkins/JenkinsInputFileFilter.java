package org.sonar.plugins.jenkins;

import java.io.File;

import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.fs.InputFileFilter;

public class JenkinsInputFileFilter implements InputFileFilter {

	/**
	 * accept all config-files, which have job-folder as parent
	 */
	@Override
	public boolean accept(InputFile file) {
		File parent = file.file().getParentFile();
		if (parent != null) {
			File grandParent = parent.getParentFile();
			if (grandParent != null) {
				if (file.file().getName().equals("config.xml") && file.file().getParentFile().getParentFile().getName().equals("jobs")) {
					return true;
				}				
			}
		}
		return false;
	}
}
