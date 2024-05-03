package com.fiap.stock.driver.database.persistence.jpa

import com.fiap.stock.driver.database.persistence.entities.StockEntity
import org.springframework.data.repository.CrudRepository

interface StockJpaRepository : CrudRepository<StockEntity, Long>
