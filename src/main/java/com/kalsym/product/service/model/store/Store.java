package com.kalsym.product.service.model.store;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;
import javax.persistence.Temporal;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

/**
 *
 * @author 7cu
 */
@Entity
@Getter
@Setter
@Table(name = "store")
@ToString
public class Store implements Serializable {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;

    private String name;

    private String city;

    private String address;

    private String clientId;

    private String verticalCode;

    private String storeDescription;

    private String postcode;

    private String email;

    private String paymentType;

    private String domain;

    private String liveChatOrdersGroupId;

    private String liveChatOrdersGroupName;

    private String liveChatCsrGroupId;

    private String liveChatCsrGroupName;

    private String regionCountryId;

    private String regionCountryStateId;

    private String phoneNumber;

    private Integer serviceChargesPercentage;
    
    private String googleAnalyticId;
        
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Date snoozeStartTime;
    
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Date snoozeEndTime;
    
    private String snoozeReason;
    
    private Boolean isBranch;
            
    private String latitude;
    
    private String longitude;
    
    public void update(Store store) {

        if (null != store.getCity()) {
            city = store.getCity();
        }

        if (null != store.getName()) {
            name = store.getName();
        }

        if (null != store.getAddress()) {
            address = store.getAddress();
        }

        if (null != store.getClientId()) {
            clientId = store.getClientId();
        }
        if (null != store.getVerticalCode()) {
            verticalCode = store.getVerticalCode();
        }

        if (null != store.getStoreDescription()) {           
            storeDescription = store.getStoreDescription();            
        }

        if (null != store.getPostcode()) {
            postcode = store.getPostcode();
        }

        if (null != store.getRegionCountryId()) {
            regionCountryId = store.getRegionCountryId();
        }

        if (null != store.getPhoneNumber()) {
            phoneNumber = store.getPhoneNumber();
        }

        if (null != store.getRegionCountryStateId()) {
            regionCountryStateId = store.getRegionCountryStateId();
        }

        if (null != store.getServiceChargesPercentage()) {
            serviceChargesPercentage = store.getServiceChargesPercentage();
        }

        if (null != store.getEmail()) {
            email = store.getEmail();
        }

        if (null != store.getPaymentType()) {
            paymentType = store.getPaymentType();
        }
        
        if (null != store.getIsBranch()) {
            isBranch = store.getIsBranch();
        }
        
        if (null != store.getLatitude()) {
            latitude = store.getLatitude();
        }
        
        if (null != store.getLongitude()) {
            longitude = store.getLongitude();
        }
        
         if (null != store.getDomain()) {
            domain = store.getDomain();
        }

    }

}
