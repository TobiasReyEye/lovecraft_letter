package de.htwg.lovecraftletter
package aview

import controller.ControllerInterface
import controller.controllState
import util.Observer

import scala.swing._
import scala.swing.event._
import scala.swing.ListView._
import de.htwg.lovecraftletter.controller.controllState

import LovecraftLetterModule.{given}

class GUI(using controller: ControllerInterface) extends Frame with Observer {
    controller.add(this)

    val boardTA = TextArea(21, 82)
    boardTA.font =(new Font("Monospaced", 0,14))
    boardTA.editable = false

    val effectTA = TextArea(2, 82)
    effectTA.font =(new Font("Monospaced", 0,16))
    effectTA.editable = false

    var inputCO = ListView(Vector("1", "2", "1", "2", "1", "2", "1", "2"))
    //inputCO.size
    // listenTo(inputCO.selection)
    // reactions += {
    //   case SelectionChanged(inputCO) => println(inputCO.selection.items(0))
    // }

    title = "Lovecraft Letter"
    menuBar = new MenuBar{
        contents += new Menu("Optionen") {
            contents += new MenuItem(Action("Tschuess") {
                sys.exit(0)
            })
            contents += new MenuItem(Action("Rueckgaengig") {
                controller.undoStep
            })
            contents += new MenuItem(Action("Rueckgaengig Rueckgaengig machen") {
                controller.redoStep
            })
            contents += MenuItem(Action("mach mal") {
                handle
            })
        }
    }

    contents = new BorderPanel {
        //add(new Label("Willkommen bei Lovecraft Letter"), BorderPanel.Position.North)
        add(boardTA, BorderPanel.Position.North)
        add(effectTA, BorderPanel.Position.Center)
        add(inputCO, BorderPanel.Position.South)

    }

    pack()
    inputCO.listData = Vector()
    centerOnScreen()
    open()


    override def update = {
        show(controller.handle)
        controller.getVarControllerState(0) match
            case controllState.standard =>
            case controllState.getEffectedPlayer =>
                inputCO.listData = controller.getAllowedPlayerForPlayerSelection
            case controllState.getInvestigatorGuess =>
                inputCO.listData = Vector("0", "2", "3", "4", "5", "6", "7", "8")
            case controllState.getInputToPlayAnotherCard =>
                inputCO.listData = Vector("1", "2")
            case _ =>
    }

    def handle = {
        controller.getVarControllerState(0) match
                case controllState.standard =>
                    inputCO.selection.items(0) match
                        case "1" =>
                            controller.setVarUserInput(1)
                            controller.makeTurn
                        case "2" =>
                            controller.setVarUserInput(2)
                            controller.makeTurn
                case controllState.getEffectedPlayer =>
                    controller.playerChoosed(inputCO.selection.items(0).toInt)
                case controllState.getInvestigatorGuess =>
                    controller.investgatorGuessed(inputCO.selection.items(0).toInt)
                case controllState.getInputToPlayAnotherCard =>
                    controller.playAnotherCard2(inputCO.selection.items(0).toInt)
                //case controllState.playerWins =>
                case _ => controller.resetControllerState
        // if(controller.controllerState == (controllState.standard,"")) {
        //     inputCO.selection.items(0) match
        //         case "1" =>
        //             controller.userInput = 1
        //             controller.makeTurn
        //         case "2" =>
        //             controller.userInput = 2
        //             controller.makeTurn
        // }
        //println(inputCO.selection.items)
    }

    def show(output:String) = {
        if(controller.getVarControllerState == (controllState.standard,"")) {
            boardTA.text = output.dropRight(41)
            effectTA.text = "Welche Karte moechtest du spielen? (1|2)"
            inputCO.listData = Vector("1", "2")
        } else if (controller.getVarControllerState(0) == controllState.informOverPlayedEffect || controller.getVarControllerState(0) == controllState.playerWins || controller.getVarControllerState(0) == controllState.tellEliminatedPlayer) {
            Dialog.showMessage(this, controller.handle)
        } else {
            effectTA.text = output
        }
    }

}
