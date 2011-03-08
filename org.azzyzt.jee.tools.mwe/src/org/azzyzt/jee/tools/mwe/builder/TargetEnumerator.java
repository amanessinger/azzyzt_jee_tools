package org.azzyzt.jee.tools.mwe.builder;

import java.util.List;
import java.util.Set;

public interface TargetEnumerator {

	public abstract List<String> getFullyQualifiedTargetNames();

	public abstract Set<String> getTargetPackageNames();

}