import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

public class Converter {
    private int artWidth = 100;
    private int downscaleFactor = 2;
    private int asciiSetIndex = 0;
    private boolean invert = false;
    private boolean saveToFile = false;
    private String outputPath = "ascii_art.txt";

    private static final String[] ASCII_SETS = {
            "@%#*+=-:. ",
            "█▓▒░ ",
            "$@B%8&WM#*oahkbdpqwmZO0QLCJUYXzcvunxrjft/\\|()1{}[]?-_+~<>i!lI;:,\"^`'. "
    };

    public void convert(String inputPath) throws IOException {
        System.out.println("Пытаемся загрузить: " + inputPath);

        File inputFile = new File(inputPath);

        if (!inputFile.exists()) {
            throw new IOException("Файл не существует: " + inputFile.getAbsolutePath());
        }

        System.out.println("Размер файла: " + inputFile.length() + " байт");


        BufferedImage image = ImageIO.read(inputFile);

        if (image == null) {
            throw new IOException("ImageIO не может прочитать файл: " + inputFile.getAbsolutePath() +
                    "\nПоддерживаемые форматы: " + String.join(", ", ImageIO.getReaderFormatNames()));
        }

        System.out.println("Изображение загружено: " + image.getWidth() + "x" + image.getHeight());
        convert(image);
    }

    public void convert(BufferedImage image) {
        String asciiSet = ASCII_SETS[asciiSetIndex];

        int newWidth = artWidth;
        int newHeight = (int)((image.getHeight() * newWidth * 0.25) / image.getWidth());

        BufferedImage smallGray = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_BYTE_GRAY);
        Graphics2D g = smallGray.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.drawImage(image, 0, 0, newWidth, newHeight, null);
        g.dispose();

        StringBuilder asciiArt = new StringBuilder();

        for (int y = 0; y < newHeight; y += downscaleFactor) {
            for (int x = 0; x < newWidth; x += downscaleFactor) {
                int pixel = smallGray.getRGB(x, y);
                int brightness = pixel & 0xFF;
                asciiArt.append(brightnessToChar(brightness, asciiSet));
            }
            asciiArt.append("\n");
        }

        String result = asciiArt.toString();
        System.out.println(result);

        if (saveToFile) {
            saveToFile(result);
        }
    }

    private char brightnessToChar(int brightness, String asciiSet) {
        int index;
        if (invert) {
            index = (brightness * (asciiSet.length() - 1)) / 255;
        } else {
            index = ((255 - brightness) * (asciiSet.length() - 1)) / 255;
        }
        return asciiSet.charAt(index);
    }

    private void saveToFile(String content) {
        try (PrintWriter writer = new PrintWriter(new File(outputPath))) {
            writer.print(content);
            System.out.println("✅ Сохранено в: " + new File(outputPath).getAbsolutePath());
        } catch (IOException e) {
            System.err.println("Ошибка сохранения: " + e.getMessage());
        }
    }

    // Сеттеры
    public void setArtWidth(int width) { this.artWidth = width; }
    public void setDownscaleFactor(int factor) { this.downscaleFactor = factor; }
    public void setAsciiSet(int index) { this.asciiSetIndex = index; }
    public void setInvert(boolean invert) { this.invert = invert; }
    public void setSaveToFile(boolean save) { this.saveToFile = save; }
    public void setOutputPath(String path) { this.outputPath = path; }
}