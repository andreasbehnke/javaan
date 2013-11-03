package org.javaan.model;

public abstract class NamedObjectBase implements NamedObject {

	private final String name;
	
	protected NamedObjectBase(String name) {
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		return name;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		NamedObjectBase other = (NamedObjectBase) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	@Override
	public int compareTo(NamedObject o) {
		// null check
		if (this == o) {
			return 0;
		}
		if (o == null) {
			return -1;
		}
		String otherName = o.getName();
		if (otherName == null && name == null) {
			return 0;
		}
		if (otherName != null && name == null) {
			return 1;
		}
		if (otherName == null && name != null) {
			return -1;
		}
		return name.compareTo(otherName);
	}

}
