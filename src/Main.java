
void main() {

    try {
        Converter converter = new Converter();


        converter.setArtWidth(500);
        converter.setDownscaleFactor(1);
        converter.setAsciiSet(0);
        converter.setInvert(false);
        converter.setSaveToFile(true);
        converter.setOutputPath("src/ascii_output.txt");


        converter.convert("cinema.png");

    } catch (Exception e) {
        e.printStackTrace();
    }
}

