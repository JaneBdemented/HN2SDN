* Copyright (C) 2014 SDN Hub

 Licensed under the GNU GENERAL PUBLIC LICENSE, Version 3.
 You may not use this file except in compliance with this License.
 You may obtain a copy of the License at

    http://www.gnu.org/licenses/gpl-3.0.txt

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 implied.

 *
 */

package org.opendaylight.tutorial.tutorial_L2_forwarding.internal;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List; 
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.lang.String;
import java.lang.reflect.Array;
import java.util.Map;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
import java.time;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleException;
import org.osgi.framework.FrameworkUtil;
import org.opendaylight.controller.sal.core.ConstructionException;
import org.opendaylight.controller.sal.core.Node;
import org.opendaylight.controller.sal.core.NodeConnector;
import org.opendaylight.controller.sal.flowprogrammer.IFlowProgrammerService;
import org.opendaylight.controller.sal.flowprogrammer.Flow;
import org.opendaylight.controller.sal.packet.ARP;
import org.opendaylight.controller.sal.packet.BitBufferHelper;
import org.opendaylight.controller.sal.packet.Ethernet;
import org.opendaylight.controller.sal.packet.ICMP;
import org.opendaylight.controller.sal.packet.IDataPacketService;
import org.opendaylight.controller.sal.packet.IListenDataPacket;
import org.opendaylight.controller.sal.packet.Packet;
import org.opendaylight.controller.sal.packet.PacketResult;
import org.opendaylight.controller.sal.packet.RawPacket;
import org.opendaylight.controller.sal.action.Action;
import org.opendaylight.controller.sal.action.Output;
import org.opendaylight.controller.sal.action.Flood;
import org.opendaylight.controller.sal.match.Match;
import org.opendaylight.controller.sal.match.MatchType;
import org.opendaylight.controller.sal.match.MatchField;
import org.opendaylight.controller.sal.utils.EtherTypes;
import org.opendaylight.controller.sal.utils.Status;
import org.opendaylight.controller.sal.utils.NetUtils;
import org.opendaylight.controller.switchmanager.ISwitchManager;
import org.opendaylight.controller.switchmanager.Subnet;
//our decision maker
import org.opendaylight.controller.tutorial_L2_forwarding.RuleChecker;

public class TutorialL2Forwarding implements IListenDataPacket {
    private static final Logger logger = LoggerFactory
            .getLogger(TutorialL2Forwarding.class);
    private ISwitchManager switchManager = null;
    private IFlowProgrammerService programmer = null;
    private IDataPacketService dataPacketService = null;
    private Map<Long, NodeConnector> mac_to_port = new HashMap<Long, NodeConnector>();
    private String function = "switch";
    //known hash for rules_4_MAC 
   
    private static LinkList<String> RulesList = userApp.getRules();
   // private LocalTime now = new LocalTime;
    		
    void setDataPacketService(IDataPacketService s) {
        this.dataPacketService = s;
    }

    void unsetDataPacketService(IDataPacketService s) {
        if (this.dataPacketService == s) {
            this.dataPacketService = null;
        }
    }

    public void setFlowProgrammerService(IFlowProgrammerService s)
    {
        this.programmer = s;
    }

    public void unsetFlowProgrammerService(IFlowProgrammerService s) {
        if (this.programmer == s) {
            this.programmer = null;
        }
    }

    void setSwitchManager(ISwitchManager s) {
        logger.debug("SwitchManager set");
        this.switchManager = s;
    }

    void unsetSwitchManager(ISwitchManager s) {
        if (this.switchManager == s) {
            logger.debug("SwitchManager removed!");
            this.switchManager = null;
        }
    }
    void getMacHash(){
    	return(mac_to_port);
    }
    /**
     * Function called by the dependency manager when all the required
     * dependencies are satisfied
     *
     */
    void init() {
        logger.info("Initialized");
        // Disabling the SimpleForwarding and ARPHandler bundle to not conflict with this one
        BundleContext bundleContext = FrameworkUtil.getBundle(this.getClass()).getBundleContext();
        for(Bundle bundle : bundleContext.getBundles()) {
            if (bundle.getSymbolicName().contains("simpleforwarding")) {
                try {
                    bundle.uninstall();
                } catch (BundleException e) {
                    logger.error("Exception in Bundle uninstall "+bundle.getSymbolicName(), e);
                }
            }
        }
    }

    /**
     * Function called by the dependency manager when at least one
     * dependency become unsatisfied or when the component is shutting
     * down because for example bundle is being stopped.
     *
     */
    void destroy() {
    }

    /**
     * Function called by dependency manager after "init ()" is called
     * and after the services provided by the class are registered in
     * the service registry
     *
     */
    void start() {
        logger.info("Started");
    }

    /**
     * Function called by the dependency manager before the services
     * exported by the component are unregistered, this will be
     * followed by a "destroy ()" calls
     *
     */
    void stop() {
        logger.info("Stopped");
    }

    private void floodPacket(RawPacket inPkt) {
        NodeConnector incoming_connector = inPkt.getIncomingNodeConnector();
        Node incoming_node = incoming_connector.getNode();

        Set<NodeConnector> nodeConnectors =
                this.switchManager.getUpNodeConnectors(incoming_node);

        for (NodeConnector p : nodeConnectors) {
            if (!p.equals(incoming_connector)) {
                try {
                    RawPacket destPkt = new RawPacket(inPkt);
                    destPkt.setOutgoingNodeConnector(p);
                    this.dataPacketService.transmitDataPacket(destPkt);
                } catch (ConstructionException e2) {
                    continue;
                }
            }
        }
    }



    @Override
    public PacketResult receiveDataPacket(RawPacket inPkt) {
        if (inPkt == null) {
            return PacketResult.IGNORED;
        }

        NodeConnector incoming_connector = inPkt.getIncomingNodeConnector();
        Node incoming_node = incoming_connector.getNode();

        // Hub implementation
        if (function.equals("hub")) {
            floodPacket(inPkt);
        } else {
            NodeConnector dst_connector = null;

            // Extract packet from inPkt
             Packet formattedPak = this.dataPacketService.decodeDataPacket(inPkt);

            if (!(formattedPak instanceof Ethernet)) {
                return PacketResult.IGNORED;
            }
            // Extract switch and port information (incoming_connector)
            incoming_connector = inPkt.getIncomingNodeConnector();
            // Extract packet srcMAC and dstMAC using getSourceMACAddress() and
            // getDestinationMACAddress() of Ethernet class, and
            // convert to hex long we can use through BitBufferHelper
            byte[] srcMAC = ((Ethernet)formattedPak).getSourceMACAddress();
            byte[] dstMAC = ((Ethernet)formattedPak).getDestinationMACAddress();
            long dstMAC_val = BitBufferHelper.toNumber(dstMAC);
            long srcMAC_val = BitBufferHelper.toNumber(srcMAC);
            // Store srcMAC and incoming_connector in the mapping dictionar
            
            //is the MAC known?
            check = null;
            check = mac_to_port.get(srcMAC_val);
            if (check != null){
            	RuleChecker ruling = new RulChecker();
            	int	nextStep = ruling.Check_MAC_Rule(srcMAC_val, RulesList);
            	if(nextStep == 0){
                       List<Action> actions = new ArrayList<Action>();
                       actions.add(new Drop(inPkt));
                       Match match = new Match();
                       match.setField( new MatchField(MatchType.IN_PORT, incoming_connector) );
                       //match.setField( new MatchField(MatchType.DL_DST, dstMAC.clone()) );
                       Flow f = new Flow(match, actions);
                       // Modify the flow on the network node
                       Status status = programmer.addFlow(incoming_node, f);
            			return PacketResult.CONSUME;
            		}
            	}
            }else{
            	mac_to_port.put(srcMAC_val, incoming_connector);
            }
            // Do I know the destination MAC? Lookup dstMAC in dictionary
            
            dst_connector = mac_to_port.get(dstMAC_val);
            // If found, generate match and action for the flow, and program a
            // flow rule in switch
                if (dst_connector != null) {

                 List<Action> actions = new ArrayList<Action>();
                 actions.add(new Output(dst_connector));

                 Match match = new Match();
                 match.setField( new MatchField(MatchType.IN_PORT, incoming_connector) );
                 match.setField( new MatchField(MatchType.DL_DST, dstMAC.clone()) );

                 Flow f = new Flow(match, actions);

                // Modify the flow on the network node
                 Status status = programmer.addFlow(incoming_node, f);
                 if (!status.isSuccess()) {
                     logger.warn("SDN Plugin failed to program the flow: {}. The failure is: {}",
                                  f, status.getDescription());
                     return PacketResult.IGNORED;
                 }
                 logger.info("Installed flow {} in node {}", f, incoming_node);
                
                } else{
                    floodPacket(inPkt);
                }
        }
        return PacketResult.CONSUME;
    }
}
