# sample-spring-data-jpa-hibernate-java-conf
sample spring data jpa project with hibernate

- Uses webjars to get jquery and bootstrap js files
- Fallback to local js files coming from webjar if js file is not available from cdn
- Entity has version column to facilitate optimistic locking
- Entity is auditable (has columns createdBy, creationDate, modifiedBy, modificationDate)
- Authentication based on users fetched by spring data JPA
- Uses DelegatingPasswordEncoder to support passwords encoded in multiple formats
- Loads initial authentication seed data on application startup
