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
import java.util.Objects;

@Slf4j
@Component
public class Disco {
  private static final String ENDERECO_SALVAR_FOTO_ATUALIZADA =
      "C://Users//gabriel.nascimento//Pictures//Camera Roll//fotos//";
  private static final String FOTO_REDIMENSIONADA = "foto-600x480.png";

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
    final String nomeFotoExtensao = getDataNomeFoto();

    Path source = Paths.get(UPLOADED_FOLDER.concat(nomeArquivo));
    Path target = Paths.get(ENDERECO_SALVAR_FOTO_ATUALIZADA.concat(nomeFotoExtensao));

    try (InputStream is = new FileInputStream(source.toFile())) {
      resize(is, target, 600, 480);
    } catch (RuntimeException e) {
      throw new RuntimeException("Não foi possivel redimencionar a foto");
    }
    return nomeFotoExtensao;
  }

  private static void resize(InputStream input, Path target, int width, int height)
      throws IOException {
    BufferedImage imagemDiretorio = ImageIO.read(input);
    Image newResizedImage = imagemDiretorio.getScaledInstance(width, height, Image.SCALE_SMOOTH);

    String nomeArquivo = target.getFileName().toString();
    String novaExtensao = nomeArquivo.substring(nomeArquivo.lastIndexOf(".") + 1);

    final BufferedImage converterImagem = converterBufferedImage(newResizedImage);

    ImageIO.write(converterImagem, novaExtensao, target.toFile());
  }

  public static BufferedImage converterBufferedImage(Image img) {
    BufferedImage bufferedImage =
        new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);

    Graphics2D g = bufferedImage.createGraphics();
    g.drawImage(img, 0, 0, null);
    g.dispose();

    return bufferedImage;
  }

  public void alterarFotoPorNome(String arquivo) throws IOException {

    final String nomeFotoExtensao = getDataNomeFoto();

    Path foto = Paths.get(ENDERECO_SALVAR_FOTO_ATUALIZADA.concat(arquivo));
    Path caminho = Paths.get(ENDERECO_SALVAR_FOTO_ATUALIZADA.concat(nomeFotoExtensao));

    String nomeArquivo = caminho.getFileName().toString();
    String novaExtensao = nomeArquivo.substring(nomeArquivo.lastIndexOf(".") + 1);

    try (InputStream is = new FileInputStream(foto.toFile())) {
      BufferedImage imagemDiretorio = ImageIO.read(is);

      final BufferedImage novaImagem = getBordaPreta(imagemDiretorio);

      ImageIO.write(novaImagem, novaExtensao, caminho.toFile());
    } catch (RuntimeException e) {
      throw new RuntimeException("Não foi possivel redimencionar a foto");
    }
  }

  private static String getDataNomeFoto() {
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
}
