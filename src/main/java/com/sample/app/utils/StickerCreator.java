package com.sample.app.utils;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.imageio.ImageIO;

import com.sample.app.exception.CustomMsgException;
import com.sample.app.model.Conteudo;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Font;

public class StickerCreator {

    private static final double H_PERC = 0.2;
    private BufferedImage imagemOriginal;

    public void criarFigurinha(Conteudo conteudo, String legenda) throws CustomMsgException {
        try {
            InputStream inputStream = new URL(conteudo.getImage()).openStream();
            imagemOriginal = ImageIO.read(inputStream);

            int newHeight = (int) (imagemOriginal.getHeight() * (H_PERC + 1));
            BufferedImage novaImagem = new BufferedImage(imagemOriginal.getWidth(),
                newHeight, BufferedImage.TRANSLUCENT);
            
            processarGraphics2D(novaImagem, legenda, newHeight);

            ImageIO.write(novaImagem, "png", new FileHandler()
                .gerarArquivo(conteudo.getTitle()
                    //LocalDateTime.now().toString().replace("[^a-zA-Z0-9]", "").replace(":", "")
            ));
        } catch (IOException e) {
            throw new CustomMsgException("[!] Erro ao processar dados da imagem: " + conteudo.getImage(), e);
        }
    }

    private void processarGraphics2D(BufferedImage novaImagem, String legenda, int newHeight) {
        int width = imagemOriginal.getWidth();
        int height = imagemOriginal.getHeight();

        Graphics2D g2d = (Graphics2D) novaImagem.getGraphics();
        g2d.drawImage(imagemOriginal, 0, 0, null);
        g2d.setColor(Color.YELLOW);

        int fontSize, legWidth, i = 0;
        Font font;
        do {
            fontSize = (int) (height * H_PERC/2) - ++i;
            font = new Font(Font.SANS_SERIF, Font.BOLD, fontSize);
            g2d.setFont(font);
            legWidth = g2d.getFontMetrics().stringWidth(legenda);
        } while (legWidth > width);

        int textX = (int) (width - legWidth)/2;
        int textY = newHeight - (int) (height * H_PERC/2) + (int) (fontSize * (1/2 - H_PERC));
        g2d.drawString(legenda, textX, textY);
    }
}
