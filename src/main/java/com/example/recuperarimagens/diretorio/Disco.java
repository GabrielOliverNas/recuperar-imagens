package com.example.recuperarimagens.diretorio;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Slf4j
@Component
public class Disco {
  private static final String SALVAR_IMAGENS_ANO =
      "C://Users//gabriel.nascimento//Pictures//Camera Roll//fotos//ano//";
  private static final String SALVAR_IMAGENS_MES =
      "C://Users//gabriel.nascimento//Pictures//Camera Roll//fotos//ano//meses//";
  private static final String SALVAR_IMAGENS_DIA =
      "C://Users//gabriel.nascimento//Pictures//Camera Roll//fotos//ano//meses//dias//";

  private static final String UPLOADED_FOLDER = "C://Users//gabriel.nascimento//Documents//fotos//";

  public String salvarFotos(MultipartFile multipartFile) throws IOException {
    try {
      byte[] bytes = multipartFile.getBytes();
      Path path =
          Paths.get(
              UPLOADED_FOLDER.concat(Objects.requireNonNull(multipartFile.getOriginalFilename())));
      Files.write(path, bytes);
    } catch (IOException e) {
      e.printStackTrace();
    }

    return multipartFile.getOriginalFilename();
  }

  public String compressImage(String nomeArquivo) throws IOException {
    final String nomeFotoExtensao = getNomeArquivo();

    Path imagemDiretorio = Paths.get(UPLOADED_FOLDER.concat(nomeArquivo));
    Path salvarNoDiretorio = Paths.get(SALVAR_IMAGENS_ANO.concat(nomeFotoExtensao));

    try (InputStream arquivo = new FileInputStream(imagemDiretorio.toFile())) {
      resize(arquivo, salvarNoDiretorio, 600, 480);
    } catch (RuntimeException e) {
      throw new RuntimeException("Não foi possivel redimencionar a foto");
    }
    return nomeFotoExtensao;
  }

  private static void resize(InputStream input, Path target, int width, int height)
      throws IOException {
    BufferedImage imagemDiretorio = ImageIO.read(input);
    Image imagemReduzida = imagemDiretorio.getScaledInstance(width, height, Image.SCALE_SMOOTH);

    String nomeArquivo = target.getFileName().toString();
    String novaExtensao = nomeArquivo.substring(nomeArquivo.lastIndexOf(".") + 1);

    final BufferedImage converterImagem = converterBufferedImage(imagemReduzida);

    escreverImagemNoDiretorio(nomeArquivo, converterImagem, novaExtensao);
  }

  public static BufferedImage converterBufferedImage(Image img) {
    BufferedImage bufferedImage =
        new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_RGB);

    Graphics2D g = bufferedImage.createGraphics();
    g.drawImage(img, 0, 0, null);
    g.dispose();

    return bufferedImage;
  }

  public void alterarFotoPorNome(String arquivo) throws IOException {

    final String nomeFotoExtensao = getNomeArquivo();

    Path foto = Paths.get(SALVAR_IMAGENS_ANO.concat(arquivo));

    String nomeArquivo = foto.getFileName().toString();
    String novaExtensao = nomeArquivo.substring(nomeArquivo.lastIndexOf(".") + 1);

    try (InputStream is = new FileInputStream(foto.toFile())) {
      BufferedImage imagemDiretorio = ImageIO.read(is);

      final BufferedImage novaImagem = getBordaPreta(imagemDiretorio);

      escreverImagemNoDiretorio(nomeFotoExtensao, novaImagem, novaExtensao);
    } catch (RuntimeException e) {
      throw new RuntimeException("Não foi possivel redimencionar a foto");
    }
  }

  private static String getNomeArquivo() {
    final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
    final LocalDateTime now = LocalDateTime.now();
    final String extensao = ".png";
    final String diretorio = "//";

    String replaceBarraData = diretorio.concat(replaceRemoveBarraData(dtf.format(now)));
    final String replaceHoraMinSeg = replaceHoraMinSegundos(replaceBarraData);
    final String dataSemEspaco = replaceRetirarEspaco(replaceHoraMinSeg);
    final String dataCodEqp = dataSemEspaco.concat("-EQP1");
    return dataCodEqp.concat("-SEQ_IMAGEM_1").concat(extensao);
  }

  private static String replaceRemoveBarraData(String data) {
    return data.replaceAll("/", "");
  }

  private static String replaceHoraMinSegundos(String data) {
    return data.replaceAll(":", "");
  }

  private static String replaceRetirarEspaco(String data) {
    return data.replaceAll(" ", "");
  }

  private static BufferedImage getBordaPreta(BufferedImage imagemDiretorio) {
    int width = 660, height = 480;

    BufferedImage novaImagem = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    Graphics2D g = novaImagem.createGraphics();
    g.setColor(Color.black);
    g.drawImage(imagemDiretorio, 0, 0, null);
    g.dispose();

    return novaImagem;
  }

  private static List<String> escreverImagemNoDiretorio(
      String nomeArquivo, BufferedImage img, String novaExtensao) {
    final List<String> diretorios =
        Arrays.asList(SALVAR_IMAGENS_ANO, SALVAR_IMAGENS_MES, SALVAR_IMAGENS_DIA);

    diretorios.forEach(
        diretorio -> {
          try {
            final Path novoDiretorio = Paths.get(diretorio.concat(nomeArquivo));
            ImageIO.write(img, novaExtensao, novoDiretorio.toFile());
          } catch (IOException e) {
            e.printStackTrace();
          }
        });
    return diretorios;
  }
}
