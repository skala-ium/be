package com.example.skala_ium.clazz.infrastructure;

import com.example.skala_ium.clazz.domain.entity.Clazz;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ClassRepository extends JpaRepository<Clazz, UUID> {
}
