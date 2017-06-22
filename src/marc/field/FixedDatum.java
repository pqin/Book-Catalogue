package marc.field;

public class FixedDatum implements Comparable<FixedDatum> {
	private int index, length;
	private String label, description;
	
	public FixedDatum(int index, int length, String label){
		this.index = index;
		this.length = length;
		this.label = label;
		this.description = null;
	}
	public FixedDatum(int index, int length, String label, String description){
		this.index = index;
		this.length = length;
		this.label = label;
		this.description = description;
	}
	
	public final int getIndex(){
		return index;
	}
	public final int getLength(){
		return length;
	}
	public final String getLabel(){
		return label;
	}
	public final String getDescription(){
		return description;
	}
	
	public final FixedDatum copy(){
		FixedDatum copy = new FixedDatum(this.index, this.length, this.label, this.label);
		return copy;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public final int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + index;
		result = prime * result + length;
		result = prime * result + ((label == null) ? 0 : label.hashCode());
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof FixedDatum)) {
			return false;
		}
		FixedDatum other = (FixedDatum) obj;
		if (index != other.index) {
			return false;
		}
		if (length != other.length) {
			return false;
		}
		if (label == null) {
			if (other.label != null) {
				return false;
			}
		} else if (!label.equals(other.label)) {
			return false;
		}
		if (description == null) {
			if (other.description != null) {
				return false;
			}
		} else if (!description.equals(other.description)) {
			return false;
		}
		return true;
	}

	@Override
	public int compareTo(FixedDatum other) {
		if (this == other) {
			return 0;
		}
		if (other == null) {
			throw new NullPointerException();
		}
		int result = this.index - other.index;
		if (result == 0){
			result = this.length - other.length;
		}
		if (result == 0){
			result = this.label.compareTo(other.label);
		}
		return result;
	}
}
