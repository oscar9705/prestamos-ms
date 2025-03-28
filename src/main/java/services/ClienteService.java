package services;

import models.Cliente;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import repository.ClienteRepository;

import java.util.List;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;


    public List<Cliente> findAll() {
        return clienteRepository.findAll();
    }

    public List<Cliente> findByDni(Integer dni) {
        return clienteRepository.findByDni(dni);

    }
}
