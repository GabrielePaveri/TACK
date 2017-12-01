package formulae.cltloc.converters;

import java.util.Set;
import java.util.function.Function;

import org.apache.commons.lang3.StringUtils;

import com.google.common.base.Preconditions;

import formulae.cltloc.CLTLocFormula;
import formulae.cltloc.atoms.BoundedVariable;
import formulae.cltloc.atoms.CLTLocClock;
import formulae.cltloc.atoms.Signal;
import formulae.cltloc.atoms.Variable;
import formulae.cltloc.visitor.CLTLoc2ZotVisitor;
import formulae.cltloc.visitor.GetBoundedVariablesVisitor;
import formulae.cltloc.visitor.GetClocksVisitor;
import formulae.cltloc.visitor.GetSignalVisitor;
import formulae.cltloc.visitor.GetVariablesVisitor;
import formulae.cltloc.visitor.ZotPlugin;

public class CLTLoc2zot implements Function<CLTLocFormula, String> {

	private final int bound;
	private final int maxConstant;

	private final ZotPlugin pluginName;
	
	public CLTLoc2zot(int bound,ZotPlugin pluginName) {
		Preconditions.checkArgument(bound > 0, "The bound must be grather than zero");
		this.bound = bound;
		this.maxConstant=bound;
		this.pluginName=pluginName;
	}
	
	public CLTLoc2zot(int bound, int maxConstant,ZotPlugin pluginName) {
		Preconditions.checkArgument(bound > 0, "The bound must be grather than zero");
		this.bound =Math.max(10, bound);
		this.maxConstant=maxConstant;
		this.pluginName=pluginName;
	}

	public String apply(CLTLocFormula formula) {
		final StringBuilder builder = new StringBuilder();
		builder.append("(asdf:operate 'asdf:load-op '"+pluginName+")\n");
		
		builder.append("(use-package :trio-utils)\n");

		Set<CLTLocClock> clocks = formula.accept(new GetClocksVisitor());
		clocks.forEach(clock -> builder.append("(define-tvar " + clock.toString() + " *real*)\n"));

		Set<Signal> signals = formula.accept(new GetSignalVisitor());
		signals.forEach(signal -> builder.append("(define-tvar " + signal.toString() + " *real*)\n"));

		Set<Variable> variables = formula.accept(new GetVariablesVisitor());
		variables.forEach(variable -> builder.append("(define-tvar " + variable.toString() + " *int*)\n"));

		Set<BoundedVariable> boundedvariables = formula.accept(new GetBoundedVariablesVisitor());
		boundedvariables.forEach(variable ->{ 
			builder.append("(define-item " + variable.toString() + " '("+StringUtils.join(variable.getValues(), ' ')+"))\n");});

		
		final StringBuilder footerBuilder = new StringBuilder();
		
		footerBuilder.append(":discrete-counters '(" + StringUtils.join(variables, ' ') + ")");

// 	ae2sbvzot
//		ae2zot
		builder.append("("+pluginName+":zot " + bound + " (&&" + formula.accept(new CLTLoc2ZotVisitor()) + ")\n\n"
				+ ":smt-lib :smt2 \n" 
				+ ":logic :QF_UFRDL \n" 
				+ ":over-clocks "+ maxConstant +"\n"
				+ ":gen-symbolic-val nil\n"
				+":parametric-regions t\n"
				+ footerBuilder.toString()
				 + ")\n");

		builder.append("\n");
		
		

		return builder.toString();
	}
}