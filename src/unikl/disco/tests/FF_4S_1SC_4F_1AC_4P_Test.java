/*
 * This file is part of the Disco Deterministic Network Calculator v2.3.3 "Centaur".
 *
 * Copyright (C) 2013 - 2017 Steffen Bondorf
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

package unikl.disco.tests;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import unikl.disco.nc.AnalysisResults;
import unikl.disco.nc.AnalysisConfig.ArrivalBoundMethod;
import unikl.disco.nc.AnalysisConfig.Multiplexing;
import unikl.disco.nc.Analysis.Analyses;
import unikl.disco.nc.analyses.PmooAnalysis;
import unikl.disco.nc.analyses.SeparateFlowAnalysis;
import unikl.disco.nc.analyses.TotalFlowAnalysis;
import unikl.disco.network.Flow;
import unikl.disco.network.Network;
import unikl.disco.numbers.NumFactory;

@RunWith(value = Parameterized.class)
/**
 * 
 * @author Steffen Bondorf
 *
 */
public class FF_4S_1SC_4F_1AC_4P_Test extends FunctionalTests {
	private static FF_4S_1SC_4F_1AC_4P_Network test_network;
	private static Network network;
	private static Flow f0, f1, f2, f3;

	protected static FunctionalTestResults expected_results = new FunctionalTestResults();
	protected static FunctionalTestResults expected_results_PMOOAB = new FunctionalTestResults();
	
	public FF_4S_1SC_4F_1AC_4P_Test( FunctionalTestConfig test_config ) {
		super( test_config );
	}
	
	@BeforeClass
	public static void createNetwork() {
		test_network = new FF_4S_1SC_4F_1AC_4P_Network();
		f0 = test_network.f0;
		f1 = test_network.f1;
		f2 = test_network.f2;
		f3 = test_network.f3;

		network = test_network.getNetwork();
		
		initializeBounds();
	}

	private static void initializeBounds() {
		expected_results.clear();
		
		// TFA
		expected_results.setBounds( Analyses.TFA, Multiplexing.FIFO, f0, new AnalysisResults( NumFactory.create( 3735, 32 ), NumFactory.create( 975 ), null ) );
		expected_results.setBounds( Analyses.TFA, Multiplexing.FIFO, f1, new AnalysisResults( NumFactory.create( 77.5 ), NumFactory.create( 975 ), null ) );
		expected_results.setBounds( Analyses.TFA, Multiplexing.FIFO, f2, new AnalysisResults( NumFactory.create( 1875, 32 ), NumFactory.create( 3975, 8 ), null ) );
		expected_results.setBounds( Analyses.TFA, Multiplexing.FIFO, f3, new AnalysisResults( NumFactory.create( 845, 8 ), NumFactory.create( 975 ), null ) );
		expected_results.setBounds( Analyses.TFA, Multiplexing.ARBITRARY, f0, new AnalysisResults( NumFactory.create( 1370, 3 ), NumFactory.create( 1400 ), null ) );
		expected_results.setBounds( Analyses.TFA, Multiplexing.ARBITRARY, f1, new AnalysisResults( NumFactory.create( 395 ), NumFactory.create( 1400 ), null ) );
		expected_results.setBounds( Analyses.TFA, Multiplexing.ARBITRARY, f2, new AnalysisResults( NumFactory.create( 1105, 6 ), NumFactory.create( 2075, 3 ), null ) );
		expected_results.setBounds( Analyses.TFA, Multiplexing.ARBITRARY, f3, new AnalysisResults( NumFactory.create( 462.5 ), NumFactory.create( 1400 ), null ) );
		
		// SFA
		expected_results.setBounds( Analyses.SFA, Multiplexing.FIFO, f0, new AnalysisResults( NumFactory.create( 1525, 16 ), NumFactory.create( 7825, 16 ), null ) );
		expected_results.setBounds( Analyses.SFA, Multiplexing.FIFO, f1, new AnalysisResults( NumFactory.create( 575, 8 ), NumFactory.create( 2975, 8 ), null ) );
		expected_results.setBounds( Analyses.SFA, Multiplexing.FIFO, f2, new AnalysisResults( NumFactory.create( 1695, 32 ), NumFactory.create( 8875, 32 ), null ) );
		expected_results.setBounds( Analyses.SFA, Multiplexing.FIFO, f3, new AnalysisResults( NumFactory.create( 1405, 16 ), NumFactory.create( 7225, 16 ), null ) );
		expected_results.setBounds( Analyses.SFA, Multiplexing.ARBITRARY, f0, new AnalysisResults( NumFactory.create( 580, 3 ), NumFactory.create( 5875, 6 ), null ) );
		expected_results.setBounds( Analyses.SFA, Multiplexing.ARBITRARY, f1, new AnalysisResults( NumFactory.create( 345, 2 ), NumFactory.create( 875 ), null ) );
		expected_results.setBounds( Analyses.SFA, Multiplexing.ARBITRARY, f2, new AnalysisResults( NumFactory.create( 1625, 18 ), NumFactory.create( 4175, 9 ), null ) );
		expected_results.setBounds( Analyses.SFA, Multiplexing.ARBITRARY, f3, new AnalysisResults( NumFactory.create( 560, 3 ), NumFactory.create( 5675, 6 ), null ) );
		
		// PMOO
		expected_results.setBounds( Analyses.PMOO, Multiplexing.ARBITRARY, f0, new AnalysisResults( NumFactory.create( 650, 3 ), NumFactory.create( 6575, 6 ), null ) );
		expected_results.setBounds( Analyses.PMOO, Multiplexing.ARBITRARY, f1, new AnalysisResults( NumFactory.create( 345, 2 ), NumFactory.create( 875 ), null ) );
		expected_results.setBounds( Analyses.PMOO, Multiplexing.ARBITRARY, f2, new AnalysisResults( NumFactory.create( 305, 3 ), NumFactory.create( 3125, 6 ), null ) );
		expected_results.setBounds( Analyses.PMOO, Multiplexing.ARBITRARY, f3, new AnalysisResults( NumFactory.create( 1145, 6 ), NumFactory.create( 2900, 3 ), null ) );
		

		// PMOO Arrival Bounding yields worse cross-traffic arrivals!
		expected_results_PMOOAB.clear();

		// TFA
		expected_results_PMOOAB.setBounds( Analyses.TFA, Multiplexing.ARBITRARY, f0, new AnalysisResults( NumFactory.create( 2765, 6 ), NumFactory.create( 8525, 6 ), null ) );
		expected_results_PMOOAB.setBounds( Analyses.TFA, Multiplexing.ARBITRARY, f1, new AnalysisResults( NumFactory.create( 2395, 6 ), NumFactory.create( 8525, 6 ), null ) );
		expected_results_PMOOAB.setBounds( Analyses.TFA, Multiplexing.ARBITRARY, f3, new AnalysisResults( NumFactory.create( 1400, 3 ), NumFactory.create( 8525, 6 ), null ) );

		// SFA
		expected_results_PMOOAB.setBounds( Analyses.SFA, Multiplexing.ARBITRARY, f0, new AnalysisResults( NumFactory.create( 2345, 12 ), NumFactory.create( 11875, 12 ), null ) );
		expected_results_PMOOAB.setBounds( Analyses.SFA, Multiplexing.ARBITRARY, f1, new AnalysisResults( NumFactory.create( 2095, 12 ), NumFactory.create( 10625, 12 ), null ) );

		// PMOO
		expected_results_PMOOAB.setBounds( Analyses.PMOO, Multiplexing.ARBITRARY, f0, new AnalysisResults( NumFactory.create( 2095, 12 ), NumFactory.create( 10625, 12 ), null ) );
		expected_results_PMOOAB.setBounds( Analyses.PMOO, Multiplexing.ARBITRARY, f1, new AnalysisResults( NumFactory.create( 875, 4 ), NumFactory.create( 4425, 4 ), null ) );
	}
	
	@Before
    public void reinitNetwork() {
		if( !super.reinitilize_numbers ){
			return;
		}
		
		test_network.reinitializeCurves();
		initializeBounds();
	}
	
//--------------------Flow 0--------------------
	@Test
	public void f0_tfa() {
		setMux( network.getServers() );
		
		if( test_config.arrivalBoundMethods().size() == 1
				&& test_config.arrivalBoundMethods().contains( ArrivalBoundMethod.PMOO ) ) {
			super.runTFAtest( new TotalFlowAnalysis( network, test_config ), f0, expected_results_PMOOAB );
		} else {
			super.runTFAtest( new TotalFlowAnalysis( network, test_config ), f0, expected_results );
		}
	}
	
	@Test
	public void f0_sfa() {
		setMux( network.getServers() );
		
		if( test_config.arrivalBoundMethods().size() == 1
				&& test_config.arrivalBoundMethods().contains( ArrivalBoundMethod.PMOO ) ) {
			super.runSFAtest( new SeparateFlowAnalysis( network, test_config ), f0, expected_results_PMOOAB );
		} else {
			super.runSFAtest( new SeparateFlowAnalysis( network, test_config ), f0, expected_results );
		}
	}
	
	@Test
	public void f0_pmoo_arbMux() {
		setArbitraryMux( network.getServers() );
		
		if( test_config.arrivalBoundMethods().size() == 1
				&& test_config.arrivalBoundMethods().contains( ArrivalBoundMethod.PMOO ) ) {
			super.runPMOOtest( new PmooAnalysis( network, test_config ), f0, expected_results_PMOOAB );
		} else {
			super.runPMOOtest( new PmooAnalysis( network, test_config ), f0, expected_results );
		}
	}

	@Test
	public void f0_sinktree_arbMux() {
		if( test_config.fullConsoleOutput() ) {
			System.out.println( "Analysis:\t\tTree Backlog Bound Analysis" );
			System.out.println( "Multiplexing:\t\tArbitrary" );
	
			System.out.println( "Flow of interest:\t" + f0.toString() );
			System.out.println();
			
			System.out.println( "--- Results: ---" );
			System.out.println( "Tree Backlog Bound calculation not applicable." );
		}
	}
	
//--------------------Flow 1--------------------
	@Test
	public void f1_tfa() {
		setMux( network.getServers() );
		
		if( test_config.arrivalBoundMethods().size() == 1
				&& test_config.arrivalBoundMethods().contains( ArrivalBoundMethod.PMOO ) ) {
			super.runTFAtest( new TotalFlowAnalysis( network, test_config ), f1, expected_results_PMOOAB );
		} else {
			super.runTFAtest( new TotalFlowAnalysis( network, test_config ), f1, expected_results );
		}
	}
	
	@Test
	public void f1_sfa() {
		setMux( network.getServers() );
		
		if( test_config.arrivalBoundMethods().size() == 1
				&& test_config.arrivalBoundMethods().contains( ArrivalBoundMethod.PMOO ) ) {
			super.runSFAtest( new SeparateFlowAnalysis( network, test_config ), f1, expected_results_PMOOAB );
		} else {
			super.runSFAtest( new SeparateFlowAnalysis( network, test_config ), f1, expected_results );
		}
	}
	
	@Test
	public void f1_pmoo_arbMux() {
		setArbitraryMux( network.getServers() );
		
		if( test_config.arrivalBoundMethods().size() == 1
				&& test_config.arrivalBoundMethods().contains( ArrivalBoundMethod.PMOO ) ) {
			super.runPMOOtest( new PmooAnalysis( network, test_config ), f1, expected_results_PMOOAB );
		} else {
			super.runPMOOtest( new PmooAnalysis( network, test_config ), f1, expected_results );
		}
	}

	
	@Test
	public void f1_sinktree_arbMux() {
		if( test_config.fullConsoleOutput() ) {
			System.out.println( "Analysis:\t\tTree Backlog Bound Analysis" );
			System.out.println( "Multiplexing:\t\tArbitrary" );
	
			System.out.println( "Flow of interest:\t" + f1.toString() );
			System.out.println();
			
			System.out.println( "--- Results: ---" );
			System.out.println( "Tree Backlog Bound calculation not applicable." );
		}
	}

//--------------------Flow 2--------------------
	@Test
	public void f2_tfa() {
		setMux( network.getServers() );
		super.runTFAtest( new TotalFlowAnalysis( network, test_config ), f2, expected_results );
	}
	
	@Test
	public void f2_sfa() {
		setMux( network.getServers() );
		super.runSFAtest( new SeparateFlowAnalysis( network, test_config ), f2, expected_results );
	}
	
	@Test
	public void f2_pmoo_arbMux() {
		setArbitraryMux( network.getServers() );
		super.runPMOOtest( new PmooAnalysis( network, test_config ), f2, expected_results );
	}
	
	@Test
	public void f2_sinktree_arbMux() {
		if( test_config.fullConsoleOutput() ) {
			System.out.println( "Analysis:\t\tTree Backlog Bound Analysis" );
			System.out.println( "Multiplexing:\t\tArbitrary" );
	
			System.out.println( "Flow of interest:\t" + f2.toString() );
			System.out.println();
			
			System.out.println( "--- Results: ---" );
			System.out.println( "Tree Backlog Bound calculation not applicable." );
		}
	}

//--------------------Flow 3--------------------
	@Test
	public void f3_tfa() {
		setMux( network.getServers() );
		
		if( test_config.arrivalBoundMethods().size() == 1
				&& test_config.arrivalBoundMethods().contains( ArrivalBoundMethod.PMOO ) ) {
			super.runTFAtest( new TotalFlowAnalysis( network, test_config ), f3, expected_results_PMOOAB );
		} else {
			super.runTFAtest( new TotalFlowAnalysis( network, test_config ), f3, expected_results );
		}
	}
	
	@Test
	public void f3_sfa() {
		setMux( network.getServers() );
		super.runSFAtest( new SeparateFlowAnalysis( network, test_config ), f3, expected_results );
	}
	
	@Test
	public void f3_pmoo_arbMux() {
		setArbitraryMux( network.getServers() );
		super.runPMOOtest( new PmooAnalysis( network, test_config ), f3, expected_results );
	}
	
	@Test
	public void f3_sinktree_arbMux() {
		if( test_config.fullConsoleOutput() ) {
			System.out.println( "Analysis:\t\tTree Backlog Bound Analysis" );
			System.out.println( "Multiplexing:\t\tArbitrary" );
	
			System.out.println( "Flow of interest:\t" + f3.toString() );
			System.out.println();
			
			System.out.println( "--- Results: ---" );
			System.out.println( "Tree Backlog Bound calculation not applicable." );
		}
	}
}