import scalafx.application.JFXApp3
import scalafx.application.JFXApp3.PrimaryStage
import scalafx.beans.property.StringProperty
import scalafx.scene.Scene
import scalafx.scene.control.{Button, Label, Spinner}
import scalafx.scene.layout.{GridPane, VBox}
import scalafx.scene.paint.Color

object DemineurInterface extends JFXApp3 {

  private var plateau: (DemineurUtil.Plateau, DemineurUtil.EtatAffichage) = DemineurUtil.creerPlateau((10, 10), 10)
  private var mines = 10
  private var rows = 10
  private var cols = 10
  private val gameStatusLabel = StringProperty("En cours")

  override def start(): Unit = {
    stage = new PrimaryStage {
      title = "Démineur"
      scene = new Scene {
        fill = Color.rgb(38, 38, 38)
        content = createVBox()
      }
    }
  }

  private def createVBox(): VBox = {
    val rowsSpinner = createNumericSpinner(15, rows)
    val colsSpinner = createNumericSpinner(20, cols)
    val minesSpinner = createNumericSpinner(80, mines)
    val gridPane = createButtonGrid(plateau._1, plateau._2)
    val restartButton = createRestartButton(rowsSpinner, colsSpinner, minesSpinner)
    val statusLabel = new Label {
      textFill = Color.White
      text <== gameStatusLabel
    }
    new VBox {
      spacing = 10
      children = Seq(
        createLabel("Lignes :"),
        rowsSpinner,
        createLabel("Colonnes :"),
        colsSpinner,
        createLabel("Mines :"),
        minesSpinner,
        restartButton,
        statusLabel,
        gridPane
      )
    }
  }

  private def createNumericSpinner(max: Int, initialValue: Int): Spinner[Int] = new Spinner(10, max, initialValue)

  private def createLabel(text: String): Label = {
    new Label(text) {
      textFill = Color.White
    }
  }

  private def createButtonGrid(plateau: DemineurUtil.Plateau, etatAffichage: DemineurUtil.EtatAffichage): GridPane = {
    val gridPane = new GridPane()

    for {
      i <- plateau.indices
      j <- plateau(i).indices
    } {
      val button = createButton((i, j), plateau, etatAffichage)
      gridPane.add(button, j, i)
    }

    gridPane
  }

  private def createButton(coord: (Int, Int), plateauParam: DemineurUtil.Plateau, etatAffichage: DemineurUtil.EtatAffichage): Button = {
    val etatDevoillee = etatAffichage(coord._1)(coord._2)

    new Button {
      prefWidth = 30
      prefHeight = 30
      if (etatDevoillee) {
        text = s"${plateauParam(coord._1)(coord._2)}"
        plateauParam(coord._1)(coord._2) match {
          case 0 => style = "-fx-base: #00FF00;"
          case 9 => style = "-fx-base: #FF0000;"
          case _ => style = "-fx-base: #FFFF00;"
        }
      } else {
        text = "?"
        style = "-fx-base: #FFFFFF;"
        onAction = _ => {
          plateau = DemineurUtil.devoilerCase(plateauParam, coord, etatAffichage)
          updateGameStatus()
        }
      }
    }
  }

  private def createRestartButton(rowsSpinner: Spinner[Int], colsSpinner: Spinner[Int], minesSpinner: Spinner[Int]): Button = {
    val restartButton = new Button("Nouvelle Partie")
    restartButton.onAction = _ => {
      mines = minesSpinner.value.value
      rows = rowsSpinner.value.value
      cols = colsSpinner.value.value
      plateau = DemineurUtil.creerPlateau((rows, cols), mines)
      gameStatusLabel.value = "En cours"
      stage.scene = new Scene {
        fill = Color.rgb(38, 38, 38)
        content = createVBox()
      }
    }
    restartButton
  }

  private def updateGameStatus(): Unit = {
    gameStatusLabel.value = if (DemineurUtil.isDefeat(plateau._2)) "Défaite..."
    else if (DemineurUtil.isVictory(plateau)) "Victoire !"
    else "En cours"
    stage.scene = new Scene {
      fill = Color.rgb(38, 38, 38)
      content = createVBox()
    }
  }
}
