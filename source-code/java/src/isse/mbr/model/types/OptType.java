package isse.mbr.model.types;

import isse.mbr.model.parsetree.AbstractPVSInstance;

/**
 * Represents an optional in MiniZinc
 *
 * @author Elias Keis
 */
public class OptType implements PrimitiveType {
	private final PrimitiveType decoratedType;

	public OptType(PrimitiveType decoratedType) {
		this.decoratedType = decoratedType;
	}

	@Override
	public String toString() {
		return "opt(" + decoratedType.toString() + ")";
	}

	@Override
	public String toMzn(AbstractPVSInstance instance) {
		return "opt " + decoratedType.toMzn(instance);
	}

	@Override
	public boolean isFloat() {
		return decoratedType.isFloat();
	}
}
