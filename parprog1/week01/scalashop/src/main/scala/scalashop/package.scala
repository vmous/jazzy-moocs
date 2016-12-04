
import common._

package object scalashop {

  /** The value of every pixel is represented as a 32 bit integer. */
  type RGBA = Int

  /** Returns the red component. */
  def red(c: RGBA): Int = (0xff000000 & c) >>> 24

  /** Returns the green component. */
  def green(c: RGBA): Int = (0x00ff0000 & c) >>> 16

  /** Returns the blue component. */
  def blue(c: RGBA): Int = (0x0000ff00 & c) >>> 8

  /** Returns the alpha component. */
  def alpha(c: RGBA): Int = (0x000000ff & c) >>> 0

  /** Used to create an RGBA value from separate components. */
  def rgba(r: Int, g: Int, b: Int, a: Int): RGBA = {
    (r << 24) | (g << 16) | (b << 8) | (a << 0)
  }

  /** Restricts the integer into the specified range. */
  def clamp(v: Int, min: Int, max: Int): Int = {
    if (v < min) min
    else if (v > max) max
    else v
  }

  /** Image is a two-dimensional matrix of pixel values. */
  class Img(val width: Int, val height: Int, private val data: Array[RGBA]) {
    def this(w: Int, h: Int) = this(w, h, new Array(w * h))
    def apply(x: Int, y: Int): RGBA = data(y * width + x)
    def update(x: Int, y: Int, c: RGBA): Unit = data(y * width + x) = c
  }

  /** Computes the blurred RGBA value of a single pixel of the input image. */
  def boxBlurKernel(src: Img, x: Int, y: Int, radius: Int): RGBA = {
    /* We compute the average value by separating the pixel into four channels,
     computing the average of each of the channels, and using the four average
     values to produce the final pixel value. In the previous figure, the radius
     parameter is equal to 1 and the average is computed from 9 pixels.

     Implement the boxBlurKernel method. Use two nested while-loops. Make sure
     that the pixels at the image edges are affected only by the pixels inside
     the image (hint: use the clamp method from the package object). */

    def clampX(v: Int): Int = clamp(v, 0, src.width - 1)
    def clampY(v: Int): Int = clamp(v, 0, src.height - 1)

    if (radius == 0)
      src(x, y)
    else {
      var xi = clampX(x - radius)
      var r, g, b, a, pixels = 0

      for {
        xi <- clampX(x - radius) to clampX(x + radius)
        yi <- clampY(y - radius) to clampY(y + radius)
      } yield {
        val pixel = src(xi, yi)
        r += red(pixel)
        g += green(pixel)
        b += blue(pixel)
        a += alpha(pixel)
        pixels += 1
      }
      rgba(r / pixels, g / pixels, b / pixels, a / pixels)
    }
  }

}
