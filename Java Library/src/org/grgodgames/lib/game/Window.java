package org.grgodgames.lib.game;

import org.grgodgames.lib.GameLogger;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import java.util.logging.Logger;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.util.glu.GLU.gluPerspective;

/**
 * The type Window.
 *
 * @author Greg Brown (GrGod123)
 * @version 1.0.0
 * @since CORE
 */
public class Window {
	private static final GameLogger LOGGER = GameLogger.loggerOf(Logger.getLogger(Window.class.getName()));
	/** The Width. */
	private final int     width;
	/** The Height. */
	private final int     height;
	/** The Fullscreen. */
	private final boolean fullscreen;
	/** The Near z. */
	private final float   nearZ;
	/** The Far z. */
	private final float   farZ;
	/** The FOV. */
	private final float   fieldOfView;
	private final float   aspectRatio;

	/**
	 * Instantiates a new Window.
	 *
	 * @param dimensions    the dimensions
	 * @param fullscreen    the fullscreen
	 * @param zDistance the z distance
	 * @param fieldOfView    the field of view
	 */
	protected Window(int[] dimensions, boolean fullscreen, float[] zDistance, float fieldOfView) {
		this.width = dimensions[0];
		this.height = dimensions[1];
		this.fullscreen = fullscreen;
		this.nearZ = zDistance[0];
		this.farZ = zDistance[1];
		this.fieldOfView = fieldOfView;
		aspectRatio = width / (float) height;
	}

	/**
	 * Init open gL.
	 *
	 */
	final void initOpenGL() {
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		gluPerspective(fieldOfView, aspectRatio, nearZ, farZ);
		glMatrixMode(GL_MODELVIEW);

		GameHelper.checkForError(LOGGER, Window.class);
	}

	final void createWindow() {
		DisplayMode displayMode = new DisplayMode(width, height);

		try {
			if(fullscreen) {
				Display.setDisplayModeAndFullscreen(displayMode);
			} else {
				Display.setDisplayMode(displayMode);
			}
		} catch(LWJGLException e) {
			try {
				displayMode = Display.getDesktopDisplayMode();
				Display.setDisplayMode(displayMode);
			} catch(LWJGLException ignored) {
			}finally{
				if(LOGGER.isWarningEnabled()) {
					LOGGER.logWarning("Error Initialising Display", e);
				}
			}
		} finally {
			try {
				Display.create();
			} catch(LWJGLException e) {
				if(LOGGER.isErrorEnabled()) {
					LOGGER.logError("Error Creating Display", e);
				}
				GameHelper.destroy();
			}
		}
	}
}
