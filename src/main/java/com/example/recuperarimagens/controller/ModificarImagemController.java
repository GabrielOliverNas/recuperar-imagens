package com.example.recuperarimagens.controller;

import com.example.recuperarimagens.service.DiscoService;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class ModificarImagemController {

  private final DiscoService discoService;

  @PostMapping()
  public ResponseEntity edicaoImagem(@RequestParam MultipartFile imagem) throws IOException {

    var nomeArquivo = discoService.salvarFotos(imagem);
    var nomeArquivoComExtensao = discoService.compressImage(nomeArquivo);
    discoService.alterarFotoPorNome(nomeArquivoComExtensao);

    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }
}
