// import statements
package org.dkenez.kotlinverse

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.math.Vector2
import ktx.math.*

/*
 * The Slingshot class is used to place new planets into the Kotlinverse.
 *
 * This class is a state machine, the current state is a SlingshotState instance.
 * The user can click on the canvas and sling new planets.
 */
class Slingshot {
    // the planet to be create is a Planet object
    // this doesn't interact with other planets, only after launching
    var slingPlanet = Planet(10F, Vector2(0F, 0F), Vector2(0F, 0F), "Nu Earth", Color.RED)

    var state: SlingshotState = SlingshotState.READY

    // change this to modify the relationship between the Slingshot pull, and the speed of the created Planet
    private val velCoefficient = 2F

    // listOf Colors the user can cycle between
    private val colorList = listOf(Color.FOREST, Color.BLUE, Color.GRAY, Color.BROWN, Color.ORANGE, Color.FIREBRICK)
    private var colorIndex = 0

    // cycle to the next Color
    fun nextColor() {
        if (colorIndex < colorList.size - 1) {
            colorIndex++

        } else {
            colorIndex = 0
        }
        setColor(colorList[colorIndex])
    }

    // cycle to the previous Color
    fun prevColor() {
        if (colorIndex > 0) {
            colorIndex--

        } else {
            colorIndex = colorList.size - 1
        }
        setColor(colorList[colorIndex])
    }

    // apply Color to Planet
    fun setColor(color: Color) {
        slingPlanet.color = color
    }

    // set mass of planet
    fun setMass(mass: Float) {
        slingPlanet.mass = mass
    }

    // increase mass of planet based on current mass
    // changing mass this way makes the process less cumbersome
    fun increaseMass() {
        val currMass = slingPlanet.mass
        val newMass = when {
            currMass < 100 -> {
                currMass + 10
            }
            currMass < 200 -> {
                currMass + 20
            }
            currMass < 500 -> {
                currMass + 50
            }
            currMass < 1000 -> {
                currMass + 100
            }
            else -> {
                currMass + 200
            }
        }

        setMass(newMass)
    }

    // decrease mass of planet based on current mass
    // changing mass this way makes the process less cumbersome
    fun decreaseMass() {
        val currMass = this.slingPlanet.mass
        val newMass = when {
            (9F < currMass && currMass < 11F) -> {
                10F
            }
            currMass < 101 -> {
                currMass - 10
            }
            currMass < 201 -> {
                currMass - 20
            }
            currMass < 501 -> {
                currMass - 50
            }
            currMass < 1001 -> {
                currMass - 100
            }
            else -> {
                currMass - 200
            }
        }

        setMass(newMass)
    }

    // set position of planet
    // the position of the mouseclick
    fun setPos(pos: Vector2) {
        slingPlanet.pos = pos
    }

    // set velocity of planet
    // proportional to how much the slingshot is pulled back
    fun setVel(vel: Vector2) {
        slingPlanet.vel = (this.slingPlanet.pos * 2) - vel
    }

    // add planet to kotlinverse
    //
    fun createPlanet(): Planet {
        slingPlanet.vel.sub(slingPlanet.pos)

        slingPlanet.vel = slingPlanet.vel * this.velCoefficient

        return slingPlanet
    }
}