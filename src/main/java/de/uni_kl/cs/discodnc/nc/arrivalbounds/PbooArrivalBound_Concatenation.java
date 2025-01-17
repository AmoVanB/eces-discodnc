/*
 * This file is part of the Disco Deterministic Network Calculator.
 *
 * Copyright (C) 2013 - 2018 Steffen Bondorf
 * Copyright (C) 2017+ The DiscoDNC contributors
 *
 * Distributed Computer Systems (DISCO) Lab
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

package de.uni_kl.cs.discodnc.nc.arrivalbounds;

import de.uni_kl.cs.discodnc.curves.ArrivalCurve;
import de.uni_kl.cs.discodnc.curves.CurvePwAffine;
import de.uni_kl.cs.discodnc.curves.ServiceCurve;
import de.uni_kl.cs.discodnc.misc.SetUtils;
import de.uni_kl.cs.discodnc.nc.AbstractArrivalBound;
import de.uni_kl.cs.discodnc.nc.AnalysisConfig;
import de.uni_kl.cs.discodnc.nc.ArrivalBound;
import de.uni_kl.cs.discodnc.nc.ArrivalBoundDispatch;
import de.uni_kl.cs.discodnc.nc.analyses.SeparateFlowAnalysis;
import de.uni_kl.cs.discodnc.nc.analyses.SeparateFlowResults;
import de.uni_kl.cs.discodnc.nc.analyses.TotalFlowAnalysis;
import de.uni_kl.cs.discodnc.nc.bounds.Bound;
import de.uni_kl.cs.discodnc.network.Flow;
import de.uni_kl.cs.discodnc.network.Link;
import de.uni_kl.cs.discodnc.network.Network;
import de.uni_kl.cs.discodnc.network.Path;
import de.uni_kl.cs.discodnc.network.Server;
import de.uni_kl.cs.discodnc.numbers.Num;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class PbooArrivalBound_Concatenation extends AbstractArrivalBound implements ArrivalBound {
	private static PbooArrivalBound_Concatenation instance = new PbooArrivalBound_Concatenation();

	private PbooArrivalBound_Concatenation() {
	}

	public PbooArrivalBound_Concatenation(Network network, AnalysisConfig configuration) {
		this.network = network;
		this.configuration = configuration;
	}

	public static PbooArrivalBound_Concatenation getInstance() {
		return instance;
	}

	public Set<ArrivalCurve> computeArrivalBound(Link link, Flow flow_of_interest) throws Exception {
		return computeArrivalBound(link, network.getFlows(link), flow_of_interest);
	}

	public Set<ArrivalCurve> computeArrivalBound(Link link, Set<Flow> f_xfcaller, Flow flow_of_interest)
			throws Exception {
		if (f_xfcaller == null || f_xfcaller.isEmpty()) {
			return new HashSet<ArrivalCurve>(Collections.singleton(CurvePwAffine.getFactory().createZeroArrivals()));
		}

		// Get the servers crossed by all flows in f_xfcaller
		// that will eventually also cross the given link.
		Set<Flow> f_xfcaller_link = SetUtils.getIntersection(network.getFlows(link), f_xfcaller);
		f_xfcaller_link.remove(flow_of_interest);
		if (f_xfcaller_link.size() == 0) {
			// The flows to bound given in f_xfcaller do not cross the given link.
			return new HashSet<ArrivalCurve>(Collections.singleton(CurvePwAffine.getFactory().createZeroArrivals()));
		}

		// The shortcut found in PmooArrivalBound for the a common_subpath of length 1
		// will not be implemented here.
		// There's not a big potential to increase performance as the PBOO arrival bound
		// implicitly handles this situation by only iterating over one server in the
		// for loop.
		Server common_subpath_src = network.findSplittingServer(link.getDest(), f_xfcaller_link);
		Flow f_representative = f_xfcaller_link.iterator().next();
		Path common_subpath = f_representative.getSubPath(common_subpath_src, link.getSource());
		
		SeparateFlowResults tandem_results = SeparateFlowAnalysis.tandemAnalysis(network, flow_of_interest, common_subpath, f_xfcaller, configuration);
		
		Set<ServiceCurve> betas_lo_subpath = tandem_results.getBetasE2E();
		if(betas_lo_subpath.size() == 1
				&& betas_lo_subpath.contains(CurvePwAffine.getFactory().createZeroService())) {
			System.out.println("No service left over during PBOO arrival bounding!");
			return new HashSet<ArrivalCurve>(Collections.singleton(CurvePwAffine.getFactory().createUnboundedArrivals()));
		}

		// Next we need to know the arrival bound of f_xfcaller at the server
		// 'common_subpath_src', i.e., at the above sub-path's source in order to
		// deconvolve it with beta_lo_s to get the arrival bound of the sub-path.
		// Note that flows f_xfcaller that originate in 'common_subpath_src' are covered
		// by this call of computeArrivalBound.
		Set<ArrivalCurve> alpha_xfcaller_src = ArrivalBoundDispatch.computeArrivalBounds(network, configuration,
				common_subpath_src, f_xfcaller, flow_of_interest);
		Set<ArrivalCurve> alphas_xfcaller = Bound.output(configuration, alpha_xfcaller_src, common_subpath, betas_lo_subpath);

		if (configuration.abConsiderTFANodeBacklog()) {
			Server last_hop_xtx = link.getSource();
			TotalFlowAnalysis tfa = new TotalFlowAnalysis(network, configuration);
			tfa.deriveBoundsAtServer(last_hop_xtx);

			Set<Num> tfa_backlog_bounds = tfa.getServerBacklogBoundMap().get(last_hop_xtx);
			Num tfa_backlog_bound_min = Num.getFactory().getPositiveInfinity();

			for (Num tfa_backlog_bound : tfa_backlog_bounds) {
				if (tfa_backlog_bound.leq(tfa_backlog_bound_min)) {
					tfa_backlog_bound_min = tfa_backlog_bound;
				}
			}

			// Reduce the burst
			
			// TODO This implementation only works for token-bucket arrivals. 
			// It disregards the potential shift in inflection points not present in this burst cap variant.
			for (ArrivalCurve alpha_xfcaller : alphas_xfcaller) {
				if(alpha_xfcaller.getSegmentCount() > 2 ) {
					// >2 segments -> >=2 inflection points -> burst reduction not applicable! 
					continue;
				}
				if (alpha_xfcaller.getBurst().gt(tfa_backlog_bound_min)) {
					// if the burst is >0 then there are at least two segments 
					// and the second one holds the burst as its y-axis value.
					alpha_xfcaller.getSegment(1).setY(tfa_backlog_bound_min);
				}
			}
		}

		return alphas_xfcaller;
	}
}
