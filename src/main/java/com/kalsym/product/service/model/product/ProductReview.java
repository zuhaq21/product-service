package com.kalsym.product.service.model.product;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

/**
 *
 * @author 7cu
 */
@Getter
@Setter
@ToString
@Entity
@Table(name = "product_review")
@NoArgsConstructor
//Using IdClass annotation to resolve the composite PK problem in hibernate
//@IdClass(ProductReviewId.class)
public class ProductReview implements Serializable {

    @Id
    private String customerId;

    private int rating;

    private String review;

//    @ManyToOne
//    @JoinColumn(name="productId")
    

    private String productId;

    
    //@ManyToOne(fetch = FetchType.LAZY, optional = false)
    //@JoinColumn(name = "productId")
    //@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "productId")
//    @Id
//    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    //@JoinColumn(name = "productId", nullable = false)
    //@OnDelete(action = OnDeleteAction.CASCADE)
    //@JsonIgnore
    //private Product product;
//    @Id 
//   
}
