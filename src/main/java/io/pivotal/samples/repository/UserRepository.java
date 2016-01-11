package io.pivotal.samples.repository;

/**
 * Created by pivotal on 07/01/2016.
 */

import io.pivotal.samples.domain.User;
import org.springframework.data.cassandra.repository.TypedIdCassandraRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface UserRepository extends TypedIdCassandraRepository<User, String> {}
