package barneshut

import java.awt._
import java.awt.event._
import javax.swing._
import javax.swing.event._
import scala.collection.parallel._
import scala.collection.par._
import scala.collection.mutable.ArrayBuffer
import scala.reflect.ClassTag

/**
  * We assume that the force between particles is the gravitational force from
  * classical mechanics. The formula for the gravitational force between two
  * stellar bodies is:
  *
  *   F = G * (m1 * m2) / distance^2
  *
  * where        F: the absolute value of the gravitational force
  *              G: The gravitational constant
  *             mX: The mass of stellar body X
  *       distance: the distance between the two stellar bodies
  *
  * For each particle, the net force is computed by summing the components of
  * the individual forces from all other particles.
  *
  * The direct sum N-body algorithm is very simple, but also inefficient because
  * we need to update N particles, and compute N - 1 force contributions for each
  * of those particles resulting to an overall complexity of O(N^2) for each
  * each iteration.
  *
  * The Barnes-Hut algorithm is an optimization of the direct sum N-body
  * algorithm, and is based on the following observation:
  *
  * If a cluster of bodies is sufficiently distant from a body A, the net force
  * on A from that cluster can be approximated with one big body with the mass
  * of all the bodies in the cluster, positioned at the center of mass of the
  * cluster.
  *
  * The Barnes-Hut algorithm relies on a quadtree -- a data structure that
  * divides the space into cells, and answers queries such as 'What is the total
  * mass and the center of mass of all the particles in this cell?'
  *
  *                ---
  *               | / |
  *                ---
  *                 |
  *    ---------------------------
  * nw |     ne |        | sw     | se
  *    |        |        |        |
  *   ---      ---      ---      ---
  *  | A |    | F |    | - |    | / | mass = m_D + m_E + m_B + m_C
  *   ---      ---      ---      ---
  *                               |
  *                 ----------------------------
  *              nw |     ne |        | sw     | se
  *                 |        |        |        |
  *                ---      ---      ---      ---
  *               | D |    | E |    | B |    | C |
  *                ---      ---      ---      ---
  *
  * Above, the total force from the bodies B, C, D and E on the body A can be
  * approximated by a single body with mass equal to the sum of masses B, C,
  * D and E, positioned at the center of mass of bodies B, C, D and E. The center
  * of mass (massX, massY) is computed as follows:
  *
  *   mass = m_B + m_C + m_D + m_E
  *   massX = (m_B * x_B + m_C * x_C + m_D * x_D + m_E * x_E) / mass
  *   massY = (m_B * y_B + m_C * y_C + m_D * y_D + m_E * y_E) / mass
  *
  * <ol> An iteration of the Barnes-Hut algorithm is composed of the following
  * steps:
  * <li>Construct the quadtree for the current arrangement of the bodies.</li>
  * <li>Determine the boundaries, i.e. the square into which all bodies fit.</li>
  * <li>
  *   Construct a quadtree that covers the boundaries and contains all the
  *   bodies.
  * </li>
  * <li>Update the bodies -- for each body:
  *   <ol>
  *     <li>Update the body position according to its current velocity.</li>
  *     <li>
  *       Using the quadtree, compute the net force on the body by adding the
  *       individual forces from all the other bodies.
  *     </li>
  *     <li>Update the velocity according to the net force on that body.</li>
  *   </ol>
  * </li>
  * </ol>
  *
  * It turns out that, for most spatial distribution of bodies, the expected
  * number of cells that contribute to the net force on a body is log n, so the
  * overall complexity of the Barnes-Hut algorithm is O(n log n).
  */
object BarnesHut {

  val model = new SimulationModel

  var simulator: Simulator = _

  def initialize(parallelismLevel: Int, pattern: String, nbodies: Int) {
    model.initialize(parallelismLevel, pattern, nbodies)
    model.timeStats.clear()
    simulator = new Simulator(model.taskSupport, model.timeStats)
  }

  class BarnesHutFrame extends JFrame("Barnes-Hut") {
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE)
    setSize(1024, 600)
    setLayout(new BorderLayout)

    val rightpanel = new JPanel
    rightpanel.setBorder(BorderFactory.createEtchedBorder(border.EtchedBorder.LOWERED))
    rightpanel.setLayout(new BorderLayout)
    add(rightpanel, BorderLayout.EAST)
    
    val controls = new JPanel
    controls.setLayout(new GridLayout(0, 2))
    rightpanel.add(controls, BorderLayout.NORTH)
    
    val parallelismLabel = new JLabel("Parallelism")
    controls.add(parallelismLabel)
    
    val items = (1 to Runtime.getRuntime.availableProcessors).map(_.toString).toArray
    val parcombo = new JComboBox[String](items)
    parcombo.setSelectedIndex(items.length - 1)
    parcombo.addActionListener(new ActionListener {
      def actionPerformed(e: ActionEvent) = {
        initialize(getParallelism, "two-galaxies", getTotalBodies)
        canvas.repaint()
      }
    })
    controls.add(parcombo)
    
    val bodiesLabel = new JLabel("Total bodies")
    controls.add(bodiesLabel)
    
    val bodiesSpinner = new JSpinner(new SpinnerNumberModel(25000, 32, 1000000, 1000))
    bodiesSpinner.addChangeListener(new ChangeListener {
      def stateChanged(e: ChangeEvent) = {
        if (frame != null) {
          initialize(getParallelism, "two-galaxies", getTotalBodies)
          canvas.repaint()
        }
      }
    })
    controls.add(bodiesSpinner)
    
    val stepbutton = new JButton("Step")
    stepbutton.addActionListener(new ActionListener {
      def actionPerformed(e: ActionEvent) {
        stepThroughSimulation()
      }
    })
    controls.add(stepbutton)
    
    val startButton = new JToggleButton("Start/Pause")
    val startTimer = new javax.swing.Timer(0, new ActionListener {
      def actionPerformed(e: ActionEvent) {
        stepThroughSimulation()
      }
    })
    startButton.addActionListener(new ActionListener {
      def actionPerformed(e: ActionEvent) {
        if (startButton.isSelected) startTimer.start()
        else startTimer.stop()
      }
    })
    controls.add(startButton)
    
    val quadcheckbox = new JToggleButton("Show quad")
    quadcheckbox.addActionListener(new ActionListener {
      def actionPerformed(e: ActionEvent) {
        model.shouldRenderQuad = quadcheckbox.isSelected
        repaint()
      }
    })
    controls.add(quadcheckbox)

    val clearButton = new JButton("Restart")
    clearButton.addActionListener(new ActionListener {
      def actionPerformed(e: ActionEvent) {
        initialize(getParallelism, "two-galaxies", getTotalBodies)
      }
    })
    controls.add(clearButton)

    val info = new JTextArea("   ")
    info.setBorder(BorderFactory.createLoweredBevelBorder)
    rightpanel.add(info, BorderLayout.SOUTH)

    val canvas = new SimulationCanvas(model)
    add(canvas, BorderLayout.CENTER)
    setVisible(true)

    def updateInformationBox() {
      val text = model.timeStats.toString
      frame.info.setText("--- Statistics: ---\n" + text)
    }

    def stepThroughSimulation() {
      SwingUtilities.invokeLater(new Runnable {
        def run() = {
          val (bodies, quad) = simulator.step(model.bodies)
          model.bodies = bodies
          model.quad = quad
          updateInformationBox()
          repaint()
        }
      })
    }

    def getParallelism = {
      val selidx = parcombo.getSelectedIndex
      parcombo.getItemAt(selidx).toInt
    }

    def getTotalBodies = bodiesSpinner.getValue.asInstanceOf[Int]

    initialize(getParallelism, "two-galaxies", getTotalBodies)
  }

  try {
    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName())
  } catch {
    case _: Exception => println("Cannot set look and feel, using the default one.")
  }

  val frame = new BarnesHutFrame

  def main(args: Array[String]) {
    frame.repaint()
  }

}
