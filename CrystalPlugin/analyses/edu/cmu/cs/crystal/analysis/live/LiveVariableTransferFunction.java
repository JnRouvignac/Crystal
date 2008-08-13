/**
 * Copyright (c) 2006, 2007, 2008 Marwan Abi-Antoun, Jonathan Aldrich, Nels E. Beckman,
 * Kevin Bierhoff, David Dickey, Ciera Jaspan, Thomas LaToza, Gabriel Zenarosa, and others.
 *
 * This file is part of Crystal.
 *
 * Crystal is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Crystal is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Crystal.  If not, see <http://www.gnu.org/licenses/>.
 */
package edu.cmu.cs.crystal.analysis.live;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jdt.core.dom.MethodDeclaration;

import edu.cmu.cs.crystal.flow.AnalysisDirection;
import edu.cmu.cs.crystal.flow.Lattice;
import edu.cmu.cs.crystal.flow.TupleLatticeElement;
import edu.cmu.cs.crystal.tac.AbstractingTransferFunction;
import edu.cmu.cs.crystal.tac.ArrayInitInstruction;
import edu.cmu.cs.crystal.tac.BinaryOperation;
import edu.cmu.cs.crystal.tac.CastInstruction;
import edu.cmu.cs.crystal.tac.ConstructorCallInstruction;
import edu.cmu.cs.crystal.tac.CopyInstruction;
import edu.cmu.cs.crystal.tac.DotClassInstruction;
import edu.cmu.cs.crystal.tac.EnhancedForConditionInstruction;
import edu.cmu.cs.crystal.tac.InstanceofInstruction;
import edu.cmu.cs.crystal.tac.LoadArrayInstruction;
import edu.cmu.cs.crystal.tac.LoadFieldInstruction;
import edu.cmu.cs.crystal.tac.LoadLiteralInstruction;
import edu.cmu.cs.crystal.tac.MethodCallInstruction;
import edu.cmu.cs.crystal.tac.NewArrayInstruction;
import edu.cmu.cs.crystal.tac.NewObjectInstruction;
import edu.cmu.cs.crystal.tac.SourceVariableDeclaration;
import edu.cmu.cs.crystal.tac.SourceVariableRead;
import edu.cmu.cs.crystal.tac.StoreArrayInstruction;
import edu.cmu.cs.crystal.tac.StoreFieldInstruction;
import edu.cmu.cs.crystal.tac.TACInstruction;
import edu.cmu.cs.crystal.tac.UnaryOperation;
import edu.cmu.cs.crystal.tac.Variable;

/* A backwards analysis, all variables begin as dead until a read changes them to live. Writes set vars back to dead.
 * 
 * This is not implemented as an AbstractingTransferFunction because we need to handle some of them differently when they have
 * different operands, which they sometimes have.
 */
public class LiveVariableTransferFunction extends AbstractingTransferFunction<TupleLatticeElement<Variable, LiveVariableLE>>
{
	private static final Logger log = Logger.getLogger(LiveVariableTransferFunction.class.getName());
	
	public Lattice<TupleLatticeElement<Variable, LiveVariableLE>> getLattice(MethodDeclaration d) 
	{
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		
		
		TupleLatticeElement<Variable, LiveVariableLE> entry = new TupleLatticeElement<Variable, LiveVariableLE>(
				LiveVariableLE.DEAD, LiveVariableLE.DEAD);
		return new Lattice<TupleLatticeElement<Variable, LiveVariableLE>>(entry, entry.bottom());
	}

	public AnalysisDirection getAnalysisDirection() {
		return AnalysisDirection.BACKWARD_ANALYSIS;
	}

	
	public TupleLatticeElement<Variable, LiveVariableLE> transfer(ArrayInitInstruction instr, 
			TupleLatticeElement<Variable, LiveVariableLE> value) {
		log(instr, value);
		value.put(instr.getTarget(), LiveVariableLE.DEAD);
		for (Variable var : instr.getInitOperands())
			value.put(var, LiveVariableLE.LIVE);
		return value;
	}

	public TupleLatticeElement<Variable, LiveVariableLE> transfer(BinaryOperation binop,
			TupleLatticeElement<Variable, LiveVariableLE> value) 
	{
		log(binop, value);
		value.put(binop.getTarget(), LiveVariableLE.DEAD);
		value.put(binop.getOperand1(), LiveVariableLE.LIVE);
		value.put(binop.getOperand2(), LiveVariableLE.LIVE);		
		return value;	
	}

	public TupleLatticeElement<Variable, LiveVariableLE> transfer(CastInstruction instr,
			TupleLatticeElement<Variable, LiveVariableLE> value) 
	{
		log(instr, value);
		value.put(instr.getTarget(), LiveVariableLE.DEAD);
		value.put(instr.getOperand(), LiveVariableLE.LIVE);	
		return value;
	}

	public TupleLatticeElement<Variable, LiveVariableLE> transfer(DotClassInstruction instr,
			TupleLatticeElement<Variable, LiveVariableLE> value)
	{
		log(instr, value);
		value.put(instr.getTarget(), LiveVariableLE.DEAD);
		return value;
	}

	public TupleLatticeElement<Variable, LiveVariableLE> transfer(ConstructorCallInstruction instr,
			TupleLatticeElement<Variable, LiveVariableLE> value) 
	{
		log(instr, value);
		for (Variable var : (List<Variable>) instr.getArgOperands())
			value.put(var, LiveVariableLE.LIVE);
		return value;
	}

	public TupleLatticeElement<Variable, LiveVariableLE> transfer(CopyInstruction instr,
			TupleLatticeElement<Variable, LiveVariableLE> value) 
	{
		log(instr, value);
		value.put(instr.getTarget(), LiveVariableLE.DEAD);
		value.put(instr.getOperand(), LiveVariableLE.LIVE);	
		return value;
	}

	public TupleLatticeElement<Variable, LiveVariableLE> transfer(EnhancedForConditionInstruction instr,
			TupleLatticeElement<Variable, LiveVariableLE> value) 
	{
		log(instr, value);
		value.put(instr.getIteratedOperand(), LiveVariableLE.LIVE);	
		return value;
	}

	public TupleLatticeElement<Variable, LiveVariableLE> transfer(InstanceofInstruction instr,
			TupleLatticeElement<Variable, LiveVariableLE> value) 
	{
		log(instr, value);
		value.put(instr.getTarget(), LiveVariableLE.DEAD);
		value.put(instr.getOperand(), LiveVariableLE.LIVE);			
		return value;
	}

	public TupleLatticeElement<Variable, LiveVariableLE> transfer(LoadLiteralInstruction instr,
			TupleLatticeElement<Variable, LiveVariableLE> value) 
	{
		log(instr, value);
		value.put(instr.getTarget(), LiveVariableLE.DEAD);
		return value;
	}

	public TupleLatticeElement<Variable, LiveVariableLE> transfer(LoadArrayInstruction instr,
			TupleLatticeElement<Variable, LiveVariableLE> value) 
	{
		log(instr, value);
		value.put(instr.getTarget(), LiveVariableLE.DEAD);
		value.put(instr.getSourceArray(), LiveVariableLE.LIVE);
		value.put(instr.getArrayIndex(), LiveVariableLE.LIVE);		
		return value;		
	}

	public TupleLatticeElement<Variable, LiveVariableLE> transfer(LoadFieldInstruction instr,
			TupleLatticeElement<Variable, LiveVariableLE> value) 
	{
		log(instr, value);
		value.put(instr.getTarget(), LiveVariableLE.DEAD);
		
		// Static field accesses do not have source objects
		if (!instr.isStaticFieldAccess())
			value.put(instr.getSourceObject(), LiveVariableLE.LIVE);	
		return value;	
	}

	public TupleLatticeElement<Variable, LiveVariableLE> transfer(MethodCallInstruction instr,
			TupleLatticeElement<Variable, LiveVariableLE> value) 
	{
		log(instr, value);
		value.put(instr.getTarget(), LiveVariableLE.DEAD);
		value.put(instr.getReceiverOperand(), LiveVariableLE.LIVE);
		for (Variable var : (List<Variable>) instr.getArgOperands())
			value.put(var, LiveVariableLE.LIVE);	
		return value;
	}

	public TupleLatticeElement<Variable, LiveVariableLE> transfer(NewArrayInstruction instr,
			TupleLatticeElement<Variable, LiveVariableLE> value) 
	{
		log(instr, value);
		value.put(instr.getTarget(), LiveVariableLE.DEAD);
		for (Variable var : instr.getDimensionOperands())
			value.put(var, LiveVariableLE.LIVE);	
		return value;
	}

	public TupleLatticeElement<Variable, LiveVariableLE> transfer(NewObjectInstruction instr,
			TupleLatticeElement<Variable, LiveVariableLE> value) 
	{
		log(instr, value);
		value.put(instr.getTarget(), LiveVariableLE.DEAD);
		Variable operand = instr.getOuterObjectSpecifierOperand();
		if (operand != null)
			value.put(operand, LiveVariableLE.LIVE);
		
		for (Variable var : (List<Variable>) instr.getArgOperands())
			value.put(var, LiveVariableLE.LIVE);
		return value;
	}

	public TupleLatticeElement<Variable, LiveVariableLE> transfer(StoreArrayInstruction instr,
			TupleLatticeElement<Variable, LiveVariableLE> value) 
	{
		log(instr, value);
		// Since there potentially exist other values in the array that are still live, we do not change
		// the state of the array to dead even though we write to it.
		value.put(instr.getArrayIndex(), LiveVariableLE.LIVE);
		value.put(instr.getSourceOperand(), LiveVariableLE.LIVE);
		return value;
	}

	public TupleLatticeElement<Variable, LiveVariableLE> transfer(StoreFieldInstruction instr,
			TupleLatticeElement<Variable, LiveVariableLE> value) 
	{
		log(instr, value);
		// Since there potentially exist other fields in the object that are still live, we do not change
		// the state of the object to dead even though we write to it.
		value.put(instr.getSourceOperand(), LiveVariableLE.LIVE);
		return value;
	}

	public TupleLatticeElement<Variable, LiveVariableLE> transfer(SourceVariableDeclaration instr,
			TupleLatticeElement<Variable, LiveVariableLE> value) 
	{
		log(instr, value);
		value.put(instr.getDeclaredVariable(), LiveVariableLE.DEAD);
		return value;
	}

	public TupleLatticeElement<Variable, LiveVariableLE> transfer(SourceVariableRead instr,
			TupleLatticeElement<Variable, LiveVariableLE> value) 
	{
		log(instr, value);
		value.put(instr.getVariable(), LiveVariableLE.LIVE);
		return value;
	}

	public TupleLatticeElement<Variable, LiveVariableLE> transfer(UnaryOperation unop,
			TupleLatticeElement<Variable, LiveVariableLE> value) 
	{
		log(unop, value);
		value.put(unop.getTarget(), LiveVariableLE.DEAD);
		value.put(unop.getOperand(), LiveVariableLE.LIVE);			
		return value;
	}
	
	private void log(TACInstruction instr, TupleLatticeElement<Variable, LiveVariableLE> value)
	{
		if (log.isLoggable(Level.FINER))
		{				
			// Compute the first line of the source text corresponding to the instruction's AST node
			String sourceText = instr.getNode().toString();
			int endOfLine = sourceText.indexOf('\n');
			String firstLine;
			// If there is only 1 line, endOfLine will be -1			
			if (endOfLine > 0)
				firstLine = sourceText.substring(0, endOfLine);
			else
				firstLine = sourceText;
			
			System.out.println(instr + " : " + firstLine );
			LiveVariableAnalysis.Instance.printLattice(value);
		}
	}
}
