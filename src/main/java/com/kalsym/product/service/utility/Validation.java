/*
 * Copyright (C) 2021 taufik
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.kalsym.product.service.utility;

import com.kalsym.product.service.model.MySQLUserDetails;
import com.kalsym.product.service.model.store.Store;
import java.util.Optional;
import com.kalsym.product.service.repository.StoreRepository;

/**
 *
 * @author taufik
 */
public class Validation {
    
    public static boolean VerifyClientId(String clientId) {
            MySQLUserDetails mysqlUserDetails = SessionInformation.getSessionInfo("Validation");            
            
            if (mysqlUserDetails.getIsSuperUser()) {
                return true;
            }
            
            if (!mysqlUserDetails.getSessionType().equals("CLIENT")) {
                return false;
            }
            
            if (clientId.equals(mysqlUserDetails.getOwnerId())) {
                return true;
            } else {
                return false;
            }
           
    }
    
    
    public static boolean VerifyStoreId(String storeId, StoreRepository storeRepository) {
            MySQLUserDetails mysqlUserDetails = SessionInformation.getSessionInfo("Validation");            
            
            Optional<Store> optStore = storeRepository.findById(storeId);

            if (!optStore.isPresent()) {
                return false;
            }
            
            if (mysqlUserDetails.getIsSuperUser()) {
                return true;
            }
            
            if (!mysqlUserDetails.getSessionType().equals("CLIENT")) {
                return false;
            }
            
            if (optStore.get().getClientId().equals(mysqlUserDetails.getOwnerId())) {
                    return true;
            } else {
                    return false;
            }            
    }
}
