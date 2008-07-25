package com.bc.ceres.glayer.support;

import com.bc.ceres.glayer.Layer;
import com.bc.ceres.glevel.LevelImage;
import com.bc.ceres.glevel.LevelImageRenderer;
import com.bc.ceres.glevel.support.ConcurrentLevelImageRenderer;
import com.bc.ceres.glevel.support.DefaultLevelImageRenderer;
import com.bc.ceres.glevel.support.DefaultMultiLevelImage;
import com.bc.ceres.glevel.support.SingleLevelImage;
import com.bc.ceres.grender.Rendering;
import com.bc.ceres.grender.Viewport;
import com.bc.ceres.grender.support.DefaultViewport;

import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.RenderedImage;

/**
 * A multi-resolution capable image layer.
 *
 * @author Norman Fomferra
 */
public class ImageLayer extends Layer {

    private LevelImage levelImage;
    private LevelImageRenderer levelImageRenderer;
    private boolean concurrent;
    private boolean debug;

    /**
     * Constructs a single-resolution-level image layer.
     *
     * @param image the image
     */
    public ImageLayer(RenderedImage image) {
        this(image, new AffineTransform());
    }

    /**
     * Constructs a single-resolution-level image layer.
     *
     * @param image                 the image
     * @param imageToModelTransform the transformation from image to model CS
     */
    public ImageLayer(RenderedImage image, AffineTransform imageToModelTransform) {
        this(image, imageToModelTransform, 1);
    }

    /**
     * Constructs a multi-resolution-level image layer.
     *
     * @param image                 the image
     * @param imageToModelTransform the transformation from image to model CS
     * @param levelCount            the number of resolution levels
     */
    public ImageLayer(RenderedImage image, AffineTransform imageToModelTransform, int levelCount) {
        this(levelCount > 1 ? new DefaultMultiLevelImage(image, imageToModelTransform, levelCount) : new SingleLevelImage(image, imageToModelTransform));
    }

    /**
     * Constructs a multi-resolution-level image layer.
     *
     * @param levelImage the multi-resolution-level image
     */
    public ImageLayer(LevelImage levelImage) {
        this.levelImage = levelImage;
    }

    public int getLevelCount() {
        return levelImage.getLevelCount();
    }

    public RenderedImage getImage() {
        return getImage(0);
    }

    public AffineTransform getImageToModelTransform() {
        return getImageToModelTransform(0);
    }

    public AffineTransform getModelToImageTransform() {
        return getModelToImageTransform(0);
    }

    public RenderedImage getImage(int level) {
        return levelImage.getPlanarImage(level);
    }

    public AffineTransform getImageToModelTransform(int level) {
        return levelImage.getImageToModelTransform(level);
    }

    public AffineTransform getModelToImageTransform(int level) {
        return levelImage.getModelToImageTransform(level);
    }

    public boolean isConcurrent() {
        return concurrent;
    }

    public void setConcurrent(boolean concurrent) {
        this.concurrent = concurrent;
        if (concurrent && levelImageRenderer != null && !(levelImageRenderer instanceof ConcurrentLevelImageRenderer)) {
            levelImageRenderer.dispose();
            levelImageRenderer = null;
        }
    }

    public boolean isDebug() {
        return debug;
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    public Rectangle2D getBounds() {
        return levelImage.getBoundingBox(0);
    }

    @Override
    protected void renderLayer(Rendering rendering) {
        final Viewport vp = rendering.getViewport();
        final double i2mScale = DefaultViewport.getScale(getImageToModelTransform());
        final double m2vScale = 1.0 / vp.getZoomFactor();
        final double scale = m2vScale / i2mScale;

        final int currentLevel = levelImage.computeLevel(scale);
        if (levelImageRenderer == null) {
            levelImageRenderer = createImagePainter();
        }
        levelImageRenderer.renderImage(rendering, levelImage, currentLevel);
    }

    public void dispose() {
        if (levelImageRenderer != null) {
            levelImageRenderer.dispose();
            levelImageRenderer = null;
        }
        levelImage = null;
        super.dispose();
    }

    private LevelImageRenderer createImagePainter() {
        if (concurrent) {
            final ConcurrentLevelImageRenderer concurrentImagePainter = new ConcurrentLevelImageRenderer();
            concurrentImagePainter.setDebug(debug);
            return concurrentImagePainter;
        }
        return new DefaultLevelImageRenderer();
    }

}