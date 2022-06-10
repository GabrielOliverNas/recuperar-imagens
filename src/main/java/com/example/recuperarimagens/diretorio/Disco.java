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
import java.util.Objects;

@Slf4j
@Component
public class Disco {

  private static final String BUSCAR_FOTO_ENDERECO =
      "C://Users//gabriel.nascimento//Pictures//Imagens origins//";
  private static final String BUSCAR_NOME_FOTO = "//images.jpg";

  private static final String ENDERECO_SALVAR_FOTO_ATUALIZADA =
      "C://Users//gabriel.nascimento//Pictures//Camera Roll//fotos";
  private static final String SALVAR_NOME_FOTO_ATUALIZADA = "//novafoto.png";

  public void salvarFotos(MultipartFile multipartFile) throws IOException {
    // salvar(BUSCAR_FOTO_ENDERECO, multipartFile);
    compressImage();
  }

  public void compressImage() throws IOException {
    Path source = Paths.get(BUSCAR_FOTO_ENDERECO.concat(BUSCAR_NOME_FOTO));
    Path target = Paths.get(ENDERECO_SALVAR_FOTO_ATUALIZADA.concat(SALVAR_NOME_FOTO_ATUALIZADA));

    try (InputStream is = new FileInputStream(source.toFile())) {
      resize(is, target, 480, 600);
    } catch (RuntimeException e) {
      throw new RuntimeException("Não foi possivel redimencionar a foto");
    }
  }

  private static void resize(InputStream input, Path target, int width, int height)
      throws IOException {
    BufferedImage imagemDiretorio = ImageIO.read(input);
    Image newResizedImage = imagemDiretorio.getScaledInstance(width, height, Image.SCALE_SMOOTH);

    String nomeArquivo = target.getFileName().toString();
    String novaExtensao = nomeArquivo.substring(nomeArquivo.lastIndexOf(".") + 1);

    ImageIO.write(converterBufferedImage(newResizedImage), novaExtensao, target.toFile());
  }

  public static BufferedImage converterBufferedImage(Image img) {
    BufferedImage bufferedImage =
        new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);

    Graphics2D g = bufferedImage.createGraphics();
    g.drawImage(img, 0, 0, null);
    g.dispose();

    return bufferedImage;
  }
}
