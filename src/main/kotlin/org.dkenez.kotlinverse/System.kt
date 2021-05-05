// import statements
package org.dkenez.kotlinverse

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.math.Vector2
import ktx.math.*
import kotlin.math.pow

/*
 * Class describing a system of planets interacting with each other
 * Planets in a system can attract each other and even collide
 * this simulation is only a rough estimation of real world interactions between celestial bodies
 * do not use for real world applications, the author doesn't accept responsibility for lost, or crashed spaceships
 */
class System {
    // gravitational coefficient, usually denoted by gamma
    private val gravCoefficient = 1000
    // initialize list of planets
    var planets = mutableListOf<Planet>()

    // add planet to list
    fun addPlanets(vararg planets: Planet) {
        planets.forEach { planet -> this.planets.add(planet) }
    }

    // remove planet from list
    private fun removePlanets(vararg planets: Planet) {
        planets.forEach { planet -> this.planets.remove(planet) }
    }

    // handle collision of two planets
    private fun collide(firstPlanet: Planet, secondPlanet: Planet): Planet {
        // conservation of mass
        val newMass = firstPlanet.mass + secondPlanet.mass
        // conservation of inertia
        val newVel = ((firstPlanet.vel * firstPlanet.mass) + (secondPlanet.vel * secondPlanet.mass)) / newMass
        // new position is the weighted average of the position of the two planets
        val newPos = ((firstPlanet.pos * firstPlanet.mass) + (secondPlanet.pos * secondPlanet.mass)) / newMass
        // join planet names ¯\_(ツ)_/¯
        val newName = firstPlanet.name + secondPlanet.name

        // new color is the weighted average of the colors
        val newR = ((firstPlanet.color.r * firstPlanet.mass) + (secondPlanet.color.r * secondPlanet.mass)) / newMass
        val newG = ((firstPlanet.color.g * firstPlanet.mass) + (secondPlanet.color.g * secondPlanet.mass)) / newMass
        val newB = ((firstPlanet.color.b * firstPlanet.mass) + (secondPlanet.color.b * secondPlanet.mass)) / newMass

        val newColor = Color(newR, newG, newB, 255F)

        return Planet(newMass, newPos, newVel, newName, newColor)
    }

    // a default system
    fun setupDefaultSystem() {
        val defaultPlanets = arrayOf(
            Planet(2500F, name = "Sun", color = Color.YELLOW),
            Planet(100F, Vector2(0F, -300F), Vector2(89F, 0F), "Uranus", Color.FOREST),
            Planet(100F, Vector2(0F, 300F), Vector2(-89F, 0F), "Venus", Color.BROWN),
            Planet(1F, Vector2(0F, -200F), Vector2(100F, 0F), "Mercury", Color.GRAY),
            Planet(1F, Vector2(-200F, 0F), Vector2(0F, -100F), "Mars", Color.RED),
            Planet(2F, Vector2(200F, 0F), Vector2(0F, 100F), "Jupiter", Color.BROWN),
            Planet(10F, Vector2(0F, 200F), Vector2(-100F, 0F), "Earth", Color.BLUE)
        )

        this.addPlanets(*defaultPlanets)
    }

    fun simulate(deltaT: Float) {
        for (firstPlanet in this.planets) { // for every pair of planets:
            for (secondPlanet in this.planets) {
                if (firstPlanet === secondPlanet) {
                    continue
                }
                // calculate displacement vector and distance
                val displacement = secondPlanet.pos - firstPlanet.pos
                val distance = displacement.len()

                // calculate gravitation attraction (Newton's law of universal gravitation)
                val gravForceMagnitude = gravCoefficient * (firstPlanet.mass * secondPlanet.mass) / distance.pow(2)
                val gravForceDir = (displacement / distance)
                val gravForce = gravForceDir * gravForceMagnitude

                // apply force on first planet
                // room for improvement, gravForce is calculated twice for each pair, once for each planet,
                // only the direction changes
                firstPlanet.applyForce(gravForce)
            }
        }

        // after calculating all interactions, update the planets
        this.planets.forEach { it.update(deltaT) }

        // save a copy of the planets in a list
        val planetList = this.planets.toList()

        for (firstPlanet in planetList) {
            if (firstPlanet !in this.planets) { continue } // check whether this planet is still a part of the system
            for (secondPlanet in planetList) {
                if (firstPlanet === secondPlanet) {
                    continue
                }
                val displacement = secondPlanet.pos - firstPlanet.pos
                val distance = displacement.len()

                if (distance < firstPlanet.radius + secondPlanet.radius) {
                    // if the distance between two planets is less then their combined radius, then these planets have collided
                    val newPlanet = collide(firstPlanet, secondPlanet)

                    // add new, merged planet to list
                    // remove collided planets
                    this.addPlanets(newPlanet)
                    this.removePlanets(firstPlanet, secondPlanet)
                }
            }
        }
    }
}