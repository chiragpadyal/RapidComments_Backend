package com.RapidComments.ProdSense.Repositorys;

import com.RapidComments.ProdSense.Entitys.ThreadEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ThreadRepo extends JpaRepository<ThreadEntity, String>, JpaSpecificationExecutor<ThreadEntity> {

}
