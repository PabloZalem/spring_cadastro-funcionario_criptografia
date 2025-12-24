package com.cadastrofuncionario.demo.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cadastrofuncionario.demo.entities.Funcionario;
import com.cadastrofuncionario.demo.repository.FuncionarioRepository;
import com.cadastrofuncionario.demo.service.impl.FuncionarioServiceImpl;

@RestController
@RequestMapping("/api/v1")
public class FuncionarioController {
    @Autowired
    private FuncionarioRepository funcionarioRepository;

    @Autowired
    private FuncionarioServiceImpl funcionarioServiceImpl;

    // Todos os funcionarios
    @GetMapping("/funcionarios")
    public List<Funcionario> getAllFuncionarios() {
        return funcionarioRepository.findAll();
    }

    // create Funcionario rest api
    @PostMapping("/funcionarios")
    public Funcionario criarFuncionario(@RequestBody Funcionario funcionario) {
        if (funcionario.getNome() == null || funcionario.getDepartamento() == null) {
            throw new IllegalArgumentException("Nome e departamento não podem ser nulos");
        }

        String encyrptNome = funcionarioServiceImpl.encrypt(funcionario.getNome());
        String encyrptDepartamento = funcionarioServiceImpl.encrypt(funcionario.getDepartamento());
        String encryptSalario = funcionarioServiceImpl.encrypt(String.valueOf(funcionario.getSalario()));

        Funcionario funcionario2 = new Funcionario();
        funcionario2.setNome(encyrptNome);
        funcionario2.setDepartamento(encyrptDepartamento);
        funcionario2.setSalario(encryptSalario); // valor original

        return funcionarioRepository.save(funcionario2);
    }

    @GetMapping("/funcionarios/{id}")
    public ResponseEntity<Funcionario> getFuncionarioById(@PathVariable Long id) {
        Funcionario funcionario = funcionarioRepository.findById(id);

        String decryptNome = funcionarioServiceImpl.decrypt(funcionario.getNome());
        String decryptDepartamento = funcionarioServiceImpl.decrypt(funcionario.getDepartamento());
        String decryptSalario = funcionarioServiceImpl.decrypt(funcionario.getSalario());

        Funcionario funcionario2 = new Funcionario();
        funcionario2.setId(funcionario.getId()); // manter o ID original
        funcionario2.setNome(decryptNome);
        funcionario2.setDepartamento(decryptDepartamento);
        funcionario2.setSalario(decryptSalario);

        return ResponseEntity.ok(funcionario2);
    }

    @PutMapping("/Funcionarios/update/{id}")
    public ResponseEntity<Funcionario> updateFuncionario(@PathVariable Long id, @RequestBody Funcionario funcionarioDetail) {
        Funcionario funcionario = funcionarioRepository.findById(id);

        String encyrptFName = funcionarioServiceImpl.encrypt(funcionario.getNome());
        String encyrptLName = funcionarioServiceImpl.encrypt(funcionario.getDepartamento());
        String encyrptEmail = funcionarioServiceImpl.encrypt(funcionario.getSalario());

        funcionario.setNome(encyrptFName);
        funcionario.setDepartamento(encyrptLName);
        funcionario.setSalario(encyrptEmail);

        Funcionario updatedFuncionario = funcionarioRepository.save(funcionario);
        return ResponseEntity.ok(updatedFuncionario);
    }

    // delete employee rest api
	@DeleteMapping("/employees/delete/{id}")
	public ResponseEntity<Map<String, Boolean>> deleteEmployee(@PathVariable Long id) {
		Funcionario funcionario = funcionarioRepository.findById(id);

		funcionarioRepository.delete(funcionario);
		Map<String, Boolean> response = new HashMap<>();
		response.put("deleted", Boolean.TRUE);
		return ResponseEntity.ok(response);
	}
}
