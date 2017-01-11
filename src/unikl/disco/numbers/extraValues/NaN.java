/*
 * This file is part of the Disco Deterministic Network Calculator v2.3.0 "Centaur".
 *
 * Copyright (C) 2014 - 2017 Steffen Bondorf
 *
 * Distributed Computer Systems (DISCO) Lab
 * University of Kaiserslautern, Germany
 *
 * http://disco.cs.uni-kl.de
 *
 *
 * The Disco Deterministic Network Calculator (DiscoDNC) is free software;
 * you can redistribute it and/or modify it under the terms of the 
 * GNU Lesser General Public License as published by the Free Software Foundation; 
 * either version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA
 *
 */
 
package unikl.disco.numbers.extraValues;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import unikl.disco.numbers.Num;

/**
 * Wrapper class around double;
 *
 * @author Steffen Bondorf
 *
 */
public final class NaN implements Num {
	public NaN() {}
	
	public boolean eqZero() {
		return false;
	}

	public boolean gt( Num num ) {
		return false;
	}

	public boolean gtZero() {
		return false;
	}

	public boolean geq( Num num ) {
		return false;
	}

	public boolean geqZero() {
		return false;
	}

	public boolean lt( Num num ) {
		return false;
	}

	public boolean ltZero() {
		return false;
	}

	public boolean leq( Num num ) {
		return false;
	}

	public boolean leqZero() {
		return false;
	}
	
	@Override
	public double doubleValue() {
	    return Double.NaN;
	}

	@Override
	public Num copy() {
		return new NaN();
	}
	
	@Override
	public boolean eq( double num ) {
		return false;
	}

	@SuppressFBWarnings(value="EQ_ALWAYS_FALSE",justification ="Comparison to NaN should always return false")
	@Override
	public boolean equals( Object obj ) {
		return false;
	}
	
	@Override
	public int hashCode() {
		return Double.hashCode( Double.NaN );
	}
	
	@Override
	public String toString(){
		return Double.toString( Double.NaN );
	}
}