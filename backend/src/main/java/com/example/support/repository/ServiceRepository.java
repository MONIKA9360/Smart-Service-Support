package com.example.support.repository;

import com.example.support.entity.Service;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ServiceRepository extends JpaRepository<Service, Long> {

    Optional<Service> findByServiceName(String serviceName);

    boolean existsByServiceName(String serviceName);

    List<Service> findByIsActiveTrue();

    List<Service> findByCategory(String category);
}
