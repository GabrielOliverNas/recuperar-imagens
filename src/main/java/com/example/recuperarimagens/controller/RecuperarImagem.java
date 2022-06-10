package com.example.recuperarimagens.controller;

import com.example.recuperarimagens.diretorio.Disco;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/fotos")
public class RecuperarImagem {

  @Autowired private Disco disco;

  @PostMapping()
  public ResponseEntity upload(@RequestParam MultipartFile imagem) throws IOException {
    final String nomeArquivo = disco.salvarFotos(imagem);
    final String nomeArquivoComExtensao = disco.compressImage(nomeArquivo);
    disco.alterarFotoPorNome(nomeArquivoComExtensao);

    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }
}
