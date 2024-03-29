package de.htwg.lovecraftletter
package model
package DrawPileImpl

import scala.util.Random

import scala.util.{Try, Success, Failure}

case class DrawPile() extends DrawPileInterface {

  override def newPile: List[Int] = {
    Random.shuffle(
      List(1, 1, 1, 1, 1, 2, 2, 3, 3, 4, 4, 5, 5, 6, 7, 8, 9, 10, 11, 12, 13,
        14, 15, 16)
    )
  }

  override def drawAndGet(drawPile: List[Int]): (List[Int], Int) = {
    val headProblem = Try(drawPile.head)
    val head = headProblem match {
      case Success(v) => v
      case Failure(e) =>
        //todo start new round
        println("Nachziehstapel ist leer")
        -1
    }
    val tailProblem = Try(drawPile.tail)
    val tail = tailProblem match {
      case Success(v) => v
      case Failure(e) => Nil
    }
    (tail, head)
  }

  override def startingHands(
                              drawPile: List[Int],
                              playerAmount: Int
                            ): (List[Int], List[Int]) = {
    rekStartingHands(drawPile, playerAmount, Nil)
  }

  override def rekStartingHands(
                                 drawPile: List[Int],
                                 playerAmount: Int,
                                 returnList: List[Int]
                               ): (List[Int], List[Int]) = {
    val (newDrawPile, tempInt) = drawAndGet(drawPile)
    val tempList = tempInt :: returnList
    if (playerAmount != tempList.length) {
      rekStartingHands(newDrawPile, playerAmount, tempList)
    } else {
      (newDrawPile, tempList)
    }
  }
}
