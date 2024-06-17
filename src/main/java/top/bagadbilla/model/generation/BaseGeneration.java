package top.bagadbilla.model.generation;

public abstract class BaseGeneration {
    protected final int width;
    protected final int height;

    protected BaseGeneration(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public abstract void generate();
}
