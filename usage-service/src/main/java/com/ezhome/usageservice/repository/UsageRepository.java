package com.ezhome.usageservice.repository;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import com.ezhome.usageservice.entity.Usage;


public interface UsageRepository extends JpaRepository<Usage, UUID> {
    
}
