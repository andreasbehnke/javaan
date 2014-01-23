package org.javaan.print;

import org.javaan.model.Package;

public class PackageFormatter implements ObjectFormatter<Package> {

	@Override
	public String format(Package object) {
		return "[P]" + object.getName();
	}

}