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
package edu.cmu.cs.crystal.simple;

import edu.cmu.cs.crystal.flow.AnalysisDirection;
import edu.cmu.cs.crystal.flow.ITACTransferFunction;
import edu.cmu.cs.crystal.tac.ArrayInitInstruction;
import edu.cmu.cs.crystal.tac.AssignmentInstruction;
import edu.cmu.cs.crystal.tac.BinaryOperation;
import edu.cmu.cs.crystal.tac.CastInstruction;
import edu.cmu.cs.crystal.tac.ConstructorCallInstruction;
import edu.cmu.cs.crystal.tac.CopyInstruction;
import edu.cmu.cs.crystal.tac.DotClassInstruction;
import edu.cmu.cs.crystal.tac.EnhancedForConditionInstruction;
import edu.cmu.cs.crystal.tac.ITACAnalysisContext;
import edu.cmu.cs.crystal.tac.InstanceofInstruction;
import edu.cmu.cs.crystal.tac.InvocationInstruction;
import edu.cmu.cs.crystal.tac.LoadArrayInstruction;
import edu.cmu.cs.crystal.tac.LoadFieldInstruction;
import edu.cmu.cs.crystal.tac.LoadLiteralInstruction;
import edu.cmu.cs.crystal.tac.MethodCallInstruction;
import edu.cmu.cs.crystal.tac.NewArrayInstruction;
import edu.cmu.cs.crystal.tac.NewObjectInstruction;
import edu.cmu.cs.crystal.tac.OneOperandInstruction;
import edu.cmu.cs.crystal.tac.ReturnInstruction;
import edu.cmu.cs.crystal.tac.SourceVariableDeclaration;
import edu.cmu.cs.crystal.tac.SourceVariableRead;
import edu.cmu.cs.crystal.tac.StoreArrayInstruction;
import edu.cmu.cs.crystal.tac.StoreFieldInstruction;
import edu.cmu.cs.crystal.tac.StoreInstruction;
import edu.cmu.cs.crystal.tac.TACInstruction;
import edu.cmu.cs.crystal.tac.UnaryOperation;

/**
 * This class implements additional transfer functions that abstract or
 * group other transfer functions according to the instruction hierarchy.
 * This is convenient for analyses that want to treat many different
 * but related instructions in the same way.
 * 
 * For example, if you want to have the same transfer function for
 * MethodCallInstruction, ConstructorCallInstruction, and NewObjectInstruction,
 * you can override TACInvocation.
 * 
 * @author Jonathan Aldrich
 *
 * @param <LE> the lattice being used in this analysis.
 */
public abstract class AbstractingTransferFunction<LE> 
		implements ITACTransferFunction<LE> {

	private ITACAnalysisContext analysisContext;

	public AnalysisDirection getAnalysisDirection() {
		return AnalysisDirection.FORWARD_ANALYSIS;
	}

	public ITACAnalysisContext getAnalysisContext() {
		return analysisContext;
	}

	public void setAnalysisContext(ITACAnalysisContext analysisContext) {
		this.analysisContext = analysisContext;
	}
	
	public LE transfer(TACInstruction instr, LE value) {
		return value;
	}

	public LE transfer(AssignmentInstruction instr, LE value) {
		return transfer((TACInstruction) instr, value);
	}

	public LE transfer(InvocationInstruction instr, LE value) {
		return transfer((AssignmentInstruction) instr, value);
	}

	public LE transfer(OneOperandInstruction instr, LE value) {
		return transfer((AssignmentInstruction) instr, value);
	}

	public LE transfer(StoreInstruction instr, LE value) {
		return transfer((TACInstruction) instr, value);
	}


	public LE transfer(ArrayInitInstruction instr, LE value) {
		return transfer((AssignmentInstruction)instr, value);
	}

	public LE transfer(BinaryOperation binop, LE value) {
		return transfer((AssignmentInstruction) binop, value);
	}

	public LE transfer(CastInstruction instr, LE value) {
		return transfer((OneOperandInstruction) instr, value);
	}

	public LE transfer(DotClassInstruction instr, LE value) {
		return transfer((AssignmentInstruction)instr, value);
	}

	public LE transfer(ConstructorCallInstruction instr, LE value) {
		return transfer((TACInstruction) instr, value);
	}

	public LE transfer(CopyInstruction instr, LE value) {
		return transfer((OneOperandInstruction) instr, value);
	}

	public LE transfer(EnhancedForConditionInstruction instr, LE value) {
		return transfer((TACInstruction) instr, value);
	}

	public LE transfer(InstanceofInstruction instr, LE value) {
		return transfer((OneOperandInstruction) instr, value);
	}

	public LE transfer(LoadLiteralInstruction instr, LE value) {
		return transfer((AssignmentInstruction)instr, value);
	}

	public LE transfer(LoadArrayInstruction instr, LE value) {
		return transfer((AssignmentInstruction)instr, value);
	}

	public LE transfer(LoadFieldInstruction instr, LE value) {
		return transfer((AssignmentInstruction)instr, value);
	}

	public LE transfer(MethodCallInstruction instr, LE value) {
		return transfer((InvocationInstruction) instr, value);
	}

	public LE transfer(NewArrayInstruction instr, LE value) {
		return transfer((AssignmentInstruction)instr, value);
	}

	public LE transfer(NewObjectInstruction instr, LE value) {
		return transfer((InvocationInstruction) instr, value);
	}

	public LE transfer(ReturnInstruction instr, LE value) {
		return transfer((TACInstruction) instr, value);
	}

	public LE transfer(StoreArrayInstruction instr, LE value) {
		return transfer((StoreInstruction) instr, value);
	}

	public LE transfer(StoreFieldInstruction instr, LE value) {
		return transfer((StoreInstruction) instr, value);
	}

	public LE transfer(SourceVariableDeclaration instr, LE value) {
		return transfer((TACInstruction) instr, value);
	}

	public LE transfer(SourceVariableRead instr, LE value) {
		return transfer((TACInstruction) instr, value);
	}

	public LE transfer(UnaryOperation unop, LE value) {
		return transfer((OneOperandInstruction) unop, value);
	}

}
