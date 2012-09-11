// Licensed to the Apache Software Foundation (ASF) under one
// or more contributor license agreements.  See the NOTICE file
// distributed with this work for additional information
// regarding copyright ownership.  The ASF licenses this file
// to you under the Apache License, Version 2.0 (the
// "License"); you may not use this file except in compliance
// with the License.  You may obtain a copy of the License at
//
//   http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing,
// software distributed under the License is distributed on an
// "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
// KIND, either express or implied.  See the License for the
// specific language governing permissions and limitations
// under the License.
package com.cloud.api.commands;

import org.apache.log4j.Logger;

import com.cloud.api.ApiConstants;
import com.cloud.api.BaseCmd;
import com.cloud.api.IdentityMapper;
import com.cloud.api.Implementation;
import com.cloud.api.Parameter;
import com.cloud.api.ServerApiException;
import com.cloud.api.response.ZoneResponse;
import com.cloud.dc.DataCenter;
import com.cloud.user.Account;
import com.cloud.user.UserContext;

@Implementation(description="Creates a Zone.", responseObject=ZoneResponse.class)
public class CreateZoneCmd extends BaseCmd {
    public static final Logger s_logger = Logger.getLogger(CreateZoneCmd.class.getName());

    private static final String s_name = "createzoneresponse";

    /////////////////////////////////////////////////////
    //////////////// API parameters /////////////////////
    /////////////////////////////////////////////////////

    @Parameter(name=ApiConstants.DNS1, type=CommandType.STRING, required=true, description="the first DNS for the Zone")
    private String dns1;

    @Parameter(name=ApiConstants.DNS2, type=CommandType.STRING, description="the second DNS for the Zone")
    private String dns2;

    @Parameter(name=ApiConstants.GUEST_CIDR_ADDRESS, type=CommandType.STRING, description="the guest CIDR address for the Zone")
    private String guestCidrAddress;

    @Parameter(name=ApiConstants.INTERNAL_DNS1, type=CommandType.STRING, required=true, description="the first internal DNS for the Zone")
    private String internalDns1;

    @Parameter(name=ApiConstants.INTERNAL_DNS2, type=CommandType.STRING, description="the second internal DNS for the Zone")
    private String internalDns2;

    @Parameter(name=ApiConstants.NAME, type=CommandType.STRING, required=true, description="the name of the Zone")
    private String zoneName;

    @Parameter(name=ApiConstants.DOMAIN, type=CommandType.STRING, description="Network domain name for the networks in the zone")
    private String domain;

    @IdentityMapper(entityTableName="domain")
    @Parameter(name=ApiConstants.DOMAIN_ID, type=CommandType.LONG, description="the ID of the containing domain, null for public zones")
    private Long domainId; 
    
    @Parameter(name=ApiConstants.NETWORK_TYPE, type=CommandType.STRING, required=true, description="network type of the zone, can be Basic or Advanced")
    private String networkType; 
    
    @Parameter(name=ApiConstants.ALLOCATION_STATE, type=CommandType.STRING, description="Allocation state of this Zone for allocation of new resources")
    private String allocationState;
    
    @Parameter(name=ApiConstants.SECURITY_GROUP_EANBLED, type=CommandType.BOOLEAN, description="true if network is security group enabled, false otherwise")
    private Boolean securitygroupenabled;

    @Parameter(name=ApiConstants.LOCAL_STORAGE_ENABLED, type=CommandType.BOOLEAN, description="true if local storage offering enabled, false otherwise")
    private Boolean localStorageEnabled;

    /////////////////////////////////////////////////////
    /////////////////// Accessors ///////////////////////
    /////////////////////////////////////////////////////

    public String getDns1() {
        return dns1;
    }

    public String getDns2() {
        return dns2;
    }

    public String getGuestCidrAddress() {
        return guestCidrAddress;
    }

    public String getInternalDns1() {
        return internalDns1;
    }

    public String getInternalDns2() {
        return internalDns2;
    }

    public String getZoneName() {
        return zoneName;
    }

    public String getDomain() {
        return domain;
    }

    public Long getDomainId(){
    	return domainId;
    }
    
    public String getNetworkType(){
        return networkType;
    }
    
    public String getAllocationState() {
    	return allocationState;
    }
    
    public Boolean getSecuritygroupenabled() {
        if (securitygroupenabled == null) {
            return false;
        }
        return securitygroupenabled;
    }

    public Boolean getLocalStorageEnabled() {
        if (localStorageEnabled == null) {
            return false;
        }
        return localStorageEnabled;
    }

    /////////////////////////////////////////////////////
    /////////////// API Implementation///////////////////
    @Override
    public String getCommandName() {
        return s_name;
    }
    
    @Override
    public long getEntityOwnerId() {
        return Account.ACCOUNT_ID_SYSTEM;
    }
    
    @Override
    public void execute(){
    	UserContext.current().setEventDetails("Zone Name: "+getZoneName());
    	DataCenter result = _configService.createZone(this);
        if (result != null){
            ZoneResponse response = _responseGenerator.createZoneResponse(result,false);
            response.setResponseName(getCommandName());
            this.setResponseObject(response);
        } else {
            throw new ServerApiException(BaseCmd.INTERNAL_ERROR, "Failed to create a zone");
        }
    }
}
