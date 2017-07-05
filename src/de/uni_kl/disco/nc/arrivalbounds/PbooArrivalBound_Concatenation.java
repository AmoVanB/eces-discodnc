/*
 * This file is part of the Disco Deterministic Network Calculator v2.4.0 "Chimera"
 *
 * Copyright (C) 2013 - 2017 Steffen Bondorf
 * Copyright (C) 2017 The DiscoDNC contributors
 *
 * Distributed Computer Systems (DISCO) Lab
 * University of Kaiserslautern, Germany
 *
 * http://disco.cs.uni-kl.de/index.php/projects/disco-dnc
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

package de.uni_kl.disco.nc.arrivalbounds;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import de.uni_kl.disco.curves.ArrivalCurve;
import de.uni_kl.disco.curves.CurvePwAffineFactory;
import de.uni_kl.disco.curves.CurvePwAffineUtils;
import de.uni_kl.disco.curves.ServiceCurve;
import de.uni_kl.disco.minplus.Convolution;
import de.uni_kl.disco.misc.SetUtils;
import de.uni_kl.disco.nc.AnalysisConfig;
import de.uni_kl.disco.nc.ArrivalBound;
import de.uni_kl.disco.nc.analyses.TotalFlowAnalysis;
import de.uni_kl.disco.nc.operations.LeftOverService;
import de.uni_kl.disco.nc.operations.OutputBound;
import de.uni_kl.disco.network.*;
import de.uni_kl.disco.numbers.Num;
import de.uni_kl.disco.numbers.NumFactory;

/**
 * @author Steffen Bondorf
 */
public class PbooArrivalBound_Concatenation extends ArrivalBound {

    @SuppressWarnings("unused")
    private PbooArrivalBound_Concatenation() {
    }

    public PbooArrivalBound_Concatenation(Network network, AnalysisConfig configuration) {
        this.network = network;
        this.configuration = configuration;
    }

    public Set<ArrivalCurve> computeArrivalBound(Link link, Flow flow_of_interest) throws Exception {
        return computeArrivalBound(link, network.getFlows(link), flow_of_interest);
    }

    public Set<ArrivalCurve> computeArrivalBound(Link link, Set<Flow> f_xfcaller, Flow flow_of_interest) throws Exception {
        Set<ArrivalCurve> alphas_xfcaller = new HashSet<ArrivalCurve>(Collections.singleton(CurvePwAffineFactory.createZeroArrivals()));
        if (f_xfcaller == null || f_xfcaller.isEmpty()) {
            return alphas_xfcaller;
        }

        // Get the servers on common sub-path of f_xfcaller flows crossing link
        // loi == location of interference
        Server loi = link.getDest();
        Set<Flow> f_loi = network.getFlows(loi);
        Set<Flow> f_xfcaller_loi = SetUtils.getIntersection(f_loi, f_xfcaller);
        f_xfcaller_loi.remove(flow_of_interest);
        if (f_xfcaller_loi.size() == 0) {
            return alphas_xfcaller;
        }

        // The shortcut found in PmooArrivalBound for the a common_subpath of length 1 will not be implemented here.
        // There's not a big potential to increase performance as the PBOO arrival bound implicitly handles this situation by only iterating over one server in the for loop.
        Server common_subpath_src = network.findSplittingServer(loi, f_xfcaller_loi);
        Server common_subpath_dest = link.getSource();
        Flow f_representative = f_xfcaller_loi.iterator().next();
        Path common_subpath = f_representative.getSubPath(common_subpath_src, common_subpath_dest);

        // Calculate the left-over service curves on this sub-path by convolution of the individual left over service curves
        Set<ServiceCurve> betas_lo_subpath = new HashSet<ServiceCurve>();
        Set<ServiceCurve> betas_lo_s;
        Link link_from_prev_s;
        Path foi_path = flow_of_interest.getPath();
        for (Server server : common_subpath.getServers()) {
            try {
                link_from_prev_s = network.findLink(foi_path.getPrecedingServer(server), server);
            } catch (Exception e) {            // Reached the path's first server
                link_from_prev_s = null;    // reset to null
            }

            Set<Flow> f_xxfcaller_server = network.getFlows(server);
            f_xxfcaller_server.removeAll(f_xfcaller);
            f_xxfcaller_server.remove(flow_of_interest);

            Set<Flow> f_xxfcaller_server_path = SetUtils.getIntersection(f_xxfcaller_server, network.getFlows(link_from_prev_s));

            // Convert f_xfoi_server to f_xfoi_server_offpath
            f_xxfcaller_server.removeAll(f_xxfcaller_server_path);

            // If we are off the path of interest, flow_of_interest is Flow.NULL_FLOW already.
            Set<ArrivalCurve> alpha_xxfcaller_path = ArrivalBound.computeArrivalBounds(network, configuration, server, f_xxfcaller_server_path, flow_of_interest);
            Set<ArrivalCurve> alpha_xxfcaller_offpath = ArrivalBound.computeArrivalBounds(network, configuration, server, f_xxfcaller_server, Flow.NULL_FLOW);

            Set<ArrivalCurve> alphas_xxfcaller_s = new HashSet<ArrivalCurve>();
            for (ArrivalCurve arrival_curve_path : alpha_xxfcaller_path) {
                for (ArrivalCurve arrival_curve_offpath : alpha_xxfcaller_offpath) {
                    alphas_xxfcaller_s.add(CurvePwAffineUtils.add(arrival_curve_path, arrival_curve_offpath));
                }
            }

            // Calculate the left-over service curve for this single server
            betas_lo_s = LeftOverService.compute(configuration, server, alphas_xxfcaller_s);

            // Check if there's any service left on this path. If not, the set only contains a null-service curve.
            if (betas_lo_s.size() == 1 && betas_lo_s.iterator().next().equals(CurvePwAffineFactory.createZeroService())) {
                System.out.println("No service left over during PBOO arrival bounding!");
                alphas_xfcaller.clear();
                alphas_xfcaller.add((ArrivalCurve) CurvePwAffineFactory.createZeroDelayInfiniteBurst());
                return alphas_xfcaller;
            }

            // Combine into the sub-path's left-over service curve
            betas_lo_subpath = Convolution.convolve_SCs_SCs(betas_lo_subpath, betas_lo_s, configuration.tbrlConvolution());
        }

        // Next we need to know the arrival bound of f_xfcaller at the server 'common_subpath_src', i.e., at the above sub-path's source
        // in order to deconvolve it with beta_lo_s to get the arrival bound of the sub-path
        // Note that flows f_xfcaller that originate in 'common_subpath_src' are covered by this call of computeArrivalBound
        Set<ArrivalCurve> alpha_xfcaller_src = super.computeArrivalBounds(common_subpath_src, f_xfcaller, flow_of_interest);
        alphas_xfcaller = OutputBound.compute(configuration, alpha_xfcaller_src, common_subpath, betas_lo_subpath);

        if (configuration.abConsiderTFANodeBacklog()) {
            Server last_hop_xtx = link.getSource();
            TotalFlowAnalysis tfa = new TotalFlowAnalysis(network, configuration);
            tfa.deriveBoundsAtServer(last_hop_xtx);

            Set<Num> tfa_backlog_bounds = tfa.getServerBacklogBoundMap().get(last_hop_xtx);
            Num tfa_backlog_bound_min = NumFactory.getPositiveInfinity();

            for (Num tfa_backlog_bound : tfa_backlog_bounds) {
                if (tfa_backlog_bound.leq(tfa_backlog_bound_min)) {
                    tfa_backlog_bound_min = tfa_backlog_bound;
                }
            }

            // Reduce the burst
            for (ArrivalCurve alpha_xfcaller : alphas_xfcaller) {
                if (alpha_xfcaller.getBurst().gt(tfa_backlog_bound_min)) {
                    alpha_xfcaller.getSegment(1).setY(tfa_backlog_bound_min); // if the burst is >0 then there are at least two segments and the second holds the burst as its y-axis value
                }
            }
        }

        return alphas_xfcaller;
    }
}