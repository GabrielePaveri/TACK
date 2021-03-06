package formulae.mitli;

import java.util.HashSet;
import java.util.Set;

import com.google.common.base.Preconditions;

import formulae.UnaryFormula;
import formulae.mitli.TemporizedFormula;


public abstract class MITLIPast extends MITLIFormula implements TemporizedFormula, UnaryFormula<MITLIFormula> {

	private MITLIFormula subformula;
	private final int hash;

	public MITLIPast(MITLIFormula subformula) {
		Preconditions.checkNotNull(subformula, "The subformula cannot be null");
		this.subformula = subformula;
		this.hash=this.computeHash();
	}

	/**
	 * {@inheritDoc}
	 * 
	 */
	@Override
	public MITLIFormula getChild() {
		return this.subformula;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Set<MITLIFormula> getChildren() {
		Set<MITLIFormula> formulae=new HashSet<>();
		formulae.add(subformula);
		return formulae;
	}
	
	private int computeHash() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((subformula == null) ? 0 : subformula.hashCode());
		return result;
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return this.hash;
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MITLIPast other = (MITLIPast) obj;
		if (subformula == null) {
			if (other.subformula != null)
				return false;
		} else if (!subformula.equals(other.subformula))
			return false;
		return true;
	}
}
