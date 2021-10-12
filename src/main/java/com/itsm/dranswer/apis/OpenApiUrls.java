package com.itsm.dranswer.apis;

/*
 * @package : com.itsm.dranswer.apis
 * @name : OpenApiUrls.java
 * @date : 2021-10-08 오후 1:23
 * @author : xeroman.k
 * @version : 1.0.0
 * @modifyed :
 */

import lombok.experimental.UtilityClass;

@UtilityClass
public class OpenApiUrls {

    // VPC
    private static final String VPC = "/vpc/v2";

    public static final String CREATE_VPC = VPC + "/createVpc";
    public static final String GET_VPC_DETAIL = VPC + "/getVpcDetail";
    public static final String GET_VPC_LIST = VPC + "/getVpcList";

    public static final String GET_NETWORK_ACL_LIST = VPC + "/getNetworkAclList";
    public static final String CREATE_NETWORK_ACL = VPC + "/createNetworkAcl";

    public static final String GET_NETWORK_ACL_RULE_LIST = VPC + "/getNetworkAclRuleList";
    public static final String ADD_NETWORK_ACL_INBOUND_RULE = VPC + "/addNetworkAclInboundRule";
    public static final String REMOVE_NETWORK_ACL_INBOUND_RULE = VPC + "/removeNetworkAclInboundRule";
    public static final String ADD_NETWORK_ACL_OUTBOUND_RULE = VPC + "/addNetworkAclOutboundRule";
    public static final String REMOVE_NETWORK_ACL_OUTBOUND_RULE = VPC + "/removeNetworkAclOutboundRule";


    public static final String GET_SUBNET_LIST = VPC + "/getSubnetList";
    public static final String CREATE_VPC_SUBNET = VPC + "/createSubnet";
    public static final String GET_SUBNET_DETAIL = VPC + "/getSubnetDetail";

    // VPC Server
    private static final String VPC_SERVER = "/vserver/v2";

    // VPC Common
    public static final String GET_VPC_REGION_LIST = VPC_SERVER + "/getRegionList";
    public static final String GET_VPC_ZONE_LIST = VPC_SERVER + "/getZoneList";
    public static final String GET_VPC_SERVER_IMAGE_PRODUCT_LIST = VPC_SERVER + "/getServerImageProductList";
    public static final String GET_VPC_SERVER_PRODUCT_LIST = VPC_SERVER + "/getServerProductList";

    // VPC Server
    public static final String GET_SERVER_INSTANCE_LIST = VPC_SERVER + "/getServerInstanceList";
    public static final String GET_SERVER_INSTANCE_DETAIL = VPC_SERVER + "/getServerInstanceDetail";
    public static final String CREATE_VPC_SERVER_INSTANCES = VPC_SERVER + "/createServerInstances";
    public static final String START_VPC_SERVER_INSTANCES = VPC_SERVER + "/startServerInstances";
    public static final String REBOOT_VPC_SERVER_INSTANCES = VPC_SERVER + "/rebootServerInstances";
    public static final String STOP_VPC_SERVER_INSTANCES = VPC_SERVER + "/stopServerInstances";
    public static final String TERMINATE_VPC_SERVER_INSTANCES = VPC_SERVER + "/terminateServerInstances";

    // LoginKey
    public static final String CREATE_LOGIN_KEY = VPC_SERVER + "/createLoginKey";
    public static final String GET_LOGIN_KEY_LIST = VPC_SERVER + "/getLoginKeyList";

    // Network Interface
    public static final String CREATE_VPC_NETWORK_INTERFACE = VPC_SERVER + "/createNetworkInterface";
    public static final String GET_VPC_NETWORK_INTERFACE_LIST = VPC_SERVER + "/getNetworkInterfaceList";

    // ACG
    public static final String GET_ACG_LIST = VPC_SERVER + "/getAccessControlGroupList";
    public static final String GET_ACG_DETAIL = VPC_SERVER + "/getAccessControlGroupDetail";
    public static final String CREATE_ACG = VPC_SERVER + "/createAccessControlGroup";

    public static final String GET_ACG_RULE_LIST = VPC_SERVER + "/getAccessControlGroupRuleList";
    public static final String ADD_ACG_INBOUND_RULE = VPC_SERVER + "/addAccessControlGroupInboundRule";
    public static final String ADD_ACG_OUTBOUND_RULE = VPC_SERVER + "/addAccessControlGroupOutboundRule";
    public static final String REMOVE_ACG_INBOUND_RULE = VPC_SERVER + "/removeAccessControlGroupInboundRule";
    public static final String REMOVE_ACG_OUTBOUND_RULE = VPC_SERVER + "/removeAccessControlGroupOutboundRule";


    // PUBLIC IP
    public static final String DELETE_PUBLIC_IP_INSTANCE = VPC_SERVER + "/deletePublicIpInstance";
    public static final String DISASSOCIATE_PUBLIC_IP_INSTANCE = VPC_SERVER + "/disassociatePublicIpFromServerInstance";

    // NAS
    private static final String VPC_NAS = "/vnas/v2";
    public static final String GET_NAS_LIST = VPC_NAS + "/getNasVolumeInstanceList";
    public static final String GET_NAS_DETAIL = VPC_NAS + "/getNasVolumeInstanceDetail";
    public static final String CREATE_NAS = VPC_NAS + "/createNasVolumeInstance";
    public static final String DELETE_NAS = VPC_NAS + "/deleteNasVolumeInstances";
    public static final String CHANGE_NAS_VOLUME_SIZE = VPC_NAS + "/changeNasVolumeSize";

}
