package com.luv2code.springdemo.entity;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Version;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.luv2code.springdemo.audit.Auditable;

import lombok.AllArgsConstructor;
import lombok.Builder;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "customer")
@Builder
@AllArgsConstructor
public class Customer extends Auditable<String> {

	/*
	Extending org.springframework.data.jpa.domain.AbstractAuditable gives error
	"Specified key was too long" if we use string as id column with hibernate JPA on mysql db
	So creating our own mappedSuperClass for auditing and defining Id column in subclass
	*/

	@Id
	@Column(name = "id", length = 40)
	private String id;

	@Column(name = "first_name", length = 127)
	private String firstName;

	@Column(name = "last_name", length = 127)
	private String lastName;

	@Column(name = "email", length = 240, unique = true)
	private String email;

	@Version
	@Column(name = "version")
	private Long version;

	public Customer() {}


	public String getId() { return id; }

	public void setId(String id) { this.id = id; }

	public String getFirstName() { return firstName; }

	public void setFirstName(String firstName) { this.firstName = firstName; }

	public String getLastName() { return lastName; }

	public void setLastName(String lastName) { this.lastName = lastName; }

	public String getEmail() { return email; }

	public void setEmail(String email) { this.email = email; }

	public Long getVersion() { return version; }

	public void setVersion(Long version) { this.version = version; }

	@Override
	public String toString() {
		return "Customer [id=" + getId() + ", firstName=" + firstName
				+ ", lastName="
				+ lastName + ", email=" + email + ", version=" + version + "]";
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) { return false; }
		if (!(obj instanceof Customer)) { return false; }
		return getId().equals(((Customer) obj).getId());
	}

	@PrePersist
	public void prePersist() {
		setId(UUID.randomUUID().toString());
	}

}
