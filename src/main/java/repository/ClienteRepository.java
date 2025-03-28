package repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import models.Cliente;

import java.util.List;

@Repository
public interface ClienteRepository extends JpaRepository <Cliente, Integer>{


    @Query("SELECT * FROM Cliente c"
            + "WHERE c.dni LIKE %:texto% "
            + "OR c.nombre LIKE %:texto% ")


    List <Cliente> findByDni(@Param("dni") Integer dni);


}
