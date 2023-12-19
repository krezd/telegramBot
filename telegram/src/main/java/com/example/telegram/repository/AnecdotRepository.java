package com.example.telegram.repository;

import com.example.telegram.entity.AnecdotEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AnecdotRepository extends JpaRepository<AnecdotEntity,Long> {

}
