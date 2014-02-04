package org.grgodgames.lib;

import org.grgodgames.lib.game.GameHelper;

import org.lwjgl.BufferUtils;

import java.nio.ByteBuffer;
import java.nio.DoubleBuffer;
import java.nio.ShortBuffer;
import java.util.logging.Logger;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL30.*;

/**
 * The type Render.
 *
 * @author Greg Brown (GrGod123)
 * @version 1.0.0
 * @since CORE
 */
public abstract class Render implements IdObject {
	private static final GameLogger LOGGER      = GameLogger.loggerOf(Logger.getLogger(Render.class.getName()));
	private static final int        NORMAL_SIZE = 3;
	private final int vaoHandler;
	private final int vboVertexHandler;
	private final int vboColorHandler;
	private final int vboTexCoordHandler;
	private final int vboNormalHandler;
	private boolean different = true;
	private byte[][]     colors;
	private double[][]   normals;
	private short[][]    texCoords;
	private double[][]   vertexes;
	private int          vertexCount;
	private ByteBuffer   colorBuffer;
	private DoubleBuffer normalBuffer;
	private ShortBuffer  texCoordBuffer;
	private DoubleBuffer vertexBuffer;
	private boolean initVAO = true;

	/** Instantiates a new Render. */
	protected Render() {
		vaoHandler = glGenVertexArrays();

		vboVertexHandler = glGenBuffers();
		vboColorHandler = glGenBuffers();
		vboTexCoordHandler = glGenBuffers();
		vboNormalHandler = glGenBuffers();

		addToHandler();
		GameHelper.checkForError(LOGGER, Render.class);
	}

	/**
	 * New double buffer.
	 *
	 * @param length
	 *   the length
	 *
	 * @return the double buffer
	 */
	private static DoubleBuffer newDoubleBuffer(int length) {
		if(LOGGER.isDebugEnabled()) {
		LOGGER.logDebug("Creating new double buffer");
		}
		DoubleBuffer buffer = BufferUtils.createDoubleBuffer(length);
		buffer.flip();
		return buffer;
	}

	private static ByteBuffer newByteBuffer(int length) {
		if(LOGGER.isDebugEnabled()) {
		LOGGER.logDebug("Creating new byte buffer");
		}
		ByteBuffer buffer = BufferUtils.createByteBuffer(length);
		buffer.flip();
		return buffer;
	}

	private static ShortBuffer newShortBuffer(int length) {
		if(LOGGER.isDebugEnabled()) {
			LOGGER.logDebug("Creating new short buffer");
		}
		ShortBuffer buffer = BufferUtils.createShortBuffer(length);
		buffer.flip();
		return buffer;
	}

	private static void copyToDoubleBuffer(double[][] doubles, DoubleBuffer buffer) {
		buffer.flip();
		buffer.clear();
		for(double[] aDouble : doubles) {
			buffer.put(aDouble);
		}
		buffer.flip();
	}

	private static void copyToShortBuffer(short[][] shorts, ShortBuffer buffer) {
		buffer.flip();
		buffer.clear();
		for(short[] aShort : shorts) {
			buffer.put(aShort);
		}
		buffer.flip();
	}

	private static void copyToByteBuffer(byte[][] bytes, ByteBuffer buffer) {
		buffer.flip();
		buffer.clear();
		for(byte[] aByte : bytes) {
			buffer.put(aByte);
		}
		buffer.flip();
	}

	private static void unbindBuffers() {
		glBindVertexArray(0);
	}

	@Override
	public final void init() {
		reset();
		initVAO();
		initVBO();
		doInit();
	}

	@Override
	public final void destroy() {
		glDeleteBuffers(vboVertexHandler);
		glDeleteBuffers(vboColorHandler);
		glDeleteBuffers(vboNormalHandler);
		glDeleteBuffers(vboTexCoordHandler);

		glDeleteVertexArrays(vaoHandler);
	}

	@Override
	public final void update(double delta) {
		doUpdate(delta);
		bindBuffers();
		draw();
		unbindBuffers();
	}

	@Override
	public final int hashCode() {
		return vboVertexHandler;
	}

	@Override
	public final boolean equals(Object o) {
		if(this == o) {
			return true;
		}
		if(!(o instanceof Render)) {
			return false;
		}

		Render render = (Render) o;

		return vboVertexHandler == render.vboVertexHandler;
	}

	@Override
	public final String toString() {
		StringBuilder sb = new StringBuilder("Render{");
		sb.append("vertex count=").append(vertexCount);
		sb.append(", vbo handler=").append(vboVertexHandler);
		sb.append('}');
		return sb.toString();
	}

	/** Reset void. */
	protected final void reset() {
		different = true;

		vertexCount = getVertexCount();

		vertexBuffer = newDoubleBuffer(getVertexCount() * getVertexSize());
		colorBuffer = newByteBuffer(getVertexCount() * getColorSize());
		normalBuffer = newDoubleBuffer(getVertexCount() * NORMAL_SIZE);
		texCoordBuffer = newShortBuffer(getVertexCount() * getTexCoordSize());

		vertexes = new double[vertexCount][getVertexSize()];
		colors = new byte[vertexCount][getColorSize()];
		normals = new double[vertexCount][NORMAL_SIZE];
		texCoords = new short[vertexCount][getTexCoordSize()];

		setVertexes(newVertexes());
		setColors(newColors());
		setNormals(newNormals());
		setTexCoords(newTexCoords());
	}

	/**
	 * Sets vertexes.
	 *
	 * @param vertexes
	 *   the vertexes
	 */
	final void setVertexes(double[][] vertexes) {
		if(vertexes.length != this.vertexes.length) {
			throw new IllegalArgumentException("The number of vertexes is incorrect");
		}
		if(vertexes.length > 0) {
			if(vertexes[0].length != this.vertexes[0].length) {
				throw new IllegalArgumentException("The vertex size is incorrect");
			}
		}
		for(int i = 0; i < this.vertexes.length; i++) {
			System.arraycopy(vertexes[i], 0, this.vertexes[i], 0, this.vertexes[i].length);
		}
		different = true;
	}

	/**
	 * Sets colors.
	 *
	 * @param colors
	 *   the colors
	 */
	final void setColors(byte[][] colors) {
		if(colors.length != this.colors.length) {
			throw new IllegalArgumentException("The number of colors is incorrect");
		}
		if(colors.length > 0) {
			if(colors[0].length != this.colors[0].length) {
				throw new IllegalArgumentException("The color size is incorrect");
			}
		}
		for(int i = 0; i < this.colors.length; i++) {
			System.arraycopy(colors[i], 0, this.colors[i], 0, this.colors[i].length);
		}
		different = true;
	}

	/**
	 * Sets normals.
	 *
	 * @param normals
	 *   the normals
	 */
	final void setNormals(double[][] normals) {
		if(normals.length != this.normals.length) {
			throw new IllegalArgumentException("The number of normals is incorrect");
		}
		if(normals.length > 0) {
			if(normals[0].length != this.normals[0].length) {
				throw new IllegalArgumentException("The normal size is incorrect");
			}
		}
		for(int i = 0; i < this.normals.length; i++) {
			System.arraycopy(normals[i], 0, this.normals[i], 0, this.normals[i].length);
		}
		different = true;
	}

	/**
	 * Sets tex coords.
	 *
	 * @param texCoords
	 *   the texCoords
	 */
	final void setTexCoords(short[][] texCoords) {
		if(texCoords.length != this.texCoords.length) {
			throw new IllegalArgumentException("The number of texture coordinates is incorrect");
		}
		if(texCoords.length > 0) {
			if(texCoords[0].length != this.texCoords[0].length) {
				throw new IllegalArgumentException("The texture coordinate size is incorrect");
			}
		}
		for(int i = 0; i < this.texCoords.length; i++) {
			System.arraycopy(texCoords[i], 0, this.texCoords[i], 0, this.texCoords[i].length);
		}
		different = true;
	}

	/**
	 * Gets handler.
	 *
	 * @return the handler
	 */
	protected abstract Handler<Render> getHandler();

	/** Post draw. */
	protected abstract void postDraw();

	/** Pre draw. */
	protected abstract void preDraw();

	/**
	 * Do update.
	 *
	 * @param delta
	 *   the delta
	 */
	protected abstract void doUpdate(double delta);

	/**
	 * New vertexes.
	 *
	 * @return the double [vertexes] [values]
	 */
	protected abstract double[][] newVertexes();

	/**
	 * New colors.
	 *
	 * @return the double [colors] [values]
	 */
	protected abstract byte[][] newColors();

	/**
	 * New normals.
	 *
	 * @return the double [normals] [values]
	 */
	protected abstract double[][] newNormals();

	/**
	 * New tex coords.
	 *
	 * @return the double [tex coords] [values]
	 */
	protected abstract short[][] newTexCoords();

	/**
	 * Gets vertex count.
	 *
	 * @return the vertex count
	 */
	protected abstract int getVertexCount();

	/**
	 * Gets vertex size.
	 *
	 * @return the vertex size
	 */
	protected abstract int getVertexSize();

	/**
	 * Gets color size.
	 *
	 * @return the color size
	 */
	protected abstract int getColorSize();

	/**
	 * Gets tex coord size.
	 *
	 * @return the tex coord size
	 */
	protected abstract int getTexCoordSize();

	/** Do init. */
	protected abstract void doInit();

	/** Add to handler. */
	private void addToHandler() {
		getHandler().addObject(this);
	}

	private void bindBuffers() {
		if(different) {
			initVBO();
			different = false;
		}
		glBindVertexArray(vaoHandler);

		GameHelper.checkForError(LOGGER, Render.class, "bindBuffers");
	}

	/** Draw void. */
	private void draw() {
		preDraw();

		glEnableClientState(GL_VERTEX_ARRAY);
		glEnableClientState(GL_COLOR_ARRAY);
		glEnableClientState(GL_TEXTURE_COORD_ARRAY);
		glEnableClientState(GL_NORMAL_ARRAY);

		glDrawArrays(GL_TRIANGLES, 0, getVertexCount());

		glDisableClientState(GL_VERTEX_ARRAY);
		glDisableClientState(GL_COLOR_ARRAY);
		glDisableClientState(GL_NORMAL_ARRAY);
		glDisableClientState(GL_TEXTURE_COORD_ARRAY);

		glBindBuffer(GL_ARRAY_BUFFER, 0);

		postDraw();

		GameHelper.checkForError(LOGGER, Render.class, "draw");
	}

	private void initVAO() {
		if(initVAO){
		initVAO=false;
			if(LOGGER.isWarningEnabled()) {
				LOGGER.logWarning("Render: initVAO: STUB", null);
			}
		}
		//todo vao stuff, too tired at time
	}

	private void initVBO() {
		glBindVertexArray(vaoHandler);

		copyToDoubleBuffer(vertexes, vertexBuffer);
		copyToByteBuffer(colors, colorBuffer);
		copyToDoubleBuffer(normals, normalBuffer);
		copyToShortBuffer(texCoords, texCoordBuffer);

		glBindBuffer(GL_ARRAY_BUFFER, vboVertexHandler);
		glBufferData(GL_ARRAY_BUFFER, vertexBuffer, GL_STATIC_DRAW);
		glVertexPointer(getVertexSize(), GL_DOUBLE, 0, 0);

		GameHelper.checkForError(LOGGER, Render.class, "vertex");

		glBindBuffer(GL_ARRAY_BUFFER, vboNormalHandler);
		glBufferData(GL_ARRAY_BUFFER, normalBuffer, GL_STATIC_DRAW);
		glNormalPointer(GL_DOUBLE, 0, 0);

		GameHelper.checkForError(LOGGER, Render.class, "normal");

		glBindBuffer(GL_ARRAY_BUFFER, vboColorHandler);
		glBufferData(GL_ARRAY_BUFFER, colorBuffer, GL_STATIC_DRAW);
		glColorPointer(getColorSize(), GL_UNSIGNED_BYTE, 0, 0);

		GameHelper.checkForError(LOGGER, Render.class, "color");

		glBindBuffer(GL_ARRAY_BUFFER, vboTexCoordHandler);
		glBufferData(GL_ARRAY_BUFFER, texCoordBuffer, GL_STATIC_DRAW);
		glTexCoordPointer(getTexCoordSize(), GL_SHORT, 0, 0);

		GameHelper.checkForError(LOGGER, Render.class, "texCoord");

		GameHelper.checkForError(LOGGER, Render.class, "initVBO");
	}
}
