package com.kalsym.product.service.controller;

import com.kalsym.product.service.ProductServiceApplication;
import com.kalsym.product.service.utility.HttpResponse;
import com.kalsym.product.service.model.product.Product;
import com.kalsym.product.service.model.product.ProductWithDetails;
import com.kalsym.product.service.repository.ProductAssetRepository;
import com.kalsym.product.service.repository.ProductInventoryItemRepository;
import com.kalsym.product.service.repository.StoreRepository;
import com.kalsym.product.service.repository.ProductRepository;
import com.kalsym.product.service.repository.ProductVariantRepository;
import com.kalsym.product.service.repository.ProductVariantAvailableRepository;
import com.kalsym.product.service.repository.ProductReviewRepository;
import com.kalsym.product.service.repository.ProductWithDetailsRepository;
import com.kalsym.product.service.utility.Logger;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.kalsym.product.service.repository.ProductInventoryWithDetailsRepository;
import com.kalsym.product.service.utility.Validation;

/**
 *
 * @author 7cu
 */
@RestController()
@RequestMapping("/products")
public class ProductController {

    @Autowired()
    ProductRepository productRepository;

    @Autowired
    ProductWithDetailsRepository productWithDetailsRepository;

    @Autowired
    ProductInventoryWithDetailsRepository productInventoryRepository;

    @Autowired
    ProductInventoryItemRepository productInventoryItemRepository;

    @Autowired
    ProductVariantRepository productVariantRepository;

    @Autowired
    ProductVariantAvailableRepository productVariantAvailableRepository;

    @Autowired
    ProductReviewRepository productReviewRepository;

    @Autowired
    ProductAssetRepository productAssetRepository;

    @Autowired
    StoreRepository storeRepository;

    /**
     * Get product by store or category or productId
     *
     * @param request
     * @param storeId
     * @param categoryId
     * @param featured
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping(path = {""}, name = "products-get", produces = "application/json")
    @PreAuthorize("hasAnyAuthority('products-get', 'all')")
    public ResponseEntity<HttpResponse> getProduct(HttpServletRequest request,
            @RequestParam(required = false) String storeId,
            @RequestParam(required = false) String categoryId,
            @RequestParam(required = false, defaultValue = "true") boolean featured,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int pageSize) {
        String logprefix = request.getRequestURI();
        HttpResponse response = new HttpResponse(request.getRequestURI());
        Logger.application.info(Logger.pattern, ProductServiceApplication.VERSION, logprefix, "", "");

        ProductWithDetails productMatch = new ProductWithDetails();
        
        Pageable pageable = PageRequest.of(page, pageSize);
        productMatch.setStoreId(storeId);
        productMatch.setCategoryId(categoryId);
        ExampleMatcher matcher = ExampleMatcher
                .matchingAll()
                .withIgnoreCase()
                .withStringMatcher(ExampleMatcher.StringMatcher.EXACT);
        Example<ProductWithDetails> example = Example.of(productMatch, matcher);

        response.setStatus(HttpStatus.OK);
        response.setData(productWithDetailsRepository.findAll(example, pageable));
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping(path = {"/{id}"}, name = "products-get-by-id", produces = "application/json")
    @PreAuthorize("hasAnyAuthority('products-get-by-id', 'all')")
    public ResponseEntity<HttpResponse> getProduct(HttpServletRequest request,
            @PathVariable(required = false) String id,
            @RequestParam(required = false, defaultValue = "true") boolean featured,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int pageSize) {
        String logprefix = request.getRequestURI();
        HttpResponse response = new HttpResponse(request.getRequestURI());
        Logger.application.info(Logger.pattern, ProductServiceApplication.VERSION, logprefix, "", "");

        Optional<ProductWithDetails> optProduct = productWithDetailsRepository.findById(id);

        if (!optProduct.isPresent()) {
            Logger.application.info(Logger.pattern, ProductServiceApplication.VERSION, logprefix, " NOT_FOUND id: " + id);
            response.setStatus(HttpStatus.NOT_FOUND);
            return ResponseEntity.status(response.getStatus()).body(response);
        }
        
        response.setStatus(HttpStatus.OK);
        response.setData(optProduct.get());
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    /**
     *
     * @param request
     * @param storeId
     * @param name
     * @param featured
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping(path = {"/search"}, name = "products-search", produces = "application/json")
    @PreAuthorize("hasAnyAuthority('products-search', 'all')")
    //@RequestMapping(value = "", method = RequestMethod.GET, produces = "application/json", params = {"storeId", "name", "featured"})
    public ResponseEntity<HttpResponse> searchProduct(HttpServletRequest request,
            @RequestParam(required = false) String storeId,
            @RequestParam(required = false) String name,
            @RequestParam(required = false, defaultValue = "true") boolean featured,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int pageSize) {
        String logprefix = request.getRequestURI();
        HttpResponse response = new HttpResponse(request.getRequestURI());
        Logger.application.info(Logger.pattern, ProductServiceApplication.VERSION, logprefix, "", "");        
        
        Product productMatch = new Product();

        Pageable pageable = PageRequest.of(page, pageSize);
        productMatch.setStoreId(storeId);
        productMatch.setName(name);
        ExampleMatcher matcher = ExampleMatcher
                .matchingAll()
                .withIgnoreCase()
                .withStringMatcher(ExampleMatcher.StringMatcher.EXACT);
        Example<Product> example = Example.of(productMatch, matcher);

        response.setStatus(HttpStatus.OK);
        response.setData(productRepository.findAll(example, pageable));
        return ResponseEntity.status(response.getStatus()).body(response);

    }

    //@PreAuthorize("hasAnyAuthority('products-delete-by-id', 'all') and projectAccessHandler.validatedProductOwner(#id)")
    
    @DeleteMapping(path = {"/{id}"}, name = "products-delete-by-id")
    @PreAuthorize("hasAnyAuthority('products-delete-by-id','all') and @customOwnerVerifier.VerifyProduct(#id)")
    public ResponseEntity<HttpResponse> deleteProductById(HttpServletRequest request,
            @PathVariable String id) {
        String logprefix = request.getRequestURI();
        HttpResponse response = new HttpResponse(request.getRequestURI());
        Logger.application.info(Logger.pattern, ProductServiceApplication.VERSION, logprefix, "", "");

        Optional<Product> optProduct = productRepository.findById(id);

        if (!optProduct.isPresent()) {
            Logger.application.info(Logger.pattern, ProductServiceApplication.VERSION, logprefix, " NOT_FOUND: " + id, "");
            response.setStatus(HttpStatus.NOT_FOUND);
            return ResponseEntity.status(response.getStatus()).body(response);
        }
        
        if (!Validation.VerifyStoreId(optProduct.get().getStoreId(), storeRepository)) {
            Logger.application.error(Logger.pattern, ProductServiceApplication.VERSION, logprefix, "Unathorized productId", "");
            response.setStatus(HttpStatus.UNAUTHORIZED);
            response.setError("Unathorized productId");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }

        Logger.application.info(Logger.pattern, ProductServiceApplication.VERSION, logprefix, "product found", "");
        productRepository.delete(optProduct.get());
        Logger.application.info(Logger.pattern, ProductServiceApplication.VERSION, logprefix, "product deleted, with id: {}", id);
        response.setStatus(HttpStatus.OK);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    /**
     *
     * @param request
     * @param id
     * @param bodyProduct
     * @return
     */
    @PutMapping(path = {"/{id}"}, name = "products-put-by-id")
    @PreAuthorize("hasAnyAuthority('products-put-by-id', 'all') and @customOwnerVerifier.VerifyProduct(#id)")
    public ResponseEntity<HttpResponse> putProductById(HttpServletRequest request,
            @PathVariable String id,
            @RequestBody Product bodyProduct) {
        String logprefix = request.getRequestURI();
        HttpResponse response = new HttpResponse(request.getRequestURI());
        Logger.application.info(Logger.pattern, ProductServiceApplication.VERSION, logprefix, "", "");

        Optional<Product> optProduct = productRepository.findById(id);

        if (!optProduct.isPresent()) {
            Logger.application.info(Logger.pattern, ProductServiceApplication.VERSION, logprefix, "NOT_FOUND: {}", id);
            response.setStatus(HttpStatus.NOT_FOUND);
            return ResponseEntity.status(response.getStatus()).body(response);
        }
        
        if (!Validation.VerifyStoreId(optProduct.get().getStoreId(), storeRepository)) {
            Logger.application.error(Logger.pattern, ProductServiceApplication.VERSION, logprefix, "Unathorized productId", "");
            response.setStatus(HttpStatus.UNAUTHORIZED);
            response.setError("Unathorized productId");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }

        Logger.application.info(Logger.pattern, ProductServiceApplication.VERSION, logprefix, "product found with productId: {}", id);
        Product product = optProduct.get();
        List<String> errors = new ArrayList<>();

        product.update(bodyProduct);

        Logger.application.info(Logger.pattern, ProductServiceApplication.VERSION, logprefix, "product updated for productId: " + id, "");
        response.setStatus(HttpStatus.ACCEPTED);
        response.setData(productRepository.save(product));
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
    }

}
