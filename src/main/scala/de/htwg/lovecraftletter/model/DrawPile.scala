package de.htwg.lovecraftletter
package model

import scala.util.Random

import scala.util.{Try, Success, Failure}

case class DrawPile() {

  def newPile: List[Int] = {
    Random.shuffle(
      //List(1, 1, 1, 1, 1, 2, 2, 3, 3, 4, 4, 5, 5, 6, 7, 8, 9, 10, 11, 12, 13,
       // 14, 15, 16)
       List(7,7,7,7)
    )
  }

  def drawAndGet(drawPile: List[Int]): (List[Int], Int) = {
    val headProblem = Try(drawPile.head)
    val head = headProblem match {
        case Success(v) => v
        case Failure(e) =>
            //todo start new round
            println("Nachziehstapel ist leer")
            0
    }
    val tailProblem = Try(drawPile.tail)
    val tail = tailProblem match {
        case Success(v) => v
        case Failure(e) => Nil
    }
    (tail, head)
  }

  def startingHands(
      drawPile: List[Int],
      playerAmount: Int
  ): (List[Int], List[Int]) = {
    rekStartingHands(drawPile, playerAmount, Nil)
  }

  def rekStartingHands(
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

/* class PackT[T](val bottles:List[T]) {
 def map(f:T => T) = bottles.map(bottle => f(bottle))
 override def toString="UUUU"
} */

// Option already exists. This is a sketch of an implementation.
/* trait Option[DrawPile] {
def map(f:DrawPile => DrawPile):Option[DrawPile]
}
case class Some[DrawPile](val b:DrawPile) extends Option[DrawPile] {
def map(f:DrawPile => DrawPile) = new Some(f(b))
}
case class None[DrawPile]() extends Option[DrawPile] {
def map(f:DrawPile => DrawPile) = new None
} */

