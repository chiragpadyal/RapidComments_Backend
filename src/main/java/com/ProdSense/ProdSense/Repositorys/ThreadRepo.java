package com.ProdSense.ProdSense.Repositorys;

import com.ProdSense.ProdSense.Entitys.ThreadEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ThreadRepo extends JpaRepository<ThreadEntity, String>, JpaSpecificationExecutor<ThreadEntity> {

}

