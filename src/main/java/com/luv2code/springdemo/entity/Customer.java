package com.luv2code.springdemo.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "customer")
public class Customer {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;

	@Column(name = "first_name")
	private String firstName;

	@Column(name = "last_name")
	private String lastName;

	@Column(name = "email")
	private String email;

	@Version
	@Column(name = "version")
	private Long version;

	@CreatedBy
	@Column(name = "created_by", nullable = false, updatable = false)
	private String createdBy;

	@CreatedDate
	@Column(name = "creation_date", nullable = false, updatable = false)
	private LocalDateTime creationDate;

	@LastModifiedBy
	@Column(name = "last_updated_by", nullable = false)
	private String lastUpdatedBy;

	@LastModifiedDate
	@Column(name = "last_update_date", nullable = false)
	private LocalDateTime lastUpdateDate;

	public Customer() {}

	public Integer getId() { return id; }

	public void setId(Integer id) { this.id = id; }

	public String getFirstName() { return firstName; }

	public void setFirstName(String firstName) { this.firstName = firstName; }

	public String getLastName() { return lastName; }

	public void setLastName(String lastName) { this.lastName = lastName; }

	public String getEmail() { return email; }

	public void setEmail(String email) { this.email = email; }

	public Long getVersion() { return version; }

	public void setVersion(Long version) { this.version = version; }

	public String getCreatedBy() { return createdBy; }

	public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }

	public LocalDateTime getCreated() { return creationDate; }

	public void setCreated(LocalDateTime created) { this.creationDate = created; }

	public String getModifiedBy() { return lastUpdatedBy; }

	public void setModifiedBy(String modifiedBy) {
		this.lastUpdatedBy = modifiedBy;
	}

	public LocalDateTime getModified() { return lastUpdateDate; }

	public void setModified(LocalDateTime modified) {
		this.lastUpdateDate = modified;
	}

	@Override
	public String toString() {
		return "Customer [id=" + id + ", firstName=" + firstName + ", lastName="
				+ lastName + ", email=" + email + ", version=" + version + "]";
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) { return false; }
		if (!(obj instanceof Customer)) { return false; }
		return id.equals(((Customer) obj).getId());
	}

}
