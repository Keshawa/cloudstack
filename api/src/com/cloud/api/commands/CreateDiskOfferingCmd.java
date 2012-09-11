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
import com.cloud.api.response.DiskOfferingResponse;
import com.cloud.offering.DiskOffering;
import com.cloud.offering.ServiceOffering;
import com.cloud.user.Account;

@Implementation(description="Creates a disk offering.", responseObject=DiskOfferingResponse.class)
public class CreateDiskOfferingCmd extends BaseCmd {
    public static final Logger s_logger = Logger.getLogger(CreateDiskOfferingCmd.class.getName());

    private static final String s_name = "creatediskofferingresponse";

    /////////////////////////////////////////////////////
    //////////////// API parameters /////////////////////
    /////////////////////////////////////////////////////

    @Parameter(name=ApiConstants.DISK_SIZE, type=CommandType.LONG, required=false, description="size of the disk offering in GB")
    private Long diskSize;

    @Parameter(name=ApiConstants.DISPLAY_TEXT, type=CommandType.STRING, required=true, description="alternate display text of the disk offering", length=4096)
    private String displayText;

    @Parameter(name=ApiConstants.NAME, type=CommandType.STRING, required=true, description="name of the disk offering")
    private String offeringName;

    @Parameter(name=ApiConstants.TAGS, type=CommandType.STRING, description="tags for the disk offering", length=4096)
    private String tags;

    @Parameter(name=ApiConstants.CUSTOMIZED, type=CommandType.BOOLEAN, description="whether disk offering is custom or not")
    private Boolean customized;
    
    @IdentityMapper(entityTableName="domain")
    @Parameter(name=ApiConstants.DOMAIN_ID, type=CommandType.LONG, description="the ID of the containing domain, null for public offerings")
    private Long domainId; 

    @Parameter(name=ApiConstants.STORAGE_TYPE, type=CommandType.STRING, description="the storage type of the disk offering. Values are local and shared.")
    private String storageType = ServiceOffering.StorageType.shared.toString();

    /////////////////////////////////////////////////////
    /////////////////// Accessors ///////////////////////
    /////////////////////////////////////////////////////

    public Long getDiskSize() {
        return diskSize;
    }

    public String getDisplayText() {
        return displayText;
    }

    public String getOfferingName() {
        return offeringName;
    }

    public String getTags() {
        return tags;
    }

    public Boolean isCustomized(){
    	return customized;
    }
    
    public Long getDomainId(){
    	return domainId;
    }

    public String getStorageType() {
        return storageType;
    }

    /////////////////////////////////////////////////////
    /////////////// API Implementation///////////////////
    /////////////////////////////////////////////////////

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
        DiskOffering offering = _configService.createDiskOffering(this);
        if (offering != null) {
            DiskOfferingResponse response = _responseGenerator.createDiskOfferingResponse(offering);
            response.setResponseName(getCommandName());
            this.setResponseObject(response);
        } else {
            throw new ServerApiException(BaseCmd.INTERNAL_ERROR, "Failed to create disk offering");
        } 
    }
}
