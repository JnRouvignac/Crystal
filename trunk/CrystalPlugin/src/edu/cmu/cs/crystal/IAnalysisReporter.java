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
package edu.cmu.cs.crystal;

import java.io.PrintWriter;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.dom.ASTNode;

/**
 * An interface that analyses use for reporting problems.
 * 
 * @author Nels E. Beckman
 */
public interface IAnalysisReporter {
	/**	
	 * Indicate that the running of this analysis on this compilation unit
	 * has created a problem.
	 * 
	 * @param problemDescription A textual description of the problem.
	 * @param node The AST node where the problem was encountered.
	 * @param analysisName The name of the Crystal analysis that is reporting the problem.
	 */
	public void reportUserProblem(String problemDescription, ASTNode node, String analysisName);
		
	public PrintWriter debugOut();
	
	public PrintWriter userOut();

	/**
	 * For the given compilation unit, clear its markers from the screen,
	 * if necessary. Implementers are free to implement this method as
	 * necessary, including by doing nothing at all.
	 */
	public void clearMarkersForCompUnit(ICompilationUnit compUnit);
}