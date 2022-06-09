package com.example.recuperarimagens.controller;

import com.example.recuperarimagens.diretorio.Disco;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/fotos")
public class RecuperarImagem {

  @Autowired private Disco disco;

  @PostMapping()
  public void upload(@RequestParam MultipartFile imagem) throws IOException {
    disco.salvarFotos(imagem);
    int a = 0;
  }
}
