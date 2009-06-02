/**
 * The interfaces for the three-address code (TAC) instructions. If you are writing a flow analysis, you may find
 * TAC easier to use that the Eclipse AST. TAC has no sub-expressions and uses temporary variables to break up
 * complex expressions in the AST into simpler expressions.
 * 
 * To create a flow analysis using TAC, implement either ITACTransferFunction or ITACBranchSensitiveTransferFunction.
 */
package edu.cmu.cs.crystal.tac;