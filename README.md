# sample-spring-data-jpa-hibernate-java-conf
sample spring data jpa project with hibernate

- Uses webjars to get jquery and bootstrap js files
- Fallback to local js files coming from webjar if js file is not available from cdn
- Entity has version column to facilitate optimistic locking
- Entity is auditable (has columns createdBy, creationDate, modifiedBy, modificationDate)
