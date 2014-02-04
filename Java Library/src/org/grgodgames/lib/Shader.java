package org.grgodgames.lib;

import org.grgodgames.lib.game.GameHelper;

import java.io.File;
import java.text.MessageFormat;
import java.util.logging.Logger;

import static org.grgodgames.lib.IOHelper.readFile;
import static org.lwjgl.opengl.GL11.GL_TRUE;
import static org.lwjgl.opengl.GL20.*;

/**
 * The type Shader.
 *
 * @author Greg Brown (GrGod123)
 * @version 1.0.0
 * @since CORE
 */
public abstract class Shader implements IdObject {
	/** The constant LOGGER. */
	private static final GameLogger LOGGER         = GameLogger.loggerOf(Logger.getLogger(Shader.class.getName()));
	/** The IdObject. */
	private final        int        programID      = glCreateProgram();
	/** The Fragment shader. */
	private final        int        fragmentShader = glCreateShader(GL_FRAGMENT_SHADER);
	/** The VertexCoord shader. */
	private final        int        vertexShader   = glCreateShader(GL_VERTEX_SHADER);
	/** The VertexCoord source. */
	private final File vertexSource;
	/** The Fragment source. */
	private final File fragmentSource;
	/** The Fail. */
	private boolean fail = false;

	/**
	 * Instantiates a new Shader.
	 *
	 * @param vert
	 *   the vert
	 * @param frag
	 *   the frag
	 */
	protected Shader(File vert, File frag) {
		this.vertexSource = vert;
		this.fragmentSource = frag;
		addToHandler();
	}

	@Override
	public final void init() {
		StringBuilder vertexCode = readFile(vertexSource, "\n");
		StringBuilder fragmentCode = readFile(fragmentSource, "\n");

		glShaderSource(vertexShader, vertexCode);
		glShaderSource(fragmentShader, fragmentCode);

		glCompileShader(vertexShader);
		glCompileShader(fragmentShader);

		if(glGetShaderi(vertexShader, GL_COMPILE_STATUS) != GL_TRUE) {
			if(LOGGER.isWarningEnabled()) {
				LOGGER.logWarning(MessageFormat.format("Failed to compile shader: {0}", vertexSource), null);
			}
			fail = true;
		}
		if(glGetShaderi(fragmentShader, GL_COMPILE_STATUS) != GL_TRUE) {
			if(LOGGER.isWarningEnabled()) {
				LOGGER.logWarning(MessageFormat.format("Failed to compile shader: {0}", fragmentSource), null);
			}
			fail = true;
		}

		glAttachShader(programID, vertexShader);
		glAttachShader(programID, fragmentShader);

		glLinkProgram(programID);

		glValidateProgram(programID);

		GameHelper.checkForError(LOGGER, Shader.class);
	}

	@Override
	public final void destroy() {
		glDeleteShader(vertexShader);
		glDeleteShader(fragmentShader);
		glDeleteProgram(programID);
	}

	@Override
	public final String toString() {
		StringBuilder sb = new StringBuilder("{");
		sb.append("ID=").append(programID);
		sb.append('}');
		return sb.toString();
	}

	public final int getID() {
		return programID;
	}

	public final boolean hasFailed() {
		return fail;
	}

	/**
	 * Gets handler.
	 *
	 * @return the handler
	 */
	protected abstract Handler<Shader> getHandler();

	/** Add to handler. */
	private void addToHandler() {
		getHandler().addObject(this);
	}
}
