package com.kalsym.product.service.model.store;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;

/**
 *
 * @author Sarosh
 */
@Entity
@Table(name = "client")
@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@ToString
public class Client implements Serializable {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;

    @NotBlank(message = "username is required")
    private String username;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @NotBlank(message = "name is required")
    private String name;

    @NotBlank(message = "email is required")
    private String email;

    private String storeId;

    private String liveChatAgentId;

    private Boolean locked;
    private Boolean deactivated;

    @CreationTimestamp
    Date created;

    @UpdateTimestamp
    Date updated;

    @NotBlank(message = "role is required")
    private String roleId;

    public void update(Client user) {
        if (null != user.getEmail()) {
            email = user.getEmail();
        }

        if (null != user.getDeactivated()) {
            deactivated = user.getDeactivated();
        }

        if (null != user.getRoleId()) {
            roleId = user.getRoleId();
        }

        if (null != user.getUsername()) {
            username = user.getUsername();
        }

        if (null != user.getPassword()) {
            password = user.getPassword();
        }

        if (null != user.getName()) {
            name = user.getName();
        }

        if (null != user.getLocked()) {
            locked = user.getLocked();
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Client other = (Client) obj;
        return Objects.equals(this.id, other.getId());
    }

    @Override
    public String toString() {
        return "Client{" + "id=" + id + ", username=" + username + ", password=" + password + ", name=" + name + ", email=" + email + ", locked=" + locked + ", deactivated=" + deactivated + ", created=" + created + ", updated=" + updated + ", roleId=" + roleId + '}';
    }
}
