package repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.SystemConfiguration;

@Repository

public interface SystemConfigurationRepository extends
		JpaRepository<SystemConfiguration, Integer> {

	@Query("select s from SystemConfiguration s where s is not null")
	SystemConfiguration findSystemConf();

}
