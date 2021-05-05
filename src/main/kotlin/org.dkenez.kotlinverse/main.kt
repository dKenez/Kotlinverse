// import statements
package org.dkenez.kotlinverse

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.backends.lwjgl.LwjglApplication
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Vector2
import ktx.app.KtxApplicationAdapter
import ktx.app.clearScreen
import ktx.graphics.use

fun main() {
    // width and height of window
    val w = 1920
    val h = 1080

    // set config of application
    val config = LwjglApplicationConfiguration().apply {
        width = w
        height = h
    }

    // create application with window size and config parameters
    LwjglApplication(Kotlinverse(w, h), config)
}

// Kotlinverse class
class Kotlinverse(private val width: Int, private val height: Int) : KtxApplicationAdapter {
    // setup renderer
    private lateinit var renderer: ShapeRenderer

    // create system and slingshot
    private val mySys = System()
    private var mySling = Slingshot()

    // initialize default system
    init {
        mySys.setupDefaultSystem()
    }

    override fun create() {
        renderer = ShapeRenderer()
    }

    // control loop
    override fun render() {
        handleInput()
        logic()
        draw()
    }

    // handle user inputs
    private fun handleInput() {
        // when left mouse button is pressed
        if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
              if (mySling.state == SlingshotState.READY) {
                // if slingshot state is READY, set state to SET
                // position planet at cursor position
                mySling.state = SlingshotState.SET

                val posX = Gdx.input.x - (width/2)
                val posY = height - Gdx.input.y - (height/2)
                mySling.setPos(Vector2(posX.toFloat(), posY.toFloat()))
            }
        }

        // when right mouse button is pressed
        if (Gdx.input.isButtonJustPressed(Input.Buttons.RIGHT)) {
            if (mySling.state == SlingshotState.SET) {
                // if slingshot state is SET, set state to CLEAR
                // slingshot reset
                mySling.state = SlingshotState.CLEAR
            }
        }

        // when left mouse button is released
        if (!Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
            if (mySling.state == SlingshotState.SET) {
                // if slingshot state is SET, set state to READY
                // launch planet
                val newPlanet = mySling.createPlanet()
                mySys.addPlanets(newPlanet)

                mySling = Slingshot()
                mySling.setMass(newPlanet.mass)
                mySling.setColor(newPlanet.color)
                mySling.state = SlingshotState.READY
            } else if (mySling.state == SlingshotState.CLEAR) {
                // if slingshot state is CLEAR, set state to READY
                // slingshot reset
                mySling.state = SlingshotState.READY
            }
        }

        if (mySling.state == SlingshotState.SET) {
            // if slingshot state is SET, update launch velocity
            val velX = Gdx.input.x - (width/2)
            val velY = (height - Gdx.input.y) - (height/2)
            mySling.setVel(Vector2(velX.toFloat(), velY.toFloat()))
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.W)) {
            // if W is pressed increase mass
            mySling.increaseMass()
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.S)) {
            // if S is pressed decrease mass
            mySling.decreaseMass()
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.D)) {
            // if D is pressed cycle next color
            mySling.nextColor()
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.A)) {
            // if D is pressed cycle previous color
            mySling.prevColor()
        }
    }

    // apply logic for next frame
    private fun logic() {
        // simulate next frame of Kotlinverse
        mySys.simulate(0.01F)
    }

    // render next frame
    private fun draw() {
        clearScreen(0f, 0f, 0f, 0f)

        renderer.use(ShapeRenderer.ShapeType.Filled) {

            // render each planet as a colored circle at correct position, and diameter proportional to the square root of its mass
            this.mySys.planets.forEach {
                renderer.color = it.color
                renderer.circle(it.pos.x + (width/2), it.pos.y + (height/2), it.radius)
            }

            // when user is aiming the slingshot:
            // render a white line to show the initial velocity of the new planet,
            // render the prospective planet at mouseclick
            if (mySling.state == SlingshotState.SET) {
                renderer.color = Color.WHITE
                renderer.rectLine(Vector2(mySling.slingPlanet.pos.x + (width/2), mySling.slingPlanet.pos.y + (height/2)),
                    Vector2(mySling.slingPlanet.vel.x + (width/2), mySling.slingPlanet.vel.y + (height/2)), 2F)

                renderer.color = mySling.slingPlanet.color
                renderer.circle(mySling.slingPlanet.pos.x + (width/2), mySling.slingPlanet.pos.y + (height/2), mySling.slingPlanet.radius)
            }
        }
    }
}
