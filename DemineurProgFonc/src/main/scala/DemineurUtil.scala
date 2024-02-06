object DemineurUtil {

  type Plateau = Vector[Vector[Int]]
  type EtatAffichage = Vector[Vector[Boolean]]

  def devoilerCase(plateau: Plateau, coordonnees: (Int, Int), etatAffichage: EtatAffichage): (Plateau, EtatAffichage) = {
    val (x, y) = coordonnees

    if (!etatAffichage(x)(y)) {
      if (plateau(x)(y) == 9) {
        (plateau, Vector.fill(etatAffichage.length, etatAffichage(0).length)(true))
      } else {
        val nouvelEtatAffichage = devoilerCaseRecursif(plateau, (x, y), etatAffichage)
        (plateau, nouvelEtatAffichage)
      }
    } else {
      (plateau, etatAffichage)
    }
  }

  private def devoilerCaseRecursif(plateau: DemineurUtil.Plateau, coordonnees: (Int, Int), etatAffichage: DemineurUtil.EtatAffichage): DemineurUtil.EtatAffichage = {
    val (x, y) = coordonnees

    if (!etatAffichage(x)(y)) {
      val nouvelEtatAffichage = etatAffichage.updated(x, etatAffichage(x).updated(y, true))

      if (plateau(x)(y) == 0) {
        val coordsAdjacentes = for {
          i <- -1 to 1
          j <- -1 to 1
          if x + i >= 0 && x + i < plateau.length && y + j >= 0 && y + j < plateau(0).length
          if !nouvelEtatAffichage(x + i)(y + j) && (x + i != x || y + j != y)
        } yield (x + i, y + j)

        coordsAdjacentes.foldLeft(nouvelEtatAffichage) {
          case (etat, (i, j)) =>
            devoilerCaseRecursif(plateau, (i, j), etat)
        }
      } else {
        nouvelEtatAffichage
      }
    } else {
      etatAffichage
    }
  }

  def creerPlateau(dimensions: (Int, Int), nombreDeMines: Int): (Plateau, EtatAffichage) = {
    val (lignes, colonnes) = dimensions
    val plateau = Vector.fill(lignes, colonnes)(0)
    val etatAffichage = Vector.fill(lignes, colonnes)(false)

    val positionsMines = scala.util.Random.shuffle(for {
      i <- 0 until lignes
      j <- 0 until colonnes
    } yield (i, j)).take(nombreDeMines)

    val plateauAvecMines = positionsMines.foldLeft(plateau) { case (p, (i, j)) =>
      p.updated(i, p(i).updated(j, 9)).zipWithIndex.map { case (ligne, x) =>
        ligne.zipWithIndex.map { case (cellule, y) =>
          if (i + 1 >= x && i - 1 <= x && j + 1 >= y && j - 1 <= y && cellule != 9) cellule + 1
          else cellule
        }
      }
    }

    (plateauAvecMines, etatAffichage)
  }

  def isVictory(plateau: (DemineurUtil.Plateau, DemineurUtil.EtatAffichage)): Boolean = {
    val (plateauValeurs, plateauDevoile) = plateau

    plateauValeurs.indices.forall { i =>
      plateauValeurs(i).indices.forall { j =>
        (plateauDevoile(i)(j) && plateauValeurs(i)(j) != 9) || (!plateauDevoile(i)(j) && plateauValeurs(i)(j) == 9)
      }
    }
  }

  def isDefeat(plateau: EtatAffichage): Boolean = plateau.flatten.forall(identity)
}