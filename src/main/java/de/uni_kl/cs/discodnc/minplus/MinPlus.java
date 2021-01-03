/*
 * This file is part of the Disco Deterministic Network Calculator.
 *
 * Copyright (C) 2017+ The DiscoDNC contributors
 *
 * disco | Distributed Computer Systems Lab
 * University of Kaiserslautern, Germany
 *
 * http://discodnc.cs.uni-kl.de
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
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 */

package de.uni_kl.cs.discodnc.minplus;

// Due to name collisions, these classes are not imported,
// they are referenced by their fully qualified names.
//import ch.ethz.rtc.kernel.Curve;
//import Curve;


import de.uni_kl.cs.discodnc.curves.ArrivalCurve;
import de.uni_kl.cs.discodnc.curves.Curve;
import de.uni_kl.cs.discodnc.curves.CurvePwAffine;
import de.uni_kl.cs.discodnc.curves.MaxServiceCurve;
import de.uni_kl.cs.discodnc.curves.ServiceCurve;
import de.uni_kl.cs.discodnc.minplus.dnc.Convolution_DNC;
import de.uni_kl.cs.discodnc.minplus.dnc.Deconvolution_DNC;
import de.uni_kl.cs.discodnc.nc.CalculatorConfig;
import de.uni_kl.cs.discodnc.nc.CalculatorConfig.CurveImpl;
import de.uni_kl.cs.discodnc.nc.CalculatorConfig.OperationImpl;

import java.util.HashSet;
import java.util.Set;

public abstract class MinPlus {
	// --------------------------------------------------------------------------------------------------------------
	// Min-Plus-Operation Dispatching
	// --------------------------------------------------------------------------------------------------------------

	// ------------------------------------------------------------
	// Convolution
	// ------------------------------------------------------------

	// Service Curves
	public static ServiceCurve convolve(ServiceCurve service_curve_1, ServiceCurve service_curve_2) throws Exception {
		return convolve(service_curve_1, service_curve_2, false);
	}

	public static ServiceCurve convolve(ServiceCurve service_curve_1, ServiceCurve service_curve_2,
			boolean tb_rl_optimized) throws Exception {
		if (CalculatorConfig.getInstance().getOperationImpl().equals(OperationImpl.DNC) // DNC operations work with
				// DNC and MPA_RTC curves
				|| CalculatorConfig.getInstance().getCurveImpl().equals(CurveImpl.DNC)) { // NATIVE operation on
			// DNC curves
			return Convolution_DNC.convolve(service_curve_1, service_curve_2, tb_rl_optimized);

		} else { // Must be CurveClass.MPA_RTC + OpertionClass.NATIVE
			throw new RuntimeException("We disable the usage of the RTC Toolbox!");
		}
	}

	// Java won't let us call this method "convolve" because it does not care about
	// the Sets' types; tells that there's already another method taking the same
	// arguments.
	public static Set<ServiceCurve> convolve_SCs_SCs(Set<ServiceCurve> service_curves_1,
			Set<ServiceCurve> service_curves_2) throws Exception {
		return convolve_SCs_SCs(service_curves_1, service_curves_2, false);
	}

	public static Set<ServiceCurve> convolve_SCs_SCs(Set<ServiceCurve> service_curves_1,
			Set<ServiceCurve> service_curves_2, boolean tb_rl_optimized) throws Exception {

		if (service_curves_1.isEmpty()) {
			return service_curves_2;
		}
		if (service_curves_2.isEmpty()) {
			return service_curves_1;
		}

		if (CalculatorConfig.getInstance().getOperationImpl().equals(OperationImpl.DNC) // DNC operations work with
				// DNC and MPA_RTC curves
				|| CalculatorConfig.getInstance().getCurveImpl().equals(CurveImpl.DNC)) { // NATIVE operation on
			// DNC curves
			return Convolution_DNC.convolve_SCs_SCs(service_curves_1, service_curves_2, tb_rl_optimized);

		} else { // Must be CurveClass.MPA_RTC + OpertionClass.NATIVE
			throw new RuntimeException("We disable the usage of the RTC Toolbox!");
		}
	}

	// Arrival Curves
	public static ArrivalCurve convolve(ArrivalCurve arrival_curve_1, ArrivalCurve arrival_curve_2) throws Exception {
		// DNC operations work with DNC and MPA_RTC curves
		if (CalculatorConfig.getInstance().getOperationImpl().equals(OperationImpl.DNC)
				// NATIVE operation on DNC curves
				|| CalculatorConfig.getInstance().getCurveImpl().equals(CurveImpl.DNC)) {
			return Convolution_DNC.convolve(arrival_curve_1, arrival_curve_2);

		} else { // Must be CurveClass.MPA_RTC + OpertionClass.NATIVE
			throw new RuntimeException("We disable the usage of the RTC Toolbox!");
		}
	}

	public static ArrivalCurve convolve(Set<ArrivalCurve> arrival_curves) throws Exception {
		// DNC operations work with DNC and MPA_RTC curves
		if (CalculatorConfig.getInstance().getOperationImpl().equals(OperationImpl.DNC)
				// NATIVE operation on DNC curves
				|| CalculatorConfig.getInstance().getCurveImpl().equals(CurveImpl.DNC)) {
			return Convolution_DNC.convolve(arrival_curves);
		} else { // Must be CurveClass.MPA_RTC + OpertionClass.NATIVE
			throw new RuntimeException("We disable the usage of the RTC Toolbox!");
		}
	}

	// Maximum Service Curves
	public static MaxServiceCurve convolve(MaxServiceCurve max_service_curve_1, MaxServiceCurve max_service_curve_2)
			throws Exception {
		// DNC operations work with DNC and MPA_RTC curves
		if (CalculatorConfig.getInstance().getOperationImpl().equals(OperationImpl.DNC)
				// NATIVE operation on DNC curves
				|| CalculatorConfig.getInstance().getCurveImpl().equals(CurveImpl.DNC)) {
			return Convolution_DNC.convolve(max_service_curve_1, max_service_curve_2);
		} else { // Must be CurveClass.MPA_RTC + OpertionClass.NATIVE
			throw new RuntimeException("We disable the usage of the RTC Toolbox!");
		}
	}

	// Arrival Curves and Max Service Curves
	public static Set<CurvePwAffine> convolve_ACs_MSC(Set<ArrivalCurve> arrival_curves,
			MaxServiceCurve maximum_service_curve) throws Exception {
		// DNC operations work with DNC and MPA_RTC curves
		if (CalculatorConfig.getInstance().getOperationImpl().equals(OperationImpl.DNC)
				// NATIVE operation on DNC curves
				|| CalculatorConfig.getInstance().getCurveImpl().equals(CurveImpl.DNC)) {
			return Convolution_DNC.convolve_ACs_MSC(arrival_curves, maximum_service_curve);
		} else { // Must be CurveClass.MPA_RTC + OpertionClass.NATIVE
			throw new RuntimeException("We disable the usage of the RTC Toolbox!");
		}
	}

	public static Set<ArrivalCurve> convolve_ACs_EGamma(Set<ArrivalCurve> arrival_curves,
			MaxServiceCurve extra_gamma_curve) throws Exception {
		// DNC operations work with DNC and MPA_RTC curves
		if (CalculatorConfig.getInstance().getOperationImpl().equals(OperationImpl.DNC)
				// NATIVE operation on DNC curves
				|| CalculatorConfig.getInstance().getCurveImpl().equals(CurveImpl.DNC)) {
			return Convolution_DNC.convolve_ACs_EGamma(arrival_curves, extra_gamma_curve);
		} else { // Must be CurveClass.MPA_RTC + OpertionClass.NATIVE
			throw new RuntimeException("We disable the usage of the RTC Toolbox!");
		}
	}

	// ------------------------------------------------------------
	// Deconvolution
	// ------------------------------------------------------------
	public static Set<ArrivalCurve> deconvolve(Set<ArrivalCurve> arrival_curves, ServiceCurve service_curve)
			throws Exception {
		return deconvolve(arrival_curves, service_curve, false);
	}

	public static Set<ArrivalCurve> deconvolve(Set<ArrivalCurve> arrival_curves, ServiceCurve service_curve,
			boolean tb_rl_optimized) throws Exception {
		// DNC operations work with DNC and MPA_RTC curves
		if (CalculatorConfig.getInstance().getOperationImpl().equals(OperationImpl.DNC)
				// NATIVE operation on DNC curves
				|| CalculatorConfig.getInstance().getCurveImpl().equals(CurveImpl.DNC)) {
			return Deconvolution_DNC.deconvolve(arrival_curves, service_curve, tb_rl_optimized);
		} else { // Must be CurveClass.MPA_RTC + OpertionClass.NATIVE
			throw new RuntimeException("We disable the usage of the RTC Toolbox!");
		}
	}

	public static Set<ArrivalCurve> deconvolve(Set<ArrivalCurve> arrival_curves, Set<ServiceCurve> service_curves)
			throws Exception {
		return deconvolve(arrival_curves, service_curves, false);
	}

	public static Set<ArrivalCurve> deconvolve(Set<ArrivalCurve> arrival_curves, Set<ServiceCurve> service_curves,
			boolean tb_rl_optimized) throws Exception {
		// DNC operations work with DNC and MPA_RTC curves
		if (CalculatorConfig.getInstance().getOperationImpl().equals(OperationImpl.DNC)
				// NATIVE operation on DNC curves
				|| CalculatorConfig.getInstance().getCurveImpl().equals(CurveImpl.DNC)) {
			return Deconvolution_DNC.deconvolve(arrival_curves, service_curves, tb_rl_optimized);
		} else { // Must be CurveClass.MPA_RTC + OpertionClass.NATIVE
			throw new RuntimeException("We disable the usage of the RTC Toolbox!");
		}
	}

	public static ArrivalCurve deconvolve(ArrivalCurve arrival_curve, ServiceCurve service_curve) throws Exception {
		return deconvolve(arrival_curve, service_curve, false);
	}

	public static ArrivalCurve deconvolve(ArrivalCurve arrival_curve, ServiceCurve service_curve,
			boolean tb_rl_optimized) throws Exception {
		// DNC operations work with DNC and MPA_RTC curves
		if (CalculatorConfig.getInstance().getOperationImpl().equals(OperationImpl.DNC)
				// NATIVE operation on DNC curves
				|| CalculatorConfig.getInstance().getCurveImpl().equals(CurveImpl.DNC)) {
			return Deconvolution_DNC.deconvolve(arrival_curve, service_curve);
		} else { // Must be CurveClass.MPA_RTC + OpertionClass.NATIVE
			throw new RuntimeException("We disable the usage of the RTC Toolbox!");
		}
	}

	public static Set<ArrivalCurve> deconvolve_almostConcCs_SCs(Set<CurvePwAffine> curves,
			Set<ServiceCurve> service_curves) throws Exception {
		// DNC operations work with DNC and MPA_RTC curves
		if (CalculatorConfig.getInstance().getOperationImpl().equals(OperationImpl.DNC)
				// NATIVE operation on DNC curves
				|| CalculatorConfig.getInstance().getCurveImpl().equals(CurveImpl.DNC)) {
			return Deconvolution_DNC.deconvolve_almostConcCs_SCs(curves, service_curves);
		} else { // Must be CurveClass.MPA_RTC + OpertionClass.NATIVE
			throw new RuntimeException("We disable the usage of the RTC Toolbox!");
		}
	}

	// --------------------------------------------------------------------------------------------------------------
	// Min-Plus-Operation Input Checks
	// --------------------------------------------------------------------------------------------------------------

	/**
	 * @param obj1
	 * @param obj2
	 * @return 0 == none of the objects is null, <br/>
	 *         1 == the first object is null, <br/>
	 *         2 == the second object is null, <br/>
	 *         3 == both objects are null.
	 */
	public static int inputNullCheck(Object obj1, Object obj2) {
		// Usually neither is null so this initial check promises best overall
		// performance.
		if (obj1 != null && obj2 != null) {
			return 0;
		}

		int return_value = 0;

		if (obj1 == null) {
			return_value += 1;
		}
		if (obj2 == null) {
			return_value += 2;
		}

		return return_value;
	}

	/**
	 * @param curve_1
	 * @param curve_2
	 * @return 0 == none of the objects is a delayed infinite burst, <br/>
	 *         1 == the first object is a delayed infinite burst, <br/>
	 *         2 == the second object is a delayed infinite burst, <br/>
	 *         3 == both objects are a delayed infinite burst.
	 */
	public static int inputDelayedInfiniteBurstCheck(Curve curve_1, Curve curve_2) {
		int return_value = 0;

		if (curve_1.isDelayedInfiniteBurst()) {
			return_value += 1;
		}

		if (curve_2.isDelayedInfiniteBurst()) {
			return_value += 2;
		}

		return return_value;
	}

	/**
	 * @param set1
	 * @param set2
	 * @return 0 == none of the sets is empty, <br/>
	 *         1 == the first sets is empty, <br/>
	 *         2 == the second sets is empty, <br/>
	 *         3 == both sets are empty.
	 */
	@SuppressWarnings("rawtypes")
	public static int inputEmptySetCheck(Set set1, Set set2) {
		// Usually neither is empty so this initial check promises best overall
		// performance.
		if (!set1.isEmpty() && !set2.isEmpty()) {
			return 0;
		}

		int return_value = 0;

		if (set1.isEmpty()) {
			return_value += 1;
		}
		if (set2.isEmpty()) {
			return_value += 2;
		}

		return return_value;
	}
}
