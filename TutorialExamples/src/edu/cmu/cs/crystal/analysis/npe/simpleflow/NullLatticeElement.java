package edu.cmu.cs.crystal.analysis.npe.simpleflow;


/**
 * There are only 4 possibilities for nullness:
 * Null: if the value is definitely null.
 * Not_null: if the value is definitely not null.
 * Maybe_null: if the value could be null.
 * Bottom: if the concept of nullness does not apply.
 * @author ciera
 *
 */
public enum NullLatticeElement {
	BOTTOM, NULL, NOT_NULL, MAYBE_NULL;

	public NullLatticeElement copy() {
		return this;
	}
}
