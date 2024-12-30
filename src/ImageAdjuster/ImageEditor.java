package ImageAdjuster;

import java.awt.image.BufferedImage;

import static java.lang.Math.floor;

public class ImageEditor {

  public BufferedImage image;

  public ImageEditor(BufferedImage image) {
    this.image = image;
  }

  /**
   * removes a certain amount of pixels from an image and adds whatever the background is
   * @param amountRemove amount to removed.
   */
  public void removeColumn(int amountRemove) {
    int imageWidth = image.getWidth();
    int imageHeight = image.getHeight();

    BufferedImage editedImage = new BufferedImage(imageWidth, imageHeight,
            BufferedImage.TYPE_INT_ARGB);

    for (int x = 0; x < imageWidth; x++) {
      for (int y = 0; y < amountRemove; y++) {
        int pixel = image.getRGB(0, 0);
        editedImage.setRGB(x, y, pixel);
      }
    }


    // redraw a grid just excluding the chosen column
    for (int x = 0; x < imageWidth; x++) {
      for (int y = 0; y < imageHeight; y++) {
        int pixel = image.getRGB(x, y);
        try {
          editedImage.setRGB(x, y + amountRemove, pixel);
        } catch (Exception e) {
          break;
        }

      }
    }

    image = editedImage;
  }

  /**
   * adds a certain amount of pixels from an image to the middle of the image.
   * @param amountChanged amount to add.
   */
  public void addColumn(int amountChanged) {
    int imageWidth = image.getWidth();
    int imageHeight = image.getHeight();

    BufferedImage editedImage = new BufferedImage(imageWidth, imageHeight,
            BufferedImage.TYPE_INT_ARGB);

    int midpoint = (int) floor((double) imageHeight / 2);

    // redraw a grid just excluding the chosen column
    for (int x = imageWidth - 1; x >= 0; x--) {
      for (int y = imageHeight - 1; y >= 0; y--) {
        int pixel = image.getRGB(x, y);

        if (y > midpoint) {
          editedImage.setRGB(x, y, pixel);
        }
        if (y == midpoint) {
          for (int i = 0; i < amountChanged; i++) {
            editedImage.setRGB(x, y - i, pixel);
          }
        } else if (y < midpoint) {
          try {
            editedImage.setRGB(x, y - amountChanged, pixel);
          } catch (Exception e) {
            break;
          }
        }
      }
    }

    image = editedImage;
  }
}
