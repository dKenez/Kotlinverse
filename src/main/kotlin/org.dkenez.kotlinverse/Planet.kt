// import statements
package org.dkenez.kotlinverse

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.math.Vector2
import ktx.math.*
import kotlin.math.sqrt

/*
 * Class describing a planet
 * Planets are usually part of a System class
 * Planets have mass, postition, velocity, acceleration and color
 */
class Planet(mass: Float, var pos: Vector2 = Vector2(0F, 0F), var vel: Vector2 = Vector2(0F, 0F), val name: String, var color: Color = Color.BLUE) {
    // update radius when mass is changed
    var mass = mass
        set(value)  {
            field = value
            this.radius = sqrt(field) * this.radiusScaler
        }

    private val radiusScaler = 1
    var radius = sqrt(mass) * radiusScaler
    private var acc = Vector2(0F, 0F)
    private var prevAcc = Vector2(0F, 0F)

    // toString() for debugging
    override fun toString(): String {
        return name
    }

    // apply a force to the object, changing its acceleration, obeys the principle of superposition
    fun applyForce(force: Vector2) {
        this.acc.add(force / this.mass)
    }

    // numerically integrate to new position of the planet based on elapsed time
    fun update(deltaT: Float) {
        // define state variables
        // n 'p'-s indicate the n-th derivative
        // the numbers indicate how many time steps behind the state variable is (0 is current time, 1 is one time step behind...)
        val xpp2 = this.prevAcc
        val xpp1 = this.acc
        val xp1 = this.vel
        val x1 = this.pos

        // second-order Adams-Bashforth method of numerical integration for calculating current velocity and position
        val xp0 = xp1 + (((xpp1 * 3) - xpp2) / 2) * deltaT
        val x0 = x1 + ((xp0 + xp1) / 2) * deltaT

        // reset acceleration
        this.acc.setZero()
        // shift state variables
        this.prevAcc = xpp1
        this.vel = xp0
        this.pos = x0
    }
}